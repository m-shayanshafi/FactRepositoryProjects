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

import pl.org.minions.stigma.globals.SizeOf;
import pl.org.minions.stigma.network.Buffer;
import pl.org.minions.stigma.network.messaging.NetworkObject;
import pl.org.minions.stigma.network.messaging.NetworkObjectCodec;

/**
 * Class representing in-game chat message. For server
 * optimization - it does not decode message string unleast
 * it is necessary.
 */
public abstract class Chat extends NetworkObject<ChatType>
{
    private static final NetworkObjectCodec<ChatType, Chat> CODEC =
            new NetworkObjectCodec<ChatType, Chat>(ChatType.class);

    private int senderId;
    private String text;
    private byte[] textBytes;

    /**
     * Constructor.
     * @param type
     *            chat type
     * @param text
     *            chat message
     */
    protected Chat(ChatType type, String text)
    {
        super(type);
        this.text = text;
    }

    /**
     * Returns codec.
     * @return codec
     */
    public static NetworkObjectCodec<ChatType, Chat> getCodec()
    {
        return CODEC;
    }

    /**
     * Decodes recipients of this message.
     * @param buffer
     *            buffer to decode from
     * @return {@code true} on success
     */
    protected abstract boolean decodeRecipients(Buffer buffer);

    /**
     * Encodes recipients of this message.
     * @param buffer
     *            buffer to encode to
     * @param toClient
     *            when {@code true} this message is going to
     *            client, so no recipients list is really
     *            needed, just stub.
     * @return {@code true} on success.
     */
    protected abstract boolean encodeRecipients(Buffer buffer, boolean toClient);

    /**
     * Returns size in bytes of recipients list.
     * @return size in bytes of recipients list.
     */
    protected abstract int recipientsSize();

    /**
     * Returns collection of identifiers of actors -
     * recipients of this message.
     * @return recipients' identifiers collection
     */
    public abstract Collection<Integer> getRecipients();

    /** {@inheritDoc} */
    @Override
    protected boolean decodeParams(Buffer buffer)
    {
        senderId = buffer.decodeInt();
        textBytes = buffer.decodeBytes();
        return decodeRecipients(buffer);
    }

    /** {@inheritDoc} */
    @Override
    protected boolean encodeParams(Buffer buffer)
    {
        buffer.encode(senderId);
        boolean toClient = textBytes != null;
        if (toClient)
            buffer.encode(textBytes);
        else
        {
            assert text != null;
            buffer.encode(text);
        }

        return encodeRecipients(buffer, toClient);
    }

    /** {@inheritDoc} */
    @Override
    protected int innerGetParamsLength()
    {
        int r = SizeOf.INT;
        if (textBytes != null)
            r += SizeOf.SHORT + textBytes.length;
        else
        {
            assert text != null;
            r += Buffer.stringBytesCount(text);
        }
        return r + recipientsSize();
    }

    /**
     * Returns senderId.
     * @return senderId
     */
    public int getSenderId()
    {
        return senderId;
    }

    /**
     * Sets new value of senderId.
     * @param senderId
     *            the senderId to set
     */
    public void setSenderId(int senderId)
    {
        this.senderId = senderId;
    }

    /**
     *Returns text of this chat. If this message came from
     * network this method will decode string, so it
     * shouln't be called on server, and only once on
     * client.
     * @return text of this chat
     */
    public String getText()
    {
        if (text == null && textBytes != null)
            return Buffer.decodeString(textBytes);
        return text;
    }

    /**
     * Returns bytes of message transported by this message.
     * Just text bytes. Returns {@code null} if message was
     * not decoded from network.
     * @return bytes of this message's text
     */
    public byte[] getTextBytes()
    {
        return textBytes;
    }

}
