/*
 * Classname			: DCClericPiece
 * Author				: knys <koen.nys@pandora.be>
 * Creation Date		: Wed Oct 16 20:43:54 CEST 2002 
 * Last Updated			: Fri Dec 06 00:30:19 CET 2002 
 * Description			: A DC Cleric piece
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
 * Represents a Cleric piece on the board.
 *
 * <p>A cleric has the following moves :
 * <ul>
 * <li>it can move and capture to any adjacent square on the same board
 * <li>for moving between boards, it can move or capture to the square directly
 * above and below it
 * </ul>
 * 
 * @author Koen Nys
 * @author Davy Herben
 * @version 021206
 */ 
public class DCClericPiece extends DCPiece {

	/*
	 * CONSTRUCTORS
	 *
	 */

	/* see DCPiece for comments */
	public DCClericPiece(int player, DCGameBoard board) {
		super(player, board);
		type = 'C';
	}

	/*
	 * METHODS
	 *
	 */
	
	/* see DCPiece for comments */
	public DCMoveList getValidMoveList() {
		//empty list
	    list = new DCMoveList();

	    //moves while on the top board
	    if (location.getBoard() == DCConstants.BOARD_TOP) {
	    
			//1: moves on the top board
			processUnblockableTarget(DCConstants.MOVE, 0, 0, +1);
			processUnblockableTarget(DCConstants.MOVE, 0, 0, -1);
			processUnblockableTarget(DCConstants.MOVE, 0, +1, +1);
			processUnblockableTarget(DCConstants.MOVE, 0, +1, -1);
			processUnblockableTarget(DCConstants.MOVE, 0, -1, +1);
			processUnblockableTarget(DCConstants.MOVE, 0, -1, -1);
			processUnblockableTarget(DCConstants.MOVE, 0, +1, 0);
			processUnblockableTarget(DCConstants.MOVE, 0, -1, 0);

			//2: captures on the top board
			processUnblockableTarget(DCConstants.CAPT, 0, 0, +1);
			processUnblockableTarget(DCConstants.CAPT, 0, 0, -1);
			processUnblockableTarget(DCConstants.CAPT, 0, +1, +1);
			processUnblockableTarget(DCConstants.CAPT, 0, +1, -1);
			processUnblockableTarget(DCConstants.CAPT, 0, -1, +1);
			processUnblockableTarget(DCConstants.CAPT, 0, -1, -1);	
			processUnblockableTarget(DCConstants.CAPT, 0, +1, 0);
			processUnblockableTarget(DCConstants.CAPT, 0, -1, 0);

			//3: moves and captures on the middle board
			processUnblockableTarget(DCConstants.MOVE, -1, 0, 0);
			processUnblockableTarget(DCConstants.CAPT, -1, 0, 0);
		} else if (location.getBoard() == DCConstants.BOARD_MIDDLE) {
	    
			//1: moves on the middle board
			processUnblockableTarget(DCConstants.MOVE, 0, 0, +1);
			processUnblockableTarget(DCConstants.MOVE, 0, 0, -1);
			processUnblockableTarget(DCConstants.MOVE, 0, +1, +1);
			processUnblockableTarget(DCConstants.MOVE, 0, +1, -1);
			processUnblockableTarget(DCConstants.MOVE, 0, -1, +1);
			processUnblockableTarget(DCConstants.MOVE, 0, -1, -1);
			processUnblockableTarget(DCConstants.MOVE, 0, +1, 0);
			processUnblockableTarget(DCConstants.MOVE, 0, -1, 0);

			//2: captures on the middle board
			processUnblockableTarget(DCConstants.CAPT, 0, 0, +1);
			processUnblockableTarget(DCConstants.CAPT, 0, 0, -1);
			processUnblockableTarget(DCConstants.CAPT, 0, +1, +1);
			processUnblockableTarget(DCConstants.CAPT, 0, +1, -1);
			processUnblockableTarget(DCConstants.CAPT, 0, -1, +1);
			processUnblockableTarget(DCConstants.CAPT, 0, -1, -1);
			processUnblockableTarget(DCConstants.CAPT, 0, +1, 0);
			processUnblockableTarget(DCConstants.CAPT, 0, -1, 0);

			//3: moves and captures on the bottom board
			processUnblockableTarget(DCConstants.MOVE, -1, 0, 0);
			processUnblockableTarget(DCConstants.CAPT, -1, 0, 0);
	    
			//4: moves and captures on the top board
			processUnblockableTarget(DCConstants.MOVE, +1, 0, 0);
			processUnblockableTarget(DCConstants.CAPT, +1, 0, 0);
		} else {
	    
			//1: moves on the bottom board
			processUnblockableTarget(DCConstants.MOVE, 0, 0, +1);
			processUnblockableTarget(DCConstants.MOVE, 0, 0, -1);
			processUnblockableTarget(DCConstants.MOVE, 0, +1, +1);
			processUnblockableTarget(DCConstants.MOVE, 0, +1, -1);
			processUnblockableTarget(DCConstants.MOVE, 0, -1, +1);
			processUnblockableTarget(DCConstants.MOVE, 0, -1, -1);
			processUnblockableTarget(DCConstants.MOVE, 0, +1, 0);
			processUnblockableTarget(DCConstants.MOVE, 0, -1, 0);

			//2: captures on the bottom board
			processUnblockableTarget(DCConstants.CAPT, 0, 0, +1);
			processUnblockableTarget(DCConstants.CAPT, 0, 0, -1);
			processUnblockableTarget(DCConstants.CAPT, 0, +1, +1);
			processUnblockableTarget(DCConstants.CAPT, 0, +1, -1);
			processUnblockableTarget(DCConstants.CAPT, 0, -1, +1);
			processUnblockableTarget(DCConstants.CAPT, 0, -1, -1);
			processUnblockableTarget(DCConstants.CAPT, 0, +1, 0);
			processUnblockableTarget(DCConstants.CAPT, 0, -1, 0);
		
			//3: moves and captures on the middle board
			processUnblockableTarget(DCConstants.MOVE, +1, 0, 0);
			processUnblockableTarget(DCConstants.CAPT, +1, 0, 0);
		}

	 	return list;
	}
} 

/* END OF FILE */
