package checkersBoard;

import java.lang.ref.SoftReference;
import java.util.ArrayList;

import checkersMain.CheckersBoard;

/**
 * This is a bit implementation of a {@link CheckersBoard}. It implements
 * caching of plies and successor boards, and it stores each board location as 3
 * bits. Its time efficiency is less than the {@link DefaultCheckersBoard}, but
 * its storage method saves a considerable amount of space. It uses
 * {@link SoftReference}s for the plies and successor boards to prevent
 * {@link OutOfMemoryError}s.
 * 
 * @author Amos Yuen
 * @version 1.02 - 7 August 2008
 */
public class BitCheckersBoard extends CheckersBoard {

	protected static long initBoard1;
	protected static int initBoard2;

	/**
	 * The first part of the board. The board is stored in a long and a short
	 * (96 bits) and uses 3 bits to store each board location.
	 */
	protected long board1;
	/**
	 * The second part of the board. The board is stored in a long and a short
	 * (96 bits) and uses 3 bits to store each board location.
	 */
	protected int board2;

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
	protected SoftReference<BitCheckersBoard> invertedBoard;
	/**
	 * A cache of the successor boards. It uses a soft reference to help prevent
	 * an {@link OutOfMemoryError}
	 * 
	 * @see SoftReference
	 */
	protected SoftReference<BitCheckersBoard[]> successorBoards;

	protected boolean jumpsOnly; // Convenience variable used for forcing jumps

	/**
	 * Initialises the board for the start of a checkers game.
	 */
	public BitCheckersBoard() {
		super();
		if (initBoard1 != 0) {
			board1 = initBoard1;
			board2 = initBoard2;
		} else {
			for (int i = 0; i < 12; i++)
				setPiece(i, PLAYER2_CHECKER);
			for (int i = 20; i < 32; i++)
				setPiece(i, PLAYER1_CHECKER);
			initBoard1 = board1;
			initBoard2 = board2;
		}
	}

	/**
	 * Copy constructor.
	 * 
	 * @param parent
	 *            - CheckersBoard to copy
	 */
	public BitCheckersBoard(CheckersBoard parent) {
		super();
		if (parent instanceof BitCheckersBoard) {
			BitCheckersBoard board = (BitCheckersBoard) parent;
			this.board1 = board.board1;
			this.board2 = board.board2;
		} else {
			setTo(parent);
		}
	}

	@Override
	public BitCheckersBoard clone() {
		return new BitCheckersBoard(this);
	}

	/**
	 * Finds and creates the array of plies depending using
	 * {@link #findPlies(byte, byte, ArrayList, ArrayList)}
	 * 
	 * @see checkersMain.CheckersBoard.Ply
	 */
	protected void createPlies() {
		if (plies == null) {
			ArrayList<Ply> pliesList = new ArrayList<Ply>();
			ArrayList<Byte> jumpIndices = new ArrayList<Byte>();
			for (byte i = 0; i < 32; i++) {
				byte pieceType = getPiece(i);
				if (pieceType == PLAYER1_CHECKER || pieceType == PLAYER1_KING)
					findPlies(i, pieceType, pliesList, jumpIndices);
			}

			plies = pliesList.toArray(new Ply[pliesList.size()]);
		}
	}

	/**
	 * Finds and creates the array of successor boards depending on the what
	 * variables have already been cached. If the plies are cached each
	 * successor will be generated on demand, otherwise all successors are
	 * generated using {@link #findSuccessors(byte, byte, ArrayList)}.
	 */
	protected void createSuccessors() {
		if (successorBoards == null || successorBoards.get() == null) {
			if (plies != null) {
				successorBoards = new SoftReference<BitCheckersBoard[]>(
						new BitCheckersBoard[plies.length]);
			} else {
				ArrayList<BitCheckersBoard> successorsList = new ArrayList<BitCheckersBoard>();

				for (byte i = 0; i < 32; i++) {
					byte pieceType = getPiece(i);
					if (pieceType == PLAYER1_CHECKER
							|| pieceType == PLAYER1_KING)
						findSuccessors(i, pieceType, successorsList);
				}

				successorBoards = new SoftReference<BitCheckersBoard[]>(
						successorsList
								.toArray(new BitCheckersBoard[successorsList
										.size()]));
			}
		}
	}

	/**
	 * This method finds all possible moves for the passed index and will add
	 * them to the list of plies. This method will force jumps. When a jump is
	 * found, this method and
	 * {@link #findPlies(byte, byte, byte, ArrayList, ArrayList, boolean)} is
	 * called recursively to find consecutive jumps.
	 * 
	 * @see checkersMain.CheckersBoard.Ply
	 * 
	 * @param index
	 *            - the board index of the piece being examined for possible
	 *            moves
	 * @param pieceType
	 *            - Either {@link CheckersBoard#PLAYER1_CHECKER} or
	 *            {@link CheckersBoard#PLAYER1_KING}
	 * @param pliesList
	 *            - a list of the currently generated plies
	 * @param jumpIndices
	 *            - a list of Bytes that contain the indices of each index the
	 *            Checkers piece moves to in a jump-chain for the current jump
	 *            recursion.
	 * @return whether the method found a move
	 */
	protected boolean findPlies(byte index, byte pieceType,
			ArrayList<Ply> pliesList, ArrayList<Byte> jumpIndices) {
		boolean moveFound = findPlies(index, pieceType,
				CheckersBoard.NORTH_WEST, pliesList, jumpIndices, false);
		moveFound = findPlies(index, pieceType, CheckersBoard.NORTH_EAST,
				pliesList, jumpIndices, moveFound);

		if (getPiece(index) == PLAYER1_KING) {
			moveFound = findPlies(index, pieceType, CheckersBoard.SOUTH_WEST,
					pliesList, jumpIndices, moveFound);
			moveFound = findPlies(index, pieceType, CheckersBoard.SOUTH_EAST,
					pliesList, jumpIndices, moveFound);
		}

		return moveFound;
	}

	/**
	 * This method finds possible moves in the passed direction and will. add
	 * them to the list of plies. This method will force jumps. When a jump is
	 * found, jumpsFound is set to true and prior moves are erased. Consecutive
	 * jumps are found from recursive calls to this method and
	 * {@link #findPlies(byte, byte, ArrayList, ArrayList)}.
	 * 
	 * @see checkersMain.CheckersBoard.Ply
	 * 
	 * @param index
	 *            The board index of the piece being examined for possible moves
	 * @param pieceType
	 *            - Either {@link CheckersBoard#PLAYER1_CHECKER} or
	 *            {@link CheckersBoard#PLAYER1_KING}
	 * @param direction
	 *            - one of {@link #NORTH_EAST}, {@link #NORTH_WEST},
	 *            {@link #SOUTH_EAST}, or {@link #SOUTH_WEST}
	 * @param pliesList
	 *            - a list of the currently generated plies
	 * @param jumpIndices
	 *            A list of Bytes that contain the indices of each index the
	 *            Checkers piece moves to in a jump-chain for the current jump
	 *            recursion.
	 * @param moveFound
	 *            whether a move has been found for the current level of the
	 *            search
	 * @return whether the method found a move
	 */
	protected boolean findPlies(byte index, byte pieceType, byte direction,
			ArrayList<Ply> pliesList, ArrayList<Byte> jumpIndices,
			boolean moveFound) {
		/*
		 * Try each possible move for the Checkers piece at index. If a jump is
		 * found, the board is temporarily modified to reflect the move, then it
		 * checks for further jumps. When the first jump is found, jumpsOnly and
		 * jumpsOnly are set to true and prior moves are erased.
		 */

		// Try moving in direction
		byte neighborIndex = CheckersBoard.getNeighborIndex(index, direction);
		byte neighborContents = getPiece(neighborIndex);
		if (neighborContents == OFFBOARD)
			return moveFound;
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
			if (getPiece(neighborTwoIndex) == EMPTY) {

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
				setPiece(index, EMPTY);
				setPiece(neighborIndex, EMPTY);
				setPiece(neighborTwoIndex, pieceType);
				boolean jumpFound = findPlies(neighborTwoIndex, pieceType,
						pliesList, jumpIndices);
				setPiece(index, pieceType);
				setPiece(neighborIndex, neighborContents);
				setPiece(neighborTwoIndex, EMPTY);

				// If no more jumps are found create and add a ply
				if (!jumpFound) {
					byte[] indices = new byte[jumpIndices.size()];
					for (int i = 0; i < indices.length; i++)
						indices[i] = jumpIndices.get(i);
					pliesList.add(new Ply((byte) pliesList.size(), indices));
				}

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
	 * {@link #findSuccessors(byte, byte, byte, ArrayList, boolean)} is called
	 * recursively to find consecutive jumps.
	 * 
	 * @param index
	 *            The board index of the piece being examined for possible moves
	 * @param pieceType
	 *            - Either {@link CheckersBoard#PLAYER1_CHECKER} or
	 *            {@link CheckersBoard#PLAYER1_KING}
	 * @param successorsList
	 *            A list of the currently generated successor boards
	 * @return whether the method found a move
	 */
	protected boolean findSuccessors(byte index, byte pieceType,
			ArrayList<BitCheckersBoard> successorsList) {
		boolean moveFound = findSuccessors(index, pieceType,
				CheckersBoard.NORTH_WEST, successorsList, false);
		moveFound = findSuccessors(index, pieceType, CheckersBoard.NORTH_EAST,
				successorsList, moveFound);

		if (pieceType == PLAYER1_KING) {
			moveFound = findSuccessors(index, pieceType,
					CheckersBoard.SOUTH_WEST, successorsList, moveFound);
			moveFound = findSuccessors(index, pieceType,
					CheckersBoard.SOUTH_EAST, successorsList, moveFound);
		}

		return moveFound;
	}

	/**
	 * This method finds possible moves in the passed direction and will add the
	 * resulting CheckersBoards to the list of successorBoards. This method will
	 * force jumps. When a jump is found, jumpsFound is set to true and prior
	 * moves are erased. Consecutive jumps are found from recursive calls to
	 * this method and {@link #findSuccessors(byte, byte, ArrayList)}.
	 * 
	 * @param index
	 *            The board index of the piece being examined for possible moves
	 * @param pieceType
	 *            - Either {@link CheckersBoard#PLAYER1_CHECKER} or
	 *            {@link CheckersBoard#PLAYER1_KING}
	 * @param direction
	 *            - one of {@link #NORTH_EAST}, {@link #NORTH_WEST},
	 *            {@link #SOUTH_EAST}, or {@link #SOUTH_WEST}
	 * @param successorsList
	 *            A list of the currently generated successor boards
	 * @param moveFound
	 *            whether a move has been found for the current level of the
	 *            search
	 * @return whether the method found a move
	 */
	protected boolean findSuccessors(byte index, byte pieceType,
			byte direction, ArrayList<BitCheckersBoard> successorsList,
			boolean moveFound) {
		/*
		 * Try each possible move for the Checkers piece at index. If a jump is
		 * found, the board is temporarily modified to reflect the move, then it
		 * checks for further jumps. When the first jump is found, jumpsOnly and
		 * jumpsOnly are set to true and prior moves are erased.
		 */

		// Try moving in direction
		byte neighborIndex = CheckersBoard.getNeighborIndex(index, direction);
		byte neighborContents = getPiece(neighborIndex);
		if (neighborContents == OFFBOARD)
			return moveFound;
		if (neighborContents == EMPTY && !jumpsOnly) {
			// Add successor board
			BitCheckersBoard board = clone();
			board.setPiece(index, EMPTY);
			if (pieceType == PLAYER1_CHECKER && neighborIndex / 4 == 0)
				board.setPiece(neighborIndex, PLAYER1_KING);
			else
				board.setPiece(neighborIndex, pieceType);
			successorsList.add(board);
			return true;
		}
		if (neighborContents == PLAYER2_CHECKER
				|| neighborContents == PLAYER2_KING) {
			// Try jumping in direction
			byte neighborTwoIndex = CheckersBoard.getNeighborIndex(
					neighborIndex, direction);
			if (getPiece(neighborTwoIndex) == EMPTY) {

				// If this is the first jump found, clear all prior moves
				// and set jumpsOnly to true
				if (!jumpsOnly) {
					successorsList.clear();
					jumpsOnly = true;
				}

				// Recursively look for jumps from that position
				byte newPieceType = pieceType;
				if (pieceType == PLAYER1_CHECKER && neighborTwoIndex / 4 == 0)
					newPieceType = PLAYER1_KING;
				setPiece(index, EMPTY);
				setPiece(neighborIndex, EMPTY);
				setPiece(neighborTwoIndex, newPieceType);
				if (!findSuccessors(neighborTwoIndex, newPieceType,
						successorsList))
					successorsList.add(clone());
				setPiece(index, pieceType);
				setPiece(neighborIndex, neighborContents);
				setPiece(neighborTwoIndex, EMPTY);

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

		if (index < 21)
			return (byte) ((board1 >> (61 - index * 3)) & 7);
		else if (index > 21)
			return (byte) ((board2 >> (93 - index * 3)) & 7);
		else {
			return (byte) ((((int) board1 & 1) << 2) | (board2 >> 30 & 3));
		}
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
	public BitCheckersBoard getSuccessor(int index) {
		createSuccessors();

		BitCheckersBoard board = successorBoards.get()[index];
		if (board == null) {
			successorBoards.get()[index] = clone();
			applyPly(successorBoards.get()[index], plies[index]);
		}

		return successorBoards.get()[index];
	}

	@Override
	public BitCheckersBoard invertCheckersBoard() {
		if (invertedBoard != null && invertedBoard.get() != null)
			return invertedBoard.get();

		BitCheckersBoard board = new BitCheckersBoard();
		board.invertedBoard = new SoftReference<BitCheckersBoard>(this);
		invertedBoard = new SoftReference<BitCheckersBoard>(board);
		for (int i = 0; i < 32; i++)
			board.setPiece(31 - i, invertPiece(getPiece(i)));
		return board;
	}

	@Override
	protected boolean setPiece(int index, byte pieceType) {
		if (index < 0 || index > 31 || pieceType == OFFBOARD)
			return false;

		if (index < 21) {
			index = 61 - index * 3;
			board1 = (board1 & ~(7l << index)) | ((pieceType & 7l) << index);
		} else if (index > 21) {
			index = 93 - index * 3;
			board2 = (board2 & ~(7 << index)) | ((pieceType & 7) << index);
		} else {
			board1 = (board1 & ~1) | ((pieceType & 7) >> 2);
			board2 = (board2 & 1073741823) | ((pieceType & 3) << 30);
		}

		return true;
	}

	@Override
	protected boolean setPiece(int row, int col, byte pieceType) {
		return setPiece(getIndex(row, col), pieceType);
	}
}