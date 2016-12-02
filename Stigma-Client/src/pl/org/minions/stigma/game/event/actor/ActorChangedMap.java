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

/**
 * Class representing map change of one actor.
 */
public class ActorChangedMap extends Event
{
    private static final int SIZE =
            SizeOf.INT + SizeOf.INT + SizeOf.SHORT + SizeOf.SHORT
                + Position.sizeOf();
    private int actorId;
    private int actorTS;
    private short mapId;
    private short instanceNo;
    private Position position;

    /** Creates empty event - needed for decoding. */
    private ActorChangedMap()
    {
        super(EventType.ACTOR_CHANGED_MAP);
    }

    /**
     * Creates new event.
     * @param actorId
     *            moving actor id
     * @param actorTS
     *            newest actor TS
     * @param mapId
     *            new map id
     * @param instanceNo
     *            new instance number
     * @param position
     *            new actor position on new map
     */
    public ActorChangedMap(int actorId,
                           int actorTS,
                           short mapId,
                           short instanceNo,
                           Position position)
    {
        this();
        this.actorId = actorId;
        this.actorTS = actorTS;
        this.instanceNo = instanceNo;
        this.mapId = mapId;
        this.position = position;
    }

    /**
     * Creates empty object (needed for decoding).
     * @return empty object
     */
    public static ActorChangedMap create()
    {
        return new ActorChangedMap();
    }

    /** {@inheritDoc} */
    @Override
    public boolean apply(World world, DataRequestSinks dataRequestSink)
    {
        Actor a = world.getActor(actorId);
        if (a == null)
        {
            a = new Actor(actorId, "");
            world.addActor(a);
            if (dataRequestSink != null)
                dataRequestSink.getActorSink().add(a);
        }
        else
        {
            if (dataRequestSink != null)
                dataRequestSink.getActorSink().compareAndAdd(a, this.actorTS);
        }

        MapInstance m = world.getMap(a.getMapId(), a.getMapInstanceNo());
        if (m != null)
        {
            // nowhere to remove from
            MapInstance.Segment s = m.getSegmentForPosition(a.getPosition());
            if (s == null || a != s.takeActor(a.getPosition()))
                return false;
        }

        a.setPosition(position);
        a.setMapId(mapId);
        a.setMapInstanceNo(instanceNo);

        m = world.getMap(mapId, instanceNo);
        if (m == null)
        {
            m = world.createMap(mapId, instanceNo);
            if (m == null)
                return false;
        }

        MapInstance.Segment s = m.getSegmentForPosition(position);
        if (s == null)
            return false;

        s.putActor(a);

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
        position = buffer.decodePosition();
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
        buffer.encode(position);
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
     * Returns instanceNo.
     * @return instanceNo
     */
    public short getInstanceNo()
    {
        return instanceNo;
    }

    /**
     * Returns mapId.
     * @return mapId
     */
    public short getMapId()
    {
        return mapId;
    }

    /**
     * Returns position.
     * @return position
     */
    public Position getPosition()
    {
        return position;
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
        return MessageFormat.format("ActorChangedMap: id:{0}, time-stamp:{4}, mapId:{1}, mapInstanceNo:{2}, position:{3}",
                                    actorId,
                                    mapId,
                                    instanceNo,
                                    position,
                                    actorTS);
    }

    /** {@inheritDoc} */
    @Override
    public Set<Integer> getAffectedActors()
    {
        return Collections.singleton(actorId);
    }

}
