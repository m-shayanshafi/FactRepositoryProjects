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

import pl.org.minions.stigma.databases.item.ItemEffectDB;
import pl.org.minions.stigma.databases.xml.client.XmlAsyncDB;
import pl.org.minions.stigma.game.item.effect.ItemEffect;
import pl.org.minions.stigma.game.item.effect.data.ItemEffectData;

/**
 * Asynchronous armor item effect data database.
 * @see XmlAsyncDB
 * @see ItemEffectDB
 */
public class ItemEffectDBAsync extends XmlAsyncDB<ItemEffect, ItemEffectData> implements
                                                                             ItemEffectDB
{

    /**
     * Constructor. Will try to load all maps from given
     * directory.
     * @param uri
     *            resources root
     */
    public ItemEffectDBAsync(URI uri)
    {
        super(uri,
              ItemEffectData.class,
              new ItemEffectData.DataConverter(),
              false);
    }

    /** {@inheritDoc} */
    @Override
    public ItemEffect getEffect(short id)
    {
        return getById(id);
    }

    /** {@inheritDoc} */
    @Override
    public String getDbDir()
    {
        return MAIN_DB_DIR + '/' + EFFECT_DB_DIR;
    }

    /** {@inheritDoc} */
    @Override
    public String getFilePrefix()
    {
        return EFFECT_FILE_PREFIX;
    }

}
