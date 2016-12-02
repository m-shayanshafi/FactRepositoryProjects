/*
 * Classname			: DCDwarfPiece
 * Author				: knys <koen.nys@pandora.be>
 * Creation Date		: Wed Oct 16 20:43:54 CEST 2002 
 * Last Updated			: Fri Dec 06 15:46:58 CET 2002 
 * Description			: A DC Dwarf piece
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
 * Represents a Dwarf piece on the board.
 * <p>A dwarf can move on the bottom and middle board and 
 * has the following rules :
 * <ul>
 * <li>move and capture to the adjacent squares left and right 
 * <li>move, but not capture, to the adjacent square directly ahead
 * <li>capture from the bottom to the middle board to the square
 * directly above the piece
 * <li>move, but not capture, from the middle to the bottom board to the square
 * directly beneath the piece
 * </ul>
 * 
 * @author Koen Nys
 * @author Davy Herben
 * @version 021206
 */ 
public class DCDwarfPiece extends DCPiece {

	/* see DCPiece for comments */
	public DCDwarfPiece(int player, DCGameBoard board) {
		super(player, board);
		type = 'D';
	}

	/*
	 * METHODS
	 *
	 */
	
	/* see DCPiece for comments */
	public DCMoveList getValidMoveList() {
	    //empty list
	    list = new DCMoveList();
	    
	    //moves for player gold
	    if (player == DCConstants.PLAYER_GOLD){

			//moves while on the middle board
			if (location.getBoard() == DCConstants.BOARD_MIDDLE) {
		    
			    //1: moves on the middle board
			    processUnblockableTarget(DCConstants.MOVE, 0, -1, 0);
			    processUnblockableTarget(DCConstants.MOVE, 0, +1, 0);
			    processUnblockableTarget(DCConstants.MOVE, 0, 0, +1);
		    
			    //2: captures on the middle board
			   	processUnblockableTarget(DCConstants.CAPT, 0, -1, +1);
		 	   	processUnblockableTarget(DCConstants.CAPT, 0, +1, +1);
		    
			    //3: moves on the bottom board
			    processUnblockableTarget(DCConstants.MOVE, -1, 0, 0);
		   
		        //moves while on the bottom board
			} else {
		    
			    //1: moves on the bottom board
			    processUnblockableTarget(DCConstants.MOVE, 0, -1, 0);
			    processUnblockableTarget(DCConstants.MOVE, 0, +1, 0);
			    processUnblockableTarget(DCConstants.MOVE, 0, 0, +1);
		    
			    //2: captures on the bottom board
			    processUnblockableTarget(DCConstants.CAPT, 0, -1, +1);
			    processUnblockableTarget(DCConstants.CAPT, 0, +1, +1);
		    
			    //3: captures on the middle board
			    processUnblockableTarget(DCConstants.CAPT, +1, 0, 0);
			}
	    //moves for player scarlet	    
	    } else {
	    
			//moves while on the middle board
			if (location.getBoard() == DCConstants.BOARD_MIDDLE) {
		    
			    //1: moves on the middle board
			    processUnblockableTarget(DCConstants.MOVE, 0, -1, 0);
			    processUnblockableTarget(DCConstants.MOVE, 0, +1, 0);
			    processUnblockableTarget(DCConstants.MOVE, 0, 0, -1);
		    
			    //2: captures on the middle board
			    processUnblockableTarget(DCConstants.CAPT, 0, +1, -1);
			    processUnblockableTarget(DCConstants.CAPT, 0, -1, -1);
		    
			    //3: moves on the bottom board
			    processUnblockableTarget(DCConstants.MOVE, -1, 0, 0);
		   
	        //moves while on the bottom board
			} else {
		    
			    //1: moves on the bottom board
			    processUnblockableTarget(DCConstants.MOVE, 0, -1, 0);
			    processUnblockableTarget(DCConstants.MOVE, 0, +1, 0);
			    processUnblockableTarget(DCConstants.MOVE, 0, 0, -1);
		    
			    //2: captures on the bottom board
			    processUnblockableTarget(DCConstants.CAPT, 0, +1, -1);
			    processUnblockableTarget(DCConstants.CAPT, 0, -1, -1);
		    
			    //3: captures on the middle board
			    processUnblockableTarget(DCConstants.CAPT, +1, 0, 0);
			}
	    }
	    return list;	
	 }
} 

/* END OF FILE */
