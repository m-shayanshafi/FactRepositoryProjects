/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: SimulationControlPanel.java
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

/**
 * User-Interface in an abstract representation.
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public interface SimulationControlPanel {
    
    public void enableStartButtons();
    public void disableStartButtons();
    public void enableStopButton();
    public void disableStopButton();
    public void enableUpButton();
    public void disableUpButton();
    public void enableDownButton();
    public void disableDownButton();
    public void setDepth(long millibar);
    public void setTime(long milliseconds);
    public void addSimulationEventListener(SimulationEventListener listener);
    public void removeSimulationEventListener(SimulationEventListener listener);

}
