/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: CustomFunctionControl.java
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

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.text.DecimalFormat;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.NumberFormatter;

import net.sf.jdivelog.ci.OSTCInterface;
import net.sf.jdivelog.ci.ostc.EightBitCustomFunction;
import net.sf.jdivelog.ci.ostc.Feature;
import net.sf.jdivelog.ci.ostc.FifteenBitCustomFunction;
import net.sf.jdivelog.ci.ostc.UnknownFeatureException;
import net.sf.jdivelog.gui.resources.Messages;

/**
 * Control for displaying and editing CustomFunction Values
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class CustomFunctionControl extends JPanel implements OSTCControl {

    private static final long serialVersionUID = 8804420491695981669L;

    private final Feature feature;

    private JTextField currentField;

    private JTextField defaultField;

    public CustomFunctionControl(Feature feature) {
        this.feature = feature;
        initialize();
    }
    
    /**
     * @see net.sf.jdivelog.gui.ostc.OSTCControl#load(net.sf.jdivelog.ci.OSTCInterface)
     */
    public void load(OSTCInterface ostcInterface) throws UnknownFeatureException {
        Object o = ostcInterface.getFeature(feature);
        if (o instanceof EightBitCustomFunction) {
            EightBitCustomFunction c = (EightBitCustomFunction) o;
            getCurrentField().setText(String.valueOf(c.currentValue()));
            getDefaultField().setText(String.valueOf(c.defaultValue()));
        } else if (o instanceof FifteenBitCustomFunction){
            FifteenBitCustomFunction c = (FifteenBitCustomFunction) o;
            getCurrentField().setText(String.valueOf(c.currentValue()));
            getDefaultField().setText(String.valueOf(c.defaultValue()));
        } else {
            // TODO PP: error!
        }
    }

    /**
     * @see net.sf.jdivelog.gui.ostc.OSTCControl#save(net.sf.jdivelog.ci.OSTCInterface)
     */
    public void save(OSTCInterface ostcInterface) throws UnknownFeatureException {
        int cur = Integer.parseInt(getCurrentField().getText());
        int def = Integer.parseInt(getDefaultField().getText());
        if (isEightBit()) {
            EightBitCustomFunction c = new EightBitCustomFunction(def, cur);
            ostcInterface.setFeature(feature, c);
        } else {
            FifteenBitCustomFunction c = new FifteenBitCustomFunction(def, cur);
            ostcInterface.setFeature(feature, c);
        }
    }
    
    private void initialize() {
        setLayout(new FlowLayout());
        add(getCurrentField());
        add(new JLabel(" "+Messages.getString("default")));
        add(getDefaultField());
        String range = "("+getMinValue()+" - "+getMaxValue()+")";
        add(new JLabel(" "+range));
        setMinimumSize(new Dimension(50,20));
    }
    
    private JTextField getCurrentField() {
        if (currentField == null) {
            DecimalFormat df = new DecimalFormat("");
            df.setMaximumFractionDigits(0);
            df.setMaximumIntegerDigits(isEightBit() ? 3 : 5);
            df.setGroupingUsed(false);
            currentField = new JFormattedTextField(new NumberFormatter(df));
            currentField.setColumns(isEightBit() ? 3 : 5);
            currentField.setInputVerifier(new InputVerifier() {

                @Override
                public boolean verify(JComponent comp) {
                    JTextField f = (JTextField) comp;
                    String str = f.getText();
                    try {
                        int num = Integer.parseInt(str);
                        return num >= getMinValue() && num <= getMaxValue();
                    } catch (NumberFormatException e) {
                    }
                    return false;
                }
                
            });
        }
        return currentField;
    }
    
    private JTextField getDefaultField() {
        if (defaultField == null) {
            DecimalFormat df = new DecimalFormat("");
            df.setMaximumFractionDigits(0);
            df.setMaximumIntegerDigits(isEightBit() ? 3 : 5);
            defaultField = new JTextField();
            defaultField.setColumns(isEightBit() ? 3 : 5);
            defaultField.setInputVerifier(new InputVerifier() {

                @Override
                public boolean verify(JComponent comp) {
                    JTextField f = (JTextField) comp;
                    String str = f.getText();
                    try {
                        int num = Integer.parseInt(str);
                        return num >= getMinValue() && num <= getMaxValue();
                    } catch (NumberFormatException e) {
                    }
                    return false;
                }
                
            });
        }
        return defaultField;
    }
    
    private int getMaxValue() {
        int max = isEightBit() ? 0xff : 0x7fff;
        return max;
    }
    
    private int getMinValue() {
        return 0;
    }
    
    private boolean isEightBit() {
        return feature.getDatatype() == EightBitCustomFunction.class;
    }

}
