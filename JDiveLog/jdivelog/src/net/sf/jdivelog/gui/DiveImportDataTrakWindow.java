/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: DiveImportDataTrakWindow.java
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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumnModel;

import net.sf.jdivelog.gui.commands.CommandAddDives;
import net.sf.jdivelog.gui.commands.CommandManager;
import net.sf.jdivelog.gui.resources.Messages;
import net.sf.jdivelog.model.JDive;
import net.sf.jdivelog.util.UnitConverter;
/**
 * Description: Window in order to import datatracks data
 * 
 * @author Volker Holthaus <v.holthaus@procar.de>
 */
public class DiveImportDataTrakWindow extends JDialog implements ActionListener {
    
    private static final long serialVersionUID = 3977581394245530418L;

    private MainWindow mainWindow = null;
        
    private DiveImportDataTrakTableModel diveFromDataTrakTableModel = null;
	private javax.swing.JPanel jContentPane = null;
	
	private JPanel diveFromDataTrakPanel = null;
    private JTable diveFromDataTrakTable = null;
	private JScrollPane diveFromDataTrakTablePane = null;


	private JPanel buttonPanel = null;
	private JButton addDiveFromDataTrakButton = null;
    private JButton cancelButton = null;
	
    private ArrayList<JDive> divesToAdd = null;

	
	public DiveImportDataTrakWindow(Window parent, MainWindow mainWindow, ArrayList<JDive> divesToAdd) {
		super(parent, ModalityType.APPLICATION_MODAL);
        this.divesToAdd = divesToAdd;
        this.mainWindow = mainWindow; 
		initialize();
	}
	
	
	private void initialize() {
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/net/sf/jdivelog/gui/resources/icons/logo.gif")));
		this.setSize(800,600);
		this.setContentPane(getJContentPane());
		this.setTitle(Messages.getString("diveimportdatatrak")); //$NON-NLS-1$
		this.setName(Messages.getString("diveimportdatatrak")); //$NON-NLS-1$
	}
	
	
	
	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJContentPane() {
		if(jContentPane == null) {
			jContentPane = new javax.swing.JPanel();
			jContentPane.setLayout(new java.awt.BorderLayout());
			jContentPane.add(getButtonPanel(), java.awt.BorderLayout.SOUTH);
			jContentPane.add(getDiveFromDataTrakPanel(), java.awt.BorderLayout.CENTER);
            new MnemonicFactory(jContentPane);
		}
		return jContentPane;
	}
	
	private JPanel getDiveFromDataTrakPanel() {
        if (diveFromDataTrakPanel == null) {
            diveFromDataTrakPanel = new JPanel();
            diveFromDataTrakPanel.setLayout(new BoxLayout(diveFromDataTrakPanel, BoxLayout.Y_AXIS));
        }
        diveFromDataTrakPanel.setName("diveimportdatatrak"); //$NON-NLS-1$
        diveFromDataTrakPanel.setComponentOrientation(java.awt.ComponentOrientation.LEFT_TO_RIGHT);
        diveFromDataTrakPanel.add(getDiveFromDataTrakTablePane(), null);
        return diveFromDataTrakPanel;
	}
        
	private JScrollPane getDiveFromDataTrakTablePane() {
	    if (diveFromDataTrakTablePane == null) {
            diveFromDataTrakTablePane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            diveFromDataTrakTablePane.setPreferredSize(new Dimension(800,550));
            diveFromDataTrakTablePane.setViewportView(getDiveFromDataTrakTable());
	    }
	    return diveFromDataTrakTablePane;
	}
	

    /**
     * This method initializes jTable
     * 
     * @return javax.swing.JTable
     */
    protected JTable getDiveFromDataTrakTable() {
        if (diveFromDataTrakTable == null) {
            diveFromDataTrakTable = new JTable();
            diveFromDataTrakTable.setComponentOrientation(java.awt.ComponentOrientation.LEFT_TO_RIGHT);
            diveFromDataTrakTable.setShowVerticalLines(false);
            diveFromDataTrakTable.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            diveFromDataTrakTable.setShowGrid(false);
            diveFromDataTrakTable.setShowHorizontalLines(true);
            diveFromDataTrakTable.setModel(getDiveFromDataTrakTableModel());
            TableColumnModel tcm = diveFromDataTrakTable.getColumnModel();
            tcm.getColumn(0).setResizable(true);
            tcm.getColumn(0).setPreferredWidth(200);
            tcm.getColumn(0).setHeaderValue(Messages.getString("date")); //$NON-NLS-1$
            tcm.getColumn(1).setResizable(true);
            tcm.getColumn(1).setPreferredWidth(300);
            tcm.getColumn(1).setHeaderValue(Messages.getString("place")); //$NON-NLS-1$
            tcm.getColumn(2).setResizable(true);
            tcm.getColumn(2).setPreferredWidth(200);
            tcm.getColumn(2).setHeaderValue(Messages.getString("country")); //$NON-NLS-1$
            tcm.getColumn(3).setResizable(true);
            tcm.getColumn(3).setPreferredWidth(100);
            tcm.getColumn(3).setHeaderValue(Messages.getString("depth")+" ["+UnitConverter.getDisplayAltitudeUnit()+"]"); //$NON-NLS-1$
            tcm.getColumn(4).setResizable(true);
            tcm.getColumn(4).setPreferredWidth(100);
            tcm.getColumn(4).setHeaderValue(Messages.getString("duration")+" ["+UnitConverter.getDisplayTimeUnit()+"]"); //$NON-NLS-1$
            diveFromDataTrakTable.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        int row = diveFromDataTrakTable.getSelectedRow();
                        JDive dive = diveFromDataTrakTableModel.getDiveFromDataTrak(row);
                        editDive(dive);
                    }
                }
            });
            
        }
        return diveFromDataTrakTable;
    }
    
    
    private void editDive(JDive dive) {
        DiveDetailWindow ddw = new DiveDetailWindow(this, this.mainWindow, dive, this.mainWindow.getLogBook().getMasterdata(), true);
        ddw.setVisible(true);
    }

    /**
     * 
     * @return The LogBookTableModel
     */
    public DiveImportDataTrakTableModel getDiveFromDataTrakTableModel() {
        if (diveFromDataTrakTableModel == null) {
            diveFromDataTrakTableModel = new DiveImportDataTrakTableModel(mainWindow.getLogBook().getMasterdata(), divesToAdd);
        }
        return diveFromDataTrakTableModel;
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
			buttonPanel.add(getAddDiveFromDataTrakButton(), gridBagConstraints1);
			gridBagConstraints1.gridx = 1;
			gridBagConstraints1.gridy = 0;
            buttonPanel.add(getCancelButton(), gridBagConstraints1);
		}
		return buttonPanel;
	}


	private JButton getAddDiveFromDataTrakButton() {
		if (addDiveFromDataTrakButton == null) {
			addDiveFromDataTrakButton = new JButton();
			addDiveFromDataTrakButton.setText(Messages.getString("add_divefromdatatrak")); //$NON-NLS-1$
			addDiveFromDataTrakButton.addActionListener(this);
		}
		return addDiveFromDataTrakButton;
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
        if (e.getSource() == addDiveFromDataTrakButton){
                int[] indices = diveFromDataTrakTable.getSelectedRows();
                for (int i = 0; i < indices.length; i++) {
                    ArrayList<JDive> diveToAdd = new ArrayList<JDive>();
                    diveToAdd.add(diveFromDataTrakTableModel.getDiveFromDataTrak(indices[i]));
                    CommandAddDives cmd = new CommandAddDives(this.mainWindow, diveToAdd);
                    CommandManager.getInstance().execute(cmd);            
                }
            dispose();            
        } else if (e.getSource() == cancelButton){
            dispose();            
        }
    }

}
