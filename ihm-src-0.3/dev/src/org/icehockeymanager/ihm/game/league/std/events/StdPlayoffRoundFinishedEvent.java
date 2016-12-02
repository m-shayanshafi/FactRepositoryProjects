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
 * StdSchedulerPlayoffRoundFinishedEvent When play() is started it calls the
 * roundPlayed() function in the StdPlayoffs.
 * 
 * @author Bernhard von Gunten
 * @created December, 2001
 */
public class StdPlayoffRoundFinishedEvent extends SchedulerInternalEvent {
  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 3905809690326546488L;

  private StdPlayoffs playoffs = null;

  private int playoffRound;

  /**
   * Constructor for the StdSchedulerPlayoffRoundFinishedEvent object
   * 
   * @param source
   *          The source of this event
   * @param day
   *          The day of this event
   * @param playoffs
   *          The playoffs of this event
   * @param playoffRound
   *          The playoffRound finished in this event
   */
  public StdPlayoffRoundFinishedEvent(Object source, Calendar day, StdPlayoffs playoffs, int playoffRound) {
    super(source, day, "StdSchedulerPlayoffRoundFinishedEvent");
    this.playoffs = playoffs;
    this.playoffRound = playoffRound;
  }

  /** Plays the event and calls roundPlayed(this.playoffRound) in the playoffs */
  public void play() {
    super.play();
    this.playoffs.roundPlayed(playoffRound);
  }

}
