/*
 * Classname			: DCGameEnv 
 * Author				: TheBlackUnicorn <TheBlackUnicorn@softhome.net>
 * Creation Date		: Fri Jul 26 21:18:40 CEST 2002 
 * Last Updated			: Sat Dec 07 16:29:28 CET 2002 
 * Description			: Super class for backend
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
import java.lang.*;
import connectivity.*;

/**
 * This is the superclass for DCLocalGameEnv and DCNetworkGameEnv.
 * @author		Davy Herben
 * @version	021207
 */ 
abstract public class DCGameEnv implements DCMessageable {

	/*
	 * VARIABLES
	 */

	/* INSTANCE VARIABLES */

	/** reference to the connectivity class. */
	protected 	DCGame		msgCarrier;
	
	/*
	 * METHODS
	 */

	/* see DCMessageable for comments */
	abstract public void sendMessage(DCMessage message);
	
	/**
	 * A player has been disconnected.
	 * <p>This class is empty. Subclasses that need disconnection handlers
	 * should override this method.
	 * @param	player		player that has been disconnected
	 */
	public void connectionBroken(int player) {
		/* not implemented, see the various subclasses instead */
	}
}

/* END OF FILE */
