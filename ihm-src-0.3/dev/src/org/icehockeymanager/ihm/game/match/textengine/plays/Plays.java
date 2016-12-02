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
  
package org.icehockeymanager.ihm.game.match.textengine.plays;

import org.icehockeymanager.ihm.game.*;
import org.icehockeymanager.ihm.game.match.textengine.*;
import org.icehockeymanager.ihm.game.match.textengine.data.*;
import org.icehockeymanager.ihm.game.player.*;
import org.icehockeymanager.ihm.game.player.fieldplayer.*;
import org.icehockeymanager.ihm.game.tactics.*;

/**
 * The Play class contains functions to play situations while a match scene.
 * <p>- Situations might be a FaceOff, Shoot on goal, movement with the puck
 * etc.
 * <p>- Plays.java also contains some functions for situation implementations.
 * 
 * @author Bernhard von Gunten
 * @created January 2005
 */
public class Plays {

  /**
   * Plas a face off in the current match. After the faceoff the MatchSnapshot
   * will be updated.
   * 
   * @param match
   */
  public static void playFaceOff(TextMatch match) {
    match.getCurrentMatchSnapshot().currentMomentum = MatchSnapshot.MOMENTUM_LOW;

    Player homeCenter = match.getCurrentMatchSnapshot().currentHomeBlock.getCenter();
    Player awayCenter = match.getCurrentMatchSnapshot().currentAwayBlock.getCenter();

    match.getCurrentMatchSnapshot().currentFaceOffData.setPlayerA(homeCenter);
    match.getCurrentMatchSnapshot().currentFaceOffData.setPlayerB(awayCenter);

    int faceOffValueHome = homeCenter.getPlayerAttributes().getSpecificPlayerAttribute(FieldPlayerAttributes.STICKHANDLING).getIntValue();
    int faceOffValueAway = awayCenter.getPlayerAttributes().getSpecificPlayerAttribute(FieldPlayerAttributes.STICKHANDLING).getIntValue();

    if (isValueOneWinner(faceOffValueHome, faceOffValueAway)) {
      match.getCurrentMatchSnapshot().currentFaceOffData.setWinnerA(true);

      match.getCurrentMatchSnapshot().currentPuckHolder = match.getCurrentMatchSnapshot().currentHomeBlock.getRandomOtherFieldPlayer(homeCenter);
      match.getCurrentMatchSnapshot().currentPuckHoldingBlock = match.getCurrentMatchSnapshot().currentHomeBlock;
    } else {
      match.getCurrentMatchSnapshot().currentFaceOffData.setWinnerA(false);

      match.getCurrentMatchSnapshot().currentPuckHolder = match.getCurrentMatchSnapshot().currentAwayBlock.getRandomOtherFieldPlayer(awayCenter);
      match.getCurrentMatchSnapshot().currentPuckHoldingBlock = match.getCurrentMatchSnapshot().currentAwayBlock;
    }

    match.getCurrentMatchSnapshot().currentFaceOffData.setPuckHolder(match.getCurrentMatchSnapshot().currentPuckHolder);
    match.getCurrentMatchSnapshot().currentFaceOffData.setSection(match.getCurrentMatchSnapshot().currentRelativeSectionHelper);

    match.getCurrentMatchSnapshot().currentPuckInPlay = true;

  }

  /**
   * Plays the next Situation and returns a boolean if match is suspended.
   * 
   * @param match
   *          Current match
   * @return True if match is suspended.
   */
  public static boolean playNextSituation(TextMatch match) {

    match.getCurrentMatchSnapshot().currentSituationData.setPuckHolder(match.getCurrentMatchSnapshot().currentPuckHolder);

    if (match.getCurrentMatchSnapshot().currentPuckHoldingBlock.isDefensePlayer(match.getCurrentMatchSnapshot().currentPuckHolder)) {
      return PlaysDefense.playDefenseSituation(match);
    } else if (match.getCurrentMatchSnapshot().currentPuckHoldingBlock.isWingPlayer(match.getCurrentMatchSnapshot().currentPuckHolder)) {
      return PlaysDefense.playDefenseSituation(match);
    } else if (match.getCurrentMatchSnapshot().currentPuckHoldingBlock.isCenterPlayer(match.getCurrentMatchSnapshot().currentPuckHolder)) {
      return PlaysDefense.playDefenseSituation(match);
    } else if (match.getCurrentMatchSnapshot().currentPuckHoldingBlock.isGoaliePlayer(match.getCurrentMatchSnapshot().currentPuckHolder)) {
      return PlaysDefense.playDefenseSituation(match);
    }

    return true;
  }

  /**
   * Simple compairson between two values on random base.
   * 
   * @param value1
   * @param value2
   * @return True if value1 is winner.
   */
  protected static boolean isValueOneWinner(int value1, int value2) {
    int r = GameController.getInstance().getScenario().getRandomInt(0, value1 + value2);
    return (r <= value1);
  }

  /**
   * Simulates a shoot on a goal. Returns boolean if match is suspended.
   * 
   * @param match
   *          Current match
   * @param riskLevel
   *          Risk level
   * @param targetSection
   *          Relative section
   * @return True if match is suspended.
   */
  protected static boolean standardShootOnGoal(TextMatch match, int riskLevel, int targetSection) {
    Player opponent = match.getCurrentMatchSnapshot().getOpponentsBlock().getGoalie();
    match.getCurrentMatchSnapshot().currentSituationData.setOpponent(opponent);

    int getValue = match.getCurrentMatchSnapshot().currentPuckHolder.getPlayerAttributes().getSpecificPlayerAttribute(FieldPlayerAttributes.SHOOTING).getIntValue();

    // TDOO : Fix the * 10 "bug" !!
    int interceptionValue = opponent.getPlayerAttributes().getCommonPlayerAttributesAverage() * 4;
    if (isValueOneWinner(getValue, interceptionValue)) {

      match.getCurrentMatchSnapshot().currentSituationData.setResult(MatchDataSituation.RESULT_SUCCSESSFULL_NO_PROB);
      match.getCurrentMatchSnapshot().actionSuccessfull(match.getCurrentMatchSnapshot().currentPuckHolder, riskLevel, targetSection);
      match.goalScored();
      return true;
    } else {

      int r = GameController.getInstance().getScenario().getRandomInt(0, 1);
      if (r == 0) {
        match.getCurrentMatchSnapshot().actionFailed(opponent, riskLevel, targetSection);
        match.getCurrentMatchSnapshot().currentSituationData.setResult(MatchDataSituation.RESULT_DRAW);
        return true;
      } else {
        Player opponent2 = match.getCurrentMatchSnapshot().getOpponentsBlock().getRandomDefensiveePlayer();
        match.getCurrentMatchSnapshot().currentSituationData.setResult(MatchDataSituation.RESULT_FAILED);
        match.getCurrentMatchSnapshot().actionFailed(opponent2, riskLevel, targetSection);
        return false;

      }
    }
  }

  /**
   * Simulates a shoot of the puck into another section
   * 
   * @param match
   *          Current match
   * @param riskLevel
   *          Risk level
   * @param targetSection
   *          Relative section
   * @return True if match is suspended.
   */
  protected static boolean standardShootPuckIntoSection(TextMatch match, int riskLevel, int targetSection) {
    Player opponent = match.getCurrentMatchSnapshot().getOpponentsBlock().getRandomPlayer();
    match.getCurrentMatchSnapshot().currentSituationData.setOpponent(opponent);

    Player otherPlayer = match.getCurrentMatchSnapshot().currentPuckHoldingBlock.getRandomOtherFieldPlayer(match.getCurrentMatchSnapshot().currentPuckHolder);
    match.getCurrentMatchSnapshot().currentSituationData.setPartner(otherPlayer);

    int getValue = otherPlayer.getPlayerAttributes().getSpecificPlayerAttribute(FieldPlayerAttributes.SKATING).getIntValue();
    int interceptionValue = opponent.getPlayerAttributes().getSpecificPlayerAttribute(FieldPlayerAttributes.SKATING).getIntValue();
    boolean easy = easyPlay(match, riskLevel);
    boolean winner = isValueOneWinner(getValue, interceptionValue);

    if (easy) {
      match.getCurrentMatchSnapshot().currentSituationData.setResult(MatchDataSituation.RESULT_SUCCSESSFULL_NO_PROB);
      match.getCurrentMatchSnapshot().actionSuccessfull(otherPlayer, riskLevel, targetSection);
      return false;

    } else {
      if (winner) {
        match.getCurrentMatchSnapshot().currentSituationData.setResult(MatchDataSituation.RESULT_SUCCSESSFULL_PROB);
        match.getCurrentMatchSnapshot().actionSuccessfull(otherPlayer, riskLevel, targetSection);
        return false;

      } else {
        // Failed
        match.getCurrentMatchSnapshot().actionFailed(opponent, riskLevel, targetSection);

        int r = GameController.getInstance().getScenario().getRandomInt(0, 1);

        if (r == 0) {
          match.getCurrentMatchSnapshot().currentSituationData.setResult(MatchDataSituation.RESULT_DRAW);
          return true;
        } else {
          match.getCurrentMatchSnapshot().currentSituationData.setResult(MatchDataSituation.RESULT_FAILED);
          return false;
        }
      }
    }
  }

  /**
   * Simulates a pass
   * 
   * @param match
   *          Current match
   * @param receiver
   *          Puck receiver
   * @param riskLevel
   *          Risk level
   * @param targetSection
   *          Relative section
   * @return True if match is suspended.
   */
  protected static boolean standardPass(TextMatch match, Player receiver, int riskLevel, int targetSection) {
    Player opponent = match.getCurrentMatchSnapshot().getOpponentsBlock().getRandomPlayer();
    match.getCurrentMatchSnapshot().currentSituationData.setOpponent(opponent);

    int passValue = match.getCurrentMatchSnapshot().currentPuckHolder.getPlayerAttributes().getSpecificPlayerAttribute(FieldPlayerAttributes.PASSING).getIntValue();
    int interceptionValue = opponent.getPlayerAttributes().getSpecificPlayerAttribute(FieldPlayerAttributes.POSITIONING).getIntValue();
    boolean easy = easyPlay(match, riskLevel);
    boolean winner = isValueOneWinner(passValue, interceptionValue);

    if (easy) {
      match.getCurrentMatchSnapshot().currentSituationData.setResult(MatchDataSituation.RESULT_SUCCSESSFULL_NO_PROB);
      match.getCurrentMatchSnapshot().actionSuccessfull(receiver, riskLevel, targetSection);
      return false;
    } else {
      if (winner) {
        match.getCurrentMatchSnapshot().currentSituationData.setResult(MatchDataSituation.RESULT_SUCCSESSFULL_PROB);
        match.getCurrentMatchSnapshot().actionSuccessfull(receiver, riskLevel, targetSection);
        return false;
      } else {
        match.getCurrentMatchSnapshot().actionFailed(opponent, riskLevel, targetSection);
        // Intercepted, but puck leaves the field
        int r = GameController.getInstance().getScenario().getRandomInt(0, 1);
        if (r == 0) {
          match.getCurrentMatchSnapshot().currentSituationData.setResult(MatchDataSituation.RESULT_DRAW);
          return true;
        } else {
          match.getCurrentMatchSnapshot().currentSituationData.setResult(MatchDataSituation.RESULT_FAILED);
          return false;
        }

      }
    }

  }

  /**
   * Simulates a move with the puck into another section.
   * 
   * @param match
   *          Current match
   * @param riskLevel
   *          Risk level
   * @param targetSection
   *          Relative section
   * @return True if match is suspended.
   */
  protected static boolean standardMoveWithPuck(TextMatch match, int riskLevel, int targetSection) {
    Player opponent = match.getCurrentMatchSnapshot().getOpponentsBlock().getRandomPlayer();
    match.getCurrentMatchSnapshot().currentSituationData.setOpponent(opponent);

    int skatingValue = match.getCurrentMatchSnapshot().currentPuckHolder.getPlayerAttributes().getSpecificPlayerAttribute(FieldPlayerAttributes.SKATING).getIntValue();
    int interceptionValue = opponent.getPlayerAttributes().getSpecificPlayerAttribute(FieldPlayerAttributes.CHECKING).getIntValue();

    boolean easy = easyPlay(match, riskLevel);
    boolean winner = isValueOneWinner(skatingValue, interceptionValue);

    if (easy) {
      match.getCurrentMatchSnapshot().currentSituationData.setResult(MatchDataSituation.RESULT_SUCCSESSFULL_PROB);
      match.getCurrentMatchSnapshot().actionSuccessfull(match.getCurrentMatchSnapshot().currentPuckHolder, riskLevel, targetSection);
      return false;
    } else {
      if (winner) {
        match.getCurrentMatchSnapshot().currentSituationData.setResult(MatchDataSituation.RESULT_SUCCSESSFULL_PROB);
        match.getCurrentMatchSnapshot().actionSuccessfull(match.getCurrentMatchSnapshot().currentPuckHolder, riskLevel, targetSection);
        return false;
      } else {
        match.getCurrentMatchSnapshot().actionFailed(opponent, riskLevel, targetSection);
        // Intercepted, but puck leaves the field
        int r = GameController.getInstance().getScenario().getRandomInt(0, 1);
        if (r == 0) {
          match.getCurrentMatchSnapshot().currentSituationData.setResult(MatchDataSituation.RESULT_DRAW);
          return true;
        } else {
          match.getCurrentMatchSnapshot().currentSituationData.setResult(MatchDataSituation.RESULT_FAILED);
          return false;
        }
      }
    }
  }

  /**
   * Simple function to check if planed move is simple (based on the selected
   * risk level and random generator)
   * 
   * @param match
   *          Current match
   * @param risk
   *          Selected risk level
   * @return True if easy to play
   */
  public static boolean easyPlay(TextMatch match, int risk) {
    int riskFactor = 0;
    if (risk == Tactics.RISK_HIGH) {
      riskFactor = 75;
    } else if (risk == Tactics.RISK_MODERATE) {
      riskFactor = 50;
    } else {
      riskFactor = 25;
    }

    if (match.getCurrentMatchSnapshot().currentMomentum == MatchSnapshot.MOMENTUM_HIGH) {
      riskFactor -= 50;
    }
    if (match.getCurrentMatchSnapshot().currentMomentum == MatchSnapshot.MOMENTUM_MODERATE) {
      riskFactor -= 25;
    }

    int r = GameController.getInstance().getScenario().getRandomInt(0, 100);

    return (r > riskFactor);
  }

}
