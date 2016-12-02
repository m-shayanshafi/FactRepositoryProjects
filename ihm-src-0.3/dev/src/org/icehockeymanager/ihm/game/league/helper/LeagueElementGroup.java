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
import java.util.*;

import org.icehockeymanager.ihm.game.league.*;
import org.icehockeymanager.ihm.game.player.*;
import org.icehockeymanager.ihm.game.team.*;

/**
 * LeagueElementGroup Helper class containing a group of LeagueElements
 * 
 * @author Bernhard von Gunten / Arik Dasen
 * @created December, 2001
 */
public class LeagueElementGroup implements TeamStatsOwner, PlayerStatsOwner, Serializable {

  static final long serialVersionUID = -5515530718294902886L;

  /** Vector of LeagueElements */
  private Vector<LeagueElement> leagueElements = null;

  /** Name of this group */
  private String nameKey = null;

  /** League of this group */
  private League league = null;

  /**
   * Constructs group
   * 
   * @param league
   *          Description of the Parameter
   * @param nameKey
   *          The name/key of this group
   */
  public LeagueElementGroup(League league, String nameKey) {
    this.leagueElements = new Vector<LeagueElement>();
    this.nameKey = nameKey;
    this.league = league;
  }

  /**
   * Add LeagueElement to group
   * 
   * @param leagueElement
   *          The leagueElement to add in this group
   */
  public void addLeagueElement(LeagueElement leagueElement) {
    if (leagueElement != null) {
      leagueElements.add(leagueElement);
      leagueElement.setLeagueElementGroup(this);
    }
  }

  /**
   * Returns vector of all LeagueElements in this group
   * 
   * @return The leagueElements value
   */
  public Vector<LeagueElement> getLeagueElements() {
    java.util.Collections.sort(leagueElements);
    return leagueElements;
  }

  /**
   * Returns count of all LeagueElements in this group
   * 
   * @return The leagueElementCount value
   */
  public int getLeagueElementCount() {
    return this.leagueElements.size();
  }

  /**
   * Returns the name of this group
   * 
   * @return The name value
   */
  public String getNameKey() {
    return this.nameKey;
  }

  /**
   * Gets the league attribute of the LeagueElementGroup object
   * 
   * @return The league value
   */
  public League getLeague() {
    return league;
  }

  /**
   * Gets the players attribute of the LeagueElementGroup object
   * 
   * @return The players value
   */
  public Player[] getPlayers() {
    Team[] teams = getTeams();
    Vector<Player> players = new Vector<Player>();
    for (int i = 0; i < teams.length; i++) {
      Player[] tmp = teams[i].getPlayers();
      for (int n = 0; n < tmp.length; n++) {
        players.add(tmp[n]);
      }
    }

    return players.toArray(new Player[players.size()]);
  }

  /**
   * Gets the playerStats attribute of the LeagueElementGroup object
   * 
   * @return The playerStats value
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
   * Gets the teams attribute of the LeagueElementGroup object
   * 
   * @return The teams value
   */
  public Team[] getTeams() {
    Vector<Team> teams = new Vector<Team>();
    for (int i = 0; i < leagueElements.size(); i++) {
      Team[] tmp = leagueElements.get(i).getTeams();
      for (int n = 0; n < tmp.length; n++) {
        // Each team only once !!
        if (!teams.contains(tmp[n])) {
          teams.add(tmp[n]);
        }
      }
    }
    return teams.toArray(new Team[teams.size()]);
  }

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
   * Overrides the toString method with something more usefull
   * 
   * @return The description of this League Element Group
   */
  public String toString() {
    String owner = this.getLeague().getLeagueOwner().getName();
    String league = this.getLeague().getName();
    String name = this.getNameKey();
    return owner + " : " + league + " : " + name;
  }

}
