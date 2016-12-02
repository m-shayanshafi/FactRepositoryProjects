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
  
package org.icehockeymanager.ihm.game.match;

import org.icehockeymanager.ihm.game.league.helper.*;
import org.icehockeymanager.ihm.game.player.*;
import org.icehockeymanager.ihm.game.team.*;

/**
 * Goal class contains goal data
 * 
 * @author Arik Dasen & Bernhard von Gunten
 * @created December, 2001
 */
public class MatchDataGoal extends MatchData {
  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 3904955365530613559L;

  private Player player;

  private Player assist1;

  private Player assist2;

  private Team teamAgainst;

  private Team team;

  /**
   * Constructor for the Goal object
   * 
   * @param time
   *          Time of goal
   * @param team
   *          Team of goal
   * @param teamAgainst
   *          Against team of goal
   * @param player
   *          Player of this goal
   * @param assist1
   *          Assistant 1 of this goal
   * @param assist2
   *          Assistant 2 of this goal
   */
  public MatchDataGoal(int time, Team team, Team teamAgainst, Player player, Player assist1, Player assist2) {
    super(time);
    this.team = team;
    this.player = player;
    this.assist1 = assist1;
    this.assist2 = assist2;
    this.teamAgainst = teamAgainst;
  }

  /**
   * Gets the player attribute of the Goal object
   * 
   * @return The player value
   */
  public Player getPlayer() {
    return player;
  }

  /**
   * Gets the assist1 attribute of the Goal object
   * 
   * @return The assist1 value
   */
  public Player getAssist1() {
    return assist1;
  }

  /**
   * Gets the assist2 attribute of the Goal object
   * 
   * @return The assist2 value
   */
  public Player getAssist2() {
    return assist2;
  }

  /**
   * Gets the teamAgainst attribute of the Goal object
   * 
   * @return The teamAgainst value
   */
  public Team getTeamAgainst() {
    return teamAgainst;
  }

  /**
   * Update team and player stats
   * 
   * @param leagueElement
   *          LeagueElement of this goal (match).
   */
  public void updateStats(LeagueElement leagueElement) {
    TeamStats[] teamStats = team.getAllStats(leagueElement);
    TeamStats[] teamStatsAgainst = teamAgainst.getAllStats(leagueElement);
    PlayerStats[] playerStats = player.getAllStats(leagueElement);
    PlayerStats[] assist1Stats = null;
    PlayerStats[] assist2Stats = null;
    if (assist1 != null) {
      assist1Stats = assist1.getAllStats(leagueElement);
    }
    if (assist2 != null) {
      assist2Stats = assist2.getAllStats(leagueElement);
    }
    for (int i = 0; i < teamStats.length; i++) {
      playerStats[i].addGoals(1);
      if (assist1 != null) {
        assist1Stats[i].addAssists(1);
      }
      if (assist2 != null) {
        assist2Stats[i].addAssists(1);
      }
      teamStats[i].addGoalsFor(1);
      teamStatsAgainst[i].addGoalsAgainst(1);
    }
  }

  /** Returns the scoring team 
   * @return Team */
  public Team getTeam() {
    return team;
  }

}
