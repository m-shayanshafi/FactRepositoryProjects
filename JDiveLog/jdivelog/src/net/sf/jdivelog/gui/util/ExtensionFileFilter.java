/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: ExtensionFileFilter.java
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
package net.sf.jdivelog.gui.util;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.filechooser.FileFilter;

/**
 * Description: A filter in order to chose the extension of the file
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class ExtensionFileFilter extends FileFilter implements FilenameFilter {
    
    private final String description;
    private final String defaultExtension;
    private final Hashtable<String,String> extensions = new Hashtable<String,String>();
    
    public ExtensionFileFilter(String description, String defaultExtension) {
        this.description = description;
        this.defaultExtension = defaultExtension;
    }

    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }
        if (f.getName().lastIndexOf(".") < 0) {
            return false;
        }
        String extension = f.getName().substring(f.getName().lastIndexOf(".")+1);
        if (extension != null && extensions.containsKey(extension.toLowerCase()) && f.canRead()) {
            return true;
        }
        return false;
    }
    
    public void addExtension(String extension) {
        extensions.put(extension.toLowerCase(), extension.toLowerCase());
    }
    
    public void removeExtension(String extension) {
        extensions.remove(extension.toLowerCase());
    }

    public String getDescription() {
        StringBuffer sb = new StringBuffer(description);
        sb.append(" (");
        Enumeration<String> enumeration = extensions.elements();
        while(enumeration.hasMoreElements()) {
            sb.append("*.");
            sb.append(enumeration.nextElement());
            if (enumeration.hasMoreElements()) {
                sb.append(", ");
            }
        }
        sb.append(" )");
        return sb.toString();
    }

    public boolean accept(File dir, String name) {
        File f = new File(dir, name);
        return accept(f);
    }

    public String getDefaultExtension() {
        return defaultExtension;
    }

}
