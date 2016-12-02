/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: ReadonlyIntegerControl.java
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
import net.sf.jdivelog.ci.ostc.ReadonlyInteger;
import net.sf.jdivelog.ci.ostc.UnknownFeatureException;

/**
 * Control for displaying Readonly Integer values.
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class ReadonlyIntegerControl extends JTextField implements OSTCControl {
    
    private static final long serialVersionUID = -5990988767258883785L;
    private final Feature feature;

    /**
     * @param feature
     */
    public ReadonlyIntegerControl(Feature feature) {
        this.feature = feature;
        setColumns(6);
        setEditable(false);
    }
    
    /**
     * @throws UnknownFeatureException 
     * @see net.sf.jdivelog.gui.ostc.OSTCControl#load(net.sf.jdivelog.ci.OSTCInterface)
     */
    public void load(OSTCInterface ostcInterface) throws UnknownFeatureException {
        Object o = ostcInterface.getFeature(feature);
        if (o instanceof ReadonlyInteger) {
            ReadonlyInteger ri = (ReadonlyInteger) o;
            setText(String.valueOf(ri.intValue()));
        }
    }
    
    /**
     * @see net.sf.jdivelog.gui.ostc.OSTCControl#save(net.sf.jdivelog.ci.OSTCInterface)
     */
    public void save(OSTCInterface ostcInterface) {
        // do nothing, it's readonly!
    }

}
