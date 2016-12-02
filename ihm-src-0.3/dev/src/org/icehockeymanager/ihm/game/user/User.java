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
  
package org.icehockeymanager.ihm.game.user;

import java.io.*;
import java.util.*;

import org.icehockeymanager.ihm.game.league.helper.*;
import org.icehockeymanager.ihm.game.team.*;

/**
 * The User class is hosting all informations of a person playing this game.
 * 
 * @author Bernhard von Gunten
 * @created December 2001
 */
public class User implements Serializable {

  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 3258688810379260211L;

  /** Stop for each games user is interested in */
  public final static int STOP_FOR_ALL_GAMES = 0;

  /** Stop for each games user is involved */
  public final static int STOP_FOR_USER_GAMES = 1;

  /** Username */
  private String userName;

  /** Pointer on the team */
  private Team team;

  /** Vector off LeagueElements user is interested in */
  private Vector<LeagueElement> interestedLeagueElements;

  /** Current stop level */
  private int stopForGames = STOP_FOR_ALL_GAMES;

  /** Playertraining by computer */
  private boolean autoTraining = true;

  /** Player transfers by computer */
  private boolean autoTransfers = true;

  /** Sponsoring by computer */
  private boolean autoSponsoring = true;

  /** Prospect hiring by computer */
  private boolean autoProspectHiring = true;

  /**
   * Creates new user
   * 
   * @param userName
   *          The user name
   */
  public User(String userName) {
    interestedLeagueElements = new Vector<LeagueElement>();
    this.userName = userName;
  }

  /** Reset all user settings */
  public void newSeason() {
    interestedLeagueElements = new Vector<LeagueElement>();
  }

  /**
   * Sets the autoTraining attribute of the User object
   * 
   * @param autoTraining
   *          The new autoTraining value
   */
  public void setAutoTraining(boolean autoTraining) {
    this.autoTraining = autoTraining;
  }

  /**
   * Gets the autoTraining attribute of the User object
   * 
   * @return The autoTraining value
   */
  public boolean isAutoTraining() {
    return autoTraining;
  }

  /**
   * Sets if the user lets the AI handle the transfers for his team.
   * 
   * @param autotransfers
   *          boolean
   */
  public void setAutoTransfers(boolean autotransfers) {
    this.autoTransfers = autotransfers;
  }

  /**
   * Returns true if the user lets the AI handle the transfers.
   * 
   * @return boolean
   */
  public boolean isAutoTransfers() {
    return this.autoTransfers;
  }

  /**
   * Returns true, when the User lets the AI handle the prospect hiring.
   * 
   * @return boolean
   */
  public boolean isAutoProspectHiring() {
    return this.autoProspectHiring;
  }

  /**
   * Sets if the User lets the AI handle the prospect hiring.
   * 
   * @param hiring
   *          boolean
   */
  public void setAutoProspectHiring(boolean hiring) {
    this.autoProspectHiring = hiring;
  }

  /**
   * Set team for this user
   * 
   * @param team
   *          The new team value
   */
  public void setTeam(Team team) {
    this.team = team;
  }

  /**
   * Returns team of this user
   * 
   * @return The team value
   */
  public Team getTeam() {
    return team;
  }

  /**
   * Set stop level for this user
   * 
   * @param stopForGames
   *          The new stopForGames value
   */
  public void setStopForGames(int stopForGames) {
    this.stopForGames = stopForGames;
  }

  /**
   * Returns stop level of this user
   * 
   * @return The stopForGames value
   */
  public int getStopForGames() {
    return this.stopForGames;
  }

  /**
   * Returns user name
   * 
   * @return The userName value
   */
  public String getUserName() {
    return userName;
  }

  /**
   * Sets user name
   * 
   * @param userName
   *          The new userName value
   */
  public void setUserName(String userName) {
    this.userName = userName;
  }

  /**
   * Add LeagueElement user is interested in
   * 
   * @param leagueElement
   *          The leagueElement to be added to the interests
   */
  public void addInterestedLeagueElement(LeagueElement leagueElement) {
    if (this.interestedLeagueElements.indexOf(leagueElement) == -1) {
      this.interestedLeagueElements.add(leagueElement);
    }
  }

  /**
   * Remove LeagueElement user isn't interested in any more
   * 
   * @param leagueElement
   *          The leagueElement to be deleted from the interests
   */
  public void removeInterestedLeagueElement(LeagueElement leagueElement) {
    interestedLeagueElements.remove(leagueElement);
  }

  /**
   * Returns true if user is interested in league Element passed by
   * 
   * @param leagueElement
   *          The leagueElement looking for
   * @return True if leagueElement is in interested list.
   */
  public boolean isInterestedInLeagueElement(LeagueElement leagueElement) {
    return (interestedLeagueElements.indexOf(leagueElement) >= 0);
  }

  /**
   * Returns true if user is interested in one of the passed LeagueElements
   * 
   * @param leagueElements
   *          The leagueElements looking for
   * @return True if one of the leagueElements is found in the interested list.
   */
  public boolean isInterestedInLeagueElements(Vector<LeagueElement> leagueElements) {
    for (int i = 0; i < leagueElements.size(); i++) {
      if (isInterestedInLeagueElement(leagueElements.get(i))) {
        return true;
      }
    }
    return false;
  }

  /**
   * Returns all league elements user is interested in
   * 
   * @return A Vector of leaugeElements
   */
  public Vector getInterestedLeagueElements() {
    return interestedLeagueElements;
  }

  /**
   * Description of the Method
   * 
   * @return Description of the Return Value
   */
  public String toString() {
    return this.userName + ", Team: " + this.team.toString();
  }

  /**
   * @return Returns the autoSponsoring.
   */
  public boolean isAutoSponsoring() {
    return autoSponsoring;
  }

  /**
   * @param autoSponsoring
   *          The autoSponsoring to set.
   */
  public void setAutoSponsoring(boolean autoSponsoring) {
    this.autoSponsoring = autoSponsoring;
  }
}
