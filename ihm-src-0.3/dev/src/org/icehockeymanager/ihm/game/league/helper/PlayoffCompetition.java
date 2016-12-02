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
  
package org.icehockeymanager.ihm.game.league.helper;

import java.io.*;

import org.icehockeymanager.ihm.game.match.*;
import org.icehockeymanager.ihm.game.team.*;

/**
 * PlayoffCompetition Helper class for all kind of cup/playoff competitions.
 * 
 * @author Bernhard von Gunten / Arik Dasen
 * @created December, 2001
 */
public class PlayoffCompetition implements Serializable {

  static final long serialVersionUID = 4020757576319986470L;

  /** Home team */
  private Team teamHome = null;

  /** Away team */
  private Team teamAway = null;

  /** Matches between teams */
  private Match[] matches = null;

  /** Best of counter */
  private int bestOf;

  /**
   * Constructs competition
   * 
   * @param teamHome
   *          The home team
   * @param teamAway
   *          The away team
   * @param bestOf
   *          Best of n playoffs
   */
  public PlayoffCompetition(Team teamHome, Team teamAway, int bestOf) {
    this.teamHome = teamHome;
    this.teamAway = teamAway;
    this.bestOf = bestOf;
    matches = new Match[bestOf];
  }

  /**
   * Returns the winner of this competition
   * 
   * @return The winner value
   */
  public Team getWinner() {
    if (getHomeWins() > getAwayWins()) {
      return teamHome;
    } else {
      return teamAway;
    }
  }

  /**
   * Returns the loser of this competition
   * 
   * @return The loser value
   */
  public Team getLoser() {
    if (getHomeWins() > getAwayWins()) {
      return teamAway;
    } else {
      return teamHome;
    }
  }

  /**
   * Returns the home team
   * 
   * @return The teamHome value
   */
  public Team getTeamHome() {
    return teamHome;
  }

  /**
   * Returns the away team
   * 
   * @return The teamAway value
   */
  public Team getTeamAway() {
    return teamAway;
  }

  /**
   * Returns the count of "home team wins"
   * 
   * @return The homeWins value
   */
  public int getHomeWins() {
    int result = 0;
    for (int i = 0; i < bestOf; i++) {
      Match tmp = matches[i];
      if (tmp.isFinished() && tmp.isTeamWinner(teamHome)) {
        result++;
      }
    }
    return result;
  }

  /**
   * Returns the count of "away team wins"
   * 
   * @return The awayWins value
   */
  public int getAwayWins() {
    int result = 0;
    for (int i = 0; i < bestOf; i++) {
      Match tmp = matches[i];
      if (tmp.isFinished() && tmp.isTeamWinner(teamAway)) {
        result++;
      }
    }
    return result;
  }

  /**
   * Returns boolean if competition is already won by a team
   * 
   * @return The competitionWon value
   */
  public boolean isCompetitionWon() {
    int neededWins = (bestOf + 1) / 2;
    if ((getHomeWins() >= neededWins) || (getAwayWins() >= neededWins)) {
      return true;
    } else {
      return false;
    }

  }

  /**
   * Add match to the competition
   * 
   * @param match
   *          The match to be added
   * @param round
   *          The round of the playoffs
   */
  public void addMatch(Match match, int round) {
    this.matches[round] = match;
  }

  /**
   * Returns the match of a round passed by
   * 
   * @param round
   *          The round of the playoffs
   * @return The match value
   */
  public Match getMatch(int round) {
    return matches[round];
  }

}
