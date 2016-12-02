package net.sf.bloodball.model.actions.test;

import net.sf.bloodball.model.actions.*;
import net.sf.bloodball.test.ModelTest;

public abstract class ActionTest extends ModelTest {

  protected Throw throwAction;
  protected Move moveAction;
  protected HandOff handOff;
  protected Block block;

  public ActionTest(String name) {
    super(name);
  }

  protected void setUp() throws Exception {
    super.setUp();
    throwAction = new Throw(getGame());
    moveAction = new Move(getGame());
    handOff = new HandOff(getGame());
    block = new Block(getGame());
  }
}