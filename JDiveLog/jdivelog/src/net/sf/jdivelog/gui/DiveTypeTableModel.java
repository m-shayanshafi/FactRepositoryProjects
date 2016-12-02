/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: DiveTypeTableModel.java
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
import net.sf.jdivelog.model.DiveType;

/**
 * Description: Table displaying the dive types 
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class DiveTypeTableModel extends AbstractTableModel implements SortableTableModel {

    private static final long serialVersionUID = 3256720693223044662L;

    private final static String[] columns = {
            Messages.getString("description")}; //$NON-NLS-1$

    private ArrayList<DiveType> divetypes;

    DiveTypeWindow divetypeWindow = null;
    

    public DiveTypeTableModel(DiveTypeWindow divetypeWindow, ArrayList<DiveType> divetypes) {
        this.divetypeWindow = divetypeWindow;
        SortedSet<DiveType> sorted = new TreeSet<DiveType>(divetypes);
        this.divetypes = new ArrayList<DiveType>(sorted);
    }

    public DiveTypeTableModel(DiveTypeWindow divetypeWindow) {
        this.divetypeWindow = divetypeWindow;
        this.divetypes = new ArrayList<DiveType>();
    }

    public int getColumnCount() {
        return columns.length;
    }

    public int getRowCount() {
        return divetypes.size();
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
        case 0:
            return divetypes.get(rowIndex).getDescription();
        }
        return null;
    }

    public void load(ArrayList<DiveType> divetypes2) {
        SortedSet<DiveType> sorted = new TreeSet<DiveType>(divetypes2);
        divetypes = new ArrayList<DiveType>(sorted);
        fireTableDataChanged();
    }

    public Object getUnformattedValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
        case 0:
            return divetypes.get(rowIndex).getDescription();
        }
        return null;
    }

    public Class<?> getColumnClass(int columnIndex) {
        return DiveType.class;
    }

    public Class<?> getUnformattedColumnClass(int columnIndex) {
        return DiveType.class;
    }

    public ArrayList<DiveType> getDivetypes() {
        return divetypes;
    }

    public void setDiveTypes(ArrayList<DiveType> divetypes) {
        this.divetypes = divetypes;
        fireTableDataChanged();
    }

    public void addDiveType(DiveType divetype) {
        divetypes.add(divetype);
        fireTableDataChanged();
    }

    public void removeDiveType(DiveType divetype) {
        divetypes.remove(divetype);
        fireTableDataChanged();
    }

    public DiveType getDiveType(int row) {
        return divetypes.get(row);
    }

    public void removeDiveType(int row) {
        divetypes.remove(row);
        fireTableRowsDeleted(row, row);
    }

    public String[] getColumnNames() {
        return columns;
    }

    public String getColumnName(int column) {
        return columns[column];
    }
    
    public ArrayList<DiveType> getAll() {
        return divetypes;
    }

}