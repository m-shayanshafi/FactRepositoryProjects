/*
 * Classname            : DCFrontEndDecoder
 * Author               : Christophe Hertigers <xof@pandora.be>
 * Creation Date        : Wednesday, October 16 2002, 21:32:18
 * Last Updated         : Friday, October 18 2002, 13:23:14
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

package gui2d;

/* package import */
import java.lang.*;
import connectivity.*;
import main.*;

/**
 * Decodes and interpretes the DCFrontEndMessages arriving in the GUI
 *
 * @author	Christophe Hertigers
 * @version	Friday, October 18 2002, 13:23:14
 */
public class DCFrontEndDecoder {

    /*
     * VARIABLES
     *
     */
	DC2dGUI	gui;
		
    /* CLASS VARIABLES */

    /* INSTANCE VARIABLES */

    /* 
     * INNER CLASSES
     *
     */

    /*
     * CONSTRUCTORS
     *
     */
	
	/**
	 * Class Constructor. Creates a new DCFrontEndDecoder that calls the
	 * methods of the referenced DC2dGUI.
	 * @param	the reference to the DC2dGUI
	 */
	public DCFrontEndDecoder(DC2dGUI gui) {
		this.gui = gui;
	}
	
    /*
     * METHODS
     *
     */

	/**
	 * Decodes the incoming DCMessage.
	 * @param	the message to decode
	 *
	 */
	public void decodeFrontEndMessage(DCMessage msg) {
		Object []data;
		String name;
		String address;
		DCCoord location;
		int playerInt;
		int gameState;
		int reason;
		int winner;
		String moveString;
		String reasonString;
		String msgString;
		String playerNameString;
		boolean publicBoolean;
		
		switch (msg.getType()) {
		case DCConstants.MSG_GAME_STARTED :
			gui.newGameStarted();
			if (gui.getPreferences().getDebugFrontEndDecoder()) {
			System.out.println("FEDEC [MSG_GAME_STARTED]");
			}
			break;
		case DCConstants.MSG_REGISTER_SUCCESS :
			data = (Object [])msg.getData();
			name = (String) data[0];
			address = (String) data[1];
			playerInt = ((Integer) data[2]).intValue();
			gui.registerSuccess(playerInt, name, address);
			if (gui.getPreferences().getDebugFrontEndDecoder()) {
			System.out.println("FEDEC [MSG_REGISTER_SUCCESS]");
			}
			break;
		case DCConstants.MSG_REGISTER_FAILURE :
			gui.registerFailure();
			if (gui.getPreferences().getDebugFrontEndDecoder()) {
			System.out.println("FEDEC [MSG_REGISTER_FAILURE]");
			}
			break;
		case DCConstants.MSG_NEW_PLAYER_REGISTERED :
			data = (Object [])msg.getData();
			name = (String) data[0];
			address = (String) data[1];
			playerInt = ((Integer) data[2]).intValue();
			gui.newPlayerRegistered(playerInt, name, address);
			if (gui.getPreferences().getDebugFrontEndDecoder()) {
			System.out.println("FEDEC [MSG_NEW_PLAYER_REGISTERED]");
			}
			break;
		case DCConstants.MSG_SET_PLAYER_INFO :
			data = (Object [])msg.getData();
			name = (String) data[1];
			address = (String) data[2];
			playerInt = ((Integer) data[0]).intValue();
			gui.setPlayerInfo(playerInt, name, address);
			if (gui.getPreferences().getDebugFrontEndDecoder()) {
			System.out.println("FEDEC [MSG_SET_PLAYER_INFO]");
			}
			break;
		case DCConstants.MSG_GAMESTATE :
			gui.setGameState(((Integer) msg.getData()).intValue());
			if (gui.getPreferences().getDebugFrontEndDecoder()) {
			System.out.println("FEDEC [MSG_GAMESTATE]");
			}
			break;
		case DCConstants.MSG_LOAD_DUMP :
			gui.loadDCFrontEndDump((DCFrontEndDump) msg.getData());
			if (gui.getPreferences().getDebugFrontEndDecoder()) {
			System.out.println("FEDEC [MSG_LOAD_DUMP]");
			}
			break;
		case DCConstants.MSG_ACTIVE_PLAYER :
			gui.setActivePlayer(((Integer) msg.getData()).intValue());
			if (gui.getPreferences().getDebugFrontEndDecoder()) {
			System.out.println("FEDEC [MSG_ACTIVE_PLAYER]");
			}
			break;
		case DCConstants.MSG_PIECE_SELECTED :
			gui.pieceSelected((DCCoord) msg.getData());
			if (gui.getPreferences().getDebugFrontEndDecoder()) {
			System.out.println("FEDEC [MSG_PIECE_SELECTED]");
			}
			break;
		case DCConstants.MSG_PIECE_NOT_SELECTED :
			data = (Object [])msg.getData();
			location = (DCCoord) data[0];
			reason = ((Integer) data[1]).intValue();
			gui.pieceNotSelected(location, reason);
			if (gui.getPreferences().getDebugFrontEndDecoder()) {
			System.out.println("FEDEC [MSG_PIECE_NOT_SELECTED]");
			}
			break;
		case DCConstants.MSG_PIECE_DESELECTED :
			data = (Object[]) msg.getData();
			location = (DCCoord) data[0];
			DCMoveList list = (DCMoveList) data[1];
			gui.pieceDeselected(location, list);
			if (gui.getPreferences().getDebugFrontEndDecoder()) {
			System.out.println("FEDEC [MSG_PIECE_DESELECTED]");
			}
			break;
		case DCConstants.MSG_VALID_TARGETS :
			gui.showValidTargets((DCMoveList) msg.getData());
			if (gui.getPreferences().getDebugFrontEndDecoder()) {
			System.out.println("FEDEC [MSG_VALID_TARGETS]");
			}
			break;
		case DCConstants.MSG_PIECE_MOVED :
			gui.pieceMoved((DCMove) msg.getData());
			if (gui.getPreferences().getDebugFrontEndDecoder()) {
			System.out.println("FEDEC [MSG_PIECE_MOVED]");
			}
			break;
		case DCConstants.MSG_PIECE_NOT_MOVED :
			data = (Object[]) msg.getData();
			location = (DCCoord) data[0];
			reason = ((Integer) data[1]).intValue();
			gui.pieceNotMoved(location, reason);
			if (gui.getPreferences().getDebugFrontEndDecoder()) {
			System.out.println("FEDEC [MSG_PIECE_NOT_MOVED]");
			}
			break;
		case DCConstants.MSG_FREEZE :
			data = (Object[]) msg.getData();
			location = (DCCoord) data[0];
			gui.setFreezeStatus(location, ((Boolean) data[1]).booleanValue());
			if (gui.getPreferences().getDebugFrontEndDecoder()) {
			System.out.println("FEDEC [MSG_FREEZE] : " + 
										((Boolean) data[1]).booleanValue());
			}
			break;
		case DCConstants.MSG_CHECK :
			gui.setCheck(((Integer) msg.getData()).intValue());
			if (gui.getPreferences().getDebugFrontEndDecoder()) {
			System.out.println("FEDEC [MSG_CHECK]");
			}
			break;
		case DCConstants.MSG_GAMEOVER :
			data = (Object[]) msg.getData();
			reason = ((Integer) data[0]).intValue();
			winner = ((Integer) data[1]).intValue();
			gui.gameOver(reason, winner);
			if (gui.getPreferences().getDebugFrontEndDecoder()) {
			System.out.println("FEDEC [MSG_GAMEOVER]");
			}
			break;
		case DCConstants.MSG_ADD_HISTORY :
			data = (Object[]) msg.getData();
			moveString = (String) data[0];
			playerInt = ((Integer) data[1]).intValue();
			gui.addHistoryString(moveString, playerInt);
			if (gui.getPreferences().getDebugFrontEndDecoder()) {
			System.out.println("FEDEC [MSG_ADD_HISTORY]");	
			}
			break;
		case DCConstants.MSG_REMOVE_HISTORY :
			gui.removeHistoryString(((Integer) msg.getData()).intValue());
			if (gui.getPreferences().getDebugFrontEndDecoder()) {
			System.out.println("FEDEC [MSG_REMOVE_HISTORY]");	
			}
			break;
		case DCConstants.MSG_MOVE_UNDONE :
			gui.moveUndone((DCMove) msg.getData());
			if (gui.getPreferences().getDebugFrontEndDecoder()) {
			System.out.println("FEDEC [MSG_MOVE_UNDONE]");	
			}
			break;
		case DCConstants.MSG_MOVE_NOT_UNDONE :
			gui.moveNotUndone(((Integer) msg.getData()).intValue());
			if (gui.getPreferences().getDebugFrontEndDecoder()) {
			System.out.println("FEDEC [MSG_MOVE_NOT_UNDONE]");	
			}
			break;
		case DCConstants.MSG_PIECE_PROMOTED :
			gui.piecePromoted((DCExtCoord) msg.getData());
			if (gui.getPreferences().getDebugFrontEndDecoder()) {
			System.out.println("FEDEC [MSG_PIECE_PROMOTED]");	
			}
			break;
		case DCConstants.MSG_PIECE_DEMOTED :
			gui.pieceDemoted((DCExtCoord) msg.getData());
			if (gui.getPreferences().getDebugFrontEndDecoder()) {
			System.out.println("FEDEC [MSG_PIECE_DEMOTED]");	
			}
			break;
		case DCConstants.MSG_PIECE_RESTORED :
			gui.pieceRestored((DCExtCoord) msg.getData());
			if (gui.getPreferences().getDebugFrontEndDecoder()) {
			System.out.println("FEDEC [MSG_PIECE_RESTORED]");	
			}
			break;
		case DCConstants.MSG_PLAYER_UNREGISTERED :
			data = (Object[]) msg.getData();
			playerInt = ((Integer) data[0]).intValue();
			reasonString = (String) data[1];
			gui.playerUnregistered(playerInt, reasonString);
			if (gui.getPreferences().getDebugFrontEndDecoder()) {
			System.out.println("FEDEC [MSG_PLAYER_UNREGISTERED]");
			}
			break;
		case DCConstants.MSG_PLAYER_CHAT_RECV :
			data = (Object[]) msg.getData();
			publicBoolean = ((Boolean) data[0]).booleanValue();
			playerNameString = (String) data[1];
			msgString = (String) data[2];
			gui.communicationReceive(publicBoolean, playerNameString, msgString);
			if (gui.getPreferences().getDebugFrontEndDecoder()) {
			System.out.println("FEDEC [MSG_PLAYER_CHAT]");
			}
			break;
		default :
			System.err.println("FEDEC: RECEIVED UNKNOWN MESSAGE TYPE : " + msg.getType());
		}
	}
}

/* END OF FILE */

