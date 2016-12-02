/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: AbstractSettingsPanel.java
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
package net.sf.jdivelog.gui;

import java.awt.LayoutManager;

import javax.swing.JPanel;

import net.sf.jdivelog.gui.commands.UndoableCommand;

public abstract class AbstractSettingsPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    public AbstractSettingsPanel(LayoutManager layout, boolean isDoubleBuffered) {
        super(layout, isDoubleBuffered);
    }

    public AbstractSettingsPanel(LayoutManager layout) {
        super(layout);
    }

    public AbstractSettingsPanel(boolean isDoubleBuffered) {
        super(isDoubleBuffered);
    }

    public AbstractSettingsPanel() {
        super();
    }
    
    public abstract void load();
    
    public abstract UndoableCommand getSaveCommand();

}
