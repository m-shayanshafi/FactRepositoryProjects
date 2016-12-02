package board;

import board.Direction.direction;

public class Indexer {
	private int rowIndex, colIndex;
	direction dir;

	public Indexer(int row, int col, direction d){
		dir = d;
		rowIndex = row;
		colIndex = col;
	}

	public Indexer(Cell cell, direction d){
		dir = d;
		rowIndex = cell.row;
		colIndex = cell.col;
	}
	
	public int nextRow(){
		switch(dir){
		case S:
		case SE:
		case SW:
			rowIndex++;
			break;
		case N:
		case NE:
		case NW:
			rowIndex--;
			break;
		default:
			break;
		}
		
		return rowIndex;
	}
	
	public int nextCol(){
		switch(dir){
		case E:
		case SE:
		case NE:
			colIndex++;
			break;
		case W:
		case SW:
		case NW:
			colIndex--;
			break;
		default:
			break;
		}
		
		return colIndex;
	}

}
