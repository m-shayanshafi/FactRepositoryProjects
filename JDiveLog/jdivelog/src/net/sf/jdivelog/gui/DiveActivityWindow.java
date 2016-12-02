/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: DiveActivityWindow.java
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

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableColumnModel;

import net.sf.jdivelog.gui.LogbookChangeEvent.EventType;
import net.sf.jdivelog.gui.resources.Messages;
import net.sf.jdivelog.gui.util.TableSorter;
import net.sf.jdivelog.model.DiveActivity;

/**
 * Description: Window in order to display and edit the dive activities
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class DiveActivityWindow extends JDialog implements ActionListener, LogbookChangeListener {
    
    private static final long serialVersionUID = 3977581394245530418L;

    private MainWindow mainWindow = null;
    private DiveActivityTableModel diveactivityTableModel = null;
	private javax.swing.JPanel jContentPane = null;
	
	private JPanel diveactivityPanel = null;
    private JTable diveactivityTable = null;
	private JScrollPane diveactivityTablePane = null;


    private JTextField description = null;
    
	private JPanel buttonPanel = null;
	private JButton addDiveActivityButton = null;
	private JButton deleteDiveActivityButton = null;
	private JButton closeButton = null;
    private JButton cancelButton;
	
    private DiveDetailPanel divedetailpanel = null;
    
    private TableSorter model;
    
    private boolean integrated;
    private final LogbookChangeNotifier logbookChangeNotifier;

	public DiveActivityWindow(Window parent, MainWindow mainWindow, LogbookChangeNotifier logbookChangeNotifier) {
        this(parent, mainWindow, false, logbookChangeNotifier);
	}

	public DiveActivityWindow(Window parent, MainWindow mainWindow, boolean integrated, LogbookChangeNotifier logbookChangeNotifier) {
        this(parent, mainWindow, null, integrated, logbookChangeNotifier);
	}

	public DiveActivityWindow(Window parent, MainWindow mainWindow, DiveDetailPanel divedetailpanel, boolean integrated, LogbookChangeNotifier logbookChangeNotifier) {
		super(parent, ModalityType.APPLICATION_MODAL);
        this.divedetailpanel = divedetailpanel;
        this.mainWindow = mainWindow;
        this.integrated = integrated;
        this.logbookChangeNotifier = logbookChangeNotifier;
        if (integrated) {
            logbookChangeNotifier.addLogbookChangeListener(this);
        }
		initialize();
	}
	
	private void initialize() {
		this.setSize(500,600);
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/net/sf/jdivelog/gui/resources/icons/logo.gif")));
		this.setContentPane(getJContentPane());
		this.setTitle(Messages.getString("diveactivity")); //$NON-NLS-1$
		this.setName(Messages.getString("diveactivity")); //$NON-NLS-1$
	}
	
	
	
	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	public javax.swing.JPanel getJContentPane() {
		if(jContentPane == null) {
			jContentPane = new javax.swing.JPanel();
			jContentPane.setLayout(new java.awt.BorderLayout());
            jContentPane.add(getButtonPanel(), java.awt.BorderLayout.SOUTH);
			jContentPane.add(getDiveActivityPanel(), java.awt.BorderLayout.CENTER);
            new MnemonicFactory(jContentPane);
		}
		return jContentPane;
	}
	
	private JPanel getDiveActivityPanel() {
        if (diveactivityPanel == null) {
            diveactivityPanel = new JPanel();
            diveactivityPanel.setLayout(new BoxLayout(diveactivityPanel, BoxLayout.Y_AXIS));
        }
        diveactivityPanel.setName("buddy"); //$NON-NLS-1$
        diveactivityPanel.setComponentOrientation(java.awt.ComponentOrientation.LEFT_TO_RIGHT);
        diveactivityPanel.add(getDiveTypeTablePane(), null);
        return diveactivityPanel;
	}
        
	private JScrollPane getDiveTypeTablePane() {
	    if (diveactivityTablePane == null) {
            diveactivityTablePane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            diveactivityTablePane.setPreferredSize(new Dimension(500,550));
            diveactivityTablePane.setViewportView(getDiveActivityTable());
	    }
	    return diveactivityTablePane;
	}
	

    /**
     * This method initializes jTable
     * 
     * @return javax.swing.JTable
     */
    private JTable getDiveActivityTable() {
        if (diveactivityTable == null) {
        	model = new TableSorter(getDiveTypeTableModel());
            diveactivityTable = new JTable();
            diveactivityTable.setComponentOrientation(java.awt.ComponentOrientation.LEFT_TO_RIGHT);
            diveactivityTable.setShowVerticalLines(false);
            diveactivityTable.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            diveactivityTable.setShowGrid(false);
            diveactivityTable.setShowHorizontalLines(true);
            diveactivityTable.setModel(model);
            model.addMouseListenerToHeader(diveactivityTable);
            TableColumnModel tcm = diveactivityTable.getColumnModel();
            tcm.getColumn(0).setResizable(true);
            tcm.getColumn(0).setPreferredWidth(200);
            tcm.getColumn(0).setHeaderValue(Messages.getString("description")); //$NON-NLS-1$
        }
        return diveactivityTable;
    }

    /**
     * 
     * @return The LogBookTableModel
     */
    public DiveActivityTableModel getDiveTypeTableModel() {
        if (diveactivityTableModel == null) {
            diveactivityTableModel = new DiveActivityTableModel(this, mainWindow.getLogBook().getMasterdata().getDiveactivities());
        }
        return diveactivityTableModel;
    }

	
	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getButtonPanel() {
		if (buttonPanel == null) {
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints1.weightx=0.5;
			buttonPanel = new JPanel();
			buttonPanel.setLayout(new GridBagLayout());
			buttonPanel.setComponentOrientation(java.awt.ComponentOrientation.LEFT_TO_RIGHT);
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.insets = new java.awt.Insets(5,5,5,5);
			buttonPanel.add(new JLabel(Messages.getString("description")), gridBagConstraints1);
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.gridy = 1;
			gridBagConstraints1.insets = new java.awt.Insets(5,5,5,5);
			buttonPanel.add(getDescription(), gridBagConstraints1);
			gridBagConstraints1.gridx = 1;
			gridBagConstraints1.gridy = 1;
			gridBagConstraints1.insets = new java.awt.Insets(5,5,5,5);
			buttonPanel.add(getAddDiveActivityButton(), gridBagConstraints1);
			gridBagConstraints1.gridx = 1;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.insets = new java.awt.Insets(5,5,5,5);
			buttonPanel.add(getDeleteDiveActivityButton(), gridBagConstraints1);
			if (!integrated) {
                gridBagConstraints1.gridx = 0;
                gridBagConstraints1.gridy = 2;
                gridBagConstraints1.insets = new java.awt.Insets(5,5,5,5);
                buttonPanel.add(getCloseButton(), gridBagConstraints1);
                gridBagConstraints1.gridx = 1;
                gridBagConstraints1.gridy = 2;
                buttonPanel.add(getCancelButton(), gridBagConstraints1);
            }
		}
		return buttonPanel;
	}

	private JTextField getDescription() {
		if (description == null) {
			description = new JTextField();
		}
		return description;
	}

	private JButton getAddDiveActivityButton() {
		if (addDiveActivityButton == null) {
			addDiveActivityButton = new JButton();
			addDiveActivityButton.setText(Messages.getString("add_diveactivity")); //$NON-NLS-1$
			addDiveActivityButton.addActionListener(this);
		}
		return addDiveActivityButton;
	}

	private JButton getDeleteDiveActivityButton() {
		if (deleteDiveActivityButton == null) {
			deleteDiveActivityButton = new JButton();
			deleteDiveActivityButton.setText(Messages.getString("delete_diveactivity")); //$NON-NLS-1$
			deleteDiveActivityButton.addActionListener(this);
		}
		return deleteDiveActivityButton;
	}

	private JButton getCloseButton() {
		if (closeButton == null) {
			closeButton = new JButton();
			closeButton.setText(Messages.getString("close")); //$NON-NLS-1$
			closeButton.addActionListener(this);
		}
		return closeButton;
	}
    
    private JButton getCancelButton() {
        if (cancelButton == null) {
            cancelButton = new JButton();
            cancelButton.setText(Messages.getString("cancel")); //$NON-NLS-1$
            cancelButton.addActionListener(this);
        }
        return cancelButton;
    }
	
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == closeButton) {
            save_data();
            dispose();
        } else if (e.getSource() == addDiveActivityButton){
            DiveActivity diveactivities = new DiveActivity();
            diveactivities.setDescription(description.getText());
            diveactivityTableModel.addDiveActivity(diveactivities);
            description.setText("");
            if (integrated) {
                save_data();
            }
        } else if (e.getSource() == deleteDiveActivityButton){
            diveactivityTableModel.removeDiveActivity(diveactivityTable.getSelectedRow());           
            if (integrated) {
                save_data();
            }
        } else if (e.getSource() == cancelButton ){
            dispose();
        }
    }
    
    private void save_data() {
        if (divedetailpanel != null) {
            String diveactivity = new String();
            int[] indices = diveactivityTable.getSelectedRows();
            for (int i = 0; i < indices.length; i++) {
                diveactivity += diveactivityTable.getValueAt(indices[i], 0);
                if (i != indices.length -1 ) {
                    diveactivity += ",";
                }
            }
            divedetailpanel.setDiveActivity(diveactivity);
        }
        ArrayList<DiveActivity> masterList = mainWindow.getLogBook().getMasterdata().getDiveactivities();
        masterList.clear();
        masterList.addAll(diveactivityTableModel.getAll());
        logbookChangeNotifier.setChanged(true);
        logbookChangeNotifier.notifyLogbookDataChanged();        
        
    }
    public void logbookChanged(LogbookChangeEvent e) {
        if (!EventType.LOGBOOK_TITLE_CHANGED.equals(e.getType())) {
            load();
        }
    }
    
    private void load() {
        getDiveTypeTableModel().load(mainWindow.getLogBook().getMasterdata().getDiveactivities());
    }

}
