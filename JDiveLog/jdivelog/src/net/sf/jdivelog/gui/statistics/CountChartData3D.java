/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: CountChartData3D.java
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
package net.sf.jdivelog.gui.statistics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CountChartData3D {

    private final List<String> rowNames = new ArrayList<String>();

    private final List<String> colNames = new ArrayList<String>();

    private Map<Integer, Map<Integer, Double>> data = new HashMap<Integer, Map<Integer, Double>>();

    private String name;
    private String catLabel;
    private String valLabel;
    
    public CountChartData3D(String name, String catLabel, String valLabel) {
        this.name = name;
        this.catLabel = catLabel;
        this.valLabel = valLabel;
    }

    public List<String> getRowNames() {
        return rowNames;
    }

    public List<String> getColNames() {
        return colNames;
    }

    public void addRow(String row) {
        getRowIdx(row);
    }

    public void addCol(String col) {
        getColIdx(col);
    }

    private synchronized int getRowIdx(String rowName) {
        int idx = rowNames.indexOf(rowName);
        if (idx < 0) {
            rowNames.add(rowName);
        }
        idx = rowNames.indexOf(rowName);
        return idx;
    }

    private synchronized int getColIdx(String colName) {
        int idx = colNames.indexOf(colName);
        if (idx < 0) {
            colNames.add(colName);
        }
        idx = colNames.indexOf(colName);
        return idx;
    }

    private synchronized Map<Integer, Double> getRow(int rowIdx) {
        Integer row = new Integer(rowIdx);
        Map<Integer, Double> r = data.get(row);
        if (r == null) {
            r = new HashMap<Integer, Double>();
            data.put(row, r);
        }
        return r;
    }

    private synchronized void add(int rowIdx, int colIdx) {
        Map<Integer, Double> r = getRow(rowIdx);
        Integer col = new Integer(colIdx);
        Double current = r.get(col);
        if (current == null) {
            r.put(col, new Double(1));
        } else {
            r.put(col, new Double(current.intValue() + 1));
        }
    }

    private synchronized void add(int rowIdx, int colIdx, double val) {
        Map<Integer, Double> r = getRow(rowIdx);
        Integer col = new Integer(colIdx);
        Double current = r.get(col);
        if (current == null) {
            r.put(col, new Double(val));
        } else {
            r.put(col, new Double(current.doubleValue() + val));
        }
    }

    private int get(int rowIdx, int colIdx) {
        Map<Integer, Double> r = getRow(rowIdx);
        Integer col = new Integer(colIdx);
        Double current = r.get(col);
        if (current == null) {
            return 0;
        }
        return current.intValue();
    }

    private double getDouble(int rowIdx, int colIdx) {
        Map<Integer, Double> r = getRow(rowIdx);
        Integer col = new Integer(colIdx);
        Double current = r.get(col);
        if (current == null) {
            return 0;
        }
        return current.doubleValue();
    }

    public void add(String row, String col) {
        int rowIdx = getRowIdx(row);
        int colIdx = getColIdx(col);
        add(rowIdx, colIdx);
    }

    public void add(String row, String col, double val) {
        int rowIdx = getRowIdx(row);
        int colIdx = getColIdx(col);
        add(rowIdx, colIdx, val);
    }

    public int get(String row, String col) {
        int rowIdx = getRowIdx(row);
        int colIdx = getColIdx(col);
        return get(rowIdx, colIdx);
    }

    public double getDouble(String row, String col) {
        int rowIdx = getRowIdx(row);
        int colIdx = getColIdx(col);
        return getDouble(rowIdx, colIdx);
    }

    public String getName() {
        return name;
    }

    public String getCatLabel() {
        return catLabel;
    }

    public String getValLabel() {
        return valLabel;
    }
}
