package thaigo.state;

import java.awt.event.MouseEvent;

import thaigo.property.AbstractRuler;
import thaigo.property.Position;
import thaigo.view.GameUI;

/**
 * Abstract class of game phases.
 * 
 * @author TG_Dream_Team
 * @version 9/5/2013
 *
 */
public abstract class GamePhaseAbstract {

	/** Time per turn in second. */
	protected static int timePerTurn;
	
	/** Running time. */
	protected int time;
	
	/** Ruler of game. */
	protected static AbstractRuler ruler;
	
	/** <code>GameUI</code> object. */
	protected static GameUI gameUI;
	
	/** TimerTask of game. */
	protected static UpdateTask task;

	/** Contains position of all <code>Pawn</code> object. */
	protected int[][] checkarr;
	
	/**
	 * Constructor of game phase class.
	 * @param ruler ruler of the game
	 * @param task timer task of the game
	 */
	public GamePhaseAbstract(AbstractRuler ruler, UpdateTask task) {
		this.ruler = ruler;
		this.task = task;
	}
	
	/**
	 * This will be invoke all time from UpdateTask.
	 */
	public abstract void updateFromTask();
	
	/**
	 * Perform when the right position <code>GOPanel</code> is clicked.
	 * @param position position of clicked <code>GOPanel</code> object
	 * @param e mouse event
	 */
	public abstract void GOPanelCommand(Position position, MouseEvent e);
	
	/**
	 * Perform when the right owner <code>Pawn</code> is clicked.
	 * @param position position of clicked <code>Pawn</code> object
	 */
	public abstract void PawnCommand(Position position);
	
	/**
	 * Clear temporary position of clicked <code>Pawn</code> and <code>GOPanel</code> object
	 * in ruler object.
	 */
	public void clearPositionMemory() {
		ruler.resetAllPosition();
	}
	
	/** Reset running time. */
	public void setTime(int timePerTurn) {
		this.timePerTurn = timePerTurn;
		time = timePerTurn;
	}
	
	/** Set <code>GameUI</code> object. */
	public void setGameUI(GameUI gameUI) {
		this.gameUI = gameUI;
	}
}
