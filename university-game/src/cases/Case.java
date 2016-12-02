package cases;

public class Case implements InterfaceCase {

	public static int ANGLE = 1;
	public static int BORD = 2;
	public static int KONAKIS = 3;
	public static int VIDE = 4;

	public boolean selectionne;

	int largeur;
	int hauteur;

	public void setDimension(int x, int y) {
		largeur = x;
		hauteur = y;
	}

	public int type(){
		return -1;
	}
}

