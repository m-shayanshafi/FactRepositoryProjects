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
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.WritableByteChannel;

import pl.org.minions.stigma.network.GlobalConnector;
import pl.org.minions.utils.logger.Log;

/**
 * Representing generic connection class. It's based on
 * Channel NIO abstraction
 * @param <ChannelType>
 *            type representing channel used by this
 *            connection
 */
public class GenericConnection<ChannelType extends SelectableChannel & WritableByteChannel> extends
                                                                                            Connection
{
    private SelectionKey selectionKey;
    private ChannelType channel;

    /**
     * Creates new GenericConnection using given channel as
     * connection. Register itself in
     * {@link GlobalConnector} using
     * {@link GlobalConnector#addReceiver(Connection, SelectableChannel)}
     * @param chan
     *            channel representing new connection
     */
    public GenericConnection(ChannelType chan)
    {
        this.channel = chan;

        try
        {
            channel.configureBlocking(false);
        }
        catch (IOException e)
        {
            Log.logger.error("DatagramConnector.CONSTRUCTOR IOError", e);
            closeConnection();
            return;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean isConnected()
    {
        return channel != null && selectionKey != null;
    }

    /** {@inheritDoc} */
    @Override
    public boolean makeActive()
    {
        assert selectionKey == null;

        selectionKey =
                GlobalConnector.globalInstance().addReceiver(this, channel);
        if (selectionKey == null)
        {
            Log.logger.error("GenericConnection.makeActive failed");
            closeConnection();
            return false;
        }

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void removeConnection()
    {
        if (channel == null)
            return;
        if (selectionKey != null)
        {
            selectionKey.cancel();
            selectionKey = null;
        }

        try
        {
            channel.close();
            channel = null;
        }
        catch (IOException exc)
        {
            Log.logger.info(exc);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void send(ByteBuffer frame) throws IOException
    {
        if (!isConnected())
            return;

        int i = 0;
        while (i != frame.limit())
        {
            int r = channel.write(frame);
            if (r == -1)
            {
                Log.logger.info("write failed");
                closeConnection();
                return;
            }
            i += r;
        }
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        if (channel == null)
            return "<null>";
        return channel.toString();
    }

}
