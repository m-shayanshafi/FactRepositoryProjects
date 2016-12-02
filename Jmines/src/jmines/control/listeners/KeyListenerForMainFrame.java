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

import java.awt.event.KeyEvent;

import jmines.view.components.MainPanel;

/**
 * The listener interface for receiving keyboard events (keystrokes).
 *
 * @author Zleurtor
 */
public class KeyListenerForMainFrame implements java.awt.event.KeyListener {

    //==========================================================================
    // Static attributes
    //==========================================================================
    /**
     * The cheat code. the keys have to be typed in the order of the array.
     */
    public static final int[] CHEAT_CODE = {KeyEvent.VK_X, KeyEvent.VK_Y, KeyEvent.VK_Z, KeyEvent.VK_Z, KeyEvent.VK_Y, KeyEvent.VK_ENTER, KeyEvent.VK_SHIFT};

    //==========================================================================
    // Attributes
    //==========================================================================
    /**
     * The step of the cheat code the user is.
     */
    private int cheatCodeStep = 0;
    /**
     * Tell whether or not the cheat mode has been activated.
     */
    private boolean cheatActivated = false;
    /**
     * The main panel of the application.
     */
    private final MainPanel mainPanel;

    //==========================================================================
    // Constructors
    //==========================================================================
    /**
     * Construct a new object that listens key events in the given frame.
     *
     * @param newMainPanel The main panel of the application.
     */
    public KeyListenerForMainFrame(final MainPanel newMainPanel) {
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
     * Invoked when a key has been pressed.
     *
     * @param e The object relating the event that occured.
     * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
     */
    public final void keyPressed(final KeyEvent e) {
        if (!cheatActivated) {
            if (e.getKeyCode() != CHEAT_CODE[cheatCodeStep]) {
                cheatCodeStep = 0;
            }
        }
    }

    /**
     * Invoked when a key has been released.
     *
     * @param e The object relating the event that occured.
     * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
     */
    public final void keyReleased(final KeyEvent e) {
        if (e.getKeyCode() != CHEAT_CODE[cheatCodeStep]) {
            cheatCodeStep = 0;
        } else {
            cheatCodeStep++;
        }

        if (cheatCodeStep == CHEAT_CODE.length) {
            if (!cheatActivated) {
                mainPanel.getCheatPixel().setVisible(true);
                cheatActivated = true;
                cheatCodeStep = 0;
            } else {
                mainPanel.getCheatPixel().setVisible(false);
                cheatActivated = false;
                cheatCodeStep = 0;
            }
        }
    }

    /**
     * Invoked when a key has been typed.
     *
     * @param e The object relating the event that occured.
     * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
     */
    public final void keyTyped(final KeyEvent e) {
    }

    //==========================================================================
    // Static methods
    //==========================================================================

    //==========================================================================
    // Methods
    //==========================================================================
}
