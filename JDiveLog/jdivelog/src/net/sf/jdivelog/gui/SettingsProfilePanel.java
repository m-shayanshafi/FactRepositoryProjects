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

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import net.sf.jdivelog.gui.commands.UndoableCommand;
import net.sf.jdivelog.gui.resources.Messages;
import net.sf.jdivelog.model.ProfileSettings;

public class SettingsProfilePanel extends AbstractSettingsPanel implements ColorChangeListener {
    
    private static final long serialVersionUID = 1L;

    private final MainWindow mainWindow;

    private ColorButton backgroundButton;

    private ColorButton gridColorButton;

    private ColorButton gridLabelColorButton;
    
    private JCheckBox fillProfileCheckbox;
    
    private JCheckBox showDecoCheckbox;
    
    private ColorButton decoColorButton;
    
    private JCheckBox showTempCheckbox;
    
    private ColorButton tempLabelColorButton;
    
    private ColorButton tempBeginColorButton;
    
    private ColorShade tempShade;
    
    private ColorButton tempEndColorButton;
    
    private JCheckBox showPpo2Checkbox;
    
    private ColorButton ppo2color1Button;
    
    private ColorButton ppo2color2Button;
    
    private ColorButton ppo2color3Button;
    
    private ColorButton ppo2color4Button;
    
    public SettingsProfilePanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        initialize();
        load();
    }

    public void load() {
        ProfileSettings settings = mainWindow.getLogBook().getProfileSettings();
        getBackgroundButton().setColor(settings.getBackgroundColor());
        getGridColorButton().setColor(settings.getGridColor());
        getGridLabelColorButton().setColor(settings.getGridLabelColor());
        getFillProfileCheckbox().setSelected(settings.isFillDepth());
        getShowDecoCheckbox().setSelected(settings.isShowDeco());
        getDecoColorButton().setColor(settings.getDecoColor());
        getShowTempCheckbox().setSelected(settings.isShowTemperature());
        getTempLabelColorButton().setColor(settings.getTemperatureLabelColor());
        getTempBeginColorButton().setColor(settings.getTemperatureColorBegin());
        getTempEndColorButton().setColor(settings.getTemperatureColorEnd());
        getShowPpo2Checkbox().setSelected(settings.isShowPpo2());
        getPpo2color1Button().setColor(settings.getPpo2Color1());
        getPpo2color2Button().setColor(settings.getPpo2Color2());
        getPpo2color3Button().setColor(settings.getPpo2Color3());
        getPpo2color4Button().setColor(settings.getPpo2Color4());
    }
    
    public UndoableCommand getSaveCommand() {
        return new CommandSave();
    }
    
    public void colorChanged(ColorChangeEvent event) {
        if (event.source == getTempBeginColorButton()) {
            getTempShade().setBegin(getTempBeginColorButton().getColor());
        } else if (event.source == getTempEndColorButton()) {
            getTempShade().setEnd(getTempEndColorButton().getColor());
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
        add(new JLabel(Messages.getString("background_color")), gc); //$NON-NLS-1$
        gc.gridx = 1;
        add(getBackgroundButton(), gc);
        gc.gridx = 0;
        gc.gridy++;
        add(new JLabel(Messages.getString("grid_color")), gc); //$NON-NLS-1$
        gc.gridx = 1;
        add(getGridColorButton(), gc);
        gc.gridy++;
        gc.gridx = 0;
        add(new JLabel(Messages.getString("grid_label_color")), gc); //$NON-NLS-1$
        gc.gridx = 1;
        add(getGridLabelColorButton(), gc);
        gc.gridy++;
        gc.gridx = 0;
        add(new JLabel(Messages.getString("fill_profile")), gc); //$NON-NLS-1$
        gc.gridx = 1;
        add(getFillProfileCheckbox(), gc);
        gc.gridy++;
        gc.gridx = 0;
        add(new JLabel(Messages.getString("show_deco")), gc); //$NON-NLS-1$
        gc.gridx = 1;
        add(getShowDecoCheckbox(), gc);
        gc.gridy++;
        gc.gridx = 0;
        add(new JLabel(Messages.getString("deco_color")), gc); //$NON-NLS-1$
        gc.gridx = 1;
        add(getDecoColorButton(), gc);
        gc.gridy++;
        gc.gridx = 0;
        add(new JLabel(Messages.getString("show_temperature")), gc); //$NON-NLS-1$
        gc.gridx = 1;
        add(getShowTempCheckbox(), gc);
        gc.gridy++;
        gc.gridx = 0;
        add(new JLabel(Messages.getString("temperature_label_color")), gc); //$NON-NLS-1$
        gc.gridx = 1;
        add(getTempLabelColorButton(), gc);
        gc.gridy++;
        gc.gridx = 0;
        add(new JLabel(Messages.getString("temperature_color")), gc); //$NON-NLS-1$
        gc.gridx = 1;
        add(getTempBeginColorButton(), gc);
        gc.gridx = 2;
        add(getTempShade(), gc); //$NON-NLS-1$
        gc.gridx = 3;
        add(getTempEndColorButton(), gc);
        gc.gridy++;
        gc.gridx = 0;
        add(new JLabel(Messages.getString("show_ppo2")), gc); //$NON-NLS-1$
        gc.gridx = 1;
        add(getShowPpo2Checkbox(), gc);
        gc.gridy++;
        gc.gridx = 0;
        add(new JLabel(Messages.getString("ppo2_colors")), gc); //$NON-NLS-1$
        gc.gridx = 1;
        gc.gridwidth = 3;
        JPanel ppo2buttons = new JPanel();
        add(ppo2buttons, gc);
        ppo2buttons.setLayout(new GridBagLayout());
        gc = new GridBagConstraints();
        gc.gridy = 0;
        gc.gridx = 0;
        ppo2buttons.add(getPpo2color1Button(), gc);
        gc.gridx = 1;
        ppo2buttons.add(getPpo2color2Button(), gc);
        gc.gridx = 2;
        ppo2buttons.add(getPpo2color3Button(), gc);
        gc.gridx = 3;
        ppo2buttons.add(getPpo2color4Button(), gc);
        Border border = BorderFactory.createTitledBorder(Messages.getString("configuration.profile")); //$NON-NLS-1$
        setBorder(border);
    }
    
    private ColorButton getBackgroundButton() {
        if (backgroundButton == null) {
            backgroundButton = new ColorButton(mainWindow);
        }
        return backgroundButton;
    }
    
    private ColorButton getGridColorButton() {
        if (gridColorButton == null) {
            gridColorButton = new ColorButton(mainWindow);
        }
        return gridColorButton;
    }
    
    private ColorButton getGridLabelColorButton() {
        if (gridLabelColorButton == null) {
            gridLabelColorButton = new ColorButton(mainWindow);
        }
        return gridLabelColorButton;
    }
    
    private JCheckBox getFillProfileCheckbox() {
        if (fillProfileCheckbox == null) {
            fillProfileCheckbox = new JCheckBox();
        }
        return fillProfileCheckbox;
    }

    /**
     * @return the showDecoCheckbox
     */
    private JCheckBox getShowDecoCheckbox() {
        if (showDecoCheckbox == null) {
            showDecoCheckbox = new JCheckBox();
        }
        return showDecoCheckbox;
    }

    /**
     * @return the decoColorButton
     */
    private ColorButton getDecoColorButton() {
        if (decoColorButton == null) {
            decoColorButton = new ColorButton(mainWindow);
        }
        return decoColorButton;
    }

    /**
     * @return the showTempCheckbox
     */
    private JCheckBox getShowTempCheckbox() {
        if (showTempCheckbox == null) {
            showTempCheckbox = new JCheckBox();
        }
        return showTempCheckbox;
    }

    /**
     * @return the tempBeginColorButton
     */
    private ColorButton getTempBeginColorButton() {
        if (tempBeginColorButton == null) {
            tempBeginColorButton = new ColorButton(mainWindow);
            tempBeginColorButton.addColorChangeListener(this);
        }
        return tempBeginColorButton;
    }

    /**
     * @return the tempShade
     */
    private ColorShade getTempShade() {
        if (tempShade == null) {
            tempShade = new ColorShade();
            tempShade.setSize(100, 32);
        }
        return tempShade;
    }

    /**
     * @return the tempEndColorButton
     */
    private ColorButton getTempEndColorButton() {
        if (tempEndColorButton == null) {
            tempEndColorButton = new ColorButton(mainWindow);
            tempEndColorButton.addColorChangeListener(this);
        }
        return tempEndColorButton;
    }

    /**
     * @return the tempLabelColorButton
     */
    private ColorButton getTempLabelColorButton() {
        if (tempLabelColorButton == null) {
            tempLabelColorButton = new ColorButton(mainWindow);
        }
        return tempLabelColorButton;
    }

    /**
     * @return the showPpo2Checkbox
     */
    private JCheckBox getShowPpo2Checkbox() {
        if (showPpo2Checkbox == null) {
            showPpo2Checkbox = new JCheckBox();
        }
        return showPpo2Checkbox;
    }

    /**
     * @return the ppo2color1Button
     */
    private ColorButton getPpo2color1Button() {
        if (ppo2color1Button == null) {
            ppo2color1Button = new ColorButton(mainWindow);
        }
        return ppo2color1Button;
    }

    /**
     * @return the ppo2color2Button
     */
    private ColorButton getPpo2color2Button() {
        if (ppo2color2Button == null) {
            ppo2color2Button = new ColorButton(mainWindow);
        }
        return ppo2color2Button;
    }

    /**
     * @return the ppo2color3Button
     */
    private ColorButton getPpo2color3Button() {
        if (ppo2color3Button == null) {
            ppo2color3Button = new ColorButton(mainWindow);
        }
        return ppo2color3Button;
    }

    /**
     * @return the ppo2color4Button
     */
    private ColorButton getPpo2color4Button() {
        if (ppo2color4Button == null) {
            ppo2color4Button = new ColorButton(mainWindow);
        }
        return ppo2color4Button;
    }

    //
    // inner classes
    //
    
    private class CommandSave implements UndoableCommand {
        
        private ProfileSettings oldProfileSettings;
        private ProfileSettings newProfileSettings;
        
        public void undo() {
            mainWindow.getLogBook().setProfileSettings(oldProfileSettings);
        }

        public void redo() {
            mainWindow.getLogBook().setProfileSettings(newProfileSettings);
        }

        public void execute() {
            oldProfileSettings = mainWindow.getLogBook().getProfileSettings();
            newProfileSettings = new ProfileSettings();
            newProfileSettings.setBackgroundColor(getBackgroundButton().getColor());
            newProfileSettings.setGridColor(getGridColorButton().getColor());
            newProfileSettings.setGridLabelColor(getGridLabelColorButton().getColor());
            newProfileSettings.setFillDepth(getFillProfileCheckbox().isSelected());
            newProfileSettings.setShowDeco(getShowDecoCheckbox().isSelected());
            newProfileSettings.setDecoColor(getDecoColorButton().getColor());
            newProfileSettings.setShowTemperature(getShowTempCheckbox().isSelected());
            newProfileSettings.setTemperatureLabelColor(getTempLabelColorButton().getColor());
            newProfileSettings.setTemperatureColorBegin(getTempBeginColorButton().getColor());
            newProfileSettings.setTemperatureColorEnd(getTempEndColorButton().getColor());
            newProfileSettings.setShowPpo2(getShowPpo2Checkbox().isSelected());
            newProfileSettings.setPpo2Color1(getPpo2color1Button().getColor());
            newProfileSettings.setPpo2Color2(getPpo2color2Button().getColor());
            newProfileSettings.setPpo2Color3(getPpo2color3Button().getColor());
            newProfileSettings.setPpo2Color4(getPpo2color4Button().getColor());
            mainWindow.getLogBook().setProfileSettings(newProfileSettings);
        }
        
    }

}
