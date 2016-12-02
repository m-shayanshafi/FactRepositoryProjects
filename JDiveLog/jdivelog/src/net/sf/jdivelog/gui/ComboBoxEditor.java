/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: ComboBoxEditor.java
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

import java.awt.Component;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

public class ComboBoxEditor extends DefaultCellEditor implements TableCellEditor {

    private static final long serialVersionUID = -5002354997185186921L;
    private final JPanel editorPanel;

    public ComboBoxEditor(String[] items) {
        super(new JComboBox(items));
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
