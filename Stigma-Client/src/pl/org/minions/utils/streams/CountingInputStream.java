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
package pl.org.minions.utils.streams;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Wrapper stream that counts read bytes.
 */
public class CountingInputStream extends FilterInputStream
{
    private long readBytes;
    private long skippedBytes;

    /**
     * Constructor.
     * @param in
     *            input stream to be wrapped
     */
    public CountingInputStream(InputStream in)
    {
        super(in);
    }

    /** {@inheritDoc} */
    @Override
    public int read() throws IOException
    {
        int ret = super.read();
        if (ret > 0)
            ++readBytes;
        return ret;
    }

    /** {@inheritDoc} */
    @Override
    public int read(byte[] b, int off, int len) throws IOException
    {
        int ret = super.read(b, off, len);
        if (ret > 0)
            readBytes += ret;
        return ret;
    }

    /** {@inheritDoc} */
    @Override
    public long skip(long n) throws IOException
    {
        long ret = super.skip(n);
        if (ret > 0)
        {
            skippedBytes += ret;
            readBytes += ret;
        }
        return ret;
    }

    /**
     * Returns amount of bytes read or skipped.
     * @return amount of bytes read or skipped.
     */
    public long getReadBytes()
    {
        return readBytes;
    }

    /**
     * Returns amount of bytes skipped.
     * @return amount of bytes skipped.
     */
    public long getSkippedBytes()
    {
        return skippedBytes;
    }

}
