package checkersPlayer;

import java.util.HashMap;

import checkersMain.CheckersBoard;
import checkersMain.CheckersPlayerInterface;

public class ChinookJr implements CheckersPlayerInterface {
	public static final boolean DEBUG_MODE = false;

	public static final byte BOARD_SIZE = 8;
	public static final int BASIC_CHECKER_VALUE = 1;
	public static final int BASIC_KING_VALUE = 4;
	public static final int MINIMAX_SEARCH_DEPTH = 12;
	public static final byte MY_CHECKER = CheckersBoard.PLAYER1_CHECKER;
	public static final byte MY_KING = CheckersBoard.PLAYER1_KING;
	public static final byte HIS_CHECKER = CheckersBoard.PLAYER2_CHECKER;
	public static final byte HIS_KING = CheckersBoard.PLAYER2_KING;
	public static byte NUMBER_OF_ACTIVE_SQUARES = 32;

	public static void debugPrint(float foo) {
		if (ChinookJr.DEBUG_MODE) {
			System.out.println(Float.toString(foo));
		}
	}

	public static void debugPrint(int foo) {
		if (ChinookJr.DEBUG_MODE) {
			System.out.println(Integer.toString(foo));
		}
	}

	public static void debugPrint(Object foo) {
		if (ChinookJr.DEBUG_MODE) {
			System.out.println(foo);
		}
	}

	public HashMap<String, Float> scoreCache;

	public long HEUR_GEN_COUNT = 0;

	public ChinookJr() {
		scoreCache = new HashMap<String, Float>();
	}

	public int choosePlyIndex(CheckersPlayerEvent cpe) {
		int bestOption = 0;

		debugPrint("Analyzing top nodes");
		if (cpe.board.getNumSuccessors() > 1) {
			float bestScore = minimax(cpe.board.getSuccessor(0),
					MINIMAX_SEARCH_DEPTH);
			debugPrint("    The score of ply 0 is " + bestScore);
			System.gc();

			for (int i = 1; i < cpe.board.getNumSuccessors(); i++) {
				CheckersBoard currentBoard = cpe.board.getSuccessor(0);
				float currentScore = minimax(currentBoard,
						ChinookJr.MINIMAX_SEARCH_DEPTH);
				debugPrint("    The score of ply " + i + " is " + currentScore);

				if (currentScore > bestScore) {
					// debugPrint ("Possible move " + i + " has score of " +
					// currentScore);
					bestScore = currentScore;
					bestOption = i;
				}

				System.gc();
			}
		}

		// scoreCache.clear();
		debugPrint("HEUR_GEN_COUNT is " + HEUR_GEN_COUNT);
		HEUR_GEN_COUNT = 0;
		return bestOption;
	}

	public void gameEnded(CheckersPlayerEvent cpe) {

	}

	public void gameStarted(CheckersPlayerEvent cpe) {
		scoreCache.clear();
	}

	protected float generateSingleStateHeuristic(CheckersBoard board) {
		float score = (float) 1.0 * getPieceCountScore(board) + (float) 1
				/ (float) ChinookJr.BOARD_SIZE * getPiecePositionScore(board);
		board = null;
		return score;
	}

	public String getDescription() {
		return "A checkers AI that traverses the checkers game tree"
				+ " in search of the optimum move. It searches to an"
				+ " arbitrary, human-set depth (currently 12 plies)"
				+ " using the minimax search algorithm and a"
				+ " heuristic based on game piece positions and"
				+ " quantity. Features include alpha-beta pruning"
				+ " and minimax value-caching."
				+ "\n\nAuthor: Matt Millar\nVersion: 1.10 - 9 August 2008";
	}

	public String getName() {
		return "ChinookJr";
	}

	protected float getPieceCountScore(CheckersBoard board) {
		HEUR_GEN_COUNT++;
		float pieceScore = 0;
		for (int row = 0; row < BOARD_SIZE; row++) {
			for (int col = 0; col < BOARD_SIZE; col++) {
				if (board.getPiece(row, col) == MY_CHECKER) {
					pieceScore += ChinookJr.BASIC_CHECKER_VALUE;
				} else if (board.getPiece(row, col) == MY_KING) {
					pieceScore += ChinookJr.BASIC_KING_VALUE;
				} else if (board.getPiece(row, col) == HIS_CHECKER) {
					pieceScore -= ChinookJr.BASIC_CHECKER_VALUE;
				} else if (board.getPiece(row, col) == HIS_KING) {
					pieceScore -= ChinookJr.BASIC_KING_VALUE;
				}
			}
		}

		board.freeCache();
		board = null;
		return pieceScore;
	}

	protected float getPiecePositionScore(CheckersBoard board) {
		float positionScore = 0;

		for (int row = 0; row < ChinookJr.BOARD_SIZE; row++) {
			for (int col = 0; col < ChinookJr.BOARD_SIZE; col++) {
				byte piece = board.getPiece(row, col);

				if (piece == HIS_CHECKER) {
					positionScore -= (row + 1);
				} else if (piece == MY_CHECKER) {
					positionScore += BOARD_SIZE - (row + 1);
				}
			}
		}

		board = null;
		return positionScore;
	}

	protected String getStorableBoardString(CheckersBoard board) {
		String boardString = "";
		for (int i = 0; i < NUMBER_OF_ACTIVE_SQUARES; i++) {
			boardString = boardString.concat(Byte.toString(board.getPiece(i)));
		}

		return boardString;
	}

	public float minimax(CheckersBoard board, int depth) {
		return minimax(board, depth, Float.MIN_VALUE, Float.MIN_VALUE + 1);
	}

	protected float minimax(CheckersBoard board, int depth, float alpha,
			float beta) {
		float minimaxValue = 0;

		if (depth == 8) {
			System.gc();
		}

		if (board.getNumSuccessors() == 0 || depth == 0) {
			minimaxValue = generateSingleStateHeuristic(board);
			return minimaxValue;
		} else if (scoreCache.containsKey(getStorableBoardString(board))) {
			minimaxValue = scoreCache.get(getStorableBoardString(board));
			return minimaxValue;
		} else {
			for (byte i = 0; i < board.getNumSuccessors(); i++) {
				alpha = Math.max(alpha, minimax(board.getSuccessor(i)
						.invertCheckersBoard(), depth - 1, -1 * beta, -1
						* alpha));

				if (alpha > beta) {
					break;
				}
			}

			minimaxValue = alpha;
			if (!scoreCache.containsKey(getStorableBoardString(board))) {
				scoreCache.put(getStorableBoardString(board), minimaxValue);
			}
			board = null;
		}

		board = null;
		return minimaxValue;
	}

	public void remainingTimeChanged(CheckersPlayerEvent cpe) {

	}
}
