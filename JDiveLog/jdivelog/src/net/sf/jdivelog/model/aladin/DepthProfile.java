/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: DepthProfile.java
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
package net.sf.jdivelog.model.aladin;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: container for one depth profile of an Aladin log file
 * 
 * @author Andr&eacute; Schenk
 * @version $Revision: 822 $
 */
public class DepthProfile
{
    public final Float startTemperature;
    private final List<DepthProfileEntry> depthProfileEntries =
        new ArrayList<DepthProfileEntry> ();

    public DepthProfile (int [] bytes, int aladinType)
    {
        if ((bytes == null) || (bytes.length < 1)) {
            throw new IllegalArgumentException
                ("byte array for depth profile is invalid");
        }

        startTemperature = new Float ((bytes [1] * 25) / 100.0);

        // skip information for decompression
        int index = 23;

        if ((aladinType == 0xf4) || (aladinType == 0xff)) {
            // Aladin Nitrox (not O2)
            index += 2;
        }
        else if (aladinType == 0xa4) {
            // Aladin O2
            index += 3;
        }

        // read body of depth profile
        while (index < bytes.length - 7) {
            int [] entryBuffer = new int [7];

            System.arraycopy
                (bytes, index, entryBuffer, 0, entryBuffer.length);
            depthProfileEntries.add (new DepthProfileEntry (entryBuffer));
            index += entryBuffer.length;

            // Aladin O2 has extra one byte, which represents 02 mix %
            if (aladinType == 0xa4) {
                index++;
            }
        }
    }

    public List<DepthProfileEntry> getProfileEntries ()
    {
        return depthProfileEntries;
    }

    public String toString ()
    {
        return
            "startTemperature: " + startTemperature + "\n" +
            "depthProfileEntries: " + depthProfileEntries + "\n";
    }
}
