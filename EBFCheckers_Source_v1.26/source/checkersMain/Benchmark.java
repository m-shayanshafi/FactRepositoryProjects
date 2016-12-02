package checkersMain;

import java.lang.ref.SoftReference;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import checkersBoard.BitCheckersBoard;
import checkersBoard.DefaultCheckersBoard;

/**
 * A test to compare the time and space efficiencies of different
 * {@link CheckersBoard}s.
 * 
 * @author Amos Yuen
 * @version 1.00 - 3 August 2008
 */
public final class Benchmark {

	public static class TestPlayer {
		protected static int END_GAME_VALUE = 1000;

		protected static float ENEMY_FACTOR = 1.015f;
		protected static float KING_VALUE = 1.75f;
		protected static float MOVES_FACTOR = 0.045f;
		protected static float CHECKER_VALUE = 1.0f;

		protected float maxSearchDepth;
		protected float searchDepth;

		public TestPlayer() {
			searchDepth = 7;
			maxSearchDepth = 10;
		}

		protected float evaluateBoard(CheckersBoard board, boolean max,
				int minPlies, int maxPlies, int levels) {
			float player1Value = 0, player2Value = 0;

			for (int row = 0; row < 8; row++) {
				for (int col = 0; col < 8; col++) {
					byte contentsOfBoardSpace = board.getPiece(row, col);
					switch (contentsOfBoardSpace) {

					case CheckersBoard.PLAYER1_CHECKER:
						player1Value += CHECKER_VALUE;
						break;

					case CheckersBoard.PLAYER1_KING:
						player1Value += KING_VALUE;
						break;

					case CheckersBoard.PLAYER2_CHECKER:
						player2Value -= CHECKER_VALUE * ENEMY_FACTOR;
						break;

					case CheckersBoard.PLAYER2_KING:
						player2Value -= KING_VALUE * ENEMY_FACTOR;
						break;
					}
				}
			}

			if (player1Value == 0)
				return -END_GAME_VALUE + (maxPlies + minPlies);
			else if (player2Value == 0)
				return END_GAME_VALUE - (maxPlies + minPlies);

			float heuristicValue = player1Value + player2Value;

			int maxLevels = Math.max(1, levels / 2);
			int minLevels = Math.max(1, levels / 2);
			if (max)
				maxLevels++;
			heuristicValue += MOVES_FACTOR
					* ((float) maxPlies / maxLevels - (float) minPlies
							/ minLevels);

			return heuristicValue;
		}

		private float getMinimaxValue(CheckersBoard board, boolean max,
				float parentValue, int maxPlies, int minPlies,
				int searchedDepth, int remainingSearchDepth) {
			if (remainingSearchDepth == 0 || searchedDepth > maxSearchDepth) {
				if (max)
					board = board.invertCheckersBoard();
				float value = evaluateBoard(board, max, minPlies, maxPlies,
						searchedDepth);
				board.freeCache();
				return value;
			}
			board = board.invertCheckersBoard();
			int numSuccessors = board.getNumSuccessors();

			float value;
			if (numSuccessors == 0) {
				if (!max)
					board = board.invertCheckersBoard();
				value = evaluateBoard(board, max, minPlies, maxPlies,
						searchedDepth);
				board.freeCache();
				return value;
			} else if (numSuccessors <= 2)
				remainingSearchDepth++;

			if (max) {
				value = Float.NEGATIVE_INFINITY;
				maxPlies += numSuccessors;
			} else {
				value = Float.POSITIVE_INFINITY;
				minPlies += numSuccessors;
			}

			for (int i = 0; i < numSuccessors; i++) {
				CheckersBoard newBoard = board.getSuccessor(i);
				float childValue = getMinimaxValue(newBoard, !max, value,
						maxPlies, minPlies, searchedDepth + 1,
						remainingSearchDepth - 1);

				if (max) {
					value = Math.max(value, childValue);
					if (value > parentValue)
						break;
				} else {
					value = Math.min(value, childValue);
					if (value < parentValue)
						break;
				}
			}

			board.freeCache();
			return value;
		}

		public int search(CheckersBoard board) {
			int numSuccessors = board.getNumSuccessors();
			int change = -1;
			if (numSuccessors <= 2)
				change++;

			int index = 0;
			float maxValue = Float.NEGATIVE_INFINITY;

			for (int i = 0; i < numSuccessors; i++) {
				float value = getMinimaxValue(board.getSuccessor(i), false,
						maxValue, numSuccessors, 0, 1, (int) searchDepth
								+ change);
				if (value > maxValue) {
					maxValue = value;
					index = i;
				}
			}

			return index;
		}
	}

	public static void main(String[] args) {
		CheckersBoard[] boards = new CheckersBoard[] {
				new DefaultCheckersBoard(), new BitCheckersBoard() };

		timeEfficiencyBenchmark(boards);
		spaceEfficiencyBenchmark(boards);
	}

	/**
	 * A space efficiency test that sees how many {@link CheckersBoard}s of a
	 * certain type can be created before the JVM runs out of memory. The method
	 * utilises a {@link SoftReference} to check if it is running out of memory,
	 * as the Garbage Collector is guaranteed to garbage collect all
	 * {@link SoftReference}s before it throws an {@link OutOfMemoryError}.
	 * Sometimes the GarbageCollector may activate earlier than at the limit, so
	 * several trials are conducted.
	 * 
	 * @param boards
	 *            - {@link CheckersBoard}s to be tested
	 */
	public static void spaceEfficiencyBenchmark(CheckersBoard... boards) {
		System.out.println("Space Efficiency Benchmark");
		System.out.println("-------------------------");

		String[] boardNames = new String[boards.length];

		int longest = 0;
		for (int i = 0; i < boardNames.length; i++) {
			boardNames[i] = boards[i].getClass().getSimpleName();
			longest = Math.max(longest, boardNames[i].length());
		}
		for (int i = 0; i < boardNames.length; i++) {
			while (boardNames[i].length() < longest)
				boardNames[i] = " " + boardNames[i];
		}

		for (int i = 0; i < boards.length; i++) {
			for (int j = 0; j < 3; j++) {
				System.gc();

				SoftReference<CheckersBoard> softReference = new SoftReference<CheckersBoard>(
						boards[i].clone());
				List<CheckersBoard> list = new LinkedList<CheckersBoard>();

				int size = 0;
				while (true) {
					if (softReference.get() == null)
						break;
					CheckersBoard clone = boards[i].clone();
					if (softReference.get() == null)
						break;
					list.add(clone);
					clone = null;
					size = list.size();
					if (softReference.get() == null)
						break;
				}
				softReference = null;
				list.clear();
				System.gc();
				System.out.println(boardNames[i] + " Size: " + size);
			}
		}

		System.out.println("\n\n\n");
	}

	/**
	 * A time efficiency test. It calls different methods multiple times and
	 * records the time required for each method. Each test should take about
	 * 3-6 seconds per {@link CheckersBoard}, except for the Checkers Search
	 * test which should take 12 - 15 seconds per {@link CheckersBoard}.
	 * <p>
	 * <b>Tests:</b>
	 * <ul>
	 * <li>Getters
	 * <li>Setters
	 * <li>Inverting
	 * <li>Generating Plies
	 * <li>Generating Successors
	 * <li>Real Checkers search
	 * </ul>
	 * 
	 * @param boards
	 *            - {@link CheckersBoard}s to be tested
	 */
	public static void timeEfficiencyBenchmark(CheckersBoard... boards) {
		System.out.println("Time Efficiency Benchmark");
		System.out.println("-------------------------");

		CheckersBoard[] clones = new CheckersBoard[boards.length];
		String[] boardNames = new String[boards.length];
		long[] boardTimes = new long[boards.length];
		long oldTime;

		int longest = 0;
		for (int i = 0; i < boardNames.length; i++) {
			boardNames[i] = boards[i].getClass().getSimpleName();
			longest = Math.max(longest, boardNames[i].length());
		}
		for (int i = 0; i < boardNames.length; i++) {
			while (boardNames[i].length() < longest)
				boardNames[i] = " " + boardNames[i];
		}

		for (int i = 0; i < clones.length; i++)
			clones[i] = boards[i].clone();
		Arrays.fill(boardTimes, 0);
		System.gc();
		System.out.println("\nGetters Test");
		for (int i = 0; i < 1000000; i++) {
			for (int j = 0; j < clones.length; j++) {
				oldTime = System.currentTimeMillis();
				for (int k = 0; k < 32; k++)
					clones[j].getPiece(i);
				for (int r = 0; r < 8; r++)
					for (int c = 0; c < 8; c++)
						clones[j].getPiece(r, c);
				boardTimes[j] += System.currentTimeMillis() - oldTime;
			}
		}
		for (int i = 0; i < clones.length; i++)
			System.out.println(boardNames[i] + " Time:\t" + boardTimes[i]);

		for (int i = 0; i < clones.length; i++)
			clones[i] = boards[i].clone();
		Arrays.fill(boardTimes, 0);
		System.gc();
		System.out.println("\nSetters Test");
		for (int i = 0; i < 200000; i++) {
			for (int j = 0; j < clones.length; j++) {
				oldTime = System.currentTimeMillis();
				for (int r = 0; r < 8; r++) {
					for (int c = 0; c < 8; c++) {
						clones[j].setPiece(r, c, CheckersBoard.OFFBOARD);
						clones[j].setPiece(r, c, CheckersBoard.EMPTY);
						clones[j].setPiece(r, c, CheckersBoard.PLAYER1_CHECKER);
						clones[j].setPiece(r, c, CheckersBoard.PLAYER1_KING);
						clones[j].setPiece(r, c, CheckersBoard.PLAYER2_CHECKER);
						clones[j].setPiece(r, c, CheckersBoard.PLAYER2_KING);
					}
				}
				clones[j].freeCache();
				boardTimes[j] += System.currentTimeMillis() - oldTime;
			}
		}
		for (int i = 0; i < clones.length; i++)
			System.out.println(boardNames[i] + " Time:\t" + boardTimes[i]);

		for (int i = 0; i < clones.length; i++)
			clones[i] = boards[i].clone();
		Arrays.fill(boardTimes, 0);
		System.gc();
		System.out.println("\nInversion Test");
		for (int i = 0; i < 3000000; i++) {
			for (int j = 0; j < clones.length; j++) {
				oldTime = System.currentTimeMillis();
				clones[j].invertCheckersBoard();
				clones[j].freeCache();
				boardTimes[j] += System.currentTimeMillis() - oldTime;
			}
		}
		for (int i = 0; i < clones.length; i++)
			System.out.println(boardNames[i] + " Time:\t" + boardTimes[i]);

		for (int i = 0; i < clones.length; i++)
			clones[i] = boards[i].clone();
		Arrays.fill(boardTimes, 0);
		System.gc();
		System.out.println("\nPlies Generation Test");
		for (int i = 0; i < 1000000; i++) {
			for (int j = 0; j < clones.length; j++) {
				oldTime = System.currentTimeMillis();
				for (int k = 0; k < clones[j].getNumPlies(); k++)
					clones[j].getPly(k);
				clones[j].freeCache();
				boardTimes[j] += System.currentTimeMillis() - oldTime;
			}
		}
		for (int i = 0; i < clones.length; i++)
			System.out.println(boardNames[i] + " Time:\t" + boardTimes[i]);

		for (int i = 0; i < clones.length; i++)
			clones[i] = boards[i].clone();
		Arrays.fill(boardTimes, 0);
		System.gc();
		System.out.println("\nSuccessors Generation Test");
		for (int i = 0; i < 1000000; i++) {
			for (int j = 0; j < clones.length; j++) {
				oldTime = System.currentTimeMillis();
				for (int k = 0; k < clones[j].getNumSuccessors(); k++)
					clones[j].getSuccessor(k);
				clones[j].freeCache();
				boardTimes[j] += System.currentTimeMillis() - oldTime;
			}
		}
		for (int i = 0; i < clones.length; i++)
			System.out.println(boardNames[i] + " Time:\t" + boardTimes[i]);

		TestPlayer tester = new TestPlayer();
		for (int i = 0; i < clones.length; i++)
			clones[i] = boards[i].clone();
		Arrays.fill(boardTimes, 0);
		System.gc();
		System.out.println("\nCheckers Search Test");
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < clones.length; j++) {
				oldTime = System.currentTimeMillis();
				tester.search(clones[j]);
				clones[j].freeCache();
				boardTimes[j] += System.currentTimeMillis() - oldTime;
			}
		}
		for (int i = 0; i < clones.length; i++)
			System.out.println(boardNames[i] + " Time:\t" + boardTimes[i]);

		System.out.println("\n\n\n");
	}

	private Benchmark() {

	}
}
