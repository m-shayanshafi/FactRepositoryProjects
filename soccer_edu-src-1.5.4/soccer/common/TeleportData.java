/* TeleportData.java

	by Vadim Kyrylov
	January 2006
*/


package soccer.common;

import java.util.*;

/**
 * Provides teleportation data for the object controlled by the user
 *
 * @author Yu Zhang
 */
    
public class TeleportData implements Data
{
  /**
   * left-team player .
   */
  public static final char LEFT_PLAYER     = 'l';

  /**
   * right-team player .
   */
  public static final char RIGHT_PLAYER     = 'r';
  
    /**
   * the ball.
   */
  public static final char BALL     = 'b';

    /**
   * goalie grabs the ball.
   */
  public static final char GRAB     = 'g';

   // object type ('b' ball, 'l' left team, 'r' right team);
  public char objType = '?';; 

  // player ID 
  public int playerID = -1;
  
 /**
   * new (x,y)-coordinates of the object
   */
  public double newX, newY;  
  
  public char side; 	// team side
  

  /**
   * Constructs an empty TeleportData for reading from an UDP packet.
   */
  public TeleportData()
  {
    this.newX = 0;
    this.newY = 0;
  } 

  /**
   * Constructs a TeleportData for writing to an UDP packet.
   * for teleportiing objects grabbed by the mouse
   *
   * @param objType object type (ball or left/right player).
   * @param force playerID player id.
   * @param newX, newY new coordinates of the object
   */
  public TeleportData(char objType, int playerID, double newX, double newY)
  {
    this.objType = objType;
    this.playerID = playerID;
    this.newX = newX;
    this.newY = newY;
    if ( objType == LEFT_PLAYER )
    	this.side = 'l';
    else
    	this.side = 'r';
  } 

  // for teleporting the ball grabbed by the goal keeper
  // @param side idicates the team 
  public TeleportData(char objType, char side, Vector2d newPos )
  {
    this.objType = objType;
    this.side = side; 
    this.newX = newPos.getX();
    this.newY = newPos.getY();
  } 
  
  // Load its data content from a string.
  public void readData(StringTokenizer st)
  {
    // object type
    objType = st.nextToken().charAt(0);
    
    // Get the " "
    st.nextToken();       
    
    // team side
    side = st.nextToken().charAt(0);

    // Get the " "
    st.nextToken();       

    // Get player ID
    playerID = Integer.parseInt(st.nextToken());
    
    // Get the " "
    st.nextToken();       
    	  
    // Get x-cordinate
    newX = Double.parseDouble(st.nextToken())/100;
    
    // Get the " "
    st.nextToken();       
    
    // Get y-coordinate
    newY = Double.parseDouble(st.nextToken())/100;     
    
  } 
  
  // Stream its data content to a string.
  public void writeData(StringBuffer sb) 
  {
    sb.append(Packet.TELEPORT);
    sb.append(' ');
    sb.append(objType);
    sb.append(' ');
    sb.append(side);
    sb.append(' ');
    sb.append(playerID);
    sb.append(' ');
    sb.append((int)(newX*100));
    sb.append(' ');
    sb.append((int)(newY*100));
  } 
}
