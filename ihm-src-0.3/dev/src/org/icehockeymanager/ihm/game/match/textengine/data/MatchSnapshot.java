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

import java.io.*;

import org.icehockeymanager.ihm.game.league.*;
import org.icehockeymanager.ihm.game.player.*;
import org.icehockeymanager.ihm.game.tactics.*;

/**
 * The MatchSnapshot class contains all informatiosn about the current scene.
 * There is one snapshot for the whole game, so much of the informations get
 * updates often.
 * 
 * @author Bernhard von Gunten
 * @created January 2005
 */
public class MatchSnapshot implements Serializable {

  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 3760847874019570992L;

  /** Regular eriod */
  public static final int MATCH_STATE_REGULAR = 1;

  /** Overtime period */
  public static final int MATCH_STATE_OVERTIME = 2;

  /** Shootout */
  public static final int MATCH_STATE_SHOOTOUT = 3;

  /** Relative position : own section */
  public static final int SECTION_OWN = 1;

  /** Relative positon : neutral section */
  public static final int SECTION_NEUTRAL = 2;

  /** Relative position : opponents section */
  public static final int SECTION_OPPONENT = 3;

  /** Momentum low */
  public static final int MOMENTUM_LOW = 1;

  /** Momentum moderate */
  public static final int MOMENTUM_MODERATE = 2;

  /** Momentum high */
  public static final int MOMENTUM_HIGH = 3;

  /** Current home block */
  public Block currentHomeBlock = null;

  /** Current away block */
  public Block currentAwayBlock = null;

  /** Seconds played in the match */
  public int currentSeconds = 0;

  /** Current puck holder */
  public Player currentPuckHolder = null;

  /** Current puck holding block */
  public Block currentPuckHoldingBlock = null;

  /** Current relative section */
  public int currentRelativeSectionHelper = SECTION_NEUTRAL;

  /** Current momentum */
  public int currentMomentum = MOMENTUM_LOW;

  /** Current match state */
  public int currentState = MATCH_STATE_REGULAR;

  /** Current period counter */
  public int currentPeriod = 0;

  /** Current overtime period counter */
  public int currentOvertimePeriod = 0;

  /** Flag, if current period has been started */
  public boolean currentPeriodStarted = false;

  /** Flag, if puck is in play */
  public boolean currentPuckInPlay = false;

  /** The last puck chain */
  public Player[] currentPuckChain = new Player[2];

  /** The next break (period end) in seconds */
  public int currentNextBreak = 0;

  /** The current faceoff data */
  public MatchDataFaceOff currentFaceOffData = null;

  /** The current situation data */
  public MatchDataSituation currentSituationData = null;

  /** Flag, if score has changed */
  public boolean scoreChange = false;

  /** Initializes the game 
   * @param rules */
  public void initGame(Rules rules) {
    currentNextBreak = rules.secondsPerPeriod();
  }

  /** Initializes the scene */
  public void initScene() {
    scoreChange = false;
  }

  /** Finishes a scene */
  public void finishScene() {
    currentPuckInPlay = false;
  }

  /** Starts the current period */
  public void startCurrentPeriod() {
    currentPeriodStarted = true;
  }

  /**
   * A situation has been succesfully done by a team.
   * 
   * @param newPlayer
   *          The puckholder after the situation
   * @param risk
   *          The current risk level
   * @param targetSection
   *          The relative section of the puck after the situation
   */
  public void actionSuccessfull(Player newPlayer, int risk, int targetSection) {
    currentPuckHolder = newPlayer;
    currentRelativeSectionHelper = targetSection;

    if (risk == Tactics.RISK_LOW) {
      currentMomentum = MOMENTUM_LOW;
    } else if (risk == Tactics.RISK_MODERATE) {
      currentMomentum = MOMENTUM_MODERATE;
    } else if (risk == Tactics.RISK_HIGH) {
      currentMomentum = MOMENTUM_HIGH;
    }

    currentPuckChain[1] = currentPuckChain[0];
    currentPuckChain[0] = currentPuckHolder;

  }

  /**
   * A situation has failed
   * 
   * @param newPlayer
   *          The puckholder after the situation
   * @param risk
   *          The current risk level
   * @param targetSection
   *          The relative section of the puck after the situation
   */
  public void actionFailed(Player newPlayer, int risk, int targetSection) {
    currentPuckHolder = newPlayer;
    currentPuckHoldingBlock = this.getOpponentsBlock();
    currentRelativeSectionHelper = switchRelativeSection(targetSection);

    if (risk == Tactics.RISK_MODERATE || risk == Tactics.RISK_HIGH) {
      currentMomentum = MOMENTUM_MODERATE;
    } else {
      currentMomentum = MOMENTUM_LOW;
    }
    currentPuckChain = new Player[3];
  }

  /** Returns the current Block of the opponent 
   * @return  Block */
  public Block getOpponentsBlock() {
    if (currentPuckHoldingBlock.equals(currentHomeBlock)) {
      return currentAwayBlock;
    } else {
      return currentHomeBlock;
    }
  }

  /** Initializes the field */
  public void initField() {
    currentPuckInPlay = false;
    currentRelativeSectionHelper = MatchSnapshot.SECTION_NEUTRAL;
    currentMomentum = MatchSnapshot.MOMENTUM_LOW;
  }

  /** Switches side relative 
   * @param section 
   * @return switched side */
  private int switchRelativeSection(int section) {
    if (section == MatchSnapshot.SECTION_OWN) {
      return MatchSnapshot.SECTION_OPPONENT;
    } else if (section == MatchSnapshot.SECTION_OPPONENT) {
      return MatchSnapshot.SECTION_OWN;
    } else {
      return MatchSnapshot.SECTION_NEUTRAL;
    }
  }

  /** Clone this object 
   * @return Object clone of MatchSnapshot 
   * @throws CloneNotSupportedException */
  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }

  /** Go to overtime 
   * @param rules */
  public void goToOvertime(Rules rules) {
    currentState = MATCH_STATE_OVERTIME;
    currentPeriodStarted = false;
    currentNextBreak += rules.secondsPerOvertime();
    initField();
  }

  /** Finish period 
   * @param rules */
  public void finishPeriod(Rules rules) {
    if (currentState == MATCH_STATE_REGULAR) {
      currentPeriod++;
      currentPeriodStarted = false;
      currentNextBreak += rules.secondsPerPeriod();
      initField();
    } else if (currentState == MATCH_STATE_OVERTIME) {
      currentOvertimePeriod++;
      currentPeriodStarted = false;
      currentNextBreak += rules.secondsPerOvertime();
      initField();
    }
  }

  /**
   * @return Returns the currentAwayBlock.
   */
  public Block getCurrentAwayBlock() {
    return currentAwayBlock;
  }

  /**
   * @param currentAwayBlock
   *          The currentAwayBlock to set.
   */
  public void setCurrentAwayBlock(Block currentAwayBlock) {
    this.currentAwayBlock = currentAwayBlock;
  }

  /**
   * @return Returns the currentHomeBlock.
   */
  public Block getCurrentHomeBlock() {
    return currentHomeBlock;
  }

  /**
   * @param currentHomeBlock
   *          The currentHomeBlock to set.
   */
  public void setCurrentHomeBlock(Block currentHomeBlock) {
    this.currentHomeBlock = currentHomeBlock;
  }

  /**
   * @return Returns the currentPuckInPlay.
   */
  public boolean isCurrentPuckInPlay() {
    return currentPuckInPlay;
  }

  /**
   * @param currentPuckInPlay
   *          The currentPuckInPlay to set.
   */
  public void setCurrentPuckInPlay(boolean currentPuckInPlay) {
    this.currentPuckInPlay = currentPuckInPlay;
  }
}