/*
 * Classname			: DCGarbageList
 * Author				: TheBlackUnicorn <TheBlackUnicorn@softhome.net>
 * Author2				: Dommi <houbrechts.dominique@pandora.be>
 * Creation Date		: Wed Oct 16 21:01:27 CEST 2002 
 * Last Updated			: Sat Dec 07 16:32:49 CET 2002 
 * Description			: List of captured pieces
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

package backend;

/* package import */
import java.util.*;
import main.DCListEmptyException;

/**
 * This class serves as a wrapper for an ArrayList. It can contain only {@link
 * DCPiece} elements.
 * @author  Davy Herben
 * @version 021207
 */ 
public class DCGarbageList {

	/*
	 * VARIABLES
	 *
	 */

	/* INSTANCE VARIABLES */ 
	
	protected LinkedList list  = new LinkedList();
	
	/*
	 * CONSTRUCTORS
	 *
	 */

	/**
	 * Class constructor.
	 */
	public DCGarbageList() {}

	/*
	 * METHODS
	 *
	 */
	
	/**
	 * adds the specified piece to the end of the list
	 * @param	piece	DCPiece to push into the list
	 */
	public void push(DCPiece piece) {
		list.add(piece);		
	}

	/**
	 * removes and retrieves the last piece from the list
	 * @return	the last DCPiece on the list
	 * @exception 	DCListEmptyException	when the list is already empty
	 */
	public DCPiece pop() throws DCListEmptyException {
		
		DCPiece lastDCPiece = (DCPiece)list.removeLast();
		return lastDCPiece;
	}

	/**
	 * gets the piece on the specified index without removing it from the list
	 * @param	index	index to get
	 * @return	DCPiece on that index
	 */
	public DCPiece get(int index) {
		return (DCPiece) list.get(index);
	}
	
	/**
	 * returns whether the list is empty
	 * @return true if empty, false otherwise
	 */
	public boolean isEmpty() {
		if (list.size() == 0) return true;
		else return false;
	}

	/**
	 * returns the number of elements in the list
	 * @return int indicating number of elements in the list
	 */
	public int size() {
		return list.size();
	}
	
} 

/* END OF FILE */
