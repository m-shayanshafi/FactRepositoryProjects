package net.sf.bloodball.view.event;

import java.awt.Point;
import java.util.EventListener;
import net.sf.bloodball.model.player.Team;

public interface GameBoardListener extends EventListener {
	
	public abstract void rightClick();

	public abstract void leftClick(Point position);
  
  public abstract void dugOutClicked(Team team, int playerNumber);
}