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
package pl.org.minions.stigma.game.item.type;

import pl.org.minions.stigma.databases.xml.XmlDbElem;
import pl.org.minions.stigma.game.item.LogicalSlotType;

/**
 * Abstract class representing highest level description for
 * item type (a model for item instance).
 */
public abstract class ItemType implements XmlDbElem
{
    /**
     * Enumeration representing groups of items divided by
     * their functionality.
     */
    public enum ItemKind
    {
        /**
         * Weapons - things you fight with: swords, axes
         * etc.
         */
        WEAPON,
        /**
         * Armors and all wearable items: armors, plates,
         * robes, rings etc.
         */
        ARMOR,
        /**
         * All non wearable objects.
         */
        OTHER;

        private static final ItemKind[] VALUES_ARRAY = values();

        /**
         * Returns enum value for given ordinal.
         * @param ordinal
         *            requested ordinal
         * @return enum value for given ordinal.
         */
        public static ItemKind getForOridinal(int ordinal)
        {
            return VALUES_ARRAY[ordinal];
        }

        /**
         * Returns number of values in enum.
         * @return number of values in enum.
         */
        public static int valuesCount()
        {
            return VALUES_ARRAY.length;
        }

    }

    private short id;
    private short weight;
    private int value;
    private String name;
    private String description;
    private boolean modified;

    private String onGroundIcon;
    private String inventoryIcon;

    private LogicalSlotType logicalSlotType;

    /**
     * Default constructor for itemType, protected because
     * ItemType is abstract.
     * @param id
     *            id of item type
     * @param weight
     *            base weight of item
     * @param value
     *            base value of item
     * @param name
     *            base name of item
     * @param description
     *            base description of item type
     * @param onGroundIcon
     *            path to icon representing item laying on
     *            ground
     * @param inventoryIcon
     *            path to icon representing item in
     *            inventory
     * @param slot
     *            logical slot of item
     */
    protected ItemType(short id,
                       short weight,
                       int value,
                       String name,
                       String description,
                       String onGroundIcon,
                       String inventoryIcon,
                       LogicalSlotType slot)
    {
        this.id = id;
        this.weight = weight;
        this.value = value;
        this.name = name;
        this.description = description;
        this.logicalSlotType = slot;
        this.onGroundIcon = onGroundIcon;
        this.inventoryIcon = inventoryIcon;
    }

    /**
     * Gets kind of an item type (is it weapon, armor, or
     * other).
     * @return kind of item type
     */
    public abstract ItemKind getKind();

    /**
     * Returns name of icon which should represent this item
     * when laying on ground.
     * @return onGroundIcon
     */
    public String getOnGroundIcon()
    {
        return onGroundIcon;
    }

    /**
     * Sets new value of onGroundIcon.
     * @param onGroundIcon
     *            the onGroundIcon to set
     */
    public void setOnGroundIcon(String onGroundIcon)
    {
        this.onGroundIcon = onGroundIcon;
    }

    /**
     * Returns name of icon which should represent this item
     * in inventory.
     * @return inventoryIcon
     */
    public String getInventoryIcon()
    {
        return inventoryIcon;
    }

    /**
     * Sets new value of inventoryIcon.
     * @param inventoryIcon
     *            the inventoryIcon to set
     */
    public void setInventoryIcon(String inventoryIcon)
    {
        this.inventoryIcon = inventoryIcon;
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
     * Sets new value of id.
     * @param id
     *            the id to set
     */
    public void setId(short id)
    {
        this.id = id;
    }

    /**
     * Returns weight.
     * @return weight
     */
    public short getWeight()
    {
        return weight;
    }

    /**
     * Sets new value of weight.
     * @param weight
     *            the weight to set
     */
    public void setWeight(short weight)
    {
        this.weight = weight;
    }

    /**
     * Returns value.
     * @return value
     */
    public int getValue()
    {
        return value;
    }

    /**
     * Sets new value of value.
     * @param value
     *            the value to set
     */
    public void setValue(int value)
    {
        this.value = value;
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
     * Sets new value of name.
     * @param name
     *            the name to set
     */
    public void setName(String name)
    {
        this.name = name;
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
     * Sets new value of description.
     * @param description
     *            the description to set
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * Returns equippementSlot.
     * @return equippementSlot
     */
    public LogicalSlotType getEquipementSlot()
    {
        return logicalSlotType;
    }

    /**
     * Sets new value of equippementSlot.
     * @param equipementSlot
     *            the equippementSlot to set
     */
    public void setEquipementSlot(LogicalSlotType equipementSlot)
    {
        this.logicalSlotType = equipementSlot;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        StringBuffer out = new StringBuffer();

        out.append("id: ").append(id).append('\n');
        out.append("name: ").append(name).append('\n');
        out.append("description: ").append(description).append('\n');
        out.append("weight: ").append(weight).append('\n');
        out.append("value: ").append(value).append('\n');
        out.append("logicSlotType: ")
           .append(logicalSlotType.name())
           .append('\n');

        return out.toString();
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
     * Returns {@code false}. Should be used to check if
     * this type is complete.
     * @return {@code false}
     */
    public boolean isStub()
    {
        return false;
    }
}
