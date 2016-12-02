package chess.pieces;

import java.awt.Color;

import board.Cell;
import board.Indexer;
import board.MovingPiece;
import board.SelectBoard;
import board.Direction.direction;

public class King extends MovingPiece{

	public King(Color c) {
		super(c);
		setSteps(1);
		setSymbol("K");
	}
	
	public void generateMoves(SelectBoard board) {
		Cell c;
		for(direction d : getDirections()){
			Indexer ind = new Indexer(getMyCell(),d);
			c = board.nextCell(ind);
			addMove(c,true);
		}
	}
}
