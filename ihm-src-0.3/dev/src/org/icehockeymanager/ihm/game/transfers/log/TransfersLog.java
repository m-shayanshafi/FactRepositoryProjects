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
 * TransferLog logs a transfer, containing newTeam, oldTeam, contract etc.
 * 
 * @author Bernhard von Gunten
 * @created September, 2004
 */
public class TransfersLog extends EventLogEntry {

  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 3979272425999184691L;

  /**
   * MESSAGE_KEY
   */
  public static final String MESSAGE_KEY = "events.transfersevent";

  /**
   * contract
   */
  private PlayerContract contract = null;

  /**
   * new Team
   */
  private Team newTeam = null;

  /**
   * old Team
   */
  private Team oldTeam = null;

  /**
   * Is it a buyOut
   */
  private boolean buyOut = false;

  /**
   * transfer fee
   */
  private double transferFee = 0;

  /**
   * TransfersLog constructor
   * 
   * @param source
   *          Object
   * @param day
   *          Calendar
   * @param newTeam
   *          Team
   * @param oldTeam
   *          Team
   * @param player
   *          Player
   * @param contract
   *          PlayerContract
   * @param buyOut
   *          boolean
   * @param transferFee
   *          double
   */
  public TransfersLog(Object source, Calendar day, Team newTeam, Team oldTeam, Player player, PlayerContract contract, boolean buyOut, double transferFee) {
    super(source, day, player);
    this.oldTeam = oldTeam;
    this.newTeam = newTeam;
    this.contract = contract;
    this.buyOut = buyOut;
    this.transferFee = transferFee;
  }

  /**
   * Returns the contract
   * 
   * @return PlayerContract
   */
  public PlayerContract getContract() {
    return contract;
  }

  /**
   * Returns the old team
   * 
   * @return Team
   */
  public Team getOldTeam() {
    return oldTeam;
  }

  /**
   * Returns the new team
   * 
   * @return Team
   */
  public Team getNewTeam() {
    return newTeam;
  }

  /**
   * Returns the buyOut flag
   * 
   * @return boolean
   */
  public boolean isBuyOut() {
    return buyOut;
  }

  /**
   * Returns the transfer fee
   * 
   * @return double
   */
  public double getTransferFee() {
    return transferFee;
  }
}
