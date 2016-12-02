package net.sf.bloodball.test;

import java.awt.Point;
import java.awt.event.MouseListener;
import junit.framework.TestCase;
import net.sf.bloodball.GameController;
import net.sf.bloodball.gameflow.GameFlowController;
import net.sf.bloodball.gameflow.SetupTeamState;
import net.sf.bloodball.model.Game;
import net.sf.bloodball.model.player.Team;
import net.sf.bloodball.view.event.DugOutHandler;

public class GameControllerTest extends TestCase {

  private GameController gameController;
  private GameFlowController controller;

  public GameControllerTest(String name) {
    super(name);
  }

  protected void setUp() throws Exception {
    super.setUp();
    gameController = new GameController();
    controller = gameController.getGameFlowController();
  }

  private void assertStartUpConditions(GameFlowController controller) {
    assertTrue(controller.getState() instanceof SetupTeamState);
    assertEquals(11, controller.getGame().getTeams().getActiveTeam().getPlayersToSetupCount());
    DugOutHandler dugOutHandler = (DugOutHandler) gameController.new TestProbe().getGameBoard().getHomeDugOutSquare(0).getActionListeners()[0];
    assertSame(controller.getGame().getTeams().getHomeTeam(), dugOutHandler.new TestProbe().getTeam());
  }

  public void testInitialization() throws Exception {
    assertStartUpConditions(controller);
  }

  public void testNewGameAfterSetup() throws Exception {
    Game game = controller.getGame();
    Team team = game.getTeams().getActiveTeam();
    controller.getState().dugOutChoosen(team, 0);
    Point setupPosition = new Point(game.getField().getSubstitutArea(team).getHorizontalRange().getLowerBound(), 0);
    controller.getState().squareChoosen(setupPosition);
    gameController.startNewGame();
    assertStartUpConditions(gameController.getGameFlowController());
  }

}