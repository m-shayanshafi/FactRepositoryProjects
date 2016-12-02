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
import org.icehockeymanager.ihm.game.infrastructure.arena.*;

import java.io.*;
import java.util.*;

/**
 * Infrastructure - Teams infrastructure objects
 * 
 * @author rik
 * @created 23.10.2004
 */
public class Infrastructure implements Costs, Serializable {

  static final long serialVersionUID = -3097568255115232971L;

  /** container for infrastructure objects */
  private Vector<InfrastructureObject> infrastructureObjects;

  /**
   * Infrastructure constructor
   * @param team 
   * 
   */
  public Infrastructure(Team team) {
    infrastructureObjects = new Vector<InfrastructureObject>();
    // dummy init of arena
    infrastructureObjects.addElement(new Arena(team, team.getTeamInfo().getTeamName() + " Arena", 3000, 10));
  }

  /**
   * Return arena (which is a special infrastructure object)
   * 
   * @return arena
   */
  public Arena getArena() {
    return (Arena) infrastructureObjects.elementAt(0);
  }

  /**
   * Adds infra structure to this team
   * 
   * @param object
   */
  public void addInfrastructureObject(InfrastructureObject object) {
    infrastructureObjects.addElement(object);
  }

  /** Returns all infrastructure of this team 
   * @return InfrastructureObject */
  public InfrastructureObject[] getAllInfrastructureObjects() {
    return infrastructureObjects.toArray(new InfrastructureObject[infrastructureObjects.size()]);
  }

  /**
   * Returns costs (maintenance)
   * @return costs 
   */
  public double getCosts() {
    double tempBalance = 0;
    for (int i = 0; i < infrastructureObjects.size(); i++) {
      tempBalance -= infrastructureObjects.elementAt(i).getCosts();
    }
    return tempBalance;
  }

  /**
   * Lowers the condition on every infrastructure object 
   */
  public void lowerConditions() {
    for (int i = 0; i < infrastructureObjects.size(); i++) {
      infrastructureObjects.elementAt(i).lowerCondition();
    }
  }
  
  
  /**
   * Returns the infrastructureObjects.
   * 
   * @return infrastructureObjects
   */
  public Vector getInfrastructureObjects() {
    return infrastructureObjects;
  }

  /**
   * Set infrastructureObjects
   * 
   * @param infrastructureObjects
   */
  public void setInfrastructureObjects(Vector<InfrastructureObject> infrastructureObjects) {
    this.infrastructureObjects = infrastructureObjects;
  }
}
