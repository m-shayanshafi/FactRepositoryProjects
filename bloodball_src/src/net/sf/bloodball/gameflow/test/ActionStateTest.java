package net.sf.bloodball.gameflow.test;

import java.awt.Point;
import net.sf.bloodball.GameBoardController;
import net.sf.bloodball.gameflow.GameFlowController;
import net.sf.bloodball.gameflow.State;

public abstract class ActionStateTest extends GameFlowTest {
  protected State state;

  public ActionStateTest(String name) {
    super(name);
  }

  abstract public State getActionState(GameFlowController context);

  protected void setUp() throws Exception {
    super.setUp();
    this.state = getActionState(controller);
    controller.setState(state);
  }

  protected void setFullTeamOnField() {
    for (int i = 0; i < 11; i++) {
      setPlayerTo(getHomeTeamPlayer(), new Point(0, i));
    }
  }

}