package model;


public class SelectedNotification {

	
	private Location oldS;
	private Location newS;

	public SelectedNotification(Location oldS,Location newS){
		this.oldS = oldS;
		this.newS = newS;
	}

	public Location getOldS() {
		return oldS;
	}

	public void setOldS(Location oldS) {
		this.oldS = oldS;
	}

	public Location getNewS() {
		return newS;
	}

	public void setNewS(Location newS) {
		this.newS = newS;
	}
	

}
