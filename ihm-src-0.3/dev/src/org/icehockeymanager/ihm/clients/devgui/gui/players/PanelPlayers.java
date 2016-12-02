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
  
package org.icehockeymanager.ihm.clients.devgui.gui.players;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

import org.icehockeymanager.ihm.clients.devgui.controller.*;
import org.icehockeymanager.ihm.clients.devgui.gui.lib.*;
import org.icehockeymanager.ihm.clients.devgui.ihm.player.*;
import org.icehockeymanager.ihm.game.player.*;
import org.icehockeymanager.ihm.game.team.*;
import org.icehockeymanager.ihm.game.user.*;

/**
 * A panel to show the team with stats and players.
 * 
 * @author Bernhard von Gunten
 * @created December 29, 2001
 */
public class PanelPlayers extends JIhmPanel {
  static final long serialVersionUID = 6647472879184891096L;

  /** Team that is shown in this frame */
  private Team teamToShow = null;

  private boolean prospects = false;

  /* Gui controls */
  BorderLayout borderLayout1 = new BorderLayout();

  JIhmTabbedPane tabbedPanePlayers = new JIhmTabbedPane();

  JScrollPane jScrollPane5 = new JScrollPane();

  BorderLayout borderLayout8 = new BorderLayout();

  JIhmPanel panelPlayerStats = new JIhmPanel();

  JIhmTable jTotalPlayerStatsTable = new JIhmTable();

  BorderLayout borderLayout9 = new BorderLayout();

  JIhmPanel panelPlayerAttributes = new JIhmPanel();

  JIhmTable jPlayerAttributesTable = new JIhmTable();

  JScrollPane jScrollPane7 = new JScrollPane();

  JIhmPanel panelPlayerContracts = new JIhmPanel();

  JScrollPane jScrollPane1 = new JScrollPane();

  JIhmTable jPlayerContractsTable = new JIhmTable();

  BorderLayout borderLayout2 = new BorderLayout();

  ImageIcon playersIcon = new ImageIcon();

  ImageIcon playerattributesIcon = new ImageIcon();

  ImageIcon playerstatsIcon = new ImageIcon();

  ImageIcon contractsIcon = new ImageIcon();

  /**
   * Constructs the frame, sets the user, calls ihmInit
   * 
   * @param user
   *          User to show this frame for
   * @param teamToShow
   *          Team to show in this frame
   * @param prospects 
   */
  public PanelPlayers(User user, Team teamToShow, boolean prospects) {
    super(user);
    try {
      jbInit();
      ihmInit(teamToShow, prospects);
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
    playersIcon = new ImageIcon(ClientController.getInstance().getGuiResource("icons/players18x18.png"));
    playerattributesIcon = new ImageIcon(ClientController.getInstance().getGuiResource("icons/playerattributes18x18.png"));
    playerstatsIcon = new ImageIcon(ClientController.getInstance().getGuiResource("icons/playerstats18x18.png"));
    contractsIcon = new ImageIcon(ClientController.getInstance().getGuiResource("icons/finances18x18.png"));
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
    panelPlayerContracts.setLayout(borderLayout2);
    jPlayerContractsTable.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        jPlayerContractsTable_mouseClicked(e);
      }
    });
    tabbedPanePlayers.addTab(ClientController.getInstance().getTranslation("team.playersByAttributes"), playerattributesIcon, panelPlayerAttributes, ClientController.getInstance().getTranslation("team.playersByAttributes"));
    panelPlayerAttributes.add(jScrollPane7, BorderLayout.CENTER);
    tabbedPanePlayers.addTab(ClientController.getInstance().getTranslation("team.playersByStats"), playerstatsIcon, panelPlayerStats, ClientController.getInstance().getTranslation("team.playersByStats"));
    tabbedPanePlayers.addTab(ClientController.getInstance().getTranslation("team.playersByContracts"), contractsIcon, panelPlayerContracts, ClientController.getInstance().getTranslation("team.playersByContracts"));
    panelPlayerStats.add(jScrollPane5, BorderLayout.CENTER);
    jScrollPane5.getViewport().add(jTotalPlayerStatsTable, null);
    jScrollPane7.getViewport().add(jPlayerAttributesTable, null);
    jScrollPane1.getViewport().add(jPlayerContractsTable, null);
    this.add(tabbedPanePlayers, BorderLayout.CENTER);
    panelPlayerContracts.add(jScrollPane1, BorderLayout.CENTER);
  }

  /**
   * Create the panelLeagueStandings and add it to the frame
   * 
   * @param teamToshow
   *          Team to show in this frame
   * @param prospects 
   */
  private void ihmInit(Team teamToshow, boolean prospects) {
    this.setTitleKey("title.players");
    this.teamToShow = teamToshow;
    this.prospects = prospects;
    displayTeam();
  }

  /** Display the team */
  private void displayTeam() {

    if (prospects) {
      tabbedPanePlayers.remove(panelPlayerStats);
      tabbedPanePlayers.remove(panelPlayerContracts);
    }

    this.jTotalPlayerStatsTable.setModel(new TMPlayerStats(teamToShow.getPlayersTotalStats()));
    jTotalPlayerStatsTable.setColumnSelectionAllowed(false);
    jTotalPlayerStatsTable.activateSorting();

    TableColumnModel tcm = jTotalPlayerStatsTable.getColumnModel();

    tcm.getColumn(TMPlayerStats.COLUMN_TEAM).setMinWidth(0);
    tcm.getColumn(TMPlayerStats.COLUMN_TEAM).setMaxWidth(0);
    tcm.getColumn(TMPlayerStats.COLUMN_RANK).setMaxWidth(30);
    tcm.getColumn(TMPlayerStats.COLUMN_POSITION).setMaxWidth(30);
    tcm.getColumn(TMPlayerStats.COLUMN_GOALS).setMaxWidth(30);
    tcm.getColumn(TMPlayerStats.COLUMN_ASSISTS).setMaxWidth(30);
    tcm.getColumn(TMPlayerStats.COLUMN_SCORERPOINTS).setMaxWidth(30);
    tcm.getColumn(TMPlayerStats.COLUMN_GAMESPLAYED).setMaxWidth(30);
    tcm.getColumn(TMPlayerStats.COLUMN_PENALTYMINUTES).setMaxWidth(40);

    DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
    dtcr.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

    tcm.getColumn(TMPlayerStats.COLUMN_RANK).setCellRenderer(dtcr);
    tcm.getColumn(TMPlayerStats.COLUMN_GOALS).setCellRenderer(dtcr);
    tcm.getColumn(TMPlayerStats.COLUMN_ASSISTS).setCellRenderer(dtcr);
    tcm.getColumn(TMPlayerStats.COLUMN_SCORERPOINTS).setCellRenderer(dtcr);
    tcm.getColumn(TMPlayerStats.COLUMN_GAMESPLAYED).setCellRenderer(dtcr);
    tcm.getColumn(TMPlayerStats.COLUMN_PENALTYMINUTES).setCellRenderer(dtcr);

    if (prospects) {
      this.jPlayerAttributesTable.setModel(new TMPlayerAttributes(teamToShow.getProspects()));
    } else {
      this.jPlayerAttributesTable.setModel(new TMPlayerAttributes(teamToShow.getPlayers()));
    }
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
    tcm.getColumn(TMPlayerAttributes.COLUMN_BIRTHDATE).setMaxWidth(80);

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

    this.jPlayerContractsTable.setModel(new TMPlayerContracts(teamToShow.getPlayers()));
    jPlayerContractsTable.setColumnSelectionAllowed(false);
    jPlayerContractsTable.activateSorting();

    tcm = jPlayerContractsTable.getColumnModel();

    tcm.getColumn(TMPlayerContracts.COLUMN_TEAM).setMinWidth(0);
    tcm.getColumn(TMPlayerContracts.COLUMN_TEAM).setMaxWidth(0);
    tcm.getColumn(TMPlayerContracts.COLUMN_RANK).setMaxWidth(30);
    tcm.getColumn(TMPlayerContracts.COLUMN_POSITION).setMaxWidth(30);
    tcm.getColumn(TMPlayerContracts.COLUMN_COSTS).setMaxWidth(120);
    tcm.getColumn(TMPlayerContracts.COLUMN_END_DATE).setMaxWidth(120);
    tcm.getColumn(TMPlayerContracts.COLUMN_TRANSFERLIST).setMaxWidth(30);

    dtcr = new DefaultTableCellRenderer();
    dtcr.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

    tcm.getColumn(TMPlayerContracts.COLUMN_RANK).setCellRenderer(dtcr);
    tcm.getColumn(TMPlayerContracts.COLUMN_COSTS).setCellRenderer(dtcr);

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

  void jPlayerContractsTable_mouseClicked(MouseEvent e) {
    if (e.getClickCount() > 1) {
      TMPlayerContracts ta = (TMPlayerContracts) jPlayerContractsTable.getModel();
      int row = jPlayerContractsTable.getSelectedRow();
      Player player = ta.getPlayer(row);

      ClientController.getInstance().getDesktop().showPanelPlayer(player);
    }
  }

}
