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

import pl.org.minions.stigma.game.item.type.ArmorType;
import pl.org.minions.stigma.game.item.type.ItemType;
import pl.org.minions.stigma.game.item.type.OtherType;
import pl.org.minions.stigma.game.item.type.WeaponType;
import pl.org.minions.stigma.game.item.type.ItemType.ItemKind;

/**
 * Interface for Items Type database.
 */
public interface ItemTypeDB
{
    /**
     * Name of items database root directory.
     */
    String MAIN_DB_DIR = "items";
    /**
     * Name of armor database sub-directory.
     */
    String ARMOR_DB_DIR = "armors";
    /**
     * Name of weapon database sub-directory.
     */
    String WEAPON_DB_DIR = "weapons";
    /**
     * Name of other items database sub-directory.
     */
    String OTHER_DB_DIR = "others";

    String ARMOR_FILE_PREFIX = "armor";
    String WEAPON_FILE_PREFIX = "weapon";
    String OTHER_FILE_PREFIX = "other";

    /**
     * Returns ItemType of selected kind with selected id.
     * @param id
     *            id of itemType
     * @param kind
     *            kind of itemType
     * @return ItemType
     */
    ItemType getItemType(short id, ItemKind kind);

    /**
     * Returns ArmorType with selected id.
     * @param id
     *            id of armor type
     * @return ArmorType.
     */
    ArmorType getArmorType(short id);

    /**
     * Returns WeaponType with selected id.
     * @param id
     *            id of weapon type
     * @return WeaponType.
     */
    WeaponType getWeaponType(short id);

    /**
     * Returns OtherType with selected id.
     * @param id
     *            id of other type
     * @return OtherType.
     */
    OtherType getOtherType(short id);
}
