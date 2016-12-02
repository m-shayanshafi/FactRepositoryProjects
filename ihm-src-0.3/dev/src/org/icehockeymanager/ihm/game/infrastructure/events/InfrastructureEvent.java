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
  
package org.icehockeymanager.ihm.game.infrastructure.events;

import java.util.*;

import org.icehockeymanager.ihm.game.*;
import org.icehockeymanager.ihm.game.scheduler.events.*;
import org.icehockeymanager.ihm.game.team.*;

public class InfrastructureEvent  extends SchedulerInternalEvent{


  private static final long serialVersionUID = 5560652439570411696L;

  public InfrastructureEvent(Object source, Calendar day) {
    super(source, day, "events.infrastructure");
  }

  /** Plays the event and does all the bookings */
  public void play() {
    super.play();

    Team[] teams = GameController.getInstance().getScenario().getTeams();

    for (int i = 0; i < teams.length; i++) {
      teams[i].getInfrastructure().lowerConditions();
    }

  }
}
