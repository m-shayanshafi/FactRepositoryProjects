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
  
package org.icehockeymanager.ihm.game.ai.events;

import java.util.*;

import org.icehockeymanager.ihm.game.scheduler.events.*;
import org.icehockeymanager.ihm.game.prospects.*;

/**
 * End Year jobs for "Computer Player"
 * 
 * @author Bernhard von Gunten
 * @created October, 2004
 */
public class AIYearEndEvent extends SchedulerInternalEvent {

  static final long serialVersionUID = -8745231275165146350L;

  /**
   * Constructor for the Weekly Computer Player Events
   * 
   * @param source
   *          Owner of the event
   * @param day
   *          Day of the event
   */
  public AIYearEndEvent(Object source, Calendar day) {
    super(source, day, "events.ai.yearend");
  }

  /** Plays the event */
  public void play() {
    super.play();

    Prospects.aiHireProspects();
  }

}
