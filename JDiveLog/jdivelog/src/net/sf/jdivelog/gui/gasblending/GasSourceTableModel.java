/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: GasSourceTableModel.java
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
import net.sf.jdivelog.model.GasFractionComparator;
import net.sf.jdivelog.model.GasFractions;
import net.sf.jdivelog.model.gasblending.GasSource;

/**
 * List of PoolTanks
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class GasSourceTableModel extends AbstractTableModel implements TableModel {

    private static final long serialVersionUID = -8002609900790551284L;

    private List<GasSource> sources;

    private TableColumnModel columnModel;

    public GasSourceTableModel() {
        columnModel = new DefaultTableColumnModel();
        TableColumn col = new TableColumn(0, 3);
        col.setCellRenderer(new CheckboxRenderer());
        col.setCellEditor(new CheckboxEditor());
        col.setHeaderValue(" ");
        columnModel.addColumn(col);
        col = new TableColumn(1, 20);
        col.setHeaderValue(Messages.getString("mixing.gassource.description")); //$NON-NLS-1$
        columnModel.addColumn(col);
        col = new TableColumn(2, 4);
        col.setHeaderValue("O2"); //$NON-NLS-1$
        columnModel.addColumn(col);
        col = new TableColumn(3, 4);
        col.setHeaderValue("He"); //$NON-NLS-1$
        columnModel.addColumn(col);
        col = new TableColumn(4, 4);
        col.setHeaderValue("N2"); //$NON-NLS-1$
        columnModel.addColumn(col);
        col = new TableColumn(5, 20);
        col.setHeaderValue(Messages.getString("mixing.gassource.type")); //$NON-NLS-1$
        columnModel.addColumn(col);
        sources = new ArrayList<GasSource>();
    }

    /**
     * @see javax.swing.table.TableModel#getRowCount()
     */
    public int getRowCount() {
        return sources.size();
    }

    /**
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    public int getColumnCount() {
        return 5;
    }

    /**
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    public Object getValueAt(int row, int col) {
        GasSource source = sources.get(row);
        switch (col) {
        case 0:
            return source.isEnabled();
        case 1:
            return source.getDescription();
        case 2:
            return source.getMix() != null ? source.getMix().getOxygen() : null;
        case 3:
            return source.getMix() != null ? source.getMix().getHelium() : null;
        case 4:
            return source.getMix() != null ? source.getMix().getNitrogen() : null;
        case 5:
            if (source.isCompressor()) {
                return Messages.getString("mixing.compressor") + ": " + Messages.getString("mixing.max_pressure") + "=" + Math.round(source.getPressure());
            }
            return Messages.getString("mixing.tank") + ": " + Messages.getString("mixing.volume") + "=" + Math.round(source.getSize()) + ", "
                    + Messages.getString("mixing.pressure") + "=" + Math.round(source.getPressure());
        }
        return null;
    }
    
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (columnIndex == 0 && aValue instanceof Boolean) {
            sources.get(rowIndex).setEnabled((Boolean)aValue);
        }
    }
    
    public List<GasSource> findSources(GasFractions mix) {
        ArrayList<GasSource> result = new ArrayList<GasSource>();
        for (GasSource source : sources) {
            if (GasFractionComparator.match(source.getMix(), mix)) {
                result.add(source);
            }
        }
        return result;
    }

    public List<GasSource> findHelium() {
        ArrayList<GasSource> result = new ArrayList<GasSource>();
        for (GasSource source : sources) {
            if (source.getMix().getHelium() == 100 && source.isEnabled()) {
                result.add(source);
            }
        }
        return result;
    }
    
    public List<GasSource> findOxygen() {
        ArrayList<GasSource> result = new ArrayList<GasSource>();
        for (GasSource source : sources) {
            if (source.getMix().getOxygen() == 100 && source.isEnabled()) {
                result.add(source);
            }
        }
        return result;
    }
    
    public List<GasSource> findCompressors() {
        ArrayList<GasSource> result = new ArrayList<GasSource>();
        for (GasSource source : sources) {
            if (source.isCompressor() && source.isEnabled()) {
                result.add(source);
            }
        }
        return result;
    }
    
    public List<GasSource> getSources() {
        return sources;
    }
    
    public void setSources(List<GasSource> newSources) {
        sources = newSources;
        fireTableDataChanged();
    }

    public GasSource getGasSource(int row) {
        return sources.get(row);
    }

    public void removeRow(int row) {
        sources.remove(row);
        fireTableDataChanged();
    }
    
    public void addSource(GasSource source) {
        sources.add(source);
        fireTableDataChanged();
    }

    public TableColumnModel getColumnModel() {
        return columnModel;
    }
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 0;
    }
    
}
