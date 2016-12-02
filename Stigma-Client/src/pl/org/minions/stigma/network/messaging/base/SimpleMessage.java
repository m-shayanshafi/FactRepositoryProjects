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
package pl.org.minions.stigma.network.messaging.base;

import pl.org.minions.stigma.network.Buffer;
import pl.org.minions.stigma.network.messaging.NetworkMessage;
import pl.org.minions.stigma.network.messaging.NetworkMessageType;

/**
 * Class provided to simplify implementation. Represents
 * message containing only type (so it's normal message) but
 * with support for transportation via network.
 */
public abstract class SimpleMessage extends NetworkMessage
{
    /**
     * Constructor.
     * @param type
     *            type of message
     */
    public SimpleMessage(NetworkMessageType type)
    {
        super(type);
    }

    /** {@inheritDoc} */
    @Override
    protected final boolean decodeParams(Buffer buffer)
    {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    protected final boolean encodeParams(Buffer buffer)
    {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    protected final int innerGetParamsLength()
    {
        return 0;
    }

}
