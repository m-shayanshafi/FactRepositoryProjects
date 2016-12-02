//package edu.emory.mathcs.util.io;
package jtarot;

import java.io.*;

/**
 * Output stream that base64-encodes supplied data on-the-fly, that is, it
 * transforms a byte stream into a base64-encoded character stream.
 * It also offers static methods for offline encoding, i.e. converting byte
 * arrays to base64-encoded strings.
 *
 * @author Dawid Kurzyniec
 * @version 1.0
 */

public class Base64Encoder extends OutputStream {
    final Writer out;
    int pendingBytes = 0;
    byte[] fragBuf = new byte[3];
    volatile boolean closed = false;

    public Base64Encoder(Writer out) {
        this.out = out;
    }

    public void write(byte[] buf, int off, int len) throws IOException {
        int max = off+len;
        ensureOpened();
        if (pendingBytes > 0) {
            // finish up the pending triplet
            while (off<max && pendingBytes<3) {
                fragBuf[pendingBytes++] = buf[off++];
            }
            if (pendingBytes<3) {
                // it was a short array indeed
                return;
            }
            writeBase64(fragBuf, 0, 3);
            pendingBytes = 0;
        }
        // process triplets directly from the buf[]
        while (off+3 <= max) {
            writeBase64(buf, off, 3);
            off+=3;
        }
        // tail
        while (off < max) {
            fragBuf[pendingBytes++] = buf[off++];
        }
    }

    public void write(int v) throws IOException {
        ensureOpened();
        fragBuf[pendingBytes++] = (byte)v;
        if (pendingBytes == 3) {
            writeBase64(fragBuf, 0, 3);
            pendingBytes = 0;
        }
    }

    /**
     * Flushes the underlying stream. Because base64 operates on
     * triples of bytes, this method does NOT guarantee to propagate all bytes
     * written thus far. Up to two bytes may remain in the internal buffer,
     * awaiting the next byte to complete the triple.
     *
     * @throws IOException if I/O error occurs
     */
    public void flush() throws IOException {
	    if(pendingBytes>0){
		    writeBase64(fragBuf, 0, pendingBytes);
		    pendingBytes = 0;
	    }
        out.flush();
        // we cannot really do anything about the fragBuf here
    }

    public void close() throws IOException {
        if (pendingBytes > 0) {
            writeBase64(fragBuf, 0, pendingBytes);
        }
        out.flush();
        out.close();
    }

    private void ensureOpened() throws IOException {
        if (closed) { throw new java.io.IOException("Stream closed"); }
    }

    private void writeBase64(byte[] b, int off, int len) throws IOException {
        if (len == 3) {
            out.write(intToBase64[(b[off+0] & 0xff) >> 2]);
            out.write(intToBase64[((b[off+0] & 0xff) << 4) & 0x3f | ((b[off+1] & 0xff) >> 4)]);
            out.write(intToBase64[((b[off+1] & 0xff) << 2) & 0x3f | ((b[off+2] & 0xff) >> 6)]);
            out.write(intToBase64[b[off+2] & 0x3f]);
        }
        else if (len == 2) {
            out.write(intToBase64[(b[off+0] & 0xff) >> 2]);
            out.write(intToBase64[((b[off+0] & 0xff) << 4) & 0x3f | ((b[off+1] & 0xff) >> 4)]);
            out.write(intToBase64[((b[off+1] & 0xff) << 2) & 0x3f]);
            out.write('=');
        }
        else if (len == 1) {
            out.write(intToBase64[(b[off+0] & 0xff) >> 2]);
            out.write(intToBase64[((b[off+0] & 0xff) << 4) & 0x3f]);
            out.write("==");
        }
    }

    /**
     * Converts the specified byte array into its base64 encoding.
     *
     * @param buf the buffer to encode
     * @return the base64-encoded data
     */
    public static String encode(byte[] buf) {
        return encode(buf, 0, buf.length);
    }

    /**
     * Converts the specified byte array region into its base64 encoding.
     *
     * @param buf the buffer containing the data to encode
     * @param off the start offset in the buffer
     * @param len the number of bytes to encode
     * @return the base64-encoded data
     */
    public static String encode(byte[] buf, int off, int len) {
        StringWriter sw = new StringWriter(buf.length*4/3+2);
        Base64Encoder enc = new Base64Encoder(sw);
        try {
            enc.write(buf, off, len);
            enc.flush();
            enc.close();
        }
        catch (IOException e) {
            // can't happen from the code of StringWriter and Base64Encoder
            throw new RuntimeException(e);
        }
        return sw.getBuffer().toString();
    }

    /**
     * This array is a lookup table that translates 6-bit positive integer
     * index values into their "Base64 Alphabet" equivalents as specified
     * in Table 1 of RFC 2045.
     */
    private static final char intToBase64[] = {
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
        'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
        'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
        'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'
    };
}
