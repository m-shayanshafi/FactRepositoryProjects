/* RefereeData.java

   Copyright (C) 2001  Yu Zhang

   This program is free software; you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation; either version 2 of the License, or
   (at your option) any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this program; if not, write to the 
   Free Software Foundation, Inc., 
   59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
   
   With modifications by Vadim Kyrylov 
   January 2006
*/

package soccer.common;

import java.util.*;

/**
 * Provides referee data for server informing all clients.
 *
 * @author Yu Zhang
 */
public class RefereeData implements Data
{

  /**
   * play period string array for displaying. 
   * periods[PRE_GAME] = "preGame", periods[FIRST_HALF] = "firstHalf",
   * periods[HALF_TIME] = "halfTime", periods[SECOND_HALF] = "secondHalf".
   */
  public static final String[] 
  			periods = {"No Game", "Pre-Game", "1st Half",
                       "Half Time", "2nd Half"};
  /**
   * play period identifiers.
   */
  public static final int NO_GAME      = 0;
  public static final int PRE_GAME     = 1;
  public static final int FIRST_HALF   = 2;
  public static final int HALF_TIME    = 3;
  public static final int SECOND_HALF  = 4;

  
  /**
   * play mode string array for displaying. 
   * periods[PRE_GAME] = "preGame", periods[FIRST_HALF] = "firstHalf",
   * periods[HALF_TIME] = "halfTime", periods[SECOND_HALF] = "secondHalf".
   */
  public static final String[] 
  				modes = {"Before KickOff", "Kick Off L", "Kick Off R",
                        "Throw In L", "Throw In R", "Corner Kick L",
						"Corner Kick R", "Goal Kick L", "Goal Kick R",
						"Offside L", "Offside R", "Play On" };

 /**
   * play mode identifiers.
   */
  public static final int BEFORE_KICK_OFF = 0;
  public static final int KICK_OFF_L      = 1;
  public static final int KICK_OFF_R      = 2;
  public static final int THROW_IN_L      = 3;
  public static final int THROW_IN_R      = 4;
  public static final int CORNER_KICK_L   = 5;
  public static final int CORNER_KICK_R   = 6;
  public static final int GOAL_KICK_L     = 7;
  public static final int GOAL_KICK_R     = 8;
  public static final int OFFSIDE_L       = 9;
  public static final int OFFSIDE_R       = 10;
  public static final int PLAY_ON         = 11;
  

  /**
   * the current simulation step.
   */
  public int time;
  /**
   * the current game period.
   */
  public int period;
  /**
   * the current game mode.
   */
  public int mode;
  /**
   * the sid who should continue the game.
   */
  public char sideToContinue;
  /**
   * left team name.
   */
  public String leftName;
  /**
   * the score of the left team.
   */
  public int score_L = 0;
  public int total_score_L = 0;
  /**
   * right team name.
   */
  public String rightName;
  /**
   * the score of the right team.
   */
  public int score_R = 0;
  public int total_score_R = 0;
  
  // game counter
  public int game;

  // number of games to play
  public int games;

  /**
   * Constructs an empty RefereeData for reading from an UDP packet.
   */
  public RefereeData()
  {
    this.time = 0;
    this.period = 0;
    this.mode = 0;
    this.sideToContinue = '?';
    this.leftName = "";
    this.score_L = 0;
    this.total_score_L = 0;
    this.rightName = "";
    this.score_R = 0;
    this.total_score_R = 0;
    this.game = 1;
    this.games = 1;
  }
  

  public RefereeData(int time, int period, int mode, char sideToContinue,
			           String leftName, int score_L, int total_score_L, 
			           String rightName, int score_R, int total_score_R, 
			           							int game, int games)
  {
    this.time = time;
    this.period = period;
    this.mode = mode;
    this.sideToContinue = sideToContinue;
    this.leftName = leftName;
    this.score_L = score_L;
    this.total_score_L = total_score_L;
    this.rightName = rightName;
    this.score_R = score_R;
    this.total_score_R = total_score_R;
    this.game = game;  
    this.games = games;  
  } 
  
  // Load its data content from a string.
  public void readData(StringTokenizer st)
  {
    // Get the time.
    time = Integer.parseInt(st.nextToken()); 
    
    // Get the " "
    st.nextToken();         

    // Get the period.
    period = Integer.parseInt(st.nextToken());
    
    // Get the " "
    st.nextToken();         

    // Get the mode.
    mode = Integer.parseInt(st.nextToken());
    
    // Get the " "
    st.nextToken();         
    
    // Get the connection type.
    sideToContinue = st.nextToken().charAt(0);
    
    // Get the " "
    st.nextToken();         
    
    // Get the left team name.
    leftName = st.nextToken();
    
    // Get the " "
    st.nextToken();         
    
    // Get the left team's score.
    score_L = Integer.parseInt(st.nextToken());
    
    // Get the " "
    st.nextToken();         

    // Get the left team's score.
    total_score_L = Integer.parseInt(st.nextToken());
    
    // Get the " "
    st.nextToken();         

    // Get the right team name.
    rightName = st.nextToken();
    
    // Get the " "
    st.nextToken();         

    // Get the right team's score.
    score_R = Integer.parseInt(st.nextToken()); 
    
    // Get the " "
    st.nextToken();         

    // Get the right team's score.
    total_score_R = Integer.parseInt(st.nextToken()); 
    
    // Get the " "
    st.nextToken();         

    // Get the game count
    game = Integer.parseInt(st.nextToken()); 
    
    // Get the " "
    st.nextToken();         

    // Get the game count
    games = Integer.parseInt(st.nextToken()); 
    
  } 
  
  // Stream its data content to a string.
  public void writeData(StringBuffer sb)
  {
    sb.append(Packet.REFEREE);
    sb.append(' ');
    sb.append(time);
    sb.append(' ');
    sb.append(period);
    sb.append(' ');
    sb.append(mode);
    sb.append(' ');
    sb.append(sideToContinue);
    sb.append(' ');
    sb.append(leftName);
    sb.append(' ');
    sb.append(score_L);
    sb.append(' ');
    sb.append(total_score_L);
    sb.append(' ');
    sb.append(rightName);
    sb.append(' ');
    sb.append(score_R);
    sb.append(' ');
    sb.append(total_score_R);
    sb.append(' ');
    sb.append(game);
    sb.append(' ');
    sb.append(games);
  } 
  
}
