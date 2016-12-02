/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: Equipment.java
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

import java.util.ArrayList;
import java.util.Iterator;

import net.sf.jdivelog.util.XmlTextEncodingUtil;

/**
 * Description: Information about the dive gear used during a dive
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class Equipment {

    /** Name of the equipment set */
    private String name = null;
    /** tanks used */
    private ArrayList<Tank> tanks = new ArrayList<Tank>();
    /** weigths */
    private String weight = null;
    /** suit (wetsuit, drysuit, ...) */
    private String suit = null;
    /** gloves (dry, wet, ...) */
    private String gloves = null;
    /** comment */
    private String comment = null;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public ArrayList<Tank> getTanks() {
        return tanks;
    }

    public void setTanks(ArrayList<Tank> tanks) {
        this.tanks = tanks;
    }

    public void addTank(Tank tank) {
        this.tanks.add(tank);
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("<Equipment>");
        sb.append("<Tanks>");
        Iterator<Tank> it = getTanks().iterator();
        while (it.hasNext()) {
            sb.append(it.next().toString());
        }
        sb.append("</Tanks>");
        sb.append("<Weight>");
        if (getWeight() != null) {
            sb.append(XmlTextEncodingUtil.xmlEntityConvert(getWeight()));
        }
        sb.append("</Weight>");
        sb.append("<Suit>");
        if (getSuit() != null) {
            sb.append(XmlTextEncodingUtil.xmlEntityConvert(getSuit()));
        }
        sb.append("</Suit>");
        sb.append("<Gloves>");
        if (getGloves() != null) {
            sb.append(XmlTextEncodingUtil.xmlEntityConvert(getGloves()));
        }
        sb.append("</Gloves>");
        sb.append("<Comment>");
        if (getComment() != null) {
            sb.append(XmlTextEncodingUtil.xmlEntityConvert(getComment()));
        }
        sb.append("</Comment>");
        sb.append("</Equipment>");
        return sb.toString();
    }

    public Equipment deepClone() {
        Equipment e = new Equipment();
        e.name = name;
        e.tanks = new ArrayList<Tank>();
        Iterator<Tank> it = tanks.iterator();
        while (it.hasNext()) {
            Tank tank = it.next();
            e.tanks.add(tank.deepClone());
        }
        e.weight = weight;
        e.suit = suit;
        e.gloves = gloves;
        e.comment = comment;
        return e;
    }
}
