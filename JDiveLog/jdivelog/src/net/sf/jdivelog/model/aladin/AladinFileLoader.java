/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: AladinFileLoader.java
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
package net.sf.jdivelog.model.aladin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.jdivelog.gui.DiveImportAladinWindow;
import net.sf.jdivelog.gui.MainWindow;
import net.sf.jdivelog.model.AladinAdapter;
import net.sf.jdivelog.model.JDive;

/**
 * Description: loads Aladin log files (2046 bytes) and converts them into
 * JDiveLog format
 *
 * @author Andr&eacute; Schenk
 * @version $Revision: 551 $
 */
public class AladinFileLoader
{

    private static final Logger LOGGER = Logger.getLogger(AladinFileLoader.class.getName());

    private ArrayList<JDive> diveToAdd = new ArrayList<JDive> ();

    public AladinFileLoader (MainWindow mainWindow, File [] files)
        throws IOException
    {
        for (int i = 0; i < files.length; i++) {
            try {
                TreeSet <JDive> dives = new AladinAdapter
                    (new AladinData (files [i].toString ()));
                Iterator <JDive> iterator = dives.iterator ();

                while (iterator.hasNext ()) {
                    diveToAdd.add ((JDive) iterator.next ());
                }
            }
            catch (Exception e) {
                LOGGER.log(Level.SEVERE, "failed to load aladin file", e);
            }
        }

        //open the diveImportDataTrak window to mark the dives for import
        DiveImportAladinWindow daw =
            new DiveImportAladinWindow (mainWindow, diveToAdd);

        daw.setVisible (true);
    }
}
