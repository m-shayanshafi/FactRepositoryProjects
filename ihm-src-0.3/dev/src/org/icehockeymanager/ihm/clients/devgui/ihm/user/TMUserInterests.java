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
  
package org.icehockeymanager.ihm.clients.devgui.ihm.user;

import java.util.*;

import javax.swing.table.*;

import org.icehockeymanager.ihm.clients.devgui.controller.*;
import org.icehockeymanager.ihm.clients.devgui.ihm.league.*;
import org.icehockeymanager.ihm.game.league.helper.*;

/**
 * TMUserInterests provides user interests in a table model
 * 
 * @author Bernhard von Gunten
 * @created December 29, 2001
 */
public class TMUserInterests extends AbstractTableModel {

  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 3258135734656316473L;

  /** Column count */
  private final static int COLUMN_COUNT = 2;

  /** User interests */
  private Vector userInterests = null;

  /** League elements of this scenario */
  private Vector<LeagueElement> leagueElements = null;

  /**
   * Constructs table model with userinterests passed by
   * 
   * @param leagueElements
   *          The leagueElements provided in this table model
   * @param userInterests
   *          The userInterests provided in this table model
   */
  public TMUserInterests(Vector<LeagueElement> leagueElements, Vector userInterests) {
    this.leagueElements = leagueElements;
    this.userInterests = userInterests;
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
   *          The row
   * @param column
   *          The collumn
   * @return The value of this field
   */
  public Object getValueAt(int row, int column) {
    LeagueElement tmp = leagueElements.get(row);
    switch (column) {
    case 0:
      return LeagueTools.getLeagueElementDescription(tmp);
    case 1: {
      if (userInterests.indexOf(tmp) >= 0) {
        return ClientController.getInstance().getTranslation("ihm.yes");
      } else {
        return ClientController.getInstance().getTranslation("ihm.no");
      }
    }

    }
    return null;
  }

  /**
   * Return leagueElement by row
   * 
   * @param row
   *          The row of a leagueElement
   * @return The leagueElement value
   */
  public LeagueElement getLeagueElement(int row) {
    return leagueElements.get(row);
  }

  /**
   * Returns row count
   * 
   * @return The rowCount value
   */
  public int getRowCount() {
    return leagueElements.size();
  }

  /**
   * Gets the columnName attribute of the TMUserInterests object
   * 
   * @param column
   *          The column nr
   * @return The columnName value
   */
  public String getColumnName(int column) {
    switch (column) {
    case 0:
      return ClientController.getInstance().getTranslation("ihm.leagueElement");
    case 1:
      return ClientController.getInstance().getTranslation("tmUserInterests.interestedIn");
    }
    return "";
  }

}
