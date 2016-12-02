/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: EquipmentWindow.java
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;

import net.sf.jdivelog.gui.LogbookChangeEvent.EventType;
import net.sf.jdivelog.gui.resources.Messages;
import net.sf.jdivelog.gui.util.AutoCompleteDictionary;
import net.sf.jdivelog.gui.util.AutoCompleteTextField;
import net.sf.jdivelog.gui.util.DefaultDictionary;
import net.sf.jdivelog.model.EquipmentSet;
import net.sf.jdivelog.model.GloveType;
import net.sf.jdivelog.model.Masterdata;
import net.sf.jdivelog.model.Suit;
import net.sf.jdivelog.util.UnitConverter;

public class EquipmentWindow extends JDialog implements ActionListener, LogbookChangeListener {

    private static final long serialVersionUID = 1L;
    private MainWindow parent = null;
    private JTabbedPane jTabbedPane = null;
    private JPanel suitPanel = null;
    private JPanel glovesPanel = null;
    private JPanel setsPanel = null;
    private JScrollPane suitTablePane;
    private JTable suitTable;
    private SuitTableModel suitTableModel;
    private JPanel contentPanel;
    private JPanel buttonPanel;
    private JButton closeButton;
    private JButton cancelButton;
    private JPanel suitButtonPanel;
    private JButton suitAddButton;
    private JButton suitRemoveButton;
    private JTextField suitDescription;
    private JScrollPane glovesTablePane;
    private JTable glovesTable;
    private GloveTypeTableModel glovesTableModel;
    private JPanel glovesButtonPanel;
    private JTextField glovesDescription;
    private JButton glovesAddButton;
    private JButton glovesRemoveButton;
    private JScrollPane setsTablePane;
    private JTable setsTable;
    private EquipmentSetTableModel setsTableModel;
    private JPanel setsButtonPanel;
    private JTextField setName;
    private JButton setAddButton;
    private JButton setRemoveButton;
    private JTextField suit;
    private AutoCompleteDictionary suitDictionary;
    private JTextField gloves;
    private AutoCompleteDictionary glovesDictionary;
    private JTextField tankVolume;
    private JComboBox tankType;
    private JTextField weight;

    private boolean integrated;
    private final LogbookChangeNotifier logbookChangeNotifier;
    
    public EquipmentWindow(MainWindow parent, LogbookChangeNotifier logbookChangeNotifier) {
        this(parent, false, logbookChangeNotifier);
    }

    public EquipmentWindow(MainWindow parent, boolean integrated, LogbookChangeNotifier logbookChangeNotifier) {
    	super(parent, ModalityType.APPLICATION_MODAL);
    	this.parent = parent;
        this.integrated = integrated;
        this.logbookChangeNotifier = logbookChangeNotifier;
        if (integrated) {
            logbookChangeNotifier.addLogbookChangeListener(this);
        }
    	initialize();
        new MnemonicFactory(this);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == suitAddButton){
            Suit suit = new Suit();
            suit.setDescription(getSuitDescription().getText());
            suitTableModel.add(suit);
            getSuitDictionary().addEntry(suit.getDescription());
            getSuitDescription().setText("");
            if (integrated) {
                save_data();
            }
        } else if (e.getSource() == suitRemoveButton) {
            int idx = suitTable.getSelectedRow();
            if (idx >= 0) {
                String value = (String)suitTableModel.getValueAt(idx, 0);
                suitTableModel.remove(suitTable.getSelectedRow());
                getSuitDictionary().removeEntry(value);
            }
            if (integrated) {
                save_data();
            }
        } else if(e.getSource() == glovesAddButton) {
            GloveType glove = new GloveType();
            glove.setDescription(getGlovesDescription().getText());
            glovesTableModel.add(glove);
            getGlovesDictionary().addEntry(glove.getDescription());
            getGlovesDescription().setText("");
            if (integrated) {
                save_data();
            }
        } else if (e.getSource() == glovesRemoveButton) {
            int idx = glovesTable.getSelectedRow();
            if (idx >= 0) {
                String value = (String)glovesTableModel.getValueAt(idx, 0);
                glovesTableModel.remove(glovesTable.getSelectedRow());
                getGlovesDictionary().removeEntry(value);
            }
            if (integrated) {
                save_data();
            }
        } else if(e.getSource() == setAddButton) {
            EquipmentSet set = new EquipmentSet();
            set.setName(getSetName().getText());
            set.setSuit(getSuit().getText());
            set.setGloves(getGloves().getText());
            set.setWeight(getWeight().getText());
            try {
                String vol = getTankVolume().getText();
                Double.parseDouble(vol);
                set.setTankVolume(vol);
            } catch (NumberFormatException nfe) {
                set.setTankVolume("12");
            }
            set.setTankType((String)getTankType().getSelectedItem());
            setsTableModel.add(set);
            getSetName().setText("");
            getSuit().setText("");
            getGloves().setText("");
            getWeight().setText("");
            getTankVolume().setText("");
            if (integrated) {
                save_data();
            }
        } else if (e.getSource() == setRemoveButton) {
            int idx = setsTable.getSelectedRow();
            if (idx >= 0) {
                setsTableModel.remove(idx);
            }
            if (integrated) {
                save_data();
            }
        } else if (e.getSource() == closeButton) {
            save_data();
            dispose();
        } else if (e.getSource() == cancelButton) {
            dispose();
        }
    }
    
    public void logbookChanged(LogbookChangeEvent e) {
        if (!EventType.LOGBOOK_TITLE_CHANGED.equals(e.getType())) {
            load();
        }
    }
        
    //
    // private methods
    //    
    
    private void save_data() {
    	Masterdata masterdata = parent.getLogBook().getMasterdata();
        ArrayList<Suit> masterSuitList = masterdata.getSuits();
        masterSuitList.clear();
        masterSuitList.addAll(getSuitTableModel().getAll());
        ArrayList<GloveType> masterGloveList = masterdata.getGloveTypes();
        masterGloveList.clear();
        masterGloveList.addAll(getGlovesTableModel().getAll());
        ArrayList<EquipmentSet> masterSetList = masterdata.getEquipmentSets();
        masterSetList.clear();
        masterSetList.addAll(getSetsTableModel().getAll());
        logbookChangeNotifier.setChanged(true);
        logbookChangeNotifier.notifyLogbookDataChanged();
    }
    
    private void load() {
    	Masterdata masterdata = parent.getLogBook().getMasterdata();
        getSuitTableModel().load(masterdata.getSuits());
        getGlovesTableModel().load(masterdata.getGloveTypes());
        getSetsTableModel().load(masterdata.getEquipmentSets());
    }

    private void initialize() {
        this.setSize(806, 521);
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/net/sf/jdivelog/gui/resources/icons/logo.gif")));
        setContentPane(getContentPanel());
        this.setTitle(Messages.getString("equipment")); //$NON-NLS-1$
        this.setName(Messages.getString("equipment")); //$NON-NLS-1$
        load();
    }

    public JPanel getContentPanel() {
        if (contentPanel == null) {
            contentPanel = new JPanel();
            contentPanel.setLayout(new BorderLayout());
            contentPanel.add(getJTabbedPane(), BorderLayout.CENTER);
            contentPanel.add(getButtonPanel(), BorderLayout.SOUTH);
        }
        return contentPanel;
    }
    
    private JTabbedPane getJTabbedPane() {
    	if (jTabbedPane == null) {
    		jTabbedPane = new JTabbedPane();
    		jTabbedPane.setName("");
    		jTabbedPane.addTab(Messages.getString("suits"), null, getSuitPanel(), null);
            jTabbedPane.addTab(Messages.getString("gloves"), null, getGlovesPanel(), null);
            jTabbedPane.addTab(Messages.getString("equipmentsets"), null, getSetsPanel(), null);
    	}
    	return jTabbedPane;
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
            if (!integrated) {
                gridBagConstraints1.gridx = 0;            
                gridBagConstraints1.gridy = 0;
                gridBagConstraints1.insets = new java.awt.Insets(5, 100, 5, 5);
                buttonPanel.add(getCloseButton(), gridBagConstraints1);
                gridBagConstraints1.gridx = 1;
                gridBagConstraints1.gridy = 0;
                gridBagConstraints1.insets = new java.awt.Insets(5, 5, 5, 100);
                buttonPanel.add(getCancelButton(), gridBagConstraints1);
            }
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

    private JPanel getSuitPanel() {
    	if (suitPanel == null) {
    		suitPanel = new JPanel();
            suitPanel.setLayout(new BorderLayout());
            suitPanel.add(getSuitTablePane(), BorderLayout.CENTER);
            suitPanel.add(getSuitButtonPanel(), BorderLayout.SOUTH);
    	}
    	return suitPanel;
    }
    
    private JScrollPane getSuitTablePane() {
        if (suitTablePane == null) {
            suitTablePane = new JScrollPane(getSuitTable());
        }
        return suitTablePane;
    }
    
    private JTable getSuitTable() {
        if (suitTable == null) {
            suitTable = new JTable(getSuitTableModel());
            TableColumnModel columnModel = suitTable.getColumnModel();
            columnModel.getColumn(0).setHeaderValue(Messages.getString("suit"));
        }
        return suitTable;
    }
    
    private SuitTableModel getSuitTableModel() {
        if (suitTableModel == null) {
            suitTableModel = new SuitTableModel();
        }
        return suitTableModel;
    }
    
    private JPanel getSuitButtonPanel() {
        if (suitButtonPanel == null) {
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints1.weightx=0.5;
            suitButtonPanel = new JPanel();
            suitButtonPanel.setLayout(new GridBagLayout());
            suitButtonPanel.setComponentOrientation(java.awt.ComponentOrientation.LEFT_TO_RIGHT);
            gridBagConstraints1.gridx = 0;
            gridBagConstraints1.gridy = 0;
            gridBagConstraints1.insets = new java.awt.Insets(5,5,5,5);
            suitButtonPanel.add(new JLabel(Messages.getString("description")), gridBagConstraints1);
            gridBagConstraints1.gridx = 0;
            gridBagConstraints1.gridy = 1;
            gridBagConstraints1.insets = new java.awt.Insets(5,5,5,5);
            suitButtonPanel.add(getSuitDescription(), gridBagConstraints1);
            gridBagConstraints1.gridx = 1;
            gridBagConstraints1.gridy = 1;
            gridBagConstraints1.insets = new java.awt.Insets(5,5,5,5);
            suitButtonPanel.add(getSuitAddButton(), gridBagConstraints1);
            gridBagConstraints1.gridx = 1;
            gridBagConstraints1.gridy = 0;
            gridBagConstraints1.insets = new java.awt.Insets(5,5,5,5);
            suitButtonPanel.add(getSuitRemoveButton(), gridBagConstraints1);
        }
        return suitButtonPanel;
    }
    
    private JTextField getSuitDescription() {
        if (suitDescription == null) {
            suitDescription = new JTextField();
        }
        return suitDescription;
    }
    
    private JButton getSuitAddButton() {
        if (suitAddButton == null) {
            suitAddButton = new JButton(Messages.getString("add"));
            suitAddButton.addActionListener(this);
        }
        return suitAddButton;
    }

    private JButton getSuitRemoveButton() {
        if (suitRemoveButton == null) {
            suitRemoveButton = new JButton(Messages.getString("remove"));
            suitRemoveButton.addActionListener(this);
        }
        return suitRemoveButton;
    }

    private JPanel getGlovesPanel() {
        if (glovesPanel == null) {
            glovesPanel = new JPanel();
            glovesPanel.setLayout(new BorderLayout());
            glovesPanel.add(getGlovesTablePane(), BorderLayout.CENTER);
            glovesPanel.add(getGlovesButtonPanel(), BorderLayout.SOUTH);
        }
        return glovesPanel;
    }

    private JScrollPane getGlovesTablePane() {
        if (glovesTablePane == null) {
            glovesTablePane = new JScrollPane(getGlovesTable());
        }
        return glovesTablePane;
    }
    
    private JTable getGlovesTable() {
        if (glovesTable == null) {
            glovesTable = new JTable(getGlovesTableModel());
            TableColumnModel columnModel = glovesTable.getColumnModel();
            columnModel.getColumn(0).setHeaderValue(Messages.getString("gloves"));
        }
        return glovesTable;
    }
    
    private GloveTypeTableModel getGlovesTableModel() {
        if (glovesTableModel == null) {
            glovesTableModel = new GloveTypeTableModel();
        }
        return glovesTableModel;
    }
    
    private JPanel getGlovesButtonPanel() {
        if (glovesButtonPanel == null) {
            GridBagConstraints gc = new GridBagConstraints();
            gc.fill = GridBagConstraints.HORIZONTAL;
            gc.weightx=0.5;
            glovesButtonPanel = new JPanel();
            glovesButtonPanel.setLayout(new GridBagLayout());
            glovesButtonPanel.setComponentOrientation(java.awt.ComponentOrientation.LEFT_TO_RIGHT);
            gc.gridx = 0;
            gc.gridy = 0;
            gc.insets = new java.awt.Insets(5,5,5,5);
            glovesButtonPanel.add(new JLabel(Messages.getString("description")), gc);
            gc.gridx = 0;
            gc.gridy = 1;
            gc.insets = new java.awt.Insets(5,5,5,5);
            glovesButtonPanel.add(getGlovesDescription(), gc);
            gc.gridx = 1;
            gc.gridy = 1;
            gc.insets = new java.awt.Insets(5,5,5,5);
            glovesButtonPanel.add(getGlovesAddButton(), gc);
            gc.gridx = 1;
            gc.gridy = 0;
            gc.insets = new java.awt.Insets(5,5,5,5);
            glovesButtonPanel.add(getGlovesRemoveButton(), gc);
        }
        return glovesButtonPanel;
    }
    
    private JTextField getGlovesDescription() {
        if (glovesDescription == null) {
            glovesDescription = new JTextField();
        }
        return glovesDescription;
    }
    
    private JButton getGlovesAddButton() {
        if (glovesAddButton == null) {
            glovesAddButton = new JButton(Messages.getString("add"));
            glovesAddButton.addActionListener(this);
        }
        return glovesAddButton;
    }

    private JButton getGlovesRemoveButton() {
        if (glovesRemoveButton == null) {
            glovesRemoveButton = new JButton(Messages.getString("remove"));
            glovesRemoveButton.addActionListener(this);
        }
        return glovesRemoveButton;
    }

    private JPanel getSetsPanel() {
        if (setsPanel == null) {
            setsPanel = new JPanel();
            setsPanel.setLayout(new BorderLayout());
            setsPanel.add(getSetsTablePane(), BorderLayout.CENTER);
            setsPanel.add(getSetsButtonPanel(), BorderLayout.SOUTH);
        }
        return setsPanel ;
    }

    private JScrollPane getSetsTablePane() {
        if (setsTablePane == null) {
            setsTablePane = new JScrollPane(getSetsTable());
        }
        return setsTablePane;
    }
    
    private JTable getSetsTable() {
        if (setsTable == null) {
            setsTable = new JTable(getSetsTableModel());
            TableColumnModel columnModel = setsTable.getColumnModel();
            columnModel.getColumn(0).setHeaderValue(Messages.getString("equipmentsetname"));
            columnModel.getColumn(1).setHeaderValue(Messages.getString("suit"));
            columnModel.getColumn(2).setHeaderValue(Messages.getString("gloves"));
            columnModel.getColumn(3).setHeaderValue(Messages.getString("weight"));
            columnModel.getColumn(4).setHeaderValue(Messages.getString("volume")+" ["+UnitConverter.getDisplayVolumeUnit()+"]");
            columnModel.getColumn(5).setHeaderValue(Messages.getString("tanktype"));
        }
        return setsTable;
    }
    
    private EquipmentSetTableModel getSetsTableModel() {
        if (setsTableModel == null) {
            setsTableModel = new EquipmentSetTableModel();
        }
        return setsTableModel;
    }
    private JPanel getSetsButtonPanel() {
        if (setsButtonPanel == null) {
            GridBagConstraints gc = new GridBagConstraints();
            gc.fill = GridBagConstraints.HORIZONTAL;
            gc.weightx=0.5;
            setsButtonPanel = new JPanel();
            setsButtonPanel.setLayout(new GridBagLayout());
            setsButtonPanel.setComponentOrientation(java.awt.ComponentOrientation.LEFT_TO_RIGHT);
            gc.gridx = 0;
            gc.gridy = 0;
            gc.insets = new java.awt.Insets(5,5,5,5);
            setsButtonPanel.add(new JLabel(Messages.getString("equipmentsetname")), gc);
            gc.gridx = 1;
            gc.gridy = 0;
            gc.insets = new java.awt.Insets(5,5,5,5);
            setsButtonPanel.add(getSetName(), gc);

            gc.gridx = 2;
            gc.gridy = 0;
            gc.insets = new java.awt.Insets(5,5,5,5);
            setsButtonPanel.add(new JLabel(Messages.getString("weight")), gc);
            gc.gridx = 3;
            gc.gridy = 0;
            gc.insets = new java.awt.Insets(5,5,5,5);
            setsButtonPanel.add(getWeight(), gc);

            gc.gridx = 0;
            gc.gridy = 1;
            gc.insets = new java.awt.Insets(5,5,5,5);
            setsButtonPanel.add(new JLabel(Messages.getString("suit")), gc);
            gc.gridx = 1;
            gc.gridy = 1;
            gc.insets = new java.awt.Insets(5,5,5,5);
            setsButtonPanel.add(getSuit(), gc);

            gc.gridx = 2;
            gc.gridy = 1;
            gc.insets = new java.awt.Insets(5,5,5,5);
            setsButtonPanel.add(new JLabel(Messages.getString("gloves")), gc);
            gc.gridx = 3;
            gc.gridy = 1;
            gc.insets = new java.awt.Insets(5,5,5,5);
            setsButtonPanel.add(getGloves(), gc);

            gc.gridx = 0;
            gc.gridy = 2;
            gc.insets = new java.awt.Insets(5,5,5,5);
            setsButtonPanel.add(new JLabel(Messages.getString("volume")+" ["+UnitConverter.getDisplayVolumeUnit()+"]"), gc);
            gc.gridx = 1;
            gc.gridy = 2;
            gc.insets = new java.awt.Insets(5,5,5,5);
            setsButtonPanel.add(getTankVolume(), gc);

            gc.gridx = 2;
            gc.gridy = 2;
            gc.insets = new java.awt.Insets(5,5,5,5);
            setsButtonPanel.add(new JLabel(Messages.getString("tanktype")), gc);
            gc.gridx = 3;
            gc.gridy = 2;
            gc.insets = new java.awt.Insets(5,5,5,5);
            setsButtonPanel.add(getTankType(), gc);

            gc.gridx = 4;
            gc.gridy = 2;
            gc.insets = new java.awt.Insets(5,5,5,5);
            setsButtonPanel.add(getSetAddButton(), gc);
            gc.gridx = 4;
            gc.gridy = 1;
            gc.insets = new java.awt.Insets(5,5,5,5);
            setsButtonPanel.add(getSetRemoveButton(), gc);
        }
        return setsButtonPanel;   
    }
    
    private JTextField getSetName() {
        if (setName == null) {
            setName = new JTextField();
        }
        return setName;
    }
    
    private JTextField getSuit() {
        if (suit == null) {
            suit = new AutoCompleteTextField(getSuitDictionary(), null);
        }
        return suit;
    }
    
    private AutoCompleteDictionary getSuitDictionary() {
        if (suitDictionary == null) {
            suitDictionary = new DefaultDictionary();
            Iterator<Suit> it = getSuitTableModel().getAll().iterator();
            while (it.hasNext()) {
                suitDictionary.addEntry(it.next().getDescription());
            }
        }
        return suitDictionary;
    }
    
    private JTextField getGloves() {
        if (gloves == null) {
            gloves = new AutoCompleteTextField(getGlovesDictionary(), null);
        }
        return gloves;
    }

    private AutoCompleteDictionary getGlovesDictionary() {
        if (glovesDictionary == null) {
            glovesDictionary = new DefaultDictionary();
            Iterator<GloveType> it = getGlovesTableModel().getAll().iterator();
            while (it.hasNext()) {
                glovesDictionary.addEntry(it.next().getDescription());
            }
        }
        return glovesDictionary;
    }
    
    private JTextField getWeight() {
        if (weight == null) {
            weight = new JTextField();
        }
        return weight;
    }
    
    private JTextField getTankVolume() {
        if (tankVolume == null) {
            tankVolume = new JTextField();
        }
        return tankVolume;
    }
    
    private JComboBox getTankType() {
        if (tankType == null) {
            tankType = new JComboBox(TankDetailWindow.TANKTYPES);
        }
        return tankType;
    }

    private JButton getSetAddButton() {
        if (setAddButton == null) {
            setAddButton = new JButton(Messages.getString("add"));
            setAddButton.addActionListener(this);
        }
        return setAddButton;
    }

    private JButton getSetRemoveButton() {
        if (setRemoveButton == null) {
            setRemoveButton = new JButton(Messages.getString("remove"));
            setRemoveButton.addActionListener(this);
        }
        return setRemoveButton;
    }
    //
    // inner classes
    //
    
    private class SuitTableModel extends AbstractTableModel {

        private static final long serialVersionUID = 1L;

        private List<Suit> suits;
        
        private SuitTableModel() {
            suits = new ArrayList<Suit>();
        }

        public void load(ArrayList<Suit> suits2) {
            SortedSet<Suit> sorted = new TreeSet<Suit>(suits2);
            suits = new ArrayList<Suit>(sorted);
            fireTableDataChanged();
        }

        public int getRowCount() {
            return suits.size();
        }

        public int getColumnCount() {
            return 1;
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            return suits.get(rowIndex).getDescription();
        }
        
        public void add(Suit suit) {
            suits.add(suit);
            fireTableDataChanged();
        }
        
        public void remove(int rowIdx) {
            suits.remove(rowIdx);
            fireTableDataChanged();
        }
        
        public List<Suit> getAll() {
            return suits;
        }        
    }

    private class GloveTypeTableModel extends AbstractTableModel {

        private static final long serialVersionUID = 1L;

        private List<GloveType> gloveTypes;
        
        protected GloveTypeTableModel() {
            gloveTypes = new ArrayList<GloveType>();
        }
        
        private void load(ArrayList<GloveType> gloveType) {
            SortedSet<GloveType> sorted = new TreeSet<GloveType>(gloveType);
            gloveTypes = new ArrayList<GloveType>(sorted);
            fireTableDataChanged();
        }

        public int getRowCount() {
            return gloveTypes.size();
        }

        public int getColumnCount() {
            return 1;
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            return gloveTypes.get(rowIndex).getDescription();
        }
        
        public void add(GloveType gloveType) {
            gloveTypes.add(gloveType);
            fireTableDataChanged();
        }
        
        public void remove(int rowIdx) {
            gloveTypes.remove(rowIdx);
            fireTableDataChanged();
        }
        
        public List<GloveType> getAll() {
            return gloveTypes;
        }        
    }

    private class EquipmentSetTableModel extends AbstractTableModel {

        private static final long serialVersionUID = 1L;

        private List<EquipmentSet> equipmentSets;
        
        protected EquipmentSetTableModel() {
            equipmentSets = new ArrayList<EquipmentSet>();
        }
        
        private void load(ArrayList<EquipmentSet> equipmentSet) {
            SortedSet<EquipmentSet> sorted = new TreeSet<EquipmentSet>(equipmentSet);
            equipmentSets = new ArrayList<EquipmentSet>(sorted);
            fireTableDataChanged();
        }

        public int getRowCount() {
            return equipmentSets.size();
        }

        public int getColumnCount() {
            return 6;
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            switch (columnIndex) {
            case 0:
                return equipmentSets.get(rowIndex).getName();
            case 1:
                return equipmentSets.get(rowIndex).getSuit();
            case 2:
                return equipmentSets.get(rowIndex).getGloves();
            case 3:
                return equipmentSets.get(rowIndex).getWeight();
            case 4:
                return equipmentSets.get(rowIndex).getTankVolume();
            case 5:
                return equipmentSets.get(rowIndex).getTankType();
            }
            return null;
        }
        
        public void add(EquipmentSet equipmentSet) {
            equipmentSets.add(equipmentSet);
            fireTableDataChanged();
        }
        
        public void remove(int rowIdx) {
            equipmentSets.remove(rowIdx);
            fireTableDataChanged();
        }
        
        public List<EquipmentSet> getAll() {
            return equipmentSets;
        }        
    }

}  //  @jve:decl-index=0:visual-constraint="10,10"
