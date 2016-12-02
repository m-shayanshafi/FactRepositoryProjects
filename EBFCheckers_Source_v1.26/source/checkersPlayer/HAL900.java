package checkersPlayer;

import java.util.LinkedList;
import java.util.List;

import checkersMain.CheckersBoard;
import checkersMain.CheckersPlayerInterface;

/**
 * The HAL900 is an advanced Artificial Intelligence for Checkers. It uses a
 * depth-limited search and a mini-max algorithm with alpha-beta pruning.
 * 
 * @author Amos Yuen and Louis Wang
 * @version {@value #VERSION}
 */

public class HAL900 implements CheckersPlayerInterface {
	public static final String VERSION = "1.15 - 16 August 2008";

	/**
	 * If ENEMY_FACTOR > 1, it makes this player more aggressive, if
	 * ENEMY_FACTOR < 1, it makes this player more passive.
	 */
	protected static final float ENEMY_FACTOR = 1.015f;
	/**
	 * The amount that the number of plies each player has affects the
	 * evaluation of the {@link CheckersBoard}.
	 */
	protected static final float PLIES_FACTOR = 0.060f;
	protected static final int END_GAME_VALUE = 1000;
	protected static final float DRAW_VALUE = -5f;
	protected static final float THRESHOLD = 0.001f;

	protected static final float CHECKER_VALUE = 1.0f;
	protected static final float KING_VALUE = 1.6f;
	protected static final float CHECKER_ROW_FACTOR = 0.05f;

	protected static final float TARGET_TIME_RATIO = 0.8f;
	protected static final int MIN_RETURN_gTIME = 1000;

	protected static final int MIN_SEARCH_DEPTH = 5;

	protected int nodesSearched;
	protected int branchingCount;
	protected int branchingFactor;
	protected float averageBranchingFactor;

	protected float nodesPerMillisecond = 100;
	protected int searchDepth;
	protected int maxSearchDepth;

	protected int remainingTime;
	protected int moveCount;

	protected int maxMoves;
	protected int[] reduceTimes;

	protected int reducedIndex;

	public HAL900() {
		reduceTimes = new int[] { 2500, 1750, 1000 };
	}

	/**
	 * Calculate the searchDepth and maxSearchDepth based on the
	 * averageBranchingFactor and nodesPerMillisecond.
	 */
	protected void calcSearchDepth() {
		/*
		 * Calculate the approximate number of nodes that this AI should be able
		 * to search using the remainingTime.
		 */
		int numSearchableNodes = (int) (remainingTime * TARGET_TIME_RATIO * nodesPerMillisecond);
		searchDepth = MIN_SEARCH_DEPTH;

		searchDepth = (int) ((float) Math.log(numSearchableNodes) / (float) Math
				.log(averageBranchingFactor));
		maxSearchDepth = searchDepth + 2;
	}

	@Override
	public int choosePlyIndex(CheckersPlayerEvent cpe) {
		// If there is only one move possible, return immediately
		if (cpe.board.getNumSuccessors() == 1)
			return 0;

		remainingTime = cpe.remainingPlyTime;
		moveCount = cpe.moveCount;
		maxMoves = cpe.maxMoves;
		calcSearchDepth();

		reducedIndex = 0;

		nodesSearched = 0;
		branchingFactor = 0;
		branchingCount = 0;

		/*
		 * System.out.println(getName() + " Ply - " +
		 * cpe.board.getNumSuccessors() + " Possible Plies");
		 * System.out.println("searchDepth: " + searchDepth +
		 * " maxSearchDepth: " + maxSearchDepth);
		 */

		int index = search(cpe.board);

		int timePassed = Math.max(1, cpe.remainingPlyTime - remainingTime);

		// Calculate number of nodes searched per millisecond
		// and average it with the old nodesPerMillisecond
		nodesPerMillisecond = (nodesPerMillisecond + (float) nodesSearched
				/ timePassed) / 2f;

		// Calculate the average branching factor
		// and average it with the old averageBranchingFactor
		averageBranchingFactor = (averageBranchingFactor + (float) branchingFactor
				/ branchingCount) / 2f;

		// System.out.println("Average Branching: " + averageBranchingFactor);
		// System.out.println("NodesSearched: " + nodesSearched);
		// System.out.println("NodesPerMillisecond: " + nodesPerMillisecond);
		/*
		 * int milliseconds = timePassed % 1000; int seconds = timePassed /
		 * 1000; System.out.println("Ply Time Taken: " + seconds + "." +
		 * milliseconds);
		 */

		return index;
	}

	/**
	 * Evaluates the value of the board in the perspective of Player1. It counts
	 * the number of pieces, each piece is assigned a value:
	 * <p>
	 * <ul>
	 * <li>{@link CheckersBoard#PLAYER1_CHECKER} = {@value #CHECKER_VALUE}
	 * <li>{@link CheckersBoard#PLAYER1_KING} = {@value #KING_VALUE}
	 * <li>{@link CheckersBoard#PLAYER2_CHECKER} = -{@value #CHECKER_VALUE}
	 * <li>{@link CheckersBoard#PLAYER2_KING} = -{@value #KING_VALUE}
	 * </ul>
	 * Also, checker's values increase as they reach the king row and the
	 * enemy's score is multiplied by {@value ENEMY_FACTOR}. And the number of
	 * possible plies that max (this player) and min (the opponent player) can
	 * take is used. This method considers that reducing the opponents possible
	 * plies and increasing this player's possible plies as favourable.
	 * 
	 * @param board
	 *            - the {@link CheckersBoard} to evaluate
	 * @param player1
	 *            - if true, it is Player1's ply (max); if false, it is
	 *            Player2's ply (min)
	 * @param player1Plies
	 *            - the number of possible Player1 (max) plies in total for this
	 *            branch of the search space
	 * @param player2Plies
	 *            - the number of possible Player2 (min) plies in total for this
	 *            branch of the search space
	 * @param depth
	 *            - the depth of this board in the search space form the current
	 *            board
	 * @return the mini-max value of the passed {@link CheckersBoard} (as a leaf
	 *         node)
	 */
	protected float evaluateBoard(CheckersBoard board, boolean player1,
			int player1Plies, int player2Plies, int depth) {
		if (!player1)
			board = board.invertCheckersBoard();

		float player1Value = 0, player2Value = 0;

		for (byte i = 0; i < 32; i++) {
			switch (board.getPiece(i)) {
			case CheckersBoard.PLAYER1_CHECKER:
				player1Value += CHECKER_VALUE + (7 - i / 4)
						* CHECKER_ROW_FACTOR;
				break;
			case CheckersBoard.PLAYER1_KING:
				player1Value += KING_VALUE;
				break;
			case CheckersBoard.PLAYER2_CHECKER:
				player2Value += CHECKER_VALUE + (7 - i / 4)
						* CHECKER_ROW_FACTOR;
				break;
			case CheckersBoard.PLAYER2_KING:
				player2Value += KING_VALUE;
				break;
			}
		}

		int player1PlyDepth = Math.max(1, depth / 2);
		int player2PlyDepth = Math.max(1, depth / 2);
		if (player1)
			player1PlyDepth++;

		float heuristicValue = player1Value - player2Value
				* ENEMY_FACTOR + PLIES_FACTOR
				* ((float) player1Plies / player1PlyDepth
						- (float) player2Plies / player2PlyDepth);

		return heuristicValue;
	}

	@Override
	public void gameEnded(CheckersPlayerEvent cpe) {
	}

	@Override
	public void gameStarted(CheckersPlayerEvent cpe) {
		branchingFactor = 6;
		branchingCount = 1;
	}

	@Override
	public String getDescription() {
		return "An advanced Checkers AI that uses a depth-limited"
				+ " search with a mini-max algorithm and alpha-beta"
				+ " pruning. It also uses a unique heuristic and modifies"
				+ " its search depth dynamically."
				+ "\n\nAuthor: Amos Yuen and Louis Wang\nVersion: " + VERSION;
	}

	/**
	 * Searches the possible plies for the passes board using a mini-max search
	 * algorithm with alpha-beta pruning.
	 * 
	 * @param board
	 *            - the {@link CheckersBoard} to be searched
	 * @param player1
	 *            - if true, it is Player1's ply (max); if false, it is
	 *            Player2's ply (min)
	 * @param parentValue
	 *            - the mini-max value of the parent board
	 * @param player1Plies
	 *            - the number of possible Player1 (max) plies in total for this
	 *            branch of the search space
	 * @param player2Plies
	 *            - the number of possible Player2 (min) plies in total for this
	 *            branch of the search space
	 * @param searchedDepth
	 *            - the depth that has been searched so far
	 * @param remainingSearchDepthModifer
	 *            - a modifier added to the searchDepth to determine the
	 *            remaining depth to search
	 * @return the mini-max value of this board
	 */
	protected float getMinimaxValue(CheckersBoard board, boolean player1,
			float parentValue, int player1Plies, int player2Plies,
			int searchedDepth, int remainingSearchDepthModifer) {
		/*
		 * System.out.println("searchedDepth: " + searchedDepth +
		 * " searchDepth: " + searchDepth);
		 */

		// Invert the board and update statistics.
		board = board.invertCheckersBoard();
		int numSuccessors = board.getNumSuccessors();
		branchingFactor += numSuccessors;
		branchingCount++;
		nodesSearched++;
		if (player1)
			player1Plies += numSuccessors;
		else
			player2Plies += numSuccessors;

		/*
		 * Evaluates and returns the board if this branch has reached its
		 * desired search depth, has reached the maximum search depth, or the
		 * next player has zero plies.
		 */
		if (searchDepth + remainingSearchDepthModifer <= 0
				|| searchedDepth >= maxSearchDepth || numSuccessors == 0) {

			// End game boards are assigned special values
			if (board.getNumSuccessors() == 0) {
				if (player1)
					return -END_GAME_VALUE + searchedDepth;
				else
					return END_GAME_VALUE - searchedDepth;
			}

			return evaluateBoard(board, player1, player1Plies, player2Plies,
					searchedDepth + 1);
		} else if (player1 && searchedDepth / 2 + moveCount > maxMoves)
			return DRAW_VALUE;

		float value;
		if (player1)
			value = Float.NEGATIVE_INFINITY;
		else
			value = Float.POSITIVE_INFINITY;

		// If there are 2 or less possible plies, search deeper.
		if (numSuccessors > 2)
			remainingSearchDepthModifer--;

		// Goes through the list of successors
		player1 = !player1;
		searchedDepth++;
		for (int i = 0; i < numSuccessors; i++) {
			float childValue = getMinimaxValue(board.getSuccessor(i), player1,
					value, player1Plies, player2Plies, searchedDepth,
					remainingSearchDepthModifer);
			/*
			 * Take the maximum or minimum value depending on if this is a
			 * Player1 (max) or Player2 (min) ply respectively. Also compares
			 * the value to the parent value for alpha-beta pruning. Note: that
			 * boolean 'player1' has been reversed before this loop.
			 */
			if (player1) {
				value = Math.min(value, childValue);
				if (value < parentValue)
					break;
			} else {
				value = Math.max(value, childValue);
				if (value > parentValue)
					break;
			}

			// Check if searchDepth has been reduced.
			if (searchDepth + remainingSearchDepthModifer <= 0
					|| searchedDepth >= maxSearchDepth)
				break;
		}

		// Free the cache
		board.freeCache();
		return value;
	}

	@Override
	public String getName() {
		return getClass().getSimpleName();
	}

	@Override
	public void remainingTimeChanged(CheckersPlayerEvent cpe) {
		this.remainingTime = cpe.remainingPlyTime;

		if (reducedIndex < reduceTimes.length
				&& remainingTime < reduceTimes[reducedIndex]) {

			reducedIndex++;
			if (reducedIndex == reduceTimes.length - 1)
				searchDepth = maxSearchDepth = 0;
			else {
				searchDepth--;
				maxSearchDepth--;
			}

			/*
			 * System.out.println("Search Depth Reduced to: " + searchDepth +
			 * " maxSearchDepth: " + maxSearchDepth);
			 */
		}
	}

	/**
	 * Starts the min-max search and returns the index of the ply with the best
	 * mini-max value.
	 * 
	 * @param board
	 *            - the {@link CheckersBoard} to be searched
	 * @return the index of the ply with the best mini-max value
	 */
	protected int search(CheckersBoard board) {
		int numSuccessors = board.getNumSuccessors();
		int change = -1;
		if (numSuccessors <= 2)
			change++;

		branchingFactor = numSuccessors;
		branchingCount = 1;

		List<Byte> indices = new LinkedList<Byte>();
		indices.add((byte) 0);
		float maxValue = Float.NEGATIVE_INFINITY;

		for (byte i = 0; i < numSuccessors && searchDepth > 0; i++) {
			float value = getMinimaxValue(board.getSuccessor(i), false,
					maxValue, numSuccessors, 0, 1, change);
			if (value > maxValue) {
				maxValue = value;
				indices.clear();
				indices.add(i);
			} else if (Math.abs(value - maxValue) < THRESHOLD)
				indices.add(i);
		}

		return indices.get((int) (Math.random() * indices.size()));
	}
}
