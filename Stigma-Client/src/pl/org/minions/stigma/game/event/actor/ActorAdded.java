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
 * Class representing event of new actor logging in.
 */
public class ActorAdded extends Event
{
    private static final int ACTOR_CONST_SIZE = SizeOf.INT // actor id
        + SizeOf.SHORT // map id 
        + SizeOf.SHORT // map instance no
        + Position.sizeOf() // actor position
        + SizeOf.INT; // actor newest TS

    private Actor actor;

    /**
     * @param type
     */
    private ActorAdded()
    {
        super(EventType.ACTOR_ADDED);
    }

    /**
     * Constructor.
     * @param actor
     *            actor to add to world
     */
    public ActorAdded(Actor actor)
    {
        this();
        this.actor = actor;
    }

    /**
     * Creates empty object (needed for decoding).
     * @return empty object
     */
    public static ActorAdded create()
    {
        return new ActorAdded();
    }

    /** {@inheritDoc} */
    @Override
    public boolean apply(World world, DataRequestSinks dataRequestSink)
    {
        if (Log.isTraceEnabled())
            Log.logger.trace(MessageFormat.format("Adding actor: {0} to map id: {1} instance: {2}",
                                                  actor.getId(),
                                                  actor.getMapId(),
                                                  actor.getMapInstanceNo()));
        MapInstance m =
                world.getMap(actor.getMapId(), actor.getMapInstanceNo());
        if (m == null)
        {
            m = world.createMap(actor.getMapId(), actor.getMapInstanceNo());
            if (m == null)
                return false;
        }

        MapInstance.Segment s = m.getSegmentForPosition(actor.getPosition());
        if (s == null)
            return false;

        if (s.getActor(actor.getPosition()) != null
            && s.getActor(actor.getPosition()) != actor)
            return false;

        Actor actor2 = world.getActor(actor.getId());
        if (actor2 != null)
        {
            if (dataRequestSink != null)
                dataRequestSink.getActorSink().compareAndAdd(actor2, actor);
            actor2.setMapId(actor.getMapId());
            actor2.setMapInstanceNo(actor.getMapInstanceNo());
            actor2.setPosition(actor.getPosition());
            actor = actor2;
        }
        else
        {
            if (dataRequestSink != null)
                dataRequestSink.getActorSink().forceAdd(actor);
            world.addActor(actor);
        }

        s.putActor(actor);

        return true;
    }

    /** {@inheritDoc} */
    @Override
    protected boolean decodeParams(Buffer buffer)
    {
        actor = new Actor(buffer.decodeInt(), null);
        actor.setMapId(buffer.decodeShort());
        actor.setMapInstanceNo(buffer.decodeShort());
        actor.setPosition(buffer.decodePosition());
        int newestTS = buffer.decodeInt();
        actor.setSlowTS(newestTS);
        actor.setFastTS(newestTS);
        return true;
    }

    /** {@inheritDoc} */
    @Override
    protected boolean encodeParams(Buffer buffer)
    {
        buffer.encode(actor.getId());
        buffer.encode(actor.getMapId());
        buffer.encode(actor.getMapInstanceNo());
        buffer.encode(actor.getPosition());
        buffer.encode(actor.getNewestTS());
        return true;
    }

    /**
     * Returns actor.
     * @return actor
     */
    public Actor getActor()
    {
        return actor;
    }

    /** {@inheritDoc} */
    @Override
    protected int innerGetParamsLength()
    {
        return ACTOR_CONST_SIZE;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return MessageFormat.format("ActorAdded: id:{0}, TS:{5}, name:{1}, mapId:{2}, mapInstanceNo:{3}, position:{4}",
                                    actor.getId(),
                                    actor.getName(),
                                    actor.getMapId(),
                                    actor.getMapInstanceNo(),
                                    actor.getPosition(),
                                    actor.getNewestTS());
    }

    /** {@inheritDoc} */
    @Override
    public Set<Integer> getAffectedActors()
    {
        return Collections.singleton(actor.getId());
    }

}
