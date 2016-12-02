package com.game.board;

import com.game.entity.Move;


public class ConnectFourBoard extends BaseBoard {

	public ConnectFourBoard(int rowSize, int columnSize) {
		super(rowSize, columnSize);
	}

	@Override
	public boolean isInvalidMove(Move move) {
		//Run regular checks
		if(super.isInvalidMove(move)){
			return true;
		}
		
		//Only valid when:
		//1. Cell at the bottom of the space
		if(move.getColumn() == getColumnSize()-1){
			return false;
		}
		
		//2. Cell above a claimed space
		if(move.getColumn()+1 < getColumnSize() && getBoard()[move.getRow()][move.getColumn() + 1] != null){
			return false;
		}

		return true;
	}
}
