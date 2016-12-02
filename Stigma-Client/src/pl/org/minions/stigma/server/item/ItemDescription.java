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
package pl.org.minions.stigma.server.item;

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlList;

import pl.org.minions.stigma.databases.xml.Modifiable;
import pl.org.minions.stigma.game.item.type.ItemType.ItemKind;

/**
 * Class describing item parameters.
 */
public class ItemDescription implements Modifiable
{
    private boolean modified;
    private String name;
    private short type;
    private ItemKind kind;
    private List<Short> dynamicModifierList = new LinkedList<Short>();
    private List<Short> dynamicEffectList = new LinkedList<Short>();

    /**
     * Constructor.
     */
    public ItemDescription()
    {
    }

    /**
     * Returns kind.
     * @return kind
     */
    public ItemKind getKind()
    {
        return kind;
    }

    /**
     * Sets new value of kind.
     * @param kind
     *            the kind to set
     */
    @XmlAttribute(name = "kind", required = true)
    public void setKind(ItemKind kind)
    {
        this.kind = kind;
    }

    /**
     * Returns type.
     * @return type
     */
    public short getType()
    {
        return type;
    }

    /**
     * Sets new value of type.
     * @param type
     *            the type to set
     */
    @XmlAttribute(name = "type", required = true)
    public void setType(short type)
    {
        this.type = type;
    }

    /**
     * Returns item name.
     * @return item name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets new value of name.
     * @param name
     *            the name to set
     */
    @XmlAttribute(name = "name", required = false)
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Returns dynamic modifiers of item.
     * @return dynamic modifiers of item
     */
    @XmlList
    @XmlElement(name = "modifiers", required = true, nillable = false)
    public List<Short> getModifiers()
    {
        return dynamicModifierList;
    }

    /**
     * Returns dynamic effects of item.
     * @return dynamic effects of item
     */
    @XmlList
    @XmlElement(name = "effects", required = true, nillable = false)
    public List<Short> getEffects()
    {
        return dynamicEffectList;
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
        StringBuilder builder = new StringBuilder();
        builder.append("ItemDescription [typeid=");
        builder.append(type);
        builder.append(", name=");
        builder.append(name);
        builder.append(", modifiers=");
        builder.append(dynamicModifierList);
        builder.append(", effects=");
        builder.append(dynamicEffectList);
        builder.append("]");
        return builder.toString();
    }

}
