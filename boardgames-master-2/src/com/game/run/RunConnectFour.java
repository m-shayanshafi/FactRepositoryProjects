package com.game.run;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.game.board.ConnectFourBoard;
import com.game.entity.Move;
import com.game.game.ConnectFourGame;

public class RunConnectFour {
	public void run() {
		int rows = 6;
		int cols = 6;
		ConnectFourBoard board = new ConnectFourBoard(rows, cols);
		ConnectFourGame t = new ConnectFourGame(board);
		
		List<Move> moves = new ArrayList<Move>();

		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				moves.add(new Move(row, col));
			}
		}
		
		moves.addAll(moves);
		moves.addAll(moves);
		moves.addAll(moves);
		moves.addAll(moves);
		moves.addAll(moves);
		
		//Add more data since there will be more invalid moves due to strict game rules		
		Collections.shuffle(moves);
		for (Move move : moves) {
			try{
				t.play(move);
				t.switchPlayer();
			}
			catch(Exception e){
				//Ignore invalid moves for now
			}
			if (t.isGameOver()) {
				break;
			}
		}
	}
}
