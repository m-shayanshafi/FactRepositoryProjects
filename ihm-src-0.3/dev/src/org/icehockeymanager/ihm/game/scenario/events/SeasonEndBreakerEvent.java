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
  
package org.icehockeymanager.ihm.game.scenario.events;

import java.util.*;

import org.icehockeymanager.ihm.game.*;
import org.icehockeymanager.ihm.game.scheduler.events.*;
import org.icehockeymanager.ihm.game.user.*;

/**
 * Empty BreakerEvent to stop the GameThread for the new season start. Returns
 * all user as "interested"
 * 
 * @author Bernhard von Gunten
 * @created October 2004
 */
public class SeasonEndBreakerEvent extends SchedulerBreakerEvent {

  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 3761975981606778167L;

  /**
   * MESSAGE_KEY
   */
  public static final String MESSAGE_KEY = "events.breaker.yearstart";

  /**
   * SeasonEndBreakerEvent
   * 
   * @param source
   * @param day
   */
  public SeasonEndBreakerEvent(Object source, Calendar day) {
    super(source, day, MESSAGE_KEY);
  }

  /**
   * Returns all users as interested
   * @return Users 
   */
  public User[] getUsersInterested() {
    return GameController.getInstance().getScenario().getUsers();
  }

}