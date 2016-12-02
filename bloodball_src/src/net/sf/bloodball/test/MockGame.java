package net.sf.bloodball.test;

import net.sf.bloodball.model.*;

public class MockGame extends Game {

	public void newBeginWithHomeTeam() {
		getTeams().initializeBeginningTeam(getTeams().getHomeTeam());
	}

	public void newBeginWithGuestTeam() {
		getTeams().initializeBeginningTeam(getTeams().getGuestTeam());
	}

	public void setBall(Ball ball) {
		getGameComponents().setBall(ball);
	}

}