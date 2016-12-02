package net.sf.bloodball.view.test;

import java.awt.Point;
import javax.swing.Icon;
import junit.framework.TestCase;
import net.sf.bloodball.ModelFacade;
import net.sf.bloodball.ModelFacadeImplementation;
import net.sf.bloodball.gameflow.GameFlowController;
import net.sf.bloodball.model.Game;
import net.sf.bloodball.test.DummyModelFacade;
import net.sf.bloodball.view.GameBoard;
import net.sf.bloodball.view.Icons;

public class GameBoardTest extends TestCase {
  private GameBoard gameBoard;
  private final static Point square = new Point(1, 1);
  
  private GameFlowController controller;

  public GameBoardTest(String name) {
    super(name);
  }

  private Icon getSquareIcon() {
    return gameBoard.getFieldSquare(square.x, square.y).getIcon();
  }

  protected void setUp() throws Exception {
    super.setUp();
    controller = new GameFlowController(new Game());
    ModelFacade modelFacade = new ModelFacadeImplementation(controller);
    gameBoard = new GameBoard(modelFacade);
  }

  public void testUpdateEmptySquare() throws Exception {
    gameBoard.updateSquare(square);
    assertNull(getSquareIcon());
  }

}