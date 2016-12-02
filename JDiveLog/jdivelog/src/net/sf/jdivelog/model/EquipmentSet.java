/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: EquipmentSet.java
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

public class EquipmentSet implements Comparable<EquipmentSet>{
    
    private String name;
    private String suit;
    private String gloves;
    private String weight;
    private String tankVolume;
    private String tankType;
    
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("<EquipmentSet>");
        sb.append("<Name>");
        sb.append(name == null ? "" : XmlTextEncodingUtil.xmlEntityConvert(name));
        sb.append("</Name>");
        sb.append("<Suit>");
        sb.append(suit == null ? "" : XmlTextEncodingUtil.xmlEntityConvert(suit));
        sb.append("</Suit>");
        sb.append("<Gloves>");
        sb.append(gloves == null ? "" : XmlTextEncodingUtil.xmlEntityConvert(gloves));
        sb.append("</Gloves>");
        sb.append("<Weight>");
        sb.append(XmlTextEncodingUtil.xmlEntityConvert(weight));
        sb.append("</Weight>");
        sb.append("<TankVolume>");
        sb.append(tankVolume == null ? "" : XmlTextEncodingUtil.xmlEntityConvert(tankVolume));
        sb.append("</TankVolume>");
        sb.append("<TankType>");
        sb.append(tankType == null ? "" : XmlTextEncodingUtil.xmlEntityConvert(tankType));
        sb.append("</TankType>");
        sb.append("</EquipmentSet>");
        return sb.toString();
    }
    
    public int compareTo(EquipmentSet other) {
        return name.compareTo(other.name);
    }

    public String getGloves() {
        return gloves;
    }

    public void setGloves(String gloves) {
        this.gloves = gloves;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSuit() {
        return suit;
    }

    public void setSuit(String suit) {
        this.suit = suit;
    }

    public String getTankType() {
        return tankType;
    }

    public void setTankType(String tankType) {
        this.tankType = tankType;
    }

    public String getTankVolume() {
        return tankVolume;
    }

    public void setTankVolume(String tankVolume) {
        this.tankVolume = tankVolume;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

}
