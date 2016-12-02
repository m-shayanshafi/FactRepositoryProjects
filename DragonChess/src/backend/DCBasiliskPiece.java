/*
 * Classname			: DCBasiliskPiece
 * Author				: knys <koen.nys@pandora.be>
 * Creation Date		: Wed Oct 16 20:43:54 CEST 2002 
 * Last Updated			: Thu Dec 05 23:54:43 CET 2002 
 * Description			: A DC Basilisk piece
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
 * Represents a Basilisk piece on the board. 
 *
 * <p>The basilisk has the following moves :
 * <ul>
 * <li>move or capture to the adjacent squares ahead, left ahead and
 * right ahead
 * <li>move, but not capture, to the adjactent square directly behind
 * </ul>
 * 
 * <p>Apart from these moves, the basilisk also has the ability to freeze an
 * enemy piece directly above it on the middle board. This ability is not
 * implemented in this class. The {@link DCLocalGameEnv} class implements this.
 * 
 * @author Koen Nys
 * @author Davy Herben
 * @version 021121 
 */ 
public class DCBasiliskPiece extends DCPiece {

	/*
	 * CONSTRUCTORS
	 *
	 */

	/* see DCPiece for comment */
	public DCBasiliskPiece(int player, DCGameBoard board) {
		super(player, board);
		type = 'B';
	}

	/*
	 * METHODS
	 *
	 */
	
	/* see DCPiece for comment */
	public DCMoveList getValidMoveList() {
	    // empty list
	    list = new DCMoveList();
	    
	    if (player == DCConstants.PLAYER_GOLD){
		
			//1: unblockable moves to diagonal forward fields
			processUnblockableTarget(DCConstants.MOVE, 0, -1, +1);
			processUnblockableTarget(DCConstants.MOVE, 0, +1, +1);

			//2: unblockable captures to diagonal forward fields
			processUnblockableTarget(DCConstants.CAPT, 0, -1, +1);
			processUnblockableTarget(DCConstants.CAPT, 0, +1, +1);
	    
			//3: unblockable move to the forward and backward fields
			processUnblockableTarget(DCConstants.MOVE, 0, 0, +1);
			processUnblockableTarget(DCConstants.MOVE, 0, 0, -1);
	    
			//4: unblockable capture to the forward field
			processUnblockableTarget(DCConstants.CAPT, 0, 0, +1);
		
	    } else {
		
			//1: unblockable moves to diagonal forward fields
			processUnblockableTarget(DCConstants.MOVE, 0, +1, -1);
			processUnblockableTarget(DCConstants.MOVE, 0, -1, -1);

			//2: unblockable captures to diagonal forward fields
			processUnblockableTarget(DCConstants.CAPT, 0, +1, -1);
			processUnblockableTarget(DCConstants.CAPT, 0, -1, -1);
	    
			//3: unblockable move to the forward and backward fields
			processUnblockableTarget(DCConstants.MOVE, 0, 0, -1);
			processUnblockableTarget(DCConstants.MOVE, 0, 0, +1);
	    
			//4: unblockable capture to the forward field
			processUnblockableTarget(DCConstants.CAPT, 0, 0, -1);
	    }
		
	    return list;
	}
} 

/* END OF FILE */
