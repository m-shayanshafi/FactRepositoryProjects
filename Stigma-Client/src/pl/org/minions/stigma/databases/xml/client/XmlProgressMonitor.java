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
package pl.org.minions.stigma.databases.xml.client;

import java.net.URL;
import java.net.URLConnection;

import pl.org.minions.utils.streams.CountingInputStream;

/**
 * Represents monitored XML processing.
 */
public class XmlProgressMonitor
{
    private URL url;
    private long totalSize;
    private URLConnection connection;
    private CountingInputStream stream;

    /**
     * Constructor.
     * @param url
     *            URL of processed XML file
     * @param stream
     *            stream with contents of file
     * @param connection
     *            connection to XML file (needed for
     *            acquiring contents length
     */
    XmlProgressMonitor(URL url,
                       URLConnection connection,
                       CountingInputStream stream)
    {
        this.url = url;
        this.stream = stream;
        this.connection = connection;
    }

    /**
     * Returns URL of processing XML file.
     * @return URL of processing XML file.
     */
    public URL getUrl()
    {
        return url;
    }

    /**
     * Returns total size of processing file. May return
     * {@code 0} if proper data not ready yet, or protocol
     * does not support it.
     * @return total size of processing file
     */
    public long getTotalSize()
    {
        if (totalSize == 0)
            totalSize = connection.getContentLength();
        return totalSize;
    }

    /**
     * Returns already downloaded/processed amount of bytes.
     * @return already downloaded/processed amount of bytes.
     */
    public long getDownloadedSize()
    {
        return stream.getReadBytes();
    }

}
