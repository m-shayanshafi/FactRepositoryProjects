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
import pl.org.minions.stigma.game.item.OtherItem;
import pl.org.minions.stigma.game.item.type.OtherType;
import pl.org.minions.stigma.game.item.type.data.OtherTypeData;
import pl.org.minions.stigma.game.item.type.stubs.OtherTypeStub;
import pl.org.minions.stigma.globals.GlobalTimer;

/**
 * Synchronous other item type data database.
 * @see XmlAsyncDB
 * @see ItemTypeDB
 * @see ItemTypeDBAsync
 */
public class OtherTypeDBAsync extends XmlAsyncDB<OtherType, OtherTypeData>
{
    private Map<Short, List<OtherTypeStub>> stubs =
            new HashMap<Short, List<OtherTypeStub>>();

    /**
     * Constructor. Will try to load all maps from given
     * directory.
     * @param uri
     *            resources root
     */
    public OtherTypeDBAsync(URI uri)
    {
        super(uri,
              OtherTypeData.class,
              new OtherTypeData.DataConverter(),
              false);
    }

    /** {@inheritDoc} */
    @Override
    public String getDbDir()
    {
        return ItemTypeDB.MAIN_DB_DIR + '/' + ItemTypeDB.OTHER_DB_DIR;
    }

    /** {@inheritDoc} */
    @Override
    public String getFilePrefix()
    {
        return ItemTypeDB.OTHER_FILE_PREFIX;
    }

    /** {@inheritDoc} */
    @Override
    protected void postParsing(final OtherType obj)
    {
        List<OtherTypeStub> list = stubs.remove(obj.getId());
        if (list != null)
            for (final OtherTypeStub stub : list)
            {
                OtherItem o = stub.getOtherItem();
                if (o != null)
                    ItemFactory.getInstance().assignType(o, obj);
                else
                    GlobalTimer.getTimer().schedule(new TimerTask()
                    {
                        @Override
                        public void run()
                        {
                            OtherItem o = stub.getOtherItem();
                            if (o != null)
                            {
                                ItemFactory.getInstance().assignType(o, obj);
                                cancel();
                            }
                        }
                    }, 0, 1);
            }
    }

    /** {@inheritDoc} */
    @Override
    protected OtherType getStub(short id)
    {
        OtherTypeStub stub = new OtherTypeStub(id);
        List<OtherTypeStub> list = stubs.get(id);
        if (list == null)
        {
            list = new LinkedList<OtherTypeStub>();
            stubs.put(id, list);
        }
        list.add(stub);
        return stub;
    }

}
