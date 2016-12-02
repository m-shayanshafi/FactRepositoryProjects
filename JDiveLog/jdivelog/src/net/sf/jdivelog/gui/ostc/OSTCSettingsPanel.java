/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: OSTCSettingsPanel.java
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
package net.sf.jdivelog.gui.ostc;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import net.sf.jdivelog.ci.DriverManager;
import net.sf.jdivelog.ci.InvalidConfigurationException;
import net.sf.jdivelog.ci.NotInitializedException;
import net.sf.jdivelog.ci.OSTCInterface;
import net.sf.jdivelog.ci.TransferException;
import net.sf.jdivelog.ci.ostc.Feature;
import net.sf.jdivelog.ci.ostc.UnknownFeatureException;
import net.sf.jdivelog.gui.JDiveLogException;
import net.sf.jdivelog.gui.MainWindow;
import net.sf.jdivelog.gui.resources.Messages;

/**
 * Panel for configuring the OSTC of HeinrichsWeikamp
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class OSTCSettingsPanel extends JPanel {

    private static final long serialVersionUID = -7014635045681862414L;
    private static final DecimalFormat TEMPERATURE_FORMAT;
    private final MainWindow mainWindow;
    private JPanel dynaPanel;
    private List<Component> components;
    private List<OSTCControl> controls;
    private int line;
    private JPanel buttonPanel;
    private JButton loadButton;
    private JButton saveButton;
    private JButton setTimeButton;
    private JButton resetDecoInformationButton;
    
    static {
        TEMPERATURE_FORMAT = new DecimalFormat("00.0");
        TEMPERATURE_FORMAT.setMaximumFractionDigits(1);
        TEMPERATURE_FORMAT.setMaximumIntegerDigits(3);
    }
    
    /**
     * Default Constructor for GUI Builder, do not use!
     */
    @Deprecated
    public OSTCSettingsPanel() {
        super();
        this.mainWindow = null;
        init();
    }
    
    public OSTCSettingsPanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        init();
    }

    /**
     * 
     */
    private void init() {
        components = new ArrayList<Component>();
        controls = new ArrayList<OSTCControl>();
        line = 0;
        setLayout(new BorderLayout());
        add(getButtonPanel(), BorderLayout.NORTH);
        JScrollPane scrollpane = new JScrollPane(getDynaPanel());
        scrollpane.getVerticalScrollBar().setUnitIncrement(25);
        add(scrollpane, BorderLayout.CENTER);
    }
    
    public void addFeature(String label, OSTCControl ctrl) {
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridy = line++;
        gc.anchor = GridBagConstraints.NORTHWEST;
        gc.insets = new Insets(5, 5, 5, 5);
        gc.gridx = 0;
        JLabel l = new JLabel(label);
        components.add(l);
        getDynaPanel().add(l, gc);
        gc.gridx = 1;
        components.add((Component) ctrl);
        controls.add(ctrl);
        getDynaPanel().add((Component) ctrl, gc);
    }
    
    public void removeAllFeatures() {
        for (Iterator<Component> it = components.iterator(); it.hasNext();) {
            Component c = it.next();
            getDynaPanel().remove(c);
        }
        controls.clear();
        line = 0;
    }
    
    private JPanel getButtonPanel() {
        if (buttonPanel == null) {
            buttonPanel = new JPanel();
            buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
            buttonPanel.add(getLoadButton());
            buttonPanel.add(getSaveButton());
            buttonPanel.add(getSetTimeButton());
            buttonPanel.add(getResetDecoInformationButton());
        }
        return buttonPanel;
    }
    
    private JButton getLoadButton() {
        if (loadButton == null) {
            loadButton = new JButton(Messages.getString("load"));
            loadButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                    try {
                        load();
                    } catch (UnknownFeatureException e) {
                        throw new JDiveLogException(Messages.getString("error.could_not_load"), e.getMessage(), e);
                    } catch (TransferException e) {
                        throw new JDiveLogException(Messages.getString("error.could_not_load"), e.getMessage(), e);
                    } catch (NotInitializedException e) {
                        throw new JDiveLogException(Messages.getString("error.could_not_load"), e.getMessage(), e);
                    } catch (InvalidConfigurationException e) {
                        throw new JDiveLogException(Messages.getString("error.could_not_load"), e.getMessage(), e);
                    }
                }
                
            });
        }
        return loadButton;
    }
    
    private JButton getSaveButton() {
        if (saveButton == null) {
            saveButton = new JButton(Messages.getString("save"));
            saveButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                    try {
                        save();
                    } catch (UnknownFeatureException e) {
                        throw new JDiveLogException(Messages.getString("error.could_not_save"), e.getMessage(), e);
                    } catch (TransferException e) {
                        throw new JDiveLogException(Messages.getString("error.could_not_save"), e.getMessage(), e);
                    } catch (NotInitializedException e) {
                        throw new JDiveLogException(Messages.getString("error.could_not_save"), e.getMessage(), e);
                    }
                }
                
            });
        }
        return saveButton;
    }
    
    private JButton getSetTimeButton() {
        if (setTimeButton == null) {
            setTimeButton = new JButton(Messages.getString("set_time"));
            setTimeButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                    try {
                        setTime();
                    } catch (Exception e) {
                        throw new JDiveLogException(Messages.getString("error.could_not_set_time"), e.getMessage(), e);
                    }
                }
                
            });
        }
        return setTimeButton;
    }
    
    private JButton getResetDecoInformationButton() {
        if (resetDecoInformationButton == null) {
            resetDecoInformationButton = new JButton(Messages.getString("reset_deco_information"));
            resetDecoInformationButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                    try {
                        resetDecoInformation();
                    } catch (Exception e) {
                        throw new JDiveLogException(Messages.getString("error.could_not_reset_deco_information"), e.getMessage(), e);
                    }
                }
                
            });
        }
        return resetDecoInformationButton;
    }
    
    private JPanel getDynaPanel() {
        if (dynaPanel == null) {
            dynaPanel = new JPanel();
            dynaPanel.setLayout(new GridBagLayout());
        }
        return dynaPanel;
    }
    
    private void load() throws UnknownFeatureException, TransferException, NotInitializedException, InvalidConfigurationException {
        OSTCInterface ostcInterface = (OSTCInterface) DriverManager.getInstance().getInterface(OSTCInterface.DRIVER_NAME);
        ostcInterface.initialize(mainWindow.getLogBook().getComputerSettings());
        ostcInterface.transferSettings(mainWindow.getStatusBar());
        removeAllFeatures();
        OSTCControlFactory factory = OSTCControlFactory.getInstance();
        Feature[] features = ostcInterface.getFeatures();
        for (int i=0; i<features.length; i++) {
            Feature feature = features[i];
            OSTCControl ctrl = factory.getControl(mainWindow, mainWindow.getGasDatabase(), feature);
            addFeature(Messages.getString(feature.getLabelKey()), ctrl);
        }
        for (Iterator<OSTCControl> it = controls.iterator(); it.hasNext();) {
            OSTCControl ctrl = it.next();
            ctrl.load(ostcInterface);
        }
        revalidate();
        dynaPanel.revalidate();
    }
    
    private void save() throws UnknownFeatureException, TransferException, NotInitializedException {
        OSTCInterface ostcInterface = (OSTCInterface) DriverManager.getInstance().getInterface(OSTCInterface.DRIVER_NAME);
        for (Iterator<OSTCControl> it = controls.iterator(); it.hasNext();) {
            OSTCControl ctrl = it.next();
            ctrl.save(ostcInterface);
        }
        ostcInterface.save(mainWindow.getStatusBar());
    }
    
    private void setTime() throws InvalidConfigurationException, TransferException, NotInitializedException {
        OSTCInterface ostcInterface = (OSTCInterface) DriverManager.getInstance().getInterface(OSTCInterface.DRIVER_NAME);
        ostcInterface.initialize(mainWindow.getLogBook().getComputerSettings());
        Date time = new Date();
        ostcInterface.setTime(mainWindow.getStatusBar(), time.getTime());
    }
    
    private void resetDecoInformation() throws InvalidConfigurationException, TransferException, NotInitializedException {
        OSTCInterface ostcInterface = (OSTCInterface) DriverManager.getInstance().getInterface(OSTCInterface.DRIVER_NAME);
        ostcInterface.initialize(mainWindow.getLogBook().getComputerSettings());
        ostcInterface.resetDecoInformation(mainWindow.getStatusBar());
    }
    
}
