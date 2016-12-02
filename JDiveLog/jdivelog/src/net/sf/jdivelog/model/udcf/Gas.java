/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: Gas.java
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

import net.sf.jdivelog.gui.resources.Messages;
import net.sf.jdivelog.model.Mix;
import net.sf.jdivelog.util.XmlTextEncodingUtil;

/**
 * Description: Informations about tanks with volume, pressure start and end, mix,...
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class Gas {

    /** the name of the gas mix */
    private String name;

    /** the tankvolumne */
    private Double tankvolume;

    /** pressure at the start of the dive */
    private Double pstart;

    /** pressure at the end of the dive */
    private Double pend;

    /** percentage of oxygen */
    private Double oxygen;

    /** percentage of nitrogen */
    private Double nitrogen;

    /** percentage of helium */
    private Double helium;

    /**
     * get the xml representation of the element.
     * 
     * @return java.lang.String The XML representation of the element.
     */
    public String toString() {
        StringBuffer sb = new StringBuffer("<MIX>");
        if (name != null) {
            sb.append("<MIXNAME>");
            sb.append(XmlTextEncodingUtil.xmlEntityConvert(name));
            sb.append("</MIXNAME>");
        }
        sb.append("<TANK>");
        if (tankvolume != null) {
            sb.append("<TANKVOLUME>");
            sb.append(tankvolume);
            sb.append("</TANKVOLUME>");
        }
        if (pstart != null) {
            sb.append("<PSTART>");
            sb.append(pstart);
            sb.append("</PSTART>");
        }
        if (pend != null) {
            sb.append("<PEND>");
            sb.append(pend);
            sb.append("</PEND>");
        }
        sb.append("</TANK>");
        if (oxygen != null) {
            sb.append("<O2>");
            sb.append(oxygen);
            sb.append("</O2>");
        }
        if (nitrogen != null) {
            sb.append("<N2>");
            sb.append(nitrogen);
            sb.append("</N2>");
        }
        if (helium != null) {
            sb.append("<HE>");
            sb.append(helium);
            sb.append("</HE>");
        }
        sb.append("</MIX>");
        return sb.toString();
    }

    /**
     * Gets the percentage of helium. 1.0 would be 100% helium.
     * 
     * @return The percentage of helium.
     */
    public Double getHelium() {
        return helium;
    }

    /**
     * Sets the percentage of helium. 1.0 would be 100% helium.
     * 
     * @param helium
     *            The percentage of helium.
     */
    public void setHelium(Double helium) {
        this.helium = helium;
    }

    /**
     * Sets the percentage of helium. 1.0 would be 100% helium. Method used by
     * digester.
     * 
     * @param helium
     *            The percentage of helium.
     */
    public void setHelium(String helium) {
        if (helium == null || "".equals(helium.trim())) {
            this.helium = null;
        } else {
            this.helium = new Double(helium);
        }
    }

    /**
     * Gets the name of the gas mix (e.g. Air)
     * 
     * @return The name of the gas.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the gas mix (e.g. Air)
     * 
     * @param name
     *            The name of the gas.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the percentage of nitrogen. 1.0 would be 100% nitrogen.
     * 
     * @return The percentage of nitrogen.
     */
    public Double getNitrogen() {
        return nitrogen;
    }

    /**
     * Sets the percentage of nitrogen. 1.0 would be 100% nitrogen.
     * 
     * @param nitrogen
     *            The percentage of nitrogen.
     */
    public void setNitrogen(Double nitrogen) {
        this.nitrogen = nitrogen;
    }

    /**
     * Sets the percentage of nitrogen. 1.0 would be 100% nitrogen. Method used
     * by digester.
     * 
     * @param nitrogen
     *            The percentage of nitrogen.
     */
    public void setNitrogen(String nitrogen) {
        if (nitrogen == null || "".equals(nitrogen.trim())) {
            this.nitrogen = null;
        } else {
            this.nitrogen = new Double(nitrogen);
        }
    }

    /**
     * Gets the percentage of oxygen. 1.0 would be 100% oxygen.
     * 
     * @return The percentage of oxygen.
     */
    public Double getOxygen() {
        return oxygen;
    }

    /**
     * Sets the percentage of oxygen. 1.0 would be 100% oxygen. Method used by
     * digester.
     * 
     * @param oxygen
     *            The percentage of oxygen.
     */
    public void setOxygen(Double oxygen) {
        this.oxygen = oxygen;
    }

    /**
     * Sets the percentage of oxygen. 1.0 would be 100% oxygen. Method used by
     * digester.
     * 
     * @param oxygen
     *            The percentage of oxygen.
     */
    public void setOxygen(String oxygen) {
        if (oxygen == null || "".equals(oxygen.trim())) {
            this.oxygen = null;
        } else {
            this.oxygen = new Double(oxygen);
        }
    }

    /**
     * Gets the pressure at the end of the dive.
     * 
     * @return The pressure at the end of the dive.
     */
    public Double getPend() {
        return pend;
    }

    /**
     * Sets the pressure at the end of the dive.
     * 
     * @param pend
     *            The pressure at the end of the dive.
     */
    public void setPend(Double pend) {
        this.pend = pend;
    }

    /**
     * Sets the pressure et the end of the dive. Method used by digester.
     * 
     * @param pend
     *            The pressure at the end of the dive.
     */
    public void setPend(String pend) {
        if (pend == null || "".equals(pend.trim())) {
            this.pend = null;
        } else {
            this.pend = new Double(pend);
        }
    }

    /**
     * Gets the pressure at the beginning of the dive.
     * 
     * @return The pressure at the beginning of the dive.
     */
    public Double getPstart() {
        return pstart;
    }

    /**
     * Sets the pressure at the beginning of the dive.
     * 
     * @param pstart
     *            The pressure at the beginning of the dive.
     */
    public void setPstart(Double pstart) {
        this.pstart = pstart;
    }

    /**
     * Sets the pressure at the beginning of the dive. Method used by digester.
     * 
     * @param pstart
     *            The pressure at the beginning of the dive.
     */
    public void setPstart(String pstart) {
        if (pstart == null || "".equals(pstart.trim())) {
            this.pstart = null;
        } else {
            this.pstart = new Double(pstart);
        }
    }

    /**
     * Gets the tankvolume.
     * 
     * @return The tankvolume.
     */
    public Double getTankvolume() {
        return tankvolume;
    }

    /**
     * Sets the tankvolume.
     * 
     * @param tankvolume
     *            The tankvolume.
     */
    public void setTankvolume(Double tankvolume) {
        this.tankvolume = tankvolume;
    }

    /**
     * Sets the tankvolume. Method used by digester.
     * 
     * @param tankvolume
     *            The tankvolume.
     */
    public void setTankvolume(String tankvolume) {
        if (tankvolume == null || "".equals(tankvolume.trim())) {
            this.tankvolume = null;
        } else {
            this.tankvolume = new Double(tankvolume);
        }
    }
    
    public void calculateName() {
        if (oxygen == null || helium == null || nitrogen == null) {
            return;
        }
        if (helium > 0) {
            // trimix
            int o2 = (int)(100.0*oxygen.doubleValue());
            int he = (int)(100.0*helium.doubleValue());
            name = "Tmx "+o2+"/"+he;
        } else if (oxygen.doubleValue() == 1.0) {
            // Oxygen
            name = Messages.getString("oxygen");
        } else if (oxygen.doubleValue() == 0.21) {
            // Air
            name = Messages.getString("air");
        } else {
            // Nitrox
            int o2 = (int)(100.0*oxygen.doubleValue());
            name = "EAN"+o2;
        }
    }

    /**
     * Creates a copy of the object, including copies of all modifiable members.
     * 
     * @return The copy of the object.
     */
    public Gas deepClone() {
        Gas g = new Gas();
        g.name = name;
        g.tankvolume = tankvolume;
        g.pstart = pstart;
        g.pend = pend;
        g.oxygen = oxygen;
        g.nitrogen = nitrogen;
        g.helium = helium;
        return g;
    }
    
    @Override
    public int hashCode() {
        return getName().hashCode();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Gas) {
            return getName().equals(((Gas)obj).getName());
        }
        return false;
    }
    
    public Mix getMix() {
        return new Mix((int)(getOxygen()*100), (int)(getHelium()*100));
    }
    
    public void setMix(Mix mix) {
        name = mix.getName();
        setOxygen(mix.getOxygenFraction());
        setHelium(mix.getHeliumFraction());
        setNitrogen(1.0-getOxygen()-getHelium());
    }
}