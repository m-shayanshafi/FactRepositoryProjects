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
import org.icehockeymanager.ihm.game.league.helper.*;
import org.icehockeymanager.ihm.game.match.*;
import org.icehockeymanager.ihm.game.scheduler.events.*;
import org.icehockeymanager.ihm.game.team.*;
import org.icehockeymanager.ihm.game.user.*;

/**
 * SchedulerEventGameDayMatches Gameday Event which contains all matches played
 * in a LeagueElement
 * 
 * @author Bernhard von Gunten / Arik Dasen
 * @created December, 2001
 */
public class GameDayMatchesEvent extends SchedulerSpecialEvent {

  static final long serialVersionUID = 130298835428907511L;

  /** Matches of this gameDay */
  private Vector<Match> matches = null;

  /** LeagueElement (owner) of the matches */
  private LeagueElement leagueElement = null;

  /** Round (iE. of season or playoffs) */
  private int round = 0;

  /**
   * Constructs the event, add matches and leagueElement to class
   * 
   * @param source
   *          The source of this event
   * @param day
   *          The day of this event
   * @param matches
   *          The matches of this gameDay
   * @param leagueElement
   *          The leagueElement of this gameDay
   * @param gameDayDescription
   *          The description of this gameDay
   * @param round 
   */
  public GameDayMatchesEvent(Object source, Calendar day, Vector<Match> matches, LeagueElement leagueElement, String gameDayDescription, int round) {
    super(source, day, gameDayDescription);
    this.matches = matches;
    this.leagueElement = leagueElement;
    this.round = round;
  }

  public int getRound() {
    return round;
  }

  /**
   * Returns leagueElement of this gameDay
   * 
   * @return The leagueElement fo this gameDay
   */
  public LeagueElement getLeagueElement() {
    return leagueElement;
  }

  /**
   * Adds match to this gameday
   * 
   * @param match
   *          The Match to be added to this gameDay
   */
  public void addMatch(Match match) {
    matches.add(match);
  }

  /**
   * Returns the matches of this gameday
   * 
   * @return All matches of this gameDay
   */
  public Vector<Match> getMatches() {
    return matches;
  }

  /**
   * Returns the matches of this gameday
   * 
   * @return All matches of this gameDay
   */
  public Match[] getMatchesArray() {
    return matches.toArray(new Match[matches.size()]);
  }

  /**
   * Returns boolean if user is involved
   * 
   * @param user
   *          The user looking for
   * @return True if user is involved in one of these matches
   */
  public boolean isUserInvolved(User user) {
    return (getMatch(user) != null);
  }

  /**
   * Returns boolean if team is involved
   * 
   * @param team
   *          The team looking for
   * @return True if team is involved in one of these matches
   */
  public boolean isTeamInvolved(Team team) {
    return (getMatch(team) != null);
  }

  /**
   * Returns match of a single user
   * 
   * @param user
   *          The user looking for
   * @return The match of a user (if found)
   */
  public Match getMatch(User user) {
    for (int i = 0; i < matches.size(); i++) {
      Match tmp = matches.get(i);
      if (tmp.isUserInvolved(user)) {
        return tmp;
      }
    }
    return null;
  }

  /**
   * Returns match of a Team
   * 
   * @param team
   *          The team looking for
   * @return The match of a team (if found)
   */
  public Match getMatch(Team team) {
    for (int i = 0; i < matches.size(); i++) {
      Match tmp = matches.get(i);
      if (tmp.getTeamHome().equals(team) || tmp.getTeamAway().equals(team)) {
        return tmp;
      }
    }
    return null;
  }

  /**
   * Returns if "gui break" is needed, depending on user interest
   * 
   * @return Returns true if "wished"
   */
  public boolean needsOnline() {

    // Gell all users playing this great game
    User[] allUsers = GameController.getInstance().getScenario().getUsers();

    // Check if anyone is interested in these games, and return true, so we see
    // the matches online
    for (int i = 0; i < allUsers.length; i++) {
      if (allUsers[i].isInterestedInLeagueElement(getLeagueElement())) {
        // Check if user is intersted in leagueElement
        if (allUsers[i].getStopForGames() == User.STOP_FOR_ALL_GAMES) {
          // User want's to stop for all games in element
          return true;
        } else {
          // User only wants to stop for games he'll really play
          Vector players = GameController.getInstance().getScenario().getUsersWithinMatches(getMatches(), true);
          if (players.indexOf(allUsers[i]) > -1) {
            return true;
          }
        }
      }
    }

    return false;
  }

}
