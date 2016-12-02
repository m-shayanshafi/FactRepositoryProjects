package board;

import java.awt.Color;

public class Piece {
	private Color c;
	private Cell myCell;
	protected String symbol;

	public Piece(Color c) {
		this.c = c;
		symbol = "O";
	}

	public Color getColor() {
		return c;
	}

	public void setSymbol(String s){
		symbol = s;
	}
	
	public String getSymbol(){
		return symbol;
	}

	public Cell getMyCell() {
		return myCell;
	}

	public void setMyCell(Cell myCell) {
		this.myCell = myCell;
	}
	
}
