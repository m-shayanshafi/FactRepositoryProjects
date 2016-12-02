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
package pl.org.minions.stigma.databases.item.client;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;

import pl.org.minions.stigma.databases.item.ModifierDB;
import pl.org.minions.stigma.databases.xml.client.SimpleXmlAsyncDB;
import pl.org.minions.stigma.game.item.modifier.ArmorModifier;
import pl.org.minions.stigma.game.item.modifier.WeaponModifier;

/**
 * Implementation of asynchronous modifier database.
 */
public class ModifierDBAsync implements ModifierDB
{
    private ArmorModifierDBAsync armorModifierDB;
    private WeaponModifierDBAsync weaponModifierDB;

    /**
     * Simple constructor.
     * <p>
     * Immediately requests loading resources.
     * @param uri
     *            URI to resource root
     */
    public ModifierDBAsync(URI uri)
    {
        this.armorModifierDB = new ArmorModifierDBAsync(uri);
        this.weaponModifierDB = new WeaponModifierDBAsync(uri);
    }

    /**
     * Default constructor.
     * @param armorModifierDB
     *            armor modifier DB
     * @param weaponModifierDB
     *            weapon modifier DB
     */
    public ModifierDBAsync(ArmorModifierDBAsync armorModifierDB,
                           WeaponModifierDBAsync weaponModifierDB)
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

    /**
     * Returns all simple xml databases included in this
     * database.
     * @return collection of sub-databases
     */
    public Collection<SimpleXmlAsyncDB<?, ?, ?>> getSubDBs()
    {
        final Collection<SimpleXmlAsyncDB<?, ?, ?>> result =
                new ArrayList<SimpleXmlAsyncDB<?, ?, ?>>(2);

        result.add(armorModifierDB);
        result.add(weaponModifierDB);
        return result;
    }

}
