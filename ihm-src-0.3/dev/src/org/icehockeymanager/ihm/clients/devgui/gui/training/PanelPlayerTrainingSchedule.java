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
import java.awt.event.*;

import org.icehockeymanager.ihm.clients.devgui.gui.lib.*;
import org.icehockeymanager.ihm.game.player.*;
import org.icehockeymanager.ihm.game.training.*;

/**
 * The PanelPlayerTrainingSchedule contains a interface to change the training
 * units for a player
 * 
 * @author Bernhard von Gunten
 * @created July 9, 2002
 */
public class PanelPlayerTrainingSchedule extends JIhmPanel {
  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 3258409547360974644L;

  Player playerToShow = null;

  PanelTrainingSchedule panelTrainingSchedule = null;

  boolean teamSchedule = true;

  BorderLayout borderLayout1 = new BorderLayout();

  JIhmPanel emptyschedule = new JIhmPanel();

  JIhmPanel panelCommands = new JIhmPanel();

  JIhmButton cmdTeam = new JIhmButton();

  JIhmButton cmdOwn = new JIhmButton();

  JIhmLabel lblTeamTraining = new JIhmLabel();

  BorderLayout borderLayout2 = new BorderLayout();

  /**
   * Creates the panel, saves the leagueElement and calls ihmInit
   * 
   * @param playerToShow
   *          Description of the Parameter
   */
  public PanelPlayerTrainingSchedule(Player playerToShow) {
    try {
      this.playerToShow = playerToShow;
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
    panelCommands.setLayout(null);
    panelCommands.setMaximumSize(new Dimension(50, 50));
    panelCommands.setMinimumSize(new Dimension(50, 50));
    panelCommands.setPreferredSize(new Dimension(50, 50));
    cmdTeam.setText("training.teamTraining");
    cmdTeam.setMsgKey("training.teamTraining");
    cmdTeam.setBounds(new Rectangle(31, 9, 158, 25));
    cmdTeam.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        cmdTeam_actionPerformed(e);
      }
    });
    cmdOwn.setBounds(new Rectangle(202, 9, 158, 25));
    cmdOwn.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        cmdOwn_actionPerformed(e);
      }
    });
    cmdOwn.setText("training.ownTraining");
    cmdOwn.setMsgKey("training.ownTraining");
    emptyschedule.setLayout(borderLayout2);
    lblTeamTraining.setFont(new java.awt.Font("Dialog", 1, 18));
    lblTeamTraining.setText("training.teamTraining");
    lblTeamTraining.setMsgKey("training.teamTraining");
    this.add(panelCommands, BorderLayout.SOUTH);
    panelCommands.add(cmdTeam, null);
    panelCommands.add(cmdOwn, null);
    emptyschedule.add(lblTeamTraining, BorderLayout.CENTER);
  }

  /** Calls displayTable that shows the table */
  private void ihmInit() {
    this.setTitleKey("title.training");
    displayTrainingSchedule();
  }

  /** Displays the player specific TrainingSchedule if needed. */
  private void displayTrainingSchedule() {
    if (playerToShow.getTrainingSchedule() == null) {
      this.add(emptyschedule, BorderLayout.CENTER);
      this.teamSchedule = true;
    } else {
      this.panelTrainingSchedule = new PanelTrainingSchedule(playerToShow.getTrainingSchedule());
      panelTrainingSchedule.updateTranslation();
      this.add(panelTrainingSchedule, BorderLayout.CENTER);
      this.teamSchedule = false;
    }
    this.validate();
    this.repaint();
  }

  /**
   * Change training to team trainingSchedule.
   * 
   * @param e
   *          Description of the Parameter
   */
  void cmdTeam_actionPerformed(ActionEvent e) {
    if (!this.teamSchedule) {
      this.remove(panelTrainingSchedule);
      playerToShow.setTrainingSchedule(null);
      displayTrainingSchedule();
    }
  }

  /**
   * Create own trainingSchedule.
   * 
   * @param e
   *          Description of the Parameter
   */
  void cmdOwn_actionPerformed(ActionEvent e) {
    if (this.teamSchedule) {
      this.remove(emptyschedule);
      playerToShow.setTrainingSchedule(new TrainingSchedule());
      displayTrainingSchedule();
    }
  }

}
