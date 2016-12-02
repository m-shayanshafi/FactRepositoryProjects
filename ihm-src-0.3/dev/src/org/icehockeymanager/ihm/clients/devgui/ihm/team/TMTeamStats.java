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
  
package org.icehockeymanager.ihm.clients.devgui.ihm.team;

import javax.swing.table.*;

import org.icehockeymanager.ihm.clients.devgui.controller.*;
import org.icehockeymanager.ihm.clients.devgui.ihm.league.*;
import org.icehockeymanager.ihm.game.league.*;
import org.icehockeymanager.ihm.game.league.helper.*;
import org.icehockeymanager.ihm.game.team.*;
import org.icehockeymanager.ihm.lib.*;

/**
 * Table model to show the actual leagueStandings.
 * 
 * @author Bernhard von Gunten
 * @created December 29, 2001
 */
public class TMTeamStats extends AbstractTableModel implements IhmTableModelSorter {

  static final long serialVersionUID = 7968717481205977889L;

  /** Column rank */
  public final static int COLUMN_RANK = 0;

  /** Column team */
  public final static int COLUMN_TEAM = 1;

  /** Column owner */
  public final static int COLUMN_OWNER = 2;

  /** Column games played */
  public final static int COLUMN_GAMESPLAYED = 3;

  /** Column games won */
  public final static int COLUMN_GAMESWON = 4;

  /** Column games tied */
  public final static int COLUMN_GAMESTIED = 5;

  /** Column games lost */
  public final static int COLUMN_GAMESLOST = 6;

  /** Column goals for */
  public final static int COLUMN_GOALSFOR = 7;

  /** Column goals against */
  public final static int COLUMN_GOALSAGAINST = 8;

  /** Column goals difference */
  public final static int COLUMN_GOALSDIFFERENCE = 9;

  /** Column goals points */
  public final static int COLUMN_POINTS = 10;

  /** Column penalty minutes */
  public final static int COLUMN_PENALTYMINUTES = 11;

  /** Column count */
  private final static int COLUMN_COUNT = 12;

  /** TeamStats of this table */
  private TeamStats[] teamStats;

  /**
   * Constructs table model with team stats passed by
   * 
   * @param teamStats
   *          The teamStats provided in this table model.
   */
  public TMTeamStats(TeamStats[] teamStats) {
    this.teamStats = teamStats;
    // Sort by points
    ihmSort(COLUMN_POINTS, true);
  }

  /**
   * Returns the team of a row passed by
   * 
   * @param row
   *          The row of the wanted team
   * @return The team value
   */
  public Team getTeam(int row) {
    return teamStats[row].getTeam();
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
   * Returns the value of a field.
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
    case COLUMN_OWNER: {
      if (teamStats[row].getOwner() == null) {
        return ClientController.getInstance().getTranslation("tmTeamStats.totalOwner");
      } else {
        Object tmp = teamStats[row].getOwner();
        if (tmp instanceof org.icehockeymanager.ihm.game.league.helper.LeagueElement) {
          LeagueElement le = (LeagueElement) tmp;
          return LeagueTools.getLeagueElementDescription(le);
        } else if (tmp instanceof org.icehockeymanager.ihm.game.league.helper.LeagueElementGroup) {
          return ClientController.getInstance().getTranslation(((LeagueElementGroup) tmp).getNameKey());
        } else if (tmp instanceof org.icehockeymanager.ihm.game.league.League) {
          return ((League) tmp).getName();
        }
      }
    }
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
    case COLUMN_PENALTYMINUTES:
      return String.valueOf(teamStats[row].getPenaltyMinutes());
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
   *          Collumn to sort
   * @param standard
   *          Standard sort order
   */
  public void ihmSort(int collumnNr, boolean standard) {
    internalSort(collumnNr, standard);
    this.fireTableDataChanged();
  }

  /**
   * Sorts teamStats by collumn and fires a tableDataChanged event
   * 
   * @param collumnNr
   *          Collumn to sort
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
    case COLUMN_PENALTYMINUTES: {
      sortCriteria = TeamStats.SORT_PENALTY_MINUTES;
      ascending = false;
      break;
    }
    default:
      return;
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
   * Gets the columnName attribute of the TMTeamStats object
   * 
   * @param collumnNr
   *          The column nr
   * @return The columnName value
   */
  public String getColumnName(int collumnNr) {
    switch (collumnNr) {
    case COLUMN_RANK:
      return ClientController.getInstance().getTranslation("ihm.tmRank");
    case COLUMN_TEAM:
      return ClientController.getInstance().getTranslation("ihm.team");
    case COLUMN_OWNER:
      return ClientController.getInstance().getTranslation("ihm.owner");
    case COLUMN_GAMESPLAYED:
      return ClientController.getInstance().getTranslation("tmTeamStats.gamesPlayed");
    case COLUMN_GAMESWON:
      return ClientController.getInstance().getTranslation("tmTeamStats.gamesWon");
    case COLUMN_GAMESTIED:
      return ClientController.getInstance().getTranslation("tmTeamStats.gamesTied");
    case COLUMN_GAMESLOST:
      return ClientController.getInstance().getTranslation("tmTeamStats.gamesLost");
    case COLUMN_GOALSFOR:
      return ClientController.getInstance().getTranslation("tmTeamStats.goalsFor");
    case COLUMN_GOALSAGAINST:
      return ClientController.getInstance().getTranslation("tmTeamStats.goalsAgainst");
    case COLUMN_GOALSDIFFERENCE:
      return ClientController.getInstance().getTranslation("tmTeamStats.goalsDifference");
    case COLUMN_POINTS:
      return ClientController.getInstance().getTranslation("tmTeamStats.points");
    case COLUMN_PENALTYMINUTES:
      return ClientController.getInstance().getTranslation("tmTeamStats.penaltyMinutes");
    }
    return "";
  }

}
