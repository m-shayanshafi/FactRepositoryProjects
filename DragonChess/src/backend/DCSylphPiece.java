/*
 * Classname			: DCSylphPiece
 * Author				: TheBlackUnicorn <TheBlackUnicorn@softhome.net>
 * Creation Date		: Wed Oct 16 20:43:54 CEST 2002 
 * Last Updated			: Sun Dec 08 13:26:21 CET 2002 
 * Description			: A DC Sylph piece
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
import main.*;


/**
 * Represents a Sylph piece on the board.
 *
 * <p>A Sylph has the following moves :
 * <ul>
 * <li>on the top board, move to adjacent squares diagonally ahead
 * <li>on the top board, capt to adjacent square directly ahead
 * <li>on the top board, capt to middle board square directly below piece
 * <li>on the middle board, move to top board square directly above piece, or
 * to one of the starting squares of the sylph pieces, if unoccupied
 * </ul>
 * 
 * @author Davy Herben
 * @version 021208
 */ 
public class DCSylphPiece extends DCPiece {

	/*
	 * CONSTRUCTORS
	 *
	 */

	/* see DCPiece for comments */
	public DCSylphPiece(int player, DCGameBoard board) {
		super(player, board);
		type = 'S';
	}
	
	/*
	 * METHODS
	 *
	 */
	
	/* see DCPiece for comments */
	public DCMoveList getValidMoveList() {
		//empty list
		list = new DCMoveList();

		//moves for all players
		
		//  moves on upper board
		if (location.getBoard() == DCConstants.BOARD_TOP) {
			//1: capture directly beneath
			processUnblockableTarget(DCConstants.CAPT,-1,0,0);
		}

		//  moves on middle board
		if (location.getBoard() == DCConstants.BOARD_MIDDLE) {
			//2: move directly upwards
			processUnblockableTarget(DCConstants.MOVE,1,0,0);
		}
		
		//moves for player gold
		if (player == DCConstants.PLAYER_GOLD) {
			//moves on upper board
			if (location.getBoard() == DCConstants.BOARD_TOP) {
				//3: move to squares diagonally ahead
				processUnblockableTarget(DCConstants.MOVE,0,-1,1);
				processUnblockableTarget(DCConstants.MOVE,0,+1,1);

				//4: capt straight ahead
				processUnblockableTarget(DCConstants.CAPT,0,0,1);
			}

			//moves on middle board
			if (location.getBoard() == DCConstants.BOARD_MIDDLE) {
				//5: move to any of the starting places
				processAbsoluteMoveTarget(2,0,1);
				processAbsoluteMoveTarget(2,2,1);
				processAbsoluteMoveTarget(2,4,1);
				processAbsoluteMoveTarget(2,6,1);
				processAbsoluteMoveTarget(2,8,1);
				processAbsoluteMoveTarget(2,10,1);
			}
		} else {
		//moves for player scarlet
			//moves on upper board
			if (location.getBoard() == DCConstants.BOARD_TOP) {
				//3: move to squares diagonally ahead
				processUnblockableTarget(DCConstants.MOVE,0,-1,-1);
				processUnblockableTarget(DCConstants.MOVE,0,+1,-1);

				//4: capt straight ahead
				processUnblockableTarget(DCConstants.CAPT,0,0,-1);
			}
			//moves on middle board
			if (location.getBoard() == DCConstants.BOARD_MIDDLE) {
				//5: move to any of the starting places
				processAbsoluteMoveTarget(2,0,6);
				processAbsoluteMoveTarget(2,2,6);
				processAbsoluteMoveTarget(2,4,6);
				processAbsoluteMoveTarget(2,6,6);
				processAbsoluteMoveTarget(2,8,6);
				processAbsoluteMoveTarget(2,10,6);
			}
		}
		
		return list;
	}

} 

/* END OF FILE */
