package net.sf.bloodball.model.actions;

import de.vestrial.util.error.Ensuring;
import java.awt.Point;
import net.sf.bloodball.gameflow.MoveActionState;
import net.sf.bloodball.model.Game;
import net.sf.bloodball.model.player.*;
import net.sf.bloodball.util.Dices;

public class Move extends MoveAction {

  public Move(Game game) {
    super(game);
  }

  public boolean isLegal(Point actorPosition, Point actionPosition) {
    Player actor = getPlayerAt(actorPosition);
    Ensuring.state(actorPosition != null, "Cannot move player to no position.");
    return !sprintsIntoTackleZone(actor, actionPosition) && !squareOccupied(actionPosition) && !actor.hasToStopMoving() && areNeighborSquares(actorPosition, actionPosition);
  }

  public boolean isMoveablePlayer(Point position) {
    return activeTeamPlayerAt(position) && game.getField().getPlayer(position).isAtCall();
  }

  private void movePlayer(Player actor, Point position) {
    game.getField().removePlayer(getPosition(actor));
    game.getField().setPlayer(position, actor);
    if (actor.inBallPossession()) {
      game.getBall().setPosition(position);
    }
    actor.move();
  }

  public void perform(Point actorPosition, Point actionPosition) {
    ensureLegalPosition(actorPosition, actionPosition, "No legal movement position: " + actionPosition);
    Player actor = getPlayerAt(actorPosition);
    movePlayer(actor, actionPosition);
    if (actor.inBallPossession()) {
      game.getBall().setPosition(actionPosition);
    }
    performTackle(actor, actorPosition, actionPosition);
  }

  private void performTackle(Player player, Point actorPosition, Point actionPosition) {
    boolean movesBetweenTackleZones = isOpponentTackleZone(player, actorPosition) && isOpponentTackleZone(player, actionPosition);
    if (movesBetweenTackleZones && Dices.D6.roll() > 4) {
      knockOver(actionPosition);
    }
  }

  private boolean squareOccupied(Point position) {
    return game.getField().getPlayer(position) != Player.NO_PLAYER;
  }
  
  private boolean sprintsIntoTackleZone(Player player, Point movePosition) {
  	return !player.inRegularMoveMode()  && game.getField().inTackleZone(movePosition, game.getTeams().getOpponentTeam(player.getTeam()));
  }

  public boolean endsTeamTurn() {
    return false;
  }
  
}