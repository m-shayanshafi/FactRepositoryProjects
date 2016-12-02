package model;

import java.util.Random;

public class SPlayer extends Player {

	@Override
	public boolean select(Location location) {
		Random rand = new Random();
		   if(this.getBoardGame().getState() == State.PIECE||location == null)	{
			   Location l = new Location(rand.nextInt(11),rand.nextInt(3));
			   return this.getBoardGame().selectPiece(l);
	
		   }
		   else if(this.getBoardGame().getState() == State.DESTINATION)
			 return  this.getBoardGame().selectDestination(location);
		   else return false;
		
	}
	

}
