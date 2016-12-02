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
import java.util.StringTokenizer;
import java.util.TreeSet;

import net.sf.jdivelog.gui.MainWindow;
import net.sf.jdivelog.model.Buddy;
import net.sf.jdivelog.model.DiveSite;
import net.sf.jdivelog.model.JDive;
import net.sf.jdivelog.model.JDiveLog;


/**
 * Description: Command to add dives to the dive collection
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class CommandAddDives implements UndoableCommand {
    
    private MainWindow mainWindow = null;
    private Collection<JDive> divesToAdd = null;
    private TreeSet<JDive> oldDives = null;
    private TreeSet<JDive> newDives = null;
    private boolean oldChanged = false;
    private JDiveLog import_logbook = null;
    
    public CommandAddDives(MainWindow mainWindow, Collection<JDive> divesToAdd) {
    	this(mainWindow, divesToAdd, null);
   	}

    public CommandAddDives(MainWindow mainWindow, Collection<JDive> divesToAdd, JDiveLog import_logbook) {
        this.mainWindow = mainWindow;
        this.divesToAdd = divesToAdd;
        this.import_logbook = import_logbook;
    }

    /**
     * @see net.sf.jdivelog.gui.commands.UndoableCommand#undo()
     */
    public void undo() {
        mainWindow.getLogBook().setDives(new TreeSet<JDive>(oldDives));
        mainWindow.setChanged(oldChanged);
        mainWindow.getLogbookChangeNotifier().notifyLogbookDataChanged();
    }

    /**
     * @see net.sf.jdivelog.gui.commands.UndoableCommand#redo()
     */
    public void redo() {
        mainWindow.getLogBook().setDives(new TreeSet<JDive>(newDives));
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
        Iterator<JDive> it = this.divesToAdd.iterator();        	
        while (it.hasNext()) {
            JDive dive = it.next();
            dive.setDiveNumber(this.mainWindow.getLogBook().getNextDiveNumber());
            
            //jdivelog import
            if (import_logbook != null) {
            	
            	//search for the divesites
            	DiveSite search_divesite = import_logbook.getMasterdata().getDiveSiteByPrivateId(dive.getDiveSiteId());
            	
            	if (search_divesite != null) {
            		DiveSite found_divesite = this.mainWindow.getLogBook().getMasterdata().getDiveSiteBySpotAndCountry(search_divesite.getSpot(), search_divesite.getCountry());
	            	if (found_divesite != null) {
	            		dive.setDiveSiteId(found_divesite.getPrivateId());
	            	} else {
	            		//search for a new divesite id
	            		search_divesite.setPrivateId(String.valueOf(this.mainWindow.getLogBook().getMasterdata().getNextPrivateDiveSiteId()));
	            		//set the new divesite id 
	            		dive.setDiveSiteId(search_divesite.getPrivateId());
	            		this.mainWindow.getLogBook().getMasterdata().addDiveSite(search_divesite);
	            	}
            	}
            	
            	//search for the buddies
            	StringTokenizer tokenizer = new StringTokenizer (dive.getBuddy(),",");
            	Buddy found_buddy;
            	String buddy_name;
            	while (tokenizer.hasMoreTokens()) {
            		buddy_name = tokenizer.nextToken();
            		//search for the buddy in the masterdata of the logbook
            		found_buddy = this.mainWindow.getLogBook().getMasterdata().getBuddyByFirstnameAndSurname(buddy_name);
            		//if not found
            		if (found_buddy == null) {
            			//check if the buddy is in the masterdata of the import logbook
            			if (import_logbook.getMasterdata().getBuddyByFirstnameAndSurname(buddy_name) != null) {
            				this.mainWindow.getLogBook().getMasterdata().addBuddy(import_logbook.getMasterdata().getBuddyByFirstnameAndSurname(buddy_name));
            			} else { //if not ... cut the name in first and surname and add the buddy
            				StringTokenizer first_surname = new StringTokenizer(buddy_name, " ");
            				found_buddy = new Buddy();
            				if (first_surname.hasMoreTokens()) {
                				found_buddy.setFirstname(first_surname.nextToken());                				
            				}
            				if (first_surname.hasMoreTokens()) {
                				found_buddy.setLastname(first_surname.nextToken());                				
            				}
            				this.mainWindow.getLogBook().getMasterdata().addBuddy(found_buddy);
            			}
            		} else {
            			found_buddy = null;
            		}
            	}            
            }

            this.mainWindow.getLogBook().addDive(dive);
        }
        
        // save new dives for redo
        this.newDives = new TreeSet<JDive>(this.mainWindow.getLogBook().getDives());
        
        // notify about table change, file change
        mainWindow.setChanged(true);
        mainWindow.getLogbookChangeNotifier().notifyLogbookDataChanged();
    }
}
