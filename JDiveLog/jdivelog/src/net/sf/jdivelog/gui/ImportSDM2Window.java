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
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;

import net.sf.jdivelog.gui.commands.CommandAddDives;
import net.sf.jdivelog.gui.commands.CommandManager;
import net.sf.jdivelog.gui.resources.Messages;
import net.sf.jdivelog.model.JDive;
import net.sf.jdivelog.model.sdm2.SDM2Dive;
import net.sf.jdivelog.util.DateFormatUtil;
import net.sf.jdivelog.util.UnitConverter;

/**
 * Description: Window for importing Suunto Dive Manager 2.x files
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class ImportSDM2Window extends JDialog implements ActionListener {

    private static final Logger LOGGER = Logger.getLogger(ImportSDM2Window.class.getName());

    private static final long serialVersionUID = 3257283651766203193L;

    private MainWindow mainWindow = null;

    private TreeSet<SDM2Dive> dives = null;

    private JPanel jContentPane = null;

    private JPanel buttonPanel = null;

    private JButton importButton = null;

    private JButton cancelButton = null;

    private ImportTableModel importTableModel = null;

    private JTable importTable = null;

    private JScrollPane importTablePane = null;

    public ImportSDM2Window(Window parent, MainWindow mainWindow, TreeSet<SDM2Dive> dives) {
    	super(parent, ModalityType.APPLICATION_MODAL);
        this.mainWindow = mainWindow;
        this.dives = dives;
        initialize();
        new MnemonicFactory(this);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == cancelButton) {
            this.dispose();
        } else if (e.getSource() == importButton) {
            ArrayList<JDive> divesToAdd = new ArrayList<JDive>();
            int[] rows = getImportTable().getSelectedRows();
            for (int i = 0; i < rows.length; i++) {
                SDM2Dive dive = getImportTableModel().getDive(rows[i]);
                divesToAdd.add(dive.asJDive(mainWindow.getLogBook().getMasterdata()));
            }
            CommandAddDives cmd = new CommandAddDives(this.mainWindow, divesToAdd);
            CommandManager.getInstance().execute(cmd);
            this.dispose();
        }
    }

    private void initialize() {
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/net/sf/jdivelog/gui/resources/icons/logo.gif")));
        this.setSize(500, 400);
        this.setContentPane(getJContentPane());
        this.setTitle(Messages.getString("import_window_title")); //$NON-NLS-1$
    }

    private javax.swing.JPanel getJContentPane() {
        if (jContentPane == null) {
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
            gridBagConstraints1.weightx = 0.5;
            buttonPanel = new JPanel();
            buttonPanel.setLayout(new GridBagLayout());
            buttonPanel.setComponentOrientation(java.awt.ComponentOrientation.LEFT_TO_RIGHT);
            gridBagConstraints1.gridx = 0;
            gridBagConstraints1.gridy = 0;
            gridBagConstraints1.insets = new java.awt.Insets(5, 50, 5, 5);
            buttonPanel.add(getImportButton(), gridBagConstraints1);
            gridBagConstraints1.gridx = 1;
            gridBagConstraints1.gridy = 0;
            gridBagConstraints1.insets = new java.awt.Insets(5, 5, 5, 50);
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
            importTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            importTable.setShowGrid(false);
            importTable.setShowHorizontalLines(true);
            TableColumnModel tcm = importTable.getColumnModel();
            tcm.getColumn(0).setResizable(true);
            tcm.getColumn(0).setPreferredWidth(100);
            tcm.getColumn(0).setHeaderValue(Messages.getString("date")); //$NON-NLS-1$
            tcm.getColumn(1).setResizable(true);
            tcm.getColumn(1).setPreferredWidth(60);
            tcm.getColumn(1).setHeaderValue(Messages.getString("depth") + " [" + UnitConverter.getDisplayAltitudeUnit() + "]"); //$NON-NLS-1$
            tcm.getColumn(2).setResizable(true);
            tcm.getColumn(2).setPreferredWidth(60);
            tcm.getColumn(2).setHeaderValue(Messages.getString("duration") + " [" + UnitConverter.getDisplayTimeUnit() + "]"); //$NON-NLS-1$
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
            for (int i = 0; i < rowCount; i++) {
                SDM2Dive dive = model.getDive(i);
                try {
                    if (!existing.containsKey(dateFormat.format(dive.getDateObject())) && !ignored.containsKey(dateFormat.format(dive.getDateObject()))) {
                        importTable.addRowSelectionInterval(i, i);
                    }
                } catch (ParseException pex) {
                    LOGGER.log(Level.SEVERE, "error selecting dives", pex);
                }
            }

        }
        return importTable;
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

    //
    // inner classes
    //

    private static class ImportTableModel extends AbstractTableModel {

        private static final long serialVersionUID = -700459963398181032L;

        private static final NumberFormat DECIMALFORMAT = new DecimalFormat(Messages.getString("numberformat")); //$NON-NLS-1$

        private String[] columns = new String[] {
                Messages.getString("date"), Messages.getString("depth") + " [" + UnitConverter.getDisplayAltitudeUnit() + "]", Messages.getString("duration") + " [" + UnitConverter.getDisplayTimeUnit() + "]" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$

        private TreeSet<SDM2Dive> dives;

        public ImportTableModel(TreeSet<SDM2Dive> dives) {
            this.dives = dives;
        }

        public int getRowCount() {
            return dives.size();
        }

        public int getColumnCount() {
            return columns.length;
        }

        @Override
        public String getColumnName(int column) {
            return columns[column];
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            SDM2Dive dive = (SDM2Dive) dives.toArray()[rowIndex];
            UnitConverter uc = new UnitConverter(UnitConverter.SYSTEM_SI, UnitConverter.getDisplaySystem());
            switch (columnIndex) {
            case 0:
                try {
                    return DateFormatUtil.getDateTimeFormat().format(dive.getDateObject());
                } catch (ParseException pex) {
                    LOGGER.log(Level.SEVERE, "error parsing date", pex);
                }
                break;
            case 1:
                return dive.getDepth();
            case 2:
                return dive.getDuration() == null ? null : DECIMALFORMAT.format(uc.convertTime(dive.getDuration()));
            }
            return null;
        }

        public SDM2Dive getDive(int row) {
            return (SDM2Dive) dives.toArray()[row];
        }

    }
}
