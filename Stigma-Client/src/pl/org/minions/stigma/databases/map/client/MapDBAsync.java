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
package pl.org.minions.stigma.databases.map.client;

import java.net.URI;

import pl.org.minions.stigma.databases.map.MapDB;
import pl.org.minions.stigma.databases.xml.client.XmlAsyncDB;
import pl.org.minions.stigma.game.map.MapType;
import pl.org.minions.stigma.game.map.TerrainSet;
import pl.org.minions.stigma.game.map.data.MapTypeData;

/**
 * Asynchronous map data database.
 * @see MapDB
 * @see XmlAsyncDB
 */
public class MapDBAsync extends XmlAsyncDB<MapType, MapTypeData> implements
                                                                MapDB
{
    private TerrainSetDBAsync tileDB;

    /**
     * Construct cache map. It gets URI to the root
     * directory, where files to read are.
     * @param uri
     *            URI to the root directory with files to
     *            read
     */
    public MapDBAsync(URI uri)
    {
        super(uri, MapTypeData.class, new MapTypeData.DataConverter(), true);
        this.tileDB = new TerrainSetDBAsync(uri);
    }

    /** {@inheritDoc} */
    @Override
    public String getDbDir()
    {
        return MapDB.MAP_TYPE_DIR;
    }

    /** {@inheritDoc} */
    @Override
    public String getFilePrefix()
    {
        return MapDB.MAP_TYPE_PREF;
    }

    /** {@inheritDoc} */
    @Override
    public MapType getMapType(short id)
    {
        return super.getById(id);
    }

    /** {@inheritDoc} */
    @Override
    public TerrainSet getTerrainSet(short id)
    {
        return tileDB.getById(id);
    }

}
