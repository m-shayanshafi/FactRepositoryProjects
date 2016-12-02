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
  
package org.icehockeymanager.ihm.clients.devgui.ihm.finance;

import javax.swing.table.*;
import java.util.*;

import org.icehockeymanager.ihm.game.finance.*;
import org.icehockeymanager.ihm.lib.*;
import org.icehockeymanager.ihm.clients.devgui.controller.*;

public class TMBookings extends AbstractTableModel {

  static final long serialVersionUID = -7100692186708606442L;

  /** Column day */
  public final static int COLUMN_DAY = 0;

  /** Column text */
  public final static int COLUMN_TEXT = 1;

  /** Column amount */
  public final static int COLUMN_AMOUNT = 2;

  /** Column count */
  private final static int COLUMN_COUNT = 3;

  private BookingEntry[] bookings;

  public TMBookings(BookingEntry[] bookings) {
    this.bookings = bookings;
  }

  public TMBookings(Vector<BookingEntry> bookings) {
    this.bookings = bookings.toArray(new BookingEntry[bookings.size()]);
  }

  public int getRowCount() {
    return bookings.length;
  }

  public int getColumnCount() {
    return COLUMN_COUNT;
  }

  public Object getValueAt(int row, int column) {
    switch (column) {
    case COLUMN_DAY:
      return bookings[row].getFormatedDate();
    case COLUMN_TEXT:
      return ClientController.getInstance().getTranslation(bookings[row].getText());
    case COLUMN_AMOUNT:
      return Tools.doubleToStringC(bookings[row].getAmount());
    default:
      return null;
    }
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
      return ClientController.getInstance().getTranslation("tmBookings.day");
    case COLUMN_TEXT:
      return ClientController.getInstance().getTranslation("tmBookings.text");
    case COLUMN_AMOUNT:
      return ClientController.getInstance().getTranslation("tmBookings.amount");
    default:
      return null;
    }
  }

}
