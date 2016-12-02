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

import net.sf.jdivelog.gui.MainWindow;
import net.sf.jdivelog.model.JDive;

/**
 * Description: Command to delete the dive from the dives collection
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class CommandDeleteDives implements UndoableCommand {
    
    private MainWindow mainWindow = null;
    private ArrayList<JDive> divesToDelete = null;
    private TreeSet<JDive> oldDiveList = null;
    private boolean oldChanged = false;
    private TreeSet<JDive> newDiveList = null;
    
    public CommandDeleteDives(MainWindow mainWindow, ArrayList<JDive> divesToDelete) {
        this.mainWindow = mainWindow;
        this.divesToDelete = divesToDelete;
    }

    /**
     * @see net.sf.jdivelog.gui.commands.UndoableCommand#undo()
     */
    public void undo() {
        this.mainWindow.getLogBook().setDives(new TreeSet<JDive>(this.oldDiveList));
        mainWindow.setChanged(oldChanged);
        mainWindow.getLogbookChangeNotifier().notifyLogbookDataChanged();
    }

    /**
     * @see net.sf.jdivelog.gui.commands.UndoableCommand#redo()
     */
    public void redo() {
        mainWindow.getLogBook().setDives(new TreeSet<JDive>(this.newDiveList));
        mainWindow.setChanged(true);
        mainWindow.getLogbookChangeNotifier().notifyLogbookDataChanged();
    }

    /**
     * @see net.sf.jdivelog.gui.commands.Command#execute()
     */
    public void execute() {
        // Backup current divelist for undo
        this.oldDiveList = (new TreeSet<JDive>(this.mainWindow.getLogBook().getDives()));
        this.oldChanged = mainWindow.isChanged();
        Iterator<JDive> it = this.divesToDelete.iterator();
        while(it.hasNext()) {
            this.mainWindow.getLogBook().getDives().remove(it.next());
        }
        // Backup new dive list for redo
        this.newDiveList = (new TreeSet<JDive>(this.mainWindow.getLogBook().getDives()));
        mainWindow.setChanged(true);
        mainWindow.getLogbookChangeNotifier().notifyLogbookDataChanged();
    }

}
