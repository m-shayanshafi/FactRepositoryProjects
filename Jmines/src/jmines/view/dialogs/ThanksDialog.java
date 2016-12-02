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
package jmines.view.dialogs;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import jmines.control.listeners.ActionListenerForDialogs;
import jmines.view.components.LinkLabel;
import jmines.view.components.MainPanel;
import jmines.view.persistence.Configuration;

/**
 * The dialog used to display thanks for JMines.
 *
 * @author Zleurtor
 */
public final class ThanksDialog extends JDialog {

    //==========================================================================
    // Static attributes
    //==========================================================================
    /**
     * The unique serial version identifier.
     */
    private static final long serialVersionUID = -7988504894354664681L;
    /**
     * The horizontal margin at the left and right of this dialog.
     */
    private static final int HORIZONTAL_MARGIN = 10;
    /**
     * The vertical margin at the top and bottom of this dialog.
     */
    private static final int VERTICAL_MARGIN = 15;
    /**
     * The space between the text and the button.
     */
    private static final int VERTICAL_SPACE = 12;

    //==========================================================================
    // Attributes
    //==========================================================================
    /**
     * The JMines main panel for which this dialog is displayed.
     */
    private final MainPanel mainPanel;
    /**
     * The panel used to display thanks.
     */
    private final JPanel textPanel;
    /**
     * The button used to close this dialog.
     */
    private final JButton okButton;

    //==========================================================================
    // Constructors
    //==========================================================================
    /**
     * Construct a new dialog using the given JMines main panel.
     *
     * @param newMainPanel The JMines main panel for which this dialog is
     *                     displayed.
     */
    public ThanksDialog(final MainPanel newMainPanel) {
        super((JFrame) null, Configuration.getInstance().getText(Configuration.KEY_TITLE_THANKS), true);

        this.mainPanel = newMainPanel;

        Configuration configuration = Configuration.getInstance();

        // Create the text panel
        List<String> textLines = new ArrayList<String>();
        String separator = "<br/>";

        String text = configuration.getText(Configuration.KEY_TEXT_THANKS);
        while (text.contains(separator)) {
            String line = text.substring(0, text.indexOf(separator));
            textLines.add(line);
            text = text.substring(line.length() + separator.length());
        }
        textLines.add(text);

        textPanel = new JPanel(new GridBagLayout());
        for (int i = 0; i < textLines.size(); i++) {
            String line = textLines.get(i);

            LinkLabel linkLabel = new LinkLabel(line);

            textPanel.add(linkLabel,
                    new GridBagConstraints(0, i, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        }

        // Create the buttons
        okButton = new JButton(configuration.getText(Configuration.KEY_TEXT_OK));

        ActionListenerForDialogs okListener = new ActionListenerForDialogs(this);
        okButton.addActionListener(okListener);

        // Add the components
        JPanel panel = new JPanel(new GridBagLayout());
        panel.add(textPanel,
                new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(VERTICAL_MARGIN, HORIZONTAL_MARGIN, VERTICAL_SPACE / 2, HORIZONTAL_MARGIN), 0, 0));
        panel.add(okButton,
                new GridBagConstraints(0, 2, 1, 2, 0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(VERTICAL_SPACE / 2, HORIZONTAL_MARGIN, VERTICAL_MARGIN, HORIZONTAL_MARGIN), 0, 0));

        add(panel, BorderLayout.CENTER);
        pack();
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
     * Shows or hides this component depending on the value of the parameter.
     * Moreover this dialog is centered relatively to its main panel.
     *
     * @param b if <code>true</code>, shows this component, otherwise, hides
     *          this component
     * @see java.awt.Component#setVisible(boolean)
     */
    @Override
    public void setVisible(final boolean b) {
        setLocationRelativeTo(mainPanel);
        super.setVisible(b);
    }

    //==========================================================================
    // Static methods
    //==========================================================================

    //==========================================================================
    // Methods
    //==========================================================================
}

