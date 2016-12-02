/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: DummyOstcSimulator.java
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

import java.util.logging.Logger;

/**
 * OSTC Simulator for those who have no OSTC...
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class DummyOstcSimulator implements OstcSimulator {
    
    private static final Logger LOGGER = Logger.getLogger(DummyOstcSimulator.class.getName());

    /**
     * @see net.sf.jdivelog.gui.ostc.sim.OstcSimulator#initialize()
     */
    public void initialize() {
        LOGGER.info("OSTC Simulator initialize");
    }

    /**
     * @see net.sf.jdivelog.gui.ostc.sim.OstcSimulator#setDepth(int)
     */
    public void setDepth(long millibar) {
        LOGGER.info("OSTC Simulator writing "+(millibar/1000.0)+"m");
    }

    /**
     * @see net.sf.jdivelog.gui.ostc.sim.OstcSimulator#cleanup()
     */
    public void cleanup() {
        LOGGER.info("OSTC Simulator initialize");
    }

}
