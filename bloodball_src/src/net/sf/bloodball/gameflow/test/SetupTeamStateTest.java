package net.sf.bloodball.gameflow.test;

import net.sf.bloodball.gameflow.*;

public class SetupTeamStateTest extends ActionStateTest {
  public SetupTeamStateTest(String name) {
    super(name);
  }

  public State getActionState(GameFlowController context) {
    return new SetupTeamState(context);
  }

  public void testInit() {
    assertSame(EndTurnOperation.FINISH_TEAM_SETUP, state.getEndTurnOperation());
    assertSame(InTurnOperation.SET_UP_TEAM, state.getInTurnOperation());
    assertTrue(!state.mayEndTurn());
  }

  public void testValidSetup() {
  	controller.getState().dugOutChoosen(getHomeTeam(), 0);
    assertSetupPlayerState();
  }

  public void testOppenentSetup() {
    controller.getState().dugOutChoosen(getGuestTeam(), 0);
    assertSetupTeamState();
  }
  
  protected void assertSetupPlayerState() {
    assertTrue(controller.getState() instanceof SetupPlayerState);
  }

  protected void assertSetupTeamState() {
    assertTrue(controller.getState() instanceof SetupTeamState);
  }

  public void testEndTurnOperationActivated() {
    assertTrue(!controller.getState().mayEndTurn());
    setFullTeamOnField();
    controller.setState(getActionState(controller));
    assertTrue(controller.getState().mayEndTurn());
    controller.getState().dugOutChoosen(getHomeTeam(), 13);
    assertSetupTeamState();
  }

  public void testTeamToggle() {
    setFullTeamOnField();
    controller.getState().performEndTurnOperation();
    assertTrue(!getGame().getTeams().getActiveTeam().isHomeTeam());
    assertTrue(!controller.getState().mayEndTurn());
  }

  public void testEmptyDugOutChoosen() {
    setPlayerTo(getHomeTeam().getPlayerByNumber(0), squareFiveZero);
    controller.getState().dugOutChoosen(getHomeTeam(), 0);
    assertSetupTeamState();
  }

}