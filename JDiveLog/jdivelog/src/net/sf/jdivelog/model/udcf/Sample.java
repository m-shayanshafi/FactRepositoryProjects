/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: Sample.java
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
package net.sf.jdivelog.model.udcf;

/**
 * Description: Informations taken by the dive computer during the dive, at a specific time t
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public interface Sample {
    
    /** Sample type <b>DELTA</b>. Sampling rate has changed. */
    public static final int TYPE_DELTA=0;
    
    /** Sample type <b>SIWTH</b>. Specifies a gas switch. */
    public static final int TYPE_SWITCH=1;
    
    /** Sample type <b>ALARM</b>. An alam has occured. */
    public static final int TYPE_ALARM=2;
    
    /** Sample type <b>D</b>. Specifies the depth at a given time. */
    public static final int TYPE_DEPTH=3;
    
    /** Sample type <b>T</b>. Specifies the time. */
    public static final int TYPE_TIME = 4;
    
    /** Sample type <b>TEMPERATURE</b>. Specifies the temperature */
    public static final int TYPE_TEMPERATURE = 5;
    
    /** Sample type <b>TEMPERATURE</b>. Specifies the temperature */
    public static final int TYPE_DECO = 6;
    
    /** Sample type <b>PPO2</b>. Specifies the ppO2 3 sensors */
    public static final int TYPE_PPO2 = 7;
    
    /**
     * get the sample type.
     * @return The Sample type.
     */
    public int getType();
    
    /** get the value of the sample.
     * @return Object The value of the sample. The java type depends on the sample type */
    public Object getValue();

}
