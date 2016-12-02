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
  
package org.icehockeymanager.ihm.clients.devgui.ihm.sponsoring;

import java.util.*;

import javax.swing.*;

import org.icehockeymanager.ihm.game.sponsoring.SponsoringContract;

/**
 * LMOfferedContracts is a simple list of offered sponsorings
 * 
 * @author Bernhard von Gunten
 * @created August, 2005
 */
public class LMOfferedContracts extends AbstractListModel {

  private static final long serialVersionUID = 6975642245694965464L;

  /**
   * Offered contracts
   */
  private Vector<SponsoringContract> offeredContracts;

  /**
   * Constructor for the LMOfferedContracts object
   * 
   * @param offeredContracts The sponsorings offereed
   */
  public LMOfferedContracts(Vector<SponsoringContract> offeredContracts) {
    this.offeredContracts = offeredContracts;
  }

  /**
   * Gets the size attribute of the LMOfferedContracts object
   * 
   * @return The size value
   */
  public int getSize() {
    return offeredContracts.size();
  }

  /**
   * Gets the contract description by row
   * 
   * @param row The row
   * @return The match description
   */
  public Object getElementAt(int row) {

    return SponsoringTools.getSponsoringListDescription(getContract(row));
  }

  /**
   * Returns the specific contract for a row
   * 
   * @param row The row of a contract
   * @return The match value
   */
  public SponsoringContract getContract(int row) {
    return offeredContracts.get(row);
  }

}
