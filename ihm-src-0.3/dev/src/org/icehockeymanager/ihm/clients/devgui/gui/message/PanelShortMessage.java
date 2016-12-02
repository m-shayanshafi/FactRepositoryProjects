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

import javax.swing.*;

import org.icehockeymanager.ihm.clients.devgui.gui.lib.*;
import org.icehockeymanager.ihm.clients.devgui.controller.*;

/**
 * PanelShortMessage shows short online message and goes on.
 * 
 * @author Bernhard von Gunten
 * @created January, 2005
 */
public class PanelShortMessage extends JIhmPanel {
  static final long serialVersionUID = -2955392078918325894L;

  private JIhmLabel lblLogo;

  private JIhmLabel lblMessage;

  public PanelShortMessage() {
    initGUI();
    initIHM();
  }

  private void initGUI() {
    try {
      BorderLayout thisLayout = new BorderLayout();
      this.setLayout(thisLayout);
      setPreferredSize(new Dimension(400, 300));
      {
        lblMessage = new JIhmLabel();
        this.add(lblMessage, BorderLayout.SOUTH);
        lblMessage.setBounds(0, 285, 400, 15);
        lblMessage.setFont(new java.awt.Font("Dialog", 1, 16));
        lblMessage.setHorizontalAlignment(SwingConstants.CENTER);
        lblMessage.setHorizontalTextPosition(SwingConstants.CENTER);
        lblMessage.setMinimumSize(new java.awt.Dimension(100, 50));
        lblMessage.setText("...");
        lblMessage.setLocation(new java.awt.Point(0, 100));
        lblMessage.setPreferredSize(new java.awt.Dimension(400, 50));
        lblMessage.setVerticalAlignment(SwingConstants.TOP);
        lblMessage.setVerticalTextPosition(SwingConstants.TOP);
      }
      {
        lblLogo = new JIhmLabel();
        this.add(lblLogo, BorderLayout.CENTER);
        lblLogo.setHorizontalTextPosition(SwingConstants.CENTER);
        lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void initIHM() {
    lblLogo.setIcon(new ImageIcon(ClientController.getInstance().getGuiResource("ihm_logo.png")));
  }

  public void showMessage(String title, String msg) {
    this.lblMessage.setText(title + " : " + msg);
  }

  public void showMessage(String msg) {
    this.lblMessage.setText(msg);
  }
}
