/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: FileOutputDevice.java
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

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.fop.apps.MimeConstants;

public class FileOutputDevice extends Object implements OutputDevice {
    
    private static final String[] MIME_TYPES = new String[] {MimeConstants.MIME_PDF, MimeConstants.MIME_POSTSCRIPT, MimeConstants.MIME_RTF};
    
    private String filename;
    private String mimetype;
    
    public FileOutputDevice(String filename, String mimetype) {
        this.filename = filename;
        this.mimetype = mimetype;
    }

    public OutputStream getOutputStream() {
        try {
            return new FileOutputStream(filename);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
    
    public String getExpectedMimeType() {
        return mimetype;
    }
    
    public static String[] getMimeTypes() {
        return MIME_TYPES;
    }
}
