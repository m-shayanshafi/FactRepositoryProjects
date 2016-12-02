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
  
package org.icehockeymanager.ihm.game.transfers;

import org.icehockeymanager.ihm.game.finance.*;
import org.icehockeymanager.ihm.game.player.*;
import org.icehockeymanager.ihm.game.*;
import org.icehockeymanager.ihm.game.team.*;
import org.icehockeymanager.ihm.lib.*;
import java.util.*;
import java.util.logging.*;

import org.icehockeymanager.ihm.game.transfers.log.*;

/**
 * Transfers is helper class for Transfers, Contracts etc.
 * 
 * @author Bernhard von Gunten
 * @created October 2004
 */
public class Transfers implements IhmLogging {

  /**
   * Returns all Players who are on the transferlist and might be transfered to
   * the given team. Filtered by position
   * 
   * @return Player[] players
   * @param team Team
   * @param position int
   */
  public static Player[] getTransferListByPosition(Team team, int position) {
    Vector<Player> tmpPlayers = new Vector<Player>();
    Player[] players = GameController.getInstance().getScenario().getPlayers();
    for (int i = 0; i < players.length; i++) {
      if (players[i].getPlayerAttributes().getPosition() == position && players[i].allowTransferToTeam(team)) {
        tmpPlayers.add(players[i]);
      }
    }
    return tmpPlayers.toArray(new Player[tmpPlayers.size()]);
  }

  /**
   * Returns all Players who are on the transferlist and might be transfered to
   * the given team.
   * 
   * @param team Team
   * @return Player[]
   */
  public static Player[] getTransferList(Team team) {
    Vector<Player> tmpPlayers = new Vector<Player>();
    Player[] players = GameController.getInstance().getScenario().getPlayers();
    for (int i = 0; i < players.length; i++) {
      if (players[i].allowTransferToTeam(team)) {
        tmpPlayers.add(players[i]);
      }
    }
    return tmpPlayers.toArray(new Player[tmpPlayers.size()]);
  }

  /**
   * AI function. Returns all Players which might be transfered to a AI
   * controlled Team, filtered by positon, marketvalue and maximal return values
   * array size.
   * 
   * @param position int
   * @param maxMV int
   * @param minMV int
   * @param maxCount int
   * @return Player[]
   */
  private static Player[] getAiTransferListByPositionMaxMarketValue(int position, int maxMV, int minMV, int maxCount) {
    Vector<Player> tmpPlayers = new Vector<Player>();
    Player[] players = GameController.getInstance().getScenario().getPlayers();
    int count = 0;
    for (int i = 0; i < players.length; i++) {
      if ((players[i].allowAiTransferToTeam()) && (players[i].getMarketValue() <= maxMV) && (players[i].getMarketValue() >= minMV)) {
        tmpPlayers.add(players[i]);
        count++;
        if (count >= maxCount) {
          break;
        }
      }
    }
    return tmpPlayers.toArray(new Player[tmpPlayers.size()]);
  }

  /**
   * AI function. Checks if a positionn in the team is understaffed next season
   * and tries to transfer a player for this position.
   */
  public static void aiTransferPlayers() {
    Team[] teams = GameController.getInstance().getScenario().getTeams();
    for (int i = 0; i < teams.length; i++) {
      if (GameController.getInstance().getScenario().isAutoTeam(teams[i]) || GameController.getInstance().getScenario().getUserByTeam(teams[i]).isAutoTransfers()) {

        Calendar nextYear = GameController.getInstance().getScenario().getScheduler().getTodayNextYear();

        // Check for understaffing
        // Orderd by goalie, defense, wing, center
        // If a understaffed section is found, buy player
        if (teams[i].getPlayersByPositionWithContractCount(PlayerAttributes.POSITION_GOALTENDING, nextYear) < GameController.getInstance().getScenario().getScenarioSettings().AUTO_TRANSFER_GOALIE_MIN_POS_COUNT) {
          aiTransferPlayer(teams[i], PlayerAttributes.POSITION_GOALTENDING);
        } else if (teams[i].getPlayersByPositionWithContractCount(PlayerAttributes.POSITION_DEFENSE, nextYear) < GameController.getInstance().getScenario().getScenarioSettings().AUTO_TRANSFER_DEFENSE_MIN_POS_COUNT) {
          aiTransferPlayer(teams[i], PlayerAttributes.POSITION_DEFENSE);
        } else if (teams[i].getPlayersByPositionWithContractCount(PlayerAttributes.POSITION_WING, nextYear) < GameController.getInstance().getScenario().getScenarioSettings().AUTO_TRANSFER_WING_MIN_POS_COUNT) {
          aiTransferPlayer(teams[i], PlayerAttributes.POSITION_WING);
        } else if (teams[i].getPlayersByPositionWithContractCount(PlayerAttributes.POSITION_CENTER, nextYear) < GameController.getInstance().getScenario().getScenarioSettings().AUTO_TRANSFER_CENTER_MIN_POS_COUNT) {
          aiTransferPlayer(teams[i], PlayerAttributes.POSITION_CENTER);
        }
      }
    }
  }

  /**
   * Ai function. Searches and transfers a player to the ai team on a given
   * position.
   * 
   * @param team Team
   * @param position int
   */
  private static void aiTransferPlayer(Team team, int position) {
    Player players[] = getAiTransferListByPositionMaxMarketValue(position, team.getAveragePlayerMarketValue() + 1, team.getAveragePlayerMarketValue() - 10, 1);

    if (players.length > 0) {
      Calendar begin = players[0].getFirstPossibleWorkingDay();
      Calendar end = GameController.getInstance().getScenario().getScheduler().getLastDayNextYear();
      end.add(Calendar.YEAR, getAIRandomContractYears() - 1);

      transferPlayer(players[0], team, begin, end, players[0].getRandomSalary(), players[0].getRandomTransferFee());
    }
  }

  /**
   * AI function. The ai looks if there are teams that are overstaffed for next
   * season, and put players on the transferlist
   */
  public static void aiPlacePlayersOnTransferList() {
    Team[] teams = GameController.getInstance().getScenario().getTeams();
    for (int i = 0; i < teams.length; i++) {
      if (GameController.getInstance().getScenario().isAutoTeam(teams[i]) || GameController.getInstance().getScenario().getUserByTeam(teams[i]).isAutoTransfers()) {

        Calendar nextYear = GameController.getInstance().getScenario().getScheduler().getTodayNextYear();

        // Check for overerstaffing
        // Orderd by goalie, defense, wing, center
        // If a overstaffed section is found, place Player on transferlist
        if (teams[i].getPlayersByPositionWithContractCount(PlayerAttributes.POSITION_GOALTENDING, nextYear) > GameController.getInstance().getScenario().getScenarioSettings().AUTO_TRANSFER_GOALIE_MAX_POS_COUNT) {
          placePlayerOnTransferList(teams[i], PlayerAttributes.POSITION_GOALTENDING);
        } else if (teams[i].getPlayersByPositionWithContractCount(PlayerAttributes.POSITION_DEFENSE, nextYear) > GameController.getInstance().getScenario().getScenarioSettings().AUTO_TRANSFER_DEFENSE_MAX_POS_COUNT) {
          placePlayerOnTransferList(teams[i], PlayerAttributes.POSITION_DEFENSE);
        } else if (teams[i].getPlayersByPositionWithContractCount(PlayerAttributes.POSITION_WING, nextYear) > GameController.getInstance().getScenario().getScenarioSettings().AUTO_TRANSFER_WING_MAX_POS_COUNT) {
          placePlayerOnTransferList(teams[i], PlayerAttributes.POSITION_WING);
        } else if (teams[i].getPlayersByPositionWithContractCount(PlayerAttributes.POSITION_CENTER, nextYear) > GameController.getInstance().getScenario().getScenarioSettings().AUTO_TRANSFER_CENTER_MAX_POS_COUNT) {
          placePlayerOnTransferList(teams[i], PlayerAttributes.POSITION_CENTER);
        }
      }
    }
  }

  /**
   * AI function. Places a player of the given team & positon on the
   * transferlist.
   * 
   * @param team Team
   * @param position int
   */
  private static void placePlayerOnTransferList(Team team, int position) {
    // First check if team has allready a player on the transferlist in this
    // position
    if (!team.hasPlayersOnTransferList(position)) {
      Player[] players = team.getPlayersByPosition(position);
      Player.sortPlayerArray(players, Player.SORT_CONTRACT_CURRENT_END_DATE, true);
      if (players.length > 0) {
        players[0].setOnTransferList(true);

        Calendar day = GameController.getInstance().getScenario().getScheduler().getToday();
        TransfersListLog event = new TransfersListLog(GameController.getInstance().getScenario(), day, players[0]);
        GameController.getInstance().getScenario().getEventLog().addEventLogEntry(event);
      }
    }
  }

  /**
   * Helper function switches player to/from the transfer list.
   * 
   * @param player Player
   */
  public static void switchTransferListFlag(Player player) {

    player.setOnTransferList(!player.isOnTransferList());

    if (player.isOnTransferList()) {

      Calendar day = GameController.getInstance().getScenario().getScheduler().getToday();
      TransfersListLog event = new TransfersListLog(GameController.getInstance().getScenario(), day, player);
      GameController.getInstance().getScenario().getEventLog().addEventLogEntry(event);

    }
  }

  /**
   * AI function. Searches for positions that are understaffed for next season
   * and extends some existing contracts.
   */
  public static void aiExtendContracts() {
    Team[] teams = GameController.getInstance().getScenario().getTeams();
    for (int i = 0; i < teams.length; i++) {
      if (GameController.getInstance().getScenario().isAutoTeam(teams[i]) || GameController.getInstance().getScenario().getUserByTeam(teams[i]).isAutoTransfers()) {

        Calendar nextYear = GameController.getInstance().getScenario().getScheduler().getTodayNextYear();

        // Check for understaffing
        // Orderd by goalie, defense, wing, center
        // If a understaffed section is found, buy player
        if (teams[i].getPlayersByPositionWithContractCount(PlayerAttributes.POSITION_GOALTENDING, nextYear) < GameController.getInstance().getScenario().getScenarioSettings().AUTO_TRANSFER_GOALIE_MIN_POS_COUNT) {
          Player[] playerList = teams[i].getAIExtendableContractsForYear(PlayerAttributes.POSITION_GOALTENDING, nextYear);
          Player.sortPlayerArray(playerList, Player.SORT_ATTRIBUTES_TOTAL_AVERAGE, false);
          if (playerList.length > 0)
            aiExtendContractForPlayer(playerList[0]);
        } else if (teams[i].getPlayersByPositionWithContractCount(PlayerAttributes.POSITION_DEFENSE, nextYear) < GameController.getInstance().getScenario().getScenarioSettings().AUTO_TRANSFER_DEFENSE_MIN_POS_COUNT) {
          Player[] playerList = teams[i].getAIExtendableContractsForYear(PlayerAttributes.POSITION_DEFENSE, nextYear);
          Player.sortPlayerArray(playerList, Player.SORT_ATTRIBUTES_TOTAL_AVERAGE, false);
          if (playerList.length > 0)
            aiExtendContractForPlayer(playerList[0]);
        } else if (teams[i].getPlayersByPositionWithContractCount(PlayerAttributes.POSITION_WING, nextYear) < GameController.getInstance().getScenario().getScenarioSettings().AUTO_TRANSFER_WING_MIN_POS_COUNT) {
          Player[] playerList = teams[i].getAIExtendableContractsForYear(PlayerAttributes.POSITION_WING, nextYear);
          Player.sortPlayerArray(playerList, Player.SORT_ATTRIBUTES_TOTAL_AVERAGE, false);
          if (playerList.length > 0)
            aiExtendContractForPlayer(playerList[0]);
        } else if (teams[i].getPlayersByPositionWithContractCount(PlayerAttributes.POSITION_CENTER, nextYear) < GameController.getInstance().getScenario().getScenarioSettings().AUTO_TRANSFER_CENTER_MIN_POS_COUNT) {
          Player[] playerList = teams[i].getAIExtendableContractsForYear(PlayerAttributes.POSITION_CENTER, nextYear);
          Player.sortPlayerArray(playerList, Player.SORT_ATTRIBUTES_TOTAL_AVERAGE, false);
          if (playerList.length > 0)
            aiExtendContractForPlayer(playerList[0]);
        }

      }
    }
  }

  /**
   * AI function. Extend contract of a given player for n years.
   * 
   * @param player Player
   */
  private static void aiExtendContractForPlayer(Player player) {
    PlayerContract contract = player.getPlayerContractCurrent();
    Calendar newEnd = contract.getEndDate();
    newEnd.add(Calendar.YEAR, getAIRandomContractYears());
    extendContract(player, newEnd, player.getRandomSalary());
  }

  /**
   * Helper function to transfer a player from one team to another. Creates new
   * contracts. Moves players now, if needed.
   * 
   * @param player Player
   * @param newTeam Team
   * @param startDate Calendar
   * @param endDate Calendar
   * @param newCosts double
   * @param newTransferFee double
   */
  public static void transferPlayer(Player player, Team newTeam, Calendar startDate, Calendar endDate, double newCosts, double newTransferFee) {

    PlayerContract oldContract = player.getPlayerContractCurrent();
    boolean buyOut = (oldContract != null && oldContract.getEndDate().after(startDate));
    Team oldTeam = null;
    double oldTransferFee = 0;

    if (oldContract != null) {
      oldTeam = oldContract.getTeam();
      oldTransferFee = oldContract.getTransferFee();
    }

    PlayerContract newContract = new PlayerContract(newTeam, player);
    newContract.setStartDate(startDate);
    newContract.setEndDate(endDate);
    newContract.setTransferFee(newTransferFee);
    newContract.setAmount(newCosts);

    if (buyOut) {
      // Player transfers now, because he was bought ot of the current contract
      player.setOnlyPlayerContract(newContract);
      oldContract.getTeam().removePlayer(player);
      newTeam.addPlayer(player);
      // TODO : Handle transferfee
    } else if (oldContract == null) {
      // Player transfers now, because he has no existing contract
      player.addPlayerContract(newContract);
      newTeam.addPlayer(player);
    } else if (oldContract != null) {
      // Player transfers at the end of the existing contract
      player.addPlayerContract(newContract);
    }

    player.finishTransfer();

    Calendar day = GameController.getInstance().getScenario().getScheduler().getToday();

    // Now do the money talk
    if (oldTransferFee != 0) {
      if (oldTeam != null) {
        oldTeam.getAccounting().addBookingEntry(new BookingEntry(oldTransferFee, day, "accounting.booking.transferIncome"));
      }
      newTeam.getAccounting().addBookingEntry(new BookingEntry(-oldTransferFee, day, "accounting.booking.transferCosts"));
    }

    TransfersLog event = new TransfersLog(GameController.getInstance().getScenario(), day, newTeam, oldTeam, player, newContract, buyOut, oldTransferFee);
    GameController.getInstance().getScenario().getEventLog().addEventLogEntry(event);

  }

  /**
   * Helper function to extend a players contract untel a new enddate and new
   * salary.
   * 
   * @param player Player
   * @param endDate Calendar
   * @param costs double
   */
  public static void extendContract(Player player, Calendar endDate, double costs) {
    try {
      PlayerContract contract = player.getPlayerContractCurrent();
      PlayerContract oldContract = (PlayerContract) contract.clone();

      contract.setEndDate(endDate);

      Calendar day = GameController.getInstance().getScenario().getScheduler().getToday();
      TransfersExtendedContractLog event = new TransfersExtendedContractLog(GameController.getInstance().getScenario(), day, player, oldContract, contract);
      GameController.getInstance().getScenario().getEventLog().addEventLogEntry(event);
    } catch (Exception err) {
      logger.log(Level.SEVERE, "extendContract failed!", err);
    }
  }

  /**
   * AI function. Returns a random number of (contract) years.
   * 
   * @return int
   */
  public static int getAIRandomContractYears() {
    return GameController.getInstance().getScenario().getRandomInt(1, GameController.getInstance().getScenario().getScenarioSettings().SCENARIO_START_MAX_CONTRACT_YEARS);
  }

  /**
   * Helper function to attach a contract to a prospect.
   * 
   * @param player Player
   * @param newTeam Team
   * @param startDate Calendar
   * @param endDate Calendar
   * @param newCosts double
   * @param newTransferFee double
   */
  public static void attachContractToProspect(Player player, Team newTeam, Calendar startDate, Calendar endDate, double newCosts, double newTransferFee) {

    PlayerContract newContract = new PlayerContract(newTeam, player);
    newContract.setStartDate(startDate);
    newContract.setEndDate(endDate);
    newContract.setTransferFee(newTransferFee);
    newContract.setAmount(newCosts);

    player.setOnlyPlayerContract(newContract);
    newTeam.removeProspect(player);
    newTeam.addPlayer(player);

    player.finishProspectHire();

    Calendar day = GameController.getInstance().getScenario().getScheduler().getToday();
    ProspectHireLog event = new ProspectHireLog(GameController.getInstance().getScenario(), day, newTeam, player, newContract);
    GameController.getInstance().getScenario().getEventLog().addEventLogEntry(event);

  }

}
