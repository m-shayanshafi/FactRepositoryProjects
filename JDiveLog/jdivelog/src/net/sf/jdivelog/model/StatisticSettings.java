/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: StatisticSettings.java
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


public class StatisticSettings {

    private ChartSettings buddyStatistic;
    
    private ChartSettings divePlaceStatistic;
    
    private ChartSettings countryStatistic;
    
    private ChartSettings diveTypeStatistic;
    
    private ChartSettings diveActivityStatistic;
    
    private ChartSettings watersStatistic;

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("<StatisticSettings>");
        sb.append("<buddyStatistic>");
        sb.append(getBuddyStatistic());
        sb.append("</buddyStatistic>");
        sb.append("<divePlaceStatistic>");
        sb.append(getDivePlaceStatistic());
        sb.append("</divePlaceStatistic>");
        sb.append("<countryStatistic>");
        sb.append(getCountryStatistic());
        sb.append("</countryStatistic>");
        sb.append("<diveTypeStatistic>");
        sb.append(getDiveTypeStatistic());
        sb.append("</diveTypeStatistic>");
        sb.append("<diveActivityStatistic>");
        sb.append(getDiveActivityStatistic());
        sb.append("</diveActivityStatistic>");
        sb.append("<watersStatistic>");
        sb.append(watersStatistic);
        sb.append("</watersStatistic>");
        sb.append("</StatisticSettings>");
        return sb.toString();
    }

    public ChartSettings getBuddyStatistic() {
        if (buddyStatistic == null) {
            buddyStatistic = new ChartSettings();
        }
        return buddyStatistic;
    }

    public void setBuddyStatistic(ChartSettings buddyStatistic) {
        this.buddyStatistic = buddyStatistic;
    }

    public ChartSettings getCountryStatistic() {
        if (countryStatistic == null) {
            countryStatistic = new ChartSettings();
        }
        return countryStatistic;
    }

    public void setCountryStatistic(ChartSettings countryStatistic) {
        this.countryStatistic = countryStatistic;
    }

    public ChartSettings getDiveActivityStatistic() {
        if (diveActivityStatistic == null) {
            diveActivityStatistic = new ChartSettings();
        }
        return diveActivityStatistic;
    }

    public void setDiveActivityStatistic(ChartSettings diveActivityStatistic) {
        this.diveActivityStatistic = diveActivityStatistic;
    }
    
    public ChartSettings getWatersStatistic() {
        if (watersStatistic == null) {
            watersStatistic = new ChartSettings();
        }
        return watersStatistic;
    }
    
    public void setWatersStatistic(ChartSettings watersStatistic) {
        this.watersStatistic = watersStatistic;
    }

    public ChartSettings getDivePlaceStatistic() {
        if (divePlaceStatistic == null) {
            divePlaceStatistic = new ChartSettings();
        }
        return divePlaceStatistic;
    }

    public void setDivePlaceStatistic(ChartSettings divePlaceStatistic) {
        this.divePlaceStatistic = divePlaceStatistic;
    }

    public ChartSettings getDiveTypeStatistic() {
        if (diveTypeStatistic == null) {
            diveTypeStatistic = new ChartSettings();
        }
        return diveTypeStatistic;
    }

    public void setDiveTypeStatistic(ChartSettings diveTypeStatistic) {
        this.diveTypeStatistic = diveTypeStatistic;
    }
    
    public StatisticSettings deepClone() {
        StatisticSettings copy = new StatisticSettings();
        copy.buddyStatistic = buddyStatistic.deepClone();
        copy.divePlaceStatistic = divePlaceStatistic.deepClone();
        copy.countryStatistic = countryStatistic.deepClone();
        copy.diveTypeStatistic = diveTypeStatistic.deepClone();
        copy.diveActivityStatistic = diveActivityStatistic.deepClone();
        copy.watersStatistic = watersStatistic.deepClone();
        return copy;
    }
    
}
