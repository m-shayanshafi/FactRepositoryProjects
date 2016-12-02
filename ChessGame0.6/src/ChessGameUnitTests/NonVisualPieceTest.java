package ChessGameUnitTests;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.awt.Color;

import org.junit.Test;

import ChessGameKenai.Chess_Data;
import ChessGameKenai.NonVisualPiece;
import ChessGameKenai.model.CPiece;


public class NonVisualPieceTest {

	@Test
	public void testIsCaptured() {
		Chess_Data data = Chess_Data.getInstance();		
		
		
		NonVisualPiece nonVisualPiece = NonVisualPiece.create(data, CPiece.BRook, 11);
		
		nonVisualPiece.isCaptured(true);
		assertTrue("isCaptured is not set", nonVisualPiece.isCaptured());
	}

	@Test
	public void testIsPieceTypeOnly() {
		Chess_Data data = Chess_Data.getInstance();		
		
		NonVisualPiece nonVisualPiece = NonVisualPiece.create(data, CPiece.BRook, 11);
				
        assertEquals("setpiecetype is not working", "Rook", nonVisualPiece.getPieceTypeOnly());
	}
	
	@Test
	public void testSetColor() {
		Chess_Data data = Chess_Data.getInstance();		
		
		NonVisualPiece nonVisualPiece = NonVisualPiece.create(data, CPiece.BRook, 11);
		nonVisualPiece.setColor(Color.white);
				
        assertEquals("nonVisualPiece color is not set", Color.white, nonVisualPiece.getColor());
	}
	
	@Test
	public void testSetPreviousPosition() {
		Chess_Data data = Chess_Data.getInstance();		
		
		NonVisualPiece nonVisualPiece = NonVisualPiece.create(data, CPiece.BRook, 11);
		nonVisualPiece.setPreviousPosition(15);
				
        assertEquals("nonVisualPiece prev position is not set", 15, nonVisualPiece.getPreviousPosition());
	}
	
	@Test
	public void testSetPosition() {
		Chess_Data data = Chess_Data.getInstance();		
		
		NonVisualPiece nonVisualPiece = NonVisualPiece.create(data, CPiece.BRook, 11);
				
        assertEquals("nonVisualPiece position is not set", 11, nonVisualPiece.getPosition());
	}
	
	@Test
	public void testSetClick() {
		Chess_Data data = Chess_Data.getInstance();		
		
		NonVisualPiece nonVisualPiece = NonVisualPiece.create(data, CPiece.BRook, 11);
		nonVisualPiece.setClickCount(40);		
        assertEquals("nonVisualPiece clickcount is not set", 40, nonVisualPiece.getClickCount());
	}

}
