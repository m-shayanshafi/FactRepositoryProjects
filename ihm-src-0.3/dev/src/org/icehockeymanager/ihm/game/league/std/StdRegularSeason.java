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
import org.icehockeymanager.ihm.game.match.*;
import org.icehockeymanager.ihm.game.player.*;
import org.icehockeymanager.ihm.game.scheduler.*;
import org.icehockeymanager.ihm.game.team.*;

/**
 * StdRegularSeason Implementation of a regular season schedule like used in
 * Switzerland.
 * <p>- Creates schedule for home and away games in a season and places them
 * into the game calendar.
 * 
 * @author Bernhard von Gunten
 * @created December, 2001
 */
public class StdRegularSeason extends LeagueElement implements Serializable {

  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 3258128072333866802L;

  /** Array of matches played this season */
  private Match[][] schedule = null;

  /** Games per Round */
  private int gamesPerRound = 0;

  /** Teams in this regular Season */
  private Team[] teams = null;

  /**
   * Constructor with teams, multiRounds, SchedulerHelper and league
   * 
   * @param rank
   *          Rank of leagueElement
   * @param name
   *          Name of this regular season
   * @param league
   *          League of this regular season
   */
  public StdRegularSeason(int rank, String name, League league) {
    super(rank, name, league);
  }

  /**
   * Starts the regular season, creates gameplan etc
   * 
   * @param teams
   *          The teams of this regular season
   * @param multiRounds
   *          The count of rounds
   * @param SchedulerHelper
   *          The calendar helper for match announcements
   */
  public void init(Team[] teams, int multiRounds, SchedulerHelper SchedulerHelper) {
    this.teams = teams;
    setAnnounced();
    generateTeamStats();
    generateGames(multiRounds);
    generateCalendarEvents(SchedulerHelper);
  }

  /**
   * Returns teams of this regular season
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
   * Gets the players attribute of the StdRegularSeason object
   * 
   * @return The players value
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

  /**
   * Generate GameDayEvents (and their StopBeforeEvents) in the calendar.
   * 
   * @param SchedulerHelper
   *          SchedulerHelper SchedulerHelper
   */
  public void generateCalendarEvents(SchedulerHelper SchedulerHelper) {
    for (int i = 0; i < schedule.length; i++) {
      Calendar day = SchedulerHelper.getNextFreeGameDay();
      GameDayMatchesEvent event = new GameDayMatchesEvent(this, day, new Vector<Match>(), this, this.getNameKey(), i + 1);
      for (int n = 0; n < gamesPerRound; n++) {
        event.addMatch(schedule[i][n]);
      }
      GameDayArrivedEvent tmp = new GameDayArrivedEvent(this, day);
      tmp.addSchedulerGameDayEvent(event);
      GameController.getInstance().getScenario().getScheduler().addEvent(tmp);

      GameController.getInstance().getScenario().getScheduler().addEvent(event);
    }
  }

  /**
   * Helper that returns the teams in sorted order (from the standings)
   * 
   * @return The leagueStandingsTeams value
   */
  public Team[] getLeagueStandingsTeams() {
    TeamStats[] tmp = new TeamStats[teams.length];
    for (int i = 0; i < teams.length; i++) {
      tmp[i] = teams[i].getStats(this);
      tmp[i].setSortCriteria(TeamStats.SORT_POINTS);
      tmp[i].setSortOrder(false);
      // Descending !!
    }

    java.util.Arrays.sort(tmp);

    Team[] result = new Team[tmp.length];
    for (int i = 0; i < tmp.length; i++) {
      result[i] = tmp[i].getTeam();
    }

    return result;
  }

  /**
   * Returns the standings of this regular season
   * 
   * @return The leagueStandings value
   */
  public LeagueStandings getLeagueStandings() {
    TeamStats[] tmp = new TeamStats[teams.length];
    for (int i = 0; i < teams.length; i++) {
      tmp[i] = teams[i].getStats(this);
    }
    return new LeagueStandingsTable(this, tmp);
  }

  /**
   * Generates the schedule with matches
   * 
   * @param multiRounds
   *          Count of rounds
   */
  private void generateGames(int multiRounds) {

    // get the tules
    StdRegularSeasonRules rules = new StdRegularSeasonRules();

    int teamCount = teams.length;
    boolean zusatzTeam = false;

    if (teams.length % 2 != 0) {
      teamCount++;
      zusatzTeam = true;
    }

    this.gamesPerRound = teamCount / 2;

    int scheduleRound = 0;
    int[][] table = new int[teamCount][teamCount];

    for (int i = 0; i < teamCount; i++) {
      for (int j = 0; j < teamCount; j++) {
        if (i == j) {
          table[i][j] = -1;
        } else {
          if (j == teamCount - 1) {
            table[i][j] = (j + 2 * i);
            while (table[i][j] >= teamCount) {
              table[i][j] -= (teamCount - 1);
            }
          } else {
            if (j > i) {
              table[i][j] = i + j;
              while (table[i][j] >= teamCount) {
                table[i][j] -= (teamCount - 1);
              }
            } else {
              table[i][j] = table[j][i] + teamCount - 1;
            }
          }
        }
      }
    }

    for (int i = 0; i < teamCount; i++) {
      for (int j = i; j < teamCount; j++) {
        scheduleRound = table[i][j];
        if (!(scheduleRound % 2 == 0)) {
          table[i][j] = table[j][i];
          table[j][i] = scheduleRound;
        }
      }
    }

    // Fill the schedule sorted by round ...
    int rounds = (teamCount * 2 - 2) * multiRounds;

    if (zusatzTeam) {
      gamesPerRound--;
    }
    schedule = new Match[rounds][gamesPerRound];

    int games = 0;
    for (int round = 0; round < teamCount * 2 - 1; round++) {
      for (int i = 0; i < teamCount; i++) {
        if (!zusatzTeam || i != teamCount - 1) {
          for (int j = 0; j < teamCount; j++) {
            if (!zusatzTeam || j != teamCount - 1) {
              scheduleRound = table[i][j];
              if (scheduleRound != -1 && round == scheduleRound) {
                for (int mr = 0; mr < multiRounds; mr++) {
                  schedule[scheduleRound - 1 + (rounds / multiRounds) * mr][games] = this.createMatch(this, teams[i], teams[j], rules);
                }
                games++;
                if (games == gamesPerRound) {
                  games = 0;
                }
              }
            }
          }
        }
      }
    }

  }

}
