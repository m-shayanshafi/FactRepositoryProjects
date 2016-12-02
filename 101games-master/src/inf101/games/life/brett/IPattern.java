package inf101.games.life.brett;

/**
 * Game of Life mønstre.
 * 
 * Hvert mønster har en relativt liten størrelse, og er beregnet på å plasseres
 * midt på et større brett.
 * 
 * @author Anya Helene Bagge
 *
 */
public interface IPattern {
	/**
	 * @param x X-koordinat, 0 <= x < getWidth()
	 * @param y Y-koordinat, 0 <= y < getHeight()
	 * @return True hvis feltet x,y skal være levende
	 */
	boolean isAlive(int x, int y);
	
	/**
	 * @return Bredden på mønsterdataene
	 */
	int getWidth();
	
	/**
	 * @return Høyden på mønsterdataene
	 */
	int getHeight();
	
	/**
	 * @return Navnet på mønsteret
	 */
	String getName();
}
