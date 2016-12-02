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

import org.icehockeymanager.ihm.game.team.*;

/**
 * LeagueStandingsTable Helper class containing table (array of teams) of a
 * LeagueElement
 * 
 * @author Bernhard von Gunten / Arik Dasen
 * @created December, 2001
 */
public class LeagueStandingsTable extends LeagueStandings {

  /** Table (array of teams) */
  private TeamStats[] teamStats = null;

  /**
   * Construct Standings
   * 
   * @param leagueElement
   *          The leagueElement of this standings
   * @param teamStats
   *          The teamStats of this standings
   */
  public LeagueStandingsTable(LeagueElement leagueElement, TeamStats[] teamStats) {
    super(leagueElement);
    this.teamStats = teamStats;
  }

  /**
   * Returns standings (array of teamStats)
   * 
   * @return The standings value
   */
  public TeamStats[] getStandings() {
    return teamStats;
  }

}
