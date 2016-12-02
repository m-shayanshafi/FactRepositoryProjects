/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: ExportSettingsGeneralPanel.java
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
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.Border;
import javax.swing.plaf.metal.MetalLookAndFeel;

import net.sf.jdivelog.JDiveLog;
import net.sf.jdivelog.gui.commands.UndoableCommand;
import net.sf.jdivelog.gui.resources.Messages;
import net.sf.jdivelog.util.UnitConverter;

public class SettingsGeneralPanel extends AbstractSettingsPanel implements ActionListener {
    
    private static final Logger LOGGER = Logger.getLogger(SettingsGeneralPanel.class.getName());
    
    private static final String[] LOCALES = new String[] {"cs_CZ","de_CH", "de_DE", "en", "fr_FR", "fr_CH", "fi", "nl", "es", "pt" };
    private static final String[] LOCALE_NAMES = new String[] {"Čeština (Czech)","Deutsch (Schweiz)", "Deutsch (Deutschland)", "English", "Français (France)", "Français (Suisse)", "Suomalainen", "Nederlaands", "Espagnol", "Português" };
    private static final String[] SYSTEMS = new String[] { "display", "display-imperial" };
    private static final String[] SYSTEM_NAMES = new String[] { Messages.getString("system.metric"), Messages.getString("system.imperial") };
    private static final String[] DATEFORMATS = new String[] {"dd.MM.yyyy", "dd/MM/yyyy", "MM/dd/yyyy", "yyyy-MM-dd" };
    private static final String[] DATETIMEFORMATS = new String[] {"dd.MM.yyyy - HH:mm", "dd/MM/yyyy - HH:mm", "MM/dd/yyyy - HH:mm", "yyyy-MM-dd - HH:mm"};
    
    private static final long serialVersionUID = 1L;

    private JComboBox localeField;
    private JComboBox systemField;
    private JComboBox dateFormatField;
    private JComboBox dateTimeFormatField;
    
    private JCheckBox systemLookCheckBox;
    private JComboBox lookAndFeels;
    private JCheckBox previewCheckBox;
    private JCheckBox startSortCheckBox;
    private JTextField documents_pathField;
    private JButton documents_pathButton;
    
    public SettingsGeneralPanel() {
        initialize();
        load();
    }

    public void load() {
        Properties p = loadProperties();
        String locale = p.getProperty(JDiveLog.PROPERTY_LOCALE);
        String system = p.getProperty(JDiveLog.PROPERTY_SYSTEM);
        String dateformat = p.getProperty(JDiveLog.PROPERTY_DATEFORMAT);
        String datetimeformat = p.getProperty(JDiveLog.PROPERTY_DATETIMEFORMAT);
        String systemLook = p.getProperty(JDiveLog.PROPERTY_SYSTEM_LOOK);
        String lookAndFeel = p.getProperty(JDiveLog.PROPERTY_LOOK_AND_FEEL);
        String preview = System.getProperty(JDiveLog.PROPERTY_PREVIEW);
        String startsort = System.getProperty(JDiveLog.PROPERTY_STARTSORT);
        String documents_path = System.getProperty(JDiveLog.PROPERTY_DOCUMENTS_PATH);
        if (locale == null || locale.equals("")) {
            getLocaleField().setSelectedIndex(getLocaleIndex("en"));
        } else {
            getLocaleField().setSelectedIndex(getLocaleIndex(locale));
        }
        getSystemField().setSelectedIndex(getSystemIndex(system));
        getDateFormatField().setSelectedIndex(getDateFormatIndex(dateformat));
        getDateTimeFormatField().setSelectedIndex(getDateTimeFormatIndex(datetimeformat));
        getSystemLookCheckBox().setSelected(Boolean.parseBoolean(systemLook));
        getLookAndFeels().setSelectedIndex(getLookAndFeelIndex(lookAndFeel));
        getPreviewCheckBox().setSelected(Boolean.parseBoolean(preview));
        getStartSortCheckBox().setSelected(Boolean.parseBoolean(startsort));
        getDocuments_pathField().setText(documents_path);
    }
    
    public UndoableCommand getSaveCommand() {
        return new CommandSave();
    }

    //
    // private methods
    //

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == documents_pathButton) {
            FileChooser fc = new FileChooser();
            fc.setFileSelectionMode(FileChooser.DIRECTORIES_ONLY);
            fc.setMultiSelectionEnabled(false);
            int status = fc.showOpenDialog(null);
            if (status == FileChooser.APPROVE_OPTION) {
                  getDocuments_pathField().setText(fc.getSelectedFile().getPath());
            }
        }
    }
    
    private void initialize() {
        setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(0, 0, 10, 10);
        gc.anchor = GridBagConstraints.NORTHWEST;
        gc.gridx = 0;
        gc.gridy = 0;
        add(new JLabel(Messages.getString("locale")), gc); //$NON-NLS-1$
        gc.gridx = 1;
        add(getLocaleField(), gc);
        gc.gridx = 0;
        gc.gridy++;
        add(new JLabel(Messages.getString("system")), gc); //$NON-NLS-1$
        gc.gridx = 1;
        add(getSystemField(), gc);
        gc.gridy++;
        gc.gridx = 0;
        add(new JLabel(Messages.getString("dateformat")), gc); //$NON-NLS-1$
        gc.gridx = 1;
        add(getDateFormatField(), gc);
        gc.gridy++;
        gc.gridx = 0;
        add(new JLabel(Messages.getString("datetime_format")), gc); //$NON-NLS-1$
        gc.gridx = 1;
        add(getDateTimeFormatField(), gc);     
        gc.gridy++;
        gc.gridx = 0;
        add(new JLabel(Messages.getString("system_look")), gc); //$NON-NLS-1$
        gc.gridx = 1;
        add(getSystemLookCheckBox(), gc);
        gc.gridy++;
        gc.gridx = 0;
        add(new JLabel(Messages.getString("look_and_feel")), gc); // $NON-NLS-1$
        gc.gridx = 1;
        add(getLookAndFeels(), gc);
        gc.gridy++;
        gc.gridx = 0;
        add(new JLabel(Messages.getString("preview")), gc); //$NON-NLS-1$
        gc.gridx = 1;
        add(getPreviewCheckBox(), gc);
        gc.gridy++;
        gc.gridx = 0;
        add(new JLabel(Messages.getString("startsort")), gc); //$NON-NLS-1$
        gc.gridx = 1;
        add(getStartSortCheckBox(), gc);
        gc.gridy++;
        gc.gridx = 0;
        /*add(new JLabel(Messages.getString("documents_path")), gc); //$NON-NLS-1$
        gc.gridx = 1;
        add(getDocuments_pathField(), gc);
        gc.gridx = 2;
        add(getDocuments_pathButton(), gc);
        gc.gridy++;
        gc.gridx = 0;*/
        gc.gridwidth = 2;
        add(new JLabel(Messages.getString("information.settings_effective_after_restart")), gc); //$NON-NLS-1$
        Border border = BorderFactory.createTitledBorder(Messages.getString("configuration.general")); //$NON-NLS-1$
        setBorder(border);
    }
    
    private JComboBox getLocaleField() {
        if (localeField == null) {
            localeField = new JComboBox(LOCALE_NAMES);
        }
        return localeField;
    }
    
    private JComboBox getSystemField() {
        if (systemField == null) {
            systemField = new JComboBox(SYSTEM_NAMES);
            
        }
        return systemField;
    }
    
    private JComboBox getDateFormatField() {
        if (dateFormatField == null) {
            dateFormatField = new JComboBox(DATEFORMATS);
        }
        return dateFormatField;
    }
    
    // Commented for release version
    private JCheckBox getSystemLookCheckBox() {
        if (systemLookCheckBox == null) {
            systemLookCheckBox = new JCheckBox();
            systemLookCheckBox.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (systemLookCheckBox.isSelected()) {
                        getLookAndFeels().setEnabled(false);
                    } else {
                        getLookAndFeels().setEnabled(true);
                    }
                }
            });
            systemLookCheckBox.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    if (systemLookCheckBox.isSelected()) {
                        getLookAndFeels().setEnabled(false);
                    } else {
                        getLookAndFeels().setEnabled(true);
                    }
                }
                
            });
        }
        return systemLookCheckBox;
    }       
        
   private JComboBox getDateTimeFormatField() {
        if (dateTimeFormatField == null) {
            dateTimeFormatField = new JComboBox(DATETIMEFORMATS);
        }
        return dateTimeFormatField;
    }
   
   private JComboBox getLookAndFeels() {
       if (lookAndFeels == null) {
           LookAndFeelInfo[] lafs = UIManager.getInstalledLookAndFeels();
           LafInfo[] lafInfos = new LafInfo[lafs.length];
           for (int i=0; i<lafs.length; i++) {
               lafInfos[i] = new LafInfo(lafs[i]);
           }
           lookAndFeels = new JComboBox(lafInfos);           
       }
       return lookAndFeels;
   }
   
   private JCheckBox getPreviewCheckBox() {
       if (previewCheckBox == null) {
           previewCheckBox = new JCheckBox();
       }
       return previewCheckBox;
   }
    
   private JCheckBox getStartSortCheckBox() {
       if (startSortCheckBox == null) {
           startSortCheckBox = new JCheckBox();
       }
       return startSortCheckBox;
   }

   private JTextField getDocuments_pathField() {
       if (documents_pathField == null) {
           documents_pathField = new JTextField(17);
       }
       return documents_pathField;
   }

   private JButton getDocuments_pathButton() {
       if (documents_pathButton == null) {
           documents_pathButton = new JButton();
           documents_pathButton.addActionListener(this);
           documents_pathButton.setIcon(new ImageIcon(getClass().getResource("/net/sf/jdivelog/gui/resources/icons/documents_small.png"))); //$NON-NLS-1$
           documents_pathButton.setToolTipText(Messages.getString("search")); //$NON-NLS-1$
           documents_pathButton.setName(Messages.getString("search")); //$NON-NLS-1$
       }
       return documents_pathButton;
   }

   private Properties loadProperties() {
        Properties p = new Properties();
        if (JDiveLog.CONFIG_FILE.exists() && JDiveLog.CONFIG_FILE.canRead()) {
            try {
                p.load(new FileInputStream(JDiveLog.CONFIG_FILE));
            } catch (FileNotFoundException e) {
                LOGGER.log(Level.SEVERE, "failed to load properties", e);
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "failed to load properties", e);
            }
        } else {
            p.setProperty(JDiveLog.PROPERTY_LOCALE, Messages.getLocale().toString());
            p.setProperty(JDiveLog.PROPERTY_SYSTEM, String.valueOf(UnitConverter.getDisplaySystem()));
            p.setProperty(JDiveLog.PROPERTY_DATEFORMAT, "dd.MM.yyyy");
            p.setProperty(JDiveLog.PROPERTY_SYSTEM_LOOK, "true");
            p.setProperty(JDiveLog.PROPERTY_LOOK_AND_FEEL, MetalLookAndFeel.class.getName());
            p.setProperty(JDiveLog.PROPERTY_PREVIEW, "false");
            p.setProperty(JDiveLog.PROPERTY_STARTSORT, "true");
            p.setProperty(JDiveLog.PROPERTY_DOCUMENTS_PATH, "");
        }
        return p;
    }

    private int getLocaleIndex(String locale) {
        for (int i=0; i<LOCALES.length; i++) {
            if (LOCALES[i].equals(locale)) {
                return i;
            }
        }
        return 0;
    }

    private int getSystemIndex(String system) {
        for (int i=0; i<SYSTEMS.length; i++) {
            if (SYSTEMS[i].equals(system)) {
                return i;
            }
        }
        return 0;
    }
    
    private int getDateFormatIndex(String dateformat) {
        for (int i=0; i<DATEFORMATS.length; i++) {
            if (DATEFORMATS[i].equals(dateformat)) {
                return i;
            }
        }
        return 0;
    }
    
    private int getDateTimeFormatIndex(String datetimeformat) {
        for (int i=0; i<DATETIMEFORMATS.length; i++) {
            if (DATETIMEFORMATS[i].equals(datetimeformat)) {
                return i;
            }
        }
        return 0;
    }
    
    private int getLookAndFeelIndex(String lookAndFeel) {
        if (lookAndFeel == null) {
            return 0;
        }
        for (int i=0; i<getLookAndFeels().getModel().getSize(); i++) {
            LafInfo lafInfo = (LafInfo) getLookAndFeels().getModel().getElementAt(i);
            if (lookAndFeel.equals(lafInfo.getClassName())) {
                return i;
            }
        }
        return 0;
    }
    
    //
    // inner classes
    //
    
    private class CommandSave implements UndoableCommand {
        
        private String newLocale;
        private String oldLocale;
        private String newSystem;
        private String oldSystem;
        private String newDateFormat;
        private String oldDateFormat;
        private String newDateTimeFormat;
        private String oldDateTimeFormat;
        private String newSystemLook;
        private String oldSystemLook;
        private String newLookAndFeel;
        private String oldLookAndFeel;
        private String newPreview;
        private String oldPreview;
        private String newStartsort;
        private String oldStartsort;
        private String newDocuments_path;
        private String oldDocuments_path;
        

        public void undo() {
            Properties p = loadProperties();
            p.setProperty(JDiveLog.PROPERTY_LOCALE, oldLocale);
            p.setProperty(JDiveLog.PROPERTY_SYSTEM, oldSystem);
            p.setProperty(JDiveLog.PROPERTY_DATEFORMAT, oldDateFormat);
            p.setProperty(JDiveLog.PROPERTY_DATETIMEFORMAT, oldDateTimeFormat);
            p.setProperty(JDiveLog.PROPERTY_SYSTEM_LOOK, oldSystemLook);
            p.setProperty(JDiveLog.PROPERTY_LOOK_AND_FEEL, oldLookAndFeel);
            p.setProperty(JDiveLog.PROPERTY_PREVIEW, oldPreview);
            p.setProperty(JDiveLog.PROPERTY_STARTSORT, oldStartsort);
            p.setProperty(JDiveLog.PROPERTY_DOCUMENTS_PATH, oldDocuments_path);
            try {
                p.store(new FileOutputStream(JDiveLog.CONFIG_FILE), "JDiveLog properties");
            } catch (FileNotFoundException e) {
                LOGGER.log(Level.SEVERE, "failed to store properties", e);
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "failed to store properties", e);
            }
        }

        public void redo() {
            Properties p = loadProperties();
            p.setProperty(JDiveLog.PROPERTY_LOCALE, newLocale);
            p.setProperty(JDiveLog.PROPERTY_SYSTEM, newSystem);
            p.setProperty(JDiveLog.PROPERTY_DATEFORMAT, newDateFormat);
            p.setProperty(JDiveLog.PROPERTY_DATETIMEFORMAT, newDateTimeFormat);
            p.setProperty(JDiveLog.PROPERTY_SYSTEM_LOOK, newSystemLook);
            p.setProperty(JDiveLog.PROPERTY_LOOK_AND_FEEL, newLookAndFeel);
            p.setProperty(JDiveLog.PROPERTY_PREVIEW, newPreview);
            p.setProperty(JDiveLog.PROPERTY_STARTSORT, newStartsort);
            p.setProperty(JDiveLog.PROPERTY_DOCUMENTS_PATH, newDocuments_path);
            try {
                p.store(new FileOutputStream(JDiveLog.CONFIG_FILE), "JDiveLog properties");
            } catch (FileNotFoundException e) {
                LOGGER.log(Level.SEVERE, "failed to store properties", e);
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "failed to store properties", e);
            }
        }

        public void execute() {
            Properties p = loadProperties();
            oldLocale = p.getProperty(JDiveLog.PROPERTY_LOCALE);
            oldSystem = p.getProperty(JDiveLog.PROPERTY_SYSTEM);
            oldDateFormat = p.getProperty(JDiveLog.PROPERTY_DATEFORMAT);
            oldDateTimeFormat = p.getProperty(JDiveLog.PROPERTY_DATETIMEFORMAT);
            oldSystemLook = p.getProperty(JDiveLog.PROPERTY_SYSTEM_LOOK);
            oldLookAndFeel = p.getProperty(JDiveLog.PROPERTY_LOOK_AND_FEEL);
            oldPreview = p.getProperty(JDiveLog.PROPERTY_PREVIEW);
            oldStartsort = p.getProperty(JDiveLog.PROPERTY_STARTSORT);
            oldDocuments_path = p.getProperty(JDiveLog.PROPERTY_DOCUMENTS_PATH);
            int locale = getLocaleField().getSelectedIndex();
            if (locale >= 0 && locale < LOCALES.length) {
                newLocale = LOCALES[locale];
            } else {
                newLocale = LOCALES[0];
            }
            int system = getSystemField().getSelectedIndex();
            if (system >= 0 && system < SYSTEMS.length) {
                newSystem = SYSTEMS[system];
            } else {
                newSystem = SYSTEMS[0];
            }
            int dateformat = getDateFormatField().getSelectedIndex();
            if (dateformat >= 0 && dateformat < DATEFORMATS.length) {
                newDateFormat = DATEFORMATS[dateformat];
            } else {
                newDateFormat = DATEFORMATS[0];
            }
            int datetimeformat = getDateFormatField().getSelectedIndex();
            if (datetimeformat >= 0 && dateformat < DATETIMEFORMATS.length) {
                newDateTimeFormat = DATETIMEFORMATS[dateformat];
            } else {
                newDateTimeFormat = DATETIMEFORMATS[0];
            }
            
            Boolean systemLook = getSystemLookCheckBox().isSelected();
            newSystemLook = systemLook.toString();
            
            LafInfo info = (LafInfo) getLookAndFeels().getSelectedItem();
            newLookAndFeel = info == null ? MetalLookAndFeel.class.getName() : info.getClassName();
            
            newPreview = String.valueOf(getPreviewCheckBox().isSelected());
            
            newStartsort = String.valueOf(getStartSortCheckBox().isSelected());
            newDocuments_path = getDocuments_pathField().getText();

            p = new Properties();
            p.setProperty(JDiveLog.PROPERTY_LOCALE, newLocale);
            p.setProperty(JDiveLog.PROPERTY_SYSTEM, newSystem);
            p.setProperty(JDiveLog.PROPERTY_DATEFORMAT, newDateFormat);
            p.setProperty(JDiveLog.PROPERTY_DATETIMEFORMAT, newDateTimeFormat);
            p.setProperty(JDiveLog.PROPERTY_SYSTEM_LOOK, newSystemLook);
            p.setProperty(JDiveLog.PROPERTY_LOOK_AND_FEEL, newLookAndFeel);
            p.setProperty(JDiveLog.PROPERTY_PREVIEW, newPreview);
            p.setProperty(JDiveLog.PROPERTY_STARTSORT, newStartsort);
            p.setProperty(JDiveLog.PROPERTY_DOCUMENTS_PATH, newDocuments_path);
            try {
                p.store(new FileOutputStream(JDiveLog.CONFIG_FILE), "JDiveLog properties");
            } catch (FileNotFoundException e) {
                LOGGER.log(Level.SEVERE, "failed to store properties", e);
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "failed to store properties", e);
            }
        }
        
    }
    
    private static class LafInfo {
        private final LookAndFeelInfo info;
        public LafInfo(LookAndFeelInfo info) {
            this.info = info;
        }
        @Override
        public String toString() {
            return info.getName();
        }
        public String getClassName() {
            return info.getClassName();
        }
    }

}
