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
  
package org.icehockeymanager.ihm.clients.devgui.gui.standings;

import java.awt.*;
import javax.swing.*;

import org.icehockeymanager.ihm.clients.devgui.controller.*;
import org.icehockeymanager.ihm.clients.devgui.gui.lib.*;
import org.icehockeymanager.ihm.game.league.helper.*;
import org.icehockeymanager.ihm.lib.*;

/**
 * The PanelLeagueStandingsPlayoffs contains: - A panel that displays playoffs
 * of a leagueElement.
 * 
 * @author Bernhard von Gunten
 * @created December 29, 2001
 */
public class PanelLeagueStandingsPlayoffs extends JIhmPanel {
  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 3256441404401137204L;

  /** LeagueElement displayed on this panel */
  LeagueElement leagueElement = null;

  BorderLayout borderLayout1 = new BorderLayout();

  JScrollPane scrollPane = new JScrollPane();

  JTextArea txtPlayoffs = new JTextArea();

  /**
   * Creates the panel, saves the leagueElement and calls ihmInit
   * 
   * @param leagueElement
   *          LeagueElement to show in this panel
   */
  public PanelLeagueStandingsPlayoffs(LeagueElement leagueElement) {
    try {
      this.leagueElement = leagueElement;
      jbInit();
      ihmInit();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   * JBuilder stuff
   * 
   * @exception Exception
   *              Exception
   */
  void jbInit() throws Exception {
    this.setLayout(borderLayout1);
    txtPlayoffs.setText("jTextArea1");
    txtPlayoffs.setFont(new java.awt.Font("Monospaced", 0, 12));
    this.add(scrollPane, BorderLayout.CENTER);
    scrollPane.getViewport().add(txtPlayoffs, null);
  }

  /** Calls displayPlayoffs */
  private void ihmInit() {
    displayPlayoffs();
  }

  /** Shows playoffs of the leagueElement */
  public void displayPlayoffs() {
    if (leagueElement.isAnnounced()) {
      LeagueStandingsPlayoffs lLeagueStandingsPlayoffs = (LeagueStandingsPlayoffs) leagueElement.getLeagueStandings();
      PlayoffCompetition[] tmp = lLeagueStandingsPlayoffs.getPlayoffCompetitions();
      String result = "";
      for (int i = 0; i < tmp.length; i++) {
        result += Tools.rPad(tmp[i].getTeamHome().getTeamInfo().getTeamName(), 20) + "vs. " + Tools.rPad(tmp[i].getTeamAway().getTeamInfo().getTeamName(), 20) + tmp[i].getHomeWins() + ":" + tmp[i].getAwayWins() + "\n";
      }
      this.txtPlayoffs.setText(result);
    } else {
      this.txtPlayoffs.setText(ClientController.getInstance().getTranslation("standings.notAnnounced"));
    }
  }

  /** Overrides function, for refreshing the playoffs if needed */
  public void ihmUpdate() {
    // super.ihmUpdate();
    displayPlayoffs();
  }
}
