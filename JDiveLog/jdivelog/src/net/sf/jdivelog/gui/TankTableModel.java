/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: TankTableModel.java
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

import javax.swing.table.AbstractTableModel;

import net.sf.jdivelog.gui.resources.Messages;
import net.sf.jdivelog.model.Tank;
import net.sf.jdivelog.util.UnitConverter;

/**
 * Description: Table displaying all the tanks created
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class TankTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 3256720693223044662L;

    private final static String[] columns = {
            Messages.getString("volume") + " [" + UnitConverter.getDisplayVolumeUnit() + "]", Messages.getString("type"), Messages.getString("gas"), Messages.getString("start") + " [" + UnitConverter.getDisplayPressureUnit() + "]", Messages.getString("end") + " [" + UnitConverter.getDisplayPressureUnit() + "]" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$

    private ArrayList<Tank> tanks;

    private UnitConverter toDisplay;

    public TankTableModel(ArrayList<Tank> tanks, int system) {
        this.tanks = tanks;
        setSystem(system);
    }

    public TankTableModel() {
        this.tanks = new ArrayList<Tank>();
        setSystem(UnitConverter.DEFAULT_SYSTEM);
    }

    public void setSystem(int system) {
        this.toDisplay = new UnitConverter(system, UnitConverter.getDisplaySystem());
        fireTableDataChanged();
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
        return tanks.size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    public Object getValueAt(int rowIndex, int columnIndex) {
        Tank tank = tanks.get(rowIndex);
        switch (columnIndex) {
        case 0:
            return toDisplay.convertVolume(tank.getGas().getTankvolume());
        case 1:
            return tank.getType();
        case 2:
            return tank.getGas().getName();
        case 3:
            return toDisplay.convertPressure(tank.getGas().getPstart());
        case 4:
            return toDisplay.convertPressure(tank.getGas().getPend());
        }
        return null;
    }

    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
        case 0:
            return Double.class;
        case 1:
            return String.class;
        case 2:
            return String.class;
        case 3:
            return Double.class;
        case 4:
            return Double.class;
        }
        return String.class;
    }

    public ArrayList<Tank> getTanks() {
        return tanks;
    }

    public void setTanks(ArrayList<Tank> tanks) {
        this.tanks = tanks;
        fireTableDataChanged();
    }

    public void addTank(Tank tank) {
        tanks.add(tank);
        fireTableDataChanged();
    }

    public void removeTank(Tank tank) {
        tanks.remove(tank);
        fireTableDataChanged();
    }

    public Tank getTank(int row) {
        return tanks.get(row);
    }

    public void removeTank(int row) {
        tanks.remove(row);
        fireTableRowsDeleted(row, row);
    }

    public String[] getColumnNames() {
        return columns;
    }

    public String getColumnName(int column) {
        return columns[column];
    }

}