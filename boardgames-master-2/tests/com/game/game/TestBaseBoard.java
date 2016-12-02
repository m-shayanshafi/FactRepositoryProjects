package com.game.game;

import org.junit.Assert;
import org.junit.Test;

import com.game.board.BaseBoard;
import com.game.entity.Move;

public class TestBaseBoard {

	@Test
	public void testRowSize() {
		BaseBoard board = new BaseBoard(8, 8);
		Assert.assertEquals(8, board.getRowSize());
	}
	
	@Test
	public void testColumnSize() {
		BaseBoard board = new BaseBoard(8, 2);
		Assert.assertEquals(2, board.getColumnSize());
	}
	
	@Test
	public void testSetAndGetMove(){
		Move m = new Move(1, 2);
		BaseBoard board = new BaseBoard(6, 6);
		board.setMove(m, 'X');
		Assert.assertEquals("X", board.getMove(1, 2).toString());
	}
	
	@Test
	public void testisInvalidMove(){
		Move m = new Move(1, 9);
		BaseBoard board = new BaseBoard(6, 6);
		Assert.assertEquals(true, board.isInvalidMove(m));
	}
	
	@Test
	public void testisValidMove(){
		Move m = new Move(1, 2);
		BaseBoard board = new BaseBoard(6, 6);
		Assert.assertEquals(false, board.isInvalidMove(m));
	}
}
