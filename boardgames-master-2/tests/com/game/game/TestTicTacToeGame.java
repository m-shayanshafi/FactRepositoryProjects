package com.game.game;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.game.board.BaseBoard;
import com.game.entity.Move;
import com.game.entity.Player;
import com.game.game.TicTacToeGame;

public class TestTicTacToeGame {

	@Test(expected=IllegalArgumentException.class)
	public void invalidMove1() {
		BaseBoard board = new BaseBoard(3, 3);
		TicTacToeGame t = new TicTacToeGame(board);

		Move m = new Move(0, 7);
		t.play(m);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void invalidMove2() {
		BaseBoard board = new BaseBoard(3, 3);
		TicTacToeGame t = new TicTacToeGame(board);

		Move m = new Move(9, -1);
		t.play(m);
	}
	
	@Test
	public void testWinVertical(){
		BaseBoard board = new BaseBoard(3, 3);
		TicTacToeGame t = new TicTacToeGame(board);

		List<Move> moves = new ArrayList<Move>();
		
		moves.add(new Move(0, 0));
		moves.add(new Move(2, 1));
		moves.add(new Move(0, 1));
		moves.add(new Move(1, 1));
		moves.add(new Move(0, 2));

		for (Move move : moves) {
			t.play(move);
			t.switchPlayer();
			if (t.isGameOver()) {
				Assert.assertEquals(Player.A, t.getWinner());
			}
		}
	}
	
	@Test
	public void testWinHorizontal(){
		BaseBoard board = new BaseBoard(3, 3);
		TicTacToeGame t = new TicTacToeGame(board);

		List<Move> moves = new ArrayList<Move>();
		
		moves.add(new Move(0, 0));
		moves.add(new Move(2, 1));
		moves.add(new Move(1, 0));
		moves.add(new Move(1, 1));
		moves.add(new Move(2, 0));

		for (Move move : moves) {
			t.play(move);
			t.switchPlayer();
			if (t.isGameOver()) {
				Assert.assertEquals(Player.A, t.getWinner());
			}
		}
	}
}
