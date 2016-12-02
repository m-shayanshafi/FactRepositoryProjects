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
  
package org.icehockeymanager.ihm.game.league.std.events;

import java.util.*;

import org.icehockeymanager.ihm.game.league.std.*;
import org.icehockeymanager.ihm.game.scheduler.events.*;

/**
 * StdSchedulerNewPlayoffLevelEvent When play() is started it calls the
 * generatePlayoffsLevel() function in the StdLeague.
 * 
 * @author Bernhard von Gunten
 * @created December, 2001
 */
public class StdNewPlayoffLevelEvent extends SchedulerInternalEvent {

  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 3833745495075402548L;

  private StdLeague league = null;

  private int level;

  /**
   * Constructor for the StdSchedulerNewPlayoffLevelEvent object
   * 
   * @param source
   *          The source of this event
   * @param day
   *          The day of this event
   * @param level
   *          The level of playoffs to create
   * @param league
   *          The league of this event
   */
  public StdNewPlayoffLevelEvent(Object source, Calendar day, int level, StdLeague league) {
    super(source, day, "StdSchedulerNewPlayoffLevelEvent");
    this.league = league;
    this.level = level;
  }

  /** Plays the event, and calls generatePlayoffsLevel(this.level) in the league */
  public void play() {
    super.play();
    league.generatePlayoffsLevel(level);
  }

}
