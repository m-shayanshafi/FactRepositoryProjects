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
  
package org.icehockeymanager.ihm.game.injuries.log;

import java.util.*;

import org.icehockeymanager.ihm.game.eventlog.*;
import org.icehockeymanager.ihm.game.injuries.*;
import org.icehockeymanager.ihm.game.player.*;

/**
 * InjuriesEventPlayerInjuredLog logs an injury on a player.
 * 
 * @author Bernhrad von Gunten
 * @created October, 2004
 */
public class InjuriesEventIPlayerInjuredLog extends EventLogEntry {

  static final long serialVersionUID = 6071418448717351607L;

  /**
   * MESSAGE_KEY
   */
  public static final String MESSAGE_KEY = "events.playerInjured";

  /**
   * Player injury.
   */
  private PlayerInjury playerInjury = null;

  /**
   * InjuriesEventPlayerInjuredLog constructor
   * 
   * @param source
   *          Object
   * @param day
   *          Calendar
   * @param player
   *          Player
   * @param playerInjury
   *          PlayerInjury
   */
  public InjuriesEventIPlayerInjuredLog(Object source, Calendar day, Player player, PlayerInjury playerInjury) {
    super(source, day, player);
    this.playerInjury = playerInjury;
  }

  /**
   * Returns the injury
   * 
   * @return PlayerInjury
   */
  public PlayerInjury getPlayerInjury() {
    return playerInjury;
  }

}
