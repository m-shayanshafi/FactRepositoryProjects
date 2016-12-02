/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: GasTank.java
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
package net.sf.jdivelog.gui.gasblending;

import java.math.BigDecimal;

import net.sf.jdivelog.model.Mix;

/**
 * Represents a tank to fill.
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class GasTank implements Cloneable {
    
    private double tankSize;
    private Mix mix;
    private double pressure;

    public GasTank(double tankSize, Mix mix, double pressure) {
        this.tankSize = tankSize;
        this.mix = mix;
        this.pressure = pressure;
    }
    
    public double getTankSize() {
        return tankSize;
    }
    
    public Mix getMix() {
        return mix;
    }
    
    public double getPressure() {
        return pressure;
    }
    
    public void fill(Mix fillMix, double pressureDiff) {
        double newO2 = mix.getOxygenFraction()*pressure + fillMix.getOxygenFraction()*pressureDiff;
        double newHe = mix.getHeliumFraction()*pressure + fillMix.getHeliumFraction()*pressureDiff;
        double newN2 = mix.getNitrogenFraction()*pressure + fillMix.getNitrogenFraction()*pressureDiff;
        double total = newO2 + newHe + newN2;
        int o2 = (int) Math.round(100*newO2 / total);
        int he = (int) Math.round(100*newHe / total);
        int mod = Mix.calcMOD(o2, mix.getPpO2());
        String name = Mix.createName(o2, he);
        double change = Mix.calcChangeDepth(mod, new BigDecimal(3));
        mix = new Mix(name, o2, he, mix.getPpO2(), mod, change);
        pressure += pressureDiff;
    }
    
    @Override
    public GasTank clone() {
        return new GasTank(tankSize, mix, pressure);
    }

}
