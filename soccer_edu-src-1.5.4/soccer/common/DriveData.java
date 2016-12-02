/* DriveData.java

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
 * Provides driving data for client controlling the player's movement.
 *
 * @author Yu Zhang
 */
public class DriveData implements Data
{

  /**
   * the force direction, which is between -180 and +180.
   */
  public double dir;   // -180<= dir <= 180
  /**
   * the force value, which is between -30 and 100.
   */
  public double force; // -30<= force <= 100

  /**
   * Constructs an empty DriveData for reading from an UDP packet.
   */
  public DriveData()
  {
    this.dir = 0;
    this.force = 0;
  } 

  /**
   * Constructs a DriveData for writing to an UDP packet.
   *
   * @param dir force direction.
   * @param force force value.
   */
  public DriveData(double dir, double force)
  {
    this.dir = dir;
    this.force = force;
  } 
  
  // Load its data content from a string.
  public void readData(StringTokenizer st)
  {
	  
    // Get the direction
    dir = Double.parseDouble(st.nextToken());
    
    // Get the " "
    st.nextToken();        
    
    // Get the force
    force = Double.parseDouble(st.nextToken());     
    
  } 
  
  // Stream its data content to a string.
  public void writeData(StringBuffer sb)
  {
    sb.append(Packet.DRIVE);
    sb.append(' ');
    sb.append((int)dir);
    sb.append(' ');
    sb.append((int)force);
  } 
  
}
