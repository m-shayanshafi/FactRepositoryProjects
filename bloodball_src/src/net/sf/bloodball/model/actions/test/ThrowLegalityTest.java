package net.sf.bloodball.model.actions.test;

import net.sf.bloodball.model.player.Player;

public class ThrowLegalityTest extends ActionTest {

  public ThrowLegalityTest(String name) {
    super(name);
  }

  public void testLegalWithEmptyField() {
    setPlayerWithBallTo(getHomeTeamPlayer(), squareZeroOne);
    assertTrue(!throwAction.isLegal(squareZeroOne, squareTwoOne));
  }

  public void testLegalWithInReachGuestTeamPlayer() {
    setPlayerWithBallTo(getHomeTeamPlayer(), squareZeroOne);
    setPlayerTo(getGuestTeamPlayer(), squareTwoOne);
    assertTrue(!throwAction.isLegal(squareZeroOne, squareTwoOne));
  }

  public void testLegalWithInReachHomeTeamPlayer() {
    setPlayerWithBallTo(getHomeTeamPlayer(), squareZeroOne);
    setPlayerTo(getHomeTeamPlayer(), squareTwoOne);
    assertTrue(throwAction.isLegal(squareZeroOne, squareTwoOne));
  }

  public void testLegalWithInReachProneHomeTeamPlayer() {
    setPlayerWithBallTo(getHomeTeamPlayer(), squareZeroOne);
    Player player = setPlayerTo(getHomeTeamPlayer(), squareTwoOne);
    player.knockOver();
    assertTrue(!throwAction.isLegal(squareZeroOne, squareTwoOne));
  }

  public void testLegalWithMaximalReach() {
    setPlayerWithBallTo(getHomeTeamPlayer(), squareZeroOne);
    setPlayerTo(getHomeTeamPlayer(), squareSixteenOne);
    assertTrue(throwAction.isLegal(squareZeroOne, squareSixteenOne));
  }

  public void testLegalWithNeighborSquare() {
    setPlayerWithBallTo(getHomeTeamPlayer(), squareZeroOne);
    setPlayerTo(getHomeTeamPlayer(), squareOneOne);
    assertTrue(!throwAction.isLegal(squareZeroOne, squareOneOne));
  }

  public void testLegalWithoutBall() {
    setPlayerTo(getHomeTeamPlayer(), squareZeroOne);
    setPlayerTo(getHomeTeamPlayer(), squareTwoOne);
    assertTrue(!throwAction.isLegal(squareZeroOne, squareTwoOne));
  }

  public void testLegalWithOutOfReachHomeTeamPlayer() {
    setPlayerWithBallTo(getHomeTeamPlayer(), squareOneOne);
    setPlayerTo(getHomeTeamPlayer(), squareEighteenOne);
    assertTrue(!throwAction.isLegal(squareOneOne, squareEighteenOne));
  }
  
  public void testLegalityAfterMove() throws Exception {
    Player player = setPlayerWithBallTo(getHomeTeamPlayer(), squareOneOne);
    movePlayerToSquare(player, squareOneTwo);
    setPlayerTo(getHomeTeamPlayer(), squareOneFour);
    assertTrue(!throwAction.isLegal(squareOneTwo, squareOneFour));
  }
}