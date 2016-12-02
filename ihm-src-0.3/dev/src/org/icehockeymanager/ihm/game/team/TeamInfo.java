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
  
package org.icehockeymanager.ihm.game.team;

import java.io.*;

import org.w3c.dom.*;

/**
 * TeamInfo contains general informations (name etc.) of a team.
 * 
 * @author Bernhard von Gunten & Arik Dasen
 * @created December, 2001
 */
public class TeamInfo implements Serializable {

  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 3257844385549922608L;

  /** Team name */
  private String teamName;

  /**
   * Creates teamInfo with the team name passed by.
   * 
   * @param teamName
   *          Team name.
   */
  public TeamInfo(String teamName) {
    this.teamName = teamName;
  }

  /**
   * TeamInfo constructor by XML element
   * 
   * @param element
   */
  public TeamInfo(Element element) {
    this(element.getAttribute("TEAMNAME"));
  }

  /**
   * Adds team info to existing XML Element
   * 
   * @param parent
   */
  public void addAsElementToParent(Element parent) {
    Element element = parent.getOwnerDocument().createElement("TEAMINFO");
    element.setAttribute("TEAMNAME", this.getTeamName());
    parent.appendChild(element);
  }

  /**
   * Returns the team name
   * 
   * @return The teamName value
   */
  public String getTeamName() {
    return teamName;
  }

}
