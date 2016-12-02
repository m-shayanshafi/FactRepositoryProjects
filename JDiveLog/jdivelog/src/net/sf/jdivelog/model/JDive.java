/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: DiveDetailWindow.java
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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;

import net.sf.jdivelog.model.udcf.Dive;
import net.sf.jdivelog.model.udcf.Gas;
import net.sf.jdivelog.util.UnitConverter;
import net.sf.jdivelog.util.XmlTextEncodingUtil;

/**
 * Description: Dive information
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class JDive implements Comparable<JDive> {
    
    /** Number of the dive */
    private Long diveNumber;
    
    /** 
     * Units used in this dive entry.
     * Valid values: <b>si</b>, <b>metric</b> or <b>imperial</b>.<br>
     * <b>si</b>: Pressure [Pa], Altitude [m], Temperature [K], Density [kg/m^3], Time [s], Volume [m^3]<br>
     * <b>metric</b>: Pressure [bar], Altitude [m], Temperature [Degree Celsius], Density [kg/m^3], Time [min], Volume [m^3]
     * <b>imperial</b>: Pressure [psi], Altitude [feet], Temperature [Degree Fahrenheit], Density [ kg/m^3 ], Time [min], Volume [m^3]
     * See <a href="http://www.streit.cc/dive/udcf_doc_ger.html">The UDCF Specification</a>.
     */ 
    private String units;
    
    /** The date of the dive */
    private Date date = null;
    
    /**
     * private id of dive site.
     */
    private String diveSiteId;
    
    /**
     * flag whether this dive has been exported or not.
     */
    private boolean htmlExported;
    
    /**
     * The dive site
     * @deprecated
     */
    private String place;
    
    /**
     * Country of the dive site
     * @deprecated
     */
    private String country;

    /** The amv */
    private Double amv;
    
    /** Maximum depth during dive */
    private Double depth;

    /** Average depth during dive */
    private Double average_depth;
    
    /**
     * Duration of the dive
     * @see #units */
    private Double duration;

    /** Description of the visibility */
    private String visibility;
    
    /** Water temperature */
    private Double temperature;
    
    /** Surface temperature */
    private Double surfaceTemperature;
    
    /** Buddy */
    private String buddy;
    
    /** Divetype */
    private String divetype;

    /** Divetype */
    private String diveactivity;

    /** Equipment used during dive */
    private Equipment equipment;
    
    /** pictures taken during dive*/
    private ArrayList<Picture> pictures;
    
    /** comment to the dive */
    private String comment;
    
    /** Dive data in udcf format from dive computer */
    private Dive dive;

    /** Dive data in udcf format from dive computer */

    public JDive() {
        htmlExported = false;
    }
    
    /**
     * @param units
     * @param dive
     */
    public JDive(String units, Dive dive) {
        this.units = units;
        this.dive = dive;
        htmlExported = false;
        copyUdcfData();
    }

    private void copyUdcfData() {
        this.setDate(this.dive.getDate());
        this.setDepth(this.dive.getMaxDepth());
        this.setAverageDepth(this.dive.getAverageDepth());
        this.setTemperature(this.dive.getTemperature());
        this.setSurfaceTemperature(this.dive.getSurfaceTemperature());
        this.setDuration(this.dive.getDuration());
        this.setEquipment(new Equipment());
        Iterator<Gas> it = this.dive.getGases().iterator();
        while (it.hasNext()) {
            Gas gas = it.next();
            Tank tank = new Tank();
            tank.setGas(gas);
            this.getEquipment().addTank(tank);
        }
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer("<JDive>");
        GregorianCalendar gc = null;
        if (getDate() != null) {
            gc = new GregorianCalendar();
            gc.setTime(getDate());
        }
        sb.append("<DiveNum>");
        if (diveNumber != null) {
            sb.append(diveNumber);
        }
        sb.append("</DiveNum>");
        sb.append("<UNITS>");
        sb.append(units);
        sb.append("</UNITS>");
        sb.append("<DATE><YEAR>");
        if (gc != null) sb.append(gc.get(Calendar.YEAR));
        sb.append("</YEAR><MONTH>");
        if (gc != null) sb.append(gc.get(Calendar.MONTH)+1);
        sb.append("</MONTH><DAY>");
        if (gc != null) sb.append(gc.get(Calendar.DAY_OF_MONTH));
        sb.append("</DAY></DATE>");
        sb.append("<TIME><HOUR>");
        if (gc != null) sb.append(gc.get(Calendar.HOUR_OF_DAY));
        sb.append("</HOUR><MINUTE>");
        if (gc != null) sb.append(gc.get(Calendar.MINUTE));
        sb.append("</MINUTE></TIME>");
        sb.append("<PLACE>");
        if (place!= null) sb.append(XmlTextEncodingUtil.xmlEntityConvert(place));
        sb.append("</PLACE>");
        sb.append("<Country>");
        if (country != null) sb.append(XmlTextEncodingUtil.xmlEntityConvert(country));
        sb.append("</Country>");
        sb.append("<diveSiteId>");
        if (diveSiteId != null)sb.append(diveSiteId);
        sb.append("</diveSiteId>");
        sb.append("<htmlExported>");
        sb.append(htmlExported);
        sb.append("</htmlExported>");
        sb.append("<amv>");
        if (amv != null) sb.append(amv);
        sb.append("</amv>");
        sb.append("<Depth>");
        if (depth != null) sb.append(depth);
        sb.append("</Depth>");
        sb.append("<Average_Depth>");
        if (average_depth != null) sb.append(average_depth);
        sb.append("</Average_Depth>");
        sb.append("<Duration>");
        if (duration != null) sb.append(duration);
        sb.append("</Duration>");
        sb.append("<Visibility>");
        if (visibility != null) sb.append(XmlTextEncodingUtil.xmlEntityConvert(visibility));
        sb.append("</Visibility>");
        sb.append("<TEMPERATURE>");
        if (temperature != null) sb.append(temperature);
        sb.append("</TEMPERATURE>");
        sb.append("<surfaceTemperature>");
        if (surfaceTemperature != null) sb.append(surfaceTemperature);
        sb.append("</surfaceTemperature>");
        sb.append("<Buddy>");
        if (buddy != null) sb.append(XmlTextEncodingUtil.xmlEntityConvert(buddy));
        sb.append("</Buddy>");
        sb.append("<DiveType>");
        if (divetype != null) sb.append(XmlTextEncodingUtil.xmlEntityConvert(divetype));
        sb.append("</DiveType>");
        sb.append("<DiveActivity>");
        if (diveactivity != null) sb.append(XmlTextEncodingUtil.xmlEntityConvert(diveactivity));
        sb.append("</DiveActivity>");
        sb.append("<Comment>");
        if (comment != null) sb.append(XmlTextEncodingUtil.xmlEntityConvert(comment));
        sb.append("</Comment>");
        if (equipment != null) {sb.append(equipment.toString());}
        sb.append("<Pictures>");
        if (pictures != null) {
            Iterator<Picture> it = pictures.iterator();
            while (it.hasNext()) {
                sb.append(it.next().toString());
            }
        }
        sb.append("</Pictures>");
        if (dive != null) {
            sb.append(dive.toString());
        }
        sb.append("</JDive>");
        return sb.toString();
    }
    
    public JDive deepClone() {
        JDive dive = new JDive();
        dive.diveNumber = diveNumber;
        dive.units = units;
        dive.date = date;
        dive.diveSiteId = diveSiteId;
        dive.place = place;
        dive.country = country;
        dive.amv = amv;
        dive.depth = depth;
        dive.average_depth = average_depth;
        dive.duration = duration;
        dive.visibility = visibility;
        dive.temperature = temperature;
        dive.surfaceTemperature = surfaceTemperature;
        dive.buddy = buddy;
        dive.divetype = divetype;
        dive.diveactivity = diveactivity;
        if (equipment != null) {
            dive.equipment = equipment.deepClone();
        } else {
            dive.equipment = null;
        }
        if (pictures != null) {
            dive.pictures = new ArrayList<Picture>();
            Iterator<Picture> it = pictures.iterator();
            while (it.hasNext()) {
                Picture picture = it.next();
                dive.pictures.add(picture.deepClone());
            }
        } else {
            dive.pictures = null;
        }
        dive.comment = comment;
        dive.dive = this.dive;
        return dive;
    }
    
    /**
     * add a picture to the list.
     * @param picture The picture to add.
     */
    public void addPicture(Picture picture) {
        if (pictures == null) {
            pictures = new ArrayList<Picture>();
        }
        pictures.add(picture);
    }
    
    /**
     * remove picture from list.
     * @param picture The picture to remove.
     */
    public void removePicture(Picture picture) {
        pictures.remove(picture);
    }

    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    /**
     * still used for migration.
     * @deprecated
     * @return dive country.
     */
    public String getCountry() {
        return country;
    }
    /**
     * still used for migration.
     * @deprecated
     * @param country dive country.
     */
    public void setCountry(String country) {
        this.country = country;
    }
    public void setAMV(Double amv) {
        this.amv = amv;
    }
    public void setAMV(String amv) {
        if (amv == null || "".equals(amv)) {
            this.amv = null;
        } else {
            this.amv = new Double(amv);
        }
    }
    public Double getAMV() {        
        if (amv == null && getAverageDepth() != null && !(getAverageDepth().doubleValue() == 0)) {
            UnitConverter convToMetric = new UnitConverter(UnitConverter.getSystem(getUnits()), UnitConverter.SYSTEM_METRIC);
            UnitConverter convFromMetric = new UnitConverter(UnitConverter.SYSTEM_METRIC, UnitConverter.getSystem(getUnits()));
            double consumption = 0.0;
            Iterator<Tank> it = getEquipment().getTanks().iterator();
            while(it.hasNext()) {
                Gas gas = it.next().getGas();
                if (gas != null && gas.getTankvolume() != null) {
                    Double pstart = convToMetric.convertPressure(gas.getPstart());
                    Double pend = convToMetric.convertPressure(gas.getPend());
                    Double vol = convToMetric.convertVolume(gas.getTankvolume());
                    Double depth = convToMetric.convertAltitude(getAverageDepth());
                    if (pstart != null && pend != null && vol != null && depth != null && pstart.doubleValue() != 0 && pend.doubleValue() != 0 && vol.doubleValue() != 0 && depth.doubleValue() != 0) {
                        double c = (pstart.doubleValue() - pend.doubleValue()) * vol / (1 + (depth / 10));
                        consumption += c;
                    }
                }
            }
            if (consumption != 0 && duration != null && duration.doubleValue() != 0) {
                amv = convFromMetric.convertAMV(new Double(consumption / convToMetric.convertTime(duration)));
            }
            
        }
        return amv;
    }
    public Double getDepth() {
        return depth;
    }
    public Double getAverageDepth() {
        return average_depth;
    }
    public void setDepth(Double depth) {
        this.depth = depth;
    }
    public void setDepth(String depth) {
        if (depth == null || "".equals(depth)) {
            this.depth = null;
        } else {
            this.depth = new Double(depth);
        }
    }
    public void setAverageDepth(Double average_depth) {
        this.average_depth = average_depth;
    }
    public void setAverageDepth(String average_depth) {
        if (average_depth == null || "".equals(average_depth)) {
            this.average_depth = null;
        } else {
            this.average_depth = new Double(average_depth);
        }
    }
    public Dive getDive() {
        return dive;
    }
    public void setDive(Dive dive) {
        this.dive = dive;
    }
    public Long getDiveNumber() {
        return diveNumber;
    }
    public void setDiveNumber(Long diveNumber) {
        this.diveNumber = diveNumber;
    }
    public void setDiveNumber(String diveNumber) {
        if (diveNumber == null || "".equals(diveNumber)) {
            this.diveNumber = null;;
        } else {
            this.diveNumber = new Long(diveNumber);
        }
    }
    public Double getDuration() {
        return duration;
    }
    public void setDuration(Double duration) {
        this.duration = duration;
    }
    public void setDuration(String duration) {
        if (duration == null || "".equals(duration)) {
            this.dive = null;
        } else {
            this.duration = new Double(duration);
        }
    }
    public Equipment getEquipment() {
        return equipment;
    }
    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }
    public ArrayList<Picture> getPictures() {
        return pictures;
    }
    public void setPictures(ArrayList<Picture> pictures) {
        this.pictures = pictures;
    }
    /**
     * still used for migration
     * @return dive place
     */
    @Deprecated
    public String getPlace() {
        return place;
    }
    /**
     * still used for migration.
     * @param place dive place 
     */
    @Deprecated
    public void setPlace(String place) {
        this.place = place;
    }
    public Double getTemperature() {
        return temperature;
    }
    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }
    public void setTemperature(String temperature) {
        if (temperature == null || "".equals(temperature)) {
            this.temperature = null;
        } else {
            this.temperature = new Double(temperature);
        }
    }
    public Double getSurfaceTemperature() {
        return surfaceTemperature;
    }
    public void setSurfaceTemperature(Double surfaceTemperature) {
        this.surfaceTemperature = surfaceTemperature;
    }
    public void setSurfaceTemperature(String surfaceTemperature) {
        if (surfaceTemperature == null || "".equals(surfaceTemperature)) {
            this.surfaceTemperature = null;
        } else {
            this.surfaceTemperature = new Double(surfaceTemperature);
        }
    }
    public String getUnits() {
        return units;
    }
    public void setUnits(String units) {
        this.units = units;
    }
    public String getVisibility() {
        return visibility;
    }
    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }
    public Date getDate() {
        return this.date;
    }
    
    public void setDate(Date date) {
        this.date = date;
    }
    
    public void setDate(String year, String month, String day) {
        GregorianCalendar gc = new GregorianCalendar();
        if (getDate() == null) {
            setDate(new Date());
        }
        if (year != null && month != null && day != null && !"".equals(year) && !"".equals(month) && !"".equals(day)) {
	        int y = Integer.parseInt(year);
	        int m = Integer.parseInt(month);
	        int d = Integer.parseInt(day);
	        gc.setTime(getDate());
	        gc.set(Calendar.YEAR, y);
	        gc.set(Calendar.MONTH, m-1);
	        gc.set(Calendar.DAY_OF_MONTH, d);
	        setDate(gc.getTime());
        }
    }
    
    public void setTime(String hour, String minute) {
        GregorianCalendar gc = new GregorianCalendar();
        if (getDate() == null) {
            setDate(new Date());
        }
        if (hour != null && minute != null && !"".equals(hour) && !"".equals(minute)) {
            gc.setTime(getDate());
            gc.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
            gc.set(Calendar.MINUTE, Integer.parseInt(minute));
            setDate(gc.getTime());
        }
    }
    public String getBuddy() {
        return buddy;
    }
    public void setBuddy(String buddy) {
        this.buddy = buddy;
    }

    public String getDiveType() {
        return divetype;
    }
    public void setDiveType(String divetype) {
        this.divetype = divetype;
    }
    public String getDiveActivity() {
        return diveactivity;
    }
    public void setDiveActivity(String diveactivity) {
        this.diveactivity = diveactivity;
    }
    public int compareTo(JDive d) {
        if (this == d) {
            return 0;
        }
        if (d == null) {
            return 1;
        }
        int ret = 0;
        if (getDiveNumber() != null && d.getDiveNumber() != null) {
            ret = getDiveNumber().compareTo(d.getDiveNumber());
        }
        if (ret == 0) {
            if (getDate() != null && d.getDate() != null) {
                ret = getDate().compareTo(d.getDate());
            }
        }
        if (ret == 0) {
            // not comparable, but not the same...
            ret = -1;
        }
        return ret;
    }

    public Double getStoredAMV() {
        return amv;
    }
    
    public void setDiveSiteId(String siteId) {
        diveSiteId = siteId;
    }
    
    public String getDiveSiteId() {
        return diveSiteId;
    }
    
    public void setHtmlExported(boolean b) {
        htmlExported = b;
    }
    
    public boolean isHtmlExported() {
        return htmlExported;
    }

    public boolean before(JDive dive2) {
        if (dive2 == null) {
            return true;
        }
        Date d1 = getDate();
        Date d2 = dive2.getDate();
        if (d1 == null) {
            return false;
        }
        if (d2 == null) {
            return true;
        }
        return d1.before(d2);
    }
}
