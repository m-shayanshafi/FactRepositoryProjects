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
 * System message. Carries type of event of which player
 * should be informed.
 */
public class SystemMessage extends NetworkMessage
{
    /**
     * Type of event that taken place and of which player
     * should be informed.
     */
    public enum SystemEventType
    {
        WORLD_SAVED, ACTOR_SAVED, LOGGED_OUT,
    };

    // speeds up decoding
    private SystemEventType eventType;

    /**
     * Constructor. Creates empty message - used for
     * decoding.
     */
    public SystemMessage()
    {
        super(NetworkMessageType.SYSTEM_MESSAGE);
    }

    /**
     * Constructor.
     * @param eventType
     *            type of event
     */
    public SystemMessage(SystemEventType eventType)
    {
        this();
        this.eventType = eventType;
    }

    /** {@inheritDoc} */
    @Override
    protected boolean decodeParams(Buffer buffer)
    {
        eventType = buffer.decodeEnum(SystemEventType.class);
        return true;
    }

    /** {@inheritDoc} */
    @Override
    protected boolean encodeParams(Buffer buffer)
    {
        buffer.encode(eventType);
        return true;
    }

    /**
     * Returns event type transported by this message.
     * @return event type transported by this message.
     */
    public SystemEventType getSystemEventType()
    {
        return eventType;
    }

    /** {@inheritDoc} */
    @Override
    protected int innerGetParamsLength()
    {
        return SizeOf.BYTE;
    }
}
