package net.sf.bloodball.model.player;

import java.awt.Color;
import java.util.*;
import net.sf.bloodball.model.Game;

public class Team {
	
	public static final Team NO_TEAM = new NoTeam();

	public final static int SIZE = 16;
	private static final int MAXIMUM_PLAYERS_ON_FIELD = 11;
	private final String name;
	private final Color color;
	private List players = new ArrayList();
	private Game game;
	private int touchdownsScored;

	public Team(Game game, String name, Color color) {
		this.name = name;
		this.game = game;
		this.color = color;
		initTeamPlayers();
	}

	public void doWithPlayers(PlayerMethod method) {
		for (Iterator iterator = players.iterator(); iterator.hasNext();) {
			method.doWithPlayer((Player) iterator.next());
		}
	}

	public Color getColor() {
		return color;
	}

	public int getInjuredPlayersCount() {
		PlayerMethods.IsInjured playerMethod = new PlayerMethods.IsInjured();
		doWithPlayers(playerMethod);
		return playerMethod.getCount();
	}

	public String getName() {
		return name;
	}

	public Player getPlayerFromReserve() {
		for (Iterator iterator = players.iterator(); iterator.hasNext();) {
			Player player = (Player) iterator.next();
			if (player.isReserve()) {
				return player;
			}
		}
		return Player.NO_PLAYER;
	}
  
  public Player getPlayerByNumber(int number) {
    return (Player) players.get(number);
  }
  
  public int getPlayerNumber(Player player) {
  	return players.indexOf(player);
  }
	
	public int getPlayersInReserveCount() {
		PlayerMethods.InReserve playerMethod = new PlayerMethods.InReserve();
		doWithPlayers(playerMethod);
		return playerMethod.getCount();
	}

	public int getPlayersOnFieldCount() {
		PlayerMethods.OnField playerMethod = new PlayerMethods.OnField();
		doWithPlayers(playerMethod);
		return playerMethod.getCount();
	}

	public int getPlayersToSetupCount() {
		int allowed = MAXIMUM_PLAYERS_ON_FIELD - getPlayersOnFieldCount();
		return Math.min(allowed, getPlayersInReserveCount());
	}

	public int getTouchdownsScored() {
		return touchdownsScored;
	}

	public boolean hasPlayerInReserve() {
		return getPlayersInReserveCount() != 0;
	}

	public boolean isHomeTeam() {
		return game.getTeams().getHomeTeam() == this;
	}

	public boolean isMember(Player player) {
		return players.contains(player);
	}

	public void scoreTouchdown() {
		touchdownsScored++;
	}

	public String toString() {
		return name;
	}
	
	private void initTeamPlayers() {
		try {
		for (Iterator playerTypes = PlayerType.getPlayerTypes(); playerTypes.hasNext();) {
			PlayerType type = (PlayerType) playerTypes.next();
			for (int i = 0; i < type.getDefaultCount(); i++) {
				players.add(new Player(game, type));
			}
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}