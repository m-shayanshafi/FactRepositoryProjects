/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: CommandSaveDive.java
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
package net.sf.jdivelog.gui.commands;

import java.util.TreeSet;

import net.sf.jdivelog.gui.MainWindow;
import net.sf.jdivelog.model.DiveSite;


/**
 *Description: Save the site among the existing sites 
 *
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class CommandSaveSite implements UndoableCommand {
    
    MainWindow mainWindow = null;
    DiveSite oldSite = null;
    DiveSite newSite = null;
    TreeSet<DiveSite> oldSites = null;
    TreeSet<DiveSite> newSites = null;
    boolean oldChanged = false;
    
    public CommandSaveSite(MainWindow mainWindow, DiveSite oldSite, DiveSite newSite) {
        this.mainWindow = mainWindow;
        this.oldSite = oldSite;
        this.newSite = newSite;
        this.oldChanged = mainWindow.isChanged();
    }

    /**
     * @see net.sf.jdivelog.gui.commands.UndoableCommand#undo()
     */
    public void undo() {
        this.mainWindow.getLogBook().getMasterdata().setDiveSites(new TreeSet<DiveSite>(oldSites));
        mainWindow.setChanged(this.oldChanged);
        mainWindow.getLogbookChangeNotifier().notifyLogbookDataChanged();
    }

    /**
     * @see net.sf.jdivelog.gui.commands.UndoableCommand#redo()
     */
    public void redo() {
        this.mainWindow.getLogBook().getMasterdata().setDiveSites(new TreeSet<DiveSite>(newSites));
        mainWindow.setChanged(true);
        mainWindow.getLogbookChangeNotifier().notifyLogbookDataChanged();
    }

    /**
     * @see net.sf.jdivelog.gui.commands.Command#execute()
     */
    public void execute() {
        // Backup old sitelist
        this.oldSites= new TreeSet<DiveSite>(mainWindow.getLogBook().getMasterdata().getDiveSites());
        
        // replace old site or add new site
        if (this.mainWindow.getLogBook().getMasterdata().getDiveSites().contains(this.oldSite)) {
            this.mainWindow.getLogBook().getMasterdata().deleteDiveSite(oldSite);
            this.mainWindow.getLogBook().getMasterdata().addDiveSite(newSite);
        } else {
            newSite.setPrivateId(String.valueOf(mainWindow.getLogBook().getMasterdata().getNextPrivateDiveSiteId()));
            this.mainWindow.getLogBook().getMasterdata().addDiveSite(newSite);
        }
        
        // Backup new divelist
        this.newSites = new TreeSet<DiveSite>(mainWindow.getLogBook().getMasterdata().getDiveSites());
        
        // notify mainwindow
        mainWindow.setChanged(true);
        mainWindow.getLogbookChangeNotifier().notifyLogbookDataChanged();
    }

}
