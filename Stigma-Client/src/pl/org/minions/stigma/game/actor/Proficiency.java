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
package pl.org.minions.stigma.game.actor;

import pl.org.minions.stigma.databases.xml.XmlDbElem;

/**
 * Class which describes proficiency.
 */
public class Proficiency implements XmlDbElem
{
    private short id;
    private String name;
    private boolean modified;

    /**
     * Dummy constructor.
     */
    public Proficiency()
    {
        this.id = -1;
        this.name = null;
    }

    /**
     * Default constructor.
     * @param id
     *            id of proficiency
     * @param name
     *            name of proficiency
     */
    public Proficiency(short id, String name)
    {
        this.id = id;
        this.name = name;
    }

    /**
     * Returns proficiency id.
     * @return the id
     */
    public short getId()
    {
        return id;
    }

    /**
     * Returns proficiency name.
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets new value of id.
     * @param id
     *            the id to set
     */
    public void setId(short id)
    {
        this.id = id;
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
    public void clearModified()
    {
        modified = false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isModified()
    {
        return modified;
    }

    /** {@inheritDoc} */
    @Override
    public void setModified()
    {
        modified = true;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "[" + id + "]:" + name;
    }
}
