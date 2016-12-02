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
  
package org.icehockeymanager.ihm.lib;

/**
 * Sort interface for all ihm table models
 * 
 * @author Bernhard von Gunten
 * @created December 29, 2001
 */
public interface IhmTableModelSorter {

  /**
   * Implement Sort in the table model
   * 
   * @param columnNr
   *          Collumn to sort
   * @param standard
   *          Standard sort order
   */
  public void ihmSort(int columnNr, boolean standard);

}
