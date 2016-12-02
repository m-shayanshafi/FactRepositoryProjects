/*
 * Classname			: DCPiece
 * Author				: TheBlackUnicorn <TheBlackUnicorn@softhome.net>
 * Creation Date		: Wed Oct 16 20:43:54 CEST 2002 
 * Last Updated			: Thu Dec 05 23:11:38 CET 2002 
 * Description			: A DC piece representation
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
 * This class is the superclass for the backend representation of pieces. A
 * DCPiece should only exist as part of a {@link DCGameBoard}, and it is holds
 * a reference to a certain board. It is not allowed to work with a DCPiece
 * outside the boundaries of a DCGameBoard, or to move it between several
 * DCGameBoards.
 *
 * <p>The DCPiece contains the following information : it's colour (represented
 * as the player to which it belongs), its type, its location on it's
 * DCGameBoard, given as a {@link DCCoord}, and whether it is frozen or not.
 * The location can be null if the piece has been captured.
 *
 * <p>A DCPiece knows its own rules. It can calculate which squares it can move
 * to. It knows the locations of the other pieces on the board by its link to
 * its DCGameBoard.
 *
 * <p>This class is abstract. The specific subclasses should be used to create
 * pieces of a certain type.
 * 
 * @author Davy Herben
 * @version 021205
 */ 
abstract public class DCPiece {

	/*
	 * VARIABLES
	 *
	 */
	
	/* CLASS VARIABLES */
	
	/* INSTANCE VARIABLES */ 
	
	/** reference to the piece's DCGameBoard */
	protected DCGameBoard board;
	/** colour of piece, either DCGameBoard.PLAYER_GOLD or
	 * DCGameBoard.PLAYER_SCARLET */
	protected int player;
	/** type of the piece */
	protected char type;
	/** location of the piece on the DCGameBoard */
	protected DCCoord location;
	/** flag indicating if the piece is frozen by a basilisk */
	protected boolean frozen;
	/** list of moves the piece can perform */
	protected DCMoveList list;
	
	/*
	 * CONSTRUCTORS
	 *
	 */
		
	/**
	 * Class constructor. Specifies the player and board to which the piece
	 * belongs.
	 * @param	player	player to which piece belongs
	 * @param	board	DCGameBoard the piece is associated with
	 */
	public DCPiece(int player, DCGameBoard board) {
		this.player = player;
		this.board = board;
	}
	
	/*
	 * METHODS
	 *
	 */

	/**
	 * sets location of the piece on the board. Can be null if the piece has
	 * been captured.
	 * @param location	DCCoord location to put piece on, or null if piece is
	 * not on a board.
	 */
	public void setLocation(DCCoord location) {
		this.location = location;
	}

	/**
	 * gets location of the piece on the board
	 * @return	DCCoord with location of piece
	 */
	public DCCoord getLocation() {
		return location;
	}
	
	/**
	 * returns the number of the player to which the piece belongs
	 * @return DCConstants.PLAYER_GOLD or DCConstants.PLAYER_SCARLET
	 */
	public int getPlayer() {
		return player;
	}

	/**
	 * returns the type of the piece as a character
	 * @return char representing the piece type (first letter of type, or R for
	 * dragon)
	 */
	public char getType() {
		return type;
	}

	/** 
	 * returns whether this DCPiece should promote on it's current location
	 * @return	true if piece should promote
	 */
	public boolean canPromote() {
		return false;
	}
		
	/**
	 * returns a promoted DCPiece of this piece, or this piece itself if it
	 * can't promote. Player number and position are the same as the original
	 * piece. If the piece can't promote, it returns itself.
	 * @return	promoted DCPiece
	 */
	public DCPiece promote() {
		return this;
	}
	
	/**
	 * returns a demoted DCPiece of this piece, or this piece itself if it
	 * can't demote. Player number and position are the same as the original
	 * piece. If the piece can't demote, it returns itself.
	 */
	public DCPiece demote() {
		return this;
	}

	/**
	 * indicates if the piece is frozen or not
	 * @return	boolean true if frozen, false otherwise
	 */
	public boolean isFrozen() {
		return frozen;
	}

	/**
	 * sets frozen state of the piece
	 * @param	frozen		true if piece should be frozen
	 */
	public void setFrozen(boolean frozen) {
		this.frozen = frozen;
	}
	
	/**
	 * Gets a list of all the valid moves this piece can perform. Also fills
	 * the internal list with these values
	 * @return	a DCMoveList containing all the valid moves for the piece
	 */
	public DCMoveList getValidMoveList() {
		return new DCMoveList();
	}

	/**
	 * Returns whether this piece threatens the specified location
	 * @param	location	location to check
	 * @return	true if piece threatens that location
	 */
	public boolean threatens(DCCoord kingLocation) {
		//refresh list;
		getValidMoveList();
	
		try {
			DCMove move = list.getMoveTo(kingLocation);
			return true;
		} catch (DCLocationException e) {
			return false;
		}
	}

	/**
	 * checks if unblockable move/capt/cafar to given offset is valid and adds
	 * it to list of valid moves if this is the case.
	 * @param	moveType	type of move to check for (move, capt, cafar)
	 * @param	bOffset		relative board position of target to piece location
	 * @param	fOffset		relative file  position of target to piece location
	 * @param   rOffset		relative rank  position of target to piece location
	 */
	protected void processUnblockableTarget(int moveType, int bOffset, int fOffset, int rOffset) {
		// establish test target location
		int testBoard = location.getBoard() + bOffset;
		int testFile  = location.getFile()  + fOffset;
		int testRank  = location.getRank()  + rOffset;

		//test if these coordinates are on the board by creating a DCCoord from
		//them. If the coord is not valid it will throw a DCLocationException
		//and we don't have to add it to the list
		try {
			DCCoord targetCoord = new DCCoord(testBoard, testFile, testRank);

			//check if targetCoord is a valid target depending on moveType
			if (moveType == DCConstants.MOVE) {
				//target should be empty
				if (!board.hasPiece(targetCoord)) {
					list.add(new DCMove(type, player, moveType, 
								location, targetCoord));
				}
			} else {
				//target should contain enemy piece
				if (board.hasPiece(targetCoord) && 
						board.getPlayer(targetCoord) != player) {
					list.add(new DCMove(type, player, moveType, 
								location, targetCoord));
				}
			}
		} catch (DCLocationException e) { /* do nothing */ }
	}

	/**
	 * Checks if unblockable move to given absolute position is valid and adds
	 * it to the list of valid moves if this is the casea
	 * @param	bPos		absolute board position of target
	 * @param	fPos		absolute file  position of target
	 * @param   rPos		absolute rank  position of target
	 */
	protected void processAbsoluteMoveTarget(int bPos, int fPos, int rPos) {
		//test if position is valid by creating DCCoord
		try {
			DCCoord targetCoord = new DCCoord(bPos, fPos, rPos);
			if (!board.hasPiece(targetCoord)) {
				list.add(new DCMove(type, player, DCConstants.MOVE,
									location, targetCoord));
			}
		} catch (DCLocationException e) { /* do nothing */ }
	}

	/**
	 * Checks if blockable move/capt/cafar to given offset is valid and adds it
	 * to the list of valid moves if this is the case. For the move to be
	 * valid, the location on the offsets bBlockOffset, fBlockOffset and
	 * rBlockOffset must be empty. The other conditions must also be met.
	 * @param	moveType	type of move to check for (move, capt, cafar)
	 * @param	bOffset		relative board position of target to piece location
	 * @param	fOffset		relative file  position of target to piece location
	 * @param   rOffset		relative rank  position of target to piece location
	 * @param	bBlockOffset	relative board position of blocking location
	 * @param	fBlockOffset	relative file  position of blocking location
	 * @param   rBlockOffset	relative rank  position of blocking location
	 */
	protected void processBlockableTarget(int moveType, int bOffset, int fOffset,
			int rOffset, int bBlockOffset, int fBlockOffset, int rBlockOffset) {
		// establish test target location
		int testBoard = location.getBoard() + bOffset;
		int testFile  = location.getFile()  + fOffset;
		int testRank  = location.getRank()  + rOffset;

		int blockBoard = location.getBoard() + bBlockOffset;
		int blockFile = location.getFile() + fBlockOffset;
		int blockRank = location.getRank() + rBlockOffset;
		

		//test if these coordinates are on the board by creating a DCCoord from
		//them. If the coord is not valid it will throw a DCLocationException
		//and we don't have to add it to the list
		try {
			DCCoord targetCoord = new DCCoord(testBoard, testFile, testRank);
			DCCoord blockingCoord = new DCCoord(blockBoard, blockFile, 
					blockRank);

			//check if targetCoord is a valid target depending on moveType
			if (moveType == DCConstants.MOVE) {
				//target and blocking location should be empty
				if (!board.hasPiece(targetCoord) &&
						!board.hasPiece(blockingCoord)) {
					list.add(new DCMove(type, player, moveType, 
								location, targetCoord));
				}
			} else {
				//target should contain enemy piece
				if (board.hasPiece(targetCoord) && 
						board.getPlayer(targetCoord) != player &&
						!board.hasPiece(blockingCoord)) {
					list.add(new DCMove(type, player, moveType, 
								location, targetCoord));
				}
			}
		} catch (DCLocationException e) { /* do nothing */ }

	}
	
	/**
	 * Takes a directional offset as input and adds all targets on that line
	 * as MOVE or CAPT until it is blocked or the end of the line is reached
	 * @param	bOffset	board direction in which to follow line (-1,0, or 1)
	 * @param	fOffset	file direction in which to follow line (-1,0, or 1)
	 * @param	rOffset	rank direction in which to follow line (-1,0, or 1)
	 */
	protected void processLineOfTargets(int bOffset, int fOffset, int rOffset) {
		//set initial values for test location
		int testBoard = location.getBoard();
		int testFile  = location.getFile();
		int testRank  = location.getRank();

		//traverse this line until we drop off the board or the line is blocked
		//by a piece. Maximum distance a line can travel is the number of files
		//-1. Dropping of the board is registered by a DCLocationException
		//thrown by the DCCoord constructor
		for (int i = 0; i < DCConstants.FILES - 1; i++) {
			testBoard += bOffset;
			testFile  += fOffset;
			testRank  += rOffset;

			try {
				DCCoord targetCoord = new DCCoord(testBoard, testFile, testRank);
				
				//if target is empty add a move, if enemy add capt and stop
				//processing
				if (!board.hasPiece(targetCoord)) {
					list.add(new DCMove(type, player, DCConstants.MOVE,
								location, targetCoord));
				} else {
					if (board.getPlayer(targetCoord) != player) {
						list.add(new DCMove(type, player, DCConstants.CAPT,
								location, targetCoord));
						break;
					} else {
						//friendly piece, stop processing
						break;
					}
				}
			} catch (DCLocationException e) {
				//stop processing this direction
				break;
			}
		}
	}
} 


/* END OF FILE */
