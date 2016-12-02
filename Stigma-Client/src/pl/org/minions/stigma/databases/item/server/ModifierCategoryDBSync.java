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

import pl.org.minions.stigma.databases.item.ModifierCategoryDB;
import pl.org.minions.stigma.databases.item.wrappers.ModifierCategoryWrapper;
import pl.org.minions.stigma.databases.xml.Converter.SimpleConverter;
import pl.org.minions.stigma.databases.xml.server.SimpleXmlSyncDB;
import pl.org.minions.stigma.game.item.modifier.ModifierCategory;

/**
 * Modifier category simple database.
 * @see ModifierCategoryDB
 * @see SimpleXmlSyncDB
 */
public class ModifierCategoryDBSync extends
                                   SimpleXmlSyncDB<ModifierCategoryWrapper, ModifierCategory, ModifierCategory> implements
                                                                                                               ModifierCategoryDB
{

    /**
     * Default constructor.
     * @param uri
     *            root URI
     */
    public ModifierCategoryDBSync(URI uri)
    {
        super(uri,
              ModifierCategoryWrapper.class,
              new SimpleConverter<ModifierCategory>());
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

    /** {@inheritDoc} */
    @Override
    public ModifierCategory getModifierCategory(short id)
    {
        return super.getById(id);
    }

}
