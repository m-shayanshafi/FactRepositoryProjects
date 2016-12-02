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
  
package org.icehockeymanager.ihm.game.team;

import org.icehockeymanager.ihm.lib.*;

/**
 * Team stats with heavy metal compareTo(o) overriding
 * 
 * @author Arik Dasen & Bernhard von Gunten
 * @created December, 2001
 */
public class TeamStats extends IhmCustomComparator implements Cloneable {
  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 3257009869025588536L;

  /** Sort by goals for */
  public final static int SORT_GOALS_FOR = 1;

  /** Sort by goals against */
  public final static int SORT_GOALS_AGAINST = 2;

  /** Sort by games played */
  public final static int SORT_GAMES_PLAYED = 3;

  /** Sort by games won */
  public final static int SORT_GAMES_WON = 4;

  /** Sort by games tied */
  public final static int SORT_GAMES_TIED = 5;

  /** Sort by games lost */
  public final static int SORT_GAMES_LOST = 6;

  /** Sort by points (special) */
  public final static int SORT_POINTS = 7;

  /** Sort by gaol difference */
  public final static int SORT_GOALS_DIFFERENCE = 8;

  /** Sort by penalty minutes */
  public final static int SORT_PENALTY_MINUTES = 9;

  /** Goals for */
  private int goalsFor = 0;

  /** Goals against */
  private int goalsAgainst = 0;

  /** Games played */
  private int gamesPlayed = 0;

  /** Games won */
  private int gamesWon = 0;

  /** Games tied */
  private int gamesTied = 0;

  /** Games lost */
  private int gamesLost = 0;

  /** Points */
  private int points = 0;

  /** Penalty minutes */
  private int penaltyMinutes = 0;

  /** Team of this stats */
  private Team team = null;

  /** Owner of this stats */
  private Object owner = null;

  /**
   * Constructs new teamStats for a owner
   * 
   * @param team
   *          Team of this stats
   * @param owner
   *          Owner of this stats (leagueElement, league etc.)
   */
  public TeamStats(Team team, Object owner) {
    this.team = team;
    this.owner = owner;
  }

  /**
   * Add goals to teamStats
   * 
   * @param goalsFor
   *          The feature to be added to the GoalsFor attribute
   */
  public void addGoalsFor(int goalsFor) {
    this.goalsFor += goalsFor;
  }

  /**
   * Add goals Against to teamStats
   * 
   * @param goalsAgainst
   *          The feature to be added to the GoalsAgainst attribute
   */
  public void addGoalsAgainst(int goalsAgainst) {
    this.goalsAgainst += goalsAgainst;
  }

  /**
   * Add points to teamStats
   * 
   * @param points
   *          The feature to be added to the Points attribute
   */
  public void addPoints(int points) {
    this.points += points;
  }

  /**
   * Add won games to teamStats
   * 
   * @param wins
   *          The feature to be added to the GameWon attribute
   */
  public void addGameWon(int wins) {
    this.gamesWon += wins;
  }

  /**
   * Add lost games to teamStats
   * 
   * @param lost
   *          The feature to be added to the GameLost attribute
   */
  public void addGameLost(int lost) {
    this.gamesLost += lost;
  }

  /**
   * Add tied games to teamStats
   * 
   * @param ties
   *          The feature to be added to the GameTied attribute
   */
  public void addGameTied(int ties) {
    this.gamesTied += ties;
  }

  /**
   * Add played games to teamStats
   * 
   * @param played
   *          The feature to be added to the GamesPlayed attribute
   */
  public void addGamesPlayed(int played) {
    this.gamesPlayed += played;
  }

  /**
   * Add penalty minutes to teamStats
   * 
   * @param penaltyMinutes
   *          The feature to be added to the PenaltyMinutes attribute
   */
  public void addPenaltyMinutes(int penaltyMinutes) {
    this.penaltyMinutes += penaltyMinutes;
  }

  /**
   * Returns goals scored
   * 
   * @return The goalsFor value
   */
  public int getGoalsFor() {
    return goalsFor;
  }

  /**
   * Returns goals against
   * 
   * @return The goalsAgainst value
   */
  public int getGoalsAgainst() {
    return goalsAgainst;
  }

  /**
   * Returns goal difference
   * 
   * @return The goalsDiff value
   */
  public int getGoalsDiff() {
    return goalsFor - goalsAgainst;
  }

  /**
   * Returns points
   * 
   * @return The points value
   */
  public int getPoints() {
    return points;
  }

  /**
   * Returns games played
   * 
   * @return The gamesPlayed value
   */
  public int getGamesPlayed() {
    return gamesPlayed;
  }

  /**
   * Returns games won
   * 
   * @return The gamesWon value
   */
  public int getGamesWon() {
    return gamesWon;
  }

  /**
   * Returns games tied
   * 
   * @return The gamesTied value
   */
  public int getGamesTied() {
    return gamesTied;
  }

  /**
   * Returns games lost
   * 
   * @return The gamesLost value
   */
  public int getGamesLost() {
    return gamesLost;
  }

  /**
   * Returns penalty minutes
   * 
   * @return The penaltyMinutes value
   */
  public int getPenaltyMinutes() {
    return penaltyMinutes;
  }

  /**
   * Returns owner of this stast
   * 
   * @return The team value
   */
  public Team getTeam() {
    return team;
  }

  /**
   * Returns owner of this stats
   * 
   * @return The owner value
   */
  public Object getOwner() {
    return owner;
  }

  /**
   * Returns sort value considering sortCriteria
   * 
   * @return The sortValue value
   */
  protected double getSortValue() {
    switch (sortCriteria) {
    case SORT_GAMES_WON:
      return gamesWon;
    case SORT_GAMES_TIED:
      return gamesTied;
    case SORT_GAMES_LOST:
      return gamesLost;
    case SORT_GOALS_FOR:
      return goalsFor;
    case SORT_GOALS_AGAINST:
      return goalsAgainst;
    case SORT_GAMES_PLAYED:
      return gamesPlayed;
    case SORT_GOALS_DIFFERENCE:
      return goalsFor - goalsAgainst;
    case SORT_POINTS:
      return points;
    case SORT_PENALTY_MINUTES:
      return penaltyMinutes;
    default:
      return points;
    }
  }

  /**
   * Override compareTo(Object o) for the points sorting
   * 
   * @param o
   *          Object to be compared
   * @return Std. compare value
   */
  public int compareTo(IhmCustomComparator o) {
    // Use standard compareTo
    if (sortCriteria != SORT_POINTS) {
      return super.compareTo(o);
    }

    // Cast stats ... (should be stats ;-)
    TeamStats other = (TeamStats) o;

    // Return values depending on ascending/descending sort order
    int smaller = -1;
    int bigger = 1;

    if (!ascending) {
      smaller = 1;
      bigger = -1;
    }

    // First check for points
    int otherPoints = other.getPoints();
    int ourPoints = this.getPoints();

    if (otherPoints != ourPoints) {
      if (otherPoints > ourPoints) {
        return smaller;
      } else {
        return bigger;
      }
    }

    // Then check difference between goalsFor & goalsAgainst
    int otherDiff = other.getGoalsDiff();
    int ourDiff = getGoalsDiff();

    if (otherDiff != ourDiff) {
      if (otherDiff > ourDiff) {
        return smaller;
      } else {
        return bigger;
      }
    }

    // Then check for goalsFor
    int otherGF = other.getGoalsFor();
    int ourGF = getGoalsFor();

    if (otherGF != ourGF) {
      if (otherGF > ourGF) {
        return smaller;
      } else {
        return bigger;
      }
    }

    // Last check for wins
    int otherWins = other.getGamesWon();
    int ourWins = getGamesWon();

    if (otherWins != ourWins) {
      if (otherWins > ourWins) {
        return smaller;
      } else {
        return bigger;
      }
    }

    // todo : Nothing left ?? Perhaps games between this teams ??

    return 0;
    // equal !!!!

  }

  /**
   * Provice clone() to this class
   * 
   * @return Clone of this object
   * @exception CloneNotSupportedException
   *              Clone not supported Exception
   */
  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }

}