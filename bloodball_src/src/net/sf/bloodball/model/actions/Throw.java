package net.sf.bloodball.model.actions;

import de.vestrial.util.awt.Points;
import java.awt.Point;
import java.util.*;
import net.sf.bloodball.gameflow.MoveActionState;
import net.sf.bloodball.model.*;
import net.sf.bloodball.model.player.*;
import net.sf.bloodball.util.Dices;

public class Throw extends MoveAction {

  private double INTERCEPTION_DISTANCE = 0.5;

  public Throw(Game game) {
    super(game);
  }

  private void executeThrowingRoll(Point throwerPosition, Point catcherPosition) {
    int catchLimit = 6 + (int) (Points.getDistance(throwerPosition, catcherPosition) / 4);
    int missLimit = 3 + (int) (Points.getDistance(throwerPosition, catcherPosition) / 4);
    rollThrowTable(throwerPosition, catcherPosition, missLimit, catchLimit);
  }

  public boolean isInReach(Point first, Point second) {
    return !areNeighborSquares(first, second) && Points.getDistance(first, second) <= 16;
  }

  public boolean isLegal(Point actorPosition, Point actionPosition) {
    Player thrower = getPlayerAt(actorPosition);
    Player catcher = getPlayerAt(actionPosition);
    return !thrower.hasMoved() && thrower.inBallPossession() && areCollegues(thrower, catcher) && !catcher.isProne() && isInReach(actorPosition, actionPosition);
  }

  public void perform(Point actorPosition, Point actionPosition) {
    ensureLegalPosition(actorPosition, actionPosition, "Ball cannot be thrown to " + actionPosition);
    executeThrowingRoll(actorPosition, actionPosition);
    game.startNewTurn();
  }

  private void rollThrowTable(Point throwerPosition, Point catcherPosition, int missLimit, int catchLimit) {
    Player thrower = getPlayerAt(throwerPosition);
    Player catcher = getPlayerAt(catcherPosition);
    int throwerSurrounders = game.getField().playersInTackleZoneCount(throwerPosition, game.getTeams().getOpponentTeam(thrower.getTeam()));
    int catcherSurrounders = game.getField().playersInTackleZoneCount(catcherPosition, game.getTeams().getOpponentTeam(catcher.getTeam()));
    int result = Dices.D6.roll(2) + thrower.getType().getThrowingSkill() + catcher.getType().getCoolness();
    result -= throwerSurrounders + catcherSurrounders;
    game.getField().getPlayer(game.getBall().getPosition()).dropBall();
    if (result < missLimit) {
      interceptBall(throwerPosition, catcherPosition);
    } else if (result < catchLimit) {
      missBall(catcherPosition);
    } else {
      catchBall(catcherPosition);
    }
  }

  private void catchBall(Point catcherPosition) {
    game.getBall().setPosition(catcherPosition);
    game.getField().getPlayer(catcherPosition).catchBall();
  }

  private void interceptBall(Point throwerPosition, Point catcherPosition) {
    Player thrower = getPlayerAt(throwerPosition);
    Point interceptionPosition = getInterceptionPosition(throwerPosition, catcherPosition);
    if (interceptionPosition != null) {
      game.getBall().setPosition(interceptionPosition);
      game.getField().getPlayer(interceptionPosition).catchBall();
    } else if (isOpponentTackleZone(thrower, throwerPosition)) {
      game.getBall().scatter();
    } else {
      missBall(catcherPosition);
    }
  }

  private void missBall(Point catcherPosition) {
    game.getBall().setPosition(catcherPosition);
    game.getBall().scatter();
  }

  private Point getInterceptionPosition(Point throwerPosition, Point catcherPosition) {
    Iterator interceptionPositions = getPossibleInterceptionPositions(throwerPosition, catcherPosition).iterator();
    if (!interceptionPositions.hasNext()) {
      return null;
    }
    Point interceptorPosition = (Point) interceptionPositions.next();
    double interceptorDistance = Points.getDistance(throwerPosition, interceptorPosition);
    while (interceptionPositions.hasNext()) {
      Point position = (Point) interceptionPositions.next();
      double distance = Points.getDistance(throwerPosition, position);
      if (distance < interceptorDistance) {
        interceptorPosition = position;
        interceptorDistance = distance;
      }
    }
    return interceptorPosition;
  }

  private List getPossibleInterceptionPositions(Point throwerPosition, Point catcherPosition) {
    List interceptionPositions = new ArrayList();
    for (int x = 0; x < FieldExtents.SIZE.width; x++) {
      for (int y = 0; y < FieldExtents.SIZE.height; y++) {
        Point position = new Point(x, y);
        Player player = game.getField().getPlayer(position);
        Team opponentTeam = game.getTeams().getInactiveTeam();
        if (!player.isProne() && opponentTeam.isMember(player) && isInterceptableAt(throwerPosition, position, catcherPosition)) {
          interceptionPositions.add(position);
        }
      }
    }
    return interceptionPositions;
  }

  private boolean isInterceptableAt(Point throwerPosition, Point interceptorPosition, Point catcherPosition) {
    double numerator = (throwerPosition.x - catcherPosition.x) * (interceptorPosition.y - catcherPosition.y);
    numerator -= (throwerPosition.y - catcherPosition.y) * (interceptorPosition.x - catcherPosition.x);
    numerator = Math.abs(numerator);
    double denominator = (catcherPosition.x - throwerPosition.x) * (catcherPosition.x - throwerPosition.x);
    denominator += (catcherPosition.y - throwerPosition.y) * (catcherPosition.y - throwerPosition.y);
    denominator = Math.sqrt(denominator);
    return numerator / denominator < INTERCEPTION_DISTANCE && inLineOfThrow(throwerPosition, interceptorPosition, catcherPosition);
  }

  private boolean inLineOfThrow(Point throwerPosition, Point interceptorPosition, Point catcherPosition) {
    double distance = Points.getDistance(throwerPosition, catcherPosition);
    double distanceToThrower = Points.getDistance(interceptorPosition, throwerPosition);
    double distanceToCatcher = Points.getDistance(interceptorPosition, catcherPosition);
    return distanceToThrower < distance && distanceToCatcher < distance;
  }
  public boolean endsTeamTurn() {
    return true;
  }

}