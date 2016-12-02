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

import java.util.List;

import pl.org.minions.stigma.game.item.LogicalSlotType;

/**
 * Abstract class representing items which can be worn (both
 * weapons and armor).
 */
public abstract class EquipmentType extends ItemType
{
    private byte requiredStrength;
    private byte requiredAgility;
    private byte requiredWillpower;
    private byte requiredFinesse;

    private List<Short> requiredProficiency;

    /**
     * Default constructor for wearable item type.
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
     * @param requiredStrength
     *            strength required to wear item
     * @param requiredAgility
     *            agility required to wear item
     * @param requiredWillpower
     *            willpower required to wear item
     * @param requiredFinesse
     *            finesse required to wear item
     * @param equipementSlot
     *            slot type in equipment where actor is
     *            allowed to put item
     * @param requiredProficiency
     *            list of proficiencies required to equip
     *            item
     */
    protected EquipmentType(short id,
                            short weight,
                            int value,
                            String name,
                            String description,
                            String onGroundIcon,
                            String inventoryIcon,
                            byte requiredStrength,
                            byte requiredAgility,
                            byte requiredWillpower,
                            byte requiredFinesse,
                            LogicalSlotType equipementSlot,
                            List<Short> requiredProficiency)
    {
        super(id,
              weight,
              value,
              name,
              description,
              onGroundIcon,
              inventoryIcon,
              equipementSlot);
        this.requiredStrength = requiredStrength;
        this.requiredAgility = requiredAgility;
        this.requiredWillpower = requiredWillpower;
        this.requiredFinesse = requiredFinesse;
        this.requiredProficiency = requiredProficiency;
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
     * Sets new value of requiredStrength.
     * @param requiredStrength
     *            the requiredStrength to set
     */
    public void setRequiredStrength(byte requiredStrength)
    {
        this.requiredStrength = requiredStrength;
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
     * Sets new value of requiredAgility.
     * @param requiredAgility
     *            the requiredAgility to set
     */
    public void setRequiredAgility(byte requiredAgility)
    {
        this.requiredAgility = requiredAgility;
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
     * Sets new value of requiredWillpower.
     * @param requiredWillpower
     *            the requiredWillpower to set
     */
    public void setRequiredWillpower(byte requiredWillpower)
    {
        this.requiredWillpower = requiredWillpower;
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
     * Sets new value of requiredProficiency.
     * @param requiredProficiency
     *            the requiredProficiency to set
     */
    public void setRequiredProficiency(List<Short> requiredProficiency)
    {
        this.requiredProficiency = requiredProficiency;
    }

    /** {@inheritDoc} */
    @Override
    public abstract ItemKind getKind();

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        StringBuffer out = new StringBuffer();
        String space = " ";
        String newline = System.getProperty("line.separator");

        out.append(super.toString());

        out.append("requiredStrength: ")
           .append(requiredStrength)
           .append(newline);
        out.append("requiredAgility: ").append(requiredAgility).append(newline);
        out.append("requiredWillpower: ")
           .append(requiredWillpower)
           .append(newline);
        out.append("requiredFinesse: ").append(requiredFinesse).append(newline);

        if (requiredProficiency != null)
        {
            out.append("requiredProficiencies:").append(newline);
            for (Short p : requiredProficiency)
            {
                out.append(p.toString()).append(space);
            }
            out.append(newline);
        }

        return out.toString();
    }
}
