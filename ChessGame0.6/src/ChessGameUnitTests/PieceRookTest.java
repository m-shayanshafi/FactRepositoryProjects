package ChessGameUnitTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import ChessGameKenai.Chess_Data;
import ChessGameKenai.NonVisualPiece;
import ChessGameKenai.model.PieceRook;

public class PieceRookTest {
	@Test
	public void testIsMoveable() {
		Chess_Data data = Chess_Data.getInstance();		
		PieceRook pRook = new PieceRook();
		assertFalse(pRook.isMoveable(data.getActivePieces(), 1, 1));
	}

	@Test
	public void testMakeMove() {
		Chess_Data data = Chess_Data.getInstance();		
		PieceRook pRook = new PieceRook();
		NonVisualPiece nonVisualPiece = NonVisualPiece.create(data, "BRook", 1);
		pRook.makeMove(nonVisualPiece, data, 1, 1);
		assertEquals(1, nonVisualPiece.getPosition());
	}

}
