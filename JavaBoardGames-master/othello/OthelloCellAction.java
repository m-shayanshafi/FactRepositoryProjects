package othello;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import board.Cell;

public class OthelloCellAction implements ActionListener{
	Othello board;
	Cell cell;

	public OthelloCellAction(Othello b, Cell c){
		board = b;
		cell = c;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(!cell.hasPiece()){
			board.populate(cell);
		}
	}

}
