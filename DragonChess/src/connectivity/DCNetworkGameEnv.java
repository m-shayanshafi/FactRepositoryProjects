/*
 * Classname			: DCNetworkGameEnv
 * Author				: TheBlackUnicorn <TheBlackUnicorn@softhome.net>
 * Creation Date		: Sun Nov 17 20:00:08 CET 2002 
 * Last Updated			: Fri Dec 13 10:29:46 CET 2002 
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
import java.net.*;
import java.io.*;
import main.*;
import backend.DCGameEnv;

/**
 * This class acts as a backend for a DCGame. It does not, however, provide any
 * game logic like an actual backend does. Instead, it uses a network
 * connection to send and receive DCMessages to a real backend.
 *
 * @author Davy Herben
 * @version 021208
 */ 
public class DCNetworkGameEnv extends DCGameEnv implements Runnable {

	/*
	 * VARIABLES
	 *
	 */
	
	/* CLASS VARIABLES */
	
	/* INSTANCE VARIABLES */ 
	private Socket			socket;
	private int				connectionNr;
	private	ObjectInputStream	in;
	private ObjectOutputStream	out;
	private boolean CONNECT_DEBUG = false;
	
	/*
	 * CONSTRUCTORS
	 *
	 */

	/** Class constructor. Creates a connection between a game and a remote
	 * client.
	 * @param	game	the game to link to
	 * @param	socket	the Socket to use
	 * @exception	DCNetworkException if connection with the server can't be
	 * established
	 */
	public DCNetworkGameEnv(DCGame game, String addressString, int port) 
			throws DCNetworkException {
		this.msgCarrier = game;
		
		//create new socket
		try {
			InetAddress address = InetAddress.getByName(addressString);
			socket = new Socket(address, port);
		} catch (UnknownHostException e) {
			System.err.println("NETGAMEENV Hostname not found");
			throw new DCNetworkException(DCConstants.NW_HOST_NOT_FOUND, 
					addressString);
		} catch (java.io.IOException e) {
			System.err.println("NETGAMEENV Error opening socket");
			throw new DCNetworkException(DCConstants.NW_SOCKET_NOT_CREATED,
					addressString);
		}
		
		//open input and output streams
		try {
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(
					new BufferedInputStream(socket.getInputStream()));
		} catch (java.io.IOException e) {
			System.err.println("NETGAMEENV Error opening streams");
			throw new DCNetworkException(DCConstants.NW_STREAMS_NOT_CREATED,
					addressString);
		}
	}
	
	/*
	 * METHODS
	 *
	 */

	/**
	 * listens on the network for incoming messages from the remote backend.
	 */
	public void run() {
		//keep listening for messages
		DCMessage message = null;
		int reason = 0;
		try {
			while (true) {
				try {
					message = (DCMessage) in.readObject();
				} catch (java.lang.ClassNotFoundException cnf) {
					System.err.println("NETGAMEENV Class Not Found Exception");
				} catch (java.io.IOException e) {
					reason = DCConstants.NW_INPUT_STREAM_ERROR;
					break;
				}
				if (message != null) {
					this.sendMessage(message);
				}
			}
		} finally {
			try {
				socket.close();
			} catch (java.io.IOException e) {
				System.err.println("NETGAMEENV Error closing socket");
			} finally {
				msgCarrier.backendConnectionBroken(reason);
			}
		}
	}
	
	/* see DCMessageable for comments */
	public void sendMessage(DCMessage message) {
		//route messages
		if (message.getDest() == DCConstants.TO_FRONTEND) {
			msgCarrier.sendMessage(message);
		} else {
			//write through outputstream
			try {
				out.writeObject(message);
			} catch (NotSerializableException e) {
				System.err.println("NETGAMEENV Not Serializable Exception");
				msgCarrier.messageDeliveryFailed(DCConstants.NW_NOT_SERIALIZABLE_ERROR, message);
			} catch (java.io.IOException e) {
				System.err.println("NETGAMEENV Error writing to output stream");
				msgCarrier.messageDeliveryFailed(DCConstants.NW_OUTPUT_STREAM_ERROR, message);
			}
		}
	}
} 

/* END OF FILE */
