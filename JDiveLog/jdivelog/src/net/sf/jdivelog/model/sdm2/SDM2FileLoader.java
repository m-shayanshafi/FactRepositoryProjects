/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: SDM2FileLoader.java
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
package net.sf.jdivelog.model.sdm2;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.TreeSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.digester.Digester;
import org.xml.sax.SAXException;

public class SDM2FileLoader {

    private TreeSet<SDM2Dive> dives;

    public SDM2FileLoader(File f) throws SAXException, IOException {
        dives = new TreeSet<SDM2Dive>();
        Digester d = new Digester();
        d.setValidating(false);
        d.addObjectCreate("SUUNTO/MSG", SDM2Dive.class);
        d.addBeanPropertySetter("SUUNTO/MSG/DATE", "date");
        d.addBeanPropertySetter("SUUNTO/MSG/TIME", "time");
        d.addBeanPropertySetter("SUUNTO/MSG/MAXDEPTH", "depth");
        d.addBeanPropertySetter("SUUNTO/MSG/MEANDEPTH", "avgDepth");
        d.addBeanPropertySetter("SUUNTO/MSG/LOGNOTES", "notes");
        d.addBeanPropertySetter("SUUNTO/MSG/LOCATION", "location");
        d.addBeanPropertySetter("SUUNTO/MSG/SITE", "site");
        d.addBeanPropertySetter("SUUNTO/MSG/WATERTEMPMAXDEPTH", "temperature");
        d.addBeanPropertySetter("SUUNTO/MSG/PARTNER", "partner");
        d.addBeanPropertySetter("SUUNTO/MSG/DIVEMASTER", "divemaster");
        d.addBeanPropertySetter("SUUNTO/MSG/CYLINDERSIZE", "tankSize");
        d.addBeanPropertySetter("SUUNTO/MSG/CYLINDERUNITS", "tankUnits");
        d.addBeanPropertySetter("SUUNTO/MSG/CYLINDERSTARTPRESSURE", "pressureStart");
        d.addBeanPropertySetter("SUUNTO/MSG/CYLINDERENDPRESSURE", "pressureEnd");
        d.addBeanPropertySetter("SUUNTO/MSG/O2PCT", "o2pct");
        d.addBeanPropertySetter("SUUNTO/MSG/O2PCT_2", "o2pct2");
        d.addBeanPropertySetter("SUUNTO/MSG/O2PCT_3", "o2pct3");
        d.addBeanPropertySetter("SUUNTO/MSG/DIVENUMBER", "diveNumber");
        d.addBeanPropertySetter("SUUNTO/MSG/WATERVISIBILITYDESC", "visibility");
        d.addCallMethod("SUUNTO/MSG/SAMPLE", "addSample", 2);
        d.addCallParam("SUUNTO/MSG/SAMPLE/SAMPLETIME", 0);
        d.addCallParam("SUUNTO/MSG/SAMPLE/DEPTH", 1);
        ZipFile zf = new ZipFile(f);
        Enumeration<? extends ZipEntry> en = zf.entries();
        while (en.hasMoreElements()) {
            ZipEntry entry = en.nextElement();
            BufferedInputStream is =
                new BufferedInputStream(zf.getInputStream(entry));
            SDM2Dive dive = (SDM2Dive)d.parse(is);
            dives.add(dive);
        }
    }
    
    public TreeSet<SDM2Dive> getDives() {
        return dives;
    }
}
