package net.sf.bloodball.model.actions.test;

import net.sf.bloodball.model.player.ExtraMoveMode;
import net.sf.bloodball.model.player.Player;
import net.sf.bloodball.model.player.SprintMoveMode;

public class MoveLegalityTest extends ActionTest {

  public MoveLegalityTest(String name) {
    super(name);
  }

  public void testIsLegalToFarField() throws Exception {
    setPlayerTo(getHomeTeamPlayer(), squareZeroOne);
    assertTrue(!moveAction.isLegal(squareZeroOne, squareZeroThree));
  }

  public void testIsLegalToFriedOccupiedField() throws Exception {
    setPlayerTo(getHomeTeamPlayer(), squareZeroTwo);
    setPlayerTo(getHomeTeamPlayer(), squareZeroOne);
    assertTrue(!moveAction.isLegal(squareZeroTwo, squareZeroOne));
  }

  public void testIsLegalToNeighborEmptyField() throws Exception {
    setPlayerTo(getHomeTeamPlayer(), squareZeroOne);
    assertTrue(moveAction.isLegal(squareZeroOne, squareZeroTwo));
  }

  public void testIsLegalToOpponentOccupiedField() throws Exception {
    setPlayerTo(getHomeTeamPlayer(), squareZeroTwo);
    setPlayerTo(getGuestTeamPlayer(), squareZeroOne);
    assertTrue(!moveAction.isLegal(squareZeroTwo, squareZeroOne));
  }

  public void testIsPlayerOfActiveTeamMoveable() {
    setPlayerTo(getHomeTeamPlayer(), squareZeroOne);
    assertTrue(moveAction.isMoveablePlayer(squareZeroOne));
  }

  public void testIsPlayerOfInactiveTeamMoveable() {
    setPlayerTo(getGuestTeamPlayer(), squareZeroOne);
    assertTrue(!moveAction.isMoveablePlayer(squareZeroOne));
  }

  public void testIsPlayerOfNoTeamMoveable() {
    assertTrue(!moveAction.isMoveablePlayer(squareZeroOne));
  }
  
  public void testSprintIntoTackleZone() throws Exception {
    Player player = setPlayerTo(getHomeTeamPlayer(), squareZeroOne);
    player.setMoveMode(new SprintMoveMode(player));
    setPlayerTo(getGuestTeamPlayer(), squareZeroThree);
    assertTrue(!moveAction.isLegal(squareZeroOne, squareZeroTwo));
  }

  public void testExtraMovementIntoTackleZone() throws Exception {
    Player player = setPlayerTo(getHomeTeamPlayer(), squareZeroOne);
    player.setMoveMode(new ExtraMoveMode(player));
    setPlayerTo(getGuestTeamPlayer(), squareZeroThree);
    assertTrue(!moveAction.isLegal(squareZeroOne, squareZeroTwo));
  }
  
  public void testBallPossessorMoveable() throws Exception {
    Player player = setPlayerTo(getHomeTeamPlayer(), squareZeroOne);
    movePlayerToSquare(player, squareZeroTwo);
    assertTrue(moveAction.isMoveablePlayer(squareZeroTwo));
  }
}