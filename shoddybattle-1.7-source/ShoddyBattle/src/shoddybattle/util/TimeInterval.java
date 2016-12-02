/*
 * TimeInterval.java
 *
 * Created on April 22, 2007, 1:04 PM
 *
 * This file is a part of Shoddy Battle.
 * Copyright (C) 2007  Colin Fitzpatrick
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 * The Free Software Foundation may be visited online at http://www.fsf.org.
 */

package shoddybattle.util;
import java.util.*;

/**
 * This class represents a difference between two times, rather than an
 * an absolute value of time. It allows formatting of the interval as such.
 *
 * @author Colin
 */
public class TimeInterval {
    
    /**
     * The units of time, in descending order.
     */
    private static final int[] m_series = {
            Calendar.YEAR,
            Calendar.MONTH,
            Calendar.DAY_OF_MONTH,
            Calendar.HOUR_OF_DAY,
            Calendar.MINUTE,
            Calendar.SECOND
        };
    
    /**
     * The names of each of these units, in singular, preceded by a space.
     */
    private static final String[] m_names = {
            " year",
            " month",
            " day",
            " hour",
            " minute",
            " second"
        };
    
    /**
     * The number of seconds in each of these units.
     */
    private static final int[] m_factor = {
            12 * 30 * 60 * 60,
            30 * 60 * 60,   // Approximation of thirty days per month.
            24 * 60 * 60,
            60 * 60,
            60,
            1
        };
    
    /**
     * The length of the interval in milliseconds.
     */
    private long m_length;
    
    /**
     * Create a time interval with the given length.
     * @param length the length of the time interval in milliseconds
     */
    public TimeInterval(long length) {
        m_length = length;
    }
    
    /**
     * Create a time interval based on the absolute value of the difference
     * between the given Date and the current time.
     */
    public static TimeInterval getDeltaInterval(Date future) {
        long length = future.getTime() - Calendar.getInstance().getTimeInMillis();
        if (length < 0) length = -length;
        return new TimeInterval(length);
    }
    
    /**
     * Format the interval as a string, giving a fairly good approximation
     * of the length of the interval. This is only an approximation to avoid
     * the length of the string becoming too long.
     */
    public String getApproximation() {
        Calendar interval = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        interval.setTime(new Date(m_length));
        
        int[] raw = new int[m_series.length];
        int[] values = new int[m_series.length];
        String[] descriptions = new String[m_series.length];
        int first = -1, i = 0;
        for (; i < values.length; ++i) {
            raw[i] = interval.get(m_series[i]);
            if (i == 0) {
                raw[i] -= 1970;
            } else if (i == 2) {
                --raw[i];
            }
            values[i] = raw[i] * m_factor[i];
            if (raw[i] != 0) {
                if (first == -1) {
                    first = i;
                }
                descriptions[i] = raw[i] + m_names[i];
                if (raw[i] != 1) {
                    descriptions[i] += "s";
                }
            }
            if (i > 0) {
                if ((values[i] * 10) < values[i - 1]) {
                    // This place is pretty insignificant.
                    break;
                }
            }
        }
        
        int length = i - first + 1;
        int j = first + 1;
        if (length > 2) {
            double ratio = ((double)raw[first + 2]) / ((double)interval.getActualMaximum(m_series[first + 2]));
            if (ratio >= 0.5) {
                if (raw[j] < interval.getActualMaximum(m_series[j])) {
                    descriptions[j] = (raw[j] + 1) + m_names[j] + "s";
                }
            }
        }
        
        String result = descriptions[first];
        if ((j < m_series.length) && (descriptions[j] != null)) {
            result += ", " + descriptions[j];
        }
        
        return result;
    }
    
}
