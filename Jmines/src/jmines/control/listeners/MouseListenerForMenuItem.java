/*
 * This file is part of JMines.
 *
 * JMines is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JMines is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with JMines. If not, see <http://www.gnu.org/licenses/>.
 */
package jmines.control.listeners;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenuItem;

import jmines.control.actions.JMinesAction;
import jmines.view.components.MainPanel;

/**
 * The object ised to listen mouse events on the menu items.
 *
 * @author Zleurtor
 */
public class MouseListenerForMenuItem implements MouseListener {

    //=========================================================================
    // Static attributes
    //=========================================================================

    //=========================================================================
    // Attributes
    //=========================================================================
    /**
     * The item for which the events are listened.
     */
    private final JMenuItem item;
    /**
     * The main panel in which events will be treated.
     */
    private final MainPanel mainPanel;

    //=========================================================================
    // Constructors
    //=========================================================================
    /**
     * Construct a new listener for menu items using a given item and a given
     * main panel.
     *
     * @param newItem The item for which the events are listened.
     * @param newMainPanel The main panel in which events will be treated.
     */
    public MouseListenerForMenuItem(final JMenuItem newItem, final MainPanel newMainPanel) {
        item = newItem;
        mainPanel = newMainPanel;
    }

    //=========================================================================
    // Getters
    //=========================================================================

    //=========================================================================
    // Setters
    //=========================================================================

    //=========================================================================
    // Inherited methods
    //=========================================================================
    /**
     * The method called when the mouse button is clicked over a menu item.
     *
     * @param evt The event Object representing the mouse clicking.
     * @see java.awt.event.MouseAdapter#mouseEntered(java.awt.event.MouseEvent)
     */
    @Override
    public final void mouseClicked(final MouseEvent evt) {
    }

    /**
     * The method called when the mouse enter a menu item.
     *
     * @param evt The event Object representing the mouse entering.
     * @see java.awt.event.MouseAdapter#mouseEntered(java.awt.event.MouseEvent)
     */
    @Override
    public final void mouseEntered(final MouseEvent evt) {
        if (item.getAction() != null) {
            mainPanel.getStatusBar().setText(((JMinesAction) item.getAction()).getStatusText());
        }
    }

    /**
     * The method called when the mouse exit from a menu item.
     *
     * @param evt The event Object representing the mouse exiting.
     * @see java.awt.event.MouseAdapter#mouseExited(java.awt.event.MouseEvent)
     */
    @Override
    public final void mouseExited(final MouseEvent evt) {
        mainPanel.getStatusBar().setText(" ");
    }

    /**
     * The method called when the mouse button is pressed over a menu item.
     *
     * @param evt The event Object representing the mouse clicking.
     * @see java.awt.event.MouseAdapter#mouseEntered(java.awt.event.MouseEvent)
     */
    @Override
    public final void mousePressed(final MouseEvent evt) {
    }

    /**
     * The method called when the mouse button is released over a menu item.
     *
     * @param evt The event Object representing the mouse clicking.
     * @see java.awt.event.MouseAdapter#mouseEntered(java.awt.event.MouseEvent)
     */
    @Override
    public final void mouseReleased(final MouseEvent evt) {
    }

    //=========================================================================
    // Static methods
    //=========================================================================

    //=========================================================================
    // Methods
    //=========================================================================
}
