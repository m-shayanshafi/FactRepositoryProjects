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

import org.icehockeymanager.ihm.game.player.*;
import org.icehockeymanager.ihm.game.team.*;
import org.w3c.dom.*;

/**
 * LeagueOwner Is owner of a list of leagues within i.e. a country.
 * <p>
 * <p>
 * Doesn't create the leagues himself. Gets them by constructor params.
 * 
 * @author Bernhard von Gunten / Arik Dasen
 * @created December, 2001
 */
public abstract class LeagueOwner implements TeamOwner, PlayerOwner, Serializable {

  /** Array of leagues */
  protected League[] leagues = null;

  /** Name of LeagueOwner */
  private String name = null;

  /**
   * Constructor (also makes a link into the leagues
   * 
   * @param name
   *          Name of this leagueOwner
   * @param leagues
   *          Leagues of this leagueOwner
   */
  public LeagueOwner(String name, League[] leagues) {
    lateConstruct(name, leagues);
  }

  /**
   * Late construct to finish the leagueOwner
   * 
   * @param name
   *          String
   * @param leagues
   *          League[]
   */
  public void lateConstruct(String name, League[] leagues) {
    this.name = name;
    this.leagues = leagues;

    // Give all leagues a link to their owner
    for (int i = 0; i < leagues.length; i++) {
      leagues[i].setLeagueOwner(this);
    }

    // Sort leagues by rank
    java.util.Arrays.sort(leagues);
  }

  /**
   * Returns all leagues
   * 
   * @return The leagues value
   */
  public League[] getLeagues() {
    return leagues;
  }

  /**
   * Returns all teams from all leagues
   * 
   * @return The teams value
   */
  public Team[] getTeams() {
    Vector<Team> result = new Vector<Team>();

    for (int i = 0; i < leagues.length; i++) {
      Team[] tmp = leagues[i].getTeams();
      for (int n = 0; n < tmp.length; n++) {
        result.add(tmp[n]);
      }
    }

    return result.toArray(new Team[result.size()]);
  }

  /**
   * Returns all players from all leagues
   * 
   * @return The players value
   */
  public Player[] getPlayers() {
    Vector<Player> tmp = new Vector<Player>();
    Team[] teams = getTeams();
    for (int i = 0; i < teams.length; i++) {
      Team team = teams[i];
      Player[] players = team.getPlayers();
      for (int n = 0; n < players.length; n++) {
        tmp.add(players[n]);
      }
    }

    return tmp.toArray(new Player[tmp.size()]);
  }

  /**
   * Returns the name of this Owner
   * 
   * @return The name value
   */
  public String getName() {
    return name;
  }

  /** Resets all leagues (new Season) */
  public abstract void newSeason();

  /** Resets all leagues (end Season) */
  public abstract void endSeason();

  /**
   * Overrides the toString method with something more usefull
   * 
   * @return The description of this League Owner
   */
  public String toString() {
    return name;
  }

  /**
   * Adds this leagueOwner to a given XML element.
   * 
   * @param parent
   *          Element
   */
  public abstract void addAsElementToParent(Element parent);

}
