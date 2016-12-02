package com.game.run;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.game.board.BaseBoard;
import com.game.entity.Move;
import com.game.game.TicTacToeGame;

public class RunTicTacToe {
	public void run() {
		int rows = 3;
		int cols = 3;
		BaseBoard board = new BaseBoard(rows, cols);
		TicTacToeGame t = new TicTacToeGame(board);

		List<Move> moves = new ArrayList<Move>();

		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				moves.add(new Move(row, col));
			}
		}

		Collections.shuffle(moves);

		for (Move move : moves) {
			t.play(move);
			t.switchPlayer();
			if (t.isGameOver()) {
				return;
			}
		}
	}
}
