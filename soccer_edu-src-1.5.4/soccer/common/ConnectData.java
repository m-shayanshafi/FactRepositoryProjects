/* ConnectData.java

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
 * Provides connection data for client connecting to server.
 *
 * @author Yu Zhang
 */
public class ConnectData implements Data
{

  /**
   * Connection client Type identifier 'p'.
   */
  public static final char PLAYER   = 'p';
  /**
   * Connection client Type identifier 'v'.
   */
  public static final char VIEWER   = 'v';

  /**
   * Side type 'a'.
   */
  public static final char ANYSIDE   = 'a';
  /**
   * Side type 'b'.
   */
  public static final char ANYSIDE_COACH   = 'b';  
  /**
   * Side type 'l'.
   */
  public static final char LEFT     = 'l';
  /**
   * Side type 'm'.
   */
  public static final char LEFT_COACH     = 'm';  
  /**
   * Side type 'r'.
   */
  public static final char RIGHT    = 'r';
  /**
   * Side type 's'.
   */
  public static final char RIGHT_COACH    = 's';

  /**
   * Describes the type of client.
   */
  public char clientType;

  /**
   * Describes the side the client is going to join.
   */
  public char sideType;
  
  // 'home' position coordinates
  public Vector2d pos;
  private double x, y;

  /**
   * Player role 'g'.
   */
  public static final char GOALIE    		= 'g';
  public static final char FIELD_PLAYER    	= 'f';
  public static final char FIELD_PLAYER_KICKER    	= 'k';
  public static final char ROLE_UNKNOWN    	= 'n';
  
  /**
   * Describes whether this client is the goalie.
   */
  public char playerRole;	


  /**
   * Constructs an empty ConnectData for reading from an UDP packet.
   */
  public ConnectData()
  {
    clientType = ' ';
    sideType   = ' ';
    playerRole = FIELD_PLAYER;	// not a goalie by default
    pos = new Vector2d();
  } 

  
  /**
   * Constructs a ConnectData for writeing to an UDP packet.
   *
   * @param ct client type.
   * @param st side type.
   */
  public ConnectData(char ct, char st, char role )
  {
    clientType = ct;
    sideType = st;
    playerRole = role;
    pos = new Vector2d();	
  } 

  public ConnectData(char ct, char st, char role, Vector2d pos )
  {
    clientType = ct;
    sideType = st;
    playerRole = role;
    this.pos = new Vector2d( pos );	
  }
   
  /**
   * this is a leagcy constructor
   * Constructs a ConnectData for writeing to an UDP packet.
   *
   * @param ct client type.
   * @param st side type.
   */
  public ConnectData(char ct, char st )
  {
    clientType = ct;
    sideType = st;
    playerRole = ROLE_UNKNOWN;	
    pos = new Vector2d();	
  }
    
  // Load its data content from a string.
  public void readData(StringTokenizer st)
  {
    // Get the connection type.
    clientType = st.nextToken().charAt(0);

    // Get the " "
    st.nextToken();    
    
    // Get the side type.
    sideType = st.nextToken().charAt(0);   

    // Get the " "
    st.nextToken();    
    
    // Get the side type.
    playerRole = st.nextToken().charAt(0);   

    // Get the " "
    st.nextToken();          

    // Get x
    x = ( Integer.parseInt(st.nextToken()) )/100.0;    

    // Get the " "
    st.nextToken();          

    // Get y
    y = ( Integer.parseInt(st.nextToken()) )/100.0;  
    
    pos = new Vector2d( x, y );  
    
  } 
  
  // Stream its data content to a string.
  public void writeData(StringBuffer sb)
  {
    sb.append(Packet.CONNECT);
    sb.append(' ');
    sb.append(clientType);
    sb.append(' ');
    sb.append(sideType);
    sb.append(' ');
    sb.append(playerRole);
    sb.append(' ');
    sb.append( (int)(pos.getX()*100) );
    sb.append(' ');
    sb.append( (int)(pos.getY()*100) );
  } 
  
}
