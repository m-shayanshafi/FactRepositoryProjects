/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: GasSourceEditWindow.java
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
package net.sf.jdivelog.gui.gasblending;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.Border;

import net.sf.jdivelog.gui.MainWindow;
import net.sf.jdivelog.gui.MessageDialog;
import net.sf.jdivelog.gui.MnemonicFactory;
import net.sf.jdivelog.gui.resources.Messages;
import net.sf.jdivelog.model.Mix;
import net.sf.jdivelog.model.gasblending.GasSource;
import net.sf.jdivelog.util.UnitConverter;

/**
 * Window to configure a Gas Source for Blending.
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class GasSourceEditWindow extends JDialog {
    
    private static final long serialVersionUID = 7359866974959354730L;
    private final GasSourceTableModel gasSourceTableModel;
    private final GasSource source;
    private JTextField description;
    private JRadioButton tank;
    private JRadioButton oxygen;
    private JRadioButton helium;
    private JTextField volume;
    private JTextField pressure;
    private JRadioButton compressor;
    private JTextField oxygenPercentage;
    private JTextField maxPressure;
    private ButtonGroup sourceTypeGroup;
    private ButtonGroup gasTypeGroup;
    private JTextField price1;
    private JTextField price2;
    private JPanel generalPanel;
    private JPanel tankPanel;
    private JPanel compressorPanel;
    private JPanel buttonPanel;
    private JButton closeButton;
    private JButton cancelButton;
    private JPanel pricePanel;
    private MainWindow mainWindow;
    
    public GasSourceEditWindow(Window parent, MainWindow mainWindow, GasSourceTableModel gasSourceTableModel) {
    	super(parent, ModalityType.APPLICATION_MODAL);
    	this.mainWindow = mainWindow;
        this.gasSourceTableModel = gasSourceTableModel;
        this.source = null;
        initialize();
        load();
        new MnemonicFactory(this);
    }
    
    public GasSourceEditWindow(Window parent, MainWindow mainWindow, GasSourceTableModel gasSourceTableModel, GasSource source) {
    	super(parent, ModalityType.APPLICATION_MODAL);
    	this.mainWindow = mainWindow;
        this.gasSourceTableModel = gasSourceTableModel;
        this.source = source;
        initialize();
        load();
    }

    private void initialize() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/net/sf/jdivelog/gui/resources/icons/logo.gif")));
        setTitle(Messages.getString("mixing.edit_gas_source")); //$NON-NLS-1$
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(getGeneralPanel());
        panel.add(getTankPanel());
        panel.add(getCompressorPanel());
        panel.add(getPricePanel());
        panel.add(getButtonPanel());
        setContentPane(panel);
        pack();
    }
    
    private void load() {
        if (source == null) {
            // new gas source
            getCompressor().setSelected(false);
            getTank().setSelected(true);
            getOxygen().setSelected(true);
            getHelium().setSelected(false);
            getPressure().setText("200");
            getVolume().setText("50");
            getPrice1().setText("0");
            getPrice2().setText("0");
        } else {
            // load data from existing source
            getDescription().setText(source.getDescription());
            if (source.isCompressor()) {
                getCompressor().setSelected(true);
                getTank().setSelected(false);
                DecimalFormat format = new DecimalFormat("######");
                getOxygenPercentage().setText(format.format(source.getMix().getOxygen()));
                getMaxPressure().setText(format.format(source.getPressure()));
                getOxygen().setSelected(true);
            } else {
                getCompressor().setSelected(false);
                getTank().setSelected(true);
                DecimalFormat format = new DecimalFormat("######");
                if (source.getMix().getOxygen() > 0) {
                    getOxygen().setSelected(true);
                    getHelium().setSelected(false);
                } else {
                    getOxygen().setSelected(false);
                    getHelium().setSelected(true);
                }
                getPressure().setText(format.format(source.getPressure()));
                getVolume().setText(format.format(source.getSize()));
            }
            NumberFormat priceFormat = NumberFormat.getNumberInstance();
            priceFormat.setMaximumFractionDigits(5);
            getPrice1().setText(priceFormat.format(source.getPrice1()));
            getPrice2().setText(priceFormat.format(source.getPrice2()));
        }
    }
    
    public void close() {
        if (save()) {
            dispose();
        }
    }
    
    public void cancel() {
        dispose();
    }
    
    private boolean save() {
        try {
            GasSource s = source;
            if (s == null) {
                s = new GasSource();
            }
            DecimalFormat format = new DecimalFormat("######");
            if (getCompressor().isSelected()) {
                // compressor
                double pressure = format.parse(getMaxPressure().getText()).doubleValue();
                double o2Percentage = format.parse(getOxygenPercentage().getText()).doubleValue();
                Mix mix = new Mix((int)o2Percentage, 0);
                s.setCompressor();
                s.setDescription(getDescription().getText());
                s.setPressure(pressure);
                s.setMix(mix);
            } else {
                // tank
                double pressure = format.parse(getPressure().getText()).doubleValue();
                double volume = format.parse(getVolume().getText()).doubleValue();
                Mix mix = null;
                if (getOxygen().isSelected()) {
                    mix = new Mix(100, 0);
                } else {
                    mix = new Mix(0, 100);
                }
                s.setTank();
                s.setDescription(getDescription().getText());
                s.setPressure(pressure);
                s.setSize(volume);
                s.setMix(mix);
            }
            if (source == null) {
                gasSourceTableModel.addSource(s);
            } else {
                gasSourceTableModel.fireTableDataChanged();
            }
            NumberFormat currencyFormat = NumberFormat.getNumberInstance();
            currencyFormat.setMaximumFractionDigits(5);
            s.setPrice1(currencyFormat.parse(getPrice1().getText()).doubleValue());
            s.setPrice2(currencyFormat.parse(getPrice2().getText()).doubleValue());
        } catch (ParseException e) {
            new MessageDialog(this.mainWindow, Messages.getString("mixing.input_error"), Messages.getString("mixing.not_a_number"), null, MessageDialog.MessageType.ERROR); //$NON-NLS-1$  //$NON-NLS-2$
            return false;
        }
        return true;
    }
    
    private void setCompressorEnabled(boolean enabled) {
        getOxygen().setEnabled(!enabled);
        getHelium().setEnabled(!enabled);
        getPressure().setEnabled(!enabled);
        getVolume().setEnabled(!enabled);
        getOxygenPercentage().setEnabled(enabled);
        getMaxPressure().setEnabled(enabled);
    }
    
    private JPanel getGeneralPanel() {
        if (generalPanel == null) {
            generalPanel = new JPanel();
            generalPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
            JPanel panel = new JPanel();
            panel.setLayout(new GridBagLayout());
            GridBagConstraints gc = new GridBagConstraints();
            gc.anchor = GridBagConstraints.NORTHWEST;
            gc.insets = new Insets(3,3,3,3);
            gc.gridy = 0;
            gc.gridx = 0;
            panel.add(new JLabel(Messages.getString("mixing.gassource.description")), gc); //$NON-NLS-1$
            gc.gridx = 1;
            gc.gridwidth = 2;
            panel.add(getDescription(), gc);
            gc.gridwidth = 1;
            gc.gridy = 1;
            gc.gridx = 0;
            panel.add(new JLabel(Messages.getString("mixing.gassource.type")), gc); //$NON-NLS-1$
            gc.gridx = 1;
            panel.add(getTank(), gc);
            gc.gridx = 2;
            panel.add(getCompressor(), gc);
            generalPanel.add(panel);
            addBorder(Messages.getString("mixing.gassource.general"), generalPanel); //$NON-NLS-1$
        }
        return generalPanel;
    }
    
    private JPanel getTankPanel() {
        if (tankPanel == null) {
            tankPanel = new JPanel();
            tankPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
            JPanel p = new JPanel();
            p.setLayout(new GridBagLayout());
            GridBagConstraints gc = new GridBagConstraints();
            gc.insets = new Insets(3,3,3,3);
            gc.anchor = GridBagConstraints.NORTHWEST;
            gc.gridy = 0;
            gc.gridx = 0;
            p.add(getOxygen(), gc);
            gc.gridx = 1;
            p.add(getHelium(), gc);
            gc.gridy = 1;
            gc.gridx = 0;
            p.add(new JLabel(Messages.getString("mixing.pressure")+" ["+UnitConverter.getDisplayPressureUnit()+"]"), gc); //$NON-NLS-1$ //$NON-NLS-2$  //$NON-NLS-3$
            gc.gridx = 1;
            p.add(getPressure(), gc);
            gc.gridy = 2;
            gc.gridx = 0;
            p.add(new JLabel(Messages.getString("mixing.volume")+" ["+UnitConverter.getDisplayVolumeUnit()+"]"), gc); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            gc.gridx = 1;
            p.add(getVolume(), gc);
            tankPanel.add(p);
            addBorder(Messages.getString("mixing.tank"), tankPanel); //$NON-NLS-1$
        }
        return tankPanel;
    }
    
    private JPanel getCompressorPanel() {
        if (compressorPanel == null) {
            compressorPanel = new JPanel();
            compressorPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
            JPanel p = new JPanel();
            p.setLayout(new GridBagLayout());
            GridBagConstraints gc = new GridBagConstraints();
            gc.insets = new Insets(3,3,3,3);
            gc.anchor = GridBagConstraints.NORTHWEST;
            gc.gridy = 0;
            gc.gridx = 0;
            p.add(new JLabel(Messages.getString("mixing.oxygen_percentage")+" [%]"), gc);  //$NON-NLS-1$ //$NON-NLS-2$
            gc.gridx = 1;
            p.add(getOxygenPercentage(), gc);
            gc.gridy = 1;
            gc.gridx = 0;
            p.add(new JLabel(Messages.getString("mixing.max_pressure")+" ["+UnitConverter.getDisplayPressureUnit()+"]"), gc); //$NON-NLS-1$ //$NON-NLS-2$  //$NON-NLS-3$
            gc.gridx = 1;
            p.add(getMaxPressure(), gc);
            compressorPanel.add(p);
            addBorder(Messages.getString("mixing.compressor"), compressorPanel); //$NON-NLS-1$
        }
        return compressorPanel;
    }
    
    private JPanel getPricePanel() {
        if (pricePanel == null) {
            pricePanel = new JPanel();
            pricePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
            JPanel p = new JPanel();
            p.setLayout(new GridBagLayout());
            GridBagConstraints gc = new GridBagConstraints();
            gc.insets = new Insets(3,3,3,3);
            gc.anchor = GridBagConstraints.NORTHWEST;
            gc.gridy = 0;
            gc.gridx = 0;
            p.add(new JLabel(Messages.getString("mixing.price1")), gc); //$NON-NLS-1$
            gc.gridx = 1;
            p.add(getPrice1(), gc);
            gc.gridy = 1;
            gc.gridx = 0;
            p.add(new JLabel(Messages.getString("mixing.price2")), gc); //$NON-NLS-1$
            gc.gridx = 1;
            p.add(getPrice2(), gc);
            pricePanel.add(p);
            addBorder(Messages.getString("mixing.price"), pricePanel);
        }
        return pricePanel;
    }
    
    private void addBorder(String title, JPanel panel) {
        Border border = BorderFactory.createTitledBorder(title);
        panel.setBorder(border);
    }
    
    private JTextField getDescription() {
        if (description == null) {
            description = new JTextField();
            description.setColumns(20);
        }
        return description;
    }
    
    private JRadioButton getTank() {
        if (tank == null) {
            tank = new JRadioButton(Messages.getString("mixing.tank")); //$NON-NLS-1$
            tank.addItemListener(new ItemListener() {

                public void itemStateChanged(ItemEvent event) {
                    if (tank.isSelected()) {
                        setCompressorEnabled(false);
                    }
                }
                
            });
            getSourceTypeGroup().add(tank);
        }
        return tank;
    }
    
    private JRadioButton getCompressor() {
        if (compressor == null) {
            compressor = new JRadioButton(Messages.getString("mixing.compressor")); //$NON-NLS-1$
            compressor.addItemListener(new ItemListener() {

                public void itemStateChanged(ItemEvent event) {
                    if (compressor.isSelected()) {
                        setCompressorEnabled(true);
                    }
                }
                
            });
            getSourceTypeGroup().add(compressor);
        }
        return compressor;
    }
    
    private ButtonGroup getSourceTypeGroup() {
        if (sourceTypeGroup == null) {
            sourceTypeGroup = new ButtonGroup();
        }
        return sourceTypeGroup;
    }
    
    private JRadioButton getOxygen() {
        if (oxygen == null) {
            oxygen = new JRadioButton(Messages.getString("mixing.oxygen")); //$NON-NLS-1$
            getGasTypeGroup().add(oxygen);
        }
        return oxygen;
    }
    
    private JRadioButton getHelium() {
        if (helium == null) {
            helium = new JRadioButton(Messages.getString("mixing.helium")); //$NON-NLS-1$
            getGasTypeGroup().add(helium);
        }
        return helium;
    }
    
    private ButtonGroup getGasTypeGroup() {
        if (gasTypeGroup == null) {
            gasTypeGroup = new ButtonGroup();
        }
        return gasTypeGroup;
    }
    
    private JTextField getVolume() {
        if (volume == null) {
            volume = new JTextField();
            volume.setColumns(5);
        }
        return volume;
    }
    
    private JTextField getPressure() {
        if (pressure == null) {
            pressure = new JTextField();
            pressure.setColumns(5);
        }
        return pressure;
    }
    
    private JTextField getOxygenPercentage() {
        if (oxygenPercentage == null) {
            oxygenPercentage = new JTextField();
            oxygenPercentage.setColumns(5);
        }
        return oxygenPercentage;
    }
    
    private JTextField getMaxPressure() {
        if (maxPressure == null) {
            maxPressure = new JTextField();
            maxPressure.setColumns(5);
        }
        return maxPressure;
    }
    
    private JTextField getPrice1() {
        if (price1 == null) {
            price1 = new JTextField();
            price1.setColumns(5);
        }
        return price1;
    }
    
    private JTextField getPrice2() {
        if (price2 == null) {
            price2 = new JTextField();
            price2.setColumns(5);
        }
        return price2;
    }

    private JPanel getButtonPanel() {
        if (buttonPanel == null) {
            GridBagConstraints gc = new GridBagConstraints();
            gc.fill = GridBagConstraints.HORIZONTAL;
            gc.weightx = 0.5;
            buttonPanel = new JPanel();
            buttonPanel.setLayout(new GridBagLayout());
            buttonPanel
                    .setComponentOrientation(java.awt.ComponentOrientation.LEFT_TO_RIGHT);
            gc.gridx = 0;
            gc.gridy = 0;
            gc.insets = new java.awt.Insets(5, 100, 5, 5);
            buttonPanel.add(getCloseButton(), gc);
            gc.gridx = 1;
            gc.gridy = 0;
            gc.insets = new java.awt.Insets(5, 5, 5, 100);
            buttonPanel.add(getCancelButton(), gc);
        }
        return buttonPanel;
    }

    private JButton getCloseButton() {
        if (closeButton == null) {
            closeButton = new JButton();
            closeButton.setText(Messages.getString("close")); //$NON-NLS-1$
            closeButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                    close();
                }
                
            });
        }
        return closeButton;
    }

    private JButton getCancelButton() {
        if (cancelButton == null) {
            cancelButton = new JButton();
            cancelButton.setText(Messages.getString("cancel")); //$NON-NLS-1$
            cancelButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                    cancel();
                }
                
            });
        }
        return cancelButton;
    }

}
