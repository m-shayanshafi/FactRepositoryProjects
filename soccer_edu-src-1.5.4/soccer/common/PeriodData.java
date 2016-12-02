/* PeriodData.java

   Copyright (C) 2004  Yu Zhang

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


	Modifications by Vadim Kyrylov 
							January 2006
*/


package soccer.common;

import java.util.*;

/**
 * Set the game simulation cycle, mainly affect the game period.
 *
 * @author Yu Zhang
 * with modifications by Vadim Kyrylov
 * January 2006
 */
public class PeriodData implements Data
{
  /**
   * control token 'a' means to save a snapshot of the game.
   */
  public static final char SAVE       = 'a';
  /**
   * control token 's' means to step forward the game.
   */
  public static final char STEP       = 's';
  /**
   * control token 'p' means to play the game.
   */
  public static final char PLAY      = 'b';
  /**
   * control token 'f' means to forward the game to the next period.
   */
  public static final char FORWARD     = 'f';

  /**
   * Describes the action by the couch client.
   */
  public char actionType;

  
  /**
   * Constructs an empty PeriodData for reading from an UDP packet.
   */
  public PeriodData()
  {
      actionType = ' ';
  } 
  
  /**
   * Constructs an PeriodData for writeing to an UDP packet.
   *
   * @param at action type.
   */
  public PeriodData(char at)
  {
    actionType = at;
  }
  
  // Load its data content from a string.
  public void readData(StringTokenizer st)
  {
    // Get the connection type.
    actionType = st.nextToken().charAt(0);

  } 
  
  // Stream its data content to a string
  public void writeData(StringBuffer sb)
  {
    sb.append(Packet.PERIOD);
    sb.append(' ');
    sb.append(actionType);
  } 
  
}
