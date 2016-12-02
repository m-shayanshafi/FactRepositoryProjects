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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import pl.org.minions.stigma.databases.xml.XmlDbElem;

/**
 * Interface representing modification put on item.
 */
public abstract class ItemModifier implements XmlDbElem
{
    private short id;
    private String name;
    private String description;
    private boolean modified;
    private short categoryId;

    /**
     * Enumeration representing type of items which modifier
     * concerns.
     */
    public enum ModifierItemType
    {
        WEAPON_MODIFIER, ARMOR_MODIFIER
    }

    /**
     * Default constructor needed by JAXB.
     */
    protected ItemModifier()
    {
    }

    /**
     * Constructor.
     * @param modifierCategoryId
     *            category of this modifier
     * @param name
     *            name of modifier
     * @param id
     *            id of modifier
     */
    protected ItemModifier(short id, String name, short modifierCategoryId)
    {
        this.id = id;
        this.name = name;
        this.categoryId = modifierCategoryId;
    }

    /**
     * Gets type of modifier.
     * @return modifier type
     */
    public abstract ModifierItemType getModifierItemType();

    /**
     * Returns id of category of this modifier.
     * @return id of category of this modifier.
     */
    public short getCategoryId()
    {
        return categoryId;
    }

    /**
     * Sets new value for id of category of this modifier.
     * @param modifierCategoryId
     *            new value
     */
    @XmlAttribute(name = "catId", required = true)
    public void setCategoryId(short modifierCategoryId)
    {
        this.categoryId = modifierCategoryId;
    }

    /**
     * Returns id.
     * @return id
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

    /** {@inheritDoc} */
    @Override
    public void clearModified()
    {
        this.modified = false;
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
        this.modified = true;
    }

    /**
     * Sets new value of id.
     * @param id
     *            the id to set
     */
    @XmlAttribute(name = "id", required = true)
    public void setId(short id)
    {
        this.id = id;
    }

    /**
     * Sets new value of name.
     * @param name
     *            the name to set
     */
    @XmlElement(name = "name", required = true)
    public void setName(String name)
    {
        this.name = name;
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
     * Returns description.
     * @return description
     */
    public String getDescription()
    {
        return description;
    }
}
