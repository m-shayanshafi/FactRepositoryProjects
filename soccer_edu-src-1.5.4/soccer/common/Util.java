/* Util.java

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

	Modifications by Vadim Kyrylov 
							January 2006
*/

package soccer.common;

import java.io.*;
import java.util.*;


/**
 * Utility static class provides conversion, normalization...
 *
 * @author Yu Zhang
 */
public class Util
{
  /**
   * converts degree to radian.
   *
   * @param  degree the angle in degree.
   * @return the same angle in radian.
   */
  public static double Deg2Rad(double degree)
  {
    return( degree * Math.PI / 180);
  }
  
  /**
   * converts any angle value into range -180 -- +180.
   * @param dir the angle value
   * @return the same angle in range -180 -- +180.
   */
  public static double normal_dir(double dir)
  {
    double tmp = dir;
    if(tmp > 180)
      tmp -= 360;
    else if(tmp < -180)
      tmp += 360;
    return tmp;
  }
  
  /** 
   * square. 
   * 
   * @param value the input value.
   * @return the square of the input value.
   */
  public static double Pow(double value)
  {
    return( value * value );
  }
  
  /**
   * convert radian to degree.
   * @param degree the angle value in radian.
   * @return the same angle in degree.
   */
  public static double Rad2Deg(double degree)
  {
    return( degree * 180 / Math.PI );
  }

  /**
   * read a line from InputStreamReader, it will block until a '\n' is read.
   *
   * @param ds the DataInputStream.
   * @return the String contains the last line read from the DataInputStream.
   * @exception IOException If any IO problems occured.
   */
  public static String readLine(DataInputStream ds) throws IOException
  {
    StringBuffer sb = new StringBuffer();
    int c = ds.readByte();
    while(c != '\n' && c != -1)
    {
      sb.append((char)c);
      c = ds.readByte();
    }
    
    return sb.toString();	    
  }

	// this method returns the sign of the input value
	public static int sign ( double number )
	{
		if ( number <  0 ) return -1;
		if ( number == 0 ) return  0;
		if ( number >  0 ) return  1;
		return 0;
	}

	
	// copies elements of source into the respective elements of dist;
	// both vectors are assumed to store objects of same class
	// if source.size()>dest.size(), the exrta elements are ignored;
	// if source.size()<dest.size(), only available elements of source are copied
	
	public void copy( Vector source, Vector dest )
	{
		for ( int i = 0; i < dest.size(); i++ ) {
			try {
				Object obj = (Object)source.elementAt(i);
				dest.setElementAt( obj, i );
			} catch (Exception e ) {}
		}
	}
}
