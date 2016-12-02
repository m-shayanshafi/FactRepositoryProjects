package ChessGameUnitTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import ChessGameKenai.Chess_Data;
import ChessGameKenai.NonVisualPiece;
import ChessGameKenai.model.PieceKnight;

public class PieceKnightTest {

@Test
	public void testIsMoveable() {
	Chess_Data data = Chess_Data.getInstance();		
		PieceKnight pKnight = new PieceKnight();
		assertFalse(pKnight.isMoveable(data.getActivePieces(), 1, 1));
	}

	@Test
	public void testMakeMove() {
		Chess_Data data = Chess_Data.getInstance();		
		PieceKnight pKnight = new PieceKnight();
		NonVisualPiece nonVisualPiece = NonVisualPiece.create(data, "BKnight", 1);
		pKnight.makeMove(nonVisualPiece, data, 1, 1);
		assertEquals(1, nonVisualPiece.getPosition());
	}
}
