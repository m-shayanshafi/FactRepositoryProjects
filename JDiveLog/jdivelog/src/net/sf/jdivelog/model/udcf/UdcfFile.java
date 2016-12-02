/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: UdcfFile.java
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

import java.util.TreeSet;

/**
 * Description: Model of UDCF files data
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class UdcfFile {
    
    private String units = null;
    private String deviceVendor = null;
    private String deviceModel = null;
    private String deviceVersion = null;
    private TreeSet<Dive> dives = null;
    
    public void addDive(Dive dive) {
        if (dives == null) {
            dives = new TreeSet<Dive>();
        }
        dives.add(dive);
    }
    
    public void removeDive(Dive dive) {
        if (dives != null) {
            dives.remove(dive);
        }
    }

    public String getDeviceModel() {
        return deviceModel;
    }
    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }
    public String getDeviceVendor() {
        return deviceVendor;
    }
    public void setDeviceVendor(String deviceVendor) {
        this.deviceVendor = deviceVendor;
    }
    public String getDeviceVersion() {
        return deviceVersion;
    }
    public void setDeviceVersion(String deviceVersion) {
        this.deviceVersion = deviceVersion;
    }
    public TreeSet<Dive> getDives() {
        return dives;
    }
    public void setDives(TreeSet<Dive> dives) {
        this.dives = dives;
    }
    public String getUnits() {
        return units;
    }
    public void setUnits(String units) {
        this.units = units;
    }
}
