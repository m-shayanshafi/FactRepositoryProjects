/*
 * Classname			: DCCoord
 * Author				: TheBlackUnicorn <TheBlackUnicorn@softhome.net>
 * Creation Date		: Wed Oct 16 21:49:18 CEST 2002 
 * Last Updated			: Wed Oct 16 23:17:31 CEST 2002 
 * Description			: Coordinate on the boards
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
 * This class represents a location on the game board
 */ 
public class DCCoord implements Serializable{

	/*
	 * VARIABLES
	 *
	 */
	
	/* CLASS VARIABLES */
	public static final int 	BOARDS 	= 3;
	public static final int		FILES 	= 12;
	public static final int		RANKS	= 8;

	
	/* INSTANCE VARIABLES */ 
	protected int board;
	protected int file;
	protected int rank;
	
	/* 
	 * INNER CLASSES
	 *
	 */
	
	/*
	 * CONSTRUCTORS
	 *
	 */

	/**
	 * Class constructor, creates DCCoord with specified coordinates.It is
	 * impossible to create a DCCoord that is not within the boundaries of the
	 * board.
	 * @param	board	the board coordinate
	 * @param	file	the file coordinate
	 * @param	rank	the rank coordinate
	 * @exception	DCLocationException	if coords not within range
	 */
	public DCCoord(int board, int file, int rank) throws DCLocationException {

		if (board < 0 || board >= BOARDS || file < 0 || file >= FILES ||
				rank < 0 || rank >= RANKS) {
			throw new DCLocationException("Coordinates not within range");
		}
		
		this.board = board;
		this.file = file;
		this.rank = rank;
	}
	
	/*
	 * METHODS
	 *
	 */

	/**
	 * Returns number of board
	 * @return int with number of board
	 */
	public int getBoard() {
		return board;
	}

	/**
	 * Returns number of file
	 * @return	int with number of file
	 */
	public int getFile() {
		return file;
	}

	/**
	 * Returns number of rank
	 * @return	int with number of rank
	 */
	public int getRank() {
		return rank;
	}

	/**
	 * Returns whether this DCCoord is equal to the specified DCCoord. Returns
	 * true if the Object is a DCCoord and has the same board, file and rank as
	 * this DCCoord
	 * @param	object	the Object to compare to
	 */
	public boolean equals(Object object) {
		if (!(object instanceof DCCoord)) {
			return false;
		} else {
			DCCoord coord = (DCCoord) object;
			if (coord.getBoard() == board 
					&& coord.getFile() == file
					&& coord.getRank() == rank) {
				return true;
			} else {
				return false;
			}
		}
	}

	/**
	 * Returns a string representation of this location on the board. This
	 * means adding 1 to board and rank number, and converting file number to
	 * letters. This produces strings like "3A1", the bottom left corner of the
	 * top board
	 * @return	String representation of the location
	 */
	public String toString() {
		String []fileStringArray = {"A", "B", "C", "D", "E", "F",
								  "G", "H", "I", "J", "K", "L"};
		String returnString = Integer.toString(board + 1);
		returnString += fileStringArray[file];
		returnString += Integer.toString(rank + 1);
		return returnString;
	}
		
} 

/* END OF FILE */
