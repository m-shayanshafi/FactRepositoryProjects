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
package pl.org.minions.stigma.client.requests;

import pl.org.minions.stigma.game.actor.Actor;
import pl.org.minions.stigma.game.command.Command;
import pl.org.minions.stigma.game.command.request.Drop;
import pl.org.minions.stigma.game.item.Item;
import pl.org.minions.stigma.game.world.World;
import pl.org.minions.stigma.globals.Position;

/**
 * A request to drop item.
 */
public class DropRequest extends UnInterruptibleRequest
{
    private int itemId;

    /**
     * Request to drop the item with given id.
     * @param itemId
     *            id of the item
     */
    public DropRequest(int itemId)
    {
        this.itemId = itemId;
    }

    /** {@inheritDoc} */
    @Override
    public Command getNextCommand(Actor playerActor,
                                  World world,
                                  Command previousCommandResponse)
    {
        final Item item = world.getItem(itemId);

        if (item == null)
            return null;

        if (item.isOnGround())
            return null;

        if (playerActor.getInventory().contains(item))
            return new Drop(itemId);
        else
            return null;
    }

    /** {@inheritDoc} */
    @Override
    public Position getTargetLocation()
    {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "Drop(#" + itemId + ")";
    }

}
