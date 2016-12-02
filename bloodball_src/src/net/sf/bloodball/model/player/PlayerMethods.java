package net.sf.bloodball.model.player;

public class PlayerMethods {
	
	public static class IsInjured extends CountPlayerMethod {
		public boolean count(Player player) {
			return player.isInjured();
		}
	}
	
	public static class InReserve extends CountPlayerMethod {
		public boolean count(Player player) {
			return player.isReserve();
		}
	}
	
	public static class OnField extends CountPlayerMethod {
		public boolean count(Player player) {
			return player.isOnField();
		}
	}
	
	public static PlayerMethod beginTurn() {
		return new PlayerMethod() {
			public void doWithPlayer(Player player) {
				player.beginTurn();
			}
		};
	}
	
	public static PlayerMethod recover() {
		return new PlayerMethod() {
			public void doWithPlayer(Player player) {
				player.recover();
			}
		};
	}
}
