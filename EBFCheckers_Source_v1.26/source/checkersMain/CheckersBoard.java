package checkersMain;

/**
 * This class is designed to hold data on the positioning and types of Checkers
 * pieces on a CheckersBoard. It also will generate all possible plies a player
 * can make and the resulting successor CheckersBoards. Player1 is the only
 * player that can take a ply, so the board should be inverted between player
 * turns.
 * 
 * @author Amos Yuen
 * @version 1.20 - 3 August 2008
 */
public abstract class CheckersBoard implements Cloneable {

	/**
	 * A package class for a possible ply that can be taken for a CheckersBoard.
	 * The Ply stores its index in the CheckersBoard plyList from the method
	 * {@link CheckersBoard#getPly(int)}. It also has an array representing the
	 * indices of the move. The first index is always the index of the Checkers
	 * piece that will be moved. The last index is always the index of the
	 * Checkers piece after the move. Any indices in between the first and last
	 * indices indicate indices the Checkers piece will move to in between jumps
	 * for a multiple-jump ply.
	 * 
	 * @see CheckersBoard#getPly(int)
	 * 
	 * @author Amos Yuen
	 * @version 1.00 - 3 August 2008
	 */
	public static final class Ply {

		/**
		 * Inverts the passed {@link Ply}, so that the indices would be correct
		 * if the board was inverted.
		 * 
		 * @param ply
		 *            The {@link Ply} to be inverted
		 * @return the inverted {@link Ply}
		 */
		public static Ply getInvertedPly(Ply ply) {
			byte[] invertedBoardIndices = new byte[ply.boardIndices.length];
			for (int i = 0; i < invertedBoardIndices.length; i++)
				invertedBoardIndices[i] = (byte) (31 - ply.boardIndices[i]);
			return new Ply(ply.plyIndex, invertedBoardIndices);
		}

		private final byte[] boardIndices;

		public final byte plyIndex;

		public Ply(byte plyIndex, byte[] boardIndices) {
			super();
			this.plyIndex = plyIndex;
			this.boardIndices = boardIndices;
		}

		public byte get(int index) {
			return boardIndices[index];
		}

		public boolean isJump() {
			return boardIndices.length > 2
					|| Math.abs(boardIndices[0] - boardIndices[1]) > 5;
		}

		public int size() {
			return boardIndices.length;
		}

		@Override
		public String toString() {
			StringBuffer strBuff = new StringBuffer(getClass().getSimpleName());
			strBuff.append(" [");
			for (int i = 0; i < boardIndices.length; i++) {
				strBuff.append(boardIndices[i]);
				if (i < boardIndices.length - 1)
					strBuff.append(',');
			}
			strBuff.append(']');
			return strBuff.toString();
		}
	}

	/**
	 * A CheckersBoard piece constant representing an empty location.
	 */
	public static final byte EMPTY = 0;
	/**
	 * A CheckersBoard piece constant representing an invalid location.
	 */
	public static final byte OFFBOARD = -1;
	/**
	 * A CheckersBoard piece constant representing a king owned by Player1.
	 */
	public static final byte PLAYER1_KING = 2;
	/**
	 * A CheckersBoard piece constant representing a checker owned by Player1.
	 */
	public static final byte PLAYER1_CHECKER = 1;
	/**
	 * A CheckersBoard piece constant representing a king owned by Player2.
	 */
	public static final byte PLAYER2_KING = 4;
	/**
	 * A CheckersBoard piece constant representing a checker owned by Player2.
	 */
	public static final byte PLAYER2_CHECKER = 3;

	/**
	 * Char constant representing the EMPTY constant.
	 */
	public static final char EMPTY_CHAR = ' ';
	/**
	 * Char constant representing the PLAYER1_KING constant.
	 */
	public static final char PLAYER1_KING_CHAR = 'R';
	/**
	 * Char constant representing the PLAYER1_CHECKER constant.
	 */
	public static final char PLAYER1_CHECKER_CHAR = 'r';
	/**
	 * Char constant representing the PLAYER2_KING constant.
	 */
	public static final char PLAYER2_KING_CHAR = 'W';
	/**
	 * Char constant representing the PLAYER2_CHECKER constant.
	 */
	public static final char PLAYER2_CHECKER_CHAR = 'w';

	/**
	 * Direction constant representing NorthEast.
	 */
	public static final byte NORTH_EAST = 1;
	/**
	 * Direction constant representing NorthWest.
	 */
	public static final byte NORTH_WEST = 0;
	/**
	 * Direction constant representing SouthEast.
	 */
	public static final byte SOUTH_EAST = 2;
	/**
	 * Direction constant representing SouthWest.
	 */
	public static final byte SOUTH_WEST = 3;

	/**
	 * Applies the passed {@link Ply} to this board to get the resulting
	 * successor board.
	 * 
	 * @see Ply
	 * 
	 * @param ply
	 *            - the {@link Ply} to be applied to the board.
	 */
	protected static void applyPly(CheckersBoard board, Ply ply) {
		byte firstIndex = ply.get(0);
		byte lastIndex = ply.get(ply.size() - 1);

		byte firstPiece = board.getPiece(firstIndex);
		board.setPiece(firstIndex, CheckersBoard.EMPTY);
		if (firstPiece == CheckersBoard.PLAYER1_CHECKER && lastIndex / 4 == 0)
			board.setPiece(lastIndex, CheckersBoard.PLAYER1_KING);
		else
			board.setPiece(lastIndex, firstPiece);

		if (ply.size() > 2 || Math.abs(firstIndex - lastIndex) > 5) {
			// Is a jump or multiple jumps

			// rightShiftedRow means that the squares that checkers
			// play on are on x indices 1,3,5,7 of the row
			boolean rightShiftedRow = firstIndex / 4 % 2 == 0;

			// Is a jump or jump-chain, so remove jumped pieces
			for (byte i = 0; i < ply.size() - 1; i++) {
				byte index = ply.get(i);
				byte nextIndex = ply.get(i + 1);
				int jumpedIndex = index + nextIndex;
				if (rightShiftedRow)
					jumpedIndex++;
				jumpedIndex /= 2;
				board.setPiece(jumpedIndex, EMPTY);
			}
		}

		board.freeCache();
	}

	/**
	 * Returns a char that represents that passed pieceType
	 * 
	 * @param pieceType
	 *            - one of {@link #PLAYER1_CHECKER}, {@link #PLAYER1_KING},
	 *            {@link #PLAYER2_CHECKER}, {@link #PLAYER2_KING}, or
	 *            {@link #EMPTY}
	 * @return the char that represents the pieceType
	 */
	public static char getChar(byte pieceType) {
		switch (pieceType) {
		case (PLAYER1_CHECKER):
			return PLAYER1_CHECKER_CHAR;
		case (PLAYER1_KING):
			return PLAYER1_KING_CHAR;
		case (PLAYER2_CHECKER):
			return PLAYER2_CHECKER_CHAR;
		case (PLAYER2_KING):
			return PLAYER2_KING_CHAR;
		default:
			return EMPTY_CHAR;
		}
	}

	/**
	 * Returns the column of the passed index.
	 * 
	 * @param index
	 *            - board location in the range [0,31] (top-left corner being
	 *            zero and bottom-right corner being 31. The index increases
	 *            from left-to-right first, then from top-to-bottom)
	 * 
	 * @return the column of the passed index, or -1 if an invalid index
	 */
	public static int getColumn(int index) {
		if (index < 0 || index > 31)
			return -1;

		int col = index % 4 * 2;
		if (getRow(index) % 2 == 0)
			col++;
		return col;
	}

	/**
	 * Returns the corresponding index for the passed row and column.
	 * 
	 * @param row
	 *            - row location in the range [0,7] (topmost being zero)
	 * @param col
	 *            - column location in the range [0,7] (leftmost being zero)
	 * @return the corresponding index for the passed row and column or -1 if
	 *         passed invalid parameters.
	 */
	public static int getIndex(int row, int col) {
		if (row < 0 || row > 7 || col < 0 || col > 7 || (row + col) % 2 == 0)
			return -1; // (row,col) is not a valid board position

		return col / 2 + row * 4;
	}

	/**
	 * Returns the index of the neighbouring board location in the passed
	 * direction from the passed starting index. If the location is off the
	 * board, an invalid index will be returned.
	 * 
	 * @param index
	 *            - board location in the range [0,31] (top-left corner being
	 *            zero and bottom-right corner being 31. The index increases
	 *            from left-to-right first, then from top-to-bottom)
	 * @param direction
	 *            - one of {@link #NORTH_WEST}, {@link #NORTH_EAST},
	 *            {@link #SOUTH_WEST}, or {@link #SOUTH_EAST}
	 * @return the location of the neighbouring board location
	 */
	public static byte getNeighborIndex(byte index, byte direction) {
		switch (direction) {
		case (NORTH_WEST):
			if (index / 4 % 2 == 0)
				return (byte) (index - 4);
			if (index % 4 != 0)
				return (byte) (index - 5);
			return -1;
		case (NORTH_EAST):
			if (index / 4 % 2 == 1)
				return (byte) (index - 4);
			if (index % 4 != 3)
				return (byte) (index - 3);
		case (SOUTH_EAST):
			if (index / 4 % 2 == 1)
				return (byte) (index + 4);
			if (index % 4 != 3)
				return (byte) (index + 5);
			return -1;
		case (SOUTH_WEST):
			if (index / 4 % 2 == 0)
				return (byte) (index + 4);
			if (index % 4 != 0)
				return (byte) (index + 3);
			return -1;
		}
		return -1;
	}

	/**
	 * Returns the row of the passed index.
	 * 
	 * @param index
	 *            - board location in the range [0,31] (top-left corner being
	 *            zero and bottom-right corner being 31. The index increases
	 *            from left-to-right first, then from top-to-bottom)
	 * @return the row of the passed index, or -1 if an invalid index
	 */
	public static int getRow(int index) {
		if (index < 0 || index > 31)
			return -1;

		return index / 4;
	}

	/**
	 * Returns the piece with the player inverted, if the pieceType is owned by
	 * a player.
	 * 
	 * @param pieceType
	 *            - one of {@link #PLAYER1_CHECKER}, {@link #PLAYER1_KING},
	 *            {@link #PLAYER2_CHECKER}, {@link #PLAYER2_KING}, {@link #EMPTY},
	 *            or {@link #OFFBOARD}
	 * @return the piece with the player inverted, if any. It will be one of
	 *         {@link #PLAYER1_CHECKER}, {@link #PLAYER1_KING},
	 *         {@link #PLAYER2_CHECKER}, {@link #PLAYER2_KING}, {@link #EMPTY}, or
	 *         {@link #OFFBOARD}
	 */
	public static byte invertPiece(byte pieceType) {
		if (pieceType == PLAYER1_CHECKER)
			return PLAYER2_CHECKER;
		if (pieceType == PLAYER1_KING)
			return PLAYER2_KING;
		if (pieceType == PLAYER2_CHECKER)
			return PLAYER1_CHECKER;
		if (pieceType == PLAYER2_KING)
			return PLAYER1_KING;
		return pieceType;
	}

	/**
	 * Returns a king owned by the same player, if the passed pieceType is a
	 * checker, else it returns the pieceType.
	 * 
	 * @param pieceType
	 *            - one of {@link #PLAYER1_CHECKER}, {@link #PLAYER1_KING},
	 *            {@link #PLAYER2_CHECKER}, {@link #PLAYER2_KING}, {@link #EMPTY},
	 *            or {@link #OFFBOARD}
	 * @return the upgraded piece type if applicable. It will be one of
	 *         {@link #PLAYER1_CHECKER}, {@link #PLAYER1_KING},
	 *         {@link #PLAYER2_CHECKER}, {@link #PLAYER2_KING}, {@link #EMPTY}, or
	 *         {@link #OFFBOARD}
	 */
	public static byte upgrade(byte pieceType) {
		if (pieceType == PLAYER1_CHECKER)
			return PLAYER1_KING;
		else if (pieceType == PLAYER2_CHECKER)
			return PLAYER2_KING;
		return pieceType;
	}

	/**
	 * Default Constructor
	 */
	public CheckersBoard() {
		;
	}

	/**
	 * Should return a clone of the current CheckersBoard such that
	 * {@link #equals(CheckersBoard)} returns true and their classes are equal.
	 */
	@Override
	public abstract CheckersBoard clone();

	/**
	 * Returns whether the pieces in each index of this board matches the pieces
	 * in each corresponding index of the other board.
	 * 
	 * @see #PLAYER1_CHECKER
	 * @see #PLAYER1_KING
	 * @see #PLAYER2_CHECKER
	 * @see #PLAYER2_KING
	 * @see #EMPTY
	 * @see #equals(CheckersBoard)
	 * 
	 * @param other
	 *            - the other CheckersBoard to compare with
	 * @return whether the boards are equal
	 */
	public boolean equals(CheckersBoard other) {
		for (int i = 0; i < 32; i++)
			if (this.getPiece(i) != other.getPiece(i))
				return false;

		return true;
	}

	/**
	 * Frees any caching.
	 */
	public void freeCache() {
	}

	/**
	 * Counts the number of pieces of the passed piece type in this
	 * CheckersBoard
	 * 
	 * @param pieceType
	 *            - one of {@link #PLAYER1_CHECKER}, {@link #PLAYER1_KING},
	 *            {@link #PLAYER2_CHECKER}, {@link #PLAYER2_KING}, or
	 *            {@link #EMPTY}
	 * @return the number of pieces of piece type in this CheckersBoard
	 */
	public int getCount(byte pieceType) {
		if (pieceType == OFFBOARD)
			return 0;

		int count = 0;
		for (int i = 0; i < 32; i++)
			if (getPiece(i) == pieceType)
				count++;
		return count;
	}

	/**
	 * Returns the number of possible plies, which should correspond with the
	 * number of successor boards. This method should return the same value as
	 * {@link #getNumSuccessors()}, but in the case that nothing is cached, it
	 * will create a list of plies to determine the plies count.
	 * 
	 * @see #getPly(int)
	 * 
	 * @return the number of possible plies
	 */
	public abstract int getNumPlies();

	/**
	 * Returns the number of successor boards, which should correspond with the
	 * number of possible plies. This method should return the same value as
	 * {@link #getNumPlies()}, but in the case that nothing is cached, it will
	 * create a list of successor boards to determine the plies count.
	 * 
	 * @see #getSuccessor(int)
	 * 
	 * @return the number of successor boards
	 */
	public abstract int getNumSuccessors();

	/**
	 * Returns the contents of the specified board location.
	 * 
	 * @param index
	 *            - board location in the range [0,31] (top-left corner being
	 *            zero and bottom-right corner being 31. The index increases
	 *            from left-to-right first, then from top-to-bottom)
	 * @return one of {@link #PLAYER1_CHECKER}, {@link #PLAYER1_KING},
	 *         {@link #PLAYER2_CHECKER}, {@link #PLAYER2_KING}, {@link #EMPTY}, or
	 *         {@link #OFFBOARD}
	 */
	public abstract byte getPiece(int index);

	/**
	 * Returns the contents of the specified board location.
	 * 
	 * @param row
	 *            - row location in the range [0,7] (topmost being zero)
	 * @param col
	 *            - column location in the range [0,7] (leftmost being zero)
	 * @return one of {@link #PLAYER1_CHECKER}, {@link #PLAYER1_KING},
	 *         {@link #PLAYER2_CHECKER}, {@link #PLAYER2_KING}, {@link #EMPTY}, or
	 *         {@link #OFFBOARD}
	 */
	public abstract byte getPiece(int row, int col);

	/**
	 * Assuming it's Player1's turn, it returns the possible ply corresponding
	 * to the passed index. The indices of the plies in the returned list should
	 * correspond to the indices of the successor boards in the list returned
	 * from {@link #getSuccessor(int)}.
	 * 
	 * @see #getNumPlies()
	 * @see Ply
	 * 
	 * @param index
	 *            - the index of the ply to retrieve
	 * @return the ply corresponding to the passed index
	 */
	public abstract Ply getPly(int index);

	/**
	 * Assuming it's Player1's turn, it returns the CheckersBoard resulting from
	 * the ply at the corresponding index. The indices of the successor boards
	 * returned should correspond to the indices of the plies returned from
	 * {@link #getPly(int)}.
	 * 
	 * @see #getNumSuccessors()
	 * 
	 * @param index
	 *            - the index of the successor to retrieve
	 * @return the resulting CheckersBoard from the corresponding ply
	 */
	public abstract CheckersBoard getSuccessor(int index);

	/**
	 * Creates a new board and it switches Player2 and Player1 pieces and up and
	 * down directions, effectively allowing it to always be "PLAYER1's turn"
	 * and Player1 to always be moving "up" even as play alternates between two
	 * players. The inverted CheckersBoard will be cached.
	 * 
	 * Note: this method in combination with the caching of successor boards can
	 * cause memory to not be released. Use the freeCache() method to help with
	 * this.
	 * 
	 * @return the inverted CheckersBoard
	 */
	public abstract CheckersBoard invertCheckersBoard();

	/**
	 * Sets the contents of the given board space to the passed pieceType.
	 * 
	 * @param index
	 *            - board location in the range [0,31] (top-left corner being
	 *            zero and bottom-right corner being 31. The index increases
	 *            from left-to-right first, then from top-to-bottom)
	 * @param pieceType
	 *            - one of {@link #PLAYER1_CHECKER}, {@link #PLAYER1_KING},
	 *            {@link #PLAYER2_CHECKER}, {@link #PLAYER2_KING}, or
	 *            {@link #EMPTY}
	 * @return whether the method executed successfully
	 */
	protected abstract boolean setPiece(int index, byte pieceType);

	/**
	 * Sets the contents of the given board space to the passed pieceType.
	 * 
	 * @param row
	 *            - row location in the range [0,7] (topmost being zero)
	 * @param col
	 *            - column location in the range [0,7] (leftmost being zero)
	 * @param pieceType
	 *            - one of {@link #PLAYER1_CHECKER}, {@link #PLAYER1_KING},
	 *            {@link #PLAYER2_CHECKER}, {@link #PLAYER2_KING}, or
	 *            {@link #EMPTY}
	 * @return whether the method executed successfully
	 */
	protected abstract boolean setPiece(int row, int col, byte pieceType);

/**
	 * Generic method to set this board to the passed board so that
	 * {@link #equals(CheckersBoard)} returns true
	 * 
	 * @param board
	 *            - the CheckersBoard this board is to be set to
	 */
	protected void setTo(CheckersBoard board) {
		for (int i = 0; i < 32; i++)
			this.setPiece(i, board.getPiece(i));
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();

		sb.append("---------------------------------\n");
		for (int i = 0; i < 32;) {
			// rightShiftedRow means that the squares that checkers
			// play on are on x indices 1,3,5,7 of the row
			boolean rightShiftedRow = i / 4 % 2 == 0;
			if (rightShiftedRow)
				sb.append("|   | ");
			else
				sb.append("| ");
			sb.append(getChar(getPiece(i++)));
			sb.append(" |   | ");
			sb.append(getChar(getPiece(i++)));
			sb.append(" |   | ");
			sb.append(getChar(getPiece(i++)));
			sb.append(" |   | ");
			sb.append(getChar(getPiece(i++)));
			if (!rightShiftedRow)
				sb.append(" |  ");
			sb.append(" |\n---------------------------------\n");
		}

		return sb.toString();
	}
}
