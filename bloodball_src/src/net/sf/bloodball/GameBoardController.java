package net.sf.bloodball;

import java.awt.Point;
import java.awt.event.*;
import net.sf.bloodball.gameflow.GameFlowController;
import net.sf.bloodball.model.player.Team;
import net.sf.bloodball.view.event.GameBoardListener;

public class GameBoardController implements GameBoardListener, ActionListener {
  private GameFlowController controller;

  public GameBoardController(GameFlowController controller) {
    this.controller = controller;
  }

  public void rightClick() {
    controller.getState().deselectInTurnOperation();
  }

  public void leftClick(Point position) {
    controller.getState().squareChoosen(position);
  }

  public void dugOutClicked(Team team, int playerNumber) {
    controller.getState().dugOutChoosen(team, playerNumber);
  }

  public void actionPerformed(ActionEvent e) {
    controller.getState().performEndTurnOperation();
  }
}