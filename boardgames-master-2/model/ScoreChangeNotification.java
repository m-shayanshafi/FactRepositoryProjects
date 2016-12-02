package model;

public class ScoreChangeNotification {

	
	private Player player;
	
	
	public ScoreChangeNotification(Player p){
		this.player = p;
	}
	public Player getPlayer() {
		
		return player;
	}
	
	public void setPlayer(Player player) {
		
		this.player = player;
	}
	
	
}
