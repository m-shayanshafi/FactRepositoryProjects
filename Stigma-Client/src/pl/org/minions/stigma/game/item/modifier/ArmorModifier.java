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

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Set;

import pl.org.minions.stigma.game.actor.DamageType;

/**
 * Class representing modifications added to armor (for
 * example: extended durability).
 */
public class ArmorModifier extends ItemModifier
{
    private Set<Short> proficiecyIds;

    private Integer valueMultiplier;
    private Integer valueModifier;
    private Integer weightMultiplier;
    private Integer weightModifier;

    private EnumMap<DamageType, Integer> resistanceMultipliers;
    private EnumMap<DamageType, Integer> resistanceModifiers;

    /**
     * Constructor used by JAXB.
     */
    public ArmorModifier()
    {
        super();
        this.proficiecyIds = new HashSet<Short>();
        this.resistanceModifiers =
                new EnumMap<DamageType, Integer>(DamageType.class);
        this.resistanceMultipliers =
                new EnumMap<DamageType, Integer>(DamageType.class);
    }

    /**
     * Default constructor.
     * @param id
     *            id of item modifier
     * @param name
     *            name of modifier
     * @param modifierCategoryId
     *            id of category of this modifier
     */
    public ArmorModifier(short id, String name, short modifierCategoryId)
    {
        super(id, name, modifierCategoryId);
        this.proficiecyIds = new HashSet<Short>();
        this.resistanceModifiers =
                new EnumMap<DamageType, Integer>(DamageType.class);
        this.resistanceMultipliers =
                new EnumMap<DamageType, Integer>(DamageType.class);
    }

    /**
     * Returns proficiecyIds.
     * @return proficiecyIds
     */
    public Set<Short> getProficiecyIds()
    {
        return proficiecyIds;
    }

    /**
     * Returns valueMultiplier.
     * @return valueMultiplier
     */
    public Integer getValueMultiplier()
    {
        return valueMultiplier;
    }

    /**
     * Returns valueModifier.
     * @return valueModifier
     */
    public Integer getValueModifier()
    {
        return valueModifier;
    }

    /**
     * Returns weightMultiplier.
     * @return weightMultiplier
     */
    public Integer getWeightMultiplier()
    {
        return weightMultiplier;
    }

    /**
     * Returns weightModifier.
     * @return weightModifier
     */
    public Integer getWeightModifier()
    {
        return weightModifier;
    }

    /**
     * Sets new value of proficiecyIds.
     * @param proficiecyIds
     *            the proficiecyIds to set
     */
    public void setProficiecyIds(Set<Short> proficiecyIds)
    {
        this.proficiecyIds = proficiecyIds;
    }

    /**
     * Sets new value of valueMultiplier.
     * @param valueMultiplier
     *            the valueMultiplier to set
     */
    public void setValueMultiplier(Integer valueMultiplier)
    {
        this.valueMultiplier = valueMultiplier;
    }

    /**
     * Sets new value of valueModifier.
     * @param valueModifier
     *            the valueModifier to set
     */
    public void setValueModifier(Integer valueModifier)
    {
        this.valueModifier = valueModifier;
    }

    /**
     * Sets new value of weightMultiplier.
     * @param weightMultiplier
     *            the weightMultiplier to set
     */
    public void setWeightMultiplier(Integer weightMultiplier)
    {
        this.weightMultiplier = weightMultiplier;
    }

    /**
     * Sets new value of weightModifier.
     * @param weightModifier
     *            the weightModifier to set
     */
    public void setWeightModifier(Integer weightModifier)
    {
        this.weightModifier = weightModifier;
    }

    /**
     * Returns resistanceMultipliers.
     * @return resistanceMultipliers
     */
    public EnumMap<DamageType, Integer> getResistanceMultipliers()
    {
        return resistanceMultipliers;
    }

    /**
     * Returns resistanceModifiers.
     * @return resistanceModifiers
     */
    public EnumMap<DamageType, Integer> getResistanceModifiers()
    {
        return resistanceModifiers;
    }

    /**
     * Sets new value of resistanceMultipliers.
     * @param resistanceMultipliers
     *            the resistanceMultipliers to set
     */
    public void setResistanceMultipliers(EnumMap<DamageType, Integer> resistanceMultipliers)
    {
        this.resistanceMultipliers = resistanceMultipliers;
    }

    /**
     * Sets new value of resistanceModifiers.
     * @param resistanceModifiers
     *            the resistanceModifiers to set
     */
    public void setResistanceModifiers(EnumMap<DamageType, Integer> resistanceModifiers)
    {
        this.resistanceModifiers = resistanceModifiers;
    }

    /** {@inheritDoc} */
    @Override
    public ModifierItemType getModifierItemType()
    {
        return ModifierItemType.ARMOR_MODIFIER;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        StringBuffer out = new StringBuffer();
        String newline = System.getProperty("line.separator");

        out.append("[")
           .append(getId())
           .append("] ")
           .append(getName())
           .append(newline);
        if (valueModifier != null)
            out.append("valueModifier: ").append(valueModifier).append(newline);
        if (valueMultiplier != null)
            out.append("valueMultiplier: ")
               .append(valueMultiplier)
               .append(newline);
        if (weightModifier != null)
            out.append("weightModifier: ")
               .append(weightModifier)
               .append(newline);
        if (weightMultiplier != null)
            out.append("weightMultiplier: ")
               .append(weightMultiplier)
               .append(newline);

        return out.toString();
    }
}
