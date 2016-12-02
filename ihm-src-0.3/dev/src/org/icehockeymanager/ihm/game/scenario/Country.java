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
  
package org.icehockeymanager.ihm.game.scenario;

import org.w3c.dom.*;
import java.io.*;

/**
 * The Country class is a data container with key, name and mastertranslation of
 * a country.
 * 
 * @author Bernhard von Gunten
 * @created September 2004
 */
public class Country implements Serializable {

  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 3906088957724012599L;

  /**
   * Key
   */
  private String key;

  /**
   * Name key
   */
  private String nameKey;

  /**
   * Mastertranslation
   */
  private String masterTranslation;

  /**
   * Country constructor
   * 
   * @param key
   * @param nameKey
   * @param masterTranslation
   */
  public Country(String key, String nameKey, String masterTranslation) {
    this.key = key;
    this.nameKey = nameKey;
    this.masterTranslation = masterTranslation;
  }

  /**
   * Country constructor (by XML Element)
   * 
   * @param element
   */
  public Country(Element element) {
    this.key = element.getAttribute("KEY");
    this.nameKey = element.getAttribute("NAMEKEY");
    this.masterTranslation = element.getAttribute("MASTERTRANSLATION");
  }

  /**
   * Adds this country as element to an existing XML tree.
   * 
   * @param parent
   */
  public void addAsElementToParent(Element parent) {
    Element element = parent.getOwnerDocument().createElement("COUNTRY");
    element.setAttribute("KEY", this.getKey());
    element.setAttribute("NAMEKEY", this.getNameKey());
    element.setAttribute("MASTERTRANSLATION", this.getMasterTranslation());
    parent.appendChild(element);
  }

  /**
   * Returns the name key
   * 
   * @return namekey
   */
  public String getNameKey() {
    return nameKey;
  }

  /**
   * Returns the key
   * 
   * @return key
   */
  public String getKey() {
    return key;
  }

  /**
   * Returns the master translation
   * 
   * @return masterTranslation
   */
  public String getMasterTranslation() {
    return masterTranslation;
  }

}
