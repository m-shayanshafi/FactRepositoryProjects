/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: ImportTableModel.java
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

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.TreeSet;

import javax.swing.table.AbstractTableModel;

import net.sf.jdivelog.gui.resources.Messages;
import net.sf.jdivelog.model.JDive;
import net.sf.jdivelog.util.DateFormatUtil;
import net.sf.jdivelog.util.UnitConverter;


/**
 * Description: Table with import dives
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class ImportTableModel extends AbstractTableModel {
    
    private static final long serialVersionUID = 3258125851869917497L;

    private static final NumberFormat DECIMALFORMAT = new DecimalFormat(Messages.getString("numberformat")); //$NON-NLS-1$
    
    private String[] columns = new String[] {Messages.getString("date"), Messages.getString("depth")+" ["+UnitConverter.getDisplayAltitudeUnit()+"]", Messages.getString("duration")+" ["+UnitConverter.getDisplayTimeUnit()+"]" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
    
    private TreeSet <JDive> jDives = null;
    
    public ImportTableModel(TreeSet <JDive> jDives) {
        this.jDives = jDives;
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    public int getColumnCount() {
        return columns.length;
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getRowCount()
     */
    public int getRowCount() {
        return this.jDives.size();
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    public Object getValueAt(int rowIndex, int columnIndex) {
        JDive dive = (JDive)jDives.toArray()[rowIndex];
        UnitConverter c = new UnitConverter(UnitConverter.getSystem(dive.getUnits()), UnitConverter.getDisplaySystem());
        switch (columnIndex) {
        	case 0: return DateFormatUtil.getDateTimeFormat().format(dive.getDate());
        	case 1: return dive.getDepth() == null ? null : DECIMALFORMAT.format(c.convertAltitude(dive.getDepth()));
        	case 2: return dive.getDuration() == null ? null : DECIMALFORMAT.format(c.convertTime(dive.getDuration()));
        }
        return null;
    }
    public String[] getColumnNames() {
        return columns;
    }
    
    public String getColumnName(int column) {
        return columns[column];
    }

    /**
     * @param row The selected row
     * @return JDive The dive at the selected row
     */
    public JDive getDive(int row) {
        return (JDive)this.jDives.toArray()[row];
    }

}
