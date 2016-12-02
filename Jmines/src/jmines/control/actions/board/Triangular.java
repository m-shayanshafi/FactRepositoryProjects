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
package jmines.control.actions.board;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import jmines.control.actions.JMinesAction;
import jmines.model.GameBoard;
import jmines.model.TilesShapeUnsupportedException;
import jmines.view.components.MainFrame;
import jmines.view.persistence.Configuration;

/**
 * The class representing the action used when the user click the Triangular
 * menu item.
 *
 * @author Zleurtor
 */
public class Triangular extends JMinesAction {

    //==========================================================================
    // Static attributes
    //==========================================================================
    /**
     * The unique serial version identifier.
     */
    private static final long serialVersionUID = -91360107761230244L;

    //==========================================================================
    // Attributes
    //==========================================================================

    //==========================================================================
    // Constructors
    //==========================================================================
    /**
     * Construct a new Triangular action.
     *
     * @param name The name of the menu item.
     * @param mainFrame The main frame of the application.
     */
    public Triangular(final String name, final MainFrame mainFrame) {
        super(name, mainFrame);

        setStatusText(Configuration.getInstance().getText(Configuration.KEY_STATUSTEXT_BOARD_TRIANGULAR));
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
     * The Method used when the user click on the menu item. Restart a new game
     * using triangular buttons.
     *
     * @param evt The event object relating the event that occurred.
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public final void actionPerformed(final ActionEvent evt) {
        try {
            getMainPanel().getGamePanel().getGameBoard().setTilesShape(GameBoard.SHAPE_TRIANGULAR);
        } catch (TilesShapeUnsupportedException e) {
            JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", Configuration.getInstance().getText(Configuration.KEY_TITLE_ERROR), JOptionPane.ERROR_MESSAGE);
        }
        getMainPanel().getGamePanel().getGameBoard().initialize();

        Configuration.getInstance().putRealTimeconfiguration(Configuration.KEY_USER_SHAPE, Configuration.SHAPE_TRIANGULAR);

        getMainPanel().manageSmiley();
        super.emptyStatusBar();
    }

    //==========================================================================
    // Static methods
    //==========================================================================

    //==========================================================================
    // Methods
    //==========================================================================
}
