/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: BuddyWindow.java
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
import net.sf.jdivelog.model.Buddy;

/**
 * Description: Window including the buddy table and interactions in order to add or delete buddies
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class BuddyWindow extends JDialog implements ActionListener, LogbookChangeListener {
    
    private static final long serialVersionUID = 3977581394245530418L;

    private MainWindow mainWindow;
    private BuddyTableModel buddyTableModel = null;
    private Buddy buddy = null;
	private javax.swing.JPanel jContentPane = null;
	
	private JPanel buddyPanel = null;
    private JTable buddyTable = null;
	private JScrollPane buddyTablePane = null;


    private JTextField firstname = null;
    private JTextField lastname = null;
    
	private JPanel buttonPanel = null;
	private JButton addBuddyButton = null;
	private JButton deleteBuddyButton = null;
	private JButton closeButton = null;
	
    private DiveDetailPanel divedetailpanel = null;

    private JButton cancelButton;

    private TableSorter model;
    
    private boolean integrated;
    private final LogbookChangeNotifier logbookChangeNotifier;
    
	public BuddyWindow(Window parent, MainWindow mainWindow, LogbookChangeNotifier logbookChangeNotifier) {
        this(parent, mainWindow, false, logbookChangeNotifier);
	}

	public BuddyWindow(Window parent, MainWindow mainWindow, boolean integrated, LogbookChangeNotifier logbookChangeNotifier) {
        this(parent, mainWindow, null, integrated, logbookChangeNotifier);
	}

	public BuddyWindow(Window parent, MainWindow mainWindow, DiveDetailPanel divedetailpanel, boolean integrated, LogbookChangeNotifier logbookChangeNotifier) {
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
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/net/sf/jdivelog/gui/resources/icons/logo.gif")));
		this.setSize(500,600);
		this.setContentPane(getJContentPane());
		this.setTitle(Messages.getString("buddydetails")); //$NON-NLS-1$
		this.setName(Messages.getString("buddydetails")); //$NON-NLS-1$
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
			jContentPane.add(getBuddyPanel(), java.awt.BorderLayout.CENTER);
            new MnemonicFactory(jContentPane);
		}
		return jContentPane;
	}
	
	private JPanel getBuddyPanel() {
        if (buddyPanel == null) {
            buddyPanel = new JPanel();
            buddyPanel.setLayout(new BoxLayout(buddyPanel, BoxLayout.Y_AXIS));
        }
        buddyPanel.setName("buddy"); //$NON-NLS-1$
        buddyPanel.setComponentOrientation(java.awt.ComponentOrientation.LEFT_TO_RIGHT);
        buddyPanel.add(getBuddyTablePane(), null);
        return buddyPanel;
	}
        
	private JScrollPane getBuddyTablePane() {
	    if (buddyTablePane == null) {
            buddyTablePane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            buddyTablePane.setPreferredSize(new Dimension(500,550));
            buddyTablePane.setViewportView(getBuddyTable());
	    }
	    return buddyTablePane;
	}
	

    /**
     * This method initializes jTable
     * 
     * @return javax.swing.JTable
     */
    private JTable getBuddyTable() {
        if (buddyTable == null) {
        	model = new TableSorter(getBuddyTableModel());
            buddyTable = new JTable();
            buddyTable.setComponentOrientation(java.awt.ComponentOrientation.LEFT_TO_RIGHT);
            buddyTable.setShowVerticalLines(false);
            buddyTable.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            buddyTable.setShowGrid(false);
            buddyTable.setShowHorizontalLines(true);
            buddyTable.setModel(model);
            model.addMouseListenerToHeader(buddyTable);
            TableColumnModel tcm = buddyTable.getColumnModel();
            tcm.getColumn(0).setResizable(true);
            tcm.getColumn(0).setPreferredWidth(200);
            tcm.getColumn(0).setHeaderValue(Messages.getString("firstname")); //$NON-NLS-1$
            tcm.getColumn(1).setResizable(true);
            tcm.getColumn(1).setPreferredWidth(200);
            tcm.getColumn(1).setHeaderValue(Messages.getString("lastname")); //$NON-NLS-1$
        }
        return buddyTable;
    }

    /**
     * 
     * @return The LogBookTableModel
     */
    public BuddyTableModel getBuddyTableModel() {
        if (buddyTableModel == null) {
            buddyTableModel = new BuddyTableModel(this, mainWindow.getLogBook().getMasterdata().getBuddys());
        }
        return buddyTableModel;
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
			buttonPanel.add(new JLabel(Messages.getString("firstname")), gridBagConstraints1);
			gridBagConstraints1.gridx = 1;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.insets = new java.awt.Insets(5,5,5,5);
			buttonPanel.add(new JLabel(Messages.getString("lastname")), gridBagConstraints1);
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.gridy = 1;
			gridBagConstraints1.insets = new java.awt.Insets(5,5,5,5);
			buttonPanel.add(getfirstname(), gridBagConstraints1);
			gridBagConstraints1.gridx = 1;
			gridBagConstraints1.gridy = 1;
			gridBagConstraints1.insets = new java.awt.Insets(5,5,5,5);
			buttonPanel.add(getlastnamename(), gridBagConstraints1);
			gridBagConstraints1.gridx = 2;
			gridBagConstraints1.gridy = 1;
			gridBagConstraints1.insets = new java.awt.Insets(5,5,5,5);
			buttonPanel.add(getAddBuddyButton(), gridBagConstraints1);
			gridBagConstraints1.gridx = 2;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.insets = new java.awt.Insets(5,5,5,5);
			buttonPanel.add(getDeleteBuddyButton(), gridBagConstraints1);
			if (!integrated) {
                gridBagConstraints1.gridx = 1;
                gridBagConstraints1.gridy = 2;
                gridBagConstraints1.insets = new java.awt.Insets(5,5,5,5);
                buttonPanel.add(getCloseButton(), gridBagConstraints1);
                gridBagConstraints1.gridx = 2;
                gridBagConstraints1.gridy = 2;
                buttonPanel.add(getCancelButton(), gridBagConstraints1);
            }
		}
		return buttonPanel;
	}

	private JTextField getfirstname() {
		if (firstname == null) {
			firstname = new JTextField();
		}
		return firstname;
	}

	private JTextField getlastnamename() {
		if (lastname == null) {
			lastname = new JTextField();
		}
		return lastname;
	}

	private JButton getAddBuddyButton() {
		if (addBuddyButton == null) {
			addBuddyButton = new JButton();
			addBuddyButton.setText(Messages.getString("new_buddy")); //$NON-NLS-1$
			addBuddyButton.addActionListener(this);
		}
		return addBuddyButton;
	}

	private JButton getDeleteBuddyButton() {
		if (deleteBuddyButton == null) {
			deleteBuddyButton = new JButton();
			deleteBuddyButton.setText(Messages.getString("delete_buddy")); //$NON-NLS-1$
			deleteBuddyButton.addActionListener(this);
		}
		return deleteBuddyButton;
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
        } else if (e.getSource() == addBuddyButton){
            buddy = new Buddy();
            buddy.setFirstname(firstname.getText());
            buddy.setLastname(lastname.getText());
            buddyTableModel.addBuddy(buddy);
            firstname.setText("");
            lastname.setText("");
            if (integrated) {
                save_data();
            }
        } else if (e.getSource() == deleteBuddyButton){
            buddyTableModel.removeBuddy(buddyTable.getSelectedRow());           
            if (integrated) {
                save_data();
            }
        } else if (e.getSource() == cancelButton) {
            dispose();
        }
    }

    private void save_data() {
        if (divedetailpanel != null) {
            String buddys = "";
            int[] indices = buddyTable.getSelectedRows();
            for (int i = 0; i < indices.length; i++) {
                buddys += buddyTable.getValueAt(indices[i], 0) + " " + buddyTable.getValueAt(indices[i], 1);
                if (i != indices.length -1 ) {
                    buddys += ",";
                }
            }
            divedetailpanel.setBuddy(buddys);
        }
        ArrayList<Buddy> masterList = mainWindow.getLogBook().getMasterdata().getBuddys();
        masterList.clear();
        masterList.addAll(buddyTableModel.getAll());
        logbookChangeNotifier.setChanged(true);
        logbookChangeNotifier.notifyLogbookDataChanged();        
    }
    
    public void logbookChanged(LogbookChangeEvent e) {
        if (!EventType.LOGBOOK_TITLE_CHANGED.equals(e.getType())) {
            load();
        }
    }
    
    private void load() {
        getBuddyTableModel().load(mainWindow.getLogBook().getMasterdata().getBuddys());
    }
    
}
