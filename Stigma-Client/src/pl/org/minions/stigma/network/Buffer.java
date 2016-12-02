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

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Arrays;

import pl.org.minions.stigma.globals.Position;
import pl.org.minions.stigma.globals.SizeOf;
import pl.org.minions.utils.logger.Log;

/**
 * Represents message frame as a vector of bytes. Every
 * frame starts with 2 bytes of type, and then contains
 * coded contents on bytes. Coding and decoding is realized
 * using this class (wrapper of {@link ByteBuffer}).
 */
public class Buffer
{
    static final int HEADER_SIZE = SizeOf.SHORT;

    private static final String STRING_ENCODING = "UTF-8";

    private ByteBuffer buffer;

    /**
     * Creates frame from read bytes buffer. Used for
     * decoding.
     * @param buffer
     *            buffer to read (decode) from
     */
    public Buffer(ByteBuffer buffer)
    {
        this.buffer = buffer;
    }

    /**
     * Creates buffer, reserves given amount of bytes
     * (buffer can be easily lengthened, and cropped to real
     * byte usage) and assigns it a message type. Used for
     * coding.
     * @param size
     *            planned size of buffer (if it's to small,
     *            buffer will enlarge itself, if it's to
     *            big, buffer will crop itself when
     *            extracting bytes. Giving good amount of
     *            bytes optimizes coding process.)
     */
    public Buffer(int size)
    {
        buffer = ByteBuffer.allocateDirect(size + HEADER_SIZE);
        buffer.putShort((short) 0);
    }

    /**
     * Some heuristic. Return maximum (pessimistic) amount
     * of bytes that can be used to store given string in
     * current coding. It's used just to optimize buffer
     * length on creation time.
     * @param s
     *            string to calculate bytes count
     * @return maximum bytes count needed to store given
     *         string
     */
    public static int stringBytesCount(String s)
    {
        if (s == null)
            return 2;
        return s.length() * 2 + 2; // this should be enough
    }

    /**
     * Returns string decoded from given bytes.
     * @param txt
     *            bytes containing string
     * @return string decoded from given bytes.
     */
    public static final String decodeString(byte[] txt)
    {
        try
        {
            String s = new String(txt, STRING_ENCODING);
            return s;
        }
        catch (UnsupportedEncodingException e)
        {
            Log.logger.error("Buffer.decodeString", e);
            return "";
        }
    }

    /**
     * Reads boolean (one byte) from buffer.
     * @return decoded boolean
     */
    public boolean decodeBoolean()
    {
        return buffer.get() != 0;
    }

    /**
     * Reads one byte from buffer.
     * @return decoded byte
     */
    public byte decodeByte()
    {
        return buffer.get();
    }

    /**
     * Reads random length bytes vector. Length is assumed
     * to be stored as short number at the beginning of
     * vector.
     * @see #encode(byte[])
     * @return decoded bytes vector
     */
    public byte[] decodeBytes()
    {
        short len = decodeShort();
        byte[] b = new byte[len];
        buffer.get(b);
        return b;
    }

    /**
     * Reads 4 byte integer number from buffer.
     * @return decoded number
     */
    public int decodeInt()
    {
        return buffer.getInt();
    }

    /**
     * Reads 8 byte integer number from buffer.
     * @return decoded number
     */
    public long decodeLong()
    {
        return buffer.getLong();
    }

    /**
     * Decodes {@link Position Position} class. his class is
     * often used in messages so it has its own
     * encode/decode function.
     * @return decoded position
     */
    public Position decodePosition()
    {
        return new Position(decodeShort(), decodeShort());
    }

    /**
     * Decodes 2 byte integer number from buffer.
     * @return decoded number
     */
    public short decodeShort()
    {
        return buffer.getShort();
    }

    /**
     * Reads string from buffer.
     * @see #encode(String)
     * @return decoded string
     */
    public String decodeString()
    {
        return decodeString(decodeBytes());
    }

    /**
     * Reads and decrypt string from buffer.
     * @return decrypted string
     * @see #encryptAndEncodeString(String)
     */
    public String decryptAndDecodeString()
    {
        try
        {
            byte[] txt = decodeBytes();
            txt = Cipher.decrypt(txt);
            String s = new String(txt, STRING_ENCODING);
            return s;
        }
        catch (UnsupportedEncodingException e)
        {
            Log.logger.error("Buffer.decryptAndDecodeString", e);
            return "";
        }
    }

    /**
     * Decodes value of given enum. Assumes that decoded
     * byte is proper ordinal like in {@link Enum#ordinal()}
     * .
     * @param <T>
     *            type of enum
     * @param clazz
     *            enum class (needed for dynamic accessing
     *            enum values)
     * @return decoded value
     */
    public <T extends Enum<T>> T decodeEnum(Class<T> clazz)
    {
        return clazz.getEnumConstants()[decodeByte()];
    }

    /**
     * Encodes boolean value as single byte.
     * @param b
     *            coded boolean
     */
    public void encode(boolean b)
    {
        buffer.put(b ? (byte) 1 : (byte) 0);

    }

    /**
     * Encodes single byte.
     * @param b
     *            coded byte
     */
    public void encode(byte b)
    {
        buffer.put(b);
    }

    /**
     * Encodes bytes vector. Vectors length should be
     * shorter than 2 {@value java.lang.Short#MAX_VALUE}.
     * Enlarges buffer if needed.
     * @param b
     *            vector to encode
     */
    public void encode(byte[] b)
    {
        encode((short) b.length);
        buffer.put(b);
    }

    /**
     * Encodes 4 byte integer number.
     * @param i
     *            coded number
     */
    public void encode(int i)
    {
        buffer.putInt(i);
    }

    /**
     * Encodes 8 byte integer number.
     * @param l
     *            coded number
     */
    public void encode(long l)
    {
        buffer.putLong(l);
    }

    /**
     * Encode (@link Position Position} class. This class is
     * often used in messages so it has its own
     * encode/decode function.
     * @param p
     *            Position to encode
     */
    public void encode(Position p)
    {
        encode(p.getX());
        encode(p.getY());
    }

    /**
     * Encodes 2 byte integer number.
     * @param s
     *            coded number
     */
    public void encode(short s)
    {
        buffer.putShort(s);
    }

    /**
     * Encodes string. String should be shorter than 2*
     * {@value java.lang.Short#MAX_VALUE}.
     * @param s
     *            coded string
     */
    public void encode(String s)
    {
        if (s == null || s.isEmpty())
        {
            encode((short) 0);
            return;
        }

        try
        {
            byte[] txt = s.getBytes(STRING_ENCODING);
            encode(txt);
        }
        catch (UnsupportedEncodingException e)
        {
            Log.logger.error("Buffer.encode", e);
        }
    }

    /**
     * Encodes enum. Uses {@link Enum#ordinal()} and casts
     * it to byte.
     * @param <T>
     *            type of enum
     * @param enumValue
     *            value to encode
     */
    public <T extends Enum<T>> void encode(T enumValue)
    {
        encode((byte) enumValue.ordinal());
    }

    /**
     * Encrypts and encodes String.
     * @see Cipher
     * @see #encode(String)
     * @param s
     *            string for encryption and coding
     */
    public void encryptAndEncodeString(String s)
    {
        try
        {
            byte[] txt = s.getBytes(STRING_ENCODING);
            txt = Cipher.encrypt(txt);
            encode(txt);
        }
        catch (UnsupportedEncodingException e)
        {
            Log.logger.error("Buffer.encryptAndEncodeString", e);
        }
    }

    /**
     * Gets frame (as {@link ByteBuffer}) ready to be send
     * via network. Crops unused bytes, encodes frame length
     * etc.
     * @return buffer containing frame
     * @see ByteBuffer
     */
    public ByteBuffer getByteBuffer()
    {
        short size = (short) (buffer.position() - HEADER_SIZE);
        buffer.putShort(0, size);
        buffer.flip();
        if (Log.isTraceEnabled())
        {
            Log.logger.trace("Sending msg length: " + size);
            byte[] tmp = new byte[size + HEADER_SIZE];
            buffer.get(tmp);
            Log.logger.trace("Message out: " + Arrays.toString(tmp));
            buffer.flip();
        }
        return buffer;
    }
}
