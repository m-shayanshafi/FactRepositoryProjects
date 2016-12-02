/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: ComputerSettingsPanel.java
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

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;

import javax.swing.InputVerifier;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.sf.jdivelog.ci.ComputerInterface;
import net.sf.jdivelog.ci.DriverManager;
import net.sf.jdivelog.gui.commands.UndoableCommand;
import net.sf.jdivelog.gui.resources.Messages;

public class ComputerSettingsPanel extends AbstractSettingsPanel implements ActionListener {

    private static final long serialVersionUID = 3257567291490055473L;
    
    private JPanel driverPanel = null;
    private JPanel driverSettingsPanel = null;
    private JComboBox driverList = null;
    private String[] drivernames = null;

    private ComputerInterface driver;

    private Properties settings;

    private MainWindow mainWindow;

    private JCheckBox periodicallyDownloadCheckBox;

    private JTextField intervalField;
    
    public ComputerSettingsPanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        drivernames = new String[DriverManager.getInstance().getDriverNames().size()];
        DriverManager.getInstance().getDriverNames().toArray(drivernames);
        this.setLayout(new BorderLayout());
        this.add(getDriverPanel(), BorderLayout.NORTH);
        replaceDriverSettingsPanel();
        load();
        updateIntervalFieldStatus();
    }
    
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == driverList) {
            replaceDriverSettingsPanel();
        }
    }

    private JPanel getDriverPanel() {
        if (driverPanel == null) {
            driverPanel = new JPanel();
            driverPanel.setLayout(new GridBagLayout());
            GridBagConstraints gc = new GridBagConstraints();
            gc.anchor = GridBagConstraints.WEST;
            gc.insets = new Insets(5,5,5,5);
            JLabel driverLabel = new JLabel(Messages.getString("ComputerSettingsPanel.driver")); //$NON-NLS-1$
            gc.gridy = 0;
            gc.gridx = 0;
            driverPanel.add(driverLabel, gc);
            gc.gridx = 1;
            driverPanel.add(getDriverList(), gc);
            gc.gridy++;
            gc.gridx = 0;
            driverPanel.add(new JLabel(Messages.getString("ComputerSettingsPanel.periodically_download_dives")), gc);
            gc.gridx = 1;
            driverPanel.add(getPeriodicallyDownloadCheckBox(), gc);
            gc.gridy++;
            gc.gridx = 0;
            driverPanel.add(new JLabel(Messages.getString("ComputerSettingsPanel.download_interval")), gc);
            gc.gridx = 1;
            driverPanel.add(getIntervalField(), gc);
        }
        return driverPanel;
    }
    
    private JCheckBox getPeriodicallyDownloadCheckBox() {
        if (periodicallyDownloadCheckBox == null) {
            periodicallyDownloadCheckBox = new JCheckBox();
            periodicallyDownloadCheckBox.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    updateIntervalFieldStatus();
                }
                
            });
        }
        return periodicallyDownloadCheckBox;
    }
    
    private void updateIntervalFieldStatus() {
        getIntervalField().setEditable(getPeriodicallyDownloadCheckBox().isSelected());
    }
    
    private JTextField getIntervalField() {
        if (intervalField == null) {
            intervalField = new JTextField(10);
            intervalField.setInputVerifier(new InputVerifier() {

                @Override
                public boolean verify(JComponent input) {
                    boolean result = false;
                    String s = intervalField.getText();
                    if (s != null && !"".equals(s)) {
                        try {
                            int i = Integer.parseInt(s);
                            result = i > 0;
                        } catch (NumberFormatException e) {
                            
                        }
                    }
                    return result;
                }
                
            });
        }
        return intervalField;
    }
    
    private JComboBox getDriverList() {
        if (driverList == null) {
            driverList = new JComboBox(drivernames);
            driverList.addActionListener(this);
        }
        return driverList;
    }
    
    private void replaceDriverSettingsPanel() {
        if (driver == null || (getDriverList().getSelectedItem() != null && !getDriverList().getSelectedItem().equals(driver.getDriverName()))) {
            if (driverSettingsPanel != null) {
                remove(driverSettingsPanel);
                driverSettingsPanel = null;
                driver = null;
            }
            ComputerInterface computerInterface = DriverManager.getInstance().getInterface((String)getDriverList().getSelectedItem());
            if (computerInterface != null) {
                driver = computerInterface;
                driverSettingsPanel = computerInterface.getConfigurationPanel();
                if (settings != null) {
                    driver.initialize(settings);
                }
                add(driverSettingsPanel, BorderLayout.CENTER);
            }
            invalidate();
            revalidate();
            repaint();
        }
        if (driver != null) {
            driver.initialize(settings);
        }
    }
        
    public void load() {
        settings = mainWindow.getLogBook().getComputerSettings();
        getDriverList().setSelectedItem(mainWindow.getLogBook().getComputerDriver());
        int interval = mainWindow.getLogBook().getComputerDownloadInterval();
        if (interval > 0) {
            getPeriodicallyDownloadCheckBox().setSelected(true);
            getIntervalField().setText(String.valueOf(interval));
        } else {
            getPeriodicallyDownloadCheckBox().setSelected(false);
        }
        revalidate();
        replaceDriverSettingsPanel();
    }
    
    public UndoableCommand getSaveCommand() {
        return new CommandSaveComputerSettings();
    }
    
    //
    // inner classes
    //
    
    private class CommandSaveComputerSettings implements UndoableCommand {

        private String newDriver;
        private Properties newSettings;
        private int newInterval;
        private String oldDriver;
        private Properties oldSettings;
        private int oldInterval;
        
        public CommandSaveComputerSettings() {
        }
        
        public void undo() {
            mainWindow.getLogBook().setComputerDriver(oldDriver);
            mainWindow.getLogBook().setComputerSettings(oldSettings);
            mainWindow.getLogBook().setComputerDownloadInterval(oldInterval);
            mainWindow.updateDownloadInterval(oldInterval);
        }

        public void redo() {
            mainWindow.getLogBook().setComputerDriver(newDriver);
            mainWindow.getLogBook().setComputerSettings(newSettings);
            mainWindow.getLogBook().setComputerDownloadInterval(newInterval);
            mainWindow.updateDownloadInterval(newInterval);
        }

        public void execute() {
            oldDriver = mainWindow.getLogBook().getComputerDriver();
            oldSettings = mainWindow.getLogBook().getComputerSettings();
            oldInterval = mainWindow.getLogBook().getComputerDownloadInterval();
            if (getDriverList().getSelectedItem() != null) {
                newDriver = getDriverList().getSelectedItem().toString();
            } else {
                newDriver = oldDriver;
            }
            if (driver != null) {
                newSettings = driver.saveConfiguration();
            } else {
                newSettings = oldSettings;
            }
            newInterval = getPeriodicallyDownloadCheckBox().isSelected() ? Integer.parseInt(getIntervalField().getText()) : 0;
            mainWindow.getLogBook().setComputerDriver(newDriver);
            mainWindow.getLogBook().setComputerSettings(newSettings);
            mainWindow.getLogBook().setComputerDownloadInterval(newInterval);
            mainWindow.updateDownloadInterval(newInterval);
        }

    }

    
}
