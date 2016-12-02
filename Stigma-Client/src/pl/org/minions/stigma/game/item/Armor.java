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

import java.util.Map;
import java.util.TreeMap;

import pl.org.minions.stigma.game.actor.DamageType;
import pl.org.minions.stigma.game.actor.Resistance;
import pl.org.minions.stigma.game.item.modifier.ArmorModifier;
import pl.org.minions.stigma.game.item.type.ArmorType;
import pl.org.minions.stigma.game.item.type.ItemType.ItemKind;
import pl.org.minions.utils.logger.Log;

/**
 * Main class which represents armor items in game.
 */
public class Armor extends Equipment implements ModifiableItem<ArmorModifier>
{
    private Map<DamageType, Resistance> resistances;

    /**
     * Default constructor.
     * @param id
     *            id of item
     * @param type
     *            item type
     */
    public Armor(int id, ArmorType type)
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
    public Armor(int id, ArmorType type, String name)
    {
        super(id, type, name);
        if (!type.isStub())
            assignType(type);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ItemKind getKind()
    {
        return ItemKind.ARMOR;
    }

    /**
     * Returns resistances.
     * @return resistances
     */
    public Map<DamageType, Resistance> getResistances()
    {
        return resistances;
    }

    /**
     * Sets new value of resistances.
     * @param resistances
     *            the resistances to set
     */
    public void setResistances(Map<DamageType, Resistance> resistances)
    {
        this.resistances = resistances;
    }

    /** {@inheritDoc} */
    @Override
    public boolean removeModifier(ArmorModifier modifier)
    {
        if (modifier == null)
        {
            Log.logger.error("Unable to remove modifier. WeaponModifier is null.");
            return false;
        }

        if (this.getType() == null)
        {
            Log.logger.error("Unable to remove modifier from item stub - ItemType is null.");
            return false;
        }

        if (!this.getType().getKind().equals(ItemKind.WEAPON))
        {
            Log.logger.error("Unable to remove modifier. Wrong ItemType for item id: "
                + this.getId());
            return false;
        }

        if (!super.getModifierMap().containsValue(modifier))
        {
            if (Log.isTraceEnabled())
            {
                Log.logger.trace("Item: " + getId()
                    + " does not contain modifier: " + modifier.getId());
            }
            return false;
        }

        int mult =
                modifier.getWeightMultiplier() == null ? 1
                                                      : modifier.getWeightMultiplier();
        int mod =
                modifier.getWeightModifier() == null ? 0
                                                    : modifier.getWeightModifier();
        this.setWeight((short) (this.getWeight() - (mult - 1)
            * this.getType().getWeight() - mod));

        mult =
                modifier.getValueMultiplier() == null ? 1
                                                     : modifier.getValueMultiplier();
        mod =
                modifier.getValueModifier() == null ? 0
                                                   : modifier.getValueModifier();
        this.setValue(this.getValue() - (mult - 1) * this.getType().getValue()
            - mod);

        for (DamageType dt : this.getResistances().keySet())
        {
            Resistance r = this.getResistances().get(dt);
            mult =
                    modifier.getResistanceMultipliers() == null
                        || modifier.getResistanceMultipliers().get(dt) == null ? 1
                                                                              : modifier.getResistanceMultipliers()
                                                                                        .get(dt);
            mod =
                    modifier.getResistanceModifiers() == null
                        || modifier.getResistanceModifiers().get(dt) == null ? 1
                                                                            : modifier.getResistanceModifiers()
                                                                                      .get(dt);
            r.setRelative((byte) (r.getRelative() - (mult - 1)
                * this.getType().getResistance(dt).getRelative() - mod));
        }

        if (super.getModifierMap().remove(modifier.getCategoryId()) == null)
        {
            Log.logger.error("Error during removing modifier. Category not found.");
        }
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public boolean addModifier(ArmorModifier modifier)
    {
        if (modifier == null)
        {
            Log.logger.error("Unable to add modifier. WeaponModifier is null.");
            return false;
        }

        if (this.getType() == null)
        {
            Log.logger.error("Unable to add modifier from item stub - ItemType is null.");
            return false;
        }

        if (!this.getType().getKind().equals(ItemKind.WEAPON))
        {
            Log.logger.error("Unable to add modifier. Wrong ItemType for item id: "
                + this.getId());
            return false;
        }

        //look whether item contains modifiers with same category and if it does
        //cancel adding it
        if (super.getModifierMap().containsKey(modifier.getCategoryId()))
        {
            if (Log.isTraceEnabled())
            {
                Log.logger.trace("Unable to add modifier: " + modifier.getId()
                    + " to item: " + getId());
            }
            return false;
        }

        int mult =
                modifier.getWeightMultiplier() == null ? 1
                                                      : modifier.getWeightMultiplier();
        int mod =
                modifier.getWeightModifier() == null ? 0
                                                    : modifier.getWeightModifier();
        this.setWeight((short) (this.getWeight() + (mult - 1)
            * this.getType().getWeight() + mod));

        mult =
                modifier.getValueMultiplier() == null ? 1
                                                     : modifier.getValueMultiplier();
        mod =
                modifier.getValueModifier() == null ? 0
                                                   : modifier.getValueModifier();
        this.setValue(this.getValue() + (mult - 1) * this.getType().getValue()
            + mod);

        for (DamageType dt : this.getResistances().keySet())
        {
            Resistance r = this.getResistances().get(dt);
            mult =
                    modifier.getResistanceMultipliers() == null
                        || modifier.getResistanceMultipliers().get(dt) == null ? 1
                                                                              : modifier.getResistanceMultipliers()
                                                                                        .get(dt);
            mod =
                    modifier.getResistanceModifiers() == null
                        || modifier.getResistanceModifiers().get(dt) == null ? 1
                                                                            : modifier.getResistanceModifiers()
                                                                                      .get(dt);
            r.setRelative((byte) (r.getRelative() + (mult - 1)
                * this.getType().getResistance(dt).getRelative() + mod));
        }

        this.getModifierMap().put(modifier.getCategoryId(), modifier);

        return true;
    }

    /** {@inheritDoc} */
    @Override
    public ArmorType getType()
    {
        return (ArmorType) super.getType();
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
    public final void assignType(ArmorType type)
    {
        super.assignType(type);
        this.resistances =
                new TreeMap<DamageType, Resistance>(type.getResistances());
    }

}
