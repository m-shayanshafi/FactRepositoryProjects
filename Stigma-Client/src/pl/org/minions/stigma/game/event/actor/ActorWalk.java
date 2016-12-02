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
package pl.org.minions.stigma.game.event.actor;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.Set;

import pl.org.minions.stigma.game.actor.Actor;
import pl.org.minions.stigma.game.command.data.sink.DataRequestSinks;
import pl.org.minions.stigma.game.event.Event;
import pl.org.minions.stigma.game.event.EventType;
import pl.org.minions.stigma.game.map.MapInstance;
import pl.org.minions.stigma.game.world.World;
import pl.org.minions.stigma.globals.Position;
import pl.org.minions.stigma.globals.SizeOf;
import pl.org.minions.stigma.network.Buffer;
import pl.org.minions.utils.logger.Log;

/**
 * Class representing position change of one actor.
 */
public class ActorWalk extends Event
{
    private static final int SIZE =
            SizeOf.INT + SizeOf.INT + Position.sizeOf() + SizeOf.SHORT
                + SizeOf.SHORT;
    private int actorId;
    private int actorTS;
    private Position newPosition;
    private short mapId;
    private short instanceNo;

    /** Creates empty event - needed for decoding. */
    private ActorWalk()
    {
        super(EventType.ACTOR_WALK);
    }

    /**
     * Creates new event.
     * @param actorId
     *            is of moved actor
     * @param actorTS
     *            newest actor TS
     * @param mapId
     *            id of map on which move taken place
     * @param instanceNo
     *            instance number of map on which move taken
     *            place
     * @param newPosition
     *            new position of moved actor
     */
    public ActorWalk(int actorId,
                     int actorTS,
                     short mapId,
                     short instanceNo,
                     Position newPosition)
    {
        this();
        this.actorId = actorId;
        this.actorTS = actorTS;
        this.newPosition = newPosition;
        this.mapId = mapId;
        this.instanceNo = instanceNo;
    }

    /**
     * Creates empty object (needed for decoding).
     * @return empty object
     */
    public static ActorWalk create()
    {
        return new ActorWalk();
    }

    /** {@inheritDoc} */
    @Override
    public boolean apply(World world, DataRequestSinks dataRequestSink)
    {
        Actor a = world.getActor(actorId);
        Position oldPosition = null;
        if (a == null)
        {
            a = new Actor(actorId, "");
            a.setMapId(mapId);
            a.setMapInstanceNo(instanceNo);
            world.addActor(a);
            if (dataRequestSink != null)
                dataRequestSink.getActorSink().add(a);
        }
        else
        {
            if (dataRequestSink != null)
                dataRequestSink.getActorSink().compareAndAdd(a, this.actorTS);

            oldPosition = a.getPosition();
        }
        a.setPosition(newPosition);

        MapInstance m = world.getMap(a.getMapId(), a.getMapInstanceNo());
        if (m == null)
        {
            m = world.createMap(a.getMapId(), a.getMapInstanceNo());
            if (m == null)
            {
                Log.logger.debug("Null map");
                return false;
            }
        }

        if (oldPosition != null)
            m.getSegmentForPosition(oldPosition).takeActor(oldPosition);
        m.getSegmentForPosition(newPosition).putActor(a);

        return true;
    }

    /** {@inheritDoc} */
    @Override
    protected boolean decodeParams(Buffer buffer)
    {
        actorId = buffer.decodeInt();
        actorTS = buffer.decodeInt();
        mapId = buffer.decodeShort();
        instanceNo = buffer.decodeShort();
        newPosition = buffer.decodePosition();
        return true;
    }

    /** {@inheritDoc} */
    @Override
    protected boolean encodeParams(Buffer buffer)
    {
        buffer.encode(actorId);
        buffer.encode(actorTS);
        buffer.encode(mapId);
        buffer.encode(instanceNo);
        buffer.encode(newPosition);
        return true;
    }

    /**
     * Returns actorId.
     * @return actorId
     */
    public int getActorId()
    {
        return actorId;
    }

    /**
     * Returns actorTS.
     * @return actorTS
     */
    public int getActorTS()
    {
        return actorTS;
    }

    /**
     * Returns newPosition.
     * @return newPosition
     */
    public Position getNewPosition()
    {
        return newPosition;
    }

    /** {@inheritDoc} */
    @Override
    protected int innerGetParamsLength()
    {
        return SIZE;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return MessageFormat.format("ActorChangedPosition: id:{0}, mapId:{1}, instanceNo:{2}, newPosition:{3}",
                                    actorId,
                                    mapId,
                                    instanceNo,
                                    newPosition);
    }

    /** {@inheritDoc} */
    @Override
    public Set<Integer> getAffectedActors()
    {
        return Collections.singleton(actorId);
    }

}
