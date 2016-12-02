package net.sf.bloodball.test;
import java.awt.*;
import net.sf.bloodball.ModelFacade;
import net.sf.bloodball.gameflow.GameFlowController;
import net.sf.bloodball.model.Game;
import net.sf.bloodball.model.player.*;

public class DummyModelFacade implements ModelFacade {

	private Point ballPosition;
	private boolean guestTeamPlayer;
	private boolean homeTeamPlayer;
	private int injuredHomePlayers;
	private boolean pronePlayer;

	public Point getBallPosition() {
		return ballPosition;
	}

	public Team getHomeTeam() {
		return new Team(null, null, Color.BLACK);
	}

	public Team getGuestTeam() {
		return new Team(null, null, Color.BLACK);
	}

	public int getProneTurns(Point position) {
		return 0;
	}

	public Player getPlayerAt(Point position) {
		return Player.NO_PLAYER;
	}

	public boolean isGuestTeamPlayerAt(java.awt.Point square) {
		return guestTeamPlayer;
	}

	public boolean isHomeTeamPlayerAt(java.awt.Point square) {
		return homeTeamPlayer;
	}

	public boolean isPlayerActiveAt(java.awt.Point square) {
		return false;
	}

	public boolean isPlayerOffCallAt(java.awt.Point square) {
		return false;
	}

	public boolean isPronePlayerAt(Point position) {
		return pronePlayer;
	}

	public void setBallPosition(Point ballPosition) {
		this.ballPosition = ballPosition;
	}

	public void setController(GameFlowController controller) {
	}

	public void setGuestTeamPlayer(boolean guestTeamPlayer) {
		this.guestTeamPlayer = guestTeamPlayer;
	}

	public void setHomeTeamPlayer(boolean homeTeamPlayer) {
		this.homeTeamPlayer = homeTeamPlayer;
	}

	public void setInjuredHomeTeamPlayerCount(int count) {
		injuredHomePlayers = count;
	}
	
	public boolean isPlayerActive(Player player) {
		return false;
	}

	/**
	 *
	 * @param newPronePlayer boolean
	 */
	public void setPronePlayer(boolean newPronePlayer) {
		pronePlayer = newPronePlayer;
	}
}