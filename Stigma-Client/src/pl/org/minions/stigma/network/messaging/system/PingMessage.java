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
package pl.org.minions.stigma.network.messaging.system;

import pl.org.minions.stigma.globals.SizeOf;
import pl.org.minions.stigma.network.Buffer;
import pl.org.minions.stigma.network.messaging.NetworkMessage;
import pl.org.minions.stigma.network.messaging.NetworkMessageType;

/**
 * 'Ping' message. Carries time-stamp in milliseconds with
 * can measure 'lag'.
 */
public class PingMessage extends NetworkMessage
{
    private long ts;

    /**
     * Constructor. Creates empty message - used for
     * decoding.
     */
    public PingMessage()
    {
        super(NetworkMessageType.PING);
    }

    /**
     * Constructor.
     * @param ts
     *            time-stamp which should be transported by
     *            this message
     */
    public PingMessage(long ts)
    {
        this();
        this.ts = ts;
    }

    /** {@inheritDoc} */
    @Override
    protected boolean decodeParams(Buffer buffer)
    {
        ts = buffer.decodeLong();
        return true;
    }

    /** {@inheritDoc} */
    @Override
    protected boolean encodeParams(Buffer buffer)
    {
        buffer.encode(ts);
        return true;
    }

    /**
     * Returns time-stamp transported by this message.
     * @return time-stamp transported by this message.
     */
    public long getTS()
    {
        return ts;
    }

    /** {@inheritDoc} */
    @Override
    protected int innerGetParamsLength()
    {
        return SizeOf.LONG;
    }
}
