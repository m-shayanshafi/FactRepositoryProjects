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
  
package org.icehockeymanager.ihm.clients.devgui.ihm.player;

import java.util.*;
import java.util.logging.*;

import org.icehockeymanager.ihm.clients.devgui.controller.*;
import org.icehockeymanager.ihm.game.player.*;
import org.icehockeymanager.ihm.lib.*;

public class PlayerTools implements IhmLogging {

  private PlayerTools() {
  }

  public static String getPlayerInfoTable(Player player) {
    String result = "";
    try {
      result += ClientController.getInstance().getTranslation("player.playerInfoBirthday") + " " + Tools.dateToString(player.getPlayerInfo().getBirthdate().getTime(), Tools.DATE_FORMAT_EU_DATE) + " (" + player.getPlayerInfo().getAge() + ")\n";
    } catch (Exception err) {
      result += ClientController.getInstance().getTranslation("player.playerInfoBirthday" + "\n");
    }

    result += ClientController.getInstance().getTranslation("player.playerInfoWeight") + " " + player.getPlayerAttributes().getWeight() + "\n";
    result += ClientController.getInstance().getTranslation("player.playerInfoSize") + " " + player.getPlayerAttributes().getWeight() + "\n";
    result += ClientController.getInstance().getTranslation("player.playerInfoHandeness") + " " + player.getPlayerAttributes().getWeight() + "\n";

    String countryName = ClientController.getInstance().getStrictTranslation(player.getPlayerInfo().getCountry().getNameKey());
    if (countryName == null) {
      countryName = player.getPlayerInfo().getCountry().getMasterTranslation();
    }
    result += ClientController.getInstance().getTranslation("player.playerInfoCountry") + " " + countryName + "\n";

    result += "\n";

    result += ClientController.getInstance().getTranslation("player.contracts") + "\n";

    PlayerContract[] contracts = player.getPlayerContracts();
    for (int i = 0; i < contracts.length; i++) {
      result += " " + contracts[i].getTeam().getTeamInfo().getTeamName() + " ";
      try {
        result += Tools.dateToString(contracts[i].getStartDate().getTime(), Tools.DATE_FORMAT_EU_DATE) + "-";
        result += Tools.dateToString(contracts[i].getEndDate().getTime(), Tools.DATE_FORMAT_EU_DATE) + "\n";
      } catch (Exception err) {
        logger.log(Level.SEVERE, "dateToString failed!", err);
      }
    }

    return result;
  }

  public static String getSpecificAttributesStringTable(Player player) {
    String result = "";
    PlayerAttribute[] pa = player.getPlayerAttributes().getSpecificPlayerAttributes();

    String prefix = "player.attributes.field.";
    if (player instanceof org.icehockeymanager.ihm.game.player.goalie.Goalie) {
      prefix = "player.attributes.goalie.";
    }

    for (int i = 0; i < pa.length; i++) {
      result += Tools.rPad(ClientController.getInstance().getTranslation(prefix + pa[i].getKey()), 20) + pa[i].getIntValue() + "\n";
    }
    return result;
  }

  public static String getCommonAttributesStringTable(Player player) {
    String result = "";
    Hashtable<String, PlayerAttribute> common = player.getPlayerAttributes().getCommonAttributes();

    for (Enumeration<PlayerAttribute> e = common.elements(); e.hasMoreElements();) {
      PlayerAttribute attr = e.nextElement();
      result += Tools.rPad(ClientController.getInstance().getTranslation("player.attributes." + attr.getKey()), 20) + attr.getIntValue() + "\n";
    }
    return result;
  }

  /**
   * Gets the attribute of the PlayerAttributes object as a String.
   * @param attributes 
   * 
   * @return The Position of a player as String.
   */
  public static String getPositionStr(PlayerAttributes attributes) {

    switch (attributes.getPosition()) {
    case PlayerAttributes.POSITION_CENTER:
      return ClientController.getInstance().getTranslation("tmPlayerAttributes.center");
    case PlayerAttributes.POSITION_GOALTENDING:
      return ClientController.getInstance().getTranslation("tmPlayerAttributes.goalie");
    case PlayerAttributes.POSITION_DEFENSE:
      return ClientController.getInstance().getTranslation("tmPlayerAttributes.defense");
    case PlayerAttributes.POSITION_WING:
      return ClientController.getInstance().getTranslation("tmPlayerAttributes.wing");
    }
    return "";
  }

}
