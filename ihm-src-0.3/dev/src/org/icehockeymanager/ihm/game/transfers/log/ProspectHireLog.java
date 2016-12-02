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
import org.icehockeymanager.ihm.game.team.*;

/**
 * ProspectHireLog logs a new contract for a prospect containing the contract.
 * 
 * @author Bernhard von Gunten
 * @created September, 2004
 */
public class ProspectHireLog extends EventLogEntry {

  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 3691043183651862323L;

  /**
   * MESSAGE_KEY
   */
  public static final String MESSAGE_KEY = "events.hireprospectevent";

  /**
   * PlayerContract
   */
  private PlayerContract contract = null;

  /**
   * ProspectHireLog constructor including contract.
   * 
   * @param source
   *          Object
   * @param day
   *          Calendar
   * @param newTeam
   *          Team
   * @param player
   *          Player
   * @param contract
   *          PlayerContract
   */
  public ProspectHireLog(Object source, Calendar day, Team newTeam, Player player, PlayerContract contract) {
    super(source, day, null, newTeam, player);
    this.contract = contract;
  }

  /**
   * Returns the new contract
   * 
   * @return PlayerContract
   */
  public PlayerContract getContract() {
    return contract;
  }
}
