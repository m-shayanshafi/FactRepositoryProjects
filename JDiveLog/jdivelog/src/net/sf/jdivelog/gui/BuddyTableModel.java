/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: BuddyTableModel.java
 * 
 * @author Volker Holthaus <v.holthaus@procar.de>
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
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.table.AbstractTableModel;

import net.sf.jdivelog.gui.resources.Messages;
import net.sf.jdivelog.gui.util.SortableTableModel;
import net.sf.jdivelog.model.Buddy;

/**
 * Description: Class defining the table containing the different buddies.
 * Extends AbstractTableModel
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class BuddyTableModel extends AbstractTableModel implements SortableTableModel{

    private static final long serialVersionUID = 3256720693223044662L;

    private final static String[] columns = {
            Messages.getString("firstname"), Messages.getString("lastname")}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$

    private ArrayList<Buddy> buddys;

    BuddyWindow buddyWindow = null;
    

    public BuddyTableModel(BuddyWindow buddyWindow, ArrayList<Buddy> buddys) {
        this.buddyWindow = buddyWindow;
        SortedSet<Buddy> sorted= new TreeSet<Buddy>(buddys);
        this.buddys = new ArrayList<Buddy>(sorted);
    }

    public BuddyTableModel(BuddyWindow buddyWindow) {
        this.buddyWindow = buddyWindow;
        this.buddys = new ArrayList<Buddy>();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    public int getColumnCount() {
        return columns.length;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#getRowCount()
     */
    public int getRowCount() {
        return buddys.size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    public Object getValueAt(int rowIndex, int columnIndex) {
        Buddy buddy = buddys.get(rowIndex);
        switch (columnIndex) {
        case 0:
            return buddy.getFirstname();
        case 1:
            return buddy.getLastname();
        }
        return null;
    }

    public void load(ArrayList<Buddy> buddys2) {
        SortedSet<Buddy> sorted = new TreeSet<Buddy>(buddys2);
        buddys = new ArrayList<Buddy>(sorted);
        fireTableDataChanged();
    }
    
    
    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    public Object getUnformattedValueAt(int rowIndex, int columnIndex) {
        Buddy buddy = buddys.get(rowIndex);
        switch (columnIndex) {
        case 0:
            return buddy.getFirstname();
        case 1:
            return buddy.getLastname();
        }
        return null;
    }

    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    public Class<?> getUnformattedColumnClass(int columnIndex) {
        return String.class;
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    public ArrayList<Buddy> getBuddys() {
        return buddys;
    }

    public void setBuddys(ArrayList<Buddy> buddys) {
        this.buddys = buddys;
        fireTableDataChanged();
    }

    public void addBuddy(Buddy buddy) {
        buddys.add(buddy);
        fireTableDataChanged();
    }

    public void removeBuddy(Buddy buddy) {
        buddys.remove(buddy);
        fireTableDataChanged();
    }

    public Buddy getBuddy(int row) {
        return buddys.get(row);
    }

    public void removeBuddy(int row) {
        buddys.remove(row);
        fireTableRowsDeleted(row, row);
    }

    public String[] getColumnNames() {
        return columns;
    }

    public String getColumnName(int column) {
        return columns[column];
    }
    
    public ArrayList<Buddy> getAll() {
        return buddys;
    }

}