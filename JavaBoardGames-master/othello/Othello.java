package othello;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;

import board.Board;
import board.Cell;
import board.Direction;
import board.Direction.direction;
import board.Indexer;
import board.Piece;

public class Othello extends Board{
	private static final long serialVersionUID = 1L;
	JLabel note1,note2;

	public Othello() {
		super(Color.BLACK, Color.WHITE, 8, Color.lightGray, Color.lightGray);
		setTitle("Othello");

		JPanel panelBottom = new JPanel();
		panelBottom.setBackground(Color.gray);
		
		note1 = new JLabel();
		note1.setForeground(player1);
		panelBottom.add(note1);
		
		note2 = new JLabel();
		note2.setForeground(player2);
		panelBottom.add(note2);

		add(panelBottom,BorderLayout.SOUTH);		
		updateScore();
	}

	public void initialSetup(){
		getCell(3, 3).setPiece(new Piece(player2));
		getCell(4, 4).setPiece(new Piece(player2));
		getCell(3, 4).setPiece(new Piece(player1));
		getCell(4, 3).setPiece(new Piece(player1));
	}
	
	@Override
	public ActionListener getCellActionListener(Board b, int i, int j) {
		return new OthelloCellAction((Othello) b, getCell(i, j));
	}

	public void populate(Cell cell) {
		Cell c;
		boolean shift = false;
		ArrayList<Cell> cells = new ArrayList<Cell>();
		for(direction d : Direction.getAll()){
			Indexer ind = new Indexer(cell, d);
			c = nextCell(ind);
			while(c != null){
				if(!c.hasPiece())
					break;
				if(c.getPieceColor() == getPlayer()){
					if(cells.size() > 0)
						shift = true;
					for(Cell c2 : cells){
						c2.setPiece(new Piece(currentPlayer));
					}
					break;
				}
				else{
					cells.add(c);
				}
				c = nextCell(ind);
			}
			cells.clear();
		}
		if(shift){
			cell.setPiece(new Piece(currentPlayer));
			changeTurn();
		}
		updateScore();
	}
	
	public void updateScore(){
		Cell c;
		int p1 = 0, p2 = 0;
		for(int i=0; i<maxSize; i++){
			for(int j=0; j<maxSize; j++){
				c = getCell(i, j);
				if(c.hasPiece()){
					if(c.getPieceColor() == player1)
						p1++;
					else
						p2++;
				}
			}
		}
		
		note1.setText("PlayerOne: "+p1);
		note2.setText("PlayerTwo: "+p2);
	}
}
