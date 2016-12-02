package pieces;

public class PieceVide extends Piece {
	public PieceVide(){
		
	}
	
	public PieceVide(int i, int j){
		setLigne(i);
		setColonne(j);
	}
	
	public PieceVide (Piece p){
		setLigne(p.getLigne());
		setColonne(p.getColonne());
	}
	
	public int type(){
    	return VIDE;
    }
}
