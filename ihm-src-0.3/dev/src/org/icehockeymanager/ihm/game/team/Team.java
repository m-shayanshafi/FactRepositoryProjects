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
  
package org.icehockeymanager.ihm.game.team;

import java.io.*;
import java.util.*;
import java.util.logging.*;

import org.icehockeymanager.ihm.game.finance.*;
import org.icehockeymanager.ihm.game.league.*;
import org.icehockeymanager.ihm.game.league.helper.*;
import org.icehockeymanager.ihm.game.player.*;
import org.icehockeymanager.ihm.game.training.*;
import org.icehockeymanager.ihm.game.infrastructure.*;
import org.icehockeymanager.ihm.game.infrastructure.arena.*;
import org.icehockeymanager.ihm.game.sponsoring.*;
import org.icehockeymanager.ihm.game.tactics.*;
import org.icehockeymanager.ihm.lib.*;
import org.w3c.dom.*;

/**
 * Team class containing player, stats etc.
 * 
 * @author Bernhard von Gunten & Arik Dasen
 * @created December, 2001
 */
public class Team implements PlayerOwner, Serializable, IhmLogging {

  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 3544395794578028855L;

  /** Vector of teamStats (Real stats for each LeagueElement) */
  private Vector<TeamStats> teamStats;

  /** Vector of teamStatsGroup (totals for each elementGroup) */
  private Vector<TeamStats> teamStatsGroup;

  /** Vector of leagueStats (totals for each League) */
  private Vector<TeamStats> teamStatsLeague;

  /** Total stats of the year (totals over everything) */
  private TeamStats teamStatsTotal;

  /** Total stats history over the years */
  private Vector<TeamStats> statsHistory = null;

  /** Team infos */
  private TeamInfo teamInfo;

  /** Team infos */
  private TeamAttributes teamAttributes;

  /** Players of this team */
  private Vector<Player> players = null;

  /** Key of this team */
  private int key = -1;

  /** Global TrainingSchedule */
  private TrainingSchedule trainingSchedule = null;

  /** Accounting for this team */
  private Accounting accounting = null;

  /** The teams infrastructure objects, element 0 is always the arena */
  private Infrastructure infrastructure;

  /** Sponsoring for this team */
  private Sponsoring sponsoring;

  /** Youth players */
  private Vector<Player> prospects = null;

  /** Tactics of this team */
  private Tactics tactics = null;

  /** Jersey Numbers of players */
  private HashMap<Integer, Player> jerseyNumbers = null;

  /**
   * Constructs team
   * 
   * @param teamInfo TeamInfo of this team
   * @param key Key of Team
   */
  public Team(int key, TeamInfo teamInfo) {
    this.players = new Vector<Player>();
    this.jerseyNumbers = new HashMap<Integer, Player>();
    lateConstruct(key, teamInfo);
  }

  /**
   * Constructs team
   * 
   * @param element Element XML Element
   * @param players Player[] List of all players in scenario
   */
  public Team(Element element, Player[] players) {
    this.players = new Vector<Player>();
    this.jerseyNumbers = new HashMap<Integer, Player>();
    int myKey = ToolsXML.getIntAttribute(element, "KEY");
    TeamInfo myTeamInfo = new TeamInfo(ToolsXML.getFirstSubElement(element, "TEAMINFO"));

    // Seek & link this team's players:
    Element teamElement = ToolsXML.getFirstSubElement(element, "TEAMPLAYERS");
    NodeList playersList = teamElement.getElementsByTagName("TEAMPLAYER");
    int count = playersList.getLength();
    for (int i = 0; i < count; i++) {
      Element playerKey = (Element) playersList.item(i);
      int key = ToolsXML.getIntAttribute(playerKey, "KEY");
      // Seek player
      for (int o = 0; o < players.length; o++) {
        if (players[o].getKey() == key) {
          addPlayer(players[o]);
          break;
        }
      }
    }

    lateConstruct(myKey, myTeamInfo);
  }

  /**
   * Late constructs the Team object (called by constructors)
   * 
   * @param key
   * @param teamInfo
   */
  public void lateConstruct(int key, TeamInfo teamInfo) {
    this.key = key;
    this.teamInfo = teamInfo;
    this.teamAttributes = new TeamAttributes(this);
    this.trainingSchedule = new TrainingSchedule();
    this.accounting = new Accounting(this);
    this.prospects = new Vector<Player>();
    this.infrastructure = new Infrastructure(this);
    this.statsHistory = new Vector<TeamStats>();

    // sponsoring init must be after arena...
    sponsoring = new Sponsoring(this);

    // Create tactics
    this.tactics = new Tactics(this);
  }

  /**
   * Adds the team to an existing XML Element.
   * 
   * @param parent
   */
  public void addAsElementToParent(Element parent) {
    Element element = parent.getOwnerDocument().createElement("TEAM");
    element.setAttribute("KEY", String.valueOf(this.getKey()));

    // Infos
    getTeamInfo().addAsElementToParent(element);

    // Players
    Element playersElement = element.getOwnerDocument().createElement("TEAMPLAYERS");
    Player[] players = getPlayers();
    for (int i = 0; i < players.length; i++) {
      Element player = element.getOwnerDocument().createElement("TEAMPLAYER");
      player.setAttribute("KEY", String.valueOf(players[i].getKey()));
      playersElement.appendChild(player);
    }
    element.appendChild(playersElement);

    parent.appendChild(element);

  }

  /**
   * Gets the key attribute of the Team object
   * 
   * @return The key value
   */
  public int getKey() {
    return key;
  }

  /**
   * Gets the playerInTeam attribute of the Team object
   * 
   * @param oPlayer Description of the Parameter
   * @return The playerInTeam value
   */
  public boolean isPlayerInTeam(Player oPlayer) {
    return this.players.contains(oPlayer);
  }

  /** Reset team, stats etc ... */
  public void newSeason() {
    teamStats = new Vector<TeamStats>();
    teamStatsGroup = new Vector<TeamStats>();
    teamStatsLeague = new Vector<TeamStats>();
    this.teamStatsTotal = new TeamStats(this, null);
    this.getSponsoring().newSeason();
  }

  /** End of the year, save stats into history */
  public void endSeason() {
    try {
      this.statsHistory.add((TeamStats) teamStatsTotal.clone());
    } catch (Exception err) {
      logger.log(Level.SEVERE, "statshistory clone failed!", err);
    }

    Player[] players = this.getPlayers();

    for (int i = 0; i < players.length; i++) {
      if (!players[i].hasContractForNextSeason()) {
        this.removePlayer(players[i]);
      }
    }
  }

  /**
   * Create stats for a league (in players too)
   * 
   * @param league Owner of the new stats
   */
  public void createStats(League league) {
    // Add stats for a league Element to the team
    teamStatsLeague.add(new TeamStats(this, league));
    // Add stats to every Player of this team
    for (int i = 0; i < players.size(); i++) {
      players.get(i).createStats(league);
    }
  }

  /**
   * Create stats for a leagueElementGroup (in players too)
   * 
   * @param leagueElementGroup Owner of the new stats
   */
  public void createStats(LeagueElementGroup leagueElementGroup) {
    // Add stats for a league Element to the team
    teamStatsGroup.add(new TeamStats(this, leagueElementGroup));
    // Add stats to every Player of this team
    for (int i = 0; i < players.size(); i++) {
      players.get(i).createStats(leagueElementGroup);
    }
  }

  /**
   * Create stats for a leagueElement (in players too)
   * 
   * @param leagueElement Owner of the new stats
   */
  public void createStats(LeagueElement leagueElement) {
    // Add stats for a league Element to the team
    teamStats.add(new TeamStats(this, leagueElement));
    // Add stats to every Player of this team
    for (int i = 0; i < players.size(); i++) {
      players.get(i).createStats(leagueElement);
    }
  }

  /**
   * Returns teamStatsTotal
   * 
   * @return The teamStatsTotal value
   */
  public TeamStats getTeamStatsTotal() {
    return teamStatsTotal;
  }

  /**
   * Return stats for given LeagueElement
   * 
   * @param leagueElement Owner to search for
   * @return The stats value
   */
  public TeamStats getStats(LeagueElement leagueElement) {
    for (int i = 0; i < teamStats.size(); i++) {
      TeamStats tmp = teamStats.get(i);
      if (leagueElement.equals(tmp.getOwner())) {
        return tmp;
      }
    }
    return null;
  }

  /**
   * Return stats for given league
   * 
   * @param league Owner to search for
   * @return The stats value
   */
  public TeamStats getStats(League league) {
    for (int i = 0; i < teamStatsLeague.size(); i++) {
      TeamStats tmp = teamStatsLeague.get(i);
      if (league.equals(tmp.getOwner())) {
        return tmp;
      }
    }
    return null;
  }

  /**
   * Return stats for given leagueElementGroup
   * 
   * @param leagueElementGroup Owner to search for
   * @return The stats value
   */
  public TeamStats getStats(LeagueElementGroup leagueElementGroup) {
    for (int i = 0; i < teamStatsGroup.size(); i++) {
      TeamStats tmp = teamStatsGroup.get(i);
      if (leagueElementGroup.equals(tmp.getOwner())) {
        return tmp;
      }
    }
    return null;
  }

  /**
   * Return all stats for given LeagueElement
   * 
   * @param leagueElement LeagueElement
   * @return The allStats value
   */
  public TeamStats[] getAllStats(LeagueElement leagueElement) {
    TeamStats[] result;
    if (leagueElement.getLeagueElementGroup() != null) {
      result = new TeamStats[4];
    } else {
      result = new TeamStats[3];
    }

    result[0] = teamStatsTotal;
    result[1] = getStats(leagueElement);
    result[2] = getStats(leagueElement.getLeague());
    if (leagueElement.getLeagueElementGroup() != null) {
      result[3] = getStats(leagueElement.getLeagueElementGroup());
    }
    return result;
  }

  /**
   * Returns team total stats in a fake array
   * 
   * @return The teamStatsTotalArray value
   */
  public TeamStats[] getTeamStatsTotalArray() {
    TeamStats[] result = new TeamStats[1];
    result[0] = teamStatsTotal;
    return result;
  }

  /**
   * Returns all (league Element) stats in a array
   * 
   * @return The allLeagueElementStats value
   */
  public TeamStats[] getAllLeagueElementStats() {
    return teamStats.toArray(new TeamStats[teamStats.size()]);
  }

  /**
   * Returns all (league) stats in a array
   * 
   * @return The allLeagueStats value
   */
  public TeamStats[] getAllLeagueStats() {
    return teamStatsLeague.toArray(new TeamStats[teamStatsLeague.size()]);
  }

  /**
   * Returns all (leagueElementGroup) stats in a array
   * 
   * @return The allLeagueElementGroupStats value
   */
  public TeamStats[] getAllLeagueElementGroupStats() {
    return teamStatsGroup.toArray(new TeamStats[teamStatsGroup.size()]);
  }

  /**
   * Returns the lowest available jersey number of the team
   * 
   * @return lowest available jersey number
   */
  private Integer getLowestAvailableJerseyNumber() {
    Integer counter = new Integer(1);
    while (jerseyNumbers.get(counter) != null) {
      counter += 1;
    }
    return counter;
  }

  /**
   * Adds player to the team
   * 
   * @param player The feature to be added to the Player attribute
   */
  public void addPlayer(Player player) {
    this.players.add(player);
    Integer jerseyNumber = getLowestAvailableJerseyNumber();
    this.jerseyNumbers.put(jerseyNumber, player);
    player.changedTeam();
    player.setTeam(this);
  }

  /**
   * Removes a player from the team
   * 
   * @param player
   */
  public void removePlayer(Player player) {
    this.players.remove(player);
    Integer jerseyNumber = getJerseyNumber(player);
    jerseyNumbers.remove(jerseyNumber);
    player.setTeam(null);
  }

  /**
   * Returns the jersey number of a player
   * 
   * @param player
   * @return the jersey number of a player
   */
  public Integer getJerseyNumber(Player player) {
    for (Iterator iter = jerseyNumbers.entrySet().iterator(); iter.hasNext();) {
      Map.Entry entry = (Map.Entry) iter.next();
      if (entry.getValue().equals(player)) {
        return (Integer) entry.getKey();
      }

    }
    return null;
  }

  /**
   * Retruns all players of this team
   * 
   * @return The players value
   */
  public Player[] getPlayers() {
    return players.toArray(new Player[players.size()]);
  }

  /**
   * Gets the playersTotalStats attribute of the Team object
   * 
   * @return The playersTotalStats value
   */
  public PlayerStats[] getPlayersTotalStats() {
    PlayerStats[] result = new PlayerStats[players.size()];
    for (int i = 0; i < result.length; i++) {
      result[i] = players.get(i).getPlayerStatsTotal();
    }
    return result;
  }

  /**
   * Returns team infos
   * 
   * @return The teamInfo value
   */
  public TeamInfo getTeamInfo() {
    return teamInfo;
  }

  /**
   * Returns team attributes
   * 
   * @return The teamAttributes value
   */
  public TeamAttributes getTeamAttributes() {
    return teamAttributes;
  }

  /**
   * Returns team attributes in a fake array
   * 
   * @return The teamStatsTotalArray value
   */
  public TeamAttributes[] getTeamAttributesArray() {
    TeamAttributes[] result = new TeamAttributes[1];
    result[0] = getTeamAttributes();
    return result;
  }

  /**
   * Description of the Method
   * 
   * @return Description of the Return Value
   */
  public String toString() {
    return this.teamInfo.getTeamName() + "(strength: " + this.teamAttributes.getOverall() + ")";
  }

  /**
   * Gets the trainingPlan attribute of the Team object
   * 
   * @return The trainingPlan value
   */
  public TrainingSchedule getTrainingSchedule() {
    return this.trainingSchedule;
  }

  /**
   * Returns accounting
   * 
   * @return Accounting
   */
  public Accounting getAccounting() {
    return accounting;
  }

  /**
   * Sets the training schedule
   * 
   * @param trainingSchedule
   */
  public void setTrainingSchedule(TrainingSchedule trainingSchedule) {
    this.trainingSchedule = trainingSchedule;
  }

  /**
   * Returns array of specific player attributes
   * 
   * @return PlayerAttribute array
   */
  public PlayerAttribute[] getSpecificFieldPlayerAttributes() {
    Player[] players = getPlayers();
    TreeMap<String, PlayerAttribute> result = new TreeMap<String, PlayerAttribute>();
    int playercount = 0;
    for (int i = 0; i < players.length; i++) {
      if (players[i].getPlayerAttributes().getPosition() != PlayerAttributes.POSITION_GOALTENDING) {
        playercount++;
        PlayerAttribute[] sa = players[i].getPlayerAttributes().getSpecificPlayerAttributes();
        for (int q = 0; q < sa.length; q++) {
          if (!result.containsKey(sa[q].getKey())) {
            result.put(sa[q].getKey(), new PlayerAttribute(sa[q].getKey(), sa[q].getValue()));
          } else {
            PlayerAttribute old = result.get(sa[q].getKey());
            old.setValue(old.getValue() + sa[q].getValue());
          }
        }
      }
    }
    return result.values().toArray(new PlayerAttribute[result.size()]);
  }

  /**
   * Returns every player on given position
   * 
   * @param position
   * @return Player array
   */
  public Player[] getPlayersByPosition(int position) {
    Vector<Player> result = new Vector<Player>();
    Player[] tmpPlayers = this.getPlayers();
    for (int i = 0; i < tmpPlayers.length; i++) {
      if (tmpPlayers[i].getPlayerAttributes().getPosition() == position) {
        result.add(tmpPlayers[i]);
      }
    }
    return result.toArray(new Player[result.size()]);
  }

  /**
   * AI Function: Retuns every player on given position which has a contract
   * that might be extended
   * 
   * @param position
   * @param year
   * @return Player array
   */
  public Player[] getAIExtendableContractsForYear(int position, Calendar year) {
    Vector<Player> result = new Vector<Player>();
    Player[] tmpPlayers = this.getPlayers();
    for (int i = 0; i < tmpPlayers.length; i++) {
      if (tmpPlayers[i].getPlayerAttributes().getPosition() == position) {
        if (tmpPlayers[i].getPlayerContractForDate(year) == null && !tmpPlayers[i].isOnTransferList() && !tmpPlayers[i].hasPlayerContractChangedThisYear()) {
          result.add(tmpPlayers[i]);
        }
      }
    }
    return result.toArray(new Player[result.size()]);
  }

  /**
   * Returns every player on given positon which has a contract for given year
   * 
   * @param position
   * @param year
   * @return Player array
   */
  public Player[] getPlayersByPositionWithContract(int position, Calendar year) {
    Vector<Player> result = new Vector<Player>();
    Player[] tmpPlayers = this.getPlayers();
    for (int i = 0; i < tmpPlayers.length; i++) {
      if (tmpPlayers[i].getPlayerAttributes().getPosition() == position) {
        if (tmpPlayers[i].getPlayerContractForDate(year) != null) {
          result.add(tmpPlayers[i]);
        }
      }
    }
    return result.toArray(new Player[result.size()]);
  }

  /**
   * Returns the number of players on given position which have a contract for
   * given year.
   * 
   * @param position
   * @param year
   * @return Number of players
   */
  public int getPlayersByPositionWithContractCount(int position, Calendar year) {
    return getPlayersByPositionWithContract(position, year).length;
  }

  /**
   * Returns true if team has a player on the transferlist on this position
   * 
   * @param position
   * @return True if team has players on transfer list
   */
  public boolean hasPlayersOnTransferList(int position) {
    Player[] players = getPlayers();
    for (int i = 0; i < players.length; i++) {
      if (players[i].getPlayerAttributes().getPosition() == position && players[i].isOnTransferList()) {
        return true;
      }
    }
    return false;
  }

  /**
   * Returns a specific goalie attributes
   * 
   * @return Array of PlayerAttribute
   */
  public PlayerAttribute[] getSpecificGoalieAttributes() {
    Player[] players = getPlayers();
    TreeMap<String, PlayerAttribute> result = new TreeMap<String, PlayerAttribute>();
    int playercount = 0;
    for (int i = 0; i < players.length; i++) {
      if (players[i].getPlayerAttributes().getPosition() == PlayerAttributes.POSITION_GOALTENDING) {
        playercount++;
        PlayerAttribute[] sa = players[i].getPlayerAttributes().getSpecificPlayerAttributes();
        for (int q = 0; q < sa.length; q++) {
          if (!result.containsKey(sa[q].getKey())) {
            result.put(sa[q].getKey(), new PlayerAttribute(sa[q].getKey(), sa[q].getValue()));
          } else {
            PlayerAttribute old = result.get(sa[q].getKey());
            old.setValue(old.getValue() + sa[q].getValue());
          }
        }
      }
    }

    return result.values().toArray(new PlayerAttribute[result.size()]);
  }

  /**
   * Returns the average player marketvalue of this team
   * 
   * @return Average player market value
   */
  public int getAveragePlayerMarketValue() {
    Player[] players = getPlayers();
    int total = 0;
    for (int i = 0; i < players.length; i++) {
      total += players[i].getMarketValue();
    }
    return total / players.length;
  }

  /**
   * Returns the arena of this team
   * 
   * @return Arena
   */
  public Arena getArena() {
    return infrastructure.getArena();
  }

  /**
   * Impact on Morale of every player
   * 
   * @param points
   */
  public void impactOnMorale(int points) {
    Player players[] = getPlayers();
    for (int i = 0; i < players.length; i++) {
      players[i].impactOnMorale(points);
    }
  }

  /**
   * Impact on Energy of every player
   * 
   * @param points
   */
  public void impactOnEnergy(int points) {
    Player players[] = getPlayers();
    for (int i = 0; i < players.length; i++) {
      players[i].impactOnEnergy(points);
    }
  }

  /**
   * Impact on Form of every player
   * 
   * @param points
   */
  public void impactOnForm(int points) {
    Player players[] = getPlayers();
    for (int i = 0; i < players.length; i++) {
      players[i].impactOnForm(points);
    }
  }

  /**
   * Returns the sponsoring
   * 
   * @return sponsoring.
   */
  public Sponsoring getSponsoring() {
    return sponsoring;
  }

  /**
   * Returns all prospects
   * 
   * @return Array of Player (prospects)
   */
  public Player[] getProspects() {
    return prospects.toArray(new Player[prospects.size()]);
  }

  /**
   * Add prospect to team
   * 
   * @param player
   */
  public void addProspect(Player player) {
    prospects.add(player);
    player.setTeam(this);
  }

  /**
   * Removes prospect from the team
   * 
   * @param player
   */
  public void removeProspect(Player player) {
    prospects.remove(player);
    player.setTeam(null);
  }

  /**
   * Returns the current number of injured players
   * 
   * @return Current number of injured players
   */
  public int getInjuriesCount() {
    Player[] players = getPlayers();
    int result = 0;
    for (int i = 0; i < players.length; i++) {
      if (players[i].getPlayerInjury() != null) {
        result++;
      }
    }
    return result;
  }

  /** Resets the match energy of every player */
  public void resetMatchEnergy() {
    Player[] players = getPlayers();
    for (int i = 0; i < players.length; i++) {
      players[i].getPlayerAttributes().resetMatchEnergy();
    }
  }

  /**
   * Returns the teams tactics
   * 
   * @return Tactics
   */
  public Tactics getTactics() {
    return tactics;
  }

  /**
   * Sets the theams tactics
   * 
   * @param tactics
   */
  public void setTactics(Tactics tactics) {
    this.tactics = tactics;
  }

  /**
   * Returns the infrastructure.
   * 
   * @return infrastructure
   */
  public Infrastructure getInfrastructure() {
    return infrastructure;
  }

  /**
   * Set infrastructure
   * 
   * @param infrastructure
   */
  public void setInfrastructure(Infrastructure infrastructure) {
    this.infrastructure = infrastructure;
  }
}