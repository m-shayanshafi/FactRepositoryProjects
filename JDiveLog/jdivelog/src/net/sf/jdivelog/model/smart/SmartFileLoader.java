/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: SmartFileLoader.java
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
package net.sf.jdivelog.model.smart;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.jdivelog.ci.SmartComputerType;
import net.sf.jdivelog.ci.SmartInterface;
import net.sf.jdivelog.gui.DiveImportAladinWindow;
import net.sf.jdivelog.gui.MainWindow;
import net.sf.jdivelog.model.SmartAdapter;
import net.sf.jdivelog.model.JDive;

/**
 * Description: loads Uwatec Smart data files and converts them into JDiveLog
 * format.
 *
 * @author Andr&eacute; Schenk
 * @version $Revision: 423 $
 */
public class SmartFileLoader
{
    private static final Logger LOGGER = Logger.getLogger(SmartFileLoader.class.getName());

    private ArrayList<JDive> diveToAdd = new ArrayList<JDive> ();

    public SmartFileLoader (MainWindow mainWindow, File [] files)
    {
        DataInputStream in = null;

        for (int i = 0; i < files.length; i++) {
            try {
                in = new DataInputStream (new BufferedInputStream
                                          (new FileInputStream (files [i])));

                int fileLength = (int) files [i].length ();
                byte [] read_data = new byte [fileLength];

                for (int index = 0; index < read_data.length; index++) {
                    read_data [index] = (byte) in.read ();
                }

                Properties properties = mainWindow.getLogBook ().getComputerSettings ();

                if (properties != null) {
        	        String model = properties.getProperty
        	        (SmartInterface.PROPERTY.COMPUTER_MODEL.toString ());

        	        if (model != null) {
                        SmartComputerType type = SmartInterface.getComputerType (Integer.parseInt (model));
                        TreeSet <JDive> dives = new SmartAdapter (new SmartData (type, read_data));
                        Iterator <JDive> iterator = dives.iterator ();

                        while (iterator.hasNext ()) {
                            diveToAdd.add ((JDive) iterator.next ());
                        }
        	        }
                }
                else {
                    LOGGER.log(Level.SEVERE, "failed to read computer settings");
                }
            }
            catch (Exception e) {
                LOGGER.log(Level.SEVERE, "failed to load Uwatec Smart file", e);
            }
            finally {
                if (in != null) {
                	try {
                        in.close ();
                	}
                	catch (IOException e) {
                	}
                }
            }
        }

        // open the diveImportDataTrak window to mark the dives for import
        DiveImportAladinWindow daw =
            new DiveImportAladinWindow (mainWindow, diveToAdd);

        daw.setVisible (true);
    }
}
