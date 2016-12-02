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
  
package org.icehockeymanager.ihm.game.player.goalie;

import java.io.*;

import org.icehockeymanager.ihm.game.league.*;
import org.icehockeymanager.ihm.game.league.helper.*;
import org.icehockeymanager.ihm.game.player.*;
import org.icehockeymanager.ihm.lib.*;
import org.icehockeymanager.ihm.game.scenario.*;
import org.w3c.dom.*;

/**
 * Goalie class
 * 
 * @author Bernhard von Gunten
 * @created January, 2002
 */
public class Goalie extends Player implements Serializable {

  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 3760567502882944052L;

  /**
   * Constructs goalie, resets all
   * 
   * @param playerInfo
   *          Infos of player (name etc.)
   * @param key
   *          Key of player
   * @param playerAttributes
   *          PlayerAttributes
   */
  public Goalie(int key, PlayerInfo playerInfo, GoalieAttributes playerAttributes) {
    super(key, playerInfo, playerAttributes);
  }

  /**
   * Goalie constructor by XML Element and countries.
   * 
   * @param element
   *          Element
   * @param countries
   *          Country[]
   * @throws Exception
   */
  public Goalie(Element element, Country[] countries) throws Exception {
    super(element, countries);
    int myKey = ToolsXML.getIntAttribute(element, "KEY");
    PlayerInfo myPlayerInfo = new PlayerInfo(ToolsXML.getFirstSubElement(element, "PLAYERINFO"), countries);
    PlayerAttributes myPlayerAttributes = new GoalieAttributes(ToolsXML.getFirstSubElement(element, "PLAYERATTRIBUTES"));

    lateConstruct(myKey, myPlayerInfo, myPlayerAttributes);
  }

  /**
   * Adds the current goalie to a given XML Element
   * 
   * @param parent
   *          Element
   */
  public void addAsElementToParent(Element parent) {
    Element element = parent.getOwnerDocument().createElement("PLAYER");
    element.setAttribute("CLASS", "GOALIE");
    element.setAttribute("KEY", String.valueOf(this.getKey()));

    // Infos
    getPlayerInfo().addAsElementToParent(element);

    // Attributes
    getPlayerAttributes().addAsElementToParent(element);

    parent.appendChild(element);
  }

  /**
   * Starts a new season to the players stats etc.
   */
  public void newSeason() {
    super.newSeason();
    playerStatsTotal = new GoalieStats(this, null);
  }

  /**
   * Create stats for given league
   * 
   * @param league
   *          League who's owner of this stats.
   */
  public void createStats(League league) {
    // Add stats for a league Element to the player
    playerStatsLeague.add(new GoalieStats(this, league));
  }

  /**
   * Create stats for given leagueElementGroup
   * 
   * @param leagueElementGroup
   *          LeagueElementGroup who's owner of this stats.
   */
  public void createStats(LeagueElementGroup leagueElementGroup) {
    // Add stats for a league Element to the player
    playerStatsGroup.add(new GoalieStats(this, leagueElementGroup));
  }

  /**
   * Create new stats for LeagueElement passed by
   * 
   * @param leagueElement
   *          Description of the Parameter
   */
  public void createStats(LeagueElement leagueElement) {
    playerStats.add(new GoalieStats(this, leagueElement));
  }

  /**
   * Object debug dump
   * 
   * @return String
   */
  public String toString() {
    return "Goalie: " + super.toString();
  }

}
