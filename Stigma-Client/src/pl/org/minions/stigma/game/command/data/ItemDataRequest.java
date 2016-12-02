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
package pl.org.minions.stigma.game.command.data;

import java.util.List;

import pl.org.minions.stigma.game.command.CommandType;
import pl.org.minions.stigma.game.data.WorldData;
import pl.org.minions.stigma.game.data.info.ItemInfo;
import pl.org.minions.stigma.game.item.Item;
import pl.org.minions.stigma.game.world.ExtendedWorld;
import pl.org.minions.utils.logger.Log;

/**
 * Request for items data.
 */
public class ItemDataRequest extends GenericDataRequest<Item>
{
    /** Constructor. */
    public ItemDataRequest()
    {
        super(CommandType.ITEM_DATA_REQUEST);
    }

    /** {@inheritDoc} */
    @Override
    protected boolean fillDeltas(ExtendedWorld world,
                                 List<WorldData> deltasForSender,
                                 List<RequestDesc> list)
    {
        ItemInfo info = new ItemInfo();

        for (RequestDesc desc : list)
        {
            Item i = world.getItem(desc.getObjectId());
            if (i == null)
            {
                Log.logger.warn("Requested info for null item id="
                    + desc.getObjectId());
                continue;
            }
            if (desc.getObjectTS() < i.getNewestTS())
                info.addItem(i);
        }
        if (!info.isEmpty())
            deltasForSender.add(info);
        return !info.isEmpty();
    }
}
