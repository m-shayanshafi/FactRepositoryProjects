/*
 * This file is part of JMines.
 * Copyright (C) 2009 Zleurtor
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
 * along with JMines.  If not, see <http://www.gnu.org/licenses/>.
 */
package jmines.control.listeners;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import jmines.view.components.MainPanel;

/**
 * The class used to catch the events that can occur on the main panel.
 *
 * @author Zleurtor
 */
public class ComponentListenerForMainPanel implements ComponentListener {

    //==========================================================================
    // Static attributes
    //==========================================================================

    //==========================================================================
    // Attributes
    //==========================================================================
    /**
     * The main panel on which the events can occur.
     */
    private final MainPanel mainPanel;

    //==========================================================================
    // Constructors
    //==========================================================================
    /**
     * Construct a new listener for a given main panel.
     *
     * @param newMainPanel The main panel on which the events can occur.
     */
    public ComponentListenerForMainPanel(final MainPanel newMainPanel) {
        mainPanel = newMainPanel;
    }

    //==========================================================================
    // Getters
    //==========================================================================

    //==========================================================================
    // Setters
    //==========================================================================

    //==========================================================================
    // Inherited methods
    //==========================================================================
    /**
     * Called method when the main panel is hidden.
     *
     * @param evt The event object relating the event that occurred.
     * @see java.awt.event.ComponentListener#componentHidden(java.awt.event.ComponentEvent)
     */
    public final void componentHidden(final ComponentEvent evt) {
        mainPanel.getTimer().pause();
        mainPanel.setShown(false);
    }

    /**
     * Called method when the main panel is moved.
     *
     * @param evt The event object relating the event that occurred.
     * @see java.awt.event.ComponentListener#componentMoved(java.awt.event.ComponentEvent)
     */
    public final void componentMoved(final ComponentEvent evt) {
        mainPanel.setShown(true);
    }

    /**
     * Called method when the main panel is resized.
     *
     * @param evt The event object relating the event that occurred.
     * @see java.awt.event.ComponentListener#componentResized(java.awt.event.ComponentEvent)
     */
    public final void componentResized(final ComponentEvent evt) {
        mainPanel.setShown(true);
    }

    /**
     * Called method when the main panel is shown.
     *
     * @param evt The event object relating the event that occurred.
     * @see java.awt.event.ComponentListener#componentShown(java.awt.event.ComponentEvent)
     */
    public final void componentShown(final ComponentEvent evt) {
        if (mainPanel.getTopPanel().getTimePanel().getNumber() > 0
                && !mainPanel.getGamePanel().isLost()
                && !mainPanel.getGamePanel().isWon()) {
            mainPanel.getTimer().resume();
        }
        mainPanel.setShown(true);
    }

    //==========================================================================
    // Static methods
    //==========================================================================

    //==========================================================================
    // Methods
    //==========================================================================
}
