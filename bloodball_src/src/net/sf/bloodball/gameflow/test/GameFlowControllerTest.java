package net.sf.bloodball.gameflow.test;

import net.sf.bloodball.gameflow.GameFlowController;
import net.sf.bloodball.model.player.Player;

public class GameFlowControllerTest extends GameFlowTest {

  private GameFlowController controller;

  public GameFlowControllerTest(String name) {
    super(name);
  }

  protected void setUp() throws Exception {
    super.setUp();
    controller = new GameFlowController(getGame());
  }

  public void testDeactivePlayer() {
    Player player = getGame().getTeams().getHomeTeam().getPlayerFromReserve();
    controller.setActivePlayer(player);
    controller.deactivatePlayer();
    assertTrue(!controller.isPlayerActive());
  }

  public void testSetActivePlayer() throws Exception {
    Player player = getGame().getTeams().getHomeTeam().getPlayerFromReserve();
    controller.setActivePlayer(player);
    assertSame(controller.getActivePlayer(), player);
  }
}