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
  
package org.icehockeymanager.ihm.clients.devgui.gui.sponsoring;

import java.awt.BorderLayout;

import javax.swing.border.LineBorder;

import org.icehockeymanager.ihm.clients.devgui.controller.ClientController;
import org.icehockeymanager.ihm.clients.devgui.gui.lib.JIhmPanel;
import org.icehockeymanager.ihm.clients.devgui.gui.lib.JIhmTabbedPane;
import org.icehockeymanager.ihm.game.sponsoring.Sponsoring;
import org.icehockeymanager.ihm.game.team.Team;

public class PanelSponsoring extends JIhmPanel {

  static final long serialVersionUID = -565450154110863700L;

  private JIhmPanel panelMain;

  private PanelSponsoringType panelSponsoringArenaBoard;

  private PanelSponsoringType panelSponsoringArenaIce;

  private JIhmPanel panelArena;

  private PanelSponsoringType panelSponsoringMediaWeb;

  private PanelSponsoringType panelSponsoringMediaRadio;

  private PanelSponsoringType panelSponsoringMediaTv;

  private JIhmPanel panelMedia;

  private PanelSponsoringType panelSponsoringTeamStick;

  private PanelSponsoringType panelSponsoringTeamEquipment;

  private PanelSponsoringType panelSponsoringTeamJersey;

  private JIhmPanel panelTeam;

  private PanelSponsoringType panelSponsoringMain;

  private JIhmTabbedPane jTabbedPane;

  /** Team that is shown in this frame */
  private Team teamToShow = null;

  /**
   * @param team Team
   */
  public PanelSponsoring(Team team) {
    this.teamToShow = team;
    initGUI();
    displaySponsoring();
  }

  private void initGUI() {
    try {
      {
        this.setPreferredSize(new java.awt.Dimension(720, 420));
        this.setBounds(25, 25, 508, 364);
        this.setBorder(new LineBorder(new java.awt.Color(0, 0, 0), 1, false));
        BorderLayout thisLayout = new BorderLayout();
        this.setLayout(thisLayout);
        {
          jTabbedPane = new JIhmTabbedPane();
          this.add(jTabbedPane, BorderLayout.CENTER);
          jTabbedPane.setPreferredSize(new java.awt.Dimension(709, 403));
          {
            panelMain = new JIhmPanel();
            jTabbedPane.addTab(ClientController.getInstance().getTranslation("sponsoring.tab.main"), null, panelMain, null);
            panelMain.setLayout(null);
            {
              panelSponsoringMain = new PanelSponsoringType();
              panelMain.add(panelSponsoringMain);
              panelSponsoringMain.setBounds(5, 5, 680, 130);
            }
          }
          {
            panelTeam = new JIhmPanel();
            jTabbedPane.addTab(ClientController.getInstance().getTranslation("sponsoring.tab.team"), null, panelTeam, null);
            panelTeam.setLayout(null);
            {
              panelSponsoringTeamJersey = new PanelSponsoringType();
              panelTeam.add(panelSponsoringTeamJersey);
              panelSponsoringTeamJersey.setBounds(5, 5, 700, 125);
            }
            {
              panelSponsoringTeamEquipment = new PanelSponsoringType();
              panelTeam.add(panelSponsoringTeamEquipment);
              panelSponsoringTeamEquipment.setBounds(5, 135, 700, 125);
            }
            {
              panelSponsoringTeamStick = new PanelSponsoringType();
              panelTeam.add(panelSponsoringTeamStick);
              panelSponsoringTeamStick.setBounds(5, 265, 700, 125);
            }
          }
          {
            panelMedia = new JIhmPanel();
            jTabbedPane.addTab(ClientController.getInstance().getTranslation("sponsoring.tab.media"), null, panelMedia, null);
            panelMedia.setLayout(null);
            {
              panelSponsoringMediaTv = new PanelSponsoringType();
              panelMedia.add(panelSponsoringMediaTv);
              panelSponsoringMediaTv.setBounds(5, 5, 700, 125);
            }
            {
              panelSponsoringMediaRadio = new PanelSponsoringType();
              panelMedia.add(panelSponsoringMediaRadio);
              panelSponsoringMediaRadio.setBounds(5, 135, 700, 125);
            }
            {
              panelSponsoringMediaWeb = new PanelSponsoringType();
              panelMedia.add(panelSponsoringMediaWeb);
              panelSponsoringMediaWeb.setBounds(5, 265, 700, 125);
            }
          }
          {
            panelArena = new JIhmPanel();
            jTabbedPane.addTab(ClientController.getInstance().getTranslation("sponsoring.tab.arena"), null, panelArena, null);
            panelArena.setLayout(null);
            {
              panelSponsoringArenaIce = new PanelSponsoringType();
              panelArena.add(panelSponsoringArenaIce);
              panelSponsoringArenaIce.setBounds(5, 5, 700, 125);
            }
            {
              panelSponsoringArenaBoard = new PanelSponsoringType();
              panelArena.add(panelSponsoringArenaBoard);
              panelSponsoringArenaBoard.setBounds(5, 135, 700, 125);
            }
          }
        }

      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void displaySponsoring() {
    this.setTitleKey("sponsoring.sponsoring");

    panelSponsoringMain.setContracts(Sponsoring.SPONSOR_TYPE_MAIN, this.teamToShow);
    panelSponsoringArenaIce.setContracts(Sponsoring.SPONSOR_TYPE_ARENA_ICE, this.teamToShow);
    panelSponsoringArenaBoard.setContracts(Sponsoring.SPONSOR_TYPE_ARENA_BOARD, this.teamToShow);
    panelSponsoringMediaTv.setContracts(Sponsoring.SPONSOR_TYPE_MEDIA_TV, this.teamToShow);
    panelSponsoringMediaRadio.setContracts(Sponsoring.SPONSOR_TYPE_MEDIA_RADIO, this.teamToShow);
    panelSponsoringMediaWeb.setContracts(Sponsoring.SPONSOR_TYPE_MEDIA_WEB, this.teamToShow);
    panelSponsoringTeamJersey.setContracts(Sponsoring.SPONSOR_TYPE_TEAM_MAIN, this.teamToShow);
    panelSponsoringTeamEquipment.setContracts(Sponsoring.SPONSOR_TYPE_TEAM_EQUIPMENT, this.teamToShow);
    panelSponsoringTeamStick.setContracts(Sponsoring.SPONSOR_TYPE_TEAM_STICK, this.teamToShow);

  }
}
