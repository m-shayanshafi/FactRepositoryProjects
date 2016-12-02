/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: CommandDeleteDives.java
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

import net.sf.jdivelog.gui.MessageDialog;
import net.sf.jdivelog.gui.MainWindow;
import net.sf.jdivelog.gui.resources.Messages;
import net.sf.jdivelog.model.DiveSite;
import net.sf.jdivelog.model.JDive;

/**
 * Description: Delete a site from the sites collection
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class CommandDeleteSites implements UndoableCommand {
    
    private MainWindow mainWindow = null;
    private ArrayList<DiveSite> sitesToDelete = null;
    private TreeSet<DiveSite> oldSiteList = null;
    private TreeSet<DiveSite> newSiteList = null;
    private boolean oldChanged = false;
    
    public CommandDeleteSites(MainWindow mainWindow, ArrayList<DiveSite> sitesToDelete) {
        this.mainWindow = mainWindow;
        this.sitesToDelete = sitesToDelete;
    }

    /**
     * @see net.sf.jdivelog.gui.commands.UndoableCommand#undo()
     */
    public void undo() {
        this.mainWindow.getLogBook().getMasterdata().setDiveSites(new TreeSet<DiveSite>(this.oldSiteList));
        mainWindow.setChanged(oldChanged);
        mainWindow.getLogbookChangeNotifier().notifyLogbookDataChanged();
    }

    /**
     * @see net.sf.jdivelog.gui.commands.UndoableCommand#redo()
     */
    public void redo() {
        this.mainWindow.getLogBook().getMasterdata().setDiveSites(new TreeSet<DiveSite>(this.newSiteList));
        mainWindow.setChanged(true);
        mainWindow.getLogbookChangeNotifier().notifyLogbookDataChanged();
    }

    /**
     * @see net.sf.jdivelog.gui.commands.Command#execute()
     */
    public void execute() {
        // Backup current divelist for undo
        this.oldSiteList = new TreeSet<DiveSite>(this.mainWindow.getLogBook().getMasterdata().getDiveSites());
        this.oldChanged = mainWindow.isChanged();
        Iterator<DiveSite> it = this.sitesToDelete.iterator();
        int errorCount = 0;
        while(it.hasNext()) {
            DiveSite site = it.next();
            if (isSiteReferenced(site)) {
                errorCount++;
            } else {
                this.mainWindow.getLogBook().getMasterdata().deleteDiveSite(site);
            }
        }
        // Backup new dive list for redo
        this.newSiteList = (new TreeSet<DiveSite>(this.mainWindow.getLogBook().getMasterdata().getDiveSites()));
        mainWindow.setChanged(true);
        mainWindow.getLogbookChangeNotifier().notifyLogbookDataChanged();
        if (errorCount > 0) {
            new MessageDialog(mainWindow, Messages.getString("warning"), Messages.getString("warning.could_not_delete_all_sites"), null, MessageDialog.MessageType.ERROR);
        }
    }
    
    //
    // private methods
    //
    
    private boolean isSiteReferenced(DiveSite site) {
        Iterator<JDive> it = mainWindow.getLogBook().getDives().iterator();
        while(it.hasNext()) {
            JDive dive = it.next();
            if (site.getPrivateId().equals(dive.getDiveSiteId())) {
                return true;
            }
        }
        return false;
    }

}
