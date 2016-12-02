package chess.pieces;

import java.awt.Color;

import board.MovingPiece;
import board.Direction.direction;

public class Rook extends MovingPiece{

	public Rook(Color c) {
		super(c);
		setSymbol("R");
	}

	public direction[] getDirections(){
		return new direction[]{direction.E,direction.W,direction.N,direction.S};
	}
}
