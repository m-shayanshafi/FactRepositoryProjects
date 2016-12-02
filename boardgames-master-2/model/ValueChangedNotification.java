package model;


public class ValueChangedNotification {
	
	private Location location;
//	private int value;
	
	public ValueChangedNotification(Location l){
		this.location = l;
		//this.value = val;
	}
	
	public Location getLocation() {
		
		return location;
	}
	
	public void setLocation(Location location) {
		
		this.location = location;
	}


	
    
}
