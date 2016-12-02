/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: DiveImportDataTrakTableModel.java
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

import javax.swing.table.AbstractTableModel;

import net.sf.jdivelog.gui.resources.Messages;
import net.sf.jdivelog.model.JDive;
import net.sf.jdivelog.model.Masterdata;
import net.sf.jdivelog.util.DateFormatUtil;

/**
 * Description: Table in order to import DataTrak data
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class DiveImportDataTrakTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 3256720693223044662L;

    private final static String[] columns = {
            Messages.getString("date"), 
            Messages.getString("place"),
            Messages.getString("country"), 
            Messages.getString("depth"),
            Messages.getString("duration")           
    }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$

    private ArrayList<JDive> divesToAdd;

    private Masterdata masterdata;
    

    public DiveImportDataTrakTableModel(Masterdata masterdata, ArrayList<JDive> divesToAdd) {
        this.masterdata = masterdata;
        this.divesToAdd = divesToAdd;
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
        return divesToAdd.size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    public Object getValueAt(int rowIndex, int columnIndex) {
        JDive dive = divesToAdd.get(rowIndex);
        switch (columnIndex) {
        case 0:
            return dive.getDate() == null ? null : DateFormatUtil.getDateTimeFormat().format(dive.getDate());        
        case 1:
            return masterdata.getDiveSite(dive) == null ? null : masterdata.getDiveSite(dive).getSpot();
        case 2:
            return masterdata.getDiveSite(dive) == null ? null : masterdata.getDiveSite(dive).getCountry();
        case 3:
            return dive.getDepth();
        case 4:
            return dive.getDuration();
        }
        return null;
    }

    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    public ArrayList<JDive> getDivingPlaceCountries() {
        return divesToAdd;
    }

    public void setDivingPlaceCountries(ArrayList<JDive> divesToAdd) {
        this.divesToAdd = divesToAdd;
        fireTableDataChanged();
    }


    public JDive getDiveFromDataTrak(int row) {
        return divesToAdd.get(row);
    }

    public String[] getColumnNames() {
        return columns;
    }

    public String getColumnName(int column) {
        return columns[column];
    }

}