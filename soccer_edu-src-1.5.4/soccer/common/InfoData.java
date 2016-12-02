/* InfoData.java
   
   by Vadim Kyrylov 
   January 2006
*/

package soccer.common;

import java.util.*;

/**
 * Provides feedback info sent by server to clients about its actions
 *
 */
public class InfoData implements Data
{

  /**
   * server command identifiers (info)
   */
  public static final int WAIT_NEXT  = 20;	// game paused
  public static final int RESUME     = 30;	// game resumed
  public static final int REPLICA    = 40;	// a replica of the game is played

  // server info
  public int info;
  public int info1;
  public int info2;


  /**
   * Constructs an empty InfoData for reading from an UDP packet.
   */
  public InfoData()
  {
    this.info = 0;
    this.info1 = 0;
    this.info2 = 0;
  }
  
  /** 
   * Constructs a InfoData for writeing to an UDP packet.
   * @param info the server feedback info
   */
  public InfoData(int info)
  {
    this.info = info;
    this.info1 = 0;
    this.info2 = 0;
  } 

  /** 
   * Constructs a InfoData for writeing to an UDP packet.
   * @param info the server feedback info
   */
  public InfoData(int info, int info1, int info2)
  {
    this.info = info;
    this.info1 = info1;
    this.info2 = info2;
  } 
  
  // Load its data content from a string.
  public void readData(StringTokenizer st)
  {
    // Get the info.
    info = Integer.parseInt(st.nextToken()); 
    // Get the " "
    st.nextToken();       
    // Get the info2.
    info1 = Integer.parseInt(st.nextToken()); 
    // Get the " "
    st.nextToken();       
    // Get the info2.
    info2 = Integer.parseInt(st.nextToken()); 
  } 
  
  // Stream its data content to a string.
  public void writeData(StringBuffer sb)
  {
    sb.append(Packet.INFO);
    sb.append(' ');
    sb.append(info);
    sb.append(' ');
    sb.append(info1);
    sb.append(' ');
    sb.append(info2);
  } 
  
}
