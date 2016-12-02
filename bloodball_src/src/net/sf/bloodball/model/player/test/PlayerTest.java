package net.sf.bloodball.model.player.test;

import de.vestrial.util.awt.Points;
import java.awt.Point;
import net.sf.bloodball.model.actions.Move;
import net.sf.bloodball.model.player.Player;
import net.sf.bloodball.test.MockBall;
import net.sf.bloodball.test.ModelTest;

public class PlayerTest extends ModelTest {
  public PlayerTest(String arg1) {
    super(arg1);
  }

  public void testAtCallAfterInitialization() {
    Player player = getHomeTeamPlayer();
    assertTrue(player.isAtCall());
  }

  public void testAtCallBallPossessingAfterMoving() {
    Player ballPossessor = setPlayerWithBallTo(getHomeTeamPlayer(), squareOneOne);
    movePlayer(ballPossessor, 1);
    ballPossessor.endTurn();
    assertTrue(!ballPossessor.isAtCall());
  }

  public void testAtCallBallPossessingNearFriendAfterMoving() {
    Player ballPossessor = setPlayerWithBallTo(getHomeTeamPlayer(), squareOneOne);
    setPlayerTo(getHomeTeamPlayer(), squareOneThree);
    movePlayerToSquare(ballPossessor, squareOneTwo);
    ballPossessor.endTurn();
    assertTrue(ballPossessor.isAtCall());
  }

  public void testAtCallWhenInjured() {
    Player player = getHomeTeamPlayer();
    placeAndInjure(player);
    assertTrue(!player.isAtCall());
  }

  public void testAtCallWhenKnockedOver() {
    Player player = getHomeTeamPlayer();
    player.knockOver();
    assertTrue(!player.isAtCall());
  }

  public void testAtCallWhenProne() {
    Player homePlayer = setPlayerTo(getHomeTeamPlayer(), squareOneOne);
    homePlayer.knockOver();
    assertTrue(!homePlayer.isAtCall());
  }

  public void testBallPossession() {
    Player player = getHomeTeamPlayer();
    assertTrue("Not on Field", !player.inBallPossession());
    setPlayerTo(player, squareZeroOne);
    assertTrue("Without ball", !player.inBallPossession());
    setBallPosition(squareZeroOne);
    assertTrue("Same Position As Ball", !player.inBallPossession());
  }

  public void testPickUpBallWithoutPriorMovement() throws Exception {
    Player player = getHomeTeamPlayer();
    setPlayerTo(player, squareZeroOne);
    setBallPosition(squareZeroOne);
    player.pickUpBall();
    assertTrue("Ball picked up", player.inBallPossession());
    assertEquals(player.getType().getMoveAllowance(), player.getRemainingMovePoints());
  }

  public void testPickUpBallWithPriorMovement() throws Exception {
    Player player = setPlayerTo(getHomeTeamPlayer(), squareZeroOne);
    setBallPosition(squareOneOne);
    movePlayerToSquare(player, squareOneOne);
    player.pickUpBall();
    assertTrue("Ball picked up", player.inBallPossession());
    assertTrue(player.inSprintMode());
  }

  public void testBeginTurn() {
    Player player = getHomeTeamPlayer();
    player.endTurn();
    player.beginTurn();
    assertTrue(player.isAtCall());
  }

  public void testEndTurn() {
    Player player = getHomeTeamPlayer();
    player.endTurn();
    assertTrue(!player.isAtCall());
  }

  public void testInitialAtCall() {
    assertTrue(getHomeTeamPlayer().isAtCall());
  }

  public void testIsReserve() {
    Player player = getHomeTeamPlayer();
    assertTrue(player.isReserve());
  }

  public void testProneTurns() {
    Player player = getHomeTeamPlayer();
    assertTrue("Player must be standing.", !player.isProne());
    setD6Results(1, 1);
    player.knockOver();
    assertTrue("Player is just knocked over.", player.isProne());
    player.beginTurn();
    assertTrue("Player is still knocked over.", player.isProne());
    player.beginTurn();
    assertTrue("Player is on his feet again.", !player.isProne());
  }

  public void testExtraMoveWithInjure() throws Exception {
    MockBall ball = new MockBall(getGame().getField());
    getGame().setBall(ball);
    setD6Result(6);
    Player player = setPlayerWithBallTo(getHomeTeamPlayer(), squareZeroZero);
    assertTrue(!player.inSprintMode());
    assertTrue(!player.inExtraMoveMode());
    movePlayer(player, player.getType().getMoveAllowance());
    assertTrue(player.inSprintMode());
    assertTrue(!player.inExtraMoveMode());
    movePlayer(player, player.getType().getSprintAllowance());
    assertTrue(!player.inSprintMode());
    assertTrue(player.inExtraMoveMode());
    setD6Result(1);
    movePlayerByOneSquare(player);
    assertTrue(player.inExtraMoveMode());
    assertTrue(!player.isInjured());
    setD6Result(6);
    movePlayerByOneSquare(player);
    assertTrue(player.isInjured());
    assertTrue(ball.hasScattered());
  }

  public void testExtraMoveWithoutInjure() {
    Player player = setPlayerWithBallTo(getHomeTeamPlayer(), squareZeroZero);
    movePlayer(player, player.getType().getMoveAllowance());
    movePlayer(player, player.getType().getSprintAllowance());
    setD6Results(1, 1);
    movePlayerByOneSquare(player);
    movePlayerByOneSquare(player);
    assertTrue(!player.isInjured());
  }

  public void testSprintStartInTackleZone() throws Exception {
    Player player = setPlayerTo(getHomeTeamPlayer(), squareZeroZero);
    movePlayerToSquare(player, squareZeroOne);
    movePlayerToSquare(player, squareZeroTwo);
    movePlayerToSquare(player, squareZeroThree);
    setPlayerTo(getGuestTeamPlayer(), squareZeroOne);
    movePlayerToSquare(player, squareZeroTwo);
    assertTrue(player.hasToStopMoving());
    assertTrue(!player.hasActed());
  }

}