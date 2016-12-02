package model;


public class HumanPlayer extends Player {

	
	public boolean select(Location location) {
	   if(this.getBoardGame().getState() == State.PIECE)	
		   this.getBoardGame().selectPiece(location);
	   else if(this.getBoardGame().getState() == State.DESTINATION)
		   this.getBoardGame().selectDestination(location);
	   
	   else
		   return false;
	   return true;
	}



	
/*	public boolean moveTo(Location location) {
		
		return false;
	}*/

}
