/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: Delta.java
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
package net.sf.jdivelog.model.udcf;

import net.sf.jdivelog.util.XmlTextEncodingUtil;

/**
 * Description: Alarm during the dive
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class Alarm implements Sample {
    
    public static final String ALARM_DECO = "DECO";
    public static final String ALARM_BOOKMARK = "BOOKMARK";
    public static final String ALARM_ATTENTION = "ATTENTION";
    public static final String ALARM_SURFACE = "SURFACE";
    public static final String ALARM_SLOW = "SLOW";
    public static final String ALARM_PPO_LOW = "PPO2 LOW";
    public static final String ALARM_PPO_HIGH = "PPO2 HIGH";
    public static final String ALARM_DECO_CEILING_PASSED = "DECO CEILING PASSED";
    public static final String ALARM_END_OF_DIVE = "END OF DIVE";
    public static final String ALARM_SAFETY_STOP_CEILING_PASSED = "SAFETY STOP CEILING PASSED";
    public static final String ALARM_DEEP_STOP_MISSED = "DEEP STOP MISSED";
    public static final String ALARM_UNKNOWN = "UNKNOWN ALARM";
    
    private String value;

    /**
     * @return the sample type
     * @see net.sf.jdivelog.model.udcf.Sample#getType()
     * @see net.sf.jdivelog.model.udcf.Sample#TYPE_ALARM
     */
    public int getType() {
        return Sample.TYPE_ALARM;
    }

    /**
     * @return java.lang.String The alarm as String.
     * @see net.sf.jdivelog.model.udcf.Sample#getValue()
     */
    public String getValue() {
        return value;
    }
    
    /**
     * set the alarm.
     * @param alarm The new alarm.
     */
    public void setValue(String alarm) {
        value = alarm;
    }
    
    /**
     * get the xml representation of the element.
     * @return java.lang.String The XML representation of the element.
     */
    public String toString() {
        StringBuffer sb = new StringBuffer("<ALARM>");
        sb.append(XmlTextEncodingUtil.xmlEntityConvert(value));
        sb.append("</ALARM>");
        return sb.toString();
    }

}
