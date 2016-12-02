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
  
package org.icehockeymanager.ihm.game.scenario;

import java.io.*;

import org.icehockeymanager.ihm.lib.*;

/**
 * ScenarioSettigns contains all important game settings for the game balance.
 * 
 * @author Bernhard von Gunten
 * @created September, 2004
 */
public class ScenarioSettings extends IhmSettingsXML implements Serializable {

  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 3257004367205774136L;

  public String TRAINING_ATTRIBUTES_MINUS_DOC = "Minus points for no training over all attributes per day ";

  public double TRAINING_ATTRIBUTES_MINUS = 1;

  public String TRAINING_ATTRIBUTES_PLUS_DOC = "Plus points for training over one attribute per day ";

  public double TRAINING_ATTRIBUTES_PLUS = 1.5;

  public String TRAINING_ENERGY_MINUS_DOC = "Energy lost for one training unit ";

  public double TRAINING_ENERGY_MINUS = .5;

  public String TRAINING_ENERGY_PLUS_DOC = "Energy win for 'relaxing' training ";

  public double TRAINING_ENERGY_PLUS = 2;

  public String IMPACT_GAMEWON_FORM_DOC = "Impact on Players Form for game won ";

  public double IMPACT_GAMEWON_FORM = 1;

  public String IMPACT_GAMEWON_MOTIVATION_DOC = "Impact on Players motivation for game won ";

  public double IMPACT_GAMEWON_MOTIVATION = 1;

  public String IMPACT_GAMETIED_FORM_DOC = "Impact on Players Form for game tied ";

  public double IMPACT_GAMETIED_FORM = 0;

  public String IMPACT_GAMETIED_MOTIVATION_DOC = "Impact on Players motivation for game tied ";

  public double IMPACT_GAMETIED_MOTIVATION = 0;

  public String IMPACT_GAMELOST_FORM_DOC = "Impact on Players Form for game lost ";

  public double IMPACT_GAMELOST_FORM = -1;

  public String IMPACT_GAMELOST_MOTIVATION_DOC = "Impact on Players motivation for game lost ";

  public double IMPACT_GAMELOST_MOTIVATION = -1;

  public String IMPACT_GOAL_FORM_DOC = "Impact on Players Form for goal ";

  public double IMPACT_GOAL_FORM = 0.5;

  public String IMPACT_GOAL_MOTIVATION_DOC = "Impact on Players motivation for goal ";

  public double IMPACT_GOAL_MOTIVATION = .5;

  public String IMPACT_ASSIST_FORM_DOC = "Impact on Players Form for assist ";

  public double IMPACT_ASSIST_FORM = 0.1;

  public String IMPACT_ASSIST_MOTIVATION_DOC = "Impact on Players motivation for assist ";

  public double IMPACT_ASSIST_MOTIVATION = 0.1;

  public String IMPACT_GAMEPLAYED_FORM_DOC = "Impact on Players Form for game palyed ";

  public double IMPACT_GAMEPLAYED_FORM = 0.1;

  public String IMPACT_GAMEPLAYED_MOTIVATION_DOC = "Impact on Players motivation for game played ";

  public double IMPACT_GAMEPLAYED_MOTIVATION = 0.1;

  public String IMPACT_PENALTY_FORM_DOC = "Impact on Players Form for penalty ";

  public double IMPACT_PENALTY_FORM = -0.1;

  public String IMPACT_PENALTY_MOTIVATION_DOC = "Impact on Players motivation for penalty ";

  public double IMPACT_PENALTY_MOTIVATION = -0.1;

  public String IMPACT_GOALAGAINST_FORM_DOC = "Impact on Players Form for goal against ";

  public double IMPACT_GOALAGAINST_FORM = -0.5;

  public String IMPACT_GOALAGAINST_MOTIVATION_DOC = "Impact on Players motivation for goal against ";

  public double IMPACT_GOALAGAINST_MOTIVATION = -0.5;

  public String AUTO_TRANSFER_GOALIE_MIN_POS_COUNT_DOC = "Do not sell goalie if less than n are in the current team ";

  public int AUTO_TRANSFER_GOALIE_MIN_POS_COUNT = 2;

  public String AUTO_TRANSFER_DEFENSE_MIN_POS_COUNT_DOC = "Do not sell defense if less than n are in the current team ";

  public int AUTO_TRANSFER_DEFENSE_MIN_POS_COUNT = 6;

  public String AUTO_TRANSFER_CENTER_MIN_POS_COUNT_DOC = "Do not sell center if less than n are in the current team ";

  public int AUTO_TRANSFER_CENTER_MIN_POS_COUNT = 3;

  public String AUTO_TRANSFER_WING_MIN_POS_COUNT_DOC = "Do not sell winger if less than n are in the current team ";

  public int AUTO_TRANSFER_WING_MIN_POS_COUNT = 6;

  public String AUTO_TRANSFER_GOALIE_MAX_POS_COUNT_DOC = "Sell goalie if more than n are in the current team ";

  public int AUTO_TRANSFER_GOALIE_MAX_POS_COUNT = 3;

  public String AUTO_TRANSFER_DEFENSE_MAX_POS_COUNT_DOC = "Sell defense if more than n are in the current team ";

  public int AUTO_TRANSFER_DEFENSE_MAX_POS_COUNT = 9;

  public String AUTO_TRANSFER_CENTER_MAX_POS_COUNT_DOC = "Sell center if more than n are in the current team ";

  public int AUTO_TRANSFER_CENTER_MAX_POS_COUNT = 5;

  public String AUTO_TRANSFER_WING_MAX_POS_COUNT_DOC = "Sell winger if more than n are in the current team ";

  public int AUTO_TRANSFER_WING_MAX_POS_COUNT = 9;

  public String AUTO_TRANSFER_MAX_COUNT_DOC = "Do not transfer (buy) more than n players/year from the transfer list ";

  public int AUTO_TRANSFER_MAX_COUNT = 5;

  public String SCENARIO_PROSPECTS_START_COUNT_DOC = "Prospects at game start count ";

  public int SCENARIO_PROSPECTS_START_COUNT = 20;

  public String SCENARIO_START_MAX_CONTRACT_YEARS_DOC = "Contracts for players are generated for  1 - X years ";

  public int SCENARIO_START_MAX_CONTRACT_YEARS = 4;

  public String PROSPECTS_MAX_WEEKLY_IMPROVEMENT_DOC = "Prospects max improvement per week ";

  public double PROSPECTS_MAX_WEEKLY_IMPROVEMENT = 0.25;

  public String PROSPECTS_MIN_AGE_DOC = "Prospects minimal age ";

  public int PROSPECTS_MIN_AGE = 12;

  public String PROSPECTS_MAX_AGE_DOC = "Prospects maximal age ";

  public int PROSPECTS_MAX_AGE = 19;

  public String PLAYER_CONTRACT_SALARY_MULTIPLIER_DOC = "Monthly salary multiplier (each one of 0 - 100 marketvaluepoints stay for x credits) ";

  public int PLAYER_CONTRACT_SALARY_MULTIPLIER = 4000;

  public String PLAYER_CONTRACT_TRANSFER_FEE_MULTIPLIER_DOC = "Salary multiplier (each one of 0 - 100 marketvaluepoints stay for x credits) ";

  public int PLAYER_CONTRACT_TRANSFER_FEE_MULTIPLIER = 2000;

  public String PLAYER_CONTRACT_FAIR_RANGE_DOC = "Percent a salary / transferfee may differ from the FAIR value ";

  public int PLAYER_CONTRACT_FAIR_RANGE = 5;

  public String INJURIES_COMMON_PER_YEAR_DOC = "How many injuries per team in one year ";

  public int INJURIES_COMMON_PER_YEAR = 15;

  public String INJURIES_AVERAGE_PER_YEAR_DOC = "How many injuries per team in one year ";

  public int INJURIES_AVERAGE_PER_YEAR = 5;

  public String INJURIES_RARE_PER_YEAR_DOC = "How many injuries per team in one year ";

  public int INJURIES_RARE_PER_YEAR = 2;

  public String INJURIES_MAX_PER_TEAM_DOC = "Max injured players per team at once ";

  public int INJURIES_MAX_PER_TEAM = 5;

  public String GLOBAL_IMPACTS_RANDOM_EVENTS_TEAM_YEAR_DOC = "Random events per year on user's Team ";

  public int GLOBAL_IMPACTS_RANDOM_EVENTS_TEAM_YEAR = 10;

  public String GLOBAL_IMPACTS_RANDOM_EVENTS_PLAYER_YEAR_DOC = "Random events per year on user's Player ";

  public int GLOBAL_IMPACTS_RANDOM_EVENTS_PLAYER_YEAR = 20;

  public String MATCH_PLAYER_ENERGY_MAX_DOC = "Max players match energy (reseted) ";

  public int MATCH_PLAYER_ENERGY_MAX = 100;

  public String MATCH_PLAYER_ENERGY_MINUTE_LOSS_DOC = "Players match energy loss by minute on ice (also dpendent on players overall energy) ";

  public int MATCH_PLAYER_ENERGY_MINUTE_LOSS = 5;

  public String MATCH_PLAYER_ENERGY_MINUTE_WIN_DOC = "Players match energy win by minute off ice (also dependent on players overall energy) ";

  public int MATCH_PLAYER_ENERGY_MINUTE_WIN = 3;

  public String SPONSORING_AMOUNT_RANGE_DOC = "How much % a offered sponsring amount may vary.";

  public int SPONSORING_AMOUNT_RANGE = 15;
  
  public String SPONSORING_NEGOTIATION_AMOUNT_INCREASE_DOC = "How much % an offer may increase while one negotiation round.";

  public int SPONSORING_NEGOTIATION_INCREASE_BONUS = 5;
  
  public String SPONSORING_NEGOTIATION_HAPPINESS_DECREASE_DOC = "How much % (to the 100%) the happienes of a company may decrease while one negotiation round.";

  public int SPONSORING_NEGOTIATION_HAPPINESS_DECREASE = 55;
  
  public String SPONSORING_TYPE_MAIN_VALUE_DOC = "Value/Multiplikator of a Sponsoring.";

  public int SPONSORING_TYPE_MAIN_VALUE = 10000;
  
  public String SPONSORING_TYPE_ARENA_BOARDS_VALUE_DOC = "Value/Multiplikator of Sponsoring.";

  public int SPONSORING_TYPE_ARENA_BOARD_VALUE = 1000;
  
  public String SPONSORING_TYPE_ARENA_ICE_VALUE_DOC = "Value/Multiplikator of a Sponsoring.";

  public int SPONSORING_TYPE_ARENA_ICE_VALUE = 1000;
  
  public String SPONSORING_TYPE_MEDIA_TV_VALUE_DOC = "Value/Multiplikator of a Sponsoring.";

  public int SPONSORING_TYPE_MEDIA_TV_VALUE = 5000;
  
  public String SPONSORING_TYPE_MEDIA_RADIO_VALUE_DOC = "Value/Multiplikator of a Sponsoring.";

  public int SPONSORING_TYPE_MEDIA_RADIO_VALUE = 2000;
  
  public String SPONSORING_TYPE_MEDIA_WEB_VALUE_DOC = "Value/Multiplikator of a Sponsoring.";

  public int SPONSORING_TYPE_MEDIA_WEB_VALUE = 1500;
  
  public String SPONSORING_TYPE_TEAM_MAIN_VALUE_DOC = "Value/Multiplikator of a Sponsoring.";

  public int SPONSORING_TYPE_TEAM_MAIN_VALUE = 5000;
  
  public String SPONSORING_TYPE_TEAM_STICK_VALUE_DOC = "Value/Multiplikator of a Sponsoring.";

  public int SPONSORING_TYPE_TEAM_STICK_VALUE = 500;
  
  public String SPONSORING_TYPE_TEAM_SKATES_VALUE_DOC = "Value/Multiplikator of a Sponsoring.";

  public int SPONSORING_TYPE_TEAM_SKATES_VALUE = 200;
  
  public String SPONSORING_TYPE_TEAM_EQUIPMENT_VALUE_DOC = "Value/Multiplikator of a Sponsoring.";

  public int SPONSORING_TYPE_TEAM_EQUIPMENT_VALUE = 2000;
  
  public String INFRASTRUCTURE_ARENA_COST_PER_SEAT_INCREASE_DOC = "Cost per Seat for category increase";

  public int INFRASTRUCTURE_ARENA_COST_PER_SEAT_INCREASE = 500;
  
  public String INFRASTRUCTURE_ARENA_COST_PER_SEAT_COMFORT_CHEAP_DOC = "Cost per Seat for cheap comfort";

  public int INFRASTRUCTURE_ARENA_COST_PER_SEAT_COMFORT_CHEAP = 150;
  
  public String INFRASTRUCTURE_ARENA_COST_PER_SEAT_COMFORT_NORMAL_DOC = "Cost per Seat for normal comfort";

  public int INFRASTRUCTURE_ARENA_COST_PER_SEAT_COMFORT_NORMAL = 250;
  
  public String INFRASTRUCTURE_ARENA_COST_PER_SEAT_COMFORT_DELUXE_DOC = "Cost per Seat for cheap comfort";

  public int INFRASTRUCTURE_ARENA_COST_PER_SEAT_COMFORT_DELUXE = 350;
  
  public String INFRASTRUCTURE_ARENA_COST_PER_SEAT_MAINTENANCE_DOC = "Cost per Seat and Percent for maintenace";

  public int INFRASTRUCTURE_ARENA_COST_PER_SEAT_MAINTENANCE = 5;
  
  public String INFRASTRUCTURE_ARENA_LOWER_CONDITION_DOC = "Condition lost by month";

  public int INFRASTRUCTURE_ARENA_LOWER_CONDITION = 1;
  
  public String MATCH_ENGINE_DOC = "Match Engine Class.";

  public String MATCH_ENGINE = "org.icehockeymanager.ihm.game.match.textengine.TextMatch";
  
  public String MATCH_USE_SIMPLEGUI_DOC = "Use the simple Gui 0 = no / 1 = yes";
  
  public int MATCH_USE_SIMPLEGUI = 0;
  
  
  /**
   * Creates the ScenarioSettings object, based on IhmSettingsXML
   * 
   */
  public ScenarioSettings() {

  }

  /**
   * Creates the ScenarioSettings object, based on IhmSettingsXML and reads the
   * settings from a file.
   * 
   * @param file
   *          XML File with settings
   * @throws Exception
   */
  public ScenarioSettings(File file) throws Exception {
    this.readSettingsFromXml(file);
  }

}
