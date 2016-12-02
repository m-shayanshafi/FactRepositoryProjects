/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: ColorControl.java
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
package net.sf.jdivelog.gui.ostc;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import net.sf.jdivelog.ci.OSTCInterface;
import net.sf.jdivelog.ci.ostc.ColorValue;
import net.sf.jdivelog.ci.ostc.Feature;
import net.sf.jdivelog.ci.ostc.UnknownFeatureException;
import net.sf.jdivelog.gui.ColorButton;
import net.sf.jdivelog.gui.resources.Messages;

public class ColorControl extends JPanel implements OSTCControl {

    private static final long serialVersionUID = 1L;

    private final Feature feature;
    private int defaultValue;
    private ColorButton colorButton;
    private JButton resetButton;

    public ColorControl(Window parent, Feature feature) {
        this.feature = feature;
        colorButton = new ColorButton(parent, false);
        resetButton = new JButton();
        resetButton.setText(Messages.getString("default"));
        resetButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                colorButton.setColor(new Color(convert8to24(defaultValue)));
            }
            
        });
        setLayout(new FlowLayout());
        add(colorButton);
        add(resetButton);
    }

    public void load(OSTCInterface ostcInterface) throws UnknownFeatureException {
        Object o = ostcInterface.getFeature(feature);
        if (o instanceof ColorValue) {
            ColorValue cv = (ColorValue) o;
            colorButton.setColor(new Color(convert8to24(cv.currentValue())));
            defaultValue = cv.defaultValue();
        }
    }

    public void save(OSTCInterface ostcInterface) throws UnknownFeatureException {
        int currentValue = convert24to8(colorButton.getColor().getRGB());
        ColorValue cv = new ColorValue(defaultValue, currentValue);
        ostcInterface.setFeature(feature, cv);
    }

    private static int convert24to8(int col24) {
        int[] i = new int[3];
        i[0] = (col24 & 0xFF0000) >> 16;
        i[1] = (col24 & 0xFF00) >> 8;
        i[2] = (col24 & 0xFF);
        float[] f = new float[3];
        f[0] = i[0] / 255.0f;
        f[1] = i[1] / 255.0f;
        f[2] = i[2] / 255.0f;
        i[0] = Math.round(f[0] * 7.0f) << 5;
        i[1] = Math.round(f[1] * 7.0f) << 2;
        i[2] = Math.round(f[2] * 3.0f);
        int col8 = i[0] | i[1] | i[2];
        return col8;
    }
    
    private static int convert8to24(int col8) {
        int r = (col8 & 0xE0) >> 5;
        int g = (col8 & 0x1C) >> 2;
        int b = (col8 & 0x03);
        float[] f = new float[3];
        f[0] = r / 7.0f;
        f[1] = g / 7.0f;
        f[2] = b / 3.0f;
        r = Math.round(f[0] * 255.0f) << 16;
        g = Math.round(f[1] * 255.0f) << 8;
        b = Math.round(f[2] * 255.0f);
        int col24 = r | g | b;
        return col24;
    }
}
