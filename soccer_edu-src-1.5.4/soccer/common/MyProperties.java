/* Properties.java
   This class describes states of the game.

   by Vadim Kyrylov
   January 2006

   This class just adds some optional verbousity while 
   loading the property file into a Java application
   
*/
package soccer.common;

import java.util.*;

/**
 * This class describes a ball. 
 * @author Yu Zhang
 */
public class MyProperties extends Properties
{
	private boolean verbose = false;
	
	public MyProperties()
	{
		super();
	}

	public MyProperties( boolean verbose )
	{
		super();
		this.verbose = verbose;
	}
	
	public String getProperty( String title, String valueof )
	{
		String strvalue = null;
		
		strvalue =super.getProperty( title );
			
		if ( strvalue == null ) {
			strvalue = super.getProperty( title, valueof );
			if ( verbose )
				System.out.println( title + " >> using default value  = " + valueof );
		} else 
			if ( verbose )
				System.out.println( title + " = " + strvalue );
		
		return strvalue; 
	}
  
    public boolean isVerbose()
    {
    	return verbose;
    }
}
