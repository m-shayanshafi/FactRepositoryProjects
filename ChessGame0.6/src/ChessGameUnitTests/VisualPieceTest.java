package ChessGameUnitTests;
import static org.junit.Assert.assertEquals;

import java.awt.Color;

import org.junit.Test;

import ChessGameKenai.Board;
import ChessGameKenai.ChessBoardView;
import ChessGameKenai.Chess_Data;
import ChessGameKenai.NonVisualPiece;
import ChessGameKenai.NormalBoard;
import ChessGameKenai.VisualPiece;
import ChessGameKenai.model.CPiece;


public class VisualPieceTest {

	@Test
	public void test() {
		Chess_Data data = Chess_Data.getInstance();		
		ChessBoardView cbv = ChessBoardView.getInstance(data);
        
        Board chessBoard = new NormalBoard(data, cbv);
        NonVisualPiece nonVisualPiece = NonVisualPiece.create(data, CPiece.BKing, 11);
		VisualPiece visualPiece = new VisualPiece(chessBoard, nonVisualPiece, chessBoard.getImageMap().get(CPiece.WKing));
		
        assertEquals("piece color is not set", Color.WHITE, visualPiece.getColor());
        assertEquals("piece position is not set", 11, visualPiece.getPosition());

	}
	
	@Test
	public void testSetColor() {
		Chess_Data data = Chess_Data.getInstance();		
		ChessBoardView cbv = ChessBoardView.getInstance(data);
        
        Board chessBoard = new NormalBoard(data, cbv);
        NonVisualPiece nonVisualPiece = NonVisualPiece.create(data, CPiece.BKing, 11);
		VisualPiece visualPiece = new VisualPiece(chessBoard, nonVisualPiece, chessBoard.getImageMap().get(CPiece.WKing));
				
        assertEquals("VisualPiece color is not set", Color.white, visualPiece.getColor());
	}
	
	@Test
	public void testSetPosition() {
		Chess_Data data = Chess_Data.getInstance();		
		ChessBoardView cbv = ChessBoardView.getInstance(data);
        
        Board chessBoard = new NormalBoard(data, cbv);
        NonVisualPiece nonVisualPiece = NonVisualPiece.create(data, CPiece.BKing, 11);
		VisualPiece visualPiece = new VisualPiece(chessBoard, nonVisualPiece, chessBoard.getImageMap().get(CPiece.WKing));
		
        assertEquals("VisualPiece prev position is not set", 11, visualPiece.getPosition());
	}
	
	
	@Test
	public void testSetClick() {
		Chess_Data data = Chess_Data.getInstance();		
		ChessBoardView cbv = ChessBoardView.getInstance(data);
        
        Board chessBoard = new NormalBoard(data, cbv);
        NonVisualPiece nonVisualPiece = NonVisualPiece.create(data, CPiece.BKing, 11);
		VisualPiece visualPiece = new VisualPiece(chessBoard, nonVisualPiece, chessBoard.getImageMap().get(CPiece.WKing));
		visualPiece.setClickCount(100);
		assertEquals("VisualPiece clickcount is not set", 100, visualPiece.getClickCount());
	}

}
