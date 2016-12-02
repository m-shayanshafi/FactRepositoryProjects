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
  
package org.icehockeymanager.ihm.game.finance;

/**
 * Costs - a simple interface for easier accounting
 * 
 * @author adasen
 * @created Oct 19, 2004
 */
public interface Costs {

  /**
   * Return costs for this object
   * @return Costs
   */
  public double getCosts();
}
