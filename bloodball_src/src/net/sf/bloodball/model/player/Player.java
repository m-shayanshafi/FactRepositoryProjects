package net.sf.bloodball.model.player;

import java.awt.Point;
import net.sf.bloodball.model.*;
import net.sf.bloodball.model.Game;
import net.sf.bloodball.util.Dices;

public class Player {

  public static final Player NO_PLAYER = new NoPlayer();
  private static final int extraSquares = 2;

  private final PlayerType type;

  private boolean hasActed = false;
  private boolean ballPosession = false;
  private int proneTurns = 0;
  private Game game;
  private MoveMode moveMode = new NoMoveMode(this);
  private Health health = Health.OKAY;

  public Player(Game game, PlayerType playerType) {
    super();
    this.game = game;
    this.type = playerType;
  }

  public void beginTurn() {
    moveMode = new RegularMoveMode(this);
    hasActed = false;
    if (isProne()) {
      proneTurns--;
    }
    fireSquareChangedEvent();
  }

  private void fireSquareChangedEvent() {
    if (isOnField()) {
      Notifier.fireSquareChangedEvent(getPosition());
    }
  }

  public void move() {
    moveMode.move();
  }

  private void checkScatter() {
    Point ballPosition = game.getBall().getPosition();
    if (isOnField() && getPosition().equals(ballPosition)) {
      ballPosession = false;
      game.getBall().scatter();
    }
  }

  public void endTurn() {
    hasActed = true;
  }

  public Point getPosition() {
    return game.getField().getPlayerPosition(this);
  }

  public int getRemainingMovePoints() {
    return moveMode.getSquaresToGo();
  }

  public boolean hasToStopMoving() {
    return moveMode.hasToStopMoving();
  }

  public int getProneTurns() {
    return proneTurns;
  }

  public Team getTeam() {
    if (game.getTeams().getHomeTeam().isMember(this)) {
      return game.getTeams().getHomeTeam();
    } else if (game.getTeams().getGuestTeam().isMember(this)) {
      return game.getTeams().getGuestTeam();
    } else {
      return Team.NO_TEAM;
    }
  }

  public boolean hasActed() {
    return hasActed;
  }

  public boolean hasMoved() {
    return moveMode.hasMoved();
  }

  public boolean inBallPossession() {
    return ballPosession;
  }

  public void injure(Health health) {
    this.health = health;
    proneTurns = 0;
    checkScatter();
    if (isOnField()) {
      Point position = getPosition();
      game.getField().removePlayer(getPosition());
      Notifier.fireSquareChangedEvent(position);
    }
    Notifier.fireDugOutChangedEvent(getTeam(), getTeam().getPlayerNumber(this));
  }

  public void pickUpBall() {
    catchBall();
    if (moveMode.hasMoved()) {
      moveMode = new SprintMoveMode(this);
    }
  }

  public void catchBall() {
    ballPosession = true;
    Notifier.fireSquareChangedEvent(getPosition());
  }

  public boolean isAtCall() {
    return (inBallPossession() && game.getField().inTackleZone(getPosition(), getTeam())) || !(hasToStopMoving() || isProne() || isInjured() || hasActed());
  }

  public boolean isInjured() {
    return health != Health.OKAY;
  }

  public boolean isOnField() {
    return getPosition() != null;
  }

  public boolean isProne() {
    return proneTurns > 0;
  }

  public boolean isReserve() {
    return !isInjured() && !isOnField();
  }

  public boolean inSprintMode() {
    return moveMode instanceof SprintMoveMode;
  }

  public boolean inExtraMoveMode() {
    return moveMode instanceof ExtraMoveMode;
  }

  public boolean inRegularMoveMode() {
    return moveMode instanceof RegularMoveMode;
  }

  public boolean inNoMoveMode() {
    return moveMode instanceof NoMoveMode;
  }

  public boolean mayPickUpBall() {
    return inRegularMoveMode() && !inBallPossession() && game.getBall().getPosition().equals(getPosition());
  }

  public void knockOver() {
    if (Dices.D6.roll(2) <= getType().getArmourValue()) {
      proneTurns = 2;
      checkScatter();
    } else {
      switch (Dices.D6.roll(2)) {
        case 7 :
        case 8 :
          injure(Health.KO);
          break;
        case 9 :
        case 10 :
        case 11 :
          injure(Health.INJURED);
          break;
        case 12 :
          injure(Health.DEAD);
          break;
        default :
          injure(Health.STUNNED);
      }
    }
  }

  public void recover() {
    health = health.getRecovery();
    proneTurns = 0;
    beginTurn();
  }

  public PlayerType getType() {
    return type;
  }

  public void dropBall() {
    ballPosession = false;
  }

  public void setMoveMode(MoveMode moveMode) {
    this.moveMode = moveMode;
  }

  public Game getGame() {
    return game;
  }

  public Health getHealth() {
    return health;
  }

}