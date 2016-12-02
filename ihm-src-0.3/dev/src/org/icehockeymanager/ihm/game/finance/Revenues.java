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
 * Revenues - a simple interface for easier accounting
 * 
 * @author adasen
 * @created Oct 19, 2004
 */
public interface Revenues {

  /**
   * Return revenues for this object
   * @return double Revenues 
   */
  public double getRevenues();
}
