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
package pl.org.minions.stigma.game.map;

import pl.org.minions.stigma.databases.xml.Modifiable;

/**
 * Class which represent type of tile. At this point it
 * contains only information whether tile is passable or
 * not. It was created to keep the convention. It is
 * connected with TerrainSet class, please update TerrainSet
 * class version after changing TileType.
 */
public class TileType implements Modifiable
{
    private int id;
    private TerrainType terrainType;
    private boolean modified;

    /**
     * Constructor.
     * @param i
     *            id of tile
     */
    public TileType(int i)
    {
        this.id = i;
    }

    /** {@inheritDoc} */
    @Override
    public void clearModified()
    {
        this.modified = false;
    }

    /**
     * Returns unique id of this tile.
     * @return unique id in terrain type
     */
    public int getId()
    {
        return id;
    }

    /**
     * Returns terrainType.
     * @return terrainType
     */
    public TerrainType getTerrainType()
    {
        return terrainType;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isModified()
    {
        return modified;
    }

    /**
     * Checks whether this tile is passable.
     * @return {@code true} when tile is passable.
     */
    public boolean isPassable()
    {
        return terrainType != null && terrainType.isPassable();
    }

    /**
     * Sets new unique id.
     * @param id
     *            the id to set
     */
    public void setId(int id)
    {
        this.id = id;
    }

    /** {@inheritDoc} */
    @Override
    public void setModified()
    {
        this.modified = true;
    }

    /**
     * Sets new value of terrainType.
     * @param terrainType
     *            the terrainType to set
     */
    public void setTerrainType(TerrainType terrainType)
    {
        this.terrainType = terrainType;
    }

}
