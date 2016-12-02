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
package pl.org.minions.stigma.server.item;

import pl.org.minions.stigma.databases.item.ItemFactory;
import pl.org.minions.stigma.game.item.Item;
import pl.org.minions.stigma.globals.Position;
import pl.org.minions.stigma.server.managers.WorldManager;
import pl.org.minions.utils.logger.Log;

/**
 * Utility class used to build {@link Item} instances on
 * specific position.
 */
public final class ItemBuilder
{
    private ItemBuilder()
    {
    }

    /**
     * Builds item.
     * @param worldManager
     *            world manager for which item should be
     *            build
     * @param itemDescription
     *            description of item to build
     * @param mapId
     *            id of map on which item should be lay
     * @param mapInstanceNo
     *            instance number of map on which item
     *            should be lay
     * @param position
     *            position of new item on map
     * @return newly created item
     */
    public static Item buildItem(WorldManager worldManager,
                                 ItemDescription itemDescription,
                                 short mapId,
                                 short mapInstanceNo,
                                 Position position)
    {
        Item i =
                ItemFactory.getInstance()
                           .getItem(ItemIdRegistry.globalInstance().nextId(),
                                    itemDescription.getType(),
                                    itemDescription.getKind(),
                                    itemDescription.getModifiers());
        if (i.getType() == null)
        {
            Log.logger.warn("Trying to create item from unknown type. Check type database.");
            return null;
        }

        i.setName(itemDescription.getName());
        i.setMapId(mapId);
        i.setMapInstanceNo(mapInstanceNo);
        i.setPosition(position);

        for (Short s : itemDescription.getEffects())
        {
            i.getEffectIdList().add(s);
        }

        return i;
    }
}
