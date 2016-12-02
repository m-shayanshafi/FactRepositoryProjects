package net.sf.bloodball.gameflow.test;

import net.sf.bloodball.gameflow.*;

public class SubstitutionStateTest extends ActionStateTest {

  private int playerNumber = 2;

  public SubstitutionStateTest(String name) {
    super(name);
  }

  public State getActionState(GameFlowController context) {
    return new SubstitutionState(context, playerNumber);
  }

  public void testCancelSubstitution() {
    controller.getState().deselectInTurnOperation();
    assertTrue(controller.getState() instanceof TurnBeginSelectionState);
  }

  public void testValidSubstitution() {
  	controller.getState().squareChoosen(squareSevenFourteen);
    assertSame(getPlayerAt(squareSevenFourteen), getHomeTeam().getPlayerByNumber(playerNumber));
    assertTrue(controller.getState() instanceof TurnBeginSelectionState);
  }

  public void testInvalidSubstitutionSquare() {
    controller.getState().squareChoosen(squareZeroZero);
    assertTrue(controller.getState() instanceof SubstitutionState);
  }

  public void testSubstitutionToOccupiedSquare() {
    setPlayerTo(getGuestTeamPlayer(), squareSevenFourteen);
    controller.getState().squareChoosen(squareSevenFourteen);
    assertTrue(controller.getState() instanceof SubstitutionState);
  }

}