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
  
package org.icehockeymanager.ihm.game.finance;

import java.io.*;
import java.util.*;

import org.icehockeymanager.ihm.game.*;
import org.icehockeymanager.ihm.game.finance.log.*;
import org.icehockeymanager.ihm.game.team.*;

/**
 * Accounting
 * 
 * @author adasen
 * @created Oct 19, 2004
 */
public class Accounting implements Serializable {

  static final long serialVersionUID = -1031873458548020512L;

  /**
   * team
   */
  private Team team;

  /**
   * bookings
   */
  private Vector<BookingEntry> bookings;

  /**
   * balance
   */
  private double balance;

  /**
   * Accounting constructor
   * 
   * @param team
   */
  public Accounting(Team team) {
    this.team = team;
    bookings = new Vector<BookingEntry>();
  }

  /** generate monthly booking entries */
  public void accountFor() {
    Calendar today = GameController.getInstance().getScenario().getScheduler().getToday();

    // generate one booking entry for all player costs
    double tempBalance = 0;
    for (int i = 0; i < team.getPlayers().length; i++) {
      tempBalance -= team.getPlayers()[i].getPlayerContractCurrent().getCosts();
    }
    addBookingEntry(new BookingEntry(tempBalance, (Calendar) today.clone(), "accounting.booking.playerSalary"));

    // generate one booking entry for all infrastructure objects
    addBookingEntry(new BookingEntry(team.getInfrastructure().getCosts(), (Calendar) today.clone(), "accounting.booking.maintenance"));

    // generate one booking for sponsoring
    addBookingEntry(new BookingEntry(team.getSponsoring().getTotalRevenues(), (Calendar) today.clone(), "accounting.booking.sponsoring"));

    // when accounting is finished, show new balance in gui
    AccountingEventLog event = new AccountingEventLog(GameController.getInstance().getScenario(), today, team, balance);
    GameController.getInstance().getScenario().getEventLog().addEventLogEntry(event);

  }

  /**
   * Adds a booking entry
   * 
   * @param newEntry
   */
  public void addBookingEntry(BookingEntry newEntry) {
    bookings.addElement(newEntry);
    balance += newEntry.getAmount();
  }

  /**
   * Returns all bookings in a array
   * 
   * @return bookings
   */
  public BookingEntry[] getBookings() {
    return bookings.toArray(new BookingEntry[bookings.size()]);
  }

  /**
   * Returns the balance.
   * 
   * @return balance
   */
  public double getBalance() {
    return balance;
  }
}
