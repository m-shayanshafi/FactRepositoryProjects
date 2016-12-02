package board;

import java.awt.Color;
import java.util.ArrayList;

import board.Direction.direction;

public class MovingPiece extends Piece{
	private ArrayList<Cell> moveSet = new ArrayList<Cell>();
	private boolean moved = false;
	private int steps = 0; 
	
	public MovingPiece(Color c) {
		super(c);
	}
	
	public void emptyMoveSet(){
		moveSet.clear();
	}
	
	public void addMove(Cell c){
		if(c != null)
			moveSet.add(c);
	}
	
	public void addMove(Cell c, boolean checkKill){
		if(checkKill){
			if(c != null && c.getPieceColor() != getColor())
				addMove(c);
		}
		else
			addMove(c);
	}	
	
	public ArrayList<Cell> getMoves(){
		return moveSet;
	}

	public int getSteps() {
		return steps;
	}

	public void setSteps(int steps) {
		this.steps = steps;
	}
	
	public direction[] getDirections(){
		return Direction.getAll();
	}

	public boolean isMoved() {
		return moved;
	}

	public void moved() {
		this.moved = true;
	}

	public void generateMoves(SelectBoard board) {
		Cell c;
		for(direction d : getDirections()){
			Indexer ind = new Indexer(getMyCell(),d);
			c = board.nextCell(ind);
			while(c != null && !c.hasPiece()){
				addMove(c);
				c = board.nextCell(ind);
			}
			addMove(c,true);
		}		
	}
}
