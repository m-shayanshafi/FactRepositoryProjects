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
  
package org.icehockeymanager.ihm.clients.devgui.ihm.eventlog;

import java.util.logging.*;

import org.icehockeymanager.ihm.lib.*;
import org.icehockeymanager.ihm.game.eventlog.*;
import org.icehockeymanager.ihm.game.injuries.log.*;
import org.icehockeymanager.ihm.game.training.log.*;
import org.icehockeymanager.ihm.game.transfers.log.*;
import org.icehockeymanager.ihm.game.scenario.log.*;
import org.icehockeymanager.ihm.game.randomimpacts.log.*;
import org.icehockeymanager.ihm.game.finance.log.*;
import org.icehockeymanager.ihm.clients.devgui.controller.*;

public class EventLogTools implements IhmLogging {

  public static String getMessage(EventLogEntry entry) {

    if (entry instanceof InjuriesEventIPlayerInjuredLog) {
      try {
        InjuriesEventIPlayerInjuredLog event = (InjuriesEventIPlayerInjuredLog) entry;
        String msg = ClientController.getInstance().getTranslation("injuries.log.injury");
        msg = Tools.replaceAllSubString(msg, "%1%", event.getPlayer().getPlayerInfo().getPlayerName());
        msg = Tools.replaceAllSubString(msg, "%2%", event.getPlayerInjury().getMasterTranslation());
        msg = Tools.replaceAllSubString(msg, "%3%", String.valueOf(event.getPlayerInjury().getDuration()));
        return msg;
      } catch (Exception err) {
        logger.log(Level.SEVERE, "InjuriesEventIPlayerInjuredLog failed!", err);
      }
    } else if (entry instanceof InjuriesEventPlayerOkLog) {
      try {
        InjuriesEventPlayerOkLog event = (InjuriesEventPlayerOkLog) entry;
        String msg = ClientController.getInstance().getTranslation("injuries.log.fit");
        msg = Tools.replaceAllSubString(msg, "%1%", event.getPlayer().getPlayerInfo().getPlayerName());
        return msg;
      } catch (Exception err) {
        logger.log(Level.SEVERE, "InjuriesEventPlayerOkLog failed!", err);
      }
    } else if (entry instanceof RandomImpactsPlayerLog) {
      try {
        RandomImpactsPlayerLog event = (RandomImpactsPlayerLog) entry;
        String msg = event.getRandomImpact().getMasterTranslation();
        msg = Tools.replaceAllSubString(msg, "%1%", event.getPlayer().getPlayerInfo().getPlayerName());
        return msg;
      } catch (Exception err) {
        logger.log(Level.SEVERE, "RandomImpactsPlayerLog failed!", err);
      }
    } else if (entry instanceof RandomImpactsTeamLog) {
      try {
        RandomImpactsTeamLog event = (RandomImpactsTeamLog) entry;
        String msg = event.getRandomImpact().getMasterTranslation();
        msg = Tools.replaceAllSubString(msg, "%1%", event.getTeam().getTeamInfo().getTeamName());
        return msg;
      } catch (Exception err) {
        logger.log(Level.SEVERE, "RandomImpactsTeamLog failed!", err);
      }
    } else if (entry instanceof TrainingEventLog) {
      try {
        TrainingEventLog event = (TrainingEventLog) entry;
        String msg;
        if (event.isAuto()) {
          msg = ClientController.getInstance().getTranslation("training.log.autoSchedule");
        } else {
          msg = ClientController.getInstance().getTranslation("training.log.userSchedule");
        }
        return msg;
      } catch (Exception err) {
        logger.log(Level.SEVERE, "TrainingEventLog failed!", err);
      }
    } else if (entry instanceof ProspectHireLog) {
      try {
        ProspectHireLog event = (ProspectHireLog) entry;
        String msg = ClientController.getInstance().getTranslation("transfers.log.prospectHire");
        msg = Tools.replaceAllSubString(msg, "%1%", event.getPlayer().getTeam().getTeamInfo().getTeamName());
        msg = Tools.replaceAllSubString(msg, "%2%", event.getPlayer().getPlayerInfo().getPlayerName());
        return msg;

      } catch (Exception err) {
        logger.log(Level.SEVERE, "ProspectHireLog failed!", err);
      }
    } else if (entry instanceof TransfersExtendedContractLog) {
      try {
        TransfersExtendedContractLog event = (TransfersExtendedContractLog) entry;
        String msg = ClientController.getInstance().getTranslation("transfers.log.extendedContract");
        msg = Tools.replaceAllSubString(msg, "%0%", event.getPlayer().getTeam().getTeamInfo().getTeamName());
        msg = Tools.replaceAllSubString(msg, "%1%", event.getPlayer().getPlayerInfo().getPlayerName());
        msg = Tools.replaceAllSubString(msg, "%2%", Tools.dateToString(event.getOldContract().getEndDate().getTime(), Tools.DATE_FORMAT_EU_DATE));
        msg = Tools.replaceAllSubString(msg, "%3%", Tools.dateToString(event.getNewContract().getEndDate().getTime(), Tools.DATE_FORMAT_EU_DATE));
        return msg;
      } catch (Exception err) {
        logger.log(Level.SEVERE, "TransfersExtendedContractLog failed!", err);
      }
    } else if (entry instanceof TransfersListLog) {
      try {
        TransfersListLog event = (TransfersListLog) entry;
        String msg = ClientController.getInstance().getTranslation("transfers.log.playerOnTransferList");
        msg = Tools.replaceAllSubString(msg, "%1%", event.getPlayer().getTeam().getTeamInfo().getTeamName());
        msg = Tools.replaceAllSubString(msg, "%2%", event.getPlayer().getPlayerInfo().getPlayerName());
        return msg;
      } catch (Exception err) {
        logger.log(Level.SEVERE, "TransfersListLog failed!", err);
      }
    } else if (entry instanceof TransfersLog) {
      try {
        TransfersLog event = (TransfersLog) entry;

        if (event.getOldTeam() == null) {
          String msg = ClientController.getInstance().getTranslation("transfers.log.transfernow");
          msg = Tools.replaceAllSubString(msg, "%1%", event.getPlayer().getPlayerInfo().getPlayerName());
          msg = Tools.replaceAllSubString(msg, "%2%", event.getNewTeam().getTeamInfo().getTeamName());
          return msg;
        } else {

          String msg = ClientController.getInstance().getTranslation("transfers.log.transferlater");
          msg = Tools.replaceAllSubString(msg, "%1%", event.getPlayer().getPlayerInfo().getPlayerName());
          msg = Tools.replaceAllSubString(msg, "%2%", event.getOldTeam().getTeamInfo().getTeamName());
          msg = Tools.replaceAllSubString(msg, "%3%", event.getNewTeam().getTeamInfo().getTeamName());
          msg = Tools.replaceAllSubString(msg, "%2%", Tools.dateToString(event.getContract().getStartDate().getTime(), Tools.DATE_FORMAT_EU_DATE));
          return msg;
        }
      } catch (Exception err) {
        logger.log(Level.SEVERE, "TransfersLog failed!", err);
      }
    } else if (entry instanceof AccountingEventLog) {
      try {
        AccountingEventLog event = (AccountingEventLog) entry;
        String msg = ClientController.getInstance().getTranslation("accounting.log.accounting");
        msg = Tools.replaceAllSubString(msg, "%1%", event.getTeam().getTeamInfo().getTeamName());
        msg = Tools.replaceAllSubString(msg, "%2%", Tools.doubleToStringC(event.getBalance()));
        return msg;
      } catch (Exception err) {
        logger.log(Level.SEVERE, "AccountingEventLog failed!", err);
      }
    } else if (entry instanceof SeasonStartEventLog) {
      return ClientController.getInstance().getTranslation("scenario.log.yearstart");
    } else if (entry instanceof SeasonEndEventLog) {
      return ClientController.getInstance().getTranslation("scenario.log.yearend");
    } else {
      return "Add translations here: " + entry.toString();
    }

    return entry.toString();

  }

}
