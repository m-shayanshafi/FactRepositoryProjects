package net.sf.bloodball.model.actions;

import de.vestrial.util.error.Ensuring;
import java.awt.Point;
import net.sf.bloodball.model.Game;
import net.sf.bloodball.model.player.*;

public class Setup extends SetupAction {

  public Setup(Game game) {
    super(game);
  }

  public boolean isLegalBallSetup(Point position) {
    return (getGame().getTeams().getOffensiveTeam().isMember(getGame().getField().getPlayer(position)));
  }

  public boolean isLegalPlayerSetup(Point position) {
    return (
      getGame().getField().inSetupZone(position, getGame().getTeams().getActiveTeam())
        && isNotOccupied(position)
        && maySetup(getGame().getTeams().getActiveTeam()));
  }

  public boolean removablePlayerAt(Point position) {
    return getGame().getTeams().getActiveTeam().isMember(getGame().getField().getPlayer(position));
  }

  public void removePlayer(Point position) {
    Ensuring.parameter(removablePlayerAt(position), "No removable TeamPlayer at " + position);
    getGame().getField().removePlayer(position);
  }

  public void setupBall(Point position) {
    Ensuring.parameter(isLegalBallSetup(position), "No legal ball position: " + position);
    if (getGame().getBall().getPosition() != null) {
      getGame().getField().getPlayer(getGame().getBall().getPosition()).dropBall();
    }
    getGame().getBall().setPosition(position);
    getGame().getField().getPlayer(position).pickUpBall();
  }

  public void setupPlayer(Point position, Player setupPlayer) {
    Ensuring.parameter(isLegalPlayerSetup(position), position + " isn't a legal setup square.");
    getGame().getField().setPlayer(position, setupPlayer);
  }

  public void teamFinished() {
    getGame().startNewTurn();
  }

}