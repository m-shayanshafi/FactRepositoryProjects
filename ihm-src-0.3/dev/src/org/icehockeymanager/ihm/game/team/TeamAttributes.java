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

import org.icehockeymanager.ihm.game.player.*;
import org.icehockeymanager.ihm.lib.*;

/**
 * Team Attributes is a helper to return the overall attributes from a team.
 * 
 * @author Bernhard von Gunten & Arik Dasen
 * @created December, 2001
 */
public class TeamAttributes extends IhmCustomComparator {
  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 3546923588905939251L;

  /** Sort by overall atribute */
  public final static int SORT_OVERALL = 1;

  /** Sort by offense atribute */
  public final static int SORT_OFFENSE = 2;

  /** Sort by defense atribute */
  public final static int SORT_DEFENSE = 3;

  /** Sort by goal tending atribute */
  public final static int SORT_GOALTENDING = 4;

  /** Owner of these attributes */
  private Team team;

  /**
   * Create a instance of this helper class
   * 
   * @param team
   *          Owner of these attributes.
   */
  public TeamAttributes(Team team) {
    this.team = team;
  }

  /**
   * Returns the offense attribute
   * 
   * @return The offense value
   */
  public int getOffense() {
    Player[] players = team.getPlayers();
    int total = 0;
    int count = 0;
    for (int i = 0; i < players.length; i++) {
      if (players[i].getPlayerAttributes().getPosition() == PlayerAttributes.POSITION_CENTER || players[i].getPlayerAttributes().getPosition() == PlayerAttributes.POSITION_WING) {
        total += players[i].getPlayerAttributes().getOverallAverage();
        count++;
      }
    }
    return total / count;
  }

  /**
   * Returns the defense attribute
   * 
   * @return The defense value
   */
  public int getDefense() {
    Player[] players = team.getPlayers();
    int total = 0;
    int count = 0;
    for (int i = 0; i < players.length; i++) {
      if (players[i].getPlayerAttributes().getPosition() == PlayerAttributes.POSITION_DEFENSE) {
        total += players[i].getPlayerAttributes().getOverallAverage();
        count++;
      }
    }
    return total / count;
  }

  /**
   * Returns the goal tending attribute
   * 
   * @return The goalTending value
   */
  public int getGoalTending() {
    Player[] players = team.getPlayers();
    int total = 0;
    int count = 0;
    for (int i = 0; i < players.length; i++) {
      if (players[i].getPlayerAttributes().getPosition() == PlayerAttributes.POSITION_GOALTENDING) {
        total += players[i].getPlayerAttributes().getOverallAverage();
        count++;
      }
    }
    return total / count;
  }

  /**
   * Returns the overall attribute
   * 
   * @return The overall value
   */
  public int getOverall() {
    return (getGoalTending() + getDefense() + getOffense()) / 3;
  }

  /**
   * Returns the owner of this attributes
   * 
   * @return The team value
   */
  public Team getTeam() {
    return team;
  }

  /**
   * returns sort value considering sortCriteria
   * 
   * @return The sortValue value
   */
  protected double getSortValue() {
    switch (sortCriteria) {
    case SORT_OVERALL:
      return getOverall();
    case SORT_OFFENSE:
      return getOffense();
    case SORT_DEFENSE:
      return getDefense();
    case SORT_GOALTENDING:
      return getGoalTending();
    default:
      return getOverall();
    }
  }

}
