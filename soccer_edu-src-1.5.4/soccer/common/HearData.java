/* HearData.java

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
*/

package soccer.common;

import java.util.*;

/**
 * Provides chat data for client.
 *
 * @author Yu Zhang
 */
public class HearData implements Data
{
  /**
   * the current simulation step.
   */
  public int time;
  /**
   * the side of speaker, 'l' means left team player, 'r' means right team player,
   * 's' means server instructions(to be implemented).
   */
  public char side;
  /**
   * the speaker's player number
   */
  public int id;  
  /**
   * the message.
   */
  public String message; 

  /**
   * Constructs an empty HearData for reading from an UDP packet.
   */
  public HearData()
  {
    this.message = null;
  } 

  /**
   * Constructs a HearData for writing to an UDP packet.
   *
   * @param time simulation step.
   * @param side the speaker's side.
   * @param id the speaker's player number.
   * @param String the message.
   */
  public HearData(int time, char side, int id, String message)
  {
    this.time = time;
    this.side = side;
    this.id = id;
    this.message = message;
  } 
  
  // Load its data content from a string.
  public void readData(StringTokenizer st)
  {
    StringBuffer sb = new StringBuffer();

    String token;

    // read simulation step
    time = Integer.parseInt(st.nextToken());
    
    // Get the " "
    st.nextToken();

    // Get speaker side
    side = st.nextToken().charAt(0);

    // Get the " "
    st.nextToken();

    // Get speaker id
    id = Integer.parseInt(st.nextToken());

    // Get the " "
    st.nextToken();
    
    // Read the message
    token = st.nextToken();
    while(token.charAt(0) != Packet.CLOSE_TOKEN)
    {
      sb.append(token);
      if(st.hasMoreTokens()) token = st.nextToken();
      else break;
    }
    message = sb.toString();
  } 
  
  // Stream its data content to a string.
  public void writeData(StringBuffer sb)
  {
    sb.append(Packet.HEAR);
    sb.append(' ');  
    sb.append(time);
    sb.append(' ');
    sb.append(side);
    sb.append(' ');
    sb.append(id);
    sb.append(' ');
    sb.append(message);
  } 
  
}
