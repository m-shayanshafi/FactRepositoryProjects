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
package pl.org.minions.stigma.game.map.data;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;

import pl.org.minions.stigma.databases.xml.Converter;
import pl.org.minions.stigma.game.map.TerrainType;
import pl.org.minions.stigma.game.map.TileType;

/**
 * Plain Old Java Object containing data for
 * {@link TerrainType} .
 */
public class TerrainTypeData
{
    /**
     * Converter between {@link TerrainType} and
     * {@link TerrainTypeData}.
     */
    public static class DataConverter implements
                                     Converter<TerrainType, TerrainTypeData>
    {
        private Converter<TileType, TileTypeData> tileConverter =
                new TileTypeData.DataConverter();

        /** {@inheritDoc} */
        @Override
        public TerrainTypeData buildData(TerrainType object)
        {
            List<TileTypeData> list = new LinkedList<TileTypeData>();
            for (TileType tt : object.getTileMap().values())
                list.add(tileConverter.buildData(tt));
            return new TerrainTypeData(object.getId(),
                                       object.isPassable(),
                                       String.format("#%06", object.getColor()
                                                                   .getRGB()),
                                       object.getImageFileName(),
                                       object.getPriority(),
                                       list,
                                       object.getName(),
                                       object.getDescription());
        }

        /** {@inheritDoc} */
        @Override
        public TerrainType buildObject(TerrainTypeData data)
        {
            TerrainType t =
                    new TerrainType(data.id,
                                    data.passable,
                                    Color.decode(data.color),
                                    data.imageFileName,
                                    data.priority);
            for (TileTypeData tt : data.tileList)
                t.addTileType(tileConverter.buildObject(tt));
            t.setDescription(data.description);
            t.setName(data.name);
            return t;
        }
    }

    private int id;
    private boolean passable;
    private String color;
    private String imageFileName;
    private int priority;
    private List<TileTypeData> tileList;
    private String name;

    private String description;

    /**
     * Default constructor (for JAXB).
     */
    public TerrainTypeData()
    {
        this.id = -1;
        this.tileList = new LinkedList<TileTypeData>();
    }

    /**
     * Constructor.
     * @param id
     *            id of terrain type
     * @param passable
     *            whether or not this terrain type should be
     *            passable
     * @param tileList
     *            list of tiles which are part of this
     *            terrain type
     * @param color
     *            color (encoded in text) of this terrain
     *            type (used when no image is available)
     * @param imageFileName
     *            file with all tile types etc representing
     *            this terrain type
     * @param priority
     *            redraw priority of this terrain type
     * @param name
     *            name of terrain type
     * @param description
     *            description of terrain type
     */
    public TerrainTypeData(int id,
                           boolean passable,
                           String color,
                           String imageFileName,
                           int priority,
                           List<TileTypeData> tileList,
                           String name,
                           String description)
    {
        super();
        this.id = id;
        this.passable = passable;
        this.tileList = tileList;
        this.color = color;
        this.imageFileName = imageFileName;
        this.priority = priority;
        this.name = name;
        this.description = description;
    }

    /**
     * Returns color.
     * @return color
     */
    @XmlAttribute(required = true)
    public String getColor()
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
    @XmlAttribute(required = true)
    public int getId()
    {
        return id;
    }

    /**
     * Returns imageFileName.
     * @return imageFileName
     */
    @XmlAttribute(required = false)
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
     * Returns priority.
     * @return priority
     */
    @XmlAttribute(required = true)
    public int getPriority()
    {
        return priority;
    }

    /**
     * Returns tileList.
     * @return tileList
     */
    @XmlElementWrapper
    @XmlElements(value = @XmlElement(name = "tile", type = TileTypeData.class))
    public List<TileTypeData> getTileList()
    {
        return tileList;
    }

    /**
     * Returns {@code true} when data is proper (logically
     * also).
     * @return {@code true} when data is proper
     */
    public boolean isGood()
    {
        if (id == -1 || color == null || color.isEmpty() || tileList == null
            || tileList.isEmpty())
            return false;
        for (TileTypeData t : tileList)
            if (!t.isGood())
                return false;
        return true;
    }

    /**
     * Returns passable.
     * @return passable
     */
    @XmlAttribute(required = true)
    public boolean isPassable()
    {
        return passable;
    }

    /**
     * Sets new value of color.
     * @param color
     *            the color to set
     */
    public void setColor(String color)
    {
        this.color = color;
    }

    /**
     * Sets new value of description.
     * @param description
     *            the description to set
     */
    @XmlElement(name = "desc", required = false)
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

    /**
     * Sets new value of name.
     * @param name
     *            the name to set
     */
    @XmlElement(name = "name", required = false)
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
