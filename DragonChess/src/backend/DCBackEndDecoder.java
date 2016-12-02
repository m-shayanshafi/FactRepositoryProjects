/*
 * Classname			: DCBackEndDecoder
 * Author				: TheBlackUnicorn <TheBlackUnicorn@softhome.net>
 * Creation Date		: Sat Oct 19 14:14:28 CEST 2002 
 * Last Updated			: Fri Dec 06 16:10:40 CET 2002 
 * Description			: Decodes DCMessages
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
import connectivity.DCMessage;
import connectivity.DCMessageable;
import main.*;

/**
 * This class decodes DCMessages backend method calls. It only has one method
 * which takes a DCMessage, extracts the appropriate data from it, and calls
 * the appropriate method of the {@link DCLocalGameEnv} to which it is linked.
 *
 * @author Davy Herben
 * @version 021206
 */ 
public class DCBackEndDecoder implements DCMessageable {

	/*
	 * VARIABLES
	 *
	 */
	
	/* INSTANCE VARIABLES */ 
	
	//target on which to call methods
	private DCLocalGameEnv	gameEnv;
	
	/*
	 * CONSTRUCTORS
	 *
	 */

	/**
	 * Class constructor. Creates a new decoder that calls methods of the
	 * specified DCLocalGameEnv
	 * @param	gameEnv 	The DCLocalGameEnv to decode to
	 */
	public DCBackEndDecoder(DCLocalGameEnv gameEnv) {
		this.gameEnv = gameEnv;
	}

	/*
	 * METHODS
	 *
	 */

	/**
	 * takes a DCMessage to decode.
	 * @param	message		the message to decode
	 */
	public void sendMessage(DCMessage message) {

		/* find type of message */
		int type = message.getType();
		int player = message.getPlayer();
		Object []data;
		switch (type) {
			case DCConstants.MSG_REGISTER_PLAYER :
				data = (Object []) message.getData();
				gameEnv.registerPlayer(	(String) data[0],
										(String) data[1],
										((Integer) data[2]).intValue(),
										message.getConnection());
				break;
			case DCConstants.MSG_START_GAME :
				gameEnv.startGame(message.getPlayer());
				break;
			case DCConstants.MSG_SELECT_PIECE :
				gameEnv.selectPiece((DCCoord) message.getData(), player);
				break;
			case DCConstants.MSG_MOVE_PIECE :
				gameEnv.movePiece((DCCoord) message.getData(), player);
				break;
			case DCConstants.MSG_REQUEST_UNDO :
				data = (Object []) message.getData();
				gameEnv.undoMovePiece(((Integer) data[0]).intValue(),
									  (String) data[1]);
				break;
			case DCConstants.MSG_RESIGN_GAME :
				data = (Object []) message.getData();
				gameEnv.resignGame(((Integer) data[0]).intValue(),
								   (String) data[1]);
				break;
			case DCConstants.MSG_UNREGISTER_PLAYER :
				gameEnv.unregisterPlayer(message.getPlayer(),
										 (String) message.getData());
				break;
			case DCConstants.MSG_PLAYER_CHAT_SEND :
				data = (Object []) message.getData();
				gameEnv.chatMessage(message.getPlayer(), 
									((Boolean) data[0]).booleanValue(),
									(String) data[1]);
				break;
			default :
				System.err.println("BEDEC : RECEIVED UNKNOWN MESSAGE TYPE " + type);
		}
	}
} 

/* END OF FILE */
