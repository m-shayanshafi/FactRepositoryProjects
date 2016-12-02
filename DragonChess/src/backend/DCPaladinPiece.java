/*
 * Classname			: DCPaladinPiece
 * Author				: TheBlackUnicorn <TheBlackUnicorn@softhome.net>
 * Creation Date		: Wed Oct 16 20:43:54 CEST 2002 
 * Last Updated			: Sun Dec 08 13:18:38 CET 2002 
 * Description			: A DC Paladin piece
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
 * Represents a Paladin piece on the board
 *
 * <p>A Paladin has the following moves :
 * <ul>
 * <li>on all boards, move and capture to all adjacent squares on the same
 * board
 * <li>move/capt between boards by making an unblockable knight-like move : 2
 * squares in one direction (including up and down) and then 1 in a
 * perpendicular direction.
 * <li>on the middle board, move/capt like a knight
 * </ul>
 * 
 * @author Davy Herben
 * @version 021208
 */ 
public class DCPaladinPiece extends DCPiece {

	/*
	 * CONSTRUCTORS
	 *
	 */

	/* see DCPiece for comments */
	public DCPaladinPiece(int player, DCGameBoard board) {
		super(player, board);
		type = 'P';
	}
	
	/*
	 * METHODS
	 *
	 */

	/* see DCPiece for comments */
	public DCMoveList getValidMoveList() {
		//empty list
		list = new DCMoveList();

		// moves on all boards

		//1: king-like move to all adjacent squares
		processUnblockableTarget(DCConstants.MOVE,0,-1,-1);
		processUnblockableTarget(DCConstants.MOVE,0,-1,0);
		processUnblockableTarget(DCConstants.MOVE,0,-1,1);
		processUnblockableTarget(DCConstants.MOVE,0,0,-1);
		processUnblockableTarget(DCConstants.MOVE,0,0,1);
		processUnblockableTarget(DCConstants.MOVE,0,1,-1);
		processUnblockableTarget(DCConstants.MOVE,0,1,0);
		processUnblockableTarget(DCConstants.MOVE,0,1,1);
	
		//2: king-like capt to all adjacent squares
		processUnblockableTarget(DCConstants.CAPT,0,-1,-1);
		processUnblockableTarget(DCConstants.CAPT,0,-1,0);
		processUnblockableTarget(DCConstants.CAPT,0,-1,1);
		processUnblockableTarget(DCConstants.CAPT,0,0,-1);
		processUnblockableTarget(DCConstants.CAPT,0,0,1);
		processUnblockableTarget(DCConstants.CAPT,0,1,-1);
		processUnblockableTarget(DCConstants.CAPT,0,1,0);
		processUnblockableTarget(DCConstants.CAPT,0,1,1);

		//moves only on middle board

		if (location.getBoard() == DCConstants.BOARD_MIDDLE) {
			//3: knight-like moves on middle board only
			processUnblockableTarget(DCConstants.MOVE,0,-2,-1);
			processUnblockableTarget(DCConstants.MOVE,0,-2,+1);
			processUnblockableTarget(DCConstants.MOVE,0,-1,-2);
			processUnblockableTarget(DCConstants.MOVE,0,-1,+2);
			processUnblockableTarget(DCConstants.MOVE,0,1,-2);
			processUnblockableTarget(DCConstants.MOVE,0,1,+2);
			processUnblockableTarget(DCConstants.MOVE,0,2,-1);
			processUnblockableTarget(DCConstants.MOVE,0,2,+1);

			//4: knight-like capts on middle board only
			processUnblockableTarget(DCConstants.CAPT,0,-2,-1);
			processUnblockableTarget(DCConstants.CAPT,0,-2,+1);
			processUnblockableTarget(DCConstants.CAPT,0,-1,-2);
			processUnblockableTarget(DCConstants.CAPT,0,-1,+2);
			processUnblockableTarget(DCConstants.CAPT,0,1,-2);
			processUnblockableTarget(DCConstants.CAPT,0,1,+2);
			processUnblockableTarget(DCConstants.CAPT,0,2,-1);
			processUnblockableTarget(DCConstants.CAPT,0,2,+1);
		}

		//moves NOT on upper board
		if (location.getBoard() != DCConstants.BOARD_TOP) {
			//5: knight-like move 1 board up
			processUnblockableTarget(DCConstants.MOVE,1,-2,0);
			processUnblockableTarget(DCConstants.MOVE,1,0,-2);
			processUnblockableTarget(DCConstants.MOVE,1,0,2);
			processUnblockableTarget(DCConstants.MOVE,1,2,0);

			//6: knight-like capts 1 board up
			processUnblockableTarget(DCConstants.CAPT,1,-2,0);
			processUnblockableTarget(DCConstants.CAPT,1,0,-2);
			processUnblockableTarget(DCConstants.CAPT,1,0,2);
			processUnblockableTarget(DCConstants.CAPT,1,2,0);
		} else {
			//moves only on upper board
			//7: knight-like move 2 boards down
			processUnblockableTarget(DCConstants.MOVE,-2,-1,0);
			processUnblockableTarget(DCConstants.MOVE,-2,0,-1);
			processUnblockableTarget(DCConstants.MOVE,-2,0,1);
			processUnblockableTarget(DCConstants.MOVE,-2,1,0);

			//8: knight-like capt 2 boards down
			processUnblockableTarget(DCConstants.CAPT,-2,-1,0);
			processUnblockableTarget(DCConstants.CAPT,-2,0,-1);
			processUnblockableTarget(DCConstants.CAPT,-2,0,1);
			processUnblockableTarget(DCConstants.CAPT,-2,1,0);
		}

		//moves NOT on lower board
		if (location.getBoard() != DCConstants.BOARD_BOTTOM) {
			//9: knight-like move 1 board down
			processUnblockableTarget(DCConstants.MOVE,-1,-2,0);
			processUnblockableTarget(DCConstants.MOVE,-1,0,-2);
			processUnblockableTarget(DCConstants.MOVE,-1,0,2);
			processUnblockableTarget(DCConstants.MOVE,-1,2,0);

			//10: knight-like capts 1 board down
			processUnblockableTarget(DCConstants.CAPT,-1,-2,0);
			processUnblockableTarget(DCConstants.CAPT,-1,0,-2);
			processUnblockableTarget(DCConstants.CAPT,-1,0,2);
			processUnblockableTarget(DCConstants.CAPT,-1,2,0);
		} else {
			//moves only on lower board
			//11: knight-like move 2 boards up
			processUnblockableTarget(DCConstants.MOVE,+2,-1,0);
			processUnblockableTarget(DCConstants.MOVE,+2,0,-1);
			processUnblockableTarget(DCConstants.MOVE,+2,0,1);
			processUnblockableTarget(DCConstants.MOVE,+2,1,0);

			//12: knight-like capt 2 boards up
			processUnblockableTarget(DCConstants.CAPT,+2,-1,0);
			processUnblockableTarget(DCConstants.CAPT,+2,0,-1);
			processUnblockableTarget(DCConstants.CAPT,+2,0,1);
			processUnblockableTarget(DCConstants.CAPT,+2,1,0);
		}
		return list;
	}
} 

/* END OF FILE */
