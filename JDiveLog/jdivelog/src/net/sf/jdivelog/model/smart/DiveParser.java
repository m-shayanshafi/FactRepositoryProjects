/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: DiveParser.java
 *
 * @author Andr&eacute; Schenk <andre_schenk@users.sourceforge.net>
 *
 * This file is part of JDiveLog.
 * JDiveLog is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.

 * JDiveLog is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with JDiveLog; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package net.sf.jdivelog.model.smart;

/**
 * Description: Basic class which provides some conversion routines.
 *
 * @author Andr&eacute; Schenk
 * @version $Revision: 423 $
 */
public class DiveParser
{
    public static long ByteToU32 (byte abyte0 [])
    {
        long l = abyte0 [3];
        l &= 255L;
        long l1 = abyte0 [2];
        l1 &= 255L;
        long l2 = abyte0 [1];
        l2 &= 255L;
        long l3 = abyte0 [0];
        l3 &= 255L;
        long l4 = l;
        l4 <<= 8;
        l4 |= l1;
        l4 <<= 8;
        l4 |= l2;
        l4 <<= 8;
        l4 |= l3;
        return l4;
    }

    protected static int ByteToU17 (byte abyte0 [])
    {
        short word0 = abyte0 [2];
        word0 &= 0x01;
        short word1 = abyte0 [1];
        word1 &= 0xff;
        short word2 = abyte0 [0];
        word1 &= 0xff;
        short word3 = word0;
        word3 <<= 8;
        word3 |= word1;
        word3 <<= 8;
        word3 |= word2;
        return word3;
    }

    protected static int ByteToU16 (byte abyte0 [])
    {
        int i = abyte0 [1];
        i &= 0xff;
        int j = abyte0 [0];
        j &= 0xff;
        int k = i;
        k <<= 8;
        k |= j;
        return k;
    }

    protected static short ByteToS16 (byte abyte0 [])
    {
        short word0 = abyte0 [1];
        word0 &= 0xff;
        short word1 = abyte0 [0];
        word1 &= 0xff;
        short word2 = word0;
        word2 <<= 8;
        word2 |= word1;
        return word2;
    }

    protected static short ByteToS11 (byte abyte0 [])
    {
        short word0 = abyte0 [0];
        word0 = (short) ((word0 & 7) << 8);
        short word1 = abyte0 [1];
        word1 &= 0xff;
        short word2 = (short) (word0 | word1);
        word2 = (short) (word2 & 0x3ff | word2 << 5 & 0x8000);
        if (word2 < 0) {
            word2 += 31744;
        }
        return word2;
    }

    protected static short ByteToS10 (byte abyte0 [])
    {
        short word0 = abyte0 [0];
        word0 = (short) ((word0 & 3) << 8);
        short word1 = abyte0 [1];
        word1 &= 0xff;
        short word2 = (short) (word0 | word1);
        word2 = (short) (word2 & 0x1ff | word2 << 6 & 0x8000);
        if (word2 < 0) {
            word2 += 32256;
        }
        return word2;
    }

    protected static short ByteToU8 (byte byte0)
    {
        short word0 = byte0;
        word0 &= 0xff;
        return word0;
    }

    protected static byte ByteToS7 (byte byte0)
    {
        byte byte1 = (byte) (byte0 & 0x3f | byte0 << 1 & 0x80);
        if (byte1 < 0) {
            byte1 += 64;
        }
        return byte1;
    }

    protected static byte ByteToS6 (byte byte0)
    {
        byte byte1 = (byte) (byte0 & 0x1f | byte0 << 2 & 0x80);
        if (byte1 < 0) {
            byte1 += 96;
        }
        return byte1;
    }

   protected static int readInt (byte [] read_data, int offset)
    {
        byte [] bytes = new byte [2];

        System.arraycopy (read_data, offset, bytes, 0, bytes.length);
        return ByteToS16 (bytes);
    }

    protected static int readUInt (byte [] read_data, int offset)
    {
        byte [] bytes = new byte [2];

        System.arraycopy (read_data, offset, bytes, 0, bytes.length);
        return ByteToU16 (bytes);
    }

    protected static long readULong (byte [] read_data, int offset)
    {
        byte [] bytes = new byte [4];

        System.arraycopy (read_data, offset, bytes, 0, bytes.length);
        return ByteToU32 (bytes);
    }

    protected static short readUShort (byte [] read_data, int offset)
    {
        byte [] bytes = new byte [1];

        System.arraycopy (read_data, offset, bytes, 0, bytes.length);
        return ByteToU8 (bytes [0]);
    }
}
