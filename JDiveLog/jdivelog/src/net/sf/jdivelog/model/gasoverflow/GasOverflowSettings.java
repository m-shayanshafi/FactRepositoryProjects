/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: GasBlendingSettings.java
 * 
 * @author Volker Holthaus <v.holthaus@procar.de>
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
package net.sf.jdivelog.model.gasoverflow;

import java.util.ArrayList;
import java.util.List;

public class GasOverflowSettings {
    
	public GasOverflowSettings() {
		super();
		// TODO Auto-generated constructor stub
	}

	private Double startTankSize;

	private Double startTankPressure;

    private Double plannedConsumptionPressure;
    
    private Double minStartPressure;

    private List<GasOverflowSource> gasSources;
    
    public Double getStartTankSize() {
		return startTankSize;
	}

	public void setStartTankSize(Double startTankSize) {
		this.startTankSize = startTankSize;
	}

	public Double getStartTankPressure() {
		return startTankPressure;
	}

	public void setStartTankPressure(Double startTankPressure) {
		this.startTankPressure = startTankPressure;
	}

	public Double getPlannedConsumptionPressure() {
		return plannedConsumptionPressure;
	}

	public void setPlannedConsumptionPressure(Double plannedConsumptionPressure) {
		this.plannedConsumptionPressure = plannedConsumptionPressure;
	}

	public Double getMinStartPressure() {
		return minStartPressure;
	}

	public void setMinStartPressure(Double minStartPressure) {
		this.minStartPressure = minStartPressure;
	}
	
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("<GasOverflowSettings>");
        if (startTankSize != null) {
            sb.append("<startTankSize>");
            sb.append(startTankSize);
            sb.append("</startTankSize>");
        }
        if (startTankPressure != null) {
            sb.append("<startTankPressure>");
            sb.append(startTankPressure);
            sb.append("</startTankPressure>");
        }
        if (plannedConsumptionPressure != null) {
            sb.append("<plannedConsumptionPressure>");
            sb.append(plannedConsumptionPressure);
            sb.append("</plannedConsumptionPressure>");
        }
        if (minStartPressure != null) {
            sb.append("<minStartPressure>");
            sb.append(minStartPressure);
            sb.append("</minStartPressure>");
        }
        if (gasSources != null) {
            sb.append("<gasOverflowSources>");
            for (GasOverflowSource gassource : gasSources) {
                sb.append(gassource.toString());
            }
            sb.append("</gasOverflowSources>");
        }        
        sb.append("</GasOverflowSettings>");
        return sb.toString();
    }

    /**
     * @param source
     *            the Gas Source to add.
     */
    public void addGasSource(GasOverflowSource source) {
        if (gasSources == null) {
            gasSources = new ArrayList<GasOverflowSource>();
        }
        gasSources.add(source);
    }
    /**
     * @return Returns the gasSources.
     */
    public List<GasOverflowSource> getGasSources() {
        if (gasSources == null) {
            gasSources = new ArrayList<GasOverflowSource>();
        }
        return gasSources;
    }
    
    /**
     * @param gasSources
     *            The gasSources to set.
     */
    public void setGasSources(List<GasOverflowSource> gasSources) {
        this.gasSources = gasSources;
    }
    
}
