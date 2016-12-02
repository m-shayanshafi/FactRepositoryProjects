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
  
package org.icehockeymanager.ihm.game.league.events;

import java.util.*;

import org.icehockeymanager.ihm.game.*;
import org.icehockeymanager.ihm.game.scheduler.events.*;
import org.icehockeymanager.ihm.game.user.*;
import org.icehockeymanager.ihm.game.match.*;
import org.icehockeymanager.ihm.game.league.helper.*;

/**
 * SchedulerEventGameDayArrived Special class that contains all gameDays of a
 * specific date, and stops for a "gui break"
 * 
 * @author Bernhard von Gunten / Arik Dasen
 * @created December, 2001
 */
public class GameDayArrivedEvent extends SchedulerBreakerEvent {

  static final long serialVersionUID = 229276424835832821L;

  /** Vector of all gamedays */
  private Vector<GameDayMatchesEvent> SchedulerGameDayEvents = null;

  /**
   * Create new event
   * 
   * @param source
   *          The source of this event
   * @param day
   *          The day of this event
   */
  public GameDayArrivedEvent(Object source, Calendar day) {
    super(source, day, "SchedulerStopBeforeGamedayEvent");
    this.SchedulerGameDayEvents = new Vector<GameDayMatchesEvent>();
  }

  /**
   * Add new gameDay on this "gui break"
   * 
   * @param SchedulerGameDayEvent
   *          The gameDay to be added to this event
   */
  public void addSchedulerGameDayEvent(GameDayMatchesEvent SchedulerGameDayEvent) {
    this.SchedulerGameDayEvents.add(SchedulerGameDayEvent);
  }

  /**
   * Returns if online is needed by userinterests
   * 
   * @return Returns true if online is "wished"
   */
  public boolean needsOnline() {
    return (getUsersInterested().length > 0);
  }

  /**
   * Returns users that are interested in this stopBeforeGameDayEvent
   * 
   * @return All users interested in this event
   */
  public User[] getUsersInterested() {
    // Result with all users who want to stop
    Vector<User> interestedUsers = new Vector<User>();

    // Gell all users playing this great game
    User[] allUsers = GameController.getInstance().getScenario().getUsers();

    // Check if anyone is interested in these games.
    for (int i = 0; i < allUsers.length; i++) {
      if (allUsers[i].isInterestedInLeagueElements(getLeagueElements())) {
        // Check if user is intersted in leagueElement
        if (allUsers[i].getStopForGames() == User.STOP_FOR_ALL_GAMES) {
          // User want's to stop for all games in element
          interestedUsers.add(allUsers[i]);
        } else {
          // User only wants to stop for games he'll really play
          Vector<User> players = GameController.getInstance().getScenario().getUsersWithinMatches(getMatches(), true);
          if (players.indexOf(allUsers[i]) > -1) {
            interestedUsers.add(allUsers[i]);
          }
        }
      }
    }

    return interestedUsers.toArray(new User[interestedUsers.size()]);
  }

  /**
   * Returns all matches of all gameDays
   * 
   * @return All matches of all gameDays
   */
  private Vector<Match> getMatches() {
    Vector<Match> result = new Vector<Match>();
    for (int i = 0; i < SchedulerGameDayEvents.size(); i++) {
      GameDayMatchesEvent tmp = SchedulerGameDayEvents.get(i);
      Vector<Match> matches = tmp.getMatches();
      for (int n = 0; n < matches.size(); n++) {
        result.add(matches.get(n));
      }
    }
    return result;
  }

  /**
   * Returns gameDayEvent for a single User
   * 
   * @param user
   *          The user looking for
   * @return The gameDay for a user (if found)
   */
  public GameDayMatchesEvent getSchedulerGameDayEvent(User user) {
    for (int i = 0; i < SchedulerGameDayEvents.size(); i++) {
      GameDayMatchesEvent tmp = SchedulerGameDayEvents.get(i);
      if (tmp.isUserInvolved(user)) {
        return tmp;
      }
    }
    return null;
  }

  /**
   * Returns all leagueElements
   * 
   * @return All leagueElements of this event
   */
  private Vector<LeagueElement> getLeagueElements() {
    Vector<LeagueElement> result = new Vector<LeagueElement>();
    for (int i = 0; i < SchedulerGameDayEvents.size(); i++) {
      GameDayMatchesEvent tmp = SchedulerGameDayEvents.get(i);
      result.add(tmp.getLeagueElement());
    }
    return result;
  }

}
