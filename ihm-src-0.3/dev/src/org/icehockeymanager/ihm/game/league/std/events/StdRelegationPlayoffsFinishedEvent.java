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
 * StdSchedulerRelegationPlayoffsFinishedEvent When play() is started it calls
 * the relegationPlayoffsFinished() function in the StdLeagueOwner.
 * 
 * @author Bernhard von Gunten / Arik Dasen
 * @created December 29, 2001
 */
public class StdRelegationPlayoffsFinishedEvent extends SchedulerBreakerEvent {

  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 4051331158069491765L;

  private StdPlayoffs playoffs = null;

  /**
   * Constructor for the StdSchedulerRelegationPlayoffsFinishedEvent object
   * 
   * @param source
   *          The source of this event
   * @param day
   *          The day of this event
   * @param leagueOwner
   *          The leagueOwner of this event
   * @param playoffs
   *          The playoffs of this event
   */
  public StdRelegationPlayoffsFinishedEvent(Object source, Calendar day, StdLeagueOwner leagueOwner, StdPlayoffs playoffs) {
    super(source, day, "StdSchedulerPlayoffRoundFinishedEvent");
    this.playoffs = playoffs;
  }

  /**
   * Returns if gui is needed (if a team changes league)
   * 
   * @return True if gui is needed
   */
  public boolean needsOnline() {
    Team winner = playoffs.getWinners()[0];
    League league = playoffs.getLeague();
    return (!league.isTeamOfLeague(winner));
  }

  /**
   * Returns if a team changes league
   * 
   * @return True if team changes league
   */
  public boolean relegation() {
    return needsOnline();
  }

  /**
   * Returns the winner
   * 
   * @return Team
   */
  public Team getWinner() {
    return playoffs.getWinners()[0];
  }

  /**
   * Returns the Loser
   * 
   * @return Team
   */
  public Team getLoser() {
    return playoffs.getLosers()[0];
  }

  /**
   * Returns the league
   * 
   * @return League
   */
  public League getLeague() {
    return playoffs.getLeague();
  }

  /**
   * Returns the users interested in this relegation playoffs.
   * 
   * @return User[]
   */
  public User[] getUsersInterested() {
    return GameController.getInstance().getScenario().getUsers();
  }

}
