package net.sf.bloodball;

import java.awt.Point;
import javax.swing.JOptionPane;
import net.sf.bloodball.gameflow.GameFlowController;
import net.sf.bloodball.model.*;
import net.sf.bloodball.model.player.Team;
import net.sf.bloodball.resources.ResourceKeys;
import net.sf.bloodball.view.GameBoard;
import net.sf.bloodball.view.MainFrame;

public class GameController implements ModelListener {

  private GameBoard gameBoard;
  private GameFlowController controller;
  private MainFrame mainFrame;
  private OperationMessages localOperations;

  public GameController() {
    controller = new GameFlowController(new Game());
    gameBoard = new GameBoard(new ModelFacadeImplementation(controller));
    mainFrame = new MainFrame(gameBoard, this);
    localOperations = new OperationMessages(controller);
    Notifier.setModelListener(this);
    initializeGameFlowController();
  }

  public void gameEnded() {
    final String message = MessageBuilder.buildMessage(ResourceKeys.GAME_ENDED, getGame().getTeams().getWinningTeam().getName());
    if (JOptionPane.showConfirmDialog(mainFrame, message, "Spielende", JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION) {
      startNewGame();
    } else {
      System.exit(0);
    }
  }

  public Game getGame() {
    return controller.getGame();
  }

  private void initializeGameFlowController() {
    controller.initialize();
    GameBoardController gameBoardController = new GameBoardController(controller);
    gameBoard.setGameBoardListener(gameBoardController);
    mainFrame.setActionListener(gameBoardController);
  }

  public void startNewGame() {
    controller = new GameFlowController(new Game());
    gameBoard.getModelFacade().setController(controller);
    localOperations.setController(controller);
    initializeGameFlowController();
    gameBoard.initVisualization();
  }

  public void touchdownScored() {
    gameBoard.initVisualization();
    mainFrame.setScore(getGame().getTeams().getHomeTeam().getTouchdownsScored(), getGame().getTeams().getGuestTeam().getTouchdownsScored());
  }

  public void dugOutPositionActivated(Team team, int playerNumber) {
    gameBoard.setDugOutSquareActivated(team, playerNumber, true);
  }

  public void dugOutPositionDeactivated(Team team, int playerNumber) {
    gameBoard.setDugOutSquareActivated(team, playerNumber, false);
  }

  public void squareContentChanged(Point position) {
    gameBoard.updateSquare(position);
  }

  public void dugOutContentChanged(Team team, int playerNumber) {
    gameBoard.updateDugOut(team, playerNumber);
  }

  public void endTurnOperationChanged() {
    mainFrame.setEndTurnButtonEnabled(controller.getState().mayEndTurn());
    mainFrame.setEndTurnButtonText(localOperations.getEndTurnTextFor(controller.getState().getEndTurnOperation()));
  }

  public void inTurnOperationChanged() {
    mainFrame.setStatusBarColor(getGame().getTeams().getActiveTeam().getColor());
    mainFrame.setStatusBarText(localOperations.getInTurnStatusTextFor(controller.getState().getInTurnOperation()));
  }

  public void showFrame() {
    mainFrame.show();
  }

  public GameFlowController getGameFlowController() {
    return controller;
  }
  
  public class TestProbe {
    public GameBoard getGameBoard() {
      return gameBoard;
    }
  }
}