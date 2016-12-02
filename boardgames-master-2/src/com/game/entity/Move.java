package com.game.entity;

public class Move {
	private int row;
	private int column;

	public Move(int row, int column) {
		this.row = row;
		this.column = column;
	}

	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}
	
	@Override
	public String toString(){
		return "Row:" + String.valueOf(row) + " ,Column:" + String.valueOf(column);
	}
}
