/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: DecompressionTable.java
 * 
 * @author Volker Holthaus <volker.urlaub@gmx.de>
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
package net.sf.jdivelog.model.decompression;

import java.util.HashMap;

import javax.swing.JPanel;

import net.sf.jdivelog.gui.MainWindow;



public interface DecompressionTable {
    
	/**
     * sets the calculated depth and duration for the decompression calculation of one dive
	 * @param depth 
	 * @param duration 
	 * @param repeat_group 
	 * @param surface_interval 
     * @param o2_ratio (e.g. 21 for air)
     * @param down_dive_speed (e.g. 30 m/min)
     */	
	public void initialize(Integer depth, Integer duration, String repeat_group, Integer surface_interval, Double o2_ratio, Integer down_dive_speed);
	
	/**
     * gets the SeaLevel for the actually dive
	 * @return a map containing the depth as key and the duration as value.
     */	
	public HashMap<Integer,Integer> getDecompressionStops();
	
	/**
     * gets the SurfaceInterval in minutes 
     * @return the surface interval.
     */	
	public Integer getSurfaceInterval();
	
	/**
     * gets the no flight duration in minutes 
     * @return the noflight-time
     */	
	public Integer getNoFlightDuration();
	
	/**
     * gets the Repeatgroup for the actually dive
     * @return ???
     */	
	public String getRepeatGroup();
	
	/**
     * gets the zero Hour for the actually depth and duration
     * @return ???
     */		
	public Integer getZeroHour();

	/**
     * gets the Panel for the Decompressionmodel
	 * @param mainWindow 
     * @return ???
     */	
	public JPanel getConfigurationPanel(MainWindow mainWindow);

}
