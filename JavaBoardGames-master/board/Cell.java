package board;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.JButton;

public class Cell extends JButton{
	private static final long serialVersionUID = 1L;
	private Piece piece = null;
	public int row,col;
	private Color defaultC;

	public Cell(int i, int j) {
		row = i;
		col = j;
	}

	public Cell(int i, int j,Color c) {
		this(i,j);
		defaultC = c;
		toDefaultColor();
	}
	
	public void toDefaultColor(){
		setBackground(defaultC);
	}
	
	public void setPiece(Piece p){
		piece = p;
		piece.setMyCell(this);
		fill();
	}
	
	public Piece getPiece(){
		return piece;
	}
	
	public boolean hasMoves(){
		MovingPiece p = (MovingPiece) piece;
		if(p != null && p.getMoves().size() != 0)
			return true;
		else
			return false;
	}

	public void fill(){
		setForeground(piece.getColor());
		setFont(new Font("Arial", Font.PLAIN, 28));
		setText(piece.getSymbol());
	}
	
	public void removePiece(){
		piece = null;
		setText("");
		toDefaultColor();
	}
	
	public boolean hasPiece(){
		return piece == null ? false : true;
	}
	
	public Color getPieceColor(){
		if(hasPiece())
			return piece.getColor();
		else
			return null;
	}
	
	public ArrayList<Cell> getMoveSet(){
		MovingPiece mp = (MovingPiece)piece;
		return mp.getMoves();
	}
}
