/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: DivesiteTableModel.java
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

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.jdivelog.gui.LogbookChangeEvent.EventType;
import net.sf.jdivelog.gui.resources.Messages;
import net.sf.jdivelog.gui.util.SortableTableModel;
import net.sf.jdivelog.gui.util.TableSorter;
import net.sf.jdivelog.model.DiveSite;
import net.sf.jdivelog.model.Masterdata;

import org.apache.commons.beanutils.BeanUtils;

/**
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class DivesiteTableModel extends TableSorter implements SortableTableModel, LogbookChangeListener {

    private static final Logger LOGGER = Logger.getLogger(DivesiteTableModel.class.getName());
    private static final long serialVersionUID = -7382896106333103993L;
    
    private final MainWindow mainWindow;
    private final Masterdata masterdata;
    private HashMap<Integer, String> propertyNames;
    
    public DivesiteTableModel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        mainWindow.getLogbookChangeNotifier().addLogbookChangeListener(this);
        masterdata = null;
        updatePropertyNames();
    }

    public DivesiteTableModel(Masterdata masterdata) {
        this.mainWindow = null;
        this.masterdata = masterdata;
        updatePropertyNames();
    }
    
    public void updatePropertyNames() {
        propertyNames = new HashMap<Integer, String>();
        // TODO make property names configurable
        propertyNames.put(0, "spot");
        propertyNames.put(1, "city");
        propertyNames.put(2, "state");
        propertyNames.put(3, "country");
        propertyNames.put(4, "waters");
        fireTableStructureChanged();
    }


    @Override
    public Class<?> getColumnClass(int col) {
        return String.class;
    }
    
    @Override
    public Object getValueAt(int row, int col) {
        return getUnformattedValueAt(row, col);
    }

    @Override
    public int getColumnCount() {
        return propertyNames.size();
    }
    
    @Override
    public int getRowCount() {
        return getDiveSites().size();
    }
    
    @Override
    public String getColumnName(int col) {
        return Messages.getString(getPropertyName(col));
    }
    
    @Override
    public DiveSite getRow(int row) {
        return (DiveSite) getDiveSites().toArray()[row];
    }

    public Class<?> getUnformattedColumnClass(int column) {
        return String.class;
    }
    
    public Object getUnformattedValueAt(int row, int column) {
        DiveSite site = (DiveSite) getDiveSites().toArray()[row];
        String property = getPropertyName(column);
        try {
            return BeanUtils.getSimpleProperty(site, property);
        } catch (InvocationTargetException ite) {
            LOGGER.log(Level.SEVERE, "error getting unformatted value at "+row+", "+column, ite);
            return Messages.getString("error");
        } catch (IllegalAccessException iae) {
            LOGGER.log(Level.SEVERE, "error getting unformatted value at "+row+", "+column, iae);
            return Messages.getString("error");
        } catch (NoSuchMethodException nsme) {
            LOGGER.log(Level.SEVERE, "error getting unformatted value at "+row+", "+column, nsme);
            return Messages.getString("error");
        }
    }
    
    public void logbookChanged(LogbookChangeEvent e) {
        if (!EventType.LOGBOOK_TITLE_CHANGED.equals(e.getType())) {
            fireTableDataChanged();
        }
    }
    
    //
    // private methods
    //
    
    private TreeSet<DiveSite> getDiveSites() {
        return getMasterdata().getDiveSites();
    }
    
    private String getPropertyName(int col) {
        return propertyNames.get(col);
    }
    
    private Masterdata getMasterdata() {
        if (masterdata != null) {
            return masterdata;
        }
        return mainWindow.getLogBook().getMasterdata();
    }
}
