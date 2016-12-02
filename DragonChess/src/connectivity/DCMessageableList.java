/*
 * Classname			: DCMessageableList
 * Author				: TheBlackUnicorn <TheBlackUnicorn@softhome.net>
 * Creation Date		: Sat Oct 26 18:24:15 CEST 2002 
 * Last Updated			: Sun Dec 08 16:24:13 CET 2002 
 * Description			: List of DCMessageable
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

package connectivity;

/* package import */
import java.lang.*;
import java.util.ArrayList;

/**
 * Represents a simple list of DCMessageable elements. It is in fact a simple
 * wrapper around an ArrayList.
 *
 * @author Davy Herben
 * @version 021208
 */ 
public class DCMessageableList {

	/*
	 * VARIABLES
	 *
	 */
	
	/* INSTANCE VARIABLES */ 
		
	protected ArrayList list;
	
	/*
	 * CONSTRUCTORS
	 *
	 */

	/**
	 * Class constructor. Creates an empty list of DCMessageable elements.
	 */
	public DCMessageableList() {
		list = new ArrayList();
	}

	
	/*
	 * METHODS
	 *
	 */

	/**
	 * Adds a DCMessageable to the end of the list
	 * @param	element	DCMessageable to add
	 */
	public void add(DCMessageable element) {
		list.add(element);
	}

	/**
	 * Gets the DCMessageable on the specified index
	 * @param 	index	index of array
	 * @return	DCMessageable on given index
	 */
	public DCMessageable get(int index) {
		return (DCMessageable) list.get(index);
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
