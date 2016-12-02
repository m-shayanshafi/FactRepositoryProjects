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

import java.util.EnumMap;
import java.util.List;

import pl.org.minions.stigma.game.actor.AttackType;
import pl.org.minions.stigma.game.actor.DamageType;
import pl.org.minions.stigma.game.item.LogicalSlotType;

/**
 * Class representing weapons in game.
 */
public class WeaponType extends EquipmentType
{
    private EnumMap<AttackType, WeaponAttack> attackMap;
    private List<Short> baseEffects;
    private List<Short> baseModifiers;

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
     * @param damageMap
     *            map containing information about damage
     *            caused by weapon
     * @param baseEffects
     *            list of effects added to weapon
     * @param baseMofifiers
     *            list of modifiers added to weapon
     */
    public WeaponType(short id,
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
                      List<Short> requiredProficiency,
                      EnumMap<AttackType, WeaponAttack> damageMap,
                      List<Short> baseEffects,
                      List<Short> baseMofifiers)
    {
        super(id,
              weight,
              value,
              name,
              description,
              onGroundIcon,
              inventoryIcon,
              requiredStrength,
              requiredAgility,
              requiredWillpower,
              requiredFinesse,
              equipementSlot,
              requiredProficiency);
        this.baseEffects = baseEffects;
        this.baseModifiers = baseMofifiers;
        this.attackMap = damageMap;
    }

    /** {@inheritDoc} */
    @Override
    public ItemKind getKind()
    {
        return ItemKind.WEAPON;
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
     * Returns baseEffects.
     * @return baseEffects
     */
    public List<Short> getBaseEffects()
    {
        return baseEffects;
    }

    /**
     * Returns baseModifiers.
     * @return baseModifiers
     */
    public List<Short> getBaseModifiers()
    {
        return baseModifiers;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        StringBuffer out = new StringBuffer();
        String newline = System.getProperty("line.separator");
        String space = " ";

        out.append(super.toString());

        out.append("attack:").append(newline);
        for (DamageType type : DamageType.getValuesArray())
        {
            if (attackMap.containsKey(type))
            {
                out.append("type: ").append(type.name()).append(newline);
                out.append(attackMap.get(type).toString());
            }
        }

        out.append("base effects:").append(newline);
        for (Short effect : baseEffects)
        {
            out.append(effect.toString()).append(space);
        }
        out.append(newline);

        out.append("base modifiers:").append(newline);
        for (Short modifier : baseModifiers)
        {
            out.append(modifier.toString()).append(space);
        }
        out.append(newline);

        return out.toString();
    }

    /**
     * Returns statistics describing specific weapon damage.
     * @param dt
     *            damage type
     * @return statistics describing specific weapon damage
     */
    public WeaponAttack getDamage(DamageType dt)
    {
        return attackMap.get(dt);
    }
}
