/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: MixControl.java
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
import java.awt.Window;

import javax.swing.JPanel;
import javax.swing.JTextField;

import net.sf.jdivelog.ci.OSTCInterface;
import net.sf.jdivelog.ci.ostc.Feature;
import net.sf.jdivelog.ci.ostc.OSTCMix;
import net.sf.jdivelog.ci.ostc.UnknownFeatureException;
import net.sf.jdivelog.gui.MixField;
import net.sf.jdivelog.model.MixDatabase;
import net.sf.jdivelog.model.Mix;

/**
 * Control for displaying and editing Mixes.
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class MixControl extends JPanel implements OSTCControl {
    
    private static final long serialVersionUID = -2718209326395053769L;
    private final Window parent;
    private final MixDatabase db;
    private final Feature feature;
    private JTextField descriptionField;
    private MixField mixField;

    public MixControl(Window parent, MixDatabase db, Feature feature) {
        this.parent = parent;
        this.db = db;
        this.feature = feature;
        initialize();
    }
    
    public void load(OSTCInterface ostcInterface) throws UnknownFeatureException {
        Object o = ostcInterface.getFeature(feature);
        if (o instanceof OSTCMix) {
            OSTCMix m = (OSTCMix) o;
            Mix mix = new Mix(m.getOxygenPercent(), m.getHeliumPercent());
            getMixField().setMix(mix);
        } else {
            // TODO PP: error!
        }
    }
    
    public void save(OSTCInterface ostcInterface) throws UnknownFeatureException {
        Mix mix = getMixField().getMix();
        if (mix != null) {
            OSTCMix m = new OSTCMix(mix.getOxygen(), mix.getHelium());
            ostcInterface.setFeature(feature, m);
        }
    }
    
    private void initialize() {
        setLayout(new FlowLayout());
        add(getMixField());
        setMinimumSize(new Dimension(50,20));
    }
    
    private MixField getMixField() {
        if (mixField == null) {
            mixField = new MixField(parent, db);
        }
        return mixField;
    }
}
