/*
 * Classname			: DCNetworkException
 * Author				: TheBlackUnicorn <TheBlackUnicorn@softhome.net>
 * Creation Date		: Tue Dec 10 18:16:56 CET 2002 
 * Last Updated			: Wed Dec 11 17:27:04 CET 2002 
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
 * thrown when something is wrong with a network connection.
 * 
 */ 
public class DCNetworkException extends Exception {

	/*
	 * VARIABLES
	 *
	 */
	
	/* INSTANCE VARIABLES */
	private		int		reason;
	
	/*
	 * CONSTRUCTORS
	 */

	/**
	 * class constructor. Takes a reason for the exception
	 * @param	reason 	info about the Exception
	 */
	public DCNetworkException(int reason) {
		super();
		this.reason = reason;
	}

	/**
	 * class constructor. Takes the reason for the exception and a String
	 * message
	 * @param	reason	info about the Exception
	 * @param	message	a String message about the exception
	 */
	public DCNetworkException(int reason, String message) {
		super(message);
		this.reason = reason;
	}

	/*
	 * METHODS
	 *
	 */

	/**
	 * gets the reason for this exception.
	 * @return int with the reason for the exception (defined in {@link
	 * DCConstants})
	 */
	public int getReason() {
		return reason;
	}
} 

/* END OF FILE */
