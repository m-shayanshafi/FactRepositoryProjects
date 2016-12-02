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
import org.icehockeymanager.ihm.game.league.std.events.*;
import org.icehockeymanager.ihm.game.scheduler.*;
import org.icehockeymanager.ihm.game.team.*;
import org.icehockeymanager.ihm.lib.*;
import org.w3c.dom.*;

/**
 * StdLeagueOwner Implementation of a standard LeagueOwner working with
 * StdLeagues.
 * <p> - After the needed leagues are finished, the last placed of an upper and
 * the winner of a bottom league will play "League playoffs" against each other
 * (and switch places if needed)
 * <p> - If 0 is passed by the constructor as starting level of
 * relegationPlayoffs none are played.
 * 
 * @author Bernhard von Gunten
 * @created December, 2001
 */
public class StdLeagueOwner extends LeagueOwner {

  static final long serialVersionUID = 572301998289500180L;

  /** Indicates if relegationPlayoffs will be played */
  private boolean playRelegationPlayoffs;

  /** RelegationPlayoffs are bestOf n */
  private int relegationPlayoffsBestOf;

  /** Gamedays while playoff/outs */
  private final static String relegationPlayoffsGameDays = "TU TH SA";

  /**
   * Creates the StdLeagueOwner with the leagues passed by owner
   * 
   * @param name
   *          Name of this leagueOwner
   * @param leagues
   *          Leagues of this leagueOwner
   * @param playRelegationPlayoffs
   *          Play relegationPlayoffs (true) or not (false)
   * @param relegationPlayoffsBestOf
   *          Best of n relegationPlayoffs
   */
  public StdLeagueOwner(String name, League[] leagues, boolean playRelegationPlayoffs, int relegationPlayoffsBestOf) {
    super(name, leagues);
    this.playRelegationPlayoffs = playRelegationPlayoffs;
    this.relegationPlayoffsBestOf = relegationPlayoffsBestOf;
  }

  /**
   * Creates a new LeagueOwner by XML Element and teams
   * 
   * @param element
   *          Element
   * @param teams
   *          Team[]
   * @return StdLeagueOwner
   */
  public static StdLeagueOwner createLeagueOwner(Element element, Team[] teams) {
    String myName = element.getAttribute("NAME");
    boolean myPlayRelegationPlayoffs = Boolean.valueOf(element.getAttribute("PLAYLEAGUEPLAYOFFS")).booleanValue();
    int myRelegationPlayoffsBestOf = ToolsXML.getIntAttribute(element, "LEAGUEPLAYOFFSBESTOF");

    Vector<League> leaguesV = new Vector<League>();
    Element leagueTeamsNode = ToolsXML.getFirstSubElement(element, "LEAGUES");
    NodeList leagueTeams = leagueTeamsNode.getElementsByTagName("LEAGUE");
    int count = leagueTeams.getLength();
    for (int i = 0; i < count; i++) {
      leaguesV.add(StdLeague.createLeague((Element) leagueTeams.item(i), teams));
    }

    StdLeague[] leagues = leaguesV.toArray(new StdLeague[leaguesV.size()]);

    return new StdLeagueOwner(myName, leagues, myPlayRelegationPlayoffs, myRelegationPlayoffsBestOf);
  }

  /**
   * Adds this LeagueOwner to an existing XML Element.
   * 
   * @param parent
   *          Element
   */
  public void addAsElementToParent(Element parent) {
    Element element = parent.getOwnerDocument().createElement("LEAGUEOWNER");
    element.setAttribute("CLASS", "STDLEAGUEOWNER");
    element.setAttribute("NAME", this.getName());
    element.setAttribute("PLAYLEAGUEPLAYOFFS", String.valueOf(this.getPlayRelegationPlayoffs()));
    element.setAttribute("LEAGUEPLAYOFFSBESTOF", String.valueOf(this.getRelegationPlayoffsBestOf()));

    Element leaguesElement = element.getOwnerDocument().createElement("LEAGUES");
    League[] leagues = getLeagues();
    for (int i = 0; i < leagues.length; i++) {
      leagues[i].addAsElementToParent(leaguesElement);
    }
    element.appendChild(leaguesElement);

    parent.appendChild(element);
  }

  /**
   * Gets the playRelegationPlayoffs attribute of the StdLeagueOwner object
   * 
   * @return The playRelegationPlayoffs value
   */
  public boolean getPlayRelegationPlayoffs() {
    return playRelegationPlayoffs;
  }

  /**
   * Gets the relegationPlayoffsBestOf attribute of the StdLeagueOwner object
   * 
   * @return The relegationPlayoffsBestOf value
   */
  public int getRelegationPlayoffsBestOf() {
    return relegationPlayoffsBestOf;
  }

  /** Called each time when playoff/outs are finished within a league 
   * @param stdLeague */
  public void leaguePartFinished(StdLeague stdLeague) {
    // Check for possible leagueplayoffstart
    if (playRelegationPlayoffs) {
      checkForRelegationPlayoffs();
    }
  }

  /** Check if relegationPlayoffs may allready be started (and starts them) */
  private void checkForRelegationPlayoffs() {
    StdLeague[] myLeagues = getStdLeagues();

    // Seek trough all possible RelegationPlayoffs and look if both leagues are
    // ready
    for (int i = 0; i < myLeagues.length; i++) {
      if (myLeagues[i].getRelegationPlayoffsElement() != null && !myLeagues[i].getRelegationPlayoffsElement().isAnnounced()) {
        StdLeague oppLeague = getFirstLeagueInRank(myLeagues[i].getRank() + 1);
        if (myLeagues[i].isLastTeamKnown() && oppLeague.isFirstTeamKnown()) {
          Team[] teams = new Team[2];
          teams[0] = myLeagues[i].getLastPlacedTeam();
          teams[1] = oppLeague.getFirstPlacedTeam();
          SchedulerHelper myHelper = GameController.getInstance().getScenario().getScheduler().getSchedulerHelperByToday();
          myHelper.setGameDays(relegationPlayoffsGameDays);
          myLeagues[i].getRelegationPlayoffsElement().init(teams, myHelper);
          GameController.getInstance().getScenario().getScheduler().addEvent(new StdRelegationPlayoffsFinishedEvent(this, myHelper.getNextFreeGameDay(), this, myLeagues[i].getRelegationPlayoffsElement()));
        }
      }
    }
  }

  /** Resets all teams and the relegationPlayoffs */
  public void newSeason() {
    for (int n = 0; n < leagues.length; n++) {
      int sameRankCount = this.getLeaguesCountInSameRank(leagues[n].getRank());
      int sameRankCountNextRank = this.getLeaguesCountInSameRank(leagues[n].getRank() + 1);
      if (sameRankCount == 1 && sameRankCountNextRank == 1) {
        StdLeague stdLeague = (StdLeague) leagues[n];
        stdLeague.setRelegationPlayoffsElement(new StdPlayoffs(999, StdConstants.KEY_LEAGUE_LEAGUE_PLAYOFFS, leagues[n], relegationPlayoffsBestOf, 0));
      }
      leagues[n].newSeason();
    }
  }

  /** Season has ended, switch teams if needed */
  public void endSeason() {
    StdLeague[] myLeagues = getStdLeagues();

    // Switch teams with relegationPlayoffs
    for (int i = 0; i < myLeagues.length; i++) {
      StdPlayoffs relegationPlayoffs = myLeagues[i].getRelegationPlayoffsElement();
      if (relegationPlayoffs != null) {
        if (!myLeagues[i].isTeamOfLeague(relegationPlayoffs.getWinners()[0])) {
          myLeagues[i].addTeamToAdd(relegationPlayoffs.getWinners()[0]);
          myLeagues[i].addTeamToRemove(relegationPlayoffs.getLosers()[0]);
          StdLeague oppLeague = getFirstLeagueInRank(myLeagues[i].getRank() + 1);
          oppLeague.addTeamToRemove(relegationPlayoffs.getWinners()[0]);
          oppLeague.addTeamToAdd(relegationPlayoffs.getLosers()[0]);
        }
      }
    }

    // Switch teams with no leagueplayoffs
    int ranks = this.getRanksCount();
    for (int i = 0; i < ranks; i++) {
      // Seek for leagues without league playoffs
      if (getFirstLeagueInRank(i).getRelegationPlayoffsElement() == null) {
        StdLeague[] upperLeagues = this.getLeaguesByRank(i);
        StdLeague[] lowerLeagues = this.getLeaguesByRank(i + 1);

        int teamsExchange = lowerLeagues.length;
        int teamsPerUpperLeague = (teamsExchange / upperLeagues.length);
        int counter = 0;
        for (int q = 0; q < upperLeagues.length; q++) {
          for (int n = 0; n < teamsPerUpperLeague; n++) {
            Team winner = lowerLeagues[counter].getFirstPlacedTeam();
            Team loser = upperLeagues[q].getLastXTeam(n);
            upperLeagues[q].addTeamToRemove(loser);
            upperLeagues[q].addTeamToAdd(winner);
            lowerLeagues[counter].addTeamToRemove(winner);
            lowerLeagues[counter].addTeamToAdd(loser);
            counter++;
          }
        }
      }
    }

    for (int i = 0; i < myLeagues.length; i++) {
      myLeagues[i].refreshTeamList();
    }
  }

  /**
   * Returns all leagues
   * 
   * @return The stdLeagues value
   */
  private StdLeague[] getStdLeagues() {
    StdLeague[] result = new StdLeague[leagues.length];
    for (int i = 0; i < leagues.length; i++) {
      result[i] = (StdLeague) leagues[i];
    }
    return result;
  }

  /**
   * Returns the number of league "ranks" in this LeagueOwner (IE First, Second +
   * Third League West / East are three ranks)
   * 
   * @return int
   */
  private int getRanksCount() {
    int result = 0;
    for (int i = 0; i < getStdLeagues().length; i++) {
      if (getLeaguesCountInSameRank(i) > 0) {
        result++;
      }
    }
    return result;
  }

  /**
   * Returns the number of leagues in the same league rank
   * 
   * @param rank
   *          int
   * @return int
   */
  private int getLeaguesCountInSameRank(int rank) {
    int result = 0;
    for (int i = 0; i < getStdLeagues().length; i++) {
      if (getStdLeagues()[i].getRank() == rank) {
        result++;
      }
    }
    return result;
  }

  /**
   * Returns an array of Leagues in the same rank
   * 
   * @param rank
   *          int
   * @return StdLeague[]
   */
  private StdLeague[] getLeaguesByRank(int rank) {
    Vector<League> leaguesV = new Vector<League>();
    for (int i = 0; i < getStdLeagues().length; i++) {
      if (getStdLeagues()[i].getRank() == rank) {
        leaguesV.add(getStdLeagues()[i]);
      }
    }
    return leaguesV.toArray(new StdLeague[leaguesV.size()]);
  }


  /**
   * Returns the first League in given rank.
   * 
   * @param rank
   *          int
   * @return StdLeague
   */
  private StdLeague getFirstLeagueInRank(int rank) {
    for (int i = 0; i < getStdLeagues().length; i++) {
      if (getStdLeagues()[i].getRank() == rank) {
        return getStdLeagues()[i];
      }
    }
    return null;
  }

}
