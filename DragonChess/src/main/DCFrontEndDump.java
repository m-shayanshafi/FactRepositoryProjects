/*
 * Classname			: DCDump
 * Author				: TheBlackUnicorn <TheBlackUnicorn@softhome.net>
 * Creation Date		: Wed Nov 13 07:35:42 CET 2002 
 * Last Updated			: Wed Nov 13 07:35:47 CET 2002 
 * Description			: Class containing backend dump
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
 *  This class stores the position of pieces on the board and in the
 *  garbage list. It does NOT contain the history list so it can't be used to
 *  reconstruct an entire backend.
 */ 
public class DCFrontEndDump implements Serializable {

	/*
	 * VARIABLES
	 *
	 */
	
	/* CLASS VARIABLES */
	
	/* INSTANCE VARIABLES */ 
		
	/* 
	 * INNER CLASSES
	 *
	 */
	
	DCExtCoordList boardList = new DCExtCoordList();
	DCExtCoordList garbageList = new DCExtCoordList();
	
	/*
	 * CONSTRUCTORS
	 *
	 */
	
	/**
	 * Class constructor.
	 * @param	pieceDump	array of DCExtCoords with position of the pieces
	 * @param	garbageDump	array of int with pieces in garbage
	 * @param	gameState	int with gameState
	 * @param	player		int with active player
	 */
	public DCFrontEndDump(DCExtCoord [][]pieceDump, int [][]garbageDump) {
		
		//create lists of DCExtCoords from the dumps
		for (int i = 0; i < DCConstants.PLAYERS; i++) {
			for (int j = 0; j < DCConstants.NROFDCPIECES; j++) {
				if (pieceDump[i][j].getLocation() != null) {
					boardList.add(pieceDump[i][j]);
				}
			}
		}
		
		for (int i =0; i < garbageDump.length; i++ ) {
			garbageList.add(pieceDump[garbageDump[i][0]][garbageDump[i][1]]);
		}
	}
	
	/*
	 * METHODS
	 *
	 */

	/**
	 * Gets the list of pieces to put on the board
	 * @return 	DCExtCoordList	with the pieces that are on the board
	 */
	public DCExtCoordList getBoardPieces() {
		return boardList;
	}

	/**
	 * Gets the list of pieces that are in the garbage list
	 * @return	DCExtCoordList	with the pieces that are in the garbageList
	 */
	public DCExtCoordList getGarbagePieces() {
		return garbageList;
	}

	/**
	 * Prints a text representation of all the pieces in the dump to standard
	 * output. To be used for debugging only.
	 */
	public void debugPrint() {
		System.out.println("**** BOARDPIECES : " + boardList.size());
		int j = 0;
		for (int i = 0; i < boardList.size(); i++) {
			System.out.print((boardList.get(i)).toString() + "\t");
			j++;
			if (j > 7) {
				System.out.println();
				j = 0;
			}
		}
		
		j = 0;
		System.out.println("\n**** GARBAGEPIECES : " + garbageList.size());
		for (int i = 0; i < garbageList.size(); i++) {
			System.out.print((garbageList.get(i)).toString() + "\t");
			j++;
			if (j > 7) {
				System.out.println();
				j = 0;
			}
		}
	}
} 

/* END OF FILE */
