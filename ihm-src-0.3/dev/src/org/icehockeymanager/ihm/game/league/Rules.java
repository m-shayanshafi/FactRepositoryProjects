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
  
package org.icehockeymanager.ihm.game.league;

import java.io.*;

/**
 * Rules class contains rules for match/scoreboard abstract base class for each
 * rule
 * 
 * @author Arik Dasen
 * @created December 29, 2001
 */
public abstract class Rules implements Serializable {

  /**
   * Number of regular periods in game
   * 
   * @return Number of regular periods in a match
   */
  public abstract int numberOfPeriods();

  /**
   * Minutes to play per regular period
   * 
   * @return Minutes to play in a match
   */
  public abstract int minutesPerPeriod();

  /**
   * Seconds to play per regular period
   * 
   * @return Seconds to play in a period
   */
  public int secondsPerPeriod() {
    return minutesPerPeriod() * 60;
  }

  /**
   * Do we play overtime when game is tied
   * 
   * @return If overtime has to be played.
   */
  public abstract boolean playOvertime();

  /**
   * Minutes to play per overtime period
   * 
   * @return Overtime period length
   */
  public abstract int minutesPerOvertime();

  /**
   * Seconds to play per overtime period
   * 
   * @return Seconds to play in a period
   */
  public int secondsPerOvertime() {
    return minutesPerOvertime() * 60;
  }

  /**
   * Does overtime ends after a goal
   * 
   * @return If overtime is finished by a sudden death
   */
  public abstract boolean suddenDeath();

  /**
   * Play multiple overtimes until we have a winner
   * 
   * @return If multiple overtimes will be played
   */
  public abstract boolean contignousOvertime();

  /**
   * How many field players in overtime
   * 
   * @return Number of field players in overtime
   */
  public abstract int playersPerOvertime();

  /**
   * Do we play a shootout
   * 
   * @return If shootout is played
   */
  public abstract boolean playShootout();

  /**
   * Points for a regular win
   * 
   * @return Number of points for a win
   */
  public abstract int pointsForWin();

  /**
   * Points for a regular loss (don't blame me on that ;-))
   * 
   * @return Number of points for a loss
   */
  public abstract int pointsForLoss();

  /**
   * Points for a regular draw
   * 
   * @return Number of points for a tied game
   */
  public abstract int pointsForTied();

  /**
   * Points for a overtime win, if overtime is played, only this will count
   * 
   * @return Number of points for a overtime win
   */
  public abstract int pointsForOvertimeWin();

  /**
   * Points for a overtime loss (this makes more sense)
   * 
   * @return Number of points for a overtime loss
   */
  public abstract int pointsForOvertimeLoss();

  // MORE RULES TO COME

  /** Constructor for the Rules object */
  public Rules() {
  }
}
