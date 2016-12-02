/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: SettingsWindow.java
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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import net.sf.jdivelog.gui.commands.CommandManager;
import net.sf.jdivelog.gui.commands.UndoableCommand;
import net.sf.jdivelog.gui.resources.Messages;

public class SettingsWindow extends JDialog implements TreeSelectionListener, ActionListener {

    private static final long serialVersionUID = 3905522717825250354L;
    private MainWindow mainWindow;
    private JScrollPane configCategoryPanel;
    private ArrayList<AbstractSettingsPanel> panels;
    private JPanel buttonPanel;
    private JButton cancelButton;
    private JButton closeButton;
    private JTree configCategoryTree;
    private DefaultMutableTreeNode rootNode;
    private JPanel currentPanel;
    
    public SettingsWindow(Window parent, MainWindow mainWindow) {
    	super(parent, ModalityType.APPLICATION_MODAL);
        this.mainWindow = mainWindow;
        panels = new ArrayList<AbstractSettingsPanel>();
        initialize();
        load();
        new MnemonicFactory(this);
    }

    public void valueChanged(TreeSelectionEvent e) {    	
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)getConfigCategoryTree().getLastSelectedPathComponent();
        ConfigurationNode configNode = (ConfigurationNode)node.getUserObject();
        if (configNode != null && configNode.hasPanel()) {
            setPanel(configNode.getPanel());
        } else {
            setPanel(new JPanel());
        }
    }
    
    //
    // private methods
    //
    
    private synchronized void setPanel(JPanel panel) {
        if (currentPanel != null) {
            remove(currentPanel);
        }
        add(panel, BorderLayout.CENTER);
        currentPanel = panel;
        currentPanel.revalidate();
        validate();
        pack();
    }
    
    private void initialize() {
        setLayout(new BorderLayout());
        add(getConfigCategoryPanel(), BorderLayout.WEST);
        add(getButtonPanel(), BorderLayout.SOUTH);
        setPanel(new JPanel());
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/net/sf/jdivelog/gui/resources/icons/logo.gif")));
        setTitle(Messages.getString("settings"));
    }
    
    private TreeNode getRootNode() {
        if (rootNode == null) {
            appendNode(null, "configuration", null); 
            appendNode(rootNode, "configuration.general", new SettingsGeneralPanel());
            DefaultMutableTreeNode export = appendNode(rootNode, "configuration.export", new ExportSettingsGeneralPanel(mainWindow));
            appendNode(export, "configuration.export.index", new ExportSettingsIndexPanel(mainWindow));
            appendNode(export, "configuration.export.detail", new ExportSettingsDetailPanel(mainWindow));
            appendNode(export, "configuration.export.pictures", new ExportSettingsPicturesPanel(mainWindow));
            appendNode(export, "configuration.export.layout", new ExportSettingsLayoutPanel(mainWindow));
            appendNode(rootNode, "configuration.divecomputer", new ComputerSettingsPanel(mainWindow));
            appendNode(rootNode, "configuration.statistics", new StatisticSettingsPanel(mainWindow));
            appendNode(rootNode, "configuration.profile", new SettingsProfilePanel(mainWindow));
            appendNode(rootNode, "configuration.slideshow", new SlideshowSettingsPanel(mainWindow));
            appendNode(rootNode, "configuration.diagnostics", new DiagnosticsPanel());            
        }
        return rootNode;
    }
    
    private DefaultMutableTreeNode appendNode(DefaultMutableTreeNode parentNode, String name, AbstractSettingsPanel panel) {
        if (panel != null) {
            panels.add(panel);
        }
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(new ConfigurationNode(name, panel));
        if (parentNode == null) {
            rootNode = node;
        } else {
            parentNode.add(node);
        }
        return node;
    }
    
    private JTree getConfigCategoryTree() {
        if (configCategoryTree == null ) {
            configCategoryTree = new JTree(getRootNode());
            configCategoryTree.addTreeSelectionListener(this);
        }
        return configCategoryTree;
    }
    
    private JScrollPane getConfigCategoryPanel() {
        if (configCategoryPanel == null) {
            configCategoryPanel = new JScrollPane(getConfigCategoryTree());
        }
        return configCategoryPanel;
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
            buttonPanel.add(getCloseButton(), gridBagConstraints1);
            gridBagConstraints1.gridx = 1;
            gridBagConstraints1.gridy = 0;
            gridBagConstraints1.insets = new java.awt.Insets(5, 5, 5, 100);
            buttonPanel.add(getCancelButton(), gridBagConstraints1);            
        }
        return buttonPanel;
    }

    private JButton getCloseButton() {
        if (closeButton == null) {
            closeButton = new JButton();
            closeButton.setText(Messages.getString("close")); //$NON-NLS-1$
            closeButton.addActionListener(this);
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

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == cancelButton) {
            dispose();
        } else if (e.getSource() == closeButton) {
            save();
            dispose();
        }
    }
    
    private void load() {
        Iterator<AbstractSettingsPanel> it = panels.iterator();
        while (it.hasNext()) {
            AbstractSettingsPanel panel = it.next();
            panel.load();
        }
    }
    
    private void save() {
        CommandSave cmd = new CommandSave();
        CommandManager.getInstance().execute(cmd);
    }

    //
    // inner classes
    //
    
    private static class ConfigurationNode {
        
        private String name;
        private JPanel panel;
        
        public ConfigurationNode(String name, AbstractSettingsPanel panel) {
            this.name = name;
            this.panel = panel;
        }
        
        public JPanel getPanel() {
            return panel;
        }
        
        public boolean hasPanel() {
            return panel != null;
        }
        
        public String toString() {
            return Messages.getString(name);
        }
    }
    
    private class CommandSave implements UndoableCommand {
        
        ArrayList<UndoableCommand> subCommands;
        private boolean oldChanged;

        public void undo() {
            Iterator<UndoableCommand> it = subCommands.iterator();
            while (it.hasNext()) {
                UndoableCommand command = it.next();
                command.undo();
            }
            mainWindow.setChanged(oldChanged);
            mainWindow.getLogbookChangeNotifier().notifyLogbookDataChanged();
        }

        public void redo() {
            Iterator<UndoableCommand> it = subCommands.iterator();
            while (it.hasNext()) {
                UndoableCommand command = it.next();
                command.redo();
            }
            mainWindow.setChanged(true);
            mainWindow.getLogbookChangeNotifier().notifyLogbookDataChanged();
        }

        public void execute() {
            subCommands = new ArrayList<UndoableCommand>();
            oldChanged = mainWindow.isChanged();
            Iterator<AbstractSettingsPanel> it = panels.iterator();
            while (it.hasNext()) {
                AbstractSettingsPanel panel = it.next();
                UndoableCommand saveCommand = panel.getSaveCommand();
                if (saveCommand != null) {
                    saveCommand.execute();
                    subCommands.add(saveCommand);
                }
            }
            mainWindow.setChanged(true);
            mainWindow.getLogbookChangeNotifier().notifyLogbookDataChanged();
        }
        
    }

}
