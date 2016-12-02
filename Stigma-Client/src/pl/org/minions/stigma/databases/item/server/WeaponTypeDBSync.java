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

import pl.org.minions.stigma.databases.item.ItemTypeDB;
import pl.org.minions.stigma.databases.xml.server.XmlSyncDB;
import pl.org.minions.stigma.game.item.type.WeaponType;
import pl.org.minions.stigma.game.item.type.data.WeaponTypeData;

/**
 * Class representing weapons database.
 */
public class WeaponTypeDBSync extends XmlSyncDB<WeaponType, WeaponTypeData>
{
    /**
     * Constructor. Will try to load all maps from given
     * directory.
     * @param uri
     *            resources root
     */
    public WeaponTypeDBSync(URI uri)
    {
        super(uri,
              WeaponTypeData.class,
              new WeaponTypeData.DataConverter(),
              true);
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
}
