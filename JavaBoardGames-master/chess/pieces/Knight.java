package chess.pieces;

import java.awt.Color;

import board.Cell;
import board.Indexer;
import board.MovingPiece;
import board.SelectBoard;
import board.Direction.direction;

public class Knight extends MovingPiece{

	public Knight(Color c) {
		super(c);
		setSteps(3);
		setSymbol("N");
	}

	public direction[] getTurns(direction d){
		if(d == direction.N || d == direction.S)
			return new direction[]{direction.E,direction.W};
		else
			return new direction[]{direction.N,direction.S};
	}

	public direction[] getDirections(){
		return new direction[]{direction.E,direction.W,direction.N,direction.S};
	}

	public void generateMoves(SelectBoard board) {
		Cell c,c2;
		for(direction d : getDirections()){
			Indexer ind = new Indexer(getMyCell(),d);
			c = board.nextCell(ind);
			if(c == null)
				continue;
			c = board.nextCell(ind);

			if(c != null){
				for(direction dir : getTurns(d)){
					Indexer ind2 = new Indexer(c,dir);
					c2 = board.nextCell(ind2);
					addMove(c2,true);
				}
			}
		}
	}
}
