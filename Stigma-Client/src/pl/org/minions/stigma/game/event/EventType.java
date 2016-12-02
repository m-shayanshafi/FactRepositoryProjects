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
package pl.org.minions.stigma.game.event;

import pl.org.minions.stigma.game.event.actor.ActorAdded;
import pl.org.minions.stigma.game.event.actor.ActorChangedMap;
import pl.org.minions.stigma.game.event.actor.ActorRemoved;
import pl.org.minions.stigma.game.event.actor.ActorWalk;
import pl.org.minions.stigma.game.event.item.ItemAdded;
import pl.org.minions.stigma.game.event.item.ItemDropped;
import pl.org.minions.stigma.game.event.item.ItemEquipped;
import pl.org.minions.stigma.game.event.item.ItemPickedUp;
import pl.org.minions.stigma.game.event.item.ItemUnEquipped;
import pl.org.minions.stigma.network.messaging.NetworkObjectCodec;

/**
 * Class listing all game event objects' types.
 */
public enum EventType implements NetworkObjectCodec.ObjectFactory<Event>
{
    ACTOR_WALK
    {
        /** {@inheritDoc} */
        @Override
        public Event buildObject()
        {
            return ActorWalk.create();
        }
    },
    ACTOR_ADDED
    {

        /** {@inheritDoc} */
        @Override
        public Event buildObject()
        {
            return ActorAdded.create();
        }
    },
    ACTOR_REMOVED
    {

        /** {@inheritDoc} */
        @Override
        public Event buildObject()
        {
            return ActorRemoved.create();
        }
    },
    ACTOR_CHANGED_MAP
    {
        /** {@inheritDoc} */
        @Override
        public Event buildObject()
        {
            return ActorChangedMap.create();
        }
    },
    ITEM_ADDED
    {
        /** {@inheritDoc} */
        @Override
        public Event buildObject()
        {
            return ItemAdded.create();
        }
    },
    ITEM_PICKED_UP
    {
        /** {@inheritDoc} */
        @Override
        public Event buildObject()
        {
            return ItemPickedUp.create();
        }
    },
    ITEM_DROPPED
    {
        /** {@inheritDoc} */
        @Override
        public Event buildObject()
        {
            return ItemDropped.create();
        }
    },
    ITEM_EQUIPPED
    {
        /** {@inheritDoc} */
        @Override
        public Event buildObject()
        {
            return ItemEquipped.create();
        }
    },
    ITEM_UNEQUIPPED
    {
        /** {@inheritDoc} */
        @Override
        public Event buildObject()
        {
            return ItemUnEquipped.create();
        }
    },
}
