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
import pl.org.minions.stigma.globals.SizeOf;
import pl.org.minions.stigma.network.Buffer;

/**
 * Class representing event of player logging out.
 */
public class ActorRemoved extends Event
{
    private int actorId;

    /** Creates empty delta - needed for decoding. */
    private ActorRemoved()
    {
        super(EventType.ACTOR_REMOVED);
    }

    /**
     * Constructor.
     * @param actorId
     *            id of actor to remove
     */
    public ActorRemoved(int actorId)
    {
        this();
        this.actorId = actorId;
    }

    /**
     * Creates empty object (needed for decoding).
     * @return empty object
     */
    public static ActorRemoved create()
    {
        return new ActorRemoved();
    }

    /** {@inheritDoc} */
    @Override
    public boolean apply(World world, DataRequestSinks dataRequestSink)
    {
        Actor a = world.getActor(actorId);
        if (a == null)
            return true; // was removed...

        world.removeActor(actorId);

        MapInstance m = world.getMap(a.getMapId(), a.getMapInstanceNo());
        if (m == null)
            return true; // was removed...

        MapInstance.Segment s = m.getSegmentForPosition(a.getPosition());
        if (s == null)
            return true; // was removed

        s.takeActor(a.getPosition());
        return true;
    }

    /**
     * Returns id of actor to remove.
     * @return id of actor to remove
     */
    public int getActorId()
    {
        return actorId;
    }

    /** {@inheritDoc} */
    @Override
    protected int innerGetParamsLength()
    {
        return SizeOf.INT;
    }

    /** {@inheritDoc} */
    @Override
    protected boolean decodeParams(Buffer buffer)
    {
        actorId = buffer.decodeInt();
        return true;
    }

    /** {@inheritDoc} */
    @Override
    protected boolean encodeParams(Buffer buffer)
    {
        buffer.encode(actorId);
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return MessageFormat.format("ActorRemoved: id:{0}", actorId);
    }

    /** {@inheritDoc} */
    @Override
    public Set<Integer> getAffectedActors()
    {
        return Collections.singleton(actorId);
    }

}
