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
  
package org.icehockeymanager.ihm.game.eventlog;

import java.io.*;
import java.text.*;
import java.util.*;

import org.icehockeymanager.ihm.game.player.*;
import org.icehockeymanager.ihm.game.team.*;
import org.icehockeymanager.ihm.game.user.*;

/**
 * EventLogEntry contains a log message including, possible parameeters like
 * team, player, useers who are related to ta message.
 * 
 * @author Arik Dasen, Bernhard von Gunten
 * @created October, 2004
 */
public class EventLogEntry implements Serializable, Comparable<EventLogEntry> {

  static final long serialVersionUID = -4267641621258438565L;

  /**
   * Team
   */
  private Team team = null;

  /**
   * Player
   */
  private Player player = null;

  /**
   * User
   */
  private User user = null;

  /**
   * Source
   */
  private Object source = null;

  /**
   * Day
   */
  private Calendar day = null;

  /**
   * MESSAGE_KEY
   */
  public String MESSAGE_KEY = "eventlogentry.base";

  /**
   * EventLogEntry constructor
   * 
   * @param source
   *          Object
   * @param day
   *          Calendar
   * @param user
   *          User
   * @param team
   *          Team
   * @param player
   *          Player
   */
  public EventLogEntry(Object source, Calendar day, User user, Team team, Player player) {

    this.team = team;
    this.player = player;
    this.day = day;
    this.source = source;
    this.user = user;
  }

  /**
   * EventLogEntry contructor
   * 
   * @param source
   *          Object
   * @param day
   *          Calendar
   * @param user
   *          User
   */
  public EventLogEntry(Object source, Calendar day, User user) {
    this(source, day, user, null, null);
  }

  /**
   * EventLogEntry constructor
   * 
   * @param source
   *          Object
   * @param day
   *          Calendar
   * @param team
   *          Team
   */
  public EventLogEntry(Object source, Calendar day, Team team) {
    this(source, day, null, team, null);
  }

  /**
   * EventLogEntry constructor
   * 
   * @param source
   *          Object
   * @param day
   *          Calendar
   * @param player
   *          Player
   */
  public EventLogEntry(Object source, Calendar day, Player player) {
    this(source, day, null, null, player);
  }

  /**
   * @return Returns the day.
   */
  public Calendar getDay() {
    return day;
  }

  /**
   * @return Returns the player.
   */
  public Player getPlayer() {
    return player;
  }

  /**
   * @return Returns the source.
   */
  public Object getSource() {
    return source;
  }

  /**
   * @return Returns the team.
   */
  public Team getTeam() {
    return team;
  }

  /**
   * @return Returns the user.
   */
  public User getUser() {
    return user;
  }

  /**
   * Returns the message key
   * 
   * @return String
   */
  public String getMessageKey() {
    return MESSAGE_KEY;
  }

  /**
   * Returns sort value
   * 
   * @return long
   */
  public long getSortValue() {
    return day.getTime().getTime();
  }

  /**
   * Returns formated date
   * 
   * @return The formatedDate value
   */
  public String getFormatedDate() {
    SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
    return df.format(day.getTime());
  }

  /**
   * Implements the comparable interface
   * 
   * @param o
   *          The object to compare
   * @return Standard compare values
   */
  public int compareTo(EventLogEntry o) {
    EventLogEntry tmp = o;
    if (tmp.getSortValue() > getSortValue()) {
      return -1;
    }
    if (tmp.getSortValue() < getSortValue()) {
      return 1;
    }
    return 0;
  }
}
