/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: PrintJob.java
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
package net.sf.jdivelog.printing;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import net.sf.jdivelog.printing.fop.FopUtil;

public class PrintJob {
    
    private String xmlData;
    private Report report;
    private OutputDevice outputDevice;
    
    public PrintJob(String xmlData, Report report, OutputDevice outDevice) {
        this.xmlData = xmlData;
        this.report = report;
        this.outputDevice = outDevice;
    }
    
    public void execute() {
        try {
            File tmp = File.createTempFile("jdl", ".fo");
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(tmp));
            report.run(new ByteArrayInputStream(xmlData.getBytes()), bos);
            FopUtil.convert(new BufferedInputStream(new FileInputStream(tmp)), outputDevice.getOutputStream(), outputDevice.getExpectedMimeType());
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

}
