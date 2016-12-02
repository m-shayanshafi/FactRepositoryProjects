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
  
package org.icehockeymanager.ihm.game.scheduler;

import java.io.*;
import java.util.*;

/**
 * Scheduler Provides a "subCalendar", used to get empty dates for placing
 * events in the Scheduler.
 * 
 * @author Bernhard von Gunten
 * @created December 2001
 */
public class SchedulerHelper implements Serializable {

  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 3907214836139176755L;

  /** Current day */
  private Calendar currentDay;

  /** Possible week days for events */
  private String gameDays = "MO TU WE TH FR SA SU";

  /**
   * Creates new instance of a helper starting with the day passed by
   * 
   * @param currentDay
   *          The current day
   */
  public SchedulerHelper(Calendar currentDay) {
    this.currentDay = currentDay;
  }

  /**
   * Set possible week days for events
   * 
   * @param gameDays
   *          The new gameDays value
   */
  public void setGameDays(String gameDays) {
    this.gameDays = gameDays;
  }

  /**
   * Returns the next free date for an event
   * 
   * @return The nextFreeGameDay value
   */
  public Calendar getNextFreeGameDay() {
    currentDay.add(Calendar.DATE, 1);
    while (!isGameDay(currentDay)) {
      currentDay.add(Calendar.DATE, 1);
    }
    return (Calendar) currentDay.clone();
  }

  /**
   * Checks if date is allowed in the week
   * 
   * @param currentDay
   *          The current Day
   * @return The gameDay value
   */
  private boolean isGameDay(Calendar currentDay) {
    int dayOfWeek = currentDay.get(Calendar.DAY_OF_WEEK);
    switch (dayOfWeek) {
    case Calendar.MONDAY: {
      return (gameDays.indexOf("MO") >= 0);
    }
    case Calendar.TUESDAY: {
      return (gameDays.indexOf("TU") >= 0);
    }
    case Calendar.WEDNESDAY: {
      return (gameDays.indexOf("WE") >= 0);
    }
    case Calendar.THURSDAY: {
      return (gameDays.indexOf("TH") >= 0);
    }
    case Calendar.FRIDAY: {
      return (gameDays.indexOf("FR") >= 0);
    }
    case Calendar.SATURDAY: {
      return (gameDays.indexOf("SA") >= 0);
    }
    case Calendar.SUNDAY: {
      return (gameDays.indexOf("SU") >= 0);
    }
    default: {
      return true;
    }
    }

  }

}
