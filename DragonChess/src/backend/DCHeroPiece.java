/*
 * Classname			: DCHeroPiece
 * Author				: knys <koen.nys@pandora.be>
 * Creation Date		: Wed Oct 16 20:43:54 CEST 2002 
 * Last Updated			: Sat Dec 07 16:36:32 CET 2002 
 * Description			: A DC Hero piece
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
 * Represents a Hero piece on the board.
 *
 * <p>A Hero on the middle board has the following moves :
 * <ul>
 * <li>move or capture to the squares diagonally adjacent to the piece or 2
 * squares from the piece diagonally. The hero can jump over intervening
 * squares
 * <li>move or capture to the top or bottom board to the squares diagonally
 * adjacent to the squares directly above or beneath it
 * </ul>
 *
 * <p>A Hero on the top or bottom boards has the following moves :
 * <ul>
 * <li>return to the middle board by moving or capturing to the squares
 * diagonally adjacent to the piece directly above or beneath it.
 * </ul>
 * 
 * @author Koen Nys
 * @author Davy Herben
 * @version 021122 
 */ 
public class DCHeroPiece extends DCPiece {

	/*
	 * CONSTRUCTORS
	 *
	 */

	/* see DCPiece for comments */
	public DCHeroPiece(int player, DCGameBoard board) {
		super(player, board);
		type = 'H';
	}
	
	/*
	 * METHODS
	 *
	 */

	/* see DCPiece for comments */
	public DCMoveList getValidMoveList() {
	    //empty list
	    list = new DCMoveList();

	    //moves while on the middle board
	    if (location.getBoard() == DCConstants.BOARD_MIDDLE) {

			//1: moves on the middle board
			processUnblockableTarget(DCConstants.MOVE, 0, -1, -1);
			processUnblockableTarget(DCConstants.MOVE, 0, +1, +1);
			processUnblockableTarget(DCConstants.MOVE, 0, -1, +1);
			processUnblockableTarget(DCConstants.MOVE, 0, +1, -1);
			processUnblockableTarget(DCConstants.MOVE, 0, -2, -2);
			processUnblockableTarget(DCConstants.MOVE, 0, +2, +2);
			processUnblockableTarget(DCConstants.MOVE, 0, -2, +2);
			processUnblockableTarget(DCConstants.MOVE, 0, +2, -2);
		
			//2: captures on the middle board
			processUnblockableTarget(DCConstants.CAPT, 0, -1, -1);
			processUnblockableTarget(DCConstants.CAPT, 0, +1, +1);
			processUnblockableTarget(DCConstants.CAPT, 0, -1, +1);
			processUnblockableTarget(DCConstants.CAPT, 0, +1, -1);
			processUnblockableTarget(DCConstants.CAPT, 0, -2, -2);
			processUnblockableTarget(DCConstants.CAPT, 0, +2, +2);
			processUnblockableTarget(DCConstants.CAPT, 0, -2, +2);
			processUnblockableTarget(DCConstants.CAPT, 0, +2, -2);

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
	
			//5: moves on the bottom board
			processUnblockableTarget(DCConstants.MOVE, -1, -1, -1);
			processUnblockableTarget(DCConstants.MOVE, -1, +1, +1);
			processUnblockableTarget(DCConstants.MOVE, -1, -1, +1);
			processUnblockableTarget(DCConstants.MOVE, -1, +1, -1);
		
			//6: captures on the bottom board
			processUnblockableTarget(DCConstants.CAPT, -1, -1, -1);
			processUnblockableTarget(DCConstants.CAPT, -1, +1, +1);
			processUnblockableTarget(DCConstants.CAPT, -1, -1, +1);
			processUnblockableTarget(DCConstants.CAPT, -1, +1, -1);
	    } else {
	   
			//moves while on the top board
			if (location.getBoard() == DCConstants.BOARD_TOP) {
	
			    //1: moves on the middle board
			    processUnblockableTarget(DCConstants.MOVE, -1, -1, -1);
			    processUnblockableTarget(DCConstants.MOVE, -1, +1, +1);
			    processUnblockableTarget(DCConstants.MOVE, -1, -1, +1);
			    processUnblockableTarget(DCConstants.MOVE, -1, +1, -1);
		
	            //2: captures on the middle board
			    processUnblockableTarget(DCConstants.CAPT, -1, -1, -1);
			    processUnblockableTarget(DCConstants.CAPT, -1, +1, +1);
			    processUnblockableTarget(DCConstants.CAPT, -1, -1, +1);
			    processUnblockableTarget(DCConstants.CAPT, -1, +1, -1);
		
			//moves while on the bottom board
			} else {
		    
			    //1: moves on the middle board
			    processUnblockableTarget(DCConstants.MOVE, +1, -1, -1);
			    processUnblockableTarget(DCConstants.MOVE, +1, +1, +1);
			    processUnblockableTarget(DCConstants.MOVE, +1, -1, +1);
			    processUnblockableTarget(DCConstants.MOVE, +1, +1, -1);
		
	            //2: captures on the middle board
			    processUnblockableTarget(DCConstants.CAPT, +1, -1, -1);
			    processUnblockableTarget(DCConstants.CAPT, +1, +1, +1);
			    processUnblockableTarget(DCConstants.CAPT, +1, -1, +1);
			    processUnblockableTarget(DCConstants.CAPT, +1, +1, -1);
			}
	    }
	    return list;
	}
} 

/* END OF FILE */
