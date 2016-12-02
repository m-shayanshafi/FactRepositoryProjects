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

import pl.org.minions.stigma.game.item.modifier.ArmorModifier;
import pl.org.minions.stigma.game.item.modifier.WeaponModifier;

/**
 * Interface for modifier database.
 */
public interface ModifierDB
{
    /**
     * Name of items database root directory.
     */
    String MAIN_DB_DIR = "items";
    /**
     * Name of modifiers database sub-directory.
     */
    String MODIFIERS_DB_DIR = "modifiers";

    /**
     * Name of armor modifiers files.
     */
    String ARMOR_MODIFIER_FILE_PREFIX = "armormod";

    /**
     * Name of weapon modifiers files.
     */
    String WEAPON_MODIFIER_FILE_PREFIX = "weaponmod";

    /**
     * Returns armor modifier with selected id.
     * @param id
     *            id of armor modifier
     * @return armor modifier
     */
    ArmorModifier getArmorModifier(short id);

    /**
     * Returns weapon modifier with selected id.
     * @param id
     *            id of weapon modifier
     * @return weapon modifier
     */
    WeaponModifier getWeaponModifier(short id);

}
