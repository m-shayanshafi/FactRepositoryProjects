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
package pl.org.minions.stigma.game.data;

import pl.org.minions.stigma.game.command.data.sink.DataRequestSinks;
import pl.org.minions.stigma.game.world.World;
import pl.org.minions.stigma.network.messaging.NetworkObject;
import pl.org.minions.stigma.network.messaging.NetworkObjectCodec;

/**
 * Class representing part of "world data". Can be used to
 * synchronize data between world representations - in
 * example between server and client.
 */
public abstract class WorldData extends NetworkObject<WorldDataType>
{
    private static NetworkObjectCodec<WorldDataType, WorldData> codec =
            new NetworkObjectCodec<WorldDataType, WorldData>(WorldDataType.class);

    /**
     * Creates data object with given type.
     * @param type
     *            type of delta
     */
    protected WorldData(WorldDataType type)
    {
        super(type);
    }

    /**
     * Returns codec for encoding/decoding all data objects.
     * @return codec for encoding/decoding all data objects.
     */
    public static NetworkObjectCodec<WorldDataType, WorldData> getCodec()
    {
        return codec;
    }

    /**
     * Applies information to given world.
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

    /**
     * Returns iterable that allows to iterate over ids of
     * updated objects.
     * @return iterable that allows to iterate over ids of
     *         updated objects.
     */
    public abstract Iterable<Integer> updatedIds();

    /** {@inheritDoc} */
    @Override
    public abstract String toString();
}
