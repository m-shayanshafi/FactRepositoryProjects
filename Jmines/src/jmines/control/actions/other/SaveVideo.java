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
package jmines.control.actions.other;

import java.awt.event.ActionEvent;

import jmines.control.actions.JMinesAction;
import jmines.view.components.MainFrame;
import jmines.view.persistence.Configuration;

/**
 * The class representing the action used when the user click on the save video
 * menu item.
 *
 * @author Zleurtor
 */
public class SaveVideo extends JMinesAction {

    //==========================================================================
    // Static attributes
    //==========================================================================
    /**
     * The unique serial version identifier.
     */
    private static final long serialVersionUID = 5073524774158637185L;

    //==========================================================================
    // Attributes
    //==========================================================================
    /**
     * Tell whether or not the check box menu item corresponding to this action
     * is checked.
     */
    private boolean checked;

    //==========================================================================
    // Constructors
    //==========================================================================
    /**
     * Construct a new SaveVideo action.
     *
     * @param name The name of the menu item.
     * @param mainFrame The main frame of the application.
     */
    public SaveVideo(final String name, final MainFrame mainFrame) {
        super(name, mainFrame);

        setStatusText(Configuration.getInstance().getText(Configuration.KEY_STATUSTEXT_OTHER_SAVEVIDEO));
    }

    //==========================================================================
    // Getters
    //==========================================================================
    /**
     * Tell whether or not the check box menu item corresponding to this action
     * is checked.
     *
     * @return true if the check box menu item corresponding to this action
     *         is checked, false otherwise.
     */
    public final boolean isChecked() {
        return checked;
    }

    //==========================================================================
    // Setters
    //==========================================================================
    /**
     * Allows to set a new value to the checked attribute.
     *
     * @param newChecked The new value for the checked attribute.
     */
    public final void setChecked(final boolean newChecked) {
        this.checked = newChecked;
    }

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
        setChecked(!isChecked());

        if (isChecked()) {
            getMainPanel().setSaveVideoEnabled(true);
        } else {
            getMainPanel().setSaveVideoEnabled(false);
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
