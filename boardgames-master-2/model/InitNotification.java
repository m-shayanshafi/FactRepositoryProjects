package model;


public class InitNotification {

	
	private int players;
	private int gameType;
	
	public int getPlayers() {
		return players;
	}

	
	public void setPlayers(int players) {
		this.players = players;
	}
	
	public int getGameType() {
		
		return gameType;
	}

	
	public void setGameType(int gameType) {
		
		this.gameType = gameType;
	}

	
	public static final int senet = 1;
	
	public static final int mancala = 2;
	
	public static final int mancala1 = 2;
	
	public static final int chinese_CHEKERS = 3;

}
