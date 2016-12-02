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
package pl.org.minions.stigma.network.connection;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import pl.org.minions.stigma.network.GlobalConnector;
import pl.org.minions.stigma.network.messaging.NetworkMessage;
import pl.org.minions.stigma.network.messaging.NetworkMessageReceiver;
import pl.org.minions.stigma.network.messaging.NetworkMessageType;
import pl.org.minions.stigma.network.messaging.system.Disconnected;
import pl.org.minions.utils.logger.Log;

/**
 * Class representing single network connection. It provides
 * easy interface for sending/receiving using
 * {@link GlobalConnector}.
 * @see GlobalConnector
 * @see SocketChannel
 */
public abstract class Connection implements NetworkMessageReceiver
{
    private NetworkMessageReceiver receiver;

    /**
     * Closes connection and removes it from
     * {@link GlobalConnector}. If
     * {@link NetworkMessageReceiver} is given it sends to
     * it {@link NetworkMessageType#DISCONNECTED} message.
     */
    public final void closeConnection()
    {
        if (!isConnected())
            return; // already disconnected 

        removeConnection();
        if (receiver != null)
            receiver.networkMessage(new Disconnected());
    }

    /**
     * Returns actual (may be {@code null}) incoming network
     * messages receiver.
     * @return incoming network messages receiver
     */
    public final synchronized NetworkMessageReceiver getNetworkMessageReceiver()
    {
        return this.receiver;
    }

    /**
     * Checks if connection is still active.
     * @return {@code true} when it is still connected
     */
    public abstract boolean isConnected();

    /**
     * Connection will be active - will be available for
     * writing and receiving messages.
     * @return {@code true} if everything was OK
     */
    public abstract boolean makeActive();

    /**
     * Puts message in {@link GlobalConnector} sending
     * queue. <b>If message is a
     * {@link NetworkMessageType#CLOSE_CONNECTION} message -
     * closes connection using {@link #closeConnection()}
     * method.</b>
     * @param message
     *            message to be send.
     */
    @Override
    public final void networkMessage(NetworkMessage message)
    {
        if (message == null)
            return;
        if (message.getType() == NetworkMessageType.CLOSE_CONNECTION)
        {
            closeConnection();
            return;
        }
        GlobalConnector.globalInstance().send(this, message);
    }

    /**
     * Called by GlobalConnector after receiving message,
     * sends it to actual {@link NetworkMessageReceiver}.
     * @param message
     *            received message
     */
    public final void receive(NetworkMessage message)
    {
        NetworkMessageReceiver localReceiver = getNetworkMessageReceiver();
        if (localReceiver == null)
        {
            Log.logger.warn("Incoming message lost (receiver null): "
                + message.toString());
            return;
        }
        localReceiver.networkMessage(message);
    }

    /**
     * Same as {@link #closeConnection()} but does not send
     * any message to receiver.
     */
    public abstract void removeConnection();

    /**
     * sends frame using given network connection. Should be
     * called only by {@link GlobalConnector}.
     * @param frame
     *            frame to send
     * @throws IOException
     *             when write fails
     */
    public abstract void send(ByteBuffer frame) throws IOException;

    /**
     * Sets new receiver of incoming messages.
     * @param receiver
     *            new incoming network messages receiver
     */
    public final synchronized void setNetworkMessageReceiver(NetworkMessageReceiver receiver)
    {
        this.receiver = receiver;
    }
}
