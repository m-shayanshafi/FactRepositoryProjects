package checkersPlayer;

import checkersMain.CheckersBoard;
import checkersMain.CheckersPlayerInterface;

/**
 * Jeremy wrote this class to demonstrate a CheckersPlayer that is slightly more
 * complex than RandomCheckersPlayer.
 * 
 * Note that any CheckersPlayer should assume that it is playing as PLAYER1. its
 * opponent is PLAYER2. The player should assume it is sitting at the "bottom"
 * of the board (PLAYER1 checkers move "up" from higher-numbered rows to
 * lower-numbered rows, and PLAYER2 checkers move "down" from lower-numbered
 * rows to higher-numbered rows.)
 * 
 * Students could copy this class for their CheckersPlayer (just rename
 * "public class BasicCheckersPlayer" to "public class YourNameCheckersPlayer"
 * and save it in YourNameCheckersPlayer.java).
 * 
 * Possible directions for improving the Checkers AI: 1. Override
 * evaluateBoard() to be a better heuristic. 2. Modify takeTurn to expand more
 * of the game tree. (You can use CheckersBoard's method invertCheckersBoard()
 * to get the board from your opponent's perspective.) 3. Lots of room for
 * improvements: add member variables to track the number of turns, perform
 * certain openings, look for forced endgame victories, etc.
 * 
 * @author Jeremy Hoffman
 * @version 1.00 - 2 July 2008
 */
public class BasicAI implements CheckersPlayerInterface {

	/**
	 * in my heuristic function evaluateBoard, I treat a promoted king piece as
	 * worth three unpromoted checker pieces.
	 */
	protected double myCheckerValue = 1.0;
	protected double myKingValue = 2.0;

	/**
	 * See CheckersPlayer.java for a detailed description of takeTurn. Note:
	 * "@Override" confirms that takeTurn is supposed to be an implementsation
	 * of an abstract interface method.
	 */
	@Override
	public int choosePlyIndex(CheckersPlayerEvent cpe) {
		// evaluate heuristic value of each successor board to find the highest
		int numSuccessors = cpe.board.getNumSuccessors();
		double maxHeuristicValue = Integer.MIN_VALUE;
		int maxHeuristicIndex = -1;
		for (int i = 0; i < numSuccessors; i++) {
			double currHeuristicValue = evaluateBoard(cpe.board.getSuccessor(i));
			/*
			 * Store this value as maxHeuristicValue if it's the highest value
			 * seen so far, or, if it's equal to the highest seen so far,
			 * replace the old one with this one randomly with probability 1 /
			 * numSuccessors.
			 */
			if (currHeuristicValue > maxHeuristicValue
					|| (currHeuristicValue == maxHeuristicValue && Math
							.random() > 1f / numSuccessors)) {
				maxHeuristicValue = currHeuristicValue;
				maxHeuristicIndex = i;
			}
		}

		// return the highest-heuristic successor
		return maxHeuristicIndex;
	}

	/**
	 * Generate a heuristic value for how good theBoard is for red player.
	 * Jeremy's simple function here just compares the number of pieces each
	 * player has, with kings counting for two checkers.
	 */
	protected double evaluateBoard(CheckersBoard theBoard) {
		double heuristicValue = 0;
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				byte contentsOfBoardSpace = theBoard.getPiece(row, col);
				switch (contentsOfBoardSpace) {

				case CheckersBoard.PLAYER1_CHECKER:
					heuristicValue += myCheckerValue;
					break;

				case CheckersBoard.PLAYER1_KING:
					heuristicValue += myKingValue;
					break;

				case CheckersBoard.PLAYER2_CHECKER:
					heuristicValue -= myCheckerValue;
					break;

				case CheckersBoard.PLAYER2_KING:
					heuristicValue -= myKingValue;
					break;

				case CheckersBoard.OFFBOARD: // ignore the off-diagonal spaces
				case CheckersBoard.EMPTY: // ignore the empty spaces
					break;
				}
			}
		}
		return heuristicValue;
	}

	@Override
	public void gameEnded(CheckersPlayerEvent cpe) {
	}

	@Override
	public void gameStarted(CheckersPlayerEvent cpe) {
	}

	@Override
	public String getDescription() {
		return "A Basic AI that only looks one step into the future."
				+ "\n\nAuthor: Jeremy Hoffman"
				+ "\nVersion: 1.00 - 3 July 2008";
	}

	public String getName() {
		return "Basic AI";
	}

	@Override
	public void remainingTimeChanged(CheckersPlayerEvent cpe) {
	}
}
