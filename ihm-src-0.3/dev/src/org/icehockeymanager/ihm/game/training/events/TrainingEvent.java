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
  
package org.icehockeymanager.ihm.game.training.events;

import java.util.*;

import org.icehockeymanager.ihm.game.*;
import org.icehockeymanager.ihm.game.player.*;
import org.icehockeymanager.ihm.game.scheduler.events.*;
import org.icehockeymanager.ihm.game.training.*;
import org.icehockeymanager.ihm.lib.*;

/**
 * Trainingevent trains all teams and their players in the play function.
 * 
 * @author Bernhard von Gunten
 * @created July, 2002
 */
public class TrainingEvent extends SchedulerInternalEvent {

  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 3544951073636168249L;

  /**
   * MESSAGE_KEY
   */
  public static final String MESSAGE_KEY = "events.training";

  /**
   * Constructor for the TrainingEvent object
   * 
   * @param source
   *          Owner of the event
   * @param day
   *          Day of the event
   */
  public TrainingEvent(Object source, Calendar day) {
    super(source, day, MESSAGE_KEY);
  }

  /**
   * Plays the event and trains the players
   */
  public void play() {
    super.play();

    Player[] players = GameController.getInstance().getScenario().getPlayers();
    int weekday = IhmTools.DayOfTheWeek(Calendar.DAY_OF_WEEK);

    for (int i = 0; i < players.length; i++) {
      Training.train(players[i], weekday);
    }
  }

}