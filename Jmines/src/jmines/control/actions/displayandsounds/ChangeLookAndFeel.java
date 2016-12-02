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
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import jmines.control.actions.JMinesAction;
import jmines.view.components.MainFrame;
import jmines.view.persistence.Configuration;

/**
 * The class representing the action used when the user click a look and feel
 * name menu item.
 *
 * @author Zleurtor
 */
public class ChangeLookAndFeel extends JMinesAction {

    //==========================================================================
    // Static attributes
    //==========================================================================
    /**
     * The unique serial version identifier.
     */
    private static final long serialVersionUID = 1119162047862927994L;

    //==========================================================================
    // Attributes
    //==========================================================================
    /**
     * All the information about the look and feel to set.
     */
    private final LookAndFeel lookAndFeel;

    //==========================================================================
    // Constructors
    //==========================================================================
    /**
     * Construct a new ChangeLookAndFeel action.
     *
     * @param newLookAndFeel The look and feel to set.
     * @param mainFrame The main frame of the application.
     */
    public ChangeLookAndFeel(final LookAndFeel newLookAndFeel, final MainFrame mainFrame) {
        super(null, mainFrame);
        putValue(Action.NAME, getLookAndFeelInfo(newLookAndFeel).getName());

        this.lookAndFeel = newLookAndFeel;

        setStatusText(Configuration.getInstance().getConfigurableText(Configuration.KEY_STATUSTEXT_DISPAY_CHANGELOOKANDFEEL, new String[] {lookAndFeel.getName()}));
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
     * Retrieve all the information about a given look and feel.
     *
     * @param laf The look and feel for which we want the information.
     * @return The information about the given look and feel.
     */
    private LookAndFeelInfo getLookAndFeelInfo(final LookAndFeel laf) {
        for (LookAndFeelInfo lafi : UIManager.getInstalledLookAndFeels()) {
            if (lafi.getClassName().equals(laf.getClass().getName())) {
                return lafi;
            }
        }

        return null;
    }

    /**
     * The Method used when the user click on the menu item.
     *
     * @param evt The event object relating the event that occurred.
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public final void actionPerformed(final ActionEvent evt) {
        setMainFrame(MainFrame.changeLookAndFeel(lookAndFeel, getMainFrame()));
        Configuration.getInstance().putRealTimeconfiguration(Configuration.KEY_USER_LOOKANDFEEL, lookAndFeel.getClass().getName());

        super.emptyStatusBar();
    }

    //==========================================================================
    // Static methods
    //==========================================================================

    //==========================================================================
    // Methods
    //==========================================================================
}
