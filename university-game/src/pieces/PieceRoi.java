package pieces;

public class PieceRoi extends Piece {
	public PieceRoi(){
		
	}
	
	public PieceRoi(int i, int j){
		setLigne(i);
		setColonne(j);
	}
	
	public PieceRoi (Piece p){
		setLigne(p.getLigne());
		setColonne(p.getColonne());
	}
	
	public int type(){
    	return ROI;
    }
}
