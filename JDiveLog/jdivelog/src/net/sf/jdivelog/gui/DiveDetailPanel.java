/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: DiveDetailWindow.java
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
import java.awt.CheckboxMenuItem;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.TableColumnModel;

import net.sf.jdivelog.gui.commands.CommandManager;
import net.sf.jdivelog.gui.commands.CommandSaveDive;
import net.sf.jdivelog.gui.commands.UndoableCommand;
import net.sf.jdivelog.gui.resources.Messages;
import net.sf.jdivelog.gui.util.AutoCompleteDictionary;
import net.sf.jdivelog.gui.util.AutoCompleteTextField;
import net.sf.jdivelog.gui.util.DefaultDictionary;
import net.sf.jdivelog.gui.util.ExtensionFileFilter;
import net.sf.jdivelog.model.Buddy;
import net.sf.jdivelog.model.DiveActivity;
import net.sf.jdivelog.model.DiveSite;
import net.sf.jdivelog.model.DiveType;
import net.sf.jdivelog.model.Equipment;
import net.sf.jdivelog.model.EquipmentSet;
import net.sf.jdivelog.model.GloveType;
import net.sf.jdivelog.model.JDive;
import net.sf.jdivelog.model.Masterdata;
import net.sf.jdivelog.model.Picture;
import net.sf.jdivelog.model.Suit;
import net.sf.jdivelog.model.Tank;
import net.sf.jdivelog.model.udcf.Gas;
import net.sf.jdivelog.util.DateFormatUtil;
import net.sf.jdivelog.util.UnitConverter;

import org.apache.batik.ext.awt.image.codec.jpeg.JPEGImageWriter;
import org.apache.batik.ext.awt.image.codec.png.PNGImageWriter;
import org.apache.batik.ext.awt.image.spi.ImageWriterParams;

/**
 * Description: Window displaying details about the dive and allowing to change them 
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class DiveDetailPanel extends JPanel implements ActionListener, ItemListener {

    private static final long serialVersionUID = 3256718472808511538L;
    private static final DateFormat TIMEFORMAT = new SimpleDateFormat(Messages.getString("timeformat")); //$NON-NLS-1$
    private static final NumberFormat DECIMALFORMAT = new DecimalFormat(Messages.getString("numberformat")); //$NON-NLS-1$
    private MainWindow mainWindow = null;   
    private Window parentWindow = null;
    private AutoCompleteDictionary buddyDictionary = null;
    private AutoCompleteDictionary diveTypeDictionary = null;
    private AutoCompleteDictionary diveActivityDictionary = null; 
    private AutoCompleteDictionary divePlaceDictionary = null;
    private AutoCompleteDictionary countryDicitonary = null;
    private AutoCompleteDictionary suitDictionary = null;
    private AutoCompleteDictionary glovesDictionary = null;
    public JDive oldDive = null;
    public JDive newDive = null;
    private JPanel buttonPanel = null;
    private JTabbedPane jTabbedPane = null;
    private JButton closeButton = null;
    private JButton cancelButton = null;
    private JPanel diveData = null;
    private JPanel panelEquipment = null;
    private JPanel panelProfile = null; //  @jve:decl-index=0:visual-constraint="32,84"
    private JPanel panelPictures = null;
    private JLabel labelDiveNumber = null;
    private JTextField fieldDiveNumber = null;
    private JLabel labelDate = null;
    private JTextField fieldDate = null;
    private JLabel labelTime = null;
    private JTextField fieldTime = null;
    private JLabel labelPlace = null;
    private JTextField fieldPlace = null;
    private JLabel labelCity = null;
    private JTextField fieldCity = null;
    private JLabel labelCountry = null;
    private JTextField fieldCountry = null;
    private JButton diveSiteChooseButton = null;     
    private JButton diveSiteDetailButton = null;
    private JLabel labelDepth = null;
    private JTextField fieldDepth = null;
    private JLabel labelAverageDepth = null;
    private JTextField fieldAverageDepth = null;
    private JLabel labelDuration = null;
    private JTextField fieldDuration = null;
    private JLabel labelTemperature = null;
    private JTextField fieldTemperature = null;
    private JLabel labelSurfaceTemperature = null;
    private JTextField fieldSurfaceTemperature = null;
    private JLabel labelVisibility = null;
    private JTextField fieldVisibility = null;
    private JLabel labelBuddy = null;
    private JTextField fieldBuddy = null;
    private JButton buttonBuddyWindow = null;    
    private JLabel labelDiveType = null;
    private JTextField fieldDiveType = null;
    private JButton buttonDiveType = null;    
    private JLabel labelDiveActivity = null;
    private JTextField fieldDiveActivity = null;
    private JButton buttonDiveActivity = null;    
    private JLabel labelComment = null;
    private JTextArea fieldComment = null;
    private JLabel labelSuit = null;
    private JTextField fieldSuit = null;
    private JLabel labelGloves = null;
    private JTextField fieldGloves = null;
    private JLabel labelWeight = null;
    private JTextField fieldWeight = null;
    private JLabel labelAMV = null;
    private JTextField fieldAMV = null;
    private JLabel labelTanks = null;
    private JLabel labelEquipmentComment = null;
    private JTextArea fieldEquipmentComment = null;
    private DiveProfile diveProfile = null;
    private JPanel tankPanel = null;
    private JTable tankTable = null;
    private TankTableModel tankTableModel = null;
    private JButton buttonAddTank = null;
    private JButton buttonDeleteTank = null;
    private JPanel pictureButtonPanel = null;
    private JButton buttonAddPictures = null;
    private JButton buttonRemovePictures = null;
    private JButton buttonStartSlideshow = null;
    private JScrollPane pictureTablePane = null;
    private JTable pictureTable = null;
    private PictureTableModel pictureTableModel = null;
    private Masterdata masterdata = null;
    private JLabel labelSet;
    private JComboBox fieldSet;
    private final boolean readonly;
    private PopupMenu popupMenu;
    private MenuItem compareMenuItem;
    private CheckboxMenuItem crosshairMenuItem;
    private JPopupMenu popupMenuGloves;
    private HashSet<JMenuItem> popupMenuItemsGloves;
    private JPopupMenu popupMenuSuits;
    private HashSet<JMenuItem> popupMenuItemsSuits;
    private JButton diveSiteNewButton;
    private LogbookChangeNotifier logbookChangeNotifier;
    private MenuItem exportPictureMenuItem;
    private MenuItem removeComparisonChartsMenuItem;

    public DiveDetailPanel(Window parentWindow, MainWindow mainWindow, JDive dive, Masterdata masterdata, boolean readonly) {
        super();
        this.parentWindow = parentWindow;
        this.mainWindow = mainWindow;
        this.masterdata = masterdata;
        this.oldDive = dive;
        this.readonly = readonly;
        this.logbookChangeNotifier = mainWindow.getLogbookChangeNotifier();
        initialize();
        loadData();
    }

    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() == fieldSet) {
            String name = (String)fieldSet.getSelectedItem();
            if (name != null) {
                Iterator<EquipmentSet> it = mainWindow.getLogBook().getMasterdata().getEquipmentSets().iterator();
                while (it.hasNext()) {
                    EquipmentSet set = it.next();
                    if (name.equals(set.getName())) {
                        getFieldSuit().setText(set.getSuit());
                        getFieldGloves().setText(set.getGloves());
                        getFieldWeight().setText(set.getWeight());
                        ArrayList<Tank> tanks = getTankTableModel().getTanks();
                        Tank tank;
                        if (tanks.size() > 0) {
                            tank = tanks.get(0);
                            if (tank.getType() == null || "".equals(tank.getType())) {
                                tank.setType(set.getTankType());
                            }
                            Gas gas = tank.getGas();
                            if (gas.getTankvolume() == null || gas.getTankvolume().equals(new Double(0))) {
                                UnitConverter c = new UnitConverter(UnitConverter.getDisplaySystem(), UnitConverter.getSystem(newDive.getUnits()));
                                gas.setTankvolume(c.convertVolume(new Double(set.getTankVolume())));
                            }
                            getTankTableModel().fireTableDataChanged();
                        } else {
                            tank = new Tank();
                            tank.setType(set.getTankType());
                            Gas gas = new Gas();
                            UnitConverter c = new UnitConverter(UnitConverter.getDisplaySystem(), UnitConverter.getSystem(newDive.getUnits()));
                            gas.setTankvolume(c.convertVolume(new Double(set.getTankVolume())));
                            tank.setGas(gas);
                            getTankTableModel().addTank(tank);
                        }
                    }
                }
            }
        }
        if (e.getSource() == crosshairMenuItem) {
            if (crosshairMenuItem.getState()) {
                setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
                getDiveProfile().setCrossHairMode(true);
            } else {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                getDiveProfile().setCrossHairMode(false);
            }
        }
    }
    
    private void loadData() {
        if (oldDive == null) {
            oldDive = new JDive();
        }
        newDive = oldDive.deepClone();
        int system = UnitConverter.getSystem(oldDive.getUnits());
        newDive.setUnits(UnitConverter.getSystemString(system));
        UnitConverter converter = new UnitConverter(system,
                UnitConverter.getDisplaySystem());
        getFieldDiveNumber().setText(
                newDive.getDiveNumber() != null ? String.valueOf(newDive
                        .getDiveNumber()) : null);        
        setName(Messages.getString("dive") + " " +fieldDiveNumber.getText() + " " +Messages.getString("details")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        DiveSite site = mainWindow.getLogBook().getMasterdata().getDiveSiteByPrivateId(newDive.getDiveSiteId());
        if (site != null) {            
            getFieldPlace().setText(site.getSpot());
            getFieldCity().setText(site.getCity());
            getFieldCountry().setText(site.getCountry());
            getDiveSiteDetailButton().setEnabled(true);
        } else {
            getDiveSiteDetailButton().setEnabled(false);
        }
        getFieldDiveType().setText(newDive.getDiveType());
        getFieldDiveActivity().setText(newDive.getDiveActivity());
        if (oldDive.getDate() != null) {
            getFieldDate().setText(DateFormatUtil.getDateFormat().format(newDive.getDate()));
            getFieldTime().setText(TIMEFORMAT.format(newDive.getDate()));
        } else {
            getFieldDate().setText(null);
            getFieldTime().setText(null);
        }
        getFieldDepth().setText(
                newDive.getDepth() != null ? DECIMALFORMAT.format(converter
                        .convertAltitude(newDive.getDepth())) : null);
        getFieldAverageDepth().setText(
                newDive.getAverageDepth() != null ? DECIMALFORMAT.format(converter
                        .convertAltitude(newDive.getAverageDepth())) : null);
        getFieldDuration().setText(
                newDive.getDuration() != null ? DECIMALFORMAT.format(converter
                        .convertTime(newDive.getDuration())) : null);
        getFieldTemperature().setText(
                newDive.getTemperature() != null ? DECIMALFORMAT.format(converter
                        .convertTemperature(newDive.getTemperature())) : null);
        getFieldSurfaceTemperature().setText(
                newDive.getSurfaceTemperature() != null ? DECIMALFORMAT.format(converter
                    .convertTemperature(newDive.getSurfaceTemperature())) : null);
        getFieldAMV().setText(
                newDive.getAMV() != null ? DECIMALFORMAT.format(converter
                        .convertAMV(newDive.getAMV())) : null);
        getFieldVisibility().setText(newDive.getVisibility());
        getFieldBuddy().setText(newDive.getBuddy());
        getFieldComment().setText(newDive.getComment());
        if (newDive.getDive() == null) {
            getPanelProfile().setEnabled(false);
        }
        getDiveProfile().setDive(newDive);
        if (newDive.getEquipment() == null) {
            newDive.setEquipment(new Equipment());
        }
        if (newDive.getEquipment().getTanks() == null) {
            newDive.getEquipment().setTanks(new ArrayList<Tank>());
        }
        getTankTableModel().setTanks(newDive.getEquipment().getTanks());
        getTankTableModel().setSystem(system);
        getFieldSuit().setText(newDive.getEquipment().getSuit());
        getFieldGloves().setText(newDive.getEquipment().getGloves());
        getFieldWeight().setText(newDive.getEquipment().getWeight());
        getFieldEquipmentComment().setText(newDive.getEquipment().getComment());
        getPictureTableModel().setPictures(newDive.getPictures());
    }

    public void saveData() {
        int system = UnitConverter.getSystem(oldDive.getUnits());
        newDive.setUnits(UnitConverter.getSystemString(system));
        UnitConverter converter = new UnitConverter(
                UnitConverter.getDisplaySystem(), system);
        try {
            newDive.setDiveNumber(new Long(fieldDiveNumber.getText()));
        } catch (NumberFormatException ex) {
            newDive.setDiveNumber((Long)null);
        }
        try {
            Date date = DateFormatUtil.getDateFormat().parse(fieldDate.getText());
            GregorianCalendar gc = new GregorianCalendar();
            gc.setTime(date);
            int year = gc.get(Calendar.YEAR);
            int month = gc.get(Calendar.MONTH) + 1;
            int day = gc.get(Calendar.DAY_OF_MONTH);
            newDive.setDate(String.valueOf(year), String.valueOf(month), String
                    .valueOf(day));
        } catch (ParseException e) {
        }
        try {
            Date date = TIMEFORMAT.parse(fieldTime.getText());
            GregorianCalendar gc = new GregorianCalendar();
            gc.setTime(date);
            newDive.setTime(String.valueOf(gc.get(Calendar.HOUR_OF_DAY)),
                    String.valueOf(gc.get(Calendar.MINUTE)));
        } catch (ParseException e) {
        }
        try {
            newDive.setDepth(converter.convertAltitude(DECIMALFORMAT.parse(fieldDepth
                    .getText()).doubleValue()));
        } catch (ParseException ex) {
            newDive.setDepth((Double)null);
        }
        try {
            newDive.setAverageDepth(converter.convertAltitude(DECIMALFORMAT.parse(fieldAverageDepth
                    .getText()).doubleValue()));
        } catch (ParseException ex) {
            newDive.setAverageDepth((Double)null);
        }
        try {
            newDive.setAMV(converter.convertAMV(DECIMALFORMAT.parse(fieldAMV
                    .getText()).doubleValue()));
        } catch (ParseException ex) {
            newDive.setAMV((Double)null);
        }
       try {
            newDive.setDuration(converter.convertTime(DECIMALFORMAT.parse(fieldDuration
                    .getText()).doubleValue()));
        } catch (ParseException ex) {
            newDive.setDuration((Double)null);
        }
        try {
            newDive.setTemperature(converter.convertTemperature(DECIMALFORMAT.parse(
                    fieldTemperature.getText()).doubleValue()));
        } catch (ParseException ex) {
            newDive.setTemperature((Double)null);
        }
        try {
            newDive.setSurfaceTemperature(converter.convertTemperature(
            		DECIMALFORMAT.parse(fieldSurfaceTemperature.getText()).doubleValue()));
        } catch (ParseException ex) {
            newDive.setSurfaceTemperature((Double)null);
        }
        newDive.setDiveType(fieldDiveType.getText());
        newDive.setDiveActivity(fieldDiveActivity.getText());
        newDive.setVisibility(fieldVisibility.getText());
        newDive.setBuddy(fieldBuddy.getText());
        newDive.setComment(fieldComment.getText());
        newDive.getEquipment().setTanks(getTankTableModel().getTanks());
        newDive.getEquipment().setSuit(getFieldSuit().getText());
        newDive.getEquipment().setGloves(getFieldGloves().getText());
        newDive.getEquipment().setWeight(getFieldWeight().getText());
        newDive.getEquipment().setComment(getFieldEquipmentComment().getText());
        newDive.setPictures(getPictureTableModel().getPictures());
    }

    private void initialize() {        
        buddyDictionary = new DefaultDictionary();
        Iterator<Buddy> bit = mainWindow.getLogBook().getMasterdata().getBuddys().iterator();
        while (bit.hasNext()) {
            Buddy buddy = bit.next();
            buddyDictionary.addEntry(buddy.getFirstname()+" "+buddy.getLastname()); //$NON-NLS-1$
        }
        diveTypeDictionary = new DefaultDictionary();
        Iterator<DiveType> tit = mainWindow.getLogBook().getMasterdata().getDivetypes().iterator();
        while (tit.hasNext()) {
            DiveType type = tit.next();
            diveTypeDictionary.addEntry(type.getDescription());
        }
        diveActivityDictionary = new DefaultDictionary();
        Iterator<DiveActivity> ait = mainWindow.getLogBook().getMasterdata().getDiveactivities().iterator();
        while (ait.hasNext()) {
            DiveActivity activity = ait.next();
            diveActivityDictionary.addEntry(activity.getDescription());
        }
        divePlaceDictionary = new DefaultDictionary();
        countryDicitonary = new DefaultDictionary();
        Iterator<DiveSite> pcit = mainWindow.getLogBook().getMasterdata().getDiveSites().iterator();
        while(pcit.hasNext()) {
            DiveSite site = pcit.next();
            String place = site.getSpot();
            String country = site.getCountry();
            divePlaceDictionary.addEntry(place);
            countryDicitonary.addEntry(country);
        }
        suitDictionary = new DefaultDictionary();
        Iterator<Suit> sit = mainWindow.getLogBook().getMasterdata().getSuits().iterator();
        while (sit.hasNext()) {
            Suit suit = sit.next();
            suitDictionary.addEntry(suit.getDescription());
        }
        glovesDictionary = new DefaultDictionary();
        Iterator<GloveType> git = mainWindow.getLogBook().getMasterdata().getGloveTypes().iterator();
        while (git.hasNext()) {
            GloveType glove = git.next();
            glovesDictionary.addEntry(glove.getDescription());
        }        
                
        this.setLayout(new java.awt.BorderLayout());
        this.add(getButtonPanel(), java.awt.BorderLayout.SOUTH);
        this.add(getJTabbedPane(), java.awt.BorderLayout.CENTER);
                    
        this.setName(Messages.getString("dive_details")); //$NON-NLS-1$
        new MnemonicFactory(this);
    }

    public void setBuddy(String buddys) {
        fieldBuddy.setText(buddys);
    }
    
    public void setDiveType(String divetypes) {
        fieldDiveType.setText(divetypes);
    }

    public void setDiveActivity(String diveactivities) {
        fieldDiveActivity.setText(diveactivities);
    }
    public void setPlace(String place) {
        fieldPlace.setText(place);
    }
    public void setCountry(String country) {
        fieldCountry.setText(country);
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
            if (parentWindow != null)
            {
                buttonPanel.add(getCloseButton(), gridBagConstraints1);
            }
            gridBagConstraints1.gridx = 1;
            gridBagConstraints1.gridy = 0;
            gridBagConstraints1.insets = new java.awt.Insets(5, 5, 5, 100);            
            if (parentWindow != null)
            {
                buttonPanel.add(getCancelButton(), gridBagConstraints1);
            }
        }
        return buttonPanel;
    }

    private JTabbedPane getJTabbedPane() {
        if (jTabbedPane == null) {
            jTabbedPane = new JTabbedPane();
            jTabbedPane.addTab(Messages.getString("data"), null, getDiveData(), null); //$NON-NLS-1$
            jTabbedPane.addTab(Messages.getString("equipment"), null, getPanelEquipment(), null); //$NON-NLS-1$
            jTabbedPane.addTab(Messages.getString("profile"), null, getPanelProfile(), null); //$NON-NLS-1$
            jTabbedPane.addTab(Messages.getString("pictures"), null, getPanelPictures(), null); //$NON-NLS-1$
        }
        return jTabbedPane;
    }
    
    public int getSelectedTab() {
        return getJTabbedPane().getSelectedIndex();
    }
    
    public void setSelectedTab(int index) {
        getJTabbedPane().setSelectedIndex(index);
    }

    private JButton getCloseButton() {
        if (closeButton == null) {
            closeButton = new JButton();
            closeButton.setText(Messages.getString("close")); //$NON-NLS-1$
            closeButton.addActionListener(this);
            closeButton.setEnabled(!readonly);
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

    private JPanel getDiveData() {
        if (diveData == null) {
            labelDiveNumber = new JLabel();
            diveData = new JPanel();
            labelPlace = new JLabel();
            labelAMV = new JLabel();
            labelDate = new JLabel();
            labelCity = new JLabel();
            labelCountry = new JLabel();
            labelTime = new JLabel();
            labelDepth = new JLabel();
            labelAverageDepth = new JLabel();
            labelDuration = new JLabel();
            labelTemperature = new JLabel();
            labelSurfaceTemperature = new JLabel();
            labelVisibility = new JLabel();
            labelBuddy = new JLabel();
            labelDiveType = new JLabel();
            labelDiveActivity = new JLabel();
            labelComment = new JLabel();
            diveData.setLayout(new GridBagLayout());
            labelDiveNumber.setText(Messages.getString("dive_no")); //$NON-NLS-1$
            labelDiveNumber.setName(Messages.getString("dive_no")); //$NON-NLS-1$
            //labelDiveNumber.setBounds(5, 5, 85, 18);
            labelPlace.setText(Messages.getString("place")); //$NON-NLS-1$
            labelPlace.setName(Messages.getString("place")); //$NON-NLS-1$
            //labelPlace.setBounds(190, 5, 75, 17);
            labelCity.setText(Messages.getString("city")); //$NON-NLS-1$
            labelCity.setName(Messages.getString("city")); //$NON-NLS-1$
            labelDate.setText(Messages.getString("date")); //$NON-NLS-1$
            labelDate.setName(Messages.getString("date")); //$NON-NLS-1$
            //labelDate.setBounds(5, 25, 85, 17);
            labelCountry.setText(Messages.getString("country")); //$NON-NLS-1$
            labelCountry.setName(Messages.getString("country")); //$NON-NLS-1$
            //labelCountry.setBounds(190, 25, 75, 17);
            labelTime.setText(Messages.getString("time")); //$NON-NLS-1$
            labelTime.setName("labelTime"); //$NON-NLS-1$
            //labelTime.setBounds(5, 45, 85, 17);
            labelDepth.setText(Messages.getString("depth")+" ["+UnitConverter.getDisplayAltitudeUnit()+"]"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            labelDepth.setName("labelDepth"); //$NON-NLS-1$
            //labelDepth.setBounds(190, 45, 75, 17);
            labelAverageDepth.setText(Messages.getString("average_depth")+" ["+UnitConverter.getDisplayAltitudeUnit()+"]"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            labelAverageDepth.setName("labelAverageDepth"); //$NON-NLS-1$
            //labelAverageDepth.setBounds(330, 45, 100, 17);
            labelDuration.setText(Messages.getString("duration")+" ["+UnitConverter.getDisplayTimeUnit()+"]"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            labelDuration.setName("labelDuration"); //$NON-NLS-1$
            //labelDuration.setBounds(495, 45, 80, 17);
            labelTemperature.setText(Messages.getString("temp")+" ["+UnitConverter.getDisplayTemperatureUnit()+"]"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            labelTemperature.setName("labelTemperature"); //$NON-NLS-1$
            //labelTemperature.setBounds(5, 65, 85, 17);
            labelSurfaceTemperature.setText(Messages.getString("surfacetemp")+" ["+UnitConverter.getDisplayTemperatureUnit()+"]"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            labelSurfaceTemperature.setName("labeSurfaceTemperature");
            labelVisibility.setText(Messages.getString("visibility")); //$NON-NLS-1$
            labelVisibility.setName(Messages.getString("visibility")); //$NON-NLS-1$
            //labelVisibility.setBounds(190, 65, 75, 17);
            labelAMV.setText(Messages.getString("amv")+" ["+UnitConverter.getDisplayAMVUnit()+"]"); //$NON-NLS-1$
            labelAMV.setName(Messages.getString("amv")); //$NON-NLS-1$
            //labelAMV.setBounds(495, 65, 75, 17);
            labelBuddy.setText(Messages.getString("buddy")); //$NON-NLS-1$
            labelBuddy.setName(Messages.getString("buddy")); //$NON-NLS-1$
            //labelBuddy.setBounds(5, 85, 85, 17);
            labelDiveType.setText(Messages.getString("divetype")); //$NON-NLS-1$
            labelDiveType.setName(Messages.getString("divetype")); //$NON-NLS-1$
            //labelDiveType.setBounds(5, 105, 85, 17);
            labelDiveActivity.setText(Messages.getString("diveactivity")); //$NON-NLS-1$
            labelDiveActivity.setName(Messages.getString("diveactivity")); //$NON-NLS-1$
            //labelDiveActivity.setBounds(5, 125, 85, 17);
            labelComment.setText(Messages.getString("comment")); //$NON-NLS-1$
            labelComment.setName(Messages.getString("comment")); //$NON-NLS-1$
            //labelComment.setBounds(5, 145, 85, 18);
            diveData.setName(Messages.getString("data")); //$NON-NLS-1$
            
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.weightx = 0.5;
            gridBagConstraints1.weighty = 0.5;
            gridBagConstraints1.anchor = GridBagConstraints.EAST;
            gridBagConstraints1.insets = new java.awt.Insets(1,1,1,1);
            
            gridBagConstraints1.gridy = 0;
            gridBagConstraints1.gridx = 0;
            gridBagConstraints1.fill = GridBagConstraints.NONE;
            diveData.add(labelDiveNumber, gridBagConstraints1);
            gridBagConstraints1.gridx = 1;
            gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
            diveData.add(getFieldDiveNumber(), gridBagConstraints1);
            gridBagConstraints1.gridx = 2;
            gridBagConstraints1.fill = GridBagConstraints.NONE;
            diveData.add(labelDate, gridBagConstraints1);
            gridBagConstraints1.gridx = 3;
            gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
            diveData.add(getFieldDate(), gridBagConstraints1);
            gridBagConstraints1.gridx = 4;
            gridBagConstraints1.fill = GridBagConstraints.NONE;
            diveData.add(labelTime, gridBagConstraints1);
            gridBagConstraints1.gridx = 5;
            gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
            diveData.add(getFieldTime(), gridBagConstraints1);

            
            gridBagConstraints1.gridx = 0;
            gridBagConstraints1.gridy = 1;            
            gridBagConstraints1.fill = GridBagConstraints.NONE;
            diveData.add(labelTemperature, gridBagConstraints1);
            gridBagConstraints1.gridx = 1;
            gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
            diveData.add(getFieldTemperature(), gridBagConstraints1);
            gridBagConstraints1.gridx = 2;
            gridBagConstraints1.fill = GridBagConstraints.NONE;
            diveData.add(labelSurfaceTemperature, gridBagConstraints1);
            gridBagConstraints1.gridx = 3;
            gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
            diveData.add(getFieldSurfaceTemperature(), gridBagConstraints1);
            gridBagConstraints1.gridx = 4;
            gridBagConstraints1.fill = GridBagConstraints.NONE;
            diveData.add(labelAverageDepth, gridBagConstraints1);
            gridBagConstraints1.gridx = 5;
            gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
            diveData.add(getFieldAverageDepth(), gridBagConstraints1);

            gridBagConstraints1.gridx = 0;
            gridBagConstraints1.gridy = 2;            
            gridBagConstraints1.fill = GridBagConstraints.NONE;
            diveData.add(labelDepth, gridBagConstraints1);
            gridBagConstraints1.gridx = 1;
            gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
            diveData.add(getFieldDepth(), gridBagConstraints1);
            gridBagConstraints1.gridx = 2;
            gridBagConstraints1.fill = GridBagConstraints.NONE;
            diveData.add(labelDuration, gridBagConstraints1);
            gridBagConstraints1.gridx = 3;
            gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
            diveData.add(getFieldDuration(), gridBagConstraints1);
            gridBagConstraints1.fill = GridBagConstraints.NONE;
            gridBagConstraints1.gridx = 4;            
            diveData.add(labelAMV, gridBagConstraints1);
            gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints1.gridx = 5;
            diveData.add(getFieldAMV(), gridBagConstraints1);
            
            gridBagConstraints1.gridx = 0;
            gridBagConstraints1.gridy = 3;
            gridBagConstraints1.fill = GridBagConstraints.NONE;
            diveData.add(labelVisibility, gridBagConstraints1);
            gridBagConstraints1.gridx = 1;
            gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints1.gridwidth=4;
            diveData.add(getFieldVisibility(), gridBagConstraints1);
            
            gridBagConstraints1.gridwidth=1;
            gridBagConstraints1.gridx = 0;
            gridBagConstraints1.gridy = 4;                        
            gridBagConstraints1.fill = GridBagConstraints.NONE;
            diveData.add(labelBuddy, gridBagConstraints1);
            gridBagConstraints1.gridx = 1;
            gridBagConstraints1.gridwidth=4;
            gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
            diveData.add(getFieldBuddy(), gridBagConstraints1);
            gridBagConstraints1.gridx = 5;
            gridBagConstraints1.gridwidth=1;
            diveData.add(getBuddy(), gridBagConstraints1);
                                
            gridBagConstraints1.gridx = 0;
            gridBagConstraints1.gridy = 5;
            gridBagConstraints1.fill = GridBagConstraints.NONE;
            diveData.add(labelPlace, gridBagConstraints1);
            gridBagConstraints1.gridx = 1;
            gridBagConstraints1.gridwidth=4;
            gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
            diveData.add(getFieldPlace(), gridBagConstraints1);
            gridBagConstraints1.gridx = 5;
            gridBagConstraints1.gridwidth=1;
            diveData.add(getDiveSiteChooseButton(), gridBagConstraints1);
                      
            gridBagConstraints1.gridx = 0;
            gridBagConstraints1.gridy = 6;
            gridBagConstraints1.fill = GridBagConstraints.NONE;
            diveData.add(labelCity, gridBagConstraints1);
            gridBagConstraints1.gridx = 1;
            gridBagConstraints1.gridwidth=4;
            gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
            diveData.add(getFieldCity(), gridBagConstraints1);
            gridBagConstraints1.gridx = 5;
            gridBagConstraints1.gridwidth=1;
            diveData.add(getDiveSiteNewButton(), gridBagConstraints1);
                      
            gridBagConstraints1.gridheight=1;
            gridBagConstraints1.gridx = 0;
            gridBagConstraints1.gridy = 7;
            gridBagConstraints1.fill = GridBagConstraints.NONE;
            diveData.add(labelCountry, gridBagConstraints1);
            gridBagConstraints1.gridx = 1;
            gridBagConstraints1.gridwidth=4;
            gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
            diveData.add(getFieldCountry(), gridBagConstraints1);
            gridBagConstraints1.gridx = 5;
            gridBagConstraints1.gridwidth=1;
            diveData.add(getDiveSiteDetailButton(), gridBagConstraints1);

            gridBagConstraints1.gridwidth=1;
            gridBagConstraints1.gridx = 0;
            gridBagConstraints1.gridy = 8;                        
            gridBagConstraints1.fill = GridBagConstraints.NONE;
            diveData.add(labelDiveType, gridBagConstraints1);
            gridBagConstraints1.gridx = 1;
            gridBagConstraints1.gridwidth=4;
            gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
            diveData.add(getFieldDiveType(), gridBagConstraints1);
            gridBagConstraints1.gridx = 5;
            gridBagConstraints1.gridwidth=1;
            diveData.add(getDiveType(), gridBagConstraints1);

            gridBagConstraints1.gridwidth=1;
            gridBagConstraints1.gridx = 0;
            gridBagConstraints1.gridy = 9;                        
            gridBagConstraints1.fill = GridBagConstraints.NONE;
            diveData.add(labelDiveActivity, gridBagConstraints1);
            gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints1.gridx = 1;
            gridBagConstraints1.gridwidth=4;
            diveData.add(getFieldDiveActivity(), gridBagConstraints1);
            gridBagConstraints1.gridx = 5;
            gridBagConstraints1.gridwidth=1;
            diveData.add(getDiveActivity(), gridBagConstraints1);

            gridBagConstraints1.gridwidth=1;
            gridBagConstraints1.gridx = 0;
            gridBagConstraints1.gridy = 10;                        
            gridBagConstraints1.fill = GridBagConstraints.NONE;
            gridBagConstraints1.anchor = GridBagConstraints.NORTHEAST;
            diveData.add(labelComment, gridBagConstraints1);
            gridBagConstraints1.gridx = 1;
            gridBagConstraints1.gridwidth=5;
            gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
            diveData.add(new JScrollPane(getFieldComment()), gridBagConstraints1);
            
        }
        return diveData;
    }

    private JTextField getFieldDiveNumber() {
        if (fieldDiveNumber == null) {
            fieldDiveNumber = new JTextField();
            fieldDiveNumber.setName("fieldDiveNumber"); //$NON-NLS-1$
            fieldDiveNumber.setEditable(!readonly);
        }
        return fieldDiveNumber;
    }

    private JTextField getFieldPlace() {
        if (fieldPlace == null) {
            fieldPlace = new AutoCompleteTextField(divePlaceDictionary, null);
            fieldPlace.setName("fieldPlace"); //$NON-NLS-1$
            fieldPlace.addFocusListener(new FocusListener() {
                String oldValue;
                public void focusGained(FocusEvent e) {
                    oldValue = fieldPlace.getText();
                }
                public void focusLost(FocusEvent e) {
                    String newValue = fieldPlace.getText();
                    if (newValue != null && !newValue.equals(oldValue)) {
                        if ("".equals(newValue)) {
                            newDive.setDiveSiteId("");
                            getFieldCountry().setText(""); //$NON-NLS-1$
                        } else {
                            DiveSite site = masterdata.getDiveSiteBySpot(newValue);
                            if (site != null) {
                                newDive.setDiveSiteId(site.getPrivateId());
                                if (site.getCountry() != null) {
                                    getFieldCity().setText(site.getCity());
                                    getFieldCountry().setText(site.getCountry());
                                }
                                getDiveSiteDetailButton().setEnabled(true);
                            } else {
                                getDiveSiteDetailButton().setEnabled(false);
                            }
                        }
                    }
                }
                
            });
            fieldPlace.setEditable(!readonly);
        }
        return fieldPlace;
    }

    private JTextField getFieldCity() {
        if (fieldCity == null) {
            fieldCity = new JTextField();
            fieldCity.setName("fieldCity"); //$NON-NLS-1$
            fieldCity.setEditable(false);
        }
        return fieldCity;
    }

    private JTextField getFieldDate() {
        if (fieldDate == null) {
            fieldDate = new JTextField();
            fieldDate.setName("fieldDate"); //$NON-NLS-1$
            fieldDate.setEditable(!readonly);
        }
        return fieldDate;
    }

    private JTextField getFieldCountry() {
        if (fieldCountry == null) {
            fieldCountry = new AutoCompleteTextField(countryDicitonary, null);
            fieldCountry.setName("fieldCountry"); //$NON-NLS-1$
            fieldCountry.setEditable(false);
        }
        return fieldCountry;
    }

    private JButton getDiveSiteChooseButton() {
        if (diveSiteChooseButton == null) {
            diveSiteChooseButton = new JButton(Messages.getString("choose_site")); //$NON-NLS-1$
            diveSiteChooseButton.addActionListener(this);
            diveSiteChooseButton.setEnabled(!readonly);
        }
        return diveSiteChooseButton;
    }

    private JButton getDiveSiteNewButton() {
        if (diveSiteNewButton == null) {
            diveSiteNewButton = new JButton(Messages.getString("new_site")); //$NON-NLS-1$
            diveSiteNewButton.addActionListener(this);
            diveSiteNewButton.setEnabled(!readonly);
        }
        return diveSiteNewButton;
    }

    private JButton getDiveSiteDetailButton() {
        if (diveSiteDetailButton == null) {
            diveSiteDetailButton = new JButton(Messages.getString("site_details")); //$NON-NLS-1$
            diveSiteDetailButton.addActionListener(this);
        }
        return diveSiteDetailButton;
    }

    private JTextField getFieldTime() {
        if (fieldTime == null) {
            fieldTime = new JTextField();
            fieldTime.setName("fieldTime"); //$NON-NLS-1$
            fieldTime.setEditable(!readonly);
        }
        return fieldTime;
    }

    private JTextField getFieldDepth() {
        if (fieldDepth == null) {
            fieldDepth = new JTextField();
            fieldDepth.setName("fieldDepth"); //$NON-NLS-1$
            fieldDepth.setEditable(!readonly);
        }
        return fieldDepth;
    }
    
    private JTextField getFieldAverageDepth() {
        if (fieldAverageDepth == null) {
            fieldAverageDepth = new JTextField();
            fieldAverageDepth.setName("fieldAverageDepth"); //$NON-NLS-1$
            fieldAverageDepth.setEditable(!readonly);
        }
        return fieldAverageDepth;
    }

    private JTextField getFieldDuration() {
        if (fieldDuration == null) {
            fieldDuration = new JTextField();
            fieldDuration.setName("fieldDuration"); //$NON-NLS-1$
            fieldDuration.setEditable(!readonly);
        }
        return fieldDuration;
    }

    private JTextField getFieldTemperature() {
        if (fieldTemperature == null) {
            fieldTemperature = new JTextField();
            fieldTemperature.setName("fieldTemperature"); //$NON-NLS-1$
            fieldTemperature.setEditable(!readonly);
        }
        return fieldTemperature;
    }
    
    private JTextField getFieldSurfaceTemperature() {
        if (fieldSurfaceTemperature == null) {
            fieldSurfaceTemperature = new JTextField();
            fieldSurfaceTemperature.setName("fieldSurfaceTemperature"); //$NON-NLS-1$
            fieldSurfaceTemperature.setEditable(!readonly);
        }
        return fieldSurfaceTemperature;
    }

    private JTextField getFieldVisibility() {
        if (fieldVisibility == null) {
            fieldVisibility = new JTextField();
            fieldVisibility.setName("fieldVisibility"); //$NON-NLS-1$
            fieldVisibility.setEditable(!readonly);
        }
        return fieldVisibility;
    }

    private JTextField getFieldAMV() {
        if (fieldAMV == null) {
            fieldAMV = new JTextField();
            fieldAMV.setName("amv"); //$NON-NLS-1$
            fieldAMV.setEditable(!readonly);
        }
        return fieldAMV;
    }

    private JTextField getFieldBuddy() {
        if (fieldBuddy == null) {
            fieldBuddy = new AutoCompleteTextField(buddyDictionary, ",");
            fieldBuddy.setName("fieldBuddy"); //$NON-NLS-1$
            fieldBuddy.setEditable(!readonly);
        }
        return fieldBuddy;
    }

    private JButton getBuddy() {
        if (buttonBuddyWindow == null) {
            buttonBuddyWindow = new JButton(Messages.getString("add_buddy")); //$NON-NLS-1$
            buttonBuddyWindow.addActionListener(this);
            buttonBuddyWindow.setEnabled(!readonly);
        }
        return buttonBuddyWindow;
    }
        
    private JTextField getFieldDiveType() {
        if (fieldDiveType == null) {
            fieldDiveType = new AutoCompleteTextField(diveTypeDictionary, ",");
            fieldDiveType.setName("fieldDiveType"); //$NON-NLS-1$
            fieldDiveType.setEditable(!readonly);
        }
        return fieldDiveType;
    }

    private JButton getDiveType() {
        if (buttonDiveType == null) {
            buttonDiveType = new JButton(Messages.getString("add_divetype"));
            buttonDiveType.addActionListener(this);
            buttonDiveType.setEnabled(!readonly);
        }
        return buttonDiveType;
    }

    private JTextField getFieldDiveActivity() {
        if (fieldDiveActivity == null) {
            fieldDiveActivity = new AutoCompleteTextField(diveActivityDictionary, ",");
            fieldDiveActivity.setName("fieldDiveActivity"); //$NON-NLS-1$
            fieldDiveActivity.setEditable(!readonly);
        }
        return fieldDiveActivity;
    }

    private JButton getDiveActivity() {
        if (buttonDiveActivity == null) {
            buttonDiveActivity = new JButton(Messages.getString("add_diveactivity"));
            buttonDiveActivity.addActionListener(this);
            buttonDiveActivity.setEnabled(!readonly);
        }
        return buttonDiveActivity;
    }

    private JPanel getPanelEquipment() {
        if (panelEquipment == null) {
            panelEquipment = new JPanel();
            panelEquipment.setLayout(null);
            labelSet = new JLabel();            
            labelSuit = new JLabel();
            labelGloves = new JLabel();
            labelWeight = new JLabel();
            labelTanks = new JLabel();
            labelEquipmentComment = new JLabel();
            labelSet.setText(Messages.getString("equipmentset")); //$NON-NLS-1$
            labelSuit.setText(Messages.getString("suit")); //$NON-NLS-1$
            labelSuit.setName(Messages.getString("suit")); //$NON-NLS-1$
            labelSuit.setBounds(5, 25, 85, 18);
            labelEquipmentComment.setText(Messages.getString("comment")); //$NON-NLS-1$
            labelEquipmentComment.setName(Messages.getString("comment")); //$NON-NLS-1$
            labelEquipmentComment.setBounds(350, 25, 85, 17);
            labelGloves.setText(Messages.getString("gloves")); //$NON-NLS-1$
            labelGloves.setName(Messages.getString("gloves")); //$NON-NLS-1$
            labelGloves.setBounds(5, 45, 85, 17);
            labelWeight.setText(Messages.getString("weight")); //$NON-NLS-1$
            labelWeight.setName(Messages.getString("weight")); //$NON-NLS-1$
            labelWeight.setBounds(5, 65, 85, 17);
            labelTanks.setText(Messages.getString("tanks")); //$NON-NLS-1$
            labelTanks.setName(Messages.getString("tanks")); //$NON-NLS-1$
            labelTanks.setBounds(5, 85, 85, 17);
            panelEquipment.add(labelSet, null);
            panelEquipment.add(getFieldSet(), null);
            panelEquipment.add(labelSuit, null);
            panelEquipment.add(getFieldSuit(), null);
            panelEquipment.add(labelGloves, null);
            panelEquipment.add(getFieldGloves(), null);
            panelEquipment.add(labelWeight, null);
            panelEquipment.add(getFieldWeight(), null);
            panelEquipment.add(labelTanks, null);
            panelEquipment.add(getTankPanel(), null);
            panelEquipment.add(labelEquipmentComment, null);
            panelEquipment.add(getFieldEquipmentComment(), null);
            panelEquipment.add(getButtonAddTank(), null);
            panelEquipment.add(getButtonDeleteTank(), null);
        }
        return panelEquipment;
    }

    private TankTableModel getTankTableModel() {
        if (tankTableModel == null) {
            if (newDive == null) {
                tankTableModel = new TankTableModel();
            } else {
                tankTableModel = new TankTableModel(newDive.getEquipment()
                        .getTanks(), UnitConverter
                        .getSystem(newDive.getUnits()));
            }
        }
        return tankTableModel;
    }


    private JPanel getPanelProfile() {
        if (panelProfile == null) {
            panelProfile = new JPanel();
            panelProfile.setLayout(new BorderLayout());
            panelProfile.add(getDiveProfile(), BorderLayout.CENTER);
        }
        return panelProfile;
    }

    private JPanel getPanelPictures() {
        if (panelPictures == null) {
            panelPictures = new JPanel();
            panelPictures.setLayout(new BorderLayout());
            panelPictures.add(getPictureButtonPanel(), BorderLayout.NORTH);
            panelPictures.add(getPictureTablePane(), BorderLayout.CENTER);
        }
        return panelPictures;
    }

    private JTextArea getFieldComment() {
        if (fieldComment == null) {
            fieldComment = new JTextArea(10,2);
            fieldComment.setEditable(!readonly);
        }
        return fieldComment;
    }

    private JComboBox getFieldSet() {
        if (fieldSet == null) {
            Vector<String> setNames = new Vector<String>();
            setNames.add("");
            Iterator<EquipmentSet> it = mainWindow.getLogBook().getMasterdata().getEquipmentSets().iterator();
            while (it.hasNext()) {
                setNames.add(it.next().getName());
            }
            fieldSet = new JComboBox(setNames);
            fieldSet.setName("fieldSet"); //$NON-NLS-1$
            fieldSet.setBounds(88, 5, 250, 19);
            fieldSet.addItemListener(this);
            fieldSet.setEditable(!readonly);
            fieldSet.setEnabled(!readonly);
        }
        return fieldSet;
    }

    private JTextField getFieldSuit() {
        if (fieldSuit == null) {
            fieldSuit = new AutoCompleteTextField(suitDictionary, ",");
            fieldSuit.setColumns(4);
            fieldSuit.setName("fieldSuit"); //$NON-NLS-1$
            fieldSuit.setBounds(88, 25, 250, 19);
            fieldSuit.add(getPopupMenuSuits());
            if (!readonly) {
                fieldSuit.addMouseListener(new MouseListener() {
    
                    public void mouseClicked(MouseEvent e) {
                        // do nothing
                    }
    
                    public void mousePressed(MouseEvent e) {
                        if (e.isPopupTrigger()) {
                            getPopupMenuSuits().show(fieldSuit, e.getX(), e.getY());
                        }
                    }
    
                    public void mouseReleased(MouseEvent e) {
                        // do nothing
                    }
    
                    public void mouseEntered(MouseEvent e) {
                        // do nothing
                    }
    
                    public void mouseExited(MouseEvent e) {
                        // do nothing
                    }
                    
                });
            }
            fieldSuit.setEditable(!readonly);
        }
        return fieldSuit;
    }
    
    private JPopupMenu getPopupMenuSuits() {
        if (popupMenuSuits == null) {
            popupMenuSuits = new JPopupMenu();
            popupMenuItemsSuits = new HashSet<JMenuItem>();
            Iterator<String> it = suitDictionary.getAll().iterator();
            while(it.hasNext()) {
                JMenuItem item = new JMenuItem(it.next());
                item.addActionListener(this);
                popupMenuItemsSuits.add(item);
                popupMenuSuits.add(item);
            }
        }
        return popupMenuSuits;
    }

    private JTextField getFieldGloves() {
        if (fieldGloves == null) {
            fieldGloves = new AutoCompleteTextField(glovesDictionary, ",");
            fieldGloves.setColumns(4);
            fieldGloves.setName("fieldGloves"); //$NON-NLS-1$
            fieldGloves.setBounds(88, 45, 250, 19);
            fieldGloves.add(getPopupMenuGloves());
            if (!readonly) {
                fieldGloves.addMouseListener(new MouseListener() {
    
                    public void mouseClicked(MouseEvent e) {
                        // do nothing
                    }
    
                    public void mousePressed(MouseEvent e) {
                        if (e.isPopupTrigger()) {
                            getPopupMenuGloves().show(fieldGloves, e.getX(), e.getY());
                        }
                    }
    
                    public void mouseReleased(MouseEvent e) {
                        // do nothing
                    }
    
                    public void mouseEntered(MouseEvent e) {
                        // do nothing
                    }
    
                    public void mouseExited(MouseEvent e) {
                        // do nothing
                    }
                    
                });
            }
            fieldGloves.setEditable(!readonly);
        }
        return fieldGloves;
    }
    
    private JPopupMenu getPopupMenuGloves() {
        if (popupMenuGloves == null) {
            popupMenuGloves = new JPopupMenu();
            popupMenuItemsGloves = new HashSet<JMenuItem>();
            Iterator<String> it = glovesDictionary.getAll().iterator();
            while(it.hasNext()) {
                JMenuItem item = new JMenuItem(it.next());
                item.addActionListener(this);
                popupMenuItemsGloves.add(item);
                popupMenuGloves.add(item);
            }
            
        }
        return popupMenuGloves;
    }

    private JTextField getFieldWeight() {
        if (fieldWeight == null) {
            fieldWeight = new JTextField();
            fieldWeight.setColumns(4);
            fieldWeight.setName("fieldWeight"); //$NON-NLS-1$
            fieldWeight.setBounds(88, 65, 250, 19);
            fieldWeight.setEditable(!readonly);
        }
        return fieldWeight;
    }

    private JPanel getTankPanel() {
        if (tankPanel == null) {
            tankPanel = new JPanel();
            tankPanel.setLayout(new BoxLayout(tankPanel, BoxLayout.Y_AXIS));
            tankPanel.add(getTankTable().getTableHeader());
            tankPanel.add(new JScrollPane(getTankTable()), null);
            tankPanel.setBounds(5, 135, 552, 90);
        }
        return tankPanel;
    }

    private JTable getTankTable() {
        if (tankTable == null) {
            tankTable = new JTable(getTankTableModel());
            TableColumnModel tcm = tankTable.getColumnModel();
            tcm.getColumn(0).setResizable(true);
            tcm.getColumn(0).setPreferredWidth(60);
            tcm.getColumn(0).setHeaderValue(
                    getTankTableModel().getColumnName(0));
            tcm.getColumn(1).setResizable(true);
            tcm.getColumn(1).setPreferredWidth(60);
            tcm.getColumn(1).setHeaderValue(
                    getTankTableModel().getColumnName(1));
            tcm.getColumn(2).setResizable(true);
            tcm.getColumn(2).setPreferredWidth(60);
            tcm.getColumn(2).setHeaderValue(
                    getTankTableModel().getColumnName(2));
            tcm.getColumn(3).setResizable(true);
            tcm.getColumn(3).setPreferredWidth(60);
            tcm.getColumn(3).setHeaderValue(
                    getTankTableModel().getColumnName(3));
            tcm.getColumn(4).setResizable(true);
            tcm.getColumn(4).setPreferredWidth(60);
            tcm.getColumn(4).setHeaderValue(
                    getTankTableModel().getColumnName(4));
            tankTable.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        int row = tankTable.getSelectedRow();
                        if (row >= 0
                                && row <= getTankTableModel().getTanks().size()) {
                            Tank tank = getTankTableModel().getTank(row);
                            editTank(tank);
                        }
                    }
                }
            });

        }
        return tankTable;
    }

    protected void editTank(Tank tank) {
        TankDetailWindow tdw = new TankDetailWindow(this.parentWindow, mainWindow.getGasDatabase(), getTankTableModel(), tank, UnitConverter.getSystem(this.newDive.getUnits()), UnitConverter.getDisplaySystem());
        tdw.setVisible(true);
    }

    protected void addTank() {
        TankDetailWindow tdw = new TankDetailWindow(this.parentWindow, mainWindow.getGasDatabase(), getTankTableModel(), UnitConverter.getSystem(this.newDive.getUnits()), UnitConverter.getDisplaySystem());
        tdw.setVisible(true);
    }

    protected void addBuddy() {
        BuddyWindow bdw = new BuddyWindow(this.parentWindow, mainWindow, this, false, logbookChangeNotifier);
        bdw.setVisible(true);
    }

    protected void addDiveType() {
        DiveTypeWindow dtw = new DiveTypeWindow(this.parentWindow, mainWindow, this, false, logbookChangeNotifier);
        dtw.setVisible(true);
    }

    protected void addDiveActivity() {
        DiveActivityWindow daw = new DiveActivityWindow(this.parentWindow, mainWindow, this, false, logbookChangeNotifier);
        daw.setVisible(true);
    }

    protected void showDiveSiteDetail() {
        DiveSite site = masterdata.getDiveSite(newDive);
        DivesiteDetailWindow ddw = new DivesiteDetailWindow(this.parentWindow, mainWindow, this, site);
        ddw.setVisible(true);
    }

    protected void deleteTank() {
        int selected = getTankTable().getSelectedRow();
        if (selected >= 0 && selected < getTankTableModel().getTanks().size()) {
            getTankTableModel().removeTank(selected);
        }
    }

    private JButton getButtonAddTank() {
        if (buttonAddTank == null) {
            buttonAddTank = new JButton(Messages.getString("add_tank")); //$NON-NLS-1$
            buttonAddTank.setBounds(5, 105, 170, 20);
            buttonAddTank.addActionListener(this);
            buttonAddTank.setEnabled(!readonly);
        }
        return buttonAddTank;
    }

    private JButton getButtonDeleteTank() {
        if (buttonDeleteTank == null) {
            buttonDeleteTank = new JButton(Messages.getString("remove_tank")); //$NON-NLS-1$
            buttonDeleteTank.setBounds(175, 105, 170, 20);
            buttonDeleteTank.addActionListener(this);
            buttonDeleteTank.setEnabled(!readonly);
        }
        return buttonDeleteTank;
    }

    private JTextArea getFieldEquipmentComment() {
        if (fieldEquipmentComment == null) {
            fieldEquipmentComment = new JTextArea();
            fieldEquipmentComment.setBounds(440, 25, 217, 61);
            fieldEquipmentComment.setEditable(!readonly);
        }
        return fieldEquipmentComment;
    }
    
    private DiveProfile getDiveProfile() {
        if (diveProfile == null) {
            diveProfile = new DiveProfile(mainWindow.getLogBook().getProfileSettings());
            diveProfile.add(getProfilePopupMenu());
            diveProfile.addMouseListener(new MouseListener() {

                public void mouseClicked(MouseEvent e) {
                    // do nothing
                }

                public void mousePressed(MouseEvent e) {
                    if (e.isPopupTrigger()) {
                        getProfilePopupMenu().show(e.getComponent(), e.getX(), e.getY());
                    }
                }

                public void mouseReleased(MouseEvent e) {
                    // do nothing
                }

                public void mouseEntered(MouseEvent e) {
                    // do nothing
                }

                public void mouseExited(MouseEvent e) {
                    // do nothing
                }
                
            });

        }
        return diveProfile;
    }
    
    private PopupMenu getProfilePopupMenu() {
        if (popupMenu == null) {
            popupMenu = new PopupMenu();
            popupMenu.add(getCompareMenuItem());
            popupMenu.add(getRemoveComparisonChartsMenuItem());
            popupMenu.add(getCrosshairMenuItem());
            popupMenu.add(getExportPictureMenuItem());
        }
        return popupMenu;
    }
    
    private MenuItem getCompareMenuItem() {
        if (compareMenuItem == null) {
            compareMenuItem = new MenuItem(Messages.getString("compare"));
            compareMenuItem.addActionListener(this);
        }
        return compareMenuItem;
    }

    private CheckboxMenuItem getCrosshairMenuItem() {
        if (crosshairMenuItem == null) {
            crosshairMenuItem = new CheckboxMenuItem(Messages.getString("crosshair"));
            crosshairMenuItem.addActionListener(this);
            crosshairMenuItem.addItemListener(this);
        }
        return crosshairMenuItem;
    }
    
    private MenuItem getExportPictureMenuItem() {
        if (exportPictureMenuItem == null) {
            exportPictureMenuItem = new MenuItem(Messages.getString("save_image"));
            exportPictureMenuItem.addActionListener(this);
        }
        return exportPictureMenuItem;
    }
    
    private MenuItem getRemoveComparisonChartsMenuItem() {
        if (removeComparisonChartsMenuItem == null) {
            removeComparisonChartsMenuItem = new MenuItem(Messages.getString("remove_comparison"));
            removeComparisonChartsMenuItem.addActionListener(this);
        }
        return removeComparisonChartsMenuItem;
    }
    
    private JPanel getPictureButtonPanel() {
        if (pictureButtonPanel == null) {
            pictureButtonPanel = new JPanel();
            pictureButtonPanel.setLayout(new BoxLayout(pictureButtonPanel, BoxLayout.X_AXIS));
            if (!readonly) {
                pictureButtonPanel.add(getButtonAddPictures(), null);
                pictureButtonPanel.add(getButtonRemovePictures(), null);
            }
            pictureButtonPanel.add(getButtonStartSlideshow(), null);
        }
        return pictureButtonPanel;
    }
    
    private JButton getButtonAddPictures() {
        if (buttonAddPictures == null) {
            buttonAddPictures = new JButton(Messages.getString("add_images")); //$NON-NLS-1$
            buttonAddPictures.addActionListener(this);
            buttonAddPictures.setEnabled(!readonly);
        }
        return buttonAddPictures;
    }
    
    private JButton getButtonRemovePictures() {
        if (buttonRemovePictures == null) {
            buttonRemovePictures = new JButton(Messages.getString("remove_images")); //$NON-NLS-1$
            buttonRemovePictures.addActionListener(this);
            buttonRemovePictures.setEnabled(!readonly);
        }
        return buttonRemovePictures;
    }
    
    private JButton getButtonStartSlideshow() {
        if (buttonStartSlideshow == null) {
            buttonStartSlideshow = new JButton(Messages.getString("slideshow")); //$NON-NLS-1$
            buttonStartSlideshow.addActionListener(this);
        }
        return buttonStartSlideshow;
    }

    private JScrollPane getPictureTablePane() {
        if (pictureTablePane == null) {
            pictureTablePane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            pictureTablePane.setViewportView(getPictureTable());
        }
        return pictureTablePane;
    }
    
    private JTable getPictureTable() {
        if (pictureTable == null) {
            pictureTable = new JTable();
            pictureTable.setModel(getPictureTableModel());
            TableColumnModel tcm = pictureTable.getColumnModel();
            for (int i=0; i<getPictureTableModel().getColumnCount(); i++) {
                tcm.getColumn(i).setHeaderValue(getPictureTableModel().getColumnName(i));
            }
            pictureTable.getColumnModel().getColumn(0).setCellRenderer(new ImageCellRenderer());
            pictureTable.getColumnModel().getColumn(1).setPreferredWidth(10);
            String[] rotationLabels = getPictureTableModel().getRotationLabels();
            ComboBoxRenderer cbr = new ComboBoxRenderer(rotationLabels);
            pictureTable.getColumnModel().getColumn(1).setCellRenderer(cbr);
            ComboBoxEditor cbe = new ComboBoxEditor(rotationLabels);
            pictureTable.getColumnModel().getColumn(1).setCellEditor(cbe);
            TextAreaCellRenderer tacr = new TextAreaCellRenderer(1,50);
            tacr.setBackground(pictureTable.getBackground());
            tacr.setForeground(pictureTable.getForeground());
            pictureTable.getColumnModel().getColumn(2).setPreferredWidth(100);
            pictureTable.getColumnModel().getColumn(2).setCellRenderer(tacr);
            tacr = new TextAreaCellRenderer(1,50);
            tacr.setBackground(pictureTable.getBackground());
            tacr.setForeground(pictureTable.getForeground());
            pictureTable.getColumnModel().getColumn(2).setCellEditor(tacr);
            tacr = new TextAreaCellRenderer(4,50);
            tacr.setBackground(pictureTable.getBackground());
            tacr.setForeground(pictureTable.getForeground());
            pictureTable.getColumnModel().getColumn(3).setCellRenderer(tacr);
            tacr = new TextAreaCellRenderer(4,50);
            tacr.setBackground(pictureTable.getBackground());
            tacr.setForeground(pictureTable.getForeground());
            pictureTable.getColumnModel().getColumn(3).setPreferredWidth(200);
            pictureTable.getColumnModel().getColumn(3).setCellEditor(tacr);
            
            String[] ratingLabels = getPictureTableModel().getRatingLabels();
            cbr = new ComboBoxRenderer(ratingLabels);
            pictureTable.getColumnModel().getColumn(4).setCellRenderer(cbr);
            cbe = new ComboBoxEditor(ratingLabels);
            pictureTable.getColumnModel().getColumn(4).setCellEditor(cbe);

            pictureTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            pictureTable.setRowHeight(PictureTableModel.THUMB_MAX_Y);
            pictureTable.setEditingColumn(2);
            pictureTable.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        int row = pictureTable.getSelectedRow();
                        Picture picture = pictureTableModel.getPictures().get(row);
                        displayPicture(picture);
                    }
                }
            });

        }
        return pictureTable;
    }
    
    private PictureTableModel getPictureTableModel() {
        if (pictureTableModel == null) {
            pictureTableModel = new PictureTableModel(mainWindow.getStatusBar());
        }
        return pictureTableModel;
    }

    private void addPictures() {
        ExtensionFileFilter ff = new ExtensionFileFilter(Messages.getString("images"), "jpg"); //$NON-NLS-1$
        ff.addExtension("gif"); //$NON-NLS-1$
        ff.addExtension("jpg"); //$NON-NLS-1$
        ff.addExtension("jpeg"); //$NON-NLS-1$
        ff.addExtension("png"); //$NON-NLS-1$
        FileChooser fc = new FileChooser();
        fc.setFileFilter(ff);
        fc.setMultiSelectionEnabled(true);
        int ret = fc.showOpenDialog(this);
        if (ret == JFileChooser.APPROVE_OPTION) {
            File[] files = fc.getSelectedFiles();
            ArrayList<Picture> pictures = new ArrayList<Picture>();
            for (int i=0; i<files.length; i++) {
                if (!getPictureTableModel().containsFile(files[i])) {
                    Picture p = new Picture();
                    p.setFilename(files[i].getPath());
                    p.setName(files[i].getName());
                    p.setDescription(Messages.getString("default_description")); //$NON-NLS-1$
                    pictures.add(p);
                }
            }
            getPictureTableModel().add(pictures);
        }
    }
    
    private void removePictures() {
        int[] rows = getPictureTable().getSelectedRows();
        getPictureTableModel().remove(rows);
    }
    
    private void startSlideshow() {
        if (getPictureTableModel().getRowCount() > 0) {
            PictureViewerWindow pvw = new PictureViewerWindow(this.parentWindow, getPictureTableModel(), mainWindow.getLogBook());
            pvw.setVisible(true);
        }
    }
    
    private void displayPicture(Picture p) {
        PictureViewerWindow win = new PictureViewerWindow(this.parentWindow, p);
        win.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == closeButton) {
            close();
        } else if (e.getSource() == cancelButton) {
            cancel();
        } else if (e.getSource() == buttonAddTank) {
            addTank();
        } else if (e.getSource() == buttonBuddyWindow) {
            addBuddy();
        } else if (e.getSource() == buttonDiveActivity) {
            addDiveActivity();
        } else if (e.getSource() == buttonDiveType) {
            addDiveType();
        } else if (e.getSource() == diveSiteChooseButton) {
            chooseDiveSite();
        } else if (e.getSource() == diveSiteNewButton) {
            newDiveSite();
        } else if (e.getSource() == diveSiteDetailButton) {
            showDiveSiteDetail();
        } else if (e.getSource() == buttonDeleteTank) {
            deleteTank();
        } else if (e.getSource() == buttonAddPictures) {
            addPictures();
        } else if (e.getSource() == buttonRemovePictures) {
            removePictures();
        } else if (e.getSource() == buttonStartSlideshow) {
            startSlideshow();
        } else if (e.getSource() == compareMenuItem) {
            ProfileSelectionWindow selectionWindow = new ProfileSelectionWindow(parentWindow, mainWindow.getLogBook());
            selectionWindow.setModal(true);
            selectionWindow.setVisible(true);
            for (Iterator<JDive>it = selectionWindow.getSelectedDives().iterator(); it.hasNext();) {
                JDive dive = it.next();
                getDiveProfile().addComparisonProfile(dive);
            }
        } else if (e.getSource() == removeComparisonChartsMenuItem) {
            removeComparisionCharts();
        } else if (e.getSource() == exportPictureMenuItem) {
            saveProfilePicture();
        } else if (popupMenuItemsGloves.contains(e.getSource())) {
            getFieldGloves().setText(((JMenuItem)e.getSource()).getText());
        } else if (popupMenuItemsSuits.contains(e.getSource())) {
            getFieldSuit().setText(((JMenuItem)e.getSource()).getText());
        }
    }

    private void removeComparisionCharts() {
        getDiveProfile().removeComparisonProfiles();
    }

    private void saveProfilePicture() {
        FileChooser fc = new FileChooser();
        ExtensionFileFilter ff = new ExtensionFileFilter(Messages.getString("png"), "png"); //$NON-NLS-1$
        ff.addExtension("png"); //$NON-NLS-1$
        fc.addChoosableFileFilter(ff);
        ff = new ExtensionFileFilter(Messages.getString("jpg"), "jpg"); //$NON-NLS-1$
        ff.addExtension("jpg"); //$NON-NLS-1$
        ff.addExtension("jpeg"); //$NON-NLS-1$
        fc.addChoosableFileFilter(ff);
        fc.setFileFilter(ff);
        int ret = fc.showSaveDialog(this);
        if (ret == FileChooser.APPROVE_OPTION) {
            File f = fc.getSelectedFile();
            if (f.getName().indexOf(".") < 0) { //$NON-NLS-1$
                String extension = "jpg";
                FileFilter sff = fc.getFileFilter();
                if (sff instanceof ExtensionFileFilter) {
                    ff = (ExtensionFileFilter) sff;
                    extension = ff.getDefaultExtension();
                }
                String path = f.getPath();
                f = new File(path + "."+extension); //$NON-NLS-1$
            }
            if (f.exists()) {
                int r = JOptionPane.showConfirmDialog(this, Messages.getString("file_exists_save_anyway"), Messages.getString("warning.file_already_exists"),
                        JOptionPane.YES_NO_OPTION);
                if (r == JOptionPane.NO_OPTION) {
                    return;
                }
            }
            Dimension size = getDiveProfile().getSize();
            BufferedImage img = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = img.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, size.width, size.height);
            getDiveProfile().paint(g);
            try {
                String extension = getExtension(f);
                if ("png".equalsIgnoreCase(extension)) {
                    writePng(img, f);
                } else {
                    writeJpeg(img, f);
                }
            } catch (IOException e) {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);
                JOptionPane.showMessageDialog(this, sw.toString() , Messages.getString("error.could_not_save_file"), JOptionPane.ERROR_MESSAGE);
            }
        }

    }
    
    private String getExtension(File f) {
        int idx = f.getName().lastIndexOf('.');
        String ext = f.getName().substring(idx+1);
        return ext;
    }
    
    private void writeJpeg(RenderedImage img, File f) throws IOException {
        int quality = mainWindow.getLogBook().getExportSettings().getImageQuality();
        quality = Math.max(0, Math.min(quality, 100));
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(f));
        JPEGImageWriter w = new JPEGImageWriter();
        ImageWriterParams params = new ImageWriterParams();
        params.setJPEGQuality(quality/100.0f, false);
        w.writeImage(img, out, params);
    }
    
    private void writePng(RenderedImage img, File f) throws IOException {
        int quality = mainWindow.getLogBook().getExportSettings().getImageQuality();
        quality = Math.max(0, Math.min(quality, 100));
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(f));
        PNGImageWriter w = new PNGImageWriter();
        w.writeImage(img, out);
    }
    
    /**
     * 
     */
    private void cancel() {
        if (parentWindow != null)
        {
            parentWindow.dispose();
        }
    }

    /**
     * 
     */ 
    public void close() {
        saveData();
        UndoableCommand cmd = new CommandSaveDive(mainWindow, oldDive,
                newDive);
        CommandManager.getInstance().execute(cmd);
        cancel();
    }
    
    private void chooseDiveSite() {
        DivesiteChooseWindow dcw = new DivesiteChooseWindow(parentWindow, this, masterdata);
        dcw.setVisible(true);
    }
    
    private void newDiveSite() {
        DivesiteDetailWindow ddw = new DivesiteDetailWindow(this.parentWindow, mainWindow, this, new DiveSite());
        ddw.setVisible(true);
    }
    
    public void setSite(DiveSite site) {
        if (site != null) {
            newDive.setDiveSiteId(site.getPrivateId());
            getFieldPlace().setText(site.getSpot());
            getFieldCity().setText(site.getCity());
            getFieldCountry().setText(site.getCountry());
            getDiveSiteDetailButton().setEnabled(true);
        } else {
            newDive.setDiveSiteId("");
            getFieldPlace().setText("");
            getFieldCity().setText("");
            getFieldCountry().setText("");
            getDiveSiteDetailButton().setEnabled(false);
        }
    }

}
