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

import java.util.*;

import org.icehockeymanager.ihm.game.team.*;
import org.icehockeymanager.ihm.game.*;
import org.icehockeymanager.ihm.lib.*;
import java.io.*;

/**
 * Contract - Base classs for all kind of contracts in IHM
 * 
 * @author adasen
 * @created Oct 19, 2004
 */
public class Contract extends IhmCustomComparator implements Costs, Revenues, Cloneable, Serializable {

  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 3257854268320266036L;

  /** Sort by End Date */
  public final static int SORT_ENDDATE = 1;

  /** Sort by Costs */
  public final static int SORT_COSTS = 2;

  /** Sort by Revenues */
  public final static int SORT_REVENUE = 3;

  public final static int INTERVAL_WEEKLY = 52;

  public final static int INTERVAL_MONTHLY = 12;

  public final static int INTERVAL_YEARLY = 1;

  /**
   * party A of contract (always the team)
   */
  private Team partyA;

  /**
   * party B of contract
   */
  private Object partyB;

  /**
   * start date of contract
   */
  protected Calendar startDate;

  /**
   * end date of contract
   */
  protected Calendar endDate;

  /**
   * interval of accounting
   */
  protected int interval;

  /**
   * amount (per year)
   */
  protected double amount;

  /**
   * Contract constructor
   * 
   * @param partyA
   *          Team
   * @param partyB
   *          Object
   */
  public Contract(Team partyA, Object partyB) {
    this.partyA = partyA;
    this.partyB = partyB;
    this.interval = INTERVAL_YEARLY;
  }

  /**
   * Returns the amount.
   * 
   * @return amount
   */
  public double getAmount() {
    return amount;
  }

  /**
   * @param amount
   *          The amount to set.
   */
  public void setAmount(double amount) {
    this.amount = amount;
  }

  /**
   * Returns the endDate.
   * 
   * @return endDate
   */
  public Calendar getEndDate() {
    return endDate;
  }

  /**
   * @param endDate
   *          The endDate to set.
   */
  public void setEndDate(Calendar endDate) {
    this.endDate = endDate;
  }

  /**
   * Returns the interval.
   * 
   * @return interval
   */
  public int getInterval() {
    return interval;
  }

  /**
   * @param interval
   *          The interval to set.
   */
  public void setInterval(int interval) {
    this.interval = interval;
  }

  /**
   * Returns the partyA.
   * 
   * @return partyA
   */
  public Team getPartyA() {
    return partyA;
  }

  /**
   * @param partyA
   *          The partyA to set.
   */
  public void setPartyA(Team partyA) {
    this.partyA = partyA;
  }

  /**
   * Returns the partyB.
   * 
   * @return partyB
   */
  public Object getPartyB() {
    return partyB;
  }

  /**
   * @param partyB
   *          The partyB to set.
   */
  public void setPartyB(Object partyB) {
    this.partyB = partyB;
  }

  /**
   * Returns the startDate.
   * 
   * @return startDate
   */
  public Calendar getStartDate() {
    return (Calendar) startDate.clone();
  }

  /**
   * @param startDate
   *          The startDate to set.
   */
  public void setStartDate(Calendar startDate) {
    this.startDate = startDate;
  }

  /**
   * Returns if the contract is valid right now
   * 
   * @return boolean
   */
  public boolean isValid() {
    Calendar today = GameController.getInstance().getScenario().getScheduler().getToday();
    return (startDate.before(today) && endDate.after(today));
  }

  /**
   * Returns costs if amount < 0
   * 
   * @return double
   */
  public double getCosts() {
    if (amount < 0) {
      return -amount / interval;
    } else {
      return 0;
    }
  }

  /**
   * Returns revenues if amount > 0
   * 
   * @return double
   */
  public double getRevenues() {
    if (amount > 0) {
      return amount / interval;
    } else {
      return 0;
    }
  }

  /**
   * Returns sort value considering sortCriteria
   * 
   * @return The sortValue value
   */
  protected double getSortValue() {
    switch (sortCriteria) {
    case SORT_ENDDATE:
      return this.getEndDate().getTime().getTime();
    case SORT_COSTS:
      return this.getCosts();
    case SORT_REVENUE:
      return this.getRevenues();
    default:
      return this.getEndDate().getTime().getTime();
    }
  }

  /** Overwrite clone, for "deep - cloned" Calendars. 
   * @return Object 
   * @throws CloneNotSupportedException */
  public Object clone() throws CloneNotSupportedException {
    Contract tmp = (Contract) super.clone();
    tmp.setEndDate(getEndDate());
    tmp.setStartDate(getStartDate());
    return tmp;
  }

  public static void sortContractArray(Contract[] contracts, int sortorder, boolean ascending) {
    for (int i = 0; i < contracts.length; i++) {
      contracts[i].setSortCriteria(sortorder);
      contracts[i].setSortOrder(ascending);
    }
    java.util.Arrays.sort(contracts);
  }

}
