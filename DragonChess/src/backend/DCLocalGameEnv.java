/*
 * Classname			: DCLocalGameEnv
 * Author				: TheBlackUnicorn <TheBlackUnicorn@softhome.net>
 * Creation Date		: Sat Oct 19 13:20:01 CEST 2002 
 * Last Updated			: Fri Dec 13 20:50:15 CET 2002 
 * Description			: The dragonchess backend
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
import connectivity.DCMessage;
import connectivity.DCGame;

/**
 * This class is the backend of the dragonchess game. It takes care of piece
 * selection, moving pieces, keeping history lists, controlling gamestate.
 * Controlling the class from a frontend should be done using DCMessages, but
 * it is also possible to perform the operations through method calls (Only the
 * DCBackEndDecoder should do this).
 * @author	Davy Herben
 * @version 021207 
 */ 
public class DCLocalGameEnv extends DCGameEnv {

	/*
	 * VARIABLES
	 *
	 */
	
	/* INSTANCE VARIABLES */ 

	// game variables
	private DCGameBoard board;
	private int gameState;
	private DCCoord selectedLocation;
	private DCMoveList validMoveList;
	private DCMoveList history;
	
	// connectivity variables
	private DCBackEndEncoder encoder;
	private DCBackEndDecoder decoder;
	private DCPlayer []playerArray = new DCPlayer[2];
	private int activePlayer = DCConstants.PLAYER_NONE;
	
	/*
	 * CONSTRUCTORS
	 *
	 */

	/**
	 * Class constructor. Creates a new backend in initial state/
	 * @param 	msgCarrier	The class that will take outgoing DCMessages
	 */
	public DCLocalGameEnv(DCGame msgCarrier) {
		
		/* set gamestate */
		setGameState(DCConstants.INITIALISING);
		
		/* Setup gameboard */
		board = new DCGameBoard();
	
		/* setup player array */
		playerArray[0] = new DCPlayer();
		playerArray[1] = new DCPlayer();
		
		/* Setup messaging system */
		this.msgCarrier = msgCarrier;
		encoder = new DCBackEndEncoder();
		decoder = new DCBackEndDecoder(this);
	}
		
	/*
	 * METHODS
	 *
	 */

	/* PRIVATE METHODS */
	
	/**
	 * Sends a message to the message carrier. Takes an already encoded
	 * message.
	 * @param	message		message to send out
	 */
	private void sendOut(DCMessage message) {
		//hand over to carrier
		msgCarrier.sendMessage(message);
	}
	
	/**
	 * Sets the game state. Also sends message to all that game state has
	 * changed.
	 * @param	state	int with game state (defined in DCConstants)
	 */
	private void setGameState(int state) {
		gameState = state;
		//send message if we have a carrier and an encoder
		if (encoder != null && msgCarrier != null) {
			sendOut(encoder.setGameState(state));
		}
	}
	
	/**
	 * Sets the active player. 
	 * @param	player		to set as active
	 */
	private void setActivePlayer(int player) {
		activePlayer = player;
		sendOut(encoder.setActivePlayer(player));
	}

	/**
	 * Sets the selected piece.
	 * @param	location	DCCoord with location of piece to select
	 */
	private void setSelectedLocation(DCCoord location) {
		selectedLocation = location;
		sendOut(encoder.pieceSelected(activePlayer, location));
	}

	/**
	 * Unset the selected piece.
	 */
	private void unsetSelectedLocation() {
		DCCoord tempLocation = selectedLocation;
		DCMoveList tempList = validMoveList;
		validMoveList = null;
		selectedLocation = null;
		sendOut(encoder.pieceDeselected(activePlayer, tempLocation, 
					tempList));
	}
	
	/**
	 * Tests if a proposed move would put the player's king in check. It does
	 * this by performing the move on a cloned DCGameBoard and checking if the
	 * king is in check after that.
	 * @param	move	the proposed move
	 * @return	true if the king would be in check, false otherwise
	 */
	public boolean putsKingInCheck(DCMove move) {
		try {
			int player = move.getPlayer();
			DCGameBoard gameBoard = (DCGameBoard) board.clone();
		
			performMove(gameBoard, move);
			if (gameBoard.kingThreatened(player)) {
				return true;
			} else {
				return false;
			}
		} catch (DCLocationException e) {
			//This should never happen
			System.err.println("Error : DCLocationException in putsKingInCheck()");
			return false;
		}
	}
	
	/**
	 * Performs the move. Takes care of the moving of pieces, including
	 * freezing and promoting. Takes an argument to specify on which board to
	 * move the pieces
	 * @param	gameBoard	the board on which to perform the move
	 * @param	move	the Move to perform
	 * @exception	DCLocationException if move can't be performed because of
	 * invalid location
	 */
	private void performMove(DCGameBoard gameBoard, DCMove move) 
			throws DCLocationException {
		char pieceType = move.getPieceType();
		int moveType = move.getMoveType();
		int player = move.getPlayer();
		DCCoord target = move.getTarget();	
		DCCoord source = move.getSource();
	
		//unfreeze piece if moved piece is basilisk
		if (pieceType == 'B') {
			//coordinate to unfreeze is on middle board directly
			//above basilisk
			DCCoord unfreezeLocation = new DCCoord(
					DCConstants.BOARD_MIDDLE,
					source.getFile(),
					source.getRank());
			if (gameBoard.hasPiece(unfreezeLocation) && 
					gameBoard.isFrozen(unfreezeLocation)) {
				gameBoard.setFrozen(unfreezeLocation, false);
				if (gameBoard == board) {
					sendOut(encoder.setFreezeStatus(unfreezeLocation, false));
				}
			}
		}
					
		//unfreeze piece if captured piece is basilisk
		if (moveType != DCConstants.MOVE) {
			if (gameBoard.getPieceType(target) == 'B') {
				DCCoord unfreezeLocation = new DCCoord(
						DCConstants.BOARD_MIDDLE,
						target.getFile(),
						target.getRank());
				if (gameBoard.hasPiece(unfreezeLocation) &&
					gameBoard.isFrozen(unfreezeLocation)) {
					gameBoard.setFrozen(unfreezeLocation, false);
					if (gameBoard == board) {
						sendOut(encoder.setFreezeStatus(unfreezeLocation, false));
					}
				}
			}
		}
				
		//carry out move
		DCCoord newLocation = moveDCPiece(gameBoard, move);
		
		//promote piece if allowed to
		if (gameBoard.canPromote(newLocation)) {
			gameBoard.promotePiece(newLocation);
			//set promotion info in the DCMove
			move.setPromote(true);
			if (gameBoard == board) {
				DCExtCoord promo = gameBoard.getDCExtCoord(newLocation);
				sendOut(encoder.piecePromoted(promo));
			}
		} else {
			//set promotion information in the DCMove
			move.setPromote(false);
		}
		
		//freeze pieces if moved piece is basilisk
		if (pieceType == 'B') {
			DCCoord freezeLocation = new DCCoord(
					DCConstants.BOARD_MIDDLE,
					newLocation.getFile(),
					newLocation.getRank());
			if (gameBoard.hasPiece(freezeLocation) &&
					gameBoard.getPlayer(freezeLocation) != player) {
				gameBoard.setFrozen(freezeLocation, true);
				if (gameBoard == board) {
					sendOut(encoder.setFreezeStatus(freezeLocation, true));
				}
			}
		}

		//freeze piece if moved on top of enemy basilisk
		if (newLocation.getBoard() == DCConstants.BOARD_MIDDLE) {
			DCCoord basiliskLocation = new DCCoord(
					DCConstants.BOARD_BOTTOM,
					newLocation.getFile(),
					newLocation.getRank());
			if (gameBoard.hasPiece(basiliskLocation)) {
				if (gameBoard.getPieceType(basiliskLocation) == 'B' 
					&& gameBoard.getPlayer(basiliskLocation) != player) {
					gameBoard.setFrozen(newLocation, true);
					if (gameBoard == board) {
						sendOut(encoder.setFreezeStatus(newLocation, true));
					}
				}
			}
		}
	}
	
	/**
	 * Undoes a move. This function takes care of restoring position, promotion
	 * and freezing status to the way they were before the move
	 * @param	gameBoard	DCGameBoard on which to undo the move
	 * @param	move	DCMove to undo
	 * @exception DCLocationException
	 */
	private void undoPerformMove(DCGameBoard gameBoard, DCMove move) 
			throws DCLocationException {
		
		DCCoord location;
		DCCoord source = move.getSource();
		DCCoord target = move.getTarget();
		char pieceType = move.getPieceType();
		int player = move.getPlayer();
		
		//find current location of moved piece
		if (move.getMoveType() == DCConstants.CAFAR) {
			location = source;
		} else { 
			location = target;
		}

		//unfreeze piece if it's frozen
		if (gameBoard.isFrozen(location)) {
			gameBoard.setFrozen(location, false);
			if (gameBoard == board) {
				sendOut(encoder.setFreezeStatus(location, false));
			}
		}

		//if basilisk, unfreeze enemy piece above
		if (pieceType == 'B') {
			DCCoord unfreezeLocation = new DCCoord(
					DCConstants.BOARD_MIDDLE,
					location.getFile(),
					location.getRank());
			if (gameBoard.hasPiece(unfreezeLocation) &&
					gameBoard.isFrozen(unfreezeLocation)) {
				gameBoard.setFrozen(unfreezeLocation, false);
				if (gameBoard == board) {
					sendOut(encoder.setFreezeStatus(unfreezeLocation, false));
				}
			}
		}

		//demote piece if necessary
		if (move.isPromoting()) {
			gameBoard.demotePiece(location);
			if (gameBoard == board) {
				DCExtCoord demote = gameBoard.getDCExtCoord(location);
				sendOut(encoder.pieceDemoted(demote));
			}
		}

		//carry out move undoing
		DCCoord newLocation = undoMoveDCPiece(gameBoard, move);
			
		//freeze piece if above enemy basilisk
		if (newLocation.getBoard() == DCConstants.BOARD_MIDDLE) {
			DCCoord basiliskLocation = new DCCoord(
					DCConstants.BOARD_BOTTOM,
					newLocation.getFile(),
					newLocation.getRank());
			if (gameBoard.hasPiece(basiliskLocation)) {
				if (gameBoard.getPieceType(basiliskLocation) == 'B'
					&& gameBoard.getPlayer(basiliskLocation) != player) {
					gameBoard.setFrozen(newLocation, true);
					if (gameBoard == board) {
						sendOut(encoder.setFreezeStatus(newLocation, true));
					}
				}
			}
		}

		//freeze pieces if moved piece is basilisk
		if (pieceType == 'B') {
			DCCoord freezeLocation = new DCCoord(
					DCConstants.BOARD_MIDDLE,
					newLocation.getFile(),
					newLocation.getRank());
			if (gameBoard.hasPiece(freezeLocation) &&
					gameBoard.getPlayer(freezeLocation) != player) {
				gameBoard.setFrozen(freezeLocation, true);
				if (gameBoard == board) {
					sendOut(encoder.setFreezeStatus(freezeLocation, true));
				}
			}
		}
	}
	
	/**
	 * Moves the physical DCPieces on the given gameBoard. This checks which
	 * type of move is requested and removes captured pieces from gameBoard if
	 * necessary, before moving the actual piece on the gameBoard. Returns the
	 * new position of the moved piece.
	 * @param 	gameBoard	the board on which to perform the move
	 * @param	move	the move to perform
	 * @return	DCCoord with new position of moved piece
	 * @exception	DCLocationException if move can't be performed
	 */
	private DCCoord moveDCPiece(DCGameBoard gameBoard, DCMove move) 
			throws DCLocationException {
		int moveType = move.getMoveType();
		DCCoord target = move.getTarget();
		DCCoord source = move.getSource();
		
		//remove captured piece 
		if (moveType != DCConstants.MOVE) {
			gameBoard.removePiece(target);
		}
		//move piece
		if (moveType != DCConstants.CAFAR) {
			gameBoard.movePiece(source, target);
		}
		
		//send out message only if gameBoard = board
		if (gameBoard == this.board) {
				sendOut(encoder.pieceMoved(move));
		}
		
		//return new position of moved piece
		if (moveType != DCConstants.CAFAR) {
			return target;
		} else {
			return source;
		}
	}

	/**
	 * Undoes the moving of the physical DCPieces on the gameBoard. This checks
	 * which type of move needs to be undone and restores a piece from the
	 * garbageList if necessary
	 * @param	gameBoard	gameBoard on which to work
	 * @param	move	DCMove to undo
	 * @return	DCCoord with location of moved piece after undo
	 * @exception DCLocationException if something goes wrong on gameBoard
	 */
	private DCCoord undoMoveDCPiece(DCGameBoard gameBoard, DCMove move) 
			throws DCLocationException {
		int moveType = move.getMoveType();
		DCCoord target = move.getTarget();
		DCCoord source = move.getSource();

		//move piece back to original location
		if (moveType != DCConstants.CAFAR) {
			gameBoard.movePiece(target, source);
		}

		//send message only if gameboard = board
		//also send message in case of CAFAR
		if (gameBoard == board) {
			sendOut(encoder.moveUndone(move));
		}
		

		//restore captured piece
		if (moveType != DCConstants.MOVE) {
			try {
				gameBoard.restorePiece(target);
			} catch (DCListEmptyException e) {
				System.err.println("ERROR : DCListEmptyException in undoMoveDCPiece.");
				System.err.println("        This should not happen. Please file bug report");
				System.exit(-1);
			}
			//send message only if gameBoard = board
			if (gameBoard == board) {
				DCExtCoord restored = gameBoard.getDCExtCoord(target);
				sendOut(encoder.pieceRestored(restored));
			}
		}
		
		//return new location of moved piece
			return source;
	
	}
	
	/**
	 * Checks if the specified player is mate. Mate is defined as being unable
	 * to perform any move that doesn't result in his own king being in check
	 */
	private boolean isMate(int player) {
		// Get the valid moves for all pieces of this player
		DCMoveList list = board.getAllValidMovesFor(player);
		if (list == null) { 
			return true; 
		}
		
		DCGameBoard newBoard = (DCGameBoard) board.clone();
		
		//for all moves, check if they put king in check
		for (int i = 0; i < list.size(); i++) {
			//trying to optimize this algorithm by not cloning the DCGameBoard
			//over and over like the putsKingInCheck does
			
			DCMove move = list.get(i);

			try {
				performMove(newBoard, move);
			
				if (!newBoard.kingThreatened(player)) {
					return false;
				} else {
					undoPerformMove(newBoard, move);
				}
			} catch (DCLocationException e) {
				//this should never happen. If it does, do nothing
			}
			
			/*
			if (!putsKingInCheck(list.get(i))) {
				return false;
			}
			*/
		}
		return true;
		
	}

	/**
	 * The game is over because of the specified reason and with the specified
	 * winner. If winner is DCConstants.PLAYER_BOTH, it's a draw.
	 * @param	reason	DCConstants GameOver mode
	 * @param	winner	winning player
	 */
	private void gameOver(int reason, int winner) {
		//send message with reason and winning person
		sendOut(encoder.gameOver(reason, winner));
		//change game state to over
		setGameState(DCConstants.OVER);

		//set both players on hold
		playerArray[0].setState(DCConstants.PL_HOLD);
		playerArray[1].setState(DCConstants.PL_HOLD);
	}

	/**
	 * Adds a DCMove to the history and sends this information to all frontends
	 * @param	DCMove	move to add
	 */
	private void addToHistory(DCMove move) {
		history.add(move);
		sendOut(encoder.addHistoryString(move.toString(), move.getPlayer()));
	}

	/**
	 * Removes a DCMove from the history and sends this information to all
	 * frontends
	 * @return	move	the move that has been removed from the history
	 * @exception	DCListEmptyException if the history is empty
	 */
	private DCMove removeFromHistory() throws DCListEmptyException {
		DCMove move = history.pop();
		sendOut(encoder.removeHistoryString(move.getPlayer()));
		return move;	
	}
	



	
	/* PUBLIC METHODS */
	
	/**
	 * Method that accepts DCMessages. It delegates these to the
	 * DCBackEndDecoder, which in turn calls the appropriate methods.
	 * @param	message		the message to decode
	 */
	public void sendMessage(DCMessage message) {
		//delegate to decoder
		decoder.sendMessage(message);
	}


	/**
	 * Registers a player in the backend. The registering player can request
	 * whether he wants to play GOLD or SCARLET. If the preferred one is no
	 * longer available, the other colour is automatically assigned. If no
	 * slots are available, an error message is sent.
	 * @param	name	String with name under which the player wants to register
	 * @param	address	String representing the location of the player
	 * @param	reqNumber	int with player number the player preferres
	 * @param	connectionNumber	int used by the DCGame to map players to
	 * 								connections
	 */
	public void registerPlayer(String name, String address, int reqNumber, 
			int connectionNumber) {
		
		int receivedPlayerNr = -1;

		//try to get reqNumber slot
		if (playerArray[reqNumber].getState() == DCConstants.PL_UNREGISTERED) {
			//register
			playerArray[reqNumber].setName(name);
			playerArray[reqNumber].setAddress(address);
			playerArray[reqNumber].setState(DCConstants.PL_REGISTERED);
			receivedPlayerNr = reqNumber;
		} else if (playerArray[reqNumber].getState() == DCConstants.PL_HOLD) {
			//check if same
			if (playerArray[reqNumber].getName().equals(name) &&
				playerArray[reqNumber].getAddress().equals(address)) {
				//register
				playerArray[reqNumber].setState(DCConstants.PL_REGISTERED);
				receivedPlayerNr = reqNumber;
			}
		}

		//try to get other slot
		int newNumber = (reqNumber + 1 ) % 2;
		if (receivedPlayerNr == -1) {
			if (playerArray[newNumber].getState() == DCConstants.PL_UNREGISTERED) {
				//register
				playerArray[newNumber].setName(name);
				playerArray[newNumber].setAddress(address);
				playerArray[newNumber].setState(DCConstants.PL_REGISTERED);
				receivedPlayerNr = newNumber;
			} else if (playerArray[newNumber].getState() == DCConstants.PL_HOLD) {
				//check if same
				if (playerArray[newNumber].getName().equals(name) &&
					playerArray[newNumber].getAddress().equals(address)) {
					//register
					playerArray[newNumber].setState(DCConstants.PL_REGISTERED);
					receivedPlayerNr = newNumber;
				}
			}
		}
	
		//send appropriate registration messages
		if (receivedPlayerNr == -1) {
			sendOut(encoder.registerFailure(connectionNumber));
		} else {
			sendOut(encoder.registerSuccess(name, address, receivedPlayerNr, 
						connectionNumber));
			sendOut(encoder.newPlayerRegistered(name, address, receivedPlayerNr));
		}

		//send appropriate information about other users
		switch (receivedPlayerNr) {
			case -1 :
				sendCurrentPlayerInfo(DCConstants.PLAYER_GOLD, connectionNumber);
				sendCurrentPlayerInfo(DCConstants.PLAYER_SCARLET, connectionNumber);
				break;
			case DCConstants.PLAYER_GOLD :
				sendCurrentPlayerInfo(DCConstants.PLAYER_SCARLET, connectionNumber);
				break;
			case DCConstants.PLAYER_SCARLET :
				sendCurrentPlayerInfo(DCConstants.PLAYER_GOLD, connectionNumber);
				break;
		}

		//if both players are registered the game is ready to start so send
		//change game state to READY
		if (playerArray[0].getState() == DCConstants.PL_REGISTERED &&
				playerArray[1].getState() == DCConstants.PL_REGISTERED) {
			setGameState(DCConstants.READY);
		}
	}

	/**
	 * Unregisters a player
	 */
	public void unregisterPlayer(int player, String reason) {
		playerArray[player].setState(DCConstants.PL_UNREGISTERED);
		sendOut(encoder.playerUnregistered(player, reason));
	}

	/**
	 * Sends messages to the specified client with info on the specified
	 * player. It sends nothing when the requested player is not yet registered
	 * @param player	player about which to send info
	 * @param connectionNumber	the connection number to send the messages to
	 */
	private void sendCurrentPlayerInfo(int player, int connectionNumber) {
		if (playerArray[player].getState() != DCConstants.PL_UNREGISTERED) {
			sendOut(encoder.setPlayerInfo(player,
						playerArray[player].getName(),
						playerArray[player].getAddress(),
						connectionNumber));
		}
	}
	
	/**
	 * Starts a game. Requires that two players are registered and game status
	 * is set to READY. 
	 * @param	player		the player that starts the game
	 */
	public void startGame(int player) {
		/* Only start if in right gameState */
		if (gameState == DCConstants.READY) {
			/* arrange pieces on the board */
			board.resetBoard();

			/* empty the history */
			history = new DCMoveList();
		
			/* send game started msg */
			sendOut(encoder.gameStarted(player));
			/* dump piece starting location */
			sendOut(encoder.loadDCFrontEndDump(
					DCConstants.PLAYER_BOTH,
					board.getDCFrontEndDump()));
		
			/* set GOLD player to active player */
			setActivePlayer(DCConstants.PLAYER_GOLD);
			/* set game state to SELECTSTARTFIELD */
			setGameState(DCConstants.SELECTSTARTFIELD);
		}
	}
	
	/**
	 * Selects a piece for the active player. Requires that the gamestate is
	 * set to SELECTSTARTFIELD
	 * @param	location	location containing the piece to select
	 * @param	player		player doing the request
	 */
	public void selectPiece(DCCoord location, int player) {
		
		/* Catch Exceptions from DCGameBoard */
		try {
			if (gameState != DCConstants.SELECTSTARTFIELD) {
				//wrong gamestate
				sendOut(encoder.pieceNotSelected(player, location,
							DCConstants.SUB_INVALID_GAMESTATE));
			} else if (player != activePlayer) {
				//wrong player
				sendOut(encoder.pieceNotSelected(player, location,
							DCConstants.SUB_INVALID_PLAYER));
			} else if (!board.hasPiece(location) || 
					(board.getPlayer(location) != player)) {
				//wrong location
				sendOut(encoder.pieceNotSelected(player, location,
							DCConstants.SUB_INVALID_LOCATION));
			} else if (board.isFrozen(location)) {
				//piece frozen
				sendOut(encoder.pieceNotSelected(player, location,
							DCConstants.SUB_PIECE_FROZEN));
			} else {
				//select location
				setSelectedLocation(location);
				validMoveList = board.getValidMoveList(location);
				sendOut(encoder.sendValidTargets(player, validMoveList));
				setGameState(DCConstants.SELECTTARGETFIELD);
			}
		} catch (DCLocationException e) {
			//wrong location
			sendOut(encoder.pieceNotSelected(player, location, 
						DCConstants.SUB_INVALID_LOCATION));
		}
	}

	/**
	 * Deselect a piece. This usually happens when a move is performed, when a
	 * move is undone, or when a piece is deselected by selecting it twice. The
	 * active player is notified of the deselection with a  message
	 */
	public void deselectPiece() {
		/* Don't do anything if there is no selected piece */
		if (selectedLocation == null) {
			//do nothing
		} else {
			unsetSelectedLocation();
		}
	}
	
	/**
	 * Performs a move. Takes a target location and moves the currently
	 * selected piece accordingly, if the target location is valid and the move
	 * can be performed. The piece is also deselected and the other player
	 * becomes acctive.
	 * @param	location	target location
	 * @param	player		the player who does the move request
	 */
	public void movePiece(DCCoord location, int player) {
		
		/* Catch exceptions from DCGameBoard */
		try {
			
			//check player
			if (player != activePlayer) {
				sendOut(encoder.pieceNotMoved(player, location, 
							DCConstants.SUB_INVALID_PLAYER));
			//check gamestate
			} else if (gameState != DCConstants.SELECTTARGETFIELD) {
				sendOut(encoder.pieceNotMoved(player, location, 
							DCConstants.SUB_INVALID_GAMESTATE));
			// deselect piece if target location matched selected location
			} else if (selectedLocation.equals(location)) {
				deselectPiece();
				setGameState(DCConstants.SELECTSTARTFIELD);
			} else {
			
				//check if the move is valid, if not, throw exception that is
				//caught below
				DCMove move = validMoveList.getMoveTo(location);
				
				//check if king will be in check after move. if so send
				//message and abort
				if (putsKingInCheck(move)) {
					sendOut(encoder.pieceNotMoved(activePlayer, location,
								DCConstants.SUB_KING_IN_CHECK));
				} else {
					
					//deselect piece
					deselectPiece();

					//perform move
					//move will be edited to include promotion information by
					//the performMove function
					performMove(board, move);

					/* Check for check, checkmate, mate */
				
					boolean check = false;
					boolean mate = false;

					check = board.kingThreatened((activePlayer + 1)%2);
					mate = isMate((activePlayer + 1)%2);

					//add check condition to performed move if applicable
					if (check) {
						move.setCheck(true);
					}

					//add move to history
					addToHistory(move);

					if (check) {
						//send check condition
						sendOut(encoder.setCheck((activePlayer+1)%2));	
						if (mate) {
							gameOver(DCConstants.GAMEOVER_CHECKMATE, 
									activePlayer);
						}
					} else if (mate) {
						gameOver(DCConstants.GAMEOVER_MATE, DCConstants.PLAYER_NONE);
					}
					
					if (gameState != DCConstants.OVER) {
						setActivePlayer((activePlayer + 1) % 2);
						setGameState(DCConstants.SELECTSTARTFIELD);
					}
				}
			}
			
		} catch (DCLocationException e) {
			sendOut(encoder.pieceNotMoved(activePlayer, location, 
						DCConstants.SUB_INVALID_LOCATION));
		}
	}
	
	/**
	 * Undoes the last move(s) up to he last move performed by the player
	 * requesting the undo
	 * @param	player		player requesting the undo
	 * @param	reason		String reason for the request
	 */
	public void undoMovePiece(int player, String reason) {
		
		/* catch DCLocationExceptions from backend */
		try {
			DCMove lastMove;

			/* Undo last move */

			//first peek at last move to check it
			if (history == null || history.size() == 0) {
				throw new DCListEmptyException("Nothing in history");
			} else {
				lastMove = history.get(history.size() - 1);
				System.out.println("LASTMOVE : " + lastMove);
			}

			/* If this last move is not from the requesting player, we need
			 * to make sure there is another move that we can undo */
			if (lastMove.getPlayer() != player) {
				if (history.size() < 2 ) {
					throw new DCListEmptyException("Not enough undo information for 2 undos");
				}
			}
		
			/* if a piece is selected, deselect it */
			if (selectedLocation != null) {
				deselectPiece();
			}

			//undo the move
			lastMove = removeFromHistory();
			undoPerformMove(board, lastMove);
			
			/* If the move that has been undone was not a move by the
			 * requesting player, undo another move */
			if (lastMove.getPlayer() != player) {
				lastMove = removeFromHistory();
				undoPerformMove(board, lastMove);
			}

			/* set game state and active player */
			setActivePlayer(player);
			setGameState(DCConstants.SELECTSTARTFIELD);
				
		} catch (DCListEmptyException e) {
			/* Nothing to undo */
			sendOut(encoder.moveNotUndone(DCConstants.UNDO_HISTORY_EMPTY, player));
		} catch (DCLocationException e) {
			/* DEBUG */
			System.err.println("ERROR : DCLocationException in undoMovePiece");
			System.err.println("        This should not happen !");
			System.err.println("        Please file a bug report.");
		}
	}

	/**
	 * accepts an incoming chat message and sends it on to the intended
	 * recipients.
	 * @param	player	player who has sent the message
	 * @param	publicBoolean	whether this is a public message
	 * @param	msg		the message that has been sent.
	 */
	public void chatMessage(int player, boolean publicBoolean, String msg) {
		sendOut(encoder.chatMessage(publicBoolean, playerArray[player].getName(), 
				msg));
	}

	/**
	 * Resigns the game. The specified player stops the game, the other player
	 * wins. This decision cannot be undone
	 * @param	player		the player who resign
	 * @param	reason		String with reason for resignation
	 */
	public void resignGame(int player, String reason) {
		//only allow this message if a game is running
		if (gameState != DCConstants.OVER &&
			gameState != DCConstants.INITIALISING &&
			gameState != DCConstants.READY) {
			//for now just send a game over message
			gameOver(DCConstants.GAMEOVER_RESIGNED, (player + 1) % 2);
		}
	}

	/* CONNECTION HANDLING METHODS */

	/**
	 * A player has been disconnected. This method unregisters the player and
	 * (for now) ends the game by resigning the player. 
	 * @param	player		the player that has been disconnected
	 */
	public void connectionBroken(int player) {
		//resignGame(player, "Player disconnected.");
		//in this case, the winner field specifies the one with the broken
		//connection
		sendOut(encoder.gameOver(DCConstants.GAMEOVER_CONNECTION_BROKEN, player));
		unregisterPlayer(player, "Player disconnected.");		
	}
} 

/* END OF FILE */
