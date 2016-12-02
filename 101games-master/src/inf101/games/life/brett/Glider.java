package inf101.games.life.brett;

/**
 * Et lite romskip.
 * 
 * Kilde: Wikipedia
 * 
 * @author Anya Helene Bagge
 *
 */
public class Glider extends AbstractPattern {
	public Glider() {
		super(new String[] {
				"     ",
				"  X  ",
				"   X ",
				" XXX ",
				"     "
		}, "Glider");
	}
}
