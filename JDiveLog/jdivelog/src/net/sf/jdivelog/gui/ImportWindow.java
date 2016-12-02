/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: ImportWindow.java
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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumnModel;

import net.sf.jdivelog.gui.commands.CommandAddDives;
import net.sf.jdivelog.gui.commands.CommandIgnoreDives;
import net.sf.jdivelog.gui.commands.CommandManager;
import net.sf.jdivelog.gui.resources.Messages;
import net.sf.jdivelog.model.JDive;
import net.sf.jdivelog.model.JDiveLog;
import net.sf.jdivelog.util.UnitConverter;

/**
 * Description: Window in order to import data from files
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class ImportWindow extends JDialog implements ActionListener {

    private static final long serialVersionUID = 3257283651766203193L;
    
    private MainWindow mainWindow = null;
    private TreeSet<JDive> dives = null;
	private javax.swing.JPanel jContentPane = null;
	private JPanel buttonPanel = null;
	private JButton importButton = null;
    private JButton ignoreButton = null;
	private JButton cancelButton = null;
	private ImportTableModel importTableModel = null;
	private JTable importTable = null;
	private JScrollPane importTablePane = null;
	private JDiveLog import_logbook = null;
    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == cancelButton) {
            this.dispose();
        } else if (e.getSource() == importButton) {
            ArrayList<JDive> divesToAdd = new ArrayList<JDive>();
            int[] rows = getImportTable().getSelectedRows();
            for (int i=0; i<rows.length; i++) {
                JDive dive = getImportTableModel().getDive(rows[i]);
                divesToAdd.add(dive);
            }
            CommandAddDives cmd = new CommandAddDives(this.mainWindow, divesToAdd, import_logbook);
            CommandManager.getInstance().execute(cmd);            	
            this.dispose();
        } else if (e.getSource() == ignoreButton) {
            ArrayList<JDive> divesToIgnore = new ArrayList<JDive>();
            int[] rows = getImportTable().getSelectedRows();
            for (int i=0; i<rows.length; i++) {
                JDive dive = getImportTableModel().getDive(rows[i]);
                divesToIgnore.add(dive);
            }
            CommandIgnoreDives cmd = new CommandIgnoreDives(this.mainWindow, divesToIgnore);
            CommandManager.getInstance().execute(cmd);
            updateSelection();
        }
    }

	public ImportWindow(MainWindow mainWindow, JDiveLog import_logbook) {
		this.import_logbook = import_logbook;
		this.mainWindow = mainWindow;
		this.dives = import_logbook.getDives();

		initialize();
        new MnemonicFactory(this);
	}

	public ImportWindow(MainWindow mainWindow, TreeSet<JDive> dives) {
		super(mainWindow, ModalityType.APPLICATION_MODAL);
		this.mainWindow = mainWindow;
		this.dives = dives;

		initialize();
        new MnemonicFactory(this);
	}
    
	private void initialize() {
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/net/sf/jdivelog/gui/resources/icons/logo.gif")));
		this.setSize(500, 400);
		this.setContentPane(getJContentPane());
		this.setTitle(Messages.getString("import_window_title")); //$NON-NLS-1$
	}
    
	private javax.swing.JPanel getJContentPane() {
		if(jContentPane == null) {
			jContentPane = new javax.swing.JPanel();
			jContentPane.setLayout(new java.awt.BorderLayout());
			jContentPane.add(getButtonPanel(), java.awt.BorderLayout.SOUTH);
			jContentPane.add(getImportTablePane(), java.awt.BorderLayout.CENTER);
		}
		return jContentPane;
	}
    
	private JPanel getButtonPanel() {
		if (buttonPanel == null) {
			buttonPanel = new JPanel();
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints1.weightx=0.5;
			buttonPanel = new JPanel();
			buttonPanel.setLayout(new GridBagLayout());
			buttonPanel.setComponentOrientation(java.awt.ComponentOrientation.LEFT_TO_RIGHT);
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.insets = new java.awt.Insets(5,50,5,5);
			buttonPanel.add(getImportButton(), gridBagConstraints1);
            gridBagConstraints1.gridx = 1;
            gridBagConstraints1.gridy = 0;
            gridBagConstraints1.insets = new java.awt.Insets(5,5,5,5);
            buttonPanel.add(getIgnoreButton(), gridBagConstraints1);
            gridBagConstraints1.gridx = 2;
            gridBagConstraints1.gridy = 0;
            gridBagConstraints1.insets = new java.awt.Insets(5,5,5,50);
            buttonPanel.add(getCancelButton(), gridBagConstraints1);
		}
		return buttonPanel;
	}
    
    private JButton getImportButton() {
        if (importButton == null) {
            importButton = new JButton();
            importButton.setText(Messages.getString("import")); //$NON-NLS-1$
            importButton.addActionListener(this);
        }
        return importButton;
    }
    
    private JButton getIgnoreButton() {
        if (ignoreButton == null) {
            ignoreButton = new JButton();
            ignoreButton.setText(Messages.getString("ignore")); //$NON-NLS-1$
            ignoreButton.addActionListener(this);
        }
        return ignoreButton;
    }
    
	private JButton getCancelButton() {
		if (cancelButton == null) {
			cancelButton = new JButton();
			cancelButton.setText(Messages.getString("cancel")); //$NON-NLS-1$
			cancelButton.addActionListener(this);
		}
		return cancelButton;
	}
    
	private ImportTableModel getImportTableModel() {
	    if (importTableModel == null) {
	        importTableModel = new ImportTableModel(dives);
	    }
	    return importTableModel;
	}

	private JTable getImportTable() {
		if (importTable == null) {
			importTable = new JTable();
            ImportTableModel model = getImportTableModel();
			importTable.setModel(model);
            importTable.setShowVerticalLines(false);
            importTable
                    .setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            importTable.setShowGrid(false);
            importTable.setShowHorizontalLines(true);
            TableColumnModel tcm = importTable.getColumnModel();
            tcm.getColumn(0).setResizable(true);
            tcm.getColumn(0).setPreferredWidth(100);
            tcm.getColumn(0).setHeaderValue(Messages.getString("date")); //$NON-NLS-1$
            tcm.getColumn(1).setResizable(true);
            tcm.getColumn(1).setPreferredWidth(60);
            tcm.getColumn(1).setHeaderValue(Messages.getString("depth")+" ["+UnitConverter.getDisplayAltitudeUnit()+"]"); //$NON-NLS-1$
            tcm.getColumn(2).setResizable(true);
            tcm.getColumn(2).setPreferredWidth(60);
            tcm.getColumn(2).setHeaderValue(Messages.getString("duration")+" ["+UnitConverter.getDisplayTimeUnit()+"]"); //$NON-NLS-1$
            updateSelection();
            importTable.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        int row = importTable.getSelectedRow();
                        JDive dive = getImportTableModel().getDive(row);
                        editDive(dive);
                    }
                }
            });
            
		}
		return importTable;
	}

    /**
     * Selects all dives which are not ignored and are not already in the logbook
     */
    private void updateSelection() {
        JTable table = getImportTable();
        table.clearSelection();
        ImportTableModel model = getImportTableModel();
        HashMap<String, JDive> existing = new HashMap<String, JDive>();
        Iterator<JDive> it = mainWindow.getLogBook().getDives().iterator();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd - HH.mm");
        while (it.hasNext()) {
            JDive dive = it.next();
            existing.put(dateFormat.format(dive.getDate()), dive);
        }
        HashMap<String, JDive> ignored = new HashMap<String, JDive>();
        it = mainWindow.getLogBook().getIgnoredDives().iterator();
        while (it.hasNext()) {
            JDive dive = it.next();
            ignored.put(dateFormat.format(dive.getDate()), dive);
        }
        int rowCount = model.getRowCount();
        for (int i=0; i<rowCount; i++) {
            JDive dive = model.getDive(i);
            if (!existing.containsKey(dateFormat.format(dive.getDate())) && !ignored.containsKey(dateFormat.format(dive.getDate()))) {
                table.addRowSelectionInterval(i, i);
            }
        }
    }
    
    public boolean hasSelectedRows() {
        return getImportTable().getSelectedRowCount() > 0;
    }
    
    private void editDive(JDive dive) {
        DiveDetailWindow ddw = new DiveDetailWindow(this, this.mainWindow, dive, this.mainWindow.getLogBook().getMasterdata(), true);
        ddw.setVisible(true);
    }
    
	/**
	 * This method initializes importTablePane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */    
	private JScrollPane getImportTablePane() {
		if (importTablePane == null) {
			importTablePane = new JScrollPane();
			importTablePane.setViewportView(getImportTable());
		}
		return importTablePane;
	}
  }
