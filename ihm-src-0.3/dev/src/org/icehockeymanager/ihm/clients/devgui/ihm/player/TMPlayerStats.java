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
  
package org.icehockeymanager.ihm.clients.devgui.ihm.player;

import java.util.*;

import javax.swing.table.*;

import org.icehockeymanager.ihm.clients.devgui.controller.*;
import org.icehockeymanager.ihm.clients.devgui.ihm.league.*;
import org.icehockeymanager.ihm.game.league.*;
import org.icehockeymanager.ihm.game.league.helper.*;
import org.icehockeymanager.ihm.game.player.*;
import org.icehockeymanager.ihm.lib.*;

/**
 * Table model to show the actual leagueStandings.
 * 
 * @author Bernhard von Gunten
 * @created December 29, 2001
 */
public class TMPlayerStats extends AbstractTableModel implements IhmTableModelSorter {

  static final long serialVersionUID = 1319128585100381882L;

  /** Column rank */
  public final static int COLUMN_RANK = 0;

  /** Column position */
  public final static int COLUMN_POSITION = 1;

  /** Column player */
  public final static int COLUMN_PLAYER = 2;

  /** Column owner */
  public final static int COLUMN_TEAM = 3;

  /** Column owner */
  public final static int COLUMN_OWNER = 4;

  /** Column goals */
  public final static int COLUMN_GOALS = 5;

  /** Column assitsts */
  public final static int COLUMN_ASSISTS = 6;

  /** Column scorer points */
  public final static int COLUMN_SCORERPOINTS = 7;

  /** Column games played */
  public final static int COLUMN_GAMESPLAYED = 8;

  /** Column penalty minutes */
  public final static int COLUMN_PENALTYMINUTES = 9;

  /** Column count */
  private final static int COLUMN_COUNT = 10;

  /** PlayerStats of this table */
  private PlayerStats[] players;

  /**
   * Constructs table model with player stats passed by
   * 
   * @param playerStats
   *          The playerStats provided in this table model.
   */
  public TMPlayerStats(PlayerStats[] playerStats) {
    this.players = playerStats;
    // Sort by points
    ihmSort(COLUMN_SCORERPOINTS, true);
  }

  /**
   * Constructor for the TMPlayerStats object
   * 
   * @param players
   *          Description of the Parameter
   */

  public void TMPslayerStats(Vector<Player> players) {
    this.players = new PlayerStats[players.size()];
    for (int i = 0; i < players.size(); i++) {
      this.players[i] = players.get(i).getPlayerStatsTotal();
    }
    // Sort by points
    ihmSort(COLUMN_SCORERPOINTS, true);
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
      return String.valueOf(players[row].getPlayer().getTeamJerseyNumber());
    case COLUMN_PLAYER:
      return players[row].getPlayer().getPlayerInfo().getPlayerName();
    case COLUMN_POSITION:
      return PlayerTools.getPositionStr(players[row].getPlayer().getPlayerAttributes());
    case COLUMN_TEAM: {
      if (players[row].getPlayer().getTeam() != null) {
        return players[row].getPlayer().getTeam().getTeamInfo().getTeamName();
      } else {
        return "--";
      }
    }
    case COLUMN_OWNER: {
      if (players[row].getOwner() == null) {
        return ClientController.getInstance().getTranslation("tmPlayerStats.totalOwner");
      } else {
        Object tmp = players[row].getOwner();
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
    case COLUMN_GOALS:
      return String.valueOf(players[row].getGoals());
    case COLUMN_ASSISTS:
      return String.valueOf(players[row].getAssists());
    case COLUMN_SCORERPOINTS:
      return String.valueOf(players[row].getScorerPoints());
    case COLUMN_GAMESPLAYED:
      return String.valueOf(players[row].getGamesPlayed());
    case COLUMN_PENALTYMINUTES:
      return String.valueOf(players[row].getPenaltyMinutes());
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
    return players.length;
  }

  /**
   * Sorts playerStats by collumn and fires a tableDataChanged event
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
   * Returns the player of a row passed by
   * 
   * @param row
   *          The row of the wanted player
   * @return The player value
   */
  public Player getPlayer(int row) {
    return players[row].getPlayer();
  }

  /**
   * Sorts playerStats by collumn and fires a tableDataChanged event
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
    case COLUMN_POSITION: {
      sortCriteria = PlayerStats.SORT_POSITION;
      ascending = false;
      break;
    }
    case COLUMN_GOALS: {
      sortCriteria = PlayerStats.SORT_GOALS;
      ascending = false;
      break;
    }
    case COLUMN_ASSISTS: {
      sortCriteria = PlayerStats.SORT_ASSISTS;
      ascending = false;
      break;
    }
    case COLUMN_SCORERPOINTS: {
      sortCriteria = PlayerStats.SORT_SCORER_POINTS;
      ascending = false;
      break;
    }
    case COLUMN_GAMESPLAYED: {
      sortCriteria = PlayerStats.SORT_GAMES_PLAYED;
      ascending = false;
      break;
    }
    case COLUMN_PENALTYMINUTES: {
      sortCriteria = PlayerStats.SORT_PENALTY_MINUTES;
      ascending = false;
      break;
    }
    default: {
      // Do nothing, just return
      return;
    }
    }

    for (int i = 0; i < players.length; i++) {
      players[i].setSortCriteria(sortCriteria);
      if (standard) {
        players[i].setSortOrder(ascending);
      } else {
        players[i].setSortOrder(!ascending);
      }
    }

    java.util.Arrays.sort(players);
  }

  /**
   * Gets the columnName attribute of the TMPlayerStats object
   * 
   * @param collumnNr
   *          The column nr
   * @return The columnName value
   */
  public String getColumnName(int collumnNr) {
    switch (collumnNr) {
    case COLUMN_RANK:
      return ClientController.getInstance().getTranslation("ihm.tmRank");
    case COLUMN_POSITION:
      return ClientController.getInstance().getTranslation("tmPlayerStats.position");
    case COLUMN_PLAYER:
      return ClientController.getInstance().getTranslation("ihm.player");
    case COLUMN_TEAM:
      return ClientController.getInstance().getTranslation("ihm.team");
    case COLUMN_OWNER:
      return ClientController.getInstance().getTranslation("ihm.owner");
    case COLUMN_GOALS:
      return ClientController.getInstance().getTranslation("tmPlayerStats.goals");
    case COLUMN_ASSISTS:
      return ClientController.getInstance().getTranslation("tmPlayerStats.assists");
    case COLUMN_SCORERPOINTS:
      return ClientController.getInstance().getTranslation("tmPlayerStats.scorerPoints");
    case COLUMN_GAMESPLAYED:
      return ClientController.getInstance().getTranslation("tmPlayerStats.gamesPlayed");
    case COLUMN_PENALTYMINUTES:
      return ClientController.getInstance().getTranslation("tmPlayerStats.penaltyMinutes");
    default:
      return null;
    }
  }

}
