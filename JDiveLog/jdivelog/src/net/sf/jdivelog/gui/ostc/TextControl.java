/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: TextControl.java
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

import javax.swing.JTextField;

import net.sf.jdivelog.ci.OSTCInterface;
import net.sf.jdivelog.ci.ostc.Feature;
import net.sf.jdivelog.ci.ostc.TextValue;
import net.sf.jdivelog.ci.ostc.UnknownFeatureException;

/**
 * Control for rendering TextValues
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class TextControl extends JTextField implements OSTCControl {
    
    /** */
    private static final long serialVersionUID = 7248317462191753181L;
    
    /** Feature */
    private final Feature feature;

    /**
     * @param feature
     */
    public TextControl(Feature feature) {
        this.feature = feature;
    }

    /**
     * @see net.sf.jdivelog.gui.ostc.OSTCControl#load(net.sf.jdivelog.ci.OSTCInterface)
     */
    public void load(OSTCInterface ostcInterface) throws UnknownFeatureException {
        Object o = ostcInterface.getFeature(feature);
        if (o instanceof TextValue) {
            TextValue tv = (TextValue) o;
            setColumns(tv.getMaxSize());
            setText(String.valueOf(tv.stringValue()));
        }
    }

    /**
     * @see net.sf.jdivelog.gui.ostc.OSTCControl#save(net.sf.jdivelog.ci.OSTCInterface)
     */
    public void save(OSTCInterface ostcInterface) throws UnknownFeatureException {
        TextValue tv = new TextValue(getColumns(), getText());
        ostcInterface.setFeature(feature, tv);
    }

}
