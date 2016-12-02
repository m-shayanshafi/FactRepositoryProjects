package net.sf.bloodball.model.player;

public class NoMoveMode extends MoveMode {

  public NoMoveMode(Player player) {
    super(player);
  }

  protected int getMoveSquares() {
    return 0;
  }

  protected MoveMode getSuccessor() {
    return this;
  }

  public boolean hasToStopMoving() {
    return true;
  }

}
