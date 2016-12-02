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
  
package org.icehockeymanager.ihm.clients.devgui.ihm.league;

import javax.swing.table.*;

import org.icehockeymanager.ihm.clients.devgui.controller.*;
import org.icehockeymanager.ihm.game.team.*;
import org.icehockeymanager.ihm.lib.*;

/**
 * Table model to show the actual leagueStandings.
 * 
 * @author Bernhard von Gunten
 * @created December 29, 2001
 */
public class TMLeagueStandingsTable extends AbstractTableModel implements IhmTableModelSorter {

  static final long serialVersionUID = -9092470449495982668L;

  /** Column rank */
  public final static int COLUMN_RANK = 0;

  /** Column team */
  public final static int COLUMN_TEAM = 1;

  /** Column games played */
  public final static int COLUMN_GAMESPLAYED = 2;

  /** Column games won */
  public final static int COLUMN_GAMESWON = 3;

  /** Column games tied */
  public final static int COLUMN_GAMESTIED = 4;

  /** Column games lost */
  public final static int COLUMN_GAMESLOST = 5;

  /** Column goals for */
  public final static int COLUMN_GOALSFOR = 6;

  /** Column goals against */
  public final static int COLUMN_GOALSAGAINST = 7;

  /** Column goals difference */
  public final static int COLUMN_GOALSDIFFERENCE = 8;

  /** Column points */
  public final static int COLUMN_POINTS = 9;

  /** Column count */
  private final static int COLUMN_COUNT = 10;

  /** TeamStats of this table */
  private TeamStats[] teamStats;

  /**
   * Constructs table model with user stats passed by
   * 
   * @param teamStats
   *          The teamStats provided by this table model
   */
  public TMLeagueStandingsTable(TeamStats[] teamStats) {
    this.teamStats = teamStats;
    ihmSort(COLUMN_POINTS, true);
    // Sort by points
  }

  /**
   * Returns column count
   * 
   * @return The columnCount value
   */
  public int getColumnCount() {
    return COLUMN_COUNT;
  }

  /**
   * Returns the teamStats for the row / collumn passed by
   * 
   * @param row
   *          The row
   * @param column
   *          The collumn
   * @return The valueAt value
   */
  public Object getValueAt(int row, int column) {
    switch (column) {
    case COLUMN_RANK:
      return String.valueOf(row + 1);
    case COLUMN_TEAM:
      return teamStats[row].getTeam().getTeamInfo().getTeamName();
    case COLUMN_GAMESPLAYED:
      return String.valueOf(teamStats[row].getGamesPlayed());
    case COLUMN_GAMESWON:
      return String.valueOf(teamStats[row].getGamesWon());
    case COLUMN_GAMESTIED:
      return String.valueOf(teamStats[row].getGamesTied());
    case COLUMN_GAMESLOST:
      return String.valueOf(teamStats[row].getGamesLost());
    case COLUMN_GOALSFOR:
      return String.valueOf(teamStats[row].getGoalsFor());
    case COLUMN_GOALSAGAINST:
      return String.valueOf(teamStats[row].getGoalsAgainst());
    case COLUMN_GOALSDIFFERENCE:
      return String.valueOf(teamStats[row].getGoalsDiff());
    case COLUMN_POINTS:
      return String.valueOf(teamStats[row].getPoints());
    default:
      return null;
    }

  }

  /**
   * Returns row count
   * 
   * @return The rowCount value
   */
  public int getRowCount() {
    return teamStats.length;
  }

  /**
   * Sorts teamStats by collumn and fires a tableDataChanged event
   * 
   * @param collumnNr
   *          The collumn to sort
   * @param standard
   *          Standard sort order
   */
  public void ihmSort(int collumnNr, boolean standard) {
    internalSort(collumnNr, standard);
    this.fireTableDataChanged();
  }

  /**
   * Sorts teamStats by collumn
   * 
   * @param collumnNr
   *          The collumn to sort
   * @param standard
   *          Standard sort order
   */
  private void internalSort(int collumnNr, boolean standard) {

    int sortCriteria = 0;
    boolean ascending = true;

    switch (collumnNr) {
    case COLUMN_GAMESPLAYED: {
      sortCriteria = TeamStats.SORT_GAMES_PLAYED;
      ascending = false;
      break;
    }
    case COLUMN_GAMESWON: {
      sortCriteria = TeamStats.SORT_GAMES_WON;
      ascending = false;
      break;
    }
    case COLUMN_GAMESTIED: {
      sortCriteria = TeamStats.SORT_GAMES_TIED;
      ascending = false;
      break;
    }
    case COLUMN_GAMESLOST: {
      sortCriteria = TeamStats.SORT_GAMES_LOST;
      ascending = false;
      break;
    }
    case COLUMN_GOALSFOR: {
      sortCriteria = TeamStats.SORT_GOALS_FOR;
      ascending = false;
      break;
    }
    case COLUMN_GOALSAGAINST: {
      sortCriteria = TeamStats.SORT_GOALS_AGAINST;
      ascending = false;
      break;
    }
    case COLUMN_GOALSDIFFERENCE: {
      sortCriteria = TeamStats.SORT_GOALS_DIFFERENCE;
      ascending = false;
      break;
    }
    case COLUMN_POINTS: {
      sortCriteria = TeamStats.SORT_POINTS;
      ascending = false;
      break;
    }
    default: {
      // Do nothing, just return
      return;
    }
    }

    for (int i = 0; i < teamStats.length; i++) {
      teamStats[i].setSortCriteria(sortCriteria);
      if (standard) {
        teamStats[i].setSortOrder(ascending);
      } else {
        teamStats[i].setSortOrder(!ascending);
      }
    }

    java.util.Arrays.sort(teamStats);
  }

  /**
   * Gets the columnName attribute of the TMLeagueStandingsTable object
   * 
   * @param collumnNr
   *          The collumn nr looking for
   * @return The columnName value
   */
  public String getColumnName(int collumnNr) {
    switch (collumnNr) {
    case COLUMN_RANK:
      return ClientController.getInstance().getTranslation("ihm.tmRank");
    case COLUMN_TEAM:
      return ClientController.getInstance().getTranslation("ihm.team");
    case COLUMN_GAMESPLAYED:
      return ClientController.getInstance().getTranslation("tmLeagueStandings.gamesPlayed");
    case COLUMN_GAMESWON:
      return ClientController.getInstance().getTranslation("tmLeagueStandings.gamesWon");
    case COLUMN_GAMESTIED:
      return ClientController.getInstance().getTranslation("tmLeagueStandings.gamesTied");
    case COLUMN_GAMESLOST:
      return ClientController.getInstance().getTranslation("tmLeagueStandings.gamesLost");
    case COLUMN_GOALSFOR:
      return ClientController.getInstance().getTranslation("tmLeagueStandings.goalsFor");
    case COLUMN_GOALSAGAINST:
      return ClientController.getInstance().getTranslation("tmLeagueStandings.goalsAgainst");
    case COLUMN_GOALSDIFFERENCE:
      return ClientController.getInstance().getTranslation("tmLeagueStandings.goalsDifference");
    case COLUMN_POINTS:
      return ClientController.getInstance().getTranslation("tmLeagueStandings.points");
    default:
      return null;
    }
  }

}
