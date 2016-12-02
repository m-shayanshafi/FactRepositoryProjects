/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: DiveActivityTableModel.java
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
import net.sf.jdivelog.model.DiveActivity;

/**
 * Description: Table model for the dive activities
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class DiveActivityTableModel extends AbstractTableModel implements SortableTableModel {

    private static final long serialVersionUID = 3256720693223044662L;

    private final static String[] columns = {
            Messages.getString("description")}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$

    private ArrayList<DiveActivity> diveactivities;

    DiveActivityWindow diveactivityWindow = null;
    

    public DiveActivityTableModel(DiveActivityWindow diveActivityWindow, ArrayList<DiveActivity> diveactivities) {
        this.diveactivityWindow = diveActivityWindow;
        SortedSet<DiveActivity> sorted = new TreeSet<DiveActivity>(diveactivities);
        this.diveactivities = new ArrayList<DiveActivity>(sorted);
    }

    public DiveActivityTableModel(DiveActivityWindow diveactivityWindow) {
        this.diveactivityWindow = diveactivityWindow;
        this.diveactivities = new ArrayList<DiveActivity>();
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
        return diveactivities.size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
        case 0:
            return diveactivities.get(rowIndex).getDescription();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    public Object getUnformattedValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
        case 0:
            return diveactivities.get(rowIndex).getDescription();
        }
        return null;
    }

    public Class<?> getColumnClass(int columnIndex) {
        return DiveActivity.class;
    }

    public Class<?> getUnformattedColumnClass(int columnIndex) {
        return DiveActivity.class;
    }

    public ArrayList<DiveActivity> getDivetypes() {
        return diveactivities;
    }

    public void setDiveActivities(ArrayList<DiveActivity> diveactivities) {
        this.diveactivities = diveactivities;
        fireTableDataChanged();
    }

    public void load(ArrayList<DiveActivity> diveactivities2) {
        SortedSet<DiveActivity> sorted = new TreeSet<DiveActivity>(diveactivities2);
        diveactivities = new ArrayList<DiveActivity>(sorted);
        fireTableDataChanged();
    }

    public void addDiveActivity(DiveActivity diveactivity) {
        diveactivities.add(diveactivity);
        fireTableDataChanged();
    }

    public void removeDiveType(DiveActivity diveactivity) {
        diveactivities.remove(diveactivity);
        fireTableDataChanged();
    }

    public DiveActivity getDiveActivity(int row) {
        return diveactivities.get(row);
    }

    public void removeDiveActivity(int row) {
        diveactivities.remove(row);
        fireTableRowsDeleted(row, row);
    }

    public String[] getColumnNames() {
        return columns;
    }

    public String getColumnName(int column) {
        return columns[column];
    }
    
    public ArrayList<DiveActivity> getAll() {
        return diveactivities;
    }

}