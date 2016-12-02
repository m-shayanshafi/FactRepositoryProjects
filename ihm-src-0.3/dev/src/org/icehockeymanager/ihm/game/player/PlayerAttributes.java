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
  
package org.icehockeymanager.ihm.game.player;

import java.io.*;
import java.util.*;

import org.icehockeymanager.ihm.game.*;
import org.icehockeymanager.ihm.lib.*;
import org.w3c.dom.*;

/**
 * Player attributes base class with player common attributes and very important
 * attributes.
 * <p>
 * <p>- Common attributes are not trainable (Talent etc.). But may change from
 * time to time like experience.
 * <p>- Very Important Attributes (VIA) change often (like morale)
 * 
 * @author Bernhard von Gunten & Arik Dasen
 * @created December, 2001
 */
public abstract class PlayerAttributes implements Serializable {

  /** Position Goalie */
  public final static int POSITION_GOALTENDING = 0;

  /** Position Defense */
  public final static int POSITION_DEFENSE = 1;

  /** Position Wing */
  public final static int POSITION_WING = 2;

  /** Position Center */
  public final static int POSITION_CENTER = 3;

  /** Handeness Right */
  public final static int HANDENESS_RIGHT = 0;

  /** Handedness Left */
  public final static int HANDENESS_LEFT = 1;

  /** Common Talent */
  public final static String COMMON_TALENT_KEY = "talent";

  /** Common Offense */
  public final static String COMMON_OFFENSE_KEY = "offense";

  /** Common Defense */
  public final static String COMMON_DEFENSE_KEY = "defense";

  /** Common Leadership */
  public final static String COMMON_LEADERSHIP_KEY = "leadership";

  /** Common Experience */
  public final static String COMMON_EXPERIENCE_KEY = "experience";

  /** Common Durability */
  public final static String COMMON_DURABILITY_KEY = "durability";

  /** VIA Energy */
  public final static String VIA_ENERGY_KEY = "energy";

  /** VIA Morale */
  public final static String VIA_MORALE_KEY = "morale";

  /** VIA Form */
  public final static String VIA_FORM_KEY = "form";

  private static Random r = new Random();

  /** Common */
  private Hashtable<String, PlayerAttribute> common = null;

  /** VIA (Very Important Attributes) */
  private Hashtable<String, PlayerAttribute> via = null;

  /** Owner of this attributes */
  private Player player = null;

  /** Position of this player */
  private int position;

  /** Size of this player in cm */
  private int size;

  /** Weight of this player in kg */
  private int weight;

  /** Handeness */
  private int handeness;

  /** Engergy in game (only used by Match routine) */
  private int matchEnergy;

  /**
   * Construct a empty attributes sheet for a player
   * 
   * @param position
   *          Description of the Parameter
   * @param weight 
   * @param size 
   * @param handeness 
   */
  public PlayerAttributes(int position, int weight, int size, int handeness) {
    this.position = position;
    this.weight = weight;
    this.size = size;
    this.handeness = handeness;
    common = new Hashtable<String, PlayerAttribute>();
    via = new Hashtable<String, PlayerAttribute>();
    createCommon();
    createVIA();
  }

  public PlayerAttributes(Element element) {
    // implement by fieldpoayer & goalie
  }

  /**
   * Fills up the players attributes by a given XML Element.
   * 
   * @param element
   *          Element
   */
  public void fillUpByElement(Element element) {

    Element viaElement = ToolsXML.getFirstSubElement(element, "VIA");
    NamedNodeMap viaList = viaElement.getAttributes();
    int viaListCount = viaList.getLength();
    for (int i = 0; i < viaListCount; i++) {
      Node node = viaList.item(i);
      String key = node.getNodeName();
      double value = Double.valueOf(node.getNodeValue()).doubleValue();
      PlayerAttribute attr = new PlayerAttribute(key, value);
      setVIAPlayerAttribute(attr);
    }

    Element commonElement = ToolsXML.getFirstSubElement(element, "COMMON");
    NamedNodeMap commonList = commonElement.getAttributes();
    int commonListCount = commonList.getLength();
    for (int i = 0; i < commonListCount; i++) {
      Node node = commonList.item(i);
      String key = node.getNodeName();
      double value = Double.valueOf(node.getNodeValue()).doubleValue();
      PlayerAttribute attr = new PlayerAttribute(key, value);
      setCommonPlayerAttribute(attr);
    }

    Element specificElement = ToolsXML.getFirstSubElement(element, "SPECIFIC");
    NamedNodeMap specificList = specificElement.getAttributes();
    int specificListCount = specificList.getLength();
    for (int i = 0; i < specificListCount; i++) {
      Node node = specificList.item(i);
      String key = node.getNodeName();
      double value = Double.valueOf(node.getNodeValue()).doubleValue();
      PlayerAttribute attr = new PlayerAttribute(key, value);
      setSpecificPlayerAttribute(attr);
    }
  }

  public void addAsElementToParent(Element parent) {
    Element element = parent.getOwnerDocument().createElement("PLAYERATTRIBUTES");
    element.setAttribute("POSITION", String.valueOf(this.getPosition()));
    element.setAttribute("WEIGHT", String.valueOf(this.getWeight()));
    element.setAttribute("SIZE", String.valueOf(this.getSize()));
    element.setAttribute("HANDENESS", String.valueOf(this.getHandeness()));

    // VIA ATTRIBUTES
    Element viaElement = element.getOwnerDocument().createElement("VIA");
    for (Enumeration<PlayerAttribute> e = via.elements(); e.hasMoreElements();) {
      PlayerAttribute attr = e.nextElement();
      viaElement.setAttribute(attr.getKey(), String.valueOf(attr.getValue()));
    }
    element.appendChild(viaElement);

    // COMMON ATTRIBUTES
    Element commonElement = element.getOwnerDocument().createElement("COMMON");
    for (Enumeration<PlayerAttribute> e = common.elements(); e.hasMoreElements();) {
      PlayerAttribute attr = e.nextElement();
      commonElement.setAttribute(attr.getKey(), String.valueOf(attr.getValue()));
    }
    element.appendChild(commonElement);

    Element specificElement = element.getOwnerDocument().createElement("SPECIFIC");
    // SPECIFIC ATTRIBUTES
    PlayerAttribute[] specAttr = getSpecificPlayerAttributes();
    for (int i = 0; i < specAttr.length; i++) {
      specificElement.setAttribute(specAttr[i].getKey(), String.valueOf(specAttr[i].getValue()));
    }
    element.appendChild(specificElement);

    parent.appendChild(element);

  }

  /** Create the Common attributes for player */
  private void createCommon() {
    common.put(COMMON_TALENT_KEY, new PlayerAttribute(COMMON_TALENT_KEY, 0));
    common.put(COMMON_OFFENSE_KEY, new PlayerAttribute(COMMON_OFFENSE_KEY, 0));
    common.put(COMMON_DEFENSE_KEY, new PlayerAttribute(COMMON_DEFENSE_KEY, 0));
    common.put(COMMON_LEADERSHIP_KEY, new PlayerAttribute(COMMON_LEADERSHIP_KEY, 0));
    common.put(COMMON_DURABILITY_KEY, new PlayerAttribute(COMMON_DURABILITY_KEY, 0));
    common.put(COMMON_EXPERIENCE_KEY, new PlayerAttribute(COMMON_EXPERIENCE_KEY, 0));
  }

  /** Create the VIA attributes for player */
  private void createVIA() {
    via.put(VIA_FORM_KEY, new PlayerAttribute(VIA_FORM_KEY, 0));
    via.put(VIA_ENERGY_KEY, new PlayerAttribute(VIA_ENERGY_KEY, 0));
    via.put(VIA_MORALE_KEY, new PlayerAttribute(VIA_MORALE_KEY, 0));
  }

  /**
   * Sets the player attribute of the PlayerAttributes object
   * 
   * @param player
   *          The new player value
   */
  public void setPlayer(Player player) {
    this.player = player;
  }

  /**
   * Gets the player attribute of the PlayerAttributes object
   * 
   * @return The player value
   */
  public Player getPlayer() {
    return player;
  }

  /**
   * Gets the position attribute of the PlayerAttributes object
   * 
   * @return The position value
   */
  public int getPosition() {
    return position;
  }

  /**
   * Temporary method for tests (position)
   * 
   * @param position
   *          Description of the Parameter
   * @param overall
   *          Description of the Parameter
   */
  public void _setRandomDefaultAttributes(int position, int overall) {
    this.position = position;
    setRandomDefaultAttributes(overall);
  }

  /**
   * Gets the CommonPlayerAttribute attribute of the PlayerAttributes object
   * 
   * @param key
   *          Description of the Parameter
   * @return The CommonPlayerAttribute value
   */
  public PlayerAttribute getCommonPlayerAttribute(String key) {
    return common.get(key);
  }

  public void setCommonPlayerAttribute(PlayerAttribute attr) {
    common.put(attr.getKey(), attr);
  }

  /**
   * Gets the vIAPlayerAttribute attribute of the PlayerAttributes object
   * 
   * @param key
   *          Description of the Parameter
   * @return The vIAPlayerAttribute value
   */
  public PlayerAttribute getVIAPlayerAttribute(String key) {
    return via.get(key);
  }

  public void setVIAPlayerAttribute(PlayerAttribute attr) {
    via.put(attr.getKey(), attr);
  }

  /**
   * Gets the specificPlayerAttribute attribute of the PlayerAttributes object
   * 
   * @param key
   *          Description of the Parameter
   * @return The playerAttribute value
   */
  public abstract PlayerAttribute getSpecificPlayerAttribute(String key);

  /**
   * Returns the specific player attributes
   * 
   * @return PlayerAttribute[]
   */
  public abstract PlayerAttribute[] getSpecificPlayerAttributes();

  /**
   * Sets a specific player attribute.
   * 
   * @param attr
   *          PlayerAttribute
   */
  public abstract void setSpecificPlayerAttribute(PlayerAttribute attr);

  /**
   * Sets the random defaultAttributes attribute of the PlayerAttributes object.
   * (passed overall + max 20)
   * 
   * @param overall
   *          Overall value
   */
  public void setRandomDefaultAttributes(int overall) {
    for (Enumeration<PlayerAttribute> e = common.elements(); e.hasMoreElements();) {
      e.nextElement().setValue(overall + Math.abs(r.nextInt(20)));
    }
    for (Enumeration<PlayerAttribute> e = via.elements(); e.hasMoreElements();) {
      e.nextElement().setValue(overall + Math.abs(r.nextInt(20)));
    }
  }

  /**
   * Gets the Average of all position (goalie or field player) specific
   * attributes
   * 
   * @return The specificAttributesAverage value
   */
  public abstract int getSpecificPlayerAttributesAverage();

  /**
   * Gets the totalAttributesAverage attribute of the PlayerAttributes object
   * 
   * @return The totalAttributesAverage value
   */
  public abstract int getTotalAttributesAverage();

  /**
   * Gets the CommonPlayerAttributesAverage attribute of the PlayerAttributes
   * object
   * 
   * @return The CommonPlayerAttributesAverage value
   */
  public int getCommonPlayerAttributesAverage() {
    int count = 0;
    double total = 0;
    for (Enumeration<PlayerAttribute> e = common.elements(); e.hasMoreElements();) {
      total += e.nextElement().getValue();
      count++;
    }

    return (int) total / count;
  }

  /**
   * Gets the overall attribute of the PlayerAttributes object
   * 
   * @return The overall value
   */
  public abstract int getOverallAverage();

  /**
   * Returns the common player attributes.
   * 
   * @return Hashtable
   */
  public Hashtable<String, PlayerAttribute> getCommonAttributes() {
    return common;
  }

  /**
   * Description of the Method
   * 
   * @return Description of the Return Value
   */
  public String toString() {
    return "PlayerAttributes";
  }

  /**
   * Returns a random players position. 100 calls should (more or less) return
   * 10 Goalies, 35 Defense, 35 Wings, 20 Centers
   * 
   * @param r
   *          Random
   * @return int
   */
  public static int getRandomPosition(Random r) {
    int rand = r.nextInt(100);
    if (rand < 10) {
      return POSITION_GOALTENDING;
    } else if (rand < 45) {
      return POSITION_DEFENSE;
    } else if (rand < 80) {
      return POSITION_WING;
    } else {
      return POSITION_CENTER;
    }
  }

  /**
   * Returns the size of a player
   * 
   * @return size
   */
  public int getSize() {
    return size;
  }

  /**
   * Returns the weight of a player
   * 
   * @return Weight
   */
  public int getWeight() {
    return weight;
  }

  /**
   * Returns the handeness of a player
   * 
   * @return Handeness
   */
  public int getHandeness() {
    return handeness;
  }

  /** Resets the Match energy of a player */
  public void resetMatchEnergy() {
    this.setMatchEngergy(GameController.getInstance().getScenario().getScenarioSettings().MATCH_PLAYER_ENERGY_MAX);
  }

  /** Returns the current Match energy 
   * @return MatchEnergy */
  public int getMatchEngergy() {
    return this.matchEnergy;
  }

  /** Sets the current Match energy 
   * @param matchEnergy */
  public void setMatchEngergy(int matchEnergy) {
    this.matchEnergy = matchEnergy;
  }

  /** In-/Decrease Match energy by int 
   * @param inc */
  public void incMatchEngergy(int inc) {
    this.matchEnergy += inc;
  }

}