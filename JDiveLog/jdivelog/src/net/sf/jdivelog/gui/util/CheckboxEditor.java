/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: CheckboxEditor.java
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

import java.awt.Component;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTable;

/**
 * Editor for Checkbox Table Cells.
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class CheckboxEditor extends DefaultCellEditor {

    private static final long serialVersionUID = -7567705418274451431L;
    private final JPanel editorPanel;

    public CheckboxEditor() {
        super(new JCheckBox());
        editorPanel = new JPanel();
        editorPanel.add(editorComponent);
        this.clickCountToStart = 1;
    }
    
    /**
     * @see javax.swing.table.TableCellEditor#getTableCellEditorComponent(javax.swing.JTable, java.lang.Object, boolean, int, int)
     */
    public Component getTableCellEditorComponent(JTable table, Object value,
                         boolean isSelected,
                         int row, int column) {
        delegate.setValue(value);
        return editorPanel;
    }

}
