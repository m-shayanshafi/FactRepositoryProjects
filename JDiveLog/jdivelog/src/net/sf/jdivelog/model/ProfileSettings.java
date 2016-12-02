/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: ProfileSettings.java
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

import java.awt.Color;
import java.util.logging.Logger;

public class ProfileSettings {
    
    private static final Logger LOGGER = Logger.getLogger(ProfileSettings.class.getName());
    
    private Color backgroundColor;
    private Color gridColor;
    private Color gridLabelColor;
    private boolean fillDepth;
    
    private boolean showDeco;
    private Color decoColor;
    
    private boolean showTemperature;
    private Color temperatureLabelColor;
    private Color temperatureColorBegin;
    private Color temperatureColorEnd;
    
    private boolean showPpo2;
    private Color ppo2Color1;
    private Color ppo2Color2;
    private Color ppo2Color3;
    private Color ppo2Color4;

    public ProfileSettings() {
        backgroundColor = Color.WHITE;
        gridColor = Color.LIGHT_GRAY;
        gridLabelColor = Color.BLACK;
        fillDepth = false;
        showDeco = true;
        decoColor = new Color(0.7f, 0.1f, 0.0f, 0.2f);
        showTemperature = true;
        temperatureLabelColor = Color.DARK_GRAY;
        temperatureColorBegin = Color.getHSBColor(0.58f, 1.0f, 0.1f);
        temperatureColorBegin = new Color(temperatureColorBegin.getRed(), temperatureColorBegin.getGreen(), temperatureColorBegin.getBlue(), 50);
        temperatureColorEnd = Color.getHSBColor(0.58f, 1.0f, 1.0f);
        temperatureColorEnd = new Color(temperatureColorEnd.getRed(), temperatureColorEnd.getGreen(), temperatureColorEnd.getBlue(), 50);
        showPpo2 = true;
        ppo2Color1 = Color.getHSBColor(0.2f, 0.8f, 1.0f);
        ppo2Color2 = Color.getHSBColor(0.3f, 0.8f, 1.0f);
        ppo2Color3 = Color.getHSBColor(0.4f, 0.8f, 1.0f);
        ppo2Color4 = Color.getHSBColor(0.5f, 0.8f, 1.0f);
    }
    
    /**
     * @return the showDeco
     */
    public boolean isShowDeco() {
        return showDeco;
    }
    /**
     * @param showDeco the showDeco to set
     */
    public void setShowDeco(boolean showDeco) {
        this.showDeco = showDeco;
    }
    public void setShowDeco(String b) {
        this.showDeco = Boolean.TRUE.toString().equals(b);
    }
    /**
     * @return the decoColor
     */
    public Color getDecoColor() {
        return decoColor;
    }
    /**
     * @param decoColor the decoColor to set
     */
    public void setDecoColor(Color decoColor) {
        this.decoColor = decoColor;
    }
    public void setDecoColorCode(String c) {
        this.decoColor = decodeColor(c);
    }
    /**
     * @return the showTemperature
     */
    public boolean isShowTemperature() {
        return showTemperature;
    }
    /**
     * @param showTemperature the showTemperature to set
     */
    public void setShowTemperature(boolean showTemperature) {
        this.showTemperature = showTemperature;
    }
    public void setShowTemperature(String b) {
        this.showTemperature = Boolean.TRUE.toString().equals(b);
    }

    /**
     * @return the temperatureColorBegin
     */
    public Color getTemperatureColorBegin() {
        return temperatureColorBegin;
    }
    /**
     * @param temperatureColorBegin the temperatureColorBegin to set
     */
    public void setTemperatureColorBegin(Color temperatureColorBegin) {
        this.temperatureColorBegin = temperatureColorBegin;
    }
    public void setTemperatureColorCodeBegin(String c) {
        this.temperatureColorBegin = decodeColor(c);
    }
    /**
     * @return the temperatureColorEnd
     */
    public Color getTemperatureColorEnd() {
        return temperatureColorEnd;
    }
    /**
     * @param temperatureColorEnd the temperatureColorEnd to set
     */
    public void setTemperatureColorEnd(Color temperatureColorEnd) {
        this.temperatureColorEnd = temperatureColorEnd;
    }
    public void setTemperatureColorCodeEnd(String c) {
        this.temperatureColorEnd = decodeColor(c);
    }
    /**
     * @return the backgroundColor
     */
    public Color getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * @param backgroundColor the backgroundColor to set
     */
    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
    public void setBackgroundColorCode(String c) {
        this.backgroundColor = decodeColor(c);
    }

    /**
     * @return the gridColor
     */
    public Color getGridColor() {
        return gridColor;
    }

    /**
     * @param gridColor the gridColor to set
     */
    public void setGridColor(Color gridColor) {
        this.gridColor = gridColor;
    }
    public void setGridColorCode(String c) {
        this.gridColor = decodeColor(c);
    }

    /**
     * @return the gridLabelColor
     */
    public Color getGridLabelColor() {
        return gridLabelColor;
    }

    /**
     * @param gridLabelColor the gridLabelColor to set
     */
    public void setGridLabelColor(Color gridLabelColor) {
        this.gridLabelColor = gridLabelColor;
    }
    public void setGridLabelColorCode(String c) {
        this.gridLabelColor = decodeColor(c);
    }

    /**
     * @return the temperatureLabelColor
     */
    public Color getTemperatureLabelColor() {
        return temperatureLabelColor;
    }

    /**
     * @param temperatureLabelColor the temperatureLabelColor to set
     */
    public void setTemperatureLabelColor(Color temperatureLabelColor) {
        this.temperatureLabelColor = temperatureLabelColor;
    }
    public void setTemperatureLabelColorCode(String c) {
        this.temperatureLabelColor = decodeColor(c);
    }

    public void setFillDepth(boolean fillDepth) {
        this.fillDepth = fillDepth;
    }
    public void setFillDepth(String b) {
        this.fillDepth = Boolean.TRUE.toString().equals(b);
    }

    public boolean isFillDepth() {
        return fillDepth;
    }

    /**
     * @return the ppo2Color1
     */
    public Color getPpo2Color1() {
        return ppo2Color1;
    }

    /**
     * @param ppo2Color1 the ppo2Color1 to set
     */
    public void setPpo2Color1(Color ppo2Color1) {
        this.ppo2Color1 = ppo2Color1;
    }

    /**
     * @param ppo2Color1 the ppo2Color1 to set
     */
    public void setPpo2Color1Code(String ppo2Color1) {
        this.ppo2Color1 = decodeColor(ppo2Color1);
    }

    /**
     * @return the ppo2Color2
     */
    public Color getPpo2Color2() {
        return ppo2Color2;
    }

    /**
     * @param ppo2Color2 the ppo2Color2 to set
     */
    public void setPpo2Color2(Color ppo2Color2) {
        this.ppo2Color2 = ppo2Color2;
    }

    /**
     * @param ppo2Color2 the ppo2Color2 to set
     */
    public void setPpo2Color2Code(String ppo2Color2) {
        this.ppo2Color2 = decodeColor(ppo2Color2);
    }

    /**
     * @return the ppo2Color3
     */
    public Color getPpo2Color3() {
        return ppo2Color3;
    }

    /**
     * @param ppo2Color3 the ppo2Color3 to set
     */
    public void setPpo2Color3(Color ppo2Color3) {
        this.ppo2Color3 = ppo2Color3;
    }

    /**
     * @param ppo2Color3 the ppo2Color3 to set
     */
    public void setPpo2Color3Code(String ppo2Color3) {
        this.ppo2Color3 = decodeColor(ppo2Color3);
    }

    /**
     * @return the ppo2Color4
     */
    public Color getPpo2Color4() {
        return ppo2Color4;
    }

    /**
     * @param ppo2Color4 the ppo2Color4 to set
     */
    public void setPpo2Color4(Color ppo2Color4) {
        this.ppo2Color4 = ppo2Color4;
    }

    /**
     * @param ppo2Color4 the ppo2Color4 to set
     */
    public void setPpo2Color4Code(String ppo2Color4) {
        this.ppo2Color4 = decodeColor(ppo2Color4);
    }

    /**
     * @return the showPpo2
     */
    public boolean isShowPpo2() {
        return showPpo2;
    }

    /**
     * @param showPpo2 the showPpo2 to set
     */
    public void setShowPpo2(boolean showPpo2) {
        this.showPpo2 = showPpo2;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("<ProfileSettings>");
        sb.append("<backgroundColorCode>");
        sb.append(encodeColor(backgroundColor));
        sb.append("</backgroundColorCode>");
        sb.append("<gridColorCode>");
        sb.append(encodeColor(gridColor));
        sb.append("</gridColorCode>");
        sb.append("<fillDepth>");
        sb.append(fillDepth);
        sb.append("</fillDepth>");
        sb.append("<showDeco>");
        sb.append(showDeco);
        sb.append("</showDeco>");
        sb.append("<decoColorCode>");
        sb.append(encodeColor(decoColor));
        sb.append("</decoColorCode>");
        sb.append("<showTemperature>");
        sb.append(showTemperature);
        sb.append("</showTemperature>");
        sb.append("<temperatureLabelColorCode>");
        sb.append(encodeColor(temperatureLabelColor));
        sb.append("</temperatureLabelColorCode>");
        sb.append("<temperatureColorCodeBegin>");
        sb.append(encodeColor(temperatureColorBegin));
        sb.append("</temperatureColorCodeBegin>");
        sb.append("<temperatureColorCodeEnd>");
        sb.append(encodeColor(temperatureColorEnd));
        sb.append("</temperatureColorCodeEnd>");
        sb.append("<showPpo2>");
        sb.append(showPpo2);
        sb.append("</showPpo2>");
        sb.append("<ppo2Color1Code>");
        sb.append(encodeColor(ppo2Color1));
        sb.append("</ppo2Color1Code>");
        sb.append("<ppo2Color2Code>");
        sb.append(encodeColor(ppo2Color2));
        sb.append("</ppo2Color2Code>");
        sb.append("<ppo2Color3Code>");
        sb.append(encodeColor(ppo2Color3));
        sb.append("</ppo2Color3Code>");
        sb.append("<ppo2Color4Code>");
        sb.append(encodeColor(ppo2Color4));
        sb.append("</ppo2Color4Code>");
        sb.append("</ProfileSettings>");
        return sb.toString();
    }
    
    private static String encodeColor(Color color) {
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        int a = color.getAlpha();
        StringBuffer result = new StringBuffer();
        result.append("#");
        result.append(toHex(r));
        result.append(toHex(g));
        result.append(toHex(b));
        result.append(toHex(a));
        return result.toString();
    }
    
    private static String toHex(int i) {
        String result = Integer.toHexString(i);
        if (result.length() == 1) {
            result = "0"+result;
        }
        return result;
    }
    
    private static Color decodeColor(String str) {
        Color result = Color.WHITE;
        if (str != null && str.length() == 9) {
            int r = Integer.parseInt(str.substring(1, 3), 16);
            int g = Integer.parseInt(str.substring(3, 5), 16);
            int b = Integer.parseInt(str.substring(5, 7), 16);
            int a = Integer.parseInt(str.substring(7, 9), 16);
            result =  new Color(r, g, b, a);
        } else {
            LOGGER.severe("could not decode string ["+str+"] as color. Using white instead!");
        }
        return result;
    }

}
