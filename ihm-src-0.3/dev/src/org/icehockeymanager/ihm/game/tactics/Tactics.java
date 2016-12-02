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
  
package org.icehockeymanager.ihm.game.tactics;

import java.io.*;

import org.icehockeymanager.ihm.game.team.*;
import org.icehockeymanager.ihm.game.player.*;
import org.icehockeymanager.ihm.game.match.*;
import org.icehockeymanager.ihm.game.*;
import org.icehockeymanager.ihm.game.player.goalie.*;
import org.icehockeymanager.ihm.game.player.fieldplayer.*;

/**
 * Tactics contains the match setup (blocks) and the tactis for the game.
 * 
 * @author Bernhard von Gunten & Arik Dasen
 * @created January 2005
 */
public class Tactics implements Serializable {

  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 3256727281719259443L;

  /** RISK_LOW */
  public static final int RISK_LOW = 1;

  /** RISK_MODERATE */
  public static final int RISK_MODERATE = 2;

  /** RISK_HIGH */
  public static final int RISK_HIGH = 3;

  /** Match setup */
  private Block[] blocks = null;

  /** Team */
  private Team team = null;

  /**
   * Constructs the tactics for a team
   * 
   * @param team
   */
  public Tactics(Team team) {
    this.team = team;
  }

  /**
   * Returns the match setup
   * 
   * @return Blocks
   */
  public Block[] getBlocks() {
    this.blocks = aiGenerateBlocks(team);
    return blocks;
  }

  /**
   * Sets the match setup
   * 
   * @param blocks
   */
  public void setBlocks(Block[] blocks) {
    this.blocks = blocks;
  }

  /**
   * Get next block for the match
   * 
   * @param match
   * @return Block
   */
  public Block getNextBlock(Match match) {
    this.blocks = aiGenerateBlocks(team);
    // May be HEAVILY improved ;-)
    return Block.getRandomBlock(blocks);
  }

  /**
   * Returns the selectd risk for the next played situation
   * 
   * @param match
   * @return Risklevel
   */
  public int getRiskLevel(Match match) {
    int r = GameController.getInstance().getScenario().getRandomInt(0, 2);
    if (r == 0)
      return RISK_LOW;
    if (r == 1)
      return RISK_MODERATE;
    return RISK_HIGH;
  }

  /**
   * AI Function. Generates 4 blocks of players.
   * 
   * @param team
   * @return Array of blocks
   */
  public static Block[] aiGenerateBlocks(Team team) {
    Block[] blocks = new Block[4];

    // Get goalie
    Player[] goalies = team.getPlayersByPosition(PlayerAttributes.POSITION_GOALTENDING);
    Player.sortPlayerArray(goalies, Player.SORT_ATTRIBUTES_TOTAL_AVERAGE, false);
    Goalie mainGoalie = null;
    if (goalies.length > 0) {
      mainGoalie = (Goalie) goalies[0];
    }

    // Get Defense players
    Player[] defense = team.getPlayersByPosition(PlayerAttributes.POSITION_DEFENSE);
    Player.sortPlayerArray(goalies, Player.SORT_ATTRIBUTES_TOTAL_AVERAGE, false);

    // Get Wing players
    Player[] wings = team.getPlayersByPosition(PlayerAttributes.POSITION_WING);
    Player.sortPlayerArray(goalies, Player.SORT_ATTRIBUTES_TOTAL_AVERAGE, false);

    // Get Center players
    Player[] centers = team.getPlayersByPosition(PlayerAttributes.POSITION_CENTER);
    Player.sortPlayerArray(goalies, Player.SORT_ATTRIBUTES_TOTAL_AVERAGE, false);

    int defensePos = 0;
    int wingPos = 0;
    int centerPos = 0;

    for (int i = 0; i < 4; i++) {
      blocks[i] = new Block(team);
      blocks[i].setGoalie(mainGoalie);

      blocks[i].setRightDefender((FieldPlayer) defense[defensePos]);
      if (defensePos++ >= defense.length) {
        defensePos = 0;
      }

      blocks[i].setLeftDefender((FieldPlayer) defense[defensePos]);
      if (defensePos++ >= defense.length) {
        defensePos = 0;
      }

      blocks[i].setLeftWing((FieldPlayer) wings[wingPos]);
      if (wingPos++ >= wings.length) {
        wingPos = 0;
      }

      blocks[i].setRightWing((FieldPlayer) wings[wingPos]);
      if (wingPos++ >= wings.length) {
        wingPos = 0;
      }

      blocks[i].setCenter((FieldPlayer) centers[centerPos]);
      if (centerPos++ >= centers.length) {
        centerPos = 0;
      }

    }

    return blocks;

  }

}
