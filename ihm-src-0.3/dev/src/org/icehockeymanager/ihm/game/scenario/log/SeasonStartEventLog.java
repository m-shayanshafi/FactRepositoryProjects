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
  
package org.icehockeymanager.ihm.game.scenario.log;

import java.util.*;

import org.icehockeymanager.ihm.game.eventlog.*;
import org.icehockeymanager.ihm.game.user.*;

/**
 * SchedulerEventSeasonStart Special event that resets the game and places
 * already a endYear event in the calendar.
 * 
 * @author Bernhard von Gunten / Arik Dasen
 * @created December, 2001
 */
public class SeasonStartEventLog extends EventLogEntry {

  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 4050762693478854960L;

  public static final String MESSAGE_KEY = "events.yearstart";

  /**
   * Creates the event
   * 
   * @param source
   *          The source of this event
   * @param day
   *          The day of this event
   * @param user 
   */
  public SeasonStartEventLog(Object source, Calendar day, User user) {
    super(source, day, user);
  }
}