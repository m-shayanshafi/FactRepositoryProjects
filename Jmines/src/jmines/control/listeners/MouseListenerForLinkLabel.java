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

import java.awt.Desktop;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import jmines.view.components.LinkLabel;
import jmines.view.persistence.Configuration;

/**
 * The mouse listener used for link labels.
 *
 * @author Zleurtor
 */
public class MouseListenerForLinkLabel implements MouseListener {

    //=========================================================================
    // Static attributes
    //=========================================================================

    //=========================================================================
    // Attributes
    //=========================================================================
    /**
     * The linked label.
     */
    private final JLabel linkLabel;
    /**
     * The original text of the linked label.
     */
    private final String text;
    /**
     * The address to which the label is linked.
     */
    private final URI address;

    //=========================================================================
    // Constructors
    //=========================================================================
    /**
     * Construct a new mouse listener for the given label.
     *
     * @param newLinkLabel The linked label.
     * @param newAddress The address this label is linked to.
     * @throws URISyntaxException If the given address is not well formed
     *                            Internet (or local) address.
     */
    public MouseListenerForLinkLabel(final JLabel newLinkLabel, final String newAddress) throws URISyntaxException {
        this.linkLabel = newLinkLabel;
        this.text = linkLabel.getText();
        this.address = new URI(newAddress);
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
     * The method used when the mouse is clicked over the listened label.
     *
     * @param evt The object relating the event that occurred.
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent).
     */
    @Override
    public final void mouseClicked(final MouseEvent evt) {
        try {
            Desktop.getDesktop().browse(address);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", Configuration.getInstance().getText(Configuration.KEY_TITLE_ERROR), JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * The method used when the mouse enter the listened label.
     *
     * @param evt The object relating the event that occurred.
     * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent).
     */
    @Override
    public final void mouseEntered(final MouseEvent evt) {
        linkLabel.setText("<html><body><u>" + text + "</u></body></html>");
    }

    /**
     * The method used when the mouse exit from the listened label.
     *
     * @param evt The object relating the event that occurred.
     * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent).
     */
    @Override
    public final void mouseExited(final MouseEvent evt) {
        linkLabel.setText(text);
    }

    /**
     * The method used when the mouse is pressed over the listened label.
     *
     * @param evt The object relating the event that occurred.
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent).
     */
    @Override
    public final void mousePressed(final MouseEvent evt) {
        linkLabel.setForeground(LinkLabel.ACTIVE_COLOR);
    }

    /**
     * The method used when the mouse is released over the listened label.
     *
     * @param evt The object relating the event that occurred.
     * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent).
     */
    @Override
    public final void mouseReleased(final MouseEvent evt) {
        linkLabel.setForeground(LinkLabel.VISITED_COLOR);
    }

    //=========================================================================
    // Static methods
    //=========================================================================

    //=========================================================================
    // Methods
    //=========================================================================
}
