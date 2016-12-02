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

import org.icehockeymanager.ihm.game.team.*;
import org.w3c.dom.*;

/**
 * RandomImpactTeam is based on RandomImpact and contains a function to generate
 * impacts on teams.
 * 
 * @author Bernhard von Gunten
 * @created October, 2004
 */
public class RandomImpactTeam extends RandomImpact {

  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 3546923575987417904L;

  /**
   * RANDOM EVENT TYPE TEAM
   */
  public static final String RANDOM_EVENT_TYPE = "RANDOMIMPACT.TEAM";

  /**
   * RandomImpactTeam constructor with XML Element
   * 
   * @param element
   */
  public RandomImpactTeam(Element element) {
    super(element);
  }

  /**
   * Generates the impact on a team
   * 
   * @param team
   *          Team
   */
  public void impactOnTeam(Team team) {
    if (this.getImpactOn().equals(IMPACT_ON_MORALE)) {
      team.impactOnMorale(this.getImpact());
    } else if (this.getImpactOn().equals(IMPACT_ON_ENERGY)) {
      team.impactOnEnergy(this.getImpact());
    } else if (this.getImpactOn().equals(IMPACT_ON_FORM)) {
      team.impactOnForm(this.getImpact());
    }
  }

}
