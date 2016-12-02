package net.sf.bloodball;
import net.sf.bloodball.gameflow.GameFlowController;
import net.sf.bloodball.model.*;
import net.sf.bloodball.model.player.*;
import de.vestrial.util.error.Ensuring;
import java.awt.*;

public class ModelFacadeImplementation implements ModelFacade {
	private GameFlowController controller;

	public ModelFacadeImplementation(GameFlowController controller) {
		this.controller = controller;
	}
	
	private Game getGame() {
		return controller.getGame();
	}

	public Point getBallPosition() {
		return getGame().getBall().getPosition();
	}

	public Team getGuestTeam() {
		return getGame().getTeams().getGuestTeam();
	}

	public Team getHomeTeam() {
		return getGame().getTeams().getHomeTeam();
	}
	
	public Player getPlayerAt(Point position) {
		return getGame().getField().getPlayer(position);
	}

	public int getProneTurns(Point position) {
		return getPlayerAt(position).getProneTurns();
	}

	public boolean isGuestTeamPlayerAt(Point square) {
		return isPlayerAt(square) && getPlayerAt(square).getTeam() == getGuestTeam();
	}

	public boolean isHomeTeamPlayerAt(Point square) {
		return isPlayerAt(square) && getPlayerAt(square).getTeam() == getHomeTeam();
	}

	public boolean isPlayerActiveAt(Point square) {
		return controller.isPlayerActive() && square.equals(controller.getActivePlayerPosition());
	}

	private boolean isPlayerAt(Point position) {
		return getGame().getField().getPlayer(position) != Player.NO_PLAYER;
	}

	public boolean isPlayerOffCallAt(Point square) {
		return isPlayerAt(square) && getPlayerAt(square).hasActed();
	}

	public boolean isPronePlayerAt(Point position) {
		return getGame().getField().getPlayer(position).isProne();
	}

	public void setController(GameFlowController controller) {
		this.controller = controller;
	}
	
	public boolean isPlayerActive(Player player) {
		return player != Player.NO_PLAYER && controller.getActivePlayer() == player;
	}


}