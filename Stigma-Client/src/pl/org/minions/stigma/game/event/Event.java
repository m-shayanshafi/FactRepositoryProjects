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
package pl.org.minions.stigma.game.event;

import java.util.Set;

import pl.org.minions.stigma.game.command.Command;
import pl.org.minions.stigma.game.command.data.sink.DataRequestSinks;
import pl.org.minions.stigma.game.world.World;
import pl.org.minions.stigma.network.messaging.NetworkObject;
import pl.org.minions.stigma.network.messaging.NetworkObjectCodec;

/**
 * Base effect of execution of {@link Command} - represents
 * what should be modified in "game world" after this
 * command.
 */
public abstract class Event extends NetworkObject<EventType>
{
    private static NetworkObjectCodec<EventType, Event> codec =
            new NetworkObjectCodec<EventType, Event>(EventType.class);

    /**
     * Constructor.
     * @param type
     *            type of event
     */
    protected Event(EventType type)
    {
        super(type);
    }

    /**
     * Returns codec for encoding/decoding all deltas.
     * @return codec for encoding/decoding all deltas.
     */
    public static NetworkObjectCodec<EventType, Event> getCodec()
    {
        return codec;
    }

    /**
     * Applies modification to given world.
     * @param world
     *            world to modify
     * @param dataRequestSink
     *            sink for requests for actor data updates,
     *            may be {@code null}, for use on client
     *            only
     * @return {@code true} if delta was applied
     *         successfully
     */
    public abstract boolean apply(World world, DataRequestSinks dataRequestSink);

    /** {@inheritDoc} */
    @Override
    public abstract String toString();

    /**
     * Returns set of identifiers of actors affected by this
     * event.
     * @return set of identifiers of actors affected by this
     *         event.
     */
    public abstract Set<Integer> getAffectedActors();

}
