package ChessGameUnitTests;

import static org.junit.Assert.*;

import java.awt.Color;

import org.junit.Test;

import ChessGameKenai.Board;
import ChessGameKenai.CapturedPieces;
import ChessGameKenai.ChessBoardView;
import ChessGameKenai.Chess_Data;
import ChessGameKenai.NormalBoard;


public class CapturedPiecesTest {

	@Test
	public void test() {

        Chess_Data data = Chess_Data.getInstance();		
		ChessBoardView cbv = ChessBoardView.getInstance(data);
		
        Board chessBoard = new NormalBoard(data, cbv);
        
        CapturedPieces capturedPieces = new CapturedPieces(Color.black, chessBoard);
        assertEquals("capturedPieces color is not set", Color.black, capturedPieces.getColor());
	}

}
