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
  
package org.icehockeymanager.ihm.clients.devgui.gui.training;

import java.awt.*;

import javax.swing.*;

import org.icehockeymanager.ihm.clients.devgui.gui.lib.*;
import org.icehockeymanager.ihm.game.team.*;
import org.icehockeymanager.ihm.game.user.*;

/**
 * A panel to show the team with stats and players.
 * 
 * @author Bernhard von Gunten
 * @created December 29, 2001
 */
public class PanelTraining extends JIhmPanel {
  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 3618134567597192243L;

  /** Team that is shown in this frame */
  private Team teamToShow = null;

  /* Gui controls */
  BorderLayout borderLayout1 = new BorderLayout();

  ImageIcon trainingIcon = new ImageIcon();

  /**
   * Constructs the frame, sets the user, calls ihmInit
   * 
   * @param user
   *          User to show this frame for
   * @param teamToShow
   *          Team to show in this frame
   */
  public PanelTraining(User user, Team teamToShow) {
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
    this.setPreferredSize(new java.awt.Dimension(664, 482));
    this.setBounds(0, 0, 664, 482);
  }

  /**
   * Create the panelLeagueStandings and add it to the frame
   * 
   * @param teamToshow
   *          Team to show in this frame
   */
  private void ihmInit(Team teamToshow) {
    this.setTitleKey("title.training");
    this.teamToShow = teamToshow;
    displayTraining();
  }

  /** Display the training schedule */
  private void displayTraining() {
    this.add(new PanelTrainingSchedule(teamToShow.getTrainingSchedule()), BorderLayout.CENTER);
  }

}
