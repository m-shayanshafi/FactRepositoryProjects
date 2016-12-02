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
package jmines.control.actions.robot;

import java.awt.event.ActionEvent;

import jmines.control.actions.JMinesAction;
import jmines.view.components.MainFrame;
import jmines.view.persistence.Configuration;

/**
 * The class representing the action used when the user click the Cheat menu
 * item.
 *
 * @author Zleurtor
 */
public class Play extends JMinesAction {

    //==========================================================================
    // Static attributes
    //==========================================================================
    /**
     * The unique serial version identifier.
     */
    private static final long serialVersionUID = -1706213499224549152L;

    //==========================================================================
    // Attributes
    //==========================================================================

    //==========================================================================
    // Constructors
    //==========================================================================
    /**
     * Construct a new Cheat action.
     *
     * @param name The name of the menu item.
     * @param mainFrame The main frame of the application.
     */
    public Play(final String name, final MainFrame mainFrame) {
        super(name, mainFrame);

        setStatusText(Configuration.getInstance().getText(Configuration.KEY_STATUSTEXT_ROBOT_PLAY));
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
     * The Method used when the user click on the menu item. Start the robot
     * playing.
     *
     * @param evt The event object relating the event that occurred.
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public final void actionPerformed(final ActionEvent evt) {
        if (getMainPanel().getGamePanel().isWon() || getMainPanel().getGamePanel().isLost()) {
            getMainPanel().getTopPanel().getSmileyButton().doClick();
        }

        Thread playThread = new Thread() {
            /**
             * The thread used by the robot to play.
             *
             * @see java.lang.Thread#run()
             */
            @Override
            public void run() {
                getMainPanel().getTopPanel().getSmileyButton().setEnabled(false);
                getMainPanel().getRobot().play();
                getMainPanel().getTopPanel().getSmileyButton().setEnabled(true);
            }
        };

        playThread.start();

        super.emptyStatusBar();
    }

    //==========================================================================
    // Static methods
    //==========================================================================

    //==========================================================================
    // Methods
    //==========================================================================
}
