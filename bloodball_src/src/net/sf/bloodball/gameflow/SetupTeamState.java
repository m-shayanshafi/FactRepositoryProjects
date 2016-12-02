package net.sf.bloodball.gameflow;

import java.awt.Point;
import net.sf.bloodball.model.*;
import net.sf.bloodball.model.actions.Setup;
import net.sf.bloodball.model.player.*;
import net.sf.bloodball.model.player.Team;

public class SetupTeamState extends SetupState {

  public SetupTeamState(GameFlowController context) {
    super(context);
    setSetup(new Setup(context.getGame()));
  }

  public void init() {
    setEndTurnOperation(EndTurnOperation.FINISH_TEAM_SETUP);
    if (getGame().getTeams().getActiveTeam().getPlayersToSetupCount() == 0) {
      setMayEndTurn(true);
    } else {
      setMayEndTurn(false);
    }
    setInTurnOperation(InTurnOperation.SET_UP_TEAM);
  }

  public void performEndTurnOperation() {
    getSetup().teamFinished();
    if (getGame().getTeams().getActiveTeam() == getGame().getTeams().getOffensiveTeam()) {
      context.setState(new SetupBallState(context));
    } else {
      context.setState(new SetupTeamState(context));
    }
  }

  public void squareChoosen(Point position) {
    if (getSetup().removablePlayerAt(position)) {
      Team activeTeam = getGame().getTeams().getActiveTeam();
      int removablePlayerNumber = activeTeam.getPlayerNumber(getGame().getField().getPlayer(position));
      removePlayer(position);
      Notifier.fireSquareChangedEvent(position);
      Notifier.fireDugOutChangedEvent(activeTeam, removablePlayerNumber);
    }
  }

  public void dugOutChoosen(Team team, int playerNumber) {
    if (getSetup().isLegalSetupPlayer(team, playerNumber)) {
      context.setState(new SetupPlayerState(context, team, playerNumber));
    }
  }

}