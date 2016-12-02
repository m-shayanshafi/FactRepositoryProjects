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
import org.icehockeymanager.ihm.game.*;
import org.icehockeymanager.ihm.game.league.*;

/**
 * The PanelLeagueStandings contains: - A panel that contains all leagueOwner
 * panels on it
 * 
 * @author Bernhard von Gunten
 * @created December 29, 2001
 */
public class PanelLeagueStandings extends JIhmPanel {
  static final long serialVersionUID = 8517718372249055806L;

  BorderLayout borderLayout1 = new BorderLayout();

  JTabbedPane rootTab = new JTabbedPane();

  /** Constructs the panel and calls ihmInit() */
  public PanelLeagueStandings() {
    try {
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
  private void jbInit() throws Exception {
    this.setLayout(borderLayout1);
    this.add(rootTab, BorderLayout.CENTER);
  }

  /**
   * Gets all LeagueOwner from the controller and build one LeagueOwner panel
   * for each of them and adds it to the tabbed view.
   */
  public void ihmInit() {
    LeagueOwner[] leagueOwners = GameController.getInstance().getScenario().getLeagueOwners();

    this.remove(rootTab);
    rootTab = new JTabbedPane();
    this.add(rootTab, BorderLayout.CENTER);

    for (int i = 0; i < leagueOwners.length; i++) {
      rootTab.add(new PanelLeagueStandingsLeagueOwner(leagueOwners[i]), leagueOwners[i].getName());
    }

  }

}
