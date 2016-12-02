package net.sf.bloodball.gameflow.test;

import net.sf.bloodball.gameflow.*;

public class MoveSelectionStateTest extends ActionStateTest {

  public MoveSelectionStateTest(String name) {
    super(name);
  }

  public State getActionState(GameFlowController context) {
    return new MoveSelectionState(context);
  }

  public void testPerformEndTurnOperation() {
    state.performEndTurnOperation();
    assertTrue(controller.getState() instanceof TurnBeginSelectionState);
  }

}