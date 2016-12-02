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
import java.util.*;

import javax.swing.*;

import org.icehockeymanager.ihm.clients.devgui.controller.*;
import org.icehockeymanager.ihm.clients.devgui.gui.lib.*;
import org.icehockeymanager.ihm.game.league.*;
import org.icehockeymanager.ihm.game.league.helper.*;

/**
 * The PanelLeagueStandingsLeague contains: - A panel that contains all
 * leagueElementGroup panels on it
 * 
 * @author Bernhard von Gunten
 * @created December 29, 2001
 */
public class PanelLeagueStandingsLeague extends JIhmPanel {
  static final long serialVersionUID = -2064282334150478837L;

  BorderLayout borderLayout1 = new BorderLayout();

  JTabbedPane rootTab = new JTabbedPane();

  /**
   * Constructs the panel and calls ihmInit()
   * 
   * @param league
   *          League to show in this panel
   */
  public PanelLeagueStandingsLeague(League league) {
    try {
      jbInit();
      ihmInit(league);
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
   * Gets all LeagueElementGroups from the league and build one
   * LeagueElementGroup panel for each of them and adds it to the tabbed view.
   * 
   * @param league
   *          League to show in this panel
   */
  public void ihmInit(League league) {

    Vector<LeagueElementGroup> groups = league.getLeagueElementGroups();

    for (int i = 0; i < groups.size(); i++) {
      LeagueElementGroup tmp = groups.get(i);
      String title = new String();
      try {
        title = ClientController.getInstance().getTranslation(tmp.getNameKey());
      } catch (Exception err) {
        System.out.println(tmp.getNameKey());
      }
      rootTab.add(new PanelLeagueStandingsLeagueGroup(tmp), title);
    }
  }

}
