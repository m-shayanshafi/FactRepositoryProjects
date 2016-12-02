/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: CurrentStatus.java
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

import java.util.*;

/**
 * Description: container for the "current status" section of an Aladin log
 * file
 * 
 * @author Andr&eacute; Schenk
 * @version $Revision: 548 $
 */
public class CurrentStatus
{
    public final int remainingBattery;
    public final int totalDiveNumbers;
    public final int logBookOffset;
    public final int numberOfDiveProfiles;
    public final int endOfProfileBuffer;
    public final Date currentTime;
    public final int checkSum;

    public CurrentStatus (int [] bytes)
    {
        if ((bytes == null) || (bytes.length < 14)) {
            throw new IllegalArgumentException ("need 14 bytes for current status");
        }
        remainingBattery     = bytes [0] * 99 / 255;
        totalDiveNumbers     = bytes [2] * 256 + bytes [3];
        logBookOffset        = (bytes [4] + 36) % 37 * 12;
        numberOfDiveProfiles = bytes [5];
        endOfProfileBuffer   = (bytes [6] + (bytes [7] >> 1) * 256) & 0x7ff;
        currentTime          = getCurrentTime (bytes);
        checkSum             = bytes [13] * 256 + bytes [12];
    }

    private Date getCurrentTime (int [] bytes)
    {
        int time = (bytes [8] * 16777216 + bytes [9] * 65536 + bytes [10] * 256 + bytes [11]) / 2;
        Calendar calendar = Calendar.getInstance (TimeZone.getTimeZone ("UTC"));

        calendar.set (Calendar.DATE, 01);
        calendar.set (Calendar.MONTH, 0);
        calendar.set (Calendar.YEAR, 1994);
        calendar.set (Calendar.HOUR_OF_DAY, 0);
        calendar.set (Calendar.MINUTE, 0);
        calendar.set (Calendar.SECOND, 0);
        calendar.add (Calendar.SECOND, time);
        return calendar.getTime ();
    }

    public String toString ()
    {
        return
            "remaining battery: " + remainingBattery + " %\n" +
            "total dive numbers: " + totalDiveNumbers + "\n" +
            "offset for the newest logbook: " + logBookOffset + " + [0x600]\n" +
            "number of dive profiles: " + numberOfDiveProfiles + "\n" +
            "end of profile buffer: 0x" + Integer.toHexString (endOfProfileBuffer) + "\n" +
            "time of data acquisition: " + currentTime + "\n" +
            "check sum: 0x" + Integer.toHexString (checkSum) + "\n";
    }
}
