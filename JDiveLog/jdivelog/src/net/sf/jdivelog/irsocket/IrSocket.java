/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: IrSocket.java
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
package net.sf.jdivelog.irsocket;

import java.io.*;

import net.sf.jdivelog.ci.UnsupportedOSException;

/**
 * Description: Abstraction layer for infrared suppport independent from the
 * operating system. So far only the Linux part has been implemented.
 * 
 * @author Andr&eacute; Schenk
 * @version $Revision: 548 $
 */
public class IrSocket
{
    private enum OperatingSystem
    {
        UNKNOWN, WINDOWS, DARWIN, LINUX;
    }

    private static final String NICKNAMES [] = {
        "Aladin Smart Pro", "Aladin Smart Com", "Aladin Smart Tec",
        "Aladin Smart Z", "Uwatec Aladin"
    };

    private final hp.infrared.IrSocket impl;

    /**
     * Create a new IR socket and connect it to the specified port number.
     *
     * @param remotePort port number
     * @throws IOException if an I/O error occurs when creating the socket.
     * @throws UnsupportedOSException if the current operating system isn't
     *                                supported
     */
    public IrSocket (int remotePort) throws IOException, UnsupportedOSException
    {
        if (getOperatingSystem () == OperatingSystem.LINUX) {
            impl = new hp.infrared.IrSocket (NICKNAMES, remotePort);
        }
        else {
            throw new UnsupportedOSException ();
        }
    }

    /**
     * Close this socket.
     *
     * @throws IOException if an I/O error occurs when shutting down this socket.
     */
    public void close () throws IOException
    {
        impl.close ();
    }

    /**
     * Return an input stream for this socket.
     *
     * @return an input stream for reading bytes from this socket.
     * @throws IOException if an I/O error occurs when creating the input
     *                     stream, the socket is closed or the socket is not
     *                     connected.
     */
    public InputStream getInputStream () throws IOException
    {
        return impl.getInputStream ();
    }

    /**
     * Return an output stream for this socket.
     *
     * @return an output stream for writing bytes to this socket.
     * @throws IOException if an I/O error occurs when creating the output
     *                     stream or if the socket is not connected.
     */
    public OutputStream getOutputStream () throws IOException
    {
        return impl.getOutputStream ();
    }

    /**
     * Get the current operating system.
     *
     * @return current operating system
     */
    private OperatingSystem getOperatingSystem ()
    {
        OperatingSystem result = OperatingSystem.UNKNOWN;
        String name = System.getProperty ("os.name");
        String version = System.getProperty ("os.version");

        if (name != null) {
            if (name.indexOf ("Windows") != -1) {
                try {
                    double d = Double.valueOf (version).doubleValue ();

                    if (d >= 95) {
                        result = OperatingSystem.WINDOWS;
                    }
                }
                catch (NumberFormatException exception) {
                }
            }
            else if ((name.indexOf ("Mac OS X") != -1) ||
                     (name.indexOf ("Darwin") != -1)) {
                result = OperatingSystem.DARWIN;
            }
            else if (name.indexOf ("Linux") != -1) {
                result = OperatingSystem.LINUX;
            }
        }
        return result;
    }
}
