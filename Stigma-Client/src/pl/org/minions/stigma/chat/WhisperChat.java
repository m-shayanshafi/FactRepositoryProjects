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
package pl.org.minions.stigma.chat;

import java.util.Collection;
import java.util.Collections;

import pl.org.minions.stigma.globals.SizeOf;
import pl.org.minions.stigma.network.Buffer;

/**
 * Class representing one-to-one whisper chat.
 */
public class WhisperChat extends Chat
{

    private int receiverId;

    /**
     * Constructor.
     * @param receiverId
     *            recipient of this whisper
     * @param text
     *            whisper message
     */
    public WhisperChat(int receiverId, String text)
    {
        super(ChatType.WHISPER, text);
        this.receiverId = receiverId;
    }

    /**
     * Factory method for decoding.
     * @return empty class instance
     */
    public static WhisperChat create()
    {
        return new WhisperChat(0, null);
    }

    /** {@inheritDoc} */
    @Override
    protected boolean decodeRecipients(Buffer buffer)
    {
        receiverId = buffer.decodeInt();
        return true;
    }

    /** {@inheritDoc} */
    @Override
    protected boolean encodeRecipients(Buffer buffer, boolean toClient)
    {
        // if toClient: encode(0) would be a little overkill...
        buffer.encode(receiverId);
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public Collection<Integer> getRecipients()
    {
        return Collections.singleton(receiverId);
    }

    /** {@inheritDoc} */
    @Override
    protected int recipientsSize()
    {
        return SizeOf.INT;
    }

}
