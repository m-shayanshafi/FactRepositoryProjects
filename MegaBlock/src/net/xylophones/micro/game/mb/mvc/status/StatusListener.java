/*
 * StatusListener.java
 *
 * Subject to the apache license v. 2.0
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * @author william@xylophones.net
 */

package net.xylophones.micro.game.mb.mvc.status;

/**
 *
 * @author william@xylophones.net
 */
public interface StatusListener {
	    
    public static final int EVENT_NEW_LEVEL = 1;
    
    public static final int EVENT_SCORE = 2;
    	
    public void statusEvent(int event);
}
