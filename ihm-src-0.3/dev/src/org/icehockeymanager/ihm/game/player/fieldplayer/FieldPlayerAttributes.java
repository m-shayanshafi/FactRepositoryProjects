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
import java.util.*;

import org.icehockeymanager.ihm.game.player.*;
import org.icehockeymanager.ihm.lib.*;
import org.w3c.dom.*;

/**
 * Field player attributes class
 * 
 * @author Bernhard von Gunten & Arik Dasen
 * @created December, 2001
 */
public class FieldPlayerAttributes extends PlayerAttributes implements Serializable {

  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 3256718472657645619L;

  /** Skating */
  public final static String SKATING = "skating";

  /** Shoooting */
  public final static String SHOOTING = "shooting";

  /** Stick handling */
  public final static String STICKHANDLING = "stickHandling";

  /** Passing */
  public final static String PASSING = "passing";

  /** Endurance */
  public final static String ENDURANCE = "endurance";

  /** Positioning */
  public final static String POSITIONING = "positioning";

  /** Checking */
  public final static String CHECKING = "checking";

  /** Intensity */
  public final static String INTENSITY = "intensity";

  /**
   * The players attributes.
   */
  private TreeMap<String, PlayerAttribute> playerAttributes = null;

  private static Random r = new Random();

  /*
   * Trainable Attributes
   */
  private static String[] possibleTrainingUnits = null;

  /**
   * Construct a empty attributes sheet for a player
   * 
   * @param position
   *          Description of the Parameter
   * @param weight
   * @param size
   * @param handeness
   */
  public FieldPlayerAttributes(int position, int weight, int size, int handeness) {
    super(position, weight, size, handeness);
    playerAttributes = new TreeMap<String, PlayerAttribute>();
    createPlayerAttributes();
  }

  /**
   * FieldPlayerAttributes constructor by given XML Element
   * 
   * @param element
   *          Element
   */
  public FieldPlayerAttributes(Element element) {
    this(ToolsXML.getIntAttribute(element, "POSITION"), ToolsXML.getIntAttribute(element, "WEIGHT"), ToolsXML.getIntAttribute(element, "SIZE"), ToolsXML.getIntAttribute(element, "HANDENESS"));
    fillUpByElement(element);
  }

  /**
   * Returns array of strings with the keys of all attributes.
   * 
   * @return String[]
   */
  public static synchronized String[] getAttributeKeys() {
    if (possibleTrainingUnits == null) {
      possibleTrainingUnits = new String[8];
      possibleTrainingUnits[0] = SKATING;
      possibleTrainingUnits[1] = SHOOTING;
      possibleTrainingUnits[2] = STICKHANDLING;
      possibleTrainingUnits[3] = PASSING;
      possibleTrainingUnits[4] = ENDURANCE;
      possibleTrainingUnits[5] = POSITIONING;
      possibleTrainingUnits[6] = CHECKING;
      possibleTrainingUnits[7] = INTENSITY;
    }
    return possibleTrainingUnits;
  }

  /** Creates all attributes for player */
  private void createPlayerAttributes() {
    playerAttributes.put(SKATING, new PlayerAttribute(SKATING, 0));
    playerAttributes.put(SHOOTING, new PlayerAttribute(SHOOTING, 0));
    playerAttributes.put(STICKHANDLING, new PlayerAttribute(STICKHANDLING, 0));
    playerAttributes.put(PASSING, new PlayerAttribute(PASSING, 0));
    playerAttributes.put(ENDURANCE, new PlayerAttribute(ENDURANCE, 0));
    playerAttributes.put(POSITIONING, new PlayerAttribute(POSITIONING, 0));
    playerAttributes.put(CHECKING, new PlayerAttribute(CHECKING, 0));
    playerAttributes.put(INTENSITY, new PlayerAttribute(INTENSITY, 0));
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
   * Gets the playerAttribute attribute of the FieldPlayerAttributes object
   * 
   * @param key
   *          Description of the Parameter
   * @return The playerAttribute value
   */
  public PlayerAttribute getSpecificPlayerAttribute(String key) {
    return playerAttributes.get(key);
  }

  /**
   * Sets a specific attribute.
   * 
   * @param attr
   *          PlayerAttribute
   */
  public void setSpecificPlayerAttribute(PlayerAttribute attr) {
    playerAttributes.put(attr.getKey(), attr);
  }

  /**
   * Sets the random defaultAttributes attribute of the FieldPlayerAttributes
   * object. (passed overall + max 20)
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
   * Returns the total average (common / specific attributes) of the player
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
   * Gets the playerAttributes attribute of the FieldPlayerAttributes object
   * 
   * @return The playerAttributes value
   */
  public PlayerAttribute[] getSpecificPlayerAttributes() {
    return playerAttributes.values().toArray(new PlayerAttribute[playerAttributes.size()]);
  }

}