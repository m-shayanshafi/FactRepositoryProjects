package pieces;

public class Piece implements InterfacePiece{
	public static int MOSCOVITE = 1;
	public static int ROI = 2;
	public static int SUEDOIS = 3;
	public static int VIDE = 4;
	
	private int ligne, colonne;
	
	public Piece(){

	}

	public Piece(int i, int j){
		setLigne(i);
		setColonne(j);
	}
	
	public Piece (Piece p){
		setLigne(p.getLigne());
		setColonne(p.getColonne());
	}
	
	public int getLigne(){
		return ligne;
	}
	
	public void setLigne(int i){
		ligne = i;
	}
	
	public int getColonne(){
		return colonne;
	}
	
	public void setColonne(int i){
		colonne = i;
	}
	
	public int type(){
    	return -1;
    }
}
