/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: SlideshowSettings.java
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

public class SlideshowSettings {
    
    /** time between images in seconds */
    private int cycletime;
    
    /** <code>true</code> if the slideshow should start at beginning after the last image */
    private boolean repeat;
    
    /** <code>true</code> if the title should be displayed */
    private boolean displayTitle;
    
    /** <code>true</code> if the description should be displayed */
    private boolean displayDescription;
    
    public SlideshowSettings() {
        cycletime = 8;
        repeat = false;
        displayTitle = false;
        displayDescription = false;
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("<SlideshowSettings>");
        sb.append("<cycletime>");
        sb.append(cycletime);
        sb.append("</cycletime>");
        sb.append("<repeat>");
        sb.append(repeat);
        sb.append("</repeat>");
        sb.append("<displayTitle>");
        sb.append(displayTitle);
        sb.append("</displayTitle>");
        sb.append("<displayDescription>");
        sb.append(displayDescription);
        sb.append("</displayDescription>");
        sb.append("</SlideshowSettings>");
        return sb.toString();
    }

    public int getCycletime() {
        return cycletime;
    }

    public void setCycletime(int cycletime) {
        this.cycletime = cycletime;
    }

    public void setCycletime(String cycletime) {
        this.cycletime = Integer.parseInt(cycletime);
    }
    
    public boolean isDisplayDescription() {
        return displayDescription;
    }

    public void setDisplayDescription(boolean displayDescription) {
        this.displayDescription = displayDescription;
    }

    public void setDisplayDescription(String displayDescription) {
        this.displayDescription = Boolean.getBoolean(displayDescription);
    }

    public boolean isDisplayTitle() {
        return displayTitle;
    }

    public void setDisplayTitle(boolean displayTitle) {
        this.displayTitle = displayTitle;
    }

    public void setDisplayTitle(String displayTitle) {
        this.displayTitle = Boolean.getBoolean(displayTitle);
    }

    public boolean isRepeat() {
        return repeat;
    }

    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat = Boolean.getBoolean(repeat);
    }
}
