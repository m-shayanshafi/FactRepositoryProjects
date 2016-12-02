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
  
package org.icehockeymanager.ihm.game.player.fieldplayer;

import java.io.*;

import org.icehockeymanager.ihm.game.league.*;
import org.icehockeymanager.ihm.game.league.helper.*;
import org.icehockeymanager.ihm.game.player.*;
import org.icehockeymanager.ihm.game.scenario.*;
import org.icehockeymanager.ihm.lib.*;
import org.w3c.dom.*;

/**
 * Fieldplayer class
 * 
 * @author Bernhard von Gunten
 * @created January 17, 2002
 */
public class FieldPlayer extends Player implements Serializable {

  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 4049919359487062072L;

  /**
   * Constructs Fieldplayer, resets all
   * 
   * @param key
   *          Key of player
   * @param playerInfo
   *          Infos of player (name etc.)
   * @param playerAttributes
   *          PlayerAttributes
   */
  public FieldPlayer(int key, PlayerInfo playerInfo, FieldPlayerAttributes playerAttributes) {
    super(key, playerInfo, playerAttributes);
  }

  /**
   * FieldPlayer constructor by given XML Element and countries.
   * 
   * @param element
   *          Element
   * @param countries
   *          Country[]
   * @throws Exception
   */
  public FieldPlayer(Element element, Country[] countries) throws Exception {
    super(element, countries);

    int myKey = ToolsXML.getIntAttribute(element, "KEY");
    PlayerInfo myPlayerInfo = new PlayerInfo(ToolsXML.getFirstSubElement(element, "PLAYERINFO"), countries);
    PlayerAttributes myPlayerAttributes = new FieldPlayerAttributes(ToolsXML.getFirstSubElement(element, "PLAYERATTRIBUTES"));
    lateConstruct(myKey, myPlayerInfo, myPlayerAttributes);

  }

  /** Description of the Method */
  public void newSeason() {
    super.newSeason();
    playerStatsTotal = new FieldPlayerStats(this, null);
  }

  /**
   * Create stats for given league
   * 
   * @param league
   *          League who's owner of this stats.
   */
  public void createStats(League league) {
    // Add stats for a league Element to the player
    playerStatsLeague.add(new FieldPlayerStats(this, league));
  }

  /**
   * Create stats for given leagueElementGroup
   * 
   * @param leagueElementGroup
   *          LeagueElementGroup who's owner of this stats.
   */
  public void createStats(LeagueElementGroup leagueElementGroup) {
    // Add stats for a league Element to the player
    playerStatsGroup.add(new FieldPlayerStats(this, leagueElementGroup));
  }

  /**
   * Create new stats for LeagueElement passed by
   * 
   * @param leagueElement
   *          Description of the Parameter
   */
  public void createStats(LeagueElement leagueElement) {
    playerStats.add(new FieldPlayerStats(this, leagueElement));
  }

  /**
   * Description of the Method
   * 
   * @return Description of the Return Value
   */
  public String toString() {
    return "Fieldplayer: " + super.toString();
  }

  /**
   * Adds this field player to a given XML Elment.
   * 
   * @param parent
   *          Element
   */
  public void addAsElementToParent(Element parent) {
    Element element = parent.getOwnerDocument().createElement("PLAYER");
    element.setAttribute("CLASS", "FIELDPLAYER");
    element.setAttribute("KEY", String.valueOf(this.getKey()));

    // Infos
    getPlayerInfo().addAsElementToParent(element);

    // Attributes
    getPlayerAttributes().addAsElementToParent(element);

    parent.appendChild(element);

  }

}
