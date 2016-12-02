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

import org.icehockeymanager.ihm.game.player.fieldplayer.*;
import org.icehockeymanager.ihm.game.player.goalie.*;
import org.icehockeymanager.ihm.game.player.*;
import org.icehockeymanager.ihm.game.team.*;
import org.icehockeymanager.ihm.game.*;
import org.icehockeymanager.ihm.lib.*;
import java.util.*;

/**
 * Block class
 * 
 * @author Arik Dasen & Bernhard von Gunten
 * @created July, 2002
 */
public class Block extends IhmCustomComparator implements Serializable {

  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 3544675074826449459L;

  public final static int SORT_MATCH_ENERGY = 1;

  public final static int SORT_ATTRIBUTES = 2;

  protected Team team;

  protected Goalie goalie;

  protected FieldPlayer rightDefender;

  protected FieldPlayer leftDefender;

  protected FieldPlayer leftWing;

  protected FieldPlayer rightWing;

  protected FieldPlayer center;

  /**
   * Constructs Block for team
   * 
   * @param team
   */
  public Block(Team team) {
    this.team = team;
  }

  /**
   * Returns total attributes of this block
   * 
   * @return Total attributes
   */
  public int getTotalAttributes() {
    int result = 0;
    result += center.getPlayerAttributes().getTotalAttributesAverage();
    result += rightWing.getPlayerAttributes().getTotalAttributesAverage();
    result += leftWing.getPlayerAttributes().getTotalAttributesAverage();
    result += rightDefender.getPlayerAttributes().getTotalAttributesAverage();
    result += leftDefender.getPlayerAttributes().getTotalAttributesAverage();
    return result;
  }

  /**
   * Returns the total match energy for this block
   * 
   * @return Total match energy
   */
  public int getTotalMatchEnergy() {
    int result = 0;
    result += center.getPlayerAttributes().getMatchEngergy();
    result += rightWing.getPlayerAttributes().getMatchEngergy();
    result += leftWing.getPlayerAttributes().getMatchEngergy();
    result += rightDefender.getPlayerAttributes().getMatchEngergy();
    result += leftDefender.getPlayerAttributes().getMatchEngergy();
    return result;
  }

  /**
   * Returns array of all other field players
   * 
   * @param me
   *          Player
   * @return Player array
   */
  public FieldPlayer[] getOtherFieldPlayers(Player me) {
    Vector<Player> fieldPlayers = new Vector<Player>();
    if (this.getCenter() != null && !this.getCenter().equals(me)) {
      fieldPlayers.add(getCenter());
    }

    if (this.getLeftWing() != null && !this.getLeftWing().equals(me)) {
      fieldPlayers.add(getLeftWing());
    }

    if (this.getRightWing() != null && !this.getRightWing().equals(me)) {
      fieldPlayers.add(getRightWing());
    }

    if (this.getLeftDefender() != null && !this.getLeftDefender().equals(me)) {
      fieldPlayers.add(getLeftDefender());
    }

    if (this.getRightDefender() != null && !this.getRightDefender().equals(me)) {
      fieldPlayers.add(getRightDefender());
    }

    return fieldPlayers.toArray(new FieldPlayer[0]);

  }

  /**
   * Returns other field player
   * 
   * @param me
   *          Player
   * @return Field player
   */
  public FieldPlayer getRandomOtherFieldPlayer(Player me) {
    FieldPlayer[] players = getOtherFieldPlayers(me);
    int r = GameController.getInstance().getScenario().getRandomInt(0, players.length - 1);
    return players[r];
  }

  /**
   * Sorts blocks by SortOrder
   * 
   * @param blocks
   *          Blocks array
   * @param sortorder
   *          SortOrder
   * @param ascending
   *          Ascending
   */
  public static void sortBlocks(Block[] blocks, int sortorder, boolean ascending) {
    if (blocks == null || blocks.length == 0) {
      return;
    }

    for (int i = 0; i < blocks.length; i++) {
      blocks[i].setSortCriteria(sortorder);
      blocks[i].setSortOrder(ascending);
    }

    java.util.Arrays.sort(blocks);
  }

  /**
   * Returns the block with the best energy average out of blocks array
   * 
   * @param blocks
   *          array
   * @return Block
   */
  public static Block getBlockWithBestEnergy(Block[] blocks) {
    sortBlocks(blocks, SORT_MATCH_ENERGY, false);
    return blocks[0];
  }

  /**
   * Returns the block with the best attributes averge out of blocks array
   * 
   * @param blocks
   *          array
   * @return Block
   */
  public static Block getBlockWithBestAttributes(Block[] blocks) {
    sortBlocks(blocks, SORT_ATTRIBUTES, false);
    return blocks[0];
  }

  /**
   * Returns a random block out of blocks array
   * 
   * @param blocks
   *          array
   * @return Block
   */
  public static Block getRandomBlock(Block[] blocks) {
    int r = GameController.getInstance().getScenario().getRandomInt(blocks.length - 1);
    return blocks[r];
  }

  /**
   * Returns true if player is defense player
   * 
   * @param player
   * @return boolean
   */
  public boolean isDefensePlayer(Player player) {
    return (this.getRightDefender().equals(player) || this.getLeftDefender().equals(player));
  }

  /**
   * Returns true if player is wing player
   * 
   * @param player
   * @return boolean
   */
  public boolean isWingPlayer(Player player) {
    return (this.getRightWing().equals(player) || this.getLeftWing().equals(player));
  }

  /**
   * Returns true if player is center
   * 
   * @param player
   * @return boolean
   */
  public boolean isCenterPlayer(Player player) {
    return (this.getCenter().equals(player));
  }

  /**
   * Returns true if player is goalie
   * 
   * @param player
   * @return boolean
   */
  public boolean isGoaliePlayer(Player player) {
    return (this.getGoalie().equals(player));
  }

  /**
   * Returns different defender
   * 
   * @param player
   * @return Player
   */
  public Player getOtherDefender(Player player) {
    if (player.equals(getRightDefender())) {
      return getLeftDefender();
    } else {
      return getRightDefender();
    }
  }

  /**
   * Returns all offensive players
   * 
   * @return Player array
   */
  public Player[] getOffensivePlayers() {
    Player[] result = new Player[3];
    result[0] = getLeftWing();
    result[1] = getCenter();
    result[2] = getRightWing();
    return result;
  }

  /**
   * Returns random offensive player
   * 
   * @return Player
   */
  public Player getRandomOffensivePlayer() {
    Player[] players = getOffensivePlayers();
    int r = GameController.getInstance().getScenario().getRandomInt(0, players.length - 1);
    return players[r];
  }

  /**
   * Returns all defensive players
   * 
   * @return Player array
   */
  public Player[] getDefensivePlayers() {
    Player[] result = new Player[2];
    result[0] = getLeftDefender();
    result[1] = getRightDefender();
    return result;
  }

  /**
   * Returns a random defensiv player
   * 
   * @return Player
   */
  public Player getRandomDefensiveePlayer() {
    Player[] players = getDefensivePlayers();
    int r = GameController.getInstance().getScenario().getRandomInt(0, players.length - 1);
    return players[r];
  }

  /**
   * Returns all field players
   * 
   * @return Player array
   */
  public Player[] getPlayers() {
    Player[] result = new Player[5];
    result[0] = getLeftWing();
    result[1] = getCenter();
    result[2] = getRightWing();
    result[3] = getLeftDefender();
    result[4] = getRightDefender();
    return result;
  }

  /**
   * Returns a random field player
   * 
   * @return Player
   */
  public Player getRandomPlayer() {
    Player[] players = getPlayers();
    int r = GameController.getInstance().getScenario().getRandomInt(0, players.length - 1);
    return players[r];
  }

  /**
   * Gets the sortValue attribute of the PlayerAttributes object
   * 
   * @return The sortValue value
   */
  public double getSortValue() {
    if (sortCriteria == SORT_ATTRIBUTES) {
      return this.getTotalAttributes();
    }
    if (sortCriteria == SORT_MATCH_ENERGY) {
      return this.getTotalMatchEnergy();
    }
    return 0;
  }

  // Setters & Getters

  public Team getTeam() {
    return team;
  }

  public void setTeam(Team team) {
    this.team = team;
  }

  public FieldPlayer getCenter() {
    return center;
  }

  public Goalie getGoalie() {
    return goalie;
  }

  public FieldPlayer getLeftDefender() {
    return leftDefender;
  }

  public FieldPlayer getLeftWing() {
    return leftWing;
  }

  public FieldPlayer getRightDefender() {
    return rightDefender;
  }

  public FieldPlayer getRightWing() {
    return rightWing;
  }

  public void setGoalie(Goalie goalie) {
    this.goalie = goalie;
  }

  public void setRightDefender(FieldPlayer rightDefender) {
    this.rightDefender = rightDefender;
  }

  public void setLeftDefender(FieldPlayer leftDefender) {
    this.leftDefender = leftDefender;
  }

  public void setLeftWing(FieldPlayer leftWing) {
    this.leftWing = leftWing;
  }

  public void setRightWing(FieldPlayer rightWing) {
    this.rightWing = rightWing;
  }

  public void setCenter(FieldPlayer center) {
    this.center = center;
  }

}
