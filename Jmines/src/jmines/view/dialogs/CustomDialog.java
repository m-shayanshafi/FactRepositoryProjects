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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import jmines.control.listeners.ActionListenerForDialogs;
import jmines.view.components.MainPanel;
import jmines.view.persistence.Configuration;

/**
 * This class is used to display dialog for the users who want to play a game
 * with personalized number of lines, columns, and mines.
 *
 * @author Zleurtor
 */
public final class CustomDialog extends JDialog {

    /**
     * An object representation of the dialog return.
     *
     * @author Zleurtor
     */
    public static final class Values {

        //======================================================================
        // Attributes
        //======================================================================
        /**
         * The number of columns of the custom grid.
         */
        private int width;
        /**
         * The number of lines of the custom grid.
         */
        private int height;
        /**
         * The number of mines of the custom grid.
         */
        private int mines;

        //======================================================================
        // Getters
        //======================================================================
        /**
         * Returns the number of columns of the custom grid.
         *
         * @return The number of columns of the custom grid.
         */
        public int getWidth() {
            return width;
        }

        /**
         * Returns the number of lines of the custom grid.
         *
         * @return The number of line of the custom grid.
         */
        public int getHeight() {
            return height;
        }

        /**
         * Returns the number of mines of the custom grid.
         *
         * @return The number of mines of the custom grid.
         */
        public int getMines() {
            return mines;
        }

        //======================================================================
        // Setters
        //======================================================================
        /**
         * Allows to set a new value to the number of columns of the custom
         * grid.
         *
         * @param newWidth The new value for the number of columns of the
         *                 custom grid.
         */
        public void setWidth(final int newWidth) {
            this.width = newWidth;
        }

        /**
         * Allows to set a new value to the number of lines of the custom grid.
         *
         * @param newHeight The new value for the number of lines of the custom
         *                  grid.
         */
        public void setHeight(final int newHeight) {
            this.height = newHeight;
        }

        /**
         * Allows to set a new value to the number of mines of the custom grid.
         *
         * @param newMines The new value for the number of mines of the custom
         *                 grid.
         */
        public void setMines(final int newMines) {
            this.mines = newMines;
        }
    }

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
    private static final int HORIZONTAL_MARGIN = 16;
    /**
     * The margin at the top and bottom of the dialog.
     */
    private static final int VERTICAL_MARGIN = 34;
    /**
     * The space between the fields and the buttons.
     */
    private static final int HORIZONTAL_SPACE = 11;
    /**
     * The space between the fields.
     */
    private static final int VERTICAL_SPACE = 4;

    //==========================================================================
    // Attributes
    //==========================================================================
    /**
     * The JMines main panel.
     */
    private final MainPanel mainPanel;
    /**
     * The label for the number of lines of the custom grid.
     */
    private final JLabel heightLabel;
    /**
     * The label for the number of columns of the custom grid.
     */
    private final JLabel widthLabel;
    /**
     * The label for the number of mines of the custom grid.
     */
    private final JLabel minesLabel;
    /**
     * The field for the number of lines of the custom grid.
     */
    private final JTextField heightTextField = new JTextField(5);
    /**
     * The field for the number of columns of the custom grid.
     */
    private final JTextField widthTextField = new JTextField(5);
    /**
     * The field for the number of mines of the custom grid.
     */
    private final JTextField minesTextField = new JTextField(5);
    /**
     * The ok button.
     */
    private final JButton okButton;
    /**
     * The cancel button.
     */
    private final JButton cancelButton;
    /**
     * The values returned by the dialog when the user click ok.
     */
    private Values values;

    //==========================================================================
    // Constructors
    //==========================================================================
    /**
     * Construct a new custom dialog using the given JMines main panel.
     *
     * @param newMainPanel The JMines main panel.
     */
    public CustomDialog(final MainPanel newMainPanel) {
        super((JFrame) null, Configuration.getInstance().getText(Configuration.KEY_TITLE_CUSTOMFIELDS), true);

        this.mainPanel = newMainPanel;

        Configuration configuration = Configuration.getInstance();

        // Create the labels and buttons
        heightLabel = new JLabel(configuration.getText(Configuration.KEY_TEXT_HEIGHT));
        widthLabel = new JLabel(configuration.getText(Configuration.KEY_TEXT_WIDTH));
        minesLabel = new JLabel(configuration.getText(Configuration.KEY_TEXT_MINES));
        okButton = new JButton(configuration.getText(Configuration.KEY_TEXT_OK));
        cancelButton = new JButton(configuration.getText(Configuration.KEY_TEXT_CANCEL));

        // Add listeners to text fields and buttons
        ActionListenerForDialogs listener = new ActionListenerForDialogs(this);

        heightTextField.addActionListener(listener);
        widthTextField.addActionListener(listener);
        minesTextField.addActionListener(listener);
        cancelButton.addActionListener(listener);
        okButton.addActionListener(listener);

        // Add the components
        setLayout(new GridBagLayout());
        add(heightLabel,
                new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(VERTICAL_MARGIN, HORIZONTAL_MARGIN, VERTICAL_SPACE / 2, HORIZONTAL_SPACE / 2), 0, 0));
        add(widthLabel,
                new GridBagConstraints(0, 1, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(VERTICAL_SPACE / 2, HORIZONTAL_MARGIN, VERTICAL_SPACE / 2, HORIZONTAL_SPACE / 2), 0, 0));
        add(minesLabel,
                new GridBagConstraints(0, 2, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(VERTICAL_SPACE / 2, HORIZONTAL_MARGIN, VERTICAL_MARGIN, HORIZONTAL_SPACE / 2), 0, 0));
        add(heightTextField,
                new GridBagConstraints(1, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(VERTICAL_MARGIN, HORIZONTAL_SPACE / 2, VERTICAL_SPACE / 2, HORIZONTAL_SPACE / 2), 0, 0));
        add(widthTextField,
                new GridBagConstraints(1, 1, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(VERTICAL_SPACE / 2, HORIZONTAL_SPACE / 2, VERTICAL_SPACE / 2, HORIZONTAL_SPACE / 2), 0, 0));
        add(minesTextField,
                new GridBagConstraints(1, 2, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(VERTICAL_SPACE / 2, HORIZONTAL_SPACE / 2, VERTICAL_MARGIN, HORIZONTAL_SPACE / 2), 0, 0));
        add(okButton,
                new GridBagConstraints(2, 0, 1, 2, 0, 0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(VERTICAL_MARGIN, HORIZONTAL_SPACE / 2, 0, HORIZONTAL_MARGIN), 0, 0));
        add(cancelButton,
                new GridBagConstraints(2, 1, 1, 2, 0, 0, GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL, new Insets(0, HORIZONTAL_SPACE / 2, VERTICAL_MARGIN, HORIZONTAL_MARGIN), 0, 0));

        pack();
        setResizable(false);
    }

    //==========================================================================
    // Getters
    //==========================================================================
    /**
     * Returns the JMines main panel.
     *
     * @return The JMines main panel.
     */
    public MainPanel getMainPanel() {
        return mainPanel;
    }

    /**
     * Returns the field for the number of lines of the custom grid.
     *
     * @return The field for the number of lines of the custom grid.
     */
    public JTextField getHeightTextField() {
        return heightTextField;
    }

    /**
     * Returns the field for the number of columns of the custom grid.
     *
     * @return The field for the number of columns of the custom grid.
     */
    public JTextField getWidthTextField() {
        return widthTextField;
    }

    /**
     * Returns the field for the number of mines of the custom grid.
     *
     * @return The field for the number of mines of the custom grid.
     */
    public JTextField getMinesTextField() {
        return minesTextField;
    }

    /**
     * Returns the ok button.
     *
     * @return The ok button.
     */
    public JButton getOkButton() {
        return okButton;
    }

    /**
     * Returns the cancel button.
     *
     * @return The cancel button.
     */
    public JButton getCancelButton() {
        return cancelButton;
    }


    //==========================================================================
    // Setters
    //==========================================================================
    /**
     * Set a new value to the values returned by the dialog when the user click
     * ok.
     *
     * @param newValues The new values to set.
     */
    public void setValues(final Values newValues) {
        this.values = newValues;
    }

    //==========================================================================
    // Inherited methods
    //==========================================================================
    /**
     * Shows or hides this component depending on the value of parameter b.
     *
     * @param b If true, shows this component; otherwise, hides this component.
     * @see java.awt.Component#setVisible(boolean)
     */
    @Override
    public void setVisible(final boolean b) {
        if (b) {
            widthTextField.setText(Integer.toString(mainPanel.getGamePanel().getGameBoard().getWidth()));
            heightTextField.setText(Integer.toString(mainPanel.getGamePanel().getGameBoard().getHeight()));
            minesTextField.setText(Integer.toString(mainPanel.getGamePanel().getGameBoard().getNumberOfMines()));
        }

        setLocationRelativeTo(mainPanel);
        super.setVisible(b);
    }

    /**
     * Show the dialog, wait the user click and then return a Values object
     * representing the information entered by the user.
     *
     * @return A Values object representing the information entered by the
     *         user.
     */
    public Values getValues() {
        values = null;

        setVisible(true);

        return values;
    }

    //==========================================================================
    // Static methods
    //==========================================================================

    //==========================================================================
    // Methods
    //==========================================================================
}

