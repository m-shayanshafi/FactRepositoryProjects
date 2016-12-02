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
package jmines.view.components;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JDialog;
import javax.swing.JPanel;

/**
 * This dialog object represent the upper left pixel indicating the content of
 * a cell when the wheat code has been typed.
 *
 * @author Zleurtor
 */
public class CheatPixel extends JDialog {

    //==========================================================================
    // Static attributes
    //==========================================================================
    /**
     * The unique serial version identifier.
     */
    private static final long serialVersionUID = -1944561477801715405L;

    //==========================================================================
    // Attributes
    //==========================================================================
    /**
     * The main panel of the dialog.
     */
    private JPanel background = new JPanel();

    //==========================================================================
    // Constructors
    //==========================================================================
    /**
     * Construct a new cheat pixel.
     */
    public CheatPixel() {
        setLayout(new BorderLayout());
        add(background, BorderLayout.CENTER);
        background.setBackground(Color.WHITE);

        setUndecorated(true);
        setLocation(0, 0);
        setSize(1, 1);
        setFocusable(false);
        setFocusableWindowState(false);
        setAlwaysOnTop(true);
        setVisible(false);
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

    //==========================================================================
    // Static methods
    //==========================================================================

    //==========================================================================
    // Methods
    //==========================================================================
    /**
     * Change the color of the pixel to the given color.
     *
     * @param newColor The new color for the upper left pixel.
     */
    public final void changeColor(final Color newColor) {
        background.setBackground(newColor);
        setVisible(isVisible());
    }

}
