package model;

import java.util.Random;

public class CCPlayer extends Player{

	@Override
	public boolean select(Location location) {
		Random rand = new Random();
		   if(this.getBoardGame().getState() == State.PIECE)	{
			   Location l = new Location(rand.nextInt(17),rand.nextInt(17));
			   return this.getBoardGame().selectPiece(l);
             }
		   else if(this.getBoardGame().getState() == State.DESTINATION){
			  Location l = new Location(rand.nextInt(17),rand.nextInt(17));
			  return  this.getBoardGame().selectDestination(l);
			 }
		   else return false;
	}

}
