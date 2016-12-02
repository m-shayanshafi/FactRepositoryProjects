/*
 * Classname			: DCDragonPiece
 * Author				: TheBlackUnicorn <TheBlackUnicorn@softhome.net>
 * Creation Date		: Wed Oct 16 20:43:54 CEST 2002 
 * Last Updated			: Fri Dec 06 15:44:18 CET 2002 
 * Description			: A DC Dragon piece
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
 * Represents a Dragon piece on the board.
 *
 * <p>A Dragon always stays on the top board. It has the following rules :
 * <ul>
 * <li>move or capture to all adjacent squares on the same board
 * <li>move or capture on all diagonal lines on same board, until blocked by
 * pieces
 * <li>capture from afar to the square directly below the piece on the middle
 * board, and to all orthogonally adjacent squares of that square
 * </ul>
 * 
 * @author Davy Herben
 * @version 021206 
 */ 
public class DCDragonPiece extends DCPiece {

	/*
	 * CONSTRUCTORS
	 *
	 */
	
	/* see DCPiece for comments */
	public DCDragonPiece(int player, DCGameBoard board) {
		super(player, board);
		type = 'R';
	}
	
	/*
	 * METHODS
	 *
	 */

	/* see DCPiece for comments */
	public DCMoveList getValidMoveList() {
		//empty list
		list = new DCMoveList();
		
		//1 : unblockable moves to orthogonally adjacent fields
		processUnblockableTarget(DCConstants.MOVE, 0, 0, 1);
		processUnblockableTarget(DCConstants.MOVE, 0, 1, 0);
		processUnblockableTarget(DCConstants.MOVE, 0, 0, -1);
		processUnblockableTarget(DCConstants.MOVE, 0, -1, 0);
		
		//2 : unblockable capts to orthogonally adjacent fields
		processUnblockableTarget(DCConstants.CAPT, 0, 0, 1);
		processUnblockableTarget(DCConstants.CAPT, 0, 1, 0);
		processUnblockableTarget(DCConstants.CAPT, 0, 0, -1);
		processUnblockableTarget(DCConstants.CAPT, 0, -1, 0);
		
		//3 : unblockable cafars to middle board
		processUnblockableTarget(DCConstants.CAFAR, -1, 0, 0);
		processUnblockableTarget(DCConstants.CAFAR, -1, 0, 1);
		processUnblockableTarget(DCConstants.CAFAR, -1, 1, 0);
		processUnblockableTarget(DCConstants.CAFAR, -1, 0, -1);
		processUnblockableTarget(DCConstants.CAFAR, -1, -1, 0);
		
		//4 : blockable move/capt to all diagonals on same board
		processLineOfTargets(0, -1, -1);
		processLineOfTargets(0, 1, 1);
		processLineOfTargets(0, -1, 1);
		processLineOfTargets(0, 1, -1);
		
		return list;
	}
} 

/* END OF FILE */
