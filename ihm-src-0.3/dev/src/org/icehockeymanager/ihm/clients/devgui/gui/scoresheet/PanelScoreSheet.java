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
  
package org.icehockeymanager.ihm.clients.devgui.gui.scoresheet;

import java.awt.*;
import java.util.*;
import java.util.logging.*;

import javax.swing.*;

import org.icehockeymanager.ihm.clients.devgui.controller.*;
import org.icehockeymanager.ihm.clients.devgui.gui.lib.*;
import org.icehockeymanager.ihm.clients.devgui.ihm.match.*;
import org.icehockeymanager.ihm.game.match.*;
import org.icehockeymanager.ihm.clients.devgui.gui.lib.JIhmButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import org.icehockeymanager.ihm.game.user.*;
import org.icehockeymanager.ihm.lib.*;

/**
 * The PanelScoreSheet contain a panel that shows the scoresheet of a match.
 * 
 * @author Bernhard von Gunten
 * @created January 4, 2002
 */
public class PanelScoreSheet extends JIhmPanel implements IhmLogging {

  static final long serialVersionUID = 482702532803336487L;

  JScrollPane jScrollPane1 = new JScrollPane();

  JTextArea txtGoalsTelegramm = new JTextArea();

  JIhmLabel lblMatch = new JIhmLabel();

  private JIhmButton btnClose;

  ImageIcon ihmIcon = new ImageIcon();

  /**
   * Conscructs the frame, sets the user
   * 
   * @param user
   *          User to show this frame for (mostly null)
   * @param match
   *          Description of the Parameter
   */
  public PanelScoreSheet(User user, Match match) {
    super(user);
    try {
      initGUI();
      ihmInit(match);
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
  private void initGUI() throws Exception {
    this.setSize(500, 390);
    this.setLayout(null);
    jScrollPane1.setBounds(new Rectangle(11, 41, 463, 253));
    txtGoalsTelegramm.setEditable(false);
    txtGoalsTelegramm.setFont(new java.awt.Font("Monospaced", 0, 12));
    lblMatch.setFont(new java.awt.Font("Dialog", 1, 18));
    lblMatch.setText("Team vs. Teamg");
    lblMatch.setBounds(new Rectangle(12, 10, 451, 22));
    this.add(lblMatch, null);
    this.add(jScrollPane1, null);
    {
      btnClose = new JIhmButton();
      this.add(btnClose);
      btnClose.setText("ihm.close");
      btnClose.setMsgKey("ihm.close");
      btnClose.setBounds(29, 319, 92, 30);
      btnClose.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent evt) {
          closeDialog();
        }
      });
    }
    jScrollPane1.getViewport().add(txtGoalsTelegramm, null);
  }

  /**
   * Display a score sheet of a match
   * 
   * @param match
   *          Match to show
   */
  public void ihmInit(Match match) {
    this.lblMatch.setText(MatchTools.getMatchDescription(match));
    this.txtGoalsTelegramm.setText(createTelegramm(match));
    this.txtGoalsTelegramm.setCaretPosition(0);
  }

  private String createTelegramm(Match match) {
    String result = new String();

    result += ClientController.getInstance().getTranslation("ihm.arena") + match.getTeamHome().getArena().getName() + "\n";
    try {
      result += ClientController.getInstance().getTranslation("ihm.spectactors") + Tools.intToString(match.getSpectators()) + "\n\n";
    } catch (Exception err) {
      logger.log(Level.SEVERE, "intToString failed!", err);
    }

    result += ClientController.getInstance().getTranslation("ihm.goals") + "\n\n";

    Vector<MatchDataGoal> goals = match.getScoreSheet().getGoals();

    int gh = 0;
    int ga = 0;
    for (int i = 0; i < goals.size(); i++) {
      MatchDataGoal goal = goals.get(i);
      if (goal.getTeam().equals(match.getTeamHome())) {
        gh++;
      } else {
        ga++;
      }
      String timeS = MatchTools.getMatchTime(goal.getTime());
      int tl = timeS.length();
      String zeile = Tools.lPad(MatchTools.getMatchTime(goal.getTime()), 2) + Tools.lPad(String.valueOf(gh), 3) + " :" + Tools.lPad(String.valueOf(ga), 3);
      zeile += " " + goal.getPlayer().getPlayerInfo().getPlayerName() + "\n";
      if (goal.getAssist1() != null) {
        zeile += Tools.lPad("", 10 + tl) + "(" + goal.getAssist1().getPlayerInfo().getPlayerName();
      }

      if (goal.getAssist2() != null) {
        zeile += " / " + goal.getAssist2().getPlayerInfo().getPlayerName() + ")" + "\n" + "\n";
      }
      result += zeile;
    }

    return result;

  }

  private void closeDialog() {
    ClientController.getInstance().getDesktop().closeDialog();
  }

}
