/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: GloveType.java
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
package net.sf.jdivelog.model;

import net.sf.jdivelog.util.XmlTextEncodingUtil;

public class GloveType implements Comparable<GloveType> {
    
    private String description;
    
    public String toString() {
        StringBuffer sb = new StringBuffer("<GloveType>");
        if (description  != null) {
            sb.append("<Description>");
            sb.append(XmlTextEncodingUtil.xmlEntityConvert(description));
            sb.append("</Description>");
        }
        sb.append("</GloveType>");
        return sb.toString();
    }

    public int compareTo(GloveType other) {
        return description.compareTo(other.description);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
