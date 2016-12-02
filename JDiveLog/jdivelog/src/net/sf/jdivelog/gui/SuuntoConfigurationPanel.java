/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: SuuntoConfigurationPanel.java
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

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import net.sf.jdivelog.ci.ChecksumException;
import net.sf.jdivelog.ci.CommunicationTimeoutException;
import net.sf.jdivelog.ci.DriverManager;
import net.sf.jdivelog.ci.InvalidConfigurationException;
import net.sf.jdivelog.ci.NotInitializedException;
import net.sf.jdivelog.ci.SuuntoInterface;
import net.sf.jdivelog.comm.PortInUseException;
import net.sf.jdivelog.comm.PortNotFoundException;
import net.sf.jdivelog.comm.UnsupportedCommOperationException;
import net.sf.jdivelog.gui.resources.Messages;
import net.sf.jdivelog.gui.statusbar.StatusBar;
import net.sf.jdivelog.gui.statusbar.StatusInterface;
import net.sf.jdivelog.util.Hexadecimal;
import net.sf.jdivelog.util.UnitConverter;

/**
 * Panel to change the Settings of Suunto Computers.
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class SuuntoConfigurationPanel extends JPanel {

    private static final Logger LOGGER = Logger.getLogger(SuuntoConfigurationPanel.class.getName());
    
    private static final long serialVersionUID = -5324181616195914683L;

    private static final String[] INTERVALS = new String[] { "10s", "20s", "30s", "60s" };

    private static final byte[] INTERVAL_VALUES = { 10, 20, 30, 60 };

    private static final String[] ALTITUDE_RANGES = new String[] { Messages.getString("suuntoConfigurationPanel.altitude_a0"),
            Messages.getString("suuntoConfigurationPanel.altitude_a1"), Messages.getString("suuntoConfigurationPanel.altitude_a2") };

    private static final String[] SAFETY_LEVELS = new String[] { Messages.getString("suuntoConfigurationPanel.safety_p0"),
            Messages.getString("suuntoConfigurationPanel.safety_p1"), Messages.getString("suuntoConfigurationPanel.safety_p2") };

    private static final String[] MODELS = new String[] { Messages.getString("suuntoConfigurationPanel.model_air"),
            Messages.getString("suuntoConfigurationPanel.model_nitrox"), Messages.getString("suuntoConfigurationPanel.model_gauge") };

    private static final byte ADDR_HIGH = 0x0;

    private static final byte ADDR_LOW_COMPUTERMODEL = 0x24;

    private static final byte ADDR_LOW_FIRMWARE = 0x25;

    private static final byte ADDR_LOW_SERIALNUMBER = 0x26;

    private static final byte ADDR_LOW_MAXDEPTH = 0x1e;

    private static final byte ADDR_LOW_NUMBEROFDIVES = 0x22;

    private static final byte ADDR_LOW_PERSONAL_INFORMATION = 0x2c;

    private static final byte ADDR_LOW_INTERVAL = 0x53;

    private static final byte ADDR_LOW_ALTITUDE = 0x54;

    private static final byte ADDR_LOW_MODEL = 0x63;

    private final MainWindow mainWindow;

    private JPanel buttonPanel;

    private JPanel informationPanel;

    private JPanel settingsPanel;

    private JButton readComputerButton;

    private JTextField numberOfDivesField;

    private JTextField maxDepthField;

    private JTextField serialNumberField;

    private JTextField firmwareField;

    private JTextField computerModelField;

    private JTextField personalInformationField;

    private JButton setPersonalInformationButton;

    private JComboBox intervalCombobox;

    private JButton setIntervalButton;

    private JComboBox altitudeCombobox;

    private JComboBox additionalSafetyCombobox;

    private JButton setAltitudeAndSafetyButton;

    private JComboBox modelCombobox;

    private JButton setModelButton;

    private boolean running;

    /**
     * Default Constructor for GUI Builder, do not use!
     */
    @Deprecated
    public SuuntoConfigurationPanel() {
        super();
        this.mainWindow = null;
        initialize();
    }
    
    public SuuntoConfigurationPanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        initialize();
        running = false;
    }

    private void readComputerValues() {
        new ComputerValueReader().start();
    }

    private void setPersonalInformation() {
        TransferRunner runner = new TransferRunner() {

            @Override
            protected void transfer(SuuntoInterface si, StatusInterface status) throws ChecksumException, CommunicationTimeoutException, IOException {
                String name = getPersonalInformationField().getText();
                if (name.length() > 30) {
                    name = name.substring(0, 30);
                }
                if (name.length() < 30) {
                    StringBuilder sb = new StringBuilder(name);
                    int missing = 30-name.length();
                    for (int i=0; i<missing;i++) {
                        sb.append(" ");
                    }
                    name = sb.toString();
                }
                si.writeMemory(status, ADDR_HIGH, ADDR_LOW_PERSONAL_INFORMATION, name.getBytes());
            }
            
        };
        runner.start();
    }

    private void setInterval() {
        TransferRunner runner = new TransferRunner() {

            @Override
            protected void transfer(SuuntoInterface si, StatusInterface status) throws ChecksumException, CommunicationTimeoutException, IOException {
                int idx = getIntervalCombobox().getSelectedIndex();
                byte val = INTERVAL_VALUES[idx];
                si.writeMemory(status, ADDR_HIGH, ADDR_LOW_INTERVAL, new byte[] { val });
            }

        };
        runner.start();
    }

    private void setAltitudeAndSafety() {
        TransferRunner runner = new TransferRunner() {

            @Override
            protected void transfer(SuuntoInterface si, StatusInterface status) throws ChecksumException, CommunicationTimeoutException, IOException {
                byte altAndSafety = (byte) (getAltitudeCombobox().getSelectedIndex() + 3 * getAdditionalSafetyCombobox().getSelectedIndex());
                si.writeMemory(status, ADDR_HIGH, ADDR_LOW_ALTITUDE, new byte[] { altAndSafety });
            }

        };
        runner.start();
    }

    private void setModel() {
        TransferRunner runner = new TransferRunner() {

            @Override
            protected void transfer(SuuntoInterface si, StatusInterface status) throws ChecksumException, CommunicationTimeoutException, IOException {
                si.writeMemory(status, ADDR_HIGH, ADDR_LOW_MODEL, new byte[] { (byte) getModelCombobox().getSelectedIndex() });
            }

        };
        runner.start();
    }

    private void initialize() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(getButtonPanel(), 0);
        add(getInformationPanel(), 1);
        add(getSettingsPanel(), 2);
    }

    private JPanel getButtonPanel() {
        if (buttonPanel == null) {
            buttonPanel = new JPanel();
            buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
            buttonPanel.add(getReadComputerButton());
        }
        return buttonPanel;
    }

    private JButton getReadComputerButton() {
        if (readComputerButton == null) {
            readComputerButton = new JButton(Messages.getString("suuntoConfigurationPanel.read_computer_values"));
            readComputerButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                    readComputerValues();

                }
            });
        }
        return readComputerButton;
    }

    private JPanel getInformationPanel() {
        if (informationPanel == null) {
            informationPanel = new JPanel();
            informationPanel.setLayout(new GridBagLayout());
            GridBagConstraints gc = new GridBagConstraints();
            gc.anchor = GridBagConstraints.FIRST_LINE_START;
            gc.fill = GridBagConstraints.REMAINDER;
            gc.insets = new Insets(3, 3, 3, 3);
            gc.gridx = 0;
            gc.gridy = 0;
            informationPanel.add(new JLabel(Messages.getString("suunto.computermodel")), gc);
            gc.gridx = 1;
            informationPanel.add(getComputerModelField(), gc);
            gc.gridx = 2;
            informationPanel.add(new JLabel(Messages.getString("suuntoConfigurationPanel.firmware")), gc);
            gc.gridx = 3;
            informationPanel.add(getFirmwareField(), gc);
            gc.gridx = 4;
            informationPanel.add(new JLabel(Messages.getString("suuntoConfigurationPanel.serialNumber")), gc);
            gc.gridx = 5;
            informationPanel.add(getSerialNumberField(), gc);
            gc.gridy = 1;
            gc.gridx = 0;
            informationPanel.add(new JLabel(Messages.getString("maxdepth") + " [" + UnitConverter.getDisplayAltitudeUnit() + "]"), gc);
            gc.gridx = 1;
            informationPanel.add(getMaxDepthField(), gc);
            gc.gridx = 2;
            informationPanel.add(new JLabel(Messages.getString("suuntoConfigurationPanel.numberOfDives")), gc);
            gc.gridx = 3;
            informationPanel.add(getNumberOfDivesField(), gc);
            Border border = BorderFactory.createTitledBorder(Messages.getString("suuntoConfigurationPanel.informations"));
            informationPanel.setBorder(border);
        }
        return informationPanel;
    }

    private JTextField getNumberOfDivesField() {
        if (numberOfDivesField == null) {
            numberOfDivesField = new JTextField(4);
            numberOfDivesField.setEditable(false);
        }
        return numberOfDivesField;
    }

    private JTextField getMaxDepthField() {
        if (maxDepthField == null) {
            maxDepthField = new JTextField(4);
            maxDepthField.setEditable(false);
        }
        return maxDepthField;
    }

    private JTextField getSerialNumberField() {
        if (serialNumberField == null) {
            serialNumberField = new JTextField(8);
            serialNumberField.setEditable(false);
        }
        return serialNumberField;
    }

    private JTextField getFirmwareField() {
        if (firmwareField == null) {
            firmwareField = new JTextField(4);
            firmwareField.setEditable(false);
        }
        return firmwareField;
    }

    private JTextField getComputerModelField() {
        if (computerModelField == null) {
            computerModelField = new JTextField(20);
            computerModelField.setEditable(false);
        }
        return computerModelField;
    }

    private JPanel getSettingsPanel() {
        if (settingsPanel == null) {
            settingsPanel = new JPanel();
            settingsPanel.setLayout(new GridBagLayout());
            GridBagConstraints gc = new GridBagConstraints();
            gc.anchor = GridBagConstraints.FIRST_LINE_START;
            gc.fill = GridBagConstraints.BOTH;
            gc.insets = new Insets(3, 3, 3, 3);
            gc.gridy = 0;
            gc.gridx = 0;
            settingsPanel.add(new JLabel(Messages.getString("suuntoConfigurationPanel.divers_name")), gc);
            gc.gridx = 1;
            gc.gridwidth = 3;
            settingsPanel.add(getPersonalInformationField(), gc);
            gc.gridx = 4;
            gc.gridwidth = 1;
            settingsPanel.add(getSetPersonalInformationButton(), gc);

            gc.gridy = 1;
            gc.gridx = 0;
            settingsPanel.add(new JLabel(Messages.getString("suuntoConfigurationPanel.interval")), gc);
            gc.gridx = 1;
            gc.gridwidth = 3;
            settingsPanel.add(getIntervalCombobox(), gc);
            gc.gridx = 4;
            gc.gridwidth = 1;
            settingsPanel.add(getSetIntervalButton(), gc);

            gc.gridy = 2;
            gc.gridx = 0;
            settingsPanel.add(new JLabel(Messages.getString("suuntoConfigurationPanel.altitude")), gc);
            gc.gridx = 1;
            settingsPanel.add(getAltitudeCombobox(), gc);
            gc.gridx = 2;
            settingsPanel.add(new JLabel(Messages.getString("suuntoConfigurationPanel.additional_safety")), gc);
            gc.gridx = 3;
            settingsPanel.add(getAdditionalSafetyCombobox(), gc);
            gc.gridx = 4;
            settingsPanel.add(getSetAltitudeAndSafetyButton(), gc);

            gc.gridy = 3;
            gc.gridx = 0;
            settingsPanel.add(new JLabel(Messages.getString("suuntoConfigurationPanel.model")), gc);
            gc.gridx = 1;
            gc.gridwidth = 3;
            settingsPanel.add(getModelCombobox(), gc);
            gc.gridx = 4;
            gc.gridwidth = 1;
            settingsPanel.add(getSetModelButton(), gc);

            Border border = BorderFactory.createTitledBorder(Messages.getString("suuntoConfigurationPanel.settings"));
            settingsPanel.setBorder(border);
        }
        return settingsPanel;
    }

    private JTextField getPersonalInformationField() {
        if (personalInformationField == null) {
            personalInformationField = new JTextField(30);
            personalInformationField.setColumns(30);
            personalInformationField.setInputVerifier(new InputVerifier() {

                @Override
                public boolean verify(JComponent input) {
                    String text = ((JTextField) input).getText();
                    boolean valid = text.length() <= 30;
                    if (!valid) {
                        ((JTextField) input).setText(text.substring(0, 30));
                    }
                    return true;
                }
            });
        }
        return personalInformationField;
    }

    private JButton getSetPersonalInformationButton() {
        if (setPersonalInformationButton == null) {
            setPersonalInformationButton = new JButton(Messages.getString("suuntoConfigurationPanel.update"));
            setPersonalInformationButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                    setPersonalInformation();
                }

            });
        }
        return setPersonalInformationButton;
    }

    private JComboBox getIntervalCombobox() {
        if (intervalCombobox == null) {
            intervalCombobox = new JComboBox(INTERVALS);
        }
        return intervalCombobox;
    }

    private JButton getSetIntervalButton() {
        if (setIntervalButton == null) {
            setIntervalButton = new JButton(Messages.getString("suuntoConfigurationPanel.update"));
            setIntervalButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                    setInterval();
                }

            });
        }
        return setIntervalButton;
    }

    private JComboBox getAltitudeCombobox() {
        if (altitudeCombobox == null) {
            altitudeCombobox = new JComboBox(ALTITUDE_RANGES);
        }
        return altitudeCombobox;
    }

    private JComboBox getAdditionalSafetyCombobox() {
        if (additionalSafetyCombobox == null) {
            additionalSafetyCombobox = new JComboBox(SAFETY_LEVELS);
        }
        return additionalSafetyCombobox;
    }

    private JButton getSetAltitudeAndSafetyButton() {
        if (setAltitudeAndSafetyButton == null) {
            setAltitudeAndSafetyButton = new JButton(Messages.getString("suuntoConfigurationPanel.update"));
            setAltitudeAndSafetyButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                    setAltitudeAndSafety();
                }

            });
        }
        return setAltitudeAndSafetyButton;
    }

    private JComboBox getModelCombobox() {
        if (modelCombobox == null) {
            modelCombobox = new JComboBox(MODELS);
        }
        return modelCombobox;
    }

    private JButton getSetModelButton() {
        if (setModelButton == null) {
            setModelButton = new JButton(Messages.getString("suuntoConfigurationPanel.update"));
            setModelButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                    setModel();
                }

            });
        }
        return setModelButton;
    }

    private SuuntoInterface getSuuntoInterface() {
        SuuntoInterface si = (SuuntoInterface) DriverManager.getInstance().getInterface(SuuntoInterface.DRIVER_NAME);
        si.initialize(mainWindow.getLogBook().getComputerSettings());
        return si;
    }

    private void errorMessage(String message) {
        new MessageDialog(mainWindow, Messages.getString(message), null, null, MessageDialog.MessageType.ERROR);
    }

    private abstract class TransferRunner extends Thread {

        @Override
        public synchronized void start() {
            if (!running) {
                running = true;
                super.start();
            } else {
                errorMessage("suunto.comport_in_use");
            }
        }

        @Override
        public void run() {
            SuuntoInterface si = getSuuntoInterface();
            StatusBar status = mainWindow.getStatusBar();
            status.messageClear();
            try {
                si.prepareTransfer(status);
                transfer(si, status);
            } catch (NotInitializedException e) {
                errorMessage("suunto.not_initialized");
            } catch (InvalidConfigurationException e) {
                errorMessage(e.getMessage());
            } catch (PortNotFoundException e) {
                errorMessage("suunto.comport_not_found");
            } catch (PortInUseException e) {
                errorMessage("suunto.comport_in_use");
            } catch (UnsupportedCommOperationException e) {
                errorMessage("suunto.could_not_set_comparams");
            } catch (IOException e) {
                errorMessage("suunto.ioexception");
            } catch (ChecksumException e) {
                errorMessage("suunto.checksum_error");
            } catch (CommunicationTimeoutException e) {
                errorMessage("suunto.comm_timeout");
            } finally {
                si.cleanup(status);
                running = false;
                status.messageInfo(Messages.getString("suuntoConfigurationPanel.transfer_finished"));
            }

        }

        protected abstract void transfer(SuuntoInterface si, StatusInterface status) throws ChecksumException, CommunicationTimeoutException, IOException;
    }

    private class ComputerValueReader extends TransferRunner {
        @Override
        protected void transfer(SuuntoInterface si, StatusInterface status) throws ChecksumException, CommunicationTimeoutException, IOException {
            long pause = 400;
            byte[] res = si.readMemory(status, ADDR_HIGH, ADDR_LOW_COMPUTERMODEL, (byte) 0x1);
            if (res.length == 1) {
                String modelname = null;
                for (int i = 0; modelname == null && i < SuuntoInterface.COMPUTERMODEL_IDS.length; i++) {
                    if (SuuntoInterface.COMPUTERMODEL_IDS[i] == res[0]) {
                        modelname = SuuntoInterface.COMPUTERMODEL_NAMES[i];
                    }
                }
                if (modelname == null) {
                    modelname = Hexadecimal.valueOf(res[0], true);
                }
                getComputerModelField().setText(modelname);
            } else {
                LOGGER.severe("length of read computer result should be 1, result: " + Arrays.toString(res));
            }
            
            try {
                sleep(pause);
            } catch (InterruptedException e) {
            }

            res = si.readMemory(status, ADDR_HIGH, ADDR_LOW_FIRMWARE, (byte) 0x1);
            if (res.length == 1) {
                String firmware = String.valueOf(res[0]);
                getFirmwareField().setText(firmware);
            } else {
                LOGGER.severe("length of read firmware result should be 1, result: " + Arrays.toString(res));
            }
            
            try {
                sleep(pause);
            } catch (InterruptedException e) {
            }

            res = si.readMemory(status, ADDR_HIGH, ADDR_LOW_SERIALNUMBER, (byte) 0x4);
            if (res.length == 4) {
                DecimalFormat fm = new DecimalFormat("00");
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < 4; i++) {
                    sb.append(fm.format(res[i]));
                }
                String serialnumber = sb.toString();
                getSerialNumberField().setText(serialnumber);
            } else {
                LOGGER.severe("length of read serialnumber result should be 4, result: " + Arrays.toString(res));
            }
            
            try {
                sleep(pause);
            } catch (InterruptedException e) {
            }

            res = si.readMemory(status, ADDR_HIGH, ADDR_LOW_MAXDEPTH, (byte) 0x2);
            if (res.length == 2) {
                UnitConverter uc = new UnitConverter(UnitConverter.SYSTEM_IMPERIAL, UnitConverter.getDisplaySystem());
                int d = (res[0] * 256 + res[1]) / 128;
                DecimalFormat df = new DecimalFormat("##0.00");
                String depth = df.format(uc.convertAltitude((double) d));
                getMaxDepthField().setText(depth);
            } else {
                LOGGER.severe("length of read max depth result should be 2, result: " + Arrays.toString(res));
            }
            
            try {
                sleep(pause);
            } catch (InterruptedException e) {
            }

            res = si.readMemory(status, ADDR_HIGH, ADDR_LOW_NUMBEROFDIVES, (byte) 0x2);
            if (res.length == 2) {
                int num = res[0] * 256 + res[1];
                String total = String.valueOf(num);
                getNumberOfDivesField().setText(total);
            } else {
                LOGGER.severe("length of read number of dives result should be 2, result: " + Arrays.toString(res));
            }
            
            try {
                sleep(pause);
            } catch (InterruptedException e) {
            }

            res = si.readMemory(status, ADDR_HIGH, ADDR_LOW_PERSONAL_INFORMATION, (byte) 0x1e);
            if (res.length == 30) {
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < res.length; i++) {
                    if (res[i] != 0x0) {
                        sb.append((char) res[i]);
                    }
                }
                String information = sb.toString();
                getPersonalInformationField().setText(information);
            } else {
                LOGGER.severe("length of read personal information should be 30, result: " + Arrays.toString(res));
            }
            
            try {
                sleep(pause);
            } catch (InterruptedException e) {
            }

            res = si.readMemory(status, ADDR_HIGH, ADDR_LOW_INTERVAL, (byte) 0x1);
            if (res.length == 1) {
                for (int i = 0; i < INTERVAL_VALUES.length; i++) {
                    if (res[0] == INTERVAL_VALUES[i]) {
                        getIntervalCombobox().setSelectedIndex(i);
                    }
                }
            } else {
                LOGGER.severe("length of read interval should be 1, result: " + Arrays.toString(res));
            }
            
            try {
                sleep(pause);
            } catch (InterruptedException e) {
            }

            res = si.readMemory(status, ADDR_HIGH, ADDR_LOW_ALTITUDE, (byte) 0x1);
            if (res.length == 1) {
                int alt = res[0] % 3;
                int safety = (res[0] - alt) / 3;
                getAltitudeCombobox().setSelectedIndex(alt);
                getAdditionalSafetyCombobox().setSelectedIndex(safety);
            } else {
                LOGGER.severe("length of read altitude should be 1, result: " + Arrays.toString(res));
            }
            
            try {
                sleep(pause);
            } catch (InterruptedException e) {
            }

            res = si.readMemory(status, ADDR_HIGH, ADDR_LOW_MODEL, (byte) 0x1);
            if (res.length == 1) {
                int model = res[0] & 3;
                getModelCombobox().setSelectedIndex(model);
            } else {
                LOGGER.severe("length of read model should be 1, result: " + Arrays.toString(res));
            }
        }
    }
}
