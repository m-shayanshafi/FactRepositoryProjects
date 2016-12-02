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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Window;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import net.sf.jdivelog.ci.OSTCInterface;
import net.sf.jdivelog.ci.ostc.Feature;
import net.sf.jdivelog.ci.ostc.ProgrammableMix;
import net.sf.jdivelog.ci.ostc.UnknownFeatureException;
import net.sf.jdivelog.gui.MixField;
import net.sf.jdivelog.model.MixDatabase;
import net.sf.jdivelog.model.Mix;

/**
 * Control for displaying and editing Mixes.
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class ProgrammableMixControl extends JPanel implements OSTCControl {
    
    private static final long serialVersionUID = -2718209326395053769L;
    private final Feature feature;
    private JCheckBox enabledCheckBox;
    private MixField mixField;
    private final Window parent;
    private final MixDatabase db;

    public ProgrammableMixControl(Window parent, MixDatabase db, Feature feature) {
        this.parent = parent;
        this.db = db;
        this.feature = feature;
        initialize();
    }
    
    public void load(OSTCInterface ostcInterface) throws UnknownFeatureException {
        Object o = ostcInterface.getFeature(feature);
        if (o instanceof ProgrammableMix) {
            ProgrammableMix m = (ProgrammableMix) o;
            int o2 = m.getOxygenPercent();
            int he = m.getHeliumPercent();
            int cd = m.getChangeDepth();
            String name = Mix.createName(o2, he);
            double ppO2 = ((cd/10.0)+1)*o2/100.0;
            Mix mix = new Mix(name, o2, he, ppO2, cd, cd);
            getMixField().setMix(mix);
            getEnabledCheckBox().setSelected(m.isEnabled());
        } else {
            // TODO PP: error!
        }
    }
    
    public void save(OSTCInterface ostcInterface) throws UnknownFeatureException {
        Mix mix = getMixField().getMix();
        int o2 = mix.getOxygen();
        int he = mix.getHelium();
        int changedepth = (int)Math.floor(mix.getChange());
        boolean enabled = getEnabledCheckBox().isSelected();
        ProgrammableMix m = new ProgrammableMix(o2, he, changedepth, enabled);
        ostcInterface.setFeature(feature, m);
    }
    
    private void initialize() {
        setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.anchor = GridBagConstraints.NORTHWEST;
        gc.gridy = 0;
        gc.gridx = 0;
        add(getMixField(), gc);
        gc.gridx++;
        add(getEnabledCheckBox(), gc);
        setMinimumSize(new Dimension(50,40));
    }
    
    private MixField getMixField() {
        if (mixField == null) {
            mixField = new MixField(parent, db);
        }
        return mixField;
    }
    
    private JCheckBox getEnabledCheckBox() {
        if (enabledCheckBox == null) {
            enabledCheckBox = new JCheckBox("enabled");
        }
        return enabledCheckBox;
    }

}
