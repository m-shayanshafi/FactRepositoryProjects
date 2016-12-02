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

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JOptionPane;

import jmines.view.dialogs.ConfigurationFrame;
import jmines.view.persistence.Configuration;

/**
 * The Windows listener used for the main frame.
 *
 * @author Zleurtor
 */
public class WindowListenerForConfigurationFrame implements WindowListener {

    //=========================================================================
    // Static attributes
    //=========================================================================

    //=========================================================================
    // Attributes
    //=========================================================================
    /**
     * The main frame to listen.
     */
    private final ConfigurationFrame configurationFrame;

    //=========================================================================
    // Constructors
    //=========================================================================
    /**
     * Contruct a new Window listener.
     *
     * @param newConfigurationDialog The configuration dialog to listen.
     */
    public WindowListenerForConfigurationFrame(final ConfigurationFrame newConfigurationDialog) {
        this.configurationFrame = newConfigurationDialog;
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
     * The called method when the window is activated.
     *
     * @param evt The event object relating the event that occurred.
     * @see java.awt.event.WindowListener#windowActivated(java.awt.event.WindowEvent).
     */
    @Override
    public final void windowActivated(final WindowEvent evt) {
    }

    /**
     * The called method when the window is closed.
     *
     * @param evt The event object relating the event that occurred.
     * @see java.awt.event.WindowListener#windowClosed(java.awt.event.WindowEvent).
     */
    @Override
    public final void windowClosed(final WindowEvent evt) {
    }

    /**
     * The called method when the window is closing.
     *
     * @param evt The event object relating the event that occurred.
     * @see java.awt.event.WindowAdapter#windowClosing(java.awt.event.WindowEvent)
     */
    @Override
    public final void windowClosing(final WindowEvent evt) {
        Configuration configuration = Configuration.getInstance();

        String message = configuration.getText(Configuration.KEY_TEXT_REALLYQUIT);
        String title = configuration.getText(Configuration.KEY_TITLE_QUIT);

        if (JOptionPane.showConfirmDialog(configurationFrame, message, title, JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    /**
     * The called method when the window is deactivated.
     *
     * @param evt The event object relating the event that occurred.
     * @see java.awt.event.WindowListener#windowDeactivated(java.awt.event.WindowEvent).
     */
    @Override
    public final void windowDeactivated(final WindowEvent evt) {
    }

    /**
     * The called method when the window is restored.
     *
     * @param evt The event object relating the event that occurred.
     * @see java.awt.event.WindowAdapter#windowDeiconified(java.awt.event.WindowEvent)
     */
    @Override
    public final void windowDeiconified(final WindowEvent evt) {
    }

    /**
     * The called method when the window is iconified.
     *
     * @param evt The event object relating the event that occurred.
     * @see java.awt.event.WindowAdapter#windowIconified(java.awt.event.WindowEvent)
     */
    @Override
    public final void windowIconified(final WindowEvent evt) {
    }

    /**
     * The called method when the window is opened.
     *
     * @param evt The event object relating the event that occurred.
     * @see java.awt.event.WindowListener#windowOpened(java.awt.event.WindowEvent).
     */
    @Override
    public final void windowOpened(final WindowEvent evt) {
    }

    //=========================================================================
    // Static methods
    //=========================================================================

    //=========================================================================
    // Methods
    //=========================================================================
}
