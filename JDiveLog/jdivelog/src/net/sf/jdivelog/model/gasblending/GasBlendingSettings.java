/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: GasBlendingSettings.java
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
package net.sf.jdivelog.model.gasblending;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import net.sf.jdivelog.model.Mix;

/**
 * Settings from Gas Blending
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class GasBlendingSettings {

    private Double tankVolume;

    private Double currentPressure;

    private Double plannedPressure;

    private List<GasSource> gasSources;

    private Mix currentMix;

    private Mix plannedMix;

    public GasBlendingSettings() {
        currentMix = Mix.AIR;
        plannedMix = Mix.AIR;
    }

    /**
     * @return Returns the currentHelium.
     * @deprecated kept for compatibility with older logbook versions
     */
    public Double getCurrentHelium() {
        return currentMix.getHeliumFraction();
    }

    /**
     * @param currentHelium
     *            The currentHelium to set.
     * @deprecated kept for compatibility with older logbook versions
     */
    public void setCurrentHelium(Double currentHelium) {
        int o2 = currentMix.getOxygen();
        int he = currentHelium.intValue();
        String name = Mix.createName(o2, he);
        int mod = Mix.calcMOD(o2, he);
        Mix m = new Mix(name, o2, he, currentMix.getPpO2(), mod, Mix.calcChangeDepth(mod, new BigDecimal("3.0")));
        currentMix = m;
    }

    /**
     * @return Returns the currentOxygen.
     * @deprecated kept for compatibility with older logbook versions
     */
    public Double getCurrentOxygen() {
        return currentMix.getOxygenFraction();
    }

    /**
     * @param currentOxygen
     *            The currentOxygen to set.
     * @deprecated kept for compatibility with older logbook versions
     */
    public void setCurrentOxygen(Double currentOxygen) {
        int o2 = currentOxygen.intValue();
        int he = currentMix.getHelium();
        String name = Mix.createName(o2, he);
        int mod = Mix.calcMOD(o2, he);
        Mix m = new Mix(name, o2, he, currentMix.getPpO2(), mod, Mix.calcChangeDepth(mod, new BigDecimal("3.0")));
        currentMix = m;
    }

    /**
     * @return Returns the currentPressure.
     */
    public Double getCurrentPressure() {
        return currentPressure;
    }

    /**
     * @param currentPressure
     *            The currentPressure to set.
     */
    public void setCurrentPressure(Double currentPressure) {
        this.currentPressure = currentPressure;
    }

    /**
     * @return Returns the gasSources.
     */
    public List<GasSource> getGasSources() {
        if (gasSources == null) {
            gasSources = new ArrayList<GasSource>();
        }
        return gasSources;
    }

    /**
     * @param gasSources
     *            The gasSources to set.
     */
    public void setGasSources(List<GasSource> gasSources) {
        this.gasSources = gasSources;
    }

    /**
     * @param source
     *            the Gas Source to add.
     */
    public void addGasSource(GasSource source) {
        if (gasSources == null) {
            gasSources = new ArrayList<GasSource>();
        }
        gasSources.add(source);
    }

    /**
     * @return Returns the plannedHelium.
     * @deprecated kept for compatibility with older logbook versions
     */
    public Double getPlannedHelium() {
        return plannedMix.getHeliumFraction();
    }

    /**
     * @param plannedHelium
     *            The plannedHelium to set.
     * @deprecated kept for compatibility with older logbook versions
     */
    public void setPlannedHelium(Double plannedHelium) {
        int o2 = plannedMix.getOxygen();
        int he = plannedHelium.intValue();
        String name = Mix.createName(o2, he);
        int mod = Mix.calcMOD(o2, he);
        Mix m = new Mix(name, o2, he, plannedMix.getPpO2(), mod, Mix.calcChangeDepth(mod, new BigDecimal("3.0")));
        plannedMix = m;
    }

    /**
     * @return Returns the plannedOxygen.
     * @deprecated kept for compatibility with older logbook versions
     */
    public Double getPlannedOxygen() {
        return plannedMix.getOxygenFraction();
    }

    /**
     * @param plannedOxygen
     *            The plannedOxygen to set.
     * @deprecated kept for compatibility with older logbook versions
     */
    public void setPlannedOxygen(Double plannedOxygen) {
        int o2 = plannedOxygen.intValue();
        int he = plannedMix.getHelium();
        String name = Mix.createName(o2, he);
        int mod = Mix.calcMOD(o2, he);
        Mix m = new Mix(name, o2, he, plannedMix.getPpO2(), mod, Mix.calcChangeDepth(mod, new BigDecimal("3.0")));
        plannedMix = m;
    }

    /**
     * @return Returns the plannedPressure.
     */
    public Double getPlannedPressure() {
        return plannedPressure;
    }

    /**
     * @param plannedPressure
     *            The plannedPressure to set.
     */
    public void setPlannedPressure(Double plannedPressure) {
        this.plannedPressure = plannedPressure;
    }

    /**
     * @return Returns the tankVolume.
     */
    public Double getTankVolume() {
        return tankVolume;
    }

    /**
     * @param tankVolume
     *            The tankVolume to set.
     */
    public void setTankVolume(Double tankVolume) {
        this.tankVolume = tankVolume;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("<GasBlendingSettings>");
        if (tankVolume != null) {
            sb.append("<tankVolume>");
            sb.append(tankVolume);
            sb.append("</tankVolume>");
        }
        if (currentPressure != null) {
            sb.append("<currentPressure>");
            sb.append(currentPressure);
            sb.append("</currentPressure>");
        }
        sb.append("<current>");
        sb.append(currentMix);
        sb.append("</current>");
        sb.append("<planned>");
        sb.append(plannedMix);
        sb.append("</planned>");
        if (plannedPressure != null) {
            sb.append("<plannedPressure>");
            sb.append(plannedPressure);
            sb.append("</plannedPressure>");
        }
        if (gasSources != null) {
            sb.append("<gasSources>");
            for (GasSource gassource : gasSources) {
                sb.append(gassource.toString());
            }
            sb.append("</gasSources>");
        }
        sb.append("</GasBlendingSettings>");
        return sb.toString();
    }

    public void setCurrentMix(Mix mix) {
        currentMix = mix;
    }
    
    public void setCurrentMix(String name, int oxygen, int helium, double ppO2, int mod, double change) {
        currentMix = new Mix(name, oxygen, helium, ppO2, mod, change);
    }
    
    public Mix getCurrentMix() {
        return currentMix;
    }

    public void setPlannedMix(Mix mix) {
        plannedMix = mix;
    }
    
    public void setPlannedMix(String name, int oxygen, int helium, double ppO2, int mod, double change) {
        plannedMix = new Mix(name, oxygen, helium, ppO2, mod, change);
    }
    
    public Mix getPlannedMix() {
        return plannedMix;
    }

}
