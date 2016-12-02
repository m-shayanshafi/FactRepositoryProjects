package net.sf.bloodball.gameflow.test;

import net.sf.bloodball.GameBoardController;
import net.sf.bloodball.gameflow.*;

public class TurnBeginSelectionStateTest extends ActionStateTest {

  public TurnBeginSelectionStateTest(String arg0) {
    super(arg0);
  }

  private void assertTurnBeginSelectionState() {
    assertTrue(controller.getState() instanceof TurnBeginSelectionState);
  }

  public State getActionState(GameFlowController context) {
    return new TurnBeginSelectionState(context);
  }

  public void testTooManySubstitutions() {
    setFullTeamOnField();
    controller.getState().dugOutChoosen(getHomeTeam(), 0);
    assertTurnBeginSelectionState();
  }

  public void testInjuredSubstitution() {
    placeAndInjure(getHomeTeam().getPlayerByNumber(3));
    controller.getState().dugOutChoosen(getHomeTeam(), 3);
    assertTurnBeginSelectionState();
  }

  public void testSubstitionAfterUndoAction() {
    setPlayerTo(getHomeTeamPlayer(), squareZeroZero);
    controller.getState().squareChoosen(squareZeroZero);
    assertTrue(controller.getState() instanceof MoveActionState);
    controller.getState().deselectInTurnOperation();
    assertTurnBeginSelectionState();
  }

  public void testMoveSelectionAfterAction() {
    setPlayerTo(getHomeTeamPlayer(), squareFiveZero);
    setBallPosition(squareOneOne);
    controller.getState().squareChoosen(squareFiveZero);
    controller.getState().squareChoosen(squareFiveOne);
    controller.getState().deselectInTurnOperation();
    assertTrue(!(controller.getState() instanceof TurnBeginSelectionState));
  }

  public void testInactiveDugOutChoosen() {
    controller.getState().dugOutChoosen(getGuestTeam(), 0);
    assertTurnBeginSelectionState();
  }

  public void testActiveDugOutChoosen() {
    controller.getState().dugOutChoosen(getHomeTeam(), 0);
    assertTrue(controller.getState() instanceof SubstitutionState);
  }

}