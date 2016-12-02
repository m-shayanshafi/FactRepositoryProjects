package checkersBoard;

import java.lang.ref.SoftReference;
import java.util.ArrayList;

import checkersMain.CheckersBoard;

/**
 * This is a Default implementation of a {@link CheckersBoard}. It implements
 * caching of plies and successor boards, and it uses a straightforward storage
 * of the board as an array of bytes. It uses {@link SoftReference}s for the
 * plies and successor boards to prevent {@link OutOfMemoryError}s.
 * 
 * @author Jeremy Hoffman (v1.00)
 * @author Amos Yuen
 * @version 1.21 - 7 August 2008
 */
public class DefaultCheckersBoard extends CheckersBoard {

	/**
	 * An array of 32 bytes. It only keeps track of the locations on the board
	 * that can contain Checkers pieces, and each location stores
	 * {@link CheckersBoard#PLAYER1_CHECKER}, {@link CheckersBoard#PLAYER1_KING}
	 * , {@link CheckersBoard#PLAYER2_CHECKER},
	 * {@link CheckersBoard#PLAYER2_KING}, or {@link CheckersBoard#EMPTY}. The
	 * board indexing range is [0,31], starting at 0 in the top-left corner, and
	 * ending at 31 in the bottom-right corner. The index increases by one as it
	 * moves from left-to-right across a row, and when it has completely
	 * traversed a row it continues on down the rows.
	 */
	protected final byte[] board;

	/**
	 * A cache of the array of possible plies.
	 */
	protected Ply[] plies;

	/**
	 * A cache of the inverted board. It uses a soft reference to help prevent
	 * an {@link OutOfMemoryError}
	 * 
	 * @see SoftReference
	 */
	protected SoftReference<DefaultCheckersBoard> invertedBoard;
	/**
	 * A cache of the successor boards. It uses a soft reference to help prevent
	 * an {@link OutOfMemoryError}
	 * 
	 * @see SoftReference
	 */
	protected SoftReference<DefaultCheckersBoard[]> successorBoards;

	protected boolean jumpsOnly; // Convenience variable used for forcing jumps

	/**
	 * Initialises the board for the start of a checkers game.
	 */
	public DefaultCheckersBoard() {
		super();
		board = new byte[] { PLAYER2_CHECKER, PLAYER2_CHECKER, PLAYER2_CHECKER,
				PLAYER2_CHECKER, PLAYER2_CHECKER, PLAYER2_CHECKER,
				PLAYER2_CHECKER, PLAYER2_CHECKER, PLAYER2_CHECKER,
				PLAYER2_CHECKER, PLAYER2_CHECKER, PLAYER2_CHECKER, EMPTY,
				EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,
				PLAYER1_CHECKER, PLAYER1_CHECKER, PLAYER1_CHECKER,
				PLAYER1_CHECKER, PLAYER1_CHECKER, PLAYER1_CHECKER,
				PLAYER1_CHECKER, PLAYER1_CHECKER, PLAYER1_CHECKER,
				PLAYER1_CHECKER, PLAYER1_CHECKER, PLAYER1_CHECKER };
	}

	/**
	 * Copy constructor.
	 * 
	 * @param parent
	 *            - CheckersBoard to copy
	 */
	public DefaultCheckersBoard(CheckersBoard parent) {
		super();
		if (parent instanceof DefaultCheckersBoard) {
			DefaultCheckersBoard board = (DefaultCheckersBoard) parent;
			this.board = board.board.clone();
		} else {
			board = new byte[32];
			setTo(parent);
		}
	}

	@Override
	public DefaultCheckersBoard clone() {
		return new DefaultCheckersBoard(this);
	}

	/**
	 * Finds and creates the array of plies depending using
	 * {@link #findPlies(byte, ArrayList, ArrayList)}
	 * 
	 * @see checkersMain.CheckersBoard.Ply
	 */
	protected void createPlies() {
		if (plies == null) {
			ArrayList<Ply> pliesList = new ArrayList<Ply>();
			ArrayList<Byte> jumpIndices = new ArrayList<Byte>();
			for (byte i = 0; i < 32; i++) {
				byte pieceType = board[i];
				if (pieceType == PLAYER1_CHECKER || pieceType == PLAYER1_KING)
					findPlies(i, pliesList, jumpIndices);
			}

			plies = pliesList.toArray(new Ply[pliesList.size()]);
		}
	}

	/**
	 * Finds and creates the array of successor boards depending on the what
	 * variables have already been cached. If the plies are cached each
	 * successor will be generated on demand, otherwise all successors are
	 * generated using {@link #findSuccessors(byte, ArrayList)}.
	 */
	protected void createSuccessors() {
		if (successorBoards == null || successorBoards.get() == null) {
			if (plies != null) {
				successorBoards = new SoftReference<DefaultCheckersBoard[]>(
						new DefaultCheckersBoard[plies.length]);
			} else {
				ArrayList<DefaultCheckersBoard> successorsList = new ArrayList<DefaultCheckersBoard>();

				for (byte i = 0; i < 32; i++) {
					byte pieceType = board[i];
					if (pieceType == PLAYER1_CHECKER
							|| pieceType == PLAYER1_KING)
						findSuccessors(i, successorsList);
				}

				successorBoards = new SoftReference<DefaultCheckersBoard[]>(
						successorsList
								.toArray(new DefaultCheckersBoard[successorsList
										.size()]));
			}
		}
	}

	/**
	 * This method finds all possible moves for the passed index and will add
	 * them to the list of plies. This method will force jumps. When a jump is
	 * found, this method and
	 * {@link #findPlies(byte, byte, ArrayList, ArrayList, boolean)} is called
	 * recursively to find consecutive jumps.
	 * 
	 * @see checkersMain.CheckersBoard.Ply
	 * 
	 * @param index
	 *            - the board index of the piece being examined for possible
	 *            moves
	 * @param pliesList
	 *            - a list of the currently generated plies
	 * @param jumpIndices
	 *            - a list of Bytes that contain the indices of each index the
	 *            Checkers piece moves to in a jump-chain for the current jump
	 *            recursion.
	 * @return whether the method found a move
	 */
	protected boolean findPlies(byte index, ArrayList<Ply> pliesList,
			ArrayList<Byte> jumpIndices) {
		boolean moveFound = findPlies(index, CheckersBoard.NORTH_WEST,
				pliesList, jumpIndices, false);
		moveFound = findPlies(index, CheckersBoard.NORTH_EAST, pliesList,
				jumpIndices, moveFound);

		if (board[index] == PLAYER1_KING) {
			moveFound = findPlies(index, CheckersBoard.SOUTH_WEST, pliesList,
					jumpIndices, moveFound);
			moveFound = findPlies(index, CheckersBoard.SOUTH_EAST, pliesList,
					jumpIndices, moveFound);
		}

		return moveFound;
	}

	/**
	 * This method finds possible moves in the passed direction and will. add
	 * them to the list of plies. This method will force jumps. When a jump is
	 * found, jumpsFound is set to true and prior moves are erased. Consecutive
	 * jumps are found from recursive calls to this method and
	 * {@link #findPlies(byte, ArrayList, ArrayList)}
	 * 
	 * @see checkersMain.CheckersBoard.Ply
	 * 
	 * @param index
	 *            - the board index of the piece being examined for possible
	 *            moves
	 * @param direction
	 *            - one of {@link #NORTH_EAST}, {@link #NORTH_WEST},
	 *            {@link #SOUTH_EAST}, or {@link #SOUTH_WEST}
	 * @param pliesList
	 *            - a list of the currently generated plies
	 * @param jumpIndices
	 *            - a list of Bytes that contain the indices of each index the
	 *            Checkers piece moves to in a jump-chain for the current jump
	 *            recursion.
	 * @param moveFound
	 *            - whether a move has been found for the current level of the
	 *            search
	 * @return whether the method found a move
	 */
	protected boolean findPlies(byte index, byte direction,
			ArrayList<Ply> pliesList, ArrayList<Byte> jumpIndices,
			boolean moveFound) {
		/*
		 * Try each possible move for the Checkers piece at index. If a jump is
		 * found, the board is temporarily modified to reflect the move, then it
		 * checks for further jumps. When the first jump is found, jumpsOnly and
		 * jumpsOnly are set to true and prior moves are erased.
		 */
		byte pieceType = board[index]; // PLAYER1_CHECKER or PLAYER1_KING

		// Try moving in direction
		byte neighborIndex = CheckersBoard.getNeighborIndex(index, direction);
		if (neighborIndex < 0 || neighborIndex > 31)
			return moveFound;
		byte neighborContents = board[neighborIndex];
		if (neighborContents == EMPTY && !jumpsOnly) {
			// Add ply
			pliesList.add(new Ply((byte) pliesList.size(), new byte[] { index,
					neighborIndex }));
			return true;
		}
		if (neighborContents == PLAYER2_CHECKER
				|| neighborContents == PLAYER2_KING) {
			// Try jumping in direction
			byte neighborTwoIndex = CheckersBoard.getNeighborIndex(
					neighborIndex, direction);
			if (neighborTwoIndex < 0 || neighborTwoIndex > 31)
				return moveFound;
			if (board[neighborTwoIndex] == EMPTY) {

				// If not currently searching a jump-chain, add the starting
				// index
				boolean newJumpChain = jumpIndices.isEmpty();
				if (newJumpChain)
					jumpIndices.add(index);
				jumpIndices.add(neighborTwoIndex);

				// If this is the first jump found, clear all prior moves
				// and set jumpsOnly to true
				if (!jumpsOnly) {
					pliesList.clear();
					jumpsOnly = true;
				}

				// Recursively look for jumps from that position
				board[index] = EMPTY;
				board[neighborIndex] = EMPTY;
				board[neighborTwoIndex] = pieceType;
				// If no more jumps are found create and add a ply
				if (!findPlies(neighborTwoIndex, pliesList, jumpIndices)) {
					byte[] indices = new byte[jumpIndices.size()];
					for (int i = 0; i < indices.length; i++)
						indices[i] = jumpIndices.get(i);
					pliesList.add(new Ply((byte) pliesList.size(), indices));
				}
				board[index] = pieceType;
				board[neighborIndex] = neighborContents;
				board[neighborTwoIndex] = EMPTY;

				if (newJumpChain)
					jumpIndices.clear();
				else
					jumpIndices.remove(jumpIndices.size() - 1);
				return true;
			}
		}

		return moveFound;
	}

	/**
	 * This method finds all possible moves for the passed index and will add
	 * the resulting CheckersBoards to the list of successorBoards. This method
	 * will force jumps. When a jump is found, this method and
	 * {@link #findSuccessors(byte, byte, ArrayList, boolean)} is called
	 * recursively to find consecutive jumps.
	 * 
	 * @param index
	 *            - the board index of the piece being examined for possible
	 *            moves
	 * @param successorsList
	 *            - a list of the currently generated successor boards
	 * @return whether the method found a move
	 */
	protected boolean findSuccessors(byte index,
			ArrayList<DefaultCheckersBoard> successorsList) {
		boolean moveFound = findSuccessors(index, CheckersBoard.NORTH_WEST,
				successorsList, false);
		moveFound = findSuccessors(index, CheckersBoard.NORTH_EAST,
				successorsList, moveFound);

		if (board[index] == PLAYER1_KING) {
			moveFound = findSuccessors(index, CheckersBoard.SOUTH_WEST,
					successorsList, moveFound);
			moveFound = findSuccessors(index, CheckersBoard.SOUTH_EAST,
					successorsList, moveFound);
		}

		return moveFound;
	}

	/**
	 * This method finds possible moves in the passed direction and will add the
	 * resulting CheckersBoards to the list of successorBoards. This method will
	 * force jumps. When a jump is found, jumpsFound is set to true and prior
	 * moves are erased. Consecutive jumps are found from recursive calls to
	 * this method and {@link #findSuccessors(byte, ArrayList)}.
	 * 
	 * @param index
	 *            - the board index of the piece being examined for possible
	 *            moves
	 * @param direction
	 *            - one of {@link #NORTH_EAST}, {@link #NORTH_WEST},
	 *            {@link #SOUTH_EAST}, or {@link #SOUTH_WEST}
	 * @param successorsList
	 *            - a list of the currently generated successor boards
	 * @param moveFound
	 *            whether a move has been found for the current level of the
	 *            search
	 * @return whether the method found a move
	 */
	protected boolean findSuccessors(byte index, byte direction,
			ArrayList<DefaultCheckersBoard> successorsList, boolean moveFound) {
		/*
		 * Try each possible move for the Checkers piece at index. If a jump is
		 * found, the board is temporarily modified to reflect the move, then it
		 * checks for further jumps. When the first jump is found, jumpsOnly and
		 * jumpsOnly are set to true and prior moves are erased.
		 */
		byte pieceType = board[index]; // PLAYER1_CHECKER or PLAYER1_KING

		// Try moving in direction
		byte neighborIndex = CheckersBoard.getNeighborIndex(index, direction);
		if (neighborIndex < 0 || neighborIndex > 31)
			return moveFound;
		byte neighborContents = board[neighborIndex];
		if (neighborContents == OFFBOARD)
			return moveFound;
		if (neighborContents == EMPTY && !jumpsOnly) {
			// Add successor board
			DefaultCheckersBoard successor = clone();
			successor.board[index] = EMPTY;
			if (pieceType == PLAYER1_CHECKER && neighborIndex / 4 == 0)
				successor.board[neighborIndex] = PLAYER1_KING;
			else
				successor.board[neighborIndex] = pieceType;
			successorsList.add(successor);
			return true;
		}
		if (neighborContents == PLAYER2_CHECKER
				|| neighborContents == PLAYER2_KING) {
			// Try jumping in direction
			byte neighborTwoIndex = CheckersBoard.getNeighborIndex(
					neighborIndex, direction);
			if (neighborTwoIndex < 0 || neighborTwoIndex > 31)
				return moveFound;
			if (board[neighborTwoIndex] == EMPTY) {

				// If this is the first jump found, clear all prior moves
				// and set jumpsOnly to true
				if (!jumpsOnly) {
					successorsList.clear();
					jumpsOnly = true;
				}

				// Recursively look for jumps from that position
				board[index] = EMPTY;
				board[neighborIndex] = EMPTY;
				if (pieceType == PLAYER1_CHECKER && neighborTwoIndex / 4 == 0)
					board[neighborTwoIndex] = PLAYER1_KING;
				else
					board[neighborTwoIndex] = pieceType;
				if (!findSuccessors(neighborTwoIndex, successorsList))
					successorsList.add(clone());
				board[index] = pieceType;
				board[neighborIndex] = neighborContents;
				board[neighborTwoIndex] = EMPTY;

				return true;
			}
		}

		return moveFound;
	}

	@Override
	public void freeCache() {
		invertedBoard = null;
		plies = null;
		successorBoards = null;
	}

	@Override
	public int getNumPlies() {
		if (successorBoards != null && successorBoards.get() != null)
			return successorBoards.get().length;

		createPlies();
		return plies.length;
	}

	@Override
	public int getNumSuccessors() {
		if (plies != null)
			return plies.length;

		createSuccessors();
		return successorBoards.get().length;
	}

	@Override
	public byte getPiece(int index) {
		if (index < 0 || index > 31)
			return OFFBOARD;
		return board[index];
	}

	@Override
	public byte getPiece(int row, int col) {
		return getPiece(getIndex(row, col));
	}

	@Override
	public Ply getPly(int index) {
		createPlies();

		return plies[index];
	}

	@Override
	public DefaultCheckersBoard getSuccessor(int index) {
		createSuccessors();

		DefaultCheckersBoard board = successorBoards.get()[index];
		if (board == null) {
			successorBoards.get()[index] = clone();
			applyPly(successorBoards.get()[index], plies[index]);
		}

		return successorBoards.get()[index];
	}

	@Override
	public DefaultCheckersBoard invertCheckersBoard() {
		if (invertedBoard != null && invertedBoard.get() != null)
			return invertedBoard.get();

		DefaultCheckersBoard inverted = new DefaultCheckersBoard();
		inverted.invertedBoard = new SoftReference<DefaultCheckersBoard>(this);
		invertedBoard = new SoftReference<DefaultCheckersBoard>(inverted);
		for (int i = 0; i < 32; i++)
			inverted.board[31 - i] = invertPiece(board[i]);
		return inverted;
	}

	@Override
	protected boolean setPiece(int index, byte pieceType) {
		if (index < 0 || index > 31 || pieceType == OFFBOARD)
			return false;

		board[index] = pieceType;
		return true;
	}

	@Override
	protected boolean setPiece(int row, int col, byte pieceType) {
		return setPiece(getIndex(row, col), pieceType);
	}
}