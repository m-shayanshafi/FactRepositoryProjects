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
  
package org.icehockeymanager.ihm.game.transfers.log;

import java.util.*;

import org.icehockeymanager.ihm.game.eventlog.*;
import org.icehockeymanager.ihm.game.player.*;

/**
 * TransfersListLog logs the player who has been placed on the TransferList.
 * 
 * @author Bernhard von Gunten
 * @created September, 2004
 */
public class TransfersListLog extends EventLogEntry {

  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 3257854272547796024L;

  /**
   * MESSAGE_KEY
   */
  public static final String MESSAGE_KEY = "events.transferslistevent";

  /**
   * TransfersListLog constructor
   * 
   * @param source
   *          Object
   * @param day
   *          Calendar
   * @param player
   *          Player
   */
  public TransfersListLog(Object source, Calendar day, Player player) {
    super(source, day, player);
  }

}
