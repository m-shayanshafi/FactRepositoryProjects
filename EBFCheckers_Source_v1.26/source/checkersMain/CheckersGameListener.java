package checkersMain;

import java.util.EventObject;

import checkersMain.CheckersBoard.Ply;

/**
 * An interface that has methods the {@link CheckersGameManager} will call to
 * notify a listener of {@link CheckersGameEvent}s.
 * 
 * @author Amos Yuen
 * @version 1.00 - 3 August 2008
 */
public interface CheckersGameListener {

	/**
	 * A simple event class that just holds the source of the CheckersGameEvent.
	 * 
	 * @author Amos Yuen
	 * @version 1.00 - 3 August 2008
	 */
	@SuppressWarnings("serial")
	public static final class CheckersGameEvent extends EventObject {
		public CheckersGameEvent(CheckersGameManager cgCheckerager) {
			super(cgCheckerager);
		}

		@Override
		public CheckersGameManager getSource() {
			return (CheckersGameManager) super.getSource();
		}
	}

	/**
	 * Notifies the listener that the checkers game has ended.
	 * 
	 * @param ce
	 *            - the {@link CheckersGameEvent}
	 */
	public void gameEnded(CheckersGameEvent ce);

	/**
	 * Notifies the listener that the checkers game has started.
	 * 
	 * @param ce
	 *            - the {@link CheckersGameEvent}
	 */
	public void gameStarted(CheckersGameEvent ce);

	/**
	 * Notifies the listener that the pause state of the source has changed.
	 * 
	 * @param ce
	 *            - the {@link CheckersGameEvent}
	 */
	public void pauseStateChanged(CheckersGameEvent ce);

	/**
	 * Notifies the listener that the {@link CheckersPlayerInterface} for
	 * Player1 has changed.
	 * 
	 * @param ce
	 *            - the {@link CheckersGameEvent}
	 */
	public void player1Changed(CheckersGameEvent ce);

	/**
	 * Notifies the listener that the {@link CheckersPlayerInterface} for
	 * Player2 has changed.
	 * 
	 * @param ce
	 *            - the {@link CheckersGameEvent}
	 */
	public void player2Changed(CheckersGameEvent ce);

	/**
	 * Notifies the listener that a {@link Ply} has been taken.
	 * 
	 * @param ce
	 *            - the {@link CheckersGameEvent}
	 */
	public void plyTaken(CheckersGameEvent ce);

	/**
	 * Notifies the listener that the remaining time for the current
	 * {@link CheckersPlayerInterface} to take its ply has changed.
	 * 
	 * @param ce
	 *            - the {@link CheckersGameEvent}
	 */
	public void plyTimeChanged(CheckersGameEvent ce);
}
