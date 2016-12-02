package com.game.board;

import com.game.entity.Move;

public interface Board {
	public int getRowSize();

	public int getColumnSize();

	public Character[][] getBoard();

	public void setMove(Move move, Character value);

	public Character getMove(int row, int column);

	public boolean isInvalidMove(Move move);

	public int movesRemaining();

	public String toString();
}
