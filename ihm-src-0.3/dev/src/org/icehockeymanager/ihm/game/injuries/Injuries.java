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
  
package org.icehockeymanager.ihm.game.injuries;

import java.io.*;
import java.util.*;

import org.icehockeymanager.ihm.game.*;
import org.icehockeymanager.ihm.game.injuries.log.*;
import org.icehockeymanager.ihm.game.player.*;
import org.icehockeymanager.ihm.game.team.*;
import org.icehockeymanager.ihm.lib.*;
import org.w3c.dom.*;

/**
 * Injuries is a helper class to generate or heal injuries to players.
 * 
 * @author Bernhard von Gunten
 * @created October, 2004
 */
public class Injuries {

  /**
   * Generate injuries.
   */
  public static void addInjuries() {
    Team[] teams = GameController.getInstance().getScenario().getTeams();
    for (int i = 0; i < teams.length; i++) {
      int rand = GameController.getInstance().getScenario().getRandomInt(364);
      if (rand <= GameController.getInstance().getScenario().getScenarioSettings().INJURIES_RARE_PER_YEAR) {
        addInjuryToTeam(teams[i], InjuryData.PROBABILITY_RARE);
      } else if (rand <= GameController.getInstance().getScenario().getScenarioSettings().INJURIES_AVERAGE_PER_YEAR) {
        addInjuryToTeam(teams[i], InjuryData.PROBABILITY_AVERAGE);
      } else if (rand <= GameController.getInstance().getScenario().getScenarioSettings().INJURIES_COMMON_PER_YEAR) {
        addInjuryToTeam(teams[i], InjuryData.PROBABILITY_COMMON);
      }
    }
  }

  /**
   * Add injuries to a team, controlled by a probability setting.
   * 
   * @param team
   *          Team
   * @param probability
   *          int
   */
  private static void addInjuryToTeam(Team team, int probability) {
    if (team.getInjuriesCount() >= GameController.getInstance().getScenario().getScenarioSettings().INJURIES_MAX_PER_TEAM) {
      return;
    }

    InjuryData[] injuries = getInjuriesByProbability(probability);
    InjuryData injuryData = injuries[GameController.getInstance().getScenario().getRandomInt(injuries.length - 1)];
    Calendar day = GameController.getInstance().getScenario().getScheduler().getToday();

    Player player = team.getPlayers()[GameController.getInstance().getScenario().getRandomInt(team.getPlayers().length - 1)];

    PlayerInjury injury = new PlayerInjury(day, injuryData);
    if (player.getPlayerInjury() == null) {
      player.setPlayerInjury(injury);
      InjuriesEventIPlayerInjuredLog event = new InjuriesEventIPlayerInjuredLog(GameController.getInstance().getScenario(), day, player, injury);
      GameController.getInstance().getScenario().getEventLog().addEventLogEntry(event);
    }

  }

  /**
   * Returns all injuries by a probability.
   * 
   * @param probability
   *          int
   * @return InjuryData[]
   */
  private static InjuryData[] getInjuriesByProbability(int probability) {
    Vector<InjuryData> result = new Vector<InjuryData>();
    InjuryData[] injuries = GameController.getInstance().getScenario().getInjuries();

    for (int i = 0; i < injuries.length; i++) {
      if (injuries[i].getProbability() == probability) {
        result.add(injuries[i]);
      }
    }
    return result.toArray(new InjuryData[result.size()]);
  }

  /**
   * Remove all healed injuries from players.
   */
  public static void removeInjuries() {
    Player[] players = GameController.getInstance().getScenario().getPlayers();
    Calendar day = GameController.getInstance().getScenario().getScheduler().getToday();
    for (int i = 0; i < players.length; i++) {
      PlayerInjury injury = players[i].getPlayerInjury();
      if (injury != null && injury.getEnd().before(day)) {
        players[i].setPlayerInjury(null);
        InjuriesEventPlayerOkLog event = new InjuriesEventPlayerOkLog(GameController.getInstance().getScenario(), day, players[i], injury);
        GameController.getInstance().getScenario().getEventLog().addEventLogEntry(event);
      }
    }
  }

  /**
   * Read injuries from XML Database
   * 
   * @param file
   *          File
   * @throws Exception
   * @return InjuryData[]
   */
  public static InjuryData[] readInjuriesFromXMLDatabase(File file) throws Exception {

    Vector<InjuryData> injuries = new Vector<InjuryData>();
    Document doc = ToolsXML.loadXmlFile(file);

    Element root = doc.getDocumentElement();

    NodeList injuryList = root.getElementsByTagName("INJURY");
    int count = injuryList.getLength();
    for (int i = 0; i < count; i++) {
      Element injuryElement = (Element) injuryList.item(i);
      injuries.add(new InjuryData(injuryElement));
    }

    return injuries.toArray(new InjuryData[injuries.size()]);
  }

}
