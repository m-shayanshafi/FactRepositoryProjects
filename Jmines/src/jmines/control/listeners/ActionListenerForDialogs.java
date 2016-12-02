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

import java.awt.event.ActionEvent;
import java.util.Map.Entry;

import jmines.model.GameBoard;
import jmines.view.dialogs.AboutDialog;
import jmines.view.dialogs.BestTimesDialog;
import jmines.view.dialogs.CustomDialog;
import jmines.view.dialogs.NameDialog;
import jmines.view.dialogs.RobotNameDialog;
import jmines.view.dialogs.StatisticsDialog;
import jmines.view.dialogs.ThanksDialog;
import jmines.view.dialogs.CustomDialog.Values;
import jmines.view.persistence.Configuration;

/**
 * The listener to use to listen the actions done on a button.
 *
 * @author Zleurtor
 */
public class ActionListenerForDialogs implements java.awt.event.ActionListener {

    //=========================================================================
    // Static attributes
    //=========================================================================

    //=========================================================================
    // Attributes
    //=========================================================================
    /**
     * The about dialog on which a user event can occurs.
     */
    private final AboutDialog aboutDialog;
    /**
     * The thanks dialog on which a user event can occurs.
     */
    private final ThanksDialog thanksDialog;
    /**
     * The best times dialog on which a user event can occurs.
     */
    private final BestTimesDialog bestTimesDialog;
    /**
     * The custom dialog on which a user event can occurs.
     */
    private final CustomDialog customDialog;
    /**
     * The name dialog on which a user event can occurs.
     */
    private final NameDialog nameDialog;
    /**
     * The robot name dialog on which a user event can occurs.
     */
    private final RobotNameDialog robotNameDialog;
    /**
     * The statistics dialog on which a user event can occurs.
     */
    private final StatisticsDialog statisticsDialog;

    //=========================================================================
    // Constructors
    //=========================================================================
    /**
     * Construct a new listener for the given dialog.
     *
     * @param newAboutDialog The about dialog on which a user event can occurs.
     */
    public ActionListenerForDialogs(final AboutDialog newAboutDialog) {
        aboutDialog = newAboutDialog;
        thanksDialog = null;
        bestTimesDialog = null;
        customDialog = null;
        nameDialog = null;
        robotNameDialog = null;
        statisticsDialog = null;
    }

    /**
     * Construct a new listener for the given dialog.
     *
     * @param newThanksDialog The thanks dialog on which a user event can
     *                        occurs.
     */
    public ActionListenerForDialogs(final ThanksDialog newThanksDialog) {
        aboutDialog = null;
        thanksDialog = newThanksDialog;
        bestTimesDialog = null;
        customDialog = null;
        nameDialog = null;
        robotNameDialog = null;
        statisticsDialog = null;
    }

    /**
     * Construct a new listener for the given dialog.
     *
     * @param newBestTimesDialog The best times dialog on which a user event
     *                           can occurs.
     */
    public ActionListenerForDialogs(final BestTimesDialog newBestTimesDialog) {
        aboutDialog = null;
        thanksDialog = null;
        bestTimesDialog = newBestTimesDialog;
        customDialog = null;
        nameDialog = null;
        robotNameDialog = null;
        statisticsDialog = null;
    }

    /**
     * Construct a new listener for the given dialog.
     *
     * @param newCustomDialog The custom dialog on which a user event can
     *                        occurs.
     */
    public ActionListenerForDialogs(final CustomDialog newCustomDialog) {
        aboutDialog = null;
        thanksDialog = null;
        bestTimesDialog = null;
        customDialog = newCustomDialog;
        nameDialog = null;
        robotNameDialog = null;
        statisticsDialog = null;
    }

    /**
     * Construct a new listener for the given dialog.
     *
     * @param newNameDialog The name dialog on which a user event can occurs.
     */
    public ActionListenerForDialogs(final NameDialog newNameDialog) {
        aboutDialog = null;
        thanksDialog = null;
        bestTimesDialog = null;
        customDialog = null;
        nameDialog = newNameDialog;
        robotNameDialog = null;
        statisticsDialog = null;
    }

    /**
     * Construct a new listener for the given dialog.
     *
     * @param newRobotNameDialog The robot name dialog on which a user event
     *                           can occurs.
     */
    public ActionListenerForDialogs(final RobotNameDialog newRobotNameDialog) {
        aboutDialog = null;
        thanksDialog = null;
        bestTimesDialog = null;
        customDialog = null;
        nameDialog = null;
        robotNameDialog = newRobotNameDialog;
        statisticsDialog = null;
    }

    /**
     * Construct a new listener for the given dialog.
     *
     * @param newStatisticsDialog The statistics dialog on which a user event
     *                            can occurs.
     */
    public ActionListenerForDialogs(final StatisticsDialog newStatisticsDialog) {
        aboutDialog = null;
        thanksDialog = null;
        bestTimesDialog = null;
        customDialog = null;
        nameDialog = null;
        robotNameDialog = null;
        statisticsDialog = newStatisticsDialog;
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
     * The method called when the button is clicked.
     *
     * @param evt The event Object representing the user action.
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent).
     */
    @Override
    public final void actionPerformed(final ActionEvent evt) {
        if (aboutDialog != null) {
            aboutDialog();
        } else if (thanksDialog != null) {
            thanksDialog();
        } else if (bestTimesDialog != null) {
            bestTimesDialog(evt);
        } else if (customDialog != null) {
            customDialog(evt);
        } else if (nameDialog != null) {
            nameDialog(evt);
        } else if (robotNameDialog != null) {
            robotNameDialog(evt);
        } else if (statisticsDialog != null) {
            statisticsDialog(evt);
        }
    }

    //=========================================================================
    // Static methods
    //=========================================================================

    //=========================================================================
    // Methods
    //=========================================================================
    /**
     * The method launched when an action occurs on the about dialog.
     */
    private void aboutDialog() {
        aboutDialog.setVisible(false);
    }

    /**
     * The method launched when an action occurs on the thanks dialog.
     */
    private void thanksDialog() {
        thanksDialog.setVisible(false);
    }

    /**
     * The method launched when an action occurs on the best times dialog.
     *
     * @param evt The event Object representing the user action.
     */
    private void bestTimesDialog(final ActionEvent evt) {
        if (evt.getSource() == bestTimesDialog.getShapesCombo()) {
            String shape = (String) bestTimesDialog.getShapesCombo().getSelectedItem();
            for (Entry<Byte, String> entry : bestTimesDialog.getShapes().entrySet()) {
                if (entry.getValue().equals(shape)) {
                    bestTimesDialog.initValues(entry.getKey());
                    bestTimesDialog.pack();
                    bestTimesDialog.setLocationRelativeTo(bestTimesDialog.getMainPanel());
                }
            }
        } else if (evt.getSource() == bestTimesDialog.getOkButton()) {
            bestTimesDialog.setVisible(false);
        } else if (evt.getSource() == bestTimesDialog.getDeleteButton()) {
            Configuration.getInstance().clearBestTimes();
            bestTimesDialog.setVisible(false);
        }
    }

    /**
     * The method launched when an action occurs on the custom dialog.
     *
     * @param evt The event Object representing the user action.
     */
    private void customDialog(final ActionEvent evt) {
        final int minWidth = Configuration.getInstance().getInt(Configuration.KEY_CUSTOM_MINWIDTH);
        final int minHeight = Configuration.getInstance().getInt(Configuration.KEY_CUSTOM_MINHEIGHT);
        final int maxWidth = Configuration.getInstance().getInt(Configuration.KEY_CUSTOM_MAXWIDTH);
        final int maxHeight = Configuration.getInstance().getInt(Configuration.KEY_CUSTOM_MAXHEIGHT);
        final int minMines = Configuration.getInstance().getInt(Configuration.KEY_CUSTOM_MINMINES);

        if (evt.getSource() == customDialog.getWidthTextField()
                || evt.getSource() == customDialog.getHeightTextField()
                || evt.getSource() == customDialog.getMinesTextField()) {
            customDialog.getOkButton().doClick();
        } else if (evt.getSource() == customDialog.getOkButton()) {
            Values values = new Values();
            try {
                values.setHeight(Integer.parseInt(customDialog.getHeightTextField().getText()));
            } catch (NumberFormatException e) {
                values.setHeight(0);
            }

            try {
                values.setWidth(Integer.parseInt(customDialog.getWidthTextField().getText()));
            } catch (NumberFormatException e) {
                values.setWidth(0);
            }

            try {
                values.setMines(Integer.parseInt(customDialog.getMinesTextField().getText()));
            } catch (NumberFormatException e) {
                values.setMines(0);
            }

            // Verify the interval of values
            if (values.getWidth() < minWidth) {
                values.setWidth(minWidth);
            }
            if (values.getHeight() < minHeight) {
                values.setHeight(minHeight);
            }
            if (values.getMines() < minMines) {
                values.setMines(minMines);
            }

            if (values.getWidth() > maxWidth) {
                values.setWidth(maxWidth);
            }
            if (values.getHeight() > maxHeight) {
                values.setHeight(maxHeight);
            }

            final int maxMines = GameBoard.getMaxMines(customDialog.getMainPanel().getGamePanel().getGameBoard().getTilesShape(), values.getWidth(), values.getHeight());
            if (values.getMines() > maxMines) {
                values.setMines(maxMines);
            }

            customDialog.setValues(values);
            customDialog.setVisible(false);
        } else if (evt.getSource() == customDialog.getCancelButton()) {
            customDialog.setVisible(false);
        }
    }

    /**
     * The method launched when an action occurs on the name dialog.
     *
     * @param evt The event Object representing the user action.
     */
    private void nameDialog(final ActionEvent evt) {
        if (evt.getSource() == nameDialog.getNameTextField()) {
            nameDialog.getOkButton().doClick();
        } else if (evt.getSource() == nameDialog.getOkButton()) {
            nameDialog.setValue(nameDialog.getNameTextField().getText());
            nameDialog.setVisible(false);
        }
    }

    /**
     * The method launched when an action occurs on the robot name dialog.
     *
     * @param evt The event Object representing the user action.
     */
    private void robotNameDialog(final ActionEvent evt) {
        if (evt.getSource() == robotNameDialog.getNameTextField()) {
            robotNameDialog.getOkButton().doClick();
        } else if (evt.getSource() == robotNameDialog.getDefaultButton()) {
            robotNameDialog.getNameTextField().setText(Configuration.getInstance().getString(Configuration.KEY_ROBOT_DEFAULTNAME));
        } else if (evt.getSource() == robotNameDialog.getOkButton()) {
            robotNameDialog.setValue(robotNameDialog.getNameTextField().getText());
            robotNameDialog.setVisible(false);
        }
    }

    /**
     * The method launched when an action occurs on the statistics dialog.
     *
     * @param evt The event Object representing the user action.
     */
    private void statisticsDialog(final ActionEvent evt) {
        if (evt.getSource() == statisticsDialog.getLevelsCombo()) {
            String level = (String) statisticsDialog.getLevelsCombo().getSelectedItem();
            for (Entry<String, String> entry : statisticsDialog.getLevelsAndShapes().entrySet()) {
                if (entry.getValue().equals(level)) {
                    statisticsDialog.initValues(entry.getKey());
                    statisticsDialog.pack();
                    statisticsDialog.setLocationRelativeTo(statisticsDialog.getMainPanel());
                }
            }
        } else if (evt.getSource() == statisticsDialog.getOkButton()) {
            statisticsDialog.setVisible(false);
        }
    }
}
