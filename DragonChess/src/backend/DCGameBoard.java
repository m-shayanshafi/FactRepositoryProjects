/*
 * Classname			: DCGameBoard
 * Author				: TheBlackUnicorn <TheBlackUnicorn@softhome.net>
 * Creation Date		: Tue Jul 16 15:43:04 CEST 2002 
 * Last Updated			: Fri Dec 06 16:19:06 CET 2002 
 * Description			: Bottom class representing physical boards and pieces
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
 * This class represents a set of 3 gameboards and a list to hold the captured
 * pieces. It also contains the pieces themselves, and provides methods to
 * perform actions on these pieces.
 *
 * <p>All pieces are accessed by their location on the boards, as a {@link
 * DCCoord}, never by their DCPiece reference. Because of this, the same
 * actions can be performed on different boards.
 * 
 * @author		Davy Herben
 * @version		021207
 */ 
public class DCGameBoard implements Cloneable {

	/*
	 * VARIABLES
	 *
	 */
	
	/* CLASS VARIABLES */

	private			static final int 	[][][]startPosArray = {
	{ //gold
	{2,0,1},{2,2,1},{2,4,1},{2,6,1},{2,8,1},{2,10,1},{2,2,0},{2,10,0},{2,6,0},
	{1,0,0},{1,11,0},{1,1,0},{1,10,0},{1,2,0},{1,9,0},{1,3,0},{1,8,0},{1,4,0},
	{1,5,0},{1,6,0},{1,7,0},{1,0,1},{1,1,1},{1,2,1},{1,3,1},{1,4,1},{1,5,1},
	{1,6,1},{1,7,1},{1,8,1},{1,9,1},{1,10,1},{1,11,1},{0,2,0},{0,10,0},{0,6,0},
	{0,1,1},{0,3,1},{0,5,1},{0,7,1},{0,9,1},{0,11,1}
	},
	{ //scarlet
	{2,0,6},{2,2,6},{2,4,6},{2,6,6},{2,8,6},{2,10,6},{2,2,7},{2,10,7},{2,6,7},
	{1,0,7},{1,11,7},{1,1,7},{1,10,7},{1,2,7},{1,9,7},{1,3,7},{1,8,7},{1,4,7},
	{1,5,7},{1,6,7},{1,7,7},{1,0,6},{1,1,6},{1,2,6},{1,3,6},{1,4,6},{1,5,6},
	{1,6,6},{1,7,6},{1,8,6},{1,9,6},{1,10,6},{1,11,6},{0,2,7},{0,10,7},{0,6,7},
	{0,1,6},{0,3,6},{0,5,6},{0,7,6},{0,9,6},{0,11,6}
	}};

	
	/* INSTANCE VARIABLES */ 
	//boards
	private DCPiece[][][] board = new DCPiece[DCConstants.BOARDS][DCConstants.FILES][DCConstants.RANKS];
	//pieces
	private	DCPiece[][]	pieceArray = new DCPiece[DCConstants.PLAYERS][DCConstants.NROFDCPIECES];
	//garbage
	private	DCGarbageList garbageList = new DCGarbageList();
	
	/* 
	 * INNER CLASSES
	 *
	 */
	
	/*
	 * CONSTRUCTORS
	 *
	 */
	
	/**
	 * Class constructor. Since the set of pieces has to be recreated every
	 * time a new game is started, it doesn't need to be done here. 
	 */
	public DCGameBoard() {
	}
	
	/*
	 * METHODS
	 */
	
	/* PRIVATE METHODS */
	


	/*    INITIALIZATION METHODS */

	/**
	 * Fills the array of pieces. After this method has been called, the
	 * pieceArray is populated by 2 x 42 pieces, with the type they have at the
	 * beginning of the game.
	 */
	private void createDCPieces() {
		int i = 0;
		//for each player create pieces in correct order
		for (int p = 0; p < DCConstants.PLAYERS; p++) {
			i = 0;
			for (int j=0; j<6; j++) { pieceArray[p][i++] = new DCSylphPiece(p, this); }
			for (int j=0; j<2; j++) { pieceArray[p][i++] = new DCGriffinPiece(p, this); }
			pieceArray[p][i++] = new DCDragonPiece(p, this);
			for (int j=0; j<2; j++) { pieceArray[p][i++] = new DCOliphantPiece(p, this); }
			for (int j=0; j<2; j++) { pieceArray[p][i++] = new DCUnicornPiece(p, this); }
			for (int j=0; j<2; j++) { pieceArray[p][i++] = new DCHeroPiece(p, this); }
			for (int j=0; j<2; j++) { pieceArray[p][i++] = new DCThiefPiece(p, this); }
			pieceArray[p][i++] = new DCClericPiece(p, this);
			pieceArray[p][i++] = new DCMagePiece(p, this);
			pieceArray[p][i++] = new DCKingPiece(p, this);
			pieceArray[p][i++] = new DCPaladinPiece(p, this);
			for (int j=0; j<12; j++) { pieceArray[p][i++] = new DCWarriorPiece(p, this); }
			for (int j=0; j<2; j++) { pieceArray[p][i++] = new DCBasiliskPiece(p, this); }
			pieceArray[p][i++] = new DCElementalPiece(p, this);
			for (int j=0; j<6; j++) { pieceArray[p][i++] = new DCDwarfPiece(p, this); }
		}
	}
	
	/**
	 * Removes all the pieces from the board. This method does not change the
	 * location stored in the individual pieces.
	 */
	private void clearBoard() {
		for (int i = 0; i < DCConstants.BOARDS; i++) {
			for (int j = 0; j < DCConstants.FILES; j++) {
				for (int k = 0; k < DCConstants.RANKS; k++) {
					board[i][j][k] = null;
				}
			}
		}
	}
	
	/**
	 * Puts the pieces on their starting positions. This method assumes that
	 * the board is empty and that there are pieces in the pieceArray
	 */
	private void setStartingDCPieces() {
		//for each player, put the pieces on the board
		DCCoord c;
		DCPiece pDCPiece;
		for (int playerInt = 0; playerInt < DCConstants.PLAYERS; playerInt++) {
			for (int pieceInt = 0; pieceInt < DCConstants.NROFDCPIECES; pieceInt++) {
				try {
					c = new DCCoord(startPosArray[playerInt][pieceInt][0],
									startPosArray[playerInt][pieceInt][1],
									startPosArray[playerInt][pieceInt][2]);
					pDCPiece = getDCPiece(playerInt, pieceInt);
					moveDCPiece(pDCPiece, c);
				} catch (DCLocationException e) { /* can't happen */}
			}
		}
	}
	

	/*    CONVERSION METHODS */
	
	/**
	 * Returns the DCPiece on the specified position in the pieceArray
	 * @param	playerInt	number of the player
	 * @param	pieceInt	sequence number of the piece in the array
	 * @return	the DCPiece on the given position in the pieceArray
	 */
	private DCPiece getDCPiece(int playerInt, int pieceInt) { 
		return pieceArray[playerInt][pieceInt];
	}

	/**
	 * Returns the DCPiece on the specified location on the board
	 * @param	location	location to get piece from
	 * @return	the DCPiece on the given location
	 * @exception DCLocationException if location doesn't contain a piece
	 */
	private DCPiece getDCPiece(DCCoord location) 
			throws DCLocationException {
		DCPiece pDCPiece =
			board[location.getBoard()][location.getFile()][location.getRank()];
		if (pDCPiece == null) {
			throw new DCLocationException("No piece on this location");
		} else {
			return pDCPiece;
		}
	}
	

	
	/*    PIECE MOVEMENT METHODS */
	
	/**
	 * Moves the specified piece to the specified location. This clears the
	 * square the piece is on before the move, if applicable, fills the new
	 * square with a reference to the piece, and sets the internal location
	 * field of the piece.
	 * @param	piece		the DCPiece to move
	 * @param	targetLocation	location to move to
	 */
	private void moveDCPiece(DCPiece piece, DCCoord targetLocation) {
		DCCoord oldLocation = piece.getLocation();
		piece.setLocation(targetLocation);
		if (oldLocation != null) {
			board[oldLocation.getBoard()][oldLocation.getFile()][oldLocation.getRank()] = null;
		}
		board[targetLocation.getBoard()][targetLocation.getFile()][targetLocation.getRank()] = piece;
	}

	/**
	 * Replaces old DCPiece with new DCPiece on the board and in the pieceArray
	 * @param	oldDCPiece		old piece
	 * @param	newDCPiece		new piece
	 */
	private void replaceDCPiece(DCPiece oldDCPiece, DCPiece newDCPiece) {
		//first store target location on board
		DCCoord location = oldDCPiece.getLocation();
		
		//search position of piece in pieceArray
		int playerInt = oldDCPiece.getPlayer();
		int posInt = -1;
		for (int i = 0; i < DCConstants.NROFDCPIECES; i++) {
			if (pieceArray[playerInt][i] == oldDCPiece) {
				posInt = i;
				break;
			}
		}
		
		//replace piece in pieceArray 
		pieceArray[playerInt][posInt] = newDCPiece;
		moveDCPiece(newDCPiece, location);
	}
		
	
	
	/* PUBLIC METHODS */
	

	
	/*    INITIALIZATION METHODS */
	
	/**
	 * resets the board. Makes the pieces, clears the board and puts the pieces
	 * on the right place again.
	 */
	public void resetBoard() {
		clearBoard();
		createDCPieces();
		setStartingDCPieces();
	}


	
	/*    PIECE MOVEMENT METHODS */

	/**
	 * moves a piece on the board. This method requieres that the target
	 * location is empty. 
	 * @param	pieceLocation	location of the piece to move
	 * @param	targetLocation	location to move to
	 */
	public void movePiece(DCCoord pieceLocation, DCCoord targetLocation) 
			throws DCLocationException {
		//get piece. Exception thrown if no piece on location
		DCPiece pDCPiece = getDCPiece(pieceLocation);
		
		//check if targetLocation is empty. We shouldn't move pieces to
		//locations that aren't empty
		if (hasPiece(targetLocation)) {
			throw new DCLocationException("Target location not empty");
		}
		
		//move piece
		moveDCPiece(pDCPiece, targetLocation);
	}

	/**
	 * removes a piece from the board into the garbage list.
	 * @param	pieceLocation	location of the piece to remove
	 * @exception	DCLocationException	if there is no piece on the specified
	 * location
	 */
	public void removePiece(DCCoord pieceLocation) 
			throws DCLocationException {
		//get piece, Exception thrown if no piece on location
		DCPiece pDCPiece = getDCPiece(pieceLocation);

		//remove from board
		board[pieceLocation.getBoard()][pieceLocation.getFile()][pieceLocation.getRank()]
			= null;
		pDCPiece.setLocation(null);
		garbageList.push(pDCPiece);
	}
	
	/**
	 * restores the last piece in the garbagelist to the specified location.
	 * This method requires that the target location is empty
	 * @param 	location		location to restore piece to
	 * @exception	DCListEmptyException	if there is no piece to restore
	 * @exception	DCLocationException		if the target location is not empty
	 */
	public void restorePiece(DCCoord location) 
			throws DCListEmptyException, DCLocationException {
		//get piece, throw exception if list is empty
		DCPiece pDCPiece = garbageList.pop();
		
		//check if targetLocation is empty. We shouldn't move pieces to
		//locations that aren't empty
		if (hasPiece(location)) {
			throw new DCLocationException("Target location not empty");
		}
		
		//move piece
		moveDCPiece(pDCPiece, location);
	}

	

	/*   GENERAL PIECE INFO METHODS */

	/**
	 * Indicates if the given location contains a piece
	 * @param 	location	location to check
	 */
	public boolean hasPiece(DCCoord location) {
		if (board[location.getBoard()][location.getFile()][location.getRank()]
			== null) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Gets the type of the piece on the specified location as a character
	 * @param	location	location of the piece to check
	 * @return	a char representing the type of the piece
	 * @exception	DCLocationException	if the specified location doesn't
	 * contain a piece
	 */
	public char getPieceType(DCCoord location) 
			throws DCLocationException {
		//get piece, throw exception if none found
		DCPiece pDCPiece = getDCPiece(location);

		return pDCPiece.getType();
	}

	/**
	 * Gets the number of the player to which the piece on the specified
	 * location belongs
	 * @param	location	location of the piece to check
	 * @return	int representing the player number
	 * @exception	DCLocationException if the specified location doesn't
	 * contain a piece
	 */
	public int getPlayer(DCCoord location) 
			throws DCLocationException {
		//get piece, throw exception if none found
		DCPiece pDCPiece = getDCPiece(location);

		return pDCPiece.getPlayer();
	}

	/**
	 * Gets the DCExtCoord associated with the specified location if it has a
	 * piece. Throw exception otherwise
	 * @param	location	location to check
	 * @return	DCExtCoord of this location
	 * @exception	DCLocationException if no piece on this location
	 */
	public DCExtCoord getDCExtCoord(DCCoord location) 
			throws DCLocationException {
		return new DCExtCoord(location,
				getPlayer(location),
				getPieceType(location),
				isFrozen(location));
	}
	

	
	/*   PIECE PROMOTION/DEMOTION METHODS */
	
	/**
	 * Indicates if the piece on the given location should promote
	 * @param 	location	location that contains the piece
	 * @return	true if piece can promote on this location
	 * @exception	DCLocationException if location contains no piece.
	 */
	public boolean canPromote(DCCoord location) 
			throws DCLocationException {
		DCPiece pDCPiece = getDCPiece(location);
		return pDCPiece.canPromote();
	}

	/**
	 * Promotes a piece. Replaces the piece on the specified location to its
	 * promoted type
	 * @param	pieceLocation	location of the piece to promote
	 * @exception	DCLocationException if the specified location doesn't
	 * contain a piece
	 */
	public void promotePiece(DCCoord pieceLocation) 
			throws DCLocationException {

		//get piece, throw exception if none found
		DCPiece pDCPiece = getDCPiece(pieceLocation);
		
		//replace piece by promoted version
		replaceDCPiece(pDCPiece, pDCPiece.promote());
	}
		
	/**
	 * Demotes a piece. Replaces the piece on the specified location with its
	 * demoted type
	 * @param	pieceLocation	location of the piece to demote
	 * @exception	DCLocationException	if the specified location doesn't
	 * contain a piece
	 */
	public void demotePiece(DCCoord pieceLocation)
			throws DCLocationException {
				
		//get piece, throw exception if none found
		DCPiece pDCPiece = getDCPiece(pieceLocation);

		//replace piece by demoted version
		replaceDCPiece(pDCPiece, pDCPiece.demote());
	}

	
	
	/*    PIECE FROZEN/UNFROZEN METHODS */
	
	/**
	 * Indicates if the piece on the specified location is frozen
	 * @param	location	location of the piece
	 * @exception	DCLocationException if the specified location doesn't
	 * contain a piece
	 */
	public boolean isFrozen(DCCoord location) 
			throws DCLocationException {
		//get piece, throw exception if none found
		DCPiece pDCPiece = getDCPiece(location);

		return pDCPiece.isFrozen();
	}

	/** Sets the frozen status of the piece on the specified location
	 * @param	location	location with piece to act on
	 * @param	frozen		true is piece should be frozen
	 * @exception	DCLocationException	if the specified location doesn't
	 * contain a piece
	 */
	public void setFrozen(DCCoord location, boolean frozen) 
			throws DCLocationException {
		//get piece, throw exception if none found
		DCPiece pDCPiece = getDCPiece(location);

		pDCPiece.setFrozen(frozen);
	}

	
	
	/* VALID MOVES METHODS */
	
	/**
	 * Gets a list of all the valid moves that the piece on the specified
	 * location can perform.
	 * @param	location	location of the piece to check
	 * @return	DCMoveList containing all the valid moves for the piece
	 * @exception	DCLocationException if the specified location doesn't
	 * contain a piece
	 */
	public DCMoveList getValidMoveList(DCCoord location) 
			throws DCLocationException {
		//get piece, throw exception if none found
		DCPiece pDCPiece = getDCPiece(location);

		return pDCPiece.getValidMoveList();
	}
	
	/**
	 * Returns a list of all valid moves of all pieces of the specified player
	 */
	public DCMoveList getAllValidMovesFor(int player) {

		DCMoveList allMoveList = new DCMoveList();
		for (int i = 0; i < DCConstants.NROFDCPIECES; i++) {
			DCPiece pDCPiece = getDCPiece(player, i);
			if (pDCPiece.getLocation() != null) {
				allMoveList.add(pDCPiece.getValidMoveList());
			}
		}

		return allMoveList;
	}

	/**
	 * Returns whether the king of the specified player is threatened by any
	 * piece from the enemy player.
	 * @param	player	number of the player for which to check king
	 * @return	true if King is threatened
	 */
	public boolean kingThreatened(int player) {
		//find king
		DCPiece king = null;
		for (int index = 0; index < DCConstants.NROFDCPIECES; index++) {
			if (pieceArray[player][index].getType() == 'K') {
				king = pieceArray[player][index];
				break;
			}
		}
		DCCoord kingLocation = king.getLocation();
		int otherPlayer = (player + 1) % 2;

		//go through list of all enemy pieces and check if they threaten this
		//location
		DCPiece enemyPiece;
		for (int i = 0; i < DCConstants.NROFDCPIECES; i++) {
			enemyPiece = getDCPiece(otherPlayer, i);
			//if location is not null, check if piece threatens the king
			if (enemyPiece.getLocation() != null) {
				if (enemyPiece.threatens(kingLocation)) {
					return true;
				}
			}
		}
		return false;
	}
	
	

	/*    BACKUP / RESTORE / COPY METHODS */
	
	/**
	 * Clones this DCGameBoard. This method creates a new instance of a
	 * DCGameBoard, with exactly the same environment. The positions of the
	 * pieces on the boards and in the DCGarbageList will be the same, as will
	 * the status of the pieces (eg frozen).
	 * @return	a copy of the DCGameBoard
	 */
	public Object clone() {
		/* This method does not actually clone this DCGameBoard. Instead, it
		 * creates a new instance of a DCGameBoard, gets a dump from the
		 * current DCGameBoard and loads the dump into the new board. 
		 * This produces the same result and works for now. Eventually, a
		 * better clone() should be implemented, mainly for performance reasons
		 */
		
		DCGameBoard clonedBoard = new DCGameBoard();

		clonedBoard.restoreDump(this.getPieceDump(), this.getGarbageDump());
		return (Object) clonedBoard;
	}

	/**
	 * Returns a DCFrontEndDump with the pieces on the board and in the
	 * garbageList. This dump can be used only to display the pieces in the
	 * frontends, it doesn't contain enough information to rebuild a new
	 * DCGameBoard from it.
	 * return	DCFrontEndDump	with state of the board
	 */
	public DCFrontEndDump getDCFrontEndDump() {
		return new DCFrontEndDump(getPieceDump(), getGarbageDump());
	}
	
	/**
	 * Returns a list representing the current position of all the pieces.
	 * Pieces that are in the garbage list are included here with location
	 * null.
	 * @return	[][]array of DCExtCoord
	 */
	public DCExtCoord[][] getPieceDump() {
		DCExtCoord [][]returnArray = new DCExtCoord[DCConstants.PLAYERS][DCConstants.NROFDCPIECES];
		for (int player=0; player < DCConstants.PLAYERS; player++) {
			for (int piece=0; piece < DCConstants.NROFDCPIECES; piece++) {
				DCPiece pDCPiece = getDCPiece(player, piece);
				returnArray[player][piece] = new DCExtCoord(
						pDCPiece.getLocation(),
						player,
						pDCPiece.getType(),
						pDCPiece.isFrozen());
			}
		}
		return returnArray;				
	}

	/**
	 * Returns an array representing which pieces of the pieceArray are in the
	 * garbageList. 
	 * @return	[][] of ints
	 */
	public int[][] getGarbageDump() {
		int arraySize = garbageList.size();
		int player=0;
		int position=0;
		int [][]returnArray = new int[arraySize][2];
		
		for (int i = 0; i < arraySize; i++ ) {
			DCPiece pDCPiece = garbageList.get(i);
			player = pDCPiece.getPlayer();
			
			//find position of the piece in the pieceArray
			for (int j = 0; j < DCConstants.NROFDCPIECES; j++) {
				if (pieceArray[player][j] == pDCPiece) {
					position = j;
					break;
				}
			}
			
			returnArray[i][0] = player;
			returnArray[i][1] = position;
		}

		return returnArray;
	}
	
	/**
	 * Loads a piece dump and a garbage dump and restores the game to the
	 * situation specified there
	 * @param	pieceDump	DCExtCoordList with location of pieces
	 * @param	garbageDump	DCExtCoordList with order of garbage bin
	 */
	public void restoreDump(DCExtCoord [][]pieceDump, int [][]garbageDump) {
		
		char pieceType;
		DCCoord location = null;
		DCExtCoord coord = null;
		DCPiece newDCPiece = null;
		
		//first reset the playing board
		clearBoard();

		//fill the pieceArray
		for (int player = 0; player < DCConstants.PLAYERS; player++) {
			for (int piece = 0; piece < DCConstants.NROFDCPIECES; piece++) {
				coord = pieceDump[player][piece];
				switch (coord.getPieceType()) {
					case 'B' : newDCPiece = new DCBasiliskPiece(player, this); break;
					case 'C' : newDCPiece = new DCClericPiece(player, this);   break;
					case 'R' : newDCPiece = new DCDragonPiece(player, this);   break;
					case 'D' : newDCPiece = new DCDwarfPiece(player, this);    break;
					case 'E' : newDCPiece = new DCElementalPiece(player, this);break;
					case 'G' : newDCPiece = new DCGriffinPiece(player, this);  break;
					case 'H' : newDCPiece = new DCHeroPiece(player, this);     break;
					case 'K' : newDCPiece = new DCKingPiece(player, this);     break;
					case 'M' : newDCPiece = new DCMagePiece(player, this);     break;
					case 'O' : newDCPiece = new DCOliphantPiece(player, this); break;
					case 'P' : newDCPiece = new DCPaladinPiece(player, this);  break;
					case 'S' : newDCPiece = new DCSylphPiece(player, this);    break;
					case 'T' : newDCPiece = new DCThiefPiece(player, this);    break;
					case 'U' : newDCPiece = new DCUnicornPiece(player, this);  break;
					case 'W' : newDCPiece = new DCWarriorPiece(player, this);  break;
				}
				if (coord.getLocation() != null) {
					moveDCPiece(newDCPiece, coord.getLocation());
				}
				pieceArray[player][piece] = newDCPiece;
			}
		}

		//empty and fill the garbageList
		garbageList = new DCGarbageList();
		for (int i = 0; i < garbageDump.length; i++) {
			garbageList.push(getDCPiece(garbageDump[i][0], garbageDump[i][1]));
		}
		
	}
}

/* END OF FILE */
