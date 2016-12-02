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
package pl.org.minions.stigma.network.server;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;

import pl.org.minions.stigma.network.GlobalConnector;
import pl.org.minions.stigma.network.connection.Connection;
import pl.org.minions.stigma.network.messaging.NetworkMessage;
import pl.org.minions.utils.logger.Log;

/**
 * Initialize {@link GlobalConnector} with {@link Listener}
 * object. By default starts to accepts incoming connection
 * and using given object for further processing; new
 * connection (after authorization) may be added using
 * {@link #addReceiver(Connection,SelectableChannel)}
 * @see Listener
 * @see pl.org.minions.stigma.network.server.Authenticator
 */
public final class ServerGlobalConnector extends GlobalConnector
{
    private Listener listener;

    private ServerGlobalConnector()
    {
        super(false);
    }

    /**
     * Initializes class in "server" mode. Uses given
     * {@link Listener} object to create listening socket,
     * and awaits for incoming connections.
     * @param listener
     *            class responsible for creation listening
     *            socket on proper port, and for receiving
     *            new connections
     */
    public static void initGlobalInstance(Listener listener)
    {
        ServerGlobalConnector instance = new ServerGlobalConnector();
        instance.startInit();

        instance.listener = listener;
        ServerSocketChannel soc = listener.getServerSocketChannel();
        if (soc == null)
        {
            Log.logger.fatal("Null socket server channel");
            System.exit(1);
        }
        try
        {
            soc.configureBlocking(false);
            soc.register(instance.getSelector(), SelectionKey.OP_ACCEPT);
        }
        catch (ClosedChannelException e)
        {
            Log.logger.fatal("Register server socket failed: " + e);
            System.exit(1);
        }
        catch (IOException e)
        {
            Log.logger.fatal("Register server socket failed (I/O exception): "
                + e);
            System.exit(1);
        }

        Log.logger.debug("Listening socket active");

        instance.endInit();
        setGlobalInstance(instance);
    }

    /** {@inheritDoc} */
    @Override
    protected boolean performAccept(SelectionKey key)
    {
        try
        {
            listener.accept(((ServerSocketChannel) key.channel()).accept());
        }
        catch (IOException e)
        {
            Log.logger.warn("Accept failed: " + e);
            return false;
        }
        return true;
    }

    /** {@inheritDoc} */
    @Override
    protected boolean processPing(NetworkMessage msg, Connection connection)
    {
        connection.networkMessage(msg);
        return false;
    }
}
