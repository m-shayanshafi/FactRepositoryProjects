/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: GasSource.java
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

import net.sf.jdivelog.model.Mix;

/**
 * Represents a Tank or Compressor in the pool
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class GasSource {
    
    private String description;
    
    private Mix mix;
    
    private double pressure;
    
    private boolean compressor;
    
    private double size;
    
    private boolean enabled;
    
    private double price1;
    
    private double price2;
    
    public GasSource() {
        enabled = true;
        price1 = 0.0;
        price2 = 0.0;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Mix getMix() {
        return mix;
    }

    public void setMix(Mix mix) {
        this.mix = mix;
    }
    
    public void setMix(String name, int oxygen, int helium, double ppO2, int mod, double change) {
        mix = new Mix(name, oxygen, helium, ppO2, mod, change);
    }
    
    /**
     * Method still needed for loading from older files!
     * @param fo2
     * @param fhe
     */
    @Deprecated
    public void setMix(double fo2, double fhe) {
        if (fo2 != 0 || fhe != 0) {
            double factor = 100.0;
            if (fo2+fhe > 1.0) {
                factor = 1.0;
            }
            int o2 = (int) (factor*fo2);
            int he = (int) (factor*fhe);
            mix = new Mix(o2, he);
        }
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }
    
    public boolean isCompressor() {
        return compressor;
    }
    
    public void setCompressor() {
        compressor = true;
    }
    
    public void setCompressor(boolean c) {
        compressor = c;
    }
    
    public void setTank() {
        compressor = false;
    }
    
    public double getSize() {
        return compressor ? 0 : size;
    }
    
    public void setSize(double size) {
        this.size = size;
    }
    
    public boolean isEnabled() {
        return enabled;
    }
    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    /**
     * returns the maximum amount of gas (delta of pressure) which can be served out of this tank.
     * @param tankSize Volume of the Tank to fill
     * @param startPressure Pressure of the Tank to Fill
     * @return delta of pressure.
     */
    public double getMaxDifference(double tankSize, double startPressure) {
        if (compressor) {
            return pressure - startPressure;
        }
        double poolAmount = size * pressure;
        double tankAmount = tankSize * startPressure;
        double endPressure = (poolAmount + tankAmount) / (size + tankSize);
        return endPressure - startPressure;
        
    }

    public void addVolume(double vol) {
        double currentVol = getSize() * getPressure();
        double newVol = currentVol + vol;
        setPressure(newVol / getSize());
    }
    
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("<GasSource>");
        if (enabled) {
            sb.append("<enabled>true</enabled>");
        } else {
            sb.append("<enabled>false</enabled>");
        }
        if (description != null) {
            sb.append("<description>");
            sb.append(description);
            sb.append("</description>");
        }
        if (mix != null) {
            sb.append(mix);
        }
        sb.append("<pressure>");
        sb.append(pressure);
        sb.append("</pressure>");
        sb.append("<compressor>");
        sb.append(compressor);
        sb.append("</compressor>");
        sb.append("<size>");
        sb.append(size);
        sb.append("</size>");
        sb.append("<price1>");
        sb.append(price1);
        sb.append("</price1>");
        sb.append("<price2>");
        sb.append(price2);
        sb.append("</price2>");
        sb.append("</GasSource>");
        return sb.toString();
    }

    /**
     * @return Returns the price1.
     */
    public double getPrice1() {
        return price1;
    }

    /**
     * @param price1 The price1 to set.
     */
    public void setPrice1(double price1) {
        this.price1 = price1;
    }

    /**
     * @return Returns the price2.
     */
    public double getPrice2() {
        return price2;
    }

    /**
     * @param price2 The price2 to set.
     */
    public void setPrice2(double price2) {
        this.price2 = price2;
    }

}
