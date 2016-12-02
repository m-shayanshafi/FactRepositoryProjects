/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: DiveSimulationPanel.java
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
package net.sf.jdivelog.gui.ostc.sim;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.sf.jdivelog.ci.DriverManager;
import net.sf.jdivelog.ci.InvalidConfigurationException;
import net.sf.jdivelog.ci.NotInitializedException;
import net.sf.jdivelog.ci.OSTCInterface;
import net.sf.jdivelog.ci.TransferException;
import net.sf.jdivelog.gui.JDiveLogException;
import net.sf.jdivelog.gui.MainWindow;
import net.sf.jdivelog.gui.ProfileSelectionWindow;
import net.sf.jdivelog.gui.ostc.sim.SimulationEvent.EventType;
import net.sf.jdivelog.gui.resources.Messages;
import net.sf.jdivelog.model.JDive;
import net.sf.jdivelog.util.UnitConverter;

/**
 * Panel for simulation of dives.
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class DiveSimulationPanel extends JPanel implements SimulationControlPanel, KeyListener {

    private static final long serialVersionUID = 434940199086345803L;
    private JPanel topPanel;
    private JPanel rightPanel;
    private JButton startInteractiveButton;
    private JButton replayProfileButton;
    private DiveSimulationCanvas diveSimulationCanvas;
    private JButton stopButton;
    private JTextField timeField;
    private JTextField depthField;
    private JButton upButton;
    private JButton downButton;
    private final ArrayList<SimulationEventListener> simulationEventListeners;
    private final MainWindow mainWindow;
    private JButton emergencyStopButton;
    private JButton resetDecoInformationButton;
    
    /**
     * Default Constructor for GUI Builder, do not use!
     */
    @Deprecated
    public DiveSimulationPanel() {
        super();
        this.mainWindow = null;
        simulationEventListeners = new ArrayList<SimulationEventListener>();
        initialize();
    }
    
    public DiveSimulationPanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        simulationEventListeners = new ArrayList<SimulationEventListener>();
        initialize();
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode)
        {
          case KeyEvent.VK_UP :
            fireSimulationEvent(EventType.UP);
            break;

          case KeyEvent.VK_DOWN :
              fireSimulationEvent(EventType.DOWN);
            break;
        }
    }

    public void keyReleased(KeyEvent e) {
    }

    
    public void enableStartButtons() {
        getStartInteractiveButton().setEnabled(true);
        getReplayProfileButton().setEnabled(true);
    }

    public void disableStartButtons() {
        getStartInteractiveButton().setEnabled(false);
        getReplayProfileButton().setEnabled(false);
    }

    public void enableStopButton() {
        getStopButton().setEnabled(true);
    }

    public void disableStopButton() {
        getStopButton().setEnabled(false);
    }

    public void enableUpButton() {
        getUpButton().setEnabled(true);
    }

    public void disableUpButton() {
        getUpButton().setEnabled(false);
    }

    public void enableDownButton() {
        getDownButton().setEnabled(true);
    }

    public void disableDownButton() {
        getDownButton().setEnabled(false);
    }

    public void setDepth(long millibar) {
        UnitConverter uc = new UnitConverter(UnitConverter.SYSTEM_METRIC, UnitConverter.getDisplaySystem());
        double depth = uc.convertAltitude(millibar/100.0);
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);
        getDepthField().setText(nf.format(depth));
    }

    public void setTime(long milliseconds) {
        long seconds = milliseconds/1000;
        long minutes = seconds/60;
        seconds = seconds%60;
        StringBuffer sb = new StringBuffer();
        sb.append(minutes);
        sb.append(":");
        if (seconds < 10) {
            sb.append("0");
        }
        sb.append(seconds);
        getTimeField().setText(sb.toString());
    }   
    
    public void addSimulationEventListener(SimulationEventListener listener) {
        simulationEventListeners.add(listener);
    }
    
    public void removeSimulationEventListener(SimulationEventListener listener) {
        simulationEventListeners.remove(listener);
    }

    private void initialize() {
        setLayout(new BorderLayout());
        add(getTopPanel(), BorderLayout.NORTH);
        add(getDiveSimulationCanvas(), BorderLayout.CENTER);
        add(getRightPanel(), BorderLayout.EAST);
    }
    
    private JPanel getTopPanel() {
        if (topPanel == null) {
            topPanel = new JPanel();
            topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
            topPanel.add(getStartInteractiveButton());
            topPanel.add(getReplayProfileButton());
            topPanel.add(getResetDecoInformationButton());
            topPanel.add(getEmergencyStopButton());
        }
        return topPanel;
    }
    
    private JPanel getRightPanel() {
        if (rightPanel == null) {
            rightPanel = new JPanel();
            rightPanel.setLayout(new GridBagLayout());
            GridBagConstraints gc = new GridBagConstraints();
            gc.fill = GridBagConstraints.HORIZONTAL;
            gc.insets = new Insets(5,5,5,5);
            gc.gridx = 0;
            gc.gridy = 0;
            rightPanel.add(getStopButton(), gc);
            gc.gridy++;
            rightPanel.add(new JLabel(Messages.getString("time")+":"), gc);
            gc.gridy++;
            rightPanel.add(getTimeField(), gc);
            gc.gridy++;
            rightPanel.add(new JLabel(Messages.getString("depth")+" ["+UnitConverter.getDisplayAltitudeUnit()+"]:"), gc);
            gc.gridy++;
            rightPanel.add(getDepthField(), gc);
            gc.gridy++;
            rightPanel.add(getUpButton(), gc);
            gc.gridy++;
            rightPanel.add(getDownButton(), gc);
        }
        return rightPanel;
    }
    
    private JButton getStartInteractiveButton() {
        if (startInteractiveButton == null) {
            startInteractiveButton = new JButton(Messages.getString("start_interactive"));
            startInteractiveButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    startInteractive();
                }
            });
            startInteractiveButton.addKeyListener(this);
        }
        return startInteractiveButton;
    }
    
    private JButton getReplayProfileButton() {
        if (replayProfileButton == null) {
            replayProfileButton = new JButton(Messages.getString("replay_profile"));
            replayProfileButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    startReplayProfile();
                }
            });
            replayProfileButton.addKeyListener(this);
        }
        return replayProfileButton;
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
    
    private JButton getEmergencyStopButton() {
        if (emergencyStopButton == null) {
            emergencyStopButton = new JButton(Messages.getString("stop"));
            emergencyStopButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    emergencyStop();
                }
            });
        }
        return emergencyStopButton;
    }
    
    private void startInteractive() {
        OSTCInterface ostcInterface = (OSTCInterface) DriverManager.getInstance().getInterface(OSTCInterface.DRIVER_NAME);
        ostcInterface.initialize(mainWindow.getLogBook().getComputerSettings());
        OstcSimulator simulator = ostcInterface.getOstcSimulator(mainWindow.getStatusBar());
        InteractiveSimulationController controller = new InteractiveSimulationController(this, getDiveSimulationCanvas(), simulator);
        controller.start();
        getDiveSimulationCanvas().requestFocus();
    }
    
    private void startReplayProfile() {
        ProfileSelectionWindow psw = new ProfileSelectionWindow(mainWindow, mainWindow.getLogBook(), false);
        psw.setModal(true);
        psw.setVisible(true);
        for (Iterator<JDive>it = psw.getSelectedDives().iterator(); it.hasNext();) {
            JDive dive = it.next();
            OSTCInterface ostcInterface = (OSTCInterface) DriverManager.getInstance().getInterface(OSTCInterface.DRIVER_NAME);
            ostcInterface.initialize(mainWindow.getLogBook().getComputerSettings());
            OstcSimulator simulator = ostcInterface.getOstcSimulator(mainWindow.getStatusBar());
            ReplaySimulationController controller = new ReplaySimulationController(this, getDiveSimulationCanvas(), simulator, dive);
            controller.start();
        }
    }
    
    private void resetDecoInformation() throws InvalidConfigurationException, TransferException, NotInitializedException {
        OSTCInterface ostcInterface = (OSTCInterface) DriverManager.getInstance().getInterface(OSTCInterface.DRIVER_NAME);
        ostcInterface.initialize(mainWindow.getLogBook().getComputerSettings());
        ostcInterface.resetDecoInformation(mainWindow.getStatusBar());
    }
    
    private void emergencyStop() {
        OSTCInterface ostcInterface = (OSTCInterface) DriverManager.getInstance().getInterface(OSTCInterface.DRIVER_NAME);
        ostcInterface.initialize(mainWindow.getLogBook().getComputerSettings());
        try {
            ostcInterface.stopDiveMode(mainWindow.getStatusBar());
        } catch (InvalidConfigurationException e) {
            throw new JDiveLogException(Messages.getString("stop_failed"), null, e);
        }
    }
    
    private DiveSimulationCanvas getDiveSimulationCanvas() {
        if (diveSimulationCanvas == null) {
            diveSimulationCanvas = new DiveSimulationCanvas();
            diveSimulationCanvas.addKeyListener(this);
        }
        return diveSimulationCanvas;
    }
    
    private JButton getStopButton() {
        if (stopButton == null) {
            stopButton = new JButton(Messages.getString("stop"));
            stopButton.setEnabled(false);
            stopButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                    fireSimulationEvent(EventType.STOP);
                }
                
            });
        }
        return stopButton;
    }
    
    private JTextField getTimeField() {
        if (timeField == null) {
            timeField = new JTextField();
            timeField.setEditable(false);
            timeField.addKeyListener(this);
        }
        return timeField;
    }
    
    private JTextField getDepthField() {
        if (depthField == null) {
            depthField = new JTextField();
            depthField.setEditable(false);
            depthField.addKeyListener(this);
        }
        return depthField;
    }
    
    private JButton getUpButton() {
        if (upButton == null) {
            upButton = new JButton(Messages.getString("up"));
            upButton.setEnabled(false);
            upButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                    fireSimulationEvent(EventType.UP);
                }
                
            });
            upButton.addKeyListener(this);
        }
        return upButton;
    }
    
    private JButton getDownButton() {
        if (downButton == null) {
            downButton = new JButton(Messages.getString("down"));
            downButton.setEnabled(false);
            downButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                    fireSimulationEvent(EventType.DOWN);
                }
                
            });
            downButton.addKeyListener(this);
        }
        return downButton;
    }
    
    private void fireSimulationEvent(EventType eventtype) {
        SimulationEvent event = new SimulationEvent(eventtype);
        for (Iterator<SimulationEventListener> it = simulationEventListeners.iterator(); it.hasNext();) {
            SimulationEventListener listener = it.next();
            listener.simulationChanged(event);
        }
    }

}
