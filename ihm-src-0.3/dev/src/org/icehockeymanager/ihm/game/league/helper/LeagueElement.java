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

import org.icehockeymanager.ihm.game.*;
import org.icehockeymanager.ihm.game.league.*;
import org.icehockeymanager.ihm.game.match.*;
import org.icehockeymanager.ihm.game.match.textengine.*;
import org.icehockeymanager.ihm.game.player.*;
import org.icehockeymanager.ihm.game.team.*;
import org.icehockeymanager.ihm.lib.*;



/**
 * LeagueElement Base class for all league elements (playoffs, regular season
 * ...)
 * 
 * @author Bernhard von Gunten / Arik Dasen
 * @created December, 2001
 */
public abstract class LeagueElement extends IhmCustomComparator implements TeamStatsOwner, PlayerStatsOwner {
  /** Sort by rank */
  public final static int SORT_RANK = 0;

  /** Owner of this element (league) */
  protected League league = null;

  /** Name of this element */
  protected String nameKey = null;

  /** Boolean if this element is already announced */
  protected boolean announced = false;

  /** LeagueElement group if any */
  protected LeagueElementGroup leagueElementGroup = null;

  /** Rank of this leagueElement */
  protected int rank = 0;

  /** Level of playoffs/playouts */
  protected int level = 0;

  /**
   * Constructs element with name & owner
   * 
   * @param nameKey
   *          The name/key of this leagueElemnt
   * @param league
   *          The league of this leagueElement
   * @param rank
   *          Rank of this element
   * @param level
   *          Level (important for playoff style elements)
   */
  public LeagueElement(int rank, String nameKey, League league, int level) {
    this.rank = rank;
    this.league = league;
    this.nameKey = nameKey;
    this.level = level;
  }

  /**
   * Constructs element with name & owner
   * 
   * @param nameKey
   *          The name/key of this leagueElemnt
   * @param league
   *          The league of this leagueElement
   * @param rank
   *          Rank of this element
   */
  public LeagueElement(int rank, String nameKey, League league) {
    this(rank, nameKey, league, 0);
  }

  public int getLevel() {
    return level;
  }

  /** Generate TeamStats for each team */
  public void generateTeamStats() {
    Team[] teams = getTeams();
    for (int i = 0; i < teams.length; i++) {
      teams[i].createStats(this);
    }
    // Add teamStatsLeague to every team for the league of this element
    for (int i = 0; i < teams.length; i++) {
      if (teams[i].getStats(this.getLeague()) == null) {
        teams[i].createStats(this.getLeague());
      }
    }
    // Add teamStatsLeague to every team for the Group of this element (if any)
    if (this.getLeagueElementGroup() != null) {
      for (int i = 0; i < teams.length; i++) {
        if (teams[i].getStats(this.getLeagueElementGroup()) == null) {
          teams[i].createStats(this.getLeagueElementGroup());
        }
      }
    }
  }

  /** Sets element as announced */
  public void setAnnounced() {
    announced = true;
  }

  /**
   * Sets the leagueElementGroup of this leagueElement
   * 
   * @param leagueElementGroup
   *          The new leagueElementGroup value
   */
  public void setLeagueElementGroup(LeagueElementGroup leagueElementGroup) {
    this.leagueElementGroup = leagueElementGroup;
  }

  /**
   * Returns the leagueElementGroup of this element (null if none)
   * 
   * @return The leagueElementGroup value
   */
  public LeagueElementGroup getLeagueElementGroup() {
    return this.leagueElementGroup;
  }

  /**
   * Returns boolean if element is announced
   * 
   * @return The announced value
   */
  public boolean isAnnounced() {
    return announced;
  }

  /**
   * Returns owner of this element
   * 
   * @return The league value
   */
  public League getLeague() {
    return this.league;
  }

  /**
   * Returns the name of this element
   * 
   * @return The name value
   */
  public String getNameKey() {
    return this.nameKey;
  }

  public LeagueOwner getLeagueOwner() {
    return getLeague().getLeagueOwner();
  }

  /**
   * Returns standings of this element
   * 
   * @return The leagueStandings value
   */
  public abstract LeagueStandings getLeagueStandings();

  /**
   * Returns teams of this element
   * 
   * @return The teams value
   */
  public abstract Team[] getTeams();

  /**
   * Returns all players of this league element
   * 
   * @return Player[]
   */
  public abstract Player[] getPlayers();

  /**
   * Gets the teamStats attribute of the LeagueElement object
   * 
   * @return The teamStats value
   */
  public TeamStats[] getTeamStats() {
    Team[] teams = getTeams();
    TeamStats[] result = new TeamStats[teams.length];
    for (int i = 0; i < teams.length; i++) {
      result[i] = teams[i].getStats(this);
    }
    return result;
  }

  /**
   * Returns all player stats of this league.
   * 
   * @return PlayerStats[]
   */
  public PlayerStats[] getPlayerStats() {
    Player[] players = getPlayers();
    PlayerStats[] result = new PlayerStats[players.length];
    for (int i = 0; i < players.length; i++) {
      result[i] = players[i].getStats(this);
    }
    return result;
  }

  /**
   * Returns sort value considering sortCriteria
   * 
   * @return The sortValue value
   */
  protected double getSortValue() {
    switch (sortCriteria) {
    case SORT_RANK:
      return rank;
    default:
      return rank;
    }
  }

  protected Match createMatch(LeagueElement leagueElement, Team teamHome, Team teamAway, Rules rules) {
    try {
      Class matchClass = Class.forName(GameController.getInstance().getScenario().getScenarioSettings().MATCH_ENGINE);
     
      if (TextMatch.class.isAssignableFrom(matchClass)) {
        return new TextMatch(leagueElement, teamHome, teamAway, rules);
      } else {
        return null;
      }
      
    } catch (Exception err) {
      //TODO: Handle exception
      return null;
    }
  }
  
}
