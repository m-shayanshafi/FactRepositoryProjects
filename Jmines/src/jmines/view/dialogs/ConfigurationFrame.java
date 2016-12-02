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
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import jmines.control.listeners.ActionListenerForConfigurationFrame;
import jmines.control.listeners.WindowListenerForConfigurationFrame;
import jmines.view.persistence.Configuration;

/**
 * The frame used to display information about JMines.
 *
 * @author Zleurtor
 */
public final class ConfigurationFrame extends JFrame {

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
     * The maximum height allowed for this dialog.
     */
    private static final int MAXIMUM_HEIGHT = 500;

    //==========================================================================
    // Attributes
    //==========================================================================
    /**
     * The button used save the configuration.
     */
    private final JButton okButton;
    /**
     * The button used to reset the configuration.
     */
    private final JButton defaultButton;
    /**
     * A mapping between attributes key and the text fields containing their
     * values.
     */
    private final Map<String, JTextField> fields = new HashMap<String, JTextField>();
    /**
     * A mapping between attributes key and the text fields containing their
     * default values.
     */
    private final Map<String, JTextField> defaultFields = new HashMap<String, JTextField>();

    //==========================================================================
    // Constructors
    //==========================================================================
    /**
     * Construct a new dialog using the given JMines main panel.
     *
     * @param toSave The list of keys of the attributes to save.
     * @param nonEditable The list of keys of the attributes that are not
     *                    editable by the user.
     */
    public ConfigurationFrame(final String[] toSave, final String[] nonEditable) {
        super(Configuration.getInstance().getText(Configuration.KEY_TITLE_CONFIGURATION));

        Configuration configuration = Configuration.getInstance();

        // Create the buttons
        okButton = new JButton(configuration.getText(Configuration.KEY_TEXT_OK));
        defaultButton = new JButton(configuration.getText(Configuration.KEY_TEXT_DEFAULT));

        okButton.setFocusable(false);
        defaultButton.setFocusable(false);
        ActionListener listener = new ActionListenerForConfigurationFrame(this);
        okButton.addActionListener(listener);
        defaultButton.addActionListener(listener);

        // Add the components
        final int columns = 3;

        JPanel tablePanel = new JPanel(new GridLayout(toSave.length + 1, columns));
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.LINE_AXIS));

        JLabel keyHeader = new JLabel(configuration.getText(Configuration.KEY_TEXT_VARIABLE), JLabel.CENTER);
        JLabel valueHeader = new JLabel(configuration.getText(Configuration.KEY_TEXT_VALUE), JLabel.CENTER);
        JLabel defaultHeader = new JLabel(configuration.getText(Configuration.KEY_TEXT_DEFAULT_VALUE), JLabel.CENTER);

        keyHeader.setBackground(Color.BLACK);
        valueHeader.setBackground(Color.BLACK);
        defaultHeader.setBackground(Color.BLACK);

        keyHeader.setForeground(Color.WHITE);
        valueHeader.setForeground(Color.WHITE);
        defaultHeader.setForeground(Color.WHITE);

        keyHeader.setOpaque(true);
        valueHeader.setOpaque(true);
        defaultHeader.setOpaque(true);

        tablePanel.add(keyHeader);
        tablePanel.add(valueHeader);
        tablePanel.add(defaultHeader);

        int maxWidth = 0;
        for (int i = 0; i < toSave.length; i++) {
            String key = toSave[i];

            if (key != null) {
                JLabel label = new JLabel(key);
                JTextField field = new JTextField();
                JTextField defaultField = new JTextField();

                if (label.getPreferredSize().width > maxWidth) {
                    maxWidth = label.getPreferredSize().width;
                }

                try {
                    field.setText(configuration.getString(key));
                    defaultField.setText(configuration.getDefaultString(key));
                } catch (MissingResourceException e) {
                    field.setText(null);
                }
                for (String nonEditableKey : nonEditable) {
                    if (key.equals(nonEditableKey)) {
                        field.setEditable(false);
                    }
                }
                defaultField.setEditable(false);

                fields.put(key, field);
                defaultFields.put(key, defaultField);

                tablePanel.add(label);
                tablePanel.add(field);
                tablePanel.add(defaultField);
            } else {
                JPanel firstSeparator = new JPanel(new GridBagLayout());
                JPanel secondSeparator = new JPanel(new GridBagLayout());
                JPanel thirdSeparator = new JPanel(new GridBagLayout());

                firstSeparator.add(new JSeparator(),
                        new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
                secondSeparator.add(new JSeparator(),
                        new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
                thirdSeparator.add(new JSeparator(),
                        new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

                tablePanel.add(firstSeparator);
                tablePanel.add(secondSeparator);
                tablePanel.add(thirdSeparator);
            }
        }

        for (JTextField field : fields.values()) {
            field.setPreferredSize(new Dimension(maxWidth, field.getPreferredSize().height));
        }
        for (JTextField field : defaultFields.values()) {
            field.setPreferredSize(new Dimension(maxWidth, field.getPreferredSize().height));
        }

        JScrollPane scrollPane = new JScrollPane(tablePanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        buttonsPanel.add(Box.createHorizontalGlue());
        buttonsPanel.add(defaultButton);
        buttonsPanel.add(Box.createHorizontalStrut(HORIZONTAL_MARGIN));
        buttonsPanel.add(okButton);

        setLayout(new BorderLayout(HORIZONTAL_MARGIN, VERTICAL_MARGIN));
        add(scrollPane, BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.SOUTH);

        pack();
        int scrollBarWidth = scrollPane.getVerticalScrollBar().getWidth();
        setSize(new Dimension(getWidth() + scrollBarWidth, MAXIMUM_HEIGHT));
        setResizable(false);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowListenerForConfigurationFrame(this));
    }

    //==========================================================================
    // Getters
    //==========================================================================
    /**
     * Returns the button used save the configuration.
     *
     * @return The button used save the configuration.
     */
    public JButton getOkButton() {
        return okButton;
    }

    /**
     * Returns the button used to reset the configuration.
     *
     * @return The button used to reset the configuration.
     */
    public JButton getDefaultButton() {
        return defaultButton;
    }

    /**
     * Returns a mapping between attributes key and the text fields containing
     * their values.
     *
     * @return A mapping between attributes key and the text fields containing
     *         their values.
     */
    public Map<String, JTextField> getFields() {
        return fields;
    }

    /**
     * Returns a mapping between attributes key and the text fields containing their
     * default values.
     *
     * @return A mapping between attributes key and the text fields containing their
     * default values.
     */
    public Map<String, JTextField> getDefaultFields() {
        return defaultFields;
    }

    //==========================================================================
    // Setters
    //==========================================================================

    //==========================================================================
    // Inherited methods
    //==========================================================================

    //==========================================================================
    // Static methods
    //==========================================================================

    //==========================================================================
    // Methods
    //==========================================================================
}

