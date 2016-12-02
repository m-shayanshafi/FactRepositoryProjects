package net.sf.bloodball.model;

import java.awt.Point;
import java.util.EventListener;
import net.sf.bloodball.model.player.Team;

public interface ModelListener extends EventListener {
	void gameEnded();
	void touchdownScored();
	void dugOutPositionActivated(Team team, int playerNumber);
	void dugOutPositionDeactivated(Team team, int playerNumber);
	void dugOutContentChanged(Team team, int playerNumber);
	void squareContentChanged(Point position);
	void endTurnOperationChanged();
	void inTurnOperationChanged();
}