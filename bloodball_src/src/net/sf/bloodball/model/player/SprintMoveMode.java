package net.sf.bloodball.model.player;

public class SprintMoveMode extends MoveMode {

  public SprintMoveMode(Player player) {
    super(player);
  }

  protected int getMoveSquares() {
    return getPlayer().getType().getSprintAllowance();
  }

  protected MoveMode getSuccessor() {
    return new ExtraMoveMode(getPlayer());
  }

}
