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
  
package org.icehockeymanager.ihm.game.finance.events;

import java.util.*;

import org.icehockeymanager.ihm.game.*;
import org.icehockeymanager.ihm.game.scheduler.events.*;
import org.icehockeymanager.ihm.game.team.*;

/**
 * AccountingEvent - account for each team
 * 
 * @author adasen
 * @created Oct 19, 2004
 */
public class AccountingEvent extends SchedulerInternalEvent {

  static final long serialVersionUID = -4006791241163622225L;

  /**
   * AccountingEvent constructor
   * 
   * @param source -
   *          Object
   * @param day -
   *          Calender
   */
  public AccountingEvent(Object source, Calendar day) {
    super(source, day, "events.finance.accounting");
  }

  /** Plays the event and does all the bookings */
  public void play() {
    super.play();

    Team[] teams = GameController.getInstance().getScenario().getTeams();

    for (int i = 0; i < teams.length; i++) {
      teams[i].getAccounting().accountFor();
    }

  }

}