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
package net.sf.jdivelog.model;

import java.math.BigDecimal;

import net.sf.jdivelog.gui.resources.Messages;

public class Mix implements Cloneable, GasFractions {
    
    private static final BigDecimal DEFAULT_STEP_SIZE = new BigDecimal("3.0");
    private static final double DEFAULT_PPO2 = 1.6;
    private final String name;
    private final int oxygen;
    private final int helium;
    private final double ppO2;
    private final int mod;
    private final double change;
    
    public Mix(String name, int oxygen, int helium, double ppO2, int mod, double change) {
        this.name = name;
        this.oxygen = oxygen;
        this.helium = helium;
        this.ppO2 = ppO2;
        this.mod = mod;
        this.change = change;
    }
    
    public Mix(int o2, int he) {
        this(createName(o2, he), o2, he, DEFAULT_PPO2, calcMOD(o2, DEFAULT_PPO2), calcChangeDepth(calcMOD(o2, DEFAULT_PPO2), DEFAULT_STEP_SIZE));
    }

    public Mix(double fo2, double fhe, double mod) {
        this(createName((int)(fo2*100), (int)(fhe*100)), (int)(fo2*100), (int)(fhe*100), calcPpO2((int)(fo2*100), (int)mod), (int)mod, mod);
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("<Mix");
        sb.append(" name=\"");
        sb.append(name);
        sb.append("\" oxygen=\"");
        sb.append(oxygen);
        sb.append("\" helium=\"");
        sb.append(helium);
        sb.append("\" ppO2=\"");
        sb.append(ppO2);
        sb.append("\" mod=\"");
        sb.append(mod);
        sb.append("\" change=\"");
        sb.append(change);
        sb.append("\"/>");
        return sb.toString();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Mix) {
            Mix o = (Mix) obj;
            return oxygen == o.oxygen && helium == o.helium;
        }
        return false;
    }
    
    public double getOxygenFraction() {
        return oxygen / 100.0;
    }
    
    public double getHeliumFraction() {
        return helium / 100.0;
    }
    
    public double getNitrogenFraction() {
        return getNitrogen() / 100.0;
    }
    
    public int getNitrogen() {
        return 100 - oxygen - helium;
    }
    
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the oxygen
     */
    public int getOxygen() {
        return oxygen;
    }

    /**
     * @return the helium
     */
    public int getHelium() {
        return helium;
    }

    /**
     * @return the ppO2
     */
    public double getPpO2() {
        return ppO2;
    }

    /**
     * @return the mod
     */
    public int getMod() {
        return mod;
    }

    /**
     * @return the change
     */
    public double getChange() {
        return change;
    }

    public static double calcChangeDepth(int mod, BigDecimal stepSize) {
        int s = stepSize.scale();
        int factor = 1;
        for (int i=0; i<s; i++) {
            factor = factor * 10;
        }
        double result = (mod * factor / stepSize.unscaledValue().intValue()) / (double)factor * stepSize.unscaledValue().intValue();
        return result;
    }

    public static int calcMOD(int oxygen, double ppO2) {
        int mod = (int) Math.floor(((ppO2 * 100 / oxygen)-1)*10);
        return mod;
    }    

    private static double calcPpO2(int o2, int mod) {
        return (mod/10.0+1)*o2 / 100.0;
    }

    public static String createName(int oxygen, int helium) {
        String name = "";
        if (helium > 0) {
            name = "TX "+oxygen+"/"+helium;
        } else if (oxygen == 100) {
            name = "Oxy 100";
        } else if (oxygen == 21) {
            name = Messages.getString("air");
        } else {
            name = "NX "+oxygen;
        }
        return name;
    }
    
    public static final Mix AIR;
    
    static {
        int o2 = 21;
        int he = 0;
        double ppO2 = 1.6;
        int mod = Mix.calcMOD(o2, ppO2);
        AIR = new Mix(Mix.createName(o2, he), o2, he, ppO2, mod, Mix.calcChangeDepth(mod, DEFAULT_STEP_SIZE));

    }

    public Mix setHeliumFraction(double f) {
        int he = (int)(100*f);
        int o2 = oxygen;
        if (o2 + he > 100) {
            o2 = 100 - he;
        }
        String newname = createName(o2, he);
        int newmod = calcMOD(o2, ppO2);
        double newchange = calcChangeDepth(newmod, DEFAULT_STEP_SIZE);
        Mix newmix = new Mix(newname, o2, he, ppO2, newmod, newchange);
        return newmix;
    }

    public Mix setOxygenFraction(double f) {
        int o2 = (int)(100*f);
        int he = helium;
        if (o2 + he > 100) {
            he = 100 - o2;
        }
        String newname = createName(o2, he);
        int newmod = calcMOD(o2, ppO2);
        double newchange = calcChangeDepth(newmod, DEFAULT_STEP_SIZE);
        Mix newmix = new Mix(newname, o2, he, ppO2, newmod, newchange);
        return newmix;
    }
    
    public Mix setMod(int mod) {
        double newPPO2 = calcPpO2(oxygen, mod);
        double newChange = calcChangeDepth(mod, DEFAULT_STEP_SIZE);
        Mix m = new Mix(name, oxygen, helium, newPPO2, mod, newChange);
        return m;
    }
    
    public Mix setChange(double change) {
        Mix m = new Mix(name, oxygen, helium, ppO2, mod, change);
        return m;
    }

}
