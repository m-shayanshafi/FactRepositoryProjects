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
  
package org.icehockeymanager.ihm.game.prospects.events;

import java.util.*;

import org.icehockeymanager.ihm.game.*;
import org.icehockeymanager.ihm.game.team.*;
import org.icehockeymanager.ihm.game.scheduler.events.*;
import org.icehockeymanager.ihm.game.prospects.*;

/**
 * ProspectTrainingEvent trains the prospects.
 * 
 * @author Bernhard von Gunten
 * @created October, 2004
 */
public class ProspectsTrainingEvent extends SchedulerInternalEvent {

  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 4050201959661581878L;

  /**
   * MESSAGE_KEY
   */
  public static final String MESSAGE_KEY = "events.prospects.training";

  /**
   * ProspectsTrainingEvent
   * 
   * @param source
   *          Owner of the event
   * @param day
   *          Day of the event
   */
  public ProspectsTrainingEvent(Object source, Calendar day) {
    super(source, day, MESSAGE_KEY);
  }

  /** Plays the event and trains the players */
  public void play() {
    super.play();

    Team[] teams = GameController.getInstance().getScenario().getTeams();

    for (int i = 0; i < teams.length; i++) {
      Prospects.trainProspects(teams[i]);
    }

  }

}
