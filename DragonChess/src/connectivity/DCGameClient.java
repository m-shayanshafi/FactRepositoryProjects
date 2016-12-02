/*
 * Classname            : DCGameClient
 * Author               : Christophe Hertigers <xof@pandora.be>
 * Creation Date        : Wednesday, October 16 2002, 21:43:15
 * Last Updated         : Fri Dec 13 10:27:15 CET 2002 
 * Description          : 
 * GPL disclaimer       :
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
import main.*;

/**
 * This class connects the calling gui with a specified server as a client. It
 * does not create its own backend, but passes all received backend messages to
 * the server backend over the network.
 *
 * @author Christophe Hertigers
 * @author Davy Herben
 * @version 021213
 */
public class DCGameClient extends DCGame {
									 
	/*
     * VARIABLES
     *
     */

    /* INSTANCE VARIABLES */
	
    /*
     * CONSTRUCTORS
     *
     */

	/**
	 * Class constructor. Creates a connection to the specified server.
	 * @param	gui		the frontend to send to
	 * @param	server	address of the server to connect to
	 * @param	port	port number to connect to on the server
	 */
	public DCGameClient(DCFrontEnd gui, String server, int port) 
			throws DCNetworkException {
        super(gui);

		this.refGUI = gui;

		//create a DCNetworkGameEnv (don't start yet)
		DCNetworkGameEnv gameEnv = new DCNetworkGameEnv(this, server, port);
		backend = gameEnv;
		
		//create a single local connection
		//we will not allow any other connections here, so we can immediately
		//point both players to this connection
		DCLocalConnection localConn = new DCLocalConnection(this, refGUI, 0);
		connections.add(localConn);
		players[0] = 0;
		players[1] = 0;
		
		//now start backend
		new Thread(gameEnv).start();
	}

    /*
     * METHODS
     *
     */

	/**
	 * This method overrides the DCGame method because it can be much simpler.
	 * Everything that goes to frontend and arrives here has to go to
	 * connection 0, everything else has to go to the backend.
	 * Also the game doesn't need to intercept register messages.
	 * @param	message	DCMessage to route
	 */
	public void sendMessage(DCMessage message) {
		if (message.getDest() == DCConstants.TO_FRONTEND) {
			connections.get(0).sendMessage(message);
		} else {
			backend.sendMessage(message);
		}
	}

	/**
	 * The connection with the backend has been broken. The frontend is
	 * notified of this
	 * @param	reason	reason for the broken connection
	 */
	void backendConnectionBroken(int reason) {
		
		System.err.println("DCGAMECLIENT : BACKEND CONNECTION HAS BEEN BROKEN !");

		//call appropriate method to notify gui of this. for now, we will take
		//care of the swing thread handling here. Eventually this should be
		//included inside the gui itself
		refGUI.backendConnectionBroken(reason);
	}
}

/* END OF FILE */

