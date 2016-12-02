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
package pl.org.minions.stigma.game.data.actor;

import java.text.MessageFormat;
import java.util.Collections;

import pl.org.minions.stigma.game.actor.Actor;
import pl.org.minions.stigma.game.command.data.sink.DataRequestSinks;
import pl.org.minions.stigma.game.data.WorldData;
import pl.org.minions.stigma.game.data.WorldDataType;
import pl.org.minions.stigma.game.world.World;
import pl.org.minions.stigma.globals.SizeOf;
import pl.org.minions.stigma.network.Buffer;

/**
 * Class representing change of 'safe' map of actor.
 */
public class ActorChangedSafeMap extends WorldData
{
    private static final int SIZE = SizeOf.INT + SizeOf.SHORT;
    private int actorId;

    private short safeMapId;

    /**
     * Constructor used for decoding. Created object is
     * 'empty'.
     */
    private ActorChangedSafeMap()
    {
        super(WorldDataType.ACTOR_CHANGED_SAFE_MAP);
    }

    /**
     * Constructor.
     * @param actorId
     *            id of actor changing safe map
     * @param safeMapId
     *            new actor's safe map id
     */
    public ActorChangedSafeMap(int actorId, short safeMapId)
    {
        this();
        this.actorId = actorId;
        this.safeMapId = safeMapId;
    }

    /**
     * Creates empty object (needed for decoding).
     * @return empty object
     */
    public static ActorChangedSafeMap create()
    {
        return new ActorChangedSafeMap();
    }

    /** {@inheritDoc} */
    @Override
    public boolean apply(World world, DataRequestSinks dataRequestSink)
    {
        Actor a = world.getActor(actorId);
        a.setSafeMapId(safeMapId);
        return true;
    }

    /** {@inheritDoc} */
    @Override
    protected boolean decodeParams(Buffer buffer)
    {
        actorId = buffer.decodeInt();
        safeMapId = buffer.decodeShort();
        return true;
    }

    /** {@inheritDoc} */
    @Override
    protected boolean encodeParams(Buffer buffer)
    {
        buffer.encode(actorId);
        buffer.encode(safeMapId);
        return true;
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
        return MessageFormat.format("ActorChangedSafeMap: actorId={0} safeMapId={1}",
                                    actorId,
                                    safeMapId);
    }

    /** {@inheritDoc} */
    @Override
    public Iterable<Integer> updatedIds()
    {
        return Collections.singleton(actorId);
    }

}
