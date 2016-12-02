/*
 * Classname			: DCWarriorPiece
 * Author				: TheBlackUnicorn <TheBlackUnicorn@softhome.net>
 * Creation Date		: Wed Oct 16 20:43:54 CEST 2002 
 * Last Updated			: Sun Dec 08 13:35:14 CET 2002 
 * Description			: A DC Warrior piece
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
 * Represents a Warrior piece on the board.
 *
 * <p>A Warrior can move only on the middle board and has the following moves
 * there : 
 * <ul>
 * <li>move to adjacent square straight ahead
 * <li>capt to adjacent squares diagonally ahead
 * </ul>
 *
 * <p>When a warrior reaches the opposite side of the middle board, it promotes
 * into a hero.
 * 
 * @author Davy Herben
 * @version 021208
 */ 
public class DCWarriorPiece extends DCPiece {

	/*
	 * CONSTRUCTORS
	 *
	 */
	
	/* see DCPiece for comments */
	public DCWarriorPiece(int player, DCGameBoard board) {
		super(player, board);
		type = 'W';
	}
	
	/*
	 * METHODS
	 *
	 */
	
	/* see DCPiece for comments */
	public DCMoveList getValidMoveList(){
	    //empty list
	    list = new DCMoveList();

		//moves for player gold
		if (player == DCConstants.PLAYER_GOLD) {
			//1: move ahead
			processUnblockableTarget(DCConstants.MOVE, 0, 0, 1);
			//1: capt ahead and to sides
			processUnblockableTarget(DCConstants.CAPT, 0, -1, 1);
			processUnblockableTarget(DCConstants.CAPT, 0, 1, 1);
		} else {
			//1: move ahead
			processUnblockableTarget(DCConstants.MOVE, 0, 0, -1);
			//1: capt ahead and to sides
			processUnblockableTarget(DCConstants.CAPT, 0, -1, -1);
			processUnblockableTarget(DCConstants.CAPT, 0, 1, -1);
		}
		return list;
	}


    /**
	 * Returns a promoted version of this piece, which is a Hero
	 * @return DCHeroPiece with same properties as this piece
	 */
	public DCPiece promote() {
		DCPiece pDCPiece = new DCHeroPiece(player, board);
		pDCPiece.setLocation(location);
		pDCPiece.setFrozen(frozen);
		return pDCPiece;
	}
} 

/* END OF FILE */
