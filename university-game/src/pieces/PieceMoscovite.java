package pieces;

public class PieceMoscovite extends Piece {
	public PieceMoscovite(){

	}

	public PieceMoscovite(int i, int j){
		setLigne(i);
		setColonne(j);
	}
	
	public PieceMoscovite (Piece p){
		setLigne(p.getLigne());
		setColonne(p.getColonne());
	}

	public int type(){
		return MOSCOVITE;
	}
}
