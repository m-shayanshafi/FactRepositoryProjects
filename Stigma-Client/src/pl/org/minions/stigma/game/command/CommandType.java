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
package pl.org.minions.stigma.game.command;

import pl.org.minions.stigma.game.command.data.ActorDataRequest;
import pl.org.minions.stigma.game.command.data.ItemDataRequest;
import pl.org.minions.stigma.game.command.request.Drop;
import pl.org.minions.stigma.game.command.request.Equip;
import pl.org.minions.stigma.game.command.request.Move;
import pl.org.minions.stigma.game.command.request.PickUp;
import pl.org.minions.stigma.game.command.request.UnEquip;
import pl.org.minions.stigma.game.command.special.ApplyData;
import pl.org.minions.stigma.game.command.special.ApplyEvents;
import pl.org.minions.stigma.network.messaging.NetworkObjectCodec;
import pl.org.minions.utils.logger.Log;

/**
 * Class listing all commands types.
 */
public enum CommandType implements NetworkObjectCodec.ObjectFactory<Command>
{
    APPLY_DATA
    {
        /** {@inheritDoc} */
        @Override
        public Command buildObject()
        {
            return ApplyData.create();
        }
    },
    APPLY_EVENTS
    {
        /** {@inheritDoc} */
        @Override
        public Command buildObject()
        {
            return ApplyEvents.create();
        }
    },
    IDLE_COMMAND
    {
        /** {@inheritDoc} */
        @Override
        public Command buildObject()
        {
            Log.logger.fatal("Decoding IdleCommand?");
            return null;
        }
    },
    MOVE
    {
        /** {@inheritDoc} */
        @Override
        public Command buildObject()
        {
            return Move.create();
        }

        /** {@inheritDoc} */
        @Override
        public boolean isHighPriority()
        {
            return true;
        }

    },
    PICK_UP
    {
        /** {@inheritDoc} */
        @Override
        public Command buildObject()
        {
            return PickUp.create();
        }
    },
    DROP
    {
        /** {@inheritDoc} */
        @Override
        public Command buildObject()
        {
            return Drop.create();
        }
    },
    ACTOR_DATA_REQUEST
    {
        /** {@inheritDoc} */
        @Override
        public Command buildObject()
        {
            return new ActorDataRequest();
        }

        /** {@inheritDoc} */
        @Override
        public boolean isStoppedByCooldown()
        {
            return false;
        }
    },
    ITEM_DATA_REQUEST
    {
        /** {@inheritDoc} */
        @Override
        public Command buildObject()
        {
            return new ItemDataRequest();
        }

        /** {@inheritDoc} */
        @Override
        public boolean isStoppedByCooldown()
        {
            return false;
        }
    },
    EQUIP
    {
        /** {@inheritDoc} */
        @Override
        public Command buildObject()
        {
            return Equip.create();
        }
    },
    UNEQUIP
    {
        /** {@inheritDoc} */
        @Override
        public Command buildObject()
        {
            return UnEquip.create();
        }
    };

    /**
     * Returns {@code true} when command is of high priority
     * (should cancel low priority messages). Default
     * implementation always returns {@code false}.
     * @return {@code true} when command is of high priority
     */
    public boolean isHighPriority()
    {
        return false;
    }

    /**
     * Returns {@code true} if command cannot be executed if
     * requester is in cooldown. Default implementation
     * always returns {@code true}.
     * @return {@code true} when command cannot be executed
     *         when requester is in cooldown
     */
    public boolean isStoppedByCooldown()
    {
        return true;
    }
}
