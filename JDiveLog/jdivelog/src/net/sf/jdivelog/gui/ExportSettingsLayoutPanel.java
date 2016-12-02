/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: ExportSettingsLayoutPanel.java
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
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.border.Border;

import net.sf.jdivelog.gui.commands.UndoableCommand;
import net.sf.jdivelog.gui.resources.Messages;
import net.sf.jdivelog.gui.util.ExtensionFileFilter;
import net.sf.jdivelog.model.ExportSettings;

public class ExportSettingsLayoutPanel extends AbstractSettingsPanel implements ActionListener {

    private static final long serialVersionUID = 1L;

    private MainWindow mainWindow;

    private ExportSettings settings;

    private JComboBox skinField;

    private JButton chooseSkinFileButton;

    public ExportSettingsLayoutPanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        load();
        initialize();
    }

    public void load() {
        settings = mainWindow.getLogBook().getExportSettings().deepClone();
        String currentSkin = settings.getSkinFile();
        assertItemExists(currentSkin);
        getSkinField().setSelectedItem(currentSkin);
    }

    public UndoableCommand getSaveCommand() {
        return new CommandSave();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == getChooseSkinFileButton()) {
            FileChooser fc = new FileChooser();
            ExtensionFileFilter ff = new SkinFileFilter();
            fc.setFileFilter(ff);
            fc.setMultiSelectionEnabled(false);
            int ret = fc.showOpenDialog(this);
            if (ret == JFileChooser.APPROVE_OPTION) {
                File skinFile = fc.getSelectedFile();
                String fn = skinFile.getPath();
                assertItemExists(fn);
                getSkinField().setSelectedItem(fn);
            }
        }
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
        add(new JLabel(Messages.getString("export.skin")), gc); //$NON-NLS-1$
        gc.gridy = 1;
        gc.gridx = 0;
        add(getSkinField(), gc);
        gc.gridy = 2;
        gc.gridx = 0;
        add(getChooseSkinFileButton(), gc);
        Border border = BorderFactory.createTitledBorder(Messages.getString("layout")); //$NON-NLS-1$
        setBorder(border);
    }

    private JComboBox getSkinField() {
        if (skinField == null) {
            ArrayList<String> skins = new ArrayList<String>();
            String skindir = System.getProperty("skindir");
            if (skindir == null) {
                new MessageDialog(mainWindow, Messages.getString("export.error.skindir_property_not_set"), null, null, MessageDialog.MessageType.INFO);
            } else {
                File sd = new File(skindir);
                if (!sd.exists() || !sd.isDirectory() || !sd.canRead()) {
                    new MessageDialog(mainWindow, Messages.getString("export.error.skindir_is_not_readable"), null, null, MessageDialog.MessageType.INFO);
                } else {
                    File skinfile = new File(skindir);
                    ExtensionFileFilter ff = new ExtensionFileFilter("Skins", "zip");
                    ff.addExtension("zip");
                    File[] files = skinfile.listFiles();
                    for (int i = 0; i < files.length; i++) {
                        if (files[i].isFile() && files[i].canRead()) {
                            skins.add(files[i].getName().substring(0, files[i].getName().length() - 4));
                        }
                    }
                }
            }
            skinField = new JComboBox(skins.toArray());
        }
        return skinField;
    }

    private JButton getChooseSkinFileButton() {
        if (chooseSkinFileButton == null) {
            chooseSkinFileButton = new JButton(Messages.getString("export.choose_skin_file"));
            chooseSkinFileButton.addActionListener(this);
        }
        return chooseSkinFileButton;
    }

    private void assertItemExists(String item) {
        int itemCount = getSkinField().getItemCount();
        HashSet<String> items = new HashSet<String>();
        for (int i = 0; i < itemCount; i++) {
            items.add((String) getSkinField().getItemAt(i));
        }
        if (!items.contains(item)) {
            getSkinField().addItem(item);
        }
    }

    //
    // inner classes
    //

    private class CommandSave implements UndoableCommand {

        private String oldSkinFile;

        private String newSkinFile;

        public void undo() {
            ExportSettings settings = mainWindow.getLogBook().getExportSettings();
            settings.setSkinFile(oldSkinFile);
        }

        public void redo() {
            ExportSettings settings = mainWindow.getLogBook().getExportSettings();
            settings.setSkinFile(newSkinFile);
        }

        public void execute() {
            oldSkinFile = settings.getSkinFile();
            newSkinFile = getSkinField().getSelectedItem().toString();
            ExportSettings settings = mainWindow.getLogBook().getExportSettings();
            settings.setSkinFile(newSkinFile);
        }

    }

    private class SkinFileFilter extends ExtensionFileFilter {

        public SkinFileFilter() {
            super(Messages.getString("export.skins"), "zip");
            addExtension("zip");
        }

        @Override
        public boolean accept(File f) {
            if (super.accept(f)) {
                if (f.isDirectory()) {
                    return true;
                }
                try {
                    ZipFile zf = new ZipFile(f);
                    ZipEntry ze = zf.getEntry("jdivelog.css");
                    if (ze != null) {
                        return true;
                    }
                } catch (ZipException e) {
                } catch (IOException e) {
                }
                return false;
            }
            return false;
        }

    }

}
