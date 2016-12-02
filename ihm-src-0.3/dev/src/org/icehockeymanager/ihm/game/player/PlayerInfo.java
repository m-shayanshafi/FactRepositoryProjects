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
import org.icehockeymanager.ihm.game.scenario.*;
import org.w3c.dom.*;
import java.util.*;

/**
 * Player infos
 * 
 * @author Bernhard von Gunten & Arik Dasen
 * @created December, 2001
 */
public class PlayerInfo implements Serializable {

  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 3258412811536380720L;

  /** First name */
  private String firstName = "";

  /** Last name */
  private String lastName = "";

  /** Date of birth */
  private Calendar birthdate = null;

  /** Country */
  private Country country;
  
  /**
   * Constructs Player infos
   * 
   * @param firstName
   *          First name of player
   * @param lastName
   *          Last name of player
   * @param birthdate
   *          Date of birth
   * @param country
   *          Country
   */
  public PlayerInfo(String firstName, String lastName, Calendar birthdate, Country country) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.birthdate = birthdate;
    this.country = country;
  }

  /**
   * PlayerInfo contructor by XML Element & countries.
   * 
   * @param element
   *          Element
   * @param countries
   *          Country[]
   * @throws Exception
   */
  public PlayerInfo(Element element, Country[] countries) throws Exception {
    this.firstName = element.getAttribute("FIRSTNAME");
    this.lastName = element.getAttribute("LASTNAME");
    this.birthdate = ToolsXML.getSimpleCalendarAttribute(element, "BIRTHDATE");
    String countryKey = element.getAttribute("COUNTRYKEY");
    for (int i = 0; i < countries.length; i++) {
      if (countries[i].getKey().equals(countryKey)) {
        this.country = countries[i];
        break;
      }
    }

  }

  /**
   * Adds the player infos to a given XML Element
   * 
   * @param parent
   *          Element
   */
  public void addAsElementToParent(Element parent) {
    Element element = parent.getOwnerDocument().createElement("PLAYERINFO");
    element.setAttribute("BIRTHDATE", ToolsXML.convertSimpleCalendar(this.getBirthdate()));
    element.setAttribute("FIRSTNAME", this.getFirstName());
    element.setAttribute("LASTNAME", this.getLastName());
    element.setAttribute("COUNTRYKEY", this.getCountry().getKey());
    parent.appendChild(element);
  }

  /**
   * Returns the country
   * 
   * @return Country
   */
  public Country getCountry() {
    return country;
  }

  /**
   * Gets the firstName attribute of the PlayerInfo object
   * 
   * @return The firstName value
   */
  public String getFirstName() {
    return firstName;
  }

  /**
   * Gets the lastName attribute of the PlayerInfo object
   * 
   * @return The lastName value
   */
  public String getLastName() {
    return lastName;
  }

  /**
   * Gets the age attribute of the PlayerInfo object
   * 
   * @return The age value
   */
  public Calendar getBirthdate() {
    return birthdate;
  }

  /**
   * Returns the current Age of a player.
   * 
   * @return int
   */
  public int getAge() {
    int thisYear = Calendar.getInstance().get(Calendar.YEAR);
    int birthDate = getBirthdate().get(Calendar.YEAR);
    return thisYear - birthDate;

  }

  /**
   * Returns name of player (last, first name)
   * 
   * @return The playerName (last, first name)
   */
  public String getPlayerName() {
    return lastName + ", " + firstName;
  }

}
