/*
 * Classname			: DCElementalPiece
 * Author				: TheBlackUnicorn <TheBlackUnicorn@softhome.net>
 * Creation Date		: Wed Oct 16 20:43:54 CEST 2002 
 * Last Updated			: Fri Dec 06 15:53:53 CET 2002 
 * Description			: A DC Elemental piece
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
 * Represents an Elemental piece on the board.
 *
 * <p>An elemental has the following moves on the bottom board :
 * <ul>
 * <li>move or capture to all adjacent squares on the same board
 * <li>move or capture to all squares 2 places from the piece orthogonally on
 * the same board, but only if the intervening square is unoccupied
 * <li>capture to the middle board by first moving to a square orthogonally
 * adjacent to the piece, then one square straight up. This move is only
 * possible if the intervening square is unoccupied.
 * </ul>
 *
 * <p>An elemental has the following moves on the middle board :
 * <ul>
 * <li>move or capture to the bottom board by first moving one square directly
 * down, then moving one square in an orthogonal direction
 * </ul>
 * 
 * @author Davy Herben
 * @version 021206 
 */ 
public class DCElementalPiece extends DCPiece {

	/*
	 * CONSTRUCTORS
	 *
	 */

	/* see DCPiece for comments */
	public DCElementalPiece(int player, DCGameBoard board) {
		super(player, board);
		type = 'E';
	}
	
	/*
	 * METHODS
	 *
	 */
	
	/* see DCPiece for comments */
	public DCMoveList getValidMoveList() {
		//empty list
		list = new DCMoveList();

		//moves on lower board
		if (location.getBoard() == DCConstants.BOARD_BOTTOM) {
			//1: unblockable move to diagonally adjacent field on same board
			processUnblockableTarget(DCConstants.MOVE, 0, -1, -1);
			processUnblockableTarget(DCConstants.MOVE, 0, -1, +1);
			processUnblockableTarget(DCConstants.MOVE, 0, +1, -1);
			processUnblockableTarget(DCConstants.MOVE, 0, +1, +1);

			//2: unblockable move to orthogonally adjacent fields on same board
			processUnblockableTarget(DCConstants.MOVE, 0, 0, -1);
			processUnblockableTarget(DCConstants.MOVE, 0, 0, +1);
			processUnblockableTarget(DCConstants.MOVE, 0, -1, 0);
			processUnblockableTarget(DCConstants.MOVE, 0, +1, 0);

			//3: unblockable capt to orthogonally adjacent fields on same board
			processUnblockableTarget(DCConstants.CAPT, 0, 0, -1);
			processUnblockableTarget(DCConstants.CAPT, 0, 0, +1);
			processUnblockableTarget(DCConstants.CAPT, 0, -1, 0);
			processUnblockableTarget(DCConstants.CAPT, 0, +1, 0);

			//4: blockable move to orthogonal fields 2 fields away on same board
			processBlockableTarget(DCConstants.MOVE, 0, 0, -2, 0, 0, -1);
			processBlockableTarget(DCConstants.MOVE, 0, 0, +2, 0, 0, +1);
			processBlockableTarget(DCConstants.MOVE, 0, -2, 0, 0, -1, 0);
			processBlockableTarget(DCConstants.MOVE, 0, +2, 0, 0, +1, 0);

			//5: blockable capt to orthogonal fields 2 fields away on same board
			processBlockableTarget(DCConstants.CAPT, 0, 0, -2, 0, 0, -1);
			processBlockableTarget(DCConstants.CAPT, 0, 0, +2, 0, 0, +1);
			processBlockableTarget(DCConstants.CAPT, 0, -2, 0, 0, -1, 0);
			processBlockableTarget(DCConstants.CAPT, 0, +2, 0, 0, +1, 0);

			//6: blockable capt to orthogonal adjacent field 1 board higher
			processBlockableTarget(DCConstants.CAPT, 1, 0, -1, 0, 0, -1);
			processBlockableTarget(DCConstants.CAPT, 1, 0, +1, 0, 0, +1);
			processBlockableTarget(DCConstants.CAPT, 1, -1, 0, 0, -1, 0);
			processBlockableTarget(DCConstants.CAPT, 1, +1, 0, 0, +1, 0);
		}

		//moves on middle board
		if (location.getBoard() == DCConstants.BOARD_MIDDLE) {
			//7: blockable move to orthogonal adjacent field 1 board lower
			processBlockableTarget(DCConstants.MOVE, -1, 0, -1, -1, 0, 0);
			processBlockableTarget(DCConstants.MOVE, -1, 0, +1, -1, 0, 0);
			processBlockableTarget(DCConstants.MOVE, -1, -1, 0, -1, 0, 0);
			processBlockableTarget(DCConstants.MOVE, -1, +1, 0, -1, 0, 0);

			//8: blockable capt to orthogonal adjacent fields 1 board lower
			processBlockableTarget(DCConstants.CAPT, -1, 0, -1, -1, 0, 0);
			processBlockableTarget(DCConstants.CAPT, -1, 0, +1, -1, 0, 0);
			processBlockableTarget(DCConstants.CAPT, -1, -1, 0, -1, 0, 0);
			processBlockableTarget(DCConstants.CAPT, -1, +1, 0, -1, 0, 0);
			
		}
		return list;
	}
} 

/* END OF FILE */
