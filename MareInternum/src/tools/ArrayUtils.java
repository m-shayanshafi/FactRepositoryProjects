/*	 
*    This file is part of Mare Internum.
*
*	 Copyright (C) 2008,2009  Johannes Hoechstaedter
*
*    Mare Internum is free software: you can redistribute it and/or modify
*    it under the terms of the GNU General Public License as published by
*    the Free Software Foundation, either version 3 of the License, or
*    (at your option) any later version.
*
*    Mare Internum is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License
*    along with Mare Internum.  If not, see <http://www.gnu.org/licenses/>.
*
*/

package tools;

public class ArrayUtils {

	/**
	 * reverses an array
	 * by exchanging the elements
	 * @param b
	 */
	public static void reverse(int[] array) {
		   int links  = 0;          
		   int rechts = array.length - 1;
		  
		   while (links < rechts) {
		      
		      int temp = array[links]; 
		      array[links]  = array[rechts]; 
		      array[rechts] = temp;
		     
		      links++;
		      rechts--;
		   }
		}

}
