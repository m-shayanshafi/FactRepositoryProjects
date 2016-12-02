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
package pl.org.minions.stigma.network.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import pl.org.minions.stigma.globals.GlobalConfig;
import pl.org.minions.stigma.network.Connector;
import pl.org.minions.stigma.network.GlobalConnector;
import pl.org.minions.stigma.network.connection.Connection;
import pl.org.minions.stigma.network.connection.SessionConnection;
import pl.org.minions.stigma.network.messaging.NetworkMessage;
import pl.org.minions.stigma.network.messaging.NetworkMessageReceiver;
import pl.org.minions.stigma.network.messaging.system.PingMessage;
import pl.org.minions.utils.logger.Log;

/**
 * Initialize {@link GlobalConnector} with given server
 * address, connects to server. Usually works only with one
 * connection (to server) - this class is provided for
 * convenience and symmetric network connection handling on
 * client and server
 */
public final class ClientGlobalConnector extends GlobalConnector
{
    private static ClientGlobalConnector instance;

    private long lag;

    private ClientGlobalConnector()
    {
        super(true);
    }

    /**
     * Singleton access method.
     * @return global instance
     */
    public static ClientGlobalConnector globalInstance()
    {
        return instance;
    }

    /**
     * Initializes class in "client" mode. Connects to given
     * address and return
     * {@link pl.org.minions.stigma.network.connection.Connection}
     * representing new connection.
     * @param url
     *            address to which it should connect
     * @param receiver
     *            object to which new Connection should send
     *            it incoming messages
     * @return client connector for receiving messages
     */
    public static Connector initGlobalInstance(URL url,
                                               NetworkMessageReceiver receiver)
    {
        instance = new ClientGlobalConnector();
        instance.startInit();

        SocketChannel socket;
        try
        {
            InetSocketAddress addr =
                    new InetSocketAddress(url.getHost(),
                                          url.getPort() > 0 ? url.getPort()
                                                           : GlobalConfig.DEFAULT_SERVER_PORT);
            socket = SocketChannel.open();
            socket.connect(addr);
            socket.finishConnect();
        }
        catch (UnknownHostException e)
        {
            Log.logger.error("Bad hostname: " + url.getHost());
            return null;
        }
        catch (IOException e)
        {
            Log.logger.error("Socket IO error " + e);
            return null;
        }

        instance.endInit();
        GlobalConnector.setGlobalInstance(instance);

        Connector connector = new Connector(new SessionConnection(socket));
        connector.setNetworkMessageReceiver(receiver);

        return connector;
    }

    /**
     * Returns last lag measure.
     * @return last lag measure.
     */
    public long getLag()
    {
        return lag;
    }

    /** {@inheritDoc} */
    @Override
    protected boolean performAccept(SelectionKey key)
    {
        Log.logger.fatal("Accept called on client");
        return false;
    }

    /** {@inheritDoc} */
    @Override
    protected boolean processPing(NetworkMessage msg, Connection connection)
    {
        PingMessage ping = (PingMessage) msg;
        lag = System.currentTimeMillis() - ping.getTS();
        if (Log.logger.isDebugEnabled())
            Log.logger.debug("Lag: " + lag);
        return false;
    }
}
