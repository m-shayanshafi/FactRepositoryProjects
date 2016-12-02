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
  
package org.icehockeymanager.ihm.clients.devgui.gui.control;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;

import org.icehockeymanager.ihm.clients.devgui.controller.*;
import org.icehockeymanager.ihm.clients.devgui.gui.*;
import org.icehockeymanager.ihm.clients.devgui.gui.lib.*;
import org.icehockeymanager.ihm.game.scheduler.events.*;
import org.icehockeymanager.ihm.game.user.*;

/**
 * PanelControl is the main panel for almost every ihm screen.
 * 
 * @author Bernhard von Gunten
 * @created January, 2005
 */
public class PanelControl extends JIhmPanel {
  static final long serialVersionUID = -6534264094808295539L;

  private JIhmPanel panelContentCenter;

  private JIhmPanel panelStatus;

  private JIhmLabel lblHeader;

  private JIhmPanel panelHeader;

  private JIhmPanel content = null;

  BorderLayout panelContentCenterLayout = new BorderLayout();

  FlowLayout flowLayout = new FlowLayout();

  public PanelControl(Desktop desktop, User user, SchedulerBreakerEvent[] events) {
    initGUI();
    initIHM(desktop, user, events);
  }

  private void initGUI() {
    try {
      BorderLayout thisLayout = new BorderLayout();
      this.setLayout(thisLayout);
      setPreferredSize(new Dimension(400, 300));
      {
        panelContentCenter = new JIhmPanel();
        panelContentCenter.setLayout(panelContentCenterLayout);
        this.add(panelContentCenter, BorderLayout.CENTER);
        {
          panelHeader = new JIhmPanel();
          BorderLayout panelHeaderLayout = new BorderLayout();
          panelHeader.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
          panelHeader.setLayout(panelHeaderLayout);
          panelContentCenter.add(panelHeader, BorderLayout.NORTH);
          {
            lblHeader = new JIhmLabel();
            panelHeader.add(lblHeader, BorderLayout.CENTER);
            lblHeader.setText("...");
            lblHeader.setFont(new java.awt.Font("Dialog", 1, 16));
            lblHeader.setPreferredSize(new java.awt.Dimension(400, 36));
            lblHeader.setHorizontalAlignment(SwingConstants.CENTER);
            lblHeader.setHorizontalTextPosition(SwingConstants.CENTER);
          }
        }
        {
          panelStatus = new JIhmPanel();
          panelContentCenter.add(panelStatus, BorderLayout.SOUTH);
          panelStatus.setPreferredSize(new java.awt.Dimension(400, 15));
          panelStatus.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void initIHM(Desktop desktop, User user, SchedulerBreakerEvent[] events) {
    // Set panel
    this.add(new PanelUserControl(desktop, user, events), BorderLayout.NORTH);
    this.validate();

  }

  public JIhmPanel getContent() {
    return this.content;
  }
  
  public void setContent(JIhmPanel panel, boolean border) {
    if (content != null) {
      this.panelContentCenter.remove(content);
    }

    if (border) {
      this.content = panel;
      panelContentCenter.setLayout(panelContentCenterLayout);
      this.panelContentCenter.add(content, BorderLayout.CENTER);
    } else {
      JIhmPanel host = new JIhmPanel();
      host.setLayout(flowLayout);
      host.add(panel);
      this.panelContentCenter.add(host);
      this.content = host;
    }

    this.lblHeader.setText(ClientController.getInstance().getTranslation(panel.getTitleKey()));

    this.validate();
  }

}
