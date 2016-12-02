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
package pl.org.minions.stigma.databases.map.server;

import java.net.URI;

import pl.org.minions.stigma.databases.xml.Converter;
import pl.org.minions.stigma.databases.xml.server.XmlSyncDB;
import pl.org.minions.stigma.server.item.StaticItems;

/**
 * Synchronous item layer database.
 * @see XmlSyncDB
 */
public class StaticItemsDB extends XmlSyncDB<StaticItems, StaticItems>
{
    private static final String DB_DIR = "staticitems";
    private static final String FILE_PREFIX = DB_DIR;

    /**
     * Constructor. Will try to load all layers from given
     * directory.
     * @param uri
     *            resources root
     */
    public StaticItemsDB(URI uri)
    {
        super(uri,
              StaticItems.class,
              new Converter.SimpleConverter<StaticItems>(),
              true);
    }

    /** {@inheritDoc} */
    @Override
    public String getDbDir()
    {
        return DB_DIR;
    }

    /** {@inheritDoc} */
    @Override
    public String getFilePrefix()
    {
        return FILE_PREFIX;
    }

}
