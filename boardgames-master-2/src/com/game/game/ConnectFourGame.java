package com.game.game;

import com.game.board.BaseBoard;
import com.game.entity.Move;

public class ConnectFourGame extends AbstractGame {
	private static int CONNECTION_LENGTH = 4;

	public ConnectFourGame(BaseBoard b) {
		super(b);
	}
	
	@Override
	protected boolean winsAnyCondition(Move move) {
		return winsVertical(move) || winsHorizontal(move) || winsDiagnal(move);
	}
	
	@Override
	protected boolean winsVertical(Move move) {
		int consecutiveMarks = 0;
		boolean win = false;
		for (int i = 0; i < board.getColumnSize(); i++) {
			Character moveOnCol = board.getMove(i, move.getColumn());
			consecutiveMarks++;
			if (moveOnCol == null || getCurrentPlayer().getValue() != moveOnCol) {
				consecutiveMarks = 0;
			}
			if (consecutiveMarks >= CONNECTION_LENGTH) {
				win = true;
				break;
			}
		}

		return win;
	}

	@Override
	protected boolean winsHorizontal(Move move) {
		int consecutiveMarks = 0;
		boolean win = false;
		for (int i = 0; i < board.getRowSize(); i++) {

			Character moveOnRow = board.getMove(move.getRow(), i);
			consecutiveMarks++;
			if (moveOnRow == null || getCurrentPlayer().getValue() != moveOnRow) {
				consecutiveMarks = 0;
			}
			if (consecutiveMarks >= CONNECTION_LENGTH) {
				win = true;
				break;
			}
		}

		return win;
	}

	@Override
	protected boolean winsDiagnal(Move move) {
		int consecutiveMarks = 0;
		boolean win = false;
		for (int i = 0; i < board.getRowSize(); i++) {
			if (consecutiveMarks >= CONNECTION_LENGTH) {
				win = true;
				break;
			}
			
			Character moveOnDiagnal = board.getMove(i, i);
			if (moveOnDiagnal == null
					|| moveOnDiagnal != getCurrentPlayer().getValue()) {
				consecutiveMarks = 0;
			}
		}

		return win;
	}

}
