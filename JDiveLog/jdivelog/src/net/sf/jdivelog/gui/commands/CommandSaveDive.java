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
import net.sf.jdivelog.model.JDive;


/**
 * Description: Command to create the new dive into the dives collection
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class CommandSaveDive implements UndoableCommand {
    
    MainWindow mainWindow = null;
    JDive oldDive = null;
    JDive newDive = null;
    TreeSet<JDive> oldDives = null;
    TreeSet<JDive> newDives = null;
    boolean oldChanged = false;
    
    public CommandSaveDive(MainWindow mainWindow, JDive oldDive, JDive newDive) {
        this.mainWindow = mainWindow;
        this.oldDive = oldDive;
        this.newDive = newDive;
        this.oldChanged = mainWindow.isChanged();
    }

    /**
     * @see net.sf.jdivelog.gui.commands.UndoableCommand#undo()
     */
    public void undo() {
        this.mainWindow.getLogBook().setDives(new TreeSet<JDive>(oldDives));
        mainWindow.setChanged(this.oldChanged);
        mainWindow.getLogbookChangeNotifier().notifyLogbookDataChanged();
    }

    /**
     * @see net.sf.jdivelog.gui.commands.UndoableCommand#redo()
     */
    public void redo() {
        this.mainWindow.getLogBook().setDives(new TreeSet<JDive>(newDives));
        mainWindow.setChanged(true);
        mainWindow.getLogbookChangeNotifier().notifyLogbookDataChanged();
    }

    /**
     * @see net.sf.jdivelog.gui.commands.Command#execute()
     */
    public void execute() {
        // Backup old divelist
        this.oldDives = new TreeSet<JDive>(mainWindow.getLogBook().getDives());
        
        // replace old dive or add new dive
        if (this.mainWindow.getLogBook().getDives().contains(this.oldDive)) {
            this.mainWindow.getLogBook().getDives().remove(oldDive);
            this.mainWindow.getLogBook().getDives().add(newDive);
        } else {
            this.mainWindow.getLogBook().getDives().add(newDive);
        }
        
        // Backup new divelist
        this.newDives = new TreeSet<JDive>(mainWindow.getLogBook().getDives());
        
        // notify mainwindow
        mainWindow.setChanged(true);
        mainWindow.getLogbookChangeNotifier().notifyLogbookDataChanged();
    }

}
