package com.game.game;

import com.game.board.BaseBoard;
import com.game.entity.Move;

public class NTicTacToeGame extends AbstractGame {
	private static int CONNECTION_LENGTH = 3;

	public NTicTacToeGame(BaseBoard b) {
		super(b);
	}

	@Override
	protected boolean winsAnyCondition(Move move) {
		return winsVertical(move) || winsHorizontal(move);
	}

	@Override
	protected boolean winsVertical(Move move) {
		int consecutiveMarks = 0;
		boolean win = false;
		for (int i = 0; i < board.getColumnSize(); i++) {

			Character colMove = board.getMove(i, move.getColumn());
			consecutiveMarks++;
			if (colMove == null || getCurrentPlayer().getValue() != colMove) {
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

			Character rowMove = board.getMove(move.getRow(), i);

			consecutiveMarks++;
			if (rowMove == null || getCurrentPlayer().getValue() != rowMove) {
				consecutiveMarks = 0;
			}
			if (consecutiveMarks >= CONNECTION_LENGTH) {
				win = true;
				break;
			}
		}

		return win;
	}

	/*
	 * No diagonal checks for this game
	 */
	@Override
	protected boolean winsDiagnal(Move move) {
		return false;
	}
}
