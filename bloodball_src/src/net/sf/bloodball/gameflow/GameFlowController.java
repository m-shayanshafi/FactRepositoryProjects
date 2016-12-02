package net.sf.bloodball.gameflow;

import de.vestrial.util.error.Ensuring;
import java.awt.Point;
import net.sf.bloodball.model.*;
import net.sf.bloodball.model.player.Player;

public class GameFlowController {

  private Player activePlayer = Player.NO_PLAYER;
  private State state;
  private Game game;

  public GameFlowController(Game game) {
    this.game = game;
  }

  public Game getGame() {
    return game;
  }

  public State getState() {
    return state;
  }

  public void initialize() {
    setState(new SetupTeamState(this));
  }

  public void setState(State state) {
    this.state = state;
    state.init();
  }
  public void deactivatePlayer() {
    if (activePlayer == Player.NO_PLAYER) {
      return;
    }
    Point position = getActivePlayerPosition();
    if (activePlayer.hasMoved()) {
      activePlayer.endTurn();
    }
    activePlayer = Player.NO_PLAYER;
    if (position != null) {
      Notifier.fireSquareChangedEvent(position);
    }
  }

  public Player getActivePlayer() {
    return activePlayer;
  }

  public Point getActivePlayerPosition() {
    Ensuring.state(isPlayerActive(), "No player active");
    return getGame().getField().getPlayerPosition(activePlayer);
  }

  public boolean isNeighborSquareOfActivePlayer(Point position) {
    return Field.neighborSquares(position, getActivePlayerPosition());
  }

  public boolean isPlayerActive() {
    return activePlayer != Player.NO_PLAYER;
  }

  public void setActivePlayer(Player player) {
    Ensuring.state(getGame().getTeams().isActiveTeam(player.getTeam()), "Active player must be of activeTeam");
    activePlayer = player;
  }

}