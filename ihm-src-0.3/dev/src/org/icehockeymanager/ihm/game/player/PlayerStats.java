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
  
package org.icehockeymanager.ihm.game.player;

import org.icehockeymanager.ihm.lib.*;

/**
 * Player stats (analog to the team stats)
 * 
 * @author Bernhard von Gunten
 * @created December, 2001
 */
public abstract class PlayerStats extends IhmCustomComparator implements Cloneable {
  /** Sort by goals */
  public final static int SORT_GOALS = 1;

  /** Sort by assists */
  public final static int SORT_ASSISTS = 2;

  /** Sort by scorer points */
  public final static int SORT_SCORER_POINTS = 3;

  /** Sort by games played */
  public final static int SORT_GAMES_PLAYED = 4;

  /** Sort by penalty minutes */
  public final static int SORT_PENALTY_MINUTES = 5;

  /** Sort by position (outside param) */
  public final static int SORT_POSITION = 6;

  /** Goals */
  private int goals = 0;

  /** Assists */
  private int assists = 0;

  /** Games played */
  private int gamesPlayed = 0;

  /** Penalty minutes */
  private int penaltyMinutes = 0;

  /** Player of thist stats */
  private Player player;

  /** Owner of this stats */
  private Object owner;

  /**
   * Constructs new playerStats for a owner
   * 
   * @param player
   *          Player of this stats
   * @param owner
   *          Owner of this stats
   */
  public PlayerStats(Player player, Object owner) {
    this.player = player;
    this.owner = owner;
  }

  /**
   * Gets the goals attribute of the PlayerStats object
   * 
   * @return The goals value
   */
  public int getGoals() {
    return goals;
  }

  /**
   * Gets the player attribute of the PlayerStats object
   * 
   * @return The player value
   */
  public Player getPlayer() {
    return player;
  }

  /**
   * Gets the assists attribute of the PlayerStats object
   * 
   * @return The assists value
   */
  public int getAssists() {
    return assists;
  }

  /**
   * Gets the scorerPoints attribute of the PlayerStats object
   * 
   * @return The scorerPoints value
   */
  public int getScorerPoints() {
    return getGoals() + getAssists();
  }

  /**
   * Gets the gamesPlayed attribute of the PlayerStats object
   * 
   * @return The gamesPlayed value
   */
  public int getGamesPlayed() {
    return gamesPlayed;
  }

  /**
   * Gets the penaltyMinutes attribute of the PlayerStats object
   * 
   * @return The penaltyMinutes value
   */
  public int getPenaltyMinutes() {
    return penaltyMinutes;
  }

  /**
   * Adds a feature to the GamesWon attribute of the PlayerStats object
   * 
   * @param wins
   *          The feature to be added to the GamesWon attribute
   */
  public void addGamesWon(int wins) {
  }

  /**
   * Adds a feature to the GamesTied attribute of the PlayerStats object
   * 
   * @param ties
   *          The feature to be added to the GamesTied attribute
   */
  public void addGamesTied(int ties) {
  }

  /**
   * Adds a feature to the GamesWon attribute of the PlayerStats object
   * 
   * @param losts
   *          The feature to be added to the GamesWon attribute
   */
  public void addGamesLost(int losts) {
  }

  /**
   * Adds a feature to the Goals attribute of the PlayerStats object
   * 
   * @param goals
   *          The feature to be added to the Goals attribute
   */
  public void addGoals(int goals) {
    this.goals += goals;
  }

  /**
   * Adds a feature to the Assists attribute of the PlayerStats object
   * 
   * @param assists
   *          The feature to be added to the Assists attribute
   */
  public void addAssists(int assists) {
    this.assists += assists;
  }

  /**
   * Adds a feature to the PenaltyMinutes attribute of the PlayerStats object
   * 
   * @param penaltyMinutes
   *          The feature to be added to the PenaltyMinutes attribute
   */
  public void addPenaltyMinutes(int penaltyMinutes) {
    this.penaltyMinutes += penaltyMinutes;
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
   * Gets the owner attribute of the PlayerStats object
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
    case SORT_POSITION:
      return getPlayer().getPlayerAttributes().getPosition();
    case SORT_GOALS:
      return goals;
    case SORT_ASSISTS:
      return assists;
    case SORT_SCORER_POINTS:
      return goals + assists;
    case SORT_GAMES_PLAYED:
      return gamesPlayed;
    case SORT_PENALTY_MINUTES:
      return penaltyMinutes;
    default:
      return goals;
    }
  }

  /**
   * Clone the object
   * 
   * @return Clone of this object
   * @exception CloneNotSupportedException
   *              Clone not supportet Exception
   */
  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }

}
