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
  
package org.icehockeymanager.ihm.game.randomimpacts;

import java.io.*;
import java.util.*;

import org.icehockeymanager.ihm.game.*;
import org.icehockeymanager.ihm.game.player.*;
import org.icehockeymanager.ihm.game.randomimpacts.log.*;
import org.icehockeymanager.ihm.game.user.*;
import org.icehockeymanager.ihm.lib.*;
import org.w3c.dom.*;

/**
 * RandomImpactTeam is a helper class to generate random impacts for teams and
 * players.
 * 
 * @author Bernhard von Gunten
 * @created October, 2004
 */
public class RandomImpacts {

  /**
   * Creates on the base of the game settings a random impact to a team.
   */
  public static void createRandomImpactOnTeam() {
    User[] users = GameController.getInstance().getScenario().getUsers();
    for (int i = 0; i < users.length; i++) {
      int rand = GameController.getInstance().getScenario().getRandomInt(364);
      if (rand <= GameController.getInstance().getScenario().getScenarioSettings().GLOBAL_IMPACTS_RANDOM_EVENTS_TEAM_YEAR) {
        RandomImpactTeam[] res = GameController.getInstance().getScenario().getRandomImpactsTeam();
        int id = GameController.getInstance().getScenario().getRandomInt(0, res.length - 1);
        RandomImpactTeam re = res[id];
        re.impactOnTeam(users[i].getTeam());

        Calendar day = GameController.getInstance().getScenario().getScheduler().getToday();
        RandomImpactsTeamLog event = new RandomImpactsTeamLog(GameController.getInstance().getScenario(), day, users[i].getTeam(), re);
        GameController.getInstance().getScenario().getEventLog().addEventLogEntry(event);
      }
    }
  }

  /**
   * Creates on the base of the game settings a random impact to a player
   * 
   */
  public static void createRandomImpactOnPlayer() {
    User[] users = GameController.getInstance().getScenario().getUsers();
    for (int i = 0; i < users.length; i++) {
      int rand = GameController.getInstance().getScenario().getRandomInt(364);
      if (rand <= GameController.getInstance().getScenario().getScenarioSettings().GLOBAL_IMPACTS_RANDOM_EVENTS_PLAYER_YEAR) {
        RandomImpactPlayer[] res = GameController.getInstance().getScenario().getRandomImpactsPlayer();
        int id = GameController.getInstance().getScenario().getRandomInt(0, res.length - 1);
        RandomImpactPlayer re = res[id];
        Player[] players = users[i].getTeam().getPlayers();
        int playerId = GameController.getInstance().getScenario().getRandomInt(0, players.length - 1);
        re.impactOnPlayer(players[playerId]);

        Calendar day = GameController.getInstance().getScenario().getScheduler().getToday();
        RandomImpactsPlayerLog event = new RandomImpactsPlayerLog(GameController.getInstance().getScenario(), day, players[playerId], re);
        GameController.getInstance().getScenario().getEventLog().addEventLogEntry(event);
      }
    }
  }

  /**
   * Reads random impacts from a XML file.
   * 
   * @param file
   * @return Array of random impacts.
   * @throws Exception
   */
  public static RandomImpact[] readRandomImpactsFromXMLDatabase(File file) throws Exception {

    Vector<RandomImpact> randomEvents = new Vector<RandomImpact>();
    Document doc = ToolsXML.loadXmlFile(file);

    Element root = doc.getDocumentElement();

    NodeList reList = root.getElementsByTagName("RANDOMIMPACT");
    int count = reList.getLength();
    for (int i = 0; i < count; i++) {
      Element reElement = (Element) reList.item(i);
      if (reElement.getAttribute("TYPE").equals(RandomImpactTeam.RANDOM_EVENT_TYPE)) {
        randomEvents.add(new RandomImpactTeam(reElement));
      } else if (reElement.getAttribute("TYPE").equals(RandomImpactPlayer.RANDOM_EVENT_TYPE)) {
        randomEvents.add(new RandomImpactPlayer(reElement));
      }

    }

    return randomEvents.toArray(new RandomImpact[randomEvents.size()]);
  }

}