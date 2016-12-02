package pieces;

public class PieceSuedois extends Piece {
	public PieceSuedois(){
		
	}
	
	public PieceSuedois(int i, int j){
		setLigne(i);
		setColonne(j);
	}
	
	public PieceSuedois (Piece p){
		setLigne(p.getLigne());
		setColonne(p.getColonne());
	}
	
	public int type(){
    	return SUEDOIS;
    }
}
