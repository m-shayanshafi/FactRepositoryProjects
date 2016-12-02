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
  
package org.icehockeymanager.ihm.clients.devgui.gui.teamstats;

import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

import org.icehockeymanager.ihm.clients.devgui.controller.*;
import org.icehockeymanager.ihm.clients.devgui.gui.lib.*;
import org.icehockeymanager.ihm.clients.devgui.ihm.league.*;
import org.icehockeymanager.ihm.clients.devgui.ihm.team.*;
import org.icehockeymanager.ihm.game.*;
import org.icehockeymanager.ihm.game.league.*;
import org.icehockeymanager.ihm.game.league.helper.*;
import org.icehockeymanager.ihm.game.team.*;
import org.icehockeymanager.ihm.game.user.*;

/**
 * A panel with a team stats grid including filtering.
 * 
 * @author Bernhard von Gunten
 * @created December 29, 2001
 */
public class PanelTeamStats extends JIhmPanel {
  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 4050769299037893170L;

  /** Possible owners of teams */
  // TODO: Cleanup vector
  private Vector<TeamStatsOwner> teamStatsOwners;

  /* Gui controls */
  BorderLayout borderLayout1 = new BorderLayout();

  JIhmPanel panelButtons = new JIhmPanel();

  JScrollPane jScrollPane1 = new JScrollPane();

  JIhmPanel JIhmPanel1 = new JIhmPanel();

  JComboBox cbTeamStatsOwners = new JComboBox();

  JIhmButton cmdFilter = new JIhmButton();

  JIhmTable jTeamStatsTable = new JIhmTable();

  JIhmLabel lblFilter = new JIhmLabel();

  ImageIcon teamstatsIcon = new ImageIcon();

  ImageIcon generalfilterIcon = new ImageIcon();

  /**
   * Constructs the frame, sets the user, calls ihmInit
   * 
   * @param user
   *          User to show this frame for
   */
  public PanelTeamStats(User user) {
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
    generalfilterIcon = new ImageIcon(ClientController.getInstance().getGuiResource("icons/generalfilter18x18.png"));
    cmdFilter.setIcon(generalfilterIcon);
    cmdFilter.setText("");
    cmdFilter.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        cmdFilter_actionPerformed(e);
      }
    });
    cbTeamStatsOwners.setToolTipText("");
    lblFilter.setText("ihm.filter");
    lblFilter.setMsgKey("ihm.filter");
    jTeamStatsTable.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        jTeamStatsTable_mouseClicked(e);
      }
    });
    this.add(panelButtons, BorderLayout.SOUTH);
    this.add(jScrollPane1, BorderLayout.CENTER);
    jScrollPane1.getViewport().add(jTeamStatsTable, null);
    this.add(JIhmPanel1, BorderLayout.NORTH);
    JIhmPanel1.add(lblFilter, null);
    JIhmPanel1.add(cbTeamStatsOwners, null);
    JIhmPanel1.add(cmdFilter, null);
  }

  /** Create the panelLeagueStandings and add it to the frame */
  private void ihmInit() {
    this.setTitleKey("title.teamsByStats");

    // Possible owners of the teams :
    teamStatsOwners = new Vector<TeamStatsOwner>();
    cbTeamStatsOwners.removeAllItems();

    // Scenario
    teamStatsOwners.add(GameController.getInstance().getScenario());
    cbTeamStatsOwners.addItem(ClientController.getInstance().getTranslation("ihm.allTeams"));

    LeagueOwner[] leagueOwners = GameController.getInstance().getScenario().getLeagueOwners();

    for (int i = 0; i < leagueOwners.length; i++) {
      League[] leagues = leagueOwners[i].getLeagues();
      for (int n = 0; n < leagues.length; n++) {
        teamStatsOwners.add(leagues[n]);
        cbTeamStatsOwners.addItem(leagues[n].getName());
        Vector<LeagueElementGroup> leagueElementGroups = leagues[n].getLeagueElementGroups();
        for (int o = 0; o < leagueElementGroups.size(); o++) {
          LeagueElementGroup leg = leagueElementGroups.get(o);
          Vector<LeagueElement> leagueElements = leg.getLeagueElements();

          if (leagueElements.size() > 1) {
            teamStatsOwners.add(leg);
            cbTeamStatsOwners.addItem(LeagueTools.getLeagueElementGroupDescription(leg));
          }
          for (int p = 0; p < leagueElements.size(); p++) {
            LeagueElement le = leagueElements.get(p);
            teamStatsOwners.add(le);
            cbTeamStatsOwners.addItem(LeagueTools.getLeagueElementDescription(le));
          }

        }
      }
    }

    displayTable();

  }

  /** Displays all the user informations */
  private void displayTable() {

    // Different team owners ...
    TeamStatsOwner filter = teamStatsOwners.get(cbTeamStatsOwners.getSelectedIndex());
    this.jTeamStatsTable.setModel(new TMTeamStats(filter.getTeamStats()));

    jTeamStatsTable.setColumnSelectionAllowed(false);
    jTeamStatsTable.activateSorting();

    TableColumnModel tmp = jTeamStatsTable.getColumnModel();

    tmp.getColumn(TMTeamStats.COLUMN_OWNER).setMinWidth(0);
    tmp.getColumn(TMTeamStats.COLUMN_OWNER).setMaxWidth(0);
    tmp.getColumn(TMTeamStats.COLUMN_RANK).setMaxWidth(30);
    tmp.getColumn(TMTeamStats.COLUMN_GAMESWON).setMaxWidth(30);
    tmp.getColumn(TMTeamStats.COLUMN_GAMESPLAYED).setMaxWidth(30);
    tmp.getColumn(TMTeamStats.COLUMN_GAMESTIED).setMaxWidth(30);
    tmp.getColumn(TMTeamStats.COLUMN_GAMESLOST).setMaxWidth(30);
    tmp.getColumn(TMTeamStats.COLUMN_GOALSFOR).setMaxWidth(40);
    tmp.getColumn(TMTeamStats.COLUMN_GOALSAGAINST).setMaxWidth(40);
    tmp.getColumn(TMTeamStats.COLUMN_GOALSDIFFERENCE).setMaxWidth(30);
    tmp.getColumn(TMTeamStats.COLUMN_POINTS).setMaxWidth(30);
    tmp.getColumn(TMTeamStats.COLUMN_PENALTYMINUTES).setMaxWidth(30);

    DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
    dtcr.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

    tmp.getColumn(TMTeamStats.COLUMN_RANK).setCellRenderer(dtcr);
    tmp.getColumn(TMTeamStats.COLUMN_GAMESPLAYED).setCellRenderer(dtcr);
    tmp.getColumn(TMTeamStats.COLUMN_GAMESWON).setCellRenderer(dtcr);
    tmp.getColumn(TMTeamStats.COLUMN_GAMESTIED).setCellRenderer(dtcr);
    tmp.getColumn(TMTeamStats.COLUMN_GAMESLOST).setCellRenderer(dtcr);
    tmp.getColumn(TMTeamStats.COLUMN_GOALSFOR).setCellRenderer(dtcr);
    tmp.getColumn(TMTeamStats.COLUMN_GOALSAGAINST).setCellRenderer(dtcr);
    tmp.getColumn(TMTeamStats.COLUMN_GOALSDIFFERENCE).setCellRenderer(dtcr);
    tmp.getColumn(TMTeamStats.COLUMN_POINTS).setCellRenderer(dtcr);
    tmp.getColumn(TMTeamStats.COLUMN_PENALTYMINUTES).setCellRenderer(dtcr);
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
   * Show selected team in a new frame.
   * 
   * @param e
   *          Source event
   */
  void jTeamStatsTable_mouseClicked(MouseEvent e) {
    if (e.getClickCount() > 1) {
      TMTeamStats ta = (TMTeamStats) jTeamStatsTable.getModel();
      int row = jTeamStatsTable.getSelectedRow();
      Team team = ta.getTeam(row);

      ClientController.getInstance().getDesktop().showPanelTeam(team);
    }
  }

}
