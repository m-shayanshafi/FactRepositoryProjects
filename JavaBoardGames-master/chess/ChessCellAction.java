package chess;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import board.Cell;
import board.SelectBoard;

public class ChessCellAction implements ActionListener{
	Chess board;
	Cell cell;

	public ChessCellAction(Chess b, Cell c){
		board = b;
		cell = c;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(cell.getBackground() == SelectBoard.moveColor){
			board.movePieceTo(cell);
		}
		else if(cell.getBackground() != SelectBoard.selectColor){
			board.unselect();
			if(cell.hasPiece()){
				board.select(cell);
			}
		}
	}
}
