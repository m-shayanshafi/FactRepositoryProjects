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

/**
 * LeagueStandingsPlayoffs Helper class containing PlayoffCompetitions of a
 * LeagueElement
 * 
 * @author Bernhard von Gunten / Arik Dasen
 * @created December, 2001
 */
public class LeagueStandingsPlayoffs extends LeagueStandings {

  /** Array of PlayoffCompetitions */
  private PlayoffCompetition[] competitions = null;

  /**
   * Constructs Standings
   * 
   * @param leagueElement
   *          The leagueElement of this standings
   * @param competitions
   *          The playoff competitions of this standings
   */
  public LeagueStandingsPlayoffs(LeagueElement leagueElement, PlayoffCompetition[] competitions) {
    super(leagueElement);
    this.competitions = competitions;
  }

  /**
   * Return all PlayoffCompetitions
   * 
   * @return The playoffCompetitions value
   */
  public PlayoffCompetition[] getPlayoffCompetitions() {
    return competitions;
  }

}
