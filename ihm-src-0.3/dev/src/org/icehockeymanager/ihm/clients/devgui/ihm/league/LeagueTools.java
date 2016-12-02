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
  
package org.icehockeymanager.ihm.clients.devgui.ihm.league;

import org.icehockeymanager.ihm.clients.devgui.controller.*;
import org.icehockeymanager.ihm.clients.devgui.ihm.league.std.*;
import org.icehockeymanager.ihm.game.league.*;
import org.icehockeymanager.ihm.game.league.events.*;
import org.icehockeymanager.ihm.game.league.helper.*;
import org.icehockeymanager.ihm.game.league.std.*;

public class LeagueTools {

  /**
   * Returns complete description of this element (owner, league, name)
   * @param leagueElement 
   * 
   * @return The description value
   */
  public static String getLeagueElementDescription(LeagueElement leagueElement) {
    if (leagueElement instanceof StdRegularSeason || leagueElement instanceof StdPlayoffs) {
      return StdLeagueTools.getLeagueElementDescription(leagueElement);
    }
    return "unknown league element";
  }

  public static String getShortLeagueElementDescription(LeagueElement leagueElement) {
    if (leagueElement instanceof StdRegularSeason || leagueElement instanceof StdPlayoffs) {
      return StdLeagueTools.getLeagueElementShortDescription(leagueElement);
    }
    return "unknown league element";
  }

  public static String getLeagueElementGroupDescription(LeagueElementGroup leg) {
    String owner = leg.getLeague().getLeagueOwner().getName();
    String league = leg.getLeague().getName();
    String name = ClientController.getInstance().getTranslation(leg.getNameKey());
    return owner + " : " + league + " : " + name;
  }

  public static String getGameDayDescription(GameDayMatchesEvent gameDay) {
    String result = getLeagueElementDescription(gameDay.getLeagueElement());
    result += ClientController.getInstance().getTranslation("ihm.round") + ": " + gameDay.getRound();
    return result;
  }

  public static String getLeagueDescription(League league) {
    return league.getLeagueOwner().getName() + " : " + league.getName();
  }

}
