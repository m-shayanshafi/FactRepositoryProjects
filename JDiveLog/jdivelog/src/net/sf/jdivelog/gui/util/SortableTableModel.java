/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: SortableTableModel.java
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

import javax.swing.table.TableModel;

public interface SortableTableModel extends TableModel {

    /**
     * @param column index of the column.
     * @return the class of the unformatted value.
     */
    public Class<?> getUnformattedColumnClass(int column);

    /**
     * @param row index of the row.
     * @param column index of the column.
     * @return the unformatted value (e.g. date as java.util.Date, ...)
     */
    public Object getUnformattedValueAt(int row, int column);

}
