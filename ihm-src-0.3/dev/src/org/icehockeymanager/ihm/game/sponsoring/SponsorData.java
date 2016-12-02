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
  
package org.icehockeymanager.ihm.game.sponsoring;

import java.io.Serializable;

import org.icehockeymanager.ihm.lib.ToolsXML;
import org.w3c.dom.Element;

/**
 * SponsorData
 * 
 * @author Arik Dasen, Bernhard von Gunten
 * @created Nov, 2004
 */
public class SponsorData implements Serializable {

  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 3978146526025495352L;

  /** Sponsor name */
  private String name = null;

  /** Area (like Arena, Main, TV) */
  private String type;

  /** Probability */
  private int probability;

  /**
   * SponsorData constructor
   * 
   * @param element
   */
  public SponsorData(Element element) {
    this.name = element.getAttribute("SPONSORNAME");
    this.type = element.getAttribute("TYPE");
    this.probability = ToolsXML.getIntAttribute(element, "PROBABILITY");
  }

  /**
   * Returns the type.
   * 
   * @return type
   */
  public String getType() {
    return type;
  }

  /**
   * Returns the name.
   * 
   * @return name
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the probability.
   * 
   * @return probability
   */
  public int getProbability() {
    return probability;
  }
}
