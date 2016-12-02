/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: ExportSettingsPicturesPanel.java
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

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.sf.jdivelog.gui.commands.UndoableCommand;
import net.sf.jdivelog.gui.resources.Messages;
import net.sf.jdivelog.model.ExportSettings;

public class ExportSettingsPicturesPanel extends AbstractSettingsPanel {
    
    private static final long serialVersionUID = 1L;

    private MainWindow mainWindow;
    private ExportSettings settings;
    
    private JCheckBox picturePageVisible;
    private JPanel imageSettingsGroup;
    private JTextField imageMaxWidthField;
    private JTextField imageMaxHeightField;
    private JPanel thumbnailSettingsGroup;
    private JTextField thumbnailMaxWidthField;
    private JTextField thumbnailMaxHeightField;
    private JCheckBox pictureName;
    private JCheckBox pictureDescription;
    private JCheckBox watermarkCheckbox;
    private JTextField watermarkField;

    private JPanel descriptionSettingsGroup;

    private JSlider imageQualitySlider;

    private JTextField imageQualityField;
    
    public ExportSettingsPicturesPanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        initialize();
    }

    public void load() {
        settings = mainWindow.getLogBook().getExportSettings();
        getPicturePageVisible().setSelected(settings.isPictureVisible());
        getImageMaxWidthField().setText(String.valueOf(settings.getPictureImageMaxWidth()));
        getImageMaxHeightField().setText(String.valueOf(settings.getPictureImageMaxHeight()));
        getThumbnailMaxWidthField().setText(String.valueOf(settings.getPictureThumbnailMaxWidth()));
        getThumbnailMaxHeightField().setText(String.valueOf(settings.getPictureThumbnailMaxHeight()));
        getPictureName().setSelected(settings.showPictureName());
        getPictureDescription().setSelected(settings.showPictureDescription());
        getWatermarkCheckbox().setSelected(settings.isWatermarkEnabled());
        getWatermarkField().setText(settings.getWatermarkText());
        getImageQualitySlider().setValue(settings.getImageQuality());
        getImageQualityField().setText(String.valueOf(settings.getImageQuality()));
    }

    public UndoableCommand getSaveCommand() {
        return new CommandSave();
    }
    
    //
    // private methods
    //
    
    private void initialize() {
        setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.anchor = GridBagConstraints.NORTHWEST;
        gc.insets = new Insets(0, 0, 10, 10);
        gc.gridy = 0;
        gc.gridx = 0;
        add(getPicturePageVisible(), gc);
        gc.gridx = 1;
        add(getImageSettingsGroup(), gc);
        gc.gridx = 2;
        add(getThumbnailSettingsGroup(), gc);
        gc.gridy = 1;
        gc.gridx = 1;
        gc.gridwidth = 2;
        add(getDescriptionSettingsGroup(), gc);
        gc.gridy = 2;
        gc.gridx = 0;
        gc.gridwidth = 1;
        add(getWatermarkCheckbox(), gc);
        gc.gridx = 1;
        gc.gridwidth = 2;
        add(getWatermarkField(), gc);
        Border border = BorderFactory.createTitledBorder(Messages.getString("picture_page")); //$NON-NLS-1$
        setBorder(border);    
    }
    
    private JCheckBox getPicturePageVisible() {
        if (picturePageVisible == null) {
            picturePageVisible = new JCheckBox(Messages.getString("visible")); //$NON-NLS-1$
        }
        return picturePageVisible;
    }

    private JPanel getImageSettingsGroup() {
        if (imageSettingsGroup == null) {
            imageSettingsGroup = new JPanel();
            imageSettingsGroup.setLayout(new GridBagLayout());
            GridBagConstraints gc = new GridBagConstraints();
            gc.anchor = GridBagConstraints.NORTHWEST;
            gc.insets = new Insets(0, 0, 10, 10);
            gc.gridy = 0;
            gc.gridx = 0;
            imageSettingsGroup.add(new JLabel(Messages.getString("max_width")), gc); //$NON-NLS-1$
            gc.gridx = 1;
            imageSettingsGroup.add(getImageMaxWidthField(), gc);
            gc.gridy = 1;
            gc.gridx = 0;
            imageSettingsGroup.add(new JLabel(Messages.getString("max_height")), gc); //$NON-NLS-1$
            gc.gridx = 1;
            imageSettingsGroup.add(getImageMaxHeightField(), gc);
            gc.gridy++;
            gc.gridx = 0;
            imageSettingsGroup.add(new JLabel(Messages.getString("image_quality")), gc); // $NON-NLS-1$
            gc.gridx = 1;
            imageSettingsGroup.add(getImageQualityField(), gc); // $NON-NLS-1$
            gc.gridy++;
            gc.gridx = 0;
            gc.gridwidth = 2;
            imageSettingsGroup.add(getImageQualitySlider(), gc);
            Border border = BorderFactory.createTitledBorder(Messages.getString("image")); //$NON-NLS-1$
            imageSettingsGroup.setBorder(border);
        }
        return imageSettingsGroup;
    }

    private JTextField getImageMaxWidthField() {
        if (imageMaxWidthField == null) {
            imageMaxWidthField = new JTextField();
            imageMaxWidthField.setPreferredSize(new Dimension(50, 20));
        }
        return imageMaxWidthField;
    }

    private JTextField getImageMaxHeightField() {
        if (imageMaxHeightField == null) {
            imageMaxHeightField = new JTextField();
            imageMaxHeightField.setPreferredSize(new Dimension(50, 20));
        }
        return imageMaxHeightField;
    }
    
    private JTextField getImageQualityField() {
        if (imageQualityField == null) {
            imageQualityField = new JTextField();
            imageQualityField.setColumns(4);
            imageQualityField.setEditable(false);
        }
        return imageQualityField;
    }
    private JSlider getImageQualitySlider() {
        if (imageQualitySlider == null) {
            imageQualitySlider = new JSlider(0, 100, 80);
            imageQualitySlider.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    getImageQualityField().setText(String.valueOf(imageQualitySlider.getValue()));
                }
            });
        }
        return imageQualitySlider;
    }

    private JPanel getThumbnailSettingsGroup() {
        if (thumbnailSettingsGroup == null) {
            thumbnailSettingsGroup = new JPanel();
            thumbnailSettingsGroup.setLayout(new GridBagLayout());
            GridBagConstraints gc = new GridBagConstraints();
            gc.anchor = GridBagConstraints.NORTHWEST;
            gc.insets = new Insets(0, 0, 10, 10);
            gc.gridy = 0;
            gc.gridx = 0;
            thumbnailSettingsGroup.add(new JLabel(Messages.getString("max_width")), gc); //$NON-NLS-1$
            gc.gridx = 1;
            thumbnailSettingsGroup.add(getThumbnailMaxWidthField(), gc);
            gc.gridy = 1;
            gc.gridx = 0;
            thumbnailSettingsGroup.add(new JLabel(Messages.getString("max_height")), gc); //$NON-NLS-1$
            gc.gridx = 1;
            thumbnailSettingsGroup.add(getThumbnailMaxHeightField(), gc);

            Border border = BorderFactory.createTitledBorder(Messages.getString("thumbnail")); //$NON-NLS-1$
            thumbnailSettingsGroup.setBorder(border);
        }
        return thumbnailSettingsGroup;
    }

    private JTextField getThumbnailMaxWidthField() {
        if (thumbnailMaxWidthField == null) {
            thumbnailMaxWidthField = new JTextField();
            thumbnailMaxWidthField.setPreferredSize(new Dimension(50, 20));
        }
        return thumbnailMaxWidthField;
    }

    private JTextField getThumbnailMaxHeightField() {
        if (thumbnailMaxHeightField == null) {
            thumbnailMaxHeightField = new JTextField();
            thumbnailMaxHeightField.setPreferredSize(new Dimension(50, 20));
        }
        return thumbnailMaxHeightField;
    }
    
    private JPanel getDescriptionSettingsGroup() {
        if (descriptionSettingsGroup == null) {
            descriptionSettingsGroup = new JPanel();
            descriptionSettingsGroup.setLayout(new GridBagLayout());
            GridBagConstraints gc = new GridBagConstraints();
            gc.anchor = GridBagConstraints.NORTHWEST;
            gc.insets = new Insets(0, 0, 10, 10);
            gc.gridy = 0;
            gc.gridx = 0;
            descriptionSettingsGroup.add(getPictureName(), gc); //$NON-NLS-1$
            gc.gridx = 1;
            descriptionSettingsGroup.add(getPictureDescription(), gc);

            Border border = BorderFactory.createTitledBorder(Messages.getString("details")); //$NON-NLS-1$
            descriptionSettingsGroup.setBorder(border);
        }
        return descriptionSettingsGroup;
    }
    
    private JCheckBox getPictureName() {
        if (pictureName == null) {
            pictureName = new JCheckBox(Messages.getString("name")); //$NON-NLS-1$
        }
        return pictureName;
    }
    
    private JCheckBox getPictureDescription() {
        if (pictureDescription == null) {
            pictureDescription = new JCheckBox(Messages.getString("description")); //$NON-NLS-1$
        }
        return pictureDescription;
    }
    
    private JCheckBox getWatermarkCheckbox() {
        if (watermarkCheckbox == null) {
            watermarkCheckbox = new JCheckBox(Messages.getString("create_watermark")); //$NON-NLS-1$
        }
        return watermarkCheckbox;
    }
    
    private JTextField getWatermarkField() {
        if (watermarkField == null) {
            watermarkField = new JTextField();
            watermarkField.setPreferredSize(new Dimension(200, 20));
        }
        return watermarkField;
    }
    
    //
    // inner classes
    //

    private class CommandSave implements UndoableCommand {
        
        private boolean oldPictureVisible;
        private int oldPictureImageMaxWidth;
        private int oldPictureImageMaxHeight;
        private int oldPictureThumbMaxWidth;
        private int oldPictureThumbMaxHeight;
        private boolean oldPictureName;
        private boolean oldPictureDescription;
        private boolean oldWatermarkEnabled;
        private String oldWatermarkText;
        private boolean newPictureVisible;
        private int newPictureImageMaxWidth;
        private int newPictureImageMaxHeight;
        private int newPictureThumbMaxWidth;
        private int newPictureThumbMaxHeight;
        private boolean newPictureName;
        private boolean newPictureDescription;
        private boolean newWatermarkEnabled;
        private String newWatermarkText;
        private int oldImageQuality;
        private int newImageQuality;

        public void undo() {
            ExportSettings settings = mainWindow.getLogBook().getExportSettings();
            settings.setPictureVisible(oldPictureVisible);
            settings.setPictureImageMaxWidth(oldPictureImageMaxWidth);
            settings.setPictureImageMaxHeight(oldPictureImageMaxHeight);
            settings.setPictureThumbnailMaxWidth(oldPictureThumbMaxWidth);
            settings.setPictureThumbnailMaxHeight(oldPictureThumbMaxHeight);
            settings.setPictureName(oldPictureName);
            settings.setPictureDescription(oldPictureDescription);
            settings.setWatermarkEnabled(oldWatermarkEnabled);
            settings.setWatermarkText(oldWatermarkText);
            settings.setImageQuality(oldImageQuality);
        }

        public void redo() {
            ExportSettings settings = mainWindow.getLogBook().getExportSettings();
            settings.setPictureVisible(newPictureVisible);
            settings.setPictureImageMaxWidth(newPictureImageMaxWidth);
            settings.setPictureImageMaxHeight(newPictureImageMaxHeight);
            settings.setPictureThumbnailMaxWidth(newPictureThumbMaxWidth);
            settings.setPictureThumbnailMaxHeight(newPictureThumbMaxHeight);
            settings.setPictureName(newPictureName);
            settings.setPictureDescription(newPictureDescription);
            settings.setWatermarkEnabled(newWatermarkEnabled);
            settings.setWatermarkText(newWatermarkText);
            settings.setImageQuality(newImageQuality);
        }

        public void execute() {
            oldPictureVisible = settings.isPictureVisible();
            oldPictureImageMaxWidth = settings.getPictureImageMaxWidth();
            oldPictureImageMaxHeight = settings.getPictureImageMaxHeight();
            oldPictureThumbMaxWidth = settings.getPictureThumbnailMaxWidth();
            oldPictureThumbMaxHeight = settings.getPictureThumbnailMaxHeight();
            oldImageQuality = settings.getImageQuality();
            oldPictureName = settings.showPictureName();
            oldPictureDescription = settings.showPictureDescription();
            oldWatermarkEnabled = settings.isWatermarkEnabled();
            oldWatermarkText = settings.getWatermarkText();
            newPictureVisible = getPicturePageVisible().isSelected();
            newPictureImageMaxWidth = new Integer(getImageMaxWidthField().getText());
            newPictureImageMaxHeight = new Integer(getImageMaxHeightField().getText());
            newPictureThumbMaxWidth = new Integer(getThumbnailMaxWidthField().getText());
            newPictureThumbMaxHeight = new Integer(getThumbnailMaxHeightField().getText());
            newPictureName = getPictureName().isSelected();
            newPictureDescription = getPictureDescription().isSelected();
            newWatermarkEnabled = getWatermarkCheckbox().isSelected();
            newWatermarkText = getWatermarkField().getText();
            newImageQuality = getImageQualitySlider().getValue();
            ExportSettings settings = mainWindow.getLogBook().getExportSettings();
            settings.setPictureVisible(newPictureVisible);
            settings.setPictureImageMaxWidth(newPictureImageMaxWidth);
            settings.setPictureImageMaxHeight(newPictureImageMaxHeight);
            settings.setPictureThumbnailMaxWidth(newPictureThumbMaxWidth);
            settings.setPictureThumbnailMaxHeight(newPictureThumbMaxHeight);
            settings.setPictureName(newPictureName);
            settings.setPictureDescription(newPictureDescription);
            settings.setWatermarkEnabled(newWatermarkEnabled);
            settings.setWatermarkText(newWatermarkText);
            settings.setImageQuality(newImageQuality);
        }
        
    }
}
