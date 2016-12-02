package board;

import java.awt.Color;
import java.util.ArrayList;

public abstract class SelectBoard extends Board{
	private static final long serialVersionUID = 1L;
	private Cell selected = null;
	private ArrayList<Cell> selectedMoves = new ArrayList<Cell>();
	public static Color selectColor = Color.blue;
	public static Color moveColor = Color.green;
	public static Color killColor = Color.red;
	public static Color validColor = Color.cyan;

	public SelectBoard(Color p1, Color p2, int size, Color m, Color n) {
		super(p1, p2, size, m, n);
		generateSuggestions();
	}
	
	public MovingPiece getSelectedPiece(){
		return (MovingPiece)selected.getPiece();
	}

	public void select(Cell c){
		if(c.getPieceColor() != getPlayer())
			return;
		
		selected = c;
		selectedMoves = c.getMoveSet(); 
		c.setBackground(selectColor);
		for(Cell cc : selectedMoves){
			cc.setBackground(moveColor);
		}
	}
	
	public void unselect(){
		if(selected != null)
			revertColor(selected);
		for(Cell cc : selectedMoves){
			revertColor(cc);
		}		
	}
	
	public void revertColor(Cell c){
		if(c.hasMoves())
			c.setBackground(validColor);
		else
			c.toDefaultColor();
	}
	
	public void movePieceTo(Cell destination) {
		MovingPiece p = getSelectedPiece();
		destination.setPiece(p);
		p.moved();
		selected.removePiece();
		unselect();
		changeTurn();
		generateSuggestions();
	}

	public void generateSuggestions(){
		for(int i = 0; i < maxSize; i++){
			for(int j = 0; j < maxSize; j++){
				Cell c = getCell(i, j);
				if(c.hasPiece()){
					MovingPiece p = (MovingPiece) c.getPiece();
					p.emptyMoveSet();
					p.generateMoves(this);
					
					if(p.getMoves().size() > 0)
						c.setBackground(validColor);	
					else
						c.toDefaultColor();
				}				
			}
		}
	}	
}
