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
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import pl.org.minions.stigma.network.Connector;
import pl.org.minions.stigma.network.GlobalConnector;
import pl.org.minions.stigma.network.connection.SessionConnection;
import pl.org.minions.stigma.server.ServerConfig;
import pl.org.minions.utils.logger.Log;

/**
 * Class responsible for opening listening socket channel
 * and for maintaining incoming connection. Used by
 * {@link GlobalConnector} to create new
 * {@link pl.org.minions.stigma.network.connection.Connection
 * connections} and directing them to {@link Authenticator}
 * @see GlobalConnector
 * @see pl.org.minions.stigma.network.connection.Connection
 * @see Authenticator
 */
public class Listener
{
    private ServerSocketChannel socketChannel;
    private Authenticator authenticator;

    /**
     * Creates new empty listener. Does not create any
     * socket channel - just acquiring data needed for
     * Listener to work.
     * @param authenticator
     *            authenticator used for authentication of
     *            incoming connections
     */
    public Listener(Authenticator authenticator)
    {
        this.authenticator = authenticator;
        this.socketChannel = null;
    }

    /**
     * Called by {@link GlobalConnector} when new connection
     * is received. Creates new
     * {@link pl.org.minions.stigma.network.connection.Connection}
     * and gives it to new {@link Authenticator} instance
     * for further authorization.
     * @param s
     *            incoming socket channel
     */
    public void accept(SocketChannel s)
    {
        try
        {
            s.finishConnect();
        }
        catch (IOException e)
        {
            Log.logger.warn("Couldn't finish connect: " + e);
            return;
        }
        Log.logger.info("Received new connection: " + s);
        SessionConnection sessionConnection = new SessionConnection(s);
        authenticator.authenticate(new Connector(sessionConnection));
    }

    /**
     * Closes listening socket - no more incoming
     * connections.
     */
    public void closeSocket()
    {
        if (socketChannel == null)
            return;

        try
        {
            socketChannel.close();
        }
        catch (IOException e)
        {
            Log.logger.error("Channel::close() failed: " + e);
        }

        socketChannel = null;
    }

    /**
     * Opens listening channel on proper port.
     * @see ServerConfig#getServerPort()
     * @return new listening channel
     */
    public ServerSocketChannel getServerSocketChannel()
    {
        if (socketChannel != null)
            return socketChannel;

        try
        {
            Log.logger.debug("Opening server socket");
            socketChannel = ServerSocketChannel.open();
            socketChannel.socket()
                         .bind(new InetSocketAddress(ServerConfig.globalInstance()
                                                                 .getServerPort()));
            Log.logger.debug("Server socket binded & configured");
        }
        catch (IOException e)
        {
            Log.logger.fatal("Opening server socket failed: " + e);
            return null;
        }

        return socketChannel;
    }
}
