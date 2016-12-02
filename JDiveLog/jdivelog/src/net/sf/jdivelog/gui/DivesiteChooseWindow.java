/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: DivesiteChooseWindow.java
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
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import net.sf.jdivelog.gui.resources.Messages;
import net.sf.jdivelog.gui.util.TableSorter;
import net.sf.jdivelog.model.DiveSite;
import net.sf.jdivelog.model.Masterdata;

/**
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class DivesiteChooseWindow extends JDialog implements ActionListener {

    private static final long serialVersionUID = -8472886889604464830L;
    
    private DiveDetailPanel diveDetailPanel;
    private Masterdata masterdata;

    private JScrollPane contentPanel;

    private JPanel buttonPanel;

    private JButton closeButton;

    private JButton cancelButton;

    private JTable diveSiteTable;
    
    private TableSorter sortedDiveSiteTableModel;

    private DivesiteTableModel diveSiteTableModel;
    
    public DivesiteChooseWindow(Window parent, DiveDetailPanel diveDetailPanel, Masterdata masterdata) {
        super(parent, ModalityType.APPLICATION_MODAL);
        this.diveDetailPanel = diveDetailPanel;
        this.masterdata = masterdata;
        initialize();
        new MnemonicFactory(this);
    }
    
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == closeButton) {
            int[] selected = getDiveSiteTable().getSelectedRows();
            if (selected.length == 1) { 
                if (diveDetailPanel != null) {
                    DiveSite site = (DiveSite)getSortedDiveSiteTableModel().getRow(selected[0]);
                    diveDetailPanel.setSite(site);
                }
                this.dispose();
            } else {
                if (diveDetailPanel != null) {
                    diveDetailPanel.setSite(null);
                }
                this.dispose();
            }
        } else if (e.getSource() == cancelButton) {
            this.dispose();
        }
    }

    //
    // private methods
    //
    
    private void initialize() {
        setSize(500, 600);
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/net/sf/jdivelog/gui/resources/icons/logo.gif")));
        setTitle(Messages.getString("choose_site"));
        setLayout(new BorderLayout());
        add(getContentPanel(), BorderLayout.CENTER);
        add(getButtonPanel(), BorderLayout.SOUTH);
    }

    private JScrollPane getContentPanel() {
        if (contentPanel == null) {
            contentPanel = new JScrollPane(getDiveSiteTable());
        }
        return contentPanel;
    }
    
    private JTable getDiveSiteTable() {
        if (diveSiteTable == null) {
            diveSiteTable = new JTable(getSortedDiveSiteTableModel());
            getSortedDiveSiteTableModel().addMouseListenerToHeader(diveSiteTable);
            diveSiteTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        }
        return diveSiteTable;
    }
    
    private TableSorter getSortedDiveSiteTableModel() {
        if (sortedDiveSiteTableModel == null) {
            sortedDiveSiteTableModel = new TableSorter(getDiveSiteTableModel());
        }
        return sortedDiveSiteTableModel;
    }
    
    private DivesiteTableModel getDiveSiteTableModel() {
        if (diveSiteTableModel == null) {
            diveSiteTableModel = new DivesiteTableModel(masterdata);
        }
        return diveSiteTableModel;
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
