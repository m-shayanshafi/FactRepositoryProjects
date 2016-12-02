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
  
package org.icehockeymanager.ihm.game.injuries;

import java.util.*;
import java.io.*;

/**
 * PlayerInjury is a object to attach an injury to a player.
 * 
 * @author Bernhard von Gunten
 * @created October, 2004
 */
public class PlayerInjury implements Serializable {

  static final long serialVersionUID = -90738397527727166L;

  /**
   * Startdate
   */
  private Calendar start = null;

  /**
   * Injury
   */
  private InjuryData injuryData = null;

  /**
   * PlayerInjury constructor
   * 
   * @param start
   *          Calendar
   * @param injuryData
   *          InjuryData
   */
  public PlayerInjury(Calendar start, InjuryData injuryData) {
    this.start = start;
    this.injuryData = injuryData;
  }

  /**
   * Helper function: Returns the duration
   * 
   * @return int
   */
  public int getDuration() {
    return injuryData.getDuration();
  }

  /**
   * Returns the start date of this injury
   * 
   * @return Calendar
   */
  public Calendar getStart() {
    return (Calendar) start.clone();
  }

  /**
   * Returns the end date of this injury.
   * 
   * @return Calendar
   */
  public Calendar getEnd() {
    Calendar result = getStart();
    result.add(Calendar.DATE, injuryData.getDuration());
    return result;
  }

  /**
   * Helper function: Retunrs the message key
   * 
   * @return String
   */
  public String getMsgKey() {
    return injuryData.getMsgKey();
  }

  /**
   * Helper function: Returns the master translation.
   * 
   * @return String
   */
  public String getMasterTranslation() {
    return injuryData.getMasterTranslation();
  }
}
