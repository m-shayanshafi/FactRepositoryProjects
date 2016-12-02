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
package pl.org.minions.stigma.databases.map.server;

import java.net.URI;

import pl.org.minions.stigma.databases.map.MapDB;
import pl.org.minions.stigma.databases.xml.server.XmlSyncDB;
import pl.org.minions.stigma.game.map.MapType;
import pl.org.minions.stigma.game.map.TerrainSet;
import pl.org.minions.stigma.game.map.data.MapTypeData;

/**
 * Synchronous map data database.
 * @see XmlSyncDB
 * @see MapDB
 */
public class MapDBSync extends XmlSyncDB<MapType, MapTypeData> implements MapDB
{
    private TerrainSetDBSync terrainSetDB;

    /**
     * Constructor. Will try to load all maps from given
     * directory.
     * @param uri
     *            resources root
     */
    public MapDBSync(URI uri)
    {
        super(uri, MapTypeData.class, new MapTypeData.DataConverter(), true);
        terrainSetDB = new TerrainSetDBSync(uri);
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
    public MapType getMapType(short type)
    {
        return getById(type);
    }

    /** {@inheritDoc} */
    @Override
    public TerrainSet getTerrainSet(short terrainSetId)
    {
        return terrainSetDB.getById(terrainSetId);
    }

    /**
     * Returns TerrainSetDBSync used by this MapDB.
     * @return TerrainSetDBSync used by this MapDB
     */
    public TerrainSetDBSync getTerrainSetDBSync()
    {
        return terrainSetDB;
    }

}
