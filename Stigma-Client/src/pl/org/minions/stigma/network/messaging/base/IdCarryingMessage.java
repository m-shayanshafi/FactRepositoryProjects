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

import pl.org.minions.stigma.globals.SizeOf;
import pl.org.minions.stigma.network.Buffer;
import pl.org.minions.stigma.network.messaging.NetworkMessage;
import pl.org.minions.stigma.network.messaging.NetworkMessageType;

/**
 * Abstract class used as super class for all network
 * messages containing only id (id of anything - map, actor,
 * player).
 */
public abstract class IdCarryingMessage extends NetworkMessage
{
    private int id;

    /**
     * Constructor - creates empty message, with bad id.
     * @param type
     *            message type
     */
    public IdCarryingMessage(NetworkMessageType type)
    {
        super(type);
        this.id = -1;
    }

    /**
     * Constructor.
     * @param type
     *            message type - for children classes
     * @param id
     *            carried id
     */
    public IdCarryingMessage(NetworkMessageType type, int id)
    {
        super(type);
        this.id = id;
    }

    /** {@inheritDoc} */
    @Override
    protected boolean decodeParams(Buffer buffer)
    {
        id = buffer.decodeInt();
        return true;
    }

    /** {@inheritDoc} */
    @Override
    protected boolean encodeParams(Buffer buffer)
    {
        buffer.encode(id);
        return true;
    }

    /**
     * Returns id carried by message.
     * @return id carried by message
     */
    public int getId()
    {
        return id;
    }

    /** {@inheritDoc} */
    @Override
    protected int innerGetParamsLength()
    {
        return SizeOf.INT;
    }
}
