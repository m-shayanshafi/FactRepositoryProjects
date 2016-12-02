/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: DummyControl.java
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

import java.util.logging.Logger;

import javax.swing.JPanel;

import net.sf.jdivelog.ci.OSTCInterface;
import net.sf.jdivelog.ci.ostc.Feature;

/**
 * Control for unknown type.
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class DummyControl extends JPanel implements OSTCControl {
    
    private static final Logger LOGGER = Logger.getLogger(DummyControl.class.getName());
    private static final long serialVersionUID = 4395607664907696587L;

    /**
     * Constructor
     * @param f 
     */
    public DummyControl(Feature f) {
        LOGGER.warning("Using dummy control for feature "+f);
    }

    /**
     * @see net.sf.jdivelog.gui.ostc.OSTCControl#load(net.sf.jdivelog.ci.OSTCInterface)
     */
    public void load(OSTCInterface ostcInterface) {
        // does nothing
    }

    /**
     * @see net.sf.jdivelog.gui.ostc.OSTCControl#save(net.sf.jdivelog.ci.OSTCInterface)
     */
    public void save(OSTCInterface ostcInterface) {
        // does nothing
    }

}
