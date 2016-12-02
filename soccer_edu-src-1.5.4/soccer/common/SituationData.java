/* SituationData.java

 	added by Vadim Kyrylov
 	January 2006
*/


package soccer.common;

import java.util.*;

/**
 * Provides initialization data for server informing the situation 
 * data transfer has been completed.
 *
 * @author Yu Zhang
 */
public class SituationData implements Data
{
  public int stepID 		= 100;	// step for resetting the server to
  public int numOfSteps 	= 100;	// number of steps in a replica
  public int numOfReplicas  =   1;	// number of replicas
  
  /**
   * Constructs an empty SituationData for reading from an UDP packet.
   */
  public SituationData()
  {
    stepID = 100;
    numOfSteps = 100;
    numOfReplicas = 1;
  } 
  
  /**
   * Constructs an SituationData for writing to an UDP packet.
   *
   * @param ct client type.
   * @param num player number.
   */
  public SituationData(int stepID, int numOfSteps, int numOfReplicas)
  {
    this.stepID = stepID;
    this.numOfSteps = numOfSteps;
    this.numOfReplicas = numOfReplicas;
  }
  
  // Load its data content from a string.
  public void readData(StringTokenizer st)
  {
    // Get stepID 
    stepID = Integer.parseInt( st.nextToken() );
    
    // Get the " "
    st.nextToken();          

    // Get numOfSteps 
    numOfSteps = Integer.parseInt( st.nextToken() );
    
    // Get the " "
    st.nextToken();          

    // Get numOfReplicas 
    numOfReplicas = Integer.parseInt( st.nextToken() );
  } 
  
  // Stream its data content to a string
  public void writeData(StringBuffer sb)
  {
    sb.append(Packet.SITUATION);
    sb.append(' ');
    sb.append(stepID);
    sb.append(' ');
    sb.append(numOfSteps);
    sb.append(' ');
    sb.append(numOfReplicas);
  } 
  
}
