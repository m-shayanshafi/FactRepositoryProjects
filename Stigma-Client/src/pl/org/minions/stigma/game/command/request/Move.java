/**
 *   Stigma - Multiplayer online RPG - http://stigma.sourceforge.net
 *   Copyright (C) 2005-2009 Minions Studio
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *   
 */
package pl.org.minions.stigma.game.command.request;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import pl.org.minions.stigma.game.actor.Actor;
import pl.org.minions.stigma.game.command.Command;
import pl.org.minions.stigma.game.command.CommandType;
import pl.org.minions.stigma.game.data.WorldData;
import pl.org.minions.stigma.game.data.actor.ActorChangedSafeMap;
import pl.org.minions.stigma.game.data.actor.ActorCooldownChanged;
import pl.org.minions.stigma.game.data.info.SegmentInfo;
import pl.org.minions.stigma.game.event.Event;
import pl.org.minions.stigma.game.event.actor.ActorChangedMap;
import pl.org.minions.stigma.game.event.actor.ActorWalk;
import pl.org.minions.stigma.game.map.EntryZone;
import pl.org.minions.stigma.game.map.ExitZone;
import pl.org.minions.stigma.game.map.MapInstance;
import pl.org.minions.stigma.game.map.MapInstance.Segment;
import pl.org.minions.stigma.game.world.ExtendedWorld;
import pl.org.minions.stigma.globals.Direction;
import pl.org.minions.stigma.globals.Position;
import pl.org.minions.stigma.globals.SizeOf;
import pl.org.minions.stigma.network.Buffer;

/**
 * Move command - request for move of actor in given
 * direction.
 */
public class Move extends Command
{
    private static final int SIZE = SizeOf.BYTE;
    private static final Random RANDOM = new Random();

    private Direction direction;

    private Move()
    {
        super(CommandType.MOVE);
    }

    /**
     * Creates request for move. Actor id will be determined
     * on server, direction is given.
     * @param dir
     *            direction in which actor requests move.
     */
    public Move(Direction dir)
    {
        this();
        this.direction = dir;
    }

    /**
     * Creates empty object (needed for decoding).
     * @return empty object
     */
    public static Move create()
    {
        return new Move();
    }

    /** {@inheritDoc} */
    @Override
    protected boolean commandSpecificDecode(Buffer buffer)
    {
        direction = buffer.decodeEnum(Direction.class);
        return true;
    }

    /** {@inheritDoc} */
    @Override
    protected boolean commandSpecificEncode(Buffer buffer)
    {
        buffer.encode(direction);
        return true;
    }

    /** {@inheritDoc} */
    @Override
    protected int commandSpecificLength()
    {
        return SIZE;
    }

    /**
     * Returns direction.
     * @return direction
     */
    public Direction getDirection()
    {
        return direction;
    }

    /** {@inheritDoc} */
    @Override
    protected boolean innerExecute(ExtendedWorld world,
                                   List<Event> output,
                                   List<WorldData> dataForSender,
                                   Set<Segment> affectedSegments)
    {
        Actor a = world.getActor(getRequesterId());
        if (a == null)
            return false;

        if (a.isOverloaded())
            return false;

        MapInstance m = world.getMap(a.getMapId(), a.getMapInstanceNo());
        if (m == null)
            return false;

        Position oldPosition = a.getPosition();
        Position newPosition = oldPosition.newPosition(direction);

        MapInstance.Segment s = m.getSegmentForPosition(oldPosition);
        affectedSegments.add(s);

        if (!m.isPassable(newPosition))
            return false;

        MapInstance.Segment s1 = m.getSegmentForPosition(newPosition);
        affectedSegments.add(s1);

        output.add(new ActorWalk(getRequesterId(),
                                 a.getNewestTS(),
                                 a.getMapId(),
                                 a.getMapInstanceNo(),
                                 newPosition));
        dataForSender.add(new ActorCooldownChanged(getRequesterId(),
                                                   a.getMoveSpeed()));

        ExitZone exit = m.getExit(newPosition);
        if (exit != null)
        {
            MapInstance destMap = world.findReadyInstance(exit.getDestMap());
            if (destMap == null)
            {
                destMap =
                        world.createMap(exit.getDestMap(),
                                        world.findFreeInstanceNo(exit.getDestMap()));
                if (destMap == null)
                    return false;
            }

            EntryZone destEntrance =
                    destMap.getEntrance(exit.getDestEntryZone());

            if (destEntrance == null)
                return false;

            boolean done = false;

            // some randomization
            Collections.shuffle(destEntrance.getPositionsList(), RANDOM);
            for (Position p : destEntrance.getPositionsList())
            {
                if (destMap.isPassable(p))
                {
                    s1 = destMap.getSegmentForPosition(p);
                    affectedSegments.add(s1);

                    int actorId = getRequesterId();
                    short destMapId = destMap.getType().getId();

                    output.add(new ActorChangedMap(actorId,
                                                   a.getNewestTS(),
                                                   destMapId,
                                                   destMap.getInstanceNo(),
                                                   p));
                    if (destMap.getType().isSafeMap())
                        dataForSender.add(new ActorChangedSafeMap(actorId,
                                                                  destMapId));
                    done = true;
                    break;
                }
            }
            if (!done)
            {
                destMap =
                        world.createMap(destMap.getType().getId(),
                                        world.findFreeInstanceNo(destMap.getType()
                                                                        .getId()));
                List<Position> positions = destEntrance.getPositionsList();
                Position p;
                if (positions.size() == 1)
                    p = positions.get(0);
                else
                    p = positions.get(RANDOM.nextInt(positions.size()));
                s1 = destMap.getSegmentForPosition(p);
                affectedSegments.add(s1);

                int actorId = getRequesterId();
                short destMapId = destMap.getType().getId();

                output.add(new ActorChangedMap(actorId,
                                               a.getNewestTS(),
                                               destMapId,
                                               destMap.getInstanceNo(),
                                               p));

                if (destMap.getType().isSafeMap())
                    dataForSender.add(new ActorChangedSafeMap(actorId,
                                                              destMapId));
            }
        }

        // this should work also when changing maps!
        if (s != s1)
        {
            Set<MapInstance.Segment> newSegments =
                    new HashSet<MapInstance.Segment>();
            for (MapInstance.Segment sx : s1.neighborhood())
            {
                newSegments.add(sx);
            }
            for (MapInstance.Segment sx : s.neighborhood())
            {
                newSegments.remove(sx);
            }
            for (MapInstance.Segment sx : newSegments)
            {
                dataForSender.add(new SegmentInfo(sx));
            }
        }

        return true;
    }

    /** {@inheritDoc} */
    @Override
    protected boolean paramsEquals(Command cmd)
    {
        return ((Move) cmd).getDirection() == getDirection();
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return MessageFormat.format("Move: id:{0}, direction:{1}",
                                    getRequesterId(),
                                    direction);
    }
}
