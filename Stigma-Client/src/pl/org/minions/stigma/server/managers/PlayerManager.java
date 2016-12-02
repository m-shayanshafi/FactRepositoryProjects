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
package pl.org.minions.stigma.server.managers;

import pl.org.minions.stigma.game.actor.Actor;
import pl.org.minions.stigma.network.Connector;
import pl.org.minions.stigma.network.messaging.NetworkMessage;
import pl.org.minions.stigma.network.messaging.NetworkMessageReceiver;
import pl.org.minions.stigma.network.messaging.chat.ChatMessage;
import pl.org.minions.stigma.network.messaging.game.CommandMessage;
import pl.org.minions.stigma.network.messaging.system.CloseConnection;
import pl.org.minions.stigma.network.messaging.system.SystemMessage;
import pl.org.minions.stigma.network.messaging.system.SystemMessage.SystemEventType;
import pl.org.minions.stigma.network.server.Authenticator;
import pl.org.minions.utils.logger.Log;

/**
 * Implementation of {@link ActorManager} for <i>alive</i>
 * players. By actor's <i>brain</i> in such situation is
 * used player's brain connected via network with this
 * manager.
 */
public class PlayerManager extends ActorManager implements
                                               NetworkMessageReceiver
{
    private Authenticator authenticator;
    private Connector connector;

    /**
     * Creates player's actor manager for given actor,
     * connected with given world manager. Player is
     * connected to actor by given connector, representing
     * network connection.
     * @param worldManager
     *            world manager to which actor should be
     *            connected
     * @param chatManager
     *            chat manager to which actor should be
     *            connected
     * @param actor
     *            actor managed by this class
     * @param connector
     *            connector used for receiving/sending
     *            network messages to player
     * @param authenticator
     *            'back-connection' for logging out etc
     */
    public PlayerManager(WorldManager worldManager,
                         ChatManager chatManager,
                         Actor actor,
                         Connector connector,
                         Authenticator authenticator)
    {
        super(worldManager, chatManager, true, actor);
        assert actor.isPC();
        assert connector != null;
        this.connector = connector;
        this.authenticator = authenticator;

        connector.setNetworkMessageReceiver(this);
    }

    /** {@inheritDoc} */
    @Override
    public void disconnect()
    {
        if (isWorking())
            connector.closeConnection();
    }

    /** {@inheritDoc} */
    @Override
    public boolean isWorking()
    {
        return connector != null && connector.isConnected();
    }

    /** {@inheritDoc} */
    @Override
    public void networkMessage(NetworkMessage msg)
    {
        switch (msg.getType())
        {
            case LOGOUT_REQUEST:
                authenticator.logout(this);
                break;
            case DISCONNECTED:
                connector.setNetworkMessageReceiver(null);
                getWorldManager().removeActor(this);
                getChatManager().removeActor(this);
                break;
            case COMMAND:
                CommandMessage cmdMsg = (CommandMessage) msg;
                if (cmdMsg.getCommand() == null)
                {
                    Log.logger.error("Null command received");
                    return;
                }
                queueCommand(cmdMsg.getCommand());
                break;
            case CHAT:
                ChatMessage chatMsg = (ChatMessage) msg;
                if (chatMsg.getChat() == null)
                {
                    Log.logger.error("Null chat received");
                    return;
                }
                queueChat(chatMsg.getChat());
                break;
            default:
                Log.logger.error("Unsupported message type: " + msg.getType());
                connector.networkMessage(new CloseConnection());
                break;
        }
    }

    /** {@inheritDoc} */
    @Override
    public void send(CommandMessage command)
    {
        connector.networkMessage(command);
    }

    /** {@inheritDoc} */
    @Override
    public void send(ChatMessage chat)
    {
        connector.networkMessage(chat);
    }

    /** {@inheritDoc} */
    @Override
    public void sendEvent(SystemEventType eventType)
    {
        connector.networkMessage(new SystemMessage(eventType));
    }

}
