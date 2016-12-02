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

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import pl.org.minions.stigma.databases.xml.Modifiable;
import pl.org.minions.stigma.globals.Position;
import pl.org.minions.utils.logger.Log;

/**
 * Class witch represent some zone on MapType. It contains
 * data about it's id and tiles which belong to it. It is
 * connected with MapType class, please update MapType class
 * version after changing this class
 */
public class Zone implements Modifiable
{
    private byte id;
    private List<Position> positionsList;
    private boolean modified;
    private String name;
    private String description;

    /**
     * Default constructor needed by JAXB.
     */
    public Zone()
    {
        this.id = -1;
        this.positionsList = new LinkedList<Position>();
    }

    /**
     * Constructor.
     * @param id
     *            id of gate in
     */
    public Zone(byte id)
    {
        this.id = id;
        this.positionsList = new LinkedList<Position>();
    }

    /**
     * Adds tile with provided position to gate.
     * @param position
     *            position of tile to add
     */
    public void addPosition(Position position)
    {
        positionsList.add(position);
        if (Log.isTraceEnabled())
        {
            Log.logger.trace("ADDED: To Zone with id: " + this.id + "Position:"
                + position.getX() + "," + position.getY());
        }
    }

    /** {@inheritDoc} */
    @Override
    public void clearModified()
    {
        this.modified = false;
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
     * Returns entrance id.
     * @return entrance id.
     */
    public byte getId()
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
     * Returns list of positions which are part of entrance.
     * @return list of positions which are part of entrance.
     */
    public List<Position> getPositionsList()
    {
        return positionsList;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode()
    {
        return this.id;
    }

    /** {@inheritDoc} */
    @XmlTransient
    @Override
    public boolean isModified()
    {
        return modified;
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
     * Sets entrance id.
     * @param id
     *            id to set
     */
    @XmlAttribute(name = "id", required = true)
    public void setId(byte id)
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
    @XmlElement(name = "name", required = false)
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Sets list of positions which are part of entrance.
     * @param positionsList
     *            new position list
     */
    @XmlElement(name = "pos", required = true)
    public void setPositionsList(List<Position> positionsList)
    {
        this.positionsList = positionsList;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        String newline = System.getProperty("line.separator");
        StringBuffer out = new StringBuffer();
        out.append("id: ").append(id).append(newline);
        out.append("name: ").append(name).append(newline);
        out.append("description: ").append(description).append(newline);
        out.append("positionsList:").append(newline);
        for (Position pos : positionsList)
        {
            out.append(pos.toString()).append(" ");
        }
        return out.toString();
    }
}
