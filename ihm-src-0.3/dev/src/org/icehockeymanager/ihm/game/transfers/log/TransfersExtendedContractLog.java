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
 * TransfersExtendedContractLog logs if a contract has been extended containing
 * the old and new Contract of a player.
 * 
 * @author Bernhard von Gunten
 * @created September, 2004
 */
public class TransfersExtendedContractLog extends EventLogEntry {

  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 3256438088652895032L;

  /**
   * MESSAGE_KEY
   */
  public static final String MESSAGE_KEY = "events.transfersextendedcontractevent";

  /**
   * old Contract
   */
  private PlayerContract oldContract = null;

  /**
   * new Contract
   */
  private PlayerContract newContract = null;

  /**
   * TransfersExtendedContractLog constructor
   * 
   * @param source
   *          Object
   * @param day
   *          Calendar
   * @param player
   *          Player
   * @param oldContract
   *          PlayerContract
   * @param newContract
   *          PlayerContract
   */
  public TransfersExtendedContractLog(Object source, Calendar day, Player player, PlayerContract oldContract, PlayerContract newContract) {
    super(source, day, player);
    this.oldContract = oldContract;
    this.newContract = newContract;
  }

  /**
   * Returns new contract
   * 
   * @return PlayerContract
   */
  public PlayerContract getNewContract() {
    return newContract;
  }

  /**
   * Returns old contract.
   * 
   * @return PlayerContract
   */
  public PlayerContract getOldContract() {
    return oldContract;
  }

}
