/*
 * Classname			: DCKingPiece
 * Author				: knys <koen.nys@pandora.be>
 * Creation Date		: Wed Oct 16 20:43:54 CEST 2002 
 * Last Updated			: Sat Dec 07 16:44:49 CET 2002 
 * Description			: A DC King piece
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
 * Represents a King piece on the board.
 *
 * <p>A king has the following moves :
 * <ul>
 * <li>On the middle board, it can move or capture to all adjacent squares and
 * to the squares directly above or beneath it
 * <li>on the top and bottom board, it can move or capture to the square
 * directly above or beneath in on the middle board
 * </ul>
 * @author Koen Nys
 * @author Davy Herben
 * @version 021207
 */ 
public class DCKingPiece extends DCPiece {

	/*
	 * CONSTRUCTORS
	 *
	 */

	/* see DCPiece for comments */
	public DCKingPiece(int player, DCGameBoard board) {
		super(player, board);
		type = 'K';
	}
	
	/*
	 * METHODS
	 *
	 */
	
	/* see DCPiece for comments */
	public DCMoveList getValidMoveList() {
		//empty list
		list = new DCMoveList();

		//moves on middle board
		if (location.getBoard() == DCConstants.BOARD_MIDDLE) {
			
			//1: moves to adjecent squares
			processUnblockableTarget(DCConstants.MOVE, 0, -1, 0);
			processUnblockableTarget(DCConstants.MOVE, 0, 0, -1);
			processUnblockableTarget(DCConstants.MOVE, 0, 0, 1);
			processUnblockableTarget(DCConstants.MOVE, 0, 1, 0);
			processUnblockableTarget(DCConstants.MOVE, 0, -1, -1);
			processUnblockableTarget(DCConstants.MOVE, 0, 1, -1);
			processUnblockableTarget(DCConstants.MOVE, 0, 1, 1);
			processUnblockableTarget(DCConstants.MOVE, 0, -1, 1);
			
			//2: captures to adjecent squares
			processUnblockableTarget(DCConstants.CAPT, 0, -1, 0);
			processUnblockableTarget(DCConstants.CAPT, 0, 0, -1);
			processUnblockableTarget(DCConstants.CAPT, 0, 0, 1);
			processUnblockableTarget(DCConstants.CAPT, 0, 1, 0);
			processUnblockableTarget(DCConstants.CAPT, 0, -1, -1);
			processUnblockableTarget(DCConstants.CAPT, 0, 1, -1);
			processUnblockableTarget(DCConstants.CAPT, 0, 1, 1);
			processUnblockableTarget(DCConstants.CAPT, 0, -1, 1);

			//3: moves and captures up and down
			processUnblockableTarget(DCConstants.MOVE, 1, 0, 0);
			processUnblockableTarget(DCConstants.MOVE, -1, 0, 0);
			processUnblockableTarget(DCConstants.CAPT, 1, 0, 0);
			processUnblockableTarget(DCConstants.CAPT, -1, 0, 0);

		//moves on the top board
		} else if(location.getBoard() == DCConstants.BOARD_TOP) {

			//1: move or capture down
			processUnblockableTarget(DCConstants.MOVE, -1, 0, 0);
			processUnblockableTarget(DCConstants.CAPT, -1, 0, 0);

		//moves on the top board
		} else { 

			//1: move or capture up 
			processUnblockableTarget(DCConstants.MOVE, 1, 0, 0);
			processUnblockableTarget(DCConstants.CAPT, 1, 0, 0);

		}	
		return list;
	}
} 

/* END OF FILE */
