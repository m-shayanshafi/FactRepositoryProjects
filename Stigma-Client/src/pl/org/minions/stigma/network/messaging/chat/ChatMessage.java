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
package pl.org.minions.stigma.network.messaging.chat;

import pl.org.minions.stigma.chat.Chat;
import pl.org.minions.stigma.network.Buffer;
import pl.org.minions.stigma.network.messaging.NetworkMessage;
import pl.org.minions.stigma.network.messaging.NetworkMessageType;

/**
 * Network message wrapping chat message.
 */
public class ChatMessage extends NetworkMessage
{
    private Chat msg;

    /**
     * Constructor.
     * @param msg
     *            message to wrap
     */
    public ChatMessage(Chat msg)
    {
        super(NetworkMessageType.CHAT);
        this.msg = msg;
    }

    /**
     * Factory method for decoding.
     * @return empty class instance
     */
    public static ChatMessage create()
    {
        return new ChatMessage(null);
    }

    /** {@inheritDoc} */
    @Override
    protected boolean decodeParams(Buffer buffer)
    {
        msg = Chat.getCodec().decode(buffer);
        return msg != null;
    }

    /** {@inheritDoc} */
    @Override
    protected boolean encodeParams(Buffer buffer)
    {
        return Chat.getCodec().encode(msg, buffer);
    }

    /** {@inheritDoc} */
    @Override
    protected int innerGetParamsLength()
    {
        return msg.getParamsLength();
    }

    /**
     * Returns chat transported by this message.
     * @return chat transported by this message.
     */
    public Chat getChat()
    {
        return msg;
    }

}
