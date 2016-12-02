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
package jmines.control.actions;

import java.awt.Color;

import javax.swing.AbstractAction;
import javax.swing.Icon;

import jmines.view.components.MainFrame;
import jmines.view.components.MainPanel;

/**
 * The super class of all actions used in JMines.
 *
 * @author Zleurtor
 */
public abstract class JMinesAction extends AbstractAction {

    //=========================================================================
    // Static attributes
    //=========================================================================
    /**
     * The unique serial version identifier.
     */
    private static final long serialVersionUID = -4830440285847430823L;

    //=========================================================================
    // Attributes
    //=========================================================================
    /**
     * The main panel for which the action can occurs.
     */
    private MainFrame mainFrame;
    /**
     * The text to display in the status bar.
     */
    private String statusText;

    //=========================================================================
    // Constructors
    //=========================================================================
    /**
     * Construct a new JMinesAction.
     *
     * @param name The name of the menu item.
     * @param newMainFrame The main frame for which the action can occurs.
     */
    protected JMinesAction(final String name, final MainFrame newMainFrame) {
        super(name);
        this.mainFrame = newMainFrame;

        statusText = name;
    }

    /**
     * Construct a new JMinesAction.
     *
     * @param name The name of the menu item.
     * @param icon The icon of the menu item.
     * @param newMainFrame The main panel for which the action can occurs.
     */
    protected JMinesAction(final String name, final Icon icon, final MainFrame newMainFrame) {
        super(name, icon);
        this.mainFrame = newMainFrame;

        statusText = name;
    }

    //=========================================================================
    // Getters
    //=========================================================================
    /**
     * Returns the main panel for which the action can occurs.
     *
     * @return The main panel for which the action can occurs.
     */
    public final MainFrame getMainFrame() {
        return mainFrame;
    }

    /**
     * Returns the text to display in the status bar.
     *
     * @return The text to display in the status bar.
     */
    public final String getStatusText() {
        return statusText;
    }

    //=========================================================================
    // Setters
    //=========================================================================
    /**
     * Set new JMines main frame.
     *
     * @param newMainFrame The new main frame to set.
     */
    public final void setMainFrame(final MainFrame newMainFrame) {
        this.mainFrame = newMainFrame;
    }

    /**
     * Set a new text to display in the status bar.
     *
     * @param newStatusText The new text to display in the status bar.
     */
    public final void setStatusText(final String newStatusText) {
        this.statusText = newStatusText;
    }

    //=========================================================================
    // Inherited methods
    //=========================================================================
    /**
     * The method launched when an action occurs.
     *
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public final void emptyStatusBar() {
        mainFrame.getMainPanel().getStatusBar().setText(" ");
    }

    //=========================================================================
    // Static methods
    //=========================================================================
    /**
     * Compute (and return) the luminance of a given color, the luminance is
     * used in the ChangeBackgroundColor action, Color action and
     * ChangeLCDColor action.<br/>
     * Luminance is computed using Y = 0,299 R + 0,587 G + 0,114 B formula.
     *
     * @param color The color we want the luminance.
     * @return The luminance of the given color.
     */
    public static int getLuminance(final Color color) {
        final float rr = .299f;
        final float gr = .587f;
        final float br = .114f;

        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();

        return Math.round(rr * r + gr * g + br * b);
    }

    //=========================================================================
    // Methods
    //=========================================================================
    /**
     * Return the JMines main panel.
     *
     * @return The JMines main panel.
     * @see jmines.view.components.MainFrame#getMainPanel().
     */
    public final MainPanel getMainPanel() {
        return mainFrame.getMainPanel();
    }

}
