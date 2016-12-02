/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: UdcfFileLoader.java
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

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.TreeSet;

import net.sf.jdivelog.model.JDive;

import org.apache.commons.digester.Digester;
import org.xml.sax.SAXException;

/**
 * Description: Loader for UDCF files
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class UdcfFileLoader {

    File file = null;

    TreeSet<JDive> jDives = null;

    public UdcfFileLoader(File f) throws SAXException, IOException {
        this.file = f;
        Digester d = new Digester();
        d.setValidating(false);
        d.addObjectCreate("PROFILE", UdcfFile.class);
        d.addCallMethod("PROFILE/UNITS", "setUnits", 1);
        d.addCallParam("PROFILE/UNITS", 0);
        d.addCallMethod("PROFILE/DEVICE/VENDOR", "setDeviceVendor", 1);
        d.addCallParam("PROFILE/DEVICE/VENDOR", 0);
        d.addCallMethod("PROFILE/DEVICE/MODEL", "setDeviceModel", 1);
        d.addCallParam("PROFILE/DEVICE/MODEL", 0);
        d.addCallMethod("PROFILE/DEVICE/VERSION", "setDeviceVersion", 1);
        d.addCallParam("PROFILE/DEVICE/VERSION", 0);
        d.addObjectCreate("PROFILE/REPGROUP/DIVE", Dive.class);
        d.addCallMethod("PROFILE/REPGROUP/DIVE/DATE", "setDate", 3);
        d.addCallParam("PROFILE/REPGROUP/DIVE/DATE/YEAR", 0);
        d.addCallParam("PROFILE/REPGROUP/DIVE/DATE/MONTH", 1);
        d.addCallParam("PROFILE/REPGROUP/DIVE/DATE/DAY", 2);
        d.addCallMethod("PROFILE/REPGROUP/DIVE/TIME", "setTime", 2);
        d.addCallParam("PROFILE/REPGROUP/DIVE/TIME/HOUR", 0);
        d.addCallParam("PROFILE/REPGROUP/DIVE/TIME/MINUTE", 1);
        d.addCallMethod("PROFILE/REPGROUP/DIVE/SURFACEINTERVAL", "setSurfaceinterval", 1);
        d.addCallParam("PROFILE/REPGROUP/DIVE/SURFACEINTERVAL", 0);
        d.addCallMethod("PROFILE/REPGROUP/DIVE/TEMPERATURE", "setTemperature", 1);
        d.addCallParam("PROFILE/REPGROUP/DIVE/TEMPERATURE", 0);
        d.addCallMethod("PROFILE/REPGROUP/DIVE/DENSITY", "setDensity", 1);
        d.addCallParam("PROFILE/REPGROUP/DIVE/DENSITY", 0);
        d.addCallMethod("PROFILE/REPGROUP/DIVE/ALTITUDE", "setAltitude", 1);
        d.addCallParam("PROFILE/REPGROUP/DIVE/ALTITUDE", 0);
        d.addObjectCreate("PROFILE/REPGROUP/DIVE/GASES/MIX", Gas.class);
        d.addCallMethod("PROFILE/REPGROUP/DIVE/GASES/MIX/MIXNAME", "setName", 1);
        d.addCallParam("PROFILE/REPGROUP/DIVE/GASES/MIX/MIXNAME", 0);
        d.addCallMethod("PROFILE/REPGROUP/DIVE/GASES/MIX/TANK/TANKVOLUME", "setTankvolume", 1);
        d.addCallParam("PROFILE/REPGROUP/DIVE/GASES/MIX/TANK/TANKVOLUME", 0);
        d.addCallMethod("PROFILE/REPGROUP/DIVE/GASES/MIX/TANK/PSTART", "setPstart", 1);
        d.addCallParam("PROFILE/REPGROUP/DIVE/GASES/MIX/TANK/PSTART", 0);
        d.addCallMethod("PROFILE/REPGROUP/DIVE/GASES/MIX/TANK/PEND", "setPend", 1);
        d.addCallParam("PROFILE/REPGROUP/DIVE/GASES/MIX/TANK/PEND", 0);
        d.addCallMethod("PROFILE/REPGROUP/DIVE/GASES/MIX/O2", "setOxygen", 1);
        d.addCallParam("PROFILE/REPGROUP/DIVE/GASES/MIX/O2", 0);
        d.addCallMethod("PROFILE/REPGROUP/DIVE/GASES/MIX/N2", "setNitrogen", 1);
        d.addCallParam("PROFILE/REPGROUP/DIVE/GASES/MIX/N2", 0);
        d.addCallMethod("PROFILE/REPGROUP/DIVE/GASES/MIX/HE", "setHelium", 1);
        d.addCallParam("PROFILE/REPGROUP/DIVE/GASES/MIX/HE", 0);
        d.addSetNext("PROFILE/REPGROUP/DIVE/GASES/MIX", "addGas");
        d.addCallMethod("PROFILE/REPGROUP/DIVE/TIMEDEPTHMODE", "setTimeDepthMode");
        d.addCallMethod("PROFILE/REPGROUP/DIVE/DELTAMODE", "setDeltaMode");
        d.addCallMethod("PROFILE/REPGROUP/DIVE/SAMPLES/DELTA", "addDelta", 1);
        d.addCallParam("PROFILE/REPGROUP/DIVE/SAMPLES/DELTA", 0);
        d.addCallMethod("PROFILE/REPGROUP/DIVE/SAMPLES/SWITCH", "addSwitch", 1);
        d.addCallParam("PROFILE/REPGROUP/DIVE/SAMPLES/SWITCH", 0);
        d.addCallMethod("PROFILE/REPGROUP/DIVE/SAMPLES/T", "addTime", 1);
        d.addCallParam("PROFILE/REPGROUP/DIVE/SAMPLES/T", 0);
        d.addCallMethod("PROFILE/REPGROUP/DIVE/SAMPLES/D", "addDepth", 1);
        d.addCallParam("PROFILE/REPGROUP/DIVE/SAMPLES/D", 0);
        d.addCallMethod("PROFILE/REPGROUP/DIVE/SAMPLES/TEMPERATURE", "addTemperature", 1);
        d.addCallParam("PROFILE/REPGROUP/DIVE/SAMPLES/TEMPERATURE", 0);
        d.addCallMethod("PROFILE/REPGROUP/DIVE/SAMPLES/ALARM", "addAlarm", 1);
        d.addCallParam("PROFILE/REPGROUP/DIVE/SAMPLES/ALARM", 0);
        d.addSetNext("PROFILE/REPGROUP/DIVE", "addDive");
        UdcfFile udcf = (UdcfFile) d.parse(file);
        Iterator<Dive> it = udcf.getDives().iterator();
        jDives = new TreeSet<JDive>();
        while (it.hasNext()) {
            jDives.add(new JDive(udcf.getUnits(), it.next()));
        }
    }

    public File getFile() {
        return file;
    }
    public void setFile(File file) {
        this.file = file;
    }
    public TreeSet<JDive> getJDives() {
        return jDives;
    }
    public void setJDives(TreeSet<JDive> dives) {
        jDives = dives;
    }
}