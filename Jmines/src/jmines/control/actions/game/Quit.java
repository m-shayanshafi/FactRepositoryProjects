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
package jmines.control.actions.game;

import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;

import jmines.control.actions.JMinesAction;
import jmines.view.components.MainFrame;
import jmines.view.persistence.Configuration;

/**
 * The class representing the action used when the user click the Quit menu
 * item.
 *
 * @author Zleurtor
 */
public class Quit extends JMinesAction {

    //==========================================================================
    // Static attributes
    //==========================================================================
    /**
     * The unique serial version identifier.
     */
    private static final long serialVersionUID = 4397277457595852201L;

    //==========================================================================
    // Attributes
    //==========================================================================

    //==========================================================================
    // Constructors
    //==========================================================================
    /**
     * Construct a new Quit action.
     *
     * @param name The name of the menu item.
     * @param mainFrame The main frame of the application.
     */
    public Quit(final String name, final MainFrame mainFrame) {
        super(name, mainFrame);

        setStatusText(Configuration.getInstance().getText(Configuration.KEY_STATUSTEXT_GAME_QUIT));
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
     * The Method used when the user click on the menu item. Displays a dialog
     * then quit (or not) the game.
     *
     * @param evt The event object relating the event that occurred.
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public final void actionPerformed(final ActionEvent evt) {
        getMainFrame().dispatchEvent(new WindowEvent(getMainFrame(), WindowEvent.WINDOW_CLOSING));

        super.emptyStatusBar();
    }

    //==========================================================================
    // Static methods
    //==========================================================================

    //==========================================================================
    // Methods
    //==========================================================================
}
