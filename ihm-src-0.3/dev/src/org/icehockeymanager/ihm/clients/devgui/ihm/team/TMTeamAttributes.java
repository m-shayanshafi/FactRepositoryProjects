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

import java.util.*;

import javax.swing.table.*;

import org.icehockeymanager.ihm.clients.devgui.controller.*;
import org.icehockeymanager.ihm.game.team.*;
import org.icehockeymanager.ihm.lib.*;

/**
 * Table model to show team attributes with sorting.
 * 
 * @author Bernhard von Gunten
 * @created December 29, 2001
 */
public class TMTeamAttributes extends AbstractTableModel implements IhmTableModelSorter {

  static final long serialVersionUID = 7623497945158289671L;

  /** Column rank */
  public final static int COLUMN_RANK = 0;

  /** Column team */
  public final static int COLUMN_TEAM = 1;

  /** Column goaltending */
  public final static int COLUMN_GOALTENDING = 2;

  /** Column defense */
  public final static int COLUMN_DEFENSE = 3;

  /** Column offense */
  public final static int COLUMN_OFFENSE = 4;

  /** Column overall */
  public final static int COLUMN_OVERALL = 5;

  /** Column count */
  private final static int COLUMN_COUNT = 6;

  /** TeamAttributes of this table */
  private TeamAttributes[] teamAttributes;

  /**
   * Constructs table model with team attributes passed by
   * 
   * @param teamAttributes
   *          TeamAttributes provided in this table model
   */
  public TMTeamAttributes(TeamAttributes[] teamAttributes) {
    this.teamAttributes = teamAttributes;
    ihmSort(COLUMN_OVERALL, true);
    // Sort by overall
  }

  /**
   * Constructs table model with teams passed by
   * 
   * @param teams
   *          Teams (-> TeamAttributes) provided in this table model
   */
  public TMTeamAttributes(Vector<Team> teams) {
    // Get team attributes out of teams passed by
    this.teamAttributes = new TeamAttributes[teams.size()];
    for (int i = 0; i < teams.size(); i++) {
      Team tmp = teams.get(i);
      this.teamAttributes[i] = tmp.getTeamAttributes();
    }
    ihmSort(COLUMN_OVERALL, true);
    // Sort by overall
  }

  /**
   * Constructs table model with teams passed by
   * 
   * @param teams
   *          (-> TeamAttributes) provided in this table model
   */
  public TMTeamAttributes(Team[] teams) {
    // Get team attributes out of teams passed by
    this.teamAttributes = new TeamAttributes[teams.length];
    for (int i = 0; i < teams.length; i++) {
      this.teamAttributes[i] = teams[i].getTeamAttributes();
    }
    ihmSort(COLUMN_OVERALL, true);
    // Sort by overall
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
   *          Row to show
   * @param column
   *          Column to show
   * @return The valueAt value
   */
  public Object getValueAt(int row, int column) {
    switch (column) {
    case COLUMN_RANK:
      return String.valueOf(row + 1);
    case COLUMN_TEAM:
      return teamAttributes[row].getTeam().getTeamInfo().getTeamName();
    case COLUMN_GOALTENDING:
      return String.valueOf(teamAttributes[row].getGoalTending());
    case COLUMN_DEFENSE:
      return String.valueOf(teamAttributes[row].getDefense());
    case COLUMN_OFFENSE:
      return String.valueOf(teamAttributes[row].getOffense());
    case COLUMN_OVERALL:
      return String.valueOf(teamAttributes[row].getOverall());
    default:
      return null;
    }
  }

  /**
   * Returns the team of a row passed by
   * 
   * @param row
   *          The row of the wanted team
   * @return The team value
   */
  public Team getTeam(int row) {
    return teamAttributes[row].getTeam();
  }

  /**
   * Returns row count
   * 
   * @return The rowCount value
   */
  public int getRowCount() {
    return teamAttributes.length;
  }

  /**
   * Sorts teamStats by collumn, including table model changed event fired.
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
   * Sorts teamStats by collumn
   * 
   * @param collumnNr
   *          Column to sort
   * @param standard
   *          Standard sort order
   */
  private void internalSort(int collumnNr, boolean standard) {

    int sortCriteria = 0;
    boolean ascending = true;

    // Change sort order by collumn ... do nothing and return if collumn is not
    // "sortable"
    switch (collumnNr) {
    case COLUMN_GOALTENDING: {
      sortCriteria = TeamAttributes.SORT_GOALTENDING;
      ascending = false;
      break;
    }
    case COLUMN_DEFENSE: {
      sortCriteria = TeamAttributes.SORT_DEFENSE;
      ascending = false;
      break;
    }
    case COLUMN_OFFENSE: {
      sortCriteria = TeamAttributes.SORT_OFFENSE;
      ascending = false;
      break;
    }
    case COLUMN_OVERALL: {
      sortCriteria = TeamAttributes.SORT_OVERALL;
      ascending = false;
      break;
    }
    default: {
      // Column is not sortable, return already here
      return;
    }
    }

    for (int i = 0; i < teamAttributes.length; i++) {
      teamAttributes[i].setSortCriteria(sortCriteria);
      if (standard) {
        teamAttributes[i].setSortOrder(ascending);
      } else {
        teamAttributes[i].setSortOrder(!ascending);
      }
    }

    java.util.Arrays.sort(teamAttributes);
  }

  /**
   * Returns column names
   * 
   * @param collumnNr
   *          ColumnName wanted
   * @return The columnName value
   */
  public String getColumnName(int collumnNr) {
    switch (collumnNr) {
    case COLUMN_RANK:
      return ClientController.getInstance().getTranslation("ihm.tmRank");
    case COLUMN_TEAM:
      return ClientController.getInstance().getTranslation("ihm.team");
    case COLUMN_GOALTENDING:
      return ClientController.getInstance().getTranslation("tmTeamAttributes.goalTending");
    case COLUMN_DEFENSE:
      return ClientController.getInstance().getTranslation("tmTeamAttributes.defense");
    case COLUMN_OFFENSE:
      return ClientController.getInstance().getTranslation("tmTeamAttributes.offense");
    case COLUMN_OVERALL:
      return ClientController.getInstance().getTranslation("tmTeamAttributes.overall");
    default:
      return null;
    }

  }

}
