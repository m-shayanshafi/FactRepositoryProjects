/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: CommandAddDives.java
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

import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;

import net.sf.jdivelog.gui.MainWindow;
import net.sf.jdivelog.model.JDive;


/**
 * Description: Implement the dives to ignore
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class CommandIgnoreDives implements UndoableCommand {
    
    private MainWindow mainWindow = null;
    private Collection<JDive> divesToIgnore = null;
    private TreeSet<JDive> oldDives = null;
    private TreeSet<JDive> newDives = null;
    private boolean oldChanged = false;
    
    public CommandIgnoreDives(MainWindow mainWindow, Collection<JDive> divesToIgnore) {
        this.mainWindow = mainWindow;
        this.divesToIgnore = divesToIgnore;
    }

    /**
     * @see net.sf.jdivelog.gui.commands.UndoableCommand#undo()
     */
    public void undo() {
        this.mainWindow.getLogBook().setIgnoredDives(new TreeSet<JDive>(oldDives));
        mainWindow.setChanged(oldChanged);
        mainWindow.getLogbookChangeNotifier().notifyLogbookDataChanged();
    }

    /**
     * @see net.sf.jdivelog.gui.commands.UndoableCommand#redo()
     */
    public void redo() {
        this.mainWindow.getLogBook().setIgnoredDives(new TreeSet<JDive>(newDives));
        mainWindow.setChanged(true);
        mainWindow.getLogbookChangeNotifier().notifyLogbookDataChanged();
    }

    /**
     * @see net.sf.jdivelog.gui.commands.Command#execute()
     */
    public void execute() {
        // save dives for undo
        this.oldDives = new TreeSet<JDive>(this.mainWindow.getLogBook().getDives());
        this.oldChanged = this.mainWindow.isChanged();
        
        // add the collection to the logbook
        Iterator<JDive> it = this.divesToIgnore.iterator();
        while (it.hasNext()) {
            JDive dive = it.next();
            this.mainWindow.getLogBook().addIgnoredDive(dive);
        }
        
        // save new dives for redo
        this.newDives = new TreeSet<JDive>(this.mainWindow.getLogBook().getDives());
        
        // notify about table change, file change
        mainWindow.setChanged(true);
        mainWindow.getLogbookChangeNotifier().notifyLogbookDataChanged();
    }

}
