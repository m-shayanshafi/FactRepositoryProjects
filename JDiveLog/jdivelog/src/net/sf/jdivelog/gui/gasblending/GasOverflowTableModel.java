/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: GasOverFlowTableModel.java
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
package net.sf.jdivelog.gui.gasblending;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import net.sf.jdivelog.gui.resources.Messages;
import net.sf.jdivelog.gui.util.CheckboxEditor;
import net.sf.jdivelog.gui.util.CheckboxRenderer;
import net.sf.jdivelog.model.gasoverflow.GasOverflowSource;
import net.sf.jdivelog.util.UnitConverter;

public class GasOverflowTableModel extends AbstractTableModel implements TableModel {

    private List<GasOverflowSource> sources;

    private TableColumnModel columnModel;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GasOverflowTableModel() {
        columnModel = new DefaultTableColumnModel();
        TableColumn col = new TableColumn(0, 2);
        col.setCellRenderer(new CheckboxRenderer());
        col.setCellEditor(new CheckboxEditor());
        col = new TableColumn(0, 20);
        col.setHeaderValue(Messages.getString("overfill.gasoverflowsource.tankdescription")); //$NON-NLS-1$
        columnModel.addColumn(col);
        col = new TableColumn(1, 20);
        col.setHeaderValue(Messages.getString("mixing.gasoverflowsource.tanksize") + " [" + UnitConverter.getDisplayVolumeUnit() + "]"); //$NON-NLS-1$
        columnModel.addColumn(col);
        col = new TableColumn(2, 20);
        col.setHeaderValue(Messages.getString("mixing.gasoverflowsource.tankpressure") + " [" +UnitConverter.getDisplayPressureUnit() + "]"); //$NON-NLS-1$
        columnModel.addColumn(col);
        sources = new ArrayList<GasOverflowSource>();
	}

	public int getColumnCount() {
        return 3;
	}


	public int getRowCount() {
		// TODO Auto-generated method stub
		return sources.size();
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
        GasOverflowSource source = sources.get(rowIndex);
        switch (columnIndex) {
        case 0:
            return source.getTankdescription();
        case 1:
            return source.getTanksize();
        case 2:
            return source.getTankpressure();
        }
        return null;
	}
	
    public TableColumnModel getColumnModel() {
        return columnModel;
    }
    
    public void addSource(GasOverflowSource source) {
        sources.add(source);
        fireTableDataChanged();
    }
    
    public GasOverflowSource getSource(int row) {
        return sources.get(row);
    }

    public void removeRow(int row) {
        sources.remove(row);
        fireTableDataChanged();
    }

	public List<GasOverflowSource> getSources() {
		return sources;
	}

	public void setSources(List<GasOverflowSource> sources) {
		this.sources = sources;
	}

}
