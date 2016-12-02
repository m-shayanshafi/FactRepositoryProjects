/*
 * Classname			: DCLocalConnection
 * Author				: TheBlackUnicorn <TheBlackUnicorn@softhome.net>
 * Creation Date		: Sat Nov 16 14:25:20 CET 2002 
 * Last Updated			: Sun Dec 08 16:17:29 CET 2002 
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

package connectivity;

/* package import */
import java.lang.*;
import main.DCConstants;
import main.DCFrontEnd;

/**
 * This class represents a local connection between a DCGame and a DCFrontEnd. 
 *
 * @author Davy Herben
 * @version 021208
 */ 
public class DCLocalConnection implements DCMessageable {

	/*
	 * VARIABLES
	 *
	 */
	
	/* INSTANCE VARIABLES */ 
	private DCFrontEnd	gui;
	private DCGame			game;
	private int				connectionNr;
	boolean	CONNECT_DEBUG = false;
	
	/*
	 * CONSTRUCTORS
	 *
	 */

	/** Class constructor. Creates a connection between a game and a local 
	 * frontend.
	 * @param	game	the game to link to
	 * @param	gui		frontend to link to
	 */
	public DCLocalConnection(DCGame game, DCFrontEnd gui, int connectionNr) {
		this.gui = gui;
		this.game = game;
		this.connectionNr = connectionNr;

		//register yourself with the gui

		gui.registerConnection(this);
	}
	
	/*
	 * METHODS
	 *
	 */

	/**
	 * routes a message to frontend or backend. Also adds a connection number
	 * to all messages going to the backend.
	 * @param	message	DCMessage to route
	 */
	public void sendMessage(DCMessage message) {
		//route messages
		if (message.getDest() == DCConstants.TO_BACKEND) {
			//add connection number to message
			message.setConnection(connectionNr);
			game.sendMessage(message);
		} else {
			gui.sendMessage(message);
		}
	}
} 

/* END OF FILE */
