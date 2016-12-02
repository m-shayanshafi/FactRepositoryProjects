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
import pl.org.minions.stigma.game.item.Armor;
import pl.org.minions.stigma.game.item.type.ArmorType;
import pl.org.minions.stigma.game.item.type.data.ArmorTypeData;
import pl.org.minions.stigma.game.item.type.stubs.ArmorTypeStub;
import pl.org.minions.stigma.globals.GlobalTimer;

/**
 * Asynchronous armor item type data database.
 * @see XmlAsyncDB
 * @see ItemTypeDB
 * @see ItemTypeDBAsync
 */
public class ArmorTypeDBAsync extends XmlAsyncDB<ArmorType, ArmorTypeData>
{
    private Map<Short, List<ArmorTypeStub>> stubs =
            new HashMap<Short, List<ArmorTypeStub>>();

    /**
     * Constructor. Will try to load all armor data from
     * given directory.
     * @param uri
     *            resources root
     */
    public ArmorTypeDBAsync(URI uri)
    {
        super(uri,
              ArmorTypeData.class,
              new ArmorTypeData.DataConverter(),
              false);
    }

    /** {@inheritDoc} */
    @Override
    public String getDbDir()
    {
        return ItemTypeDB.MAIN_DB_DIR + '/' + ItemTypeDB.ARMOR_DB_DIR;
    }

    /** {@inheritDoc} */
    @Override
    public String getFilePrefix()
    {
        return ItemTypeDB.ARMOR_FILE_PREFIX;
    }

    /** {@inheritDoc} */
    @Override
    protected void postParsing(final ArmorType obj)
    {
        List<ArmorTypeStub> list = stubs.remove(obj.getId());
        if (list != null)
        {
            for (final ArmorTypeStub stub : list)
            {
                Armor a = stub.getArmor();
                if (a != null)
                {
                    ItemFactory.getInstance()
                               .assignType(a,
                                           obj,
                                           stub.getDynamicModifierList());
                }
                else
                {
                    GlobalTimer.getTimer().schedule(new TimerTask()
                    {
                        @Override
                        public void run()
                        {
                            Armor a = stub.getArmor();
                            if (a != null)
                            {
                                ItemFactory.getInstance()
                                           .assignType(a,
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
        }
    }

    /** {@inheritDoc} */
    @Override
    protected ArmorType getStub(short id)
    {
        ArmorTypeStub stub = new ArmorTypeStub(id);
        List<ArmorTypeStub> list = stubs.get(id);
        if (list == null)
        {
            list = new LinkedList<ArmorTypeStub>();
            stubs.put(id, list);
        }
        list.add(stub);
        return stub;
    }

}
