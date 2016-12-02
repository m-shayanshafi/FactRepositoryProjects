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
import javax.swing.JSeparator;

import jmines.control.listeners.ActionListenerForDialogs;
import jmines.view.components.LinkLabel;
import jmines.view.components.MainPanel;
import jmines.view.persistence.Configuration;

/**
 * The dialog used to display information about JMines.
 *
 * @author Zleurtor
 */
public final class AboutDialog extends JDialog {

    //==========================================================================
    // Static attributes
    //==========================================================================
    /**
     * The unique serial version identifier.
     */
    private static final long serialVersionUID = -6115476044801712627L;
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
     * The panel used to display information.
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
    public AboutDialog(final MainPanel newMainPanel) {
        super((JFrame) null, Configuration.getInstance().getText(Configuration.KEY_TITLE_ABOUT), true);

        final int separatorInsets = 20;

        this.mainPanel = newMainPanel;

        Configuration configuration = Configuration.getInstance();

        // Create the text panel
        List<String> textLines = new ArrayList<String>();
        List<String> gplLines = new ArrayList<String>();
        List<String> jvmNameLines = new ArrayList<String>();
        String separator = "<br/>";

        String text = configuration.getConfigurableText(Configuration.KEY_TEXT_ABOUT, new String[] {configuration.getString(Configuration.KEY_VERSION)});
        while (text.contains(separator)) {
            textLines.add(text.substring(0, text.indexOf(separator)));
            text = text.substring(text.indexOf(separator) + separator.length());
        }
        textLines.add(text);

        String gpl = configuration.getText(Configuration.KEY_TEXT_GPL);
        while (gpl.contains(separator)) {
            gplLines.add(gpl.substring(0, gpl.indexOf(separator)));
            gpl = gpl.substring(gpl.indexOf(separator) + separator.length());
        }
        gplLines.add(gpl);

        String jvmName = configuration.getConfigurableText(Configuration.KEY_TEXT_JVMNAME, new String[] {System.getProperty("java.vm.name")});
        while (jvmName.contains(separator)) {
            jvmNameLines.add(jvmName.substring(0, jvmName.indexOf(separator)));
            jvmName = jvmName.substring(jvmName.indexOf(separator) + separator.length());
        }
        jvmNameLines.add(jvmName);

        textPanel = new JPanel(new GridBagLayout());
        for (int i = 0; i < textLines.size(); i++) {
            String line = textLines.get(i);
            textPanel.add(new LinkLabel(line),
                    new GridBagConstraints(0, i, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        }
        textPanel.add(new JSeparator(JSeparator.HORIZONTAL), new GridBagConstraints(0, textLines.size(), 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(separatorInsets, 0, separatorInsets, 0), 0, 0));
        for (int i = 0; i < gplLines.size(); i++) {
            String line = gplLines.get(i);
            textPanel.add(new LinkLabel(line),
                    new GridBagConstraints(0, textLines.size() + 1 + i, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        }
        textPanel.add(new JSeparator(JSeparator.HORIZONTAL), new GridBagConstraints(0, textLines.size() + 1 + gplLines.size(), 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(separatorInsets, 0, separatorInsets, 0), 0, 0));
        for (int i = 0; i < jvmNameLines.size(); i++) {
            String line = jvmNameLines.get(i);
            textPanel.add(new LinkLabel(line),
                    new GridBagConstraints(0, textLines.size() + 1 + gplLines.size() + 1 + i, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
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

