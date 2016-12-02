/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: ReplaySimulationController.java
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

import java.io.IOException;

import net.sf.jdivelog.ci.InvalidConfigurationException;
import net.sf.jdivelog.ci.TransferException;
import net.sf.jdivelog.comm.PortInUseException;
import net.sf.jdivelog.comm.PortNotFoundException;
import net.sf.jdivelog.comm.UnsupportedCommOperationException;
import net.sf.jdivelog.gui.JDiveLogException;
import net.sf.jdivelog.gui.JDiveLogExceptionHandler;
import net.sf.jdivelog.gui.ostc.sim.SimulationEvent.EventType;
import net.sf.jdivelog.gui.resources.Messages;

/**
 * Controller to Simulate an exisiting profile.
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class InteractiveSimulationController extends Thread implements SimulationEventListener {
    
    private final static long TIMEOUT = 60000; // 60 secs
    private final SimulationControlPanel controlPanel;
    private final DiveProfileDisplay display;
    private final OstcSimulator simulator;
    private boolean stopPressed;
    private long depth;
    private long zerotimer;

    public InteractiveSimulationController(SimulationControlPanel controlPanel, DiveProfileDisplay display, OstcSimulator simulator) {
        this.controlPanel = controlPanel;
        this.display = display;
        this.simulator = simulator;
    }

    @Override
    public void run() {
        try {
            initialize();
            loop();
            cleanup();
        } catch (Throwable t) {
            JDiveLogExceptionHandler handler = new JDiveLogExceptionHandler();
            handler.handle(t);
            controlPanel.disableStopButton();
            controlPanel.enableStartButtons();
        }
    }

    public void simulationChanged(SimulationEvent event) {
        if (event.getType() == EventType.STOP) {
            stopPressed = true;
        } else if (event.getType() == EventType.UP) {
            synchronized (this) {
                depth -= 10;
                if (depth < 0) {
                    depth = 0;
                }
                updateZeroTimeout();
                controlPanel.setDepth(depth);
            }
        } else if (event.getType() == EventType.DOWN) {
            synchronized (this) {
                depth += 10;
                updateZeroTimeout();
                controlPanel.setDepth(depth);
            }
        }
    }
    
    private void initialize() {
        controlPanel.addSimulationEventListener(this);
        controlPanel.disableStartButtons();
        controlPanel.enableUpButton();
        controlPanel.enableDownButton();
        stopPressed = false;
        controlPanel.enableStopButton();
        display.reset();
        depth = 0;
        zerotimer = 0;
        try {
            simulator.initialize();
        } catch (InvalidConfigurationException e) {
            throw new JDiveLogException(Messages.getString("ostc_simulation_failed"), e.getMessage(), e);
        } catch (PortNotFoundException e) {
            throw new JDiveLogException(Messages.getString("ostc_simulation_failed"), e.getMessage(), e);
        } catch (PortInUseException e) {
            throw new JDiveLogException(Messages.getString("ostc_simulation_failed"), e.getMessage(), e);
        } catch (UnsupportedCommOperationException e) {
            throw new JDiveLogException(Messages.getString("ostc_simulation_failed"), e.getMessage(), e);
        } catch (IOException e) {
            throw new JDiveLogException(Messages.getString("ostc_simulation_failed"), null, e);
        } catch (TransferException e) {
            throw new JDiveLogException(Messages.getString("ostc_simulation_failed"), e.getMessage(), e);
        }
    }
    
    private void updateZeroTimeout() {
        if (depth == 0) {
            if (zerotimer == 0) {
                zerotimer = System.currentTimeMillis();
            }
        } else {
            if (zerotimer != 0) {
                zerotimer = 0;
            }
        }
    }
    
    private boolean zeroTimeout() {
        if (zerotimer != 0) {
            return System.currentTimeMillis() - zerotimer > TIMEOUT;
        }
        return false;
    }
    
    private void loop() {
        long starttime = System.currentTimeMillis();
        boolean finished = false;
        while (!finished) {
            long currentTime = System.currentTimeMillis();
            long delta = currentTime - starttime;
            if (stopPressed || zeroTimeout()) {
                // end of dive...
                finished = true;
                setTimeAndDepth(delta, 0);
            } else {
                setTimeAndDepth(delta, depth);
                try {
                    sleep(200);
                } catch (InterruptedException e) {
                }
            }
        }
    }
    
    private void cleanup() {
        try {
            simulator.cleanup();
        } catch (IOException e) {
            throw new JDiveLogException(Messages.getString("ostc_simulation_failed"), null, e);
        }
        controlPanel.removeSimulationEventListener(this);
        controlPanel.disableStopButton();
        controlPanel.disableUpButton();
        controlPanel.disableDownButton();
        controlPanel.enableStartButtons();
    }
    
    private void setTimeAndDepth(long millis, long depth) {
        controlPanel.setTime(millis);
        controlPanel.setDepth(depth);
        display.setDiverLocation(millis, depth);
        try {
            simulator.setDepth(depth);
        } catch (IOException e) {
            throw new JDiveLogException(Messages.getString("ostc_simulation_failed"), null, e);
        }
    }
}
