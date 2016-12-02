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

import java.util.EnumMap;

import pl.org.minions.stigma.game.actor.AttackType;
import pl.org.minions.stigma.game.item.modifier.WeaponModifier;
import pl.org.minions.stigma.game.item.type.WeaponAttack;
import pl.org.minions.stigma.game.item.type.WeaponType;
import pl.org.minions.stigma.game.item.type.ItemType.ItemKind;
import pl.org.minions.utils.logger.Log;

/**
 * Main class representing weapons in game.
 */
public class Weapon extends Equipment implements ModifiableItem<WeaponModifier>
{
    private EnumMap<AttackType, WeaponAttack> attackMap;

    /**
     * Default constructor.
     * @param id
     *            id of item
     * @param type
     *            item type
     */
    public Weapon(int id, WeaponType type)
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
    public Weapon(int id, WeaponType type, String name)
    {
        super(id, type, name);
        if (!type.isStub())
            assignType(type);
    }

    /**
     * Returns attackMap.
     * @return attackMap
     */
    public EnumMap<AttackType, WeaponAttack> getAttackMap()
    {
        return attackMap;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ItemKind getKind()
    {
        return ItemKind.WEAPON;
    }

    /**
     * Sets new value of attackMap.
     * @param attackMap
     *            the attackMap to set
     */
    public void setAttackMap(EnumMap<AttackType, WeaponAttack> attackMap)
    {
        this.attackMap = attackMap;
    }

    /** {@inheritDoc} */
    @Override
    public WeaponType getType()
    {
        return (WeaponType) super.getType();
    }

    /** {@inheritDoc} */
    @Override
    public boolean removeModifier(WeaponModifier modifier)
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

        for (AttackType at : this.getAttackMap().keySet())
        {
            WeaponAttack wa = this.getAttackMap().get(at);

            mult =
                    modifier.getBaseDamageMultipier() == null ? 1
                                                             : modifier.getBaseDamageMultipier();
            mod = modifier.getBaseDamageModifier() == null ? 0

            : modifier.getBaseDamageModifier();
            WeaponAttack waType = this.getType().getAttackMap().get(wa);
            wa.setBaseDamage((short) (wa.getBaseDamage() - (mult - 1)
                * (waType == null ? 0 : waType.getBaseDamage()) - mod));
        }

        if (super.getModifierMap().remove(modifier.getCategoryId()) == null)
        {
            Log.logger.error("Error during removing modifier. Category not found.");
        }

        return true;
    }

    /** {@inheritDoc} */
    @Override
    public boolean addModifier(WeaponModifier modifier)
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

        for (AttackType at : this.getAttackMap().keySet())
        {
            WeaponAttack wa = this.getAttackMap().get(at);

            mult =
                    modifier.getBaseDamageMultipier() == null ? 1
                                                             : modifier.getBaseDamageMultipier();
            mod =
                    modifier.getBaseDamageModifier() == null ? 0
                                                            : modifier.getBaseDamageModifier();

            WeaponAttack waType = this.getType().getAttackMap().get(wa);
            wa.setBaseDamage((short) (wa.getBaseDamage() + (mult - 1)
                * (waType == null ? 0 : waType.getBaseDamage()) + mod));
        }

        this.getModifierMap().put(modifier.getCategoryId(), modifier);

        return true;
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
    public final void assignType(WeaponType type)
    {
        super.assignType(type);
        this.attackMap =
                new EnumMap<AttackType, WeaponAttack>(type.getAttackMap());
    }

}
