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
  
package org.icehockeymanager.ihm.clients.devgui.gui.gameday.text;

import java.awt.event.*;
import java.util.*;

import org.icehockeymanager.ihm.clients.devgui.controller.*;
import org.icehockeymanager.ihm.clients.devgui.gui.*;
import org.icehockeymanager.ihm.clients.devgui.gui.gameday.simple.*;
import org.icehockeymanager.ihm.clients.devgui.ihm.league.*;
import org.icehockeymanager.ihm.clients.devgui.ihm.match.*;
import org.icehockeymanager.ihm.game.league.events.*;
import org.icehockeymanager.ihm.game.match.*;
import org.icehockeymanager.ihm.game.match.textengine.*;
import org.icehockeymanager.ihm.game.match.textengine.data.*;
import org.icehockeymanager.ihm.game.user.*;

public class PanelTextGameDay extends PanelSimpleGameDay {


  protected TextMatchListener allListener = null;
  private static final long serialVersionUID = -8671688997683100312L;

  /**
   * Conscructs the frame, sets the user (=null) and desktop of this frame
   * @param desktop 
   * 
   * @param user
   *          User to show this frame for (mostly null)
   * @param gameDayEvent
   *          GameDay to show in this frame
   */
  public PanelTextGameDay(Desktop desktop, User user, GameDayMatchesEvent gameDayEvent) {
    super(user);
    this.desktop = desktop;
    try {
      initGUI();
      ihmInit(gameDayEvent);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  /**
   * Display the game Day
   * 
   * @param gameDayEvent
   *          GameDay to show in this frame
   */
  public void ihmInit(GameDayMatchesEvent gameDayEvent) {
    this.setTitleKey("menu.gameDay");
    this.gameDayEvent = gameDayEvent;
    this.lblLeague.setText(LeagueTools.getGameDayDescription(gameDayEvent));
    this.displayMatches();

    this.allListener = getAllMatchListener();

    this.currentRadioMatch = gameDayEvent.getMatch(this.getOwner());

    if (currentRadioMatch == null) {
      this.currentRadioMatch = gameDayEvent.getMatchesArray()[0];
    }

    updateRadio();
  }

  /**
   * Plays all matches
   * 
   * @param e
   *          Description of the Parameter
   */
  protected void cmdPlay_actionPerformed(ActionEvent e) {

    this.jMatchList.setEnabled(false);
    this.cmdPlay.setEnabled(false);
    this.radioMatches = new Hashtable<Match, StringBuffer>();

    Vector<Match> matches = gameDayEvent.getMatches();
    for (int i = 0; i < matches.size(); i++) {
      // Cast to TextMatch to add the TextMatchListener to the TextMatch not normal Match
      TextMatch tmp = (TextMatch) matches.get(i);
      tmp.addMatchListener(allListener);

      RadioTextMatchListener rml = new RadioTextMatchListener((TextMatch) tmp);
      tmp.addMatchListener(rml);

      radioMatches.put(tmp, new StringBuffer());
    }

    controller = new MatchController(gameDayEvent, ClientController.getInstance().getSettings().MATCHCONTROLLER_DELAY);

    controller.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        cmdPlay.setEnabled(false);
        cmdMoveOn.setEnabled(true);
      }
    });

    controller.start();

  }
  
  protected TextMatchListener getAllMatchListener() {
    return new TextMatchListener() {

      public void regularPeriodStarted() {

      }

      public void overtimePeriodStarted() {

      }

      public void goToOvertime() {

      }

      public void regularPeriodFinished() {

      }

      public void overtimePeriodFinished() {

      }

      public void gameFinished() {

      }

      public void scoreChange() {
        displayMatches();
      }

      public void sceneFinished() {
      }

      public void faceOff(MatchDataFaceOff matchDataFaceOff) {
       
      }

      public void situation(MatchDataSituation matchDataSituation) {
      }

    };
  }

  
  
  public class RadioTextMatchListener implements TextMatchListener {

    private TextMatch myMatch = null;

    public RadioTextMatchListener(TextMatch match) {
      this.myMatch = match;
    }

    public void regularPeriodStarted() {
      appendToRadio(myMatch, "");
      appendToRadio(myMatch, "This regular period starts right now. Have fun !");
      appendToRadio(myMatch, "");
    }

    public void overtimePeriodStarted() {
      appendToRadio(myMatch, "");
      appendToRadio(myMatch, "This overtime period starts right now. Have fun !");
      appendToRadio(myMatch, "");
    }

    public void goToOvertime() {
      appendToRadio(myMatch, "");
      appendToRadio(myMatch, "The regular game is over, we're heading to overtime !");
      appendToRadio(myMatch, "");
      if (currentRadioMatch.equals(myMatch) && cbStopForPeriodFinished.isSelected()) {
        haltGames();
      }
    }

    public void regularPeriodFinished() {
      appendToRadio(myMatch, "");
      appendToRadio(myMatch, "This regular period is finished !");
      appendToRadio(myMatch, "");
      if (currentRadioMatch.equals(myMatch) && cbStopForPeriodFinished.isSelected()) {
        haltGames();
      }
    }

    public void overtimePeriodFinished() {
      appendToRadio(myMatch, "");
      appendToRadio(myMatch, "This overtime period is finished !");
      appendToRadio(myMatch, "");
      if (currentRadioMatch.equals(myMatch) && cbStopForPeriodFinished.isSelected()) {
        haltGames();
      }
    }

    public void sceneFinished() {
      appendToRadio(myMatch, "");
      if (currentRadioMatch.equals(myMatch) && cbStopForSceneFinished.isSelected()) {
        haltGames();
      }
    }

    public void faceOff(MatchDataFaceOff ev) {
      appendToRadio(myMatch, ev.getPlayerA().getPlayerInfo().getLastName() + " meets " + ev.getPlayerB().getPlayerInfo().getLastName() + " at the faceoff.");
      appendToRadio(myMatch, "The Puck goes to " + ev.getPuckHolder().getPlayerInfo().getLastName());
      appendToRadio(myMatch, "Possession " + ev.getPuckHolder().getTeam().getTeamInfo().getTeamName());
      appendToRadio(myMatch, "");
    }

    public void situation(MatchDataSituation ev) {
      appendToRadio(myMatch, MatchTools.generateSituationRadioMessage(ev));
    }

    public void gameFinished() {
      appendToRadio(myMatch, "");
      appendToRadio(myMatch, "This game is finished !");
      appendToRadio(myMatch, "");
    }

    public void scoreChange() {
      updateRadioFacts("");
      appendToRadio(myMatch, "");
      if (currentRadioMatch.equals(myMatch) && cbStopForScoreChanges.isSelected()) {
        haltGames();
      }

    }

  }

  
  
}
