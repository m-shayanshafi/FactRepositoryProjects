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
  
package org.icehockeymanager.ihm.game.match.textengine.data;

import org.icehockeymanager.ihm.game.player.*;
import java.io.*;

/**
 * MatchDataSituation contains all informations about a played situation.
 * <p>
 * Each situation contains one puck holder (player who tries to do a step), one
 * partner of the puckholder, one opponent player, and other informations
 * (result, section etc.).
 * 
 * @author Bernhard von Gunten
 * @created January 2005
 */
public class MatchDataSituation implements Serializable {
  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 3257289145096484919L;

  /** SITUATION_DEFENSE_OWN_PASS_TO_DEFENSEPLAYER */
  public static final String SITUATION_DEFENSE_OWN_PASS_TO_DEFENSEPLAYER = "situation.defense_own_pass_to_defenseplayer";

  /** SITUATION_DEFENSE_OWN_MOVE_TO_SECTION */
  public static final String SITUATION_DEFENSE_OWN_MOVE_TO_SECTION = "situation.defense_own_move_to_section";

  /** SITUATION_DEFENSE_OWN_PASS_TO_OFFENSEPLAYER */
  public static final String SITUATION_DEFENSE_OWN_PASS_TO_OFFENSEPLAYER = "situation.defense_own_pass_to_offenseplayer";

  /** SITUATION_DEFENSE_NEUTRAL_PASS_TO_PLAYER */
  public static final String SITUATION_DEFENSE_NEUTRAL_PASS_TO_PLAYER = "situation.defense_neutral_pass_to_player";

  /** SITUATION_DEFENSE_NEUTRAL_SHOT_INTO_OPPONENTS_SECTION */
  public static final String SITUATION_DEFENSE_NEUTRAL_SHOT_INTO_OPPONENTS_SECTION = "situation.defense_neutral_shot_into_opponents_section";

  /** SITUATION_DEFENSE_NEUTRAL_MOVE_INTO_OPPONENTS_SECTION */
  public static final String SITUATION_DEFENSE_NEUTRAL_MOVE_INTO_OPPONENTS_SECTION = "situation.defense_neutral_move_into_opponents_section";

  /** SITUATION_DEFENSE_OPPONENT_PASS_TO_PLAYER */
  public static final String SITUATION_DEFENSE_OPPONENT_PASS_TO_PLAYER = "situation.defense_opponent_pass_to_player";

  /** SITUATION_DEFENSE_OPPONENT_SHOOT_ON_GOAL */
  public static final String SITUATION_DEFENSE_OPPONENT_SHOOT_ON_GOAL = "situation.defense_opponent_shoot_on_goal";

  /** SITUATION_DEFENSE_OPPONENT_MOVE_CLOSER_SCORE */
  public static final String SITUATION_DEFENSE_OPPONENT_MOVE_CLOSER_SCORE = "situation.defense_opponent_move_closer_score";

  /** RESULT_SUCCSESSFULL_NO_PROB */
  public static final int RESULT_SUCCSESSFULL_NO_PROB = 1;

  /** RESULT_SUCCSESSFULL_PROB */
  public static final int RESULT_SUCCSESSFULL_PROB = 2;

  /** RESULT_FAILED */
  public static final int RESULT_FAILED = 3;

  /** RESULT_DRAW */
  public static final int RESULT_DRAW = 4;

  /** The key of the situation */
  private String situationKey;

  /** Result of the situation */
  private int result;

  /** Puck holder */
  private Player puckHolder;

  /** Partner of the puck holder */
  private Player partner;

  /** Opponent player */
  private Player opponent;

  /** Risk level of the situation */
  private int riskLevel;

  /** Section the situation ended */
  private int targetSection;

  /** MatchDataSituation Constructor */
  public MatchDataSituation() {
  }

  /**
   * Returns the opponent player
   * 
   * @return Opponent
   */
  public Player getOpponent() {
    return opponent;
  }

  /**
   * Sets the opponent player
   * 
   * @param opponent
   */
  public void setOpponent(Player opponent) {
    this.opponent = opponent;
  }

  /**
   * Returns the partner of the puck holder
   * 
   * @return partner
   */
  public Player getPartner() {
    return partner;
  }

  /**
   * Sets the partner of the puck holder
   * 
   * @param partner
   */
  public void setPartner(Player partner) {
    this.partner = partner;
  }

  /**
   * Returns the puck holder
   * 
   * @return Puck holder
   */
  public Player getPuckHolder() {
    return puckHolder;
  }

  /**
   * Sets the puck holder
   * 
   * @param puckHolder
   */
  public void setPuckHolder(Player puckHolder) {
    this.puckHolder = puckHolder;
  }

  /**
   * Returns the result of the situation
   * 
   * @return Result
   */
  public int getResult() {
    return result;
  }

  /**
   * Sets the result of the situation
   * 
   * @param result
   */
  public void setResult(int result) {
    this.result = result;
  }

  /**
   * Returns the Situation key
   * 
   * @return Situationkey
   */
  public String getSituationKey() {
    return situationKey;
  }

  /**
   * Sets the situation key
   * 
   * @param situationKey
   */
  public void setSituationKey(String situationKey) {
    this.situationKey = situationKey;
  }

  /**
   * Returns the risk level of the situation
   * 
   * @return Risk level
   */
  public int getRiskLevel() {
    return riskLevel;
  }

  /**
   * Sets the risk level of the situation
   * 
   * @param riskLevel
   */
  public void setRiskLevel(int riskLevel) {
    this.riskLevel = riskLevel;
  }

  /**
   * Returns the relative target section after the situation
   * 
   * @return Section
   */
  public int getTargetSection() {
    return targetSection;
  }

  /**
   * Sets the relative target section
   * 
   * @param targetSection
   */
  public void setTargetSection(int targetSection) {
    this.targetSection = targetSection;
  }

}
