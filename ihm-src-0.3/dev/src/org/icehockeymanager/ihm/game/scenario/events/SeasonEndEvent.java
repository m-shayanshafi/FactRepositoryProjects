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

/**
 * SchedulerEventSeasonEnd Special event that "marks" the end of a year and
 * calls controller functions for a calendar "endSeason", and so should be
 * really the last event of a season !
 * 
 * @author Bernhard von Gunten / Arik Dasen
 * @created December, 2001
 */
public class SeasonEndEvent extends SchedulerInternalEvent {

  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 3257008765268931377L;

  /**
   * Creates event
   * 
   * @param source
   *          The source of this event
   * @param day
   *          The day of this event
   */
  public SeasonEndEvent(Object source, Calendar day) {
    super(source, day, "events.internal.yearend");
  }

  /** Resets the calendar and adds a (happy) newYear event */
  public void play() {
    super.play();
    // Backup season data for history etc.
    GameController.getInstance().getScenario().endSeason();
    // Set a NEW YEAR in the Calendar
    GameController.getInstance().getScenario().getScheduler().endSeason();
    // Add the first event to the year
    GameController.getInstance().getScenario().getScheduler().addEvent(new SeasonStartEvent(this, GameController.getInstance().getScenario().getScheduler().getFirstDay()));
  }

}