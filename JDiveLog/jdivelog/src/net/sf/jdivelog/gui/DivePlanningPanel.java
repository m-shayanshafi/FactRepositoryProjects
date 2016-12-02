/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: StatisticPanel.java
 * 
 * @author Volker Holthaus <v.holthaus@procar.de>
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

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import net.sf.jdivelog.gui.resources.Messages;
import net.sf.jdivelog.model.decompression.Deco2000_700_surface;
import net.sf.jdivelog.model.decompression.Deco2000_1500_surface;

/**
 * Description: Panel to switch the decompression model
 * 
 * @author Volker Holthaus <v.holthaus@procar.de>
 */
public class DivePlanningPanel extends JPanel implements ActionListener {
    
    private JComboBox decompressionModelList = null;
	
    private static final long serialVersionUID = 3258134643768178998L;

    private Deco2000_700_surface deco2000_700_surface = null;
    private Deco2000_1500_surface deco2000_1500_surface = null;
    private String[] decompressionmodels_names = new String[] {"", Messages.getString("decotable.deco2000_surface_700_m"), Messages.getString("decotable.deco2000_surface_1500_m")};        
    private boolean initialized = false;
    
    private JToolBar jToolBar = null;  
    
    private MainWindow mainWindow = null;
    
    /**
     * Default Constructor for GUI Builder, do not use!
     */
    @Deprecated
    public DivePlanningPanel() {
        super();
        init();
    }
    
    public DivePlanningPanel(MainWindow mainWindow) {
        super();
        this.mainWindow = mainWindow;
        init();        
    }

    //
    // private methods
    //

    private void init() {
        if (!initialized) {
            this.setComponentOrientation(java.awt.ComponentOrientation.LEFT_TO_RIGHT);
            this.setLayout(new BorderLayout(3, 3));
            this.add(getJToolBar(),BorderLayout.NORTH);
        }
    }
    
    private JToolBar getJToolBar() {
        if (jToolBar == null) {
            jToolBar = new JToolBar();
            jToolBar.setComponentOrientation(java.awt.ComponentOrientation.LEFT_TO_RIGHT);
            jToolBar.setLayout(new GridBagLayout());
            GridBagConstraints gc = new GridBagConstraints();
            gc.anchor = GridBagConstraints.CENTER;
            gc.insets = new Insets(5, 5, 5, 5);
            jToolBar.setPreferredSize(new java.awt.Dimension(44, 48));
            jToolBar.add(new JLabel(Messages.getString("DecompressionModel")), gc);
            gc.insets = new Insets(5, 5, 5, 5);
            jToolBar.add(getDecompressionModelList(), gc);
        };
        return jToolBar;
    }    
    
    private JComboBox getDecompressionModelList() {
        if (decompressionModelList == null) {
        	decompressionModelList = new JComboBox(decompressionmodels_names);
        	decompressionModelList.addActionListener(this);
        }
        return decompressionModelList;
    }

    private void replaceDecompressionModelSettingsPanel() {
    	if (decompressionModelList.getSelectedItem().equals(Messages.getString("decotable.deco2000_surface_700_m"))) {
    		deco2000_700_surface = new Deco2000_700_surface();
            this.removeAll();
            this.add(getJToolBar(),BorderLayout.NORTH);
            this.add(deco2000_700_surface.getConfigurationPanel(mainWindow), BorderLayout.CENTER);
            this.repaint();
    	}
    	if (decompressionModelList.getSelectedItem().equals(Messages.getString("decotable.deco2000_surface_1500_m"))) {
    		deco2000_1500_surface = new Deco2000_1500_surface();
            this.removeAll();
            this.add(getJToolBar(),BorderLayout.NORTH);
            this.add(deco2000_1500_surface.getConfigurationPanel(mainWindow), BorderLayout.CENTER);
            this.repaint();
    	}
        revalidate();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == decompressionModelList) {
        	replaceDecompressionModelSettingsPanel();
        }    
    }
}