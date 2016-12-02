/**
 *   Stigma - Multiplayer online RPG - http://stigma.sourceforge.net
 *   Copyright (C) 2005-2009 Minions Studio
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *   
 */
package pl.org.minions.stigma.editor.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

/**
 * Factory for GUI components.
 */
public final class GUIFactory
{
    public static final int ROW_HEIGHT = 20;

    private static final int MARGIN_X = 5;
    private static final int MARGIN_Y = 5;
    private static final int BASIC_FONT_SIZE = 11;

    /**
     * Hidden constructor of utility class.
     */
    protected GUIFactory()
    {
    }

    /**
     * Creates JLabel.
     * @param text
     *            text on label
     * @return JLabel with provided text
     */
    public static JLabel getLabel(String text)
    {
        return new JLabel(text);
    }

    /**
     * Creates label ands adds it.
     * @param text
     *            text to put on label
     * @param x
     *            x position
     * @param row
     *            row number
     * @param width
     *            width of label
     * @param container
     *            container to add label to
     * @return created JLabel
     */
    public static JLabel createAndAddLabel(String text,
                                           int x,
                                           int row,
                                           int width,
                                           JComponent container)
    {
        JLabel label = new JLabel(text);
        label.setBounds(MARGIN_X + x,
                        MARGIN_Y + row * (MARGIN_Y + ROW_HEIGHT),
                        width,
                        ROW_HEIGHT);
        container.add(label);
        return label;
    }

    /**
     * Creates JTextField and adds it.
     * @param label
     *            label of field
     * @param value
     *            value of field
     * @param x
     *            x position
     * @param row
     *            row number
     * @param width
     *            width of label
     * @param container
     *            container to add label to
     * @return JTextField with provided label and value
     */
    public static JTextField createTitledTextField(String label,
                                                   String value,
                                                   int x,
                                                   int row,
                                                   int width,
                                                   JComponent container)
    {
        JLabel labelField = new JLabel(label);
        labelField.setBounds(MARGIN_X + x, MARGIN_Y + row
            * (MARGIN_Y + 2 * ROW_HEIGHT), width, ROW_HEIGHT);
        setFont(labelField);
        container.add(labelField);

        JTextField textField = new JTextField(value);
        textField.setBounds(MARGIN_X + x, MARGIN_Y + ROW_HEIGHT + row
            * (MARGIN_Y + 2 * ROW_HEIGHT), width, ROW_HEIGHT);
        container.add(textField);
        setFont(textField);

        return textField;
    }

    /**
     * Creates titled number spinner field.
     * @param label
     *            label
     * @param value
     *            value
     * @param x
     *            margin
     * @param row
     *            row
     * @param width
     *            width
     * @param container
     *            containter
     * @return spinner number model of the spinner field
     */
    public static SpinnerNumberModel createTitledNumberSpinnerField(String label,
                                                                    int value,
                                                                    int x,
                                                                    int row,
                                                                    int width,
                                                                    JComponent container)
    {

        JLabel labelField = new JLabel(label);
        labelField.setBounds(MARGIN_X + x, MARGIN_Y + row
            * (MARGIN_Y + 2 * ROW_HEIGHT), width, ROW_HEIGHT);
        setFont(labelField);
        container.add(labelField);

        JSpinner valueSpinner = new JSpinner();
        SpinnerNumberModel spinnerNumberModel = new SpinnerNumberModel();
        valueSpinner.setBounds(MARGIN_X + x, MARGIN_Y + ROW_HEIGHT + row
            * (MARGIN_Y + 2 * ROW_HEIGHT), width, ROW_HEIGHT);
        setFont(valueSpinner);
        valueSpinner.setModel(spinnerNumberModel);
        container.add(valueSpinner);

        spinnerNumberModel.setStepSize(1);
        spinnerNumberModel.setMinimum(1);
        spinnerNumberModel.setValue(value);

        return spinnerNumberModel;
    }

    /**
     * Creates titled combo box field.
     * @param label
     *            label
     * @param x
     *            margin
     * @param row
     *            row
     * @param width
     *            width
     * @param container
     *            container
     * @return combo box
     */
    public static JComboBox createTitledComboBoxField(String label,
                                                      int x,
                                                      int row,
                                                      int width,
                                                      JComponent container)
    {
        JLabel labelField = new JLabel(label);
        labelField.setBounds(MARGIN_X + x, MARGIN_Y + row
            * (MARGIN_Y + 2 * ROW_HEIGHT), width, ROW_HEIGHT);
        setFont(labelField);
        container.add(labelField);

        JComboBox comboBox = new JComboBox();
        comboBox.setBounds(MARGIN_X + x, MARGIN_Y + ROW_HEIGHT + row
            * (MARGIN_Y + 2 * ROW_HEIGHT), width, ROW_HEIGHT);
        setFont(comboBox);
        comboBox.setBackground(Color.white);
        container.add(comboBox);

        return comboBox;
    }

    /**
     * Creates titled button.
     * @param title
     *            title
     * @return button
     */
    public static JButton createTitledButton(String title)
    {
        JButton button = new JButton(title);
        setFont(button);
        return button;
    }

    /**
     * Sets font in a component to Dialog.
     * @param component
     *            component
     */
    public static void setFont(Component component)
    {
        component.setFont(new Font("Dialog", 0, BASIC_FONT_SIZE));
    }

    /**
     * Wraps text with HTML tags and replaces linebreakes
     * with BR tags.
     * @param text
     *            text to be presented as HTML
     * @return text presented as HTML
     */
    public static String htmlizeText(String text)
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<HTML>");
        stringBuilder.append(text.replace("\n", "<br>"));
        stringBuilder.append("</HTML>");
        return stringBuilder.toString();
    }
}
