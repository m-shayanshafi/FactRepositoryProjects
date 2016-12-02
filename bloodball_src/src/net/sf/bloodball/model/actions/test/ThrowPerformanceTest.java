package net.sf.bloodball.model.actions.test;

import java.awt.Point;
import net.sf.bloodball.test.MockBall;

public class ThrowPerformanceTest extends ActionTest {

  public ThrowPerformanceTest(String name) {
    super(name);
  }

  private void assertBallCaught(Point throwerPosition, Point catcherPosition) {
    performLegalThrow(throwerPosition, catcherPosition);
    assertEquals(getBallPosition(), catcherPosition);
  }

  private void assertBallScattersAfterThrow(Point throwerPosition, Point catcherPosition) {
    setMockBall();
    performLegalThrow(throwerPosition, catcherPosition);
    assertTrue(((MockBall) getGame().getBall()).hasScattered());
  }

  private void performLegalThrow(Point throwerPosition, Point catcherPosition) {
    setPlayerWithBallTo(getHomeTeamPlayer(), throwerPosition);
    setPlayerTo(getHomeTeamPlayer(), catcherPosition);
    throwAction.perform(throwerPosition, catcherPosition);
  }

  private void setSuccessfulThrowResult() {
    setD6Results(6, 6);
  }

  private void setInterceptedThrowResult() {
    setD6Results(1, 1);
  }

  public void testCaughtQuickThrow() {
    setD6Results(5, 1);
    performLegalThrow(squareOneOne, squareOneThree);
    assertEquals(getBallPosition(), squareOneThree);
    assertTrue(!getPlayerAt(squareOneOne).inBallPossession());
    assertTrue(getPlayerAt(squareOneThree).inBallPossession());
  }

  public void testIllegalThrow() {
    setPlayerTo(getHomeTeamPlayer(), squareOneOne);
    try {
      throwAction.perform(squareOneOne, squareOneTwo);
      fail("IllegalStateException expected");
    } catch (IllegalStateException expected) {
    }
  }

  public void testMissedBombThrow() {
    setD6Results(6, 2);
    assertBallScattersAfterThrow(squareOneOne, squareFourteenOne);
    assertTrue(!getPlayerAt(squareOneOne).inBallPossession());
  }

  public void testMissedLongThrow() {
    setD6Results(6, 1);
    assertBallScattersAfterThrow(squareOneOne, squareTenOne);
  }

  public void testMissedQuickThrow() {
    setD6Results(4, 1);
    assertBallScattersAfterThrow(squareOneOne, squareOneThree);
  }

  public void testMissedShortThrow() {
    setD6Results(5, 1);
    assertBallScattersAfterThrow(squareOneOne, squareSixOne);
  }

  public void testSuccessfulBombThrow() {
    setD6Results(6, 3);
    assertBallCaught(squareOneOne, squareFourteenOne);
  }

  public void testSuccessfulLongThrow() {
    setD6Results(6, 2);
    assertBallCaught(squareOneOne, squareTenOne);
  }

  public void testSuccessfullShortThrow() {
    setD6Results(6, 1);
    assertBallCaught(squareOneOne, squareSixOne);
  }

  public void testSuccessfulQuickThrow() {
    setD6Results(5, 1);
    assertBallCaught(squareOneOne, squareOneThree);
  }

  public void testThrowChancesBallPosition() {
    setSuccessfulThrowResult();
    performLegalThrow(squareZeroOne, squareTwoOne);
    assertEquals(squareTwoOne, getBallPosition());
  }

  public void testThrowEndsTurn() {
    performLegalThrow(squareZeroOne, squareTwoOne);
    assertTrue(getTeams().isActiveTeam(getGuestTeam()));
  }

  public void testStraightInterceptedThrow() {
    setInterceptedThrowResult();
    setPlayerTo(getGuestTeamPlayer(), squareOneOne);
    performLegalThrow(squareZeroOne, squareTwoOne);
    assertEquals(squareOneOne, getBallPosition());
    assertTrue(getPlayerAt(squareOneOne).inBallPossession());
  }

  public void testDiagonalInterceptedThrow() {
    setMockBall();
    setInterceptedThrowResult();
    setPlayerTo(getGuestTeamPlayer(), squareOneTwo);
    performLegalThrow(squareOneOne, squareThreeThree);
    assertTrue(((MockBall) getGame().getBall()).hasScattered());
  }

  public void testBackwardInterceptedThrow() {
    setMockBall();
    setInterceptedThrowResult();
    setPlayerTo(getGuestTeamPlayer(), squareOneOne);
    performLegalThrow(squareOneTwo, squareOneFour);
    assertTrue(((MockBall) getGame().getBall()).hasScattered());
  }

}