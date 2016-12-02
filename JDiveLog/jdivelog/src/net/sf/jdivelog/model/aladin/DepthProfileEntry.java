/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: DepthProfileEntry.java
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

/**
 * Description: container for one depth profile entry of an Aladin log file
 * 
 * @author Andr&eacute; Schenk
 * @version $Revision: 548 $
 */
public class DepthProfileEntry
{
    public final Float depth20;
    public final int   warnings20;
    public final Float depth40;
    public final int   warnings40;
    public final Float depth00;
    public final int   warnings00;
    public final int   decompression;

    public DepthProfileEntry (int [] bytes)
    {
        if ((bytes == null) || (bytes.length < 7)) {
            throw new IllegalArgumentException
                ("byte array for depth profile entry is invalid");
        }
        depth20       = new Float ((((bytes [0] * 256 + bytes [1]) >> 6) * 1000
                                    / 64) / 100.0);
        warnings20    = bytes [1] & 0x3f;
        depth40       = new Float ((((bytes [2] * 256 + bytes [3]) >> 6) * 1000
                                    / 64) / 100.0);
        warnings40    = bytes [3] & 0x3f;
        depth00       = new Float ((((bytes [4] * 256 + bytes [5]) >> 6) * 1000
                                    / 64) / 100.0);
        warnings00    = bytes [5] & 0x3f;
        decompression = bytes [6];
    }

    public String toString ()
    {
        return
            "depth20: "       + depth20       + " m\n" +
            "warnings20: "    + warnings20    + "\n" +
            "depth40: "       + depth40       + " m\n" +
            "warnings40: "    + warnings40    + "\n" +
            "depth00: "       + depth00       + " m\n" +
            "warnings00: "    + warnings00    + "\n" +
            "decompression: " + decompression + "\n";
    }
}
