package checkers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import board.Cell;
import board.SelectBoard;

public class CheckersCellAction implements ActionListener{
	Checkers board;
	Cell cell;

	public CheckersCellAction(Checkers b, Cell c){
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
