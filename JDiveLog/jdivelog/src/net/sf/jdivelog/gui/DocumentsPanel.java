/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: ExportSettingsIndexPanel.java
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

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

public class DocumentsPanel extends JPanel {
    
    private static final long serialVersionUID = 1L;

    private MainWindow mainWindow;
    private JPanel documentsTreePanel;
    private JPanel documentsTablePanel;
    private JTree documentsTree;
    
    public DocumentsPanel(MainWindow mainWindow) {
       this.mainWindow = mainWindow;
       initialize();
    }
        
    
    private void initialize() {
    	this.setComponentOrientation(java.awt.ComponentOrientation.LEFT_TO_RIGHT);
        this.setLayout(new BorderLayout());
        add(getdocumentsTreePanel(), BorderLayout.WEST);
        //add(getdocumentsTablePanel(), BorderLayout.EAST);
        //Border border = BorderFactory.createTitledBorder(Messages.getString("index_page")); //$NON-NLS-1$
        //setBorder(border);

    }


    private JPanel getdocumentsTreePanel() {
        if (documentsTreePanel == null) {
        	documentsTreePanel = new JPanel();   
        	documentsTreePanel.add(getdocumentsTree());
        }
        return documentsTreePanel;
    }

    private JTree getdocumentsTree() {
        if (documentsTree == null) {
        	DefaultMutableTreeNode top = new DefaultMutableTreeNode("Dokumente");
        	documentsTree = new JTree(top);
        }
        return documentsTree;
    }

    private JPanel getdocumentsTablePanel() {
        if (documentsTablePanel == null) {
        	documentsTablePanel = new JPanel();
        }
        return documentsTablePanel;
    }


}
