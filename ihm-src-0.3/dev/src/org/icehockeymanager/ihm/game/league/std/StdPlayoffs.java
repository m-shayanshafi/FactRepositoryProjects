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
  
package org.icehockeymanager.ihm.game.league.std;

import java.io.*;
import java.util.*;

import org.icehockeymanager.ihm.game.*;
import org.icehockeymanager.ihm.game.league.*;
import org.icehockeymanager.ihm.game.league.events.*;
import org.icehockeymanager.ihm.game.league.helper.*;
import org.icehockeymanager.ihm.game.league.std.events.*;
import org.icehockeymanager.ihm.game.match.*;
import org.icehockeymanager.ihm.game.player.*;
import org.icehockeymanager.ihm.game.scheduler.*;
import org.icehockeymanager.ihm.game.team.*;

/**
 * StdPlayoffs Implementation of playoffs. - Creates matches & GameDayEvents and
 * post them in the calendar.
 * 
 * @author Bernhard von Gunten
 * @created December, 2001
 */
public class StdPlayoffs extends LeagueElement implements Serializable {

  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 3977584692780545847L;

  /** Array of Competitions [teams/2] */
  private PlayoffCompetition[] playoffCompetitions = null;

  /** Array of possible gameDates for additional matches */
  private Calendar[] addGameDates;

  /** Maximum count of matches between two teams */
  private int bestOf;

  /** Count of competitions (4 if 8 teams are in playoffs) */
  private int competitionsCount;

  /** Teams of this playoffs */
  private Team[] teams;

  /**
   * Create empty Playoffs
   * 
   * @param rank
   *          Rank of leagueElement
   * @param name
   *          Name of this playoffs
   * @param league
   *          League of this playoffs
   * @param bestOf
   *          Best of n Playoffs
   * @param level 
   */
  public StdPlayoffs(int rank, String name, League league, int bestOf, int level) {
    super(rank, name, league, level);
    this.bestOf = bestOf;
  }

  /**
   * Init Playoffs
   * 
   * @param teams
   *          Teams in this playoffs
   * @param SchedulerHelper
   *          SchedulerHelper for match announcements
   */
  public void init(Team[] teams, SchedulerHelper SchedulerHelper) {
    this.teams = teams;
    setAnnounced();
    this.competitionsCount = teams.length / 2;
    playoffCompetitions = new PlayoffCompetition[competitionsCount];
    addGameDates = new Calendar[(bestOf - 1) / 2];
    generateTeamStats();
    generateMatches();
    generateCalendarEvents(SchedulerHelper);
  }

  /**
   * Returns teams of this leagueElement
   * 
   * @return The teams value
   */
  public Team[] getTeams() {
    if (teams == null) {
      return new Team[0];
    } else {
      return teams;
    }
  }

  /**
   * Returns all players in these playoffs.
   * 
   * @return Player[]
   */
  public Player[] getPlayers() {
    if (teams == null) {
      return new Player[0];
    } else {
      Vector<Player> tmp = new Vector<Player>();
      Team[] teams = getTeams();
      for (int i = 0; i < teams.length; i++) {
        Player[] players = teams[i].getPlayers();
        for (int n = 0; n < players.length; n++) {
          tmp.add(players[n]);
        }
      }

      return tmp.toArray(new Player[tmp.size()]);

    }
  }

  /** Generates schedule for this playoffs */
  public void generateMatches() {
    Rules rules = new StdPlayoffRules();
    for (int i = 0; i < playoffCompetitions.length; i++) {
      playoffCompetitions[i] = new PlayoffCompetition(teams[i], teams[teams.length - i - 1], bestOf);
      for (int n = 0; n < bestOf; n++) {
        if (n % 2 == 0) {
          playoffCompetitions[i].addMatch(createMatch(this, teams[i], teams[teams.length - i - 1], rules), n);
        } else {
          playoffCompetitions[i].addMatch(createMatch(this, teams[teams.length - i - 1], teams[i], rules), n);
        }
      }
    }
  }

  /**
   * Generate GameDayEvents (and their StopBeforeEvents) for playoff games and
   * make "reservations" for more games if needed
   * 
   * @param SchedulerHelper
   *          SchedulerHelper for match announcements
   */
  public void generateCalendarEvents(SchedulerHelper SchedulerHelper) {
    // Place fix games
    Calendar day = GameController.getInstance().getScenario().getScheduler().getToday();
    // Fill with today, just in case
    for (int n = 0; n < (bestOf + 1) / 2; n++) {
      day = SchedulerHelper.getNextFreeGameDay();
      GameDayMatchesEvent event = new GameDayMatchesEvent(this, day, new Vector<Match>(), this, this.getNameKey(), n + 1);
      for (int i = 0; i < playoffCompetitions.length; i++) {
        event.addMatch(playoffCompetitions[i].getMatch(n));
      }
      /*
       * GameDayArrivedEvent tmp = GameController.getInstance().getScenario().
       * getScheduler().getStopBeforeGamedayEvent(day);
       * tmp.addSchedulerGameDayEvent(event);
       */
      GameDayArrivedEvent tmp = new GameDayArrivedEvent(this, day);
      tmp.addSchedulerGameDayEvent(event);
      GameController.getInstance().getScenario().getScheduler().addEvent(tmp);

      GameController.getInstance().getScenario().getScheduler().addEvent(event);
    }

    // Add a first PlayoffRoundFinished event after first half of playoff
    GameController.getInstance().getScenario().getScheduler().addEvent(new StdPlayoffRoundFinishedEvent(this, day, this, (bestOf - 1) / 2));

    // "Reservation" for additional games
    for (int n = 0; n < (bestOf - 1) / 2; n++) {
      addGameDates[n] = SchedulerHelper.getNextFreeGameDay();
    }
  }

  /**
   * Playoff round is played. Look if further matches are needed
   * 
   * @param playedRound
   *          The nr of the played round
   */
  public void roundPlayed(int playedRound) {
    boolean postEvent = false;
    Calendar day = addGameDates[playedRound - ((bestOf - 1) / 2)];
    GameDayMatchesEvent event = new GameDayMatchesEvent(this, day, new Vector<Match>(), this, this.getNameKey(), playedRound + 2);
    for (int i = 0; i < playoffCompetitions.length; i++) {
      if (!playoffCompetitions[i].isCompetitionWon()) {
        postEvent = true;
        event.addMatch(playoffCompetitions[i].getMatch(playedRound + 1));
      }
    }
    if (postEvent) {
      /*
       * GameDayArrivedEvent tmp = GameController.getInstance().getScenario().
       * getScheduler().getStopBeforeGamedayEvent(day);
       * tmp.addSchedulerGameDayEvent(event);
       */
      GameDayArrivedEvent tmp = new GameDayArrivedEvent(this, day);
      tmp.addSchedulerGameDayEvent(event);
      GameController.getInstance().getScenario().getScheduler().addEvent(tmp);

      GameController.getInstance().getScenario().getScheduler().addEvent(event);
      if (playedRound < bestOf - 2) {
        GameController.getInstance().getScenario().getScheduler().addEvent(new StdPlayoffRoundFinishedEvent(this, day, this, playedRound + 1));
      }
    }
  }

  /**
   * Returns the winners of this playoffs
   * 
   * @return The winners value
   */
  public Team[] getWinners() {
    return getWinnersOrLosers(true);
  }

  /**
   * Returns the losers of this playoffs
   * 
   * @return The losers value
   */
  public Team[] getLosers() {
    return getWinnersOrLosers(false);
  }

  /**
   * Returns the winners or losers of this playoffs
   * 
   * @param winners
   *          If winners (true) or losers (false) are searched.
   * @return An array of teams
   */
  private Team[] getWinnersOrLosers(boolean winners) {
    Team[] result = new Team[competitionsCount];

    // Calculate winner
    for (int i = 0; i < playoffCompetitions.length; i++) {
      if (winners) {
        result[i] = playoffCompetitions[i].getWinner();
      } else {
        result[i] = playoffCompetitions[i].getLoser();
      }
    }
    return result;
  }

  /**
   * Returns all competitions
   * 
   * @return The playoffCompetitions value
   */
  public PlayoffCompetition[] getPlayoffCompetitions() {
    return playoffCompetitions;
  }

  /**
   * Returns the standings of this playoffs
   * 
   * @return The leagueStandings value
   */
  public LeagueStandings getLeagueStandings() {
    return new LeagueStandingsPlayoffs(this, getPlayoffCompetitions());
  }

}
