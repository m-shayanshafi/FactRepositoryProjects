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

import java.util.List;

import pl.org.minions.stigma.client.PlayerController.PlayerRequest;
import pl.org.minions.stigma.game.actor.Actor;
import pl.org.minions.stigma.game.command.Command;
import pl.org.minions.stigma.game.command.request.PickUp;
import pl.org.minions.stigma.game.item.Item;
import pl.org.minions.stigma.game.map.MapInstance;
import pl.org.minions.stigma.game.world.World;
import pl.org.minions.stigma.globals.Position;

/**
 * A request to pick up all items lying on the map tile that
 * the player actor is currently standing on.
 */
public class PickUpAllRequest implements PlayerRequest
{

    /** {@inheritDoc} */
    @Override
    public Command getNextCommand(Actor playerActor,
                                  World world,
                                  Command previousCommandResponse)
    {
        final MapInstance map =
                world.getMap(playerActor.getMapId(),
                             playerActor.getMapInstanceNo());
        if (map == null)
            return null;

        final List<Item> items = map.getItems(playerActor.getPosition());

        if (items == null || items.isEmpty())
            return null;

        return new PickUp(items.get(0).getId());
    }

    /** {@inheritDoc} */
    @Override
    public Position getTargetLocation()
    {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isInterruptible()
    {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "PickUpAll()";
    }

}
