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
package jmines.view.components;

import java.awt.Color;
import java.awt.Cursor;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import jmines.control.listeners.MouseListenerForLinkLabel;
import jmines.view.persistence.Configuration;

/**
 * A clickable label link to an internet address.
 *
 * @author Zleurtor
 */
public class LinkLabel extends JPanel {

    //=========================================================================
    // Static attributes
    //=========================================================================
    /**
     * The unique serial version identifier.
     */
    private static final long serialVersionUID = -3898119420438616745L;
    /**
     * The foreground of the label.
     */
    public static final Color COLOR = new Color(Configuration.getInstance().getInt(Configuration.KEY_LINK_COLOR, 16));
    /**
     * The foreground of the label while the user press the mouse button over
     * it.
     */
    public static final Color ACTIVE_COLOR = new Color(Configuration.getInstance().getInt(Configuration.KEY_LINK_ACTIVECOLOR, 16));
    /**
     * The foreground of the label after the user has visited the link.
     */
    public static final Color VISITED_COLOR = new Color(Configuration.getInstance().getInt(Configuration.KEY_LINK_VISITEDCOLOR, 16));

    //=========================================================================
    // Attributes
    //=========================================================================

    //=========================================================================
    // Constructors
    //=========================================================================
    /**
     * Construct a new link label.
     *
     * @param line The text to display in the label.
     */
    public LinkLabel(final String line) {
        super();
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

        final String linkMarkerStart = "@link{";
        final String linkMarkerEnd = "}";

        List<JLabel> labels = new ArrayList<JLabel>();

        String tmpLine = line;
        while (tmpLine.contains(linkMarkerStart) && tmpLine.contains(linkMarkerEnd)) {
            if (tmpLine.startsWith(linkMarkerStart)) {
                String subLine = tmpLine.substring(linkMarkerStart.length(), tmpLine.indexOf(linkMarkerEnd, linkMarkerStart.length()));
                tmpLine = tmpLine.substring(linkMarkerStart.length() + subLine.length() + linkMarkerEnd.length());

                JLabel label = new JLabel(subLine);
                label.setForeground(COLOR);
                label.setCursor(new Cursor(Cursor.HAND_CURSOR));
                try {
                    label.addMouseListener(new MouseListenerForLinkLabel(label, subLine));
                } catch (URISyntaxException e) {
                    JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", Configuration.getInstance().getText(Configuration.KEY_TITLE_ERROR), JOptionPane.ERROR_MESSAGE);
                }

                labels.add(label);
            } else {
                String subLine = tmpLine.substring(0, tmpLine.indexOf(linkMarkerStart));
                tmpLine = tmpLine.substring(subLine.length());

                labels.add(new JLabel(subLine));
            }
        }
        labels.add(new JLabel(tmpLine));

        for (JLabel label : labels) {
            add(label);
        }
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

    //=========================================================================
    // Static methods
    //=========================================================================

    //=========================================================================
    // Methods
    //=========================================================================
}
