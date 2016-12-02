package com.game.board;

import com.game.entity.Move;


public class BaseBoard implements Board{
	private final int rowSize;
	private final int columnSize;
	private final Character[][] board;

	public BaseBoard(int rowSize, int columnSize) {
		this.board = new Character[rowSize][columnSize];
		this.rowSize = rowSize;
		this.columnSize = columnSize;
	}

	public int getRowSize() {
		return rowSize;
	}

	public int getColumnSize() {
		return columnSize;
	}

	public Character[][] getBoard(){
		return board;
	}
	
	public void setMove(Move move, Character value) {
		getBoard()[move.getRow()][move.getColumn()] = value;
	}

	public Character getMove(int row, int column) {
		return getBoard()[row][column];
	}

	public boolean isInvalidMove(Move move) {
		if(move.getColumn() < 0 || move.getColumn() > this.getColumnSize()){
			return true;
		} else if(move.getRow() < 0 || move.getRow() > this.getRowSize()){
			return true;
		} else if(getBoard()[move.getRow()][move.getColumn()] != null){
			return true;
		}
		
		return false;
	}

	public int movesRemaining() {
		int moves = 0;
		for (int x = 0; x < getRowSize(); x++) {
			for (int y = 0; y < getColumnSize(); y++) {
				moves += getMove(x, y) == null ? 1 : 0;
			}
		}
		return moves;
	}
	
	public String toString() {
		StringBuffer stringBuffer = new StringBuffer();
		for (int y = 0; y < getColumnSize(); y++) {
			for (int x = 0; x < getRowSize(); x++) {
				Character move = getMove(x, y);
				String movesAsString = "";
				if (move == null) {
					movesAsString = "-";
				} else {
					movesAsString = move.toString();
				}
				stringBuffer.append("|").append(movesAsString);
			}
			stringBuffer.append("|\n");
		}
		return stringBuffer.toString();
	}
}