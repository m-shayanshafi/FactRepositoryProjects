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
  
package org.icehockeymanager.ihm.clients.devgui.ihm.match;

import org.icehockeymanager.ihm.game.match.*;
import org.icehockeymanager.ihm.game.match.textengine.data.*;
import org.icehockeymanager.ihm.lib.Tools;
import org.icehockeymanager.ihm.clients.devgui.controller.*;
import java.util.*;

public class MatchTools {

  public static String getMatchDescription(Match match) {
    return match.getTeamHome().getTeamInfo().getTeamName() + " vs. " + match.getTeamAway().getTeamInfo().getTeamName() + " Result : " + match.getScoreSheet().getGoalsHome() + ":" + match.getScoreSheet().getGoalsAway();
  }

  public static String getMatchTime(int matchSeconds) {
    int minutes = matchSeconds / 60;
    int seconds = matchSeconds - (minutes * 60);
    return minutes + ":" + seconds;
  }

  public static String[] generateSituationRadioMessage(MatchDataSituation situation) {
    Vector<String> resultStrings = new Vector<String>();

    String puckholder = situation.getPuckHolder().getPlayerInfo().getLastName();

    String opponent = "";
    if (situation.getOpponent() != null) {
      opponent = situation.getOpponent().getPlayerInfo().getLastName();
    } else {
      opponent = "#NO OPPONENT#";
    }

    String partner = "";
    if (situation.getPartner() != null) {
      partner = situation.getPartner().getPlayerInfo().getLastName();
    } else {
      partner = "#NO PARTNER#";
    }

    String situationS = ClientController.getInstance().getTranslation(situation.getSituationKey());
    resultStrings.add(replacePlayers(situationS, puckholder, opponent, partner));

    switch (situation.getResult()) {
    case MatchDataSituation.RESULT_SUCCSESSFULL_NO_PROB: {
      String no_prob = ClientController.getInstance().getTranslation(situation.getSituationKey() + ".successfull_no_prob");
      resultStrings.add(replacePlayers(no_prob, puckholder, opponent, partner));
      break;
    }
    case MatchDataSituation.RESULT_SUCCSESSFULL_PROB: {
      String with_prob = ClientController.getInstance().getTranslation(situation.getSituationKey() + ".successfull_prob");
      resultStrings.add(replacePlayers(with_prob, puckholder, opponent, partner));
      break;
    }
    case MatchDataSituation.RESULT_FAILED: {
      String failed = ClientController.getInstance().getTranslation(situation.getSituationKey() + ".failed");
      resultStrings.add(replacePlayers(failed, puckholder, opponent, partner));
      break;
    }
    case MatchDataSituation.RESULT_DRAW: {
      String failed_draw = ClientController.getInstance().getTranslation(situation.getSituationKey() + ".draw");
      resultStrings.add(replacePlayers(failed_draw, puckholder, opponent, partner));
      break;
    }
    }

    return resultStrings.toArray(new String[resultStrings.size()]);
  }

  private static String replacePlayers(String src, String puckholder, String opponent, String partner) {
    String result = Tools.replaceAllSubString(src, "<Puckholder>", puckholder);
    result = Tools.replaceAllSubString(result, "<Opponent>", opponent);
    result = Tools.replaceAllSubString(result, "<Partner>", partner);
    return result;
  }
}
