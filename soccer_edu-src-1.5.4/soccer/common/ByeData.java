/* ByeData.java

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
 * This class is an empty packet, used by a player/viewer client to inform the sever that
 * the client is going to leave.
 *
 * @author Yu Zhang
 */
    
public class ByeData implements Data
{
  
  /**
   * Constructs a ByeData for reading and writing from an UDP packet.
   */
  public ByeData()
  {

  } 
  
  // Load its data content from a string.
  public void readData(StringTokenizer st)
  {
  
  } 
  
  // Stream its data content to a string
  public void writeData(StringBuffer sb)
  {
    sb.append(Packet.BYE);
  } 
  
}
