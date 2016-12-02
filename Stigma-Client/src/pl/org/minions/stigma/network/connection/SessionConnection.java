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

import java.net.SocketException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SocketChannel;

import pl.org.minions.stigma.network.GlobalConnector;
import pl.org.minions.utils.logger.Log;

/**
 * Class representing single network connection. It provides
 * easy interface for sending/receiving using
 * {@link GlobalConnector}.
 * @see GlobalConnector
 * @see SocketChannel
 */
public class SessionConnection extends GenericConnection<SocketChannel>
{
    /**
     * Creates new Connection using given socket channel as
     * connection. Register itself in
     * {@link GlobalConnector} using
     * {@link GlobalConnector#addReceiver(Connection, SelectableChannel)}
     * @param soc
     *            socket channel representing new connection
     */
    public SessionConnection(SocketChannel soc)
    {
        super(soc);
        try
        {
            soc.socket().setTcpNoDelay(true);
        }
        catch (SocketException e)
        { // something strange happened better report
            Log.logger.error("_Connector.CONSTRUCTOR SocketError", e);
            closeConnection();
            return;
        }
    }

}
