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
  
package org.icehockeymanager.ihm.game.infrastructure;

import org.icehockeymanager.ihm.game.team.*;
import org.icehockeymanager.ihm.game.finance.*;
import java.io.*;

/**
 * InfrastructureObject - Base class for all infrastructur objects
 * 
 * @author adasen
 * @created Oct 19, 2004
 */
public abstract class InfrastructureObject implements Costs, Serializable {

  static final long serialVersionUID = 4494711665877296502L;

  /**
   * maintenance costs
   */
  protected double maintenance;

  /**
   * value of infrastructure object
   */
  protected double value;

  /**
   * owner
   */
  protected Team owner;

  
  /**
   * Condition
   */
  protected int condition;
  
  /**
   * InfrastructureObject constructor
   * 
   * @param owner
   * @param value
   */
  public InfrastructureObject(Team owner, double value) {
    this.condition = 100;
    this.owner = owner;
    this.value = value;
    this.maintenance = value / 10000;
  }

  public abstract void lowerCondition();
  
  public int getCondition() {
    return condition;
  }
  
  public void setCondition(int condition) {
    this.condition = condition;
  }
  
  
  
  /**
   * Returns costs (maintenance)
   * @return costs 
   */
  public double getCosts() {
    return maintenance;
  }

  /**
   * Get maintenance
   * 
   * @return maintenance
   */
  public double getMaintenance() {
    return maintenance;
  }

  /**
   * Set the maintenance costs
   * 
   * @param maintenance
   *          The maintenance to set.
   */
  public void setMaintenance(double maintenance) {
    this.maintenance = maintenance;
  }

  /**
   * Get the owner (Team)
   * 
   * @return owner
   */
  public Team getOwner() {
    return owner;
  }

  /**
   * Set the owner (Team)
   * 
   * @param owner
   *          The owner to set.
   */
  public void setOwner(Team owner) {
    this.owner = owner;
  }

  /**
   * Get value
   * 
   * @return Returns the value.
   */
  public double getValue() {
    return value;
  }

  /**
   * Set value
   * 
   * @param value
   *          The value to set.
   */
  public void setValue(double value) {
    this.value = value;
  }
}
