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
package jmines.control.actions.language;

import java.awt.event.ActionEvent;
import java.util.Locale;

import jmines.control.actions.JMinesAction;
import jmines.view.components.MainFrame;
import jmines.view.components.MenuBar;
import jmines.view.persistence.Configuration;

/**
 * The class representing the action used when the user click on a language
 * menu item.
 *
 * @author Zleurtor
 */
public class Language extends JMinesAction {

    //==========================================================================
    // Static attributes
    //==========================================================================
    /**
     * The unique serial version identifier.
     */
    private static final long serialVersionUID = 5073524774158637185L;
    /**
     * The locale currently used.
     */
    private static Locale currentLocale = null;

    //==========================================================================
    // Attributes
    //==========================================================================
    /**
     * The locale to set when the user click on this item.
     */
    private final Locale locale;

    //==========================================================================
    // Constructors
    //==========================================================================
    /**
     * Construct a new Language action.
     *
     * @param newLocale The locale to set.
     * @param mainFrame The main frame for which the action can occurs.
     */
    public Language(final Locale newLocale, final MainFrame mainFrame) {
        super(newLocale.getDisplayName(newLocale), mainFrame);

        locale = newLocale;

        setStatusText(Configuration.getInstance().getConfigurableText(Configuration.KEY_STATUSTEXT_LANGUAGE, new String[] {locale.getDisplayLanguage(currentLocale)}));
    }

    //==========================================================================
    // Getters
    //==========================================================================

    //==========================================================================
    // Setters
    //==========================================================================
    /**
     * The method used to store the currently used Locale.
     *
     * @param newCurrentLocale The currently used Locale.
     */
    public static void setCurrentLocale(final Locale newCurrentLocale) {
        Language.currentLocale = newCurrentLocale;
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
        getMainFrame().getJMenuBar().setVisible(false);

        getMainFrame().getJMenuBar().removeAll();
        Configuration.getInstance().loadLanguage(locale);
        Configuration.getInstance().putRealTimeconfiguration(Configuration.KEY_USER_LOCALE, locale.toString());
        ((MenuBar) getMainFrame().getJMenuBar()).init(getMainFrame());

        getMainFrame().getJMenuBar().setVisible(true);
        getMainFrame().getJMenuBar().repaint();

        getMainFrame().pack();

        super.emptyStatusBar();
    }

    //==========================================================================
    // Static methods
    //==========================================================================

    //==========================================================================
    // Methods
    //==========================================================================
}
