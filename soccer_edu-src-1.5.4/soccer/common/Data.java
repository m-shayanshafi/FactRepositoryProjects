/* Data.java

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
 * This interface defines the data content in a packet. 
 * By implementing this interface, a piece of data can be inserted
 * to a packet, and be exchanged over network by using defined format.
 *
 * @author Yu Zhang
 */
      
interface Data
{

  /**
   * Load and parse data content from a string using StringTokenizer.
   * 
   * @param st   the StringTokenizer from packet, which has the rest
   *             unparsed string
   * @see        Packet#readPacket(String)
   */
  public void readData(StringTokenizer st);
  
  /**
   * Stream its data content to a string using defined data format.
   *
   * @param sb   the StringBuffer set by packet, which should already contain
   *             the packet header information
   * @see        Packet#writePacket()
   */

  public void writeData(StringBuffer sb);
  
}
