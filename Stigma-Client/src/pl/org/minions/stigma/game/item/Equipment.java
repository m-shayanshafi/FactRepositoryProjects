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

import java.util.List;

import pl.org.minions.stigma.game.item.type.EquipmentType;

/**
 * Class representing all wearable items.
 */
public abstract class Equipment extends Item
{
    private byte requiredStrength;
    private byte requiredAgility;
    private byte requiredWillpower;
    private byte requiredFinesse;

    private List<Short> requiredProficiency;

    /**
     * Default constructor.
     * @param id
     *            id of item
     * @param type
     *            item type
     */
    protected Equipment(int id, EquipmentType type)
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
    protected Equipment(int id, EquipmentType type, String name)
    {
        super(id, type, name);
    }

    /** {@inheritDoc} */
    @Override
    public EquipmentType getType()
    {
        return (EquipmentType) super.getType();
    }

    /**
     * Returns requiredStrength.
     * @return requiredStrength
     */
    public byte getRequiredStrength()
    {
        return requiredStrength;
    }

    /**
     * Returns requiredAgility.
     * @return requiredAgility
     */
    public byte getRequiredAgility()
    {
        return requiredAgility;
    }

    /**
     * Returns requiredWillpower.
     * @return requiredWillpower
     */
    public byte getRequiredWillpower()
    {
        return requiredWillpower;
    }

    /**
     * Returns requiredFinesse.
     * @return requiredFinesse
     */
    public byte getRequiredFinesse()
    {
        return requiredFinesse;
    }

    /**
     * Sets new value of requiredStrength.
     * @param requiredStrength
     *            the requiredStrength to set
     */
    public void setRequiredStrength(byte requiredStrength)
    {
        this.requiredStrength = requiredStrength;
    }

    /**
     * Sets new value of requiredAgility.
     * @param requiredAgility
     *            the requiredAgility to set
     */
    public void setRequiredAgility(byte requiredAgility)
    {
        this.requiredAgility = requiredAgility;
    }

    /**
     * Sets new value of requiredWillpower.
     * @param requiredWillpower
     *            the requiredWillpower to set
     */
    public void setRequiredWillpower(byte requiredWillpower)
    {
        this.requiredWillpower = requiredWillpower;
    }

    /**
     * Sets new value of requiredFinesse.
     * @param requiredFinesse
     *            the requiredFinesse to set
     */
    public void setRequiredFinesse(byte requiredFinesse)
    {
        this.requiredFinesse = requiredFinesse;
    }

    /**
     * Returns requiredProficiency.
     * @return requiredProficiency
     */
    public List<Short> getRequiredProficiency()
    {
        return requiredProficiency;
    }

    /**
     * Assigns real type. Should be called when type arrived
     * and should replace stub.
     * <p>
     * Note to implementers: always remember to call {@code
     * super.assignType(type)} in subclasses.
     * @param type
     *            type to assign
     */
    public final void assignType(EquipmentType type)
    {
        super.assignType(type);
        this.requiredStrength = type.getRequiredStrength();
        this.requiredAgility = type.getRequiredAgility();
        this.requiredWillpower = type.getRequiredWillpower();
        this.requiredFinesse = type.getRequiredFinesse();
        this.requiredProficiency = type.getRequiredProficiency();
    }

}
