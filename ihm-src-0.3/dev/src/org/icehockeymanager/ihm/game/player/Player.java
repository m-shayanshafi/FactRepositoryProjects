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
  
package org.icehockeymanager.ihm.game.player;

import java.io.*;
import java.util.*;
import java.util.logging.*;

import org.icehockeymanager.ihm.game.*;
import org.icehockeymanager.ihm.game.finance.*;
import org.icehockeymanager.ihm.game.league.*;
import org.icehockeymanager.ihm.game.league.helper.*;
import org.icehockeymanager.ihm.game.team.*;
import org.icehockeymanager.ihm.game.training.*;
import org.icehockeymanager.ihm.game.player.fieldplayer.*;
import org.icehockeymanager.ihm.game.player.goalie.*;
import org.icehockeymanager.ihm.game.scenario.*;
import org.icehockeymanager.ihm.lib.*;
import org.icehockeymanager.ihm.game.injuries.*;
import org.w3c.dom.*;

/**
 * Player class
 * 
 * Player extends IhmCustomComparator for helping to sort players by attributes,
 * postions etc. Some, most, of these sortby values are in member objects (iE
 * Contracts etc) but you always want to sort players so it's done here.
 * 
 * @author Bernhard von Gunten & Arik Dasen
 * @created December 2001
 */
public abstract class Player extends IhmCustomComparator implements Serializable, IhmLogging {

  /** Sort by Position */
  public final static int SORT_ATTRIBUTES_POSITION = 1;

  /** Sort by Player Attributes */
  public final static int SORT_ATTRIBUTES_SPECIFIC_AVERAGE = 2;

  /** Sort by Common Attributes */
  public final static int SORT_ATTRIBUTES_COMMON_AVERAGE = 3;

  /** Sort by Total Attributes */
  public final static int SORT_ATTRIBUTES_TOTAL_AVERAGE = 4;

  /** Sort by Average */
  public final static int SORT_ATTRIBUTES_AVERAGE = 5;

  /** Sort by form */
  public final static int SORT_ATTRIBUTES_VIA_FORM = 6;

  /** Sort by motivation */
  public final static int SORT_ATTRIBUTES_VIA_MOTIVATION = 7;

  /** Sort by energy */
  public final static int SORT_ATTRIBUTES_VIA_ENERGY = 8;

  /** Sort by contract end date */
  public final static int SORT_CONTRACT_CURRENT_END_DATE = 11;

  /** Sort by contract costs */
  public final static int SORT_CONTRACT_CURRENT_COSTS = 12;

  /** Sort by birthdate */
  public final static int SORT_INFO_BIRTHDATE = 20;

  /** Player infos (name etc) */
  private PlayerInfo playerInfo;

  /** Player attributes */
  private PlayerAttributes playerAttributes;

  /** Vector of PlayerStats */
  protected Vector<PlayerStats> playerStats;

  /** Vector of playerStatsGroup (totals for each elementGroup) */
  protected Vector<PlayerStats> playerStatsGroup;

  /** Vector of playerStats (totals for each League) */
  protected Vector<PlayerStats> playerStatsLeague;

  /** Total stats of the year (totals over everything) */
  protected PlayerStats playerStatsTotal;

  /** Stats history over the years */
  private Vector<PlayerStats> statsHistory = null;

  /** Key of this player */
  private int key = -1;

  /** trainingSchedule */
  private TrainingSchedule trainingSchedule = null;

  /** Has the player the puck (in GamePlay only) */
  protected boolean hasPuck = false;

  /** Team */
  private Team team = null;

  /** Player contract */
  private Vector<PlayerContract> playerContracts = null;

  /** Player contract changed this year */
  private boolean playerContractChanged = false;

  /** Player is on transfer list */
  private boolean transferList = false;

  /** Player is prospect */
  private boolean isProspect = false;

  /** Current injury */
  private PlayerInjury playerInjury = null;

  /**
   * Constructs player, resets all
   * 
   * @param playerInfo Infos of player (name etc.)
   * @param key Key of player
   * @param playerAttributes PlayerAttributes
   */
  public Player(int key, PlayerInfo playerInfo, PlayerAttributes playerAttributes) {
    lateConstruct(key, playerInfo, playerAttributes);
  }

  /**
   * Late construct is a helper to finalize the creation of a player (called by
   * constructors)
   * 
   * @param key int
   * @param playerInfo PlayerInfo
   * @param playerAttributes PlayerAttributes
   */
  public void lateConstruct(int key, PlayerInfo playerInfo, PlayerAttributes playerAttributes) {
    this.key = key;
    this.playerInfo = playerInfo;
    this.playerAttributes = playerAttributes;
    this.playerAttributes.setPlayer(this);
    statsHistory = new Vector<PlayerStats>();
    this.playerContracts = new Vector<PlayerContract>();
    newSeason();
  }

  public Player(Element element, Country[] countries) {
    // Implemented by Goalie & FieldPlayer
  }

  /**
   * Gets the key attribute of the Player object
   * 
   * @return The key value
   */
  public int getKey() {
    return key;
  }

  /** Reset stats at the begin of a new season */
  public void newSeason() {
    playerStats = new Vector<PlayerStats>();
    playerStatsGroup = new Vector<PlayerStats>();
    playerStatsLeague = new Vector<PlayerStats>();
    playerContractChanged = false;
  }

  /**
   * Has to be called when the player changes a team. Resets stats
   */
  public void changedTeam() {
    playerStats = new Vector<PlayerStats>();
    playerStatsGroup = new Vector<PlayerStats>();
    playerStatsLeague = new Vector<PlayerStats>();
  }

  /** End of the year, save stats into history */
  public void endSeason() {
    try {
      this.statsHistory.add((PlayerStats) playerStatsTotal.clone());
    } catch (Exception err) {
      logger.log(Level.SEVERE, "statshistory clone failed!", err);
    }

    // Check & remove old contracts
    PlayerContract[] contracts = this.getPlayerContracts();
    Calendar nextYear = GameController.getInstance().getScenario().getScheduler().getToday();
    nextYear.add(Calendar.YEAR, 1);
    for (int i = 0; i < contracts.length; i++) {
      if (contracts[i].getEndDate().before(nextYear)) {
        this.playerContracts.remove(contracts[i]);
        contracts[i] = null;
      }
    }

    // Check if player plays elsewhere next season
    PlayerContract contract = this.getPlayerContractForDate(nextYear);
    if (contract == null && getTeam() != null) {
      getTeam().removePlayer(this);
    }
    if (contract != null && !contract.getPartyA().equals(getTeam())) {
      if (getTeam() != null) {
        getTeam().removePlayer(this);
      }
      Team newTeam = contract.getPartyA();
      newTeam.addPlayer(this);
      this.setOnTransferList(false);
    }

  }

  /**
   * Returns player info
   * 
   * @return The playerInfo value
   */
  public PlayerInfo getPlayerInfo() {
    return playerInfo;
  }

  /**
   * Sets the team of a player (backlink). This helps to gain some performance.
   * 
   * @param team Team
   */
  public void setTeam(Team team) {
    this.team = team;
  }

  /**
   * Returns the team of a player (backlink). This helps to gain some
   * performance.
   * 
   * @return The team value
   */
  public Team getTeam() {
    return team;
    // Could also be implemented that way: (slow)
    // return GameController.getInstance().getScenario().getTeamByPlayer(this);
  }

  /**
   * Returns the jersey number of a player
   * 
   * @return Jersey Number
   */
public int getTeamJerseyNumber() {
    if (getTeam() != null) {
      if ( getTeam().getJerseyNumber(this) == null) {
        return 0;
      } else {
        return getTeam().getJerseyNumber(this);
      }
      // Could also be implemented that way: (slow)
      // return
      // GameController.getInstance().getScenario().getTeamByPlayer(this).getJerseyNumber(this);
    } else {
      return 0;
    }
  }
  /**
   * Create stats for given league (to be implemented by subclasses)
   * 
   * @param league League who's owner of this stats.
   */
  public abstract void createStats(League league);

  /**
   * Create stats for given leagueElementGroup
   * 
   * @param leagueElementGroup LeagueElementGroup who's owner of this stats.
   */
  public abstract void createStats(LeagueElementGroup leagueElementGroup);

  /**
   * Returns Playerattributes
   * 
   * @return The playerAttributes value
   */
  public PlayerAttributes getPlayerAttributes() {
    return playerAttributes;
  }

  /**
   * Returns player attributes in a fake array
   * 
   * @return The playerStatsTotalArray value
   */
  public PlayerAttributes[] getPlayerAttributesArray() {
    PlayerAttributes[] result = new PlayerAttributes[1];
    result[0] = getPlayerAttributes();
    return result;
  }

  /**
   * Create new stats for LeagueElement passed by
   * 
   * @param leagueElement Description of the Parameter
   */
  public abstract void createStats(LeagueElement leagueElement);

  /**
   * Return total stats
   * 
   * @return Description of the Return Value
   */
  public PlayerStats getPlayerStatsTotal() {
    return playerStatsTotal;
  }

  /**
   * Returns player total stats in a fake array
   * 
   * @return The playerStatsTotalArray value
   */
  public PlayerStats[] getPlayerStatsTotalArray() {
    PlayerStats[] result = new PlayerStats[1];
    result[0] = playerStatsTotal;
    return result;
  }

  /**
   * Return stats for given LeagueElement
   * 
   * @param leagueElement Owner to search for
   * @return The stats value
   */
  public PlayerStats getStats(LeagueElement leagueElement) {
    for (int i = 0; i < playerStats.size(); i++) {
      PlayerStats tmp = (PlayerStats) playerStats.get(i);
      if (leagueElement.equals(tmp.getOwner())) {
        return tmp;
      }
    }
    this.createStats(leagueElement);
    return getStats(leagueElement);

    // return null;
  }

  /**
   * Return stats for given league
   * 
   * @param league Owner to search for
   * @return The stats value
   */
  public PlayerStats getStats(League league) {
    for (int i = 0; i < playerStatsLeague.size(); i++) {
      PlayerStats tmp = (PlayerStats) playerStatsLeague.get(i);
      if (league.equals(tmp.getOwner())) {
        return tmp;
      }
    }
    this.createStats(league);
    return getStats(league);
    // return null;
  }

  /**
   * Return stats for given leagueElementGroup
   * 
   * @param leagueElementGroup Owner to search for
   * @return The stats value
   */
  public PlayerStats getStats(LeagueElementGroup leagueElementGroup) {
    for (int i = 0; i < playerStatsGroup.size(); i++) {
      PlayerStats tmp = (PlayerStats) playerStatsGroup.get(i);
      if (leagueElementGroup.equals(tmp.getOwner())) {
        return tmp;
      }
    }
    this.createStats(leagueElementGroup);
    return getStats(leagueElementGroup);

    // return null;
  }

  /**
   * Return all stats for given LeagueElement
   * 
   * @param leagueElement LeagueElement
   * @return The allStats value
   */
  public PlayerStats[] getAllStats(LeagueElement leagueElement) {
    PlayerStats[] result;
    if (leagueElement.getLeagueElementGroup() != null) {
      result = new PlayerStats[4];
    } else {
      result = new PlayerStats[3];
    }

    result[0] = playerStatsTotal;
    result[1] = getStats(leagueElement);
    result[2] = getStats(leagueElement.getLeague());
    if (leagueElement.getLeagueElementGroup() != null) {
      result[3] = getStats(leagueElement.getLeagueElementGroup());
    }
    return result;
  }

  /**
   * Returns all (league Element) stats in a array
   * 
   * @return The allLeagueElementStats value
   */
  public PlayerStats[] getAllLeagueElementStats() {
    return playerStats.toArray(new PlayerStats[playerStats.size()]);
  }

  /**
   * Returns all (league) stats in a array
   * 
   * @return The allLeagueStats value
   */
  public PlayerStats[] getAllLeagueStats() {
    return playerStatsLeague.toArray(new PlayerStats[playerStatsLeague.size()]);
  }

  /**
   * Returns all (leagueElementGroup) stats in a array
   * 
   * @return The allLeagueElementGroupStats value
   */
  public PlayerStats[] getAllLeagueElementGroupStats() {
    return playerStatsGroup.toArray(new PlayerStats[playerStatsGroup.size()]);
  }

  /**
   * Description of the Method
   * 
   * @return Description of the Return Value
   */
  public String toString() {
    return this.playerInfo.getFirstName() + " " + this.playerInfo.getLastName();
  }

  /**
   * Gets the trainingSchedule attribute of the Player object (if not found and
   * member of a team, returns the team trainingSchedule)
   * 
   * @return The trainingSchedule value
   */
  public TrainingSchedule getTrainingSchedule() {
    return trainingSchedule;
  }

  /**
   * Sets the trainingSchedule attribute of the Player object
   * 
   * @param trainingSchedule The new trainingSchedule value
   */
  public void setTrainingSchedule(TrainingSchedule trainingSchedule) {
    this.trainingSchedule = trainingSchedule;
  }

  /**
   * Returns all player Contracts
   * 
   * @return PlayerContract[]
   */
  public PlayerContract[] getPlayerContracts() {
    return playerContracts.toArray(new PlayerContract[playerContracts.size()]);
  }

  /**
   * Adds a contrct to the existing contracts list.
   * 
   * @param contract PlayerContract
   */
  public void addPlayerContract(PlayerContract contract) {
    this.playerContracts.add(contract);
    this.playerContractChanged = true;
  }

  /**
   * Saves this Contract, deletes all others.
   * 
   * @param playerContract PlayerContract
   */
  public void setOnlyPlayerContract(PlayerContract playerContract) {
    this.playerContracts = new Vector<PlayerContract>();
    addPlayerContract(playerContract);
  }

  /**
   * Returns true if player had a contract changed / applied this season.
   * 
   * @return boolean
   */
  public boolean hasPlayerContractChangedThisYear() {
    return playerContractChanged;
  }

  /**
   * Returns the current valid Contract.
   * 
   * @return PlayerContract
   */
  public PlayerContract getPlayerContractCurrent() {
    Calendar today = GameController.getInstance().getScenario().getScheduler().getToday();
    return getPlayerContractForDate(today);
  }

  /**
   * Returns the first possible working day, after the last contract in his
   * contract list has ended. If player has no contracts, it would be today.
   * 
   * @return Calendar
   */
  public Calendar getFirstPossibleWorkingDay() {
    PlayerContract[] contracts = getPlayerContracts();
    if (contracts.length == 0) {
      return GameController.getInstance().getScenario().getScheduler().getToday();
    } else {
      Contract.sortContractArray(contracts, Contract.SORT_ENDDATE, false);
      Calendar result = contracts[0].getEndDate();
      result.add(Calendar.DATE, 1);
      return result;
    }
  }

  /**
   * Returns the valid contract for a given date. If none is found,null will be
   * returned.
   * 
   * @param cal Calendar
   * @return PlayerContract
   */
  public PlayerContract getPlayerContractForDate(Calendar cal) {
    PlayerContract[] pcs = getPlayerContracts();
    Calendar tomorrow = (Calendar) cal.clone();
    tomorrow.add(Calendar.DATE, 1);
    Calendar yesterday = (Calendar) cal.clone();
    yesterday.add(Calendar.DATE, -1);

    for (int i = 0; i < pcs.length; i++) {
      if (pcs[i].getStartDate().before(tomorrow) && pcs[i].getEndDate().after(yesterday)) {
        return pcs[i];
      }
    }

    return null;
  }

  /**
   * Returns true, if player has a valid Contract for the next season
   * 
   * @return boolean
   */
  public boolean hasContractForNextSeason() {
    Calendar tmpCal = GameController.getInstance().getScenario().getScheduler().getToday();
    tmpCal.add(Calendar.YEAR, 1);
    return (getPlayerContractForDate(tmpCal) != null);
  }

  /**
   * Helper function: Returns true, if a given team is allowed to extend a
   * contract (only owner of player). Checks also other influences
   * 
   * @param teamPoV Team
   * @return boolean
   */
  public boolean allowExtendContractForTeam(Team teamPoV) {
    if (!isProspect() && (this.getTeam() != null && this.getTeam().equals(teamPoV)) && !this.hasContractForNextSeason() && !this.hasPlayerContractChangedThisYear()) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * AI Helper function: Returns true, if agiven team is allowed to extentend a
   * contract of a player.
   * 
   * @param teamPoV Team
   * @return boolean
   */
  public boolean allowAiExtendContractForTeam(Team teamPoV) {
    if (!isProspect() && (this.getTeam() != null && this.getTeam().equals(teamPoV)) && !this.hasContractForNextSeason() && !this.hasPlayerContractChangedThisYear() && !this.isOnTransferList()) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * AI Helper function: Returns true, if AI may transfer this player
   * 
   * @return boolean
   */
  public boolean allowAiTransferToTeam() {
    if (!isProspect() && this.getTeam() == null && !this.hasContractForNextSeason() && !this.hasPlayerContractChangedThisYear() && this.isOnTransferList()) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * Helper functino: Returns true if a given team may transfer/hire this
   * player.
   * 
   * @param teamPoV Team
   * @return boolean
   */
  public boolean allowTransferToTeam(Team teamPoV) {
    if (!isProspect() && (this.getTeam() == null || !this.getTeam().equals(teamPoV)) && !this.hasPlayerContractChangedThisYear() && this.isOnTransferList()) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * Helper function: Returns true, if this player is a prospect and may be
   * hired by a given team.
   * 
   * @param team Team
   * @return boolean
   */
  public boolean allowProspectToBeHired(Team team) {
    if (isProspect() && this.getTeam() != null && this.getTeam().equals(team)) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * Helper function: Returns true, if this player may be placed on the
   * transferlist by a given team.
   * 
   * @param team Team
   * @return boolean
   */
  public boolean allowTransferListPlacement(Team team) {
    if (!isProspect() && !this.hasPlayerContractChangedThisYear() && this.getTeam() != null && this.getTeam().equals(team)) {
      return true;
    } else {
      return false;
    }
  }

  /** Compute impact on player after a game is won */
  public void impactOnGameWon() {
    PlayerAttribute form = playerAttributes.getVIAPlayerAttribute(PlayerAttributes.VIA_FORM_KEY);
    form.increase(GameController.getInstance().getScenario().getScenarioSettings().IMPACT_GAMEWON_FORM);
    PlayerAttribute motivation = playerAttributes.getVIAPlayerAttribute(PlayerAttributes.VIA_FORM_KEY);
    motivation.increase(GameController.getInstance().getScenario().getScenarioSettings().IMPACT_GAMEWON_MOTIVATION);
  }

  /** Compute impact on player after a game is tied */
  public void impactOnGameTied() {
    PlayerAttribute form = playerAttributes.getVIAPlayerAttribute(PlayerAttributes.VIA_FORM_KEY);
    form.increase(GameController.getInstance().getScenario().getScenarioSettings().IMPACT_GAMETIED_FORM);
    PlayerAttribute motivation = playerAttributes.getVIAPlayerAttribute(PlayerAttributes.VIA_FORM_KEY);
    motivation.increase(GameController.getInstance().getScenario().getScenarioSettings().IMPACT_GAMETIED_MOTIVATION);
  }

  /** Compute impact on player after a game is lost */
  public void impactOnGameLost() {
    PlayerAttribute form = playerAttributes.getVIAPlayerAttribute(PlayerAttributes.VIA_FORM_KEY);
    form.increase(GameController.getInstance().getScenario().getScenarioSettings().IMPACT_GAMELOST_FORM);
    PlayerAttribute motivation = playerAttributes.getVIAPlayerAttribute(PlayerAttributes.VIA_FORM_KEY);
    motivation.increase(GameController.getInstance().getScenario().getScenarioSettings().IMPACT_GAMELOST_MOTIVATION);
  }

  /** Compute impact on player after he scored a goal */
  public void impactOnGoals() {
    PlayerAttribute form = playerAttributes.getVIAPlayerAttribute(PlayerAttributes.VIA_FORM_KEY);
    form.increase(GameController.getInstance().getScenario().getScenarioSettings().IMPACT_GOAL_FORM);
    PlayerAttribute motivation = playerAttributes.getVIAPlayerAttribute(PlayerAttributes.VIA_FORM_KEY);
    motivation.increase(GameController.getInstance().getScenario().getScenarioSettings().IMPACT_GOAL_MOTIVATION);
  }

  /** Compute impact on player after he scored an assist */
  public void impactOnAssist() {
    PlayerAttribute form = playerAttributes.getVIAPlayerAttribute(PlayerAttributes.VIA_FORM_KEY);
    form.increase(GameController.getInstance().getScenario().getScenarioSettings().IMPACT_ASSIST_FORM);
    PlayerAttribute motivation = playerAttributes.getVIAPlayerAttribute(PlayerAttributes.VIA_FORM_KEY);
    motivation.increase(GameController.getInstance().getScenario().getScenarioSettings().IMPACT_ASSIST_MOTIVATION);
  }

  /** Compute impact on player after goal against the team */
  public void impactOnGoalsAggainst() {
    PlayerAttribute form = playerAttributes.getVIAPlayerAttribute(PlayerAttributes.VIA_FORM_KEY);
    form.increase(GameController.getInstance().getScenario().getScenarioSettings().IMPACT_GOALAGAINST_FORM);
    PlayerAttribute motivation = playerAttributes.getVIAPlayerAttribute(PlayerAttributes.VIA_FORM_KEY);
    motivation.increase(GameController.getInstance().getScenario().getScenarioSettings().IMPACT_GOALAGAINST_MOTIVATION);
  }

  /** Compute impact on player after a penalty */
  public void impactOnPenalty() {
    PlayerAttribute form = playerAttributes.getVIAPlayerAttribute(PlayerAttributes.VIA_FORM_KEY);
    form.increase(GameController.getInstance().getScenario().getScenarioSettings().IMPACT_PENALTY_FORM);
    PlayerAttribute motivation = playerAttributes.getVIAPlayerAttribute(PlayerAttributes.VIA_FORM_KEY);
    motivation.increase(GameController.getInstance().getScenario().getScenarioSettings().IMPACT_PENALTY_MOTIVATION);
  }

  /** Compute impact on player after he played a game */
  public void impactOnGamePlayed() {
    PlayerAttribute form = playerAttributes.getVIAPlayerAttribute(PlayerAttributes.VIA_FORM_KEY);
    form.increase(GameController.getInstance().getScenario().getScenarioSettings().IMPACT_GAMEPLAYED_FORM);
    PlayerAttribute motivation = playerAttributes.getVIAPlayerAttribute(PlayerAttributes.VIA_FORM_KEY);
    motivation.increase(GameController.getInstance().getScenario().getScenarioSettings().IMPACT_GAMEPLAYED_MOTIVATION);
  }

  /**
   * Computes all impacts on a player (attributes) training over all attributes.
   * Decreases all attributes, before the specific training units may be called.
   */
  public void impactOnTrainingOverall() {
    PlayerAttribute[] pa = getPlayerAttributes().getSpecificPlayerAttributes();
    double dec = GameController.getInstance().getScenario().getScenarioSettings().TRAINING_ATTRIBUTES_MINUS / pa.length * 2;
    for (int i = 0; i < pa.length; i++) {
      pa[i].decrease(dec);
    }
  }

  /**
   * Computes all impacts on a player (attributes) for a specific training unit.
   * 
   * @param attribute Training unit
   */
  public void impactOnTrainingSpecific(String attribute) {
    // Yes, we train
    if (attribute != null) {
      // First take the lesson
      double inc = GameController.getInstance().getScenario().getScenarioSettings().TRAINING_ATTRIBUTES_PLUS;
      playerAttributes.getSpecificPlayerAttribute(attribute).increase(inc);
      // But takes also energy
      PlayerAttribute energy = playerAttributes.getVIAPlayerAttribute(PlayerAttributes.VIA_ENERGY_KEY);
      energy.decrease(GameController.getInstance().getScenario().getScenarioSettings().TRAINING_ENERGY_MINUS);
    } else {
      // No training ? Cool !!!
      PlayerAttribute energy = playerAttributes.getVIAPlayerAttribute(PlayerAttributes.VIA_ENERGY_KEY);
      energy.increase(GameController.getInstance().getScenario().getScenarioSettings().TRAINING_ENERGY_PLUS);
    }
  }

  /**
   * Creates an impact on the players morale
   * 
   * @param points int
   */
  public void impactOnMorale(int points) {
    PlayerAttribute morale = playerAttributes.getVIAPlayerAttribute(PlayerAttributes.VIA_MORALE_KEY);
    morale.increase(points);
  }

  /**
   * Creates an impact on the players energy.
   * 
   * @param points int
   */
  public void impactOnEnergy(int points) {
    PlayerAttribute energy = playerAttributes.getVIAPlayerAttribute(PlayerAttributes.VIA_ENERGY_KEY);
    energy.increase(points);
  }

  /**
   * Creates an impact on the players form
   * 
   * @param points int
   */
  public void impactOnForm(int points) {
    PlayerAttribute form = playerAttributes.getVIAPlayerAttribute(PlayerAttributes.VIA_FORM_KEY);
    form.increase(points);
  }

  /**
   * Adds the improvement to very attribute of a prospect
   * 
   * @param improvement
   */
  public void impactOnProspectsTraining(double improvement) {
    PlayerAttribute[] pa = getPlayerAttributes().getSpecificPlayerAttributes();
    for (int i = 0; i < pa.length; i++) {
      pa[i].increase(improvement);
    }
  }

  /**
   * Adds this player to a given XML element
   * 
   * @param parent Element
   */
  public abstract void addAsElementToParent(Element parent);

  /**
   * Helper function: Sorts array of players
   * 
   * @param players Player[]
   * @param sortorder int
   * @param ascending boolean
   */
  public static void sortPlayerArray(Player[] players, int sortorder, boolean ascending) {
    if (players == null || players.length == 0) {
      return;
    }

    for (int i = 0; i < players.length; i++) {
      players[i].setSortCriteria(sortorder);
      players[i].setSortOrder(ascending);
    }

    java.util.Arrays.sort(players);

  }

  /**
   * Returns the players market value (0 - 100) based on age, and attributes
   * 
   * @return Marketvalue
   */
  public int getMarketValue() {
    int result = 0;
    // Attributes (1-70)
    result = this.getPlayerAttributes().getOverallAverage() - 30;
    if (result > 70) {
      result = 70;
    } else if (result < 0) {
      result = 0;
    }

    // Age (1-30)
    int age = 30 - (this.playerInfo.getAge()) + 20;
    if (age > 30) {
      age = 30;
    }

    result += age;

    return result;
  }

  /**
   * Returns true if player is on the transfer list
   * 
   * @return boolean
   */
  public boolean isOnTransferList() {
    return transferList;
  }

  /**
   * Sets flag that player is on the transferlist.
   * 
   * @param set boolean
   */
  public void setOnTransferList(boolean set) {
    transferList = set;
  }

  /**
   * Gets the sortValue attribute of the PlayerAttributes object
   * 
   * @return The sortValue value
   */
  public double getSortValue() {
    if (sortCriteria == SORT_ATTRIBUTES_POSITION) {
      return getPlayerAttributes().getPosition() * 10000 + getPlayerAttributes().getOverallAverage();
    }
    if (sortCriteria == SORT_ATTRIBUTES_SPECIFIC_AVERAGE) {
      return getPlayerAttributes().getSpecificPlayerAttributesAverage();
    }
    if (sortCriteria == SORT_ATTRIBUTES_COMMON_AVERAGE) {
      return getPlayerAttributes().getCommonPlayerAttributesAverage();
    }
    if (sortCriteria == SORT_ATTRIBUTES_TOTAL_AVERAGE) {
      return getPlayerAttributes().getTotalAttributesAverage();
    }
    if (sortCriteria == SORT_ATTRIBUTES_AVERAGE) {
      return getPlayerAttributes().getOverallAverage();
    }
    if (sortCriteria == SORT_ATTRIBUTES_VIA_FORM) {
      return getPlayerAttributes().getVIAPlayerAttribute(PlayerAttributes.VIA_FORM_KEY).getValue();
    }
    if (sortCriteria == SORT_ATTRIBUTES_VIA_ENERGY) {
      return getPlayerAttributes().getVIAPlayerAttribute(PlayerAttributes.VIA_ENERGY_KEY).getValue();
    }
    if (sortCriteria == SORT_ATTRIBUTES_VIA_MOTIVATION) {
      return getPlayerAttributes().getVIAPlayerAttribute(PlayerAttributes.VIA_MORALE_KEY).getValue();
    }
    if (sortCriteria == SORT_INFO_BIRTHDATE) {
      return getPlayerInfo().getBirthdate().getTime().getTime();
    }
    if (sortCriteria == SORT_CONTRACT_CURRENT_COSTS) {
      return getPlayerContractCurrent().getAmount();
    }
    if (sortCriteria == SORT_CONTRACT_CURRENT_END_DATE) {
      return getPlayerContractCurrent().getEndDate().getTime().getTime();
    }
    return 0;
  }

  /**
   * Returns a new created player by given argments
   * 
   * @param r Random
   * @param today Calendar
   * @param key int
   * @param familyNames String[]
   * @param firstNames String[]
   * @param minAge int
   * @param maxAge int
   * @param minOverall int
   * @param maxOverall int
   * @param countries Country[]
   * @return Player
   */
  public static Player createRandomPlayer(Random r, Calendar today, int key, String[] familyNames, String[] firstNames, int minAge, int maxAge, int minOverall, int maxOverall, Country[] countries) {
    int position = PlayerAttributes.getRandomPosition(r);
    return createRandomPlayer(r, today, key, position, familyNames, firstNames, minAge, maxAge, minOverall, maxOverall, countries);
  }

  /**
   * Creates a random player by given argments.
   * 
   * @param r Random
   * @param today Calendar
   * @param key int
   * @param position int
   * @param familyNames String[]
   * @param firstNames String[]
   * @param minAge int
   * @param maxAge int
   * @param minOverall int
   * @param maxOverall int
   * @param countries Country[]
   * @return Player
   */
  public static Player createRandomPlayer(Random r, Calendar today, int key, int position, String[] familyNames, String[] firstNames, int minAge, int maxAge, int minOverall, int maxOverall, Country[] countries) {
    String lastName = familyNames[r.nextInt(familyNames.length)];
    String firstName = firstNames[r.nextInt(firstNames.length)];
    Calendar birthday = (Calendar) today.clone();
    birthday.add(Calendar.YEAR, -(r.nextInt(maxAge - minAge) + minAge + 1));
    birthday.set(Calendar.DATE, r.nextInt(365));
    Country country = countries[r.nextInt(countries.length)];

    // TODO : Calculate these attributes
    int weight = 85;
    int height = 185;
    int handeness = PlayerAttributes.HANDENESS_RIGHT;

    Player player = null;

    if (position == PlayerAttributes.POSITION_GOALTENDING) {
      player = new Goalie(key, new PlayerInfo(firstName, lastName, birthday, country), new GoalieAttributes(position, weight, height, handeness));
    } else {
      player = new FieldPlayer(key, new PlayerInfo(firstName, lastName, birthday, country), new FieldPlayerAttributes(position, weight, height, handeness));
    }

    player.getPlayerAttributes()._setRandomDefaultAttributes(position, (r.nextInt(maxOverall - minOverall) + minOverall));

    return player;
  }

  /**
   * Finishes a prospec hire.
   */
  public void finishProspectHire() {
    this.setProspect(false);
  }

  /**
   * Finishes a player transfer
   */
  public void finishTransfer() {
    setOnTransferList(false);
  }

  /**
   * Returns true if player is a prospect.
   * 
   * @return boolean
   */
  public boolean isProspect() {
    return isProspect;
  }

  /**
   * Sets player to prospect.
   * 
   * @param prospect boolean
   */
  public void setProspect(boolean prospect) {
    this.isProspect = prospect;
  }

  /**
   * Retruns a fair salary fot this player
   * 
   * @return double
   */
  public double getFairSalary() {
    int salaryMultiplier = GameController.getInstance().getScenario().getScenarioSettings().PLAYER_CONTRACT_SALARY_MULTIPLIER;
    return getMarketValue() * salaryMultiplier;
  }

  /**
   * Returns a random salary on the fair salary amount
   * 
   * @return double
   */
  public double getRandomSalary() {
    int fairRange = GameController.getInstance().getScenario().getScenarioSettings().PLAYER_CONTRACT_FAIR_RANGE;
    int randomPercent = GameController.getInstance().getScenario().getRandomInt(-fairRange, +fairRange);
    return getFairSalary() / 100 * (100 + randomPercent);
  }

  /**
   * Returns a fair transfer fee
   * 
   * @return double
   */
  public double getFairTransferFee() {
    int transferFeeMultiplier = GameController.getInstance().getScenario().getScenarioSettings().PLAYER_CONTRACT_TRANSFER_FEE_MULTIPLIER;
    return getMarketValue() * transferFeeMultiplier;
  }

  /**
   * Returns a random transferfee based on the fair transferfee
   * 
   * @return double
   */
  public double getRandomTransferFee() {
    int fairRange = GameController.getInstance().getScenario().getScenarioSettings().PLAYER_CONTRACT_FAIR_RANGE;
    int randomPercent = GameController.getInstance().getScenario().getRandomInt(-fairRange, +fairRange);
    return getFairTransferFee() / 100 * (100 + randomPercent);
  }

  /**
   * Sets the players current injury
   * 
   * @param playerInjury PlayerInjury
   */
  public void setPlayerInjury(PlayerInjury playerInjury) {
    this.playerInjury = playerInjury;
  }

  /**
   * Returns the current players injury. Returns null if player has none.
   * 
   * @return PlayerInjury
   */
  public PlayerInjury getPlayerInjury() {
    return this.playerInjury;
  }

}
