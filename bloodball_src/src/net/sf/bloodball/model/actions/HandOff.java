package net.sf.bloodball.model.actions;

import java.awt.Point;
import net.sf.bloodball.gameflow.MoveActionState;
import net.sf.bloodball.model.Game;
import net.sf.bloodball.model.player.Player;

public class HandOff extends MoveAction {

  public HandOff(Game game) {
    super(game);
  }

  public boolean isLegal(Point actorPosition, Point actionPosition) {
    Player actor = getPlayerAt(actorPosition);
    Player reactor = getPlayerAt(actionPosition);
    return actor.inBallPossession()
      && !actorPosition.equals(actionPosition)
      && areNeighbors(actor, reactor)
      && areCollegues(actor, reactor)
      && !reactor.isProne();
  }

  public void perform(Point actorPosition, Point actionPosition) {
    Player actor = getPlayerAt(actorPosition);
    Player reactor = getPlayerAt(actionPosition);
    ensureLegalPosition(actorPosition, actionPosition, "Can not hand off ball to " + actionPosition);
    actor.dropBall();
    game.getBall().setPosition(actionPosition);
    reactor.catchBall();
    game.startNewTurn();
  }
  
  public boolean endsTeamTurn() {
  	return true;
  }
}