/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: ColorShadingUtil.java
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
package net.sf.jdivelog.gui;

import java.awt.Color;

public class ColorShadingUtil {
    
    public static Color calculate(double value, double min, double max, Color begin, Color end) {
        /* If min and max is the same temperature, just return end color */
        if (min == max)
            return end;
        float[] hsbBegin = Color.RGBtoHSB(begin.getRed(), begin.getGreen(), begin.getBlue(), null);
        float[] hsbEnd = Color.RGBtoHSB(end.getRed(), end.getGreen(), end.getBlue(), null);
        int alphaBegin = begin.getAlpha();
        int alphaEnd = end.getAlpha();
        float h = calculate(value, min, max, hsbBegin[0], hsbEnd[0]);
        float s = calculate(value, min, max, hsbBegin[1], hsbEnd[1]);
        float b = calculate(value, min, max, hsbBegin[2], hsbEnd[2]);
        int a = calculate(value, min, max, alphaBegin, alphaEnd);
        Color hsbc = Color.getHSBColor(h, s, b);
//        return hsbc;
        Color result = new Color(hsbc.getRed(), hsbc.getGreen(), hsbc.getBlue(), a);
        return result;
    }
    
    private static float calculate(double value, double min, double max, float begin, float end) {
        double percent = (value-min) / (max - min);
        float diff = end - begin;
        float result = begin + (float)(diff*percent);
        return result;
    }
    
    private static int calculate(double value, double min, double max, int begin, int end) {
        double percent = (value-min) / (max - min);
        float diff = end - begin;
        int result = begin + (int)(diff*percent);
        return result;
    }

}
