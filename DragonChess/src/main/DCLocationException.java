/*
 * Classname			: DCLocationException
 * Author				: TheBlackUnicorn <TheBlackUnicorn@softhome.net>
 * Creation Date		: Thu Oct 17 19:37:57 CEST 2002 
 * Last Updated			: Thu Oct 17 19:38:00 CEST 2002 
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
 * thrown when something is wrong with a location (DCCoord or sublass).
 * Examples of when these exceptions can occur are selecting a location without
 * a piece on it, moving a piece to an invalid location, etc.
 */ 
public class DCLocationException extends Exception {

	/*
	 * CONSTRUCTORS
	 */

	/**
	 * class constructor. Takes a String message
	 * @param	message		info about the Exception
	 */
	public DCLocationException(String message) {
		super(message);
	}
} 

/* END OF FILE */
