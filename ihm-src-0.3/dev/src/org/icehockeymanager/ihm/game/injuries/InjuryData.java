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

import org.w3c.dom.*;
import org.icehockeymanager.ihm.lib.*;
import java.io.*;

/**
 * InjuryData containing an injury with duration, description an dprobability.
 * 
 * @author Bernhar von Gunten
 * @created October, 2004
 */
public class InjuryData implements Serializable {

  static final long serialVersionUID = -5018964457482035488L;

  /**
   * Duration in days
   */
  private int duration;

  /**
   * Message key
   */
  private String msgKey;

  /**
   * Master translation.
   */
  private String masterTranslation;

  /**
   * Probability
   */
  private int probablity;

  /**
   * PROBABILITY_COMMON
   */
  public static final int PROBABILITY_COMMON = 1;

  /**
   * PROBABILITY_AVERAGE
   */
  public static final int PROBABILITY_AVERAGE = 2;

  /**
   * PROBABILITY_RARE
   */
  public static final int PROBABILITY_RARE = 3;

  /**
   * InjuryData constructor by XML Element.
   * 
   * @param element
   *          Element
   */
  public InjuryData(Element element) {
    this.duration = ToolsXML.getIntAttribute(element, "DURATION");
    this.msgKey = element.getAttribute("MSGKEY");
    this.masterTranslation = element.getAttribute("MASTERTRANSLATION");
    this.probablity = ToolsXML.getIntAttribute(element, "PROBABILITY");
  }

  /**
   * Returns the duration of a injury
   * 
   * @return int
   */
  public int getDuration() {
    return duration;
  }

  /**
   * Returns the message key
   * 
   * @return String
   */
  public String getMsgKey() {
    return msgKey;
  }

  /**
   * Returns the master translation.
   * 
   * @return String
   */
  public String getMasterTranslation() {
    return masterTranslation;
  }

  /**
   * Returns the probability of this injury.
   * 
   * @return int
   */
  public int getProbability() {
    return this.probablity;
  }

}
