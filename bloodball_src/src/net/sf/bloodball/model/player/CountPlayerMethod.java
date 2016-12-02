package net.sf.bloodball.model.player;

public abstract class CountPlayerMethod implements PlayerMethod {

	private int count = 0;

	public void doWithPlayer(Player player) {
		if (count(player)) {
			count++;
		}
	}

	public int getCount() {
		return count;
	}

	protected abstract boolean count(Player player);

}