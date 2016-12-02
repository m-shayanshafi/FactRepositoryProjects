package model;


public class WinNotification {
	
	
	
	private Player player;
	
	public WinNotification(Player p){
		this.player = p;
	}
	public Player getPlayer() {
		
		return player;
	}
	
	public void setPlayer(Player player) {
		
		this.player = player;
	}
	
}
