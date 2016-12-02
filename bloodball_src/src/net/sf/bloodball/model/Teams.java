package net.sf.bloodball.model;

import de.vestrial.util.error.Ensuring;
import java.awt.Color;
import java.util.Random;
import net.sf.bloodball.model.player.*;
import net.sf.bloodball.resources.*;
import net.sf.bloodball.resources.ResourceHandler;

public class Teams {
	
	private Team activeTeam;
	private Team homeTeam;
	private Team guestTeam;
	private Team offensiveTeam;
	private Game game;

	public Teams(Game game) {
		this.homeTeam = new Team(game, ResourceHandler.getString(ResourceKeys.HOME_TEAM), Color.blue.darker());
		this.guestTeam = new Team(game, ResourceHandler.getString(ResourceKeys.GUEST_TEAM), Color.red.darker());
		this.game = game;
	}

	protected void doWithAllPlayers(PlayerMethod method) {
		homeTeam.doWithPlayers(method);
		guestTeam.doWithPlayers(method);
	}

	public Team getActiveTeam() {
		return activeTeam;
	}

	public Team getGuestTeam() {
		return guestTeam;
	}

	public Team getHomeTeam() {
		return homeTeam;
	}

	public Team getInactiveTeam() {
		return getOpponentTeam(activeTeam);
	}

	public Team getOffensiveTeam() {
		return offensiveTeam;
	}

	public Team getOpponentTeam(Team team) {
		return (team == homeTeam) ? guestTeam : homeTeam;
	}

	private Team getRandomTeam() {
		return new Random().nextInt(2) == 1 ? getHomeTeam() : getGuestTeam();
	}

	public Team getWinningTeam() {
		if (getHomeTeam().getTouchdownsScored() == game.getVictoryTouchdowns()) {
			return getHomeTeam();
		}
		if (getGuestTeam().getTouchdownsScored() == game.getVictoryTouchdowns()) {
			return getGuestTeam();
		}
		return Team.NO_TEAM;
	}

	public void initializeBeginningTeam() {
		initializeBeginningTeam(getRandomTeam());
	}

	public void initializeBeginningTeam(Team beginningTeam) {
		this.offensiveTeam = beginningTeam;
		this.activeTeam = beginningTeam;
		doWithAllPlayers(PlayerMethods.beginTurn());
	}
  
  public boolean isActiveTeam(Team team) {
    return team == getActiveTeam();
  }

	public void scoreTouchdown(Team team) {
		Ensuring.state(getWinningTeam() == Team.NO_TEAM, "Winning team already determined.");
		team.scoreTouchdown();
	}

	public void startNewRound(Team offensiveTeam) {
		this.activeTeam = offensiveTeam;
		this.offensiveTeam = offensiveTeam;
		doWithAllPlayers(PlayerMethods.recover());
	}

	public void startNewTurn() {
		this.activeTeam = getInactiveTeam();
		doWithAllPlayers(PlayerMethods.beginTurn());
	}
	
}