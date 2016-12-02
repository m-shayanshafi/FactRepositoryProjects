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
  
package org.icehockeymanager.ihm.clients.devgui.ihm.scheduler;

import java.util.*;

import javax.swing.table.*;

import org.icehockeymanager.ihm.clients.devgui.controller.*;
import org.icehockeymanager.ihm.clients.devgui.ihm.league.*;
import org.icehockeymanager.ihm.game.league.events.*;
import org.icehockeymanager.ihm.game.scheduler.events.*;

/**
 * Table model to show the game calendar
 * 
 * @author Bernhard von Gunten
 * @created January 02, 2002
 */
public class TMScheduler extends AbstractTableModel {

  static final long serialVersionUID = 1198656350873316760L;

  /** Column day */
  public final static int COLUMN_DAY = 0;

  /** Column event */
  public final static int COLUMN_EVENT = 1;

  /** Column count */
  private final static int COLUMN_COUNT = 2;

  /** SchedulerEvents of this table */
  private SchedulerEvent[] SchedulerEvents;

  /**
   * Constructs table model with SchedulerEvents passed by
   * 
   * @param SchedulerEvents
   *          SchedulerEvents of this table
   */
  public TMScheduler(SchedulerEvent[] SchedulerEvents) {
    this.SchedulerEvents = SchedulerEvents;
  }

  /**
   * Constructs table model with SchedulerEvents passed by
   * 
   * @param events
   *          Description of the Parameter
   */
  public TMScheduler(Vector<SchedulerEvent> events) {
    this.SchedulerEvents = events.toArray(new SchedulerEvent[events.size()]);
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
    case COLUMN_DAY:
      return SchedulerEvents[row].getFormatedDate();
    case COLUMN_EVENT:
      if (SchedulerEvents[row] instanceof GameDayMatchesEvent) {
        GameDayMatchesEvent gameDay = (GameDayMatchesEvent) SchedulerEvents[row];
        return (LeagueTools.getGameDayDescription(gameDay));
      }
      return ClientController.getInstance().getTranslation(SchedulerEvents[row].getMessageKey());
    default:
      return null;
    }
  }

  /**
   * Returns the SchedulerEvent of a row passed by
   * 
   * @param row
   *          The row of the wanted event
   * @return The team value
   */
  public SchedulerEvent getSchedulerEvent(int row) {
    return SchedulerEvents[row];
  }

  /**
   * Returns row count
   * 
   * @return The rowCount value
   */
  public int getRowCount() {
    return SchedulerEvents.length;
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
    case COLUMN_DAY:
      return ClientController.getInstance().getTranslation("tmScheduler.day");
    case COLUMN_EVENT:
      return ClientController.getInstance().getTranslation("tmScheduler.event");
    default:
      return null;
    }
  }

}
