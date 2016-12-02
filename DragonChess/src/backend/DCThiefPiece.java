/*
 * Classname			: DCThiefPiece
 * Author				: knys <koen.nys@pandora.be>
 * Creation Date		: Wed Oct 16 20:43:54 CEST 2002 
 * Last Updated			: Sun Dec 08 13:30:52 CET 2002 
 * Description			: A DC Thief piece
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
 * Represents a Thief piece on the board.
 *
 * <p>A Thief piece can only move on the middle board and has the following
 * moves there :
 * <ul>
 * <li>move or capt to squares in a diagonal line from the piece, as long as
 * there are no intervening pieces
 * </ul>
 * 
 * @author  Koen Nys
 * @author 	Davy Herben
 * @version 021208
 */ 
public class DCThiefPiece extends DCPiece {

	/*
	 * CONSTRUCTORS
	 *
	 */

	/* see DCPiece for comments */
	public DCThiefPiece(int player, DCGameBoard board) {
		super(player, board);
		type = 'T';
	}
	
	/*
	 * METHODS
	 *
	 */

	/* see DCPiece for comments */
	public DCMoveList getValidMoveList() {
		//empty list
		list = new DCMoveList();
		
		//1: moves/captures
		processLineOfTargets(0, 1, 1);
		processLineOfTargets(0, -1, -1);
		processLineOfTargets(0, -1, 1);
		processLineOfTargets(0, 1, -1);
		    
		return list;
	}
} 

/* END OF FILE */
