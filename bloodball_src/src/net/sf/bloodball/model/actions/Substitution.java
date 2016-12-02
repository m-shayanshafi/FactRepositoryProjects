package net.sf.bloodball.model.actions;

import java.awt.Point;
import net.sf.bloodball.model.Game;
import net.sf.bloodball.model.player.*;

public class Substitution extends SetupAction {
  
  
	public Substitution(Game game) {
		super(game);
	}

	public boolean isLegal(Point position) {
		return isWithinActiveTeamSubstitionArea(position) && isNotOccupied(position);
	}

	private boolean isWithinActiveTeamSubstitionArea(Point position) {
		return getGame().getField().getSubstitutArea(getGame().getTeams().getActiveTeam()).contains(position);
	}
  
	public void perform(Point position, int playerNumber) {
    getGame().getField().setPlayer(position, getGame().getTeams().getActiveTeam().getPlayerByNumber(playerNumber));
	}

}
