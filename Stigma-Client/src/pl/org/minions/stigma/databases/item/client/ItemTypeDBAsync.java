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

import pl.org.minions.stigma.databases.item.ItemTypeDB;
import pl.org.minions.stigma.databases.xml.client.XmlAsyncDB;
import pl.org.minions.stigma.databases.xml.client.XmlCompletionObserver;
import pl.org.minions.stigma.game.item.type.ArmorType;
import pl.org.minions.stigma.game.item.type.ItemType;
import pl.org.minions.stigma.game.item.type.OtherType;
import pl.org.minions.stigma.game.item.type.WeaponType;
import pl.org.minions.stigma.game.item.type.ItemType.ItemKind;
import pl.org.minions.utils.logger.Log;

/**
 * Asynchronous implementation of database for item types.
 */
public class ItemTypeDBAsync implements ItemTypeDB
{
    private WeaponTypeDBAsync weaponDB;
    private ArmorTypeDBAsync armorDB;
    private OtherTypeDBAsync otherDB;

    /**
     * Simple constructor.
     * @param uri
     *            URI to resource root
     */
    public ItemTypeDBAsync(URI uri)
    {
        this.armorDB = new ArmorTypeDBAsync(uri);
        this.weaponDB = new WeaponTypeDBAsync(uri);
        this.otherDB = new OtherTypeDBAsync(uri);
    }

    /**
     * Default constructor.
     * @param weaponDB
     *            weapons database
     * @param armorDB
     *            armor database
     * @param otherDB
     *            other items database
     */
    public ItemTypeDBAsync(WeaponTypeDBAsync weaponDB,
                           ArmorTypeDBAsync armorDB,
                           OtherTypeDBAsync otherDB)
    {
        this.armorDB = armorDB;
        this.weaponDB = weaponDB;
        this.otherDB = otherDB;
    }

    /** {@inheritDoc} */
    @Override
    public ArmorType getArmorType(short id)
    {
        return armorDB.getById(id);
    }

    /** {@inheritDoc} */
    @Override
    public ItemType getItemType(short id, ItemKind kind)
    {
        switch (kind)
        {
            case ARMOR:
                return armorDB.getById(id);
            case OTHER:
                return otherDB.getById(id);
            case WEAPON:
                return weaponDB.getById(id);
            default:
                Log.logger.error("Cannot return item - unknown ItemKind.");
                return null;
        }
    }

    /** {@inheritDoc} */
    @Override
    public OtherType getOtherType(short id)
    {
        return otherDB.getById(id);
    }

    /** {@inheritDoc} */
    @Override
    public WeaponType getWeaponType(short id)
    {
        return weaponDB.getById(id);
    }

    /**
     * Adds observer to all inner databases.
     * @param observer
     *            observer to add
     * @see XmlAsyncDB#addObserver
     */
    public void addCompletionObserver(XmlCompletionObserver observer)
    {
        weaponDB.addObserver(observer);
        armorDB.addObserver(observer);
        otherDB.addObserver(observer);
    }

    /**
     * Removes observer from all inner databases.
     * @param observer
     *            observer to remove
     * @see XmlAsyncDB#removeObserver
     */
    public void removeCompletionObserver(XmlCompletionObserver observer)
    {
        weaponDB.removeObserver(observer);
        armorDB.removeObserver(observer);
        otherDB.removeObserver(observer);
    }

}
