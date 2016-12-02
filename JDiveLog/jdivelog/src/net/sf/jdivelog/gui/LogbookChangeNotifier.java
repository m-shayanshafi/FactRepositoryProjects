/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: LogbookChangeNotifier.java
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
package net.sf.jdivelog.gui;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class LogbookChangeNotifier {
    
    private Set<LogbookChangeListener> listeners;
    private boolean changed;
    
    public LogbookChangeNotifier() {
        listeners = new HashSet<LogbookChangeListener>();
        changed = false;
    }
    
    public synchronized void addLogbookChangeListener(LogbookChangeListener l) {
        listeners.add(l);
    }
    
    public synchronized void removeLogbookChangeListener(LogbookChangeListener l) {
        listeners.remove(l);
    }
    
    public synchronized void notifyLogbookLoaded() {
        LogbookChangeEvent e = new LogbookChangeEvent(LogbookChangeEvent.EventType.LOGBOOK_LOADED);
        notify(e);
    }
    
    public synchronized void notifyLogbookUnloaded() {
        LogbookChangeEvent e = new LogbookChangeEvent(LogbookChangeEvent.EventType.LOGBOOK_UNLOADED);
        notify(e);
    }
    
    public synchronized void notifyLogbookDataChanged() {
        LogbookChangeEvent e = new LogbookChangeEvent(LogbookChangeEvent.EventType.LOGBOOK_DATA_CHANGED);
        notify(e);
    }
    
    public synchronized void notifyLogbookTitleChanged() {
        LogbookChangeEvent e = new LogbookChangeEvent(LogbookChangeEvent.EventType.LOGBOOK_TITLE_CHANGED);
        notify(e);
    }
    
    private synchronized void notify(LogbookChangeEvent e) {
        ArrayList<LogbookChangeListener> ls = new ArrayList<LogbookChangeListener>(listeners);
        for (LogbookChangeListener l : ls) {
            l.logbookChanged(e);
        }
    }

    public boolean isChanged() {
        return changed;
    }

    public void setChanged(boolean changed) {
        boolean oldChanged = this.changed;
        this.changed = changed;
        if (changed != oldChanged) {
            this.notifyLogbookTitleChanged();
        }
    }
}
