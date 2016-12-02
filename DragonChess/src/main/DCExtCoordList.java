/*
 * Classname			: DCExtCoordList
 * Author				: TheBlackUnicorn <TheBlackUnicorn@softhome.net>
 * Creation Date		: Sat Oct 26 18:24:15 CEST 2002 
 * Last Updated			: Sat Oct 26 18:24:17 CEST 2002 
 * Description			: List of DCExtCoord
 * GPL disclaimer		:
 *   This program is free software; you can redistribute it and/or modify it
 *   under the terms of the GNU General Public License as published by the
 *   Free Software Foundation; version 2 of the License.
 *   This program is distributed in the hope that it will be useful, but
 *   WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 *   or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 *   for more details. You should have received a copy of the GNU General
 *   Public License along with this program; if not, write to the Free Software
 *   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package main;

/* package import */
import java.lang.*;
import java.util.ArrayList;
import java.io.*;

/**
 * Represents a simple list of DCExtCoord elements. Used to dump piece
 * information
 */ 
public class DCExtCoordList implements Serializable {

	/*
	 * VARIABLES
	 *
	 */
	
	/* CLASS VARIABLES */
	
	/* INSTANCE VARIABLES */ 
		
	protected ArrayList list;
	
	/* 
	 * INNER CLASSES
	 *
	 */
	
	/*
	 * CONSTRUCTORS
	 *
	 */

	/**
	 * Class constructor: Creates an empty list of DCExtCoord elements
	 */
	public DCExtCoordList() {
		list = new ArrayList();
	}

	
	/*
	 * METHODS
	 *
	 */

	/**
	 * Adds a DCExtCoord to the end of the list
	 * @param	element	DCExtCoord to add
	 */
	public void add(DCExtCoord element) {
		list.add(element);
	}

	/**
	 * Gets the DCExtCoord on the specified index
	 * @param 	index	index of array
	 * @return	DCExtCoord on given index
	 */
	public DCExtCoord get(int index) {
		return (DCExtCoord) list.get(index);
	}
	 
	/**
	 * Returns the size of the list
	 * @return	int with number of elements in the list
	 */
	public int size() {
		return list.size();
	}

	/**
	 * Returns whether the list is empty
	 * @return	true if list has no elements
	 */
	public boolean isEmpty() {
		return list.isEmpty();
	}
	
} 

/* END OF FILE */
