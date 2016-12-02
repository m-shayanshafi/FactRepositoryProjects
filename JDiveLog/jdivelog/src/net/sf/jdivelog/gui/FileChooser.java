/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: FileChooser.java
 * 
 * @author Andr&eacute; Schenk <andre_schenk@users.sourceforge.net>
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
package net.sf.jdivelog.gui;

import java.io.File;
import javax.swing.JFileChooser;

/**
 * File chooser which globally remembers the last used directory
 * 
 * @author Andr&eacute; Schenk
 * @version $Revision: 548 $
 */
public class FileChooser extends JFileChooser
{

    private static final long serialVersionUID = -572579892814979886L;
    private static File workingDirectory = null;

    /**
     * Constructs a FileChooser pointing to the user's default directory.
     * This default depends on the operating system. It is typically the "My
     * Documents" folder on Windows, and the user's home directory on Unix.
     */
    public FileChooser ()
    {
        super ();
        setCurrentDirectory (workingDirectory);
    }

    /**
     * Returns the selected file. This can be set either by the programmer via
     * setFile or by a user action, such as either typing the filename into the
     * UI or selecting the file from a list in the UI.
     * this method stores the current directory.
     * @return the selected file
     */
    public File getSelectedFile ()
    {
        storeCurrentDirectory ();
        return super.getSelectedFile ();
    }

    /**
     * Returns a list of selected files if the file chooser is set to allow
     * multiple selection.
     * this method stores the current directory.
     * @return the selected files
     */
    public File [] getSelectedFiles ()
    {
        storeCurrentDirectory ();
        return super.getSelectedFiles ();
    }

    /**
     * Remembers the current directory
     */
    private synchronized void storeCurrentDirectory ()
    {
        File directory = getCurrentDirectory ();

        if (directory != null) {
            workingDirectory = directory;
        }
    }

    public static void setWorkingDirectory(String dir) {
        workingDirectory = new File(dir);
    }
    
    public static File getWorkingDirectory() {
        return workingDirectory;
    }
}
