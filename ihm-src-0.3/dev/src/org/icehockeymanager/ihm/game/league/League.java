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
  
package org.icehockeymanager.ihm.game.league;

import java.io.*;
import java.util.*;

import org.icehockeymanager.ihm.clients.devgui.ihm.scheduler.*;
import org.icehockeymanager.ihm.game.player.*;
import org.icehockeymanager.ihm.game.team.*;
import org.icehockeymanager.ihm.game.league.helper.*;
import org.w3c.dom.*;
import org.icehockeymanager.ihm.game.scheduler.events.*;

/**
 * League Is owner of a list of teams within this league. Compareable to the NLA
 * or NLB of Switzerland
 * <p>
 * <p>- Gets the teams trough constructor params.
 * <p>- Provides some help functions that are needed in every league. (i.e to
 * replace a team with another, or lookup functions for teams)
 * <p>- Implements the Comparable interface for sorting leagues in an owner.
 * <p>
 * <p>
 * This abstract class has to be implemented by every league class (i.E. std
 * league)
 * 
 * @author Bernhard von Gunten / Arik Dasen
 * @created December, 2001
 */
public abstract class League implements TeamStatsOwner, PlayerStatsOwner, TeamOwner, PlayerOwner, SchedulerEventOwner, Comparable, Serializable {

  /** Array of the teams in this league */
  protected Team[] teams;

  /** Name of this league */
  protected String name;

  /** Rank of this league (within an owner) */
  protected int rank = 0;

  /** Pointer to the owner of this league */
  protected LeagueOwner leagueOwner = null;

  /** List of teams to add when season is over */
  protected Vector<Team> teamsToAdd = new Vector<Team>();

  /** List of teams to remove when season is over */
  protected Vector<Team> teamsToRemove = new Vector<Team>();

  /**
   * Constructor with leaguename, ranking in owner and the teams
   * 
   * @param name
   *          Name of this league
   * @param rank
   *          Rank of this league (in the leagueOwner)
   * @param teams
   *          Teams of this league
   */
  public League(String name, int rank, Team[] teams) {
    this.rank = rank;
    this.teams = teams;
    this.name = name;
  }

  /** Resets all teams (new Season) */
  public abstract void newSeason();

  /**
   * Sets the owner of this league (called by LeagueOwner)
   * 
   * @param leagueOwner
   *          The new leagueOwner value
   */
  public void setLeagueOwner(LeagueOwner leagueOwner) {
    this.leagueOwner = leagueOwner;
  }

  /**
   * Returns it's owner
   * 
   * @return The leagueOwner value
   */
  public LeagueOwner getLeagueOwner() {
    return leagueOwner;
  }

  /**
   * Returns name of this league
   * 
   * @return The name value
   */
  public String getName() {
    return name;
  }

  /**
   * Returns all teams of this league
   * 
   * @return The teams value
   */
  public Team[] getTeams() {
    return teams;
  }

  /**
   * Gets the teamStats attribute of the League object
   * 
   * @return The teamStats value
   */
  public TeamStats[] getTeamStats() {
    TeamStats[] result = new TeamStats[teams.length];
    for (int i = 0; i < teams.length; i++) {
      result[i] = teams[i].getStats(this);
    }
    return result;
  }

  /**
   * Gets the playerStats attribute of the League object
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
   * Returns all players of this league
   * 
   * @return The players value
   */
  public Player[] getPlayers() {
    Vector<Player> tmp = new Vector<Player>();
    for (int i = 0; i < teams.length; i++) {
      Player[] players = teams[i].getPlayers();
      for (int n = 0; n < players.length; n++) {
        tmp.add(players[n]);
      }
    }

    return tmp.toArray(new Player[tmp.size()]);
  }

  /**
   * Lookup function to search a team within this league
   * 
   * @param team
   *          Team looking for
   * @return If team is in this league
   */
  public boolean isTeamOfLeague(Team team) {
    for (int i = 0; i < teams.length; i++) {
      if (teams[i].equals(team)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Replaces oldTeam with newTeam. Returns true if team was replace, false if
   * the oldTeam wasn't found
   * 
   * @param oldTeam
   *          Old team
   * @param newTeam
   *          New Team
   * @return Is old team even found
   */
  public boolean replaceTeam(Team oldTeam, Team newTeam) {
    for (int i = 0; i < teams.length; i++) {
      if (teams[i].equals(oldTeam)) {
        teams[i] = newTeam;
        return true;
      }
    }
    return false;
  }

  /**
   * Returns all Standings (Playoffs or Tables)
   * 
   * @return The leagueElements value
   */
  public abstract Vector<LeagueElement> getLeagueElements();

  /**
   * Returns all LeagueElementGroups
   * 
   * @return The leagueElementGroups value
   */
  public abstract Vector<LeagueElementGroup> getLeagueElementGroups();

  /**
   * Small helper for the Comparable interface
   * 
   * @return The sortValue value
   */
  public int getSortValue() {
    return this.rank;
  }

  /**
   * Gets the rank attribute of the League object
   * 
   * @return The rank value
   */
  public int getRank() {
    return rank;
  }

  /**
   * Implementation of the compareTo function to sort the leagues
   * 
   * @param o
   *          Object to compare
   * @return Std compare value
   */
  public int compareTo(Object o) {
    League tmp = (League) o;
    if (tmp.getSortValue() > getSortValue()) {
      return -1;
    }
    if (tmp.getSortValue() < getSortValue()) {
      return 1;
    }
    return 0;
  }

  /**
   * Overrides the toString method with something more usefull
   * 
   * @return The description of this League Owner
   */
  public String toString() {
    String owner = this.getLeagueOwner().getName();
    String name = this.getName();
    return owner + " : " + name;
  }

  /**
   * Adds this league to a given XML Element
   * 
   * @param parent
   *          Element
   */
  public abstract void addAsElementToParent(Element parent);

  /**
   * Adds a team to a list of teams who shall be removed at the end of a year.
   * 
   * @param team
   *          Team
   */
  public void addTeamToRemove(Team team) {
    this.teamsToRemove.add(team);
  }

  /**
   * Adds a team to a list of teams to be added next Year.
   * 
   * @param team
   *          Team
   */
  public void addTeamToAdd(Team team) {
    this.teamsToAdd.add(team);
  }

  /**
   * Refreshes the team (addes / removes teams at the end of a year)
   */
  public void refreshTeamList() {
    Vector<Team> newTeams = new Vector<Team>();
    for (int i = 0; i < teams.length; i++) {
      newTeams.addElement(teams[i]);
    }

    for (int i = 0; i < teamsToRemove.size(); i++) {
      newTeams.remove(teamsToRemove.get(i));
    }

    for (int i = 0; i < teamsToAdd.size(); i++) {
      newTeams.add(teamsToAdd.get(i));
    }

    this.teams = newTeams.toArray(new Team[newTeams.size()]);

    teamsToAdd = new Vector<Team>();
    teamsToRemove = new Vector<Team>();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.icehockeymanager.ihm.game.scheduler.events.SchedulerEventOwner#getTMScheduler()
   */
  public TMScheduler getTMScheduler() {
    return SchedulerTools.getTMSchedulerEventsBySource(this, true);
  }

}
