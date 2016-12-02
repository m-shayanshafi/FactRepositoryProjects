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
import java.util.Iterator;
import java.util.TreeSet;

import net.sf.jdivelog.ci.InvalidConfigurationException;
import net.sf.jdivelog.ci.TransferException;
import net.sf.jdivelog.comm.PortInUseException;
import net.sf.jdivelog.comm.PortNotFoundException;
import net.sf.jdivelog.comm.UnsupportedCommOperationException;
import net.sf.jdivelog.gui.JDiveLogException;
import net.sf.jdivelog.gui.JDiveLogExceptionHandler;
import net.sf.jdivelog.gui.ostc.sim.SimulationEvent.EventType;
import net.sf.jdivelog.gui.resources.Messages;
import net.sf.jdivelog.model.JDive;
import net.sf.jdivelog.model.udcf.Delta;
import net.sf.jdivelog.model.udcf.Depth;
import net.sf.jdivelog.model.udcf.Dive;
import net.sf.jdivelog.model.udcf.Sample;
import net.sf.jdivelog.model.udcf.Time;
import net.sf.jdivelog.util.UnitConverter;

/**
 * Controller to Simulate an exisiting profile.
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class ReplaySimulationController extends Thread implements SimulationEventListener {
    
    private final SimulationControlPanel controlPanel;
    private final DiveProfileDisplay display;
    private final OstcSimulator simulator;
    private final JDive dive;
    private final long duration;
    private final TreeSet<TimeDepth> samples;
    private boolean stopPressed;

    public ReplaySimulationController(SimulationControlPanel controlPanel, DiveProfileDisplay display, OstcSimulator simulator, JDive dive) {
        this.controlPanel = controlPanel;
        this.display = display;
        this.simulator = simulator;
        this.dive = dive;
        UnitConverter uc = new UnitConverter(UnitConverter.getSystem(dive.getUnits()), UnitConverter.SYSTEM_SI);
        this.duration = 1000 * uc.convertTime(dive.getDuration()).longValue();
        this.samples = new TreeSet<TimeDepth>();
        parseDive();
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
        }
        // ignore all other events (no interactive control)
    }
    
    private void initialize() {
        controlPanel.addSimulationEventListener(this);
        controlPanel.disableStartButtons();
        controlPanel.disableUpButton();
        controlPanel.disableDownButton();
        stopPressed = false;
        controlPanel.enableStopButton();
        display.reset();
        for (Iterator<TimeDepth> it = samples.iterator(); it.hasNext(); ) {
            TimeDepth td = it.next();
            display.addProfilePoint(td.time, td.depth);
        }
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
    
    private void loop() {
        long starttime = System.currentTimeMillis();
        boolean finished = false;
        while (!finished) {
            long currentTime = System.currentTimeMillis();
            long delta = currentTime - starttime;
            if (stopPressed || delta > duration) {
                // end of dive...
                finished = true;
                setTimeAndDepth(delta, 0);
            } else {
                long depth = getDepth(delta);
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
    
    private long getDepth(long millis) {
        if (millis > duration) {
            return 0;
        }
        TimeDepth before = new TimeDepth(0,0);
        TimeDepth after = null; 
        for (Iterator<TimeDepth> it = samples.iterator(); after == null && it.hasNext();) {
            TimeDepth td = it.next();
            if (td.time < millis) {
                before = td;
            } else {
                after = td;
            }
        }
        if (after == null) {
             // should not occur, but you never know...
            return 0;
        }
        if (after.time == millis) {
            // perfect match
            return after.depth;
        }
        return weightedAverage(millis, before, after);
    }
    
    private long weightedAverage(long millis, TimeDepth before, TimeDepth after) {
        long diffbefore = millis - before.time;
        long diffafter = after.time - millis;
        long avg = (diffbefore * after.depth + diffafter * before.depth) / (diffbefore + diffafter);
        return avg;
    }
    
    private void parseDive() {
        if (dive.getDive().getMode() == Dive.MODE_DELTA) {
            UnitConverter c = new UnitConverter(UnitConverter.getSystem(this.dive.getUnits()), UnitConverter.SYSTEM_SI);
            int interval = 0;
            int time = 0;
            for (Iterator<Sample> it = dive.getDive().getSamples().iterator(); it.hasNext(); ) {
                Sample sample = it.next();
                if (sample.getType() == Sample.TYPE_DELTA) {
                    Delta delta = (Delta) sample;
                    interval = c.convertTime(delta.getValue()).intValue();
                } else if (sample.getType() == Sample.TYPE_DEPTH) {
                    time += interval;
                    Depth sdepth = (Depth) sample;
                    int depth = (int)(c.convertAltitude(sdepth.getValue()) * 100);
                    TimeDepth td = new TimeDepth(time*1000, depth);
                    samples.add(td);
                }
            }
        } else {
            UnitConverter c = new UnitConverter(UnitConverter.getSystem(this.dive.getUnits()), UnitConverter.SYSTEM_SI);
            int time = 0;
            for (Iterator<Sample> it = dive.getDive().getSamples().iterator(); it.hasNext(); ) {
                Sample sample = it.next();
                if (sample.getType() == Sample.TYPE_TIME) {
                    Time stime = (Time) sample;
                    time = (int)(c.convertTime(stime.getValue())*1000);
                } else if (sample.getType() == Sample.TYPE_DEPTH) {
                    Depth sdepth = (Depth) sample;
                    int depth = (int)(c.convertAltitude(sdepth.getValue()) * 100);
                    TimeDepth td = new TimeDepth(time, depth);
                    samples.add(td);
                }
            }
        }
    }

    private static class TimeDepth implements Comparable<TimeDepth> {
        
        private final int time;
        private final int depth;
        
        private TimeDepth(int time, int depth) {
            this.time = time;
            this.depth = depth;
        }

        public int compareTo(TimeDepth other) {
            if (time == other.time) {
                return 0;
            }
            if (time < other.time) {
                return -1;
            }
            return 1;
        }
        
        @Override
        public String toString() {
            return "t:"+time+", d:"+depth;
        }
        
    }
    
}
