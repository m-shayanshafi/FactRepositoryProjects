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
package pl.org.minions.stigma.network.messaging;

import pl.org.minions.stigma.network.messaging.auth.LoginActorChosen;
import pl.org.minions.stigma.network.messaging.auth.LoginActorsList;
import pl.org.minions.stigma.network.messaging.auth.LoginBadVersion;
import pl.org.minions.stigma.network.messaging.auth.LoginCorrect;
import pl.org.minions.stigma.network.messaging.auth.LoginData;
import pl.org.minions.stigma.network.messaging.auth.LoginDisabledActorList;
import pl.org.minions.stigma.network.messaging.auth.LoginError;
import pl.org.minions.stigma.network.messaging.auth.LoginPasswordAccepted;
import pl.org.minions.stigma.network.messaging.auth.LoginProceed;
import pl.org.minions.stigma.network.messaging.auth.LoginRefreshDisabledActorList;
import pl.org.minions.stigma.network.messaging.auth.LoginRequest;
import pl.org.minions.stigma.network.messaging.chat.ChatMessage;
import pl.org.minions.stigma.network.messaging.game.CommandMessage;
import pl.org.minions.stigma.network.messaging.system.LogoutRequest;
import pl.org.minions.stigma.network.messaging.system.PingMessage;
import pl.org.minions.stigma.network.messaging.system.SystemMessage;
import pl.org.minions.utils.logger.Log;

/**
 * Represents types of messages sent via network.
 */
public enum NetworkMessageType implements
                              NetworkObjectCodec.ObjectFactory<NetworkMessage>
{
    /** request for authorization (pub_key). */
    LOGIN_REQUEST
    {
        /** {@inheritDoc} */
        @Override
        public NetworkMessage buildObject()
        {
            return LoginRequest.create();
        }
    },
    /**
     * email and password correct.
     */
    LOGIN_PASSWORD_ACCEPTED
    {
        /** {@inheritDoc} */
        @Override
        public NetworkMessage buildObject()
        {
            return new LoginPasswordAccepted();
        }
    },
    /**
     * client is ready - sending available actors list.
     */
    LOGIN_ACTORS_LIST
    {

        /**
         * {@inheritDoc}
         */
        @Override
        public NetworkMessage buildObject()
        {
            return LoginActorsList.create();
        }

    },
    /** authorization success (chosen actor id). */
    LOGIN_CORRECT
    {
        /** {@inheritDoc} */
        @Override
        public NetworkMessage buildObject()
        {
            return LoginCorrect.create();
        }
    },
    /** authorization failure. */
    LOGIN_ERROR
    {
        /** {@inheritDoc} */
        @Override
        public NetworkMessage buildObject()
        {
            return new LoginError();
        }
    },
    /** authorization failure - bad client version. */
    LOGIN_BAD_VERSION
    {
        /** {@inheritDoc} */
        @Override
        public NetworkMessage buildObject()
        {
            return new LoginBadVersion();
        }
    },
    /**
     * authorization data (login, password, client version).
     */
    LOGIN_DATA
    {
        /** {@inheritDoc} */
        @Override
        public NetworkMessage buildObject()
        {
            return LoginData.create();
        }
    },
    /**
     * client is ready, server can send more data.
     */
    LOGIN_PROCEED
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public NetworkMessage buildObject()
        {
            return new LoginProceed();
        }
    },
    /** chosen actor (actor_id). */
    LOGIN_ACTOR_CHOSEN
    {
        /** {@inheritDoc} */
        @Override
        public NetworkMessage buildObject()
        {
            return LoginActorChosen.create();
        }
    },
    /** request for refreshing disabled actors list. */
    LOGIN_REFRESH_DISABLED_ACTOR_LIST
    {
        /** {@inheritDoc} */
        @Override
        public NetworkMessage buildObject()
        {
            return new LoginRefreshDisabledActorList();
        }
    },
    LOGIN_DISABLED_ACTOR_LIST
    {
        /** {@inheritDoc} */
        @Override
        public NetworkMessage buildObject()
        {
            return LoginDisabledActorList.create();
        }
    },
    /**
     * information that current system lost/closed it
     * network connection (sent by Connection).
     */
    DISCONNECTED
    {
        /** {@inheritDoc} */
        @Override
        public NetworkMessage buildObject()
        {
            Log.logger.error("DISCONNECTED message should never be decoded!");
            assert false;
            return null;
        }
    },
    /** request to close connection (for Connection). */
    CLOSE_CONNECTION
    {
        /** {@inheritDoc} */
        @Override
        public NetworkMessage buildObject()
        {
            Log.logger.error("CLOSE_CONNECTION message should never be decoded!");
            assert false;
            return null;
        }
    },
    /** message wrapping command. */
    COMMAND
    {
        /** {@inheritDoc} */
        @Override
        public NetworkMessage buildObject()
        {
            return CommandMessage.create();
        }
    },
    /** message wrapping chat. */
    CHAT
    {
        /** {@inheritDoc} */
        @Override
        public NetworkMessage buildObject()
        {
            return ChatMessage.create();
        }
    },
    /** logout request. */
    LOGOUT_REQUEST
    {
        /** {@inheritDoc} */
        @Override
        public NetworkMessage buildObject()
        {
            return new LogoutRequest();
        }
    },
    /** ping message. */
    PING
    {
        /** {@inheritDoc} */
        @Override
        public NetworkMessage buildObject()
        {
            return new PingMessage();
        }
    },
    /** system message. */
    SYSTEM_MESSAGE
    {
        /** {@inheritDoc} */
        @Override
        public NetworkMessage buildObject()
        {
            return new SystemMessage();
        }
    };

}
