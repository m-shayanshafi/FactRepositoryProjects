/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: ExportSettingsIndexPanel.java
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
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import net.sf.jdivelog.gui.commands.UndoableCommand;
import net.sf.jdivelog.gui.resources.Messages;
import net.sf.jdivelog.model.ExportSettings;

public class ExportSettingsIndexPanel extends AbstractSettingsPanel {
    
    private static final long serialVersionUID = 1L;

    private MainWindow mainWindow;
    private ExportSettings settings;
    private JTextField indexPageTitle = null;
    private JCheckBox groupByYear = null;
    private JComboBox indexImageSelection = null;
    private JPanel indexShowGroup = null;
    private JCheckBox indexShowCity = null;
    private JCheckBox indexShowCountry = null;
    private JCheckBox indexShowDate = null;
    private JCheckBox indexShowDepth = null;
    private JCheckBox indexShowDuration = null;
    private JCheckBox indexShowLocation = null;
    private JCheckBox indexShowTime = null;
    private JCheckBox indexShowPictureCount = null;
    
    public ExportSettingsIndexPanel(MainWindow mainWindow) {
       this.mainWindow = mainWindow;
       initialize();
    }
    
    public void load() {
        settings = mainWindow.getLogBook().getExportSettings().deepClone();
        getIndexImageSelection().setSelectedIndex(settings.getIndexImages());
        getIndexPageTitle().setText(settings.getIndexTitle());
        getGroupByYear().setSelected(settings.groupByYear());
        getIndexShowDate().setSelected(settings.showIndexDate());
        getIndexShowTime().setSelected(settings.showIndexTime());
        getIndexShowLocation().setSelected(settings.showIndexLocation());
        getIndexShowCity().setSelected(settings.showIndexCity());
        getIndexShowCountry().setSelected(settings.showIndexCountry());
        getIndexShowDepth().setSelected(settings.showIndexDepth());
        getIndexShowDuration().setSelected(settings.showIndexDuration());
        getIndexShowPictureCount().setSelected(settings.showIndexPictureCount());
    }
    
    public UndoableCommand getSaveCommand() {
        return new CommandSave();
    }
    
    private void initialize() {
        GridBagConstraints gc = new GridBagConstraints();
        setLayout(new GridBagLayout());
        gc.insets = new Insets(10, 10, 10, 10);
        gc.gridy = 0;
        gc.gridx = 0;
        gc.anchor = GridBagConstraints.NORTHEAST;
        add(new JLabel(Messages.getString("title")), gc); //$NON-NLS-1$
        gc.gridx = 1;
        gc.gridwidth = 2;
        gc.anchor = GridBagConstraints.NORTHWEST;
        add(getIndexPageTitle(), gc);
        gc.gridwidth = 1;
        gc.anchor = GridBagConstraints.NORTHEAST;
        gc.gridy = 3;
        gc.gridx = 1;
        add(getGroupByYear(), gc);
        gc.gridy = 2;
        gc.gridx = 0;
        add(new JLabel(Messages.getString("images")), gc); //$NON-NLS-1$
        gc.gridx = 1;
        gc.anchor = GridBagConstraints.NORTHWEST;
        add(getIndexImageSelection(), gc);
        gc.gridy = 2;
        gc.gridheight = 2;
        gc.gridx = 2;
        add(getIndexShowGroup(), gc);
        Border border = BorderFactory.createTitledBorder(Messages.getString("index_page")); //$NON-NLS-1$
        setBorder(border);

    }

    private JTextField getIndexPageTitle() {
        if (indexPageTitle == null) {
            indexPageTitle = new JTextField();
            indexPageTitle.setPreferredSize(new Dimension(400, 20));
        }
        return indexPageTitle;
    }
    
    private JCheckBox getGroupByYear() {
        if (groupByYear == null) {
            groupByYear = new JCheckBox(Messages.getString("group_by_year")); //$NON-NLS-1$
        }
        return groupByYear;
    }
    
    private JComboBox getIndexImageSelection() {
        if (indexImageSelection == null) {
            indexImageSelection = new JComboBox(new String[] { Messages.getString("indeximage_none"), Messages.getString("indeximage_profile"), Messages.getString("indeximage_image") }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        }
        return indexImageSelection;
    }

    private JPanel getIndexShowGroup() {
        if (indexShowGroup == null) {
            indexShowGroup = new JPanel();
            indexShowGroup.setLayout(new GridBagLayout());
            GridBagConstraints gc = new GridBagConstraints();
            gc.insets = new Insets(0, 0, 10, 10);
            gc.anchor = GridBagConstraints.NORTHWEST;
            gc.gridy = 0;
            gc.gridx = 0;
            indexShowGroup.add(getIndexShowDate(), gc);
            gc.gridx = 1;
            indexShowGroup.add(getIndexShowTime(), gc);
            gc.gridx = 2;
            indexShowGroup.add(getIndexShowLocation(), gc);
            gc.gridy = 1;
            gc.gridx = 0;
            indexShowGroup.add(getIndexShowCity(), gc);
            gc.gridx = 1;
            indexShowGroup.add(getIndexShowCountry(), gc);
            gc.gridx = 2;
            indexShowGroup.add(getIndexShowDepth(), gc);
            gc.gridy = 3;
            gc.gridx = 0;
            indexShowGroup.add(getIndexShowDuration(), gc);
            gc.gridx = 1;
            indexShowGroup.add(getIndexShowPictureCount(), gc);
            Border border = BorderFactory.createTitledBorder(Messages.getString("elements_on_index_page")); //$NON-NLS-1$
            indexShowGroup.setBorder(border);
        }
        return indexShowGroup;
    }

    private JCheckBox getIndexShowDate() {
        if (indexShowDate == null) {
            indexShowDate = new JCheckBox(Messages.getString("date")); //$NON-NLS-1$
        }
        return indexShowDate;
    }

    private JCheckBox getIndexShowTime() {
        if (indexShowTime == null) {
            indexShowTime = new JCheckBox(Messages.getString("time")); //$NON-NLS-1$
        }
        return indexShowTime;
    }

    private JCheckBox getIndexShowLocation() {
        if (indexShowLocation == null) {
            indexShowLocation = new JCheckBox(Messages.getString("location")); //$NON-NLS-1$
        }
        return indexShowLocation;
    }

    private JCheckBox getIndexShowCity() {
        if (indexShowCity == null) {
            indexShowCity = new JCheckBox(Messages.getString("city")); //$NON-NLS-1$
        }
        return indexShowCity;
    }

    private JCheckBox getIndexShowCountry() {
        if (indexShowCountry == null) {
            indexShowCountry = new JCheckBox(Messages.getString("country")); //$NON-NLS-1$
        }
        return indexShowCountry;
    }

    private JCheckBox getIndexShowDepth() {
        if (indexShowDepth == null) {
            indexShowDepth = new JCheckBox(Messages.getString("depth")); //$NON-NLS-1$
        }
        return indexShowDepth;
    }

    private JCheckBox getIndexShowDuration() {
        if (indexShowDuration == null) {
            indexShowDuration = new JCheckBox(Messages.getString("duration")); //$NON-NLS-1$
        }
        return indexShowDuration;
    }
    
    private JCheckBox getIndexShowPictureCount() {
        if (indexShowPictureCount == null) {
            indexShowPictureCount = new JCheckBox(Messages.getString("picturecount")); //$NON-NLS-1$
        }
        return indexShowPictureCount;
    }

    //
    // inner classes
    //
    
    private class CommandSave implements UndoableCommand {
        
        private String oldPageTitle;
        private String newPageTitle;
        private int oldImageSelection;
        private int newImageSelection;
        private boolean oldGroupByYear;
        private boolean newGroupByYear;
        private boolean oldShowCity;
        private boolean newShowCity;
        private boolean oldShowCountry;
        private boolean newShowCountry;
        private boolean oldShowDate;
        private boolean newShowDate;
        private boolean oldShowDepth;
        private boolean newShowDepth;
        private boolean oldShowDuration;
        private boolean newShowDuration;
        private boolean oldShowLocation;
        private boolean newShowLocation;
        private boolean oldShowPictureCount;
        private boolean newShowPictureCount;
        private boolean oldShowTime;
        private boolean newShowTime;
        
        public void undo() {
            ExportSettings settings = mainWindow.getLogBook().getExportSettings();
            settings.setIndexTitle(oldPageTitle);
            settings.setIndexImages(oldImageSelection);
            settings.setGroupByYear(oldGroupByYear);
            settings.setIndexCountry(oldShowCity);
            settings.setIndexCountry(oldShowCountry);
            settings.setIndexDate(oldShowDate);
            settings.setIndexDepth(oldShowDepth);
            settings.setIndexDuration(oldShowDuration);
            settings.setIndexPictureCount(oldShowPictureCount);
            settings.setIndexLocation(oldShowLocation);
            settings.setIndexTime(oldShowTime);
        }

        public void redo() {
            ExportSettings settings = mainWindow.getLogBook().getExportSettings();
            settings.setIndexTitle(newPageTitle);
            settings.setIndexImages(newImageSelection);
            settings.setGroupByYear(newGroupByYear);
            settings.setIndexCountry(newShowCity);
            settings.setIndexCountry(newShowCountry);
            settings.setIndexDate(newShowDate);
            settings.setIndexDepth(newShowDepth);
            settings.setIndexDuration(newShowDuration);
            settings.setIndexPictureCount(newShowPictureCount);
            settings.setIndexLocation(newShowLocation);
            settings.setIndexTime(newShowTime);
        }

        public void execute() {
            oldPageTitle = settings.getIndexTitle();
            oldImageSelection = settings.getIndexImages();
            oldGroupByYear = settings.groupByYear();
            oldShowCity = settings.showIndexCity();
            oldShowCountry = settings.showIndexCountry();
            oldShowDate = settings.showIndexDate();
            oldShowDepth = settings.showIndexDepth();
            oldShowDuration = settings.showIndexDepth();
            oldShowPictureCount = settings.showIndexPictureCount();
            oldShowLocation = settings.showIndexLocation();
            oldShowTime = settings.showIndexTime();
            newPageTitle = getIndexPageTitle().getText();
            newImageSelection = getIndexImageSelection().getSelectedIndex();
            newGroupByYear = getGroupByYear().isSelected();
            newShowCity = getIndexShowCity().isSelected();
            newShowCountry = getIndexShowCountry().isSelected();
            newShowDate = getIndexShowDate().isSelected();
            newShowDepth = getIndexShowDepth().isSelected();
            newShowDuration = getIndexShowDuration().isSelected();
            newShowPictureCount = getIndexShowPictureCount().isSelected();
            newShowLocation = getIndexShowLocation().isSelected();
            newShowTime = getIndexShowTime().isSelected();
            ExportSettings settings = mainWindow.getLogBook().getExportSettings();
            settings.setIndexTitle(newPageTitle);
            settings.setIndexImages(newImageSelection);
            settings.setGroupByYear(newGroupByYear);
            settings.setIndexCountry(newShowCity);
            settings.setIndexCountry(newShowCountry);
            settings.setIndexDate(newShowDate);
            settings.setIndexDepth(newShowDepth);
            settings.setIndexDuration(newShowDuration);
            settings.setIndexPictureCount(newShowPictureCount);
            settings.setIndexLocation(newShowLocation);
            settings.setIndexTime(newShowTime);
        }
        
    }

}
