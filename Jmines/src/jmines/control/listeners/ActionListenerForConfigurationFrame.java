/*
 * This file is part of JMines.
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
 * along with JMines. If not, see <http://www.gnu.org/licenses/>.
 */
package jmines.control.listeners;

import java.awt.event.ActionEvent;

import jmines.view.dialogs.ConfigurationFrame;
import jmines.view.persistence.Configuration;

/**
 * The listener to use to listen the actions done on a button.
 *
 * @author Zleurtor
 */
public class ActionListenerForConfigurationFrame implements java.awt.event.ActionListener {

    //=========================================================================
    // Static attributes
    //=========================================================================

    //=========================================================================
    // Attributes
    //=========================================================================
    /**
     * The configuration frame on which a user event can occurs.
     */
    private final ConfigurationFrame configurationFrame;

    //=========================================================================
    // Constructors
    //=========================================================================
    /**
     * Construct a new listener for the given configuration frame.
     *
     * @param newConfigurationFrame The configuration frame on which a user
     *                              event can occurs.
     */
    public ActionListenerForConfigurationFrame(final ConfigurationFrame newConfigurationFrame) {
        this.configurationFrame = newConfigurationFrame;
    }

    //=========================================================================
    // Getters
    //=========================================================================

    //=========================================================================
    // Setters
    //=========================================================================

    //=========================================================================
    // Inherited methods
    //=========================================================================
    /**
     * The method called when the button is clicked.
     *
     * @param evt The event Object representing the user action.
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent).
     */
    @Override
    public final void actionPerformed(final ActionEvent evt) {
        if (evt.getSource() == configurationFrame.getOkButton()) {
            new Thread() {

                /**
                 * The method used to save all the configuration values.
                 *
                 * @see java.lang.Thread#run().
                 */
                @Override
                public void run() {
                    configurationFrame.getOkButton().setEnabled(false);

                    Configuration configuration = Configuration.getInstance();

                    for (String key : configurationFrame.getFields().keySet()) {
                        String value = configurationFrame.getFields().get(key).getText();
                        if (value != null && (!value.equals("") || configurationFrame.getFields().get(key).isEditable())) {
                            configuration.putRealTimeconfiguration(key, value);
                        }
                    }

                    configurationFrame.getOkButton().setEnabled(true);
                }

            } .start();
        } else if (evt.getSource() == configurationFrame.getDefaultButton()) {
            for (String key : configurationFrame.getFields().keySet()) {
                configurationFrame.getFields().get(key).setText(configurationFrame.getDefaultFields().get(key).getText());
            }
        }
    }

    //=========================================================================
    // Static methods
    //=========================================================================

    //=========================================================================
    // Methods
    //=========================================================================
}
