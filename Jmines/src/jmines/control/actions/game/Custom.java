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

import jmines.control.actions.JMinesAction;
import jmines.view.components.MainFrame;
import jmines.view.dialogs.CustomDialog;
import jmines.view.dialogs.CustomDialog.Values;
import jmines.view.persistence.Configuration;

/**
 * The class representing the action used when the user click the Custom menu
 * item.
 *
 * @author Zleurtor
 */
public class Custom extends JMinesAction {

    //==========================================================================
    // Static attributes
    //==========================================================================
    /**
     * The unique serial version identifier.
     */
    private static final long serialVersionUID = -1980429650032904577L;

    //==========================================================================
    // Attributes
    //==========================================================================
    /**
     * The dialog that allows to input all needed information (the number of
     * lines, number of columns and number of mines).
     */
    private final CustomDialog dialog;

    //==========================================================================
    // Constructors
    //==========================================================================
    /**
     * Construct a new Custom action.
     *
     * @param name The name of the menu item.
     * @param mainFrame The main frame of the application.
     */
    public Custom(final String name, final MainFrame mainFrame) {
        super(name, mainFrame);

        dialog = new CustomDialog(mainFrame.getMainPanel());

        setStatusText(Configuration.getInstance().getText(Configuration.KEY_STATUSTEXT_GAME_CUSTOM));
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
     * The Method used when the user click on the menu item. This shows a
     * dialog needing information about the next game (number of lines, of
     * columns and mines) and then restart a game.
     *
     * @param evt The event object relating the event that occurred.
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public final void actionPerformed(final ActionEvent evt) {
        // Display a dialog
        Values values = dialog.getValues();
        if (values == null) {
            return;
        }

        final int width = values.getWidth();
        final int height = values.getHeight();
        final int mines = values.getMines();

        Configuration configuration = Configuration.getInstance();

        getMainPanel().getGamePanel().getGameBoard().setWidth(width);
        getMainPanel().getGamePanel().getGameBoard().setHeight(height);
        getMainPanel().getGamePanel().getGameBoard().setNumberOfMines(mines, true);
        getMainPanel().getGamePanel().getGameBoard().initialize();
        getMainPanel().getGamePanel().setDifficulty(Configuration.DIFFICULTY_CUSTOM);

        configuration.putRealTimeconfiguration(Configuration.KEY_USER_DIFFICULTY,
                Configuration.DIFFICULTY_CUSTOM + Configuration.COMA + width + Configuration.COMA + height + Configuration.COMA + mines);

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
