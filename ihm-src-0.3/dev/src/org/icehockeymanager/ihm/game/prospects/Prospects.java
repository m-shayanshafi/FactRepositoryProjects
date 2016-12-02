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
  
package org.icehockeymanager.ihm.game.prospects;

import java.util.*;

import org.icehockeymanager.ihm.game.player.*;
import org.icehockeymanager.ihm.game.team.*;
import org.icehockeymanager.ihm.game.transfers.*;
import org.icehockeymanager.ihm.game.*;
import org.icehockeymanager.ihm.game.scenario.*;

/**
 * Prospects is a helper class to train the prospects or hiring them.
 * 
 * @author Bernhard von Gunten
 * @created October, 2004
 */
public class Prospects {

  /**
   * Trains every prospect of a team
   * 
   * @param team
   *          Team
   */
  public static void trainProspects(Team team) {
    Player[] prospects = team.getProspects();

    for (int i = 0; i < prospects.length; i++) {
      trainProspect(prospects[i]);
    }
  }

  /**
   * Trains one prospect.
   * 
   * @param prospect
   *          Player
   */
  private static void trainProspect(Player prospect) {
    // Get some values first
    // Talent (somehwere between 0 and 100) the most important key to fast
    // attributes improvement
    double talent = prospect.getPlayerAttributes().getCommonPlayerAttribute(PlayerAttributes.COMMON_TALENT_KEY).getValue();
    // Age in years (somewhere between 0 and 20)
    int age = prospect.getPlayerInfo().getAge();

    // TODO: Implement prospects infrastructure, coach etc to get
    // "prospectsBoost"
    // How good does a team handle it's prospects (somehwere between 1 and 10)
    double prospectsBoost = 1.5;

    // Max improvement per week
    double maxImprovement = GameController.getInstance().getScenario().getScenarioSettings().PROSPECTS_MAX_WEEKLY_IMPROVEMENT;

    // Get the players improvement value (somehwere between 0 and 100%)
    double playersPercent = (talent + (20 - age)) * prospectsBoost;
    if (playersPercent > 100) {
      playersPercent = 100;
    }
    double improvement = maxImprovement * (playersPercent / 100);

    // Improve the player attributes
    prospect.impactOnProspectsTraining(improvement);
  }

  /**
   * AI function. Hires every prospect which reached his max age.
   */
  public static void aiHireProspects() {
    Team[] teams = GameController.getInstance().getScenario().getTeams();
    for (int n = 0; n < teams.length; n++) {
      if (GameController.getInstance().getScenario().isAutoTeam(teams[n]) || GameController.getInstance().getScenario().getUserByTeam(teams[n]).isAutoProspectHiring()) {
        Player[] prospects = teams[n].getProspects();
        for (int i = 0; i < prospects.length; i++) {
          if (prospects[i].getPlayerInfo().getAge() >= GameController.getInstance().getScenario().getScenarioSettings().PROSPECTS_MAX_AGE) {
            Calendar begin = prospects[i].getFirstPossibleWorkingDay();
            Calendar end = GameController.getInstance().getScenario().getScheduler().getLastDayNextYear();
            end.add(Calendar.YEAR, Transfers.getAIRandomContractYears() - 1);
            hireProspect(prospects[i], prospects[i].getTeam(), begin, end, prospects[i].getFairSalary(), prospects[i].getFairTransferFee());
          }
        }
      }
    }
  }

  /**
   * Hireprospect creates a new Contract for a prospect.
   * 
   * @param prospect
   *          Player
   * @param team
   *          Team
   * @param startDate
   *          Calendar
   * @param endDate
   *          Calendar
   * @param newCosts
   *          double
   * @param newTransferFee
   *          double
   */
  public static void hireProspect(Player prospect, Team team, Calendar startDate, Calendar endDate, double newCosts, double newTransferFee) {
    Transfers.attachContractToProspect(prospect, team, startDate, endDate, newCosts, newTransferFee);
    prospect.finishProspectHire();

    // Create new prospect
    attachNewProspectToTeam(team);
  }

  /**
   * Creates a new prospect with random name, age etc. for a given team.
   * 
   * @param team
   *          Team
   */
  public static void attachNewProspectToTeam(Team team) {
    int minAge = GameController.getInstance().getScenario().getScenarioSettings().PROSPECTS_MIN_AGE;
    int maxAge = GameController.getInstance().getScenario().getScenarioSettings().PROSPECTS_MAX_AGE;
    Random r = GameController.getInstance().getScenario().getRandom();
    Calendar currentYear = GameController.getInstance().getScenario().getScheduler().getToday();
    String[] firstNames = GameController.getInstance().getScenario().getFirstNames();
    String[] lastNames = GameController.getInstance().getScenario().getLastNames();
    Country[] countries = GameController.getInstance().getScenario().getCountries();
    int key = GameController.getInstance().getScenario().getNextPlayerKey();
    int overall = team.getTeamAttributes().getOverall();

    Player prospect = Player.createRandomPlayer(r, currentYear, key, lastNames, firstNames, minAge, maxAge, overall - 20, overall - 10, countries);
    prospect.setProspect(true);

    team.addProspect(prospect);
  }

}
