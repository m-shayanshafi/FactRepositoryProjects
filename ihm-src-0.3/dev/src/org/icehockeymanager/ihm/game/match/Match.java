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
  
package org.icehockeymanager.ihm.game.match;

import java.io.*;
import java.util.*;

import org.icehockeymanager.ihm.game.league.*;
import org.icehockeymanager.ihm.game.league.helper.*;
import org.icehockeymanager.ihm.game.team.*;
import org.icehockeymanager.ihm.game.user.*;

/**
 * The Match class contains all informations about a match. This includes the
 * scoresheet (MatchSheet), but also "current" data (MatchSnapshot) used while
 * the game is still running.
 * <p>
 * The/A MatchController calls "moveOn()" until the game is finished. The
 * function informs Listeners to the match about every change and/or plays.
 * 
 * @author Bernhard von Gunten & Arik Dasen
 * @created January 2005
 */
public abstract class Match implements Serializable {

  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 3689631393609232947L;

  /** Our devoted match listeners :-) */
  protected Vector<MatchListener> listener;


  /** Is match finished */
  protected boolean matchFinished = false;

  /** Has overtime been played */
  protected boolean overtimePlayed = false;

  /** The rules of this match */
  protected Rules rules = null;

  /** Spectators */
  protected int spectators;

  /** Scoresheet */
  protected MatchSheet scoreSheet = null;

  /** LeagueEleement */
  private LeagueElement leagueElement;

  /** Home team */
  protected Team homeTeam;

  /** Away team */
  protected Team awayTeam;

  public Match() {
    // do nothing
  }
  
  
  /**
   * Constructor for the Match object
   * 
   * @param leagueElement
   *          LeagueElement of this match
   * @param teamHome
   *          Home team of this match
   * @param teamAway
   *          Away team of this match
   * @param rules
   *          Rules of this match
   */
  public Match(LeagueElement leagueElement, Team teamHome, Team teamAway, Rules rules) {
    this.leagueElement = leagueElement;
    this.homeTeam = teamHome;
    this.awayTeam = teamAway;
    this.rules = rules;
    this.scoreSheet = new MatchSheet(this);
  }

  /**
   * Returns boolean if user passed by is involved in this match
   * 
   * @param user
   *          User looking for
   * @return The userInvolved value
   */
  public boolean isUserInvolved(User user) {
    if (homeTeam.equals(user.getTeam())) {
      return true;
    } else if (awayTeam.equals(user.getTeam())) {
      return true;
    }
    return false;
  }

  /**
   * Returns if teamHome is winner of this match.
   * 
   * @return Boolean if teamHome is winner
   */
  public boolean _isTeamHomeWinner() {
    return scoreSheet.isHomeTeamWinner();
  }

  /**
   * Gets the teamWinner attribute of the Match object
   * 
   * @param team
   *          Team to look if it's the winner of this match.
   * @return The teamWinner value
   */
  public boolean isTeamWinner(Team team) {
    Team winner = null;
    if (_isTeamHomeWinner()) {
      winner = homeTeam;
    } else {
      winner = awayTeam;
    }
    if (team.equals(winner)) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * Gets the gameTied attribute of the Match object
   * 
   * @return The gameTied value
   */
  public boolean isGameTied() {
    return scoreSheet.isGameTied();
  }

  /**
   * Gets the leagueElement attribute of the Match object
   * 
   * @return The leagueElement value
   */
  public LeagueElement getLeagueElement() {
    return leagueElement;
  }

  /**
   * Gets the teamHome attribute of the Match object
   * 
   * @return The teamHome value
   */
  public Team getTeamHome() {
    return homeTeam;
  }

  /**
   * Gets the teamAway attribute of the Match object
   * 
   * @return The teamAway value
   */
  public Team getTeamAway() {
    return awayTeam;
  }

  /**
   * Gets the scoreSheet attribute of the Match object
   * 
   * @return The scoreSheet value
   */
  public MatchSheet getScoreSheet() {
    return scoreSheet;
  }

  /**
   * Who gets how many points ...
   * 
   * @param team
   *          Description of the Parameter
   * @return The points value
   */
  public int getPoints(Team team) {
    if (isGameTied()) {
      return rules.pointsForTied();
    } else {
      if (overtimePlayed) {
        if (this.isTeamWinner(team)) {
          return rules.pointsForOvertimeWin();
        } else {
          return rules.pointsForOvertimeLoss();
        }
      } else {
        if (this.isTeamWinner(team)) {
          return rules.pointsForWin();
        } else {
          return rules.pointsForLoss();
        }
      }
    }
  }

  /**
   * Returns true if the match is finished
   * 
   * @return true/false
   */
  public boolean isFinished() {
    return matchFinished;
  }

  /**
   * Returns the spectators
   * 
   * @return No of spectators
   */
  public int getSpectators() {
    return spectators;
  }
  
  
  // IMPLEMENTED IN THE ENGINES:
  
  public abstract  void moveOn() ;
  public abstract int getSecondsPlayed() ;
  
  
  // BASE LISTENERS / FEEDERS 
  
  
  /**
   * Remove all listeners on this match (quite unpopular)
   * 
   */
  public synchronized void removeAllMatchListeners() {
    this.listener = null;
  }
  
  

  /**
   * Adds Matchlistener to the Match
   * 
   * @param m
   *          Listener
   */
  public synchronized void addMatchListener(MatchListener m) {
    if (listener == null) {
      listener = new Vector<MatchListener>(5, 5);
    }
    if (!listener.contains(m)) {
      listener.addElement( m);
    }
  }
 
  
  
  // / LISTENER FEEDERS ....

  protected  synchronized void fireRegularPeriodStarted() {
    if (listener != null) {
      for (Enumeration<MatchListener> e = listener.elements(); e.hasMoreElements();) {
        (e.nextElement()).regularPeriodStarted();
      }
    }
  }

  protected  synchronized void fireOvertimePeriodStarted() {
    if (listener != null) {
      for (Enumeration<MatchListener> e = listener.elements(); e.hasMoreElements();) {
        (e.nextElement()).overtimePeriodStarted();
      }
    }
  }

  protected  synchronized void fireGoToOvertime() {
    if (listener != null) {
      for (Enumeration<MatchListener> e = listener.elements(); e.hasMoreElements();) {
        (e.nextElement()).goToOvertime();
      }
    }
  }

  protected  synchronized void fireGameFinished() {
    if (listener != null) {
      for (Enumeration<MatchListener> e = listener.elements(); e.hasMoreElements();) {
        (e.nextElement()).gameFinished();
      }
    }
  }

  protected  synchronized void fireRegularPeriodFinished() {
    if (listener != null) {
      for (Enumeration<MatchListener> e = listener.elements(); e.hasMoreElements();) {
        (e.nextElement()).regularPeriodFinished();
      }
    }
  }

  protected  synchronized void fireOvertimePeriodFinished() {
    if (listener != null) {
      for (Enumeration<MatchListener> e = listener.elements(); e.hasMoreElements();) {
        (e.nextElement()).overtimePeriodFinished();
      }
    }
  }

  protected  synchronized void fireScoreChange() {
    if (listener != null) {
      for (Enumeration<MatchListener> e = listener.elements(); e.hasMoreElements();) {
        (e.nextElement()).scoreChange();
      }
    }
  }

  protected  synchronized void fireSceneFinished() {
    if (listener != null) {
      for (Enumeration<MatchListener> e = listener.elements(); e.hasMoreElements();) {
        (e.nextElement()).sceneFinished();
      }
    }
  }

}