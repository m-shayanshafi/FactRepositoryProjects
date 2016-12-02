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
  
package org.icehockeymanager.ihm.game.player;

import org.icehockeymanager.ihm.game.finance.*;
import org.icehockeymanager.ihm.game.team.*;
import java.io.*;

/**
 * PlayerContract is based on Contract. contains additional transfer fee
 * informations
 * 
 * @author Arik Dasen / Bernhard von Gunten
 * @created September, 2004
 */
public class PlayerContract extends Contract implements Serializable {

  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 3258129154816619570L;

  /** Transfer fee */
  private double transferFee;

  /** Creates a player contract based on Contract between Team and Player. 
   * @param a 
   * @param b */
  public PlayerContract(Team a, Player b) {
    super(a, b);
    interval = INTERVAL_MONTHLY;
  }

  public void setTransferFee(double transferFee) {
    this.transferFee = transferFee;
  }

  public double getTransferFee() {
    return transferFee;
  }

  public double getCosts() {
    return amount / interval;
  }

  public Team getTeam() {
    return getPartyA();
  }

  public Player getPlayer() {
    return (Player) getPartyB();
  }

}
