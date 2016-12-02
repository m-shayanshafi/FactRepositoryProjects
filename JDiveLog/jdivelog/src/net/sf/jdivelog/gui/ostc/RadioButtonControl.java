/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: RadioButtonControl.java
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

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import net.sf.jdivelog.ci.OSTCInterface;
import net.sf.jdivelog.ci.ostc.Feature;
import net.sf.jdivelog.ci.ostc.RadioButtonValue;
import net.sf.jdivelog.ci.ostc.UnknownFeatureException;
import net.sf.jdivelog.gui.resources.Messages;

/**
 * Description: TODO Change Type description
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class RadioButtonControl extends JPanel implements OSTCControl {

    private static final long serialVersionUID = 3458923959533218563L;
    private final Feature feature;
    private JRadioButton[] radioButtons;
    private int[] values;
    private String[] descriptions;

    public RadioButtonControl(Feature feature) {
        this.feature = feature;
    }
    
    /**
     * @see net.sf.jdivelog.gui.ostc.OSTCControl#load(net.sf.jdivelog.ci.OSTCInterface)
     */
    public void load(OSTCInterface ostcInterface) throws UnknownFeatureException {
        Object o = ostcInterface.getFeature(feature);
        if (o instanceof RadioButtonValue) {
            RadioButtonValue val = (RadioButtonValue) o;
            removeAll();
            values = val.getValues();
            descriptions = val.getDescriptions();
            addRadioButtons();
            for (int i=0; i<values.length; i++) {
                if (values[i] == val.getSelectedValue()) {
                    radioButtons[i].setSelected(true);
                }
            }
        } else {
            // TODO PP: error
        }
    }

    /**
     * @see net.sf.jdivelog.gui.ostc.OSTCControl#save(net.sf.jdivelog.ci.OSTCInterface)
     */
    public void save(OSTCInterface ostcInterface) throws UnknownFeatureException {
        int value = getSelectedValue();
        ostcInterface.setFeature(feature, new RadioButtonValue(value, values, descriptions));
    }
    
    private int getSelectedValue() {
        for (int i=0; i<radioButtons.length; i++) {
            if (radioButtons[i].isSelected()) {
                return values[i];
            }
        }
        throw new IllegalArgumentException("Nothing selected");
    }
    
    private void addRadioButtons() {
        ButtonGroup bg = new ButtonGroup();
        radioButtons = new JRadioButton[values.length];
        for (int i=0; i<radioButtons.length; i++) {
            radioButtons[i] = new JRadioButton(Messages.getString(descriptions[i]));
            bg.add(radioButtons[i]);
            add(radioButtons[i]);
        }
        revalidate();
    }

}
