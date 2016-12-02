package net.sf.bloodball.gameflow;

import java.awt.Point;
import net.sf.bloodball.model.*;
import net.sf.bloodball.model.player.Team;

public class SetupPlayerState extends SetupState {

  private int playerNumber;
  private Team team;

  public SetupPlayerState(GameFlowController context, Team team, int playerNumber) {
    super(context);
    this.playerNumber = playerNumber;
    this.team = team;
    Notifier.fireDugOutActivatedEvent(team, playerNumber);
  }

  public void init() {
    setEndTurnOperation(EndTurnOperation.FINISH_TEAM_SETUP);
    setMayEndTurn(false);
    setInTurnOperation(InTurnOperation.SET_UP_TEAM);
  }

  public void performEndTurnOperation() {
  }

  public void squareChoosen(Point position) {
    if (getSetup().isLegalPlayerSetup(position)) {
      putPlayer(position, team.getPlayerByNumber(playerNumber));
      inciteNewPlayerSelection();
      Notifier.fireSquareChangedEvent(position);
      Notifier.fireDugOutChangedEvent(team, playerNumber);
    }
  }

  public void deselectInTurnOperation() {
    inciteNewPlayerSelection();
  }

  private void inciteNewPlayerSelection() {
    context.setState(new SetupTeamState(context));
    Notifier.fireDugOutDeactivatedEvent(team, playerNumber);
  }

}