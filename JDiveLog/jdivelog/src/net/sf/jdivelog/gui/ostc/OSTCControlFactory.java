/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: OSTCControlFactory.java
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

import java.awt.Window;

import net.sf.jdivelog.ci.ostc.ColorValue;
import net.sf.jdivelog.ci.ostc.EightBitCustomFunction;
import net.sf.jdivelog.ci.ostc.Feature;
import net.sf.jdivelog.ci.ostc.FifteenBitCustomFunction;
import net.sf.jdivelog.ci.ostc.OSTCMix;
import net.sf.jdivelog.ci.ostc.ProgrammableMix;
import net.sf.jdivelog.ci.ostc.RadioButtonValue;
import net.sf.jdivelog.ci.ostc.ReadonlyInteger;
import net.sf.jdivelog.ci.ostc.TextValue;
import net.sf.jdivelog.model.MixDatabase;

/**
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class OSTCControlFactory {
    
    private static OSTCControlFactory instance = null;
    
    public static OSTCControlFactory getInstance() {
        if (instance == null) {
            instance = new OSTCControlFactory();
        }
        return instance;
    }
    
    private OSTCControlFactory() {
        
    }
    
    public OSTCControl getControl(Window parent, MixDatabase db, Feature feature) {
        if (feature.getDatatype() == ReadonlyInteger.class) {
            return new ReadonlyIntegerControl(feature);
        }
        if (feature.getDatatype() == OSTCMix.class) {
            return new MixControl(parent, db, feature);
        }
        if (feature.getDatatype() == EightBitCustomFunction.class || feature.getDatatype() == FifteenBitCustomFunction.class) {
            return new CustomFunctionControl(feature);
        }
        if (feature.getDatatype() == RadioButtonValue.class) {
            return new RadioButtonControl(feature);
        }
        if (feature.getDatatype() == ProgrammableMix.class) {
            return new ProgrammableMixControl(parent, db, feature);
        }
        if (feature.getDatatype() == TextValue.class) {
            return new TextControl(feature);
        }
        if (feature.getDatatype() == ColorValue.class) {
            return new ColorControl(parent, feature);
        }
        return new DummyControl(feature);
    }

}
