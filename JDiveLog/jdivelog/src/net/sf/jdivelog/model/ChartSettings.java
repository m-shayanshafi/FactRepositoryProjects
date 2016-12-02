/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: ChartSettings.java
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

import net.sf.jdivelog.gui.StatisticPanel;

public class ChartSettings {
    
    private String type;
    
    private String orientation;
    
    public ChartSettings() {
        type = StatisticPanel.TYPE_BAR;
        orientation = StatisticPanel.ORIENTATION_HORIZONTAL;
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("<type>");
        sb.append(type);
        sb.append("</type>");
        sb.append("<orientation>");
        sb.append(orientation);
        sb.append("</orientation>");
        return sb.toString();
    }

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    public ChartSettings deepClone() {
        ChartSettings copy = new ChartSettings();
        copy.type = type;
        copy.orientation = orientation;
        return copy;
    }
}
