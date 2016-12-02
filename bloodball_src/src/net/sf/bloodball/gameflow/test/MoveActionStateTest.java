package net.sf.bloodball.gameflow.test;

import java.awt.Point;
import net.sf.bloodball.gameflow.*;
import net.sf.bloodball.model.FieldExtents;
import net.sf.bloodball.model.actions.Move;
import net.sf.bloodball.model.player.ExtraMoveMode;
import net.sf.bloodball.model.player.Player;

public class MoveActionStateTest extends ActionStateTest {

  public MoveActionStateTest(String name) {
    super(name);
  }

  public State getActionState(GameFlowController context) {
    return new MoveActionState(context, new MoveSelectionState(context));
  }
  
  protected void setUp() throws Exception {
  	super.setUp();
	setBallPosition(squareFourteenZero);
  }

  public void testInitialization() {
    assertSame(EndTurnOperation.PICK_UP_BALL, state.getEndTurnOperation());
    assertSame(InTurnOperation.MOVE, state.getInTurnOperation());
    assertTrue(!state.mayEndTurn());
  }

  public void testUndoActivation() {
    setActiveHomeTeamPlayerTo(squareOneOne);
    assertTrue(getActivePlayer() != Player.NO_PLAYER);
    controller.getState().deselectInTurnOperation();
    assertSame(Player.NO_PLAYER, getActivePlayer());
  }

  public void testActivatePlayerOnBallField() throws Exception {
    setPlayerTo(getHomeTeamPlayer(), squareOneOne);
    setBallPosition(squareOneOne);
    controller.setState(new MoveSelectionState(controller));
    controller.getState().squareChoosen(squareOneOne);
    assertTrue(controller.getState() instanceof MoveActionState);
    assertTrue(controller.getState().mayEndTurn());
    controller.getState().performEndTurnOperation();
    assertTrue(!controller.getState().mayEndTurn());
  }

  public void testNoPlayerActiveAfterBlock() {
    setActiveHomeTeamPlayerWithBallTo(squareOneOne);
    setPlayerTo(getGuestTeamPlayer(), squareZeroTwo);
    getActionState(controller).squareChoosen(squareZeroTwo);
    assertTrue(noPlayerActive());
  }

  public void testHandOffDeactivatesPlayer() {
    setActiveHomeTeamPlayerWithBallTo(squareZeroOne);
    setPlayerTo(getHomeTeamPlayer(), squareZeroTwo);
    getActionState(controller).squareChoosen(squareZeroTwo);
    assertTrue(noPlayerActive());
    assertTrue(!getPlayerAt(squareZeroOne).inBallPossession());
    assertTrue(getPlayerAt(squareZeroTwo).inBallPossession());
  }

  public void testTouchdownScored() {
    Point firstEndZoneSquare = new Point(FieldExtents.GUEST_END_ZONE.getLowerBound(), 0);
    Point endZoneSquare = new Point(FieldExtents.GUEST_END_ZONE.getLowerBound(), 1);
    Player player = setActiveHomeTeamPlayerWithBallTo(firstEndZoneSquare);
    getActionState(controller).squareChoosen(endZoneSquare);
    assertTrue(noPlayerActive());
    assertTrue(controller.getState() instanceof SetupTeamState);
    assertTrue(!player.inBallPossession());
  }
  
  public void testThrowDeactivatesPlayer() {
    Player thrower = setActiveHomeTeamPlayerWithBallTo(squareOneOne);
    setPlayerTo(getHomeTeamPlayer(), squareOneThree);
    getActionState(controller).squareChoosen(squareOneThree);
    assertTrue(noPlayerActive());
    assertTrue(!thrower.hasActed());
  }

  public void testPlayerActivationAfterMove() {
    setActiveHomeTeamPlayerTo(squareZeroOne);
    controller.getState().squareChoosen(squareZeroTwo);
    controller.getState().deselectInTurnOperation();
    assertTrue(controller.getState() instanceof MoveSelectionState);
    controller.getState().squareChoosen(squareZeroTwo);
    assertTrue(controller.getState() instanceof MoveSelectionState);
  }

  public void testIsPlayerNotActedMovableSecondTime() {
    setActiveHomeTeamPlayerTo(squareZeroOne);
    controller.getState().deselectInTurnOperation();
    assertTrue(controller.getState() instanceof MoveSelectionState);
    controller.getState().squareChoosen(squareZeroOne);
    assertTrue(controller.getState() instanceof MoveActionState);
  }
  
  public void testInjuryOfActivePlayer() throws Exception {
    setActiveHomeTeamPlayerTo(squareZeroOne);
    Player player = controller.getActivePlayer();
    player.setMoveMode(new ExtraMoveMode(player));
    setD6Result(6);
    controller.getState().squareChoosen(squareZeroTwo);
    assertTrue(noPlayerActive());
  }
  
  public void testNoMoveModeAfterCancelled() throws Exception {
    Player player = setActiveHomeTeamPlayerWithBallTo(squareZeroOne);
    controller.getState().squareChoosen(squareZeroTwo);
    controller.getState().deselectInTurnOperation();
    assertTrue(player.inNoMoveMode());
    setPlayerTo(getHomeTeamPlayer(), squareZeroOne);
    controller.getState().squareChoosen(squareZeroTwo);
    assertTrue(player.inNoMoveMode());
    assertSame(InTurnOperation.HAND_OFF, controller.getState().getInTurnOperation());
  }
}