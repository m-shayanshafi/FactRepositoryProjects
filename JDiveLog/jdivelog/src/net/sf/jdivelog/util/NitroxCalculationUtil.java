/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: NitroxCalculationUtil.java
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
package net.sf.jdivelog.util;

import java.util.HashMap;

/**
 * Helper class to calculate several Nitrox Specific issues. 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class NitroxCalculationUtil {
    
    private static HashMap<Integer, Double> noaaTableSingleDive;
    private static HashMap<Integer, Double> noaaTableDaily;
    
    static {
        noaaTableSingleDive = new HashMap<Integer, Double>();
        noaaTableSingleDive.put(16, 45.0);
        noaaTableSingleDive.put(15, 120.0);
        noaaTableSingleDive.put(14, 150.0);
        noaaTableSingleDive.put(13, 180.0);
        noaaTableSingleDive.put(12, 210.0);
        noaaTableSingleDive.put(11, 240.0);
        noaaTableSingleDive.put(10, 300.0);
        noaaTableSingleDive.put(9, 360.0);
        noaaTableSingleDive.put(8, 450.0);
        noaaTableSingleDive.put(7, 570.0);
        noaaTableSingleDive.put(6, 720.0);
        noaaTableDaily = new HashMap<Integer, Double>();
        noaaTableDaily.put(16, 150.0);
        noaaTableDaily.put(15, 180.0);
        noaaTableDaily.put(14, 180.0);
        noaaTableDaily.put(13, 210.0);
        noaaTableDaily.put(12, 240.0);
        noaaTableDaily.put(11, 270.0);
        noaaTableDaily.put(10, 300.0);
        noaaTableDaily.put(9, 360.0);
        noaaTableDaily.put(8, 450.0);
        noaaTableDaily.put(7, 570.0);
        noaaTableDaily.put(6, 720.0);
    }

    /**
     * calculates the EAD for a specific depth.
     * @param depth in meters(!)
     * @param o2_ratio percentage of oxygen (e.g. 21.0 for air)
     * @return depth in meters.
     */
    public static Double calculateEAD(Double depth, Double o2_ratio) {
        Double n2_ratio = null;
        if (o2_ratio == null) {
            o2_ratio = 21.0;
        }
        n2_ratio = (100 - o2_ratio) / 100;
        return Math.max((n2_ratio * (depth + 10) / 0.79) - 10, 0);
    }
    
    /**
     * calculates the partial pressure of O2 in a certain depth.
     * @param depth in meters(!)
     * @param o2_ratio percentage of oxygen (e.g. 21.0 for air)
     * @return partial pressure in bar.
     */
    public static Double calculatePPO(Double depth, Double o2_ratio) {
        return ((depth+10)/10)*(o2_ratio/100);
    }
    
    /**
     * calculates the oxygen charge in percentage of the maximum for a dive.
     * @param ppo partial pressure in bar.
     * @param minutes time of exposure in minutes.
     * @return percentage of the maximum exposure for one dive. <code>null</code> if the partial pressure is over 1.6 bar (not in table allowed)
     */
    public static Double getSingleDiveCNS(Double ppo, Double minutes) {
        int ppo2 = new Double(Math.ceil(ppo * 10)).intValue();
        if (ppo2 < 6) {
            return 0.0;
        }
        if (ppo2 > 16) {
            return null;
        }
        Double maximumExposure = noaaTableSingleDive.get(ppo2);
        return 100*minutes/maximumExposure;
    }
    
    /**
     * calculates the oxygen charge in percentage of the maximum per day.
     * @param ppo partial pressure in bar.
     * @param minutes time of exposure in minutes.
     * @return percentage of the maximum exposure per day. <code>null</code> if the partial pressure is over 1.6 bar (not in table allowed)
     */
    public static Double getDailyCNS(Double ppo, Double minutes) {
        int ppo2 = new Double(Math.ceil(ppo * 10)).intValue();
        if (ppo2 < 6) {
            return 0.0;
        }
        if (ppo2 > 16) {
            return null;
        }
        Double maximumExposure = noaaTableDaily.get(ppo2);
        return 100*minutes/maximumExposure;
    }

}
