package checkersMain;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import checkersBoard.DefaultCheckersBoard;
import checkersMain.CheckersBoard.Ply;
import checkersMain.CheckersGameListener.CheckersGameEvent;
import checkersMain.CheckersPlayerInterface.CheckersPlayerEvent;
import checkersPlayer.Human;

/**
 * A Checkers game simulator that manages a game of checkers between two
 * {@link CheckersPlayerInterface}s. It uses multi-threading and supports
 * pauseing. It also can add and remove {@link CheckersGameListener}, which it
 * will notify of appropriate events. It allows the user to change several
 * variables:
 * 
 * <ul>
 * <li> {@link #player1}
 * <li> {@link #player2}
 * <li> {@link #maxMoves}
 * <li>
 * {@link #plyTime}
 * <li> {@link #waitTime}
 * </ul>
 * <p>
 * <b>Definitions:</b> <br>
 * Ply: A player's turn/move. <br>
 * Move: A round of plies; all players' plies.
 * 
 * @author Jeremy Hoffman (v1.00)
 * @author Amos Yuen
 * @version 1.24 - 6 November 2009
 */
public final class CheckersGameManager {

	/**
	 * A Thread class for a Checkers game.
	 * 
	 * @author Amos Yuen
	 * @version 1.11 - 6 November 2009
	 */
	private class GameThread extends Thread {
		public GameThread() {
			super();
			setDaemon(true);
		}

		/**
		 * Handles the taking of a Player's ply and time notification and
		 * restraints.
		 * 
		 * @param player
		 *            theCurrentPlayer
		 * @return whether this method successfully took a ply
		 */
		@SuppressWarnings("deprecation")
		public boolean playerPly(CheckersPlayerInterface player) {
			playerMoveIndex = -1;

			long time = System.currentTimeMillis();
			elapsedTime += time - currTime;
			currTime = time;
			remainingTime = plyTime;

			/*
			 * Start a PlayerPlyThread if not paused, otherwise when the
			 * GameCheckerager is unpaused, the setPaused(boolean paused) method
			 * will create a PlayerPlyThread.
			 */
			if (!paused) {
				playerPlyThread = new PlayerPlyThread(player);
				playerPlyThread.start();
			}
			while (playerMoveIndex == -1 && remainingTime > 0) {
				// Update every SLEEP_TIME
				try {
					Thread.sleep(SLEEP_TIME);
				} catch (InterruptedException e) {
					return false;
				}
				time = System.currentTimeMillis();
				if (!paused) {
					/*
					 * If unpaused, update the remainingTime and elapsedTime and
					 * notify the current player of the remainingTime for the
					 * current player's ply. Notify the listeners that the
					 * remainingTime has changed.
					 */
					cachedThreadPool.execute(new PlayerTask(player,
							new CheckersPlayerEvent(board, GAME_IN_PROGRESS,
									maxMoves, moveCount, remainingTime),
							PlayerTask.TASK_REMAINING_TIME));
					CheckersGameEvent ce = new CheckersGameEvent(
							CheckersGameManager.this);
					for (CheckersGameListener listener : listeners) {
						cachedThreadPool.execute(new ListenerTask(listener, ce,
								ListenerTask.TASK_PLY_TIME_CHANGED));
					}
					int diff = (int) (time - currTime);
					remainingTime -= diff;
					elapsedTime += diff;
				}
				currTime = time;
			}

			System.gc();
			if (waitTime > 0) {
				long passedTime = plyTime - remainingTime;
				try {
					Thread.sleep(Math.max(0, waitTime - passedTime));
				} catch (InterruptedException e) {
					return false;
				}
			}

			// Update Board
			// If AI did not return an index, set the index randomly
			int numPlies = board.getNumPlies();
			if (playerMoveIndex < 0 || playerMoveIndex >= numPlies) {
				System.out.println(player.getName() + " Invalid Index: "
						+ playerMoveIndex + "  Move randomly");
				playerMoveIndex = (int) (numPlies * Math.random());
			}

			// Update the current board and free the cache of the old board
			lastMove = board.getPly(playerMoveIndex);
			CheckersBoard oldBoard = board;
			board = board.getSuccessor(playerMoveIndex);
			oldBoard.freeCache();

			if (!player1Turn)
				lastMove = Ply.getInvertedPly(lastMove);

			/*
			 * If the playerPlyThread did not return an index, meaning the
			 * current player ran out of time, interrupt the thread.
			 */
			if (playerPlyThread != null) {
				playerPlyThread.stop();
				playerPlyThread = null;
			}
			// Changes the current player
			player1Turn = !player1Turn;

			return true;
		}

		/**
		 * The central method of the GameThread, it handles the simulation of a
		 * Checkers game.
		 */
		@Override
		public void run() {
			// Initialise variables
			displayBoard = board = new DefaultCheckersBoard();
			remainingTime = elapsedTime = 0;
			gameOutcome = GAME_IN_PROGRESS;
			player1Turn = true;
			currTime = System.currentTimeMillis();
			lastMove = null;
			moveCount = 0;

			CheckersPlayerEvent cpe = new CheckersPlayerEvent(board,
					GAME_IN_PROGRESS, maxMoves, moveCount, 0);
			cachedThreadPool.execute(new PlayerTask(player1, cpe,
					PlayerTask.TASK_GAME_STARTED));
			cachedThreadPool.execute(new PlayerTask(player2, cpe,
					PlayerTask.TASK_GAME_STARTED));

			// Notify listeners that the game has started
			CheckersGameEvent ce = new CheckersGameEvent(
					CheckersGameManager.this);
			for (CheckersGameListener listener : listeners) {
				cachedThreadPool.execute(new ListenerTask(listener, ce,
						ListenerTask.TASK_GAME_STARTED));
			}
			System.out.println("\nNew Checkers Game");

			// Main loop
			for (moveCount = 1;; moveCount++) {
				// If moveCount > MAX_MOVES, game is declared a draw.
				if (moveCount > maxMoves) {
					gameOutcome = DRAW;
					moveCount--;
					break;
				}

				// If Player1 has no moves available a, Player2 wins.
				if (board.getNumPlies() == 0) {
					gameOutcome = PLAYER2_WINS;
					break;
				}

				if (playerPly(player1)) {
					displayBoard = board;

					if (printBoard) {
						System.out.println("Player 1 moves...");
						System.out.println(board.toString());
					}

					// Flip the board so that PLAYER2 thinks that he is PLAYER1
					board = board.invertCheckersBoard();
					displayBoard.freeCache();

					// Notify listeners that a ply has been taken
					ce = new CheckersGameEvent(CheckersGameManager.this);
					for (CheckersGameListener listener : listeners) {
						cachedThreadPool.execute(new ListenerTask(listener, ce,
								ListenerTask.TASK_PLY_TAKEN));
					}
				} else
					break;

				// If PLAYER2 has no moves available, PLAYER1 wins
				if (board.getNumPlies() == 0) {
					gameOutcome = PLAYER1_WINS;
					board.freeCache();
					board = displayBoard;
					break;
				}

				if (playerPly(player2)) {
					// Flip the board so that PLAYER2 is back as PLAYER2
					displayBoard = board.invertCheckersBoard();
					board.freeCache();
					board = displayBoard;

					if (printBoard) {
						System.out.println("Player 2 moves...");
						System.out.println(board.toString());
					}
					// Notify listeners that a ply has been taken
					ce = new CheckersGameEvent(CheckersGameManager.this);
					for (CheckersGameListener listener : listeners) {
						cachedThreadPool.execute(new ListenerTask(listener, ce,
								ListenerTask.TASK_PLY_TAKEN));
					}
				} else
					break;
			}

			try {
				Thread.sleep(SLEEP_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (gameOutcome == GAME_IN_PROGRESS)
				gameOutcome = INTERRUPTED;
			if (playerPlyThread != null) {
				playerPlyThread.interrupt();
				playerPlyThread = null;
			}
			long time = System.currentTimeMillis();
			elapsedTime += time - currTime;
			currTime = time;
			remainingTime = 0;
			currGame = null;

			switch (gameOutcome) {
			case PLAYER1_WINS:
				System.out.println("~~~~~\n" + player1.getName()
						+ " wins against " + player2.getName() + " in "
						+ moveCount + " turns " + "!\n~~~~~");
				break;
			case PLAYER2_WINS:
				System.out.println("~~~~~\n" + player2.getName()
						+ " wins against " + player1.getName() + " in "
						+ moveCount + " turns " + "!\n~~~~~");
				break;
			case DRAW:
				System.out.println("~~~~~\n" + player1.getName() + " and "
						+ player2.getName() + " draw after " + moveCount
						+ " turns.\n~~~~~");
				break;
			default:
				System.out.println("Game Thread Interrupted");
			}

			cpe = new CheckersPlayerEvent(displayBoard, gameOutcome, maxMoves,
					moveCount, 0);
			cachedThreadPool.execute(new PlayerTask(player1, cpe,
					PlayerTask.TASK_GAME_ENDED));
			if (gameOutcome == PLAYER1_WINS)
				cpe = new CheckersPlayerEvent(displayBoard
						.invertCheckersBoard(), PLAYER2_WINS, maxMoves,
						moveCount, 0);
			else if (gameOutcome == PLAYER2_WINS)
				cpe = new CheckersPlayerEvent(displayBoard
						.invertCheckersBoard(), PLAYER1_WINS, maxMoves,
						moveCount, 0);
			else
				cpe = new CheckersPlayerEvent(displayBoard
						.invertCheckersBoard(), gameOutcome, maxMoves,
						moveCount, 0);
			cachedThreadPool.execute(new PlayerTask(player2, cpe,
					PlayerTask.TASK_GAME_ENDED));

			// Notify listeners that the game has ended
			ce = new CheckersGameEvent(CheckersGameManager.this);
			for (CheckersGameListener listener : listeners) {
				cachedThreadPool.execute(new ListenerTask(listener, ce,
						ListenerTask.TASK_GAME_ENDED));
			}
		}
	}

	private static class ListenerTask implements Runnable {
		public static final byte TASK_GAME_STARTED = 0;
		public static final byte TASK_GAME_ENDED = 1;
		public static final byte TASK_PAUSE_STATE_CHANGED = 2;
		public static final byte TASK_PLAYER1_CHANGED = 3;
		public static final byte TASK_PLAYER2_CHANGED = 4;
		public static final byte TASK_PLY_TAKEN = 5;
		public static final byte TASK_PLY_TIME_CHANGED = 6;

		protected final CheckersGameEvent ce;
		protected final CheckersGameListener listener;
		protected final byte task;

		private ListenerTask(CheckersGameListener listener,
				CheckersGameEvent ce, byte task) {
			super();
			this.listener = listener;
			this.ce = ce;
			this.task = task;
		}

		@Override
		public void run() {
			switch (task) {
			case (TASK_GAME_STARTED):
				listener.gameStarted(ce);
				break;
			case (TASK_GAME_ENDED):
				listener.gameEnded(ce);
				break;
			case (TASK_PAUSE_STATE_CHANGED):
				listener.pauseStateChanged(ce);
				break;
			case (TASK_PLAYER1_CHANGED):
				listener.player1Changed(ce);
				break;
			case (TASK_PLAYER2_CHANGED):
				listener.player2Changed(ce);
				break;
			case (TASK_PLY_TAKEN):
				listener.plyTaken(ce);
				break;
			case (TASK_PLY_TIME_CHANGED):
				listener.plyTimeChanged(ce);
				break;
			}
		}

	}

	private class PlayerPlyThread extends Thread {
		private CheckersPlayerInterface player;

		public PlayerPlyThread(CheckersPlayerInterface player) {
			super();
			this.player = player;
		}

		@Override
		@SuppressWarnings("deprecation")
		public void interrupt() {
			player.remainingTimeChanged(new CheckersPlayerEvent(board,
					gameOutcome, maxMoves, moveCount, 0));

			if (playerPlyThread.isAlive())
				stop();
		}

		@Override
		public void run() {
			int index = player.choosePlyIndex(new CheckersPlayerEvent(board,
					gameOutcome, maxMoves, moveCount, plyTime));
			playerMoveIndex = index;
		}
	}

	private static class PlayerTask implements Runnable {
		public static final byte TASK_GAME_STARTED = 0;
		public static final byte TASK_GAME_ENDED = 1;
		public static final byte TASK_REMAINING_TIME = 2;

		protected final CheckersPlayerEvent cpe;
		protected final CheckersPlayerInterface player;
		protected final byte task;

		public PlayerTask(CheckersPlayerInterface player,
				CheckersPlayerEvent cpe, byte task) {
			this.player = player;
			this.cpe = cpe;
			this.task = task;
		}

		@Override
		public void run() {
			switch (task) {
			case (TASK_GAME_STARTED):
				player.gameStarted(cpe);
				break;
			case (TASK_GAME_ENDED):
				player.gameEnded(cpe);
				break;
			case (TASK_REMAINING_TIME):
				player.remainingTimeChanged(cpe);
				break;
			}
		}
	}

	private static ExecutorService cachedThreadPool = Executors
			.newCachedThreadPool();

	/**
	 * Checkers game outcome constant representing a draw.
	 */
	public static final int DRAW = 0;
	/**
	 * Checkers game outcome constant representing that the game is still in
	 * progress.
	 */
	public static final int GAME_IN_PROGRESS = 5;
	/**
	 * Checkers game outcome constant representing an interrupted game.
	 */
	public static final int INTERRUPTED = -5;

	/**
	 * Checkers game outcome constant representing a win by Player1.
	 */
	public static final int PLAYER1_WINS = 1;
	/**
	 * Checkers game outcome constant representing a win by Player2.
	 */
	public static final int PLAYER2_WINS = -1;
	private final static long SLEEP_TIME = 100;

	private CheckersBoard board;
	private GameThread currGame;

	private long currTime;
	/**
	 * The current board from the perspective of Player1. Meaning the board is
	 * not inverted.
	 */
	private CheckersBoard displayBoard;

	private int gameOutcome;
	/**
	 * The last move that was taken, from the perspective of Player 1. Meaning
	 * the ply indices will fit correctly with the display board.
	 */
	private Ply lastMove;
	private List<CheckersGameListener> listeners;
	private int maxMoves;
	private int moveCount;
	private boolean paused;
	private CheckersPlayerInterface player1, player2;

	private boolean player1Turn, printBoard;

	private int playerMoveIndex;

	private PlayerPlyThread playerPlyThread;
	private int plyTime, remainingTime, elapsedTime, waitTime;

	public CheckersGameManager() {
		// change these lines to change the default type of AI playing
		// e.g., CheckersPlayer player1 = new YourNewCheckersPlayer();
		listeners = new LinkedList<CheckersGameListener>();
		player1 = new Human();
		player2 = new Human();
		plyTime = 30000;
		maxMoves = 75;

		displayBoard = board = new DefaultCheckersBoard();
		player1Turn = true;
		gameOutcome = INTERRUPTED;
	}

	/**
	 * Registers a CheckersGameListener.
	 * 
	 * @param listener
	 *            - the CheckersGameListener to be registered
	 */
	public void addCheckersGameListener(CheckersGameListener listener) {
		listeners.add(listener);
	}

	/**
	 * Returns the current board. This will be inverted on Player2's ply.
	 * 
	 * @return the current board
	 */
	public CheckersBoard getBoard() {
		return board;
	}

	/**
	 * Returns an array of the registered CheckersGameListeners.
	 * 
	 * @return an array of the registered CheckersGameListeners
	 */
	public CheckersGameListener[] getCheckersGameListeners() {
		return listeners.toArray(new CheckersGameListener[listeners.size()]);
	}

	/**
	 * Returns the current CheckersPlayerInterface that is choosing its ply.
	 * 
	 * @return the current CheckersPlayerInterface that is choosing its ply
	 */
	public CheckersPlayerInterface getCurrentPlayer() {
		return (isPlayer1Turn()) ? getPlayer1() : getPlayer2();
	}

	/**
	 * Returns the board from Player1's perspective. Meaning the board is not
	 * inverted.
	 * 
	 * @return the board from Player1's perspective
	 */
	public CheckersBoard getDisplayBoard() {
		return displayBoard;
	}

	/**
	 * Returns the total time that has elapsed for the current game.
	 * 
	 * @return the total time that has elapsed for the current game
	 */
	public int getElapsedTime() {
		return elapsedTime;
	}

	/**
	 * Returns the game outcome.
	 * 
	 * @return one of CheckersGameManager.GAME_IN_PROGRESS,
	 *         CheckersGameManager.INTERRUPTED, CheckersGameManager.DRAW,
	 *         CheckersGameManager.PLAYER1_WINS,
	 *         CheckersGameManager.PLAYER2_WINS
	 */
	public int getGameOutcome() {
		return gameOutcome;
	}

	/**
	 * Returns the last ply that was taken.
	 * 
	 * @return the last ply that was taken
	 */
	public Ply getLastPly() {
		return lastMove;
	}

	/**
	 * Returns the maximum number of moves a Checkers game will last before it
	 * is declared a draw.
	 * 
	 * @return the maximum number of moves a Checkers game will last
	 */
	public int getMaxMoves() {
		return maxMoves;
	}

	/**
	 * Returns the current move count of the current game.
	 * 
	 * @return the current move count of the current game
	 */
	public int getMoveCount() {
		return moveCount;
	}

	/**
	 * Returns the current CheckersPlayerInterface that is Player1.
	 * 
	 * @return the current CheckersPlayerInterface that is Player1
	 */
	public CheckersPlayerInterface getPlayer1() {
		return player1;
	}

	/**
	 * Returns the current CheckersPlayerInterface that is Player2.
	 * 
	 * @return the current CheckersPlayerInterface that is Player2
	 */
	public CheckersPlayerInterface getPlayer2() {
		return player2;
	}

	/**
	 * Returns the maximum time in milliseconds each player gets to choose its
	 * ply.
	 * 
	 * @return the maximum time in milliseconds each player gets to choose its
	 *         ply
	 */
	public int getPlyTime() {
		return plyTime;
	}

	/**
	 * Returns the remaining time in milliseconds for the current player to
	 * choose its ply.
	 * 
	 * @return the remaining time in milliseconds for the current player to
	 *         choose its ply
	 */
	public int getRemainingPlyTime() {
		return remainingTime;
	}

	/**
	 * Returns the time in milliseconds after each ply that the
	 * CheckersGameManager will wait.
	 * 
	 * @return the time in milliseconds after each ply that the
	 *         CheckersGameManager will wait
	 */
	public int getWaitTime() {
		return waitTime;
	}

	/**
	 * Returns whether there is a Checkers game in progress.
	 * 
	 * @return whether there is a Checkers game in progress
	 */
	public boolean hasGameRunning() {
		return gameOutcome == GAME_IN_PROGRESS;
	}

	/**
	 * Returns whether this CheckersGameManager is currently paused.
	 * 
	 * @return whether this CheckersGameManager is currently paused
	 */
	public boolean isPaused() {
		return paused;
	}

	/**
	 * Returns whether it is Player1's turn.
	 * 
	 * @return whether it is Player1's turn
	 */
	public boolean isPlayer1Turn() {
		return player1Turn;
	}

	/**
	 * Returns whether it is Player2's turn.
	 * 
	 * @return whether it is Player2's turn
	 */
	public boolean isPlayer2Turn() {
		return !player1Turn;
	}

	/**
	 * Returns whether this CheckersGameManager is printing out the display
	 * board to the console after each ply.
	 * 
	 * @return whether this CheckersGameManager is printing out the display
	 *         board to the console after each ply
	 */
	public boolean isPrintingBoard() {
		return printBoard;
	}

	/**
	 * Starts a new Checkers game on a new thread. If there is a game in
	 * progress, it will be interrupted first.
	 */
	public synchronized void newGame() {
		stop();
		paused = false;
		currGame = new GameThread();
		currGame.start();
	}

	/**
	 * Unregisters the passed CheckersGameListener.
	 * 
	 * @param listener
	 *            - The CheckersGameListener to be unregistered
	 */
	public void removeCheckersGameListener(CheckersGameListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Sets the maximum number of moves a Checkers game will last before it is
	 * declared a draw.
	 * 
	 * @param maxMoves
	 *            - The maximum number of moves a Checkers game will last
	 */
	public void setMaxMoves(int maxMoves) {
		this.maxMoves = Math.max(1, maxMoves);
	}

	/**
	 * Sets whether this CheckersGameManager is paused.
	 * 
	 * @param paused
	 *            - If true, pauses the current Checkers game if any. If false,
	 *            unpauses the current Checkers game if any.
	 */
	public synchronized void setPaused(boolean paused) {
		if (paused == this.paused)
			return;
		if (currGame == null) {
			paused = false;
			return;
		}

		this.paused = paused;
		if (paused) {
			// If pausing, stop PlayerPlyThread
			if (playerPlyThread != null) {
				playerPlyThread.interrupt();
				playerPlyThread = null;
			}
		} else {
			// If player is AI, reset turn time.
			if (!(getCurrentPlayer() instanceof Human))
				remainingTime = plyTime;

			// Create new PlayerPlyThread
			playerPlyThread = new PlayerPlyThread(getCurrentPlayer());
			playerPlyThread.start();
		}

		// Notify listeners that the pause state has been changed
		CheckersGameEvent ce = new CheckersGameEvent(CheckersGameManager.this);
		for (CheckersGameListener listener : listeners) {
			cachedThreadPool.execute(new ListenerTask(listener, ce,
					ListenerTask.TASK_PAUSE_STATE_CHANGED));
		}
	}

	/**
	 * Sets the CheckersPlayerInterface to be used for Player1.
	 * 
	 * @param player
	 *            - the CheckersPlayerInterface to be used for Player1
	 */
	public void setPlayer1(CheckersPlayerInterface player) {
		if (player == null || player == player1)
			return;
		player1 = player;
		if (currGame != null) {
			cachedThreadPool.execute(new PlayerTask(player,
					new CheckersPlayerEvent(displayBoard, GAME_IN_PROGRESS,
							maxMoves, moveCount, 0),
					PlayerTask.TASK_GAME_STARTED));

			try {
				Thread.sleep(SLEEP_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		// Notify listeners that the Player1 has been changed
		CheckersGameEvent ce = new CheckersGameEvent(CheckersGameManager.this);
		for (CheckersGameListener listener : listeners) {
			cachedThreadPool.execute(new ListenerTask(listener, ce,
					ListenerTask.TASK_PLAYER1_CHANGED));
		}
	}

	/**
	 * Sets the CheckersPlayerInterface to be used for Player2.
	 * 
	 * @param player
	 *            - the CheckersPlayerInterface to be used for Player2
	 */
	public void setPlayer2(CheckersPlayerInterface player) {
		if (player == null)
			return;
		player2 = player;
		if (currGame != null) {
			cachedThreadPool.execute(new PlayerTask(player,
					new CheckersPlayerEvent(displayBoard.invertCheckersBoard(),
							GAME_IN_PROGRESS, maxMoves, moveCount, 0),
					PlayerTask.TASK_GAME_STARTED));

			try {
				Thread.sleep(SLEEP_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		// Notify listeners that the Player2 has been changed
		CheckersGameEvent ce = new CheckersGameEvent(CheckersGameManager.this);
		for (CheckersGameListener listener : listeners) {
			cachedThreadPool.execute(new ListenerTask(listener, ce,
					ListenerTask.TASK_PLAYER2_CHANGED));
		}
	}

	/**
	 * Sets the maximum time in milliseconds each player gets to choose its ply.
	 * 
	 * @param plyTime
	 *            - the maximum time in milliseconds each player gets to choose
	 *            its ply.
	 */
	public void setPlyTime(int plyTime) {
		plyTime = Math.max(1000, plyTime);
		if (paused && getCurrentPlayer() instanceof Human)
			remainingTime = Math.min(plyTime, remainingTime + plyTime
					- this.plyTime);
		this.plyTime = plyTime;
	}

	/**
	 * Sets whether this CheckersGameManager should print out the display board
	 * to the console after each ply.
	 * 
	 * @param printBoard
	 *            - If true this CheckersGameManager will print out the display
	 *            board to the console after each ply
	 */
	public void setPrintBoard(boolean printBoard) {
		this.printBoard = printBoard;
	}

	/**
	 * Sets the time in milliseconds after each ply that the CheckersGameManager
	 * will wait.
	 * 
	 * @param waitTime
	 *            - the time in milliseconds after each ply that the
	 *            CheckersGameManager will wait
	 */
	public void setWaitTime(int waitTime) {
		this.waitTime = Math.max(0, waitTime);
	}

	/**
	 * Stops the current Checkers game if any.
	 */
	public synchronized void stop() {
		if (currGame != null) {
			currGame.interrupt();
			try {
				currGame.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}