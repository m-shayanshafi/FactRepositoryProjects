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
  
package org.icehockeymanager.ihm.clients.devgui.ihm.eventlog;

import javax.swing.table.*;

import org.icehockeymanager.ihm.clients.devgui.controller.*;
import org.icehockeymanager.ihm.game.eventlog.*;

/**
 * Table model to show the game calendar
 * 
 * @author Bernhard von Gunten
 * @created January 02, 2002
 */
public class TMEventLog extends AbstractTableModel {

  static final long serialVersionUID = -4652095169089154169L;

  /** Column day */
  public final static int COLUMN_DAY = 0;

  /** Column event */
  public final static int COLUMN_EVENT = 1;

  /** Column count */
  private final static int COLUMN_COUNT = 2;

  /** SchedulerEvents of this table */
  private EventLogEntry[] eventLogEntries;

  /**
   * Constructs table model with SchedulerEvents passed by
   * 
   * @param eventLogEntries
   */
  public TMEventLog(EventLogEntry[] eventLogEntries) {
    this.eventLogEntries = eventLogEntries;
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
      return eventLogEntries[row].getFormatedDate();
    case COLUMN_EVENT:
      return EventLogTools.getMessage(eventLogEntries[row]);
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
  public EventLogEntry getEventLogEntry(int row) {
    return eventLogEntries[row];
  }

  /**
   * Returns row count
   * 
   * @return The rowCount value
   */
  public int getRowCount() {
    return eventLogEntries.length;
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
