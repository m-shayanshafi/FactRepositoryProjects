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

import org.icehockeymanager.ihm.game.player.*;
import org.w3c.dom.*;

/**
 * RandomImpactPlayer is based on RandomImpact and contains a function to
 * generate impacts on players.
 * 
 * @author Bernhard von Gunten
 * @created October, 2004
 */
public class RandomImpactPlayer extends RandomImpact {

  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 3616732690372374834L;

  /**
   * EVENT TYPE PLAYER
   */
  public static final String RANDOM_EVENT_TYPE = "RANDOMIMPACT.PLAYER";

  /**
   * RandomImpactPlayer constructor with XML Element
   * 
   * @param element
   */
  public RandomImpactPlayer(Element element) {
    super(element);
  }

  /**
   * Generates the impact on a player
   * 
   * @param player
   *          Player
   */
  public void impactOnPlayer(Player player) {
    if (this.getImpactOn().equals(IMPACT_ON_MORALE)) {
      player.impactOnMorale(this.getImpact());
    } else if (this.getImpactOn().equals(IMPACT_ON_ENERGY)) {
      player.impactOnEnergy(this.getImpact());
    } else if (this.getImpactOn().equals(IMPACT_ON_FORM)) {
      player.impactOnForm(this.getImpact());
    }
  }

}
