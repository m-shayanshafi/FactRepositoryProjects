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

import java.io.*;
import java.util.*;

import org.icehockeymanager.ihm.game.league.*;
import org.icehockeymanager.ihm.game.league.std.*;
import org.icehockeymanager.ihm.game.player.*;
import org.icehockeymanager.ihm.game.player.fieldplayer.*;
import org.icehockeymanager.ihm.game.player.goalie.*;
import org.icehockeymanager.ihm.game.team.*;
import org.icehockeymanager.ihm.lib.*;
import org.w3c.dom.*;

/**
 * ScenarioTools contains some static functions to read/write a scenario from/to
 * an XML File.
 * 
 * @author Bernhard von Gunten
 * @created September, 2004
 */
public class ScenarioTools {

  /**
   * Value key
   */
  public static final String VALUE_KEY = "VALUE";

  /**
   * Writes a scenario to XML file.
   * 
   * @param scenario
   * @param file
   * @throws Exception
   */
  public static void writeScenarioToXMLDatabase(Scenario scenario, File file) throws Exception {
    Document document = ToolsXML.createXmlDocument();
    Element root = document.createElement("SCENARIO");

    // Countries
    Element countriesElement = root.getOwnerDocument().createElement("COUNTRIES");
    Country[] countries = scenario.getCountries();
    for (int i = 0; i < countries.length; i++) {
      countries[i].addAsElementToParent(countriesElement);
    }
    root.appendChild(countriesElement);

    // Last names
    Element lastNameElement = root.getOwnerDocument().createElement("LASTNAMES");
    String[] lastNames = scenario.getLastNames();
    for (int i = 0; i < lastNames.length; i++) {
      Element element = lastNameElement.getOwnerDocument().createElement("LASTNAME");
      element.setAttribute(VALUE_KEY, lastNames[i]);
      lastNameElement.appendChild(element);
    }
    root.appendChild(lastNameElement);

    // Last names
    Element firstNameElement = root.getOwnerDocument().createElement("FIRSTNAMES");
    String[] firstNames = scenario.getFirstNames();
    for (int i = 0; i < firstNames.length; i++) {
      Element element = firstNameElement.getOwnerDocument().createElement("FIRSTNAME");
      element.setAttribute(VALUE_KEY, firstNames[i]);
      firstNameElement.appendChild(element);
    }
    root.appendChild(firstNameElement);

    // Players
    Element playersElement = root.getOwnerDocument().createElement("PLAYERS");
    Player[] players = scenario.getPlayers();
    for (int i = 0; i < players.length; i++) {
      players[i].addAsElementToParent(playersElement);
    }
    root.appendChild(playersElement);

    // Teams
    Element teamsElement = root.getOwnerDocument().createElement("TEAMS");
    Team[] teams = scenario.getTeams();
    for (int i = 0; i < teams.length; i++) {
      teams[i].addAsElementToParent(teamsElement);
    }
    root.appendChild(teamsElement);

    // League Owners
    Element loElement = root.getOwnerDocument().createElement("LEAGUEOWNERS");
    LeagueOwner[] los = scenario.getLeagueOwners();
    for (int i = 0; i < los.length; i++) {
      los[i].addAsElementToParent(loElement);
    }
    root.appendChild(loElement);

    document.appendChild(root);
    ToolsXML.saveXmlFile(file, document);
  }

  /**
   * Reads the countries from XML document
   * 
   * @param doc
   * @return Array of countries
   * @throws Exception
   */
  public static Country[] readCountriesFromXMLDatabase(Document doc) throws Exception {

    Vector<Country> countries = new Vector<Country>();

    Element root = doc.getDocumentElement();
    Element countriesElement = ToolsXML.getFirstSubElement(root, "COUNTRIES");

    NodeList countryList = countriesElement.getElementsByTagName("COUNTRY");
    int count = countryList.getLength();
    for (int i = 0; i < count; i++) {
      Element countryElement = (Element) countryList.item(i);
      countries.add(new Country(countryElement));
    }

    return countries.toArray(new Country[countries.size()]);
  }

  /**
   * Reads Strings from a XML document by section and element descriptions.
   * 
   * @param doc
   * @param section
   * @param element
   * @return Array of String
   * @throws Exception
   */
  private static String[] readStringsFromXMLDatabase(Document doc, String section, String element) throws Exception {
    Vector<String> result = new Vector<String>();

    Element root = doc.getDocumentElement();
    Element countriesElement = ToolsXML.getFirstSubElement(root, section);

    NodeList resultList = countriesElement.getElementsByTagName(element);
    int count = resultList.getLength();
    for (int i = 0; i < count; i++) {
      Element value = (Element) resultList.item(i);
      result.add(value.getAttribute(VALUE_KEY));
    }

    return result.toArray(new String[result.size()]);
  }

  /**
   * Reads first names from XML document
   * 
   * @param doc
   * @return String Array of first names
   * @throws Exception
   */
  public static String[] readFirstNamesFromXMLDatabase(Document doc) throws Exception {
    return readStringsFromXMLDatabase(doc, "FIRSTNAMES", "FIRSTNAME");
  }

  /**
   * Reads last names from XML document
   * 
   * @param doc
   * @return String Array of last names
   * @throws Exception
   */
  public static String[] readLasttNamesFromXMLDatabase(Document doc) throws Exception {
    return readStringsFromXMLDatabase(doc, "LASTNAMES", "LASTNAME");
  }

  /**
   * Reads players from XML document
   * 
   * @param doc
   * @param countries
   * @return Array of players
   * @throws Exception
   */
  public static Player[] readPlayersFromXMLDatabase(Document doc, Country[] countries) throws Exception {

    Vector<Player> players = new Vector<Player>();

    Element root = doc.getDocumentElement();
    Element playersElement = ToolsXML.getFirstSubElement(root, "PLAYERS");
    NodeList playersList = playersElement.getElementsByTagName("PLAYER");
    int count = playersList.getLength();
    for (int i = 0; i < count; i++) {
      Element playerElement = (Element) playersList.item(i);
      String CLASS = playerElement.getAttribute("CLASS");
      if (CLASS.equalsIgnoreCase("GOALIE")) {
        players.add(new Goalie(playerElement, countries));
      } else {
        players.add(new FieldPlayer(playerElement, countries));
      }

    }
    return players.toArray(new Player[players.size()]);
  }

  /**
   * Reads teams from XML datasource.
   * 
   * @param doc
   * @param players
   * @return Array of teams
   * @throws Exception
   */
  public static Team[] readTeamsFromXMLDatabase(Document doc, Player[] players) throws Exception {
    Vector<Team> teams = new Vector<Team>();

    Element root = doc.getDocumentElement();
    Element teamsElement = ToolsXML.getFirstSubElement(root, "TEAMS");

    NodeList teamsList = teamsElement.getElementsByTagName("TEAM");
    int count = teamsList.getLength();
    for (int i = 0; i < count; i++) {
      Element teamElement = (Element) teamsList.item(i);
      Team team = new Team(teamElement, players);
      // Link Players to team
      teams.add(team);
    }

    return teams.toArray(new Team[teams.size()]);
  }

  /**
   * Reads LeagueOwners from XML docment
   * 
   * @param doc
   * @param teams
   * @return Array of league owners
   * @throws Exception
   */
  public static LeagueOwner[] readLeagueOwnersFromXMLDatabase(Document doc, Team[] teams) throws Exception {

    Vector<LeagueOwner> leagueOwners = new Vector<LeagueOwner>();

    Element root = doc.getDocumentElement();
    Element playersElement = ToolsXML.getFirstSubElement(root, "LEAGUEOWNERS");

    NodeList playersList = playersElement.getElementsByTagName("LEAGUEOWNER");
    int count = playersList.getLength();
    for (int i = 0; i < count; i++) {
      Element playerElement = (Element) playersList.item(i);
      String CLASS = playerElement.getAttribute("CLASS");
      if (CLASS.equalsIgnoreCase("STDLEAGUEOWNER")) {
        leagueOwners.add(StdLeagueOwner.createLeagueOwner(playerElement, teams));
      }
    }

    return leagueOwners.toArray(new LeagueOwner[leagueOwners.size()]);
  }


}