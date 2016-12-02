/*
 * Classname			: DCUnicornPiece
 * Author				: knys <koen.nys@pandora.be>
 * Creation Date		: Wed Oct 16 20:43:54 CEST 2002 
 * Last Updated			: Sun Dec 08 13:33:13 CET 2002 
 * Description			: A DC Unicorn piece
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
 * Represents a Unicorn piece on the board.
 *
 * <p>A unicorn can only move on the middle board and has the following moves
 * there :
 * <ul>
 * <li>unblockable move or capt like a knight : 2 squares in one direction,
 * then one in a perpendicular direction
 * </ul>
 * 
 * @author Koen Nys
 * @author Davy Herben
 * @version 021208
 */ 
public class DCUnicornPiece extends DCPiece {

	/*
	 * CONSTRUCTORS
	 *
	 */

	/* see DCPiece for comments */
	public DCUnicornPiece(int player, DCGameBoard board) {
		super(player, board);
		type = 'U';
	}
	
	/*
	 * METHODS
	 *
	 */

	/* see DCPiece for comments */
	public DCMoveList getValidMoveList() {
		//empty list
		list = new DCMoveList();
		
		//1: moves
		processUnblockableTarget(DCConstants.MOVE, 0, +1, +2);
		processUnblockableTarget(DCConstants.MOVE, 0, +1, -2);
		processUnblockableTarget(DCConstants.MOVE, 0, -1, +2);
		processUnblockableTarget(DCConstants.MOVE, 0, -1, -2);
		processUnblockableTarget(DCConstants.MOVE, 0, +2, +1);
		processUnblockableTarget(DCConstants.MOVE, 0, +2, -1);
		processUnblockableTarget(DCConstants.MOVE, 0, -2, +1);
		processUnblockableTarget(DCConstants.MOVE, 0, -2, -1);
		
		//2: captures
		processUnblockableTarget(DCConstants.CAPT, 0, +1, +2);
		processUnblockableTarget(DCConstants.CAPT, 0, +1, -2);
		processUnblockableTarget(DCConstants.CAPT, 0, -1, +2);
		processUnblockableTarget(DCConstants.CAPT, 0, -1, -2);
		processUnblockableTarget(DCConstants.CAPT, 0, +2, +1);
		processUnblockableTarget(DCConstants.CAPT, 0, +2, -1);
		processUnblockableTarget(DCConstants.CAPT, 0, -2, +1);
		processUnblockableTarget(DCConstants.CAPT, 0, -2, -1);

		return list;
	}
} 

/* END OF FILE */
