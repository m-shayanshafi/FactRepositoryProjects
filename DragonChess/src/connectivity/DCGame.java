/*
 * Classname            : DCGame
 * Author               : Christophe Hertigers <xof@pandora.be>
 * Creation Date        : Wednesday, October 16 2002, 21:43:15
 * Last Updated         : Sun Dec 08 15:52:12 CET 2002 
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
import main.*;
import backend.DCGameEnv;

/**
 * This is the superclass for all connectivity classes. The goal of these
 * classes is to act as a router for messages between the backend and the
 * frontend or frontends.
 *
 * <p>This superclass already defines the complete routing system for messages,
 * and also implements methods to take care of errors, such as broken
 * connections.
 *
 * @author Davy Herben
 * @version 021208
 */
public abstract class DCGame implements DCMessageable {
									 
	/*
     * VARIABLES
     *
     */

    /* CLASS VARIABLES */

    /* INSTANCE VARIABLES */
	/** the frontend that has started this instance */
	protected	DCFrontEnd		refGUI;
	/** the backend to send backend messages to, either a real backend or a
	 * dummy network backend
	 */
	protected	DCGameEnv	backend;
	/** list of all connections, both active and broken */
	protected 	DCMessageableList	connections = new DCMessageableList();
	/** maps player number to connections list entry for easier routing of
	 * messages */
	protected	int				[]players = {-1, -1};
	
    /*
     * CONSTRUCTORS
     *
     */
	public DCGame(DCFrontEnd gui) {

	}

    /*
     * METHODS
     *
     */

	/**
	 * handles the message routing itself. It extracts the necessary
	 * information from the messages it receives and decides where they should
	 * be sent. It also catches registration messages, to update its own player
	 * mapping.
	 * @param	message		DCMessage to route
	 */
	public void sendMessage(DCMessage message) {
		Object []data = null;
		DCMessageable connection = null;
		
		/* intercept register messages */

		//assign player when registered
		if (message.getType() == DCConstants.MSG_REGISTER_SUCCESS) {
			// extract data
			data = (Object []) message.getData();
			int playerInt = ((Integer) data[2]).intValue();
			//set player
			players[playerInt] = message.getConnection(); 
		}

		//route message
		if (message.getDest() == DCConstants.TO_BACKEND) {
			backend.sendMessage(message);
		} else {
			switch (message.getPlayer()) {
				case DCConstants.PLAYER_GOLD :
					if (players[DCConstants.PLAYER_GOLD] != -1) {
						connections.get(players[DCConstants.PLAYER_GOLD]
								).sendMessage(message);
					}
					break;
				case DCConstants.PLAYER_SCARLET :
					if (players[DCConstants.PLAYER_SCARLET] != -1) {
						connections.get(players[DCConstants.PLAYER_SCARLET]
								).sendMessage(message);
					}
					break;
				case DCConstants.PLAYER_BOTH :
					if (players[DCConstants.PLAYER_GOLD] != -1) {
						connections.get(players[DCConstants.PLAYER_GOLD]
								).sendMessage(message);
					}
					//don't send again if to same connection
					if (players[DCConstants.PLAYER_SCARLET] != -1 &&
						players[DCConstants.PLAYER_SCARLET] !=
						players[DCConstants.PLAYER_GOLD]) {
						connections.get(players[DCConstants.PLAYER_SCARLET]
							).sendMessage(message);
					}
					break;
				case DCConstants.PLAYER_NONE :
					//send to connection Number if specified
					if (message.getConnection() != -1) {
						connections.get(message.getConnection()
							).sendMessage(message);
					}
			}
		}

		//send to spectators if required
		if (message.getSpectators()) {
			for (int i = 0; i < connections.size(); i++) {
				if (i != players[DCConstants.PLAYER_GOLD] &&
					i != players[DCConstants.PLAYER_SCARLET]) {
					connections.get(i).sendMessage(message);
				}
			}
		}
	}

	/**
	 * A connection has been broken. This method is called by the {@link
	 * DCConnection DCConnections} when a socket error occurs and the network
	 * connection is broken. It is <b>not</b> used when a DCNetworkGameEnv
	 * loses its connection (see {@link #backendConnectionBroken} for that.
	 * 
	 * <p>This method does nothing as this class needs no networking. All
	 * DCGame subclasses that need networking should override this class.
	 *
	 * @param reason	reason for the breaking of the connection, defined in
	 * {@link DCConstants}
	 * @param connection	number of the connection that is broken
	 */
	void connectionBroken(int reason, int connection) {
		/* not implemented, use the subclass version instead */
	}

	/** 
	 * A message could not be delivered over the network. This may mean that
	 * the other side has closed the connection. It may also mean that there is
	 * something wrong with the DCMessage in question, eg that it was not
	 * serializable.
	 * 
	 * @param	reason		reason for failure (defined in {@link DCConstants}
	 * @param	message		the message that couldn't be sent
	 */
	void messageDeliveryFailed(int reason, DCMessage message) {
		// XXX : Implement this
		System.err.println("A message could not be delivered");

	}

	/**
	 * The connection with the backend has been broken. This can only happen
	 * when the {@link DCGame} instance is in fact a network connection. 
	 *
	 * <p>This method is empty. Classes that have a networked DCGame should
	 * override it.
	 */
	void backendConnectionBroken(int reason) {
		/* not implemented, use subclass version instead */
	}

}

/* END OF FILE */

