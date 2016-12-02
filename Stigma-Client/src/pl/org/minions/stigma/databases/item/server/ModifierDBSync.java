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
package pl.org.minions.stigma.databases.item.server;

import java.net.URI;

import pl.org.minions.stigma.databases.item.ModifierDB;
import pl.org.minions.stigma.game.item.modifier.ArmorModifier;
import pl.org.minions.stigma.game.item.modifier.WeaponModifier;

/**
 * Implementation of synchronous modifier database.
 */
public class ModifierDBSync implements ModifierDB
{
    private ArmorModifierDBSync armorModifierDB;
    private WeaponModifierDBSync weaponModifierDB;

    /**
     * Simple constructor.
     * @param uri
     *            uri to resource root
     */
    public ModifierDBSync(URI uri)
    {
        this.armorModifierDB = new ArmorModifierDBSync(uri);
        this.weaponModifierDB = new WeaponModifierDBSync(uri);
    }

    /**
     * Default constructor.
     * @param armorModifierDB
     *            armor modifier DB
     * @param weaponModifierDB
     *            weapon modifier DB
     */
    public ModifierDBSync(ArmorModifierDBSync armorModifierDB,
                          WeaponModifierDBSync weaponModifierDB)
    {
        this.armorModifierDB = armorModifierDB;
        this.weaponModifierDB = weaponModifierDB;
    }

    /** {@inheritDoc} */
    @Override
    public ArmorModifier getArmorModifier(short id)
    {
        return armorModifierDB.getById(id);
    }

    /** {@inheritDoc} */
    @Override
    public WeaponModifier getWeaponModifier(short id)
    {
        return weaponModifierDB.getById(id);
    }

}
