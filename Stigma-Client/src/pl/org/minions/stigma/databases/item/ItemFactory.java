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
package pl.org.minions.stigma.databases.item;

import java.util.Collections;
import java.util.List;

import pl.org.minions.stigma.game.item.Armor;
import pl.org.minions.stigma.game.item.Item;
import pl.org.minions.stigma.game.item.OtherItem;
import pl.org.minions.stigma.game.item.Weapon;
import pl.org.minions.stigma.game.item.type.ArmorType;
import pl.org.minions.stigma.game.item.type.OtherType;
import pl.org.minions.stigma.game.item.type.WeaponType;
import pl.org.minions.stigma.game.item.type.ItemType.ItemKind;
import pl.org.minions.stigma.game.item.type.stubs.ArmorTypeStub;
import pl.org.minions.stigma.game.item.type.stubs.OtherTypeStub;
import pl.org.minions.stigma.game.item.type.stubs.WeaponTypeStub;
import pl.org.minions.utils.logger.Log;

/**
 * Utility class for building {@link Item} instances. Offers
 * possibility to create item from type everywhere.
 */
public class ItemFactory
{
    private static ItemFactory instance;

    private static final List<Short> EMPTY_LIST = Collections.emptyList();

    private ItemTypeDB typeDatabase;
    private ModifierDB modifierDatabase;

    /**
     * Protected constructor.
     * @param typeDatabase
     *            database of types
     * @param modifierDatabase
     *            modifier database
     */
    protected ItemFactory(ItemTypeDB typeDatabase, ModifierDB modifierDatabase)
    {
        this.typeDatabase = typeDatabase;
        this.modifierDatabase = modifierDatabase;
    }

    /**
     * Initializes ItemFactory.
     * @param typeDatabase
     *            database of types
     * @param modifierDatabase
     *            database of modifiers
     */
    public static void initialize(ItemTypeDB typeDatabase,
                                  ModifierDB modifierDatabase)
    {
        instance = new ItemFactory(typeDatabase, modifierDatabase);
    }

    /**
     * Returns item created from selected type and kind.
     * @param id
     *            item id
     * @param typeId
     *            id of item type
     * @param kind
     *            kind of item type
     * @return item
     */
    public Item getItem(int id, short typeId, ItemKind kind)
    {
        return getItem(id, typeId, kind, EMPTY_LIST);
    }

    /**
     * Returns item created from selected type and kind with
     * list of dynamic modifiers.
     * @param id
     *            item id
     * @param typeId
     *            id of item type
     * @param kind
     *            kind of item type
     * @param dynamicModifierList
     *            list of dynamic modifiers
     * @return item
     */
    public Item getItem(int id,
                        short typeId,
                        ItemKind kind,
                        List<Short> dynamicModifierList)
    {
        switch (kind)
        {
            case ARMOR:
                ArmorType atype = typeDatabase.getArmorType(typeId);
                if (atype == null)
                    break;

                //Armor armor = new Armor(id, atype);
                //TODO add item naming convention here
                Armor armor = new Armor(id, atype, atype.getName());

                if (atype.isStub())
                {
                    ArmorTypeStub stub = (ArmorTypeStub) atype;
                    stub.assignItem(armor, dynamicModifierList);
                    return armor;
                }

                addArmorBaseModifier(atype, armor);
                addArmorModifier(armor, dynamicModifierList);
                return armor;
            case WEAPON:
                WeaponType wtype = typeDatabase.getWeaponType(typeId);
                if (wtype == null)
                    break;

                //Weapon weapon = new Weapon(id, wtype);
                //TODO add item naming convention here
                Weapon weapon = new Weapon(id, wtype, wtype.getName());

                if (wtype.isStub())
                {
                    WeaponTypeStub stub = (WeaponTypeStub) wtype;
                    stub.assignItem(weapon, dynamicModifierList);
                    return weapon;
                }

                addWeaponBaseModifier(wtype, weapon);
                addWeaponModifier(weapon, dynamicModifierList);
                return weapon;
            case OTHER:
                if (dynamicModifierList.size() > 0)
                    Log.logger.warn("Trying to create OtherItem with modifiers.");

                OtherType otype = typeDatabase.getOtherType(typeId);
                if (otype == null)
                    break;

                //OtherItem other = new OtherItem(id, otype);
                //TODO add item naming convention here
                OtherItem other = new OtherItem(id, otype, otype.getName());

                if (otype.isStub())
                {
                    OtherTypeStub stub = (OtherTypeStub) otype;
                    stub.assignItem(other);
                }
                return other;

            default:
                Log.logger.error("Cannot return item - unknown ItemKind.");
                return null;
        }

        Log.logger.error("Bad type id: " + typeId + " (kind: " + kind + ")");
        return null;
    }

    /**
     * Applies modifier to item.
     * @param item
     *            item to add modifier to
     * @param modifiers
     *            list of modifiers
     */
    public void applyModifiers(Item item, List<Short> modifiers)
    {
        switch (item.getKind())
        {
            case ARMOR:
                Armor armor = (Armor) item;
                for (Short s : modifiers)
                {
                    if (!armor.addModifier(modifierDatabase.getArmorModifier(s)))
                        Log.logger.warn("Unable to add armor modifier: " + s
                            + " to armor item with type: "
                            + armor.getType().getId());
                }
                break;
            case WEAPON:
                Weapon weapon = (Weapon) item;
                for (Short s : modifiers)
                {
                    if (!weapon.addModifier(modifierDatabase.getWeaponModifier(s)))
                        Log.logger.warn("Unable to add weapon modifier: " + s
                            + " to weapon item with type: "
                            + weapon.getType().getId());
                }
                break;
            case OTHER:
            default:
                Log.logger.error("Unable to add modifiers to other or unknown item kind.");
                break;
        }
    }

    /**
     * Assigns real type to incomplete weapon.
     * @param weapon
     *            weapon to be completed
     * @param weaponType
     *            real type
     * @param dynamicModifiers
     *            list of dynamic modifiers which should be
     *            applied to weapon
     */
    public void assignType(Weapon weapon,
                           WeaponType weaponType,
                           List<Short> dynamicModifiers)
    {
        assert weapon != null;
        assert weaponType != null;
        assert !weapon.isComplete();
        assert !weaponType.isStub();

        weapon.assignType(weaponType);
        addWeaponBaseModifier(weaponType, weapon);
        addWeaponModifier(weapon, dynamicModifiers);

        weapon.resetComplete();
    }

    /**
     * Assigns real type to incomplete armor.
     * @param armor
     *            armor to be completed
     * @param armorType
     *            real type
     * @param dynamicModifiers
     *            list of dynamic modifiers which should be
     *            applied to armor
     */
    public void assignType(Armor armor,
                           ArmorType armorType,
                           List<Short> dynamicModifiers)

    {
        assert armor != null;
        assert armorType != null;
        assert !armor.isComplete();
        assert !armorType.isStub();

        armor.assignType(armorType);
        addArmorBaseModifier(armorType, armor);
        addArmorModifier(armor, dynamicModifiers);

        armor.resetComplete();
    }

    /**
     * Assigns real type to incomplete item.
     * @param other
     *            item to be completed
     * @param otherType
     *            real type
     */
    public void assignType(OtherItem other, OtherType otherType)
    {
        assert other != null;
        assert otherType != null;
        assert !other.isComplete();
        assert !otherType.isStub();

        other.assignType(otherType);

        other.resetComplete();
    }

    private void addArmorBaseModifier(ArmorType atype, Armor armor)
    {
        addArmorModifier(armor, atype.getBaseModifiers());
    }

    private void addWeaponBaseModifier(WeaponType wtype, Weapon weapon)
    {
        addWeaponModifier(weapon, wtype.getBaseModifiers());
    }

    private void addWeaponModifier(Weapon weapon, List<Short> amods)
    {
        for (Short s : amods)
        {
            if (!weapon.addModifier(modifierDatabase.getWeaponModifier(s)))
                Log.logger.warn("Weapon creation error. Unable add modifier: "
                    + s + " to weapon with type: " + weapon.getType());
        }
    }

    private void addArmorModifier(Armor armor, List<Short> amods)
    {
        for (Short s : amods)
        {
            if (!armor.addModifier(modifierDatabase.getArmorModifier(s)))
                Log.logger.warn("Armor creation error. Unable add modifier: "
                    + s + " to armor with type: " + armor.getType());
        }
    }

    /**
     * Returns instance of item factory or null if factory
     * was not initialized.
     * @return instance of item factory
     */
    public static ItemFactory getInstance()
    {
        if (instance == null)
            Log.logger.fatal("Item factory not initialized!");
        return instance;
    }
}
