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

import pl.org.minions.stigma.network.Buffer;

/**
 * Abstract class representing message that can be sent via
 * network.
 */
public abstract class NetworkMessage extends NetworkObject<NetworkMessageType>
{
    private Buffer buffer;

    /**
     * Creates network message with given type.
     * @param type
     *            type of network message
     */
    protected NetworkMessage(NetworkMessageType type)
    {
        super(type);
    }

    /**
     * Codes message on frame. Creates buffer if needed,
     * calls {@link NetworkObject#encode(Buffer)}.
     * @return frame containing coded message contents
     */
    public final Buffer encode()
    {
        if (buffer == null)
        {
            buffer = new Buffer(this.getParamsLength());
            if (!encode(buffer))
                return null;
        }
        return buffer;
    }

    /**
     * Returns text representation of this message type.
     * @return string representation of messages type
     */
    @Override
    public String toString()
    {
        return this.getClass().getName();
    }
}
