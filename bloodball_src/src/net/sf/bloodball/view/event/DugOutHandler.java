package net.sf.bloodball.view.event;

import java.awt.event.ActionEvent;
import net.sf.bloodball.model.player.Team;
import net.sf.bloodball.view.GameBoard;

public class DugOutHandler extends MouseHandler {
  
  private Team team;
  private int playerNumber;
  
  public DugOutHandler(int playerNumber, Team team, GameBoard gameBoard) {
  	super(gameBoard);
  	this.playerNumber = playerNumber;
    this.team = team;
  }
  
	public void actionPerformed(ActionEvent e) {
    gameBoard.getGameBoardListener().dugOutClicked(team, playerNumber);
	}

  public class TestProbe {
    public Team getTeam() {
      return team;
    }
  }
}
