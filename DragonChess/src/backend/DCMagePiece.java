/*
 * Classname			: DCMagePiece
 * Author				: TheBlackUnicorn <TheBlackUnicorn@softhome.net>
 * Creation Date		: Wed Oct 16 20:43:54 CEST 2002 
 * Last Updated			: Sat Dec 07 17:11:46 CET 2002 
 * Description			: A DC Mage piece
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
 * Represents a Mage piece on the board.
 *
 * <p>A mage has the following moves :
 * <ul>
 * <li>move or capt between boards by moving one or two squares directly up or
 * down, but only if there are no intervening pieces.
 * <li>on the middle board, move/capt to all squares in an orthogonal or diagonal
 * line, as long as there are no intervening pieces
 * <li>on the top or bottom board, move or capt to orthogonally adjacent
 * squares
 * </ul>
 * 
 * @author Davy Herben
 * @version 021207 
 */ 
public class DCMagePiece extends DCPiece {

	/*
	 * CONSTRUCTORS
	 *
	 */

	/* see DCPiece for comments */
	public DCMagePiece(int player, DCGameBoard board) {
		super(player, board);
		type = 'M';
	}
		
	/*
	 * METHODS
	 *
	 */
	
	/* see DCPiece for comments */
	public DCMoveList getValidMoveList() {
		//empty list
		list = new DCMoveList();

		//moves on all boards
		
		//1: one or two boards directly up/down move/capt if intervening
		//unblocked
		processLineOfTargets(-1,0,0);
		processLineOfTargets(1,0,0);

		//moves on middle board
		if (location.getBoard() == DCConstants.BOARD_MIDDLE) {
			//2: All possible lines (diagonal and orthogonal) on this board
			processLineOfTargets(0,-1,-1);
			processLineOfTargets(0,-1,0);
			processLineOfTargets(0,-1,1);
			processLineOfTargets(0,0,-1);
			processLineOfTargets(0,0,1);
			processLineOfTargets(0,1,-1);
			processLineOfTargets(0,1,0);
			processLineOfTargets(0,1,1);
		//moves on other boards
		} else {
			//3: Move to all adjacent orthogonal on same board
			processUnblockableTarget(DCConstants.MOVE, 0, -1, 0);
			processUnblockableTarget(DCConstants.MOVE, 0, 0, -1);
			processUnblockableTarget(DCConstants.MOVE, 0, 0, 1);
			processUnblockableTarget(DCConstants.MOVE, 0, 1, 0);
			
			//4: Capt to all adjacent orthogonal on same board
			processUnblockableTarget(DCConstants.CAPT, 0, -1, 0);
			processUnblockableTarget(DCConstants.CAPT, 0, 0, -1);
			processUnblockableTarget(DCConstants.CAPT, 0, 0, 1);
			processUnblockableTarget(DCConstants.CAPT, 0, 1, 0);
		}	

		return list;
	}

} 

/* END OF FILE */
