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
  
package org.icehockeymanager.ihm.game.training;

import java.util.*;

import org.icehockeymanager.ihm.game.*;
import org.icehockeymanager.ihm.game.player.*;
import org.icehockeymanager.ihm.game.team.*;
import org.icehockeymanager.ihm.game.training.log.*;

/**
 * Training is a helper class to generate trainingschedules and train teams and
 * their players.
 * 
 * @author Bernhard von Gunten
 * @created September, 2004
 */
public class Training {

  /**
   * Train a player for a given week day
   * 
   * @param player
   *          Player to train
   * @param weekday
   *          Day of the week
   */
  public static void train(Player player, int weekday) {
    // First all player attributes decrease
    player.impactOnTrainingOverall();
    // Then the player could train twice a day
    trainUnit(player, weekday);
  }

  /**
   * Train a specific attribute on a player
   * 
   * @param player
   *          Player to train
   * @param weekday
   *          Day of the week
   */
  public static void trainUnit(Player player, int weekday) {
    TrainingSchedule trainingSchedule = player.getTrainingSchedule();
    if (trainingSchedule == null && player.getTeam() != null) {
      trainingSchedule = player.getTeam().getTrainingSchedule();
    }
    if (trainingSchedule != null) {
      if (player.getPlayerAttributes().getPosition() == PlayerAttributes.POSITION_GOALTENDING) {
        player.impactOnTrainingSpecific(trainingSchedule.getGoalieTrainingAttribute(weekday, 0));
        player.impactOnTrainingSpecific(trainingSchedule.getGoalieTrainingAttribute(weekday, 1));
      } else {
        player.impactOnTrainingSpecific(trainingSchedule.getFieldPlayerTrainingAttribute(weekday, 0));
        player.impactOnTrainingSpecific(trainingSchedule.getFieldPlayerTrainingAttribute(weekday, 1));
      }
    }
  }

  /**
   * Generates a training schedule based on the 2 worst attributes in the team
   * average for both: fieldplayer & goalie.
   * 
   * @param team
   *          Team
   */
  public static void generateTrainingSchedule(Team team) {
    TrainingSchedule trainingSchedule = new TrainingSchedule();
    PlayerAttribute[] fieldPlayerAttributes = team.getSpecificFieldPlayerAttributes();
    PlayerAttribute[] goalieAttributes = team.getSpecificGoalieAttributes();

    java.util.Arrays.sort(fieldPlayerAttributes);
    java.util.Arrays.sort(goalieAttributes);

    // Training each day, until sunday -> free
    for (int i = 0; i < 6; i++) {
      trainingSchedule.setFieldPlayerTrainingAttribute(fieldPlayerAttributes[0].getKey(), i, 0);
      trainingSchedule.setFieldPlayerTrainingAttribute(fieldPlayerAttributes[1].getKey(), i, 1);
    }
    for (int i = 0; i < 6; i++) {
      trainingSchedule.setGoalieTrainingAttribute(goalieAttributes[0].getKey(), i, 0);
      trainingSchedule.setGoalieTrainingAttribute(goalieAttributes[1].getKey(), i, 1);
    }

    team.setTrainingSchedule(trainingSchedule);
  }

  /**
   * Ai function to generate Trianingplans for teams.
   * 
   */
  public static void aiGenerateTrainingPlans() {
    Team[] teams = GameController.getInstance().getScenario().getTeams();
    Calendar day = GameController.getInstance().getScenario().getScheduler().getToday();

    for (int i = 0; i < teams.length; i++) {
      if (GameController.getInstance().getScenario().isAutoTeam(teams[i]) || GameController.getInstance().getScenario().getUserByTeam(teams[i]).isAutoTraining()) {
        generateTrainingSchedule(teams[i]);
        // Save the current plan for every team for later studies
        TrainingEventLog event = new TrainingEventLog(GameController.getInstance().getScenario(), day, teams[i], teams[i].getTrainingSchedule(), true);
        GameController.getInstance().getScenario().getEventLog().addEventLogEntry(event);
      } else {
        // Save the current plan for every team for later studies
        TrainingEventLog event = new TrainingEventLog(GameController.getInstance().getScenario(), day, teams[i], teams[i].getTrainingSchedule(), false);
        GameController.getInstance().getScenario().getEventLog().addEventLogEntry(event);
      }
    }
  }

}