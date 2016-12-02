package net.sf.bloodball.gameflow.test;

import net.sf.bloodball.gameflow.*;

public class SetupBallStateTest extends ActionStateTest {
	public SetupBallStateTest(String name) {
		super(name);
	}

	public State getActionState(GameFlowController context) {
		return new SetupBallState(context);
	}
	
	public void testBallSetup() throws Exception {
    setPlayerTo(getHomeTeamPlayer(), squareOneOne);
    setPlayerTo(getHomeTeamPlayer(), squareOneThree);
    assertTrue(!getPlayerAt(squareOneOne).inBallPossession());
    controller.getState().squareChoosen(squareOneOne);
    assertTrue(getPlayerAt(squareOneOne).inBallPossession());
    controller.getState().squareChoosen(squareOneThree);
    assertTrue(getPlayerAt(squareOneThree).inBallPossession());
    assertTrue(!getPlayerAt(squareOneOne).inBallPossession());
  }

	public void testActionPerformed() {
		state.performEndTurnOperation();
		assertTrue(controller.getState() instanceof MoveSelectionState);
	}
}