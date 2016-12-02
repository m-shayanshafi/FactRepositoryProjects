/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: DateFormatUtil.java
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
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
package net.sf.jdivelog.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import net.sf.jdivelog.JDiveLog;

/**
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class DateFormatUtil {
    
    private static DateFormat dateFormat;
    private static DateFormat dateTimeFormat;
    
    static {
        String format = System.getProperty(JDiveLog.PROPERTY_DATEFORMAT);
        if (format != null && !"".equals(format)) {
            dateFormat = new SimpleDateFormat(format);
        } else {
            dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        }
        format = System.getProperty(JDiveLog.PROPERTY_DATETIMEFORMAT);
        if (format != null && !"".equals(format)) {
            dateTimeFormat = new SimpleDateFormat(format);
        } else {
            dateTimeFormat = new SimpleDateFormat("dd.MM.yyyy - HH:mm");
        }
    }
    
    public static final DateFormat getDateFormat() {
        return dateFormat;
    }
    
    public static final DateFormat getDateTimeFormat() {
        return dateTimeFormat;
    }
    
    public static final String formatDateTime(String year, String month, String day, String hour, String minute) {
        return getDateTimeFormat().format(getDate(year, month, day, hour, minute));
    }
    
    private static final Date getDate(String year, String month, String day, String hour, String minute) {
        GregorianCalendar gc = new GregorianCalendar();
        int y = Integer.parseInt(year);
        int m = Integer.parseInt(month);
        int d = Integer.parseInt(day);
        int h = Integer.parseInt(hour);
        int min = Integer.parseInt(minute);
        gc.set(Calendar.YEAR, y);
        gc.set(Calendar.MONTH, m-1);
        gc.set(Calendar.DAY_OF_MONTH, d);
        gc.set(Calendar.HOUR_OF_DAY, h);
        gc.set(Calendar.MINUTE, min);
        return gc.getTime();
    }

}
