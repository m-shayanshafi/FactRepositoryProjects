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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import pl.org.minions.stigma.databases.xml.XmlDbElem;

/**
 * Class with represents the library of game terrains.
 */
public class TerrainSet implements XmlDbElem
{
    private Map<Integer, TerrainType> terrainMap;
    private short id;
    private String name;
    private String description;
    private boolean modified;

    /**
     * Constructor.
     * @param id
     *            id of terrain set
     */
    public TerrainSet(short id)
    {
        this.terrainMap = new HashMap<Integer, TerrainType>();
        this.id = id;
    }

    /**
     * Adds terrain type to this set.
     * @param t
     *            terrain type to add
     */
    public void addTerrainType(TerrainType t)
    {
        terrainMap.put(t.getId(), t);
    }

    /** {@inheritDoc} */
    @Override
    public void clearModified()
    {
        this.modified = false;
        for (TerrainType tt : terrainMap.values())
            tt.clearModified();
    }

    /**
     * Returns description.
     * @return description
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * Returns unique terrain set id.
     * @return terrain set id
     */
    public short getId()
    {
        return id;
    }

    /**
     * Returns name.
     * @return name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Returns terrain type for given id.
     * @param id
     *            id of requested terrain type
     * @return terrain type for given id.
     */
    public TerrainType getTerrain(int id)
    {
        return terrainMap.get(id);
    }

    /**
     * Returns terrain types in this set.
     * @return terrain types
     */
    public Collection<TerrainType> getTerrainTypes()
    {
        return terrainMap.values();
    }

    /**
     * Returns tile type for given identifiers.
     * @param terrainType
     *            id of enclosing terrain type
     * @param tileId
     *            id of tile from terrain type
     * @return tile type for given identifiers.
     */
    public TileType getType(int terrainType, int tileId)
    {
        return this.terrainMap.get(terrainType).getTile(tileId);
    }

    /** {@inheritDoc} */
    @Override
    public boolean isModified()
    {
        if (modified)
            return true;
        for (TerrainType tt : terrainMap.values())
            if (tt.isModified())
                return true;
        return false;
    }

    /**
     * Sets new value of description.
     * @param description
     *            the description to set
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * Sets new id of terrain set.
     * @param id
     *            terrain set id to set
     */
    public void setId(short id)
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
     * Sets new value of name.
     * @param name
     *            the name to set
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        StringBuffer out = new StringBuffer();
        out.append("id: " + id + "\n");
        out.append("terrainMap:\n");
        for (Integer key : terrainMap.keySet())
        {
            out.append("[" + key + ": " + terrainMap.get(key).isPassable()
                + "] ");
        }
        return out.toString();
    }

}
