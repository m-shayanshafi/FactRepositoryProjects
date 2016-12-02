/*
 * Classname            : DCGameServer
 * Author               : Christophe Hertigers <xof@pandora.be>
 * Creation Date        : Wednesday, October 16 2002, 21:43:15
 * Last Updated         : Wed Dec 11 17:42:11 CET 2002 
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
import java.net.*;
import main.*;
import backend.DCLocalGameEnv;
import javax.swing.SwingUtilities;

/**
 * This class acts as a server for a backend. It is initially connected to a
 * real backend and one frontend, and listens for incoming connections. Other
 * players and spectators can connect themselves to this server.
 *
 * @author Christophe Hertigers
 * @author Davy Herben
 * @version 021208
 */
public class DCGameServer extends DCGame {
									 
	/*
     * VARIABLES
     *
     */

    /* INSTANCE VARIABLES */
	private DCGameServer	me = this;
	
    /* 
     * INNER CLASSES
     *
     */

	/**
	 * Inner class. This class is a thread that keeps listening for network
	 * connections. Upon receiving a connection it creates a
	 * DCNetworkConnection to process that connection and adds it to the
	 * connections list
	 */
	private class NetConnectionListener extends Thread {

		/* VARIABLES */
		ServerSocket	servSocket;
		
		/* CONSTRUCTORS */

		/**
		 * Class constructor. Creates a listener on the specified port
		 * @param	port	port to listen on
		 * @exception	DCNetworkException if server socket can't be created
		 */
		public NetConnectionListener(int port) 
				throws DCNetworkException {
			try {
				servSocket = new ServerSocket(port);
			} catch (java.io.IOException e) {
				System.err.println("NETCONNECTIONLISTENER [Error opening Server Socket]");
				e.printStackTrace();
				throw new DCNetworkException(DCConstants.NW_SERVERSOCKET_NOT_CREATED);
			}
		}

		
		/**
		 * listens on the port for incoming connections and creates
		 * DCNetworkConnection threads to handle them. 
		 */
		public void run() {
			String address = null;	
			while (true) {
				address = "Unknown";
				try {
					//may throw exception, is caught below
					Socket s = servSocket.accept();
					address = s.getInetAddress().getHostName();
					DCNetworkConnection netConn = new DCNetworkConnection(me, 
							s, connections.size());
					connections.add(netConn);
					netConn.start();
				} catch (DCNetworkException e) {
					//do not throw exception but call connectionFailed
					connectionFailed(e.getReason(), address);
				} catch (java.io.IOException e) {
					System.err.println("NETCONNECTIONLISTENER [Error Accepting Connection]");
					e.printStackTrace();
					//do not throw an exception but call connectionFailed
					connectionFailed(DCConstants.NW_SOCKET_NOT_CREATED, address);
				}
			}
		}
	}
	
    /*
     * CONSTRUCTORS
     *
     */

	/**
	 * Class constructor. Creates a server that listens for network activity on
	 * the given port
	 * @param	gui		frontend that creates this server
	 * @param	port	port to listen on
	 * @exception	DCNetworkException when server socket can't be initialized
	 */
	public DCGameServer(DCFrontEnd gui, int port) 
			throws DCNetworkException {
        super(gui);

		//create a DCLocalGameEnv
		backend = new DCLocalGameEnv(this);

		//create one local connection to the client that started us
		DCLocalConnection localConn = new DCLocalConnection(this, gui,
				connections.size());
		connections.add(localConn);

		NetConnectionListener listener;
		//start network listener. This will throw an exception if the
		//serversocket can't be set up
		listener = new NetConnectionListener(port);

		listener.start();
	}

	/*
	 * METHODS
	 *
	 */

	/**
	 * Run when a connection attempts fails. Called by the
	 * NetConnectionListener that creates the DCNetwork?onnection instances.
	 * This method should notify the frontend that runs the server that a
	 * failed connection attempt has been tried.
	 * @param	reason		int with reason for failure (defined in
	 * DCConstants, NW_*)
	 * @param	address		address from which connection attempt originated.
	 * "Unknown" if the address could not be established
	 */
	void connectionFailed(int reason, String address) {
		// XXX should notify server frontend of this failed connection attempt
		System.err.println("Failed connection attempt from : " + address);
		
		//notify the player. 
		refGUI.connectionFailed(reason, address);
		
	}

	/**
	 * A Connection has been broken. This method notifies the frontend and
	 * backend of the connection loss, and unregisters the player in the
	 * backend if this is applicable.
	 * @param	connection	connection that has been broken
	 */
	void connectionBroken(int reason, int connection) {
		
			
		//if the connection belonged to one of the players, remove this player
		//from the players array
		if (players[DCConstants.PLAYER_GOLD] == connection) {
			players[DCConstants.PLAYER_GOLD] = -1;
			backend.connectionBroken(DCConstants.PLAYER_GOLD);
		}

		if (players[DCConstants.PLAYER_SCARLET] == connection) {
			players[DCConstants.PLAYER_SCARLET] = -1;
			backend.connectionBroken(DCConstants.PLAYER_SCARLET);
		}
	}
}

/* END OF FILE */

