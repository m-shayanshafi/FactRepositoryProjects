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

import org.icehockeymanager.ihm.game.*;
import org.icehockeymanager.ihm.game.league.*;
import org.icehockeymanager.ihm.game.league.std.*;
import org.icehockeymanager.ihm.game.scheduler.events.*;
import org.icehockeymanager.ihm.game.team.*;
import org.icehockeymanager.ihm.game.user.*;

/**
 * BreakerEvent to announce winner of a league.
 * 
 * @author Bernhard von Gunten
 * @created December, 2001
 */
public class StdFirstPlaceKnownEvent extends SchedulerBreakerEvent {

  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 3977582498119037494L;

  /**
   * League
   */
  private StdLeague league = null;

  /**
   * Constructor for the StdSchedulerPlayoffFinishedEvent object
   * 
   * @param source
   *          The source of this event
   * @param day
   *          The day of this event
   * @param league
   *          The league of this event
   */
  public StdFirstPlaceKnownEvent(Object source, Calendar day, StdLeague league) {
    super(source, day, "StdSchedulerPlayoffFinishedEvent");
    this.league = league;
  }

  /**
   * Returns the winner of this playoffs
   * 
   * @return Team
   */
  public Team getWinner() {
    return league.getFirstPlacedTeam();
  }

  /**
   * Returns the league.
   * 
   * @return League
   */
  public League getLeague() {
    return league;
  }

  /**
   * Returns users interested in this league.
   * 
   * @return User[]
   */
  public User[] getUsersInterested() {
    return GameController.getInstance().getScenario().getUsers();
  }

}
