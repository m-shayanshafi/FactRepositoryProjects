/*
 * Classname			: DCExtCoord
 * Author				: TheBlackUnicorn <TheBlackUnicorn@softhome.net>
 * Creation Date		: Sat Oct 26 18:02:13 CEST 2002 
 * Last Updated			: Sat Oct 26 18:02:16 CEST 2002 
 * Description			: 
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
import java.io.*;

/**
 * This class represents a field, with info about the location of the field and
 * the piece on it. DCExtCoords should not be used to represent empty
 * locations, only filled ones.
 * @author	Davy Herben
 * @version 021026 
 */ 
public class DCExtCoord implements Serializable {

	/*
	 * VARIABLES
	 *
	 */
	
	/* CLASS VARIABLES */
	
	/* INSTANCE VARIABLES */ 
	DCCoord location;
	int		player;
	char	pieceType;
	boolean frozen;
	
	/* 
	 * INNER CLASSES
	 *
	 */
	
	/*
	 * CONSTRUCTORS
	 *
	 */

	/**
	 * Class constructor. Creates a Coord with given characteristics
	 * @param	location	the location
	 * @param	player		player who owns the piece
	 * @param	pieceType	type of piece
	 * @param	frozen		whether or not picee is frozen
	 */
	public DCExtCoord(DCCoord location, int player, char pieceType, 
			boolean frozen) {
		this.location = location;
		this.player = player;
		this.pieceType = pieceType;
		this.frozen = frozen;
	}
	
	/**
	 * Class constructor. Creates a Coord with given characteristics. It is
	 * impossible to create a DCExtCoord that is not on the board.
	 * @param	board		nr of board
	 * @param	file		file of piece
	 * @param	rank		rank of piece
	 * @param	player		player who owns the piece
	 * @param	pieceType	type of piece
	 * @param	frozen		whether or not piece is frozen
	 * @exception	DCLocationException	if coords not within range
	 */
	public DCExtCoord(int board, int file, int rank, int player,
			char pieceType, boolean frozen) throws DCLocationException {
		this.location = new DCCoord (board, file, rank);
		this.player = player;
		this.pieceType = pieceType;
		this.frozen = frozen;
	}

	/*
	 * METHODS
	 *
	 */

	/**
	 * Returns location of the piece
	 * @return	DCCoord with location of piece
	 */
	public DCCoord getLocation() {
		return location;
	}

	/**
	 * Returns board of piece
	 * @return	int with number of board
	 */
	public int getBoard() {
		return location.getBoard();
	}

	/**
	 * Returnss file of piece
	 * @return	int with file of piece
	 */
	public int getFile() {
		return location.getFile();
	}

	/**
	 * Returns rank of piece
	 * @return	int with rank of piece
	 */
	public int getRank() {
		return location.getRank();
	}

	/**
	 * Returns player who owns the piece
	 * @return int with player number
	 */
	public int getPlayer() {
		return player;
	}

	/**
	 * Returns type of piece
	 * @return	char with type of piece
	 */
	public char getPieceType() {
		return pieceType;
	}

	/**
	 * Returns whether piece is frozen
	 * @return	true if piece is frozen
	 */
	public boolean isFrozen() {
		return frozen;
	}

	/**
	 * Returns a string representation of this ExtCoord
	 */
	public String toString() {
		String s = new String();
		s = Character.toString(pieceType);
		s += location.toString();

		return s;
	}
} 

/* END OF FILE */
