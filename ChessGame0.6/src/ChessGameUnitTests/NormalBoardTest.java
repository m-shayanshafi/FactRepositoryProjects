package ChessGameUnitTests;
import static org.junit.Assert.*;

import java.util.ArrayList;
import org.junit.Test;

import ChessGameKenai.NormalBoard;
import ChessGameKenai.ChessBoardView;
import ChessGameKenai.Chess_Data;
import ChessGameKenai.Board;
import ChessGameKenai.VisualPiece;
import ChessGameKenai.Square;


public class NormalBoardTest {

	@Test
	public void testPopulateBoard() {

        Chess_Data data = Chess_Data.getInstance();		
		ChessBoardView cbv = ChessBoardView.getInstance(data);
        
        Board chessBoard = new NormalBoard(data, cbv);
        ArrayList<VisualPiece> visualPieces = chessBoard.getPieces();
        chessBoard.populateBoard();
        assertEquals("Board is not populated", 64, visualPieces.size(), 0.01);		
	}

	@Test
	public void testRemoveAllPieces() {
		Chess_Data data = Chess_Data.getInstance();		
		ChessBoardView cbv = ChessBoardView.getInstance(data);
        
        Board chessBoard = new NormalBoard(data, cbv);
        
        ArrayList<Square> squares = chessBoard.getSquares();
        chessBoard.populateBoard();        
        
        chessBoard.removeAllPieces();
        int populatedSquares = 0;
        for (int i = 0; i < squares.size(); i++)
        {
        	populatedSquares = populatedSquares + squares.get(i).getComponentCount();
        }
        
        assertEquals("Board is not removed pieces", 32, populatedSquares, 0.01);	
	}

}









