/* GState.java
   This class describes states of the game.

   by Vadim Kyrylov
   January 2006

   Ideally, all applicationd in this system should have state-transition
   model. This class provides the set of states. 
   However, such models have not been implemented consistently
   
*/


package soccer.common;

/**
 * This class describes a ball. 
 * @author Yu Zhang
 */
public class GState
{

	public static final int INIT 		=  0; 
	public static final int CONNECTED 	= 20; 
	public static final int READY	 	= 30; 
	public static final int WAITING 	= 40; 
	public static final int RUNNING 	= 50; 
	public static final int END		 	= 60; 
  
    
}
