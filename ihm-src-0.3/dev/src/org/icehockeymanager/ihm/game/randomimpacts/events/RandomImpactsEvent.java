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
  
package org.icehockeymanager.ihm.game.randomimpacts.events;

import java.util.*;

import org.icehockeymanager.ihm.game.randomimpacts.*;
import org.icehockeymanager.ihm.game.scheduler.events.*;

/**
 * RandomImpactsEvent trains all teams and their players in the play function.
 * 
 * @author Bernhard von Gunten
 * @created July, 2002
 */
public class RandomImpactsEvent extends SchedulerInternalEvent {

  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 3977583576139116849L;

  public static final String MESSAGE_KEY = "events.randomglobalimpacts";

  public RandomImpactsEvent(Object source, Calendar day) {
    super(source, day, MESSAGE_KEY);
  }

  public void play() {
    super.play();

    RandomImpacts.createRandomImpactOnPlayer();
    RandomImpacts.createRandomImpactOnTeam();

  }

}
