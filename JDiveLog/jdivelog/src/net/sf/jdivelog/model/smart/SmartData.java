/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: AladinData.java
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

import java.io.*;
import java.util.*;

import net.sf.jdivelog.ci.SmartComputerType;

/**
 * Description: container for the contents of an Uwatec Smart log file.
 * 
 * For further information see <a href="http://diversity.sourceforge.net/uwatec_smart_format.html">Uwatec Smart Dive Computer Data Format &amp; Protocol</a>
 * @author Andr&eacute; Schenk
 * @version $Revision: 159 $
 */
public class SmartData
{
	private final SmartComputerType computerType;

    private List <LogEntry> logbook = new Vector <LogEntry> ();

    public SmartData (SmartComputerType computerType, byte [] read_data)
    {
    	this.computerType = computerType;
        parseData (read_data);
    }

    public SmartData (SmartComputerType computerType, String fileName)
        throws IOException
    {
    	this.computerType = computerType;
        if (fileName == null) {
            throw new IllegalArgumentException ("file name = null");
        }

        DataInputStream in = new DataInputStream
            (new BufferedInputStream (new FileInputStream (fileName)));
        // skip 4 bytes (data length)
        in.skipBytes (4);

        int fileLength = (int) new File (fileName).length ();
        byte [] read_data = new byte [fileLength - 4];

        for (int index = 0; index < read_data.length; index++) {
            read_data [index] = (byte) in.read ();
        }
        parseData (read_data);
        in.close ();
    }

    public List <LogEntry> getLogbook ()
    {
        return logbook;
    }

    private void parseData (byte [] read_data)
    {
        int offset = 4; // skip 4 bytes (data length)

        do {
            LogEntry log = null;

            if (computerType == SmartComputerType.ALADIN_TEC_2G) {
            	log = new AladinTecLogEntry (read_data, offset);
            }
            else {
            	throw new IllegalArgumentException
            	("computer model \"" + computerType + "\" not supported yet");
            }
            logbook.add (log);
            offset += (int) log.diveDataLength;
        }
        while (offset < read_data.length);
    }

    public String toString ()
    {
        return logbook.toString ();
    }
}
