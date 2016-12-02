/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: DiveSite.java
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
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class DiveSite implements Comparable<DiveSite> {
    private String privateId;
    private String publicId;
    private boolean publish;
    private boolean published;
    private String ownerId;
    private String spot;
    private String city;
    private String state;
    private String country;
    private String waters;
    private String timezone;
    private String description;
    private String directions;
    private String warnings;
    private String privateRemarks;
    private int siteType;
    private String longitude;
    private String latitude;
    private String maxDepth;
    private String avgDepth;
    private String minDepth;
    private String altitude;
    private String mainDiveActivity;
    private String evaluation;
    
    public DiveSite() {
        privateId = "";
        publicId = "";
        publish = false;
        published = false;
        ownerId = "";
        spot = "";
        city = "";
        state = "";
        country = "";
        waters = "";
        timezone = "";
        description = "";
        directions = "";
        warnings = "";
        privateRemarks = "";
        siteType = 0;
        longitude="";
        latitude="";
        maxDepth="";
        avgDepth="";
        minDepth="";
        altitude="";
        evaluation="10";
        mainDiveActivity="";
    }
    
    
    public String getAltitude() {
    	return altitude;
    }
    
    public void setAltitude(String altitude) {
    	this.altitude=altitude;
    }
    
    
    public String getMainDiveActivity() {
    	return mainDiveActivity;
    }
    
    public void setMainDiveActivity(String activity) {
    	this.mainDiveActivity=activity;
    }
    
    public Integer getEvaluation() {
    	return Integer.parseInt(evaluation);
    }
    
    public void setEvaluation(String eval) {
    	this.evaluation=eval;
    }
    
    public void setEvaluation(Integer eval) {
        this.evaluation = eval.toString();
    }
    
    
    public String getMaxDepth() {
    	return maxDepth;
    }
    
    public void setMaxDepth(String max) {
    	this.maxDepth=max;
    }
    
    
    public String getAvgDepth() {
    	return avgDepth;
    }
    
    public void setAvgDepth(String avg) {
    	this.avgDepth=avg;
    }
    
    
    public String getMinDepth() {
    	return minDepth;
    }
    
    public void setMinDepth(String min) {
    	this.minDepth=min;
    }
    
    public String getLongitude() {
    	return longitude;
    }
    
    public void setLongitude(String longitude) {
    	this.longitude=longitude;
    }

    public String getLatitude() {
    	return latitude;
    }
    
    public void setLatitude(String latitude) {
    	this.latitude=latitude;
    }
    
    public String getPrivateRemarks() {
        return privateRemarks;
    }
    public void setPrivateRemarks(String privateRemarks) {
        this.privateRemarks = privateRemarks;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getDirections() {
        return directions;
    }
    public void setDirections(String directions) {
        this.directions = directions;
    }
    public String getOwnerId() {
        return ownerId;
    }
    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }
    public String getPrivateId() {
        return privateId;
    }
    public void setPrivateId(String privateId) {
        this.privateId = privateId;
    }
    public String getPublicId() {
        return publicId;
    }
    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }
    public boolean isPublish() {
        return publish;
    }
    public void setPublish(boolean publish) {
        this.publish = publish;
    }
    public boolean isPublished() {
        return published;
    }
    public void setPublished(boolean published) {
        this.published = published;
    }
    public String getSpot() {
        return spot;
    }
    public void setSpot(String spot) {
        this.spot = spot;
    }
    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }
    public String getWarnings() {
        return warnings;
    }
    public void setWarnings(String warnings) {
        this.warnings = warnings;
    }
    public String getWaters() {
        return waters;
    }
    public void setWaters(String waters) {
        this.waters = waters;
    }
    public String getTimezone() {
        return timezone;
    }
    public void setTimezone(String newTimezone) {
        this.timezone = newTimezone;
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("<DiveSite>");
        sb.append("<privateId>");
        sb.append(privateId);
        sb.append("</privateId>");
        sb.append("<publicId>");
        sb.append(publicId);
        sb.append("</publicId>");
        sb.append("<ownerId>");
        sb.append(ownerId);
        sb.append("</ownerId>");
        sb.append("<publish>");
        sb.append(publish);
        sb.append("</publish>");
        sb.append("<published>");
        sb.append(published);
        sb.append("</published>");
        sb.append("<spot>");
        sb.append(XmlTextEncodingUtil.xmlEntityConvert(spot));
        sb.append("</spot>");
        sb.append("<city>");
        sb.append(XmlTextEncodingUtil.xmlEntityConvert(city));
        sb.append("</city>");
        sb.append("<state>");
        sb.append(XmlTextEncodingUtil.xmlEntityConvert(state));
        sb.append("</state>");
        sb.append("<country>");
        sb.append(XmlTextEncodingUtil.xmlEntityConvert(country));
        sb.append("</country>");
        sb.append("<longitude>");
        sb.append(XmlTextEncodingUtil.xmlEntityConvert(longitude));
        sb.append("</longitude>");
        sb.append("<latitude>");
        sb.append(XmlTextEncodingUtil.xmlEntityConvert(latitude));
        sb.append("</latitude>");
        sb.append("<minDepth>");
        sb.append(XmlTextEncodingUtil.xmlEntityConvert(minDepth));
        sb.append("</minDepth>");
        sb.append("<avgDepth>");
        sb.append(XmlTextEncodingUtil.xmlEntityConvert(avgDepth));
        sb.append("</avgDepth>");
        sb.append("<maxDepth>");
        sb.append(XmlTextEncodingUtil.xmlEntityConvert(maxDepth));
        sb.append("</maxDepth>");
        sb.append("<waters>");
        sb.append(XmlTextEncodingUtil.xmlEntityConvert(waters));
        sb.append("</waters>");
        sb.append("<timezone>");
        sb.append(XmlTextEncodingUtil.xmlEntityConvert(timezone));
        sb.append("</timezone>");
        sb.append("<description>");
        sb.append(XmlTextEncodingUtil.xmlEntityConvert(description));
        sb.append("</description>");
        sb.append("<directions>");
        sb.append(XmlTextEncodingUtil.xmlEntityConvert(directions));
        sb.append("</directions>");
        sb.append("<altitude>");
        sb.append(XmlTextEncodingUtil.xmlEntityConvert(altitude));
        sb.append("</altitude>");
        sb.append("<warnings>");
        sb.append(XmlTextEncodingUtil.xmlEntityConvert(warnings));
        sb.append("</warnings>");
        sb.append("<privateRemarks>");
        sb.append(XmlTextEncodingUtil.xmlEntityConvert(privateRemarks));
        sb.append("</privateRemarks>");
        sb.append("<siteType>");
        sb.append(siteType);
        sb.append("</siteType>");
        sb.append("<mainDiveActivity>");
        sb.append(XmlTextEncodingUtil.xmlEntityConvert(mainDiveActivity));
        sb.append("</mainDiveActivity>");
        sb.append("<evaluation>");
        sb.append(evaluation);
        sb.append("</evaluation>");
        sb.append("</DiveSite>");
        return sb.toString();
    }


    public int compareTo(DiveSite s) {
        if (this == s) {
            return 0;
        }
        if (s == null) {
            return 1;
        }
        return getIdentification().compareTo(s.getIdentification());
    }
    
    private String getIdentification() {
        StringBuffer sb = new StringBuffer();
        sb.append(spot);
        sb.append(city);
        sb.append(state);
        sb.append(country);
        return sb.toString().toLowerCase();
    }


    public int getSiteType() {
        return siteType;
    }


    public void setSiteType(int siteType) {
        this.siteType = siteType;
    }
}
