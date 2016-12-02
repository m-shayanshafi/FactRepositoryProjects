package ChessGameUnitTests;

import static org.junit.Assert.assertFalse;

import org.junit.Test;

import ChessGameKenai.Chess_Data;
import ChessGameKenai.NonVisualPiece;
import ChessGameKenai.model.PieceKing;

public class PieceKingTest {

	@Test
	public void testIsMoveable() {
		Chess_Data data = Chess_Data.getInstance();		
		PieceKing pKing = new PieceKing();
		assertFalse(pKing.isMoveable(data.getActivePieces(), 1, 1));
	}

	@Test
	public void testMakeMove() {
		Chess_Data data = Chess_Data.getInstance();
		PieceKing pKing = new PieceKing();
		NonVisualPiece nonVisualPiece = NonVisualPiece.create(data, "BKing", 1);
		pKing.makeMove(nonVisualPiece, data, 1, 1);
	}
}
