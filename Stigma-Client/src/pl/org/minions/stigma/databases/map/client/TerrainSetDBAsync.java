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
import pl.org.minions.stigma.game.map.TerrainSet;
import pl.org.minions.stigma.game.map.data.TerrainSetData;

/**
 * Asynchronous terrain set database. Utility class for
 * {@link MapDBAsync}.
 */
public class TerrainSetDBAsync extends XmlAsyncDB<TerrainSet, TerrainSetData>
{
    /**
     * Constructor.
     * @param uri
     *            resources root
     */
    public TerrainSetDBAsync(URI uri)
    {
        super(uri,
              TerrainSetData.class,
              new TerrainSetData.DataConverter(),
              true);
    }

    /** {@inheritDoc} */
    @Override
    public String getDbDir()
    {
        return MapDB.TERRAINSET_DIR;
    }

    /** {@inheritDoc} */
    @Override
    public String getFilePrefix()
    {
        return MapDB.TERRAINSET_PREF;
    }

    /**
     * Just for use of {@link MapDB}.
     * @param id
     *            id of terrain set
     * @return terrain set for given id
     * @see MapDB#getTerrainSet(short)
     */
    TerrainSet getTerrainSet(short id)
    {
        return super.getById(id);
    }
}
