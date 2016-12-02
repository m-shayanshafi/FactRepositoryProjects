/*
 * Classname			: DCNetworkConnection
 * Author				: TheBlackUnicorn <TheBlackUnicorn@softhome.net>
 * Creation Date		: Sun Nov 17 20:00:08 CET 2002 
 * Last Updated			: Fri Dec 13 08:53:23 CET 2002 
 * Description			: Connection between DCGames via sockets
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

/**
 * This class creates a connection between a game and a remote client using
 * network sockets. 
 *
 * @author Davy Herben
 * @version 021213
 */ 
public class DCNetworkConnection extends Thread implements DCMessageable {

	/*
	 * VARIABLES
	 *
	 */
	
	/* CLASS VARIABLES */
	
	/* INSTANCE VARIABLES */ 
	private Socket			socket;
	private DCGame			game;
	private int				connectionNr;
	private	ObjectInputStream	in;
	private ObjectOutputStream	out;
	private boolean	connectionActive = false;
	private boolean CONNECT_DEBUG = false;
	
	/*
	 * CONSTRUCTORS
	 *
	 */

	/** Class constructor. Creates a connection between a game and a remote
	 * client.
	 * @param	game	the game to link to
	 * @param	socket	the Socket to use
	 * @param	connectionNr	the connection nr of this connection
	 * @exception	DCNetworkException when streams can't be created
	 */
	public DCNetworkConnection(DCGame game, Socket socket, int connectionNr) 
			throws DCNetworkException {
		this.socket = socket;
		this.game = game;
		this.connectionNr = connectionNr;

		//open input and output streams
		try {
			in = new ObjectInputStream(
					new BufferedInputStream(socket.getInputStream()));
			out = new ObjectOutputStream(socket.getOutputStream());
			connectionActive = true;
		} catch (java.io.IOException e) {
			System.err.println("Error opening streams");
			throw new DCNetworkException(DCConstants.NW_STREAMS_NOT_CREATED);
		}
	}
	
	/*
	 * METHODS
	 *
	 */

	/**
	 * listens for messages on the network socket, and closes the connection if
	 * an error occurs.
	 */
	public void run() {
		//keep listening for messages
		DCMessage message = null;
		int reason = 0;
		try {
			while (true) {
				try {
					message = (DCMessage) in.readObject();
				} catch (java.lang.ClassNotFoundException e) {
					System.err.println("NETCONN [Class Not Found Exception]");
					e.printStackTrace();
				} catch (java.io.IOException e) {
					System.err.println("NETCONN [IO Exception while reading input stream]");
					e.printStackTrace();
					reason = DCConstants.NW_INPUT_STREAM_ERROR;
					break;
				}
				if (message != null) {
					this.sendMessage(message);
				}
			}
		} finally {
			try {
				System.err.println("NETCONN Connection broken");
				socket.close();
				
				//mark connection as broken
				connectionActive = false;
				//game should perform necessary steps to handle this
				game.connectionBroken(reason, connectionNr);
				
			} catch (java.io.IOException e) {
				System.err.println("NETCONN Error closing socket");
				connectionActive = false;
				game.connectionBroken(reason, connectionNr);
			}
		}
	}
	

	/* see DCMessageable for comments */
	public void sendMessage(DCMessage message) {
		if (connectionActive) {
			//route messages
			if (message.getDest() == DCConstants.TO_BACKEND) {
				//add connection number to message
				message.setConnection(connectionNr);
				game.sendMessage(message);
			} else {
				//write through outputstream
				try {
					out.writeObject(message);
				} catch (NotSerializableException e) {
					System.err.println("NETCONN Not Serializable Exception");
				} catch (java.io.IOException e) {
					System.err.println("NETCONN Error writing to output stream");
				}
			}
		}
	}
} 

/* END OF FILE */
