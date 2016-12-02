package net.sf.bloodball.event.test;

import net.sf.bloodball.gameflow.MoveActionState;
import net.sf.bloodball.gameflow.test.GameFlowTest;
import net.sf.bloodball.model.Notifier;
import net.sf.bloodball.model.player.Health;
import net.sf.bloodball.model.player.Player;
import net.sf.bloodball.test.MockModelListener;

public class SquareContentChangedTest extends GameFlowTest {

  private MockModelListener modelListener;

  public SquareContentChangedTest(String name) {
    super(name);
  }

  protected void setUp() throws Exception {
    super.setUp();
    modelListener = new MockModelListener();
    Notifier.setModelListener(modelListener);
  }

  public void testSetInitialBallPosition() throws Exception {
    modelListener.addExpectedContentChange(squareOneOne);
    setBallPosition(squareOneOne);
    modelListener.verify();
  }

  public void testBallPositionChange() throws Exception {
    setBallPosition(squareOneTwo);
    modelListener.addExpectedContentChange(squareOneTwo);
    modelListener.addExpectedContentChange(squareOneThree);
    setBallPosition(squareOneThree);
    modelListener.verify();
  }

  public void testPickUpBall() throws Exception {
    Player player = setPlayerTo(getHomeTeamPlayer(), squareOneOne);
    setBallPosition(squareOneOne);
    modelListener.addExpectedContentChange(squareOneOne);
    player.pickUpBall();
    modelListener.verify();
  }

  public void testMovePlayer() throws Exception {
    setActiveHomeTeamPlayerWithBallTo(squareOneOne);
    controller.setState(new MoveActionState(controller, null));
    modelListener.addExpectedContentChange(squareOneOne);
    modelListener.addExpectedContentChange(squareOneTwo);
    controller.getState().squareChoosen(squareOneTwo);
    modelListener.verify();
  }

  public void testThrow() throws Exception {
    setD6Results(6, 6);
    setActiveHomeTeamPlayerWithBallTo(squareOneOne);
    setPlayerTo(getHomeTeamPlayer(), squareOneThree);
    controller.setState(new MoveActionState(controller, null));
    modelListener.addExpectedContentChange(squareOneOne);
    modelListener.addExpectedContentChange(squareOneThree);
    controller.getState().squareChoosen(squareOneThree);
    modelListener.verify();
  }

  protected void tearDown() throws Exception {
    Notifier.setModelListener(null);
  }

  public void testInjureActivePlayer() throws Exception {
    Player player = setPlayerTo(getHomeTeamPlayer(), squareOneOne);
    modelListener.addExpectedContentChange(squareOneOne);
    player.injure(Health.INJURED);
    modelListener.verify();
  }

}