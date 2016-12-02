/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: SlideshowSettingsPanel.java
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

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import net.sf.jdivelog.gui.commands.UndoableCommand;
import net.sf.jdivelog.gui.resources.Messages;
import net.sf.jdivelog.model.SlideshowSettings;

public class SlideshowSettingsPanel extends AbstractSettingsPanel {
    
    private static final long serialVersionUID = -5309786313647879254L;
    
    private MainWindow mainWindow;
    private SlideshowSettings settings;
    private JTextField cycletimeField;
    private JCheckBox repeatField;
    private JCheckBox displayTitleField;
    private JCheckBox displayDescriptionField;

    public SlideshowSettingsPanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        initialize();
    }

    @Override
    public void load() {
        settings = mainWindow.getLogBook().getSlideshowSettings();
        getCycletimeField().setText(String.valueOf(settings.getCycletime()));
        getRepeatField().setSelected(settings.isRepeat());
        getDisplayTitleField().setSelected(settings.isDisplayTitle());
        getDisplayDescriptionField().setSelected(settings.isDisplayDescription());
    }

    @Override
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
        add(new JLabel(Messages.getString("configuration.slideshow.cycletime")), gc);
        gc.gridx = 1;
        add(getCycletimeField(), gc);
        
        gc.gridy = 1;
        gc.gridx = 0;
        add(new JLabel(Messages.getString("configuration.slideshow.repeat")), gc);
        gc.gridx = 1;
        add(getRepeatField(), gc);
        
        gc.gridy = 2;
        gc.gridx = 0;
        add(new JLabel(Messages.getString("configuration.slideshow.displayTitle")), gc);
        gc.gridx = 1;
        add(getDisplayTitleField(), gc);

        gc.gridy = 3;
        gc.gridx = 0;
        add(new JLabel(Messages.getString("configuration.slideshow.displayDescription")), gc);
        gc.gridx = 1;
        add(getDisplayDescriptionField(), gc);

        Border border = BorderFactory.createTitledBorder(Messages.getString("slideshow_settings")); //$NON-NLS-1$
        setBorder(border);    
    }
    
    private JTextField getCycletimeField() {
        if (cycletimeField == null) {
            cycletimeField = new JTextField();
            cycletimeField.setColumns(4);
        }
        return cycletimeField;
    }
    
    private JCheckBox getRepeatField() {
        if (repeatField == null) {
            repeatField = new JCheckBox();
        }
        return repeatField;
    }
    
    private JCheckBox getDisplayTitleField() {
        if (displayTitleField == null) {
            displayTitleField = new JCheckBox();
        }
        return displayTitleField;
    }
    
    private JCheckBox getDisplayDescriptionField() {
        if (displayDescriptionField == null) {
            displayDescriptionField = new JCheckBox();
        }
        return displayDescriptionField;
    }
    
    //
    // inner classes
    //
    
    private class CommandSave implements UndoableCommand {
        
        private int oldCycletime;
        private boolean oldRepeat;
        private boolean oldDisplayTitle;
        private boolean oldDisplayDescription;
        private int newCycletime;
        private boolean newRepeat;
        private boolean newDisplayTitle;
        private boolean newDisplayDescription;

        public void undo() {
            settings.setCycletime(oldCycletime);
            settings.setRepeat(oldRepeat);
            settings.setDisplayTitle(oldDisplayTitle);
            settings.setDisplayDescription(oldDisplayDescription);
        }

        public void redo() {
            settings.setCycletime(newCycletime);
            settings.setRepeat(newRepeat);
            settings.setDisplayTitle(newDisplayTitle);
            settings.setDisplayDescription(newDisplayDescription);
        }

        public void execute() {
            oldCycletime = settings.getCycletime();
            oldRepeat = settings.isRepeat();
            oldDisplayTitle = settings.isDisplayTitle();
            oldDisplayDescription = settings.isDisplayDescription();
            try {
                newCycletime = Integer.valueOf(getCycletimeField().getText());
            } catch (NumberFormatException e) {
                newCycletime = oldCycletime;
            }
            newRepeat = getRepeatField().isSelected();
            newDisplayTitle = getDisplayTitleField().isSelected();
            newDisplayDescription = getDisplayDescriptionField().isSelected();
            settings.setCycletime(newCycletime);
            settings.setRepeat(newRepeat);
            settings.setDisplayTitle(newDisplayTitle);
            settings.setDisplayDescription(newDisplayDescription);
        }
        
    }

}
