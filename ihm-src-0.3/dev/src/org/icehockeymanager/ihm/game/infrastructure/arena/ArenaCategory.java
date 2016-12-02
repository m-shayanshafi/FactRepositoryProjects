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

import java.io.*;
import java.util.*;

import org.icehockeymanager.ihm.game.*;
import org.icehockeymanager.ihm.game.finance.*;
import org.icehockeymanager.ihm.game.team.*;

/**
 * ArenaCategory - Each arena is splitted in categories to have different
 * prices, etc
 * 
 * @author adasen, Bernhard von Gunten
 * @created Oct 19, 2004
 */
public class ArenaCategory implements Serializable {

  public static final int COMFORT_CHEAP = 0;
  
  public static final int COMFORT_NORMAL = 1;
  
  public static final int COMFORT_DELUXE = 2;
  
  static final long serialVersionUID = 2285381575789355336L;
  
  /**
   * Is this section actually built
   */
  protected boolean built = false;
  

  /**
   * Category comfort state
   */
  protected int categoryComfort;
  
  /**
   * Condition of category
   */
  protected int condition = 100;
  
  /**
   * capacity of categrory
   */
  protected int capacity;

  /**
   * Number of season tickets sold for this category
   */
  protected int seasonTickets;

  /**
   * ticket prize for this category
   */
  protected double ticketPrize;

  /**
   * season ticket prize for this category
   */
  protected double seasonTicketPrize;

  /**
   * current spectators in this category (temporarly for game)
   */
  protected int currentSpectators;

  /**
   * Short name (like "A") of the category
   */
  protected String categoryTitle;

  
  protected Team owner;
  
  /**
   * ArenaCategory constructor
   * @param owner 
   * @param categoryTitle 
   * @param capacity
   * @param ticketPrize
   * @param seasonTicketPrize
   * @param comfort 
   */
  public ArenaCategory(Team owner, String categoryTitle, int capacity, double ticketPrize, double seasonTicketPrize, int comfort) {
    this(owner, categoryTitle);
    this.capacity = capacity;
    this.ticketPrize = ticketPrize;
    this.seasonTicketPrize = seasonTicketPrize;
    this.condition = 100;
    seasonTickets = 0;
    currentSpectators = 0;
    this.built = true;
  }

  /**
   * ArenaCategory constructor
   * @param owner 
   * @param categoryTitle 
   * 
   */
  public ArenaCategory(Team owner, String categoryTitle) {
    this.owner = owner;
    this.categoryTitle = categoryTitle;
  }
  
  
  public int calculatePriceForSeatsIncrease(int seats, int comfort) {
    int costBase = GameController.getInstance().getScenario().getScenarioSettings().INFRASTRUCTURE_ARENA_COST_PER_SEAT_INCREASE;
    int costComfort = calculatePriceForComfortIncrease(seats, comfort); 
    
    return ((seats * costBase) + costComfort);
  }

  
  public int calculatePriceForMaintenance() {
    int costBase = GameController.getInstance().getScenario().getScenarioSettings().INFRASTRUCTURE_ARENA_COST_PER_SEAT_MAINTENANCE;
    int toMaintain = 100 - this.getCondition();
    int costMaintenance = toMaintain * costBase; 
    
    return costMaintenance;
  }

  
  public int calculatePriceForComfortIncrease(int seats, int comfort) {
    int costComfort = 0;
    if (comfort == COMFORT_CHEAP) {
      costComfort = GameController.getInstance().getScenario().getScenarioSettings().INFRASTRUCTURE_ARENA_COST_PER_SEAT_COMFORT_CHEAP;
    } else if (comfort == COMFORT_NORMAL) {
      costComfort = GameController.getInstance().getScenario().getScenarioSettings().INFRASTRUCTURE_ARENA_COST_PER_SEAT_COMFORT_NORMAL;
    } else if (comfort == COMFORT_DELUXE) {
      costComfort = GameController.getInstance().getScenario().getScenarioSettings().INFRASTRUCTURE_ARENA_COST_PER_SEAT_COMFORT_DELUXE;
    }
    return (seats * costComfort);
  }
  
  public void increaseCapacity(int seats) {
    this.built = true;
    this.setCapacity(this.getCapacity() + seats);
    Calendar day = GameController.getInstance().getScenario().getScheduler().getToday();

    owner.getAccounting().addBookingEntry(new BookingEntry(-calculatePriceForSeatsIncrease(seats, this.getCategoryComfort()), day, "accounting.booking.categorySizeIncrease"));
  }
  
  public void increaseComfort(int comfort) {
    this.setCategoryComfort(comfort);
    Calendar day = GameController.getInstance().getScenario().getScheduler().getToday();

    owner.getAccounting().addBookingEntry(new BookingEntry(-calculatePriceForComfortIncrease(getCapacity(), comfort), day, "accounting.booking.categoryComfortIncrease"));
  }
  
  public void maintain() {
    int price = this.calculatePriceForMaintenance();
    this.setCondition(100);
    Calendar day = GameController.getInstance().getScenario().getScheduler().getToday();

    owner.getAccounting().addBookingEntry(new BookingEntry(-price, day, "accounting.booking.categoryMaintenance"));
  }
  
  
  /**
   * Calculates and returns spectators for this game
   * 
   * @return spectators
   */
  public int calculateSpectators() {
    // full house ;-)
    currentSpectators = capacity;
    return currentSpectators;
  }

  /**
   * Calculates and returns number of season tickets
   * 
   * @return seasonTickets
   */
  public int calculateSeasonTickets() {
    // 30% season tickets
    // TODO: some more sophisticated calculation
    seasonTickets = Math.round(capacity * 0.3f);
    return seasonTickets;
  }

  /**
   * Returns season ticket revenues
   * 
   * @return revenues
   */
  public double getSeasonTicketsRevenues() {
    return seasonTickets * seasonTicketPrize;
  }

  /**
   * Return current revenues (for this game)
   * 
   * @return current revenues (for this game)
   */
  public double getCurrentRevenues() {
    return (currentSpectators - seasonTickets) * ticketPrize;
  }

  /**
   * Returns the capacity.
   * 
   * @return capacity
   */
  public int getCapacity() {
    return capacity;
  }

  /**
   * Set capacity of this category
   * 
   * @param capacity
   *          The capacity to set.
   */
  public void setCapacity(int capacity) {
    this.capacity = capacity;
  }

  /**
   * Returns the currentSpectators.
   * 
   * @return currentSpectators
   */
  public int getCurrentSpectators() {
    return currentSpectators;
  }

  /**
   * Set number of current spectators in this category
   * 
   * @param currentSpectators
   *          The currentSpectators to set.
   */
  public void setCurrentSpectators(int currentSpectators) {
    this.currentSpectators = currentSpectators;
  }

  /**
   * Returns the seasonTicketPrize.
   * 
   * @return seasonTicketPrize
   */
  public double getSeasonTicketPrize() {
    return seasonTicketPrize;
  }

  /**
   * Set the prize for season tickets in this category
   * 
   * @param seasonTicketPrize
   *          The seasonTicketPrize to set.
   */
  public void setSeasonTicketPrize(double seasonTicketPrize) {
    this.seasonTicketPrize = seasonTicketPrize;
  }

  /**
   * Returns the seasonTickets.
   * 
   * @return seasonTickets
   */
  public int getSeasonTickets() {
    return seasonTickets;
  }

  /**
   * Set number of season tickets sold in this category
   * 
   * @param seasonTickets
   *          The seasonTickets to set.
   */
  public void setSeasonTickets(int seasonTickets) {
    this.seasonTickets = seasonTickets;
  }

  /**
   * Returns the ticketPrize.
   * 
   * @return ticketPrize
   */
  public double getTicketPrize() {
    return ticketPrize;
  }

  /**
   * Set ticket prize for this category
   * 
   * @param ticketPrize
   *          The ticketPrize to set.
   */
  public void setTicketPrize(double ticketPrize) {
    this.ticketPrize = ticketPrize;
  }

  public boolean isBuilt() {
    return built;
  }

  public void setBuilt(boolean built) {
    this.built = built;
  }

  public int getCategoryComfort() {
    return categoryComfort;
  }

  public void setCategoryComfort(int categoryComfort) {
    this.categoryComfort = categoryComfort;
  }

  public String getCategoryTitle() {
    return categoryTitle;
  }

  public void setCategoryTitle(String categoryTitle) {
    this.categoryTitle = categoryTitle;
  }

  public int getCondition() {
    return condition;
  }

  public void setCondition(int condition) {
    this.condition = condition;
  }
  
  public void lowerCondition() {
    int lowerConditionBy = GameController.getInstance().getScenario().getScenarioSettings().INFRASTRUCTURE_ARENA_LOWER_CONDITION;
    this.setCondition(this.getCondition() - lowerConditionBy);
  }
}
