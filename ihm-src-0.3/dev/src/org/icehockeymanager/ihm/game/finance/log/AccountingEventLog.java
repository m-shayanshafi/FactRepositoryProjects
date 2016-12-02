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
  
package org.icehockeymanager.ihm.game.finance.log;

import java.util.*;

import org.icehockeymanager.ihm.game.eventlog.*;
import org.icehockeymanager.ihm.game.team.*;

/**
 * AccountingEventLog - Logs an accounting for a team
 * 
 * @author Arik Dasen
 * @created September, 2004
 */
public class AccountingEventLog extends EventLogEntry {

  static final long serialVersionUID = -5805542154353407577L;

  /**
   * MESSAGE_KEY
   */
  public final String MESSAGE_KEY = "accounting.accounting";

  /**
   * balance
   */
  private double balance;

  /**
   * AccountingEventLog constructor
   * 
   * @param source
   * @param day
   * @param team
   * @param balance
   */
  public AccountingEventLog(Object source, Calendar day, Team team, double balance) {
    super(source, day, team);
    this.balance = balance;
  }

  public double getBalance() {
    return balance;
  }

}