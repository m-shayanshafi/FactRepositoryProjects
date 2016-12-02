/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: DiveType.java
 * 
 * @author Volker Holthaus <v.holthaus@procar.de>
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
package net.sf.jdivelog.model;

import net.sf.jdivelog.util.XmlTextEncodingUtil;


/**
 * Description: class describing a dive type
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class DiveType implements Comparable<DiveType> {
    
    /** description */
    private String description = null;
        
    public String toString() {
        StringBuffer sb = new StringBuffer("<Divetype>");
        sb.append("<Description>");
        if (description != null) sb.append(XmlTextEncodingUtil.xmlEntityConvert(description));
        sb.append("</Description>");
        sb.append("</Divetype>");
        return sb.toString();
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public DiveType deepClone() {
        DiveType t = new DiveType();
        t.description = description;
        return t;
    }

    public int compareTo(DiveType other) {
        return description.compareTo(other.description);
    }
}
