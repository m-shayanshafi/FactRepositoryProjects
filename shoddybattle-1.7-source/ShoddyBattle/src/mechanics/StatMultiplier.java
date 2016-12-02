/*
 * StatMultiplier.java
 *
 * Created on December 16, 2006, 9:17 AM
 *
 * This file is a part of Shoddy Battle.
 * Copyright (C) 2006  Colin Fitzpatrick
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, visit the Free Software Foundation, Inc.
 * online at http://gnu.org.
 */

package mechanics;

/**
 * This class represents a stat multiplier. Such a multiplier might be instated
 * because of a move, but hold items and intrinsic abilities do not use this
 * class. They might later, but they don't now.
 * @author Colin
 */
public class StatMultiplier {
    
    /**
     * Multipliers used for statistics.
     */
    private static final double[] m_stats = new double[] {
      4.0, 3.5, 3.0, 2.5, 2.0, 1.5, 1.0, 2.0/3.0, 0.5, 0.4, 1.0/3.0, 2.0/7.0, 0.25  
    };
    
    /**
     * Multipliers used for accuracy and evasion.
     */
    private static final double[] m_acc = new double[] {
      3.0, 8.0/3.0, 7.0/3.0, 2.0, 5.0/3.0, 4.0/3.0, 1.0, 0.75, 0.6, 0.5, 3.0/7.0, 3.0/8.0, 1.0/3.0  
    };
    
    private int m_position = 6; // Centre of the stat multipliers.
    private double[] m_multipliers;
    private double m_secondary = 1.0;
    
    public StatMultiplier() {
        m_multipliers = m_stats;
    }
    
    public StatMultiplier(boolean bAccuracy) {
        m_multipliers = (bAccuracy ? m_acc : m_stats);
    }
    
    public void multiplyBy(double factor) {
        m_secondary *= factor;
    }
    
    public void divideBy(double factor) {
        m_secondary /= factor;
    }
    
    public void setSecondaryMultiplier(double m) {
        m_secondary = m;
    }
    
    public double getSecondaryMultiplier() {
        return m_secondary;
    }
    
    public double getMultiplier() {
        return (m_multipliers[m_position] * m_secondary);
    }
    
    public boolean decreaseMultiplier() {
        if (m_position == (m_multipliers.length - 1)) return false;
        ++m_position;
        return true;
    }
    
    public boolean increaseMultiplier() {
        if (m_position == 0) return false;
        --m_position;
        return true;
    }
    
}
