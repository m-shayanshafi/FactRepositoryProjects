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
  
package org.icehockeymanager.ihm.game.infrastructure.arena;

import java.util.*;

import org.icehockeymanager.ihm.game.*;
import org.icehockeymanager.ihm.game.finance.*;
import org.icehockeymanager.ihm.game.infrastructure.*;
import org.icehockeymanager.ihm.game.match.*;
import org.icehockeymanager.ihm.game.team.*;
import java.io.*;

/**
 * Arena
 * 
 * @author adasen
 * @created Oct 19, 2004
 */
public class Arena extends InfrastructureObject implements Serializable {

  static final long serialVersionUID = 248896386272968145L;

  public static final int CATEGORY_COUNT = 8;
  
  /**
   * name of arena (like "Joe Louis Arena")
   */
  private String name;

  /**
   * array of categories
   */
  private ArenaCategory[] category;

  /**
   * total spectators for this match (temporarly)
   */
  private int totalSpectators;

  /**
   * number of sponsor areas
   */
  private int numOfSponsorAreas;

  /**
   * Arena constructor
   * 
   * @param owner
   *          Team
   * @param name
   *          String
   * @param capacity
   *          int
   * @param numOfSponsorAreas
   *          int
   */
  public Arena(Team owner, String name, int capacity, int numOfSponsorAreas) {
    super(owner, 5000000);
    this.name = name;
    this.numOfSponsorAreas = numOfSponsorAreas;
    category = new ArenaCategory[CATEGORY_COUNT];

    // Initialize all stadiums the same
    category[0] = new ArenaCategory(owner, "A", capacity / 4, 20, 300, ArenaCategory.COMFORT_NORMAL);
    category[1] = new ArenaCategory(owner, "B", capacity / 4, 20, 300, ArenaCategory.COMFORT_NORMAL);
    category[2] = new ArenaCategory(owner, "C", capacity / 4, 20, 300, ArenaCategory.COMFORT_NORMAL);
    category[3] = new ArenaCategory(owner, "D", capacity / 4, 20, 300, ArenaCategory.COMFORT_NORMAL);
    category[4] = new ArenaCategory(owner, "A-1");
    category[5] = new ArenaCategory(owner, "B-1");
    category[6] = new ArenaCategory(owner, "C-1");
    category[7] = new ArenaCategory(owner, "D-1");
    

  }

  /**
   * calculates, sets and accounts season tickets
   */
  public void seasonTickets() {
    // can only be called once per season; subject to change
    int revenue = 0;
    int seasonTickets = 0;
    for (int i = 0; i < category.length; i++) {
      seasonTickets += category[i].calculateSeasonTickets();
      revenue += category[i].getSeasonTicketsRevenues();
    }
    Calendar today = (Calendar) GameController.getInstance().getScenario().getScheduler().getToday().clone();
    // put the revenues in the accounting object
    owner.getAccounting().addBookingEntry(new BookingEntry(revenue, today, "accounting.booking.seasonTicketsRevenues"));

  }

  /**
   * Do things before the game, such as ticketing ...
   * 
   * @param match
   *          Match
   */
  public void preGame(Match match) {
    int revenue = 0;
    totalSpectators = 0;
    for (int i = 0; i < category.length; i++) {
      totalSpectators += category[i].calculateSpectators();
      revenue += category[i].getCurrentRevenues();
    }
    Calendar today = (Calendar) GameController.getInstance().getScenario().getScheduler().getToday().clone();
    // put the revenues in the accounting object
    match.getTeamHome().getAccounting().addBookingEntry(new BookingEntry(revenue, today, "accounting.booking.matchRevenues"));
  }

  /**
   * Do things after game, like setting spectators to 0
   */
  public void postGame() {
    for (int i = 0; i < category.length; i++) {
      category[i].setCurrentSpectators(0);
    }
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
   * Set the name of the Arena (like "Joe Louis Arena")
   * 
   * @param name
   *          The name to set.
   */
  public void setName(String name) {
    this.name = name;
  }
  /**
   * Returns the numOfSponsorAreas.
   * 
   * @return numOfSponsorAreas
   */
  public int getNumOfSponsorAreas() {
    return numOfSponsorAreas;
  }

  /**
   * Set number of sponsor areas
   * 
   * @param numOfSponsorAreas
   *          The numOfSponsorAreas to set.
   */
  public void setNumOfSponsorAreas(int numOfSponsorAreas) {
    this.numOfSponsorAreas = numOfSponsorAreas;
  }

  /**
   * Returns the totalCapacity.
   * 
   * @return totalCapacity
   */
  public int getTotalCapacity() {
    int result = 0;
    for (int i = 0; i < category.length; i ++ ) {
      result += category[i].getCapacity();
    }
    return result;
  }

  /**
   * Returns the totalSpectators.
   * 
   * @return totalSpectators
   */
  public int getTotalSpectators() {
    return totalSpectators;
  }
  
  public ArenaCategory[] getArenaCategories() {
    return category;
  }
  
  
  public int calculatePriceForMaintenance() {
    int price = 0;
    for (int i = 0; i < category.length; i ++) {
      if (category[i].isBuilt()) {
        price += category[i].calculatePriceForMaintenance();
      }
    }
    return price;
  }

  public void maintain() {
    for (int i = 0; i < category.length; i ++) {
      if (category[i].isBuilt()) {
        category[i].maintain();
      }
    }
  }
  
  
  public void lowerCondition() {
    for (int i = 0; i < category.length; i ++) {
      if (category[i].isBuilt()) {
        category[i].lowerCondition();
      }
    }
  }
  
  
  
}
