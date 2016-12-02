/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: ProfileSelectionWindow.java
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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.TreeSet;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import net.sf.jdivelog.gui.resources.Messages;
import net.sf.jdivelog.model.JDive;
import net.sf.jdivelog.model.JDiveLog;

public class ProfileSelectionWindow extends JDialog implements ActionListener {

    private static final long serialVersionUID = 1L;
    
    private JDiveLog logbook;
    private JScrollPane logBookPane;
    private JTable logBookTable;
    private LogBookTableModel logBookTableModel;
    private JPanel buttonPanel;
    private JButton cancelButton;
    private JButton closeButton;
    private TreeSet<JDive> selectedDives;

    private final boolean multiselect;
    
    public ProfileSelectionWindow(Window owner, JDiveLog logbook) throws HeadlessException {
        this(owner, logbook, true);
    }

    public ProfileSelectionWindow(Window owner, JDiveLog logbook, boolean multiselect) throws HeadlessException {
        super(owner);
        this.logbook = logbook;
        this.multiselect = multiselect;
        this.selectedDives = new TreeSet<JDive>();
        initialize();
        new MnemonicFactory(this);
    }
    
    public TreeSet<JDive> getSelectedDives() {
        return selectedDives;
    }
    

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == closeButton) {
            selectedDives.clear();
            int[] selectedRows = getLogBookTable().getSelectedRows();
            for (int i=0;i<selectedRows.length;i++) {
                JDive dive = getLogBookTableModel().getDive(selectedRows[i]);
                selectedDives.add(dive);
            }
            this.dispose();
        } else if (e.getSource() == cancelButton) {
            this.dispose();
        }
    }
    
    //
    // private methods
    //
    
    private void initialize() {
        setSize(600, 700);
        setLayout(new BorderLayout());
        setTitle(Messages.getString("choose_profiles"));
        add(getLogBookPane(), BorderLayout.CENTER);
        add(getButtonPanel(), BorderLayout.SOUTH);
    }
    
    private JScrollPane getLogBookPane() {
        if (logBookPane == null) {
            logBookPane = new JScrollPane(getLogBookTable());
        }
        return logBookPane;
    }
    
    private JTable getLogBookTable() {
        if (logBookTable == null) {
            logBookTable = new JTable(getLogBookTableModel());
            logBookTable.setSelectionMode(multiselect ? ListSelectionModel.MULTIPLE_INTERVAL_SELECTION : ListSelectionModel.SINGLE_SELECTION);
        }
        return logBookTable;
    }
    
    private LogBookTableModel getLogBookTableModel() {
        if (logBookTableModel == null) {
            TreeSet<JDive> divesWithProfile = new TreeSet<JDive>();
            for (Iterator<JDive> it = logbook.getDives().iterator(); it.hasNext();) {
                JDive dive = it.next();
                if (dive.getDive() != null) {
                    divesWithProfile.add(dive);
                }
            }
            logBookTableModel = new LogBookTableModel(logbook, divesWithProfile);
        }
        return logBookTableModel;
    }
    
    private JPanel getButtonPanel() {
        if (buttonPanel == null) {
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints1.weightx = 0.5;
            buttonPanel = new JPanel();
            buttonPanel.setLayout(new GridBagLayout());
            buttonPanel
                    .setComponentOrientation(java.awt.ComponentOrientation.LEFT_TO_RIGHT);
            gridBagConstraints1.gridx = 0;
            gridBagConstraints1.gridy = 0;
            gridBagConstraints1.insets = new java.awt.Insets(5, 100, 5, 5);
            buttonPanel.add(getCloseButton(), gridBagConstraints1);
            gridBagConstraints1.gridx = 1;
            gridBagConstraints1.gridy = 0;
            gridBagConstraints1.insets = new java.awt.Insets(5, 5, 5, 100);
            buttonPanel.add(getCancelButton(), gridBagConstraints1);
        }
        return buttonPanel;
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

}
