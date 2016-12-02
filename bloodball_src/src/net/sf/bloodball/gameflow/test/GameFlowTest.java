package net.sf.bloodball.gameflow.test;

import java.awt.Point;
import net.sf.bloodball.gameflow.GameFlowController;
import net.sf.bloodball.model.Game;
import net.sf.bloodball.model.player.Player;
import net.sf.bloodball.test.MockGame;
import net.sf.bloodball.test.ModelTest;

public abstract class GameFlowTest extends ModelTest {

  protected GameFlowController controller;

  public GameFlowTest(String name) {
    super(name);
  }

  protected void setUp() throws Exception {
    super.setUp();
    controller = new GameFlowController(new MockGame());
    setGame((MockGame) controller.getGame());
  }

  protected boolean noPlayerActive() {
    return !controller.isPlayerActive();
  }

  protected Player getActivePlayer() {
    return controller.getActivePlayer();
  }

  protected Player setActiveHomeTeamPlayerTo(Point position) {
    Player player = setPlayerTo(getHomeTeamPlayer(), position);
    controller.setActivePlayer(player);
    return player;

  }

  protected Player setActiveHomeTeamPlayerWithBallTo(Point position) {
    Player player = setActiveHomeTeamPlayerTo(position);
    setBallPosition(position);
    player.catchBall();
    return player;
  }

}