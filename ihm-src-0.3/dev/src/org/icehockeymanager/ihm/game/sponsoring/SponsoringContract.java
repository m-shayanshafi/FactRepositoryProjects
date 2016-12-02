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

import org.icehockeymanager.ihm.game.finance.Contract;
import org.icehockeymanager.ihm.game.team.Team;
import java.io.*;

/**
 * SponsoringContract
 * 
 * @author Arik Dasen, Bernhard von Gunten
 * @created Oct, 2004
 */
public class SponsoringContract extends Contract implements Serializable {

  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 3257288032565408568L;

  /**
   * sponsor name
   */
  private String sponsorName;

  /**
   * Sponsoring type
   */
  private String sponsoringType;

  /**
   * Sponsors happiness
   */
  private int sponsorHappiness;

  /**
   * SponsoringContract constructor
   * 
   * @param a Team a
   * @param b Object (Player b);
   * @param sponsorName
   * @param sponsoringType
   */
  public SponsoringContract(Team a, Object b, String sponsorName, String sponsoringType) {
    super(a, b);
    this.sponsorName = sponsorName;
    this.sponsoringType = sponsoringType;
    this.sponsorHappiness = 100;
    interval = INTERVAL_MONTHLY;
  }

  /**
   * @return Returns the sponsorName.
   */
  public String getSponsorName() {
    return sponsorName;
  }

  /**
   * @param sponsorName The sponsorName to set.
   */
  public void setSponsorName(String sponsorName) {
    this.sponsorName = sponsorName;
  }

  /**
   * Returns sponsoring type
   * 
   * @return sponsoring type
   */
  public String getSponsoringType() {
    return sponsoringType;
  }

  /**
   * Sets sponsoring Type
   * 
   * @param sponsoringType
   */
  public void setSponsoringType(String sponsoringType) {
    this.sponsoringType = sponsoringType;
  }

  /**
   * Returns the sponsors happiness
   * 
   * @return sponsr happiness (0-100)
   */
  public int getSponsorHappiness() {
    return sponsorHappiness;
  }

  /**
   * Sets the sponsors happiness
   * 
   * @param sponsorHappiness
   */
  public void setSponsorHappiness(int sponsorHappiness) {
    this.sponsorHappiness = sponsorHappiness;

    if (sponsorHappiness < 0) {
      sponsorHappiness = 0;
    }
    if (sponsorHappiness > 100) {
      sponsorHappiness = 100;
    }
  }

  /**
   * Changes the sponsors happiness
   * 
   * @param inc
   */
  public void increaseSponsorHappines(int inc) {
    this.sponsorHappiness += inc;

    if (sponsorHappiness < 0) {
      sponsorHappiness = 0;
    }
    if (sponsorHappiness > 100) {
      sponsorHappiness = 100;
    }

  }
}
