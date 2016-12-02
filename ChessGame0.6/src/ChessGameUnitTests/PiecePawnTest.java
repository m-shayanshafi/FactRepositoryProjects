package ChessGameUnitTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import ChessGameKenai.Chess_Data;
import ChessGameKenai.NonVisualPiece;
import ChessGameKenai.model.PiecePawn;

public class PiecePawnTest {
@Test
	public void testIsMoveable() {
		Chess_Data data = Chess_Data.getInstance();		
		PiecePawn pPawn = new PiecePawn();
		assertFalse(pPawn.isMoveable(data.getActivePieces(), 1, 1));
	}

	@Test
	public void testMakeMove() {
		Chess_Data data = Chess_Data.getInstance();		
		PiecePawn pPawn = new PiecePawn();
		NonVisualPiece nonVisualPiece = NonVisualPiece.create(data, "BPawn", 1);
		pPawn.makeMove(nonVisualPiece, data, 1, 1);
		assertEquals(1, nonVisualPiece.getPosition());
	}
}
