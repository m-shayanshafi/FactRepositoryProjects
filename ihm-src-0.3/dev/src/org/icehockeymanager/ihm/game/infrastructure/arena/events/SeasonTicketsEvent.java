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
  
package org.icehockeymanager.ihm.game.infrastructure.arena.events;

import java.util.*;

import org.icehockeymanager.ihm.game.*;
import org.icehockeymanager.ihm.game.scheduler.events.*;
import org.icehockeymanager.ihm.game.team.*;

/**
 * SeasonTicketsEvent - Selling of season tickets
 * 
 * @author adasen
 * @created Oct 19, 2004
 */
public class SeasonTicketsEvent extends SchedulerInternalEvent {

  static final long serialVersionUID = -3593586367405366673L;

  /**
   * SeasonTicketsEvent constructor
   * 
   * @param source
   *          Object
   * @param day
   *          Calendar
   */
  public SeasonTicketsEvent(Object source, Calendar day) {
    super(source, day, "events.infrastructure.seasonTickets");
  }

  /** Plays the event and does all the bookings */
  public void play() {
    super.play();

    Team[] teams = GameController.getInstance().getScenario().getTeams();

    for (int i = 0; i < teams.length; i++) {
      teams[i].getArena().seasonTickets();
    }

  }

}
