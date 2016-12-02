package checkers;

import java.awt.Color;
import java.util.Hashtable;

import board.Cell;
import board.Indexer;
import board.MovingPiece;
import board.SelectBoard;
import board.Direction.direction;

public class CheckersPiece extends MovingPiece{
	private boolean atTop = false;
	private boolean isKing = false;
	private Hashtable<Cell, Cell> kills = new Hashtable<>();
	
	public CheckersPiece(Color c) {
		super(c);
	}
	
	public Cell getKill(Cell key){
		if(!kills.containsKey(key))
			return null;
		
		return kills.get(key);
	}

	public CheckersPiece(Color c, boolean top) {
		super(c);
		atTop = top;
	}

	public direction[] getDirections(){
		if(isKing)
			return new direction[]{direction.SE,direction.SW,direction.NE,direction.NW};
		if(atTop)
			return new direction[]{direction.SE,direction.SW};
		else
			return new direction[]{direction.NE,direction.NW};
	}
	
	public void evolve(){
		isKing = true;
		setSymbol("@");
		getMyCell().fill();
	}

	public void generateMoves(SelectBoard board) {
		Cell c,c2;
		for(direction d : getDirections()){
			Indexer ind = new Indexer(getMyCell(),d);
			c = board.nextCell(ind);
			if(c!=null && !c.hasPiece())
				addMove(c,true);
			else if(c!=null && c.getPieceColor() != getColor()){
				c2 = board.nextCell(ind);
				if(c2!=null && !c2.hasPiece()){
					kills.put(c2, c);
					addMove(c2);
				}
			}
		}
	}
}
