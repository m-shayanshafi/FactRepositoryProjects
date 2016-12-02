package ChessGameUnitTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import ChessGameKenai.Chess_Data;
import ChessGameKenai.NonVisualPiece;
import ChessGameKenai.model.PieceBishop;

public class PieceBishopTest {

	@Test
	public void testIsMoveable() {
		Chess_Data data = Chess_Data.getInstance();		
		PieceBishop pBishop = new PieceBishop();
		assertFalse(pBishop.isMoveable(data.getActivePieces(), 1, 1));
	}

	@Test
	public void testMakeMove() {
		Chess_Data data = Chess_Data.getInstance();		
		PieceBishop pBishop = new PieceBishop();
		NonVisualPiece nonVisualPiece = NonVisualPiece.create(data, "BBishop", 1);
		pBishop.makeMove(nonVisualPiece, data, 1, 1);
		assertEquals(1, nonVisualPiece.getPosition());
	}

}
