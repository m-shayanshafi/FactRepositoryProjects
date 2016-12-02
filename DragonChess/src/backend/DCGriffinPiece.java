/*
 * Classname			: DCGriffinPiece
 * Author				: knys <koen.nys@pandora.be>
 * Creation Date		: Wed Oct 16 20:43:54 CEST 2002 
 * Last Updated			: Fri Dec 06 16:01:02 CET 2002 
 * Description			: A DC Griffin piece
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
 * Represents a Griffin piece on the board.
 *
 * <p>A griffin on the top board has the following moves :
 * <ul>
 * <li>move and capture on the same board by performing a knight-like move,
 * moving three squares to an orthogonal direction, then 2 squares
 * perpendicularly
 * <li>move and capture to the middle board to a square diagonally adjacent
 * to the square directly below the piece
 * </ul>
 *
 * <p>A griffin on the middle board has the following moves :
 * <ul>
 * <li>move and capture to all diagonally adjacent squares on the same board
 * <li>move and capture to the top board to one of the squares diagonally
 * adjacent to the square directly above the piece
 * </ul>
 * 
 * @author Koen Nys
 * @author Davy Herben
 * @version 021206
 */ 
public class DCGriffinPiece extends DCPiece {

	/*
	 * CONSTRUCTORS
	 *
	 */

	/* see DCPiece for comments */
	public DCGriffinPiece(int player, DCGameBoard board) {
		super(player, board);
		type = 'G';
	}
	
	/*
	 * METHODS
	 *
	 */
	
	/* see DCPiece for comments */
	public DCMoveList getValidMoveList(){
	    //empty list
	    list = new DCMoveList();

	    //moves while on the top board
	    if (location.getBoard() == DCConstants.BOARD_TOP) {

			//1: moves on the top board
			processUnblockableTarget(DCConstants.MOVE, 0, -3, +2);
			processUnblockableTarget(DCConstants.MOVE, 0, -3, -2);
			processUnblockableTarget(DCConstants.MOVE, 0, -2, +3);
			processUnblockableTarget(DCConstants.MOVE, 0, -2, -3);
			processUnblockableTarget(DCConstants.MOVE, 0, +3, +2);
			processUnblockableTarget(DCConstants.MOVE, 0, +3, -2);
			processUnblockableTarget(DCConstants.MOVE, 0, +2, +3);
			processUnblockableTarget(DCConstants.MOVE, 0, +2, -3);
		
			//2: captures on the top board
			processUnblockableTarget(DCConstants.CAPT, 0, -3, +2);
			processUnblockableTarget(DCConstants.CAPT, 0, -3, -2);	
			processUnblockableTarget(DCConstants.CAPT, 0, -2, +3);
			processUnblockableTarget(DCConstants.CAPT, 0, -2, -3);
			processUnblockableTarget(DCConstants.CAPT, 0, +3, +2);
			processUnblockableTarget(DCConstants.CAPT, 0, +3, -2);
			processUnblockableTarget(DCConstants.CAPT, 0, +2, +3);
			processUnblockableTarget(DCConstants.CAPT, 0, +2, -3);
		
			//3: moves on the middle board
			processUnblockableTarget(DCConstants.MOVE, -1, -1, -1);
			processUnblockableTarget(DCConstants.MOVE, -1, +1, +1);
			processUnblockableTarget(DCConstants.MOVE, -1, -1, +1);
			processUnblockableTarget(DCConstants.MOVE, -1, +1, -1);
		
			//4: captures on the middle board
			processUnblockableTarget(DCConstants.CAPT, -1, -1, -1);
			processUnblockableTarget(DCConstants.CAPT, -1, +1, +1);
			processUnblockableTarget(DCConstants.CAPT, -1, -1, +1);
			processUnblockableTarget(DCConstants.CAPT, -1, +1, -1);
	   
	    //moves while on the middle board
	    } else {
		
			//1: moves on the middle board
			processUnblockableTarget(DCConstants.MOVE, 0, -1, -1);
			processUnblockableTarget(DCConstants.MOVE, 0, +1, +1);
			processUnblockableTarget(DCConstants.MOVE, 0, -1, +1);
			processUnblockableTarget(DCConstants.MOVE, 0, +1, -1);
		
			//2: captures on the middle board
			processUnblockableTarget(DCConstants.CAPT, 0, -1, -1);
			processUnblockableTarget(DCConstants.CAPT, 0, +1, +1);
			processUnblockableTarget(DCConstants.CAPT, 0, -1, +1);
			processUnblockableTarget(DCConstants.CAPT, 0, +1, -1);
		
			//3: moves on the top board
			processUnblockableTarget(DCConstants.MOVE, +1, -1, -1);
			processUnblockableTarget(DCConstants.MOVE, +1, +1, +1);
			processUnblockableTarget(DCConstants.MOVE, +1, -1, +1);
			processUnblockableTarget(DCConstants.MOVE, +1, +1, -1);
		
			//4: captures on the top board
			processUnblockableTarget(DCConstants.CAPT, +1, -1, -1);
			processUnblockableTarget(DCConstants.CAPT, +1, +1, +1);
			processUnblockableTarget(DCConstants.CAPT, +1, -1, +1);
			processUnblockableTarget(DCConstants.CAPT, +1, +1, -1);
	    }
	    return list;
	}
} 

/* END OF FILE */
