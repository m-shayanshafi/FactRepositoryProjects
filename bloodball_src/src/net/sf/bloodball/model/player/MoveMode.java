package net.sf.bloodball.model.player;

public abstract class MoveMode {

  private Player player;
  private int squaresMoved;

  public MoveMode(Player player) {
    this.player = player;
  }

  protected abstract int getMoveSquares();

  protected abstract MoveMode getSuccessor();

  public boolean hasMoved() {
    return true;
  }

  public boolean hasToStopMoving() {
    return false;
  }

  public int getSquaresToGo() {
    return getMoveSquares() - getSquaresMoved();
  }

  public void move() {
    squaresMoved++;
    if (getSquaresToGo() == 0) {
      player.setMoveMode(getSuccessor());
    }
  }

  protected Player getPlayer() {
    return player;
  }

  protected int getSquaresMoved() {
    return squaresMoved;
  }

}