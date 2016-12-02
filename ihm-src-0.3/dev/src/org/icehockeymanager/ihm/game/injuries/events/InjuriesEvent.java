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
  
package org.icehockeymanager.ihm.game.injuries.events;

import java.util.*;

import org.icehockeymanager.ihm.game.injuries.*;
import org.icehockeymanager.ihm.game.scheduler.events.*;

/**
 * InjuriesEvent creates & heales injuries on players.
 * 
 * @author Bernhard von Gunten
 * @created October, 2004
 */
public class InjuriesEvent extends SchedulerInternalEvent {

  static final long serialVersionUID = -3263012172625472296L;

  /**
   * MESSAGE_KEY
   */
  public static final String MESSAGE_KEY = "events.injuries";

  /**
   * Constructor for the TrainingEvent object
   * 
   * @param source
   *          Owner of the event
   * @param day
   *          Day of the event
   */
  public InjuriesEvent(Object source, Calendar day) {
    super(source, day, MESSAGE_KEY);
  }

  /** Plays the event and trains the players */
  public void play() {
    super.play();

    Injuries.removeInjuries();
    Injuries.addInjuries();

  }

}
