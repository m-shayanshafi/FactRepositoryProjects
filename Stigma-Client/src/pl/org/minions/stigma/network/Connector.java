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
package pl.org.minions.stigma.network;

import pl.org.minions.stigma.network.connection.Connection;
import pl.org.minions.stigma.network.messaging.NetworkMessage;
import pl.org.minions.stigma.network.messaging.NetworkMessageReceiver;
import pl.org.minions.utils.logger.Log;

/**
 * Class representing network connection. Currently it's
 * only wrapper for {@link Connection} but it might be used
 * in future for more complicated connection (for example,
 * using multiple {@link Connection})
 */
public class Connector implements NetworkMessageReceiver
{
    private Connection connection;

    /**
     * Constructor.
     * @param connection
     *            connection to wrap
     */
    public Connector(Connection connection)
    {
        this.connection = connection;
        if (!connection.makeActive())
            Log.logger.error("Connector ctor failed");
    }

    /**
     * Closes connection.
     */
    public void closeConnection()
    {
        if (connection != null)
            connection.closeConnection();
    }

    /**
     * Returns {@code true} when connection is active.
     * @return {@code true} when connection is active.
     */
    public boolean isConnected()
    {
        return connection != null && connection.isConnected();
    }

    /** {@inheritDoc} */
    @Override
    public void networkMessage(NetworkMessage msg)
    {
        if (isConnected())
            connection.networkMessage(msg);
    }

    /**
     * Changes message receiver of connection.
     * @param receiver
     *            new network message receiver
     */
    public void setNetworkMessageReceiver(NetworkMessageReceiver receiver)
    {
        if (isConnected())
            connection.setNetworkMessageReceiver(receiver);
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        if (connection == null)
            return "<null>";
        return connection.toString();
    }

}
