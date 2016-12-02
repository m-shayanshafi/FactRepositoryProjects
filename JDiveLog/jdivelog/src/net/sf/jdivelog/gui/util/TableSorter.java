/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: LogBookTableModel.java
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
package net.sf.jdivelog.gui.util;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

/**
 * A Sorter for Tables
 * 
 * @author Volker Holthaus
 * @version 1.00 2000-02-17
 */
public class TableSorter extends AbstractTableModel implements TableModelListener, RowTableInterface {

    private static final long serialVersionUID = -6818099362390219742L;

    /** The Model, which is used from the table */
    public SortableTableModel model;

    /**
     * The array that contains all information on the order and to which all
     * changes are made
     */
    public int indexList[];

    /**
     * an array containing the fixed rows, that should not be sorted only the
     * blocks between the fixed rows are sorted. each for themselves
     */
    protected int fixedRows[];

    /** Indicates if sort order is ascending or descending */
    protected boolean asc = true;

    /** indicates if the table sorting is enabled or not */
    protected boolean sortTable = true;

    /** indicates if the table has a numbering or not */
    protected boolean displayNumbering = false;

    /** Represents the column order by */
    protected int column = 0;

    /** the text shown in the first columnheader if displaynumbering == true */
    protected String columnheader = "";

    /** the collator with the current locale */
    protected java.text.Collator collator;

    /**
     * Default-Constructor
     */
    public TableSorter() {
        indexList = new int[0];
        collator = java.text.Collator.getInstance();
    }

    /**
     * Constructor
     * 
     * @param model
     *            the TableModel which is used
     */
    public TableSorter(SortableTableModel model) {
        setModel(model);
        collator = java.text.Collator.getInstance();
    }

    public void setColumnHeaderText(String txt) {
        columnheader = new String(txt);
    }

    /**
     * Sets the TableModel in use and adds this class as a TableModelListener
     * @param model tablemodel to use.
     */
    public void setModel(SortableTableModel model) {
        this.model = model;
        rebuildIndexList();
        model.addTableModelListener(this);
    }

    /**
     * Rebuilds the IndexList
     */
    private void rebuildIndexList() {
        int rows = model.getRowCount();
        indexList = new int[rows];
        // initialize the array
        for (int i = 0; i < rows; i++)
            indexList[i] = i;
    }

    /**
     * Compares two rows by the column specified by the global variable column
     * @param row1 index of the first row
     * @param row2 index of the second row
     * @return 0 if both row are equal, 1 if row1 > row2, -1 if row1 < row2
     */
    private int compareRowsByColumn(int row1, int row2) {
        Class<?> type = model.getUnformattedColumnClass(column);

        // check for null
        Object obj1 = model.getUnformattedValueAt(row1, column);
        Object obj2 = model.getUnformattedValueAt(row2, column);
        if (obj1 == null && obj2 == null)
            return 0;
        if (obj1 == null)
            return -1;
        if (obj2 == null)
            return 1;

        // Check for possible classes
        if (type.getSuperclass() == java.lang.Number.class || type.getSuperclass().getSuperclass() == java.lang.Number.class) {
            double d1 = ((Number) obj1).doubleValue();
            double d2 = ((Number) obj2).doubleValue();
            if (d1 < d2)
                return -1;
            if (d1 > d2)
                return 1;
            return 0;
        }
        if (type == String.class) {
            String str1 = (String) obj1;
            String str2 = (String) obj2;
            return collator.compare(str1, str2);
        }
        if (type == Boolean.class) {
            boolean b1 = ((Boolean) obj1).booleanValue();
            boolean b2 = ((Boolean) obj2).booleanValue();
            if (b1 == b2)
                return 0;
            if (b1)
                return 1;
            return -1;
        }
        if (type == java.util.Date.class) {
            long n1 = ((java.util.Date) obj1).getTime();
            long n2 = ((java.util.Date) obj2).getTime();
            if (n1 < n2)
                return -1;
            if (n1 > n2)
                return 1;
            return 0;
        }
        String str1 = obj1.toString();
        String str2 = obj2.toString();
        int res = str1.compareTo(str2);
        if (res < 0)
            return -1;
        if (res > 0)
            return 1;
        return 0;
    }

    private int compare(int row1, int row2) {
        int result = compareRowsByColumn(row1, row2);
        if (result != 0)
            return asc ? result : -result;
        return 0;
    }

    /**
     * Sorting method: implements "Shuttle Sort"
     * @param from 
     * @param to 
     * @param low 
     * @param high 
     */
    private void shuttlesort(int from[], int to[], int low, int high) {
        if (high - low < 2)
            return;

        int middle = (low + high) / 2;
        shuttlesort(to, from, low, middle);
        shuttlesort(to, from, middle, high);

        int p = low;
        int q = middle;

        if (high - low >= 4 && compare(from[middle - 1], from[middle]) <= 0) {
            for (int i = low; i < high; i++) {
                to[i] = from[i];
            }
            return;
        }

        // A normal merge.
        for (int i = low; i < high; i++) {
            if (q >= high || (p < middle && compare(from[p], from[q]) <= 0)) {
                to[i] = from[p++];
            } else {
                to[i] = from[q++];
            }
        }
    }

    /**
     * Starts the sort-Method and fire a TableChanged-Event
     */
    synchronized private void sort() {
        if (fixedRows == null)
            shuttlesort(indexList.clone(), indexList, 0, indexList.length);
        else if (fixedRows.length == 0)
            shuttlesort(indexList.clone(), indexList, 0, indexList.length);
        else {
            // sort each block of rows
            int low = 0;
            int high = 0;
            for (int i = 0; i < fixedRows.length; i++) {
                high = fixedRows[i];
                if (high - low > 2)
                    shuttlesort(indexList.clone(), indexList, low, high);

                low = high + 1;
            }

            high = indexList.length;
            if (high - low > 2)
                shuttlesort(indexList.clone(), indexList, low, high);
        }
        fireTableChanged(new TableModelEvent(this));
    }

    /**
     * Implements TableModel by forwarding the message getValueAt Substitutes
     * the row-Variable for the sorted row-Variable
     * @param row 
     * @param col 
     * @return the (formatted) value to display.
     */
    public Object getValueAt(int row, int col) {
        if (displayNumbering) {
            if (col == 0)
                return new Integer(row + 1);
            return model.getValueAt(indexList[row], col - 1);
        }
        return model.getValueAt(indexList[row], col);
    }

    /**
     * Implements TableModel by forwarding the message setValueAt Substitutes
     * the row-Variable for the sorted row-Variable
     * @param obj 
     * @param row 
     * @param col 
     */
    public void setValueAt(Object obj, int row, int col) {
        if (displayNumbering) {
            if (col > 0)
                model.setValueAt(obj, indexList[row], col - 1);
        } else
            model.setValueAt(obj, indexList[row], col);
    }

    /**
     * Implements TableModel by forwarding the message isCellEditable
     */
    /*
     * public boolean isCellEditable(int row, int col) { if(displayNumbering) {
     * if(col == 0) return false; else return
     * model.isCellEditable(indexList[row], col-1); } else return
     * model.isCellEditable(indexList[row], col); }
     */

    /**
     * Implements TableModel by forwarding the message getRowCount
     * @return number of rows
     */
    public int getRowCount() {
        return (model == null) ? 0 : model.getRowCount();
    }

    /**
     * Implements RowTableInterface
     * @param row index of row
     * @return the row at the specified index
     */
    public Object getRow(int row) {
        Object res;
        try {
            res = ((RowTableInterface) model).getRow(indexList[row]);
        } catch (ClassCastException ccex) {
            res = null;
        } catch (ArrayIndexOutOfBoundsException ex) {
            res = null;
        }
        return res;
    }

    /**
     * Implements TableModel by forwarding the message getColumnCount
     * @return the number of columns
     */
    public int getColumnCount() {
        return (model == null) ? 0 : (displayNumbering ? model.getColumnCount() + 1 : model.getColumnCount());
    }

    /**
     * Implements TableModel by forwarding the message getColumnName
     * @param col index of column
     * @return name of the column
     */
    public String getColumnName(int col) {
        if (displayNumbering) {
            if (col == 0)
                return columnheader;
            return model.getColumnName(col - 1);
        }
        return model.getColumnName(col);
    }

    /**
     * Implements TableModel by forwarding the message getColumnClass
     * @param col index of the column.
     * @return the class which will be returned at the specified column.
     */
    public Class<?> getColumnClass(int col) {
        if (displayNumbering) {
            if (col == 0)
                return Integer.class;
            return model.getColumnClass(col - 1);
        }
        return model.getColumnClass(col);
    }

    /**
     * Returns the table model
     * @return the underlying table model.
     */
    public TableModel getModel() {
        return model;
    }

    /**
     * Implementation of the TableModelListener interface Calls the method
     * rebuildIndexList() and then forwards all events
     * @param e the event.
     */
    public void tableChanged(TableModelEvent e) {
        rebuildIndexList();
        fireTableChanged(e);
    }

    public int getRowIndex(int row) {
        if (row > indexList.length) {
            return -1;
        }
        return indexList[row];
    }

    /**
     * Adds an mouse listener to the table Triggers a table sort when a column
     * heading is clicked and changes the order to descending if the column is
     * clicked twice
     * @param table table to add the listener to.
     */
    public void addMouseListenerToHeader(JTable table) {
        final TableSorter sorter = this;
        final JTable tableView = table;
        JTableHeader th = table.getTableHeader();

        th.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (sortTable) {
                    TableColumnModel columnModel = tableView.getColumnModel();
                    int viewColumn = columnModel.getColumnIndexAtX(e.getX());
                    int col = tableView.convertColumnIndexToModel(viewColumn);

                    if (displayNumbering)
                        col--;

                    if (col != -1) {
                        if (col == sorter.column)
                            asc = !asc;
                        else
                            asc = true;
                        sorter.column = col;
                        sort();
                    }
                }
            }
        });
    }

    /**
     * Sorts the rows.
     * 
     * @param col
     *            the column to sort over
     */
    public void sortColumn(int col) {

        if (col < 0 || col > model.getRowCount())
            return;

        if (col == this.column)
            asc = !asc;
        else
            asc = true;

        this.column = col;
        sort();
    }

    /**
     * Sorts the rows.
     * 
     * @param col
     *            the column to sort over
     * @param ascending
     *            sort ascend
     */
    public void sortColumn(int col, boolean ascending) {

        if (col < 0 || col > model.getRowCount())
            return;

        asc = ascending;

        this.column = col;
        sort();
    }

    /**
     * Sets the sortTable flag.
     * @param sort <code>true</code> if the table should be sorted.
     */
    public void setSortTable(boolean sort) {
        sortTable = sort;
    }

    /**
     * Gets the sortTable flag.
     * @return <code>true</code> if the table is sorted.
     */
    public boolean getSortTable() {
        return sortTable;
    }

    /**
     * Sets the displayNumbering flag.
     * @param flag 
     */
    public void setDisplayNumbering(boolean flag) {
        displayNumbering = flag;
    }

    /**
     * Gets the displayNumbering flag.
     * @return <code>true</code> if numbering should be displayed.
     */
    public boolean getDisplayNumbering() {
        return displayNumbering;
    }

    /**
     * Sets the fixed rows list.
     * 
     * @param list
     *            an array containing the fixed rows
     * @return ???
     */
    public boolean setFixedRows(int[] list) {
        // check list
        if (list != null) {
            int rows = getRowCount();
            int last = -1;
            for (int i = 0; i < list.length; i++) {
                int v = list[i];
                if (v > rows)
                    return false;
                if (v <= last)
                    return false;
                last = v;
            }
        }
        fixedRows = list;
        return true;
    }

    /**
     * Gets the fixed rows list.
     * @return indizes of the fixed rows.
     */
    public int[] getFixedRows() {
        return fixedRows;
    }
}
