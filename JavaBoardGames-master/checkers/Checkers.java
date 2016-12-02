package checkers;

import java.awt.Color;
import java.awt.event.ActionListener;

import board.Board;
import board.Cell;
import board.SelectBoard;

public class Checkers extends SelectBoard{
	private static final long serialVersionUID = 1L;

	public Checkers() {
		super(Color.WHITE, Color.BLACK, 8, Color.gray, Color.lightGray);
		setTitle("Checkers");
	}

	public void initialSetup() {
		for(int i : new int[]{0,1,6,7}){
			for(int j = 0; j < 8; j++){
				if(i < 2 && (i+j)%2 == 0)
					getCell(i, j).setPiece(new CheckersPiece(player1,true));
				else if(i > 5 && (i+j)%2 == 0)
					getCell(i, j).setPiece(new CheckersPiece(player2));
			}
		}
	}
	
	public void movePieceTo(Cell c){
		CheckersPiece cp = (CheckersPiece) getSelectedPiece();
		if(c.row == 0 || c.row == 7)
			cp.evolve();
		
		Cell kill = cp.getKill(c);
		if(kill != null)
			kill.removePiece();
		
		super.movePieceTo(c);
	}

	@Override
	public ActionListener getCellActionListener(Board b, int i, int j) {
		return new CheckersCellAction((Checkers) b, getCell(i, j));
	}
	
}