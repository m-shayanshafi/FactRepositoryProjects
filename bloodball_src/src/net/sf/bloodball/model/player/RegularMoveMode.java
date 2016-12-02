package net.sf.bloodball.model.player;

import net.sf.bloodball.model.actions.Block;

public class RegularMoveMode extends MoveMode {

  public RegularMoveMode(Player player) {
    super(player);
  }

  protected int getMoveSquares() {
    return getPlayer().getType().getMoveAllowance();
  }

  public boolean hasMoved() {
  	return getSquaresMoved() > 0;
  }
  
  protected MoveMode getSuccessor() {
  	Player player = getPlayer();
  	if (new Block(player.getGame()).mayBlock(player)) {
  		return new NoMoveMode(player);
  	}
    return new SprintMoveMode(player);
  }

}