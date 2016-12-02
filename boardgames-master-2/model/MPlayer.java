package model;




public  class MPlayer extends Player {

	//public abstract boolean select(Location location);
//	public abstract boolean moveTo(Location location);
	
	
	public boolean select(Location location) {
		
		   Location l = new Location((int) (Math.random()*6),1);
		   
		   return this.getBoardGame().selectPiece(l);
	
			
		}

		
}
