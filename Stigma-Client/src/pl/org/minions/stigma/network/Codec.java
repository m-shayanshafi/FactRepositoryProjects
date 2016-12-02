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

import pl.org.minions.stigma.network.messaging.NetworkMessage;
import pl.org.minions.stigma.network.messaging.NetworkMessageType;
import pl.org.minions.stigma.network.messaging.NetworkObjectCodec;
import pl.org.minions.utils.logger.Log;

/**
 * Class used to translate in-system messages to network
 * frames, and network frames back into messages.
 */
public class Codec extends
                  NetworkObjectCodec<NetworkMessageType, NetworkMessage>
{
    /** Creates codec. */
    public Codec()
    {
        super(NetworkMessageType.class);
    }

    /** {@inheritDoc} */
    @Override
    public NetworkMessage decode(Buffer buffer)
    {
        try
        {
            return super.decode(buffer);
        }
        catch (IndexOutOfBoundsException e)
        {
            Log.logger.error(e);
            return null;
        }
    }

    /**
     * Encodes message on frame ({@link Buffer}). Uses
     * {@link NetworkMessage#encode()} method.
     * @param msg
     *            message to be encoded
     * @return frame with encoded message
     */
    public Buffer encode(NetworkMessage msg)
    {
        return msg.encode();
    }
}
