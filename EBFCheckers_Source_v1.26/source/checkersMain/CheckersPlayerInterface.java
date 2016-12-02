package checkersMain;

import javax.swing.JTextArea;

import checkersMain.CheckersBoard.Ply;

/**
 * This is a basic interface that every checkers player should implement. It is
 * used by the {@link CheckersGameManager} to simulate a game of Checkers
 * between checkers players.
 * <p>
 * Note: The CheckersPlayerInterface should have a constructor with no
 * parameters.
 * 
 * @author Jeremy Hoffman (v1.00)
 * @author Amos Yuen
 * @version 1.20 - 3 August 2008
 */
public interface CheckersPlayerInterface {

	/**
	 * A wrapper class that contains information the
	 * {@link CheckersPlayerInterface} needs without giving the
	 * {@link CheckersPlayerInterface} access to the {@link CheckersGameManager}
	 * .
	 * 
	 * @author Amos Yuen
	 * @version 1.00 - 3 August 2008
	 */
	public static final class CheckersPlayerEvent {
		/**
		 * The current {@link CheckersBoard} such that this
		 * {@link CheckersPlayerInterface} is Player1.
		 */
		public final CheckersBoard board;

		/**
		 * The {@link CheckersGameManager}'s current game outcome at the time of
		 * this {@link CheckersPlayerEvent} creation. It will be one of
		 * {@link CheckersGameManager#GAME_IN_PROGRESS},
		 * {@link CheckersGameManager#INTERRUPTED},
		 * {@link CheckersGameManager#DRAW},
		 * {@link CheckersGameManager#PLAYER1_WINS}, or
		 * {@link CheckersGameManager#PLAYER2_WINS}
		 */
		public final int gameOutcome;

		/**
		 * The maximum number of moves the Checkers game will last before it is
		 * declared a {@link CheckersGameManager#DRAW}.
		 */
		public final int maxMoves;

		/**
		 * The {@link CheckersGameManager}'s move count at the time of this
		 * {@link CheckersPlayerEvent} creation.
		 */
		public final int moveCount;

		/**
		 * The remaining ply time for the current
		 * {@link CheckersPlayerInterface} at the time of this
		 * {@link CheckersPlayerEvent} creation.
		 */
		public final int remainingPlyTime;

		public CheckersPlayerEvent(CheckersBoard board, int gameOutcome,
				int maxMoves, int moveCount, int remainingPlyTime) {
			this.board = board;
			this.gameOutcome = gameOutcome;
			this.maxMoves = maxMoves;
			this.moveCount = moveCount;
			this.remainingPlyTime = remainingPlyTime;
		}
	}

	/**
	 * Returns the ply index of this {@link CheckersPlayerInterface}'s choice
	 * that corresponds to the {@link CheckersBoard} and {@link Ply} returned
	 * from the methods {@link CheckersBoard#getSuccessor(int)} or
	 * {@link CheckersBoard#getPly(int)} respectively.
	 * <p>
	 * This {@link CheckersPlayerInterface} should assume that the current
	 * player is Player1, it is this {@link CheckersPlayerInterface} turn, and
	 * that Player1's checkers and kings move from the bottom-to-top.
	 * <p>
	 * Note: If this returns an index value outside the range from zero to
	 * {@link CheckersBoard#getNumSuccessors()}, the {@link CheckersGameManager}
	 * will choose a move at random.)
	 * 
	 * @see CheckersBoard#getPly(int)
	 * @see CheckersBoard#getSuccessor(int)
	 * @see CheckersBoard#getNumPlies()
	 * @see CheckersBoard#getNumSuccessors()
	 * @see CheckersBoard.Ply
	 * 
	 * @param cpe
	 *            - the {@link CheckersPlayerEvent} event
	 * @return the index this {@link CheckersPlayerInterface} chooses to take
	 */
	public int choosePlyIndex(CheckersPlayerEvent cpe);

	/**
	 * Notifies the {@link CheckersPlayerInterface} that the checkers game has
	 * ended.
	 * 
	 * @see CheckersGameManager#GAME_IN_PROGRESS
	 * @see CheckersGameManager#INTERRUPTED
	 * @see CheckersGameManager#DRAW
	 * @see CheckersGameManager#PLAYER1_WINS
	 * @see CheckersGameManager#PLAYER2_WINS
	 * 
	 * @param cpe
	 *            - the {@link CheckersPlayerEvent} event
	 */
	public void gameEnded(CheckersPlayerEvent cpe);

	/**
	 * Notifies {@link CheckersPlayerInterface} that it is beginning a new game.
	 * It is meant for AI's that need to set certain variables at the start of a
	 * game.
	 * 
	 * @param cpe
	 *            - the {@link CheckersPlayerEvent} event
	 */
	public void gameStarted(CheckersPlayerEvent cpe);

	/**
	 * Returns a description of this {@link CheckersPlayerInterface}. The text
	 * should be displayed in a {@link JTextArea} using line wrap.
	 * 
	 * @return a description of this {@link CheckersPlayerInterface}
	 */
	public String getDescription();

	/**
	 * Returns the display name of this {@link CheckersPlayerInterface}.
	 * 
	 * @return the display name of this {@link CheckersPlayerInterface}
	 */
	public String getName();

	/**
	 * Notifies this {@link CheckersPlayerInterface} on how much time is
	 * remaining for its {@link #choosePlyIndex(CheckersPlayerEvent)} method to
	 * run.
	 * <p>
	 * This method will be called constantly by the {@link CheckersGameManager},
	 * so the remaining time should probably be stored, and allow the
	 * {@link #choosePlyIndex(CheckersPlayerEvent)} method to check the
	 * remaining time to determine future actions.
	 * 
	 * @param cpe
	 *            - the {@link CheckersPlayerEvent} event
	 */
	public void remainingTimeChanged(CheckersPlayerEvent cpe);
}
