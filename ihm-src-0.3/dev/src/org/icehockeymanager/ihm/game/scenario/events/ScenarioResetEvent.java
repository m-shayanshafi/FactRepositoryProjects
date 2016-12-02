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
  
package org.icehockeymanager.ihm.game.scenario.events;

import java.util.*;

import org.icehockeymanager.ihm.game.*;
import org.icehockeymanager.ihm.game.player.*;
import org.icehockeymanager.ihm.game.prospects.*;
import org.icehockeymanager.ihm.game.scheduler.events.*;
import org.icehockeymanager.ihm.game.team.*;

/**
 * Scenario reset event is the very first Event to be played for a new game. It
 * attaches Contracts to players, fills up the prospects team etc etc.
 * 
 * @author Bernhard von Gunten
 * @created October 2004
 */
public class ScenarioResetEvent extends SchedulerInternalEvent {

  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 3833748780691239476L;

  /**
   * ScenarioResetEvent constructor
   * 
   * @param source
   *          The source of this event
   * @param day
   *          The day of this event
   */
  public ScenarioResetEvent(Object source, Calendar day) {
    super(source, day, "events.internal.yearstart");
  }

  /** Reset scenario and add an endYear event to the Scheduler */
  public void play() {
    super.play();

    // Attach a random contract to every player employed in a team
    Player[] players = GameController.getInstance().getScenario().getEmployedPlayers();
    for (int i = 0; i < players.length; i++) {
      attachRandomContract(players[i]);
    }

    // Move freeplayers to the transferlist
    Player[] freePlayers = GameController.getInstance().getScenario().getFreePlayers();
    for (int i = 0; i < freePlayers.length; i++) {
      freePlayers[i].setOnTransferList(true);
    }

    // Attach random prospects to the teams
    Team[] teams = GameController.getInstance().getScenario().getTeams();
    for (int n = 0; n < teams.length; n++) {
      for (int i = 0; i < GameController.getInstance().getScenario().getScenarioSettings().SCENARIO_PROSPECTS_START_COUNT; i++) {
        Prospects.attachNewProspectToTeam(teams[n]);
      }
    }

    // Now add the first day of the season
    GameController.getInstance().getScenario().getScheduler().addEvent(new SeasonStartEvent(this, this.getDay()));

  }

  /**
   * Attaches random start contracts to given player
   * 
   * @param player
   */
  private void attachRandomContract(Player player) {
    int contractYears = GameController.getInstance().getScenario().getRandomInt(GameController.getInstance().getScenario().getScenarioSettings().SCENARIO_START_MAX_CONTRACT_YEARS);
    PlayerContract contract = new PlayerContract(player.getTeam(), player);
    Calendar startDate = GameController.getInstance().getScenario().getScheduler().getFirstDay();
    Calendar endDate = GameController.getInstance().getScenario().getScheduler().getLastDay();
    endDate.add(Calendar.YEAR, contractYears);
    contract.setStartDate(startDate);
    contract.setEndDate(endDate);
    contract.setAmount(player.getRandomSalary());
    contract.setTransferFee(player.getRandomTransferFee());
    player.setOnlyPlayerContract(contract);
  }

}