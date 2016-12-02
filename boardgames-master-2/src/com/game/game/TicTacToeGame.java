package com.game.game;

import com.game.board.BaseBoard;
import com.game.entity.Move;

public class TicTacToeGame extends AbstractGame {

	public TicTacToeGame(BaseBoard b) {
		super(b);
	}

	@Override
	protected boolean winsAnyCondition(Move move) {
		return winsVertical(move) || winsHorizontal(move) || winsDiagnal(move);
	}

	@Override
	protected boolean winsVertical(Move move) {
		boolean matchesColumn = true;

		for (int i = 0; i < board.getRowSize(); i++) {
			Character moveOnCol = board.getMove(move.getRow(), i);
			if (moveOnCol == null || getCurrentPlayer().getValue() != moveOnCol) {
				matchesColumn = false;
				break;
			}
		}

		return matchesColumn;
	}

	@Override
	protected boolean winsHorizontal(Move move) {
		boolean matchesRow = true;
		for (int i = 0; i < board.getColumnSize(); i++) {
			Character moveOnRow = board.getMove(i, move.getColumn());
			if (moveOnRow == null || getCurrentPlayer().getValue() != moveOnRow) {
				matchesRow = false;
				break;
			}
		}
		return matchesRow;
	}

	@Override
	protected boolean winsDiagnal(Move move) {
		boolean matchesDiagnal = true;
		for (int i = 0; i < board.getRowSize(); i++) {
			Character moveOnDiagnal = board.getMove(i, i);
			if (moveOnDiagnal == null
					|| moveOnDiagnal != getCurrentPlayer().getValue()) {
				matchesDiagnal = false;
				break;
			}
		}

		return matchesDiagnal;
	}

}
