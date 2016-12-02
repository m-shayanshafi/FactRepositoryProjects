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

import org.icehockeymanager.ihm.lib.*;

/**
 * Single player attribute
 * 
 * @author Bernhard von Gunten
 * @created January, 2002
 */
public class PlayerAttribute extends IhmCustomComparator implements Serializable {

  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 4049916082443924533L;

  private String key = null;

  private double value = 0;

  /**
   * Constructs attribute
   * 
   * @param key
   *          Key of player
   * @param value
   *          Description of the Parameter
   */
  public PlayerAttribute(String key, double value) {
    this.key = key;
    this.value = value;
  }

  /**
   * Decreases the attribute points
   * 
   * @param dec
   *          Points to decrease
   */
  public void decrease(double dec) {
    value -= dec;
    if (value < 1) {
      value = 1;
    }
    if (value > 99) {
      value = 99;
    }
  }

  /**
   * Increases the attribute points
   * 
   * @param inc
   *          Points to add
   */
  public void increase(double inc) {
    value += inc;
    if (value > 99) {
      value = 99;
    }
    if (value < 1) {
      value = 1;
    }
  }

  /**
   * Sets the value attribute of the PlayerAttribute object
   * 
   * @param value
   *          The new value value
   */
  public void setValue(double value) {
    this.value = value;
    if (value < 1) {
      value = 1;
    }
  }

  /**
   * Gets the value attribute of the PlayerAttribute object
   * 
   * @return The value value
   */
  public double getValue() {
    return value;
  }

  /**
   * Gets the value attribute of the PlayerAttribute object
   * 
   * @return The intValue value
   */
  public int getIntValue() {
    return (int) value;
  }

  /**
   * Gets the key attribute of the PlayerAttribute object
   * 
   * @return The key value
   */
  public String getKey() {
    return key;
  }

  /**
   * Returns sort value considering sortCriteria
   * 
   * @return The sortValue value
   */
  protected double getSortValue() {
    return getIntValue();
  }

}
