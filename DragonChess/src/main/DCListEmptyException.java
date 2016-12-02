/*
 * Classname			: DCListEmptyException
 * Author				: TheBlackUnicorn <TheBlackUnicorn@softhome.net>
 * Creation Date		: Fri Oct 18 13:27:48 CEST 2002 
 * Last Updated			: Fri Oct 18 13:27:53 CEST 2002 
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

/**
 * thrown when an element is requested from an empty list
 */ 
public class DCListEmptyException extends Exception {

	/*
	 * CONSTRUCTORS
	 */

	/**
	 * class constructor. Takes a String message
	 * @param	message		info about the Exception
	 */
	public DCListEmptyException(String message) {
		super(message);
	}
} 

/* END OF FILE */
