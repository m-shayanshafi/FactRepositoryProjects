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

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import pl.org.minions.stigma.databases.xml.Modifiable;

/**
 * Class representing terrain type. Terrain is set of tiles
 * with shared "passable", similar graphics etc.
 */
public class TerrainType implements Modifiable
{
    public static final TerrainType EMPTY_TERRAIN =
            new TerrainType(-1, false, Color.BLACK, null, -1);
    private int id;
    private boolean passable;
    private Color color;
    private String imageFileName;
    private int priority;
    private boolean modified;
    private String name;
    private String description;

    private Map<Integer, TileType> tileMap = new HashMap<Integer, TileType>();

    /**
     * Constructor.
     * @param id
     *            id of terrain type
     * @param passable
     *            whether or not this terrain type should be
     *            passable
     * @param color
     *            color of this terrain type (used when no
     *            image is available)
     * @param imageFileName
     *            file with all tile types etc representing
     *            this terrain type
     * @param priority
     *            redraw priority of this terrain type
     */
    public TerrainType(int id,
                       boolean passable,
                       Color color,
                       String imageFileName,
                       int priority)
    {
        super();
        this.id = id;
        this.passable = passable;
        this.color = color;
        this.imageFileName = imageFileName;
        this.priority = priority;
    }

    /**
     * Adds tile type to terrain type. Assigns parent
     * terrain type to tile type.
     * @param tile
     *            tile to add
     */
    public void addTileType(TileType tile)
    {
        tileMap.put(tile.getId(), tile);
        tile.setTerrainType(this);
    }

    /** {@inheritDoc} */
    @Override
    public void clearModified()
    {
        this.modified = false;
        for (TileType tt : tileMap.values())
            tt.clearModified();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o)
    {
        return o instanceof TerrainType && ((TerrainType) o).getId() == id;
    }

    /**
     * Returns color.
     * @return color
     */
    public Color getColor()
    {
        return color;
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
     * Returns id.
     * @return id
     */
    public int getId()
    {
        return id;
    }

    /**
     * Returns imageFileName.
     * @return imageFileName
     */
    public String getImageFileName()
    {
        return imageFileName;
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
     * Returns the number of tile types this terrain type
     * consists of.
     * @return number of tile types
     */
    public int getNumberOfTileTypes()
    {
        return tileMap.size();
    }

    /**
     * Returns priority.
     * @return priority
     */
    public int getPriority()
    {
        return priority;
    }

    /**
     * Returns tile for given id.
     * @param id
     *            id of requested tile
     * @return tile for given id
     */
    public TileType getTile(int id)
    {
        return tileMap.get(id);
    }

    /**
     * Returns tileMap.
     * @return tileMap
     */
    public Map<Integer, TileType> getTileMap()
    {
        return tileMap;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode()
    {
        return id;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isModified()
    {
        if (modified)
            return true;
        for (TileType tt : tileMap.values())
            if (tt.isModified())
                return true;
        return false;
    }

    /**
     * Returns passable.
     * @return passable
     */
    public boolean isPassable()
    {
        return passable;
    }

    /**
     * Sets new value of color.
     * @param color
     *            the color to set
     */
    public void setColor(Color color)
    {
        this.color = color;
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
     * Sets new value of id.
     * @param id
     *            the id to set
     */
    public void setId(int id)
    {
        this.id = id;
    }

    /**
     * Sets new value of imageFileName.
     * @param imageFileName
     *            the imageFileName to set
     */
    public void setImageFileName(String imageFileName)
    {
        this.imageFileName = imageFileName;
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

    /**
     * Sets new value of passable.
     * @param passable
     *            the passable to set
     */
    public void setPassable(boolean passable)
    {
        this.passable = passable;
    }

    /**
     * Sets new value of priority.
     * @param priority
     *            the priority to set
     */
    public void setPriority(int priority)
    {
        this.priority = priority;
    }
}
