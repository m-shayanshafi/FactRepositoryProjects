package net.sf.bloodball.model;

import de.vestrial.util.error.Ensuring;
import java.awt.Point;
import net.sf.bloodball.model.player.*;

public class Game {

	private GameComponents gameComponents;
	private int victoryTouchdowns = 2;

	public Game() {
		gameComponents = new GameComponents(this);
		getTeams().initializeBeginningTeam();
	}

	protected GameComponents getGameComponents() {
		return gameComponents;
	}

	public Ball getBall() {
		return gameComponents.getBall();
	}

	public Field getField() {
		return gameComponents.getField();
	}

	public Teams getTeams() {
		return gameComponents.getTeams();
	}

	public int getVictoryTouchdowns() {
		return victoryTouchdowns;
	}

	public void startNewRound(Team offensiveTeam) {
		getBall().remove();
		getField().resetSquares();
		getTeams().startNewRound(offensiveTeam);
		Notifier.fireEndTurnOperationChangedEvent();
		Notifier.fireInTurnOperationChangedEvent();
	}

	public void startNewTurn() {
		getTeams().startNewTurn();
	}
}