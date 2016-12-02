/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: DivesiteDetailWindow.java
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
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.TimeZone;
import java.util.TreeSet;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import net.sf.jdivelog.gui.commands.CommandManager;
import net.sf.jdivelog.gui.commands.CommandSaveSite;
import net.sf.jdivelog.gui.commands.UndoableCommand;
import net.sf.jdivelog.gui.resources.Messages;
import net.sf.jdivelog.gui.util.AutoCompleteDictionary;
import net.sf.jdivelog.gui.util.AutoCompleteTextField;
import net.sf.jdivelog.gui.util.DefaultDictionary;
import net.sf.jdivelog.model.DiveSite;

/**
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class DivesiteDetailWindow extends JDialog implements ActionListener {
    
    private static final long serialVersionUID = 8860510231587301560L;

    private static final String[] SITE_TYPES = new String[] {Messages.getString("sitetype.land"), Messages.getString("sitetype.boat")};

    private MainWindow mainWindow;

    private DiveDetailPanel diveDetailPanel;

    private DiveSite oldSite;

    private DiveSite newSite;

    private JPanel buttonPanel;

    private JButton closeButton;

    private JButton cancelButton;

    private JPanel contentPanel;

    private JTextField fieldSpot;

    private JTextField fieldCity;

    private JTextField fieldState;

    private JTextField fieldCountry;

    private JTextField fieldWaters;

    private JTextField fieldMaxDepth;

    private JTextField fieldMinDepth;

    private JTextField fieldAvgDepth;

    private JTextField fieldAltitude;

    private JTextField fieldLatitude;

    private JTextField fieldLongitude;
    
    private JTextArea areaDescription;
    
    private JTextArea areaWarnings;
    
    private JTextArea areaDirections;

    private JTextArea areaPrivateRemarks;

    private JSpinner spinEvaluation;
    
    private JComboBox fieldSiteType;

    private AutoCompleteDictionary watersDictionary;

    private AutoCompleteDictionary cityDictionary;

    private AutoCompleteDictionary countryDictionary;

    private AutoCompleteDictionary stateDictionary;

    private JComboBox fieldTimezone;

    /**
     * Constructor
     * @param parent 
     * @param mainWindow
     */
    public DivesiteDetailWindow(Window parent, MainWindow mainWindow) {
    	super(parent, ModalityType.APPLICATION_MODAL);
        this.mainWindow = mainWindow;
        this.oldSite = null;
        initialize();
        new MnemonicFactory(this);
    }

    /**
     * Constructor
     * @param parent 
     * @param mainWindow
     * @param site
     */
    public DivesiteDetailWindow(Window parent, MainWindow mainWindow, DiveSite site) {
    	super(parent, ModalityType.APPLICATION_MODAL);
        this.mainWindow = mainWindow;
        this.oldSite = site;
        initialize();
        loadData();
    }

    /**
     * Constructor
     * @param parent 
     * @param mainWindow
     * @param detailPanel
     * @param site
     */
    public DivesiteDetailWindow(Window parent, MainWindow mainWindow, DiveDetailPanel detailPanel, DiveSite site) {
    	super(parent, ModalityType.APPLICATION_MODAL);
        this.mainWindow = mainWindow;
        this.diveDetailPanel = detailPanel;
        this.oldSite = site;
        initialize();
        loadData();
    }

    /**
     * Action listener
     * 
     * @param e
     *            action event
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == closeButton) {
            saveData();
            UndoableCommand cmd = new CommandSaveSite(mainWindow, oldSite, newSite);
            CommandManager.getInstance().execute(cmd);
            if (diveDetailPanel != null) {
                diveDetailPanel.setSite(newSite);
            }
            this.dispose();
        } else if (e.getSource() == cancelButton) {
            this.dispose();
        }
    }

    //
    // private methods
    //
    /**
     * Initialization of the window
     */
    private void initialize() {
        setSize(800, 600);
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/net/sf/jdivelog/gui/resources/icons/logo.gif")));
        setTitle(Messages.getString("edit_site"));
        setLayout(new BorderLayout());
        add(getContentPanel(), BorderLayout.CENTER);
        add(getButtonPanel(), BorderLayout.SOUTH);
    }

    /**
     * Save the site datas contained into the textfields into the DiveSite
     * instance
     * 
     */
    private void saveData() {
        newSite = new DiveSite();
        newSite.setPrivateId(oldSite.getPrivateId());
        newSite.setPublicId(oldSite.getPublicId());
        newSite.setSpot(getFieldSpot().getText());
        newSite.setCity(getFieldCity().getText());
        newSite.setState(getFieldState().getText());
        newSite.setCountry(getFieldCountry().getText());
        newSite.setWaters(getFieldWaters().getText());
        newSite.setTimezone(getFieldTimezone().getSelectedItem().toString());
        newSite.setMaxDepth(getFieldMaxDepth().getText());
        newSite.setMinDepth(getFieldMinDepth().getText());
        newSite.setAvgDepth(getFieldAvgDepth().getText());
        newSite.setLatitude(getFieldLatitude().getText());
        newSite.setLongitude(getFieldLongitude().getText());
        newSite.setAltitude(getFieldAltitude().getText());
        newSite.setPrivateRemarks(getAreaPrivateRemarks().getText());
        newSite.setDirections(getAreaDirections().getText());
        newSite.setDescription(getAreaDescription().getText());
        newSite.setWarnings(getAreaWarnings().getText());
        newSite.setEvaluation(getSpinEvaluation().getValue().toString());
        newSite.setSiteType(getFieldSiteType().getSelectedIndex());

    }

    /**
     * Load the data into the window from the DiveSite instance
     * 
     */
    private void loadData() {
        getFieldSpot().setText(oldSite.getSpot());
        getFieldCity().setText(oldSite.getCity());
        getFieldState().setText(oldSite.getState());
        getFieldCountry().setText(oldSite.getCountry());
        getFieldWaters().setText(oldSite.getWaters());
        getFieldTimezone().setSelectedItem(oldSite.getTimezone());
        getFieldMaxDepth().setText(oldSite.getMaxDepth());
        getFieldMinDepth().setText(oldSite.getMinDepth());
        getFieldAvgDepth().setText(oldSite.getAvgDepth());
        getFieldLatitude().setText(oldSite.getLatitude());
        getFieldLongitude().setText(oldSite.getLongitude());
        getFieldAltitude().setText(oldSite.getAltitude());
        getAreaPrivateRemarks().setText(oldSite.getPrivateRemarks());
        getAreaDescription().setText(oldSite.getDescription());
        getAreaWarnings().setText(oldSite.getWarnings());
        getAreaDirections().setText(oldSite.getDirections());
        getSpinEvaluation().setValue(oldSite.getEvaluation());
        getFieldSiteType().setSelectedIndex(oldSite.getSiteType());
        Iterator<DiveSite> it = mainWindow.getLogBook().getMasterdata().getDiveSites().iterator();
        while(it.hasNext()) {
            DiveSite site = it.next();
            getCityDictionary().addEntry(site.getCity());
            getStateDictionary().addEntry(site.getState());
            getCountryDictionary().addEntry(site.getCountry());
            getWatersDictionary().addEntry(site.getWaters());
        }
    }

    /**
     * Create a JPanel with the fields
     * 
     * @return JPane, the content panel with the fields
     */
    private JPanel getContentPanel() {
        if (contentPanel == null) {
            contentPanel = new JPanel();
            contentPanel.setSize(800, 550);
            contentPanel.setLayout(new GridBagLayout());
            
            JPanel theLeftPanel = new JPanel();
            theLeftPanel.setLayout(new GridBagLayout());
            JPanel theRightPanel = new JPanel();
            theRightPanel.setLayout(new GridBagLayout());
            
            GridBagConstraints thePanelConstraints = new GridBagConstraints();
            thePanelConstraints.insets = new Insets(0,25,0,0);
            thePanelConstraints.fill = GridBagConstraints.BOTH;
            thePanelConstraints.weightx = 0.5D;
            thePanelConstraints.weighty = 0.5D;
            thePanelConstraints.gridy = 0;

            
            GridBagConstraints theLabelConstraints = new GridBagConstraints();
            theLabelConstraints.fill = GridBagConstraints.NONE;
            theLabelConstraints.weightx = 0.0D;
            theLabelConstraints.weighty = 0.1D;
            theLabelConstraints.gridx = 0;
            theLabelConstraints.anchor = GridBagConstraints.EAST;
            theLabelConstraints.insets = new Insets(0,5,0,5);
            
            GridBagConstraints theFieldConstraints = new GridBagConstraints();
            theFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
            theFieldConstraints.weightx = 0.0D;
            theFieldConstraints.weighty = 0.1D;
            theFieldConstraints.gridx = 1;
            theFieldConstraints.anchor = GridBagConstraints.CENTER;
            theFieldConstraints.insets = new Insets(0,5,0,2);
            
            GridBagConstraints theTextFieldConstraints = new GridBagConstraints();
            theTextFieldConstraints.fill = GridBagConstraints.BOTH;
            theTextFieldConstraints.weightx = 1.0D;
            theTextFieldConstraints.weighty = 0.9D;
            theTextFieldConstraints.gridx = 1;
            theTextFieldConstraints.anchor = GridBagConstraints.CENTER;
            theTextFieldConstraints.insets = new Insets(0,5,0,2);            
            
            
            theLabelConstraints.gridy = 0;
            theFieldConstraints.gridy = 0;
            theLeftPanel.add(new JLabel(Messages.getString("spot")), theLabelConstraints);
            theLeftPanel.add(getFieldSpot(), theFieldConstraints);
            
            
            theLabelConstraints.gridy = 1;
            theFieldConstraints.gridy = 1;
            theLeftPanel.add(new JLabel(Messages.getString("city")), theLabelConstraints);
            theLeftPanel.add(getFieldCity(), theFieldConstraints);
            
            theLabelConstraints.gridy = 2;
            theFieldConstraints.gridy = 2;
            theLeftPanel.add(new JLabel(Messages.getString("state")), theLabelConstraints);
            theLeftPanel.add(getFieldState(), theFieldConstraints);
            
            theLabelConstraints.gridy = 3;
            theFieldConstraints.gridy = 3;
            theLeftPanel.add(new JLabel(Messages.getString("country")), theLabelConstraints);
            theLeftPanel.add(getFieldCountry(), theFieldConstraints);
            
            theLabelConstraints.gridy = 4;
            theFieldConstraints.gridy = 4;
            theLeftPanel.add(new JLabel(Messages.getString("waters")), theLabelConstraints);
            theLeftPanel.add(getFieldWaters(), theFieldConstraints);
            
            theLabelConstraints.gridy = 5;
            theFieldConstraints.gridy = 5;
            theLeftPanel.add(new JLabel(Messages.getString("timezone")), theLabelConstraints);
            theLeftPanel.add(getFieldTimezone(), theFieldConstraints);

            theLabelConstraints.gridy = 6;
            theFieldConstraints.gridy = 6;
            theLeftPanel.add(new JLabel(Messages.getString("sitetype")), theLabelConstraints);
            theLeftPanel.add(getFieldSiteType(), theFieldConstraints);

            theLabelConstraints.gridy = 7;
            theTextFieldConstraints.gridy = 7;
            theLeftPanel.add(new JLabel(Messages.getString("privateRemarks")), theLabelConstraints);
            JScrollPane p = new JScrollPane(getAreaPrivateRemarks());
            p.setMinimumSize(new Dimension(400,100));
            theLeftPanel.add(p, theTextFieldConstraints);
            
            theLabelConstraints.gridy = 8;
            theTextFieldConstraints.gridy = 8;
            theLeftPanel.add(new JLabel(Messages.getString("description")), theLabelConstraints);
            p = new JScrollPane(getAreaDescription());
            p.setMinimumSize(new Dimension(400,100));
            theLeftPanel.add(p, theTextFieldConstraints);            
            
            // col 1+2
            theLabelConstraints.gridy = 0;
            theLabelConstraints.gridx = 2;
            theFieldConstraints.gridy = 0;
            theFieldConstraints.gridx = 3;

            theTextFieldConstraints.gridx = 3;
            theRightPanel.add(new JLabel(Messages.getString("longitude")), theLabelConstraints);
            theRightPanel.add(getFieldLongitude(), theFieldConstraints);
                        
            theLabelConstraints.gridy = 1;
            theFieldConstraints.gridy = 1;
            theRightPanel.add(new JLabel(Messages.getString("latitude")), theLabelConstraints);
            theRightPanel.add(getFieldLatitude(), theFieldConstraints);
            
            theLabelConstraints.gridy = 2;
            theFieldConstraints.gridy = 2;
            theRightPanel.add(new JLabel(Messages.getString("altitude")), theLabelConstraints);
            theRightPanel.add(getFieldAltitude(), theFieldConstraints);

            theLabelConstraints.gridy = 3;
            theFieldConstraints.gridy = 3;
            theRightPanel.add(new JLabel(Messages.getString("evaluation")), theLabelConstraints);
            theRightPanel.add(getSpinEvaluation(), theFieldConstraints);
            
            theLabelConstraints.gridy = 4;
            theFieldConstraints.gridy = 4;
            theRightPanel.add(new JLabel(Messages.getString("maxdepth")), theLabelConstraints);
            theRightPanel.add(getFieldMaxDepth(), theFieldConstraints);
            
            theLabelConstraints.gridy = 5;
            theFieldConstraints.gridy = 5;
            theRightPanel.add(new JLabel(Messages.getString("avgdepth")), theLabelConstraints);
            theRightPanel.add(getFieldAvgDepth(), theFieldConstraints);
            
            theLabelConstraints.gridy = 6;
            theFieldConstraints.gridy = 6;
            theRightPanel.add(new JLabel(Messages.getString("mindepth")), theLabelConstraints);
            theRightPanel.add(getFieldMinDepth(), theFieldConstraints);
            
            theLabelConstraints.gridy = 7;
            theTextFieldConstraints.gridy = 7;
            theRightPanel.add(new JLabel(Messages.getString("warnings")), theLabelConstraints);
            p = new JScrollPane(getAreaWarnings());
            p.setMinimumSize(new Dimension(400,100));
            theRightPanel.add(p, theTextFieldConstraints);            
            
            theLabelConstraints.gridy = 8;
            theTextFieldConstraints.gridy = 8;
            theRightPanel.add(new JLabel(Messages.getString("directions")), theLabelConstraints);
            p = new JScrollPane(getAreaDirections());
            p.setMinimumSize(new Dimension(400,100));
            theRightPanel.add(p, theTextFieldConstraints); 
            
            thePanelConstraints.gridx = 0;
            contentPanel.add(theLeftPanel, thePanelConstraints);
            
            thePanelConstraints.gridx = 1;
            contentPanel.add(theRightPanel, thePanelConstraints);

        }
        return contentPanel;
    }

    /**
     * Create the JTextField for the spot site
     * 
     * @return JTextField
     */
    private JTextField getFieldSpot() {
        if (fieldSpot == null) {
            fieldSpot = new JTextField(100);
            fieldSpot.setMinimumSize(new Dimension(200, 20));
        }
        return fieldSpot;
    }

    /**
     * Create the JTextField for the minimal depth
     * 
     * @return JTextField
     */
    private JTextField getFieldMinDepth() {
        if (fieldMinDepth == null) {
            fieldMinDepth = new JTextField(100);
            fieldMinDepth.setMinimumSize(new Dimension(50, 20));
        }
        return fieldMinDepth;
    }

    /**
     * Create the JTextField for the average depth
     * 
     * @return JTextField
     */
    private JTextField getFieldAvgDepth() {
        if (fieldAvgDepth == null) {
            fieldAvgDepth = new JTextField(100);
            fieldAvgDepth.setMinimumSize(new Dimension(50, 20));
        }
        return fieldAvgDepth;
    }
    
    private JSpinner getSpinEvaluation() {
        if (spinEvaluation == null) {
            Integer value = 10;
            if (oldSite != null) {
                value = this.oldSite.getEvaluation();
            }
            Integer min = new Integer(0);
            Integer max = new Integer(20);
            Integer step = new Integer(1);
            spinEvaluation = new JSpinner(new SpinnerNumberModel(value, min, max, step));
        }
        return spinEvaluation;
    }
    
    private JComboBox getFieldSiteType() {
        if (fieldSiteType == null) {
            fieldSiteType = new JComboBox(SITE_TYPES);
        }
        return fieldSiteType;
    }

    /**
     * Create the JTextField for the city
     * 
     * @return JTextField
     */
    private JTextField getFieldCity() {
        if (fieldCity == null) {
            fieldCity = new AutoCompleteTextField(getCityDictionary(), null);
            fieldCity.setMinimumSize(new Dimension(200, 20));
        }
        return fieldCity;
    }
    
    private AutoCompleteDictionary getCityDictionary() {
        if (cityDictionary == null) {
            cityDictionary = new DefaultDictionary();
        }
        return cityDictionary;
    }

    /**
     * Create the JTextField for the maximal depth
     * 
     * @return JTextField
     */
    private JTextField getFieldMaxDepth() {
        if (fieldMaxDepth == null) {
            fieldMaxDepth = new JTextField(100);
            fieldMaxDepth.setMinimumSize(new Dimension(50, 20));
        }
        return fieldMaxDepth;
    }

    /**
     * Create the JTextField for the longitude
     * 
     * @return JTextField
     */
    private JTextField getFieldLongitude() {
        if (fieldLongitude == null) {
            fieldLongitude = new JTextField(50);
            fieldLongitude.setMinimumSize(new Dimension(50, 20));
        }
        return fieldLongitude;
    }

    /**
     * Create the JTextField for the latitude
     * 
     * @return JTextField
     */
    private JTextField getFieldLatitude() {
        if (fieldLatitude == null) {
            fieldLatitude = new JTextField(50);
            fieldLatitude.setMinimumSize(new Dimension(50, 20));
        }
        return fieldLatitude;
    }

    /**
     * Create the JTextField for the altitude
     * 
     * @return JTextField
     */
    private JTextField getFieldAltitude() {
        if (fieldAltitude == null) {
            fieldAltitude = new JTextField(50);
            fieldAltitude.setMinimumSize(new Dimension(50, 20));
        }
        return fieldAltitude;
    }

    /**
     * Create the JTextArea for the private remarks
     * 
     * @return JTextArea
     */
    private JTextArea getAreaPrivateRemarks() {
        if (areaPrivateRemarks == null) {
            areaPrivateRemarks = new JTextArea(400, 200);
            areaPrivateRemarks.setMinimumSize(new Dimension(400, 200));
        }
        return areaPrivateRemarks;
    }

    /**
     * Create the JTextArea for the site description
     * 
     * @return JTextArea
     */
    private JTextArea getAreaDescription() {
        if (areaDescription == null) {
            areaDescription = new JTextArea(400, 200);
            areaDescription.setMinimumSize(new Dimension(400, 200));
        }
        return areaDescription;
    }

    /**
     * Create the JTextArea for the site warnings
     * 
     * @return JTextArea
     */
    private JTextArea getAreaWarnings() {
        if (areaWarnings == null) {
            areaWarnings = new JTextArea(400, 200);
            areaWarnings.setMinimumSize(new Dimension(400, 200));
        }
        return areaWarnings;
    }

    /**
     * Create the JTextArea for the driving directions
     * 
     * @return JTextArea
     */
    private JTextArea getAreaDirections() {
        if (areaDirections == null) {
            areaDirections = new JTextArea(400, 200);
            areaDirections.setMinimumSize(new Dimension(400, 200));
        }
        return areaDirections;
    }

    /**
     * Create the JTextField for the altitude
     * 
     * @return JTextField
     */
    private JTextField getFieldState() {
        if (fieldState == null) {
            fieldState = new AutoCompleteTextField(getStateDictionary(), null);
            fieldState.setMinimumSize(new Dimension(200, 20));
        }
        return fieldState;
    }

    private AutoCompleteDictionary getStateDictionary() {
        if (stateDictionary == null) {
            stateDictionary = new DefaultDictionary();
        }
        return stateDictionary;
    }

    /**
     * Create the JTextField for the country of the site
     * 
     * @return JTextField
     */
    private JTextField getFieldCountry() {
        if (fieldCountry == null) {
            fieldCountry = new AutoCompleteTextField(getCountryDictionary(), null);
            fieldCountry.setMinimumSize(new Dimension(200, 20));
        }
        return fieldCountry;
    }
    
    private AutoCompleteDictionary getCountryDictionary() {
        if (countryDictionary == null) {
            countryDictionary = new DefaultDictionary();
        }
        return countryDictionary;
    }

    private JTextField getFieldWaters() {
        if (fieldWaters == null) {
            fieldWaters = new AutoCompleteTextField(getWatersDictionary(), null);
            fieldWaters.setMinimumSize(new Dimension(200, 20));
        }
        return fieldWaters;
    }

    private AutoCompleteDictionary getWatersDictionary() {
        if (watersDictionary == null) {
            watersDictionary = new DefaultDictionary();
        }
        return watersDictionary;
    }
    
    private JComboBox getFieldTimezone() {
        if (fieldTimezone == null) {
            String[] ids = TimeZone.getAvailableIDs();
            TreeSet<String> idSet = new TreeSet<String>();
            idSet.add("");
            for (int i=0; i<ids.length;i++) {
                idSet.add(ids[i]);
            }
            fieldTimezone = new JComboBox(idSet.toArray());
            fieldTimezone.setPreferredSize(new Dimension(200,20));
        }
        return fieldTimezone;
    }

    /**
     * Create the panel containing all the buttons
     * 
     * @return buttonPanel
     */
    private JPanel getButtonPanel() {
        if (buttonPanel == null) {
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints1.weightx = 0.5;
            buttonPanel = new JPanel();
            buttonPanel.setLayout(new GridBagLayout());
            buttonPanel.setComponentOrientation(java.awt.ComponentOrientation.LEFT_TO_RIGHT);
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

    /**
     * Create the button to close the window
     * 
     * @return close button
     */
    private JButton getCloseButton() {
        if (closeButton == null) {
            closeButton = new JButton();
            closeButton.setText(Messages.getString("close")); //$NON-NLS-1$
            closeButton.addActionListener(this);
        }
        return closeButton;
    }

    /**
     * Create the button to cancel the modifications made
     * 
     * @return cancelButton
     */
    private JButton getCancelButton() {
        if (cancelButton == null) {
            cancelButton = new JButton();
            cancelButton.setText(Messages.getString("cancel")); //$NON-NLS-1$
            cancelButton.addActionListener(this);
        }
        return cancelButton;
    }
    
}
