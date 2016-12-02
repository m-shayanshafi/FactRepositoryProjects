/*
 * Classname		: DCMessage
 * Author			: TheBlackUnicorn <TheBlackUnicorn@softhome.net>
 * Creation Date	: Wed Oct 16 16:57:09 CEST 2002  
 * Last Updated		: Sun Dec 08 16:21:50 CET 2002  
 * Description		: Message
 * GPL disclaimer   :
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
import java.io.Serializable;
import main.DCConstants;

/**
 * Message that can be sent to classes implementing the DCMessageable
 * interface. This class is the basic element of the connectivity in
 * DragonChess. 
 *
 * @author Davy Herben
 * @version 021208
 */ 
public class DCMessage implements Serializable {

	/*
	 * VARIABLES
	 *
	 */
	
	/* INSTANCE VARIABLES */
	protected int type;
	
	protected int dest;
	protected int connectionInt = -1;
	protected int player = DCConstants.PLAYER_NONE;
	protected boolean spectators = false;
		
	protected Object data;
	
	/*
	 * CONSTRUCTORS
	 */

	/**
	 * class constructor. Creates a new DCMessage with specified type and
	 * player info, and no object data, forming a control message
	 * @param	type	int containing type of message
	 * @param	dest	direction of travel, DCConstants.TO_BACKEND or
	 * 					DCConstants.TO_FRONTEND
	 * @param	player	player info : DCConstants.PLAYER_NONE,
	 * 								  DCConstants.PLAYER_GOLD,
	 * 								  DCConstants.PLAYER_SCARLET,
	 * 								  DCConstants.PLAYER_BOTH
	 */
	public DCMessage(int type, int dest, int player) {
		this(type,dest,player,false,null);
	}
	
	/**
	 * class constructor. Creates a new DCMessage with specified type and
	 * player info, and no object data, forming a control message
	 * @param	type	int containing type of message
	 * @param	dest	direction of travel, DCConstants.TO_BACKEND or
	 * 					DCConstants.TO_FRONTEND
	 * @param	player	player info : DCConstants.PLAYER_NONE,
	 * 								  DCConstants.PLAYER_GOLD,
	 * 								  DCConstants.PLAYER_SCARLET,
	 * 								  DCConstants.PLAYER_BOTH
	 * @param	data	Object with message payload
	 */
	public DCMessage(int type, int dest, int player, Object data) {
		this(type,dest,player,false,data);
	}


	/**
	 * class constructor. Creates a new DCMessage with specified type and
	 * player info, and no object data, forming a control message
	 * @param	type	int containing type of message
	 * @param	dest	direction of travel, DCConstants.TO_BACKEND or
	 * 					DCConstants.TO_FRONTEND
	 * @param	player	player info : DCConstants.PLAYER_NONE,
	 * 								  DCConstants.PLAYER_GOLD,
	 * 								  DCConstants.PLAYER_SCARLET,
	 * 								  DCConstants.PLAYER_BOTH
	 * @param	spectators	true if spectators should get this
	 */
	public DCMessage(int type, int dest, int player, boolean spectators) {
		this(type,dest,player,spectators,null);
	}

	/**
	 * class constructor. Creates a new DCMessage with specified type and
	 * player info, and no object data, forming a control message
	 * @param	type	int containing type of message
	 * @param	dest	direction of travel, DCConstants.TO_BACKEND or
	 * 					DCConstants.TO_FRONTEND
	 * @param	player	player info : DCConstants.PLAYER_NONE,
	 * 								  DCConstants.PLAYER_GOLD,
	 * 								  DCConstants.PLAYER_SCARLET,
	 * 								  DCConstants.PLAYER_BOTH
	 * @param	spectators	true if spectators should get this
	 * @param	data	Object with message payload
	 */
	public DCMessage(int type, int dest, int player, boolean spectators, Object data) {
		this.type = type;
		this.dest = dest;
		this.player = player;
		this.spectators = spectators;
		this.data = data;
	}
	
	
	/*
	 * METHODS
	 *
	 */

	/**
	 * returns the type of message.
	 * @return an int with the type of message (stored in DCConstants)
	 */
	public int getType() {
		return type;
	}

	/**
	 * returns the direction in which this message travels
	 * @return either DCConstants.TO_FRONTEND or DCConstants.TO_BACKEND
	 */
	public int getDest() {
		return dest;
	}
	
	/**
	 * returns the source/destination player number.
	 * @return int with source/destination player number
	 */
	public int getPlayer() {
		return player;
	}

	/**
	 * Gives the message data. The return value should be cast by 
	 * @return	Object representing the message data
	 */
	public Object getData() {
		return data;
	}

	/**
	 * returns whether the message is sent to spectators.
	 * @return	true if message should be sent to spectators, false otherwise
	 */
	public boolean getSpectators() {
		return spectators;
	}

	/**
	 * sets whether the message is sent to spectators.
	 * @param 	spectators	true if message should be sent to spectators
	 */
	public void setSpectators(boolean spectators) {
		this.spectators = spectators;
	}
	
	/**
	 * Sets the message data. The specified data should be Serializable
	 * @param	data	data to put in message
	 */
	public void setData(Object data) {
		this.data = data;
	}

	/**
	 * Sets the connection number of the message.
	 * @param	nr		connection number
	 */
	public void setConnection(int nr) {
		connectionInt = nr;
	}

	/**
	 * Gets the connection number that has sent this message. -1 means that
	 * the message has not yet been given a number or has been sent by the
	 * backend
	 * @return	an int with the connection number
	 */
	public int getConnection() {
		return connectionInt;
	}
	
} 

/* END OF FILE */
