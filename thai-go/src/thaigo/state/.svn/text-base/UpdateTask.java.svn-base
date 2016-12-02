package thaigo.state;

import java.awt.event.MouseEvent;
import java.util.TimerTask;

import thaigo.property.AbstractRuler;
import thaigo.property.Position;
import thaigo.utility.PropertyManager;
import thaigo.view.GameUI;

/**
 * This is TimerTask of game phase.
 * @author SPYSERVER
 *
 */

public class UpdateTask extends TimerTask {

	/** Time per turn in second. */
	private static final int TIMEPERTURN = 60;

	/** This class object. */
	private static UpdateTask task;

	private static AbstractRuler ruler;
	private static GameUI gameUI;

	private static GamePhaseAbstract state;
	private static GamePhaseAbstract WAITING_PHASE;
	private static GamePhaseAbstract MAIN_PHASE;
	private static GamePhaseAbstract END_PHASE;

	private UpdateTask(AbstractRuler ruler) {
		this.ruler = ruler;
		WAITING_PHASE = new WaitingPhase(ruler, this);
		WAITING_PHASE.setTime(TIMEPERTURN);
		MAIN_PHASE = new MainPhase(ruler, this);
		MAIN_PHASE.setTime(TIMEPERTURN);
		END_PHASE = new MainPhase(ruler, this);
		END_PHASE.setTime(TIMEPERTURN);

		if (PropertyManager.getProperty("mode").equals("server"))
			state = MAIN_PHASE;
		else
			state = WAITING_PHASE;
	}

	/**
	 * Use singleton pattern instead of creating this object every time.
	 * @param ruler ruler of the game
	 * @return this class object
	 */
	public static UpdateTask getInstance(AbstractRuler ruler) {
		if (task == null)
			return new UpdateTask(ruler);
		return task;
	}

	/**
	 * @see java.util.TimerTask#run()
	 */
	@Override
	public void run() {
		state.updateFromTask();
	}

	/**
	 * @see thaigo.state.GamePhaseAbstract#GOPanelCommand(thaigo.property.Position, java.awt.event.MouseEvent)
	 */
	public void GOPanelCommand(Position position, MouseEvent e) {
		state.GOPanelCommand(position, e);
	}

	/**
	 * @see thaigo.state.GamePhaseAbstract#PawnCommand(thaigo.property.Position)
	 */
	public void PawnCommand(Position position) {
		state.PawnCommand(position);
	}

	/**
	 * Set state to WaitingPhase.
	 */
	public void setToWaitingPhase() {
		this.state = WAITING_PHASE;
		this.state.setTime(TIMEPERTURN);
	}

	/**
	 * Set state to MainPhase.
	 */
	public void setToMainPhase() {
		this.state = MAIN_PHASE;
		this.state.setTime(TIMEPERTURN);
	}
	
	/**
	 * Set state to MainPhase.
	 */
	public void setToEndPhase() {
		this.state = END_PHASE;
		gameUI.getRuler().stopTimer();
		this.state.setTime(TIMEPERTURN);
	}

	public static void setGameUI(GameUI ui) {
		gameUI = ui;
		MAIN_PHASE.setGameUI(gameUI);
		WAITING_PHASE.setGameUI(gameUI);
	}
}
