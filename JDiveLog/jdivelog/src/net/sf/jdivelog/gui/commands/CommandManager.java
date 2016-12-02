/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: CommandManager.java
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

/**
 * Description: Main class managing the different commands
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class CommandManager {

    private static CommandManager instance = null;

    static {
        instance = new CommandManager();
    }

    public static CommandManager getInstance() {
        return instance;
    }

    private ArrayList<CommandManagerListener> listeners = new ArrayList<CommandManagerListener>();

    private ArrayList<Command> commandHistory = new ArrayList<Command>();

    private Command lastCommand = null;

    private CommandManager() {

    }

    public synchronized void execute(Command cmd) {
        boolean undo = canUndo();
        boolean redo = canRedo();
        cmd.execute();
        if (!(cmd instanceof UndoableCommand)) {
            // command has no undo function.
            // clean history
            commandHistory.clear();
            lastCommand = null;
        } else {
            int idx = commandHistory.lastIndexOf(lastCommand);
            if (lastCommand != null && idx != commandHistory.size() - 1) {
                // a command has been executed somewehere between beginning and
                // end of history.
                // remove all subsequent commands (since redo would be
                // dangerous)
                while (idx + 1 < commandHistory.size()) {
                    commandHistory.remove(idx + 1);
                }
            }
            commandHistory.add(cmd);
            lastCommand = cmd;
        }
        if (undo != canUndo() || redo != canRedo()) {
            notifyListeners();
        }
    }

    public void undo() {
        if (canUndo()) {
            boolean undo = true;
            boolean redo = canRedo();
            int idx = commandHistory.lastIndexOf(lastCommand);
            UndoableCommand cmd = (UndoableCommand) commandHistory.get(idx);
            cmd.undo();
            if (idx > 0) {
                lastCommand = commandHistory.get(idx - 1);
            } else {
                lastCommand = null;
            }
            if (undo != canUndo() || redo != canRedo()) {
                notifyListeners();
            }
        }
    }

    public void redo() {
        if (canRedo()) {
            boolean undo = canUndo();
            boolean redo = true;
            int idx = 0;
            if (lastCommand == null) {
                idx = -1;
            } else {
                idx = commandHistory.lastIndexOf(lastCommand);
            }
            UndoableCommand cmd = (UndoableCommand) commandHistory.get(idx + 1);
            cmd.redo();
            lastCommand = cmd;
            if (undo != canUndo() || redo != canRedo()) {
                notifyListeners();
            }
        }
    }

    public boolean canUndo() {
        if (lastCommand != null && commandHistory.size() > 0) {
            return true;
        }
        return false;
    }

    public boolean canRedo() {
        if (commandHistory.size() > 0 && lastCommand == null) {
            return true;
        }
        if (lastCommand != null
                && commandHistory.size() > 0
                && commandHistory.lastIndexOf(lastCommand) < commandHistory
                        .size() - 1) {
            return true;
        }
        return false;
    }

    public void addCommandListener(CommandManagerListener listener) {
        this.listeners.add(listener);
    }

    public void removeCommandListener(CommandManagerListener listener) {
        this.listeners.remove(listener);
    }

    protected void notifyListeners() {
        Iterator<CommandManagerListener> it = this.listeners.iterator();
        while (it.hasNext()) {
            CommandManagerListener listener = it.next();
            listener.commandManagerChanged();
        }
    }
}