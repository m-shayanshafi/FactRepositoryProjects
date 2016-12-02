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
  
package org.icehockeymanager.ihm.game.scenario;

import java.io.*;
import java.util.*;

import org.icehockeymanager.ihm.game.league.*;
import org.icehockeymanager.ihm.game.league.helper.*;
import org.icehockeymanager.ihm.game.match.*;
import org.icehockeymanager.ihm.game.player.*;
import org.icehockeymanager.ihm.game.randomimpacts.*;
import org.icehockeymanager.ihm.game.scenario.events.*;
import org.icehockeymanager.ihm.game.scheduler.*;
import org.icehockeymanager.ihm.game.team.*;
import org.icehockeymanager.ihm.game.user.*;
import org.icehockeymanager.ihm.game.injuries.*;
import org.icehockeymanager.ihm.game.sponsoring.*;
import org.icehockeymanager.ihm.game.eventlog.*;

/**
 * The scenario class is hosting the whole game. (Settings, standings etc.)
 * 
 * @author Bernhard von Gunten
 * @created December, 2001
 */
public class Scenario implements TeamStatsOwner, PlayerStatsOwner, TeamOwner, PlayerOwner, Serializable {

  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 3907217043768946741L;

  /** All Teams in this scenario */
  private Team[] teams;

  /** All players in this scenario */
  private Player[] players;

  /** All LeagueOwners (i.E. contries) in this scenario */
  private LeagueOwner[] leagueOwners;

  /** All users playing this scenario */
  private User[] users;

  /** Scheduler in this scenario */
  private Scheduler scheduler = null;

  /** Event log */
  private EventLog eventLog = null;

  /** Coutries */
  private Country[] countries = null;

  /** Scenario Settings */
  private ScenarioSettings scenarioSettings = null;

  /** Random object of this scenario */
  private Random random = null;

  /** Family names */
  private String[] lastNames = null;

  /** First names */
  private String[] firstNames = null;

  /** Hightes Player key */
  private int playerKey = 0;

  /** Injuries */
  private InjuryData[] injuries = null;

  /** RandomImpacts fir Teams */
  private RandomImpactTeam[] randomImpactsTeam = null;

  /** RandomImpacts for players */
  private RandomImpactPlayer[] randomImpactsPlayer = null;

  /** Sponsors */
  private SponsorData[] sponsors = null;

  /**
   * Scenario constructor
   * 
   * @param leagueOwners
   * @param teams
   * @param players
   * @param users
   * @param countries
   * @param injuries
   * @param randomImpacts
   * @param lastNames
   * @param firstNames
   * @param scenarioSettings
   * @param sponsors 
   */
  public Scenario(LeagueOwner[] leagueOwners, Team[] teams, Player[] players, User[] users, Country[] countries, InjuryData[] injuries, RandomImpact[] randomImpacts, String[] lastNames, String[] firstNames, ScenarioSettings scenarioSettings,
      SponsorData[] sponsors) {

    // Init randomizer
    this.random = new Random();

    this.leagueOwners = leagueOwners;
    this.teams = teams;
    this.players = players;
    this.users = users;
    this.scenarioSettings = scenarioSettings;
    this.countries = countries;
    this.lastNames = lastNames;
    this.firstNames = firstNames;
    this.injuries = injuries;
    this.sponsors = sponsors;

    // Split up random impacts to teams & players
    Vector<RandomImpact> reTeam = new Vector<RandomImpact>();
    Vector<RandomImpact> rePlayer = new Vector<RandomImpact>();
    for (int i = 0; i < randomImpacts.length; i++) {
      if (randomImpacts[i] instanceof RandomImpactTeam) {
        reTeam.add(randomImpacts[i]);
      } else if (randomImpacts[i] instanceof RandomImpactPlayer) {
        rePlayer.add(randomImpacts[i]);
      }
    }
    randomImpactsTeam = reTeam.toArray(new RandomImpactTeam[reTeam.size()]);
    randomImpactsPlayer = rePlayer.toArray(new RandomImpactPlayer[rePlayer.size()]);

    // Set (highest) player key
    for (int i = 0; i < players.length; i++) {
      if (players[i].getKey() > playerKey) {
        playerKey = players[i].getKey();
      }
    }

    scheduler = new Scheduler();
    eventLog = new EventLog();

    // Init calendar on september 1st this year
    Calendar scenarioYear = Calendar.getInstance();
    scenarioYear.set(Calendar.MONTH, Calendar.SEPTEMBER);
    scenarioYear.set(Calendar.DATE, 1);

    // tmp.setTime(date);
    scheduler.setStartDate(scenarioYear);
    scheduler.addEvent(new ScenarioResetEvent(this, scenarioYear));

  }

  /**
   * Wrapper of the constructor without users
   * 
   * @param leagueOwners
   * @param teams
   * @param players
   * @param countries
   * @param injuries
   * @param randomImpacts
   * @param lastNames
   * @param firstNames
   * @param scenarioSettings
   * @param sponsors
   */
  public Scenario(LeagueOwner[] leagueOwners, Team[] teams, Player[] players, Country[] countries, InjuryData[] injuries, RandomImpact[] randomImpacts, String[] lastNames, String[] firstNames, ScenarioSettings scenarioSettings, SponsorData[] sponsors) {
    this(leagueOwners, teams, players, null, countries, injuries, randomImpacts, lastNames, firstNames, scenarioSettings, sponsors);
  }

  /**
   * Returns the next available player key iE for prospects
   * 
   * @return playerKey
   */
  public int getNextPlayerKey() {
    playerKey++;
    return playerKey;
  }

  /**
   * Returns all injuries
   * 
   * @return injuries
   */
  public InjuryData[] getInjuries() {
    return injuries;
  }

  /**
   * Returns all sponsors
   * 
   * @return sponsors
   */
  public SponsorData[] getSponsors() {
    return sponsors;
  }

  /**
   * Returns random impacts for teams
   * 
   * @return randomImpactsTeam
   */
  public RandomImpactTeam[] getRandomImpactsTeam() {
    return randomImpactsTeam;
  }

  /**
   * Returns random impacts for players
   * 
   * @return randomImpactsPlayer
   */
  public RandomImpactPlayer[] getRandomImpactsPlayer() {
    return randomImpactsPlayer;
  }

  /**
   * Returns the scenarios random object.
   * 
   * @return Random Random object
   */
  public Random getRandom() {
    return random;
  }

  /**
   * Returns a random int (including 0 & maxInt)
   * @param maxInt 
   * 
   * @return random int
   */
  public int getRandomInt(int maxInt) {
    if (maxInt < 1) {
      return 0;
    } else {
      return random.nextInt(maxInt + 1);
    }
  }

  /**
   * Returns true or false :-)
   * 
   * @return trueOrfalse
   */
  public boolean getRandomBoolean() {
    return random.nextBoolean();
  }

  /**
   * Returns a random int between two ints (including maxInt)
   * 
   * @param minInt
   * @param maxInt
   * @return random int
   */
  public int getRandomInt(int minInt, int maxInt) {
    return getRandomInt(maxInt - minInt) + minInt;
  }

  /**
   * Returns all last names
   * 
   * @return lastNames
   */
  public String[] getLastNames() {
    return lastNames;
  }

  /**
   * Returns all first names
   * 
   * @return firstNames
   */
  public String[] getFirstNames() {
    return firstNames;
  }

  /**
   * Returns Scheduler of this scenario
   * 
   * @return The Scheduler of this scenario
   */
  public Scheduler getScheduler() {
    return scheduler;
  }

  /**
   * Returns event log of this scenario
   * 
   * @return eventLog
   */
  public EventLog getEventLog() {
    return eventLog;
  }

  /**
   * Returns the scenario Settings object.
   * 
   * @return ScenarioSettings ScenarioSettings
   */
  public ScenarioSettings getScenarioSettings() {
    return scenarioSettings;
  }

  /** Ends the season (for history backup etc.) */
  public void endSeason() {
    // Save Player stats history
    for (int i = 0; i < teams.length; i++) {
      teams[i].endSeason();
    }
    // Save player stats history
    Player[] aPlayer = this.getPlayers();
    int iSize = aPlayer.length;
    for (int i = 0; i < iSize; i++) {
      aPlayer[i].endSeason();

    }

    // LeagueOwners
    for (int i = 0; i < leagueOwners.length; i++) {
      leagueOwners[i].endSeason();
    }
  }

  /** Reset the whole scneario (new year) */
  public void newSeason() {

    // Reset player (Stats)
    Player[] aPlayer = this.getPlayers();
    for (int i = 0; i < aPlayer.length; i++) {
      aPlayer[i].newSeason();
    }

    // Reset team (Stats)
    for (int i = 0; i < teams.length; i++) {
      teams[i].newSeason();
    }

    // Reset leagues
    for (int i = 0; i < leagueOwners.length; i++) {
      leagueOwners[i].newSeason();
    }

    // Reset users
    for (int i = 0; i < users.length; i++) {
      users[i].newSeason();
      // Reset interests
      // Generate automated interests ... (very cool)
      for (int l = 0; l < leagueOwners.length; l++) {
        League[] leagues = leagueOwners[l].getLeagues();
        for (int q = 0; q < leagues.length; q++) {
          if (leagues[q].isTeamOfLeague(users[i].getTeam())) {
            Vector<LeagueElement> leagueElements = leagues[q].getLeagueElements();
            for (int n = 0; n < leagueElements.size(); n++) {
              users[i].addInterestedLeagueElement(leagueElements.get(n));
            }
          }
        }
      }
    }


  }

  /**
   * Returns all LeagueOwner of this scenario
   * 
   * @return The leagueOwners value
   */
  public LeagueOwner[] getLeagueOwners() {
    return leagueOwners;
  }

  /**
   * Returns all teams in this scenario
   * 
   * @return The teams value
   */
  public Team[] getTeams() {
    return teams;
  }

  /**
   * Returns all players in this scenario
   * 
   * @return The players value
   */
  public Player[] getPlayers() {
    return players;
  }

  /**
   * Returns all players in this scenario
   * 
   * @return The freePlayers value
   */
  public Player[] getFreePlayers() {
    Vector<Player> tmpPlayers = new Vector<Player>();

    for (int i = 0; i < players.length; i++) {
      if (players[i].getTeam() == null) {
        tmpPlayers.add(players[i]);
      }
    }

    return tmpPlayers.toArray(new Player[tmpPlayers.size()]);
  }

  /**
   * Returns all players within teams
   * 
   * @return Player[] Players employed in team
   */
  public Player[] getEmployedPlayers() {
    Vector<Player> tmpPlayers = new Vector<Player>();

    for (int i = 0; i < players.length; i++) {
      if (players[i].getTeam() != null) {
        tmpPlayers.add(players[i]);
      }
    }

    return tmpPlayers.toArray(new Player[tmpPlayers.size()]);
  }

  /**
   * Gets the playersTotalStats attribute of the Scenario object
   * 
   * @return The playersTotalStats value
   */
  public PlayerStats[] getPlayersTotalStats() {
    Player[] aPlayer = this.getPlayers();
    PlayerStats[] result = new PlayerStats[aPlayer.length];

    for (int i = 0; i < result.length; i++) {
      result[i] = aPlayer[i].getPlayerStatsTotal();
    }
    return result;
  }

  /**
   * Gets the freePlayersTotalStats attribute of the Scenario object
   * 
   * @return The freePlayersTotalStats value
   */
  public PlayerStats[] getFreePlayersTotalStats() {
    Player[] freePlayers = getFreePlayers();
    PlayerStats[] result = new PlayerStats[freePlayers.length];
    for (int i = 0; i < result.length; i++) {
      result[i] = freePlayers[i].getPlayerStatsTotal();
    }
    return result;
  }

  /**
   * Gets the teamsTotalStats attribute of the Scenario object
   * 
   * @return The teamsTotalStats value
   */
  public TeamStats[] getTeamStats() {
    TeamStats[] result = new TeamStats[teams.length];
    for (int i = 0; i < result.length; i++) {
      result[i] = teams[i].getTeamStatsTotal();
    }
    return result;
  }

  /**
   * Returns all leagueElements in this scenario
   * 
   * @return The allLeagueElements value
   */
  public Vector<LeagueElement> getAllLeagueElements() {
    Vector<LeagueElement> result = new Vector<LeagueElement>();
    for (int l = 0; l < leagueOwners.length; l++) {
      League[] leagues = leagueOwners[l].getLeagues();
      for (int q = 0; q < leagues.length; q++) {
        Vector<LeagueElement> leagueElements = leagues[q].getLeagueElements();
        for (int n = 0; n < leagueElements.size(); n++) {
          result.add(leagueElements.get(n));
        }
      }
    }
    return result;
  }

  /**
   * Returns all User of this scneario
   * 
   * @return The users value
   */
  public User[] getUsers() {
    return users;
  }

  /**
   * Sets the users
   * 
   * @param users
   */
  public void setUsers(User[] users) {
    this.users = users;
  }

  /**
   * Returns the owner of a team
   * 
   * @param team
   *          Team
   * @return The user/owner
   */
  public User getUserByTeam(Team team) {
    User[] users = getUsers();
    for (int i = 0; i < users.length; i++) {
      if (users[i].getTeam().equals(team)) {
        return users[i];
      }
    }
    return null;
  }

  /**
   * Returns a team by a player
   * 
   * @param player
   * @return team (null if no team is found)
   */
  public Team getTeamByPlayer(Player player) {
    for (int i = 0; i < teams.length; i++) {
      for (int q = 0; q < teams[i].getPlayers().length; q++) {
        if (teams[i].getPlayers()[q].equals(player)) {
          return teams[i];
        }
      }
    }
    // No team
    return null;
  }

  /**
   * Returns true if team is managed by the computer
   * 
   * @param team
   *          Description of the Parameter
   * @return The autoTeam value
   */
  public boolean isAutoTeam(Team team) {
    User[] users = getUsers();
    for (int i = 0; i < users.length; i++) {
      if (users[i].getTeam().equals(team)) {
        return false;
      }
    }
    return true;
  }

  /**
   * Returns all countries
   * 
   * @return countries
   */
  public Country[] getCountries() {
    return countries;
  }

  /**
   * Returns country by key
   * 
   * @param key
   * @return country (null if not found)
   */
  public Country getCountryByKey(String key) {
    for (int i = 0; i < countries.length; i++) {
      if (countries[i].getKey().equals(key)) {
        return countries[i];
      }
    }
    return null;
  }

  /**
   * Returns all users who are involved in one of the matches passed by
   * 
   * @param matches
   *          The matches to look in
   * @param interestedOnly
   *          Return only users who are interested in matches
   * @return A Vector of users
   */
  public Vector<User> getUsersWithinMatches(Vector<Match> matches, boolean interestedOnly) {
    Vector<User> result = new Vector<User>();
    for (int n = 0; n < users.length; n++) {
      for (int i = 0; i < matches.size(); i++) {
        Match tmp = matches.get(i);
        if (interestedOnly) {
          LeagueElement le = tmp.getLeagueElement();
          if (users[n].isInterestedInLeagueElement(le)) {
            if (tmp.isUserInvolved(users[n])) {
              result.add(users[n]);
            }
          }
        } else {
          if (tmp.isUserInvolved(users[n])) {
            result.add(users[n]);
          }
        }
      }
    }
    return result;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.icehockeymanager.ihm.game.player.PlayerStatsOwner#getPlayerStats()
   */
  public PlayerStats[] getPlayerStats() {
    throw new IllegalArgumentException("Call either getPlayersTotalStats or getFreePlayersTotalStats");
  }

}