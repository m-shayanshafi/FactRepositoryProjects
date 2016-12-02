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
 * Description: Gas switch during the dive
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class Switch implements Sample {
    
    private String value;

    /**
     * @return the sample type
     * @see net.sf.jdivelog.model.udcf.Sample#getType()
     * @see net.sf.jdivelog.model.udcf.Sample#TYPE_SWITCH
     */
    public int getType() {
        return Sample.TYPE_SWITCH;
    }

    /**
     * @return java.lang.Double The sampling rate as Double
     * @see net.sf.jdivelog.model.udcf.Sample#getValue()
     */
    public String getValue() {
        return value;
    }
    
    /**
     * set the gas mix name
     * @see net.sf.jdivelog.model.udcf.Gas#getName()
     * @param name The new gas name.
     */
    public void setValue(String name) {
        value = name;
    }
    
    /**
     * get the xml representation of the element.
     * @return java.lang.String The XML representation of the element.
     */
    public String toString() {
        StringBuffer sb = new StringBuffer("<SWITCH>");
        sb.append(XmlTextEncodingUtil.xmlEntityConvert(value));
        sb.append("</SWITCH>");
        return sb.toString();
    }

}
