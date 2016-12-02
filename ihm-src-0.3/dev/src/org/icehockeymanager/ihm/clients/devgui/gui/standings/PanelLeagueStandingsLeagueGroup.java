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

import java.util.*;

import java.awt.*;
import javax.swing.*;

import org.icehockeymanager.ihm.clients.devgui.gui.lib.*;
import org.icehockeymanager.ihm.clients.devgui.ihm.league.*;
import org.icehockeymanager.ihm.game.league.helper.*;

/**
 * The PanelLeagueStandingsLeagueGroup contains: - A panel that contains all
 * leagueElement panels on it
 * 
 * @author Bernhard von Gunten
 * @created December 29, 2001
 */
public class PanelLeagueStandingsLeagueGroup extends JIhmPanel {
  static final long serialVersionUID = 6832123084896394841L;

  BorderLayout borderLayout1 = new BorderLayout();

  JTabbedPane rootTab = new JTabbedPane();

  /**
   * Constructs the panel and calls ihmInit()
   * 
   * @param leagueElementGroup
   *          League Element group to show in this panel
   */
  public PanelLeagueStandingsLeagueGroup(LeagueElementGroup leagueElementGroup) {
    try {
      jbInit();
      ihmInit(leagueElementGroup);
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
    this.add(rootTab, BorderLayout.CENTER);
  }

  /**
   * Gets all leaugeElements out of the leagueElementGroup and adds a Table oder
   * Playoff Panel for each of them on the tabbed view.
   * 
   * @param leagueElementGroup
   *          League Element group to show in this panel
   */
  public void ihmInit(LeagueElementGroup leagueElementGroup) {

    Vector<LeagueElement> elements = leagueElementGroup.getLeagueElements();

    for (int i = 0; i < elements.size(); i++) {
      LeagueElement tmp = elements.get(i);
      LeagueStandings stnd = tmp.getLeagueStandings();

      // Create table view
      if (stnd instanceof LeagueStandingsTable) {
        rootTab.add(new PanelLeagueStandingsTable(tmp), LeagueTools.getShortLeagueElementDescription(tmp));
      }

      // Create playoff view
      if (stnd instanceof LeagueStandingsPlayoffs) {
        rootTab.add(new PanelLeagueStandingsPlayoffs(tmp), LeagueTools.getShortLeagueElementDescription(tmp));
      }
    }

  }

}
