package ChessGameUnitTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ChessGameKenai.Board;
import ChessGameKenai.ChessBoardView;
import ChessGameKenai.Chess_Data;
import ChessGameKenai.NormalBoard;

public class BoardTest {

	@Test
	public void testPopulateBoard() {
		Chess_Data data = Chess_Data.getInstance();		
		ChessBoardView view = ChessBoardView.getInstance(data);
		
		Board board = new NormalBoard(data, view);
		board.populateBoard();
		
		assertEquals(64, board.getPieces().size());
		assertEquals(64, board.getmapPositions().size());
		assertTrue(board.getmapPositions().get(1).equals("8a"));
		
	}

	@Test
	public void testGetPieces() {
	}

	@Test
	public void testMapAllPositions() {
	}

}
