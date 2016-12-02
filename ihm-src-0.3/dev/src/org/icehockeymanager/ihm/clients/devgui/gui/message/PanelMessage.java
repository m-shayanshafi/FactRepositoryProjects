/************************************************************* 
 * 
 * Ice Hockey Manager 
 * ================== 
 * 
 * Copyright (C) by the IHM Team (see doc/credits.txt) 
 * 
 * This program is released under the GPL (see doc/gpl.txt) 
 * 
 * Further informations: http://www.icehockeymanager.org  
 * 
 *************************************************************/ 
  
package org.icehockeymanager.ihm.clients.devgui.gui.message;

import java.awt.BorderLayout;
import javax.swing.SwingConstants;
import java.awt.Dimension;

import org.icehockeymanager.ihm.clients.devgui.gui.lib.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import org.icehockeymanager.ihm.clients.devgui.gui.*;

/**
 * PanelMessage shows a online message with a "moveOn" button
 * 
 * @author Bernhard von Gunten
 * @created January, 2005
 */
public class PanelMessage extends JIhmPanel {
  static final long serialVersionUID = -8464013081449687997L;

  private JIhmLabel lblMessage;

  private JIhmButton btnMoveOn;

  private Desktop desktop = null;

  public PanelMessage(Desktop desktop, String msg) {
    initGUI();
    initIHM(desktop, msg);
  }

  private void initGUI() {
    try {
      BorderLayout thisLayout = new BorderLayout();
      this.setLayout(thisLayout);
      setPreferredSize(new Dimension(400, 300));
      {
        btnMoveOn = new JIhmButton();
        this.add(btnMoveOn, BorderLayout.SOUTH);
        btnMoveOn.setMsgKey("ihm.moveOn");
        btnMoveOn.setBounds(0, 285, 400, 15);
        btnMoveOn.setFont(new java.awt.Font("Dialog", 1, 16));
        btnMoveOn.setHorizontalTextPosition(SwingConstants.CENTER);
        btnMoveOn.setPreferredSize(new java.awt.Dimension(400, 52));
        btnMoveOn.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            moveOn();
          }
        });
      }
      {
        lblMessage = new JIhmLabel();
        this.add(lblMessage, BorderLayout.CENTER);
        lblMessage.setHorizontalTextPosition(SwingConstants.CENTER);
        lblMessage.setHorizontalAlignment(SwingConstants.CENTER);
        lblMessage.setFont(new java.awt.Font("Dialog", 1, 18));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void initIHM(Desktop desktop, String msg) {
    this.desktop = desktop;
    this.lblMessage.setText(msg);
  }

  public void showMessage(String title, String msg) {
    this.lblMessage.setText(title + " : " + msg);
  }

  public void showMessage(String msg) {
    this.lblMessage.setText(msg);
  }

  private void moveOn() {
    desktop.moveOn();
  }
}
