package ChessGameUnitTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import ChessGameKenai.Chess_Data;
import ChessGameKenai.NonVisualPiece;
import ChessGameKenai.model.PieceQueen;

public class PieceQueenTest {
@Test
	public void testIsMoveable() {
		Chess_Data data = Chess_Data.getInstance();		
		PieceQueen pQueen = new PieceQueen();
		assertFalse(pQueen.isMoveable(data.getActivePieces(), 1, 1));
	}

	@Test
	public void testMakeMove() {
		Chess_Data data = Chess_Data.getInstance();		
		PieceQueen pQueen = new PieceQueen();
		NonVisualPiece nonVisualPiece = NonVisualPiece.create(data, "BQueen", 1);
		pQueen.makeMove(nonVisualPiece, data, 1, 1);
		assertEquals(1, nonVisualPiece.getPosition());
	}
}
