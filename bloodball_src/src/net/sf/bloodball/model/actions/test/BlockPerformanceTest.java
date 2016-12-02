package net.sf.bloodball.model.actions.test;

import net.sf.bloodball.model.*;
import net.sf.bloodball.model.player.*;
import net.sf.bloodball.test.*;
import java.awt.Point;

public class BlockPerformanceTest extends ActionTest {
  private Player blocker;
  private Player blockedPlayer;

  public BlockPerformanceTest(String name) {
    super(name);
  }

  private void performBlock(Point blockerPosition, Point blockedPosition) {
    setPlayerTo(blocker, blockerPosition);
    setPlayerTo(blockedPlayer, blockedPosition);
    block.perform(blockerPosition, blockedPosition);
  }

  private void performProneBlock(Point blockerPosition, Point blockedPosition) {
    setPlayerTo(blocker, blockerPosition);
    setPlayerTo(blockedPlayer, blockedPosition);
    blockedPlayer.knockOver();
    block.perform(blockerPosition, blockedPosition);
  }

  protected void setUp() throws Exception {
    super.setUp();
    blocker = getHomeTeamPlayer();
    blockedPlayer = getGuestTeamPlayer();
  }

  public void testBlockerTurnEndedAfterBlock() {
    performBlock(squareZeroOne, squareZeroTwo);
    assertTrue(!getPlayerAt(squareZeroOne).isAtCall());
  }

  public void testBlockResultEight() {
    setD6Results(new int[]{4, 4, 1, 1});
    performBlock(squareZeroOne, squareZeroTwo);
    assertSame(getPlayerAt(squareZeroOne), blocker);
    assertTrue(blocker.isProne());
    assertSame(getPlayerAt(squareZeroTwo), blockedPlayer);
    assertTrue(blockedPlayer.isProne());
  }

  public void testBlockResultFiveToSeven() {
    setD6Results(3, 2);
    performBlock(squareZeroOne, squareZeroTwo);
    assertSame(getPlayerAt(squareZeroOne), blocker);
    assertTrue(!blocker.isProne());
    assertSame(getPlayerAt(squareZeroTwo), blockedPlayer);
    assertTrue(!blockedPlayer.isProne());
  }

  public void testBlockResultNineToEleven() {
    setD6Results(new int[]{5, 4, 1, 1});
    performBlock(squareZeroOne, squareZeroTwo);
    assertSame(getPlayerAt(squareZeroOne), blocker);
    assertTrue(!blocker.isProne());
    assertSame(getPlayerAt(squareZeroTwo), blockedPlayer);
    assertTrue(blockedPlayer.isProne());
  }

  public void testBlockResultThreeToFour() {
    setD6Results(2, 1);
    performBlock(squareZeroOne, squareZeroTwo);
    assertSame(getPlayerAt(squareZeroOne), blocker);
    assertTrue(blocker.isProne());
    assertSame(getPlayerAt(squareZeroTwo), blockedPlayer);
    assertTrue(!blockedPlayer.isProne());
  }

  public void testBlockResultTwelve() {
    setD6Results(6, 6);
    performBlock(squareZeroOne, squareZeroTwo);
    assertSame(getPlayerAt(squareZeroOne), blocker);
    assertTrue(!blocker.isProne());
    assertSame(getPlayerAt(squareZeroTwo), Player.NO_PLAYER);
  }

  public void testBlockResultTwo() {
    setD6Results(1, 1);
    performBlock(squareZeroOne, squareZeroTwo);
    assertSame(getPlayerAt(squareZeroOne), Player.NO_PLAYER);
    assertSame(getPlayerAt(squareZeroTwo), blockedPlayer);
    assertTrue(!blockedPlayer.isProne());
  }

  public void testIllegalBlock() {
    try {
      performBlock(squareOneOne, squareSixOne);
      fail("IllegalArgumentException expected");
    } catch (IllegalStateException expected) {
    }
  }

  public void testProneBlockResultFourToNine() {
    setD6Results(new int[]{1, 1, 2, 2});
    performProneBlock(squareZeroOne, squareZeroTwo);
    assertSame(getPlayerAt(squareZeroOne), blocker);
    assertTrue(!blocker.isProne());
    assertSame(getPlayerAt(squareZeroTwo), blockedPlayer);
  }

  public void testProneBlockResultTenToTwelve() {
    setD6Results(new int[]{1, 1, 5, 5});
    performProneBlock(squareZeroOne, squareZeroTwo);
    assertSame(getPlayerAt(squareZeroOne), blocker);
    assertTrue(!blocker.isProne());
    assertSame(getPlayerAt(squareZeroTwo), Player.NO_PLAYER);
  }

  public void testProneBlockResultTwoToThree() {
    setD6Results(1, 1);
    performProneBlock(squareZeroOne, squareZeroTwo);
    assertSame(getPlayerAt(squareZeroOne), blocker);
    assertTrue(blocker.isProne());
    assertSame(getPlayerAt(squareZeroTwo), blockedPlayer);
  }

}