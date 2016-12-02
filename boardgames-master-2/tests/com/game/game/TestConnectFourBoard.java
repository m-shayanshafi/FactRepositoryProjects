package com.game.game;


import org.junit.Assert;
import org.junit.Test;

import com.game.board.ConnectFourBoard;
import com.game.entity.Move;

public class TestConnectFourBoard {

	@Test
	public void testIsInvalidMove() {
		ConnectFourBoard cfb = new ConnectFourBoard(6, 6);
		Move m = new Move(1, 1);
		Assert.assertEquals(true, cfb.isInvalidMove(m));
	}

	@Test
	public void testIsValidMove() {
		ConnectFourBoard cfb = new ConnectFourBoard(6, 6);
		Move m = new Move(5, 5);
		Assert.assertEquals(false, cfb.isInvalidMove(m));
	}	
}
