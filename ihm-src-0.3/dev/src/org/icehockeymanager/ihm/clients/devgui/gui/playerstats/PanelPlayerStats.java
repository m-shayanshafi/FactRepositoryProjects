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
  
package org.icehockeymanager.ihm.clients.devgui.gui.playerstats;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.table.*;

import org.icehockeymanager.ihm.clients.devgui.controller.*;
import org.icehockeymanager.ihm.clients.devgui.gui.lib.*;
import org.icehockeymanager.ihm.clients.devgui.ihm.league.*;
import org.icehockeymanager.ihm.clients.devgui.ihm.player.*;
import org.icehockeymanager.ihm.game.*;
import org.icehockeymanager.ihm.game.league.*;
import org.icehockeymanager.ihm.game.league.helper.*;
import org.icehockeymanager.ihm.game.player.*;
import org.icehockeymanager.ihm.game.user.*;

/**
 * A panel with a player stats grid including filtering.
 * 
 * @author Bernhard von Gunten
 * @created December 29, 2001
 */
public class PanelPlayerStats extends JIhmPanel {
  static final long serialVersionUID = -4472265289007338770L;

  /** Possible owners of players */
  private Vector<PlayerStatsOwner> playerStatsOwners;

  /* Gui controls */
  BorderLayout borderLayout1 = new BorderLayout();

  JIhmPanel panelButtons = new JIhmPanel();

  JScrollPane jScrollPane1 = new JScrollPane();

  JIhmPanel JIhmPanel1 = new JIhmPanel();

  JComboBox cbPlayerStatsOwners = new JComboBox();

  JIhmButton cmdFilter = new JIhmButton();

  JIhmTable jPlayerStatsTable = new JIhmTable();

  JIhmLabel lblFilter = new JIhmLabel();

  ImageIcon playerstatsIcon = new ImageIcon();

  ImageIcon playerfilterIcon = new ImageIcon();

  /**
   * Constructs the frame, sets the user, calls ihmInit
   * 
   * @param user
   *          User to show this frame for
   */
  public PanelPlayerStats(User user) {
    super(user);
    try {
      jbInit();
      ihmInit();
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
    this.setSize(500, 400);
    this.setLayout(borderLayout1);
    panelButtons.setMaximumSize(new Dimension(50, 50));
    panelButtons.setMinimumSize(new Dimension(50, 50));
    playerfilterIcon = new ImageIcon(ClientController.getInstance().getGuiResource("icons/filterplayer18x18.png"));
    cmdFilter.setIcon(playerfilterIcon);
    cmdFilter.setText("");
    cmdFilter.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        cmdFilter_actionPerformed(e);
      }
    });
    cbPlayerStatsOwners.setToolTipText("");
    lblFilter.setText("ihm.filter");
    lblFilter.setMsgKey("ihm.filter");
    jPlayerStatsTable.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        jPlayerStatsTable_mouseClicked(e);
      }
    });
    this.add(panelButtons, BorderLayout.SOUTH);
    this.add(jScrollPane1, BorderLayout.CENTER);
    jScrollPane1.getViewport().add(jPlayerStatsTable, null);
    this.add(JIhmPanel1, BorderLayout.NORTH);
    JIhmPanel1.add(lblFilter, null);
    JIhmPanel1.add(cbPlayerStatsOwners, null);
    JIhmPanel1.add(cmdFilter, null);
  }

  /** Create the panelLeagueStandings and add it to the frame */
  private void ihmInit() {
    this.setTitleKey("title.playersByStats");
    // Possible owners of the players :
    playerStatsOwners = new Vector<PlayerStatsOwner>();
    cbPlayerStatsOwners.removeAllItems();

    // Scenario
    playerStatsOwners.add(GameController.getInstance().getScenario());
    cbPlayerStatsOwners.addItem(ClientController.getInstance().getTranslation("ihm.allPlayers"));

    // Free players
    playerStatsOwners.add(GameController.getInstance().getScenario());
    cbPlayerStatsOwners.addItem(ClientController.getInstance().getTranslation("ihm.freePlayers"));

    LeagueOwner[] leagueOwners = GameController.getInstance().getScenario().getLeagueOwners();

    for (int i = 0; i < leagueOwners.length; i++) {
      League[] leagues = leagueOwners[i].getLeagues();
      for (int n = 0; n < leagues.length; n++) {
        playerStatsOwners.add(leagues[n]);
        cbPlayerStatsOwners.addItem(leagues[n].getName());
        Vector<LeagueElementGroup> leagueElementGroups = leagues[n].getLeagueElementGroups();
        for (int o = 0; o < leagueElementGroups.size(); o++) {
          LeagueElementGroup leg = leagueElementGroups.get(o);
          Vector<LeagueElement> leagueElements = leg.getLeagueElements();

          if (leagueElements.size() > 1) {
            playerStatsOwners.add(leg);
            cbPlayerStatsOwners.addItem(LeagueTools.getLeagueElementGroupDescription(leg));

          }
          for (int p = 0; p < leagueElements.size(); p++) {
            LeagueElement le = leagueElements.get(p);
            playerStatsOwners.add(le);
            cbPlayerStatsOwners.addItem(LeagueTools.getLeagueElementDescription(le));
          }

        }
      }
    }

    displayTable();

  }

  /** Displays all the user informations */
  private void displayTable() {

    // Different player owners ...
    PlayerStatsOwner filter = playerStatsOwners.get(cbPlayerStatsOwners.getSelectedIndex());

    if (filter instanceof org.icehockeymanager.ihm.game.scenario.Scenario) {
      if (cbPlayerStatsOwners.getSelectedIndex() == 0) {
        this.jPlayerStatsTable.setModel(new TMPlayerStats(GameController.getInstance().getScenario().getPlayersTotalStats()));
      } else {
        this.jPlayerStatsTable.setModel(new TMPlayerStats(GameController.getInstance().getScenario().getFreePlayersTotalStats()));
      }
    } else {
      this.jPlayerStatsTable.setModel(new TMPlayerStats(filter.getPlayerStats()));
    }

    jPlayerStatsTable.setColumnSelectionAllowed(false);
    jPlayerStatsTable.activateSorting();

    TableColumnModel tcm = jPlayerStatsTable.getColumnModel();

    tcm.getColumn(TMPlayerStats.COLUMN_OWNER).setMinWidth(0);
    tcm.getColumn(TMPlayerStats.COLUMN_OWNER).setMaxWidth(0);
    tcm.getColumn(TMPlayerStats.COLUMN_RANK).setMaxWidth(30);
    tcm.getColumn(TMPlayerStats.COLUMN_GOALS).setMaxWidth(30);
    tcm.getColumn(TMPlayerStats.COLUMN_ASSISTS).setMaxWidth(30);
    tcm.getColumn(TMPlayerStats.COLUMN_POSITION).setMaxWidth(30);
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
  }

  /**
   * Reset table
   * 
   * @param e
   *          Source event
   */
  void cmdFilter_actionPerformed(ActionEvent e) {
    displayTable();
  }

  /**
   * Show selected player in a new frame.
   * 
   * @param e
   *          Source event
   */
  void jPlayerStatsTable_mouseClicked(MouseEvent e) {
    if (e.getClickCount() > 1) {
      TMPlayerStats ta = (TMPlayerStats) jPlayerStatsTable.getModel();
      int row = jPlayerStatsTable.getSelectedRow();
      Player player = ta.getPlayer(row);

      ClientController.getInstance().getDesktop().showPanelPlayer(player);
    }
  }

}
