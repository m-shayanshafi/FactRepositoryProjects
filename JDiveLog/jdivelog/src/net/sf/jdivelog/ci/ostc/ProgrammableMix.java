/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: Mix.java
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
package net.sf.jdivelog.ci.ostc;

/**
 * Mix for set on divecomputer
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public final class ProgrammableMix implements OSTCValue {
    
    private final int oxygenPercent;
    private final int heliumPercent;
    private final int changeDepth;
    private final boolean enabled;

    /**
     * @param oxygenPercent Percentage of Oxygen in Mix (9-100)
     * @param heliumPercent Percentage of Helium in Mix (0-100)
     * @param changeDepth Depth to change to this Gas in meters
     * @param enabled if the gas should be considered for deco calculation
     */
    public ProgrammableMix(int oxygenPercent, int heliumPercent, int changeDepth, boolean enabled) {
        if (oxygenPercent < 9 || oxygenPercent > 100) {
            throw new IllegalArgumentException("oxygenPercent is not a valid Percentage value '"+oxygenPercent+"'");
        }
        if (heliumPercent < 0 || heliumPercent > 99) {
            throw new IllegalArgumentException("heliumPercent is not a valid Percentage value '"+heliumPercent+"'");
        }
        if (oxygenPercent + heliumPercent > 100) {
            throw new IllegalArgumentException("oxygen + helium are more than 100%");
        }
        this.oxygenPercent = oxygenPercent;
        this.heliumPercent = heliumPercent;
        this.changeDepth = changeDepth;
        this.enabled = enabled;
    }
    
    public int getOxygenPercent() {
        return oxygenPercent;
    }
    
    public int getHeliumPercent() {
        return heliumPercent;
    }
    
    public int getChangeDepth() {
        return changeDepth;
    }
    
    public boolean isEnabled() {
        return enabled;
    }

}
