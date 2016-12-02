package chess.pieces;

import java.awt.Color;

import board.Cell;
import board.Indexer;
import board.MovingPiece;
import board.SelectBoard;
import board.Direction.direction;

public class Pawn extends MovingPiece{

	public Pawn(Color c) {
		super(c);
		setSteps(2);
		setSymbol("P");
	}
	
	public direction[] getDirections(boolean isPlayer1){
		if(isPlayer1){
			return new direction[]{direction.S,direction.SE,direction.SW};
		}else{
			return new direction[]{direction.N,direction.NE,direction.NW};
		}
	}

	public void generateMoves(SelectBoard board) {
		Cell c;
		boolean isPlayer1 = board.isPlayer1(getColor());
		for(direction d : getDirections(isPlayer1)){
			Indexer ind = new Indexer(getMyCell(),d);
			c = board.nextCell(ind);
			if(c != null){
				if(d == direction.N || d == direction.S){
					if(c.hasPiece())
						continue;
					addMove(c);
					if(!isMoved()){
						c = board.nextCell(ind);
						if(c != null && !c.hasPiece())
							addMove(c);
					}
				}
				else if(c.hasPiece()){
					addMove(c,true);
				}
			}
		}
	}
}
