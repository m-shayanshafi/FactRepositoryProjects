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
package net.sf.jdivelog.model.smart;

import java.util.*;

/**
 * Description: container for one logbook entry of an Uwatec Smart log file.
 *
 * @author Andr&eacute; Schenk
 * @version $Revision: 423 $
 */
public abstract class LogEntry extends DiveParser
{
    public long diveDataLength = 0;
    public Date diveStartTime = null;
    public short utcOffset = 0;
    public short repNo = 0;
    public short mbLevel = 0;
    public short battery = 0;
    public short alarmsDuringDive = 0;
    public Float maximumDepth = null;
    public int duration = 0; // in seconds
    public Float minimumTemperature = null;
    public Float maximumTemperature = null;
    public int o2Percentage = 0;
    public Float airTemperature = null;
    public int surfaceInterval = 0;
    public int cnsPercentage = 0;
    public int altitudeLevel = 0;
    public int po2Limit = 0;
    public int depthLimit = 0;
    public int desatBeforeDive = 0;
    public final transient List <DepthProfileEntry> profile = new Vector <DepthProfileEntry> ();

    protected Date getDateFromAladin (int halfSecondsSince2000)
    {
        Calendar calendar = Calendar.getInstance (TimeZone.getTimeZone ("UTC"));

        calendar.set (Calendar.DATE, 1);
        calendar.set (Calendar.MONTH, 0);
        calendar.set (Calendar.YEAR, 2000);
        calendar.set (Calendar.HOUR_OF_DAY, 0);
        calendar.set (Calendar.MINUTE, 0);
        calendar.set (Calendar.SECOND, 0);
        calendar.add (Calendar.SECOND, halfSecondsSince2000 / 2);
        return calendar.getTime ();
    }
}
