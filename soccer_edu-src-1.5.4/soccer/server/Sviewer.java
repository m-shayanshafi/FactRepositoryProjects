/* Sviewer.java

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


package soccer.server;

import java.net.*;

import soccer.common.RWLock;

public class Sviewer
{
  // networking properties
  public InetAddress address;
  public int         port;

  public int viewerId;

  public boolean coach = false;
  
  private RWLock lastTimeLock = null;
  private int lastTime; // last time when viewer is active
    
  public Sviewer(InetAddress address, int port, int viewerId, int lastTime, boolean coach)
  {
    this.address = address;
    this.port = port;
    this.viewerId = viewerId;
    this.lastTime = lastTime;
	this.coach = coach;
	this.lastTimeLock = new RWLock();	
  }
    

/**
 * @return
 */
public int getLastTime() {
	try{
		lastTimeLock.lockRead();
		return lastTime;
	}
	finally {
		lastTimeLock.unlock();
	}	
}

/**
 * @param i
 */
public void setLastTime(int i) {
	try {
		lastTimeLock.lockWrite();
		lastTime = i;
	}    	
	finally {
		lastTimeLock.unlock();
	}	
}

}
