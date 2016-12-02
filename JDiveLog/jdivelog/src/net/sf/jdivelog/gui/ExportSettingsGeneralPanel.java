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


import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import net.sf.jdivelog.gui.commands.UndoableCommand;
import net.sf.jdivelog.gui.resources.Messages;
import net.sf.jdivelog.model.ExportSettings;

public class ExportSettingsGeneralPanel extends AbstractSettingsPanel implements ActionListener {
    
    private static final long serialVersionUID = 1L;

    private MainWindow mainWindow;
    private ExportSettings settings;

    private JTextField exportDirectoryField;
    private JButton openDirectoryButton;
    private JTextField footerLinkName;
    private JTextField footerLink;
    private JCheckBox fullExportCheckbox;
    private JCheckBox keepImagesCheckbox;
    private JPanel scpPanel;
    private JCheckBox scpEnabledCheckbox;
    private JTextField scpHostField;
    private JTextField scpDirectoryField;
    private JTextField scpUserField;
    
    public ExportSettingsGeneralPanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        initialize();
        load();
    }

    public void load() {
        settings = mainWindow.getLogBook().getExportSettings();
        exportDirectoryField.setText(settings.getExportDirectory());
        getFooterLinkName().setText(settings.getFooterLinkName());
        getFooterLink().setText(settings.getFooterLink());
        getFullExportCheckbox().setSelected(settings.isFullExport());
        updateKeepImagesState();
        getKeepImagesCheckbox().setSelected(settings.isKeepImages());
        getScpEnabledCheckbox().setSelected(settings.isScpEnabled());
        getScpHostField().setText(settings.getScpHost());
        getScpDirectoryField().setText(settings.getScpDirectory());
        getScpUserField().setText(settings.getScpUser());
    }

    public UndoableCommand getSaveCommand() {
        return new CommandSave();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == getOpenDirectoryButton()) {
            // show open dialog
            JFileChooser fc = new JFileChooser();
            File f = new File(getExportDirectoryField().getText());
            if (f.exists() && f.isDirectory()) {
                fc.setCurrentDirectory(f);
            }
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int ret = fc.showOpenDialog(this);
            if (ret == JFileChooser.APPROVE_OPTION) {
                getExportDirectoryField().setText(fc.getSelectedFile().getPath());
            }
        }
        if (e.getSource() == fullExportCheckbox) {
            updateKeepImagesState();
        }
    }
    
    //
    // private methods
    //
    
    private void initialize() {
        setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(0, 0, 10, 10);
        gc.anchor = GridBagConstraints.NORTHWEST;
        gc.gridx = 0;
        gc.gridy = 0;
        add(new JLabel(Messages.getString("directory")), gc); //$NON-NLS-1$
        gc.gridx = 1;
        gc.gridwidth = 1;
        add(getExportDirectoryField(), gc);
        gc.gridwidth = 1;
        gc.gridx = 2;
        add(getOpenDirectoryButton(), gc);
        gc.gridy = 1;
        gc.gridx = 0;
        add(new JLabel(Messages.getString("footer_link_name")), gc);
        gc.gridx = 1;
        add(getFooterLinkName(), gc);
        gc.gridx = 2;
        add(new JLabel(Messages.getString("footer_link")), gc);
        gc.gridx = 3;
        add(getFooterLink(), gc);
        gc.gridy = 2;
        gc.gridx = 0;
        add(getFullExportCheckbox() ,gc);
        gc.gridx = 1;
        add(getKeepImagesCheckbox() ,gc);
        gc.gridy = 3;
        gc.gridx = 0;
        gc.gridwidth = 4;
        add(getScpPanel(), gc);
        gc.gridwidth = 1;
        gc.gridy = 4;
        gc.gridx = 0;
        JPanel spacer = new JPanel();
        spacer.setMinimumSize(new Dimension(200, 300));
        add(spacer, gc);
        Border border = BorderFactory.createTitledBorder(Messages.getString("export_directory")); //$NON-NLS-1$
        setBorder(border);
    }
    
    private JTextField getExportDirectoryField() {
        if (exportDirectoryField == null) {
            exportDirectoryField = new JTextField();
            exportDirectoryField.setPreferredSize(new Dimension(200, 20));
        }
        return exportDirectoryField;
    }

    private JButton getOpenDirectoryButton() {
        if (openDirectoryButton == null) {
            openDirectoryButton = new JButton(new ImageIcon(getClass().getResource("/net/sf/jdivelog/gui/resources/icons/open.gif"))); //$NON-NLS-1$
            openDirectoryButton.addActionListener(this);
        }
        return openDirectoryButton;
    }
    
    private JTextField getFooterLinkName() {
        if (footerLinkName == null) {
            footerLinkName = new JTextField();
            footerLinkName.setPreferredSize(new Dimension(200, 20));
        }
        return footerLinkName;
    }
    
    private JTextField getFooterLink() {
        if (footerLink == null) {
            footerLink = new JTextField();
            footerLink.setPreferredSize(new Dimension(200, 20));
        }
        return footerLink;
    }
    
    private JCheckBox getFullExportCheckbox() {
        if (fullExportCheckbox == null) {
            fullExportCheckbox = new JCheckBox(Messages.getString("export.full_export")); //$NON-NLS-1
            fullExportCheckbox.addActionListener(this);
        }
        return fullExportCheckbox;
    }
    
    private JCheckBox getKeepImagesCheckbox() {
        if (keepImagesCheckbox == null) {
            keepImagesCheckbox = new JCheckBox(Messages.getString("export.keep_images")); //$NON-NLS-1
        }
        return keepImagesCheckbox;
    }
    
    private JPanel getScpPanel() {
        if (scpPanel == null) {
            scpPanel = new JPanel();
            scpPanel.setLayout(new GridBagLayout());
            GridBagConstraints gc = new GridBagConstraints();
            gc.gridy = 0;
            gc.gridx = 0;
            scpPanel.add(getScpEnabledCheckbox(), gc);
            gc.gridy = 1;
            gc.gridx = 0;
            scpPanel.add(new JLabel(Messages.getString("export.scp.host")), gc); //$NON-NLS-1$
            gc.gridx = 1;
            scpPanel.add(getScpHostField(), gc);
            gc.gridx = 2;
            scpPanel.add(new JLabel(Messages.getString("export.scp.directory")), gc); //$NON-NLS-1$
            gc.gridx = 3;
            scpPanel.add(getScpDirectoryField(), gc);
            gc.gridy = 2;
            gc.gridx = 0;
            scpPanel.add(new JLabel(Messages.getString("export.scp.user")), gc); //$NON-NLS-1$
            gc.gridx = 1;
            scpPanel.add(getScpUserField(), gc);
            Border border = BorderFactory.createTitledBorder(Messages.getString("export.scp")); //$NON-NLS-1$
            scpPanel.setBorder(border);
        }
        return scpPanel;
    }
    
    private JCheckBox getScpEnabledCheckbox() {
        if (scpEnabledCheckbox == null) {
            scpEnabledCheckbox = new JCheckBox(Messages.getString("export.scp.enabled")); //$NON-NLS-1$
        }
        return scpEnabledCheckbox;
    }
    
    private JTextField getScpHostField() {
        if (scpHostField == null) {
            scpHostField = new JTextField();
            scpHostField.setPreferredSize(new Dimension(200, 20));
        }
        return scpHostField;
    }
    
    private JTextField getScpDirectoryField() {
        if (scpDirectoryField == null) {
            scpDirectoryField = new JTextField();
            scpDirectoryField.setPreferredSize(new Dimension(200, 20));
        }
        return scpDirectoryField;
    }
    
    private JTextField getScpUserField() {
        if (scpUserField == null) {
            scpUserField = new JTextField();
            scpUserField.setPreferredSize(new Dimension(200, 20));
        }
        return scpUserField;
    }

    private void updateKeepImagesState() {
        if (getFullExportCheckbox().isSelected()) {
            getKeepImagesCheckbox().setEnabled(true);
        } else {
            getKeepImagesCheckbox().setEnabled(false);
        }
    }
    
    //
    // inner classes
    //
    
    private class CommandSave implements UndoableCommand {
        
        private String oldExportDirectory;
        private String newExportDirectory;
        private String oldFooterLinkName;
        private String newFooterLinkName;
        private String oldFooterLink;
        private String newFooterLink;
        private boolean oldFullExport;
        private boolean newFullExport;
        private boolean oldKeepImages;
        private boolean newKeepImages;
        private boolean oldScpEnabled;
        private boolean newScpEnabled;
        private String oldScpHost;
        private String newScpHost;
        private String oldScpDirectory;
        private String newScpDirectory;
        private String oldScpUser;
        private String newScpUser;

        public void undo() {
            ExportSettings settings = mainWindow.getLogBook().getExportSettings();
            settings.setExportDirectory(oldExportDirectory);
            settings.setFooterLinkName(oldFooterLinkName);
            settings.setFooterLink(oldFooterLink);
            settings.setFullExport(oldFullExport);
            settings.setKeepImages(oldKeepImages);
            settings.setScpEnabled(oldScpEnabled);
            settings.setScpHost(oldScpHost);
            settings.setScpDirectory(oldScpDirectory);
            settings.setScpUser(oldScpUser);
        }

        public void redo() {
            ExportSettings settings = mainWindow.getLogBook().getExportSettings();
            settings.setExportDirectory(newExportDirectory);
            settings.setFooterLinkName(newFooterLinkName);
            settings.setFooterLink(newFooterLink);
            settings.setFullExport(newFullExport);
            settings.setKeepImages(newKeepImages);
            settings.setScpEnabled(newScpEnabled);
            settings.setScpHost(newScpHost);
            settings.setScpDirectory(newScpDirectory);
            settings.setScpUser(newScpUser);
        }

        public void execute() {
            oldExportDirectory = settings.getExportDirectory();
            oldFooterLinkName = settings.getFooterLinkName();
            oldFooterLink = settings.getFooterLink();
            oldFullExport = settings.isFullExport();
            oldKeepImages = settings.isKeepImages();
            oldScpEnabled = settings.isScpEnabled();
            oldScpHost = settings.getScpHost();
            oldScpDirectory = settings.getScpDirectory();
            oldScpUser = settings.getScpUser();
            newExportDirectory = getExportDirectoryField().getText();
            newFooterLinkName = getFooterLinkName().getText();
            newFooterLink = getFooterLink().getText();
            newFullExport = getFullExportCheckbox().isSelected();
            newKeepImages = getKeepImagesCheckbox().isSelected();
            newScpEnabled = getScpEnabledCheckbox().isSelected();
            newScpHost = getScpHostField().getText();
            newScpDirectory = getScpDirectoryField().getText();
            newScpUser = getScpUserField().getText();
            ExportSettings settings = mainWindow.getLogBook().getExportSettings();
            settings.setExportDirectory(newExportDirectory);
            settings.setFooterLinkName(newFooterLinkName);
            settings.setFooterLink(newFooterLink);
            settings.setFullExport(newFullExport);
            settings.setKeepImages(newKeepImages);
            settings.setScpEnabled(newScpEnabled);
            settings.setScpHost(newScpHost);
            settings.setScpDirectory(newScpDirectory);
            settings.setScpUser(newScpUser);
        }
        
    }

}
