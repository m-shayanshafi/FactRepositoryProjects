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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import pl.org.minions.stigma.databases.item.ItemFactory;
import pl.org.minions.stigma.databases.item.ItemTypeDB;
import pl.org.minions.stigma.databases.xml.client.XmlAsyncDB;
import pl.org.minions.stigma.game.item.Weapon;
import pl.org.minions.stigma.game.item.type.WeaponType;
import pl.org.minions.stigma.game.item.type.data.WeaponTypeData;
import pl.org.minions.stigma.game.item.type.stubs.WeaponTypeStub;
import pl.org.minions.stigma.globals.GlobalTimer;

/**
 * Class representing weapons database.
 */
public class WeaponTypeDBAsync extends XmlAsyncDB<WeaponType, WeaponTypeData>
{
    private Map<Short, List<WeaponTypeStub>> stubs =
            new HashMap<Short, List<WeaponTypeStub>>();

    /**
     * Constructor. Will try to load all maps from given
     * directory.
     * @param uri
     *            resources root
     */
    public WeaponTypeDBAsync(URI uri)
    {
        super(uri,
              WeaponTypeData.class,
              new WeaponTypeData.DataConverter(),
              false);
    }

    /** {@inheritDoc} */
    @Override
    public String getDbDir()
    {
        return ItemTypeDB.MAIN_DB_DIR + '/' + ItemTypeDB.WEAPON_DB_DIR;
    }

    /** {@inheritDoc} */
    @Override
    public String getFilePrefix()
    {
        return ItemTypeDB.WEAPON_FILE_PREFIX;
    }

    /** {@inheritDoc} */
    @Override
    protected void postParsing(final WeaponType obj)
    {
        List<WeaponTypeStub> list = stubs.remove(obj.getId());
        if (list != null)
            for (final WeaponTypeStub stub : list)
            {
                Weapon w = stub.getWeapon();
                if (w != null)
                    ItemFactory.getInstance()
                               .assignType(w,
                                           obj,
                                           stub.getDynamicModifierList());
                else
                    GlobalTimer.getTimer().schedule(new TimerTask()
                    {
                        @Override
                        public void run()
                        {
                            Weapon w = stub.getWeapon();
                            if (w != null)
                            {
                                ItemFactory.getInstance()
                                           .assignType(w,
                                                       obj,
                                                       stub.getDynamicModifierList());
                                cancel();
                            }
                        }
                    },
                                                    0,
                                                    1);
            }
    }

    /** {@inheritDoc} */
    @Override
    protected WeaponType getStub(short id)
    {
        WeaponTypeStub stub = new WeaponTypeStub(id);
        List<WeaponTypeStub> list = stubs.get(id);
        if (list == null)
        {
            list = new LinkedList<WeaponTypeStub>();
            stubs.put(id, list);
        }
        list.add(stub);
        return stub;
    }
}
