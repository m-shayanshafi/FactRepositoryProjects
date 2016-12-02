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

import org.icehockeymanager.ihm.clients.devgui.gui.lib.*;
import org.icehockeymanager.ihm.game.league.*;

/**
 * The PanelLeagueStandingsLeagueOwner contains: - A panel that contains all
 * league panels on it
 * 
 * @author Bernhard von Gunten
 * @created December 29, 2001
 */
public class PanelLeagueStandingsLeagueOwner extends JIhmPanel {
  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 3546645416792764466L;

  BorderLayout borderLayout1 = new BorderLayout();

  JTabbedPane rootTab = new JTabbedPane();

  /**
   * Constructs the panel and calls ihmInit()
   * 
   * @param leagueOwner
   *          LeagueOwner to show in this panel.
   */
  public PanelLeagueStandingsLeagueOwner(LeagueOwner leagueOwner) {
    try {
      jbInit();
      ihmInit(leagueOwner);
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
   * Gets all League from the leagueOwner and build one League panel for each of
   * them and adds it to the tabbed view.
   * 
   * @param leagueOwner
   *          LeagueOwner to show in this panel.
   */
  public void ihmInit(LeagueOwner leagueOwner) {

    League[] leagues = leagueOwner.getLeagues();

    for (int i = 0; i < leagues.length; i++) {
      rootTab.add(new PanelLeagueStandingsLeague(leagues[i]), leagues[i].getName());
    }

  }

}
