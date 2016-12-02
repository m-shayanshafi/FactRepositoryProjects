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
  
package org.icehockeymanager.ihm.game.scheduler.events;

import java.io.*;
import java.util.*;

import org.icehockeymanager.ihm.game.user.*;

/**
 * SchedulerBreakerEvent is a abstract class for every event that shall break
 * the game thread, and give back the cotrol to the Client.
 * 
 * @author Bernhard von Gunten
 * @created October 2004
 */
public abstract class SchedulerBreakerEvent extends SchedulerEvent implements Serializable {

  /**
   * SchedulerBreakerEvent Constructor
   * 
   * @param source
   *          Object
   * @param day
   *          Calendar
   * @param messageKey
   *          String
   */
  public SchedulerBreakerEvent(Object source, Calendar day, String messageKey) {
    super(source, day, messageKey);
  }

  /**
   * Plays the super.play() function and "finalizes" this function, so no real
   * BreakerEvent may have a play function.
   */
  public final void play() {
    super.play();
  }

  /**
   * Abstract interface for sub classes to get all Users interested in this
   * BreakerEvent.
   * 
   * @return User[] The users
   */
  public abstract User[] getUsersInterested();

  /**
   * Returns true if User is interested in this BreakerEvent.
   * 
   * @param user
   *          User
   * @return boolean
   */
  public final boolean isUserInterested(User user) {
    User[] users = getUsersInterested();

    for (int i = 0; i < users.length; i++) {
      if (users[i].equals(user)) {
        return true;
      }
    }

    return false;
  }

  /**
   * Function to add Users who are interested in this BreakerEvent to an
   * existing User Vector. Only adds new users to the vector.
   * 
   * @param existings
   *          Vector
   */
  public final void attachUsersInterested(Vector<User> existings) {
    User[] users = getUsersInterested();
    for (int i = 0; i < users.length; i++) {
      if (!existings.contains(users[i])) {
        existings.add(users[i]);
      }
    }

  }

}
