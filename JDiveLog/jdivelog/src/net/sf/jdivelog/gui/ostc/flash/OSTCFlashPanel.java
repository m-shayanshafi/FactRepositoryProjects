/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: OSTCFlashPanel.java
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
package net.sf.jdivelog.gui.ostc.flash;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.sf.jdivelog.ci.InvalidConfigurationException;
import net.sf.jdivelog.comm.CommPortIdentifier;
import net.sf.jdivelog.comm.CommUtil;
import net.sf.jdivelog.comm.PortInUseException;
import net.sf.jdivelog.comm.PortNotFoundException;
import net.sf.jdivelog.comm.UnsupportedCommOperationException;
import net.sf.jdivelog.gui.MessageDialog;
import net.sf.jdivelog.gui.MainWindow;
import net.sf.jdivelog.gui.resources.Messages;
import net.sf.jdivelog.gui.statusbar.StatusInterface;

/**
 * Panel for flashing the firmware of the OSTC by HeinrichsWeikamp
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class OSTCFlashPanel extends JPanel implements ActionListener {

    private static final long serialVersionUID = 604281706783929563L;
    private final MainWindow mainWindow;
    private JPanel hintPanel;
    private JPanel mainPanel;
    private JComboBox portField;
    private JTextField fileField;
    private JButton fileButton;
    private File selectedFirmware;
    private JPanel buttonPanel;
    private JButton writeButton;
    
    /**
     * Default Constructor for GUI Builder, do not use!
     */
    @Deprecated
    public OSTCFlashPanel() {
        super();
        this.mainWindow = null;
        initialize();
    }
    
    public OSTCFlashPanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        initialize();
    }
    
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == getFileButton()) {
            // show open dialog
            JFileChooser fc = new JFileChooser();
            File f = selectedFirmware != null ? selectedFirmware.getParentFile() : new File(System.getProperty("user.home"));
            if (f.exists() && f.isDirectory()) {
                fc.setCurrentDirectory(f);
            }
            fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int ret = fc.showOpenDialog(this);
            if (ret == JFileChooser.APPROVE_OPTION) {
                File newFile = fc.getSelectedFile();
                if (newFile.exists() && newFile.canRead()) {
                    getFileField().setText(newFile.getName());
                    selectedFirmware = newFile;
                } else {
                    getFileField().setText("");
                    selectedFirmware = null;
                }
            }
        } else if (e.getSource() == getWriteButton()) {
            String port = (String) getPortField().getSelectedItem();
            if (port != null) {
                // check if firmware is selected and readable
                if (selectedFirmware != null && selectedFirmware.canRead()) {
                    OSTCFlashRunner runner = new OSTCFlashRunner(port, selectedFirmware, mainWindow.getStatusBar());
                    Thread t = new Thread(runner);
                    t.start();
                } else {
                    errorMessage("ostc_no_firmware_selected");
                }
            } else {
                errorMessage("ostc_no_comport_selected");
            }
        }
    }

    private void errorMessage(String message) {
        new MessageDialog(mainWindow, Messages.getString(message), null, null, MessageDialog.MessageType.ERROR);
    }

   
    private void initialize() {
        setLayout(new BorderLayout());
        add(getHintPanel(), BorderLayout.NORTH);
        add(getMainPanel(), BorderLayout.CENTER);
        add(getButtonPanel(), BorderLayout.SOUTH);
    }
    
    private JPanel getHintPanel() {
        if (hintPanel == null) {
            hintPanel = new JPanel();
        }
        return hintPanel;
    }
    
    private JPanel getMainPanel() {
        if (mainPanel == null) {
            mainPanel = new JPanel();
            mainPanel.setLayout(new GridBagLayout());
            GridBagConstraints gc = new GridBagConstraints();
            gc.fill = GridBagConstraints.HORIZONTAL;
            gc.anchor = GridBagConstraints.NORTHWEST;
            gc.insets = new Insets(5,5,5,5);
            gc.ipadx = 5;
            gc.gridy = 0;
            gc.gridx = 0;
            mainPanel.add(new JLabel(Messages.getString("suunto.commport")), gc);
            gc.gridx = 1;
            mainPanel.add(getPortField(), gc);
            gc.gridy = 1;
            gc.gridx = 0;
            mainPanel.add(new JLabel(Messages.getString("ostc_firmware")), gc);
            gc.gridx = 1;
            mainPanel.add(getFileField(), gc);
            gc.gridx = 2;
            mainPanel.add(getFileButton(), gc);
        }
        return mainPanel;
    }
    
    private JPanel getButtonPanel() {
        if (buttonPanel == null) {
            buttonPanel = new JPanel();
            buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
            buttonPanel.add(getWriteButton());
        }
        return buttonPanel;
    }
    
    private JComboBox getPortField() {
        if (portField == null) {
            Vector<String> availablePorts = new Vector<String>();
            Iterator<CommPortIdentifier> it = CommUtil.getInstance().getPortIdentifiers();
            while (it.hasNext()) {
                CommPortIdentifier cpId = it.next();
                availablePorts.add(cpId.getName());
            }
            portField = new JComboBox(availablePorts);
        }
        return portField;
    }
    
    private JTextField getFileField() {
        if (fileField == null) {
            fileField = new JTextField();
            fileField.setEnabled(false);
            fileField.setColumns(20);
            fileField.setText("                            ");
        }
        return fileField;
    }
    
    private JButton getFileButton() {
        if (fileButton == null) {
            fileButton = new JButton(new ImageIcon(getClass().getResource("/net/sf/jdivelog/gui/resources/icons/open.gif"))); //$NON-NLS-1$
            fileButton.addActionListener(this);
        }
        return fileButton;

    }
    
    private JButton getWriteButton() {
        if (writeButton == null) {
            writeButton = new JButton(Messages.getString("ostc_write_flash"));
            writeButton.addActionListener(this);
        }
        return writeButton;
    }
    
    private class OSTCFlashRunner implements Runnable {
        
        private final String port;
        private final File selectedFirmware;
        private final StatusInterface status;

        public OSTCFlashRunner(String port, File selectedFirmware, StatusInterface status) {
            this.port = port;
            this.selectedFirmware = selectedFirmware;
            this.status = status;
        }

        public void run() {
            OSTCFlashTool ft = new OSTCFlashTool(port, selectedFirmware, status);
            try {
                ft.writeFlash();
            } catch (InvalidConfigurationException e1) {
                errorMessage(e1.getMessage());
            } catch (PortNotFoundException e1) {
                errorMessage("suunto.comport_not_found");
            } catch (PortInUseException e1) {
                errorMessage("suunto.comport_in_use");
            } catch (InvalidAddressException ex) {
                errorMessage("ostc_invalid_address_exception");
            } catch (NoStartException ex) {
                errorMessage("ostc_no_start_exception");
            } catch (IOException ex) {
                errorMessage("suunto.ioexception");
            } catch (ParseException ex) {
                errorMessage("ostc_parse_exception");
            } catch (UnsupportedCommOperationException ex) {
                errorMessage("suunto.could_not_set_comparams");
            } catch (HandshakeFailedException ex) {
                errorMessage("ostc_handshake_failed");
            }
        }
        
    }
}