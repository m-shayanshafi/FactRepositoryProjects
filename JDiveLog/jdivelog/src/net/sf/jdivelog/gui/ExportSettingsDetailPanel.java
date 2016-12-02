/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: ExportSettingsDetailPanel.java
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
import javax.swing.JTextField;
import javax.swing.border.Border;

import net.sf.jdivelog.gui.commands.UndoableCommand;
import net.sf.jdivelog.gui.resources.Messages;
import net.sf.jdivelog.model.ExportSettings;

public class ExportSettingsDetailPanel extends AbstractSettingsPanel {
    
    private static final long serialVersionUID = 1L;
    
    private MainWindow mainWindow;
    private ExportSettings settings;
    private JCheckBox detailVisible;
    private JTextField detailProfileWidth;
    private JTextField detailProfileHeight;
    private JPanel detailShowGroup;
    private JCheckBox detailShowBuddy;
    private JCheckBox detailShowComment;
    private JCheckBox detailShowCity;
    private JCheckBox detailShowCountry;
    private JCheckBox detailShowWaters;
    private JCheckBox detailShowDate;
    private JCheckBox detailShowDepth;
    private JCheckBox detailShowDuration;
    private JCheckBox detailShowEquipment;
    private JCheckBox detailShowLocation;
    private JCheckBox detailShowProfile;
    private JCheckBox detailShowTime;
    private JCheckBox detailShowVisibility;

    public ExportSettingsDetailPanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        initialize();
     }
     public void load() {
         settings = mainWindow.getLogBook().getExportSettings().deepClone();
         getDetailVisible().setSelected(settings.isDetailVisible());
         getDetailShowDate().setSelected(settings.showDetailDate());
         getDetailShowTime().setSelected(settings.showDetailTime());
         getDetailShowLocation().setSelected(settings.showDetailLocation());
         getDetailShowCity().setSelected(settings.showDetailCity());
         getDetailShowCountry().setSelected(settings.showDetailCountry());
         getDetailShowWaters().setSelected(settings.showDetailWaters());
         getDetailShowDepth().setSelected(settings.showDetailDepth());
         getDetailShowDuration().setSelected(settings.showDetailDuration());
         getDetailShowEquipment().setSelected(settings.showDetailEquipment());
         getDetailShowBuddy().setSelected(settings.showDetailBuddy());
         getDetailShowVisibility().setSelected(settings.showDetailVisibility());
         getDetailShowComment().setSelected(settings.showDetailComment());
         getDetailShowProfile().setSelected(settings.showDetailProfile());
         getDetailProfileWidth().setText(String.valueOf(settings.getDetailProfileWidth()));
         getDetailProfileHeight().setText(String.valueOf(settings.getDetailProfileHeight()));
     }
     
     public UndoableCommand getSaveCommand() {
         return new CommandSave();
     }
     
     private void initialize() {
         setLayout(new GridBagLayout());
         GridBagConstraints gc = new GridBagConstraints();
         gc.anchor = GridBagConstraints.NORTHWEST;
         gc.insets = new Insets(0, 0, 10, 10);
         gc.gridwidth = 2;
         gc.gridy = 0;
         gc.gridx = 0;
         add(getDetailVisible(), gc);
         gc.gridwidth = 1;
         gc.gridy = 1;
         gc.gridx = 0;
         add(new JLabel(Messages.getString("profile_width")), gc); //$NON-NLS-1$
         gc.gridx = 1;
         add(getDetailProfileWidth(), gc);
         gc.gridy = 2;
         gc.gridx = 0;
         add(new JLabel(Messages.getString("profile_height")), gc); //$NON-NLS-1$
         gc.gridx = 1;
         add(getDetailProfileHeight(), gc);
         gc.gridheight = 3;
         gc.gridy = 0;
         gc.gridx = 2;
         add(getDetailShowGroup(), gc);
         Border border = BorderFactory.createTitledBorder(Messages.getString("detail_page")); //$NON-NLS-1$
         setBorder(border);
     }

     private JCheckBox getDetailVisible() {
         if (detailVisible == null) {
             detailVisible = new JCheckBox(Messages.getString("visible")); //$NON-NLS-1$
         }
         return detailVisible;
     }

     private JTextField getDetailProfileWidth() {
         if (detailProfileWidth == null) {
             detailProfileWidth = new JTextField();
             detailProfileWidth.setPreferredSize(new Dimension(50, 20));
         }
         return detailProfileWidth;
     }

     private JTextField getDetailProfileHeight() {
         if (detailProfileHeight == null) {
             detailProfileHeight = new JTextField();
             detailProfileHeight.setPreferredSize(new Dimension(50, 20));
         }
         return detailProfileHeight;
     }
     
     private JPanel getDetailShowGroup() {
         if (detailShowGroup == null) {
             detailShowGroup = new JPanel();
             detailShowGroup.setLayout(new GridBagLayout());
             GridBagConstraints gc = new GridBagConstraints();
             gc.anchor = GridBagConstraints.NORTHWEST;
             gc.insets = new Insets(0, 0, 10, 10);
             gc.gridx = 0;
             gc.gridy = 0;
             detailShowGroup.add(getDetailShowDate(), gc);
             gc.gridx = 1;
             detailShowGroup.add(getDetailShowTime(), gc);
             gc.gridx = 2;
             detailShowGroup.add(getDetailShowLocation(), gc);
             gc.gridx = 3;
             detailShowGroup.add(getDetailShowCity(), gc);
             gc.gridx = 4;
             detailShowGroup.add(getDetailShowCountry(), gc);
             gc.gridy = 1;
             gc.gridx = 0;
             detailShowGroup.add(getDetailShowWaters(), gc);
             gc.gridx = 1;
             detailShowGroup.add(getDetailShowDepth(), gc);
             gc.gridx = 2;
             detailShowGroup.add(getDetailShowDuration(), gc);
             gc.gridx = 3;
             detailShowGroup.add(getDetailShowEquipment(), gc);
             gc.gridx = 4;
             detailShowGroup.add(getDetailShowBuddy(), gc);
             gc.gridy = 2;
             gc.gridx = 0;
             detailShowGroup.add(getDetailShowVisibility(), gc);
             gc.gridx = 1;
             detailShowGroup.add(getDetailShowComment(), gc);
             gc.gridx = 2;
             detailShowGroup.add(getDetailShowProfile(), gc);
             Border border = BorderFactory.createTitledBorder(Messages.getString("elements_on_detail_page")); //$NON-NLS-1$
             detailShowGroup.setBorder(border);
         }
         return detailShowGroup;
     }

     private JCheckBox getDetailShowDate() {
         if (detailShowDate == null) {
             detailShowDate = new JCheckBox(Messages.getString("date")); //$NON-NLS-1$
         }
         return detailShowDate;
     }

     private JCheckBox getDetailShowTime() {
         if (detailShowTime == null) {
             detailShowTime = new JCheckBox(Messages.getString("time")); //$NON-NLS-1$
         }
         return detailShowTime;
     }

     private JCheckBox getDetailShowLocation() {
         if (detailShowLocation == null) {
             detailShowLocation = new JCheckBox(Messages.getString("location")); //$NON-NLS-1$
         }
         return detailShowLocation;
     }

     private JCheckBox getDetailShowCity() {
         if (detailShowCity == null) {
             detailShowCity = new JCheckBox(Messages.getString("city")); //$NON-NLS-1$
         }
         return detailShowCity;
     }

     private JCheckBox getDetailShowCountry() {
         if (detailShowCountry == null) {
             detailShowCountry = new JCheckBox(Messages.getString("country")); //$NON-NLS-1$
         }
         return detailShowCountry;
     }

     private JCheckBox getDetailShowWaters() {
         if (detailShowWaters == null) {
             detailShowWaters = new JCheckBox(Messages.getString("waters")); //$NON-NLS-1$
         }
         return detailShowWaters;
     }

     private JCheckBox getDetailShowDepth() {
         if (detailShowDepth == null) {
             detailShowDepth = new JCheckBox(Messages.getString("depth")); //$NON-NLS-1$
         }
         return detailShowDepth;
     }

     private JCheckBox getDetailShowDuration() {
         if (detailShowDuration == null) {
             detailShowDuration = new JCheckBox(Messages.getString("duration")); //$NON-NLS-1$
         }
         return detailShowDuration;
     }

     private JCheckBox getDetailShowEquipment() {
         if (detailShowEquipment == null) {
             detailShowEquipment = new JCheckBox(Messages.getString("equipment")); //$NON-NLS-1$
         }
         return detailShowEquipment;
     }

     private JCheckBox getDetailShowProfile() {
         if (detailShowProfile == null) {
             detailShowProfile = new JCheckBox(Messages.getString("profile")); //$NON-NLS-1$
         }
         return detailShowProfile;
     }
     
     private JCheckBox getDetailShowBuddy() {
         if (detailShowBuddy == null) {
             detailShowBuddy = new JCheckBox(Messages.getString("buddy")); //$NON-NLS-1$
         }
         return detailShowBuddy;
     }

     private JCheckBox getDetailShowVisibility() {
         if (detailShowVisibility == null) {
             detailShowVisibility = new JCheckBox(Messages.getString("visibility")); //$NON-NLS-1$
         }
         return detailShowVisibility;
     }
     
     private JCheckBox getDetailShowComment() {
         if (detailShowComment == null) {
             detailShowComment = new JCheckBox(Messages.getString("comment")); //$NON-NLS-1$
         }
         return detailShowComment;
     }

     //
     // inner classes
     //
     
     private class CommandSave implements UndoableCommand {
         
         private boolean oldDetailVisible;
         private boolean newDetailVisible;
         private boolean oldDetailDate;
         private boolean newDetailDate;
         private boolean oldDetailTime;
         private boolean newDetailTime;
         private boolean oldDetailLocation;
         private boolean newDetailLocation;
         private boolean oldDetailCity;
         private boolean newDetailCity;
         private boolean oldDetailCountry;
         private boolean newDetailCountry;
         private boolean oldDetailWaters;
         private boolean newDetailWaters;
         private boolean oldDetailDepth;
         private boolean newDetailDepth;
         private boolean oldDetailDuration;
         private boolean newDetailDuration;
         private boolean oldDetailEquipment;
         private boolean newDetailEquipment;
         private boolean oldDetailBuddy;
         private boolean newDetailBuddy;
         private boolean oldDetailVisibility;
         private boolean newDetailVisibility;
         private boolean oldDetailComment;
         private boolean newDetailComment;
         private boolean oldDetailProfile;
         private boolean newDetailProfile;
         private int oldDetailProfileWidth;
         private int newDetailProfileWidth;
         private int oldDetailProfileHeight;
         private int newDetailProfileHeight;

        public void undo() {
            ExportSettings settings = mainWindow.getLogBook().getExportSettings();
            settings.setDetailVisible(oldDetailVisible);
            settings.setDetailDate(oldDetailDate);
            settings.setDetailTime(oldDetailTime);
            settings.setDetailLocation(oldDetailLocation);
            settings.setDetailCity(oldDetailCity);
            settings.setDetailCountry(oldDetailCountry);
            settings.setDetailWaters(oldDetailWaters);
            settings.setDetailDepth(oldDetailDepth);
            settings.setDetailDuration(oldDetailDuration);
            settings.setDetailEquipment(oldDetailEquipment);
            settings.setDetailBuddy(oldDetailBuddy);
            settings.setDetailVisibility(oldDetailVisibility);
            settings.setDetailComment(oldDetailComment);
            settings.setDetailProfile(oldDetailProfile);
            settings.setDetailProfileWidth(oldDetailProfileWidth);
            settings.setDetailProfileHeight(oldDetailProfileHeight);
        }

        public void redo() {
            ExportSettings settings = mainWindow.getLogBook().getExportSettings();
            settings.setDetailVisible(newDetailVisible);
            settings.setDetailDate(newDetailDate);
            settings.setDetailTime(newDetailTime);
            settings.setDetailLocation(newDetailLocation);
            settings.setDetailCity(newDetailCity);
            settings.setDetailCountry(newDetailCountry);
            settings.setDetailWaters(newDetailWaters);
            settings.setDetailDepth(newDetailDepth);
            settings.setDetailDuration(newDetailDuration);
            settings.setDetailEquipment(newDetailEquipment);
            settings.setDetailBuddy(newDetailBuddy);
            settings.setDetailVisibility(newDetailVisibility);
            settings.setDetailComment(newDetailComment);
            settings.setDetailProfile(newDetailProfile);
            settings.setDetailProfileWidth(newDetailProfileWidth);
            settings.setDetailProfileHeight(newDetailProfileHeight);
        }

        public void execute() {
            oldDetailVisible = settings.isDetailVisible();
            oldDetailDate = settings.showDetailDate();
            oldDetailTime = settings.showDetailTime();
            oldDetailLocation = settings.showDetailLocation();
            oldDetailCity = settings.showDetailCity();
            oldDetailCountry = settings.showDetailCountry();
            oldDetailWaters = settings.showDetailWaters();
            oldDetailDepth = settings.showDetailDepth();
            oldDetailDuration = settings.showDetailDuration();
            oldDetailEquipment = settings.showDetailEquipment();
            oldDetailBuddy = settings.showDetailBuddy();
            oldDetailVisibility = settings.showDetailVisibility();
            oldDetailComment = settings.showDetailComment();
            oldDetailProfile = settings.showDetailProfile();
            oldDetailProfileWidth = settings.getDetailProfileWidth();
            oldDetailProfileHeight = settings.getDetailProfileHeight();
            newDetailVisible = getDetailVisible().isSelected();
            newDetailDate = getDetailShowDate().isSelected();
            newDetailTime = getDetailShowTime().isSelected();
            newDetailLocation = getDetailShowLocation().isSelected();
            newDetailCity = getDetailShowCity().isSelected();
            newDetailCountry = getDetailShowCountry().isSelected();
            newDetailWaters = getDetailShowWaters().isSelected();
            newDetailDepth = getDetailShowDepth().isSelected();
            newDetailDuration = getDetailShowDuration().isSelected();
            newDetailEquipment = getDetailShowEquipment().isSelected();
            newDetailBuddy = getDetailShowBuddy().isSelected();
            newDetailVisibility = getDetailShowVisibility().isSelected();
            newDetailComment = getDetailShowComment().isSelected();
            newDetailProfile = getDetailShowProfile().isSelected();
            newDetailProfileWidth = new Integer(getDetailProfileWidth().getText());
            newDetailProfileHeight = new Integer(getDetailProfileHeight().getText());
            ExportSettings settings = mainWindow.getLogBook().getExportSettings();
            settings.setDetailVisible(newDetailVisible);
            settings.setDetailDate(newDetailDate);
            settings.setDetailTime(newDetailTime);
            settings.setDetailLocation(newDetailLocation);
            settings.setDetailCountry(newDetailCity);
            settings.setDetailCountry(newDetailCountry);
            settings.setDetailCountry(newDetailWaters);
            settings.setDetailDepth(newDetailDepth);
            settings.setDetailDuration(newDetailDuration);
            settings.setDetailEquipment(newDetailEquipment);
            settings.setDetailBuddy(newDetailBuddy);
            settings.setDetailVisibility(newDetailVisibility);
            settings.setDetailComment(newDetailComment);
            settings.setDetailProfile(newDetailProfile);
            settings.setDetailProfileWidth(newDetailProfileWidth);
            settings.setDetailProfileHeight(newDetailProfileHeight);
        }
         
     }
}
