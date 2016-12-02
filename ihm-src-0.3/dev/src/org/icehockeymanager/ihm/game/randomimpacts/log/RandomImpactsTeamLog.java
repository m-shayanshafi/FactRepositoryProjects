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
  
package org.icehockeymanager.ihm.game.randomimpacts.log;

import java.util.*;

import org.icehockeymanager.ihm.game.eventlog.*;
import org.icehockeymanager.ihm.game.randomimpacts.*;
import org.icehockeymanager.ihm.game.team.*;

/**
 * RandomImpactsTeamLog is a log containing the random impact object.
 * 
 * @author Bernhard von Gunten
 * @created October, 2004
 */
public class RandomImpactsTeamLog extends EventLogEntry {

  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 3761973743878025525L;

  /**
   * MESSAGE_KEY
   */
  public static final String MESSAGE_KEY = "events.playerInjured";

  /**
   * RandomImpactTeam
   */
  private RandomImpactTeam randomImpactTeam = null;

  /**
   * RandomImpactsTeamLog constructor.
   * 
   * @param source
   * @param day
   * @param team
   * @param randomImpactTeam
   */
  public RandomImpactsTeamLog(Object source, Calendar day, Team team, RandomImpactTeam randomImpactTeam) {
    super(source, day, team);
    this.randomImpactTeam = randomImpactTeam;
  }

  /**
   * Returns the RandomImpactTeam object
   * 
   * @return randomImpactTeam
   */
  public RandomImpactTeam getRandomImpact() {
    return randomImpactTeam;
  }

}
