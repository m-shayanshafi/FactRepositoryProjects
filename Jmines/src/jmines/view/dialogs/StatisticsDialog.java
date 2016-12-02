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

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.Box;
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
 * The dialog used to display the statistics.
 *
 * @author Zleurtor
 */
public class StatisticsDialog extends JDialog {

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
     * The header label of this dialog.
     */
    private final JLabel headLabel = new JLabel(Configuration.getInstance().getText(Configuration.KEY_TITLE_STATISTICS));
    /**
     * The header label the level.
     */
    private final JLabel levelLabel = new JLabel();

    /**
     * The played games label.
     */
    private final JLabel playedLabel = new JLabel(Configuration.getInstance().getText(Configuration.KEY_TEXT_PLAYEDGAMES));
    /**
     * The win games label.
     */
    private final JLabel wonLabel = new JLabel(Configuration.getInstance().getText(Configuration.KEY_TEXT_WONGAMES));
    /**
     * The win percent label.
     */
    private final JLabel wonPercentLabel = new JLabel(Configuration.getInstance().getText(Configuration.KEY_TEXT_WONPERCENT));

    /**
     * The played games value.
     */
    private final JLabel playedValue = new JLabel();
    /**
     * The win games value.
     */
    private final JLabel wonValue = new JLabel();
    /**
     * The win percent value.
     */
    private final JLabel wonPercentValue = new JLabel();

    /**
     * The label for intermediate time.
     */
    private final JLabel mediumTimeLabel = new JLabel(Configuration.getInstance().getText(Configuration.KEY_TEXT_MEDIUMTIME));
    /**
     * The label for intermediate time.
     */
    private final JLabel minimumTimeLabel = new JLabel(Configuration.getInstance().getText(Configuration.KEY_TEXT_MINIMUMTIME));
    /**
     * The lanel for intermediate time.
     */
    private final JLabel maximumTimeLabel = new JLabel(Configuration.getInstance().getText(Configuration.KEY_TEXT_MAXIMUMTIME));

    /**
     * The value for intermediate time.
     */
    private final JLabel mediumTimeValue = new JLabel();
    /**
     * The value for intermediate time.
     */
    private final JLabel minimumTimeValue = new JLabel();
    /**
     * The value for intermediate time.
     */
    private final JLabel maximumTimeValue = new JLabel();

    /**
     * The combo box used to allow the user to choose the level and tiles shape.
     */
    private final JComboBox levelsCombo = new JComboBox();
    /**
     * The button used to close this dialog without doing anything.
     */
    private final JButton okButton = new JButton(Configuration.getInstance().getText(Configuration.KEY_TEXT_OK));

    /**
     * The JMines main panel for which this dialog is displayed.
     */
    private final MainPanel mainPanel;

    /**
     * A map linking each [level, tiles shape] to a displayable string.
     */
    private final Map<String, String> levelsAndShapes = new LinkedHashMap<String, String>();

    //==========================================================================
    // Constructors
    //==========================================================================
    /**
     * Construct a new dialog using the given JMines main panel.
     *
     * @param newMainPanel The JMines main panel for which this dialog is
     *                     displayed.
     */
    public StatisticsDialog(final MainPanel newMainPanel) {
        super((JFrame) null, Configuration.getInstance().getText(Configuration.KEY_TITLE_STATISTICS), true);

        this.mainPanel = newMainPanel;

        init();

        pack();
        setResizable(false);

        // Initializing the levels map ...
        Configuration configuration = Configuration.getInstance();
        String[] levels = new String[] {
                Configuration.DIFFICULTY_BEGINNER,
                Configuration.DIFFICULTY_INTERMEDIATE,
                Configuration.DIFFICULTY_EXPERT
        };

        for (Iterator<Byte> j = GameBoard.SUPPORTED_SHAPES.iterator(); j.hasNext();) {
            byte shape = j.next();
            for (int i = 0; i < levels.length; i++) {
                String key = shape + Configuration.DOT + levels[i];

                String value = Configuration.COMA + ' ' + configuration.getText(Configuration.PREFIX_TEXT + '.' + levels[i]);
                switch (shape) {
                case GameBoard.SHAPE_TRIANGULAR:
                    value = configuration.getText(Configuration.KEY_TEXT_TRIANGULAR) + value;
                    break;
                case GameBoard.SHAPE_TRIANGULAR_14:
                    value = configuration.getText(Configuration.KEY_TEXT_TRIANGULAR_14) + value;
                    break;
                case GameBoard.SHAPE_SQUARE:
                    value = configuration.getText(Configuration.KEY_TEXT_SQUARE) + value;
                    break;
                case GameBoard.SHAPE_PENTAGONAL:
                    value = configuration.getText(Configuration.KEY_TEXT_PENTAGONAL) + value;
                    break;
                case GameBoard.SHAPE_HEXAGONAL:
                    value = configuration.getText(Configuration.KEY_TEXT_HEXAGONAL) + value;
                    break;
                case GameBoard.SHAPE_OCTOSQUARE:
                    value = configuration.getText(Configuration.KEY_TEXT_OCTOSQUARE) + value;
                    break;
                case GameBoard.SHAPE_PARQUET:
                    value = configuration.getText(Configuration.KEY_TEXT_PARQUET) + value;
                    break;
                default:
                break;
                }

                levelsAndShapes.put(key, value);
            }
        }

        ActionListenerForDialogs listener = new ActionListenerForDialogs(this);

        levelsCombo.addActionListener(listener);
        okButton.addActionListener(listener);
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
    public final JComboBox getLevelsCombo() {
        return levelsCombo;
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
     * Returns a map linking each [level, tiles shape] to a displayable string.
     *
     * @return A map linking each [level, tiles shape] to a displayable string.
     */
    public final Map<String, String> getLevelsAndShapes() {
        return levelsAndShapes;
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
        String key = mainPanel.getGamePanel().getGameBoard().getTilesShape() + Configuration.DOT + mainPanel.getGamePanel().getDifficulty();
        if (!levelsAndShapes.containsKey(key)) {
            key = mainPanel.getGamePanel().getGameBoard().getTilesShape() + Configuration.DOT + Configuration.DIFFICULTY_BEGINNER;
        }
        initValues(key);

        pack();
        setLocationRelativeTo(mainPanel);
        super.setVisible(b);
    }

    //==========================================================================
    // Static methods
    //==========================================================================

    //==========================================================================
    // Methods
    //==========================================================================
    /**
     * Initialize the dialog and place all the sub components.
     */
    private void init() {
        final int three = 3;
        final int four = 4;
        final int five = 5;
        final int six = 6;
        final int seven = 7;
        final int eight = 8;

        setLayout(new GridBagLayout());

        add(headLabel, new GridBagConstraints(0, 0, 2, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(VERTICAL_MARGIN, HORIZONTAL_MARGIN, VERTICAL_MARGIN / 2, HORIZONTAL_MARGIN), 0, 0));

        add(levelLabel, new GridBagConstraints(0, 1, 2, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(VERTICAL_MARGIN / 2, HORIZONTAL_MARGIN, VERTICAL_MARGIN / 2, HORIZONTAL_MARGIN), 0, 0));

        add(playedLabel, new GridBagConstraints(0, 2, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(VERTICAL_MARGIN / 2, HORIZONTAL_MARGIN, 0, HORIZONTAL_SPACE / 2), 0, 0));
        add(wonLabel, new GridBagConstraints(0, three, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, HORIZONTAL_MARGIN, 0, HORIZONTAL_SPACE / 2), 0, 0));
        add(wonPercentLabel, new GridBagConstraints(0, four, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, HORIZONTAL_MARGIN, VERTICAL_MARGIN / 2, HORIZONTAL_SPACE / 2), 0, 0));

        add(mediumTimeLabel, new GridBagConstraints(0, five, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(VERTICAL_MARGIN / 2, HORIZONTAL_MARGIN, 0, HORIZONTAL_SPACE / 2), 0, 0));
        add(minimumTimeLabel, new GridBagConstraints(0, six, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, HORIZONTAL_MARGIN, 0, HORIZONTAL_SPACE / 2), 0, 0));
        add(maximumTimeLabel, new GridBagConstraints(0, seven, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, HORIZONTAL_MARGIN, VERTICAL_MARGIN / 2, HORIZONTAL_SPACE / 2), 0, 0));

        add(playedValue, new GridBagConstraints(1, 2, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(VERTICAL_MARGIN / 2, HORIZONTAL_SPACE / 2, 0, HORIZONTAL_MARGIN), 0, 0));
        add(wonValue, new GridBagConstraints(1, three, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, HORIZONTAL_SPACE / 2, 0, HORIZONTAL_MARGIN), 0, 0));
        add(wonPercentValue, new GridBagConstraints(1, four, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, HORIZONTAL_SPACE / 2, VERTICAL_MARGIN / 2, HORIZONTAL_MARGIN), 0, 0));

        add(mediumTimeValue, new GridBagConstraints(1, five, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(VERTICAL_MARGIN / 2, HORIZONTAL_SPACE / 2, 0, HORIZONTAL_MARGIN), 0, 0));
        add(minimumTimeValue, new GridBagConstraints(1, six, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, HORIZONTAL_SPACE / 2, 0, HORIZONTAL_MARGIN), 0, 0));
        add(maximumTimeValue, new GridBagConstraints(1, seven, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, HORIZONTAL_SPACE / 2, VERTICAL_MARGIN / 2, HORIZONTAL_MARGIN), 0, 0));

        Box box = Box.createHorizontalBox();

        box.add(levelsCombo, new GridBagConstraints(0, eight, 2, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(VERTICAL_MARGIN / 2, HORIZONTAL_MARGIN, VERTICAL_MARGIN, HORIZONTAL_SPACE / 2), 0, 0));
        box.add(Box.createHorizontalStrut(HORIZONTAL_SPACE));
        box.add(okButton, new GridBagConstraints(0, eight, 2, 1, 0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(VERTICAL_MARGIN / 2, HORIZONTAL_SPACE / 2, VERTICAL_MARGIN, HORIZONTAL_MARGIN), 0, 0));

        add(box, new GridBagConstraints(0, eight, 2, 1, 0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(VERTICAL_MARGIN / 2, HORIZONTAL_SPACE / 2, VERTICAL_MARGIN, HORIZONTAL_MARGIN), 0, 0));
    }

    /**
     * The method used to initialize the values contained in this dialog.
     *
     * @param key A string defining the wanted level and the tiles shape.
     */
    public final void initValues(final String key) {
        final double ten = 10;
        final double hundred = 100;
        final double thousand = 1000d;
        Configuration configuration = Configuration.getInstance();

        headLabel.setFont(new Font(headLabel.getFont().getName(), Font.BOLD, headLabel.getFont().getSize()));

        String level = levelsAndShapes.get(key);
        levelLabel.setText(level);

        int played = 0;
        if (mainPanel.getRobot().getPlayed().containsKey(key)) {
            played = mainPanel.getRobot().getPlayed().get(key);
        }
        int won = 0;
        if (mainPanel.getRobot().getWon().containsKey(key)) {
            won = mainPanel.getRobot().getWon().get(key);
        }
        double wonPercent = 0;
        if (played != 0) {
            wonPercent = Math.round(((hundred * won) / played) * ten) / ten;
        }

        playedValue.setText(Integer.toString(played));
        wonValue.setText(Integer.toString(won));
        wonPercentValue.setText(Double.toString(wonPercent));

        String medium = configuration.getText(Configuration.KEY_TEXT_SECONDS);
        if (mainPanel.getRobot().getMediumTime().containsKey(key)) {
            medium = Double.toString(mainPanel.getRobot().getMediumTime().get(key) / thousand) + " " + medium;
        }
        String minimum = configuration.getText(Configuration.KEY_TEXT_SECONDS);
        if (mainPanel.getRobot().getMinimumTime().containsKey(key)) {
            minimum = Double.toString(mainPanel.getRobot().getMinimumTime().get(key) / thousand) + " " + minimum;
        }
        String maximum = configuration.getText(Configuration.KEY_TEXT_SECONDS);
        if (mainPanel.getRobot().getMaximumTime().containsKey(key)) {
            maximum = Double.toString(mainPanel.getRobot().getMaximumTime().get(key) / thousand) + " " + maximum;
        }

        if (won == 0) {
            mediumTimeLabel.setVisible(false);
            minimumTimeLabel.setVisible(false);
            maximumTimeLabel.setVisible(false);
            mediumTimeValue.setVisible(false);
            minimumTimeValue.setVisible(false);
            maximumTimeValue.setVisible(false);
        } else {
            mediumTimeLabel.setVisible(true);
            minimumTimeLabel.setVisible(true);
            maximumTimeLabel.setVisible(true);
            mediumTimeValue.setVisible(true);
            minimumTimeValue.setVisible(true);
            maximumTimeValue.setVisible(true);
        }

        mediumTimeValue.setText(medium);
        minimumTimeValue.setText(minimum);
        maximumTimeValue.setText(maximum);

        ComboBoxModel model = new DefaultComboBoxModel(levelsAndShapes.values().toArray());
        model.setSelectedItem(level);
        levelsCombo.setModel(model);
    }
}
