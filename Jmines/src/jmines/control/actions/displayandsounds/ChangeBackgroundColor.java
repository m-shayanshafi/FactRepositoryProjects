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
package jmines.control.actions.displayandsounds;

import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.JColorChooser;

import jmines.view.components.MainFrame;
import jmines.view.persistence.Configuration;

/**
 * The super class of change background color action used in JMines.
 *
 * @author Zleurtor
 */
public class ChangeBackgroundColor extends ChangeColor {

    //==========================================================================
    // Static attributes
    //==========================================================================
    /**
     * The unique serial version identifier.
     */
    private static final long serialVersionUID = 2331116861580436027L;

    //==========================================================================
    // Attributes
    //==========================================================================

    //==========================================================================
    // Constructors
    //==========================================================================
    /**
     * Construct a new ChangeBackgroundColor action.
     *
     * @param colorId The identifier for the color to set.
     * @param mainFrame The main frame of the application.
     */
    public ChangeBackgroundColor(final byte colorId, final MainFrame mainFrame) {
        super(colorId, mainFrame);

        setStatusText(Configuration.getInstance().getConfigurableText(Configuration.KEY_STATUSTEXT_DISPAY_CHANGEBGCOLOR, new String[] {getValue(Action.NAME).toString()}));
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
     * The Method used when the user click on the menu item.
     *
     * @param evt The event object relating the event that occurred.
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public final void actionPerformed(final ActionEvent evt) {
        if (getColorId() != LOOK_AND_FEEL_DEPENDENT && getColorId() != CUSTOM) {
            getMainPanel().setBackground(getColor());

            Configuration.getInstance().putRealTimeconfiguration(Configuration.KEY_USER_BGCOLOR, getValue("id").toString());
        } else if (getColorId() == LOOK_AND_FEEL_DEPENDENT) {
            getMainPanel().setBackground(getMainPanel().getLookAndFeelDependentBackground());

            Configuration.getInstance().putRealTimeconfiguration(Configuration.KEY_USER_BGCOLOR, getValue("id").toString());
        } else if (getColorId() == CUSTOM) {
            java.awt.Color color = JColorChooser.showDialog(getMainPanel(), Configuration.getInstance().getText(Configuration.KEY_TITLE_CHOOSECOLOR), getColor());
            if (color != null) {
                setColor(color);
                getMainPanel().setBackground(color);

                Configuration.getInstance().putRealTimeconfiguration(Configuration.KEY_USER_BGCOLOR, toHexadecimal(color));
            }
        }

        super.emptyStatusBar();
    }

    //==========================================================================
    // Static methods
    //==========================================================================

    //==========================================================================
    // Methods
    //==========================================================================
}
