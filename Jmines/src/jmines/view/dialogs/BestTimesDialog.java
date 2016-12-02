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
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.MissingResourceException;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

import jmines.control.listeners.ActionListenerForDialogs;
import jmines.model.GameBoard;
import jmines.view.components.MainPanel;
import jmines.view.persistence.Configuration;

/**
 * The dialog used to display the best player times.
 *
 * @author Zleurtor
 */
public class BestTimesDialog extends JDialog {

    //==========================================================================
    // Static attributes
    //==========================================================================
    /**
     * The unique serial version identifier.
     */
    private static final long serialVersionUID = -5334491057212807539L;
    /**
     * The margin used at the left and right of this dialog.
     */
    private static final int HORIZONTAL_MARGIN = 17;
    /**
     * The margin used at the top and bottom of this dialog.
     */
    private static final int VERTICAL_MARGIN = 27;
    /**
     * The margin used between a label and a value and between a value and a
     * name.
     */
    private static final int HORIZONTAL_SPACE = 22;

    //==========================================================================
    // Attributes
    //==========================================================================
    /**
     * The label for beginner time.
     */
    private final JLabel beginnerLabel = new JLabel(Configuration.getInstance().getText(Configuration.KEY_TEXT_BEGINNER));
    /**
     * The value for beginner time.
     */
    private final JLabel beginnerTime = new JLabel();
    /**
     * The name for beginner time.
     */
    private final JLabel beginnerName = new JLabel();

    /**
     * The label for intermediate time.
     */
    private final JLabel intermediateLabel = new JLabel(Configuration.getInstance().getText(Configuration.KEY_TEXT_INTERMEDIATE));
    /**
     * The value for intermediate time.
     */
    private final JLabel intermediateTime = new JLabel();
    /**
     * The name for intermediate time.
     */
    private final JLabel intermediateName = new JLabel();

    /**
     * The label for expert time.
     */
    private final JLabel expertLabel = new JLabel(Configuration.getInstance().getText(Configuration.KEY_TEXT_EXPERT));
    /**
     * The value for expert time.
     */
    private final JLabel expertTime = new JLabel();
    /**
     * The name for expert time.
     */
    private final JLabel expertName = new JLabel();

    /**
     * The combo box used to allow the user to choose the level and tiles shape.
     */
    private final JComboBox shapesCombo = new JComboBox();
    /**
     * The button used to reset all the best times.
     */
    private final JButton deleteButton = new JButton(Configuration.getInstance().getText(Configuration.KEY_TEXT_DELETETIMES));
    /**
     * The button used to close this dialog without doing anything.
     */
    private final JButton okButton = new JButton(Configuration.getInstance().getText(Configuration.KEY_TEXT_OK));

    /**
     * The JMines main panel for which this dialog is displayed.
     */
    private final MainPanel mainPanel;

    /**
     * A map linking each tiles shape to a displayable string.
     */
    private final Map<Byte, String> shapes = new LinkedHashMap<Byte, String>();

    //==========================================================================
    // Constructors
    //==========================================================================
    /**
     * Construct a new dialog using the given JMines main panel.
     *
     * @param newMainPanel The JMines main panel for which this dialog is
     *                     displayed.
     */
    public BestTimesDialog(final MainPanel newMainPanel) {
        super((JFrame) null, Configuration.getInstance().getText(Configuration.KEY_TITLE_BESTTIMES), true);

        this.mainPanel = newMainPanel;

        init();

        pack();
        setResizable(false);

        // Initializing the shapes map ...
        Configuration configuration = Configuration.getInstance();

        for (Iterator<Byte> j = GameBoard.SUPPORTED_SHAPES.iterator(); j.hasNext();) {
            byte key = j.next();

            String value = "";
            switch (key) {
            case GameBoard.SHAPE_TRIANGULAR:
                value = configuration.getText(Configuration.KEY_TEXT_TRIANGULAR);
                break;
            case GameBoard.SHAPE_TRIANGULAR_14:
                value = configuration.getText(Configuration.KEY_TEXT_TRIANGULAR_14);
                break;
            case GameBoard.SHAPE_SQUARE:
                value = configuration.getText(Configuration.KEY_TEXT_SQUARE);
                break;
            case GameBoard.SHAPE_PENTAGONAL:
                value = configuration.getText(Configuration.KEY_TEXT_PENTAGONAL);
                break;
            case GameBoard.SHAPE_HEXAGONAL:
                value = configuration.getText(Configuration.KEY_TEXT_HEXAGONAL);
                break;
            case GameBoard.SHAPE_OCTOSQUARE:
                value = configuration.getText(Configuration.KEY_TEXT_OCTOSQUARE);
                break;
            case GameBoard.SHAPE_PARQUET:
                value = configuration.getText(Configuration.KEY_TEXT_PARQUET);
                break;
            default:
                break;
            }

            shapes.put(key, value);
        }

        ActionListenerForDialogs listener = new ActionListenerForDialogs(this);
        shapesCombo.addActionListener(listener);
        okButton.addActionListener(listener);
        deleteButton.setFocusable(false);
        deleteButton.addActionListener(listener);
    }

    //==========================================================================
    // Getters
    //==========================================================================
    /**
     * Returns the combo box used to allow the user to choose the level and
     * tiles shape.
     *
     * @return The combo box used to allow the user to choose the level and
     *         tiles shape.
     */
    public final JComboBox getShapesCombo() {
        return shapesCombo;
    }

    /**
     * Returns the button used to reset all the best times.
     *
     * @return The button used to reset all the best times.
     */
    public final JButton getDeleteButton() {
        return deleteButton;
    }

    /**
     * Returns the button used to close this dialog without doing anything.
     *
     * @return The button used to close this dialog without doing anything.
     */
    public final JButton getOkButton() {
        return okButton;
    }

    /**
     * Returns the JMines main panel for which this dialog is displayed.
     *
     * @return The JMines main panel for which this dialog is displayed.
     */
    public final MainPanel getMainPanel() {
        return mainPanel;
    }

    /**
     * Returns a map linking each tiles shape to a displayable string.
     *
     * @return A map linking each tiles shape to a displayable string.
     */
    public final Map<Byte, String> getShapes() {
        return shapes;
    }

    //==========================================================================
    // Setters
    //==========================================================================

    //==========================================================================
    // Inherited methods
    //==========================================================================
    /**
     * Shows or hides this component depending on the value of the parameter.
     * Moreover the values and names are updated and this dialog is centered
     * relatively to its main panel.
     *
     * @param b if <code>true</code>, shows this component, otherwise, hides
     *          this component
     * @see java.awt.Component#setVisible(boolean)
     */
    @Override
    public final void setVisible(final boolean b) {
        byte shape = mainPanel.getGamePanel().getGameBoard().getTilesShape();
        initValues(shape);

        pack();
        setLocationRelativeTo(mainPanel);
        super.setVisible(b);
    }

    /**
     * Initialize the dialog and place all the sub components.
     */
    private void init() {
        // Add the labels
        final int three = 3;
        final int four = 4;

        setLayout(new GridBagLayout());
        add(beginnerLabel,
                new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(VERTICAL_MARGIN, HORIZONTAL_MARGIN, 0, HORIZONTAL_SPACE / 2), 0, 0));
        add(beginnerTime,
                new GridBagConstraints(1, 0, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(VERTICAL_MARGIN, HORIZONTAL_SPACE / 2, 0, HORIZONTAL_SPACE / 2), 0, 0));
        add(beginnerName,
                new GridBagConstraints(2, 0, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(VERTICAL_MARGIN, HORIZONTAL_SPACE / 2, 0, HORIZONTAL_MARGIN), 0, 0));

        add(intermediateLabel,
                new GridBagConstraints(0, 1, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, HORIZONTAL_MARGIN, 0, HORIZONTAL_SPACE / 2), 0, 0));
        add(intermediateTime,
                new GridBagConstraints(1, 1, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, HORIZONTAL_SPACE / 2, 0, HORIZONTAL_SPACE / 2), 0, 0));
        add(intermediateName,
                new GridBagConstraints(2, 1, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, HORIZONTAL_SPACE / 2, 0, HORIZONTAL_MARGIN), 0, 0));

        add(expertLabel,
                new GridBagConstraints(0, 2, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, HORIZONTAL_MARGIN, VERTICAL_MARGIN / 2, HORIZONTAL_SPACE / 2), 0, 0));
        add(expertTime,
                new GridBagConstraints(1, 2, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, HORIZONTAL_SPACE / 2, VERTICAL_MARGIN / 2, HORIZONTAL_SPACE / 2), 0, 0));
        add(expertName,
                new GridBagConstraints(2, 2, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, HORIZONTAL_SPACE / 2, VERTICAL_MARGIN / 2, HORIZONTAL_MARGIN), 0, 0));

        add(shapesCombo,
                new GridBagConstraints(0, three, GridBagConstraints.REMAINDER, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(VERTICAL_MARGIN / 2, HORIZONTAL_MARGIN, VERTICAL_MARGIN / 2, 0), 0, 0));

        add(deleteButton,
                new GridBagConstraints(0, four, 2, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(VERTICAL_MARGIN / 2, HORIZONTAL_MARGIN, VERTICAL_MARGIN, 0), 0, 0));
        add(okButton,
                new GridBagConstraints(2, four, 1, 1, 0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(VERTICAL_MARGIN / 2, 0, VERTICAL_MARGIN, HORIZONTAL_MARGIN), 0, 0));
    }

    //==========================================================================
    // Static methods
    //==========================================================================

    //==========================================================================
    // Methods
    //==========================================================================
    /**
     * The method used to initialize the values contained in this dialog.
     *
     * @param shape The shape for which the information are to be displayed.
     */
    public final void initValues(final byte shape) {
        Configuration configuration = Configuration.getInstance();

        String shapeName = "";
        switch (shape) {
        case GameBoard.SHAPE_TRIANGULAR:
            shapeName = Configuration.SHAPE_TRIANGULAR;
            break;
        case GameBoard.SHAPE_TRIANGULAR_14:
            shapeName = Configuration.SHAPE_TRIANGULAR_14;
            break;
        case GameBoard.SHAPE_SQUARE:
            shapeName = Configuration.SHAPE_SQUARE;
            break;
        case GameBoard.SHAPE_PENTAGONAL:
            shapeName = Configuration.SHAPE_PENTAGONAL;
            break;
        case GameBoard.SHAPE_HEXAGONAL:
            shapeName = Configuration.SHAPE_HEXAGONAL;
            break;
        case GameBoard.SHAPE_OCTOSQUARE:
            shapeName = Configuration.SHAPE_OCTOSQUARE;
            break;
        case GameBoard.SHAPE_PARQUET:
            shapeName = Configuration.SHAPE_PARQUET;
            break;
        default:
            break;
        }

        String text;

        try {
            text = configuration.getString(shapeName + Configuration.DOT + Configuration.DIFFICULTY_BEGINNER + Configuration.DOT + Configuration.SUFFIX_BEST);
            if (text == null) {
                throw new MissingResourceException(null, null, null);
            }
        } catch (MissingResourceException e) {
            text = configuration.getText(Configuration.KEY_BEST_DEFAULT_NAME) + Configuration.COMA + configuration.getString(Configuration.KEY_BEST_DEFAULT_TIME);
        }
        beginnerTime.setText(text.substring(text.lastIndexOf(Configuration.COMA) + 1) + " " + configuration.getText(Configuration.KEY_TEXT_SECONDS));
        beginnerName.setText(text.substring(0, text.lastIndexOf(Configuration.COMA)));
        try {
            text = configuration.getString(shapeName + Configuration.DOT + Configuration.DIFFICULTY_INTERMEDIATE + Configuration.DOT + Configuration.SUFFIX_BEST);
            if (text == null) {
                throw new MissingResourceException(null, null, null);
            }
        } catch (MissingResourceException e) {
            text = configuration.getText(Configuration.KEY_BEST_DEFAULT_NAME) + Configuration.COMA + configuration.getString(Configuration.KEY_BEST_DEFAULT_TIME);
        }
        intermediateTime.setText(text.substring(text.lastIndexOf(Configuration.COMA) + 1) + " " + configuration.getText(Configuration.KEY_TEXT_SECONDS));
        intermediateName.setText(text.substring(0, text.lastIndexOf(Configuration.COMA)));
        try {
            text = configuration.getString(shapeName + Configuration.DOT + Configuration.DIFFICULTY_EXPERT + Configuration.DOT + Configuration.SUFFIX_BEST);
            if (text == null) {
                throw new MissingResourceException(null, null, null);
            }
        } catch (MissingResourceException e) {
            text = configuration.getText(Configuration.KEY_BEST_DEFAULT_NAME) + Configuration.COMA + configuration.getString(Configuration.KEY_BEST_DEFAULT_TIME);
        }
        expertTime.setText(text.substring(text.lastIndexOf(Configuration.COMA) + 1) + " " + configuration.getText(Configuration.KEY_TEXT_SECONDS));
        expertName.setText(text.substring(0, text.lastIndexOf(Configuration.COMA)));


        ComboBoxModel model = new DefaultComboBoxModel(shapes.values().toArray());
        model.setSelectedItem(shapes.get(shape));
        shapesCombo.setModel(model);
    }
}
