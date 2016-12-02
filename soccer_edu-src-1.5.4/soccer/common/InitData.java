/* InitData.java

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
 * Provides initialization data for server informing the newly connected client.
 *
 * @author Yu Zhang
 */
public class InitData implements Data
{
  /**
   * Initialization token 'l' means that the client has been put to the left team.
   */
  public static final char LEFT       = 'l';
  /**
   * Initialization token 'm' means that the client has been put to 
   * the left team and it has coach ability.
   */
  public static final char LEFT_COACH       = 'm';  
  /**
   * Initialization token 'r' means that the client has been put to the right team.
   */
  public static final char RIGHT      = 'r';
  /**
   * Initialization token 's' means that the client has been put to 
   * the right team and it has coach ability.
   */
  public static final char RIGHT_COACH      = 's';  
  /**
   * Initialization token 'v' means that the client has been set as a viewer.
   */
  public static final char VIEWER     = 'v';
  /**
   * Initialization token 'w' means that the client has been set as a viewer
   * and it has coach ability.
   */
  public static final char VIEWER_COACH     = 'w';  
  /**
   * Initialization token 'f' means that the client is denied of connection because there
   * is no available connection.
   */
  public static final char FULL       = 'f';  

  /**
   * Describes the type of client. If no connection available, 'f' is put here.
   */
  public char clientType;

  /**
   * the ID number assigned to the client.
   */
  public int num;
  
  /**
   * the server heart rate (duration of the simulation step)
   */
  public double heartRate;

  /**
   * the maximal number of steps goalie allowed to grab the ball
   */
  public int maxGrabSteps = 0;

  /**
   * Constructs an empty InitData for reading from an UDP packet.
   */
  public InitData()
  {
    clientType = ' ';
    num = 0;
  } 
  
  /**
   * Constructs an InitData for writeing to an UDP packet.
   *
   * @param ct client type.
   * @param num player number.
   */

  public InitData(char ct, int num, double heartRate, int maxGrabSteps)
  {
    clientType = ct;
    this.num = num;
    this.heartRate = heartRate;
    this.maxGrabSteps = maxGrabSteps;
  }

  // a legacy constructor
  public InitData(char ct, int num )
  {
    clientType = ct;
    this.num = num;
    this.heartRate = 0.05;
    this.maxGrabSteps = 70;
  }

  
  // Load its data content from a string.
  public void readData(StringTokenizer st)
  {
    // Get the connection type.
    clientType = st.nextToken().charAt(0);
    
    // Get the " "
    st.nextToken();          

    // Get the player/viewer number.
    num = Integer.parseInt(st.nextToken()); 
       
    // Get the " "
    st.nextToken();          

    // Get the heart rate
    heartRate = ( Integer.parseInt(st.nextToken()) )/1000.0;    
    //System.out.println("Receiving heartRate = " + heartRate );

    // Get the " "
    st.nextToken();          

    // Get the number of steps
    maxGrabSteps = Integer.parseInt(st.nextToken()); 
       
  } 
  
  // Stream its data content to a string  heartRate
  public void writeData(StringBuffer sb)
  {
    sb.append(Packet.INIT);
    sb.append(' ');
    sb.append(clientType);
    sb.append(' ');
    sb.append(num);  
    sb.append(' ');
    sb.append((int)(heartRate*1000));  
    sb.append(' ');
    sb.append(maxGrabSteps);  
    //System.out.println("Sending heartRate = " + heartRate + " sb = " + sb );
  } 
  
}
