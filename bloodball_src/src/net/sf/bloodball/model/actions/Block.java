package net.sf.bloodball.model.actions;

import net.sf.bloodball.gameflow.MoveActionState;
import net.sf.bloodball.model.*;
import net.sf.bloodball.model.player.Health;
import net.sf.bloodball.model.player.Player;
import net.sf.bloodball.util.*;
import de.vestrial.util.*;
import java.awt.Point;

public class Block extends MoveAction {

  public Block(Game game) {
    super(game);
  }

  private void executeBlockingRoll(Player blocker, Player blocked) {
    if (blocked.isProne()) {
      rollProneBlockTable(blocker, blocked);
    } else {
      rollBlockTable(blocker, blocked);
    }
  }

  public boolean isLegal(Point blockerPosition, Point blockedPosition) {
    Player blocker = getPlayerAt(blockerPosition);
    Player blocked = getPlayerAt(blockedPosition);
    return areNeighbors(blocker, blocked) && areOpponents(blocker, blocked) && !blocker.hasActed();
  }

  public boolean mayBlock(Player blocker) {
    if (blocker == Player.NO_PLAYER || !blocker.isOnField() || blocker.hasActed()) {
      return false;
    }
    Point position = game.getField().getPlayerPosition(blocker);
    return game.getField().inTackleZone(position, game.getTeams().getOpponentTeam(blocker.getTeam()));
  }

  public void perform(Point actorPosition, Point destination) {
    Player blocker = getPlayerAt(actorPosition);
    Player blocked = getPlayerAt(destination);
    ensureLegalPosition(actorPosition, destination, "No blockable player at position: " + destination);
    executeBlockingRoll(blocker, blocked);
    blocker.endTurn();
  }

  private void rollBlockTable(Player blocker, Player blocked) {
    executeBlockTable(blocker, blocked, 0);
  }

  private void rollProneBlockTable(Player blocker, Player blocked) {
    executeBlockTable(blocker, blocked, 2);
  }

  private void executeBlockTable(Player blocker, Player blocked, int modifier) {
  	int result = Dices.D6.roll(2) + blocker.getType().getStrength() - blocker.getType().getStrength() + modifier;
    if (result <= 2) {
      blocker.injure(Health.STUNNED);
    } else if (result >= 12) {
      blocked.injure(Health.STUNNED);
    } else {
      switch (result) {
        case 3 :
        case 4 :
          blocker.knockOver();
          break;
        case 5 :
        case 6 :
        case 7 :
          break;
        case 8 :
          blocker.knockOver();
          blocked.knockOver();
          break;
        case 9 :
        case 10 :
        case 11 :
          blocked.knockOver();
      }
    }
  }
  
  public boolean endsTeamTurn() {
    return false;
  }

}