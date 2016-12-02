/*
 * Classname			: DCMove
 * Author				: TheBlackUnicorn <TheBlackUnicorn@softhome.net>
 * Creation Date		: Fri Oct 25 13:07:42 CEST 2002 
 * Last Updated			: Fri Oct 25 13:07:44 CEST 2002 
 * Description			: Move class
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

package main;

/* package import */
import java.lang.*;
import java.io.Serializable;

/**
 * Class representing a move. This class contains a move of a certain type
 * (MOVE, CAPTure ot Capture from AFAR), the player that performs this move,
 * and the type of piece that performs this move.
 * @author	Davy Herben
 * @author  Christophe Hertigers
 */ 
public class DCMove implements Serializable {

	/*
	 * VARIABLES
	 *
	 */
		
	/* CLASS VARIABLES */
	
	/* INSTANCE VARIABLES */ 
	DCCoord	source;
	DCCoord target;
	int player;
	int moveType;
	char pieceType;
	boolean promoting = false;
	boolean check = false;
	
	/* 
	 * INNER CLASSES
	 *
	 */
	
	/*
	 * CONSTRUCTORS
	 *
	 */

	/**
	 * Class constructor. Creates a move for a specified piece of a specified
	 * player of a specified type, from source to target
	 * @param 	pieceType	char representing type of piece
	 * @param	player		player to which the moving piece belongs
	 * @param	moveType	type of move (DCConstants.MOVE, DCConstants.CAPT or
	 * DCConstants.CAFAR)
	 * @param	source		location of the piece to move
	 * @param	target		if MOVE or CAPT : location to move to, if CAFAR,
	 * location to capture piece on
	 */
	public DCMove(char pieceType, int player, int moveType, DCCoord source,
			DCCoord target) {
		this.pieceType = pieceType;
		this.player = player;
		this.moveType = moveType;
		this.source = source;
		this.target = target;
	}

	/*
	 * METHODS
	 *
	 */

	/**
	 * Gets the type of the moving piece.
	 * @return	char representing piece type
	 */
	public char getPieceType() {
		return pieceType;
	}

	/**
	 * Gets the player that can perform this move
	 * @return	int with player
	 */
	public int	getPlayer() {
		return player;
	}

	/**
	 * Gets the type of move
	 * @return	int with type of move
	 */
	public int getMoveType() {
		return moveType;
	}

	/**
	 * Gets the source of the move
	 * @return DCCoord with source location
	 */
	public DCCoord getSource() {
		return source;
	}

	/**
	 * Returns the target of the move
	 * @return DCCoord with target location
	 */
	public DCCoord getTarget() {
		return target;
	}

    /**
     * Returns a string representation of this move, according to the
     * dragonchess rules :
     * <ul>
     * <li>MOVE : piece/startfield-endfield (eg S/3E2-3D3)
     * <li>CAPT : piece/startfieldxTargetfield (eg W/2D2x2E3)
     * <li>CAFAR : RxTargetField (eg Rx2C4)
     * <li>Promotion : add (H) to end (eg W/2G7-2G8)
     * <li>Check     : adds ch to end (eg W/2G7-2G8ch)
     * </ul>
     *
     * @return      a String representation of the move
     */
    public String toString() {
        String sString = new String();

        //type info toevoegen
        sString += getPieceType();
        sString += "/";
        
		sString += getSource().toString();

		switch(getMoveType()) {
		case DCConstants.MOVE:
            sString += "-";
			break;
		case DCConstants.CAPT:
		case DCConstants.CAFAR:
            sString += "x";
		}

		sString += getTarget().toString();
        
		if (promoting) {             //type:: W/2G8-2G8(H)
            sString += "(H)";
        }
        if (check) {
            sString += "ch";
        }

        return sString;
    }

	/**
	 * Indicates if the piece that performs this move is promoting. This
	 * information should be used exclusively by the backend. Messages deliver
	 * this information in the frontend.
	 * @param promoting		boolean indicating whether or not to promote
	 */
	public void setPromote(boolean promoting) {
		this.promoting = promoting;
	}

	/**
	 * Returns if the moving piece is promoting after this move
	 * @return	boolean true if piece promoted, false otherwise
	 */
	public boolean isPromoting() {
		return promoting;
	}

	/**
	 * Sets whether this move puts the opposing king in check. This should be
	 * used only to determine the string representation of the move
	 * @param check	boolean true if puts other king in check
	 */
	public void setCheck(boolean check) {
		this.check = check;
	}

	/**
	 * Returns if the move puts the opposing king in check
	 * @return true if opposing king is in check after this move
	 */
	public boolean isCheck() {
		return check;
	}
} 

/* END OF FILE */
