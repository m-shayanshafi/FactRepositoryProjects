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
  
package org.icehockeymanager.ihm.game.match;

import java.io.*;

/**
 * MatchData is the base class for all informations (goals, situations etc.)
 * given to clients about a match
 * 
 * @author Bernhard von Gunten
 * @created January 2005
 */
public abstract class MatchData implements Serializable {

  /** The time in seconds of this data */
  protected int time;

  /**
   * Constructs the MatchData
   * 
   * @param time
   */
  public MatchData(int time) {
    this.time = time;
  }

  /**
   * Returns the time of this "object" happend.
   * 
   * @return Time
   */
  public int getTime() {
    return time;
  }

}