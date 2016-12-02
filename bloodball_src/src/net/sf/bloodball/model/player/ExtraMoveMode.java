package net.sf.bloodball.model.player;

import net.sf.bloodball.util.Dices;

public class ExtraMoveMode extends MoveMode {

  public ExtraMoveMode(Player player) {
    super(player);
  }

  public void checkForInjury() {
    if (Dices.D6.roll() == 6) {
      getPlayer().injure(Health.INJURED);
    }
  }

  protected int getMoveSquares() {
    return 2;
  }

  protected MoveMode getSuccessor() {
    return new NoMoveMode(getPlayer());
  }

  public void move() {
    checkForInjury();
    super.move();
  }

}