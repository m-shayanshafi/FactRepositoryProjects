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
  
package org.icehockeymanager.ihm.game.league.std;

import java.util.*;

import org.icehockeymanager.ihm.game.*;
import org.icehockeymanager.ihm.game.league.*;
import org.icehockeymanager.ihm.game.league.helper.*;
import org.icehockeymanager.ihm.game.league.std.events.*;
import org.icehockeymanager.ihm.game.scheduler.*;
import org.icehockeymanager.ihm.game.team.*;
import org.icehockeymanager.ihm.lib.*;
import org.w3c.dom.*;

/**
 * StdLeague Implementation of a standard League after the model of the lovely
 * country called Switzerland ;-)
 * <p>
 * <p>- Stdleague may have n teams.
 * <p>
 * <p>- The teams will play n1 times against each other (once at home, once
 * away) called regular season.
 * <p>
 * <p>- After the regular season the top teams play Playoffs, the bottom teams
 * Playouts.
 * <p>
 * <p>- The playoff/outs work with n2 "levels" (quarter, semi- and final or
 * more)
 * <p>
 * <p>- If 0 is passed by the constructor as starting level of playoffs/outs no
 * playoffs will be played are played.
 * 
 * @author Bernhard von Gunten
 * @created December, 2001
 */
public class StdLeague extends League {

  static final long serialVersionUID = 7551130701141362832L;

  /** Game calendar helper to place matches in the calendar */
  private SchedulerHelper SchedulerHelper = null;

  /** Game calendar helper to place Playoff matches in the calendar */
  private SchedulerHelper SchedulerPlayoffHelper = null;

  /** Game calendar helper to place Playouts matches in the calendar */
  private SchedulerHelper SchedulerPlayoutHelper = null;

  /** Gamedays while regular season */
  private final static String regularSeasonGameDays = "TU SA";

  /** Gamedays while playoff/outs */
  private final static String playoffGameDays = "TU TH SA";

  /** Regular season */
  private StdRegularSeason regularSeason = null;

  /** Playoffs */
  private StdPlayoffs[] playoffs = null;

  /** Playouts */
  private StdPlayoffs[] playouts = null;

  /** Other Elements (i.E. RelegationPlayoffs) are grouped in the playouts group ! */
  private StdPlayoffs relegationPlayoffs = null;

  /** Regular Seasons group */
  private LeagueElementGroup regularSeasonGroup = null;

  /** Playoffs group */
  private LeagueElementGroup playoffsGroup = null;

  /** Playouts group */
  private LeagueElementGroup playoutsGroup = null;

  /** Playofflevels */
  private int playoffLevels;

  /** Playoff are bestOf n */
  private int playoffsBestOf;

  /** Playoutlevels */
  private int playoutLevels;

  /** Playouts are bestOf n */
  private int playoutsBestOf;

  /** How many times play teams against each other (1 = 1 home and 1 away game) */
  private int multiRounds;

  /** Helper to mark if first place is known */
  private boolean firstPlaceKnown = false;

  /** Helper to mark if last place is known */
  private boolean lastPlaceKnown = false;

  /**
   * Constructor with leaguename, rank in the owner, teams, multirounds,
   * playoffLevels, playoffbestOf, playoutLevels, playoutBestOf. Doesn't create
   * new matches ! See reset function.
   * 
   * @param leagueName
   *          The name of this league
   * @param ranking
   *          The rank of this league (in the leagueOwner)
   * @param teams
   *          The teams of this league
   * @param multiRounds
   *          The nr of rounds played in the regular season
   * @param playoffLevels
   *          The nr of levels played in the whole playoffs
   * @param playoffsBestOf
   *          Best of n playOffs
   * @param playoutLevels
   *          The nr of levels played in the whole playouts
   * @param playoutsBestOf
   *          Best of n playOuts
   */
  public StdLeague(String leagueName, int ranking, Team[] teams, int multiRounds, int playoffLevels, int playoffsBestOf, int playoutLevels, int playoutsBestOf) {
    super(leagueName, ranking, teams);

    this.playoffLevels = playoffLevels;
    this.playoffsBestOf = playoffsBestOf;

    this.playoutLevels = playoutLevels;
    this.playoutsBestOf = playoutsBestOf;

    this.playoffs = new StdPlayoffs[playoffLevels];
    this.playouts = new StdPlayoffs[playoutLevels];
    this.multiRounds = multiRounds;

  }

  /**
   * StdLeague constructor with XML Element & teams
   * 
   * @param element
   *          Element
   * @param teams
   *          Team[]
   * @return StdLeague
   */
  public static StdLeague createLeague(Element element, Team[] teams) {
    String myName = element.getAttribute("NAME");
    int myRank = ToolsXML.getIntAttribute(element, "RANK");
    int myMultiRounds = ToolsXML.getIntAttribute(element, "MULTIROUNDS");
    int myPlayoffLevels = ToolsXML.getIntAttribute(element, "PLAYOFFLEVELS");
    int myPlayoffBestof = ToolsXML.getIntAttribute(element, "PLAYOFFBESTOF");
    int myPlayoutLevels = ToolsXML.getIntAttribute(element, "PLAYOUTLEVELS");
    int myPlayoutBestof = ToolsXML.getIntAttribute(element, "PLAYOUTBESTOF");

    Vector<Team> teamsV = new Vector<Team>();
    Element leagueTeamsNode = ToolsXML.getFirstSubElement(element, "LEAGUETEAMS");
    NodeList leagueTeams = leagueTeamsNode.getElementsByTagName("LEAGUETEAM");
    int count = leagueTeams.getLength();
    for (int i = 0; i < count; i++) {
      int key = Integer.valueOf(((Element) leagueTeams.item(i)).getAttribute("KEY")).intValue();
      for (int q = 0; q < teams.length; q++) {
        if (teams[q].getKey() == key) {
          teamsV.add(teams[q]);
          break;
        }
      }
    }

    Team[] myTeams = teamsV.toArray(new Team[teamsV.size()]);

    return new StdLeague(myName, myRank, myTeams, myMultiRounds, myPlayoffLevels, myPlayoffBestof, myPlayoutLevels, myPlayoutBestof);

  }

  /**
   * Adds this League to an existing XML Element
   * 
   * @param parent
   *          Element
   */
  public void addAsElementToParent(Element parent) {
    Element element = parent.getOwnerDocument().createElement("LEAGUE");
    element.setAttribute("NAME", this.getName());
    element.setAttribute("RANK", String.valueOf(this.getRank()));
    element.setAttribute("MULTIROUNDS", String.valueOf(this.getMultiRounds()));
    element.setAttribute("PLAYOFFLEVELS", String.valueOf(this.getPlayoffLevels()));
    element.setAttribute("PLAYOFFBESTOF", String.valueOf(this.getPlayoffsBestOf()));
    element.setAttribute("PLAYOUTLEVELS", String.valueOf(this.getPlayoutLevels()));
    element.setAttribute("PLAYOUTBESTOF", String.valueOf(this.getPlayoutsBestOf()));

    Element teamsElement = element.getOwnerDocument().createElement("LEAGUETEAMS");
    // Players
    Team[] teams = getTeams();
    for (int i = 0; i < teams.length; i++) {
      Element player = element.getOwnerDocument().createElement("LEAGUETEAM");
      player.setAttribute("KEY", String.valueOf(teams[i].getKey()));
      teamsElement.appendChild(player);
    }
    element.appendChild(teamsElement);

    parent.appendChild(element);

  }

  /**
   * Gets the multiRounds attribute of the StdLeague object
   * 
   * @return The multiRounds value
   */
  public int getMultiRounds() {
    return multiRounds;
  }

  /**
   * Gets the playoffLevels attribute of the StdLeague object
   * 
   * @return The playoffLevels value
   */
  public int getPlayoffLevels() {
    return playoffLevels;
  }

  /**
   * Gets the playoffsBestOf attribute of the StdLeague object
   * 
   * @return The playoffsBestOf value
   */
  public int getPlayoffsBestOf() {
    return playoffsBestOf;
  }

  /**
   * Gets the playoutLevels attribute of the StdLeague object
   * 
   * @return The playoutLevels value
   */
  public int getPlayoutLevels() {
    return playoutLevels;
  }

  /**
   * Gets the playoutsBestOf attribute of the StdLeague object
   * 
   * @return The playoutsBestOf value
   */
  public int getPlayoutsBestOf() {
    return playoutsBestOf;
  }

  /** Resets league, and generates the regular Season */
  public void newSeason() {
    this.firstPlaceKnown = false;
    this.lastPlaceKnown = false;
    this.SchedulerHelper = GameController.getInstance().getScenario().getScheduler().getSchedulerHelper();
    this.SchedulerHelper.setGameDays(regularSeasonGameDays);

    // Create regular season, playoffs and playouts
    regularSeason = new StdRegularSeason(0, StdConstants.KEY_REGUALR_SEASON, this);

    for (int i = 0; i < this.playoffs.length; i++) {
      this.playoffs[i] = new StdPlayoffs(this.playoffs.length - i - 1, StdConstants.KEY_LEAGUE_PLAYOFFS, this, playoffsBestOf, i);
    }

    for (int i = 0; i < this.playouts.length; i++) {
      this.playouts[i] = new StdPlayoffs(this.playouts.length - i - 1, StdConstants.KEY_LEAGUE_PLAYOUTS, this, playoutsBestOf, i);
    }

    // Reset all groups
    regularSeasonGroup = new LeagueElementGroup(this, StdConstants.KEY_REGUALR_SEASON);
    playoffsGroup = new LeagueElementGroup(this, StdConstants.KEY_LEAGUE_PLAYOFFS);

    playoutsGroup = new LeagueElementGroup(this, StdConstants.KEY_LEAGUE_PLAYOUTS);

    // Add leagueElement to leagueElement groups
    regularSeasonGroup.addLeagueElement(regularSeason);

    for (int i = 0; i < this.playoffs.length; i++) {
      playoffsGroup.addLeagueElement(this.playoffs[i]);
    }
    for (int i = 0; i < this.playouts.length; i++) {
      playoutsGroup.addLeagueElement(this.playouts[i]);
    }

    if (relegationPlayoffs != null) {
      playoutsGroup.addLeagueElement(relegationPlayoffs);
    }

    // And now create the regular Season ...
    this.generateRegularSeason();

  }

  /**
   * Returns the owner of this league
   * 
   * @return The stdLeagueOwner value
   */
  private StdLeagueOwner getStdLeagueOwner() {
    return (StdLeagueOwner) leagueOwner;
  }

  /**
   * Creates the matches of the regular season, and places the matches in the
   * game calendar. Important: Also places a event in the calendar, which will
   * create playoff/outs (if they are wanted, else directly the end of them).
   */
  public void generateRegularSeason() {
    regularSeason.init(teams, multiRounds, SchedulerHelper);

    // Create two new calendar helper, so playoff & playout games can be on the
    // same day
    Calendar day = SchedulerHelper.getNextFreeGameDay();

    SchedulerPlayoffHelper = GameController.getInstance().getScenario().getScheduler().getSchedulerHelperByDay(day);
    this.SchedulerPlayoffHelper.setGameDays(playoffGameDays);

    SchedulerPlayoutHelper = GameController.getInstance().getScenario().getScheduler().getSchedulerHelperByDay(day);
    this.SchedulerPlayoutHelper.setGameDays(playoffGameDays);

    // Now add the first playoffs or the end of the regular season;
    if (playoffLevels > 0) {
      GameController.getInstance().getScenario().getScheduler().addEvent(new StdNewPlayoffLevelEvent(this, SchedulerPlayoffHelper.getNextFreeGameDay(), playoffLevels - 1, this));
    } else {
      GameController.getInstance().getScenario().getScheduler().addEvent(new StdFirstPlaceKnownEvent(this, SchedulerPlayoffHelper.getNextFreeGameDay(), this));
    }

    // Now add the first playouts or the end of the regular season;
    if (playoutLevels > 0) {
      GameController.getInstance().getScenario().getScheduler().addEvent(new StdNewPlayoutLevelEvent(this, SchedulerPlayoutHelper.getNextFreeGameDay(), playoutLevels - 1, this));
    } else {
      GameController.getInstance().getScenario().getScheduler().addEvent(new StdLastPlaceKnownEvent(this, SchedulerPlayoutHelper.getNextFreeGameDay(), this));
      this.setLastPlaceKnown();
    }

  }

  /**
   * Creates the level'th round of the playoffs
   * <p>- If there is only one round to play (final) or the it's the first
   * round it takes the teams of the league directly, else out of the winners of
   * the last playoff round.
   * 
   * @param level
   *          Level of playoffs
   */
  public void generatePlayoffsLevel(int level) {
    int teamsCount = (int) Math.pow(2, level + 1);

    if (level == playoffs.length - 1) {
      // Take teams from regular Season
      Team[] tmp = new Team[teamsCount];
      for (int i = 0; i < teamsCount; i++) {
        tmp[i] = regularSeason.getLeagueStandingsTeams()[i];
      }
      playoffs[level].init(tmp, SchedulerPlayoffHelper);
    } else {
      // Take them of the last playoff round
      playoffs[level].init(playoffs[level + 1].getWinners(), SchedulerPlayoffHelper);
    }

    // Now add the next playoffs or the end of the playoffs;
    if (level > 0) {
      GameController.getInstance().getScenario().getScheduler().addEvent(new StdNewPlayoffLevelEvent(this, SchedulerPlayoffHelper.getNextFreeGameDay(), level - 1, this));
    } else {
      GameController.getInstance().getScenario().getScheduler().addEvent(new StdFirstPlaceKnownEvent(this, SchedulerPlayoffHelper.getNextFreeGameDay(), this));
      this.setFirstPlaceKnown();
    }
  }

  /**
   * Like playoffs, but different ;-))
   * 
   * @param level
   *          The level of playouts
   */
  public void generatePlayoutsLevel(int level) {
    int teamsCount = (int) Math.pow(2, level + 1);

    if (level == playouts.length - 1) {
      // Take teams from regular Season
      Team[] tmp = new Team[teamsCount];
      for (int i = teams.length - teamsCount; i < teams.length; i++) {
        tmp[i - (teams.length - teamsCount)] = regularSeason.getLeagueStandingsTeams()[i];
      }
      playouts[level].init(tmp, SchedulerPlayoutHelper);
    } else {
      // Take them of the last playout round
      playouts[level].init(playouts[level + 1].getLosers(), SchedulerPlayoutHelper);
    }

    // Now add the next playouts or the end of the playouts;
    if (level > 0) {
      GameController.getInstance().getScenario().getScheduler().addEvent(new StdNewPlayoutLevelEvent(this, SchedulerPlayoutHelper.getNextFreeGameDay(), level - 1, this));
    } else {
      GameController.getInstance().getScenario().getScheduler().addEvent(new StdLastPlaceKnownEvent(this, SchedulerPlayoutHelper.getNextFreeGameDay(), this));
      this.setLastPlaceKnown();
    }
  }

  /**
   * Returns the first placed team of this league (called by leagueOwner to i.E.
   * start the relegationPlayoffsfs
   * 
   * @return The leagueFirstTeam value
   */
  public Team getFirstPlacedTeam() {
    if (playoffLevels > 0) {
      Team[] winner = playoffs[0].getWinners();
      return winner[0];
    } else {
      return regularSeason.getLeagueStandingsTeams()[0];
    }
  }

  /**
   * Returns the last placed team of this league (called by leagueOwner to i.E.
   * start the relegationPlayoffsfs
   * 
   * @return The leagueLastTeam value
   */
  public Team getLastPlacedTeam() {
    if (playoutLevels > 0) {
      Team[] loser = playouts[0].getLosers();
      return loser[0];
    } else {
      return regularSeason.getLeagueStandingsTeams()[teams.length - 1];
    }
  }

  /**
   * Returns the last x teams from the current regular (!!) season.
   * 
   * @param last
   *          int
   * @return Team
   */
  public Team getLastXTeam(int last) {
    return regularSeason.getLeagueStandingsTeams()[teams.length - (last + 1)];
  }

  /**
   * Checks if first team (winner) is known (so we could start the
   * relegationPlayoffs
   * 
   * @return The firstTeamKnown value
   */
  public boolean isFirstTeamKnown() {
    return firstPlaceKnown;
  }

  /**
   * Checks if last team (loser) is known (so we could start the
   * relegationPlayoffs
   * 
   * @return The lastTeamKnown value
   */
  public boolean isLastTeamKnown() {
    return lastPlaceKnown;
  }

  /** Sets playoutsFinished flag to true and informes the owner */
  public void setLastPlaceKnown() {
    this.lastPlaceKnown = true;
    getStdLeagueOwner().leaguePartFinished(this);
  }

  /** Sets playoffFinished flag to true and informes the owner */
  public void setFirstPlaceKnown() {
    this.firstPlaceKnown = true;
    getStdLeagueOwner().leaguePartFinished(this);
  }

  /**
   * Add playoffs (League Playoffs) to this league
   * 
   * @param relegationPlayoffs
   *          Relegation playoffs
   */
  public void setRelegationPlayoffsElement(StdPlayoffs relegationPlayoffs) {
    this.relegationPlayoffs = relegationPlayoffs;
  }

  /**
   * Returns the relegation playoffs
   * 
   * @return StdPlayoffs
   */
  public StdPlayoffs getRelegationPlayoffsElement() {
    return relegationPlayoffs;
  }

  /**
   * Returns all leagueElements
   * 
   * @return The leagueElements value
   */
  public Vector<LeagueElement> getLeagueElements() {
    Vector<LeagueElement> result = new Vector<LeagueElement>();
    // Regular Season
    result.add(regularSeason);
    // Playoffs
    for (int i = 0; i < playoffs.length; i++) {
      result.add(playoffs[i]);
    }

    // Playouts
    for (int i = 0; i < playouts.length; i++) {
      result.add(playouts[i]);
    }

    if (relegationPlayoffs != null) {
      result.add(relegationPlayoffs);
    }

    java.util.Collections.sort(result);

    return result;
  }

  /**
   * Returns the LeagueElementGroups of this League
   * 
   * @return The leagueElementGroups value
   */
  public Vector<LeagueElementGroup> getLeagueElementGroups() {
    Vector<LeagueElementGroup> result = new Vector<LeagueElementGroup>();
    if (regularSeasonGroup.getLeagueElementCount() > 0) {
      result.add(regularSeasonGroup);
    }
    if (playoffsGroup.getLeagueElementCount() > 0) {
      result.add(playoffsGroup);
    }
    if (playoutsGroup.getLeagueElementCount() > 0) {
      result.add(playoutsGroup);
    }
    return result;
  }

}
