/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: Picture.java
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

/**
 * Description: Class to use in order to display images
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class Picture {
    
    /** filename with relative path */
    private String filename;
    
    /** name of the picture */
    private String name;
    
    /** description of the picture */
    private String description;
    
    /** rotation angle = (value * 90) */
    private int rotation;
    
    /** image rating */
    private int rating;
    
    public Picture() {
        rating = 5;
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer("<Picture>");
        sb.append("<Filename>");
        sb.append(XmlTextEncodingUtil.xmlEntityConvert(filename));
        sb.append("</Filename>");
        sb.append("<Name>");
        sb.append(XmlTextEncodingUtil.xmlEntityConvert(name));
        sb.append("</Name>");
        sb.append("<Description>");
        sb.append(XmlTextEncodingUtil.xmlEntityConvert(description));
        sb.append("</Description>");
        sb.append("<rotation>");
        sb.append(rotation);
        sb.append("</rotation>");
        sb.append("<rating>");
        sb.append(rating);
        sb.append("</rating>");
        sb.append("</Picture>");
        return sb.toString();
    }

    public Picture deepClone() {
        Picture p = new Picture();
        p.filename = filename;
        p.name = name;
        p.description = description;
        p.rotation = rotation;
        p.rating = rating;
        return p;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getFilename() {
        return filename;
    }
    public void setFilename(String filename) {
        this.filename = filename;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getRotation() {
        return rotation;
    }
    public void setRotation(String rotation) {
        if (rotation == null || "".equals(rotation )) {
            this.rotation = 0;
        } else {
            this.rotation = Integer.parseInt(rotation);
        }
        if (this.rotation > 4 || this.rotation < 0) {
            this.rotation = 0;
        }
    }
    public void setRotation(int rotation) {
        this.rotation = rotation;
    }
    public int getRating() {
        return rating;
    }
    public void setRating(int rating) {
        this.rating = rating;
    }
    public void setRating(String rating) {
        this.rating = Integer.parseInt(rating);
    }
}
