/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: LogEntry.java
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

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Description: container for one logbook entry of an Aladin log file
 * 
 * @author Andr&eacute; Schenk
 * @version $Revision: 822 $
 */
public class LogEntry
{
    public final int   alarms;
    public final int   bottomTime;
    public final Float maximumDepth;
    public final int   surfaceTime;
    public final int   airConsumption;
    public final Date  entryTime;
    public final Float waterTemperature;

    public LogEntry (int [] bytes, int aladinType)
    {
        if ((bytes == null) || (bytes.length < 12)) {
            throw new IllegalArgumentException ("need 12 bytes for log entry");
        }
        alarms           = bytes [0];
        bottomTime       = getBottomTime (bytes [1], alarms);
        maximumDepth     = getMaximumDepth (bytes [2], bytes [3]);
        surfaceTime      = getSurfaceTime (bytes [4], bytes [5], alarms);
        airConsumption   = getAirConsumption (bytes [6], aladinType);
        entryTime        = getEntryTime (bytes [7], bytes [8], bytes [9], bytes [10]);
        waterTemperature = new Float ((bytes [11] * 25) / 100.0);
    }

    public static int getAirConsumption (int byte1, int aladinType)
    {
        int result = byte1;

        if (aladinType == 0x1c) {
            // Aladin Air: unit = x * 20 psi 
            result = result * 1378 / 1000;
        }
        return result;
    }

    public static String getAlarms (int alarms)
    {
        StringBuffer result = new StringBuffer ();

        if ((alarms & 1) > 0) {
            result.append (" ascent warning too long |");
        }
        if ((alarms & 2) > 0) {
            result.append (" repeated diving |");
        }
        if ((alarms & 8) > 0) {
            result.append (" decompression violation |");
        }
        if ((alarms & 16) > 0) {
            result.append (" work too hard |");
        }
        if ((alarms & 32) > 0) {
            result.append (" SOS mode |");
        }
        return result.toString ();
    }

    public static int getBottomTime (int byte1, int alarms)
    {
        int result = byte1 % 256 / 16 * 10 + byte1 % 16;
        if ((alarms & 4) > 0) {
            result += 100;
        }
        return result;
    }

    public static Date getEntryTime (int byte1, int byte2, int byte3, int byte4)
    {
        int time = (byte1 * 16777216 + byte2 * 65536 + byte3 * 256 + byte4) / 2;
        Calendar calendar = Calendar.getInstance (TimeZone.getTimeZone ("UTC"));

        calendar.clear ();
        calendar.set (Calendar.DATE, 1);
        calendar.set (Calendar.MONTH, 0);
        calendar.set (Calendar.YEAR, 1994);
        calendar.set (Calendar.HOUR_OF_DAY, 0);
        calendar.set (Calendar.MINUTE, 0);
        calendar.set (Calendar.SECOND, 0);
        calendar.add (Calendar.SECOND, time);
        return calendar.getTime ();
    }

    private String getHighPlace ()
    {
        switch ((alarms & 0xc0) >> 6) {
        case 0 : return "0 m - 900 m";
        case 1 : return "900 m - 1700 m";
        case 2 : return "1700 m - 2700 m";
        case 3 : return "2700 m - 4000 m";
        default : return "error calculating high place value";
        }
    }

    public static Float getMaximumDepth (int byte1, int byte2)
    {
        return new Float (((byte1 * 256 + byte2) * 1000 / 4096) / 100.0);
    }

    public static int getSurfaceTime (int byte1, int byte2, int alarms)
    {
        int time = byte1 * 256 + byte2;
        int hours = 0;
        int minutes = 0;

        if ((alarms & 2) > 0) {
            hours = time / 4096 * 10 + time % 4096 / 256;
            minutes = time % 256 / 16 * 10 + time % 16;
        }
        return hours * 60 + minutes;
    }

    public String toString ()
    {
        return
            "  high place: "        + getHighPlace ()    + "\n" +
            "  alarms:"             + getAlarms (alarms) + "\n" +
            "  bottom time: "       + bottomTime         + " min\n" +
            "  maximum depth: "     + maximumDepth       + " m\n" +
            "  surface time: "      + surfaceTime        + " min\n" +
            "  air consumption: "   + airConsumption     + " bar\n" +
            "  entry time: "        + entryTime          + "\n" +
            "  water temperature: " + waterTemperature   + " \u00b0C\n";
    }
}
