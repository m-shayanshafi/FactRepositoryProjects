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
  
package org.icehockeymanager.ihm.game.randomimpacts;

import java.io.*;

import org.icehockeymanager.ihm.lib.*;
import org.w3c.dom.*;

/**
 * RandomImpact is a (base) class containing a possible impact on a player, team
 * etc.
 * 
 * @author Bernhard von Gunten
 * @created October, 2004
 */
public class RandomImpact implements Serializable {

  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 3905244511369507891L;

  /**
   * Impact on morale key
   */
  public static final String IMPACT_ON_MORALE = "impact.on.morale";

  /**
   * Impact on energy key
   */
  public static final String IMPACT_ON_ENERGY = "impact.on.energy";

  /**
   * Impact on form key
   */
  public static final String IMPACT_ON_FORM = "impact.on.form";

  /**
   * Msgkey
   */
  private String msgKey;

  /**
   * Master translation
   */
  private String masterTranslation;

  /**
   * Impact on (morale, energy, form)
   */
  private String impactOn;

  /**
   * Impact height
   */
  private int impact;

  /**
   * RandomImpact constructor (by XML element)
   * 
   * @param element
   */
  public RandomImpact(Element element) {
    this.impactOn = element.getAttribute("IMPACT_ON");
    this.msgKey = element.getAttribute("MSGKEY");
    this.masterTranslation = element.getAttribute("MASTERTRANSLATION");
    this.impact = ToolsXML.getIntAttribute(element, "IMPACT");
  }

  /**
   * Returns message key
   * 
   * @return msgKEy
   */
  public String getMsgKey() {
    return msgKey;
  }

  /**
   * Returns master translation
   * 
   * @return Master translation
   */
  public String getMasterTranslation() {
    return masterTranslation;
  }

  /**
   * Returns impact target
   * 
   * @return impactOn
   */
  public String getImpactOn() {
    return impactOn;
  }

  /**
   * Returns impact value
   * 
   * @return impact
   */
  public int getImpact() {
    return impact;
  }

}
