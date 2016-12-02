package thaigo.state;

import java.awt.event.MouseEvent;
import thaigo.property.AbstractRuler;
import thaigo.property.Position;

/**
 * Waiting phase of the game.
 * Player cannot play if in this phase.
 *
 * @author TG_Dream_Team
 * @version 9/5/2013
 *
 */
public class WaitingPhase extends GamePhaseAbstract {

	/**
	 * Constructor to initialize <code>WaitingPhase</code> object. 
	 * @param ruler ruler of the game
	 * @param task timer task of the game
	 */
	public WaitingPhase(AbstractRuler ruler, UpdateTask task) {
		super(ruler, task);
	}

	/**
	 * @see thaigo.state.GamePhaseAbstract#updateFromTask()
	 */
	@Override
	public void updateFromTask() {
		if (time == 00) {
			task.setToEndPhase();
			gameUI.win();
		}
		if (gameUI != null) {
			gameUI.setYourTime(timePerTurn);
			gameUI.setFoeTime(time--);
		}
	}

	/**
	 * @see thaigo.state.GamePhaseAbstract#GOPanelCommand(thaigo.property.Position, java.awt.event.MouseEvent)
	 */
	@Override
	public void GOPanelCommand(Position position, MouseEvent e) {
	}

	/**
	 * @see thaigo.state.GamePhaseAbstract#PawnCommand(thaigo.property.Position)
	 */
	@Override
	public void PawnCommand(Position position) {
	}
}
