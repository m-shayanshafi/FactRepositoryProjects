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
package pl.org.minions.stigma.databases.map;

import pl.org.minions.stigma.game.map.MapType;
import pl.org.minions.stigma.game.map.TerrainSet;

/**
 * Map data base interface.
 */
public interface MapDB
{
    /**
     * Directory for storage of map types.
     */
    String MAP_TYPE_DIR = "maptypes";

    /**
     * File prefix for storage of map types.
     */
    String MAP_TYPE_PREF = "maptype";

    /**
     * Directory for storage of terrain sets.
     */
    String TERRAINSET_DIR = "terrainsets";
    /**
     * File prefix for storage of terrain sets.
     */
    String TERRAINSET_PREF = "terrainset";

    /**
     * Returns map type for given id.
     * @param type
     *            id of requested map type
     * @return map type for given id
     */
    MapType getMapType(short type);

    /**
     * Returns terrain set for given id.
     * @param terrainSetId
     *            id of requested terrain set
     * @return terrain set for given id.
     */
    TerrainSet getTerrainSet(short terrainSetId);
}
