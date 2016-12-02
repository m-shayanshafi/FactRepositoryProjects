package thaigo.state;

import java.awt.event.MouseEvent;

import thaigo.property.AbstractRuler;
import thaigo.property.Position;
import thaigo.view.GameUI;

/**
 * Main phase of the game.
 * Player can play if in this phase.
 *
 * @author TG_Dream_Team
 * @version 9/5/2013
 *
 */
public class MainPhase extends GamePhaseAbstract {

	/**
	 * Constructor to initialize <code>MainPhase</code> object. 
	 * @param ruler ruler of the game
	 * @param task timer task of the game
	 */
	public MainPhase(AbstractRuler ruler, UpdateTask task) {
		super(ruler, task);
	}

	/**
	 * @see thaigo.state.GamePhaseAbstract#updateFromTask()
	 */
	@Override
	public void updateFromTask() {
		if (time == 00) {
			task.setToEndPhase();
			gameUI.lose();
		}
		if (gameUI != null) {
			gameUI.setFoeTime(timePerTurn);
			gameUI.setYourTime(time--);
		}
	}

	/**
	 * @see thaigo.state.GamePhaseAbstract#GOPanelCommand(thaigo.property.Position, java.awt.event.MouseEvent)
	 */
	@Override
	public void GOPanelCommand(Position position, MouseEvent e) {
		checkarr = ruler.getCheckArr();
		if (checkarr[position.getX()][position.getY()] == 2) {
			ruler.setGOPanelPosition(position);
			if (!ruler.isPawnPosNull()){
				ruler.walking(e);
				if (ruler.isRightPosition()) {
					task.setToWaitingPhase();
					ruler.setRightPosition(false);
					clearPositionMemory();				
				}
			}
		}
	}

	/**
	 * @see thaigo.state.GamePhaseAbstract#PawnCommand(thaigo.property.Position)
	 */
	@Override
	public void PawnCommand(Position position) {
		ruler.setPawnPosition(position);
		ruler.showing();
	}

}


