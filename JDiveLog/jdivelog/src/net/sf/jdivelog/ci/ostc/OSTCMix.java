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
public final class OSTCMix implements OSTCValue {
    
    private final int oxygenPercent;
    private final int heliumPercent;

    public OSTCMix(int oxygenPercent, int heliumPercent) {
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
    }
    
    public int getOxygenPercent() {
        return oxygenPercent;
    }
    
    public int getHeliumPercent() {
        return heliumPercent;
    }

}
