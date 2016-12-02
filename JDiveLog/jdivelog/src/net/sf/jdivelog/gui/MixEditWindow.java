/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: GasEditWindow.java
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

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;

import javax.swing.BorderFactory;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.text.JTextComponent;

import net.sf.jdivelog.gui.resources.Messages;
import net.sf.jdivelog.model.MixDatabase;
import net.sf.jdivelog.model.Mix;

public class MixEditWindow extends JDialog {
    
    private static final long serialVersionUID = 1L;
    private static final DecimalFormat DECIMALFORMAT = new DecimalFormat("0.00");
    private final MixDatabase db;
    private JPanel currentGasPanel;
    private JPanel favoritesPanel;
    private JPanel buttonPanel;
    private JButton closeButton;
    private JButton cancelButton;
    private JTextField gasField;
    private JTextField oxygenField;
    private JTextField heliumField;
    private JTextField nitrogenField;
    private JTextField ppO2Field;
    private JTextField modField;
    private JTextField endField;
    private JTextField changeField;
    private BigDecimal stepSize;
    private boolean update;
    private String lastOxy;
    private String lastHe;
    private String lastPpO2;
    private String lastMod;
    private Mix mix;
    private JTable favoritesTable;
    private AbstractTableModel favoritesTableModel;
    private JButton addFavoriteButton;
    private JButton removeFavoriteButton;

    public MixEditWindow(Window parent, MixDatabase db) {
        super(parent, ModalityType.APPLICATION_MODAL);
        this.db = db;
        inititalize();
    }
    
    public MixEditWindow(Dialog parent, MixDatabase db) {
        super(parent, ModalityType.APPLICATION_MODAL);
        this.db = db;
        inititalize();
    }
    
    public MixEditWindow(Frame parent, MixDatabase db) {
        super(parent, ModalityType.APPLICATION_MODAL);
        this.db = db;
        inititalize();
    }
    
    private void inititalize() {
        stepSize = new BigDecimal(3.0);
        setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridy = 0;
        gc.gridx = 0;
        add(getCurrentGasPanel(), gc);
        gc.gridy = 1;
        add(getFavoritesPanel(), gc);
        gc.gridy = 2;
        add(getButtonPanel(), gc);
        new MnemonicFactory(this);
        pack();
    }
    
    private JPanel getCurrentGasPanel() {
        if (currentGasPanel == null) {
            currentGasPanel = new JPanel();
            currentGasPanel.setLayout(new GridBagLayout());
            GridBagConstraints gc = new GridBagConstraints();
            gc.insets = new Insets(5,5,5,5);
            JPanel p1 = new JPanel();
            p1.setLayout(new GridBagLayout());
            JPanel p2 = new JPanel();
            p2.setLayout(new GridBagLayout());
            JPanel p3 = new JPanel();
            p3.setLayout(new GridBagLayout());
            gc.fill=GridBagConstraints.BOTH;
            gc.gridy = 0;
            gc.gridx = 0;
            currentGasPanel.add(p1, gc);
            gc.gridx = 1;
            currentGasPanel.add(p2, gc);
            gc.gridx = 2;
            currentGasPanel.add(p3, gc);
            
            gc.fill = GridBagConstraints.NONE;
            gc.anchor = GridBagConstraints.WEST;
            gc.gridy = 0;
            gc.gridx = 0;
            p1.add(new JLabel(Messages.getString("gas")), gc);
            gc.gridx = 1;
            p1.add(getGasField(), gc);
            gc.gridy = 1;
            gc.gridx = 0;
            p1.add(new JLabel(Messages.getString("oxygen_percent")), gc);
            gc.gridx = 1;
            p1.add(getOxygenField(), gc);
            gc.gridy = 2;
            gc.gridx = 0;
            p1.add(new JLabel(Messages.getString("helium_percent")), gc);
            gc.gridx = 1;
            p1.add(getHeliumField(), gc);        
            gc.gridy = 3;
            gc.gridx = 0;
            p1.add(new JLabel(Messages.getString("nitrogen_percent")), gc);
            gc.gridx = 1;
            p1.add(getNitrogenField(), gc);        

            gc.gridy = 0;
            gc.gridx = 0;
            p2.add(new JLabel("ppO2"), gc);
            gc.gridx = 1;
            p2.add(getPpO2Field(), gc);
            gc.gridy = 1;
            gc.gridx = 0;
            p2.add(new JLabel("MOD"), gc);
            gc.gridx = 1;
            p2.add(getModField(), gc);
            gc.gridy = 2;
            gc.gridx = 0;
            p2.add(new JLabel("END"), gc);
            gc.gridx = 1;
            p2.add(getEndField(), gc);        
            gc.gridy = 4;
            gc.gridx = 0;
            p2.add(new JLabel(Messages.getString("change_depth")), gc);
            gc.gridx = 1;
            p2.add(getChangeField(), gc);
            
            gc.gridy = 0;
            gc.gridx = 0;
            p3.add(getAddFavoriteButton(), gc);
            gc.gridy = 1;
            p3.add(getRemoveFavoriteButton(), gc);
            
            TitledBorder b = BorderFactory.createTitledBorder(Messages.getString("gas"));
            currentGasPanel.setBorder(b);
        }
        return currentGasPanel;
    }
    
    private JTextField getGasField() {
        if (gasField == null) {
            gasField = new JTextField(10);
            gasField.setEditable(false);
        }
        return gasField;
    }
    
    private JTextField getOxygenField() {
        if (oxygenField == null) {
            oxygenField = new JTextField(3);
            oxygenField.addCaretListener(new CaretListener() {

                public void caretUpdate(CaretEvent e) {
                    oxyChanged();
                }});
            oxygenField.addFocusListener(new FocusListener() {

                public void focusGained(FocusEvent e) {
                    oxygenField.selectAll();
                }

                public void focusLost(FocusEvent e) {
                    oxyChanged();
                }});
            oxygenField.setInputVerifier(new PercentageVerifier());
        }
        return oxygenField;
    }
    
    private JTextField getHeliumField() {
        if (heliumField == null) {
            heliumField = new JTextField(3);
            heliumField.addCaretListener(new CaretListener() {

                public void caretUpdate(CaretEvent e) {
                    heChanged();
                }});
            heliumField.addFocusListener(new FocusListener() {

                public void focusGained(FocusEvent e) {
                    heliumField.selectAll();
                }

                public void focusLost(FocusEvent e) {
                    heChanged();
                }});
            heliumField.setInputVerifier(new PercentageVerifier());
        }
        return heliumField;
    }
    
    private JTextField getNitrogenField() {
        if (nitrogenField == null) {
            nitrogenField = new JTextField(3);
            nitrogenField.setEditable(false);
            nitrogenField.setFocusable(false);
        }
        return nitrogenField;
    }
    
    private JTextField getPpO2Field() {
        if (ppO2Field == null) {
            ppO2Field = new JTextField(6);
            ppO2Field.setText(DECIMALFORMAT.format(1.6));
            ppO2Field.addCaretListener(new CaretListener() {

                public void caretUpdate(CaretEvent e) {
                    ppO2Changed();
                }});
            ppO2Field.addFocusListener(new FocusListener() {

                public void focusGained(FocusEvent e) {
                    ppO2Field.selectAll();
                }

                public void focusLost(FocusEvent e) {
                    ppO2Changed();
                }});
            ppO2Field.setInputVerifier(new PositiveDoubleVerifier());
        }
        return ppO2Field;
    }
    
    private JTextField getModField() {
        if (modField == null) {
            modField = new JTextField(3);
            modField.addCaretListener(new CaretListener() {

                public void caretUpdate(CaretEvent e) {
                    modChanged();
                }});
            modField.addFocusListener(new FocusListener() {

                public void focusGained(FocusEvent e) {
                    modField.selectAll();
                }

                public void focusLost(FocusEvent e) {
                    modChanged();
                }});
            modField.setInputVerifier(new PositiveIntegerVerifier());
        }
        return modField;
    }
    
    private JTextField getEndField() {
        if (endField == null) {
            endField = new JTextField(3);
            endField.setEditable(false);
            endField.setFocusable(false);
        }
        return endField;
    }
    
    private JTextField getChangeField() {
        if (changeField ==  null) {
            changeField = new JTextField(10);
            changeField.setEditable(false);
            changeField.setFocusable(false);
        }
        return changeField;
    }
    
    private void oxyChanged() {
        if (!update) {
            if (!getOxygenField().getText().equals(lastOxy)) {
                updateNitrogen();
                updateName();
                updateMOD();
                updateEND();
                updateChange();
            }
        }
        lastOxy = getOxygenField().getText();
    }
    
    private void heChanged() {
        if (!update) {
            if (!getHeliumField().equals(lastHe)) {
                updateNitrogen();
                updateName();
                updateMOD();
                updateEND();
                updateChange();
            }
        }
        lastHe = getHeliumField().getText();
    }
    
    private void ppO2Changed() {
        if (!update) {
            if (!getPpO2Field().getText().equals(lastPpO2)) {
                updateMOD();
                updateEND();
                updateChange();
            }
        }
        lastPpO2 = getPpO2Field().getText();
    }
    
    private void modChanged() {
        if (!update) {
            if (!getModField().getText().equals(lastMod)) {
                updatePpO2();
                updateEND();
                updateChange();
            }
        }
        lastMod = getModField().getText();
    }
    
    private int getOxygen() {
        String s = getOxygenField().getText();
        int result = 0;
        if (s != null && !"".equals(s)) {
            try {
                result = Integer.parseInt(s.trim());
            } catch (NumberFormatException e) {
            }
        }
        return result;
    }
    
    private int getHelium() {
        String s = getHeliumField().getText();
        int result = 0;
        if (s != null && !"".equals(s)) {
            try {
                result = Integer.parseInt(s.trim());
            } catch (NumberFormatException e) {
                
            }
        }
        return result;
    }
    
    private double getPpO2() {
        String s = getPpO2Field().getText();
        double result = 0.0;
        if (s != null && !"".equals(s)) {
            try {
                result = DECIMALFORMAT.parse(s).doubleValue();
            } catch (ParseException e) {
                result = 1.6;
            }
        } else {
            result = 1.6;
        }
        return result;
    }
    
    private int getMOD() {
        String s = getModField().getText();
        int result = 0;
        if (s != null && !"".equals(s)) {
            try {
                result = Integer.parseInt(s.trim());                
            } catch (NumberFormatException e) {
                
            }
        }
        return result;
    }
    
    private int getNitrogen() {
        return 100 - getOxygen() - getHelium();
    }
    
    private void updateNitrogen() {
        boolean old = update;
        update = true;
        int nitrogen = 100-getOxygen()-getHelium();
        getNitrogenField().setText(String.valueOf(nitrogen));
        update = old;
    }
    
    private void updateName() {
        int helium = getHelium();
        int oxygen = getOxygen();
        String name = Mix.createName(oxygen, helium);
        getGasField().setText(name);
    }

    private void updateMOD() {
        boolean old = update;
        update = true;
        int mod = Mix.calcMOD(getOxygen(), getPpO2());
        getModField().setText(String.valueOf(mod));
        update = old;
    }
    
    private void updateEND() {
        int end = getMOD() * getNitrogen() / 79;
        getEndField().setText(String.valueOf(end));
    }
    
    private void updatePpO2() {
        boolean old = update;
        update = true;
        double ppO2 = (getMOD()/10.0 + 1) * getOxygen() / 100.0;
        getPpO2Field().setText(DECIMALFORMAT.format(ppO2));
        update = old;
    }
    
    private void updateChange() {
        boolean old = update;
        update = true;
        double change = Mix.calcChangeDepth(getMOD(), stepSize);
        getChangeField().setText(DECIMALFORMAT.format(change));
        update = old;
    }
    
    public static int calcEND(int mod, int nitrogen) {
        return mod * nitrogen / 79;
    }

    private JPanel getFavoritesPanel() {
        if (favoritesPanel == null) {
            favoritesPanel = new JPanel();
            favoritesPanel.add(new JScrollPane(getFavoritesTable()));
            TitledBorder b = BorderFactory.createTitledBorder(Messages.getString("favorite_gases"));
            favoritesPanel.setBorder(b);
        }
        return favoritesPanel;
    }
    
    private JTable getFavoritesTable() {
        if (favoritesTable == null) {
            favoritesTable = new JTable(getFavoritesTableModel());
            ListSelectionModel selectionModel = favoritesTable.getSelectionModel();
            selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            selectionModel.addListSelectionListener(new ListSelectionListener() {

                public void valueChanged(ListSelectionEvent e) {
                    int selected = favoritesTable.getSelectedRow();
                    if (selected == -1) {
                        getRemoveFavoriteButton().setEnabled(false);
                    } else {
                        getRemoveFavoriteButton().setEnabled(true);
                        Mix m = db.getFavorites().get(selected);
                        if (m != null) {
                            setMix(m);
                        }
                    }
                }
            });
        }
        return favoritesTable;
    }
    
    private AbstractTableModel getFavoritesTableModel() {
        if (favoritesTableModel == null) {
            favoritesTableModel = new AbstractTableModel() {

                private static final long serialVersionUID = 1L;

                public int getColumnCount() {
                    return 6;
                }

                public int getRowCount() {
                    return db.getFavorites().size();
                }
                
                @Override
                public String getColumnName(int column) {
                    switch (column) {
                    case 0:
                        return Messages.getString("gas");
                    case 1:
                        return Messages.getString("oxygen");
                    case 2:
                        return Messages.getString("helium");
                    case 3:
                        return "ppO2";
                    case 4:
                        return "MOD";
                    case 5:
                        return Messages.getString("change_depth");
                    }
                    return "";
                }
                
                @Override
                public Class<?> getColumnClass(int columnIndex) {
                    switch(columnIndex) {
                    case 0:
                        return String.class;
                    case 1:
                        return Integer.class;
                    case 2:
                        return Integer.class;
                    case 3:
                        return String.class;
                    case 4:
                        return Integer.class;
                    case 5:
                        return String.class;
                    }
                    return Object.class;
                }
                
                public Object getValueAt(int rowIndex, final int columnIndex) {
                    if (rowIndex >= 0 && rowIndex < db.getFavorites().size()) {
                        final Mix m = db.getFavorites().get(rowIndex);
                        if (m != null) {
                            switch(columnIndex) {
                            case 0:
                                return m.getName();
                            case 1:
                                return m.getOxygen();
                            case 2:
                                return m.getHelium();
                            case 3:
                                return DECIMALFORMAT.format(m.getPpO2());
                            case 4:
                                return m.getMod();
                            case 5:
                                return DECIMALFORMAT.format(m.getChange());
                            }
                        }
                    }
                    return null;
                }
                
            };
        }
        return favoritesTableModel;
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
            gridBagConstraints1.insets = new java.awt.Insets(5,30,5,5);
            buttonPanel.add(getCloseButton(), gridBagConstraints1);
            gridBagConstraints1.gridx = 1;
            gridBagConstraints1.gridy = 0;
            gridBagConstraints1.insets = new java.awt.Insets(5,5,5,30);
            buttonPanel.add(getCancelButton(), gridBagConstraints1);
        }
        return buttonPanel;
    }

    private JButton getCloseButton() {
        if (closeButton == null) {
            closeButton = new JButton();
            closeButton.setText(Messages.getString("close")); //$NON-NLS-1$
            closeButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (save()) {
                        dispose();
                    }                    
                }
            });
        }
        return closeButton;
    }
    
    private JButton getCancelButton() {
        if (cancelButton == null) {
            cancelButton = new JButton();
            cancelButton.setText(Messages.getString("cancel")); //$NON-NLS-1$
            cancelButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    dispose();
                }
            });
        }
        return cancelButton;
    }

    private JButton getAddFavoriteButton() {
        if (addFavoriteButton == null) {
            addFavoriteButton = new JButton(Messages.getString("add_to_favorites"));
            addFavoriteButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    Mix m = buildMix();
                    if (m != null) {
                        db.addFavorite(m);
                        getFavoritesTableModel().fireTableDataChanged();
                    }
                }
            });
        }
        return addFavoriteButton;
    }
    
    private JButton getRemoveFavoriteButton() {
        if (removeFavoriteButton == null) {
            removeFavoriteButton = new JButton(Messages.getString("remove_from_favorites"));
            removeFavoriteButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    int idx = getFavoritesTable().getSelectedRow();
                    if (idx != -1) {
                        Mix m = db.getFavorites().get(idx);
                        db.removeFavorite(m);
                        getFavoritesTableModel().fireTableDataChanged();
                    }
                }
                
            });
        }
        return removeFavoriteButton;
    }

    private boolean save() {
        mix = buildMix();
        return mix != null;
    }
    
    private Mix buildMix() {
        String name = getGasField().getText();
        if (name == null || "".equals(name)) {
            // TODO show error
            return null;
        }
        if (getOxygen() <= 0 || getOxygen() > 100) {
            // TODO show error
            return null;
        }
        // TODO more checks!
        return new Mix(getGasField().getText(), getOxygen(), getHelium(), getPpO2(), getMOD(), getChange());
    }

    /**
     * @return the stepSize
     */
    public BigDecimal getStepSize() {
        return stepSize;
    }

    /**
     * @param stepSize the stepSize to set
     */
    public void setStepSize(BigDecimal stepSize) {
        this.stepSize = stepSize;
    }
    
    public void setMix(Mix mix) {
        if (mix == null) {
            mix = Mix.AIR;
        }
        getOxygenField().setText(String.valueOf(mix.getOxygen()));
        getHeliumField().setText(String.valueOf(mix.getHelium()));
        getPpO2Field().setText(DECIMALFORMAT.format(mix.getPpO2()));
        getModField().setText(String.valueOf(mix.getMod()));
        getChangeField().setText(DECIMALFORMAT.format(mix.getChange()));
        getOxygenField().grabFocus();
    }
    
    public Mix getMix() {
        return mix;
    }
    
    public static Mix edit(Window window, MixDatabase db, Mix m) {
        MixEditWindow gew = new MixEditWindow(window, db);
        gew.setMix(m);
        gew.setVisible(true);
        return gew.getMix();
    }

    private double getChange() {
        double result = 0.0;
        String s = getChangeField().getText();
        if (s != null && !"".equals(s)) {
            try {
                result = DECIMALFORMAT.parse(getChangeField().getText()).doubleValue();
            } catch (ParseException e) {
            }
        }
        return result;
    }
    
    private static class PercentageVerifier extends InputVerifier {

        @Override
        public boolean verify(JComponent input) {
            if (input instanceof JTextComponent) {
                JTextComponent tc = (JTextComponent)input;
                String t = tc.getText();
                if (t != null && !"".equals(t)) {
                    try {
                        int i = Integer.parseInt(t);
                        return i >= 0 && i <= 100;
                    } catch (NumberFormatException e) {
                        return false;
                    }
                }
                return true;
            }
            return false;
        }
    }
    
    private static class PositiveIntegerVerifier extends InputVerifier {

        @Override
        public boolean verify(JComponent input) {
            if (input instanceof JTextComponent) {
                JTextComponent tc = (JTextComponent)input;
                String t = tc.getText();
                if (t != null && !"".equals(t)) {
                    try {
                        int i = Integer.parseInt(t);
                        return i >= 0;
                    } catch (NumberFormatException e) {
                        return false;
                    }
                }
                return true;
            }
            return false;
        }
    }
    
    private static class PositiveDoubleVerifier extends InputVerifier {
        
        @Override
        public boolean verify(JComponent input) {
            if (input instanceof JTextComponent) {
                JTextComponent tc = (JTextComponent)input;
                String t = tc.getText();
                if (t != null && !"".equals(t)) {
                    try {
                        double i = DECIMALFORMAT.parse(t).doubleValue();
                        return i >= 0.0;
                    } catch (NumberFormatException e) {
                        return false;
                    } catch (ParseException e) {
                        return false;
                    }
                }
                return true;
            }
            return false;
        }
    }

}
