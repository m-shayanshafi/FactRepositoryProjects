/*
 * Classname			: DCMoveList
 * Author				: TheBlackUnicorn <TheBlackUnicorn@softhome.net>
 * Creation Date		: Fri Oct 18 20:03:25 CEST 2002 
 * Last Updated			: Fri Oct 18 20:03:28 CEST 2002 
 * Description			: 
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
import java.io.Serializable;

/** 
 * List wrapper for DCMove elements. Wraps around an ArrayList
 */ 
public class DCMoveList implements Serializable {

	/*
	 * VARIABLES
	 *
	 */
	
	/* CLASS VARIABLES */
	
	/* INSTANCE VARIABLES */ 

	ArrayList list;

	/* 
	 * INNER CLASSES
	 *
	 */
	
	/*
	 * CONSTRUCTORS
	 *
	 */
	
	/**
	 * Class constructor: Creates an empty list of DCMove elements
	 */
	public DCMoveList() {
		list = new ArrayList();
	}
	
	/*
	 * METHODS
	 *
	 */
	
	/**
	 * Adds a DCMove to the end of the list
	 * @param	element	DCMove to add
	 */
	public void add(DCMove element) {
		list.add(element);
	}

	/**
	 * Adds all elements from specified DCMoveList to this list
	 * @param	list DCMoveList to add
	 */
	public void add(DCMoveList newList) {
		for (int i = 0; i < newList.size(); i++) {
			list.add(newList.get(i));
		}
	}
	
	/**
	 * Gets the DCMove on the specified index
	 * @param 	index	index of array
	 * @return	DCMove on given index
	 */
	public DCMove get(int index) {
		return (DCMove) list.get(index);
	}

	/**
	 * Returns the last DCMove in the list and removes it.
	 * @return	last DCMove on list
	 * @exception DCListEmptyException if list is empty
	 */
	public DCMove pop() throws DCListEmptyException {
		try {
			DCMove move = (DCMove) list.remove(list.size() - 1);
			return move;
		} catch (java.util.NoSuchElementException e) {
			throw new DCListEmptyException("List empty");
		}
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
	
	/**
	 * Returns the DCMove in the list with the given target location
	 * @param	location	target location of move to search
	 * @return	DCMove that goes to target location
	 * @exception	DCLocationException	if there is no move to the specified
	 * target
	 */
	public DCMove getMoveTo(DCCoord location) throws DCLocationException {

		//go through all moves in the list until right target found
		for (int i = 0; i < list.size(); i++) {
			DCMove move = (DCMove) list.get(i);
			if (move.getTarget().equals(location)) {
				return move;
			}
		}
		//if no move found, throw exception
		throw new DCLocationException("No Valid Move Found");
	}
	
} 

/* END OF FILE */
