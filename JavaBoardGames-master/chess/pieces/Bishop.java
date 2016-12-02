package chess.pieces;

import java.awt.Color;

import board.Direction.direction;
import board.MovingPiece;

public class Bishop extends MovingPiece{

	public Bishop(Color c) {
		super(c);
		setSymbol("B");
	}
	
	public direction[] getDirections(){
		return new direction[]{direction.SE,direction.SW,direction.NE,direction.NW};
	}
}
