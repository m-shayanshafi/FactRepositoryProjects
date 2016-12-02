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
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JDialog;

import net.sf.jdivelog.gui.commands.CommandManager;
import net.sf.jdivelog.gui.commands.CommandSaveDive;
import net.sf.jdivelog.gui.commands.UndoableCommand;
import net.sf.jdivelog.gui.resources.Messages;
import net.sf.jdivelog.model.JDive;
import net.sf.jdivelog.model.Masterdata;


/**
 * Description: Window displaying details about the dive and allowing to change them 
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class DiveDetailWindow extends JDialog implements ActionListener, ItemListener {

    private static final long serialVersionUID = 3256718472808511538L;    
    private MainWindow mainWindow = null;
    private DiveDetailPanel detailPanel = null;
    private JDive oldDive = null;
    private JDive newDive = null;
    private Masterdata masterdata = null;
    private final boolean readonly;
    
    public DiveDetailWindow(Window parent, MainWindow mainWindow, JDive dive, Masterdata masterdata, boolean readonly) {
        super(parent, ModalityType.APPLICATION_MODAL);
        this.mainWindow = mainWindow;
        this.masterdata = masterdata;
        this.oldDive = dive;
        this.readonly = readonly;
        initialize();        
    }

          

    private void initialize() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/net/sf/jdivelog/gui/resources/icons/logo.gif")));
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_ENTER) {
                    close();
                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    cancel();
                }
            }
        });

        detailPanel = new DiveDetailPanel(this, mainWindow, oldDive, masterdata, readonly);                
        this.getContentPane().add(detailPanel, BorderLayout.CENTER);                        
        this.setSize(790, 668);
        // this.setContentPane(detailPanel);
        this.setTitle(Messages.getString("dive_details")); //$NON-NLS-1$
        this.setName(Messages.getString("dive_details")); //$NON-NLS-1$
        new MnemonicFactory(this);
    }
    
    /**
     * 
     */
    private void cancel() {
        this.dispose();
    }

    /**
     * 
     */
    private void close() {
        detailPanel.saveData();
        UndoableCommand cmd = new CommandSaveDive(mainWindow, oldDive,
                newDive);
        CommandManager.getInstance().execute(cmd);
        cancel();
    } 
    
    public void actionPerformed(ActionEvent e) {        
    }
    
    public void itemStateChanged(ItemEvent e) {      
    }  
}
