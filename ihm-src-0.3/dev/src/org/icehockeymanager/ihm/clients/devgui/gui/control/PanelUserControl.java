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

import java.awt.BorderLayout;

import org.icehockeymanager.ihm.clients.devgui.controller.*;
import org.icehockeymanager.ihm.clients.devgui.gui.*;
import org.icehockeymanager.ihm.clients.devgui.ihm.league.*;
import org.icehockeymanager.ihm.clients.devgui.ihm.match.*;
import org.icehockeymanager.ihm.game.league.events.*;
import org.icehockeymanager.ihm.game.match.*;
import org.icehockeymanager.ihm.game.scheduler.events.*;
import org.icehockeymanager.ihm.clients.devgui.gui.lib.JIhmLabel;
import org.icehockeymanager.ihm.clients.devgui.gui.lib.JIhmPanel;
import javax.swing.BorderFactory;
import javax.swing.border.BevelBorder;
import org.icehockeymanager.ihm.clients.devgui.gui.lib.JIhmButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import org.icehockeymanager.ihm.game.user.*;

/**
 * PanelUserControl is the main title bar on the main screen (including "moveOn"
 * button).
 * 
 * @author Bernhard von Gunten
 * @created January, 2005
 */
public class PanelUserControl extends JIhmPanel {
  static final long serialVersionUID = -5320013747359426739L;

  private Desktop desktop = null;

  private User user = null;

  private SchedulerBreakerEvent[] events = null;

  private JIhmLabel lblGameDay;

  private JIhmLabel lblMatch;

  private JIhmLabel lblUser;

  private JIhmLabel lblTeam;

  private JIhmLabel lblDate;

  private JIhmPanel panelInfo;

  private JIhmButton btnMoveOn;

  public PanelUserControl(Desktop desktop, User user, SchedulerBreakerEvent[] events) {
    initGUI();
    initIHM(desktop, user, events);
  }

  private void initGUI() {
    try {
      BorderLayout thisLayout = new BorderLayout();
      this.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
      this.setMinimumSize(new java.awt.Dimension(10, 70));
      this.setLayout(thisLayout);
      this.setPreferredSize(new java.awt.Dimension(742, 71));
      {
        btnMoveOn = new JIhmButton();
        this.add(btnMoveOn, BorderLayout.EAST);
        btnMoveOn.setText("ihm.moveOn");
        btnMoveOn.setMsgKey("ihm.moveOn");
        btnMoveOn.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            moveOn();
          }
        });
      }
      {
        panelInfo = new JIhmPanel();
        this.add(panelInfo, BorderLayout.CENTER);
        panelInfo.setLayout(null);
        {
          lblDate = new JIhmLabel();
          panelInfo.add(lblDate);
          lblDate.setText("ihm.date");
          lblDate.setBounds(18, 15, 113, 15);
        }
        {
          lblTeam = new JIhmLabel();
          panelInfo.add(lblTeam);
          lblTeam.setText("ihm.team");
          lblTeam.setBounds(133, 15, 113, 15);
        }
        {
          lblUser = new JIhmLabel();
          panelInfo.add(lblUser);
          lblUser.setText("ihm.user");
          lblUser.setBounds(133, 38, 113, 15);
        }
        {
          lblMatch = new JIhmLabel();
          panelInfo.add(lblMatch);
          lblMatch.setText("ihm.match");
          lblMatch.setBounds(324, 38, 278, 15);
        }
        {
          lblGameDay = new JIhmLabel();
          panelInfo.add(lblGameDay);
          lblGameDay.setText("ihm.gameday");
          lblGameDay.setBounds(324, 15, 278, 15);
        }
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void initIHM(Desktop desktop, User user, SchedulerBreakerEvent[] events) {
    this.desktop = desktop;
    this.user = user;
    this.events = events;
    fillUp();
  }

  private void fillUp() {

    // Values displayed in the controller frame
    String date = events[0].getFormatedDate();
    String username = user.getUserName();
    String team = user.getTeam().getTeamInfo().getTeamName();
    String eventGameDayDescription = "";
    String eventMatchDescription = "";

    // Find out, if user plays himself, or is just interested in something
    GameDayArrivedEvent gameDayArrived = null;
    GameDayMatchesEvent gameDay = null;
    for (int i = 0; i < events.length; i++) {
      if (events[i] instanceof GameDayArrivedEvent) {
        gameDayArrived = (GameDayArrivedEvent) events[i];
        if (gameDayArrived.getSchedulerGameDayEvent(user) != null) {
          gameDay = gameDayArrived.getSchedulerGameDayEvent(user);
        }
      }
    }

    if (gameDay == null) {
      // No gameDay with user involved, must be a interested in stop !
      eventGameDayDescription = ClientController.getInstance().getTranslation("control.noMatchesToPlay");
    } else {
      Match match = gameDay.getMatch(user);
      eventGameDayDescription = LeagueTools.getGameDayDescription(gameDay);
      eventMatchDescription = MatchTools.getMatchDescription(match);
    }

    this.lblDate.setText(date);
    this.lblTeam.setText(team);
    this.lblUser.setText(username);
    this.lblGameDay.setText(eventGameDayDescription);
    this.lblMatch.setText(eventMatchDescription);

  }

  private synchronized void moveOn() {
    if (this.btnMoveOn.isEnabled()) {
      this.btnMoveOn.setEnabled(false);
      desktop.moveOn();
    }
  }

}
