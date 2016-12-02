/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: StatisticSettingsPanel.java
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 * 
 * This file is part of JDiveLog.
 * JDiveLog is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.

 * JDiveLog is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with JDiveLog; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package net.sf.jdivelog.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.border.Border;

import net.sf.jdivelog.gui.commands.UndoableCommand;
import net.sf.jdivelog.gui.resources.Messages;
import net.sf.jdivelog.model.StatisticSettings;

public class StatisticSettingsPanel extends AbstractSettingsPanel {
    
    private static final long serialVersionUID = 1L;
    private static final String[] STATISTIC_TYPES = { StatisticPanel.TYPE_BAR, StatisticPanel.TYPE_BAR3D, StatisticPanel.TYPE_PIE, StatisticPanel.TYPE_PIE3D };
    private static final String[] STATISTIC_ORIENTATIONS = { StatisticPanel.ORIENTATION_HORIZONTAL, StatisticPanel.ORIENTATION_VERTICAL };
    
    private MainWindow mainWindow;
    private StatisticSettings settings;
    
    private JComboBox buddyStatisticTypeField;
    private JComboBox buddyStatisticOrientationField;
    private JComboBox divePlaceStatisticTypeField;
    private JComboBox divePlaceStatisticOrientationField;
    private JComboBox countryStatisticTypeField;
    private JComboBox countryStatisticOrientationField;
    private JComboBox diveTypeStatisticTypeField;
    private JComboBox diveTypeStatisticOrientationField;
    private JComboBox diveActivityStatisticTypeField;
    private JComboBox diveActivityStatisticOrientationField;
    private JComboBox watersStatisticTypeField;
    private JComboBox watersStatisticOrientationField;

    public StatisticSettingsPanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        initialize();
    }

    public void load() {
        settings = mainWindow.getLogBook().getStatisticSettings();
        getBuddyStatisticTypeField().setSelectedItem(settings.getBuddyStatistic().getType());
        getBuddyStatisticOrientationField().setSelectedItem(settings.getBuddyStatistic().getOrientation());
        getDivePlaceStatisticTypeField().setSelectedItem(settings.getDivePlaceStatistic().getType());
        getDivePlaceStatisticOrientationField().setSelectedItem(settings.getDivePlaceStatistic().getOrientation());
        getCountryStatisticTypeField().setSelectedItem(settings.getCountryStatistic().getType());
        getCountryStatisticOrientationField().setSelectedItem(settings.getCountryStatistic().getOrientation());
        getDiveTypeStatisticTypeField().setSelectedItem(settings.getDiveTypeStatistic().getType());
        getDiveTypeStatisticOrientationField().setSelectedItem(settings.getDiveTypeStatistic().getOrientation());
        getDiveActivityStatisticTypeField().setSelectedItem(settings.getDiveActivityStatistic().getType());
        getDiveActivityStatisticOrientationField().setSelectedItem(settings.getDiveActivityStatistic().getOrientation());
        getWatersStatisticTypeField().setSelectedItem(settings.getWatersStatistic().getType());
        getWatersStatisticOrientationField().setSelectedItem(settings.getWatersStatistic().getOrientation());
    }

    public UndoableCommand getSaveCommand() {
        return new CommandSave();
    }
    
    //
    // private methods
    //
    
    private void initialize() {
        setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.anchor = GridBagConstraints.NORTHWEST;
        gc.insets = new Insets(0, 0, 10, 10);
        gc.gridy = 0;
        gc.gridx = 0;
        add(new JLabel(Messages.getString("configuration.statistics.buddy")), gc); //$NON-NLS-1$
        gc.gridx = 1;
        add(getBuddyStatisticTypeField(), gc);
        gc.gridx = 2;
        add(getBuddyStatisticOrientationField(), gc);
        
        gc.gridy = 1;
        gc.gridx = 0;
        add(new JLabel(Messages.getString("configuration.statistics.diveplace")), gc); //$NON-NLS-1$
        gc.gridx = 1;
        add(getDivePlaceStatisticTypeField(), gc);
        gc.gridx = 2;
        add(getDivePlaceStatisticOrientationField(), gc);
        
        gc.gridy = 2;
        gc.gridx = 0;
        add(new JLabel(Messages.getString("configuration.statistics.country")), gc); //$NON-NLS-1$
        gc.gridx = 1;
        add(getCountryStatisticTypeField(), gc);
        gc.gridx = 2;
        add(getCountryStatisticOrientationField(), gc);
        
        gc.gridy = 3;
        gc.gridx = 0;
        add(new JLabel(Messages.getString("configuration.statistics.divetype")), gc); //$NON-NLS-1$
        gc.gridx = 1;
        add(getDiveTypeStatisticTypeField(), gc);
        gc.gridx = 2;
        add(getDiveTypeStatisticOrientationField(), gc);
        
        gc.gridy = 4;
        gc.gridx = 0;
        add(new JLabel(Messages.getString("configuration.statistics.diveactivity")), gc); //$NON-NLS-1$
        gc.gridx = 1;
        add(getDiveActivityStatisticTypeField(), gc);
        gc.gridx = 2;
        add(getDiveActivityStatisticOrientationField(), gc);
        
        gc.gridy = 5;
        gc.gridx = 0;
        add(new JLabel(Messages.getString("configuration.statistics.waters")), gc); //$NON-NLS-1$
        gc.gridx = 1;
        add(getWatersStatisticTypeField(), gc);
        gc.gridx = 2;
        add(getWatersStatisticOrientationField(), gc);

        Border border = BorderFactory.createTitledBorder(Messages.getString("configuration.statistics")); //$NON-NLS-1$
        setBorder(border);    
    }
    
    private JComboBox getBuddyStatisticTypeField() {
        if (buddyStatisticTypeField == null) {
            buddyStatisticTypeField = new JComboBox(STATISTIC_TYPES);
        }
        return buddyStatisticTypeField;
    }
    
    private JComboBox getBuddyStatisticOrientationField() {
        if (buddyStatisticOrientationField == null) {
            buddyStatisticOrientationField = new JComboBox(STATISTIC_ORIENTATIONS);
        }
        return buddyStatisticOrientationField;
    }

    private JComboBox getDivePlaceStatisticTypeField() {
        if (divePlaceStatisticTypeField == null) {
            divePlaceStatisticTypeField = new JComboBox(STATISTIC_TYPES);
        }
        return divePlaceStatisticTypeField;
    }
    
    private JComboBox getDivePlaceStatisticOrientationField() {
        if (divePlaceStatisticOrientationField == null) {
            divePlaceStatisticOrientationField = new JComboBox(STATISTIC_ORIENTATIONS);
        }
        return divePlaceStatisticOrientationField;
    }

    private JComboBox getCountryStatisticTypeField() {
        if (countryStatisticTypeField == null) {
            countryStatisticTypeField = new JComboBox(STATISTIC_TYPES);
        }
        return countryStatisticTypeField;
    }
    
    private JComboBox getCountryStatisticOrientationField() {
        if (countryStatisticOrientationField == null) {
            countryStatisticOrientationField = new JComboBox(STATISTIC_ORIENTATIONS);
        }
        return countryStatisticOrientationField;
    }

    private JComboBox getDiveTypeStatisticTypeField() {
        if (diveTypeStatisticTypeField == null) {
            diveTypeStatisticTypeField = new JComboBox(STATISTIC_TYPES);
        }
        return diveTypeStatisticTypeField;
    }
    
    private JComboBox getDiveTypeStatisticOrientationField() {
        if (diveTypeStatisticOrientationField == null) {
            diveTypeStatisticOrientationField = new JComboBox(STATISTIC_ORIENTATIONS);
        }
        return diveTypeStatisticOrientationField;
    }

    private JComboBox getDiveActivityStatisticTypeField() {
        if (diveActivityStatisticTypeField == null) {
            diveActivityStatisticTypeField = new JComboBox(STATISTIC_TYPES);
        }
        return diveActivityStatisticTypeField;
    }
    
    private JComboBox getDiveActivityStatisticOrientationField() {
        if (diveActivityStatisticOrientationField == null) {
            diveActivityStatisticOrientationField = new JComboBox(STATISTIC_ORIENTATIONS);
        }
        return diveActivityStatisticOrientationField;
    }

    private JComboBox getWatersStatisticTypeField() {
        if (watersStatisticTypeField == null) {
            watersStatisticTypeField = new JComboBox(STATISTIC_TYPES);
        }
        return watersStatisticTypeField;
    }
    
    private JComboBox getWatersStatisticOrientationField() {
        if (watersStatisticOrientationField == null) {
            watersStatisticOrientationField = new JComboBox(STATISTIC_ORIENTATIONS);
        }
        return watersStatisticOrientationField;
    }

    //
    // inner classes
    //
    
    private class CommandSave implements UndoableCommand {
        
        private StatisticSettings oldSettings;
        private StatisticSettings newSettings;
        
        public void undo() {
            mainWindow.getLogBook().setStatisticSettings(oldSettings);
        }

        public void redo() {
            mainWindow.getLogBook().setStatisticSettings(newSettings);
        }

        public void execute() {
            oldSettings = settings.deepClone();
            newSettings = new StatisticSettings();
            newSettings.getBuddyStatistic().setType(buddyStatisticTypeField.getSelectedItem().toString());
            newSettings.getBuddyStatistic().setOrientation(buddyStatisticOrientationField.getSelectedItem().toString());
            newSettings.getDivePlaceStatistic().setType(divePlaceStatisticTypeField.getSelectedItem().toString());
            newSettings.getDivePlaceStatistic().setOrientation(divePlaceStatisticOrientationField.getSelectedItem().toString());
            newSettings.getCountryStatistic().setType(countryStatisticTypeField.getSelectedItem().toString());
            newSettings.getCountryStatistic().setOrientation(countryStatisticOrientationField.getSelectedItem().toString());
            newSettings.getDiveTypeStatistic().setType(diveTypeStatisticTypeField.getSelectedItem().toString());
            newSettings.getDiveTypeStatistic().setOrientation(diveTypeStatisticOrientationField.getSelectedItem().toString());
            newSettings.getDiveActivityStatistic().setType(diveActivityStatisticTypeField.getSelectedItem().toString());
            newSettings.getDiveActivityStatistic().setOrientation(diveActivityStatisticOrientationField.getSelectedItem().toString());
            newSettings.getWatersStatistic().setType(watersStatisticTypeField.getSelectedItem().toString());
            newSettings.getWatersStatistic().setOrientation(watersStatisticOrientationField.getSelectedItem().toString());
            mainWindow.getLogBook().setStatisticSettings(newSettings);
        }
        
    }

}
