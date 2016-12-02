/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: MessageDialog.java
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
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;

import net.sf.jdivelog.gui.resources.Messages;

/**
 * Dialog Box for displaying Messages
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class MessageDialog extends JDialog {
    
    public enum MessageType {
        ERROR, INFO, SUCCESS;
    }
    
    private static final Image ERRORIMAGE =Toolkit.getDefaultToolkit().getImage(MessageDialog.class.getResource("/net/sf/jdivelog/gui/resources/icons/msg_error.png"));
    private static final Image INFOIMAGE =Toolkit.getDefaultToolkit().getImage(MessageDialog.class.getResource("/net/sf/jdivelog/gui/resources/icons/msg_info.png"));
    private static final Image SUCCESSIMAGE =Toolkit.getDefaultToolkit().getImage(MessageDialog.class.getResource("/net/sf/jdivelog/gui/resources/icons/msg_success.png"));
    private static final long serialVersionUID = -7492405980787466591L;
    private final String title;
    private final String message;
    private final String detail;
    private final MessageType messageType;
    private JPanel messagePanel;
    private JPanel buttonPanel;
    private JButton okButton;
    
    /**
     * @param parent Probably MainWindow...
     * @param title The Title of the Dialog and the description beneath the Icon
     * @param message The detailed Message below title (optional)
     * @param detail A Stacktrace or very precise/long description what happened (optional)
     * @param messageType Type of Messagebox (e.g. which Icon to display)
     */
    public MessageDialog(Frame parent, String title, String message, String detail, MessageType messageType) {
        super(parent, title, true);
        this.title = title;
        this.message = message;
        this.detail = detail;
        this.messageType = messageType;
        setResizable(false);
        initialize();
        pack();
        int x = (int)((getParent().getLocation().getX() + getParent().getWidth() / 2) - getWidth()/2);
        int y = (int)((getParent().getLocation().getY() + getParent().getHeight() / 2) - getHeight()/2);
        setLocation(x, y);
        new MnemonicFactory(this);
        setVisible(true);
    }
    
    @Override
    protected void processWindowEvent(WindowEvent e) {
        if(e.getID() == WindowEvent.WINDOW_CLOSING) {
            close();
        }
    }
    @Override
    public Insets getInsets() {
        Insets insets = (Insets)super.getInsets().clone();
        insets.left += 8;
        insets.right += 8;
        insets.bottom += 16;
        insets.top += 8;
        return insets;
    }
    
    private void initialize() {
        setLayout(new BorderLayout());
        add(getMessagePanel(), BorderLayout.CENTER);
        add(getButtonPanel(), BorderLayout.SOUTH);
    }
    
    private JPanel getMessagePanel() {
        if (messagePanel == null) {
            messagePanel = new JPanel();
            messagePanel.setLayout(new GridBagLayout());
            GridBagConstraints gc = new GridBagConstraints();
            gc.anchor = GridBagConstraints.NORTHWEST;
            gc.insets = new Insets(5,5,5,5); 
            gc.gridy = 0;
            gc.gridx = 0;
            Icon icon = null;
            if (messageType != null) {
                if (messageType == MessageType.ERROR) {
                    icon = new ImageIcon(ERRORIMAGE);
                } else if (messageType == MessageType.INFO) {
                    icon = new ImageIcon(INFOIMAGE);
                } else if (messageType == MessageType.SUCCESS) {
                    icon = new ImageIcon(SUCCESSIMAGE);
                }
            }
            JLabel ltitle = null;
            if (icon != null) {
                ltitle = new JLabel(title, icon, JLabel.CENTER);
            } else {
                ltitle = new JLabel(title);
            }
            Font f = ltitle.getFont();
            f = f.deriveFont((float)4.0+f.getSize());
            ltitle.setFont(f);
            messagePanel.add(ltitle, gc);
            gc.gridy++;
            if (message != null) {
                JLabel lmessage = new JLabel(message);
                f = lmessage.getFont();
                f = f.deriveFont((float)2+f.getSize());
                lmessage.setFont(f);
                messagePanel.add(lmessage, gc);
            }
            gc.gridy++;
            if (detail != null) {
                final JTextPane ldetail = new JTextPane();
                ldetail.setText(detail);
                ldetail.setEditable(false);
                ldetail.setBackground(getBackground());
                ldetail.setVisible(false);
                JButton detailsButton = new JButton(Messages.getString("details"));
                detailsButton.addActionListener(new ActionListener() {
    
                    public void actionPerformed(ActionEvent arg0) {
                        ldetail.setVisible(!ldetail.isVisible());
                        pack();
                        repaint();
                        getParent().repaint();
                    }
                    
                });
                messagePanel.add(detailsButton, gc);
                gc.gridy++;
                messagePanel.add(ldetail, gc);
            }
        }
        return messagePanel;
    }
    
    private JPanel getButtonPanel() {
        if (buttonPanel == null) {
            buttonPanel = new JPanel();
            buttonPanel.add(getOkButton());
        }
        return buttonPanel;
    }
    
    private JButton getOkButton() {
        if (okButton == null) {
            okButton = new JButton("OK");
            okButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                    close();
                }
                
            });
        }
        return okButton;
    }

    private void close() {
        dispose();
    }
}
