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
  
package org.icehockeymanager.ihm.clients.devgui.ihm.sponsoring;

import org.icehockeymanager.ihm.clients.devgui.controller.ClientController;
import org.icehockeymanager.ihm.game.sponsoring.SponsoringContract;
import org.icehockeymanager.ihm.game.sponsoring.Sponsoring;
import org.icehockeymanager.ihm.lib.Tools;

public class SponsoringTools {

  public static String getSponsoringDescription(SponsoringContract contract) {
    try  {
      String result = contract.getSponsorName() + " (";
      result += Tools.dateToString(contract.getStartDate().getTime(), Tools.DATE_FORMAT_EU_DATE) + "-";
      result += Tools.dateToString(contract.getEndDate().getTime(), Tools.DATE_FORMAT_EU_DATE) + ") "; 
      result += getSponsorLabelAndAmount(contract);
      return result;
    } catch (Exception err) {
      return "<ERROR>";
    }
  }

  public static String getSponsoringListDescription(SponsoringContract contract) {
    try  {
      String result = Tools.rPad(contract.getSponsorName(), 10) + " : ";
      result += Tools.lPad(Tools.doubleToStringC(contract.getAmount()), 12);
      return result;
    } catch (Exception err) {
      return "<ERROR>";
    }
  }
  
  public static String getSponsorLabelAndName(SponsoringContract contract) {
    return ClientController.getInstance().getTranslation("sponsoring.name") + " : " + contract.getSponsorName();
  }

  public static String getSponsorLabelAndHappiness(SponsoringContract contract) {
    try {
      return ClientController.getInstance().getTranslation("sponsoring.happiness") + " : " + Tools.intToString(contract.getSponsorHappiness());
    } catch (Exception err) {
      return "";
    }
  }
  
  public static String getSponsorLabelAndAmount(SponsoringContract contract) {
    return ClientController.getInstance().getTranslation("sponsoring.amount") + " : " + Tools.doubleToStringC(contract.getAmount());
  }
  
  public static String getTypeDescription(String type) {
    if (type.equals(Sponsoring.SPONSOR_TYPE_MAIN)) {
      return ClientController.getInstance().getTranslation("sponsoring.type.main");
    } else if (type.equals(Sponsoring.SPONSOR_TYPE_ARENA_BOARD)) {
      return ClientController.getInstance().getTranslation("sponsoring.type.arena.board");
    } else if (type.equals(Sponsoring.SPONSOR_TYPE_ARENA_ICE)) {
      return ClientController.getInstance().getTranslation("sponsoring.type.arena.ice");
    } else if (type.equals(Sponsoring.SPONSOR_TYPE_MEDIA_RADIO)) {
      return ClientController.getInstance().getTranslation("sponsoring.type.media.radio");
    } else if (type.equals(Sponsoring.SPONSOR_TYPE_MEDIA_TV)) {
      return ClientController.getInstance().getTranslation("sponsoring.type.media.tv");
    } else if (type.equals(Sponsoring.SPONSOR_TYPE_MEDIA_WEB)) {
      return ClientController.getInstance().getTranslation("sponsoring.type.media.web");
    } else if (type.equals(Sponsoring.SPONSOR_TYPE_TEAM_MAIN)) {
      return ClientController.getInstance().getTranslation("sponsoring.type.team.main");
    } else if (type.equals(Sponsoring.SPONSOR_TYPE_TEAM_EQUIPMENT)) {
      return ClientController.getInstance().getTranslation("sponsoring.type.team.equipment");
    } else if (type.equals(Sponsoring.SPONSOR_TYPE_TEAM_STICK)) {
      return ClientController.getInstance().getTranslation("sponsoring.type.team.stick");
    } else {
      return "## unknown type ##";
    }
  }
  
}
