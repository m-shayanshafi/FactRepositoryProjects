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
import java.util.*;

import org.icehockeymanager.ihm.game.player.*;
import org.icehockeymanager.ihm.lib.*;
import org.w3c.dom.*;

/**
 * Goalie attributes class containing all goalie specific attributes (like
 * glove).
 * 
 * @author Bernhard von Gunten & Arik Dasen
 * @created December, 2001
 */
public class GoalieAttributes extends PlayerAttributes implements Serializable {

  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 3256719589433030196L;

  /** Glove right */
  public final static String GLOVE = "glove";

  /** Stick left */
  public final static String STICK = "stick";

  /** Puck cover */
  public final static String PUCKCOVER = "puckCover";

  /** Recover */
  public final static String RECOVER = "recover";

  /** Five hole */
  public final static String FIVEHOLE = "fiveHole";

  /** Poke check */
  public final static String POKECHECK = "pokeCheck";

  /** Players attributes */
  private TreeMap<String, PlayerAttribute> playerAttributes = null;

  private static Random r = new Random();

  /** Returns the keys of trainable units */
  private static String[] possibleTrainingUnits = null;

  /**
   * Construct a empty attributes sheet for a player
   * 
   * @param position
   *          Position of the player
   * @param weight 
   * @param size 
   * @param handeness 
   */
  public GoalieAttributes(int position, int weight, int size, int handeness) {
    super(position, weight, size, handeness);
    playerAttributes = new TreeMap<String, PlayerAttribute>();
    createPlayerAttributes();
  }

  /**
   * GoalieAttributes constructor by given XML Element
   * 
   * @param element
   *          Element
   */
  public GoalieAttributes(Element element) {
    this(ToolsXML.getIntAttribute(element, "POSITION"), ToolsXML.getIntAttribute(element, "WEIGHT"), ToolsXML.getIntAttribute(element, "SIZE"), ToolsXML.getIntAttribute(element, "HANDENESS"));
    fillUpByElement(element);
  }

  /**
   * Returns an array of Strings with the keys of all goalie attributes.
   * 
   * @return String[]
   */
  public static synchronized String[] getAttributeKeys() {
    if (possibleTrainingUnits == null) {
      possibleTrainingUnits = new String[6];
      possibleTrainingUnits[0] = GLOVE;
      possibleTrainingUnits[1] = STICK;
      possibleTrainingUnits[2] = PUCKCOVER;
      possibleTrainingUnits[3] = RECOVER;
      possibleTrainingUnits[4] = FIVEHOLE;
      possibleTrainingUnits[5] = POKECHECK;
    }
    return possibleTrainingUnits;
  }

  /** Creates all player attributes for player */
  private void createPlayerAttributes() {
    playerAttributes.put(GLOVE, new PlayerAttribute(GLOVE, 0));
    playerAttributes.put(STICK, new PlayerAttribute(STICK, 0));
    playerAttributes.put(PUCKCOVER, new PlayerAttribute(PUCKCOVER, 0));
    playerAttributes.put(RECOVER, new PlayerAttribute(RECOVER, 0));
    playerAttributes.put(FIVEHOLE, new PlayerAttribute(FIVEHOLE, 0));
    playerAttributes.put(POKECHECK, new PlayerAttribute(POKECHECK, 0));
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
    super._setRandomDefaultAttributes(position, overall);
    setRandomDefaultAttributes(overall);
  }

  /**
   * Gets the playerAttribute attribute of the GoalieAttributes object
   * 
   * @param key
   *          Description of the Parameter
   * @return The playerAttribute value
   */
  public PlayerAttribute getSpecificPlayerAttribute(String key) {
    return playerAttributes.get(key);
  }

  /**
   * Sets a specific PlayerAttribute.
   * 
   * @param attr
   *          PlayerAttribute
   */
  public void setSpecificPlayerAttribute(PlayerAttribute attr) {
    playerAttributes.put(attr.getKey(), attr);
  }

  /**
   * Sets the random defaultAttributes attribute of the GoalieAttributes object.
   * (passed overall + max 20)
   * 
   * @param overall
   *          Overall value
   */
  public void setRandomDefaultAttributes(int overall) {
    super.setRandomDefaultAttributes(overall);
    PlayerAttribute[] pa = getSpecificPlayerAttributes();
    for (int i = 0; i < pa.length; i++) {
      pa[i].setValue(overall + Math.abs(r.nextInt(20)));
    }
  }

  /**
   * Gets the Average of all position (goalie or field player) specific
   * attributes
   * 
   * @return The specificAttributesAverage value
   */
  public int getSpecificPlayerAttributesAverage() {
    int count = 0;
    double total = 0;
    PlayerAttribute[] pa = getSpecificPlayerAttributes();
    for (int i = 0; i < pa.length; i++) {
      total += pa[i].getValue();
      count++;
    }
    return (int) total / count;
  }

  /**
   * Returns average attributes for player (common, specific,form, energy,
   * motivation)
   * 
   * @return The Average value
   */
  public int getOverallAverage() {
    double common = getCommonPlayerAttributesAverage();
    double player = getSpecificPlayerAttributesAverage();
    double totalPlayerAttributes = (common + player) / 2;

    double form = getVIAPlayerAttribute(PlayerAttributes.VIA_FORM_KEY).getValue();
    double energy = getVIAPlayerAttribute(PlayerAttributes.VIA_ENERGY_KEY).getValue();
    double motivation = getVIAPlayerAttribute(PlayerAttributes.VIA_MORALE_KEY).getValue();

    return (int) (totalPlayerAttributes + form + energy + motivation) / 4;
  }

  /**
   * Returns the total average (common, specific attributes) of the player
   * attributes
   * 
   * @return The totalAttributesAverage value
   */
  public int getTotalAttributesAverage() {
    double common = getCommonPlayerAttributesAverage();
    double player = getSpecificPlayerAttributesAverage();
    return (int) (common + player) / 2;
  }

  /**
   * Gets the playerAttributes attribute of the GoalieAttributes object
   * 
   * @return The playerAttributes value
   */
  public PlayerAttribute[] getSpecificPlayerAttributes() {
    return playerAttributes.values().toArray(new PlayerAttribute[playerAttributes.size()]);
  }

}
