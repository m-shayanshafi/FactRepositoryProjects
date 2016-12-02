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
package pl.org.minions.stigma.game.item;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import pl.org.minions.stigma.game.TimestampedObject;
import pl.org.minions.stigma.game.item.modifier.ItemModifier;
import pl.org.minions.stigma.game.item.type.ItemType;
import pl.org.minions.stigma.game.item.type.ItemType.ItemKind;
import pl.org.minions.stigma.globals.Position;
import pl.org.minions.utils.logger.Log;

/**
 * Main class representing dynamic aspect of items in game.
 */
public abstract class Item implements TimestampedObject<Item>
{
    // Identification
    private int id;

    private ItemType type;
    private String name;
    private volatile boolean complete;
    private Map<Short, ItemModifier> itemModifierMap =
            new HashMap<Short, ItemModifier>();
    private Map<Short, ItemModifier> baseItemModifierMap =
            new HashMap<Short, ItemModifier>();
    private List<Short> itemEffectIdList = new LinkedList<Short>();

    // Position on map
    private short mapId;
    private short mapInstanceNo;
    private Position position;
    // Position in inventory
    private PhysicalSlotType equippedPosition;

    // Time-stamp
    private int ts;

    // Item attributes
    private short weight;
    private int value;

    /**
     * Default constructor.
     * @param id
     *            id of item
     * @param type
     *            item type
     */
    protected Item(int id, ItemType type)
    {
        this(id, type, null);
    }

    /**
     * Default constructor.
     * @param id
     *            id of item
     * @param name
     *            name of item
     * @param type
     *            item type
     */
    protected Item(int id, ItemType type, String name)
    {
        assert type != null;
        this.id = id;
        this.name = name;
        this.type = type;

        resetComplete();

        if (Log.isTraceEnabled())
        {
            Log.logger.trace("CREATED: Item with id: " + getId() + " name: "
                + getName());
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean compareTS(Item other)
    {
        return getNewestTS() < other.getNewestTS();
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
     * Returns last turn when this item was modified.
     * @return item time-stamp
     */
    @Override
    public int getNewestTS()
    {
        return ts;
    }

    /**
     * Return map of modifiers which affect base statistics
     * of this item.
     * @return map of modifiers which affect base statistics
     *         of this item.
     */
    public Map<Short, ItemModifier> getModifierMap()
    {
        return itemModifierMap;
    }

    /**
     * Return map of base modifiers which affect base
     * statistics of this item.
     * @return map of base modifiers which affect base
     *         statistics of this item.
     */
    public Map<Short, ItemModifier> getBaseModifierMap()
    {
        return baseItemModifierMap;
    }

    /**
     * Return list of effects which affect base statistics
     * of this item.
     * @return list of effects which affect base statistics
     *         of this item.
     */
    public List<Short> getEffectIdList()
    {
        return itemEffectIdList;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode()
    {
        return id;
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
     * Sets new item time-stamp.
     * @param ts
     *            new item time-stamp
     */
    public void setTS(int ts)
    {
        this.ts = ts;
    }

    /**
     * Returns mapId.
     * @return mapId
     */
    public short getMapId()
    {
        return mapId;
    }

    /**
     * Sets new value of mapId. Nulls equipped item
     * position.
     * @param mapId
     *            the mapId to set
     */
    public void setMapId(short mapId)
    {
        this.mapId = mapId;
        this.equippedPosition = null;
    }

    /**
     * Returns mapInstanceNo.
     * @return mapInstanceNo
     */
    public short getMapInstanceNo()
    {
        return mapInstanceNo;
    }

    /**
     * Sets new value of mapInstanceNo.
     * @param mapInstanceNo
     *            the mapInstanceNo to set
     */
    public void setMapInstanceNo(short mapInstanceNo)
    {
        this.mapInstanceNo = mapInstanceNo;
    }

    /**
     * Returns base physical positions in equipment where
     * item is placed.
     * @return equippedPosition
     */
    public PhysicalSlotType getEquippedPosition()
    {
        return equippedPosition;
    }

    /**
     * Sets new value of equippedPosition.
     * @param equippedPosition
     *            the equippedPosition to set
     */
    public void setEquippedPosition(PhysicalSlotType equippedPosition)
    {
        this.equippedPosition = equippedPosition;
    }

    /**
     * Returns position.
     * @return position
     */
    public Position getPosition()
    {
        return position;
    }

    /**
     * Sets new value of position.
     * @param position
     *            the position to set
     */
    public void setPosition(Position position)
    {
        this.position = position;
    }

    /**
     * Returns {@code true} when item is on ground.
     * Equivalent to {@code getEquippedPosition() == null}.
     * @return {@code true} when item is on ground.
     */
    public boolean isOnGround()
    {
        return equippedPosition == null;
    }

    /**
     * Returns {@code true} when item is equipped (is not on
     * the ground and not in the inventory}.
     * @return {@code true} when item is equipped.
     */
    public boolean isEquipped()
    {
        return !isOnGround()
            && !equippedPosition.equals(PhysicalSlotType.INVENTORY);
    }

    /**
     * Returns {@code true} when item is in inventory.
     * Equivalent to {@code !isOnGround() && !isEquipped()}.
     * @return {@code true} when item is in inventory.
     */
    public boolean isInInventory()
    {
        return !isOnGround()
            && equippedPosition.equals(PhysicalSlotType.INVENTORY);
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
     * Returns type.
     * @return type
     */
    public ItemType getType()
    {
        return type;
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
     * Returns value.
     * @return value
     */
    public int getValue()
    {
        return value;
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
     * Sets new value of value.
     * @param value
     *            the value to set
     */
    public void setValue(int value)
    {
        this.value = value;
    }

    /**
     * Returns kind.
     * @return kind
     */
    public abstract ItemKind getKind();

    /**
     * Assigns real type. Should be called when type arrived
     * and should replace stub.
     * <p>
     * Note to implementers: always remember to call {@code
     * super.assignType(type)} in subclasses.
     * @param type
     *            type to assign
     */
    public final void assignType(ItemType type)
    {
        assert this.type == type || this.type.isStub();
        assert type != null;
        assert !type.isStub();

        this.type = type;

        this.weight = type.getWeight();
        this.value = type.getValue();
    }

    /**
     * Returns {@code true} when item has assigned real type
     * (not stub). Should be called instead of {@code
     * getType().isStub()}.
     * @return {@code true} when item data is complete
     */
    public final boolean isComplete()
    {
        return complete;
    }

    /**
     * Checks if item has assigned real type (not stub).
     * Changes {@link #isComplete()} value. Should be called
     * in same thread as {@code assignType}.
     */
    public final void resetComplete()
    {
        this.complete = !type.isStub();
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return getType().getKind().toString() + " type: " + getType().getId()
            + " id: " + id;
    }
}
