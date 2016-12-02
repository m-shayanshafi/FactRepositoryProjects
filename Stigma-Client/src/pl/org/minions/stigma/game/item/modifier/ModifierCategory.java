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
package pl.org.minions.stigma.game.item.modifier;

import pl.org.minions.stigma.databases.xml.XmlDbElem;

/**
 * Class representing category of modifier.
 */
public class ModifierCategory implements XmlDbElem
{
    private short id;
    private String name;
    private String description;
    private boolean modified;

    /**
     * Default constructor (needed by JAXB).
     */
    public ModifierCategory()
    {
        this.id = -1;
        this.name = null;
        this.description = null;
    }

    /**
     * Main constructor.
     * @param id
     *            id of category
     * @param name
     *            name of category
     */
    public ModifierCategory(short id, String name)
    {
        this.id = id;
        this.name = name;
    }

    /** {@inheritDoc} */
    @Override
    public short getId()
    {
        return id;
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

    /**
     * Returns name.
     * @return name
     */
    public String getName()
    {
        return name;
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

    /**
     * Sets new value of description.
     * @param description
     *            the description to set
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        StringBuffer out = new StringBuffer();

        out.append("[").append(id).append("] ").append(name);
        if (description != null && !description.isEmpty())
            out.append("(").append(description).append(")");

        return out.toString();
    }
}
