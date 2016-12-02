/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: TextAreaCellRenderer.java
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

import java.awt.Color;
import java.awt.Component;
import java.util.EventObject;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.EventListenerList;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

/**
 * Description: Cell containing text
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class TextAreaCellRenderer extends JTextArea implements TableCellRenderer, TableCellEditor {

    private static final long serialVersionUID = 3258128063777813301L;

    /** EventListenerList to handle the events within this editor */
    private EventListenerList listenerList = new EventListenerList();

    /** Event for handlich cancel and stop editing events */
    private ChangeEvent changeEvent = new ChangeEvent(this);

    /** Border configuration */
    private static Border noFocusBorder;

    /** Color of unselected foreground */
    private Color unselectedForeground;

    /** Color of unselected background */
    private Color unselectedBackground;

    /**
     * The constructor
     * 
     * @param rows
     *            number of rows
     * @param columns
     *            number of columns
     */
    public TextAreaCellRenderer(int rows, int columns) {
        super(rows, columns);

        /*
         * Sets the line-wrapping policy of the text area. If set to true the
         * lines will be wrapped if they are too long to fit within the
         * allocated width.
         */

        setLineWrap(true);

        /*
         * Sets the style of wrapping used if the text area is wrapping lines.
         * If set to true the lines will be wrapped at word boundaries
         * (whitespace) if they are too long to fit within the allocated width.
         */

        setWrapStyleWord(true);
        setOpaque(true);
    }

    /**
     * Returns the editor component, a JTextArea. Interface CellEditor.
     * 
     * @param table
     *            The Table.
     * @param value
     *            the value to render.
     * @param isSelected
     *            if the cell is selected
     * @param row
     *            the row of the cell to render
     * @param column
     *            the column of the cell to render
     * @return textarea as cell editor
     */
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        TableModel model = table.getModel();
        setText((String) model.getValueAt(row, column));
        return new JScrollPane(this);
    }

    /**
     * Returns true if the editing cell should be selected, false otherwise.
     * Interface CellEditor.
     * 
     * @param event
     *            the event
     * @return always true
     */
    public boolean shouldSelectCell(EventObject event) {
        return true;
    }

    /**
     * Asks the editor if it can start editing using event. event is in the
     * invoking component coordinate system. Interface CellEditor.
     * 
     * @param event
     *            the event
     * @return always true
     */
    public boolean isCellEditable(EventObject event) {
        return true;
    }

    /**
     * Returns the value contained in the editor. Because this is a text area,
     * the value contained in this editor is text. Interface CellEditor.
     * 
     * @return text of text area
     */
    public Object getCellEditorValue() {
        return this.getText();
    }

    /**
     * Adds a listener to the list that's notified when the editor stops, or
     * cancels editing. Interface CellEditor.
     * 
     * @param listener
     *            a CellEditorListener
     */
    public void addCellEditorListener(CellEditorListener listener) {
        getListenerList().add(CellEditorListener.class, listener);
    }

    /**
     * Removes a listener from the list that's notified. Interface CellEditor.
     * 
     * @param listener
     *            a CellEditorListener
     */
    public void removeCellEditorListener(CellEditorListener listener) {
        getListenerList().remove(CellEditorListener.class, listener);
    }

    /**
     * Notifies all listeners that have registered interest for notification on
     * this event type. The event instance is created lazily.
     */
    protected void fireEditingStopped() {
        CellEditorListener listener;
        Object[] listeners = getListenerList().getListenerList();
        for (int i = 0; i < listeners.length; i++) {
            if (listeners[i] == CellEditorListener.class) {
                listener = (CellEditorListener) listeners[i + 1];
                listener.editingStopped(changeEvent);
            }
        }
    }

    /**
     * Notifies all listeners that have registered interest for notification on
     * this event type. The event instance is created lazily.
     */
    protected void fireEditingCanceled() {
        CellEditorListener listener;
        Object[] listeners = getListenerList().getListenerList();
        for (int i = 0; i < listeners.length; i++) {
            if (listeners[i] == CellEditorListener.class) {
                listener = (CellEditorListener) listeners[i + 1];
                listener.editingCanceled(changeEvent);
            }
        }
    }

    /**
     * Returns the value contained in the editor. Interface CellEditor.
     * 
     * @return always true
     */
    public boolean stopCellEditing() {
        fireEditingStopped();
        return true;
    }

    /**
     * Tells the editor to stop editing and accept any partially edited value as
     * the value of the editor. The editor returns false if editing was not
     * stopped; this is useful for editors that validate and can not accept
     * invalid entries. Interface CellEditor.
     */
    public void cancelCellEditing() {
        fireEditingCanceled();
    }

    /**
     * Sets the component's value. Because the component is a text area, the
     * value is text.
     * @param value The value to set.
     */
    protected void setValue(Object value) {
        setText((value == null) ? "" : value.toString());
    }

    /**
     * Sets the foreground color of the component. Overridden from JComponent.
     * 
     * @param c
     *            the foreground color
     */
    public void setForeground(Color c) {
        super.setForeground(c);
        unselectedForeground = c;
    }

    /**
     * Sets the background color of the component. Overridden from JComponent.
     * 
     * @param c
     *            the background color
     */
    public void setBackground(Color c) {
        super.setBackground(c);
        unselectedBackground = c;
    }

    /** Updates the UI if necessary. Overridden from JTextComponent. */
    public void updateUI() {
        super.updateUI();
        setForeground(null);
        setBackground(null);
    }

    /**
     * Get the unselected background color
     * 
     * @return the unselectedBackground
     */
    public Color getUnselectedBackground() {
        return unselectedBackground;
    }

    /**
     * Set the unselected background color
     * 
     * @param unselectedBackground
     *            the unselectedBackgroundColor
     */
    public void setUnselectedBackground(Color unselectedBackground) {
        this.unselectedBackground = unselectedBackground;
    }

    /**
     * Get the unselected foreground color
     * 
     * @return unselectedForegroundColor
     */
    public Color getUnselectedForeground() {
        return unselectedForeground;
    }

    /**
     * Set the unselected foreground color
     * 
     * @param unselectedForeground
     *            the unselectedForegroundColor
     */
    public void setUnselectedForeground(Color unselectedForeground) {
        this.unselectedForeground = unselectedForeground;
    }

    /**
     * Sets the border for no focus
     * 
     * @param noFocusBorder
     *            border for no focus
     */
    public static void setNoFocusBorder(Border noFocusBorder) {
        TextAreaCellRenderer.noFocusBorder = noFocusBorder;
    }

    /**
     * Gets the border for no focus
     * 
     * @return noFocusBorder border for no focus
     */
    public static Border getNoFocusBorder() {
        return noFocusBorder;
    }

    /**
     * Gets the event listener list
     * 
     * @return the event listener list
     */
    public EventListenerList getListenerList() {
        return listenerList;
    }

    /**
     * Sets the event listener list
     * 
     * @param listenerList
     *            The event listener list.
     */
    public void setListenerList(EventListenerList listenerList) {
        this.listenerList = listenerList;
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        TableModel model = table.getModel();
        setText((String) model.getValueAt(row, column));
        return new JScrollPane(this);
    }
}