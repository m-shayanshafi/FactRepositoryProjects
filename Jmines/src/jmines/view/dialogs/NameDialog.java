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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.plaf.basic.BasicBorders;

import jmines.control.listeners.ActionListenerForDialogs;
import jmines.view.components.MainPanel;
import jmines.view.persistence.Configuration;

/**
 * The dialog used to ask the user when name when he win a game.
 *
 * @author Zleurtor
 */
public final class NameDialog extends JDialog {

    //==========================================================================
    // Static attributes
    //==========================================================================
    /**
     * The unique serial version identifier.
     */
    private static final long serialVersionUID = -6115476044801712627L;
    /**
     * The margin at the left and right of the dialog.
     */
    private static final int HORIZONTAL_MARGIN = 19;
    /**
     * The margin at the top and bottom of the dialog.
     */
    private static final int VERTICAL_MARGIN = 23;
    /**
     * The space between the text panel and field and between field and button.
     */
    private static final int VERTICAL_SPACE = 12;

    //==========================================================================
    // Attributes
    //==========================================================================
    /**
     * The JMines main panel.
     */
    private final MainPanel mainPanel;
    /**
     * The panel in which the text is written.
     */
    private final JPanel textPanel;
    /**
     * The text field in which the user has to enter his name.
     */
    private final JTextField nameTextField = new JTextField(9);
    /**
     * The button used to validate the entered name.
     */
    private final JButton okButton;
    /**
     * The entered name.
     */
    private String value;

    //==========================================================================
    // Constructors
    //==========================================================================
    /**
     * Construct a new name dialog using the given JMines main panel.
     *
     * @param newMainPanel The JMines main panel.
     */
    public NameDialog(final MainPanel newMainPanel) {
        super((JFrame) null, "", true);

        this.mainPanel = newMainPanel;

        Configuration configuration = Configuration.getInstance();

        // Create the text panel
        String difficulty = configuration.getText(Configuration.PREFIX_TEXT + Configuration.DOT + mainPanel.getGamePanel().getDifficulty());
        String text = configuration.getConfigurableText(Configuration.KEY_TEXT_BESTTIME, new String[] {difficulty});
        List<String> lines = new ArrayList<String>();
        String separator = "<br/>";
        while (text.contains(separator)) {
            lines.add(text.substring(0, text.indexOf(separator)));
            text = text.substring(text.indexOf(separator) + separator.length());
        }
        lines.add(text);
        textPanel = new JPanel(new GridBagLayout());
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            textPanel.add(new JLabel(line),
                    new GridBagConstraints(0, i, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        }


        // Create the buttons
        okButton = new JButton(configuration.getText(Configuration.KEY_TEXT_OK));

        // Add listeners to text fields and buttons
        ActionListenerForDialogs listener = new ActionListenerForDialogs(this);

        nameTextField.addActionListener(listener);
        okButton.addActionListener(listener);

        // Add the components
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BasicBorders.getInternalFrameBorder());
        panel.add(textPanel,
                new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(VERTICAL_MARGIN, HORIZONTAL_MARGIN, VERTICAL_SPACE / 2, HORIZONTAL_MARGIN), 0, 0));
        panel.add(nameTextField,
                new GridBagConstraints(0, 1, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(VERTICAL_SPACE / 2, HORIZONTAL_MARGIN, VERTICAL_SPACE / 2, HORIZONTAL_MARGIN), 0, 0));
        panel.add(okButton,
                new GridBagConstraints(0, 2, 1, 2, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(VERTICAL_SPACE / 2, HORIZONTAL_MARGIN, VERTICAL_MARGIN, HORIZONTAL_MARGIN), 0, 0));

        add(panel, BorderLayout.CENTER);
        setUndecorated(true);
        pack();
    }

    //==========================================================================
    // Getters
    //==========================================================================
    /**
     * Returns the text field in which the user has to enter his name.
     *
     * @return The text field in which the user has to enter his name.
     */
    public JTextField getNameTextField() {
        return nameTextField;
    }

    /**
     * Returns the button used to validate the entered name.
     *
     * @return The button used to validate the entered name.
     */
    public JButton getOkButton() {
        return okButton;
    }

    //==========================================================================
    // Setters
    //==========================================================================
    /**
     * Allows to set new value to The entered name.
     *
     * @param newValue The new entered name to set.
     */
    public void setValue(final String newValue) {
        this.value = newValue;
    }

    //==========================================================================
    // Inherited methods
    //==========================================================================
    /**
     * Shows or hides this component depending on the value of parameter b.
     *
     * @param b if true, shows this component; otherwise, hides this
     *          component.
     * @see java.awt.Component#setVisible(boolean)
     */
    @Override
    public void setVisible(final boolean b) {
        setLocationRelativeTo(mainPanel);
        super.setVisible(b);
    }

    /**
     * Shows this dialog, wait the user press enter or click the ok button then
     * return the entered name.
     *
     * @param name The default displayed name.
     * @return The user's entered name.
     */
    public String getValue(final String name) {
        value = null;
        nameTextField.setText(name);

        setVisible(true);

        return value;
    }

    //==========================================================================
    // Static methods
    //==========================================================================

    //==========================================================================
    // Methods
    //==========================================================================
}

