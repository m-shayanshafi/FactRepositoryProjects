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
import org.icehockeymanager.ihm.game.player.*;
import org.icehockeymanager.ihm.game.team.*;
import org.icehockeymanager.ihm.lib.*;

/**
 * Table model to show player attributes with sorting.
 * 
 * @author Bernhard von Gunten
 * @created December 29, 2001
 */
public class TMPlayerAttributes extends AbstractTableModel implements IhmTableModelSorter {

  static final long serialVersionUID = -7703902793549930313L;

  /** Column rank */
  public final static int COLUMN_RANK = 0;

  /** Column position */
  public final static int COLUMN_POSITION = 1;

  /** Column player */
  public final static int COLUMN_PLAYER = 2;

  /** Column team */
  public final static int COLUMN_TEAM = 3;

  /** Column specific attributes */
  public final static int COLUMN_SPECIFIC_ATTRIBUTES = 4;

  /** Column common attributes */
  public final static int COLUMN_COMMON_ATTRIBUTES = 5;

  /** Column total attributes */
  public final static int COLUMN_TOTAL_ATTRIBUTES = 6;

  /** Column form */
  public final static int COLUMN_FORM = 7;

  /** Column energy */
  public final static int COLUMN_ENERGY = 8;

  /** Column morale */
  public final static int COLUMN_MORALE = 9;

  /** Column overall */
  public final static int COLUMN_OVERALL = 10;

  /** Column birthdated */
  public final static int COLUMN_BIRTHDATE = 11;

  /** Column count */
  private final static int COLUMN_COUNT = 12;

  /** PlayerAttributes of this table */
  private Player[] players;

  /**
   * Constructs table model with players passed by
   * 
   * @param playersV
   *          Players Vector
   */
  public TMPlayerAttributes(Vector<Player> playersV) {
    // Get player attributes out of players passed by
    this.players = new Player[playersV.size()];
    for (int i = 0; i < playersV.size(); i++) {
      this.players[i] = playersV.get(i);
    }

    // Sort by position
    ihmSort(COLUMN_POSITION, true);
  }

  /**
   * Constructs table model with players passed by
   * 
   * @param players
   *          Description of the Parameter
   */
  public TMPlayerAttributes(Player[] players) {
    this.players = players;
    // Sort by position
    ihmSort(COLUMN_POSITION, true);
  }

  public TMPlayerAttributes(Player player) {
    this.players = new Player[1];
    players[0] = player;
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
   * Returns leagueElement name or interested flag
   * 
   * @param row
   *          Row of value looking for
   * @param column
   *          Column of value looking for
   * @return The valueAt value
   */
  public Object getValueAt(int row, int column) {
    switch (column) {
    case COLUMN_RANK:
      return String.valueOf(players[row].getTeamJerseyNumber());
    case COLUMN_POSITION:
      return PlayerTools.getPositionStr(players[row].getPlayerAttributes());
    case COLUMN_PLAYER:
      return players[row].getPlayerInfo().getPlayerName();
    case COLUMN_TEAM: {
      Team team = players[row].getTeam();
      if (team == null) {
        return "--";
      } else {
        return team.getTeamInfo().getTeamName();
      }
    }
    case COLUMN_SPECIFIC_ATTRIBUTES:
      return String.valueOf(players[row].getPlayerAttributes().getSpecificPlayerAttributesAverage());
    case COLUMN_COMMON_ATTRIBUTES:
      return String.valueOf(players[row].getPlayerAttributes().getCommonPlayerAttributesAverage());
    case COLUMN_TOTAL_ATTRIBUTES:
      return String.valueOf(players[row].getPlayerAttributes().getTotalAttributesAverage());
    case COLUMN_FORM:
      return String.valueOf(players[row].getPlayerAttributes().getVIAPlayerAttribute(PlayerAttributes.VIA_FORM_KEY).getIntValue());
    case COLUMN_ENERGY:
      return String.valueOf(players[row].getPlayerAttributes().getVIAPlayerAttribute(PlayerAttributes.VIA_ENERGY_KEY).getIntValue());
    case COLUMN_MORALE:
      return String.valueOf(players[row].getPlayerAttributes().getVIAPlayerAttribute(PlayerAttributes.VIA_MORALE_KEY).getIntValue());
    case COLUMN_OVERALL:
      return String.valueOf(players[row].getPlayerAttributes().getOverallAverage());
    case COLUMN_BIRTHDATE:
      return String.valueOf(players[row].getPlayerInfo().getAge());
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
   * Sorts playerStats by collumn and fires table data changed event.
   * 
   * @param collumnNr
   *          Column to sort
   * @param standard
   *          Standard sort order
   */
  public void ihmSort(int collumnNr, boolean standard) {
    internalSort(collumnNr, standard);
    this.fireTableDataChanged();
  }

  /**
   * Sorts playerStats by collumn
   * 
   * @param collumnNr
   *          Column to sort
   * @param standard
   *          Standard sort order
   */
  private void internalSort(int collumnNr, boolean standard) {

    int sortCriteria = 0;
    boolean ascending = true;

    switch (collumnNr) {
    case COLUMN_POSITION: {
      sortCriteria = Player.SORT_ATTRIBUTES_POSITION;
      ascending = false;
      break;
    }
    case COLUMN_SPECIFIC_ATTRIBUTES: {
      sortCriteria = Player.SORT_ATTRIBUTES_SPECIFIC_AVERAGE;
      ascending = false;
      break;
    }
    case COLUMN_COMMON_ATTRIBUTES: {
      sortCriteria = Player.SORT_ATTRIBUTES_COMMON_AVERAGE;
      ascending = false;
      break;
    }
    case COLUMN_TOTAL_ATTRIBUTES: {
      sortCriteria = Player.SORT_ATTRIBUTES_TOTAL_AVERAGE;
      ascending = false;
      break;
    }
    case COLUMN_FORM: {
      sortCriteria = Player.SORT_ATTRIBUTES_VIA_FORM;
      ascending = false;
      break;
    }
    case COLUMN_ENERGY: {
      sortCriteria = Player.SORT_ATTRIBUTES_VIA_ENERGY;
      ascending = false;
      break;
    }
    case COLUMN_MORALE: {
      sortCriteria = Player.SORT_ATTRIBUTES_VIA_MOTIVATION;
      ascending = false;
      break;
    }
    case COLUMN_OVERALL: {
      sortCriteria = Player.SORT_ATTRIBUTES_AVERAGE;
      ascending = false;
      break;
    }
    case COLUMN_BIRTHDATE: {
      sortCriteria = Player.SORT_INFO_BIRTHDATE;
      ascending = false;
      break;
    }
    default: {
      // do no sorting, just leave ...
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
   * Returns column names
   * 
   * @param collumnNr
   *          Column nr looking for
   * @return The columnName value
   */
  public String getColumnName(int collumnNr) {
    switch (collumnNr) {
    case COLUMN_RANK:
      return ClientController.getInstance().getTranslation("ihm.tmRank");
    case COLUMN_POSITION:
      return ClientController.getInstance().getTranslation("tmPlayerAttributes.position");
    case COLUMN_PLAYER:
      return ClientController.getInstance().getTranslation("ihm.player");
    case COLUMN_TEAM:
      return ClientController.getInstance().getTranslation("ihm.team");
    case COLUMN_SPECIFIC_ATTRIBUTES:
      return ClientController.getInstance().getTranslation("tmPlayerAttributes.specificAttributes");
    case COLUMN_COMMON_ATTRIBUTES:
      return ClientController.getInstance().getTranslation("tmPlayerAttributes.commonAttributes");
    case COLUMN_TOTAL_ATTRIBUTES:
      return ClientController.getInstance().getTranslation("tmPlayerAttributes.totalAttributes");
    case COLUMN_FORM:
      return ClientController.getInstance().getTranslation("tmPlayerAttributes.form");
    case COLUMN_ENERGY:
      return ClientController.getInstance().getTranslation("tmPlayerAttributes.energy");
    case COLUMN_MORALE:
      return ClientController.getInstance().getTranslation("tmPlayerAttributes.morale");
    case COLUMN_OVERALL:
      return ClientController.getInstance().getTranslation("tmPlayerAttributes.overall");
    case COLUMN_BIRTHDATE:
      return ClientController.getInstance().getTranslation("tmPlayerAttributes.birthdate");
    default:
      return null;
    }
  }

  /**
   * Returns the player of a row passed by
   * 
   * @param row
   *          The row of the wanted player
   * @return The player value
   */
  public Player getPlayer(int row) {
    return players[row];
  }

}
