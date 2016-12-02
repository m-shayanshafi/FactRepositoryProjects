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
import org.icehockeymanager.ihm.game.user.*;

/**
 * The PanelStandings contains: - A panel that contains all leagueOwners on it
 * 
 * @author Bernhard von Gunten
 * @created December 29, 2001
 */
public class PanelStandings extends JIhmPanel {
  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 3256719567991486005L;

  /** Panel with all leagueOwners */
  PanelLeagueStandings panelLeagueStandings = null;

  /* Gui controls */
  BorderLayout borderLayout1 = new BorderLayout();

  ImageIcon standingsIcon = new ImageIcon();

  /**
   * Constructs the frame, sets the user, calls ihmInit
   * 
   * @param user
   *          User to show this frame for
   */
  public PanelStandings(User user) {
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
  }

  /** Create the panelLeagueStandings and add it to the frame */
  private void ihmInit() {
    this.setTitleKey("title.standings");
    panelLeagueStandings = new PanelLeagueStandings();
    this.add(panelLeagueStandings, BorderLayout.CENTER);
  }

}
