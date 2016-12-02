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
  
package org.icehockeymanager.ihm.clients.devgui.gui.team;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

import org.icehockeymanager.ihm.clients.devgui.controller.*;
import org.icehockeymanager.ihm.clients.devgui.gui.lib.*;
import org.icehockeymanager.ihm.clients.devgui.gui.training.*;
import org.icehockeymanager.ihm.clients.devgui.ihm.player.*;
import org.icehockeymanager.ihm.clients.devgui.ihm.scheduler.*;
import org.icehockeymanager.ihm.clients.devgui.ihm.team.*;
import org.icehockeymanager.ihm.game.match.*;
import org.icehockeymanager.ihm.game.player.*;
import org.icehockeymanager.ihm.game.team.*;
import org.icehockeymanager.ihm.game.user.*;

/**
 * A panel to show the team with stats and players.
 * 
 * @author Bernhard von Gunten
 * @created December 29, 2001
 */
public class PanelTeam extends JIhmPanel {
  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 3977860683134219825L;

  /** Team that is shown in this frame */
  private Team teamToShow = null;

  /* Gui controls */
  BorderLayout borderLayout1 = new BorderLayout();

  JIhmTabbedPane tabbedPaneTeam = new JIhmTabbedPane();

  JIhmPanel panelTeamInfo = new JIhmPanel();

  JIhmPanel panelTeamStats = new JIhmPanel();

  BorderLayout borderLayout2 = new BorderLayout();

  JIhmLabel txtTeamName = new JIhmLabel();

  JIhmPanel panelLeagueElementStats = new JIhmPanel();

  BorderLayout borderLayout4 = new BorderLayout();

  JIhmLabel lblLEagueElementStats = new JIhmLabel();

  JScrollPane jScrollPane2 = new JScrollPane();

  JIhmTable jLeagueElementStatsTable = new JIhmTable();

  JIhmPanel panelNorth = new JIhmPanel();

  JScrollPane jScrollPane3 = new JScrollPane();

  JIhmTable jLeagueStatsTable = new JIhmTable();

  BorderLayout borderLayout5 = new BorderLayout();

  JIhmLabel lblLeagueStats = new JIhmLabel();

  JIhmPanel panelLeagueStats = new JIhmPanel();

  BorderLayout borderLayout6 = new BorderLayout();

  JIhmLabel lblTotalStats = new JIhmLabel();

  JScrollPane jScrollPane1 = new JScrollPane();

  JIhmPanel panelTotalStats = new JIhmPanel();

  BorderLayout borderLayout3 = new BorderLayout();

  JIhmTable jTotalStatsTable = new JIhmTable();

  JIhmPanel panelLeagueElementGroupsTable = new JIhmPanel();

  BorderLayout borderLayout7 = new BorderLayout();

  JIhmLabel lblLeagueElementGroupStats = new JIhmLabel();

  JScrollPane jScrollPane4 = new JScrollPane();

  JIhmTable jLeagueElementGroupStatsTable = new JIhmTable();

  JScrollPane jScrollPane6 = new JScrollPane();

  JIhmTable jTeamAttributesTable = new JIhmTable();

  JIhmLabel lblTeamAttributes = new JIhmLabel();

  JIhmPanel panelPlayers = new JIhmPanel();

  BorderLayout borderLayout10 = new BorderLayout();

  JIhmTabbedPane tabbedPanePlayers = new JIhmTabbedPane();

  JScrollPane jScrollPane5 = new JScrollPane();

  BorderLayout borderLayout8 = new BorderLayout();

  JIhmPanel panelPlayerStats = new JIhmPanel();

  JIhmTable jTotalPlayerStatsTable = new JIhmTable();

  BorderLayout borderLayout9 = new BorderLayout();

  JIhmPanel panelPlayerAttributes = new JIhmPanel();

  JIhmTable jPlayerAttributesTable = new JIhmTable();

  JScrollPane jScrollPane7 = new JScrollPane();

  JIhmPanel panelTeamCalendar = new JIhmPanel();

  BorderLayout borderLayout11 = new BorderLayout();

  JScrollPane jScrollPane8 = new JScrollPane();

  JIhmTable jTeamCalendarTable = new JIhmTable();

  ImageIcon teamIcon = new ImageIcon();

  ImageIcon teamstatsIcon = new ImageIcon();

  ImageIcon playersIcon = new ImageIcon();

  ImageIcon calendarIcon = new ImageIcon();

  ImageIcon trainingIcon = new ImageIcon();

  /**
   * Constructs the frame, sets the user, calls ihmInit
   * 
   * @param user
   *          User to show this frame for
   * @param teamToShow
   *          Team to show in this frame
   */
  public PanelTeam(User user, Team teamToShow) {
    super(user);
    try {
      jbInit();
      ihmInit(teamToShow);
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  /**
   * JBuilder stuff
   * 
   * @exception Exception
   *              Exception
   */
  private void jbInit() throws Exception {
    this.setSize(730, 500);
    this.setLayout(borderLayout1);
    this.teamIcon = new ImageIcon(ClientController.getInstance().getGuiResource("icons/team18x18.png"));
    teamstatsIcon = new ImageIcon(ClientController.getInstance().getGuiResource("icons/teamstats18x18.png"));
    playersIcon = new ImageIcon(ClientController.getInstance().getGuiResource("icons/players18x18.png"));
    calendarIcon = new ImageIcon(ClientController.getInstance().getGuiResource("icons/calendar18x18.png"));
    trainingIcon = new ImageIcon(ClientController.getInstance().getGuiResource("icons/training18x18.png"));
    panelTeamStats.setLayout(borderLayout2);
    panelTeamInfo.setLayout(null);
    txtTeamName.setFont(new java.awt.Font("Dialog", 1, 16));
    txtTeamName.setText("");
    txtTeamName.setBounds(new Rectangle(5, 13, 326, 26));
    panelLeagueElementStats.setLayout(borderLayout4);
    lblLEagueElementStats.setText("ihm.leagueElementStats");
    lblLEagueElementStats.setMsgKey("ihm.leagueElementStats");
    panelLeagueElementStats.setMinimumSize(new Dimension(200, 200));
    panelLeagueElementStats.setPreferredSize(new Dimension(453, 150));
    lblLeagueStats.setText("ihm.leagueStats");
    lblLeagueStats.setMsgKey("ihm.leagueStats");
    panelLeagueStats.setPreferredSize(new Dimension(10, 100));
    panelLeagueStats.setLayout(borderLayout5);
    panelNorth.setLayout(borderLayout6);
    lblTotalStats.setText("ihm.totalStats");
    lblTotalStats.setMsgKey("ihm.totalStats");
    panelTotalStats.setLayout(borderLayout3);
    panelTotalStats.setMaximumSize(new Dimension(150, 150));
    panelTotalStats.setMinimumSize(new Dimension(45, 41));
    panelTotalStats.setPreferredSize(new Dimension(150, 90));
    panelLeagueElementGroupsTable.setLayout(borderLayout7);
    panelLeagueElementGroupsTable.setPreferredSize(new Dimension(0, 100));
    lblLeagueElementGroupStats.setText("ihm.leagueGroupStats");
    lblLeagueElementGroupStats.setMsgKey("ihm.leagueGroupStats");
    jScrollPane6.setBounds(new Rectangle(4, 78, 298, 68));
    lblTeamAttributes.setBounds(new Rectangle(5, 56, 247, 15));
    lblTeamAttributes.setText("team.attributes");
    lblTeamAttributes.setMsgKey("team.attributes");
    lblTeamAttributes.setFont(new java.awt.Font("Dialog", 1, 14));
    panelPlayers.setLayout(borderLayout10);
    panelPlayerStats.setLayout(borderLayout8);
    panelPlayerAttributes.setLayout(borderLayout9);
    jPlayerAttributesTable.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        jPlayerAttributesTable_mouseClicked(e);
      }
    });
    jTotalPlayerStatsTable.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        jTotalPlayerStatsTable_mouseClicked(e);
      }
    });
    panelTeamCalendar.setLayout(borderLayout11);
    jTeamCalendarTable.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        jTeamCalendarTable_mouseClicked(e);
      }
    });
    this.add(tabbedPaneTeam, BorderLayout.CENTER);
    tabbedPaneTeam.addTab(ClientController.getInstance().getTranslation("ihm.team"), teamIcon, panelTeamInfo, ClientController.getInstance().getTranslation("ihm.team"));
    panelTeamInfo.add(txtTeamName, null);
    panelTeamInfo.add(lblTeamAttributes, null);
    panelTeamInfo.add(jScrollPane6, null);
    jScrollPane6.getViewport().add(jTeamAttributesTable, null);
    tabbedPaneTeam.addTab(ClientController.getInstance().getTranslation("ihm.stats"), teamstatsIcon, panelTeamStats, ClientController.getInstance().getTranslation("ihm.stats"));
    panelTeamStats.add(panelLeagueElementStats, BorderLayout.SOUTH);
    panelLeagueElementStats.add(lblLEagueElementStats, BorderLayout.NORTH);
    panelLeagueElementStats.add(jScrollPane2, BorderLayout.CENTER);
    panelTeamStats.add(panelNorth, BorderLayout.NORTH);
    panelNorth.add(panelLeagueStats, BorderLayout.SOUTH);
    panelLeagueStats.add(lblLeagueStats, BorderLayout.NORTH);
    panelLeagueStats.add(jScrollPane3, BorderLayout.CENTER);
    panelNorth.add(panelTotalStats, BorderLayout.NORTH);
    panelTotalStats.add(lblTotalStats, BorderLayout.NORTH);
    panelTotalStats.add(jScrollPane1, BorderLayout.CENTER);
    panelTeamStats.add(panelLeagueElementGroupsTable, BorderLayout.CENTER);
    panelLeagueElementGroupsTable.add(lblLeagueElementGroupStats, BorderLayout.NORTH);
    panelLeagueElementGroupsTable.add(jScrollPane4, BorderLayout.CENTER);
    tabbedPaneTeam.addTab(ClientController.getInstance().getTranslation("ihm.players"), playersIcon, panelPlayers, ClientController.getInstance().getTranslation("ihm.players"));
    panelPlayers.add(tabbedPanePlayers, BorderLayout.CENTER);
    tabbedPanePlayers.add(panelPlayerAttributes, ClientController.getInstance().getTranslation("team.playersByAttributes"));
    tabbedPanePlayers.add(panelPlayerStats, ClientController.getInstance().getTranslation("team.playersByStats"));
    panelPlayerStats.add(jScrollPane5, BorderLayout.CENTER);
    tabbedPaneTeam.addTab(ClientController.getInstance().getTranslation("ihm.calendar"), calendarIcon, panelTeamCalendar, ClientController.getInstance().getTranslation("ihm.calendar"));
    panelTeamCalendar.add(jScrollPane8, BorderLayout.CENTER);
    jScrollPane8.getViewport().add(jTeamCalendarTable, null);
    panelPlayerAttributes.add(jScrollPane7, BorderLayout.CENTER);
    jScrollPane7.getViewport().add(jPlayerAttributesTable, null);
    jScrollPane5.getViewport().add(jTotalPlayerStatsTable, null);
    jScrollPane4.getViewport().add(jLeagueElementGroupStatsTable, null);
    jScrollPane1.getViewport().add(jTotalStatsTable, null);
    jScrollPane3.getViewport().add(jLeagueStatsTable, null);
    jScrollPane2.getViewport().add(jLeagueElementStatsTable, null);
  }

  /**
   * Create the panelLeagueStandings and add it to the frame
   * 
   * @param teamToshow
   *          Team to show in this frame
   */
  private void ihmInit(Team teamToshow) {
    this.setTitleKey("title.team");
    this.teamToShow = teamToshow;
    displayTeam();
  }

  /**
   * Sets the teamStats widhts
   * 
   * @param tcm
   *          The new teamStatsWidths value
   */
  public void setTeamStatsWidths(TableColumnModel tcm) {
    tcm.getColumn(TMTeamStats.COLUMN_TEAM).setMinWidth(0);
    tcm.getColumn(TMTeamStats.COLUMN_TEAM).setMaxWidth(0);
    tcm.getColumn(TMTeamStats.COLUMN_RANK).setMaxWidth(30);
    tcm.getColumn(TMTeamStats.COLUMN_GAMESWON).setMaxWidth(30);
    tcm.getColumn(TMTeamStats.COLUMN_GAMESPLAYED).setMaxWidth(30);
    tcm.getColumn(TMTeamStats.COLUMN_GAMESTIED).setMaxWidth(30);
    tcm.getColumn(TMTeamStats.COLUMN_GAMESLOST).setMaxWidth(30);
    tcm.getColumn(TMTeamStats.COLUMN_GOALSFOR).setMaxWidth(40);
    tcm.getColumn(TMTeamStats.COLUMN_GOALSAGAINST).setMaxWidth(40);
    tcm.getColumn(TMTeamStats.COLUMN_GOALSDIFFERENCE).setMaxWidth(30);
    tcm.getColumn(TMTeamStats.COLUMN_POINTS).setMaxWidth(30);
    tcm.getColumn(TMTeamStats.COLUMN_PENALTYMINUTES).setMaxWidth(30);

    DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
    dtcr.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

    tcm.getColumn(TMTeamStats.COLUMN_RANK).setCellRenderer(dtcr);
    tcm.getColumn(TMTeamStats.COLUMN_GAMESPLAYED).setCellRenderer(dtcr);
    tcm.getColumn(TMTeamStats.COLUMN_GAMESWON).setCellRenderer(dtcr);
    tcm.getColumn(TMTeamStats.COLUMN_GAMESTIED).setCellRenderer(dtcr);
    tcm.getColumn(TMTeamStats.COLUMN_GAMESLOST).setCellRenderer(dtcr);
    tcm.getColumn(TMTeamStats.COLUMN_GOALSFOR).setCellRenderer(dtcr);
    tcm.getColumn(TMTeamStats.COLUMN_GOALSAGAINST).setCellRenderer(dtcr);
    tcm.getColumn(TMTeamStats.COLUMN_GOALSDIFFERENCE).setCellRenderer(dtcr);
    tcm.getColumn(TMTeamStats.COLUMN_POINTS).setCellRenderer(dtcr);
    tcm.getColumn(TMTeamStats.COLUMN_PENALTYMINUTES).setCellRenderer(dtcr);
  }

  /** Display the team */
  private void displayTeam() {
    // General team infos
    txtTeamName.setText(teamToShow.getTeamInfo().getTeamName());

    // Team Attributes
    this.jTeamAttributesTable.setModel(new TMTeamAttributes(teamToShow.getTeamAttributesArray()));
    jTeamAttributesTable.setColumnSelectionAllowed(false);
    jTeamAttributesTable.activateSorting();

    TableColumnModel tcm = jTeamAttributesTable.getColumnModel();

    tcm.getColumn(TMTeamStats.COLUMN_TEAM).setMinWidth(0);
    tcm.getColumn(TMTeamStats.COLUMN_TEAM).setMaxWidth(0);
    tcm.getColumn(TMTeamAttributes.COLUMN_RANK).setMinWidth(0);
    tcm.getColumn(TMTeamAttributes.COLUMN_RANK).setMaxWidth(0);

    DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
    dtcr.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

    tcm.getColumn(TMTeamAttributes.COLUMN_RANK).setCellRenderer(dtcr);
    tcm.getColumn(TMTeamAttributes.COLUMN_GOALTENDING).setCellRenderer(dtcr);
    tcm.getColumn(TMTeamAttributes.COLUMN_DEFENSE).setCellRenderer(dtcr);
    tcm.getColumn(TMTeamAttributes.COLUMN_OFFENSE).setCellRenderer(dtcr);
    tcm.getColumn(TMTeamAttributes.COLUMN_OVERALL).setCellRenderer(dtcr);

    // Stats Table
    this.jTotalStatsTable.setModel(new TMTeamStats(teamToShow.getTeamStatsTotalArray()));
    jTotalStatsTable.setColumnSelectionAllowed(false);
    jTotalStatsTable.activateSorting();
    setTeamStatsWidths(jTotalStatsTable.getColumnModel());

    this.jLeagueStatsTable.setModel(new TMTeamStats(teamToShow.getAllLeagueStats()));
    jLeagueStatsTable.setColumnSelectionAllowed(false);
    jLeagueStatsTable.activateSorting();
    setTeamStatsWidths(jLeagueStatsTable.getColumnModel());

    this.jLeagueElementGroupStatsTable.setModel(new TMTeamStats(teamToShow.getAllLeagueElementGroupStats()));
    jLeagueElementGroupStatsTable.setColumnSelectionAllowed(false);
    jLeagueElementGroupStatsTable.activateSorting();
    setTeamStatsWidths(jLeagueElementGroupStatsTable.getColumnModel());

    this.jLeagueElementStatsTable.setModel(new TMTeamStats(teamToShow.getAllLeagueElementStats()));
    jLeagueElementStatsTable.setColumnSelectionAllowed(false);
    jLeagueElementStatsTable.activateSorting();
    setTeamStatsWidths(jLeagueElementStatsTable.getColumnModel());

    this.jTotalPlayerStatsTable.setModel(new TMPlayerStats(teamToShow.getPlayersTotalStats()));
    jTotalPlayerStatsTable.setColumnSelectionAllowed(false);
    jTotalPlayerStatsTable.activateSorting();

    tcm = jTotalPlayerStatsTable.getColumnModel();

    tcm.getColumn(TMPlayerStats.COLUMN_TEAM).setMinWidth(0);
    tcm.getColumn(TMPlayerStats.COLUMN_TEAM).setMaxWidth(0);
    tcm.getColumn(TMPlayerStats.COLUMN_RANK).setMaxWidth(30);
    tcm.getColumn(TMPlayerStats.COLUMN_POSITION).setMaxWidth(30);
    tcm.getColumn(TMPlayerStats.COLUMN_GOALS).setMaxWidth(30);
    tcm.getColumn(TMPlayerStats.COLUMN_ASSISTS).setMaxWidth(30);
    tcm.getColumn(TMPlayerStats.COLUMN_SCORERPOINTS).setMaxWidth(30);
    tcm.getColumn(TMPlayerStats.COLUMN_GAMESPLAYED).setMaxWidth(30);
    tcm.getColumn(TMPlayerStats.COLUMN_PENALTYMINUTES).setMaxWidth(40);

    dtcr = new DefaultTableCellRenderer();
    dtcr.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

    tcm.getColumn(TMPlayerStats.COLUMN_RANK).setCellRenderer(dtcr);
    tcm.getColumn(TMPlayerStats.COLUMN_GOALS).setCellRenderer(dtcr);
    tcm.getColumn(TMPlayerStats.COLUMN_ASSISTS).setCellRenderer(dtcr);
    tcm.getColumn(TMPlayerStats.COLUMN_SCORERPOINTS).setCellRenderer(dtcr);
    tcm.getColumn(TMPlayerStats.COLUMN_GAMESPLAYED).setCellRenderer(dtcr);
    tcm.getColumn(TMPlayerStats.COLUMN_PENALTYMINUTES).setCellRenderer(dtcr);

    this.jPlayerAttributesTable.setModel(new TMPlayerAttributes(teamToShow.getPlayers()));
    jPlayerAttributesTable.setColumnSelectionAllowed(false);
    jPlayerAttributesTable.activateSorting();

    tcm = jPlayerAttributesTable.getColumnModel();

    tcm.getColumn(TMPlayerAttributes.COLUMN_TEAM).setMinWidth(0);
    tcm.getColumn(TMPlayerAttributes.COLUMN_TEAM).setMaxWidth(0);
    tcm.getColumn(TMPlayerAttributes.COLUMN_RANK).setMaxWidth(30);
    tcm.getColumn(TMPlayerAttributes.COLUMN_FORM).setMaxWidth(30);
    tcm.getColumn(TMPlayerAttributes.COLUMN_ENERGY).setMaxWidth(30);
    tcm.getColumn(TMPlayerAttributes.COLUMN_MORALE).setMaxWidth(30);
    tcm.getColumn(TMPlayerAttributes.COLUMN_POSITION).setMaxWidth(30);
    tcm.getColumn(TMPlayerAttributes.COLUMN_SPECIFIC_ATTRIBUTES).setMaxWidth(30);
    tcm.getColumn(TMPlayerAttributes.COLUMN_COMMON_ATTRIBUTES).setMaxWidth(30);
    tcm.getColumn(TMPlayerAttributes.COLUMN_TOTAL_ATTRIBUTES).setMaxWidth(30);
    tcm.getColumn(TMPlayerAttributes.COLUMN_OVERALL).setMaxWidth(30);

    dtcr = new DefaultTableCellRenderer();
    dtcr.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

    tcm.getColumn(TMPlayerAttributes.COLUMN_RANK).setCellRenderer(dtcr);
    tcm.getColumn(TMPlayerAttributes.COLUMN_FORM).setCellRenderer(dtcr);
    tcm.getColumn(TMPlayerAttributes.COLUMN_ENERGY).setCellRenderer(dtcr);
    tcm.getColumn(TMPlayerAttributes.COLUMN_MORALE).setCellRenderer(dtcr);
    tcm.getColumn(TMPlayerAttributes.COLUMN_SPECIFIC_ATTRIBUTES).setCellRenderer(dtcr);
    tcm.getColumn(TMPlayerAttributes.COLUMN_COMMON_ATTRIBUTES).setCellRenderer(dtcr);
    tcm.getColumn(TMPlayerAttributes.COLUMN_TOTAL_ATTRIBUTES).setCellRenderer(dtcr);
    tcm.getColumn(TMPlayerAttributes.COLUMN_OVERALL).setCellRenderer(dtcr);

    // Team Calendar
    this.jTeamCalendarTable.setModel(SchedulerTools.getTMTeamCalendar(teamToShow));
    jTeamCalendarTable.setColumnSelectionAllowed(false);
    tcm = jTeamCalendarTable.getColumnModel();
    tcm.getColumn(TMTeamCalendar.COLUMN_DAY).setMaxWidth(100);

    // Team Training Schedule
    // Only showed for own team
    if (teamToShow.equals(getOwner().getTeam())) {
      tabbedPaneTeam.addTab(ClientController.getInstance().getTranslation("title.training"), trainingIcon, new PanelTrainingSchedule(teamToShow.getTrainingSchedule()), ClientController.getInstance().getTranslation("title.training"));
    }

  }

  /**
   * Show player behind selected row
   * 
   * @param e
   *          Source event
   */
  void jPlayerAttributesTable_mouseClicked(MouseEvent e) {
    if (e.getClickCount() > 1) {
      TMPlayerAttributes ta = (TMPlayerAttributes) jPlayerAttributesTable.getModel();
      int row = jPlayerAttributesTable.getSelectedRow();
      Player player = ta.getPlayer(row);

      ClientController.getInstance().getDesktop().showPanelPlayer(player);
    }
  }

  /**
   * Show player behind selected row
   * 
   * @param e
   *          Source event
   */
  void jTotalPlayerStatsTable_mouseClicked(MouseEvent e) {
    if (e.getClickCount() > 1) {
      TMPlayerStats ta = (TMPlayerStats) jTotalPlayerStatsTable.getModel();
      int row = jTotalPlayerStatsTable.getSelectedRow();
      Player player = ta.getPlayer(row);

      ClientController.getInstance().getDesktop().showPanelPlayer(player);
    }
  }

  /**
   * Description of the Method
   * 
   * @param e
   *          Description of the Parameter
   */
  void jTeamCalendarTable_mouseClicked(MouseEvent e) {
    if (e.getClickCount() > 1) {
      TMTeamCalendar tc = (TMTeamCalendar) jTeamCalendarTable.getModel();
      int row = jTeamCalendarTable.getSelectedRow();
      Match match = tc.getMatch(row);
      ClientController.getInstance().getDesktop().showPanelScoreSheet(match);
    }
  }

}
