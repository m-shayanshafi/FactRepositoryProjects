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

import org.icehockeymanager.ihm.game.match.textengine.*;
import org.icehockeymanager.ihm.game.match.textengine.data.*;
import org.icehockeymanager.ihm.game.player.*;
import org.icehockeymanager.ihm.game.tactics.*;

/**
 * PlaysDefense simulates all possible actions for a defense player in each
 * section (own, neutral, opponent) and every risk level (low, moderate, high).
 * 
 * @author Bernhard von Gunten & Arik Dasen
 * @created January 2005
 */
public class PlaysDefense {

  /**
   * Plays a defense situation based on risk level and current section
   * 
   * @param match
   *          Current match
   * @return True if game is suspended
   */
  protected static boolean playDefenseSituation(TextMatch match) {
    int riskLevel = match.getCurrentMatchSnapshot().currentPuckHolder.getTeam().getTactics().getRiskLevel(match);
    match.getCurrentMatchSnapshot().currentSituationData.setRiskLevel(riskLevel);

    // Depends on Secion we're in
    if (match.getCurrentMatchSnapshot().currentRelativeSectionHelper == MatchSnapshot.SECTION_OWN) {

      if (riskLevel == Tactics.RISK_LOW) {
        // Pass to other defense player, may be intercepted by opponent player
        // (low risk)
        match.getCurrentMatchSnapshot().currentSituationData.setSituationKey(MatchDataSituation.SITUATION_DEFENSE_OWN_PASS_TO_DEFENSEPLAYER);
        Player to = match.getCurrentMatchSnapshot().currentPuckHoldingBlock.getOtherDefender(match.getCurrentMatchSnapshot().currentPuckHolder);
        match.getCurrentMatchSnapshot().currentSituationData.setPartner(to);
        return Plays.standardPass(match, to, riskLevel, match.getCurrentMatchSnapshot().currentRelativeSectionHelper);
      } else if (riskLevel == Tactics.RISK_MODERATE) {
        // Move to neutral section with puck (moderate risk)
        match.getCurrentMatchSnapshot().currentSituationData.setSituationKey(MatchDataSituation.SITUATION_DEFENSE_OWN_MOVE_TO_SECTION);
        return Plays.standardMoveWithPuck(match, riskLevel, MatchSnapshot.SECTION_NEUTRAL);
      } else if (riskLevel == Tactics.RISK_HIGH) {
        // Pass to offensive player to the opponents section, may be intercepted
        // by opponent player
        // (high risk)
        match.getCurrentMatchSnapshot().currentSituationData.setSituationKey(MatchDataSituation.SITUATION_DEFENSE_OWN_PASS_TO_OFFENSEPLAYER);
        Player to = match.getCurrentMatchSnapshot().currentPuckHoldingBlock.getRandomOffensivePlayer();
        match.getCurrentMatchSnapshot().currentSituationData.setPartner(to);
        return Plays.standardPass(match, to, riskLevel, MatchSnapshot.SECTION_OPPONENT);
      }

    } else if (match.getCurrentMatchSnapshot().currentRelativeSectionHelper == MatchSnapshot.SECTION_NEUTRAL) {

      if (riskLevel == Tactics.RISK_LOW) {

        // Pass to other player in same section, may be intercepted by opponent
        // player (low risk)
        match.getCurrentMatchSnapshot().currentSituationData.setSituationKey(MatchDataSituation.SITUATION_DEFENSE_NEUTRAL_PASS_TO_PLAYER);
        Player to = match.getCurrentMatchSnapshot().currentPuckHoldingBlock.getRandomOtherFieldPlayer(match.getCurrentMatchSnapshot().currentPuckHolder);
        match.getCurrentMatchSnapshot().currentSituationData.setPartner(to);
        return Plays.standardPass(match, to, riskLevel, match.getCurrentMatchSnapshot().currentRelativeSectionHelper);
      } else if (riskLevel == Tactics.RISK_MODERATE) {

        // Shoot the puck into opponents section (moderate risk)
        match.getCurrentMatchSnapshot().currentSituationData.setSituationKey(MatchDataSituation.SITUATION_DEFENSE_NEUTRAL_SHOT_INTO_OPPONENTS_SECTION);
        return Plays.standardShootPuckIntoSection(match, riskLevel, MatchSnapshot.SECTION_OPPONENT);
      } else if (riskLevel == Tactics.RISK_HIGH) {

        // Move to opponents section with puck (high risk as a defense)
        match.getCurrentMatchSnapshot().currentSituationData.setSituationKey(MatchDataSituation.SITUATION_DEFENSE_NEUTRAL_MOVE_INTO_OPPONENTS_SECTION);
        return Plays.standardMoveWithPuck(match, riskLevel, MatchSnapshot.SECTION_OPPONENT);
      }

    } else if (match.getCurrentMatchSnapshot().currentRelativeSectionHelper == MatchSnapshot.SECTION_OPPONENT) {

      if (riskLevel == Tactics.RISK_LOW) {
        // Passing in opponents section
        match.getCurrentMatchSnapshot().currentSituationData.setSituationKey(MatchDataSituation.SITUATION_DEFENSE_OPPONENT_PASS_TO_PLAYER);

        Player to = match.getCurrentMatchSnapshot().currentPuckHoldingBlock.getRandomOtherFieldPlayer(match.getCurrentMatchSnapshot().currentPuckHolder);
        match.getCurrentMatchSnapshot().currentSituationData.setPartner(to);
        return Plays.standardPass(match, to, riskLevel, match.getCurrentMatchSnapshot().currentRelativeSectionHelper);
      } else if (riskLevel == Tactics.RISK_MODERATE || riskLevel == Tactics.RISK_HIGH) {

        // Shoot on goal (moderate risk)
        match.getCurrentMatchSnapshot().currentSituationData.setSituationKey(MatchDataSituation.SITUATION_DEFENSE_OPPONENT_SHOOT_ON_GOAL);
        return Plays.standardShootOnGoal(match, riskLevel, MatchSnapshot.SECTION_OPPONENT);
      } else if (riskLevel == Tactics.RISK_HIGH) {

        // Move closer to the goal and try to score
        match.getCurrentMatchSnapshot().currentSituationData.setSituationKey(MatchDataSituation.SITUATION_DEFENSE_OPPONENT_MOVE_CLOSER_SCORE);
        return Plays.standardShootOnGoal(match, riskLevel, MatchSnapshot.SECTION_OPPONENT);
      }

    }

    return true;
  }

}
