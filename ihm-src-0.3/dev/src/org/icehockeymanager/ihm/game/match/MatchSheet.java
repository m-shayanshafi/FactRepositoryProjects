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

import org.icehockeymanager.ihm.game.player.*;
import org.icehockeymanager.ihm.game.team.*;

/**
 * ScoreSheet class contains match data, does stats
 * 
 * @author Arik Dasen, Bernhard von Gunten
 * @created December, 2001
 */
public class MatchSheet implements Serializable {

  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 3977017331828340016L;

  /** Match reference */
  private Match match = null;

  /** Vector of Goal */
  private Vector<MatchDataGoal> goals;

  /** Vector of Penalty */
  private Vector penalties;

  /** Goals by the home team */
  private int goalsHome = 0;

  /** Goals by the away team */
  private int goalsAway = 0;

  /**
   * Constructor for the ScoreSheet object
   * 
   * @param match
   *          Match of this scoreSheet
   */
  public MatchSheet(Match match) {
    this.match = match;
    goals = new Vector<MatchDataGoal>();
    penalties = new Vector();
  }

  /** Match has ended */
  public void finish() {
    impactOnPlayers();

    // update stats
    TeamStats[] teamHomeStats = match.getTeamHome().getAllStats(match.getLeagueElement());
    TeamStats[] teamAwayStats = match.getTeamAway().getAllStats(match.getLeagueElement());

    // for each stats
    for (int i = 0; i < teamHomeStats.length; i++) {
      teamHomeStats[i].addGamesPlayed(1);
      teamAwayStats[i].addGamesPlayed(1);
      if (isGameTied()) {
        teamHomeStats[i].addGameTied(1);
        teamAwayStats[i].addGameTied(1);
      } else if (isHomeTeamWinner()) {
        teamHomeStats[i].addGameWon(1);
        teamAwayStats[i].addGameLost(1);
      } else {
        teamHomeStats[i].addGameLost(1);
        teamAwayStats[i].addGameWon(1);
      }
      teamHomeStats[i].addPoints(match.getPoints(match.getTeamHome()));
      teamAwayStats[i].addPoints(match.getPoints(match.getTeamAway()));
    }

    // For each player
    Player[] homePlayers = match.getTeamHome().getPlayers();
    Player[] awayPlayers = match.getTeamAway().getPlayers();

    for (int i = 0; i < homePlayers.length; i++) {
      PlayerStats[] playerStats = homePlayers[i].getAllStats(match.getLeagueElement());
      for (int n = 0; n < playerStats.length; n++) {
        playerStats[n].addGamesPlayed(1);
        if (isGameTied()) {
          playerStats[n].addGamesTied(1);
        } else if (isHomeTeamWinner()) {
          playerStats[n].addGamesWon(1);
        } else {
          playerStats[n].addGamesLost(1);
        }
      }

    }

    for (int i = 0; i < awayPlayers.length; i++) {
      PlayerStats[] playerStats = awayPlayers[i].getAllStats(match.getLeagueElement());
      for (int n = 0; n < playerStats.length; n++) {
        playerStats[n].addGamesPlayed(1);
        if (isGameTied()) {
          playerStats[n].addGamesTied(1);
        } else if (isHomeTeamWinner()) {
          playerStats[n].addGamesLost(1);
        } else {
          playerStats[n].addGamesWon(1);
        }
      }

    }
  }

  /** Call impacts on players after match */
  private void impactOnPlayers() {
    Player[] homePlayers = match.getTeamHome().getPlayers();
    Player[] awayPlayers = match.getTeamAway().getPlayers();

    // Home first
    for (int i = 0; i < homePlayers.length; i++) {
      // Won, lost, tied
      if (isGameTied()) {
        homePlayers[i].impactOnGameTied();
      } else if (isHomeTeamWinner()) {
        homePlayers[i].impactOnGameWon();
      } else {
        homePlayers[i].impactOnGameLost();
      }
      // Games played
      homePlayers[i].impactOnGamePlayed();

      // Goals against
      for (int q = 0; q < this.getGoalsAway(); q++) {
        homePlayers[i].impactOnGoalsAggainst();
      }

      // Goals for
      for (int q = 0; q < this.getGoalsHome(); q++) {
        homePlayers[i].impactOnGoals();
      }

    }

    for (int i = 0; i < awayPlayers.length; i++) {
      if (isGameTied()) {
        awayPlayers[i].impactOnGameTied();
      } else if (isHomeTeamWinner()) {
        awayPlayers[i].impactOnGameLost();
      } else {
        awayPlayers[i].impactOnGameWon();
      }
      awayPlayers[i].impactOnGamePlayed();

      // Goals against
      for (int q = 0; q < this.getGoalsHome(); q++) {
        awayPlayers[i].impactOnGoalsAggainst();
      }

      // Goals for
      for (int q = 0; q < this.getGoalsAway(); q++) {
        awayPlayers[i].impactOnGoals();
      }
    }
  }

  /**
   * is game tied
   * 
   * @return The gameTied value
   */
  public boolean isGameTied() {
    return goalsHome == goalsAway;
  }

  /**
   * is home team winner
   * 
   * @return The homeTeamWinner value
   */
  public boolean isHomeTeamWinner() {
    return goalsHome > goalsAway;
  }

  /**
   * Return goals home team
   * 
   * @return The goalsHome value
   */
  public int getGoalsHome() {
    return goalsHome;
  }

  /**
   * Return goals away team
   * 
   * @return The goalsAway value
   */
  public int getGoalsAway() {
    return goalsAway;
  }

  /**
   * Add goal
   * 
   * @param goal
   *          The feature to be added to the Goal attribute
   */
  public void addGoal(MatchDataGoal goal) {
    goals.addElement(goal);
    if (goal.getTeam() == match.getTeamHome()) {
      goalsHome++;
    } else {
      goalsAway++;
    }
    goal.updateStats(match.getLeagueElement());
  }

  /**
   * Returns goals
   * 
   * @return Goals
   */
  public Vector<MatchDataGoal> getGoals() {
    return goals;
  }

  /**
   * Returns penalties
   * 
   * @return Penalties
   */
  public Vector getPenalties() {
    return penalties;
  }
  
  public MatchDataGoal getLastGoal() {
    return goals.get(goals.size() -1);
  }
  

}
