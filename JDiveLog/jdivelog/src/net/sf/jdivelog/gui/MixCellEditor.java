/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: GasCellEditor.java
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
import java.awt.Window;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import net.sf.jdivelog.model.MixDatabase;
import net.sf.jdivelog.model.Mix;

public class MixCellEditor implements TableCellEditor, TableCellRenderer {
    
    private static final long serialVersionUID = 1L;
    private final Window parent;
    private final MixDatabase db;
    private final MixField field;
    private final List<CellEditorListener> listeners;

    public MixCellEditor(Window parent, MixDatabase db) {
        this.parent = parent;
        this.db = db;
        field = new MixField(parent, db);
        listeners = new ArrayList<CellEditorListener>();
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        MixField field = new MixField(parent, db);
        if (value instanceof Mix) {
            field.setMix((Mix)value);
        } else {
            field.setMix(null);
        }
        return field;
    }
    
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if (value instanceof Mix) {
            field.setMix((Mix)value);
        } else {
            field.setMix(null);
        }
        return field;
    }

    public void addCellEditorListener(CellEditorListener l) {
        listeners.add(l);
    }

    public void cancelCellEditing() {
        ChangeEvent e = new ChangeEvent(this);
        List<CellEditorListener> ls = new ArrayList<CellEditorListener>(listeners);
        for(CellEditorListener l : ls) {
            l.editingCanceled(e);
        }
    }

    public Object getCellEditorValue() {
        return field.getMix();
    }

    public boolean isCellEditable(EventObject anEvent) {
        return true;
    }

    public boolean shouldSelectCell(EventObject anEvent) {
        return false;
    }

    public void removeCellEditorListener(CellEditorListener l) {
        listeners.remove(l);
    }

    public boolean stopCellEditing() {
        boolean result = field.isGasValid();
        if (result) {
            ChangeEvent e = new ChangeEvent(this);
            List<CellEditorListener> ls = new ArrayList<CellEditorListener>(listeners);
            for(CellEditorListener l : ls) {
                l.editingStopped(e);
            }
        }
        return result;
    }

}
