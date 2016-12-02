
/*
 * Classname        : DCFrontEndMessageEncoder
 * Author           : Christophe Hertigers <xof@pandora.be>
 * Creation Date    : Wednesday, October 16 2002, 20:56:32
 * Last Updated     : Sunday, December 08 2002, 14:33:34
 * Description      : class for encoding DCFrontEndMessages
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

package gui2d;

/* package import */
import java.lang.*;
import main.*;
import connectivity.*;

/**
 * Encodes the DCFrontEndMessage to be sent to DCGame. Contains methods that
 * encode DCMessage that, after being decoded by the DCBackEndDecoder, specify
 * which method should be started in the backend.
 * 
 * @author	Christophe Hertigers
 * @version	Sunday, December 08 2002, 14:33:34
 */
public class DCFrontEndEncoder {

    /*
     * VARIABLES
     *
     */

    /* CLASS VARIABLES */

    /* INSTANCE VARIABLES */
	private DC2dGUI		myGUI;
		
    /* 
     * INNER CLASSES
     *
     */

    /*
     * CONSTRUCTORS
     *
     */
	public DCFrontEndEncoder(DC2dGUI myGUI) {
		this.myGUI = myGUI;
	}
	
    /*
     * METHODS
     *
     */

	/**
	 * Makes a DCMessage to register a new player
	 * @param playerInt			current player
	 * @param name				playername (String)
	 * @param address			String containing address information
	 * @param reqPlayerNumber	
	 * @return	the DCMessage to send
	 */
	public DCMessage registerPlayer(String name, String address, 
														int reqPlayerNumber) {
		Object[] data = new Object[4];
		data[0] = name;
		data[1] = address;
		data[2] = new Integer(reqPlayerNumber);
		data[3] = new Integer(-1);
		DCMessage myMsg = new DCMessage(DCConstants.MSG_REGISTER_PLAYER,
										DCConstants.TO_BACKEND,
										DCConstants.PLAYER_NONE,
										data);
		return myMsg;
	}
    
	/**
	 * Makes a DCMessage to request to undo the last move.
	 * @param playerNumber	the player who asked the undo
	 * @param reason		the reason for the undo
	 * @return	the DCMessage to send
	 */
	public DCMessage requestUndoMove(int playerNumber, String reason) {
		
		Object[] data = new Object[2];
		data[0] = new Integer(playerNumber);
		data[1] = reason;
		DCMessage myMsg = new DCMessage(DCConstants.MSG_REQUEST_UNDO,
										DCConstants.TO_BACKEND,
										playerNumber,
										data);
		return myMsg;

	}
	
	/**
	 * Makes a DCMessage to request to undo the last move.
	 * @param playerNumber	the player who asked the undo
	 * @return	the DCMessage to send
	 */
	public DCMessage requestUndoMove(int playerNumber) {
		return requestUndoMove(playerNumber, "");
	}

	/**
	 * Makes a DCMessage to resign the current game.
	 * @param playerNumber	the player who resigns
	 * @param reason		the reason of the resign
	 * @return	the DCMessage to send
	 */
	public DCMessage resignGame(int playerNumber, String reason) {
		Object[] data = new Object[2];
		data[0] = new Integer(playerNumber);
		data[1] = reason;
		DCMessage myMsg = new DCMessage(DCConstants.MSG_RESIGN_GAME,
										DCConstants.TO_BACKEND,
										playerNumber,
										data);
		return myMsg;
	}

	/**
	 * Makes a DCMessage to resign the current game.
	 * @param playerNumber	the player who resigns
	 * @return	the DCMessage to send
	 */
	public DCMessage resignGame(int playerNumber) {
		return resignGame(playerNumber, "");
	}

	/**
	 * Makes a DCMessage to select a piece on the board.
	 * @param playerNumber	the player who wants to select a piece
	 * @param location		the location of the to be selected piece
	 * @return	the DCMessage to send
	 */
	public DCMessage selectPiece(int playerNumber, DCCoord location) {
		Object data = location;
		DCMessage myMsg = new DCMessage(DCConstants.MSG_SELECT_PIECE,
										DCConstants.TO_BACKEND,
										playerNumber,
										data);
		return myMsg;
			
	}

	/**
	 * Makes a DCMessage to move the selected piece.
	 * @param playerNumber	the player who wants to move
	 * @param location		the location to move the selected piece to
	 * @return	the DCMessage to send
	 */
	public DCMessage movePiece(int playerNumber, DCCoord location) {
		Object data = location;
		DCMessage myMsg = new DCMessage(DCConstants.MSG_MOVE_PIECE,
										DCConstants.TO_BACKEND,
										playerNumber,
										data);
		return myMsg;

	}
	
	/**
	 * Makes a DCMessage to start a new game of DragonChess
	 * @param playerNumber	the player who wants to start a new game
	 * @return	the DCMessage to send
	 */
	public DCMessage startGame(int playerNumber) {

		DCMessage myMsg = new DCMessage(DCConstants.MSG_START_GAME,
										DCConstants.TO_BACKEND,
										playerNumber);
		return myMsg;
	}

	/**
	 * Makes a DCMessage to unregister a player.
	 * @param playerNumber	the player who wants to be unregistered
	 * @param reason		the reason of the unregistration
	 * @return	the DCMessage to send
	 */
	public DCMessage unregisterPlayer(int playerNumber, String reason) {
		Object data = reason;
		DCMessage myMsg = new DCMessage(DCConstants.MSG_UNREGISTER_PLAYER,
										DCConstants.TO_BACKEND,
										playerNumber,
										data);
		return myMsg;
	
	}
	
	/**
	 * Makes a DCMessage to unregister a player.
	 * @param playerNumber	the player who wants to be unregistered
	 * @return	the DCMessage to send
	 */
	public DCMessage unregisterPlayer(int playerNumber) {
		return unregisterPlayer(playerNumber, "");
	}
	
	/**
	 * Makes a DCMessage to send a chat msg to a player
	 * @param playerNumber	the player who sends the message
	 * @param str	the string to send 
	 * @return	the DCMessage to send
	 */
	public DCMessage chatMessage(int playerNumber, boolean publicMessage, String str) {
		Object[] data = new Object[2];
		data[0] = new Boolean(publicMessage);
		data[1] = str;


		DCMessage myMsg = new DCMessage(DCConstants.MSG_PLAYER_CHAT_SEND,
										DCConstants.TO_BACKEND,
										playerNumber,
										data);
		return myMsg;
	}

}

/* END OF FILE */

