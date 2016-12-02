package net.sf.bloodball.model.actions.test;

import java.awt.Point;
import net.sf.bloodball.model.FieldExtents;
import net.sf.bloodball.model.player.Player;

public class HandOffTest extends ActionTest {

  public HandOffTest(String name) {
    super(name);
  }

  private void performLegalHandOffToSquareZeroTwo() {
    setPlayerWithBallTo(getHomeTeamPlayer(), squareZeroOne);
    setPlayerTo(getHomeTeamPlayer(), squareZeroTwo);
    handOff.perform(squareZeroOne, squareZeroTwo);
  }

  public void testHandOffChancesBallPosition() {
    performLegalHandOffToSquareZeroTwo();
    assertEquals(squareZeroTwo, getBallPosition());
  }

  public void testHandOffEndsTurn() {
    performLegalHandOffToSquareZeroTwo();
    assertTrue(getGame().getTeams().isActiveTeam(getGuestTeam()));
  }

  public void testIllegalHandOff() {
    setPlayerTo(getHomeTeamPlayer(), squareOneOne);
    try {
      handOff.perform(squareOneOne, squareOneTwo);
      fail("IllegalArgumentException expected");
    } catch (IllegalStateException expected) {
    }
  }

  public void testIsLegalToEmptySquare() {
    setPlayerTo(getHomeTeamPlayer(), squareZeroOne);
    assertTrue(!handOff.isLegal(squareZeroOne, squareZeroTwo));
  }

  public void testIsLegalToNeighborFriend() {
    setPlayerWithBallTo(getHomeTeamPlayer(), squareZeroTwo);
    setPlayerTo(getHomeTeamPlayer(), squareZeroOne);
    assertTrue(handOff.isLegal(squareZeroTwo, squareZeroOne));
  }

  public void testIsLegalToNeighborOpponent() {
    setPlayerTo(getHomeTeamPlayer(), squareZeroTwo);
    setPlayerTo(getGuestTeamPlayer(), squareZeroOne);
    assertTrue(!handOff.isLegal(squareZeroTwo, squareZeroOne));
  }

  public void testIsLegalToProneFriend() {
    setPlayerTo(getHomeTeamPlayer(), squareZeroTwo);
    setPlayerTo(getHomeTeamPlayer(), squareZeroOne);
    getLegalPlayerAt(squareZeroOne).knockOver();
    assertTrue(!handOff.isLegal(squareZeroTwo, squareZeroOne));
  }

  public void testIsLegalToUnreachableFriend() {
    setPlayerTo(getHomeTeamPlayer(), squareZeroOne);
    setPlayerTo(getHomeTeamPlayer(), squareZeroThree);
    assertTrue(!handOff.isLegal(squareZeroOne, squareZeroThree));
  }

  public void testIsLegalWithoutBall() {
    setPlayerTo(getHomeTeamPlayer(), squareZeroOne);
    setPlayerTo(getHomeTeamPlayer(), squareZeroTwo);
    assertTrue(!handOff.isLegal(squareZeroOne, squareZeroTwo));
  }

  public void testLegalityAfterMove() throws Exception {
    Player player = setPlayerWithBallTo(getHomeTeamPlayer(), squareOneOne);
    movePlayerToSquare(player, squareOneTwo);
    setPlayerTo(getHomeTeamPlayer(), squareOneThree);
    assertTrue(handOff.isLegal(squareOneTwo, squareOneThree));
  }
}