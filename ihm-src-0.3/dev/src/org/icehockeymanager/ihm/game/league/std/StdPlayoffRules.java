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
  
package org.icehockeymanager.ihm.game.league.std;

import org.icehockeymanager.ihm.game.league.*;

/**
 * StdPlayoffRules class contains rules for match/scoreboard in StdPlayoffs
 * 
 * @author Arik Dasen
 * @created December, 2001
 */
public class StdPlayoffRules extends Rules {

  static final long serialVersionUID = -8763470507426373584L;

  /**
   * Returns the number of periods.
   * 
   * @return Number of periods
   */
  public int numberOfPeriods() {
    return 3;
  }

  /**
   * Minutes per period
   * 
   * @return Minutes per period
   */
  public int minutesPerPeriod() {
    return 20;
  }

  /**
   * Will overtime be played
   * 
   * @return Will overtime be played
   */
  public boolean playOvertime() {
    return true;
  }

  /**
   * Minutes per overtime
   * 
   * @return Minutes per overtime
   */
  public int minutesPerOvertime() {
    return 20;
  }

  /**
   * Will suddenDeath be played
   * 
   * @return If suddenDeath will be played
   */
  public boolean suddenDeath() {
    return true;
  }

  /**
   * Play multiple overtimes until we have a winner
   * 
   * @return Play multiple overtimes until we have a winner
   */
  public boolean contignousOvertime() {
    return false;
  }

  /**
   * Players per Overtime
   * 
   * @return Players per Overtime
   */
  public int playersPerOvertime() {
    return 5;
  }

  /**
   * Play shootout
   * 
   * @return If shootout will be played
   */
  public boolean playShootout() {
    return true;
  }

  /**
   * Points for win
   * 
   * @return Points for win
   */
  public int pointsForWin() {
    return 2;
  }

  /**
   * Points for loss
   * 
   * @return Points for loss
   */
  public int pointsForLoss() {
    return 0;
    // so what ???
  }

  /**
   * Points for tied
   * 
   * @return Points for tied
   */
  public int pointsForTied() {
    return 1;
  }

  /**
   * Points for overtime win
   * 
   * @return Points for overtime win
   */
  public int pointsForOvertimeWin() {
    return 0;
  }

  /**
   * Points for overtime loss
   * 
   * @return Points for overtime loss
   */
  public int pointsForOvertimeLoss() {
    return 0;
  }

  /** Constructor for the StdPlayoffRules object */
  public StdPlayoffRules() {
  }
}
