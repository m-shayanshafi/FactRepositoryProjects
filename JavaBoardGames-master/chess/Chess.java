package chess;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionListener;

import chess.pieces.Bishop;
import chess.pieces.King;
import chess.pieces.Knight;
import chess.pieces.Pawn;
import chess.pieces.Queen;
import chess.pieces.Rook;
import board.Board;
import board.Cell;
import board.SelectBoard;

public class Chess extends SelectBoard{
	private static final long serialVersionUID = 1L;
	private Cell king1, king2;

	public Chess() {
		super(Color.WHITE, Color.BLACK, 8, Color.gray, Color.lightGray);
		setTitle("Chess");
	}

	public void initialSetup() {
		putPieces(player1,0);
		putPieces(player2,7);
		king1 = getCell(0, 3);
		king2 = getCell(7, 3);
	}
	
	public Cell getMyKing(){
		return getPlayer() == player1 ? king1 : king2;
	}
	
	public void putPieces(Color p, int row){
		getCell(row, 0).setPiece(new Rook(p));
		getCell(row, 1).setPiece(new Knight(p));
		getCell(row, 2).setPiece(new Bishop(p));
		getCell(row, 3).setPiece(new King(p));
		getCell(row, 4).setPiece(new Queen(p));
		getCell(row, 5).setPiece(new Bishop(p));
		getCell(row, 6).setPiece(new Knight(p));
		getCell(row, 7).setPiece(new Rook(p));
		
		row = row == 0 ? row+1 : row-1;
		for(int i = 0; i < 8; i++)
			getCell(row, i).setPiece(new Pawn(p));
			
	}

	public void generateSuggestions(){
		super.generateSuggestions();

		Cell c;
		Cell king = getMyKing();
		for(int i = 0; i < maxSize; i++){
			for(int j = 0; j < maxSize; j++){
				c = getCell(i, j);
				if(!currPlayerPiece(c) && c.hasMoves()){
					c.setBackground(Color.yellow);
					for(Cell c2 : c.getMoveSet()){
						if(c2 == king){
							c.setBackground(killColor);
						}
					}
				}
			}
		}
	}
	
	public boolean currPlayerPiece(Cell c){
		return c.getPieceColor() == getPlayer() ? true : false;
	}
		
	@Override
	public ActionListener getCellActionListener(Board b, int i, int j) {
		return new ChessCellAction((Chess) b,getCell(i, j));
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable(){
			public void run(){
				new Chess();
			}
		});
	}
}
