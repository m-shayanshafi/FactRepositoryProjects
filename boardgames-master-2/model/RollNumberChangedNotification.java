package model;


public class RollNumberChangedNotification {
	
	private int number;
	
	public RollNumberChangedNotification(int rn){
		this.number = rn;
	}
	public int getNumber() {
		
		return number;
	}
	
	public void setNumber(int number) {
		
		this.number = number;
	}

}
