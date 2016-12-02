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
import org.icehockeymanager.ihm.game.league.std.*;
import org.icehockeymanager.ihm.game.scheduler.events.*;
import org.icehockeymanager.ihm.game.team.*;
import org.icehockeymanager.ihm.game.user.*;

/**
 * BreakerEvent to announce loser of a league.
 * 
 * @author Bernhard von Gunten
 * @created December, 2001
 */
public class StdLastPlaceKnownEvent extends SchedulerBreakerEvent {

  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 3257005440947990579L;

  /**
   * League
   */
  private StdLeague league = null;

  /**
   * Constructor for the StdSchedulerPlayoutFinishedEvent object
   * 
   * @param source
   *          The source of this event
   * @param day
   *          The day of this event
   * @param league
   *          The league of this event
   */
  public StdLastPlaceKnownEvent(Object source, Calendar day, StdLeague league) {
    super(source, day, "StdSchedulerPlayoutFinishedEvent");
    this.league = league;
  }

  /**
   * Returns the loser of this league.
   * 
   * @return Team
   */
  public Team getLoser() {
    return league.getLastPlacedTeam();
  }

  /**
   * Returns the league.
   * 
   * @return StdLeague
   */
  public StdLeague getLeague() {
    return league;
  }

  /**
   * Returns the users interested in this league.
   * 
   * @return User[]
   */
  public User[] getUsersInterested() {
    return GameController.getInstance().getScenario().getUsers();
  }

}
