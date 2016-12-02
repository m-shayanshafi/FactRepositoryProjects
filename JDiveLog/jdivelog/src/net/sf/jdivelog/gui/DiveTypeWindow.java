/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: DiveTypeWindow.java
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
import net.sf.jdivelog.model.DiveType;

/**
 * Description: Window displaying the differents dive types and enabling to edit them
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class DiveTypeWindow extends JDialog implements ActionListener, LogbookChangeListener {
    
    private static final long serialVersionUID = 3977581394245530418L;
        
    private MainWindow mainWindow = null;
    private DiveTypeTableModel divetypeTableModel = null;
	private javax.swing.JPanel jContentPane = null;
	
	private JPanel divetypePanel = null;
    private JTable divetypeTable = null;
	private JScrollPane divetypeTablePane = null;


    private JTextField description = null;
    
	private JPanel buttonPanel = null;
	private JButton addDiveTypeButton = null;
	private JButton deleteDiveTypeButton = null;
	private JButton closeButton = null;
    private JButton cancelButton;
	
    private DiveDetailPanel divedetailpanel = null;

    private TableSorter model;
    
    private boolean integrated;
    private final LogbookChangeNotifier logbookChangeNotifier;
    
    
	public DiveTypeWindow(Window parent, MainWindow mainWindow, LogbookChangeNotifier logbookChangeNotifier) {
        this(parent, mainWindow, false, logbookChangeNotifier);
	}

	public DiveTypeWindow(Window parent, MainWindow mainWindow, boolean integrated, LogbookChangeNotifier logbookChangeNotifier) {
        this(parent, mainWindow, null, integrated, logbookChangeNotifier);
	}

	public DiveTypeWindow(Window parent, MainWindow mainWindow, DiveDetailPanel divedetailpanel, boolean integrated, LogbookChangeNotifier logbookChangeNotifier) {
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
		this.setTitle(Messages.getString("divetypedetails")); //$NON-NLS-1$
		this.setName(Messages.getString("divetypedetails")); //$NON-NLS-1$
	}

	public javax.swing.JPanel getJContentPane() {
		if(jContentPane == null) {
			jContentPane = new javax.swing.JPanel();
			jContentPane.setLayout(new java.awt.BorderLayout());
			jContentPane.add(getButtonPanel(), java.awt.BorderLayout.SOUTH);
			jContentPane.add(getDiveTypePanel(), java.awt.BorderLayout.CENTER);
            new MnemonicFactory(jContentPane);
		}
		return jContentPane;
	}
	
	private JPanel getDiveTypePanel() {
        if (divetypePanel == null) {
            divetypePanel = new JPanel();
            divetypePanel.setLayout(new BoxLayout(divetypePanel, BoxLayout.Y_AXIS));
        }
        divetypePanel.setName("buddy"); //$NON-NLS-1$
        divetypePanel.setComponentOrientation(java.awt.ComponentOrientation.LEFT_TO_RIGHT);
        divetypePanel.add(getDiveTypeTablePane(), null);
        return divetypePanel;
	}
        
	private JScrollPane getDiveTypeTablePane() {
	    if (divetypeTablePane == null) {
            divetypeTablePane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            divetypeTablePane.setPreferredSize(new Dimension(500,550));
            divetypeTablePane.setViewportView(getDiveTypeTable());
	    }
	    return divetypeTablePane;
	}
	
    private JTable getDiveTypeTable() {
        if (divetypeTable == null) {
        	model = new TableSorter(getDiveTypeTableModel());
            divetypeTable = new JTable();
            divetypeTable.setComponentOrientation(java.awt.ComponentOrientation.LEFT_TO_RIGHT);
            divetypeTable.setShowVerticalLines(false);
            divetypeTable.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            divetypeTable.setShowGrid(false);
            divetypeTable.setShowHorizontalLines(true);
            divetypeTable.setModel(model);
            model.addMouseListenerToHeader(divetypeTable);
            TableColumnModel tcm = divetypeTable.getColumnModel();
            tcm.getColumn(0).setResizable(true);
            tcm.getColumn(0).setPreferredWidth(200);
            tcm.getColumn(0).setHeaderValue(Messages.getString("description")); //$NON-NLS-1$
        }
        return divetypeTable;
    }

    public DiveTypeTableModel getDiveTypeTableModel() {
        if (divetypeTableModel == null) {
            divetypeTableModel = new DiveTypeTableModel(this, mainWindow.getLogBook().getMasterdata().getDivetypes());
        }
        return divetypeTableModel;
    }

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
			buttonPanel.add(getdescription(), gridBagConstraints1);
			gridBagConstraints1.gridx = 1;
			gridBagConstraints1.gridy = 1;
			gridBagConstraints1.insets = new java.awt.Insets(5,5,5,5);
			buttonPanel.add(getAddDiveTypeButton(), gridBagConstraints1);
			gridBagConstraints1.gridx = 1;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.insets = new java.awt.Insets(5,5,5,5);
			buttonPanel.add(getDeleteDiveTypeButton(), gridBagConstraints1);
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
	/**
	 * This method initializes closeButton	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JTextField getdescription() {
		if (description == null) {
			description = new JTextField();
		}
		return description;
	}

	private JButton getAddDiveTypeButton() {
		if (addDiveTypeButton == null) {
			addDiveTypeButton = new JButton();
			addDiveTypeButton.setText(Messages.getString("add_divetype")); //$NON-NLS-1$
			addDiveTypeButton.addActionListener(this);
		}
		return addDiveTypeButton;
	}

	private JButton getDeleteDiveTypeButton() {
		if (deleteDiveTypeButton == null) {
			deleteDiveTypeButton = new JButton();
			deleteDiveTypeButton.setText(Messages.getString("delete_divetype")); //$NON-NLS-1$
			deleteDiveTypeButton.addActionListener(this);
		}
		return deleteDiveTypeButton;
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
        } else if (e.getSource() == addDiveTypeButton){
            DiveType divetypes = new DiveType();
            divetypes.setDescription(description.getText());
            divetypeTableModel.addDiveType(divetypes);
            description.setText("");
            if (integrated) {
                save_data();
            }
        } else if (e.getSource() == deleteDiveTypeButton){
            divetypeTableModel.removeDiveType(divetypeTable.getSelectedRow());           
            if (integrated) {
                save_data();
            }
        } else if (e.getSource() == cancelButton){
            dispose();
        }
    }
    
    private void save_data() {
        if (divedetailpanel != null) {
            String divetype = new String();
            int[] indices = divetypeTable.getSelectedRows();
            for (int i = 0; i < indices.length; i++) {
                divetype += divetypeTable.getValueAt(indices[i], 0);
                if (i != indices.length -1 ) {
                    divetype += ",";
                }
            }
            divedetailpanel.setDiveType(divetype);
        }
        ArrayList<DiveType> masterList = mainWindow.getLogBook().getMasterdata().getDivetypes();
        masterList.clear();
        masterList.addAll(divetypeTableModel.getAll());
        logbookChangeNotifier.setChanged(true);
        logbookChangeNotifier.notifyLogbookDataChanged();        
    }
    
    public void logbookChanged(LogbookChangeEvent e) {
        if (!EventType.LOGBOOK_TITLE_CHANGED.equals(e.getType())) {
            load();
        }
    }
    
    private void load() {
        getDiveTypeTableModel().load(mainWindow.getLogBook().getMasterdata().getDivetypes());
    }

}
